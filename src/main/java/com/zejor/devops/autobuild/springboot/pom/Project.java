package com.zejor.devops.autobuild.springboot.pom;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@XStreamAlias("project")
public class Project implements Serializable {

    private String groupId;

    private String artifactId;

    private String version;

    private String name;

    private List<String> modules;

    private List<Dependency> dependencies;

    private Build build;

}
