package com.zejor.devops.autobuild.domain;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("build_type")
public class BuildType {

    @Id
    @Column("build_type_id")
    private int buildTypeId;

    @Column("build_type_name")
    private String buildTypeName;

    @Column("build_type_args")
    private String buildTypeArgs;

    @Column("build_class")
    private String buildClass;

}
