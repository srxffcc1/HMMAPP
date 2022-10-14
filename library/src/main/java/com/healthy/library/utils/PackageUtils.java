package com.healthy.library.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Author: Li
 * Date: 2018/11/1 0001
 * Description:
 */
public class PackageUtils {
    /**
     * 判断App是否安装
     * @param packageName   包名
     * @param context   上下文
     * @return  是否安装
     */
    public static boolean isAppInstalled(String packageName, Context context) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}