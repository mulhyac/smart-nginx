package com.zejor.devops.nginx.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SS");

    public static String getTime() {
        return DEFAULT_FORMAT.format(new Date());
    }

}
