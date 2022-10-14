package com.health.client;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;


import java.util.List;

/**
 * Created by le.xin on 2017/3/23.
 */

public class HMMPlayerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        //System.out.println("MyPlayerReceiver.onReceive " + intent);
//        if("com.app.test.android.Action_Close".equals(intent.getAction())) {
//            try {
//                XmPlayerManager.getInstance(context).pause();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            String actionName = "com.ximalaya.ting.android.ACTION_CLOSE";
//            if ("com.ximalaya.ting.android".equals(context.getPackageName())) {
//                actionName = "com.ximalaya.ting.android.ACTION_CLOSE_MAIN";
//            }
//            Intent intenclose = new Intent(actionName);
//            intenclose.setClass(context, PlayerReceiver.class);
//            context.sendBroadcast(intenclose);
//            try {
//                FloatWindow.get().hide();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if("com.app.test.android.Action_PAUSE_START".equals(intent.getAction())) {
//            try {
//                if(XmPlayerManager.getInstance(context).isPlaying()) {
//                    XmPlayerManager.getInstance(context).pause();
//                } else {
//                    XmPlayerManager.getInstance(context).play();
//                    FloatWindow.get().show();
//                    System.out.println("喜马拉雅悬浮出现1");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
