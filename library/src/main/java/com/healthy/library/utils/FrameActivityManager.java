package com.healthy.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;


import java.util.Stack;


/**
 * Created by Administrator on 2017/5/10.
 * Activity栈
 */

public class FrameActivityManager {
    private static Stack<Activity> activityStack;
    private static final FrameActivityManager instance = new FrameActivityManager();
    private FrameActivityManager() {
    }
    public static FrameActivityManager instance() {
        return instance;
    }
    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }
    public static boolean checkNowIsTopActivity(Activity baseActivity) {
        try {
            if(activityStack==null){
                return true;
            }
            if(activityStack.lastElement().getClass().getName().equals(baseActivity.getClass().getName())){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }
    public void startActivity(Class clas){
        Context context=topActivity();
        if(context!=null){
            context.startActivity(new Intent(context,clas).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    /**
     * 获取当前Activity栈中元素个数
     */
    public int getCount() {
        return activityStack.size();
    }

    public void showAllStack() {
        for (Activity activity : activityStack) {
            try {
                Log.v("KJActivityStackShow", activity.getClass().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }
    public Activity topActivity() {
        if (activityStack == null) {
            return null;
        }
        if (activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return (Activity) activity;
    }
    public String topActivityName() {
        Activity activity=topActivity();
        return activity==null?"":activity.getClass().getSimpleName();
    }
    public Activity topActivityLifecycleOwner() {
        if (activityStack == null) {
            return null;
        }
        if (activityStack.isEmpty()) {
            return null;
        }
        for (int i = activityStack.size(); i >0; i--) {
            try {
                Activity activity = activityStack.get(i);
                if(activity instanceof LifecycleOwner){
                    return activity;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public Activity findActivity(Class<?> cls) {
        Activity activity = null;
        for (Activity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return activity;
    }
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity((Activity) activity);
    }
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                ((Activity) activity).finish();
            }
        }
    }
    public void finishActivity(Activity activity) {
        if (activity != null) {
            try {
                boolean flag = activityStack.remove((Activity) activity);
                activity.finish();//此处不用finish
                activity = null;
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
        }
    }
    public void removeActivity(Activity activity) {
        if (activity != null) {
            try {
                boolean flag = activityStack.remove((Activity) activity);
                activity = null;
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
        }
    }
    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        if(activityStack==null){
            return;
        }
        for (Activity activity : activityStack) {
            if (!(activity.getClass().equals(cls))) {
                try {
                    ((Activity) activity).finish();
                } catch (Exception e) {

                }
            }
        }
        for (int i = 0; i < activityStack.size(); i++) {
            if (!activityStack.get(i).getClass().getName()
                    .equals(cls.getName())) {
                activityStack.remove(i);
                i--;
            }
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if(activityStack==null){
            return;
        }
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                try {
                    ((Activity) activityStack.get(i)).finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                activityStack.remove(i);
                i--;
            }
        }
        activityStack.clear();
    }
//    @Deprecated
//    public void AppExit(Context cxt) {
//        appExit(cxt);
//    }
    /**
     * 应用程序退出
     */
    public void appExit(Context context) {
        try {
            finishAllActivity();
            Runtime.getRuntime().exit(0);
        } catch (Exception e) {
            Runtime.getRuntime().exit(-1);
        }
    }
    public void appExitJustKill(Context context) {
        try {
            Runtime.getRuntime().exit(0);
        } catch (Exception e) {
            Runtime.getRuntime().exit(-1);
        }
    }
    public void appFinishJust(Context context) {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }
}
