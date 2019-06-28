package com.zejor.devops.nginx.common;

import com.zejor.devops.nginx.domain.Linux;
import com.zejor.devops.nginx.domain.Nginx;
import com.github.odiszapc.nginxparser.NgxConfig;
import org.nutz.lang.Files;

public class DefaultConfWriter implements ConfWriter {

    @Override
    public boolean write(Nginx nginx, Linux linux, String conf) {
        Files.write(nginx.getNginxConf(),conf);
        return true;
    }

}
