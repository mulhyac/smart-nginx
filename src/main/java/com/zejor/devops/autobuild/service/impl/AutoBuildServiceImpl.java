package com.zejor.devops.autobuild.service.impl;

import com.zejor.devops.autobuild.AutoBuild;
import com.zejor.devops.autobuild.domain.BuildInfo;
import com.zejor.devops.autobuild.service.AutoBuildService;
import com.zejor.devops.nginx.domain.Linux;
import com.zejor.devops.nginx.logger.domain.Log;
import com.zejor.devops.nginx.spring.plugins.SpringUtils;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AutoBuildServiceImpl implements AutoBuildService {

    @Autowired
    private Dao dao;

    @Override
    public void build(int buildId) {
        BuildInfo buildInfo = dao.fetch(BuildInfo.class, buildId);
        dao.fetchLinks(buildInfo, "buildType");
        String buildClassName = buildInfo.getBuildType().getBuildClass();
        String buildTaskId = System.currentTimeMillis() + "";
        String buildArgs = buildInfo.getBuildArgs();
        NutMap params = Json.fromJson(NutMap.class, buildArgs);
        try {
            if (!SpringUtils.containsBean(buildClassName)) {
                Class<?> clazz = Class.forName(buildClassName);
                SpringUtils.regeditDynamicBean(buildClassName, clazz, new HashMap<>());
            }
            AutoBuild autoBuild = SpringUtils.getBean(buildClassName);
            List<Linux> linuxs = getBuildLinux(buildId);
            for (Linux linux : linuxs) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        autoBuild.build(buildInfo, buildTaskId, params, linux);
                    }
                }).start();
                String log = params.getString("log");
                dao.insert(new Log(log + "/smart_shell_" + buildTaskId + ".log"));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Linux> getBuildLinux(int buildId) {
        Sql sql = Sqls.create("select l.* from linux l right join build_linux bl on l.linux_id=bl.linux_id where build_id=" + buildId);
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(dao.getEntity(Linux.class));
        dao.execute(sql);
        return sql.getList(Linux.class);
    }
}
