package com.zejor.devops.nginx.xstream;

import com.thoughtworks.xstream.XStream;
import com.zejor.devops.springboot.ETLXstream;
import com.zejor.devops.springboot.pom.Build;
import com.zejor.devops.springboot.pom.Dependency;
import com.zejor.devops.springboot.pom.Plugin;
import com.zejor.devops.springboot.pom.Project;
import org.nutz.json.Json;

import java.util.List;

public class XStreamTestPom {

    public static void main(String[] args) {
        String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>com.yilin</groupId>\n" +
                "    <artifactId>qzbk-cloud</artifactId>\n" +
                "    <version>1.0.0</version>\n" +
                "    <packaging>pom</packaging>\n" +
                "    <parent>\n" +
                "        <groupId>org.springframework.boot</groupId>\n" +
                "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                "        <version>1.5.9.RELEASE</version>\n" +
                "    </parent>\n" +
                "    <properties>\n" +
                "        <java.version>1.8</java.version>\n" +
                "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "        <docker.image.prefix>qzbk</docker.image.prefix>\n" +
                "        <qzbk.version>1.0.0</qzbk.version>\n" +
                "        <!--  <spring.version>4.3.8.RELEASE</spring.version>  -->\n" +
                "        <nutz.version>1.r.65</nutz.version>\n" +
                "        <mybatis-plus-boot-starter.version>2.1.9</mybatis-plus-boot-starter.version>\n" +
                "        <mybatisplus.version>2.1.8</mybatisplus.version>\n" +
                "        <fastjson.version>1.2.39</fastjson.version>\n" +
                "\n" +
                "    </properties>\n" +
                "\n" +
                "\n" +
                "    <modules>\n" +
                "        <module>qzbk-commons</module>\n" +
                "        <module>qzbk-mapper</module>\n" +
                "        <module>qzbk-eureka-server</module>\n" +
                "        <module>qzbk-config-server</module>\n" +
                "        <module>qzbk-order-api</module>\n" +
                "        <module>qzbk-order-provider</module>\n" +
                "        <module>qzbk-user-api</module>\n" +
                "        <module>qzbk-user-provider</module>\n" +
                "        <module>qzbk-risk-api</module>\n" +
                "        <module>qzbk-risk-provider</module>\n" +
                "        <module>qzbk-pay-api</module>\n" +
                "        <module>qzbk-pay-provider</module>\n" +
                "        <module>qzbk-message-api</module>\n" +
                "        <module>qzbk-message-provider</module>\n" +
                "        <module>qzbk-quartz-schedule</module>\n" +
                "\n" +
                "    </modules>\n" +
                "\n" +
                "    <dependencies>\n" +
                "\n" +
                "        <!-- The client -->\n" +
                "        <dependency>\n" +
                "            <groupId>io.prometheus</groupId>\n" +
                "            <artifactId>simpleclient</artifactId>\n" +
                "            <version>0.0.24</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>io.prometheus</groupId>\n" +
                "            <artifactId>simpleclient_spring_boot</artifactId>\n" +
                "            <version>0.0.24</version>\n" +
                "        </dependency>\n" +
                "        <!-- Hotspot JVM metrics-->\n" +
                "        <dependency>\n" +
                "            <groupId>io.prometheus</groupId>\n" +
                "            <artifactId>simpleclient_hotspot</artifactId>\n" +
                "            <version>0.0.24</version>\n" +
                "        </dependency>\n" +
                "        <!-- Exposition servlet-->\n" +
                "        <dependency>\n" +
                "            <groupId>io.prometheus</groupId>\n" +
                "            <artifactId>simpleclient_servlet</artifactId>\n" +
                "            <version>0.0.24</version>\n" +
                "        </dependency>\n" +
                "        <!-- Pushgateway exposition-->\n" +
                "        <dependency>\n" +
                "            <groupId>io.prometheus</groupId>\n" +
                "            <artifactId>simpleclient_pushgateway</artifactId>\n" +
                "            <version>0.0.24</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.nutz</groupId>\n" +
                "            <artifactId>nutz</artifactId>\n" +
                "            <version>1.r.65</version>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "\n" +
                "    <dependencyManagement>\n" +
                "        <dependencies>\n" +
                "            <dependency>\n" +
                "                <groupId>org.springframework.cloud</groupId>\n" +
                "                <artifactId>spring-cloud-dependencies</artifactId>\n" +
                "                <version>Dalston.SR5</version>\n" +
                "                <type>pom</type>\n" +
                "                <scope>import</scope>\n" +
                "            </dependency>\n" +
                "\n" +
                "        </dependencies>\n" +
                "    </dependencyManagement>\n" +
                "        <build>\n" +
                "            <plugins>\n" +
                "\n" +
                "            <plugin>\n" +
                "                    <groupId>com.spotify</groupId>\n" +
                "                    <artifactId>docker-maven-plugin</artifactId>\n" +
                "                    <version>1.2.0</version>\n" +
                "                    <configuration>\n" +
                "                        <imageName>${docker.image.prefix}/${project.name}</imageName>\n" +
                "                        <dockerDirectory>src/main/docker</dockerDirectory>\n" +
                "                        <resources>\n" +
                "                            <resource>\n" +
                "                                <targetPath>/</targetPath>\n" +
                "                                <directory>${project.build.directory}</directory>\n" +
                "                                <include>${project.build.finalName}.jar</include>\n" +
                "                            </resource>\n" +
                "                        </resources>\n" +
                "                    </configuration>\n" +
                "                </plugin>\n" +
                "        </plugins>\n" +
                "        <defaultGoal>compile</defaultGoal>\n" +
                "    </build>\n" +
                "\n" +
                "</project>";
        XStream xStream = new ETLXstream();
        xStream.autodetectAnnotations(true);
        xStream.alias("dependency", Dependency.class);
        xStream.alias("project", Project.class);
        xStream.alias("build", Build.class);
//        xStream.alias("plugin", String.class);
        Project project = (Project) xStream.fromXML(xml);
        System.out.println(Json.toJson(project));
    }

}
