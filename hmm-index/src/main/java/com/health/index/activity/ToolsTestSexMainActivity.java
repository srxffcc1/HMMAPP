package com.health.index.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.index.R;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.TopBar;

@Route(path = IndexRoutes.INDEX_TOOLS_TEST_SEX_MAIN)
public class ToolsTestSexMainActivity extends BaseActivity implements IsNeedShare {

    private com.healthy.library.widget.TopBar topBar;
    private android.widget.ImageView toolsBgUmg;
    private android.widget.LinearLayout qinggongsex;
    private android.widget.LinearLayout taidongsex;
    private android.widget.LinearLayout duzisex;
    private android.widget.LinearLayout bianhuasex;

    private String surl;
    private String sdes;
    private String stitle;
    private Bitmap sBitmap;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_test_sex_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                showShare();
            }
        });

    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }
    public void passToQ(View view){
        ARouter.getInstance()
                .build(IndexRoutes.INDEX_TOOLS_TEST_SEX_EXL)
                .navigation();
    }
    public void passToT(View view){
        ARouter.getInstance()
                .build(IndexRoutes.INDEX_TOOLS_TEST_SEX)
                .withString("vcType","2")
                .navigation();
    }
    public void passToD(View view){
        ARouter.getInstance()
                .build(IndexRoutes.INDEX_TOOLS_TEST_SEX)
                .withString("vcType","3")
                .navigation();
    }
    public void passToB(View view){
        ARouter.getInstance()
                .build(IndexRoutes.INDEX_TOOLS_TEST_SEX)
                .withString("vcType","4")
                .navigation();
    }

    private void initView() {

        topBar = (TopBar) findViewById(R.id.top_bar);
        toolsBgUmg = (ImageView) findViewById(R.id.tools_bg_umg);
        qinggongsex = (LinearLayout) findViewById(R.id.qinggongsex);
        taidongsex = (LinearLayout) findViewById(R.id.taidongsex);
        duzisex = (LinearLayout) findViewById(R.id.duzisex);
        bianhuasex = (LinearLayout) findViewById(R.id.bianhuasex);
    }


    @Override
    public String getSurl() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_babyNameUrl);
        String url = String.format("%s?type=1", urlPrefix);
        surl = url;
        return surl;
    }

    @Override
    public String getSdes() {
        return "4种方法预测宝宝性别，80%的宝妈都说准，你也来试试吧...";
    }

    @Override
    public String getStitle() {
        return "生男生女预测神器";
    }

    @Override
    public Bitmap getsBitmap() {
        return BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.index_share_humb_snsn));
    }
}
