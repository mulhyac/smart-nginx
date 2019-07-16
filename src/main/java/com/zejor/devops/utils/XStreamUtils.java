package com.zejor.devops.utils;

import com.thoughtworks.xstream.XStream;
import com.zejor.devops.autobuild.springboot.ETLXstream;

public class XStreamUtils {

    public static <T> T toBean(Class<T> clazz, String xml) {
        try {
            XStream xstream = new ETLXstream();
            xstream.processAnnotations(clazz);
            xstream.autodetectAnnotations(true);
            xstream.setClassLoader(clazz.getClassLoader());
            return (T) xstream.fromXML(xml);
        } catch (Exception e) {
//            log.error("[XStream]XML转对象出错:{}", e.getCause());
            e.printStackTrace();
            throw new RuntimeException("[XStream]XML转对象出错");
        }
    }

}
