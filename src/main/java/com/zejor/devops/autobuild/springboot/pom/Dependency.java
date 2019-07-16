package com.zejor.devops.autobuild.springboot.pom;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("dependency")
public class Dependency {

    private String groupId;
    private String artifactId;
    private String version;

}
