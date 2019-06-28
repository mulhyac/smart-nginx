package com.zejor.devops.nginx.common;

import com.zejor.devops.nginx.domain.Linux;
import com.zejor.devops.nginx.domain.Nginx;
import com.github.odiszapc.nginxparser.NgxConfig;

public interface ConfWriter {

    public boolean write(Nginx nginx, Linux linux, String conf);

}
