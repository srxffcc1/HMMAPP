package com.healthy.library.utils;

/**
 * 防止多次点击
 *
 * @author: long
 * @date: 2021/4/13
 */
public class FastClickUtils {

    private static final int MIN_DELAY_TIME = 1000; // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

}
