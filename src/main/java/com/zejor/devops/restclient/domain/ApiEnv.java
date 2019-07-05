package com.zejor.devops.restclient.domain;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("api_env")
public class ApiEnv {

    @Id
    @Column("env_id")
    private int envId;

    @Column("env_name")
    private String envName;

    @Column("env_ctx")
    private String envCtx;

}
