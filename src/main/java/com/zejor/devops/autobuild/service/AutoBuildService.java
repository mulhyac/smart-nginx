package com.zejor.devops.autobuild.service;

public interface AutoBuildService {

    /**
     * 编译指定项目
     *
     * @param buildId
     */
    public void build(int buildId);

}
