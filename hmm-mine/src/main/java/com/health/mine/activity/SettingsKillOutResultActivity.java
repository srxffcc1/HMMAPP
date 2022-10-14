package com.health.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.contract.SettingsContract;
import com.health.mine.presenter.LogKillPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.UserInfoModel;
import com.healthy.library.net.RetrofitHelper;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.SectionView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import cn.jpush.android.api.JPushInterface;

/**
 * @author Li
 * @date 2019/04/17 14:34
 * @des 设置界面
 */
@Route(path = MineRoutes.MINE_SETTINGS_KILL_RESULT)
public class SettingsKillOutResultActivity extends BaseActivity implements SettingsContract.View {


    private StatusLayout layoutStatus;
    private TopBar topBar;
    private ConstraintLayout ivLogoll;
    private android.widget.ImageView ivLogo;
    private TextView oneVersion;
    private TextView twoVersion;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_killoutresult;
    }

    @Override
    protected void findViews() {
        initView();
    }

    @Override
    public void onBackBtnPressed() {
        logout(null);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }

    public void logout(View view) {
        onLogoutSuccess();
    }



    @Override
    public void onLogoutSuccess() {
        String phone = SpUtils.getValue(this, SpKey.PHONE);
//        JPushInterface.deleteAlias(getApplicationContext(), Integer.parseInt(phone.substring(phone.length() > 4 ? phone.length() - 4 : 0, phone.length())));
        SpUtils.store(mContext, SpKey.USER_ID, null);
        SpUtils.store(mContext, SpKey.STATUS, null);
        String testIP = SpUtils.getValue(mContext, SpKey.TEST_IP);
        boolean isShowUserGift = SpUtils.getValue(mContext, "isShowUserGift", false);
        boolean isShowUserActGift = SpUtils.getValue(mContext, "isShowUserActGift", false);
        String value = SpUtils.getValue(mContext, "redDotKey");
        SpUtils.clear(mContext);
        SpUtils.store(mContext,"redDotKey",value);
        //新客礼包首次进入引导 -> 2021/11/02 跟手机进行绑定 不根据当前用户绑定
        SpUtils.store(mContext, "isShowUserGift", isShowUserGift);
        //节日礼包首次进入引导
        SpUtils.store(mContext, "isShowUserActGift", isShowUserActGift);
        SpUtils.store(mContext, SpKey.TEST_IP, testIP);
        SpUtils.store(mContext, SpKey.SHOW_GUIDE, false);
        SpUtils.store(mContext, SpKey.YSLOOK, true);
        SpUtils.store(mContext, "isShowZZ", true);
        SpUtils.store(mContext, "专家答疑Guide", 1);
        SpUtils.store(mContext, "完善资料Guide", 1);
        SpUtils.store(mContext, "同城憨妈Guide", 1);
        SpUtils.isFirst(mContext, "floatMall2");
        SpUtils.isFirst(mContext, "floatPost");
        SpUtils.isFirst(mContext, "floatFaq");
        RetrofitHelper.clear();
        ARouter.getInstance()
                .build(AppRoutes.APP_LOGINTRANSFER)
                .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .navigation();
    }

    @Override
    public void onGetUserInfoSuccess(UserInfoModel userInfoModel) {

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        showContent();
    }

    private void initView() {

        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        topBar = (TopBar) findViewById(R.id.top_bar);
        ivLogoll = (ConstraintLayout) findViewById(R.id.iv_logoll);
        ivLogo = (ImageView) findViewById(R.id.iv_logo);
        oneVersion = (TextView) findViewById(R.id.one_version);
        twoVersion = (TextView) findViewById(R.id.two_version);
    }
}
