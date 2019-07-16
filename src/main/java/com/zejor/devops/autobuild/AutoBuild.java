package com.zejor.devops.autobuild;

import com.zejor.devops.autobuild.domain.BuildInfo;
import com.zejor.devops.nginx.domain.Linux;
import org.nutz.lang.util.NutMap;

import java.util.Map;

public interface AutoBuild {

    /**
     * 自动编译
     * @param buildInfo
     */
    public void build(BuildInfo buildInfo, String buildTaskId, NutMap params, Linux linux);

}
