package com.zejor.devops.nginx.service.impl;


import com.zejor.devops.nginx.bean.NginxServer;
import com.zejor.devops.nginx.bean.NginxLocation;
import com.zejor.devops.nginx.bean.NginxUpstream;
import com.zejor.devops.nginx.common.NginxConfEditor;
import com.zejor.devops.nginx.domain.Nginx;
import com.zejor.devops.nginx.service.NginxService;
import com.zejor.devops.nginx.spring.plugins.SpringUtils;
import com.zejor.devops.nginx.utils.TimeUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NginxServiceImpl implements NginxService {

    @Autowired
    private Dao dao;

    @Override
    public NginxConfEditor getNginxConfEditor(int nginxId) {
        NginxConfEditor nginxConfEditor = null;
        String editorBeanName = "nginx_editor_" + nginxId;
        if (!SpringUtils.containsBean(editorBeanName)) {
            Nginx nginx = dao.fetch(Nginx.class, nginxId);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("nginx", nginx);
            SpringUtils.regeditDynamicBean(editorBeanName, NginxConfEditor.class, params);
            nginxConfEditor = SpringUtils.getBean(editorBeanName);
            nginxConfEditor.setNginx(nginx);
            nginxConfEditor.init();
        } else {
            nginxConfEditor = SpringUtils.getBean(editorBeanName);
        }
        return nginxConfEditor;
    }

    @Override
    public List<NginxServer> listNginxServer(int nginxId) {
        NginxConfEditor nginxConfEditor = getNginxConfEditor(nginxId);
        return nginxConfEditor.listNginxServer();
    }

    @Override
    public boolean addServerListener(int nginxId, NginxServer nginxServer) {
        Nginx nginx = dao.fetch(Nginx.class, nginxId);
        NginxConfEditor nginxConfEditor = getNginxConfEditor(nginxId);
        nginxConfEditor.addServerListener(nginxServer);
        return true;
    }

    @Override
    public boolean deleteServerListener(int nginxId, NginxServer nginxServer) {
        NginxConfEditor nginxConfEditor = getNginxConfEditor(nginxId);
        boolean delete = nginxConfEditor.deleteServerListener(nginxId, nginxServer);
        return delete;
    }

    @Override
    public List<NginxLocation> listLocation(int nginxId, NginxServer nginxServer) {
        NginxConfEditor nginxConfEditor = getNginxConfEditor(nginxId);
        return nginxConfEditor.listRules(nginxServer);
    }

    @Override
    public boolean addLocation(int nginxId, NginxServer nginxServer, NginxLocation nginxLocation) {
        NginxConfEditor nginxConfEditor = getNginxConfEditor(nginxId);
        return nginxConfEditor.addLocation(nginxServer, nginxLocation);
    }

    @Override
    public boolean deleteLocation(int nginxId, NginxServer nginxServer, NginxLocation location) {
        NginxConfEditor nginxConfEditor = getNginxConfEditor(nginxId);
        return nginxConfEditor.deleteLocation(nginxServer, location);
    }

    @Override
    public List<NginxUpstream> listUpstream(int nginxId) {
        NginxConfEditor nginxConfEditor = getNginxConfEditor(nginxId);
        return nginxConfEditor.listUpstreams();
    }

    @Override
    public void addUpstream(int nginxId, NginxUpstream nginxUpstream) {
        NginxConfEditor nginxConfEditor = getNginxConfEditor(nginxId);
        nginxConfEditor.addUpstream(nginxUpstream);
    }

    @Override
    public void deleteUpstream(int nginxId, String upstream) {
        NginxConfEditor nginxConfEditor = getNginxConfEditor(nginxId);
        nginxConfEditor.deleteUpstream(upstream);
    }

    @Override
    public NginxUpstream getUpstream(int nginxId, String upstream) {
        return null;
    }

    @Override
    public List<Nginx> listNginx(int pageNumber, int pageSize) {
        return dao.query(Nginx.class, Cnd.NEW().limit(pageNumber, pageSize).orderBy().desc("add_time"));
    }

    @Override
    public Nginx getNginx(int nginxId) {
        return dao.fetch(Nginx.class, nginxId);
    }

    @Override
    public void updateNginx(Nginx nginx) {
        dao.update(nginx, Cnd.where("nginxId", "=", nginx.getNginxId()));
    }

    @Override
    public void addNginx(Nginx nginx) {
        nginx.setAddTime(TimeUtils.getTime());
        dao.insert(nginx);
    }

    @Override
    public void deleteNginx(int nginxId) {
        dao.clear(Nginx.class, Cnd.where("nginxId", "=", nginxId));
    }

}
