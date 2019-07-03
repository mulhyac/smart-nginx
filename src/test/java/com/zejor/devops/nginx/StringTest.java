package com.zejor.devops.nginx;


import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.ArrayUtils;

public class StringTest {

    public static void main(String[] args) {
//        System.out.println(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, "test-data"));//testData
//        System.out.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "test_data"));//testData
//        System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "test_data"));//TestData
//
//        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "testdata"));//testdata
//        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "TestData"));//test_data
//        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "testData"));//test-data

        String[] names="aaa.bbb.com".split("\\.");
        ArrayUtils.reverse(names);
        String target = Joiner.on(".").join(names);
        System.out.println(target);
    }

}
