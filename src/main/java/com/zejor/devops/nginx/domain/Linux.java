package com.zejor.devops.nginx.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Getter
@Setter
@ToString
@Table
public class Linux {

    @Id
    @Column("linux_id")
    private int linuxId;

    @Column("linux_name")
    private String linuxName;

    @Column("linux_host")
    private String linuxHost;

    @Column("linux_port")
    private int linuxPort = 22;

    @Column("linux_user")
    private String linuxUser;

    @Column("linux_pwd")
    private String linuxPwd;

    @Column("linux_intro")
    private String linuxIntro;

    @Column("add_time")
    private String addTime;

}
