package com.zejor.devops.springboot;


import com.thoughtworks.xstream.XStream;
import com.zejor.devops.springboot.pom.Build;
import com.zejor.devops.springboot.pom.Dependency;
import com.zejor.devops.springboot.pom.Plugin;
import com.zejor.devops.springboot.pom.Project;
import org.nutz.json.Json;
import org.nutz.lang.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpringBootFinder {


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

    public List<BootModule> getBootModule(String xml,Project parentProject){
        List<BootModule> bootModules=new ArrayList<>();
        Project project=pomXml(Files.read(xml));
        String parent=new File(xml).getParent();
        if(project.getVersion()==null){
            //如果该模块无版本声明，则使用父级版本号
            project.setVersion(parentProject.getVersion());
        }
        if(project.getModules()!=null&&project.getModules().size()>0){
           for(String moduleName:project.getModules()){
               bootModules.addAll( getBootModule(parent+File.separator+moduleName+File.separator+"pom.xml",project));
           }
        }
        if(project.getBuild()==null){
            return bootModules;
        }
        for(Plugin plugin:project.getBuild().getPlugins()){
            if("org.springframework.boot".equals(plugin.getGroupId())&&"spring-boot-maven-plugin".equals(plugin.getArtifactId())){
                String name=project.getName();
                if(name==null){
                    name=new File(xml).getParentFile().getName();
                }
                BootModule bootModule = new BootModule(parent + File.separator + "target/" + project.getArtifactId() + "-"+project.getVersion() + ".jar", xml, name);
                bootModule.setBootName(name);
                bootModules.add(bootModule);
            }
        }
        return bootModules;
    }

    public void clone(String git,String workspace){
        try {
            Runtime.getRuntime().exec("cd "+workspace);
            Process process=Runtime.getRuntime().exec("git clone "+git);
            //暂时不处理日志，后期需要将日志通过websocket显示到前端页面
            Runtime.getRuntime().exec("cd "+workspace);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        SpringBootFinder springBootFinder = new SpringBootFinder();
        String pom = "/home/eziep/Desktop/qzbk-cloud/pom.xml";
//        String pom = "/app/data/git/20190601/smart-nginx/pom.xml";
        List<BootModule> bootModules = springBootFinder.getBootModule(pom,null);
        if (bootModules == null || bootModules.size() == 0) {
            return;
        }
        for (BootModule bootModule : bootModules) {
            System.out.println(Json.toJson(bootModule));
        }
    }

}
