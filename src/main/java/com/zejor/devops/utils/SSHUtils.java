package com.zejor.devops.utils;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.zejor.devops.nginx.domain.Linux;
import org.nutz.lang.Files;

import java.io.*;

public class SSHUtils extends SecureShell {

    private static final String ENCODING = "UTF-8";

    public static String shell(Session session,String log,String[] shells)throws IOException,JSchException{
        String result = "";

        //2.尝试解决 远程ssh只能执行一句命令的情况
        ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
        InputStream inputStream = channelShell.getInputStream();//从远端到达的数据  都能从这个流读取到
        channelShell.setPty(true);
        channelShell.connect();

        OutputStream outputStream = channelShell.getOutputStream();//写入该流的数据  都将发送到远程端
        //使用PrintWriter 就是为了使用println 这个方法
        //好处就是不需要每次手动给字符加\n
        PrintWriter printWriter = new PrintWriter(outputStream);
        for(String shell :shells) {
            printWriter.println(shell);
            printWriter.flush();//把缓冲区的数据强行输出
        }
        File logFile=null;
        if(log!=null){
            logFile=new File(log);
        }
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
//                if (s.indexOf("--More--") >= 0) {
//                    outputStream.write((" ").getBytes());
//                    outputStream.flush();
//                }
                if(logFile!=null){
                    Files.appendWrite(logFile,s);
                }
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
        outputStream.close();
        inputStream.close();
        channelShell.disconnect();
        session.disconnect();

        return result;
    }
}
