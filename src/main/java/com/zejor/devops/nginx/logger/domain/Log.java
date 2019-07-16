package com.zejor.devops.nginx.logger.domain;

import com.zejor.devops.nginx.domain.Linux;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.One;
import org.nutz.dao.entity.annotation.Table;

@Data
@NoArgsConstructor
@Table("log")
public class Log {

    @Id
    @Column("log_id")
    private int logId;

    @Column("linux_id")
    private int linuxId;

    @One(field = "linuxId", target = Linux.class)
    private Linux linux;

    @Column("log_type")
    private int logType;

    @Column("log_path")
    private String logPath;


    public Log(String logPath) {
        this.logType = 1;
        this.logPath = logPath;
    }

}
