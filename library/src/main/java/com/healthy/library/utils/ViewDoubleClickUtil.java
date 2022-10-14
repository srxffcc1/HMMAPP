package com.healthy.library.utils;

import android.annotation.SuppressLint;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
@Deprecated //未确认此功能的有效程度
public class ViewDoubleClickUtil {

    public static void hookView(View view) {
        try {
            Class viewClazz = Class.forName("android.view.View");
            //事件监听器都是这个实例保存的
            Method listenerInfoMethod = viewClazz.getDeclaredMethod("getListenerInfo");
            if (!listenerInfoMethod.isAccessible()) {
                listenerInfoMethod.setAccessible(true);
            }
            Object listenerInfoObj = listenerInfoMethod.invoke(view);

            @SuppressLint("PrivateApi")
            Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");

            Field onClickListenerField = listenerInfoClazz.getDeclaredField("mOnClickListener");
            //修改修饰符带来不能访问的问题
            if (!onClickListenerField.isAccessible()) {
                onClickListenerField.setAccessible(true);
            }
            View.OnClickListener mOnClickListener = (View.OnClickListener) onClickListenerField.get(listenerInfoObj);
            //自定义事件监听器
            View.OnClickListener onClickListenerProxy = new OnClickListenerProxy(mOnClickListener);
            //更换成自己的点击事件
            onClickListenerField.set(listenerInfoObj, onClickListenerProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //自定义的事件监听器
    private static class OnClickListenerProxy implements View.OnClickListener {

        private View.OnClickListener object;
        //点击时间控制
        private int MIN_CLICK_DELAY_TIME = 1000;

        private long lastClickTime = 0;

        private OnClickListenerProxy(View.OnClickListener object) {
            this.object = object;
        }

        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                if (object != null) {
                    object.onClick(v);
                }
            }
        }
    }
}
