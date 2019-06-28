package com.zejor.devops.nginx.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class NginxServer {

    /**
     * 监听端口
     */
    private int port;

    /**
     * 监听域名
     */
    private String name;


}
