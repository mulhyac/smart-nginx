package com.zejor.devops.nginx.sort;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ListSortMain {

    public static void main(String[] args) throws InterruptedException {
//
//        List<Dog> dogList = new ArrayList<Dog>();
//        for (int i = 0; i < 5; i++) {
//            Dog d = new Dog();
//            d.setName("dog" + i);
//            d.setTime(new Date());
//            dogList.add(d);
//            Thread.sleep(1000);
//        }
//        System.out.println("排序前:" + dogList.toString());
//
//        SortUtils.sort(dogList, "name", true);
//        System.out.println("按name正序排：" + dogList.toString());
//
//        SortUtils.sort(dogList, "name", false);
//        System.out.println("按name逆序排：" + dogList.toString());
//
//        SortUtils.sort(dogList, "time", true);
//        System.out.println("按time正序排：" + dogList.toString());
//
//        SortUtils.sort(dogList, "time", false);
//        System.out.println("按time逆序排：" + dogList.toString());

        String prop = "username";
        long t1 = new Date().getTime();
        for(int i=0;i<100000;i++) {
            String methodStr = "get" + prop.substring(0, 1).toUpperCase() + prop.substring(1);
        }
        long t2 = new Date().getTime();
        for(int i=0;i<100000;i++) {
            char[] cs=prop.toCharArray();
            cs[0]-=32;
            String methodStr = "get" + String.valueOf(cs);
        }
        long t3=new Date().getTime();
        System.out.println("t1-t2:"+(t2-t1));
        System.out.println("t2-t3:"+(t3-t2));
    }

}