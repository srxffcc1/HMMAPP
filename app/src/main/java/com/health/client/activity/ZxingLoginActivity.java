package com.health.client.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.client.R;
import com.health.client.contract.LoginContract;
import com.health.client.model.UserInfoModel;
import com.health.client.presenter.LoginPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.interfaces.IsNoNeedPatchShow;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.widget.TopBar;

@Route(path = AppRoutes.APP_ZXINGLOGIN)
public class ZxingLoginActivity extends BaseActivity implements LoginContract.View , IsNoNeedPatchShow {

    @Autowired
    String qrCode;

    private TopBar topBar;
    private ImageView img;
    private TextView tips1;
    private TextView tips2;
    private TextView commit;
    private TextView cancle;
    private LoginPresenter mLoginPresenter;

    private boolean isAgain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zxing_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mLoginPresenter = new LoginPresenter(mContext, this);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAgain) {
                    isAgain = false;
                    ARouter.getInstance()
                            .build(MineRoutes.MINE_ZXING_SCAN)
                            .navigation();
                    finish();
                } else {
                    mLoginPresenter.zxingLogin(new SimpleHashMapBuilder<>().puts("qrCode", qrCode));//扫完码过来了，登录
                }
            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        topBar = (TopBar) findViewById(R.id.top_bar);
        img = (ImageView) findViewById(R.id.img);
        tips1 = (TextView) findViewById(R.id.tips1);
        tips2 = (TextView) findViewById(R.id.tips2);
        commit = (TextView) findViewById(R.id.commit);
        cancle = (TextView) findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onGetCodeSuccess() {

    }

    @Override
    public void onGetCodeFail() {

    }

    @Override
    public void onLoginSuccess(UserInfoModel userInfo) {

    }

    @Override
    public void onLoginFail() {

    }

    @Override
    public void onZxingLoginSuccess(String result) {
        if (result != null && result.contains("成功")) {
            showToast("登录成功");
            finish();
        } else if (result != null && result.contains("请使用主播账号登录")) {

        } else {
            isAgain = true;
            img.setImageResource(R.drawable.zxing_xing_icon);
            tips1.setText("二维码失效");
            commit.setText("重新扫描");
            tips2.setText("重新扫描");
        }
    }

    @Override
    public void updatePwdResult(String result) {

    }

    @Override
    public void checkCodeResult(String result) {

    }
}