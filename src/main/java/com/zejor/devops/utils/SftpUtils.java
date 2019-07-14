package com.zejor.devops.utils;


import com.jcraft.jsch.*;
import com.zejor.devops.nginx.domain.Linux;
import org.nutz.json.Json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

public class SftpUtils extends SecureShell{
//    /**
//     * @param host
//     * @param port
//     * @param username
//     * @param password
//     * @return
//     * @throws JSchException
//     */
//    public Session getSession(String host, int port, String username, String password) throws JSchException {
//        // 2.实例化JSch
//        JSch nJSch = new JSch();
//        // 3.获取session
//        Session nSShSession = null;
//        nSShSession = nJSch.getSession(username, host, port);
//        System.out.println("Session创建成功");
//        // 4.设置密码
//        nSShSession.setPassword(password);
//        // 5.实例化Properties
//        Properties nSSHConfig = new Properties();
//        // 6.设置配置信息
//        nSSHConfig.put("StrictHostKeyChecking", "no");
//        // 7.session中设置配置信息
//        nSShSession.setConfig(nSSHConfig);
//        // 8.session连接
//        nSShSession.connect();
//        System.out.println("Session已连接");
//        return nSShSession;
//    }

    /**
     * @param session
     * @return
     * @throws JSchException
     */
    public ChannelSftp connect(Session session) throws JSchException {
        // 1.声明连接Sftp的通道
        ChannelSftp nChannelSftp = null;
        // 9.打开sftp通道
        Channel channel = session.openChannel("sftp");
        // 10.开始连接
        channel.connect();
        nChannelSftp = (ChannelSftp) channel;
        return nChannelSftp;
    }

    /**
     * 文件重命名
     *
     * @param directory
     * @param oldname
     * @param newname
     * @param sftp
     */
    public void renameFile(String directory, String oldname, String newname, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rename(oldname, newname);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     *
     * @param directory
     * @param uploadFile
     * @param sftp
     */
    public void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sftp.disconnect();
        sftp.exit();
    }

    /**
     * 上传文件
     *
     * @param directory
     * @param sftp
     */
    public void upload(String directory, InputStream inputStream, String fileName, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.put(inputStream, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sftp.disconnect();
        sftp.exit();
    }

    /**
     * 下载文件
     *
     * @param directory
     * @param downloadFile
     * @param saveFile
     * @param sftp
     */
    public void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            sftp.get(downloadFile, new FileOutputStream(file));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param directory
     * @param deleteFile
     * @param sftp
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            mkdirs(sftp, directory);
            sftp.cd(directory);
            sftp.rm(deleteFile);
            System.out.println("删除成功");
        } catch (Exception e) {
            System.out.println("删除失败");
            e.printStackTrace();
        }
    }

    /**
     * 列出列表下的文件
     *
     * @param directory
     * @param sftp
     * @return
     * @throws SftpException
     */
    public Vector listFiles(String directory, ChannelSftp sftp) throws SftpException {
        return sftp.ls(directory);
    }

    /**
     * 下载文件夹下面的所有文件
     *
     * @param viDirectory
     * @param viSaveDir
     * @return
     */
    public List<String> downloadDirFile(String viDirectory, Session session, String viSaveDir) {
        ChannelSftp nChannelSftp = null;
        List<String> nFileNameList = null;
        try {
            // 1.实例化nSftpUtil工具类
            SftpUtils nSftpUtil = new SftpUtils();
            nChannelSftp = nSftpUtil.connect(session);
            // 3.获取目录下面所有文件
            Vector nVector = nChannelSftp.ls(viDirectory);
            // 4.循环遍历文件
            for (int i = 0; i < nVector.size(); i++) {
                // 5.进入服务器文件夹
                nChannelSftp.cd(viDirectory);
                // 6.实例化文件对象
                String nFileName = nVector.get(i).toString().substring(56, nVector.get(i).toString().length());
                if (!nFileName.contains("csv")) {
                    continue;
                }
                File nFile = new File(viSaveDir + File.separator + nFileName);
                // 7.下载文件
                nChannelSftp.get(nFileName, new FileOutputStream(nFile));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nChannelSftp.disconnect();
        }
        return nFileNameList;
    }

    /**
     * 关闭连接 server
     */
    public void logout(ChannelSftp sftp, Session session) {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                System.out.println("sftp is closed already");
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }

    public boolean mkdirs(ChannelSftp sftp, String dir) {
        SftpATTRS attrs = null;
        String[] dirs = dir.split("/");
        System.out.println(Json.toJson(dirs));
        String absolute = "";
        for (String temp : dirs) {
            absolute += "/" + temp;
            try {
                attrs = sftp.stat(dir);
            } catch (SftpException e) {
//                e.printStackTrace();
                try {
                    sftp.mkdir(absolute);
                    System.out.println(absolute+"目录已创建！");
                } catch (SftpException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return true;
    }

    public boolean mkdir(ChannelSftp sftp, String dir) {
        SftpATTRS attrs = null;
        try {
            sftp.mkdir(dir);
            return true;
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return false;
    }

}