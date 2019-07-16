package com.zejor.devops.nginx.logger;

import com.zejor.devops.nginx.logger.domain.Log;
import com.zejor.devops.nginx.spring.plugins.SpringUtils;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 目前由于没有用户，可随机生成32位以下字母或数字
 */
@Component
@ServerEndpoint(value = "/admin/logger/{logId}")
public class WebSocketLogger {

    private String log = null;


    /**
     * 在线人数
     */
    public static int onlineNumber = 0;

    /**
     * 以uuid作为sessionId，值为连接会话,如果生成uuid重复，将导致前面连接中断。但这种概率在本应用中应该不高的吧
     */
    private static Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

    /**
     * 解决同一用户同日志同时不同会话的解决办法
     */
    private static Map<Integer, Set<String>> loggerRouters = new HashMap<Integer, Set<String>>();

    /**
     * 日志终端映射
     */
    private static Map<Integer, RemoteLogger> loggers = new HashMap<Integer, RemoteLogger>();

    /**
     * 日志标识
     */
    private int logId;

    private String sessionId;


    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("logId") int logId, Session session) {
        onlineNumber++;
        this.logId = logId;
        //设置能接收的消息体字节大小()，此处设置为100M
        session.setMaxTextMessageBufferSize(104857);

        sessionId = UUID.randomUUID().toString().replace("-", "");
        sessions.put(sessionId, session);
        Set<String> routers = loggerRouters.get(logId);
        if (routers == null) {
            routers = new HashSet<>();
            loggerRouters.put(logId, routers);
        }
        routers.add(sessionId);
        logger(session, logId);
//        try {
//            // 执行tail -f命令
//            Process process = Runtime.getRuntime().exec("tail -f /home/eziep/Desktop/log/smart_shell_1563111577193.log");
//            InputStream inputStream = process.getInputStream();
//
//            // 一定要启动新的线程，防止InputStream阻塞处理WebSocket的线程
//            LocalLogger thread = new LocalLogger(inputStream, session);
//            thread.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void logger(Session session, int logId) {
        Dao dao = SpringUtils.getBean("dao");
        Log log = dao.fetch(Log.class, logId);
        if (log.getLinuxId() > 0) {
            dao.fetchLinks(log, "linux");
        }
        switch (log.getLogType()) {
            case 1:
                localLog(session, log);
                break;
            case 2:
                remoteLog(session, log);
                break;
            default:
                break;
        }
    }

    /**
     * 实时查看远程日志
     *
     * @param session
     * @param log
     */
    public void remoteLog(Session session, Log log) {
        //如该日志
        if (loggers.containsKey(logId)) {
            return;
        }
        RemoteLogger logger = new RemoteLogger(log.getLinux(), this, log.getLogPath(), logId);
        System.out.println("开始启动SSH通道！");
        loggers.put(logId, logger);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void localLog(Session session, Log log) {
        try {
            // 执行tail -f命令
            Thread.sleep(1000);
            Process process = Runtime.getRuntime().exec("tail -100f " + log.getLogPath());
            InputStream inputStream = process.getInputStream();
            System.out.println("开始实时刷新本地日志[" + log.getLogPath() + "].");
            // 一定要启动新的线程，防止InputStream阻塞处理WebSocket的线程
            LocalLogger thread = new LocalLogger(inputStream, session);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        onlineNumber--;
        if (sessionId == null) {
            return;
        }
        if (loggerRouters.get(logId).size() > 0) {
            return;
        }
        try {
            loggers.get(logId).stop();
            System.out.println("停止了一个SSH通道！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            loggers.remove(logId);
            sessions.remove(sessionId);
            loggerRouters.get(logId).remove(sessionId);
        }

    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("我不接收任何消息哦！只做日志输出");
    }


    public void sendMessageTo(String message, int logId) throws IOException {
        for (String sessionId : loggerRouters.get(logId)) {
            sessions.get(sessionId).getBasicRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }

}