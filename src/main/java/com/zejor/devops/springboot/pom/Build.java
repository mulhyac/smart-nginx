package com.zejor.devops.springboot.pom;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@XStreamAlias("build")
public class Build {

    private List<Plugin> plugins;

}
