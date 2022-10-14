package com.healthy.library.base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.healthy.library.utils.ScreenUtils;

/**
 * @author Li
 * @date 2019/03/04 14:38
 * @des
 */

public class BaseApp extends Application {
    private boolean isDebug = true;

    @Override
    public void onCreate() {
        super.onCreate();
        ScreenUtils.setDensity(this, 375f);
        if (isDebug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }
}
