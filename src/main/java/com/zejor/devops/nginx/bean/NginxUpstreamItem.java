package com.zejor.devops.nginx.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NginxUpstreamItem {

    /**
     * 代理地址
     */
    private String address;

    /**
     * 权重
     */
    private int weight;
}
