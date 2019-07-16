package com.zejor.devops.autobuild.springboot;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.thoughtworks.xstream.XStream;
import com.zejor.devops.autobuild.AutoBuild;
import com.zejor.devops.autobuild.domain.BuildInfo;
import com.zejor.devops.nginx.domain.Linux;
import com.zejor.devops.utils.FreeMarkerTemplateUtils;
import com.zejor.devops.utils.SSHUtils;
import com.zejor.devops.utils.SftpUtils;
import com.zejor.devops.utils.XStreamUtils;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.stream.StringInputStream;
import org.nutz.lang.util.NutMap;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zejor.devops.autobuild.springboot.pom.*;

@Service
public class SpringBootBuild implements AutoBuild {

    private static final String SCRIPT_PATH = "/scritps/mvn/install.sh";
    private String charset = "UTF-8";

    public List<BootModule> getBootModule(Session session, String xml, Project parentProject) throws IOException, JSchException {
        List<BootModule> bootModules = new ArrayList<>();
        String text = new SSHUtils().cat(session, xml);
        Project project = XStreamUtils.toBean(Project.class, text);
        String parent = new File(xml).getParent();
        if (project.getVersion() == null) {
            //如果该模块无版本声明，则使用父级版本号
            project.setVersion(parentProject.getVersion());
        }
        if (project.getModules() != null && project.getModules().size() > 0) {
            for (String moduleName : project.getModules()) {
                String tempParentPom = parent + File.separator + moduleName + File.separator + "pom.xml";
                bootModules.addAll(getBootModule(session, tempParentPom, project));
            }
        }
        if (project.getBuild() == null) {
            return bootModules;
        }
        //迭代查看是否包含spring boot插件
        for (Plugin plugin : project.getBuild().getPlugins()) {
            if ("org.springframework.boot".equals(plugin.getGroupId()) && "spring-boot-maven-plugin".equals(plugin.getArtifactId())) {
                String name = project.getName();
                if (name == null) {
                    name = new File(xml).getParentFile().getName();
                }
                String bootTarget = parent + File.separator + "target/" + project.getArtifactId() + "-" + project.getVersion() + ".jar";
                BootModule bootModule = new BootModule(bootTarget, xml, name, project.getVersion());
                bootModule.setBootName(name);
                bootModules.add(bootModule);
            }
        }
        return bootModules;
    }

    @Override
    public void build(BuildInfo buildInfo, String buildTaskId, NutMap params, Linux linux) {
        String git = params.getString("git");
        String workspace = params.getString("workspace");
        int start = git.lastIndexOf("/") + 1;
        int end = git.lastIndexOf(".");
        String cloneName = git.substring(start, end);
        exec(params, workspace, cloneName, buildInfo, linux, buildTaskId);
    }

    public void exec(NutMap params, String workspace, String cloneName, BuildInfo buildInfo, Linux linux, String buildTaskId) {
        System.out.println(Json.toJson(linux));
        Map<String, String> map = new HashMap<>();
        map.put("cloneName", cloneName);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            map.put(entry.getKey(), entry.getValue().toString());
        }
        try {
            String script = IOUtils.toString(SpringBootBuild.class.getResourceAsStream(SCRIPT_PATH), charset);
            script = FreeMarkerTemplateUtils.processTemplateToString(script, map);
            String log = params.getString("log");
            exec(script, workspace, buildTaskId, linux, log);
            List<BootModule> bootModules = getBootModule(getSession(linux), workspace + "/" + cloneName + "/pom.xml", null);
            StringBuilder sb = new StringBuilder("#!/bin/bash");
            for (BootModule bootModule : bootModules) {
                sb.append("\ncp " + bootModule.getBootTarget() + " ${runtime}");
                sb.append("\n" + buildInfo.getStopShell() + "\necho '服务已杀死！'");
                sb.append("\n" + buildInfo.getStartShell() + "\necho '服务已启动！'");
                map.put("moduleName", bootModule.getBootName());
                map.put("version", bootModule.getBootVersion());
                script = FreeMarkerTemplateUtils.processTemplateToString(sb.toString(), map);
                exec(script, workspace, buildTaskId, linux, log);
            }
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public Session getSession(Linux linux) {
        Session session = null;
        try {
            session = SftpUtils.getSession(linux);
            return session;
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void exec(String script, String workspace, String buildTaskId, Linux linux, String log) {
        System.out.println("**********************************执行脚本***************************************");
        System.out.println(script);
        SftpUtils sftpUtils = new SftpUtils();
        Session session = getSession(linux);
        try {
            ChannelSftp sftp = sftpUtils.connect(session);
            sftpUtils.mkdirs(sftp, workspace);
            sftpUtils.upload(workspace, new StringInputStream(script), buildTaskId, sftp);
            String chmod = "chmod +x " + workspace + "/" + buildTaskId;
            String cd = "cd " + workspace;
            String sh = "sh ./" + buildTaskId;
            String rm = "rm -rf " + workspace + "/" + buildTaskId;
            String exit = "exit";
            String[] shells = {chmod, cd, sh, rm, exit};
            log = log + "/smart_shell_" + buildTaskId + ".log";
            SSHUtils sshUtils = new SSHUtils();
            sshUtils.exec(session, log, shells, "channel");
            System.out.println("**********************************执行脚本执行结束，终止会话***************************************");
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exec(String cmd) {
        try {
            StringBuilder result = new StringBuilder();
            BufferedReader bufrIn = null;
            BufferedReader bufrError = null;
            try {
                Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", cmd});
                // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
                bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), charset));
                bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), charset));

                // 读取输出
                String line = null;
                while ((line = bufrIn.readLine()) != null) {
                    result.append(line).append('\n');
                }
                while ((line = bufrError.readLine()) != null) {
                    result.append(line).append('\n');
                }
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void build(BuildInfo buildInfo, String buildTaskId, NutMap params,Linux linux) {
////        Linux linux = new Linux();
////        linux.setLinuxHost("183.136.213.178");
////        linux.setLinuxUser("root");
////        linux.setLinuxPwd("www.mulhyac.com@lili520spz");
////        springBootFinder.autobuild("https://shipz:amz123456@gitlab.jie360.com.cn/qyqsf/qyqsf-cms.git", "/root/workspace","/root/workspace/apache-maven-3.6.1",linux);
//
//        build(buildInfo, params, linux, buildTaskId);
//    }
}
