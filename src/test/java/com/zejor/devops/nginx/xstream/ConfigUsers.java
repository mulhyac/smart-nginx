package com.zejor.devops.nginx.xstream;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class ConfigUsers {
    private String type;
    private List<AdminUser> users;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<AdminUser> getUsers() {
        return users;
    }

    public void setUsers(List<AdminUser> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("users", users)
                .toString();
    }
}

