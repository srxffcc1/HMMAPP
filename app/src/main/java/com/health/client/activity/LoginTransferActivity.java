package com.health.client.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.OneKeyLoginListener;
import com.chuanglan.shanyan_sdk.listener.OpenLoginAuthListener;
import com.chuanglan.shanyan_sdk.view.CmccLoginActivity;
import com.chuanglan.shanyan_sdk.view.ShanYanOneKeyActivity;
import com.google.gson.Gson;
import com.health.client.R;
import com.health.client.contract.LoginContract;
import com.health.client.model.UserInfoModel;
import com.health.client.presenter.LoginPresenter;
import com.health.client.shanyanutils.AbScreenUtils;
import com.health.client.shanyanutils.ConfigUtils;
import com.health.client.shanyanutils.ForebackUtils;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.AdContract;
import com.healthy.library.interfaces.IsNoNeedPatchShow;
import com.healthy.library.model.AdModel;
import com.healthy.library.presenter.AdPresenter;
import com.healthy.library.presenter.ChannelPresenterCopy;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.NewStatusBarUtil;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

@Route(path = AppRoutes.APP_LOGINTRANSFER)
public class LoginTransferActivity extends BaseActivity implements LoginContract.View, AdContract.View, IsNoNeedPatchShow {
    private int MY_READ_PHONE_STATE = 0;
    private LoginPresenter mLoginPresenter;
    private AdPresenter adPresenter;
    private String[] mPermissions = new String[]{Manifest.permission.READ_PHONE_STATE};
    private StatusLayout layoutStatus;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

//    public void showKillDialog(){
//        StyledDialog.init(mContext);
//        StyledDialog.buildIosAlert("?????????", "???????????????????????????,???????????????????", new MyDialogListener() {
//            @Override
//            public void onFirst() {
//
//            }
//            @Override
//            public void onThird() {
//                super.onThird();
//            }
//            @Override
//            public void onSecond() {
//                FrameActivityManager.instance().appExit(LibApplication.getAppContext());
//                System.exit(0);
//            }
//        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark,0).setBtnText("??????", "??????").show();
//    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void killMine(UpdateKillMsg msg) {
//        showKillDialog();
//    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_transfer;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mLoginPresenter = new LoginPresenter(this, this);
        adPresenter = new AdPresenter(this, this);
        showLoading();
        if (PermissionUtils.hasPermissions(this, mPermissions)) {
            OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getCJSConfig(mContext), null);
            openLoginActivity();
        } else {
            SpUtils.store(mContext,SpKey.SHOW_JUST_LOGIN,true);
            StyledDialog.init(mContext);
            StyledDialog.buildIosAlert("?????????????????????", "???????????????????????????????????????????????????\n????????????????????????????????????????????????????????????????????????", new MyDialogListener() {
                @Override
                public void onFirst() {

                    requestPermission(Manifest.permission.READ_PHONE_STATE);
                }

                @Override
                public void onThird() {
                    super.onThird();
                    SpUtils.store(mContext,SpKey.SHOW_JUST_LOGIN,true);
                    finish();
                    ARouter.getInstance().build(AppRoutes.APP_LOGIN)
                            .withString("shanyanType", "1")
                            .navigation();
                }

                @Override
                public void onSecond() {
                    finish();
                    ARouter.getInstance().build(AppRoutes.APP_LOGIN)
                            .withString("shanyanType", "1")
                            .navigation();
                }
            }).setCancelable(true,true).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark,R.color.colorPrimaryDark)
                    .setBtnText("??????", "??????","??????????????????").show();
        }
        NewStatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //?????????????????????
        NewStatusBarUtil.setTranslucentStatus(this);
        //?????????????????????????????????????????????????????????, ???????????????????????????????????????, ?????????????????????????????????
        //??????????????????????????????,?????????????????????, ??????????????????????????????????????????, ???????????????????????????????????????if??????
        if (!NewStatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //????????????????????????????????? ???????????????????????????????????????????????????, ?????????????????????????????????????????????,
            //???????????????+???=???, ??????????????????????????????
            NewStatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
        ForebackUtils.init((Application) getApplicationContext());
        ForebackUtils.unregisterListener(listener);
        ForebackUtils.registerListener(listener);
        AbScreenUtils.hideBottomUIMenu(this);
//        mRecyclerView = findViewById(R.id.mRecyclerView);
//        mRecyclerView.setAdapter(new SplashAdapter(LoginTransferActivity.this));
//        mRecyclerView.setLayoutManager(new ScollLinearLayoutManager(LoginTransferActivity.this));
//
//        //smoothScrollToPosition??????????????????????????????????????????
//        mRecyclerView.smoothScrollToPosition(Integer.MAX_VALUE / 2);
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void openLoginActivity() {
        //??????????????????
        OneKeyLoginManager.getInstance().openLoginAuth(false, new OpenLoginAuthListener() {
            @Override
            public void getOpenLoginAuthStatus(int code, String result) {
                if (1000 == code) {
                    OneKeyLoginManager.getInstance().setCheckBoxValue(false);
                    //?????????????????????
                    Log.e("VVV", "???????????????????????? _code==" + code + "   _result==" + result);
                } else {
//                    CrashReport.postCatchedException(new Throwable("???????????????????????? code==" + code + "   result==" + result));
                    //?????????????????????
                    Log.e("VVV", "???????????????????????? _code==" + code + "   _result==" + result);
                    //AbScreenUtils.showToast(getApplicationContext(), result);
                    finish();
                    ARouter.getInstance().build(AppRoutes.APP_LOGIN)
                            .withString("shanyanType", "1")
                            .navigation();
                }
            }
        }, new OneKeyLoginListener() {
            @Override
            public void getOneKeyLoginStatus(int code, String result) {
                if (1011 == code) {
                    Log.e("VVV", "?????????????????????????????? _code==" + code + "   _result==" + result);
                    finish();
                    return;
                } else if (1000 == code) {
                    Log.e("VVV", "????????????????????????token????????? _code==" + code + "   _result==" + result);
                    OneKeyLoginManager.getInstance().setLoadingVisibility(false);
                    //AbScreenUtils.showToast(getApplicationContext(), "????????????????????????token??????");
                    if (result != null && !TextUtils.isEmpty(result)) {
                        try {
                            JSONObject jsonObject = new JSONObject(result.toString());
                            String token = jsonObject.getString("token");
                            if (token != null && !TextUtils.isEmpty(token)) {
                                new ChannelPresenterCopy(LibApplication.getAppContext()).getIsAuditing(new SimpleHashMapBuilder<>());
                                mLoginPresenter.login(null, null, "1", token);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    CrashReport.postCatchedException(new Throwable("????????????????????????token????????? code==" + code + "   result==" + result));
                    Log.e("VVV", "????????????????????????token????????? _code==" + code + "   _result==" + result);
                    finish();
                    ARouter.getInstance().build(AppRoutes.APP_LOGIN)
                            .withString("shanyanType", "1")
                            .navigation();
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //???????????????????????????????????????????????????????????????
        if (requestCode == MY_READ_PHONE_STATE) {
            if (PermissionUtils.hasPermissions(this, mPermissions)) {
                OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getCJSConfig(mContext), null);
                openLoginActivity();
            } else {
                if(PermissionUtils.somePermissionPermanentlyDenied(this,mPermissions)){//?????????????????? ????????????????????????
                    SpUtils.store(mContext,SpKey.SHOW_JUST_LOGIN,true);

                }
//                Toast.makeText(mContext, "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                ARouter.getInstance().build(AppRoutes.APP_LOGIN)
                        .withString("shanyanType", "1")
                        .navigation();
                finish();

            }
            //??????SDK?????????????????????????????????????????????


//            OneKeyLoginManager.getInstance().getPhoneInfo(new GetPhoneInfoListener() {
//                @Override
//                public void getPhoneInfoStatus(int code, String result) {
//                    //???????????????
//                    Log.e("VVV", "???????????? code==" + code + "   result==" + result);
//                    if (code == 1022) {//code???1022:????????????????????????
//                        //???????????????
//                        OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getCJSConfig(mContext), null);
//                        //????????????
//                        openLoginActivity();
//                    }else{
//                        finish();
//                        CrashReport.postCatchedException(new Throwable("?????????????????? code==" + code + "   result==" + result));
//                        ARouter.getInstance().build(AppRoutes.APP_LOGIN)
//                                .withString("shanyanType","1")
//                                .navigation();
//                    }
//                }
//            });
        }
    }

    private void requestPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if ("OPPO".equals(Build.MANUFACTURER)) {
                startRunnable(permission);
            } else {
                ActivityCompat.requestPermissions(LoginTransferActivity.this, new String[]{permission}, MY_READ_PHONE_STATE);
            }
        } else {
            OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getCJSConfig(mContext), null);
            openLoginActivity();
            //??????SDK?????????????????????????????????????????????
//            OneKeyLoginManager.getInstance().getPhoneInfo(new GetPhoneInfoListener() {
//                @Override
//                public void getPhoneInfoStatus(int code, String result) {
//                    //???????????????
//                    Log.e("VVV", "???????????? code==" + code + "   result==" + result);
//                    if (code == 1022) {//code???1022:????????????????????????
//                        //???????????????
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                startActivity(new Intent(mContext,TmpActivity.class));
//                                try {
//                                    Thread.sleep(400);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getCJSConfig(mContext), null);
//                                        //????????????
//                                        openLoginActivity();
//                                    }
//                                });
//                            }
//                        }).start();
//
//
//                    }else {
//                        finish();
//                        CrashReport.postCatchedException(new Throwable("?????????????????? code==" + code + "   result==" + result));
//                        ARouter.getInstance().build(AppRoutes.APP_LOGIN)
//                                .withString("shanyanType","1")
//                                .navigation();
//                        //OneKeyLoginManager.getInstance().finishAuthActivity();
//                    }
//                }
//            });
        }
    }

    ForebackUtils.Listener listener = new ForebackUtils.Listener() {
        @Override//????????????????????????????????????????????????????????????????????????????????????????????????
        public void onApplicationEnterForeground(Activity activity) {
            if (activity instanceof LoginActivity || activity instanceof ShanYanOneKeyActivity || activity instanceof CmccLoginActivity) {
                AbScreenUtils.hideBottomUIMenu(activity);
            }
        }

        @Override
        public void onApplicationEnterBackground(Activity activity) {
            if (activity instanceof LoginActivity || activity instanceof ShanYanOneKeyActivity || activity instanceof CmccLoginActivity) {
                AbScreenUtils.hideBottomUIMenu(activity);
            }
        }
    };

    private void startRunnable(final String permission) {
        if(isFinishing()){
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????");
        builder.setMessage("????????????????????????????????????");
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(LoginTransferActivity.this, new String[]{permission}, MY_READ_PHONE_STATE);
            }
        });
        builder.setCancelable(false);//?????????????????????????????????
        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//???????????????????????????
        dialog.setCanceledOnTouchOutside(false);//????????????????????????
        builder.show();
    }

    @Override
    public void onGetCodeSuccess() {

    }

    @Override
    public void onGetCodeFail() {

    }

    @Override
    public void onLoginSuccess(UserInfoModel userInfo) {
        String status = SpUtils.getValue(mContext, SpKey.STATUS);
        String birthday = SpUtils.getValue(mContext, SpKey.USER_BIRTHDAY);

        MobclickAgent.onEvent(mContext, "event2LoginClick");
        adPresenter.getAd(new SimpleHashMapBuilder<>());
        try {
            adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "3").puts("triggerPage", "8").puts("advertisingArea", LocUtil.getAreaNo(this, SpKey.LOC_ORG)));
            adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "1").puts("triggerPage", "1").puts("advertisingArea", LocUtil.getAreaNo(this, SpKey.LOC_ORG)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(TextUtils.isEmpty(birthday)){
            registerPush();
            ARouter.getInstance().build(MineRoutes.MINE_UPDATE_USER_INFO)
                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation();
        }else if (Constants.STATUS_NONE.equals(status)) {


            registerPush();
            ARouter.getInstance().build(AppRoutes.APP_CHOOSE_SEX)
                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation();


        } else {

            registerPush();
            ARouter.getInstance().build(AppRoutes.APP_MAIN)
                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation();

        }
    }

    private void registerPush() {
        String id = SpUtils.getValue(this, SpKey.USER_ID);
        String status = SpUtils.getValue(this, SpKey.STATUS);
        String phone = SpUtils.getValue(this, SpKey.PHONE);
        int phoneed = 0;
        if (!TextUtils.isEmpty(phone)) {

            phoneed = Integer.parseInt(phone.substring(phone.length() - 4, phone.length()));
            ////System.out.println("?????????????????????" + phoneed);
        }
        System.out.println("????????????:" + new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        JPushInterface.setAlias(getApplicationContext(),
                phoneed,
                new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
//        JPushInterface.resumePush(getApplication());
    }

    @Override
    public void onLoginFail() {

    }

    @Override
    public void onZxingLoginSuccess(String result) {

    }

    @Override
    public void updatePwdResult(String result) {

    }

    @Override
    public void checkCodeResult(String result) {

    }

    @Override
    public void onSucessGetAds(List<AdModel> adModels) {
        if (adModels.size() > 0) {
            AdModel adModel = adModels.get(0);
            if (adModel.type == 3 && adModel.triggerPage == 8) {
                SpUtils.store(this, SpKey.USER_Wel_AD_Bean, new Gson().toJson(adModel));
                ////System.out.println("??????????????????");
            }
            if (adModel.type == 1 && adModel.triggerPage == 1) {
                SpUtils.store(this, SpKey.USER_Main_AD_Bean, new Gson().toJson(adModel));
                ////System.out.println("??????????????????");
            }
        }
    }

    private void initView() {
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
    }
}