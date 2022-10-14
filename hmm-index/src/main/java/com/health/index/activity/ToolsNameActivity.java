package com.health.index.activity;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.index.R;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.ResUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.TopBar;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.HashMap;
import java.util.Map;

@Route(path = IndexRoutes.INDEX_TOOLS_NAME)
public class ToolsNameActivity extends BaseActivity implements IsNeedShare,IsFitsSystemWindows  {
    private com.healthy.library.widget.TopBar topBar;
    private android.widget.ImageView toolsBgUmg;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_name;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();

    }

    @Override
    protected void init(Bundle savedInstanceState) {


        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                buildDes();
                showShare();
            }
        });
    }
    public void passTOA(View view){
        ARouter.getInstance()
                .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                .withString("title", "易其名大师团")
                .withBoolean("isinhome",false)
                .withBoolean("doctorshop",true)
                .withString("url", "http://zx.dcfdsg.cn/dashiqinsuan/index?channel=swhmm000&schannel=syzhouyiqiming&type=zhouyiqiming")
                .navigation();
    }
    public void passTOB(View view){
        ARouter.getInstance()
                .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                .withString("title", "八字起名")
                .withBoolean("isinhome",false)
                .withBoolean("doctorshop",true)
                .withString("url", "http://zx.caijiexinxi.cn/baobaoqiming/index?channel=swhmm000")
                .navigation();
    }
    public void passTOC(View view){
        ARouter.getInstance()
                .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                .withString("title", "科学起名")
                .withBoolean("isinhome",false)
                .withBoolean("doctorshop",true)
                .withString("url", "http://zx.caijiexinxi.cn/liyilinqiming/index?channel=swhmm000")
                .navigation();
    }
    public void passTOD(View view){
        ARouter.getInstance()
                .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                .withString("title", "周易起名")
                .withBoolean("isinhome",false)
                .withBoolean("doctorshop",true)
                .withString("url", "http://zx.caijiexinxi.cn/zhouyiqiming/index?channel=swhmm000")
                .navigation();
    }
    public void passTOE(View view){
        ARouter.getInstance()
                .build(IndexRoutes.INDEX_WEBVIEW_ALL)
                .withString("title", "大师起名")
                .withBoolean("isinhome",false)
                .withBoolean("doctorshop",true)
                .withString("url", "http://zx.dcfdsg.cn/dashiqinsuan/index?channel=swhmm000&schannel=syzhouyiqiming&type=zhouyiqiming")
                .navigation();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        toolsBgUmg = (ImageView) findViewById(R.id.tools_bg_umg);
    }
    String surl;
    String sdes;
    String stitle;

    private void buildDes() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_babyNameUrl);
        String url = String.format("%s", urlPrefix);
        surl=url;
        stitle="宝宝起名超好用,快来试试吧!";

        sdes="取名不再烦恼,好名字一生受益,快来给宝宝取个好名字吧!";

    }

    @Override
    public String getSurl() {
        return surl;
    }

    @Override
    public String getSdes() {
        return sdes;
    }

    @Override
    public String getStitle() {
        return stitle;
    }

    @Override
    public Bitmap getsBitmap() {
        return null;
    }
}
