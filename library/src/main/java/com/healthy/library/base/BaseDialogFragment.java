package com.healthy.library.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNoNeedShowOnTop;
import com.healthy.library.utils.FrameActivityManager;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

public class BaseDialogFragment extends DialogFragment {

    public static void bundleMap(Map<String, Object> maporg, Bundle args) {
        if (maporg != null) {
            for (Map.Entry<String, Object> entry : maporg.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Integer) {
                    args.putInt(key, (Integer) value);
                } else if (value instanceof Boolean) {
                    args.putBoolean(key, (Boolean) value);
                } else if (value instanceof String) {
                    args.putString(key, (String) value);
                } else {
                    args.putSerializable(key, (Serializable) value);
                }
            }
        }

    }
    protected boolean cantoast = true;

    @Override
    public void show(final FragmentManager manager, String tag) {
        if(!manager.isDestroyed() && !manager.isStateSaved()) {
            if (!isAdded()) {
                try {
                    tag=tag+System.currentTimeMillis();
                    final String finalTag = tag;
                    showReal(manager, finalTag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else {
            if(this instanceof IsNoNeedShowOnTop){

            }else {
                try {
                    if(((FragmentActivity) FrameActivityManager.instance().topActivity())!=null&&!((FragmentActivity) FrameActivityManager.instance().topActivity()).isFinishing()){
                        if (!isAdded()) {
                            try {
                                tag=tag+System.currentTimeMillis();
                                final String finalTag = tag;
                                showReal(((FragmentActivity) FrameActivityManager.instance().topActivity())
                                        .getSupportFragmentManager(), finalTag);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    try {
                        CrashReport.postCatchedException(new Throwable("尝试使用顶层打开Dialog"));
                    } catch (Exception e2) {
                    }
                    e.printStackTrace();
                }
            }
        }
//        super.show(manager, tag);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },100);

    }


    public void showReal(FragmentManager manager, String tag){
        try {
            Class c = Class.forName("androidx.fragment.app.DialogFragment");
            Constructor con = c.getConstructor();
            Object obj = con.newInstance();
            Field dismissed = c.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(obj, false);
            Field shownByMe = c.getDeclaredField("mShownByMe");
            shownByMe.setAccessible(true);
            shownByMe.set(obj, false);
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void hideSoftKeyBoard(final View v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }, 100);
    }
}
