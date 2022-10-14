package com.health.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
 * @date 2019/03/19 09:36
 * @des 孕期选择
 */

@Route(path = AppRoutes.APP_STATUS_PREGNANCY)
public class StatusPregnancyActivity extends BaseActivity implements OnDateConfirmListener,
        UpdateStatusContract.View, IsNoNeedPatchShow {
    @Autowired
    String sex="女";
    @Autowired
    String id;
    @Autowired
    String useStatus;
    @Autowired
    String isadd;
    private DateDialog mDateDialog;
    private TextView mTvDate;
    @Autowired(name = Constants.STATUS)
    String mStatus;
    private UpdateStatusPresenter mUpdateStatusPresenter;
    private StatusLayout mStatusLayout;
    private TextView tv_choose_title;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_status_pregnancy;
    }

    @Override
    protected void findViews() {
        mTvDate = findViewById(R.id.tv_date);
        tv_choose_title=findViewById(R.id.tv_choose_title);
        mStatusLayout = findViewById(R.id.layout_status);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setStatusLayout(mStatusLayout);
        mUpdateStatusPresenter = new UpdateStatusPresenter(mContext, this);
        if("女".equals(sex)){

            tv_choose_title.setText("请选择末次月经初潮日期");
        }else {
            tv_choose_title.setText("请选择预产期");
        }
        mTvDate.performClick();
    }

    public void chooseDate(View view) {
        if (mDateDialog == null) {
            long currentMill = System.currentTimeMillis();
            long duration;
            long duration2;
            if("女".equals(sex)){
                duration  = currentMill - DaysLimit.DAYS_LAST_MEN * 24 * 60 * 60 * 1000L;
                mDateDialog = DateDialog.newInstance(
                        currentMill, duration, currentMill, ""
                );

            }else {
                duration  = currentMill - DaysLimit.DAYS_LAST_MEN * 24 * 60 * 60 * 1000L;
                duration2  = currentMill + DaysLimit.DAYS_LAST_MEN * 24 * 60 * 60 * 1000L;
                mDateDialog = DateDialog.newInstance(
                        currentMill, duration, duration2, ""
                );
            }




            mDateDialog.setOnConfirmClick(this);
        }
        mDateDialog.show(getSupportFragmentManager(), "dateDialog");
    }

    @Override
    public void onConfirm(int pos, Date data) {
        String time = TimestampUtils.timestamp2String(data.getTime(), "yyyy-MM-dd");
        mTvDate.setText(time);
        Map<String, Object> map = new HashMap<>();
        map.put("memberSex","女".equals(sex)?"2":"1");
        map.put("stageStatus", mStatus);
        map.put("currentStatus", mStatus);
        if("女".equals(sex)){

            map.put("lastMensesDate", time);
        }else {
            map.put("deliveryDate", time);
        }
        if(id!=null&&!"".equals(id)){

            map.put("useStatus",useStatus);
            map.put("id",id);
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

    @Override
    public void onRequestFinish() {
        mStatusLayout.updateStatus(StatusLayout.Status.STATUS_CONTENT);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void showNetErr() {
    }

    @Override
    public void showDataErr() {

    }
}
