package com.zejor.devops.nginx.logger;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.zejor.devops.nginx.domain.Linux;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RemoteLogger {

    private Linux linux;

    private WebSocketLogger logger;

    private String log;

    private int logId;

    @Accessors
    Session session;

    @Accessors
    ChannelShell channelShell;

    @Accessors
    OutputStream outputStream;

    @Accessors
    InputStream inputStream;

    public RemoteLogger(Linux linux, WebSocketLogger logger, String log, int logId) {
        this.linux = linux;
        this.logger = logger;
        this.log = log;
        this.logId = logId;
    }

    public void start() throws Exception {
        JSch jsch = new JSch();

        session = jsch.getSession(linux.getLinuxUser(), linux.getLinuxHost(), linux.getLinuxPort());
        session.setPassword(linux.getLinuxPwd());
        session.setConfig("StrictHostKeyChecking", "no");//第一次访问服务器不用输入yes
        session.setTimeout(30000);
        session.connect();
        String result = "";

        //2.尝试解决 远程ssh只能执行一句命令的情况
        channelShell = (ChannelShell) session.openChannel("shell");
        inputStream = channelShell.getInputStream();//从远端到达的数据  都能从这个流读取到
        channelShell.setPty(true);
        channelShell.connect();

        outputStream = channelShell.getOutputStream();//写入该流的数据  都将发送到远程端
        //使用PrintWriter 就是为了使用println 这个方法
        //好处就是不需要每次手动给字符加\n
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println("tail -f " + log);
        printWriter.flush();//把缓冲区的数据强行输出

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
                String s = new String(tmp, 0, i);
                if (s.indexOf("--More--") >= 0) {
                    outputStream.write((" ").getBytes());
                    outputStream.flush();
                }
                logger.sendMessageTo(s, logId);
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

    }

    public void stop() throws Exception {
        outputStream.close();
        inputStream.close();
        channelShell.disconnect();
        session.disconnect();
        System.out.println("DONE");
    }
}
