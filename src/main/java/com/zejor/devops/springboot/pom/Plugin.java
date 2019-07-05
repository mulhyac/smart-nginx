package com.zejor.devops.springboot.pom;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@XStreamAlias("plugin")
public class Plugin {

    private String groupId;

    private String artifactId;

}
