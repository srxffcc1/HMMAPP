package com.health.servicecenter.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.servicecenter.R;
import com.health.servicecenter.contract.AppointmentListContract;
import com.health.servicecenter.contract.AppointmentMainContract;
import com.health.servicecenter.presenter.AppointmentListPresenter;
import com.health.servicecenter.presenter.AppointmentMainPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.AppointmentListItemModel;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.model.AppointmentTimeSettingModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.widget.TopBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Long
 */
@Route(path = ServiceRoutes.SERVICE_APPOINTMENTPAYRESULT)
public class AppointmentPayResultActivity extends BaseActivity implements IsFitsSystemWindows, View.OnClickListener, AppointmentListContract.View {

    private TopBar topBar;
    private ImageView payStatusImg;
    private TextView successTxt;
    private TextView secondTxt;
    private TextView redBtn;
    private TextView whiteBtn;
    @Autowired
    boolean isSuccess;
    @Autowired
    long id;
    //private Map<String, Object> mMap = new HashMap<>();

    private boolean isOnClick;

    private Handler mCountHandler = new Handler();
    private Runnable mCountRun = new Runnable() {
        @Override
        public void run() {
            if (isOnClick) return;
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_APPOINTMENTMAIN)
                    .navigation();
        }
    };
    private AppointmentListPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appointment_pay_result;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
//        if (){//支付成功
//            payStatusImg.setImageResource(R.drawable.appointment_pay_success);
//            successTxt.setText("恭喜您支付成功");
//            secondTxt.setText("3秒后自动跳转商品首页");
//            redBtn.setText("返回首页");
//        }else{
//            payStatusImg.setImageResource(R.drawable.appointment_pay_fail);
//            successTxt.setText("支付失败");
//            secondTxt.setText("建议您重新尝试发起支付");
//            redBtn.setText("重新支付");
//        }

        if (isSuccess) {
            payStatusImg.setImageResource(R.drawable.appointment_pay_success);
            topBar.setTitle("支付成功");
            successTxt.setText("恭喜您支付成功");
            secondTxt.setText("3秒后自动跳转商品首页");
            redBtn.setText("返回首页");
            whiteBtn.setVisibility(View.VISIBLE);
            mCountHandler.postDelayed(mCountRun, 3000);
        } else {
            payStatusImg.setImageResource(R.drawable.appointment_pay_fail);
            topBar.setTitle("支付失败");
            successTxt.setText("支付失败");
            secondTxt.setText("建议您重新尝试发起支付");
            redBtn.setText("返回商品详情页");
            whiteBtn.setVisibility(View.GONE);
        }

        showContent();

        //mMainPresenter = new AppointmentListPresenter(mContext, this);
        //mMap.put("id", id);
        //getData();
    }

    @Override
    public void getData() {
        super.getData();
        //mMainPresenter.getDetails(mMap);
    }

    @Override
    protected void findViews() {
        super.findViews();
        topBar = (TopBar) findViewById(R.id.top_bar);
        payStatusImg = (ImageView) findViewById(R.id.payStatusImg);
        successTxt = (TextView) findViewById(R.id.successTxt);
        secondTxt = (TextView) findViewById(R.id.secondTxt);
        redBtn = (TextView) findViewById(R.id.redBtn);
        whiteBtn = (TextView) findViewById(R.id.whiteBtn);

        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountHandler.removeCallbacks(mCountRun);
        mCountHandler.removeCallbacksAndMessages(null);
        mCountHandler = null;
    }

    private void initListener() {
        redBtn.setOnClickListener(this);
        whiteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.whiteBtn) {
            //查看预约单
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_APPOINTMENTRESULT)
                    .withLong("id",id)
                    .navigation();
            isOnClick = true;
            finish();
        } else if (vId == R.id.redBtn) {
            if (isSuccess) {
                isOnClick = true;
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_APPOINTMENTMAIN)
                        .navigation();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onGetListSuccess(List<AppointmentListItemModel> modelList) {

    }

    @Override
    public void onGetDetailsSuccess(AppointmentListItemModel model) {

        AppointmentListItemModel.AppointmentTrade appointmentTrade = model.getAppointmentTrade();
        //if (appointmentTrade != null) {
        isSuccess = appointmentTrade.isPayed();
        if (isSuccess) {
            payStatusImg.setImageResource(R.drawable.appointment_pay_success);
            topBar.setTitle("支付成功");
            successTxt.setText("恭喜您支付成功");
            secondTxt.setText("3秒后自动跳转商品首页");
            redBtn.setText("返回首页");
            whiteBtn.setVisibility(View.VISIBLE);
            mCountHandler.postDelayed(mCountRun, 3000);
        } else {
            payStatusImg.setImageResource(R.drawable.appointment_pay_fail);
            topBar.setTitle("支付失败");
            successTxt.setText("支付失败");
            secondTxt.setText("建议您重新尝试发起支付");
            redBtn.setText("返回商品详情页");
            whiteBtn.setVisibility(View.GONE);
        }
        //}
        showContent();
    }

    @Override
    public void onCleanSuccess() {

    }
}