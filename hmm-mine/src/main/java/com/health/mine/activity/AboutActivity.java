package com.health.mine.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.health.mine.BuildConfig;
import com.health.mine.R;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.contract.AppVersionContract;
import com.healthy.library.message.UpdatePatchHasMsg;
import com.healthy.library.model.UpdateVersion;
import com.healthy.library.presenter.AppVersionPresenter;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpUtilsOld;
import com.healthy.library.widget.SectionView;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.util.Calendar;

@Route(path = MineRoutes.MINE_SETTINGSABOUT)
public class AboutActivity extends BaseActivity implements AppVersionContract.View {
//master3289
    TextView tv_version;
    private com.healthy.library.widget.SectionView sectionAbout5;
    private TextView tvCopyright;
    AppVersionPresenter appVersionPresenter;
    private SectionView sectionVersion;
    private android.widget.ImageView hasUpdateFlag;
    private ImageView ivLogo;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_about;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
        tv_version=findViewById(R.id.tv_version);
    }


    long[] mHits = new long[3];

    @Override
    protected void init(Bundle savedInstanceState) {
        tv_version.setText(String.format("V %s", BuildConfig.VERSION_NAME));
        if(SpUtilsOld.getValue(LibApplication.getAppContext(), SpKey.YSLOOK, false)){
            try {
                Tinker tinker = Tinker.with(getApplicationContext());
                String tinkerId=tinker.getTinkerLoadResultIfPresent().getPackageConfigByName(ShareConstants.NEW_TINKER_ID);
                if(!TextUtils.isEmpty(tinkerId)){
                    tv_version.setText(String.format("V %s", BuildConfig.VERSION_NAME)+"\n"+tinkerId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"????????????:"+ ChannelUtil.getChannel(null)+"-"+ChannelUtil.getChannel(mContext),Toast.LENGTH_SHORT).show();
            }
        });
        tv_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                //????????????????????????
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                //???????????????????????????500??????????????????
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    SpUtils.store(mContext, SpKey.OPERATIONSTATUS,!SpUtils.getValue(mContext, SpKey.OPERATIONSTATUS,false));
                    if(SpUtils.getValue(mContext, SpKey.OPERATIONSTATUS,false)){
                        Toast.makeText(mContext,"??????????????????????????????",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mContext,"??????????????????????????????",Toast.LENGTH_SHORT).show();
                    }
                    mHits = null;
                    mHits = new long[3];
                }
            }
        });
        tvCopyright.setText(String.format("???ICP???19042355??? Tel:0512-68152717\n" +
                        "Copyright ?? 2019-2020 HanMama Network Technology",
                String.valueOf(Calendar.getInstance().get(Calendar.YEAR))));
        appVersionPresenter=new AppVersionPresenter(this,this);
        hasUpdateFlag.setVisibility(View.GONE);
        appVersionPresenter.checkVersionOnly(new SimpleHashMapBuilder<>().puts("platform","1").puts(Functions.FUNCTION, "6000"));
    }

    public void checkVersionClick(View view){
        appVersionPresenter.checkVersion(new SimpleHashMapBuilder<>().puts("platform","1").puts(Functions.FUNCTION, "6000"));
        shouldCheckUpdatePatch(new UpdatePatchHasMsg());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isShowFloat){
            startOnlineVideoFloat();
        }
    }

    public void passHMMXY(View view) {
        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                .withString("url", UrlKeys.URL_PROTOCOL)
                .withString("title", "")
                .navigation();
//        WebDialog.newInstance().setUrl(UrlKeys.URL_PROTOCOL).setIsinhome(true).show(getSupportFragmentManager(),"webdialog");
    }

    public void passHMMXXFB(View view) {
        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                .withString("url", UrlKeys.URL_TCHMYHGLGF)
                .withString("title", "")
                .navigation();
    }

    public void passHMMYSCL(View view) {
        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                .withString("url", UrlKeys.URL_HMMPTYSZC)
                .withString("title", "")
                .navigation();
    }

    public void passHMMZXYSCL(View view) {
        ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
                .withString("url", UrlKeys.URL_ZXFWYSZC)
                .withString("title", "")
                .navigation();
    }
    public void passCall(View view){
        IntentUtils.dial(mContext,"051268152717");
    }

    private void initView() {
        sectionAbout5 = (SectionView) findViewById(R.id.section_about5);
        tvCopyright = (TextView) findViewById(R.id.tv_copyright);
        sectionVersion = (SectionView) findViewById(R.id.section_version);
        hasUpdateFlag = (ImageView) findViewById(R.id.hasUpdateFlag);
        ivLogo = (ImageView) findViewById(R.id.iv_logo);
    }

    @Override
    public void onSucessCheckVersion(UpdateVersion result) {
        if(result!=null){
            ////System.out.println("????????????2"+result.version);
            SpUtils.store(mContext, SpKey.USE_UPDATE,new Gson().toJson(result));
            if(result.getVersionCode()> BuildConfig.VERSION_CODE){
                ////System.out.println("????????????");
                Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(mContext, "????????????????????????Android????????????!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            Toast.makeText(mContext,"??????????????????",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSucessCheckVersionOnly(UpdateVersion result) {
        hasUpdateFlag.setVisibility(View.GONE);
        if(result!=null){
            ////System.out.println("????????????2"+result.version);
            SpUtils.store(mContext, SpKey.USE_UPDATE,new Gson().toJson(result));
            if(result.getVersionCode()> BuildConfig.VERSION_CODE){
                ////System.out.println("????????????");
                hasUpdateFlag.setVisibility(View.VISIBLE);
                return;
            }
        }

    }
}
