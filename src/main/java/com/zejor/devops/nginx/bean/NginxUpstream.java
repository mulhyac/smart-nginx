package com.zejor.devops.nginx.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class NginxUpstream {

    private String name;

    private String value;

    private List<NginxUpstreamItem> items;

}
