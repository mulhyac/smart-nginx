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

    private String clientMaxBodySize;

    private String clientBodyBufferSize;

    private String proxyConnectTimeout;

    private String proxyReadTimeout;

    private String proxySendTimeout;

    private String proxyBufferSize;

    private String proxyBuffers;

    private String proxyBusyBuffersSize;

    private String proxyTempFileWriteSize;

    private String tryFiles;

    private String deny;

    private String logNotFound;

    private String expires;

    private String accessLog;

    private String fastcgiPass;

    private String fastcgiIndex;

    private String fastcgiParam;

    private String include;

    private List<NginxProxySetHeader> proxySetHeader;

}
