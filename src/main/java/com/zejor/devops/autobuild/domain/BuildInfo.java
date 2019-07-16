package com.zejor.devops.autobuild.domain;


import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("build_info")
public class BuildInfo {

    @Id
    @Column("build_id")
    private int buildId;

    @Column("build_name")
    private String buildName;

    @Column("build_type_id")
    private int buildTypeId;

    @One(field = "buildTypeId", target = BuildType.class)
    private BuildType buildType;

    @Column("build_args")
    private String buildArgs;

    @Column("start_shell")
    private String startShell;

    @Column("stop_shell")
    private String stopShell;

}
