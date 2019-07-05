package com.zejor.devops.nginx.xstream;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class AdminUser {
    private String name;
    private String pwd;
    private List<String> ip;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIp() {
        return ip;
    }

    public void setIp(List<String> ip) {
        this.ip = ip;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name)
                .append("pwd", pwd).append("ip", ip).toString();
    }
}