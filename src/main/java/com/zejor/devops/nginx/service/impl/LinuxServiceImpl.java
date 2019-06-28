package com.zejor.devops.nginx.service.impl;

import com.zejor.devops.nginx.domain.Linux;
import com.zejor.devops.nginx.service.LinuxService;
import com.zejor.devops.nginx.utils.TimeUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinuxServiceImpl implements LinuxService {

    @Autowired
    private Dao dao;

    @Override
    public void addLinux(Linux linux) {
        linux.setAddTime(TimeUtils.getTime());
        dao.insert(linux);
    }

    @Override
    public void deleteLinux(int linuxId) {
        dao.clear(Linux.class, Cnd.where("linuxId", "=", linuxId));
    }

    @Override
    public void updateLinux(Linux linux) {
        dao.update(linux, Cnd.where("linuxId", "=", linux.getLinuxId()));
    }

    @Override
    public Linux getLinux(int linuxId) {
        return dao.fetch(Linux.class, linuxId);
    }

    @Override
    public List<Linux> listLinux(int pageNumber, int pageSize) {
        return dao.query(Linux.class, Cnd.NEW().limit(pageNumber, pageSize).orderBy().desc("add_time"));
    }

}
