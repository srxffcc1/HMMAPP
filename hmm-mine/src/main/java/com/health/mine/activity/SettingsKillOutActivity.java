package com.health.mine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.contract.SettingsContract;
import com.health.mine.presenter.LogKillPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.model.UserInfoModel;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.widget.SectionView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

/**
 * @author Li
 * @date 2019/04/17 14:34
 * @des 设置界面
 */
@Route(path = MineRoutes.MINE_SETTINGS_KILL)
public class SettingsKillOutActivity extends BaseActivity implements SettingsContract.View {


    private StatusLayout layoutStatus;
    private TopBar topBar;
    private androidx.constraintlayout.widget.ConstraintLayout ivLogoll;
    private SectionView oneVersion;
    private SectionView twoVersion;
    private SectionView threeVersion;
    private TextView fourVersion;
    LogKillPresenter logKillPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_killout;
    }

    @Override
    protected void findViews() {
        initView();
    }


    @Override
    protected void init(Bundle savedInstanceState) {
         logKillPresenter=new LogKillPresenter(this,this);
         //Toast.makeText(mContext,"当前打补丁呢",Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        StyledDialog.init(this);
        StyledDialog.buildIosAlert("确定", "是否确认注销账户，注销后无法找回！" , new MyDialogListener() {
            @Override
            public void onFirst() {
            }

            @Override
            public void onThird() {
                super.onThird();
            }

            @Override
            public void onSecond() {
                logKillPresenter.logout();
            }
        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();

    }


    @Override
    public void onLogoutSuccess() {
        ARouter.getInstance()
                .build(MineRoutes.MINE_SETTINGS_KILL_RESULT)
                .navigation();
        finish();
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
        oneVersion = (SectionView) findViewById(R.id.one_version);
        twoVersion = (SectionView) findViewById(R.id.two_version);
        threeVersion = (SectionView) findViewById(R.id.three_version);
        fourVersion = (TextView) findViewById(R.id.four_version);
    }
}
