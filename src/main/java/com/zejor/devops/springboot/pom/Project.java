package com.zejor.devops.springboot.pom;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@XStreamAlias("project")
public class Project {

    private String groupId;

    private String artifactId;

    private String version;

    private String name;

    private List<String> modules;

    private List<Dependency> dependencies;

    private Build build;



}
