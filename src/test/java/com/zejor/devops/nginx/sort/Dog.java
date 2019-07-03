package com.zejor.devops.nginx.sort;

import java.util.Date;

public class Dog {
    private String name;

    private Date time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Dog [name=" + name + ", time=" + time + "]";
    }
}