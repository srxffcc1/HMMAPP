package com.healthy.library.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Li
 * @date 2019/03/12 11:07
 * @des 时间戳相关操作
 */
@Deprecated
public class TimestampUtils {

    public static String timestamp2String(String timestamp, String pattern) {
        try {
            long time = Long.parseLong(timestamp);
            Date date = new Date(time);
            return new SimpleDateFormat(pattern, Locale.CHINA).format(date);
        } catch (Exception e) {
            return timestamp;
        }
    }

    public static String timestamp2String(long timestamp, String pattern) {
        try {
            Date date = new Date(timestamp);
            return new SimpleDateFormat(pattern, Locale.CHINA).format(date);
        } catch (Exception e) {
            return pattern;
        }
    }

    public static Date string2Date(String s, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.CHINA).parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}