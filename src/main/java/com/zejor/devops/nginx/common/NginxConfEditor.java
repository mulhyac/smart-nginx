package com.zejor.devops.nginx.common;

import com.github.odiszapc.nginxparser.*;
import com.google.common.base.CaseFormat;
import com.zejor.devops.nginx.bean.NginxServer;
import com.zejor.devops.nginx.bean.NginxLocation;
import com.zejor.devops.nginx.bean.NginxUpstream;
import com.zejor.devops.nginx.bean.NginxUpstreamItem;
import com.zejor.devops.nginx.domain.Nginx;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ReflectionUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Nginx 配置编辑器
 */
@Setter
@Getter
public class NginxConfEditor {

    private NgxConfig ngxConfig;

    private Nginx nginx;

    NgxDumper dumper = null;

    public NginxConfEditor() {

    }

    public NginxConfEditor(Nginx nginx, NgxConfig ngxConfig) {
        this.ngxConfig = ngxConfig;
        this.nginx = nginx;
        init();
    }

    public NginxConfEditor(Nginx nginx) {
        this.nginx = nginx;
        init();
    }

    /**
     * 读配置
     *
     * @return : NgxConfig
     */
    public void init() {
        try {
            ngxConfig = NgxConfig.read(nginx.getNginxConf());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dumper = new NgxDumper(ngxConfig);
    }

    public String getConfContent() {
        return dumper.dump();
    }

    public void dump() {
        try {
            dumper.dump(new FileOutputStream(nginx.getNginxConf()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void dumpRemote(int nginxId) {


    }
//
//    /**
//     * 校验Nginx配置文件
//     */
//    public void check(String confText) {
//        String confPath = Configure.getNginxConfPath() + ".temp.check";
//        try (FileOutputStream out = new FileOutputStream(confPath)) {
//            out.write(confText.getBytes("UTF-8"));
//            out.flush();
//        } catch (Exception e) {
//            throw new NginxServiceManagerException("Nginx临时配置写入配置");
//        }
//
//        //校验
//        try {
//            String check = CMDUtil.excuse(CommonFields.NGINX + " -t -c " + confPath, Configure.getNginxPath());
//            if (check.indexOf(CommonFields.NGINX + ": configuration file " + confPath + " test is successful") == -1) {
//                throw new NginxServiceManagerException("Nginx配置文件校验失败:" + check);
//            }
//        } finally {
//            new File(confPath).delete();
//        }
//    }

    /**
     * 根据指定主机和端口的upstream
     *
     * @param serverName
     * @param port
     * @return
     */
    public List<NginxUpstream> listUpstream(String serverName, int port) {
        List<NginxServer> nginxServers = listNginxServer();
        for (NginxServer server : nginxServers) {
            if (server.getName().equals(serverName) && server.getPort() == port) {
                //获得负载配置列表
//                    List<NginxUpstream> nginxUpstreams = listUpstreams(serverName,port);
//                    return nginxUpstreams;
            }
        }
        return new ArrayList<NginxUpstream>();
    }

    /**
     * 获得Nginx服务监听列表
     */
    public List<NginxServer> listNginxServer() {
        List<NginxServer> result = new ArrayList<>();
        List<NgxEntry> servers = ngxConfig.findAll(NgxConfig.BLOCK, "http", "server");
        for (NgxEntry enty : servers) {
            NgxBlock serverBlock = (NgxBlock) enty;
            NginxServer nginxServer = new NginxServer();
            nginxServer.setPort(findIntParam(serverBlock, "listen"));
            nginxServer.setName(findParam(serverBlock, "server_name"));
            result.add(nginxServer);
        }
        return result;
    }


    public boolean deleteServerListener(int nginxId, NginxServer nginxServer) {
        NgxBlock http = ngxConfig.findBlock("http");
        List<NgxEntry> servers = http.findAll(NgxConfig.BLOCK, "server");
        for (NgxEntry entry : servers) {
            NgxBlock ser = (NgxBlock) entry;
            NgxParam listen = ser.findParam("listen");
            NgxParam server = ser.findParam("server_name");
            if (null != listen && null != server) {
                if (server.getValue().equals(nginxServer.getName()) && Integer.parseInt(listen.getValue()) == nginxServer.getPort()) {
                    ngxConfig.remove(http);
                    http.remove(ser);
                    ngxConfig.addEntry(http);
                    dump();
                    return true;
//                    return Result.success("删除成功！");
                }
            }
        }
        return false;
    }

    /**
     * 修改或添加NgxConfig对象中的监听配置
     *
     * @param nginxServer
     */
    public void addServerListener(NginxServer nginxServer) {
        NgxBlock http = ngxConfig.findBlock("http");
        List<NgxEntry> servers = http.findAll(NgxConfig.BLOCK, "server");
        List<NginxServer> nginxServers = listNginxServer();
        for (NginxServer server : nginxServers) {
            if (server.getPort() == nginxServer.getPort() && server.getName().equals(nginxServer.getName())) {
                return;
            }
        }
        NgxBlock serBlock = buildServer(nginxServer);
        ngxConfig.remove(http);
        http.addEntry(serBlock);
        ngxConfig.addEntry(http);
        dump();
    }

    public NgxBlock buildServer(NginxServer nginxServer) {
        NgxBlock ngxBlock = new NgxBlock();
        ngxBlock.addValue(new NgxToken("server"));
        NgxParam port = new NgxParam();
        port.addValue("listen");
        port.addValue(String.valueOf(nginxServer.getPort()));
        ngxBlock.addEntry(port);
        NgxParam name = new NgxParam();
        name.addValue("server_name");
        name.addValue(nginxServer.getName());
        ngxBlock.addEntry(name);
        return ngxBlock;
    }

    private List<NgxEntry> setNgxParam(List<NgxEntry> servConfs, NgxParam param, String name) {
        boolean has = false;
        List<NgxEntry> res = new ArrayList<>();
        for (NgxEntry servConf : servConfs) {
            if (servConf instanceof NgxParam) {
                NgxParam p = ((NgxParam) servConf);
                if (p.getName().equals(name)) {
                    servConf = param;
                    has = true;
                }
            }
            res.add(servConf);
        }
        if (!has) {
            res.add(param);
        }
        return res;
    }

    public List<NginxUpstream> listUpstreams() {
        List<NgxEntry> upstreamNgxEntries = ngxConfig.findAll(NgxConfig.BLOCK, "http", "upstream");
        List<NginxUpstream> result = new ArrayList<>();
        for (NgxEntry ngxEntry : upstreamNgxEntries) {
            NginxUpstream upstream = new NginxUpstream();
            NgxBlock block = (NgxBlock) ngxEntry;
            upstream.setItems(new ArrayList<>());
            upstream.setName(block.getName());
            upstream.setValue(block.getValue());
            for (NgxEntry temp : block.getEntries()) {
                NgxParam ngxBlock = (NgxParam) temp;
                NginxUpstreamItem item = new NginxUpstreamItem();
                List<String> values = ngxBlock.getValues();
                item.setAddress(values.get(0));
                if (values.size() > 1) {
                    item.setWeight(Integer.parseInt(values.get(1).replace("weight=", "").trim()));
                }
                upstream.getItems().add(item);
            }
            result.add(upstream);
        }
        return result;
    }

    public Set<String> getAlreadyUpstream() {
        List<NgxEntry> upstreamNgxEntries = ngxConfig.findAll(NgxConfig.BLOCK, "http", "upstream");
        Set<String> upstreams = new HashSet<>();
        for (NgxEntry ngxEntry : upstreamNgxEntries) {
            upstreams.add(((NgxBlock) ngxEntry).getValue());
        }
        return upstreams;
    }

    public void addUpstream(NginxUpstream upstream) {
        NgxBlock http = ngxConfig.findBlock("http");
        List<NgxEntry> servers = ngxConfig.findAll(NgxConfig.BLOCK, "http", "server");
        Set<String> upstreams = getAlreadyUpstream();
        if (upstreams.contains(upstream.getValue())) {
            return;
        }
        NgxBlock upstreamBlock = new NgxBlock();
        upstreamBlock.addValue("upstream");
        upstreamBlock.addValue(upstream.getValue());

        // 负载列表
        for (NginxUpstreamItem item : upstream.getItems()) {
            NgxParam param = new NgxParam();
            param.addValue("server");
            param.addValue(item.getAddress());
            if (item.getWeight() > 0) {
                param.addValue("weight=" + item.getWeight());
            }
            upstreamBlock.addEntry(param);
        }
        http.addEntry(upstreamBlock);
        ngxConfig.remove(http);
        for (NgxEntry server : servers) {
            http.remove(server);
        }
        for (NgxEntry server : servers) {
            http.addEntry(server);
        }
        ngxConfig.addEntry(http);
        dump();
    }

    public void deleteUpstream(String upstream) {
        NgxBlock http = ngxConfig.findBlock("http");
        List<NgxEntry> servers = ngxConfig.findAll(NgxConfig.BLOCK, "http", "server");
        List<NgxEntry> upstreamNgxEntries = ngxConfig.findAll(NgxConfig.BLOCK, "http", "upstream");
        for (NgxEntry ngxEntry : upstreamNgxEntries) {
            if (((NgxBlock) ngxEntry).getValue().equals(upstream))
                http.remove(ngxEntry);
        }
        ngxConfig.remove(http);
        for (NgxEntry server : servers) {
            http.remove(server);
        }
        for (NgxEntry server : servers) {
            http.addEntry(server);
        }
        ngxConfig.addEntry(http);
        dump();
    }

    public List<NginxLocation> listRules(NginxServer nginxServer) {
        NgxBlock http = ngxConfig.findBlock("http");
        List<NgxEntry> serverList = http.findAll(NgxBlock.class, "server");
        for (NgxEntry ngxEntry : serverList) {
            NgxBlock block = (NgxBlock) ngxEntry;
            int listen = findIntParam(block, "listen");
            String serverName = findParam(block, "server_name");
            if ((!serverName.equals(nginxServer.getName())) || listen != nginxServer.getPort()) {
                continue;
            }
            return listRules(block);
        }
        return new ArrayList<>();
    }

    public boolean addLocation(NginxServer nginxServer, NginxLocation nginxLocation) {
        NgxBlock http = ngxConfig.findBlock("http");
        List<NgxEntry> servers = ngxConfig.findAll(NgxConfig.BLOCK, "http", "server");
        for (NgxEntry server : servers) {
            NgxBlock block = (NgxBlock) server;
            int listen = findIntParam(block, "listen");
            String serverName = findParam(block, "server_name");
            if ((serverName.equals(nginxServer.getName())) && listen == nginxServer.getPort()) {
                Set<String> already = getAlreadyLocation(block);
                if (already.contains(nginxLocation.getPath())) {
                    return false;
                }
                NgxBlock ngxBlock = buildLocation(nginxLocation);
                block.addEntry(ngxBlock);
            }
        }
        ngxConfig.remove(http);
        ngxConfig.addEntry(http);
        dump();
        return true;
    }

    public boolean deleteLocation(NginxServer nginxServer, NginxLocation nginxLocation) {
        NgxBlock http = ngxConfig.findBlock("http");
        List<NgxEntry> servers = ngxConfig.findAll(NgxConfig.BLOCK, "http", "server");
        for (NgxEntry server : servers) {
            NgxBlock block = (NgxBlock) server;
            int listen = findIntParam(block, "listen");
            String serverName = findParam(block, "server_name");
            if ((serverName.equals(nginxServer.getName())) && listen == nginxServer.getPort()) {
                List<NgxEntry> locationList = block.findAll(NgxBlock.class, "location");
                for (NgxEntry ngxEntry : locationList) {
                    NgxBlock locationBlock = (NgxBlock) ngxEntry;
                    if (locationBlock.getValue().equals(nginxLocation.getPath())) {
                        block.remove(locationBlock);
                        ngxConfig.remove(http);
                        ngxConfig.addEntry(http);
                        dump();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Set<String> getAlreadyLocation(NgxBlock block) {
        HashSet<String> already = new HashSet<String>();
        List<NgxEntry> locationList = block.findAll(NgxBlock.class, "location");
        for (NgxEntry ngxEntry : locationList) {
            NgxBlock locationBlock = (NgxBlock) ngxEntry;
            already.add(locationBlock.getValue());
        }
        return already;
    }

    public List<NginxLocation> listRules(NgxBlock block) {
        List<NginxLocation> locations = new ArrayList<>();
        List<NgxEntry> locationList = block.findAll(NgxBlock.class, "location");
        for (NgxEntry ngxEntry : locationList) {
            NgxBlock locationBlock = (NgxBlock) ngxEntry;
            NginxLocation location = buildLocation(locationBlock);
            locations.add(location);
        }
        return locations;
    }

    public NgxBlock buildLocation(NginxLocation nginxLocation) {
        NgxBlock ngxBlock = new NgxBlock();
        ngxBlock.addValue("location");
        ngxBlock.addValue(nginxLocation.getPath());

        Class<NginxLocation> clazz = NginxLocation.class;
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if ("path".equals(field.getName())) {
                continue;
            }
            // 获取getXxx()方法名称
            char[] cs = field.getName().toCharArray();
            cs[0] -= 32;
            Method method = ReflectionUtils.findMethod(clazz, "get" + String.valueOf(cs));
            Object value = ReflectionUtils.invokeMethod(method, nginxLocation);
            if (value != null) {
                NgxParam ngxParam = new NgxParam();
                String entryKey = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
                ngxParam.addValue(entryKey + " " + value);
                ngxBlock.addEntry(ngxParam);
            }
        }
        return ngxBlock;
    }

    public NginxLocation buildLocation(NgxBlock locationBlock) {
        NginxLocation location = new NginxLocation();
        location.setPath(locationBlock.getValue());
        location.setProxyPass(findParam(locationBlock, "proxy_pass"));
        location.setIndex(findParam(locationBlock, "index"));
        location.setRoot(findParam(locationBlock, "root"));
        location.setTryFiles(findParam(locationBlock, "try_files"));
        location.setRoot(findParam(locationBlock, "deny"));
        return location;
    }

    public String findParam(NgxBlock locationBlock, String key) {
        NgxParam rootParam = locationBlock.findParam(key);
        if (rootParam != null) {
            return rootParam.getValue();
        }
        return null;
    }

    public int findIntParam(NgxBlock locationBlock, String key) {
        NgxParam rootParam = locationBlock.findParam(key);
        if (rootParam != null) {
            return Integer.parseInt(rootParam.getValue());
        }
        return -1;
    }
}
