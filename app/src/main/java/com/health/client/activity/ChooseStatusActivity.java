package com.health.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.client.R;
import com.health.client.contract.UpdateStatusContract;
import com.health.client.presenter.UpdateStatusPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Constants;
import com.healthy.library.interfaces.IsNoNeedPatchShow;
import com.healthy.library.message.UpdateUserInfoMsg;
import com.healthy.library.routes.AppRoutes;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/18 10:54
 * @des 选择状态界面 （怀孕中、备孕期、育儿期）
 */

@Route(path = AppRoutes.APP_CHOOSE_STATUS)
public class ChooseStatusActivity extends BaseActivity implements UpdateStatusContract.View, IsNoNeedPatchShow {
    @Autowired
    String sex="女";
    @Autowired
    String isadd;
    @Autowired
    String id;
    @Autowired
    String useStatus;
    @Autowired
    String babyId;
    private android.widget.TextView tvChooseStatusTitle;
    private UpdateStatusPresenter mUpdateStatusPresenter;
    private android.widget.LinearLayout ivOther;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_status;
    }

    @Override
    protected void findViews() {
        initView();

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mUpdateStatusPresenter=new UpdateStatusPresenter(mContext,this);
        tvChooseStatusTitle.setText("女".equals(sex)?"请选择您当前的状态":"请选择您妻子当前的状态");
        if(id!=null&&!"".equals(id)){
            ivOther.setVisibility(View.GONE);
        }
        if(isadd!=null&&!"".equals(isadd)){
            ivOther.setVisibility(View.GONE);
        }
    }

    public void statusPregnancy(View view) {
        ARouter.getInstance()
                .build(AppRoutes.APP_STATUS_PREGNANCY)
                .withString("sex", sex)
                .withString("isadd",isadd)
                .withString("id",id)
                .withString("useStatus",useStatus)
                .withString(Constants.STATUS, Constants.STATUS_PREGNANCY)
                .navigation(this,Constants.STATUS_PREGNANCYCODE);
    }

    public void statusMenstruation(View view) {
        ARouter.getInstance()
                .build(AppRoutes.APP_STATUS_MENSTRUATION)
                .withString("sex", sex)
                .withString("id",id)
                .withString("isadd",isadd)
                .withString("useStatus",useStatus)
                .withString(Constants.STATUS, Constants.STATUS_FOR_PREGNANCY)
                .navigation(this,Constants.STATUS_FOR_PREGNANCYCODE);
    }

    public void statusParenting(View view) {
        ARouter.getInstance()
                .build(AppRoutes.APP_STATUS_PARENTING)
                .withString("sex", sex)
                .withString("id",id)
                .withString("isadd",isadd)
                .withString("useStatus",useStatus)
                .withString("babyId",babyId)
                .withString(Constants.STATUS, Constants.STATUS_AFTER_PREGNANCY)
                .navigation(this,Constants.STATUS_AFTER_PREGNANCYCODE);
    }
    public void statusOther(View view) {
//        ARouter.getInstance()
//                .build(AppRoutes.APP_STATUS_PARENTING)
//                .withString(Constants.STATUS, "女".equals(sex)?Constants.STATUS_AFTER_PREGNANCY:Constants.STATUS_MAN_AFTER_PREGNANCY)
//                .navigation();
        Map<String, Object> map = new HashMap<>();
        map.put("memberSex","女".equals(sex)?"2":"1");
        map.put("stageStatus", "-1");
        mUpdateStatusPresenter.updateStatus(map);
//        SpUtils.store(mContext,
//                SpKey.STATUS, "-1");
//        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setClass(mContext, MainActivity.class);
//        startActivity(intent);

    }

    public void back(View view) {
        finish();
    }

    private void initView() {
        tvChooseStatusTitle = (TextView) findViewById(R.id.tv_choose_status_title);
        ivOther = (LinearLayout) findViewById(R.id.iv_other);
    }

    @Override
    public void onUpdateSuccess() {
        if(id!=null&&!"".equals(id)){
            finish();
        }else {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass(mContext, MainActivity.class);
            startActivity(intent);
//            ARouter.getInstance().build(MineRoutes.MINE_UPDATE_USER_INFO).navigation();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.STATUS_FOR_PREGNANCYCODE||
                requestCode==Constants.STATUS_PREGNANCYCODE||
                requestCode==Constants.STATUS_AFTER_PREGNANCYCODE){
            if(resultCode== Activity.RESULT_OK){
                setResult(Activity.RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void onUpdateSuccessEx() {

    }

    @Override
    public void onUpdateFail() {

    }

    @Override
    public void finish() {
        super.finish();
        EventBus.getDefault().post(new UpdateUserInfoMsg());
    }
}
