package com.zejor.devops.nginx.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.mapper.ClassAliasingMapper;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("test")
public class Test {
    public List<String> tags = new ArrayList<String>();
    public List<String> notags = new ArrayList<String>();
    public List<String> tttt = new ArrayList<String>();

    public Test(String tag, String tag2) {
        tags.add(tag);
        tags.add(tag2);
        notags.add(tag);
        notags.add(tag2);
        tttt.add(tag);
        tttt.add(tag2);
    }

    public static void main(String[] args) {
        Test test = new Test("foo", "bar");
        XStream xstream = new XStream();
        ClassAliasingMapper mapper = new ClassAliasingMapper(xstream.getMapper());
        mapper.addClassAlias("tag", String.class);
        mapper.addClassAlias("test", Test.class);
        xstream.registerLocalConverter(Test.class, "tags", new CollectionConverter(mapper));
        xstream.alias("test", Test.class);
        System.out.println(xstream.toXML(test));
    }
}