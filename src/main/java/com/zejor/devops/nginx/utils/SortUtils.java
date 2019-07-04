package com.zejor.devops.nginx.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtils<T> {

    /**
     * @param targetList 要排序的实体类List集合
     * @param sortField  排序字段(实体类属性名)
     * @param sortMode   true正序，false逆序
     */
    public static <T> void sort(List<T> targetList, final String sortField, final boolean sortMode) {
        if (targetList == null || targetList.size() < 2 || sortField == null || sortField.length() == 0) {
            return;
        }
        Collections.sort(targetList, new Comparator() {
            @Override
            public int compare(Object obj1, Object obj2) {
                int retVal = 0;
                try {
                    // 获取getXxx()方法名称
                    char[] cs = sortField.toCharArray();
                    cs[0] -= 32;
                    String methodStr = "get" + String.valueOf(cs);
                    Method method = ReflectionUtils.findMethod(obj1.getClass(), methodStr);
                    String value1 = ReflectionUtils.invokeMethod(method, obj1).toString();
                    String value2 = ReflectionUtils.invokeMethod(method, obj1).toString();
                    retVal = SortUtils.compare(value1, value2, sortMode);
                } catch (Exception e) {
                    System.out.println("List<" + ((T) obj1).getClass().getName() + ">排序异常！");
                    e.printStackTrace();
                }
                return retVal;
            }
        });
    }

    public static <T> void sort(List<T> targetList, final boolean sortMode, List<Converter> converters) {
        Collections.sort(targetList, new Comparator() {
            @Override
            public int compare(Object obj1, Object obj2) {
                int retVal = 0;
                try {
                    Object v1 = null;
                    Object v2 = null;
                    if (converters != null && converters.size() > 0) {
                        v1 = convert(obj1, converters);
                        v2 = convert(obj2, converters);
                    }
                    String value1 = v1.toString();
                    String value2 = v2.toString();
                    retVal = SortUtils.compare(value1, value2, sortMode);
                } catch (Exception e) {
                    System.out.println("List<" + ((T) obj1).getClass().getName() + ">排序异常！");
                    e.printStackTrace();
                }
                return retVal;
            }
        });
    }


    private static int compare(String value1, String value2, boolean sortMode) {
        if (sortMode) {
            return value1.compareTo(value2);
        } else {
            return value2.compareTo(value1);
        }
    }

    private static Object convert(Object source, List<Converter> converters) {
        for (Converter converter : converters) {
            source = converter.convert(source);
        }
        return source;
    }

    public interface Converter {
        public Object convert(Object source);
    }

}