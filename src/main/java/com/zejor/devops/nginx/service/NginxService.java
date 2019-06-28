package com.zejor.devops.nginx.service;

import com.zejor.devops.nginx.bean.NginxServer;
import com.zejor.devops.nginx.bean.NginxLocation;
import com.zejor.devops.nginx.bean.NginxUpstream;
import com.zejor.devops.nginx.common.NginxConfEditor;
import com.zejor.devops.nginx.domain.Nginx;

import java.util.List;

public interface NginxService {

    /**
     * 获取指定实例的nginx配置编辑器，由于配置信息是保存到内存中，故每个nginx对应一个编辑器
     *
     * @param nginxId
     * @return
     */
    public NginxConfEditor getNginxConfEditor(int nginxId);

    /**
     * 获取指定实例下所有server节点
     *
     * @param nginxId
     * @return
     */
    public List<NginxServer> listNginxServer(int nginxId);

    /**
     * 向指定实例添加server监听
     *
     * @param nginxId
     * @param nginxServer
     * @return
     */
    public boolean addServerListener(int nginxId, NginxServer nginxServer);

    /**
     * 删除指定实例中对应server监听节点
     *
     * @param nginxId
     * @param nginxServer
     * @return
     */
    public boolean deleteServerListener(int nginxId, NginxServer nginxServer);

    /**
     * 获取指定实例，指定server监听下所有location节点
     *
     * @param nginxId
     * @param nginxServer
     * @return
     */
    public List<NginxLocation> listLocation(int nginxId, NginxServer nginxServer);

    /**
     * 向指定实例，指定server监听下添加location节点
     *
     * @param nginxId
     * @param nginxServer
     * @return
     */
    public boolean addLocation(int nginxId, NginxServer nginxServer, NginxLocation nginxLocation);

    /**
     * 删除指定实例，指定server监听下所有location节点
     *
     * @param nginxId
     * @param nginxServer
     * @return
     */
    public boolean deleteLocation(int nginxId, NginxServer nginxServer, NginxLocation location);

    /**
     * 获取所有nginx列表
     * 后期计划：
     * 1、多角色管理不同Nginx实例，后期考虑
     * 2、标签和组功能，查找功能
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public List<Nginx> listNginx(int pageNumber, int pageSize);

    /**
     * 获取指定标识Nginx实例
     *
     * @param nginxId
     * @return
     */
    public Nginx getNginx(int nginxId);

    /**
     * 更新Nginx实例信息
     *
     * @param nginx
     * @return
     */
    public void updateNginx(Nginx nginx);


    /**
     * 添加Nginx实例信息
     *
     * @param nginx
     */
    public void addNginx(Nginx nginx);

    /**
     * 删除指定Nginx实例信息
     *
     * @param nginxId
     */
    public void deleteNginx(int nginxId);

    /**
     * 获取指定nginx实例下所有upstream
     *
     * @param nginxId
     * @return
     */
    List<NginxUpstream> listUpstream(int nginxId);

    void addUpstream(int nginxId, NginxUpstream nginxUpstream);

    void deleteUpstream(int nginxId, String upstream);

    NginxUpstream getUpstream(int nginxId, String upstream);
}
