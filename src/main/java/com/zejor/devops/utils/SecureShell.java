package com.zejor.devops.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.zejor.devops.nginx.domain.Linux;

public class SecureShell {

    public static Session getSession(Linux linux) throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(linux.getLinuxUser(), linux.getLinuxHost(), linux.getLinuxPort());
        session.setPassword(linux.getLinuxPwd());
        session.setConfig("StrictHostKeyChecking", "no");//第一次访问服务器不用输入yes
        session.setTimeout(60000);
        session.connect();
        return session;
    }

}
