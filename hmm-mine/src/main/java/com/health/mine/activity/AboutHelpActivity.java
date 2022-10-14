package com.health.mine.activity;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MineRoutes;

@Route(path = MineRoutes.MINE_SETTINGSABOUTHELP)
public class AboutHelpActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_about_help;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    public void passXSWDSYSM(View view) {
        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                .withString("url", UrlKeys.URL_XSWDSYSM)
                .withString("title", "")
                .navigation();
    }

    public void passZJWDSYSM(View view) {
        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                .withString("url", UrlKeys.URL_ZJWDSYSM)
                .withString("title", "")
                .navigation();
    }

    public void passHDDSYSM(View view) {
        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                .withString("url", UrlKeys.URL_HDDSYSM)
                .withString("title", "")
                .navigation();
    }
}
