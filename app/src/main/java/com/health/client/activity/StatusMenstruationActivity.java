package com.health.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.client.R;
import com.health.client.contract.UpdateStatusContract;
import com.health.client.presenter.UpdateStatusPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.DaysLimit;
import com.healthy.library.dialog.DateDialog;
import com.healthy.library.dialog.MenstruationDialog;
import com.healthy.library.interfaces.IsNoNeedPatchShow;
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.message.UpdateUserInfoMsg;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.utils.TimestampUtils;
import com.healthy.library.widget.StatusLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/19 09:50
 * @des 备孕期  1.选择最近月经时间  2.选择月经周期和月经持续时长
 */

@Route(path = AppRoutes.APP_STATUS_MENSTRUATION)
public class StatusMenstruationActivity extends BaseActivity implements OnDateConfirmListener,
        MenstruationDialog.OnConfirmListener, UpdateStatusContract.View, IsNoNeedPatchShow {
    @Autowired
    String sex="女";
    @Autowired
    String id;
    @Autowired
    String useStatus;
    @Autowired
    String isadd;
    private Group mGroupStep1;
    private Group mGroupStep2;
    private DateDialog mDateDialog;
    private MenstruationDialog mMenstruationDialog;
    private TextView mTvDate;
    @Autowired(name = Constants.STATUS)
    String mStatus;
    private TextView mTvCycleDuration;
    private UpdateStatusPresenter mUpdateStatusPresenter;
    private String mDate;
    private StatusLayout mStatusLayout;
    private TextView tv_title_step_1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_status_menstruation;
    }

    @Override
    protected void findViews() {
        mGroupStep1 = findViewById(R.id.group_step_1);
        mGroupStep2 = findViewById(R.id.group_step_2);
        mTvDate = findViewById(R.id.tv_date);
        mTvCycleDuration = findViewById(R.id.tv_cycle_duration);
        mStatusLayout = findViewById(R.id.layout_status);
        tv_title_step_1=findViewById(R.id.tv_title_step_1);


    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setStatusLayout(mStatusLayout);
        mUpdateStatusPresenter = new UpdateStatusPresenter(mContext, this);
        if("女".equals(sex)){
            mTvDate.performClick();
            tv_title_step_1.setText("请选择最近月经开始时间");
        }else {
            tv_title_step_1.setText("自动获得男性备孕期数据");
            mTvDate.setText("正在为您自动计算");
            mTvDate.setClickable(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Map<String, Object> map = new HashMap<>();
                    map.put("memberSex","女".equals(sex)?"2":"1");
                    map.put("currentStatus", mStatus);
                    map.put("stageStatus", mStatus);
                    if(id!=null&&!"".equals(id)){
                        map.put("id",id);
                        map.put("useStatus",useStatus);
                        mUpdateStatusPresenter.updateStatusEx(map);
                    }else {
                        mUpdateStatusPresenter.updateStatus(map);
                    }
                }
            },900);


        }
    }

    public void chooseRecentDate(View view) {
        if (mDateDialog == null) {
            long currentMill = System.currentTimeMillis();
            long duration;
            duration  = currentMill - DaysLimit.DAYS_MEN_RECENT * 24 * 60 * 60 * 1000L;
            mDateDialog = DateDialog.newInstance(
                    currentMill, duration, currentMill, ""
            );
            mDateDialog.setOnConfirmClick(this);
        }
        mDateDialog.show(getSupportFragmentManager(), "menStart");
    }

    public void chooseCycleDuration(View view) {
        if (mMenstruationDialog == null) {
            mMenstruationDialog = MenstruationDialog.newInstance("");
            mMenstruationDialog.setOnConfirmListener(this);
        }
        mMenstruationDialog.show(getSupportFragmentManager(), "cycleDuration");
    }

    @Override
    public void onConfirm(int pos, Date data) {
        String time = TimestampUtils.timestamp2String(data.getTime(), "yyyy-MM-dd");
        mDate = time;
        mTvDate.setText(time);

        mGroupStep1.setVisibility(View.GONE);
        mGroupStep2.setVisibility(View.VISIBLE);

        mTvCycleDuration.performClick();
    }

    @Override
    public void onConfirm(int posCycle, int posDuration, String txtCycle, String txtDuration) {
        mTvCycleDuration.setText(String.format("%s,%s", txtCycle, txtDuration));
        String duration = String.valueOf(posDuration + 1);
        String cycle = String.valueOf(posCycle + 1);
        Map<String, Object> map = new HashMap<>();
        map.put("memberSex","女".equals(sex)?"2":"1");
        map.put("currentStatus", mStatus);
        map.put("stageStatus", mStatus);
        map.put("menstrualCycle", cycle);
        map.put("menstrualDays", duration);
        if("女".equals(sex)){
            map.put("latelyMensesDate", mDate);
        }else {
            map.put("deliveryDate", mDate);
        }
        if(id!=null&&!"".equals(id)){
            map.put("id",id);
            map.put("useStatus",useStatus);
            mUpdateStatusPresenter.updateStatusEx(map);
        }else {
            mUpdateStatusPresenter.updateStatus(map);
        }
    }

    @Override
    public void onUpdateSuccess() {
        EventBus.getDefault().post(new UpdateUserInfoMsg());
        if(id!=null&&!"".equals(id)){
            setResult(Activity.RESULT_OK);
            finish();
        }else {
            if(isadd!=null&&!"".equals(isadd)){
                setResult(Activity.RESULT_OK);
                finish();
            }else {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(mContext, MainActivity.class);
                startActivity(intent);
//                ARouter.getInstance().build(MineRoutes.MINE_UPDATE_USER_INFO).navigation();
            }

        }
    }

    @Override
    public void onUpdateSuccessEx() {
        EventBus.getDefault().post(new UpdateUserInfoMsg());
        if(id!=null&&!"".equals(id)){
            setResult(Activity.RESULT_OK);
            finish();
        }else {
            if(isadd!=null&&!"".equals(isadd)){
                setResult(Activity.RESULT_OK);
                finish();
            }else {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(mContext, MainActivity.class);
                startActivity(intent);
//                ARouter.getInstance().build(MineRoutes.MINE_UPDATE_USER_INFO).navigation();
            }

        }
    }

    @Override
    public void onUpdateFail() {

    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onRequestFinish() {
        mStatusLayout.updateStatus(StatusLayout.Status.STATUS_CONTENT);
    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void showDataErr() {

    }
}