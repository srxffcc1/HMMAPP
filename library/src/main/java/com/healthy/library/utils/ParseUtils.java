package com.healthy.library.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Li
 * @date 2019/04/04 14:26
 * @des 转换工具类
 */

public class ParseUtils {
    public static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int parseInt(String value) {
        return parseInt(value, 0);
    }

    public static float parseFloat(String value, float defaultValue) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static float parseFloat(String value) {
        return parseFloat(value, 0);
    }

    public static long parseLong(String value, long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String parseTimeToTime(String dayold, int dayafter) {
        String day = dayold.split(" ")[0].trim();
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day1 = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day1 + dayafter);

        String dayresult = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return dayresult;

    }


    public static long parseLong(String value) {
        return parseLong(value, 0);
    }

    public static String parseDistance(String distanceStr) {
        float distance = parseFloat(distanceStr);
        if (distance < 1000) {
            int d = (int) distance;
            return String.format("%sm", String.valueOf(d));
        } else {
            return String.format(Locale.CHINA, "%.1fkm", distance / 1000);
        }
    }

    public static String parseNumber(String orgNumber, int parse, String unitName) {
        float distance = parseFloat(orgNumber);
        if (distance < 0) {
            distance = 0;
        }
        if ("万".equals(unitName)) {
            if (distance < parse) {
                int d = (int) distance;
                return String.format("%s", String.valueOf(d));
            } else {
                if (distance < parse * 10000) {
                    return String.format(Locale.CHINA, "%.1f" + unitName, distance / parse);
                } else {
                    return String.format(Locale.CHINA, "%.1f" + "亿", distance / (parse * 10000));
                }
            }
        } else {
            if (distance < parse) {
                int d = (int) distance;
                return String.format("%s", String.valueOf(d));
            } else {
                return String.format(Locale.CHINA, "%.1f" + unitName, distance / parse);
            }
        }

    }
    public static String parseNumberWithAdd(String orgNumber, int parse, String unitName) {
        float distance = parseFloat(orgNumber);
        if (distance < 0) {
            distance = 0;
        }
        if ("万".equals(unitName)) {
            if (distance < parse) {
                int d = (int) distance;
                return String.format("%s", String.valueOf(d));
            } else {
                if (distance < parse * 10000) {
                    String result=String.format(Locale.CHINA, "%.1f", distance / parse);
                    if(result.split("\\.").length>1){//
                        return result.split("\\.")[0]+unitName+"+";
                    }
                    return String.format(Locale.CHINA, "%.1f" + unitName, distance / parse);
                } else {
                    String result=String.format(Locale.CHINA, "%.1f", distance / (parse * 10000));
                    if(result.split("\\.").length>1){//
                        return result.split("\\.")[0]+"亿"+"+";
                    }
                    return String.format(Locale.CHINA, "%.1f" + "亿", distance / (parse * 10000));
                }
            }
        } else {
            if (distance < parse) {
                int d = (int) distance;
                return String.format("%s", String.valueOf(d));
            } else {
                return String.format(Locale.CHINA, "%.1f" + unitName, distance / parse);
            }
        }

    }

}
