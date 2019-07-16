package com.zejor.devops.utils;

import com.jcraft.jsch.*;
import com.zejor.devops.nginx.domain.Linux;
import org.apache.commons.io.IOUtils;
import org.nutz.lang.Files;

import java.io.*;

public class SSHUtils extends SecureShell {

    private static final String ENCODING = "UTF-8";

    private Session session;

    private String[] shells;

    private String log;

    private String exec = "shell";

    private OutputStream outputStream;

    private InputStream inputStream;

    private File logFile = null;

    private ChannelShell channelShell = null;

    public String exec(Session session, String log, String[] shells, String exec) throws IOException, JSchException {
        String result = "";
        this.session = session;
        this.log = log;
        this.shells = shells;
        this.exec = exec;
        switch (exec) {
            case "exec":
                exec();
                break;
            case "channel":
                channel();
                break;
            default:
                exec();
                break;
        }
        return result;
    }

    public void log() {
        try {
            /**
             shell管道本身就是交互模式的。要想停止，有两种方式：
             一、人为的发送一个exit命令，告诉程序本次交互结束
             二、使用字节流中的available方法，来获取数据的总大小，然后循环去读。
             为了避免阻塞
             */
            byte[] tmp = new byte[1024];
            while (true) {
                while (inputStream.available() > 0) {
                    int i = inputStream.read(tmp, 0, 1024);
                    if (i < 0) break;
                    String temp = new String(tmp, 0, i);
                    Files.appendWrite(logFile, temp);
                }
                if (channelShell.isClosed()) {
                    System.out.println("exit-status:" + channelShell.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void channel() throws IOException, JSchException {
        try {
            //2.尝试解决 远程ssh只能执行一句命令的情况
            channelShell = (ChannelShell) session.openChannel("shell");
            inputStream = channelShell.getInputStream();//从远端到达的数据  都能从这个流读取到
            channelShell.setPty(true);
            channelShell.connect();
            //写入该流的数据  都将发送到远程端
            outputStream = channelShell.getOutputStream();
            //使用PrintWriter 就是为了使用println 这个方法
            //好处就是不需要每次手动给字符加\n
            PrintWriter printWriter = new PrintWriter(outputStream);
            for (String shell : shells) {
                printWriter.println(shell);
                printWriter.flush();//把缓冲区的数据强行输出
            }
            if (log != null) {
                logFile = new File(log);
            }
            log();
            outputStream.close();
            inputStream.close();
            channelShell.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exec() throws IOException, JSchException {
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        for (String shell : shells) {
            //1.默认方式，执行单句命令
            InputStream in = channelExec.getInputStream();
            channelExec.setCommand(shell);
            channelExec.setErrStream(System.err);
            channelExec.connect();
            String temp = IOUtils.toString(in, ENCODING);
            Files.appendWrite(logFile, temp);
        }
        channelExec.disconnect();

    }

    public String cat(Session session, String remote) throws JSchException, IOException {
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand("cat " + remote);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        String temp = IOUtils.toString(in, ENCODING);
        return temp;
    }

}
