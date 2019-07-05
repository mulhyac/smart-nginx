package com.zejor.devops.restclient.domain;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Getter
@Setter
@Table("api")
public class Api {

    @Id
    @Column("api_id")
    private int apiId;

    @Column("api_Name")
    private String apiName;

    @Column("api_uri")
    private String apiUri;

}
