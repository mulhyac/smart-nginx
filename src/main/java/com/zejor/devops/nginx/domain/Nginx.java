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
@Table("nginx")
public class Nginx {

    @Id
    @Column("nginx_id")
    private int nginxId;

    @Column("nginx_name")
    private String nginxName;

    @Column("nginx_home")
    private String nginxHome;

    @Column("nginx_conf")
    private String nginxConf;

    @Column("nginx_intro")
    private String nginxIntro;

    @Column("nginx_status")
    private int nginxStatus;

    @Column("host_Id")
    private int hostId;

    @Column("add_time")
    private String addTime;

}
