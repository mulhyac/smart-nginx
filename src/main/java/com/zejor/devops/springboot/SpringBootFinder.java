package com.zejor.devops.springboot;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.thoughtworks.xstream.XStream;
import com.zejor.devops.nginx.domain.Linux;
import com.zejor.devops.springboot.pom.Build;
import com.zejor.devops.springboot.pom.Dependency;
import com.zejor.devops.springboot.pom.Plugin;
import com.zejor.devops.springboot.pom.Project;
import com.zejor.devops.utils.FreeMarkerTemplateUtils;
import com.zejor.devops.utils.SSHUtils;
import com.zejor.devops.utils.SftpUtils;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.stream.StringInputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringBootFinder {

    private static final String SCRIPT_PATH = "/scritps/mvn/install.sh";
    private static final String DEFAULT_ENCODING = "UTF-8";


    public Project pomXml(String xml) {
        //创建xstream对象
        XStream xStream = new ETLXstream();
        xStream.allowTypes(new Class[]{Project.class, Plugin.class, Dependency.class, Build.class});

        //使用注解修改对象名称
        xStream.processAnnotations(Project.class);
        xStream.alias("module", String.class);
        //将字符串类型的xml转换为对象
        return (Project) xStream.fromXML(xml);
    }

    public List<BootModule> getBootModule(String xml, Project parentProject) {
        List<BootModule> bootModules = new ArrayList<>();
        Project project = pomXml(Files.read(xml));
        String parent = new File(xml).getParent();
        if (project.getVersion() == null) {
            //如果该模块无版本声明，则使用父级版本号
            project.setVersion(parentProject.getVersion());
        }
        if (project.getModules() != null && project.getModules().size() > 0) {
            for (String moduleName : project.getModules()) {
                bootModules.addAll(getBootModule(parent + File.separator + moduleName + File.separator + "pom.xml", project));
            }
        }
        if (project.getBuild() == null) {
            return bootModules;
        }
        for (Plugin plugin : project.getBuild().getPlugins()) {
            if ("org.springframework.boot".equals(plugin.getGroupId()) && "spring-boot-maven-plugin".equals(plugin.getArtifactId())) {
                String name = project.getName();
                if (name == null) {
                    name = new File(xml).getParentFile().getName();
                }
                BootModule bootModule = new BootModule(parent + File.separator + "target/" + project.getArtifactId() + "-" + project.getVersion() + ".jar", xml, name);
                bootModule.setBootName(name);
                bootModules.add(bootModule);
            }
        }
        return bootModules;
    }

    public void build(String git, String workspace,String mavenHome, Linux linux) {
        int start = git.lastIndexOf("/") + 1;
        int end = git.lastIndexOf(".");
        String cloneName = git.substring(start, end);
        exec(git, workspace, cloneName,mavenHome,linux);

    }

    public void exec(String git, String workspace, String cloneName,String mavenHome, Linux linux) {
        System.out.println(Json.toJson(linux));
        Map<String, String> map = new HashMap<>();
        map.put("workspace", workspace);
        map.put("separator", File.separator);
        map.put("cloneName", cloneName);
        map.put("mavenHome", mavenHome);
        map.put("git", git);
        try {
            String script = IOUtils.toString(SpringBootFinder.class.getResourceAsStream(SCRIPT_PATH), DEFAULT_ENCODING);
            script = FreeMarkerTemplateUtils.processTemplateToString(script, map);
            System.out.println(script);
            String uuid = "smart_shell_" + System.currentTimeMillis()+".sh";
            SftpUtils sftpUtils = new SftpUtils();
            Session session = SftpUtils.getSession(linux);
            ChannelSftp sftp = sftpUtils.connect(session);
            sftpUtils.mkdirs(sftp,workspace);
            sftpUtils.upload(workspace, new StringInputStream(script), uuid, sftp);
            String chmod="chmod +x "+workspace+"/"+uuid;
            String cd = "cd " + workspace;
            String sh = "sh ./" + uuid;
            String[] shells={chmod, cd, sh, "rm -rf " + workspace + "/" + uuid};
            SSHUtils.shell(session,"/home/eziep/Desktop/"+uuid+".log",shells);
            sftpUtils.logout(sftp, session);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSchException e) {
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
                bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), DEFAULT_ENCODING));
                bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), DEFAULT_ENCODING));

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

    public static void main(String[] args) {
        SpringBootFinder springBootFinder = new SpringBootFinder();
//        String pom = "/home/eziep/Desktop/qzbk-cloud/pom.xml";
////        String pom = "/app/data/git/20190601/smart-nginx/pom.xml";
//        List<BootModule> bootModules = springBootFinder.getBootModule(pom,null);
//        if (bootModules == null || bootModules.size() == 0) {
//            return;
//        }
//        for (BootModule bootModule : bootModules) {
//            System.out.println(Json.toJson(bootModule));
//        }


        Linux linux=new Linux();
        linux.setLinuxHost("183.136.213.178");
        linux.setLinuxUser("root");
        linux.setLinuxPwd("www.mulhyac.com@lili520spz");
        springBootFinder.build("https://shipz:amz123456@gitlab.jie360.com.cn/qyqsf/qyqsf-cms.git", "/root/workspace","/root/workspace/apache-maven-3.6.1",linux);
    }

}
