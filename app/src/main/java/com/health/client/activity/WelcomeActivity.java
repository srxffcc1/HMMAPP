package com.health.client.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.health.client.ClientApp;
import com.health.client.R;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseNoTitleActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.DownLoadFragment;
import com.healthy.library.business.MessageDialog;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.AdContract;
import com.healthy.library.contract.ChannelContract;
import com.healthy.library.contract.DataStatisticsContract;
import com.healthy.library.dialog.YSXY1Dialog;
import com.healthy.library.dialog.YSXY2Dialog;
import com.healthy.library.interfaces.IsNoNeedPatchShow;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.UpdateVersion;
import com.healthy.library.model.UserInfoMonModel;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.presenter.AdPresenterTop;
import com.healthy.library.presenter.ChannelPresenter;
import com.healthy.library.presenter.DataStatisticsPresenter;
import com.healthy.library.presenter.DeleteJiGuangPresenter;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.NewStatusBarUtil;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpUtilsOld;
import com.umeng.commonsdk.UMConfigure;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import cn.net.shoot.sharetracesdk.AppData;
import cn.net.shoot.sharetracesdk.ShareTrace;
import cn.net.shoot.sharetracesdk.ShareTraceInstallListener;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @author Li
 * @date 2019/03/15 13:15
 * @des 欢迎页
 */

public class WelcomeActivity extends BaseNoTitleActivity implements AMapLocationListener, AdContract.View , IsNoNeedPatchShow, ChannelContract.View, DataStatisticsContract.View {
    private int flag = 0;
    private boolean isfinish = false;
    private Disposable mDisposable;
    private String[] mPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    private String cityNo;
    private String provinceNo;
    private String areaNo;
    private double longitude;
    private double latitude;
    private String locCityName;
    private String areaName;
    private String newCityName;
    private int RC_PROVINCE_CITY = 114697;
    private int reLocTime = 0;


    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;

    private ImageView ad_Img, ic_welcome;
    private TextView ad_timer;
    private Integer ms = 4;
    private CountDownTimer textTimer;
    private boolean isAD = true;//是否展示广告
    AdPresenterTop adPresenter;
    DataStatisticsPresenter dataStatisticsPresenter;
    private YSXY1Dialog ysxy1Dialog;
    private YSXY2Dialog ysxy2Dialog;
    ChannelPresenter channelPresenter;
    private void buildShareTraceTest() {
        ShareTrace.getInstallTrace(new ShareTraceInstallListener() {
            @Override
            public void onInstall(AppData data) {//这里说明获取到分享下载的导购码
                if (data != null) {
                    String result = data.toString();
                    if (result == null) {
                        return;
                    }
                    if (result.contains("paramsData") && result.contains("referral_code")) {//这里说明获取到分享下载的导购码格式正确
                        Map<String, String> map = new HashMap<>();
                        String[] resultarray = data.getParamsData().split("&");
                        for (int i = 0; i < resultarray.length; i++) {
                            map.put(resultarray[i].split("=")[0], resultarray[i].split("=").length > 1 ? resultarray[i].split("=")[1] : "");
                        }
                        String code = map.get("referral_code");
                        if(!ChannelUtil.isRealRelease()){
                            Toast.makeText(LibApplication.getAppContext(),"获得了referral_code:"+code,Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (result.contains("paramsData") && result.contains("keyFrame")) {//这里说明有一个关键帧 出现一个安装意图

                        Map<String, String> map = new HashMap<>();
                        String[] resultarray = data.getParamsData().split("&");
                        for (int i = 0; i < resultarray.length; i++) {
                            map.put(resultarray[i].split("=")[0], resultarray[i].split("=").length > 1 ? resultarray[i].split("=")[1] : "");
                        }
                        String code = map.get("keyFrame");
                        if (!ChannelUtil.isRealRelease()) {
                            Toast.makeText(LibApplication.getAppContext(), "获得了keyFrame:" + code, Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }

            @Override
            public void onError(int code, String msg) {
                Log.e("ShareTrace", "Get install trace info error. code=" + code + ",msg=" + msg);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
//        EventBus.getDefault().register(this);
        channelPresenter=new ChannelPresenter(this,this);
        SpUtils.store(LibApplication.getAppContext(), SpKey.USE_DOWN, 0);
        SpUtils.store(LibApplication.getAppContext(), SpKey.LIVETMPCOURSEADDRESS, "");
        SpUtils.store(LibApplication.getAppContext(), SpKey.LIVETMPCOURSEID, "");
        NewStatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        NewStatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!NewStatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            NewStatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_welcome);

        adPresenter = new AdPresenterTop(this, this);
        dataStatisticsPresenter = new DataStatisticsPresenter(this, this);
        initView();

        if (!SpUtilsOld.getValue(this, SpKey.YSLOOK, false)) {
            showXY1();
        } else {
            startCountDown(false);
        }



    }



    //    public void initSDk(){
//        UMConfigure.init(LibApplication.getAppContext(), Ids.UMENG_APP_KEY,
//                ChannelUtil.getChannel(LibApplication.getAppContext()),UMConfigure.DEVICE_TYPE_PHONE,"");
//        Notification mNotification = XmNotificationCreater.getInstanse(getApplication()).initNotification(getApplication(), MainActivity.class);
//        XmPlayerManager.getInstance(getApplication()).init((int) System.currentTimeMillis(),mNotification);
//
//        OneKeyLoginManager.getInstance().setDebug(BuildConfig.DEBUG);
//        initShanyanSDK(getApplication());
//
//        ShareTrace.init(getApplication());
//
//        JPushInterface.setDebugMode(BuildConfig.DEBUG);
//        JPushInterface.init(getApplication());
//        initializeBugly();
//        TXLiveBase.getInstance().setLicence(getApplication(), Ids.TENCENT_LIVE_LICENSE_URL, Ids.TENCENT_LIVE_LICENSE_KEY);
//        OneKeyLoginManager.getInstance().getPhoneInfo(new GetPhoneInfoListener() {
//
//            @Override
//            public void getPhoneInfoStatus(int i, String s) {
//
//            }
//        });
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        ic_welcome = findViewById(R.id.ic_welcome);
        ad_Img = findViewById(R.id.ad_image);
        ad_timer = findViewById(R.id.ad_timer);
        ad_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAD) {
                    textTimer.cancel();
                    ARouter.getInstance().build(AppRoutes.APP_MAIN)
                            .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK)
                            .navigation();
                }
            }
        });
    }

    private void adCountDown() {
        if (TextUtils.isEmpty(SpUtils.getValue(this, SpKey.USER_Wel_AD_Bean))) {
            //System.out.println("没有开屏信息");
            routePass();
        } else {
            ad_Img.setVisibility(View.VISIBLE);
            ad_timer.setVisibility(View.VISIBLE);
            System.out.println("首页加载的图"+new Gson().fromJson(SpUtils.getValue(WelcomeActivity.this, SpKey.USER_Wel_AD_Bean), AdModel.class).photoUrls);
            com.healthy.library.businessutil.GlideCopy.with(this)
                    .load(new Gson().fromJson(SpUtils.getValue(WelcomeActivity.this, SpKey.USER_Wel_AD_Bean), AdModel.class).photoUrls)

                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            //System.out.println("广告载入失败直接跳转");
                            routePass();
                        }

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                            ic_welcome.setVisibility(View.GONE);
                            com.healthy.library.businessutil.GlideCopy.with(ad_Img).load(resource).into(ad_Img);
                            showAdTimer();

                        }
                    });
            final AdModel adModel = new Gson().fromJson(SpUtils.getValue(WelcomeActivity.this, SpKey.USER_Wel_AD_Bean), AdModel.class);
            String passString = adModel.getLink();
            ad_Img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    adPresenter.posAd(new SimpleHashMapBuilder<String, Object>().puts("id", adModel.id + ""));
                    dataStatisticsPresenter.bannerClickNum(new SimpleHashMapBuilder<>().puts("advertisingId",adModel.id));
                    ARouter.getInstance().build(AppRoutes.APP_MAIN)
                            .withString("passPath",adModel.getLink())
                            .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK)
                            .navigation();

                }
            });

        }

    }

    private void showAdTimer() {
        ad_timer.getBackground().setAlpha(200);//跳过按钮透明度
        if (textTimer != null) {
            textTimer.cancel();
        }

        if (ms == 0) {

            routePass();
        } else {
            textTimer = new CountDownTimer(ms * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) { //第一个参数为总计时，第二个参数为计时速度
                    ms -= 1;
                    ad_timer.setText("跳过 " + ms + "s");
                }

                @Override
                public void onFinish() {
                    //跳转activity
                    routePass();
                }
            }.start();
        }
    }

    private void startCountDown(boolean needpermession) {
//        initSDk();
        GlideCopy.with(this)
                .load("https://hmm-public.oss-cn-beijing.aliyuncs.com/back-config/banner/20210711/sum_left_banner_one.png")

                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                    }
                });
        GlideCopy.with(this)
                .load("https://hmm-public.oss-cn-beijing.aliyuncs.com/back-config/banner/20210711/sum_left_banner_two.png")

                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                    }
                });
        GlideCopy.with(this)
                .load("https://hmm-public.oss-cn-beijing.aliyuncs.com/back-config/banner/20210711/sum_left_banner_three.png")

                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                    }
                });
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "mmvideo");
            if (!dir.exists()) {
                dir.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "mmtmppic");
            if (!dir.exists()) {
                dir.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        SpUtils.store(WelcomeActivity.this, SpKey.YSLOOK, true);
        SpUtilsOld.store(WelcomeActivity.this, SpKey.YSLOOK, true);

        buildShareTraceTest();
        //System.out.println("开始跳转");

        AutoDisposeConverter<Long> objectAutoDisposeConverter = AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
        Observable.intervalRange(0, 1, 0, 0, TimeUnit.SECONDS)
                .compose(RxThreadUtils.<Long>Obs_io_main())
                .to(objectAutoDisposeConverter)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //System.out.println("出错了");
                    }

                    @Override
                    public void onComplete() {
                        flag = 2;
                        isfinish = true;
                        if (needpermession) {
                            if (PermissionUtils.hasPermissionsIgnore(WelcomeActivity.this, mPermissions)) {
                                //System.out.println("申请权限");
                                route();
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    //System.out.println("申请权限");
                                    requestPermissions(mPermissions, RC_LOCATION);
                                }
                            }
                        } else {
                            route();
                        }

                    }
                });
    }

    private int RC_LOCATION = 11024;

    @Override
    public void onBackPressed() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        super.onBackPressed();
//        if (PermissionUtils.hasPermissions(WelcomeActivity.this, mPermissions)) {
//            locate();
//            route();
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(mPermissions, RC_LOCATION);
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag == 0) {//说明进行了第一次进来

        } else {//说明存在一次退出
            if (!isfinish && flag != 2) {//说明选项也没完成
                if (PermissionUtils.hasPermissions(WelcomeActivity.this, mPermissions)) {
                    locate();
                    route();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //System.out.println("申请权限");
                        try {
                            requestPermissions(mPermissions, RC_LOCATION);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                flag = 3;
            }

        }
    }

    /**
     * 定位
     */
    private void locate() {
//        successLocation();
//        if (LocUtil.getLocationBean(WelcomeActivity.this, SpKey.LOC_CHOSE) != null) {
//            successLocation();
//        } else {
//            mLocationClient = new AMapLocationClient(WelcomeActivity.this);
//            mLocationOption = new AMapLocationClientOption();
//            mLocationOption.setOnceLocation(true);
//            //设置定位监听
//            mLocationClient.setLocationListener(this);
//            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            mLocationClient.setLocationOption(mLocationOption);
//            mLocationClient.startLocation();
//        }

    }

    private void successLocation() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_LOCATION) {
            if (PermissionUtils.hasPermissionsIgnore(this, mPermissions)) {
                locate();
                route();
            } else {
                //System.out.println("申请权限2");
//                if (PermissionUtils.somePermissionPermanentlyDenied(this, mPermissions)) {
//                    PermissionUtils.showRationale(this);
//                } else {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        requestPermissions(mPermissions, RC_LOCATION);
//                    }
//                }
                locate();
                route();
            }
        }
    }
    private void routePassReal() {
        if (isFinishing()) {
            return;
        }
        //先加载点资源
        new DeleteJiGuangPresenter(LibApplication.getAppContext()).deleteJiGuang();
        String id = SpUtils.getValue(WelcomeActivity.this, SpKey.USER_ID);
        System.out.println("登录检查Id" + id);
        String status = SpUtils.getValue(WelcomeActivity.this, SpKey.STATUS);
        String birthday = SpUtils.getValue(WelcomeActivity.this, SpKey.USER_BIRTHDAY);
        String phone = SpUtils.getValue(WelcomeActivity.this, SpKey.PHONE);
        int phoneed = 0;
        if (!TextUtils.isEmpty(phone)) {

            phoneed = Integer.parseInt(phone.substring(phone.length() - 4, phone.length()));
            //System.out.println("注册手机尾号：" + phoneed);
        }
//        new DeleteJiGuangPresenter(WelcomeActivity.this).deleteJiGuangOnly();
        boolean showGuide = SpUtils
                .getValue(WelcomeActivity.this, SpKey.SHOW_GUIDE, true);
        String route;
        //System.out.println("是否需要显示引导：" + showGuide);
//        if (showGuide) {
//            route = AppRoutes.APP_GUIDE;
//        } else {
        if (TextUtils.isEmpty(id)) {

            route = AppRoutes.APP_LOGINTRANSFER;
            if (!PermissionUtils.hasPermissionsIgnore(WelcomeActivity.this, mPermissions)) {
                route = AppRoutes.APP_LOGIN;
            }
            if(SpUtils.getValue(WelcomeActivity.this,SpKey.SHOW_JUST_LOGIN,false)){
                route = AppRoutes.APP_LOGIN;
            }
            if(PermissionUtils.hasPermissions(WelcomeActivity.this, Manifest.permission.READ_PHONE_STATE)){
                route = AppRoutes.APP_LOGINTRANSFER;
            }
        } else if (TextUtils.isEmpty(birthday)&&!SpUtils.getValue(LibApplication.getAppContext(), SpKey.AUDITSTATUS, true)) {
            route = MineRoutes.MINE_UPDATE_USER_INFO;
        } else if (TextUtils.isEmpty(status) || Constants.STATUS_NONE.equals(status)) {
            //System.out.println("注册别名:" + new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            route = AppRoutes.APP_CHOOSE_SEX;
        } else {
            //System.out.println("注册别名:" + new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
//
            route = AppRoutes.APP_MAIN;
        }
        ARouter.getInstance().build(route)
                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation();
    }
    private void routePass() {
        channelPresenter.getIsAuditing(new SimpleHashMapBuilder<>());
    }

    private void route() {
//        routePass();
        if(!TextUtils.isEmpty(SpUtils.getValue(this, SpKey.TOKEN))){
            channelPresenter.getMine();
        }
        try {
            adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "3").puts("triggerPage", "8").puts("advertisingArea", LocUtil.getAreaNo(this, SpKey.LOC_ORG)));
            adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "1").puts("triggerPage", "1").puts("advertisingArea", LocUtil.getAreaNo(this, SpKey.LOC_ORG)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        adCountDown();
//        //System.out.println("测试强更问题");
//        if (Beta.getUpgradeInfo() != null) {
//            //System.out.println("获得线上最新版");
//            if (Beta.getUpgradeInfo().versionCode > BuildConfig.VERSION_CODE) {
//                //System.out.println("获得线上最新版2");
//                SpUtils.store(this,SpKey.UPDATE_TMP,true);
//                Beta.checkUpgrade(true, false);
//            } else {
//                //System.out.println("获得线上最新版3");
//                routePass();
//            }
//        } else {
//            if(SpUtils.getValue(this,SpKey.UPDATE_TMP,false)){
//                //System.out.println("检查线上最新版");
//                if (Beta.getUpgradeInfo() != null&&Beta.getUpgradeInfo().versionCode > BuildConfig.VERSION_CODE) {
//
//                    Beta.checkUpgrade(true, false);
//                }else {
//
//                    routePass();
//                }
//            }else {
//                routePass();
//            }
//        }


    }

    public synchronized void showXY2() {
        if(ChannelUtil.getChannel(this).contains("huawei")){
            if (ysxy2Dialog != null) {
                ysxy2Dialog.dismiss();
            }
            ysxy2Dialog = YSXY2Dialog.newInstance();
            ysxy2Dialog.setOnConfirmClick(new YSXY2Dialog.OnSelectConfirm() {
                @Override
                public void onClick(int result) {
                    if (result == 1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                startCountDown(true);
                            }
                        }, 300);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                startCountDown(false);
                            }
                        }, 300);
                    }
                }
            });
            ysxy2Dialog.show(getSupportFragmentManager(), "");
        }else {

            startCountDown(false);
        }

    }

    public void showXY1() {
        if (ysxy1Dialog != null) {
            ysxy1Dialog.dismiss();
        }
        ysxy1Dialog = YSXY1Dialog.newInstance();
        ysxy1Dialog.setOnConfirmClick(new YSXY1Dialog.OnSelectConfirm() {
            @Override
            public void onClick(int result) {
                if (result == 1) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SpUtils.store(WelcomeActivity.this, SpKey.YSLOOKAGREE, true);
                            SpUtilsOld.store(WelcomeActivity.this, SpKey.YSLOOKAGREE, true);
                            UMConfigure.submitPolicyGrantResult(getApplicationContext(), true);
                            ClientApp.initSDk(getApplication());
                            showXY2();
                        }
                    }, 300);
                } else {
                    finish();
                }
            }
        });
        ysxy1Dialog.show(getSupportFragmentManager(), "");
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation.getErrorCode() == 0) {

            LocUtil.storeLocation(WelcomeActivity.this, aMapLocation);
            LocUtil.storeLocationChose(WelcomeActivity.this, aMapLocation);
            EventBus.getDefault().post(new UpdateUserLocationMsg());
            successLocation();
        } else {
            if (NavigateUtils.openGPSSettings(WelcomeActivity.this) && reLocTime <= 1) {
                mLocationClient.startLocation();
                reLocTime++;
            } else {

                MessageDialog.newInstance()
                        .setMessageTopRes(R.drawable.dialog_message_icon_loc, "开启定位", "为给您提供更好的本地化服务，请您到系统设置中打开GPS定位")
                        .setMessageOkClickListener(new MessageDialog.MessageOkClickListener() {
                            @Override
                            public void onMessageOkClick(View view) {
                                IntentUtils.toLocationSetting(WelcomeActivity.this);
                            }
                        })
                        .show(getSupportFragmentManager(), "打开定位");

            }
        }
    }

    @Override
    public void onSucessGetAds(List<AdModel> adModels) {
        if (adModels.size() > 0) {
            AdModel adModel = adModels.get(0);
            if (adModel.type == 3 && adModel.triggerPage == 8) {
                if("-1".equals(adModel.id)){

                    SpUtils.store(this, SpKey.USER_Wel_AD_Bean, null);
                }else {

                    SpUtils.store(this, SpKey.USER_Wel_AD_Bean, new Gson().toJson(adModel));
                }
                //System.out.println("保存开屏信息");
            }
            if (adModel.type == 1 && adModel.triggerPage == 1) {
                if("-1".equals(adModel.id)){

                    SpUtils.store(this, SpKey.USER_Main_AD_Bean, null);
                }else {

                    SpUtils.store(this, SpKey.USER_Main_AD_Bean, new Gson().toJson(adModel));
                }
                //System.out.println("保存开屏信息");
            }

        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showToast(CharSequence msg) {

    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onRequestStart(Disposable disposable) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void getData() {

    }

    @Override
    public void showDataErr() {

    }

    @Override
    public void getSucessIsAuditing() {
        if(SpUtils.getValue(this,SpKey.AUDITSTATUS,false)){//true 为审核中 fasle为通过
            routePassReal();//直接跳转
        }else {//检查下版本再跳转
            channelPresenter.checkVersion(new SimpleHashMapBuilder<>().puts("platform", "1").puts(Functions.FUNCTION, "6000"));
        }
    }

    @Override
    public void onSucessCheckVersion(UpdateVersion result) {
        if (result != null) {
            //System.out.println("需要更新2" + result.version);
            SpUtils.store(this, SpKey.USE_UPDATE, new Gson().toJson(result));
            if (result.getVersionCode() > com.health.client.BuildConfig.VERSION_CODE) {
                //System.out.println("需要更新");
                long mDownloadId = SpUtils.getValue(this, SpKey.USE_DOWN, 0L);
                boolean isTodayShow = SpUtils.getValue(this, new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "推荐", false);
                if (result.forceUpate == 1) {//如果是强制 则每次都弹出 只判断是不是在下载中
                    DownLoadFragment.newInstance(new Gson().toJson(result)).setOnDownLoadCloseClickListener(new DownLoadFragment.OnDownLoadCloseClickListener() {
                        @Override
                        public void onCloseClick() {
                            routePassReal();//直接跳转
                            SpUtils.store(LibApplication.getAppContext(),SpKey.VERSION_CHECK_FLAG,true);
                        }
                    }).show(getSupportFragmentManager(), "升级弹窗");
                } else {//非强制判断下今日弹出次数
                    if (!isTodayShow) {
                        DownLoadFragment.newInstance(new Gson().toJson(result)).setOnDownLoadCloseClickListener(new DownLoadFragment.OnDownLoadCloseClickListener() {
                            @Override
                            public void onCloseClick() {
                                routePassReal();//直接跳转
                                SpUtils.store(LibApplication.getAppContext(),SpKey.VERSION_CHECK_FLAG,true);
                            }
                        }).show(getSupportFragmentManager(), "升级弹窗");
                    }else {
                        SpUtils.store(LibApplication.getAppContext(),SpKey.VERSION_CHECK_FLAG,true);
                        routePassReal();//直接跳转
                    }
                }
            }else {
                SpUtils.store(LibApplication.getAppContext(),SpKey.VERSION_CHECK_FLAG,true);
                routePassReal();//直接跳转
            }
        }else {
            SpUtils.store(LibApplication.getAppContext(),SpKey.VERSION_CHECK_FLAG,true);
            routePassReal();//直接跳转
        }
    }

    @Override
    public void getMineSuccess(UserInfoMonModel userInfoMonModel) {
        if(userInfoMonModel!=null){
            SpUtils.store(this, SpKey.USER_NICK, userInfoMonModel.nickName);
            SpUtils.store(this, SpKey.USER_ICON, userInfoMonModel.faceUrl);
            SpUtils.store(this, SpKey.USER_BIRTHDAY, userInfoMonModel.birthday);
            SpUtils.store(this, SpKey.STATUS_USER, userInfoMonModel.status);
        }
    }
}
