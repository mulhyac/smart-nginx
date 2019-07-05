package com.zejor.devops.restclient.domain;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("api_request")
public class ApiRequest {

    @Id
    @Column("request_id")
    private int requestId;

    @Column("request_method")
    private int requestMethod;

    @Column("request_body_type")
    private String requestBodyType;

    @Column("reuqest_body")
    private String requestBody;

    @Column("request_content_type")
    private String requestContentType;


}
