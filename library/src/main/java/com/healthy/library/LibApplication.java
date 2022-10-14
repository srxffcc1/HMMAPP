package com.healthy.library;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.getkeepsafe.relinker.ReLinker;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpUtilsOld;
import com.tencent.mmkv.MMKV;


public class LibApplication extends MultiDexApplication {
    private static Application app = null;
    public final static String TAG = "x024";
    public final static String ERROR_FILENAME = "x024_error.log";
    public static MMKV mmkv;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initMMKVReal(this);
    }

    public static void initMMKVReal(Application application) {
        String rootDir = application.getFilesDir().getAbsolutePath() + "/mmkv";
        //一些 Android 设备（API level 19 Android 4.4）在安装/更新 APK 时可能出错, 导致 libmmkv.so 找不到。
        // 然后就会遇到 java.lang.UnsatisfiedLinkError 之类的 crash。有个开源库 ReLinker 专门解决这个问题，
        // 你可以用它来加载 MMKV：
        if (android.os.Build.VERSION.SDK_INT == 19) {
            MMKV.initialize(rootDir, new MMKV.LibLoader() {
                @Override
                public void loadLibrary(String libName) {
                    ReLinker.loadLibrary(app, libName);
                }
            });
        } else {
            rootDir = MMKV.initialize(application);
        }
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "MMKVPath" + rootDir);
        }
//        mmkv = MMKV.defaultMMKV();
        mmkv = MMKV.mmkvWithID("com.health.client.mmkv",MMKV.MULTI_PROCESS_MODE);//避免安卓多进程重启sp丢失
        initMMKV(application);
    }

    /**
     * 获取Application的Context
     **/
    public static Context getAppContext() {

        return app.getApplicationContext();
    }

    /**
     * 初始化MMKV
     */
    public static void initMMKV(Application application) {
        SharedPreferences old_main = PreferenceManager
                .getDefaultSharedPreferences(application);
        // 迁移旧数据
        int size = mmkv.importFromSharedPreferences(old_main);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "import size = " + size);
        }
        if(size > 0) {
            old_main.edit().clear().apply();
            SpUtilsOld.store(application, SpKey.YSLOOK, SpUtils.getValue(application,SpKey.YSLOOK,false));
            SpUtilsOld.store(application, "keyFrame", SpUtils.getValue(application,"keyFrame",false));
            SpUtilsOld.store(application, "referralCodeBind", SpUtils.getValue(application,"referralCodeBind",false));
        }
    }
}
