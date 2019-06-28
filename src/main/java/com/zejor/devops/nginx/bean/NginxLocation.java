package com.zejor.devops.nginx.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class NginxLocation {

    private String name;

    private String path;

    private String root;

    private String index;

    private String proxyPass;

    private String tryFiles;

    private String deny;

    private String logNotFound;

    private String expires;

    private String accessLog;

    private List<NginxProxySetHeader> proxySetHeader;

}
