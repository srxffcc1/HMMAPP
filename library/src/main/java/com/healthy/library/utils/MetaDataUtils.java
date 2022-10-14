package com.healthy.library.utils;//package com.healthy.library.utils;
//
//import android.content.Context;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//
///**
// * Author: Li
// * Date: 2018/12/13 0013
// * Description:
// */
//public class MetaDataUtils {
//    public static String getDataFromApp(Context context, String metaDataKey) {
//        try {
//            ApplicationInfo applicationInfo = context
//                    .getPackageManager()
//                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
//            return applicationInfo.metaData.getString(metaDataKey,"");
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//}
