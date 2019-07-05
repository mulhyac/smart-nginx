package com.zejor.devops.nginx.xstream;

import com.thoughtworks.xstream.XStream;
import org.nutz.json.Json;

import java.util.List;

public class XStreamTest {

    public static void main(String[] args) {
        String xml="<ConfigUser>\n" +
                "    <type>ADMIN</type>\n" +
                "    <users>\n" +
                "        <AdminUser>\n" +
                "            <name>zhaoyb</name>\n" +
                "            <pwd>abc123</pwd>\n" +
                "            <ip>\n" +
                "                <string>127.0.0.1</string>\n" +
                "                <string>203.119.80.128</string>\n" +
                "            </ip>\n" +
                "        </AdminUser>\n" +
                "        <AdminUser>\n" +
                "            <name>liangqunxing</name>\n" +
                "            <pwd>abc123</pwd>\n" +
                "            <ip>\n" +
                "                <string>127.0.0.1</string>\n" +
                "                <string>202.173.100.126</string>\n" +
                "            </ip>\n" +
                "        </AdminUser>\n" +
                "        <AdminUser>\n" +
                "            <name>liuyu</name>\n" +
                "            <pwd>abc123</pwd>\n" +
                "            <ip>\n" +
                "                <string>127.0.0.1</string>\n" +
                "                <string>203.119.80.108</string>\n" +
                "            </ip>\n" +
                "        </AdminUser>\n" +
                "    </users>\n" +
                "</ConfigUser>";
        XStream xStream = new XStream();
        xStream.alias("AdminUser", AdminUser.class);
        xStream.alias("ConfigUser", ConfigUsers.class);
        ConfigUsers users = (ConfigUsers) xStream.fromXML(xml);
        List<AdminUser> adminUsers = users.getUsers();
        System.out.println(Json.toJson(users));
    }

}
