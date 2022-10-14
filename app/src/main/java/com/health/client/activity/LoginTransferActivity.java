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
//        StyledDialog.buildIosAlert("新功能", "已为您安装新的功能,是否立即重启?", new MyDialogListener() {
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
//        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark,0).setBtnText("稍后", "立即").show();
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
            StyledDialog.buildIosAlert("手机号一键登录", "即将使用手机号一键登录（无需密码）\n需要获取手机号，需要请求手机号相关权限是否允许？", new MyDialogListener() {
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
                    .setBtnText("允许", "取消","取消不再提示").show();
        }
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
        ForebackUtils.init((Application) getApplicationContext());
        ForebackUtils.unregisterListener(listener);
        ForebackUtils.registerListener(listener);
        AbScreenUtils.hideBottomUIMenu(this);
//        mRecyclerView = findViewById(R.id.mRecyclerView);
//        mRecyclerView.setAdapter(new SplashAdapter(LoginTransferActivity.this));
//        mRecyclerView.setLayoutManager(new ScollLinearLayoutManager(LoginTransferActivity.this));
//
//        //smoothScrollToPosition滚动到某个位置（有滚动效果）
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
        //拉授权页方法
        OneKeyLoginManager.getInstance().openLoginAuth(false, new OpenLoginAuthListener() {
            @Override
            public void getOpenLoginAuthStatus(int code, String result) {
                if (1000 == code) {
                    OneKeyLoginManager.getInstance().setCheckBoxValue(false);
                    //拉起授权页成功
                    Log.e("VVV", "拉起授权页成功： _code==" + code + "   _result==" + result);
                } else {
//                    CrashReport.postCatchedException(new Throwable("拉起授权页失败： code==" + code + "   result==" + result));
                    //拉起授权页失败
                    Log.e("VVV", "拉起授权页失败： _code==" + code + "   _result==" + result);
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
                    Log.e("VVV", "用户点击授权页返回： _code==" + code + "   _result==" + result);
                    finish();
                    return;
                } else if (1000 == code) {
                    Log.e("VVV", "用户点击登录获取token成功： _code==" + code + "   _result==" + result);
                    OneKeyLoginManager.getInstance().setLoadingVisibility(false);
                    //AbScreenUtils.showToast(getApplicationContext(), "用户点击登录获取token成功");
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
                    CrashReport.postCatchedException(new Throwable("用户点击登录获取token失败： code==" + code + "   result==" + result));
                    Log.e("VVV", "用户点击登录获取token失败： _code==" + code + "   _result==" + result);
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
        //无论权限是否申请成功，均调用闪验预取号方法
        if (requestCode == MY_READ_PHONE_STATE) {
            if (PermissionUtils.hasPermissions(this, mPermissions)) {
                OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getCJSConfig(mContext), null);
                openLoginActivity();
            } else {
                if(PermissionUtils.somePermissionPermanentlyDenied(this,mPermissions)){//拒绝过权限了 那试试完全关闭吧
                    SpUtils.store(mContext,SpKey.SHOW_JUST_LOGIN,true);

                }
//                Toast.makeText(mContext, "使用一键登录需要获取手机号", Toast.LENGTH_SHORT).show();
                ARouter.getInstance().build(AppRoutes.APP_LOGIN)
                        .withString("shanyanType", "1")
                        .navigation();
                finish();

            }
            //闪验SDK预取号（可缩短拉起授权页时间）


//            OneKeyLoginManager.getInstance().getPhoneInfo(new GetPhoneInfoListener() {
//                @Override
//                public void getPhoneInfoStatus(int code, String result) {
//                    //预取号回调
//                    Log.e("VVV", "预取号： code==" + code + "   result==" + result);
//                    if (code == 1022) {//code为1022:成功；其他：失败
//                        //授权页配置
//                        OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getCJSConfig(mContext), null);
//                        //拉授权页
//                        openLoginActivity();
//                    }else{
//                        finish();
//                        CrashReport.postCatchedException(new Throwable("预取号失败： code==" + code + "   result==" + result));
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
            //闪验SDK预取号（可缩短拉起授权页时间）
//            OneKeyLoginManager.getInstance().getPhoneInfo(new GetPhoneInfoListener() {
//                @Override
//                public void getPhoneInfoStatus(int code, String result) {
//                    //预取号回调
//                    Log.e("VVV", "预取号： code==" + code + "   result==" + result);
//                    if (code == 1022) {//code为1022:成功；其他：失败
//                        //授权页配置
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
//                                        //拉授权页
//                                        openLoginActivity();
//                                    }
//                                });
//                            }
//                        }).start();
//
//
//                    }else {
//                        finish();
//                        CrashReport.postCatchedException(new Throwable("预取号失败： code==" + code + "   result==" + result));
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
        @Override//这个监听是为了防止离开本页面之后再回来会出现页面显示不正常的问题
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
        builder.setTitle("提示");
        builder.setMessage("将会有一些权限需要被授予");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(LoginTransferActivity.this, new String[]{permission}, MY_READ_PHONE_STATE);
            }
        });
        builder.setCancelable(false);//弹出框不可以返回键取消
        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//将弹出框设置为全局
        dialog.setCanceledOnTouchOutside(false);//失去焦点不会消失
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
            ////System.out.println("注册手机尾号：" + phoneed);
        }
        System.out.println("注册别名:" + new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
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
                ////System.out.println("保存开屏信息");
            }
            if (adModel.type == 1 && adModel.triggerPage == 1) {
                SpUtils.store(this, SpKey.USER_Main_AD_Bean, new Gson().toJson(adModel));
                ////System.out.println("保存开屏信息");
            }
        }
    }

    private void initView() {
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
    }
}