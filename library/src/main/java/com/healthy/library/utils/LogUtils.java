package com.healthy.library.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.ListUtil;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Li
 * @date 2018/11/1 09:39
 * @des
 */

public class LogUtils {

    private static String TAG="LogUtils: ";

    public static void e(String msg) {
        if (ChannelUtil.isRealRelease()) {
            return;
        }
        String tag = "net";
        StackTraceElement element;
        element = new Exception().getStackTrace()[1];

        if (msg.contains("{")) {
            msg = msg.replaceAll("[{],", "{\n")
                    .replaceAll("[}]", "}\n")
                    .replaceAll(",", ",\n");
        }

        int length = msg.length();
        int maxLength = 3000;

        if (length <= maxLength) {
            Log.e(tag, String.format("%s-%s-%s:%s",
                    element.getClassName().substring(element.getClassName().lastIndexOf('.')),
                    element.getMethodName(),
                    element.getLineNumber(), msg));
            return;
        }

        int start = 0;
        int end = maxLength;
        String prefix = String.format("%s-%s-%s:",
                element.getClassName().substring(element.getClassName().lastIndexOf('.')),
                element.getMethodName(),
                element.getLineNumber());
        while (start < length) {
            Log.e(tag, String.format("%s%s", prefix, msg.substring(start, end)));
            prefix = "";
            start += maxLength;
            if (length - start >= maxLength) {
                end += maxLength;
            } else {
                end = length;
            }
        }
    }

    public static void e(String msg, String tag) {
        if (ChannelUtil.isRealRelease()) {
            return;
        }
        if (msg.contains("{")) {
            msg = msg.replaceAll("[{],", "{\n")
                    .replaceAll("[}]", "}\n")
                    .replaceAll(",", ",\n");
        }
        Log.e(tag, msg);
    }

    //打印bean对象
    public static void e(Object obj, String tag) {
        if (obj == null) {
            return;
        }
        e(new Gson().toJson(obj), tag);
    }

    //打印ArrayList
    public static void e(ArrayList<Object> list, String tag) {
        if (ListUtil.isEmpty(list)) {
            return;
        }
        e(new Gson().toJson(list), tag);
    }

    //打印map
    public static void e(Map<Object,Object> map, String tag) {
        if (map == null) {
            return;
        }
        e(new Gson().toJson(map), tag);
    }

    public static void e(Object obj) {
        if (obj == null) {
            return;
        }
        e(new Gson().toJson(obj), TAG);
    }

    public static void e(ArrayList<Object> list) {
        if (ListUtil.isEmpty(list)) {
            return;
        }
        e(new Gson().toJson(list), TAG);
    }

    public static void e(Map<Object, Object> map) {
        if (map == null) {
            return;
        }
        e(new Gson().toJson(map), TAG);
    }

}
