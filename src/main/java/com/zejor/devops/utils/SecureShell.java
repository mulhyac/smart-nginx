package com.zejor.devops.utils;

import com.jcraft.jsch.ChannelSftp;
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

    /**
     * 关闭连接 server
     */
    public void logout(Session session) {
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }
//    /**
//     * 关闭连接 server
//     */
//    public void logout(ChannelSftp sftp, Session session) {
//        if (sftp != null) {
//            if (sftp.isConnected()) {
//                sftp.disconnect();
//                System.out.println("sftp is closed already");
//            }
//        }
//        if (session != null) {
//            if (session.isConnected()) {
//                session.disconnect();
//            }
//        }
//    }

}
