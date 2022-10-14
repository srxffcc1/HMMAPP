package com.health.servicecenter.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.health.servicecenter.contract.AppointmentListContract;
import com.health.servicecenter.presenter.AppointmentListPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.AppointmentListItemModel;
import com.healthy.library.model.ChatMessage;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.StatusLayout;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Long
 */
@Route(path = ServiceRoutes.SERVICE_APPOINTMENTRESULT)
public class AppointmentResultActivity extends BaseActivity implements IsFitsSystemWindows, View.OnClickListener, AppointmentListContract.View {

    @Autowired
    long id;
    private View mImgBack;
    private View mCancel;
    private ViewGroup mLayoutBottom;
    private TextView mTvTitle;
    private TextView mTvBody;
    private TextView mRemarkBody;
    private AppointmentListPresenter mListPresenter;
    private Map<String, Object> mMap = new HashMap<>();
    private TextView storeName;
    private TextView selectTime;
    private TextView selectTechnician;
    private ImageView goodsImg;
    private TextView goodsTitle;
    private TextView goodsPrice;
    private TextView goodsSpace;
    private TextView goodsTime;
    private TextView appointmentName;
    private TextView appointmentPhone;
    private TextView tvAllPrice;
    private TextView tvNetWorkPayType;
    private TextView tvPayType;
    private TextView tvPayPrice;
    private TextView tvPayPriceTitle;
    private TextView tvAppointmentNo;
    private TextView tvAppointmentSubmitTime;
    private TextView tvAppointmentPayTime;
    private ConstraintLayout remarkView;
    private StatusLayout mStatusLayout;
    private TextView technicianTitle;
    private ViewGroup mLayout;
    private Intent mResultIntent;
    private CountDownTimer countDownTimer;
    private int mSeconds;
    private RefreshLayout mRefreshLayout;
    private ViewGroup mPayInfoLayout;
    private View tvAppointmentPayTimeTitle;
    private int modelPayType;
    private String mStartTimeStr;
    private int mAdvanceCancelNumber;
    private int mAdvanceCancelUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appointment_result;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mResultIntent = new Intent();
        mResultIntent.putExtra("isLoadData", false);
        mListPresenter = new AppointmentListPresenter(mContext, this);
        mMap.put("id", String.valueOf(id));

        mRefreshLayout.finishLoadMoreWithNoMoreData();
        mRefreshLayout.setEnableRefresh(false);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        mListPresenter.getDetails(mMap);
    }

    @Override
    protected void findViews() {
        super.findViews();

        mStatusLayout = findViewById(R.id.layout_status);
        setStatusLayout(mStatusLayout);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mLayout = findViewById(R.id.layout);
        mImgBack = findViewById(R.id.img_back);
        mTvTitle = findViewById(R.id.tv_appointment_title);
        mTvBody = findViewById(R.id.tv_appointment_body);
        mCancel = findViewById(R.id.wheel_cancel_appointment);
        mLayoutBottom = findViewById(R.id.layout_bottom);
        TextView mRemark = findViewById(R.id.remark);
        // 预约人信息
        mRemarkBody = findViewById(R.id.remarkBody);
        storeName = findViewById(R.id.storeName);
        selectTime = findViewById(R.id.selectTime);
        selectTechnician = findViewById(R.id.selectTechnician);
        technicianTitle = findViewById(R.id.technicianTitle);
        goodsImg = findViewById(R.id.goodsImg);
        goodsTitle = findViewById(R.id.goodsTitle);
        goodsPrice = findViewById(R.id.goodsPrice);
        goodsSpace = findViewById(R.id.goodsSpace);
        remarkView = findViewById(R.id.remarkView);
        goodsTime = findViewById(R.id.goodsTime);
        appointmentName = findViewById(R.id.appointmentName);
        appointmentPhone = findViewById(R.id.appointmentPhone);
        tvAllPrice = findViewById(R.id.tv_allPrice);
        tvNetWorkPayType = findViewById(R.id.tv_NetWork_PayType);
        tvPayType = findViewById(R.id.tv_PayType);
        tvPayPrice = findViewById(R.id.tv_pay_price);
        tvPayPriceTitle = findViewById(R.id.tv_pay_price_title);
        tvAppointmentNo = findViewById(R.id.tv_appointmentNo);
        tvAppointmentSubmitTime = findViewById(R.id.tv_appointment_SubmitTime);
        tvAppointmentPayTime = findViewById(R.id.tv_appointment_PayTime);
        tvAppointmentPayTimeTitle = findViewById(R.id.tv_appointment_PayTime_title);
        mPayInfoLayout = findViewById(R.id.result_payInfo_layout);

        initListener();
    }

    private void initListener() {
        mImgBack.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.img_back) {
            setResult(Activity.RESULT_OK, mResultIntent);
            finish();
        } else if (vId == R.id.wheel_cancel_appointment) {
            //是否超出取消预约时间
            if (isCancelTimeOut()) {
                return;
            }
            //取消预约操作
           /* if (1 == modelPayType) {*/
            isAgree("", "<br/>是否确定取消此服务！<br/>", 1, null);
            /*} else {
                isAgree("", "<br/>是否确定取消此服务！<br/><br/>资金将会从<font color=\"#ff0000\">原路退回</font>至您的账户中!", 1, null);
            }*/
        }
    }

    /**
     * 用于判断是否超时取消预约时间范围
     *
     * @return
     */
    private boolean isCancelTimeOut() {
        boolean isError = false;
        String advanceCancelUnit = mAdvanceCancelUnit == 2 ? "天" : "小时";
        /**
         * 预约起始时间 （格式 2021-06-17）
         */
        String[] mStartDate = null;
        if (!TextUtils.isEmpty(mStartTimeStr)) {
            String[] splitAll = mStartTimeStr.split("\\s+");
            mStartDate = splitAll[0].split("-");
        }

        /**
         * 当前时间 （格式 2021-06-18）
         */
        String[] mThisDate;
        String thisTime = DateUtils.formatLongAll(String.valueOf(System.currentTimeMillis()));
        String[] splitAll = thisTime.split("\\s+");
        mThisDate = splitAll[0].split("-");

        //两个时间相差距离多少天
        String[] distanceTime = DateUtils.getDistanceTime(mStartTimeStr, thisTime);
        //单独只判断时间差不太能行
        if (mStartDate != null && mStartDate.length > 2 && mThisDate.length > 2) {
            //如果年份不同或月份不同
            if (!mStartDate[0].equals(mThisDate[0])
                    || !mStartDate[1].equals(mThisDate[1])) {
                isError = true;
            } else {
                //按天比
                if (2 == mAdvanceCancelUnit) {
                    //根据 预约开始时间和当前时间得出的时间差天数小于规定时间 (错误情况)
                    if (Integer.parseInt(distanceTime[0]) < mAdvanceCancelNumber) {
                        isError = true;
                    }
                }
                //按小时比
                if (1 == mAdvanceCancelUnit) {
                    //根据 预约时间天数小于当前天数 或 预约开始时间和当前时间得出的时间差小时小于规定时间 (错误情况)
                    if ((Integer.parseInt(mStartDate[2]) < Integer.parseInt(mThisDate[2]))
                            || (Integer.parseInt(distanceTime[1]) < mAdvanceCancelNumber)) {
                        isError = true;
                    }
                }
            }
        }

        if (isError) {
            showToast("到店前" + mAdvanceCancelNumber + advanceCancelUnit + "可取消");
            return true;
        }
        return false;
    }

    private void initData(AppointmentListItemModel model) {

        mStartTimeStr = model.getAppointmentDate() + " " + model.getAppointmentRangeStartTime();

        mAdvanceCancelNumber = model.getAdvanceCancelNumber();
        mAdvanceCancelUnit = model.getAdvanceCancelUnit();

        String mTitle = "";
        String mBody = "";
        //mLayoutBottom.setVisibility(View.GONE);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        mTvTitle.setText("");
        //	1待接单 2带到店 3已完成 4已取消
        switch (model.getStatus()) {
            case 1:
                //是否支持取消及退款  1 支持	2 不支持
                if (1 == model.getSupportRefund()) {
                    mLayoutBottom.setVisibility(View.VISIBLE);
                }
                if (model.getAutoCancelSurplusSeconds() == 0) {
                    mTitle = "00:00:00 等待接单";
                    mResultIntent.putExtra("isLoadData", true);
                } else {
                    mTitle = "";
                    downTimer(model.getAutoCancelSurplusSeconds());
                }
                mBody = "商家会尽快处理您的订单，请耐心等待";
                break;
            case 2:
                mTitle = "预约成功，待到店";
                mBody = "请准时到店享受服务哦～";
                //是否支持取消及退款  1 支持	2 不支持
                if (1 == model.getSupportRefund()) {
                    mLayoutBottom.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                mTitle = "预约已完成";
                mBody = "期待您的下一次光临～";
                mLayoutBottom.setVisibility(View.GONE);
                break;
            case 4:
                mTitle = "预约已取消";
                //取消原因
                mBody = model.getCancelReason();
                mLayoutBottom.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        mTvTitle.setText(mTitle);
        mTvBody.setText(mBody);

    }

    /**
     * 倒计时
     *
     * @param time
     */
    private void downTimer(long time) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        } else {
            //后台返回距离自动取消剩余：秒数
            mSeconds = Integer.parseInt(String.valueOf(time));
        }
        if (mSeconds > 0) {
            countDownTimer = new CountDownTimer(mSeconds * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    millisUntilFinished = millisUntilFinished / 1000;
                    mSeconds = (int) millisUntilFinished;
                    int hours = (int) (millisUntilFinished / (60 * 60));
                    int minutes = (int) millisUntilFinished / 60 % 60;
                    int seconds = (int) millisUntilFinished % 60;
                    mTvTitle.setText(String.format("%02d", Math.max(0, hours)) + ":"
                            + String.format("%02d", Math.max(0, minutes)) + ":"
                            + String.format("%02d", Math.max(0, seconds)) + " 等待接单");
                }

                @Override
                public void onFinish() {
                    //剩余支付时间结束后进行相应逻辑处理
                    mTvTitle.setText("0");
                    mResultIntent.putExtra("isLoadData", true);
                    getData();
                }
            }.start();
        }
    }

    @Override
    public void onGetListSuccess(List<AppointmentListItemModel> modelList) {

    }

    //获取详情数据
    @Override
    public void onGetDetailsSuccess(final AppointmentListItemModel model) {
        if (model == null) {
            showNetErr();
            return;
        }
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.finishLoadMoreWithNoMoreData();
        //mRefreshLayout.setEnableLoadMore(true);

        initData(model);

        int technicianVisibility = TextUtils.isEmpty(model.getTechnicianName())
                || TextUtils.isEmpty(model.getTechnicianNickName())
                || TextUtils.isEmpty(model.getTechnicianPhone())
                || TextUtils.isEmpty(String.valueOf(model.getTechnicianId())) ? View.GONE : View.VISIBLE;
        technicianTitle.setVisibility(technicianVisibility);
        selectTechnician.setVisibility(technicianVisibility);

        String projectPrice = "¥" + FormatUtils.moneyKeep2Decimals(model.getProjectPrice());
        storeName.setText(model.getShopName());
        String mTime = DateUtils.getDateDay(model.getAppointmentDate(), "yyyy-MM-dd", "MM月dd日") + "\u3000" +
                DateUtils.getDateTimeSplit(model.getAppointmentRangeStartTime()) + "-" + DateUtils.getDateTimeSplit(model.getAppointmentRangeEndTime());
        selectTime.setText(mTime);
        selectTechnician.setText(model.getTechnicianNickName());
        goodsTitle.setText(model.getProjectName());
        goodsPrice.setText(projectPrice);
        goodsTime.setText("时长" + model.getAppointmentDuration() + "分钟");
        //多规格时设置文本
        if ("2".equals(model.getProjectAttributeType())) {
            goodsSpace.setText(model.getProjectAttributeName());
        }
        List<String> imgList = model.getImgList();
        String url = "";
        if (!ListUtil.isEmpty(imgList)) {
            url = imgList.get(0);
        }
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(url)
                .error(R.drawable.img_1_1_default)
                .placeholder(R.drawable.img_1_1_default)
                .into(goodsImg);

        appointmentName.setText(model.getMemberName());
        appointmentPhone.setText(model.getMemberPhone());
        if (model.getMemberRemark() != null && !TextUtils.isEmpty(model.getMemberRemark())) {
            remarkView.setVisibility(View.VISIBLE);
            mRemarkBody.setText(model.getMemberRemark());
        } else {
            remarkView.setVisibility(View.GONE);
        }

        String price = "¥" + FormatUtils.moneyKeep2Decimals(model.getAppointmentAmount());
        String payPrice = "¥0.0";
        String payWay = "未付款";
        String payPriceTitle = "应付款";
        //payType	付款类型(1:到店付款，2:在线支付)
        //payWay	支付方式（1：支付宝，2：微信）
        AppointmentListItemModel.AppointmentTrade appointmentTrade = model.getAppointmentTrade();

        //String mPayTypeData;
        int visibility;
        modelPayType = model.getPayType();
        if (1 == modelPayType) {
            //mPayTypeData = "到店付款";
            visibility = View.GONE;
            //mPayInfoLayout.setVisibility(View.GONE);
        } else {
            //mPayTypeData = "在线支付";
            visibility = View.VISIBLE;
        }
        //tvPayType.setText(mPayTypeData);
        mPayInfoLayout.setVisibility(visibility);
        tvAppointmentPayTimeTitle.setVisibility(visibility);
        tvAppointmentPayTime.setVisibility(visibility);

        tvAppointmentNo.setText(model.getAppointmentNo());
        //合计金额
        tvAllPrice.setText(price);
        if (appointmentTrade != null) {
            if (1 == appointmentTrade.getPayWay()) {
                payWay = "支付宝";
            }
            if (2 == appointmentTrade.getPayWay()) {
                payWay = "微信";
            }
            if (3 == appointmentTrade.getPayWay()) {
                payWay = "微信小程序";
            }
            payPrice = "¥" + FormatUtils.moneyKeep2Decimals(appointmentTrade.getPayAmount());
            tvAppointmentPayTime.setText(appointmentTrade.getPaySuccessTime());
            payPriceTitle = "实付款";
        }
        tvNetWorkPayType.setText(payWay);
        //实付款
        tvPayPrice.setText(payPrice);
        tvPayPriceTitle.setText(payPriceTitle);
        tvAppointmentSubmitTime.setText(model.getCreateTime());
        mLayout.setVisibility(View.VISIBLE);
        showContent();
        tvAppointmentNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyContentToClipboard(model.getAppointmentNo(), mContext);
            }
        });
    }

    /**
     * 复制内容到剪贴板
     *
     * @param content
     * @param context
     */
    public void copyContentToClipboard(String content, Context context) {
        try {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", content);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            Toast.makeText(context, "已复制预约单号到剪贴板", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCleanSuccess() {
        //Toast.makeText(mContext, "取消成功", Toast.LENGTH_SHORT).show();
        showToast("预约已取消");
        //取消成功需重新查询订单状态
        getData();
        if(countDownTimer != null) {
            //停止计时器
            countDownTimer.cancel();
            countDownTimer = null;
        }
        mResultIntent.putExtra("isLoadData", true);
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK, mResultIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void isAgree(String title, String msg, final int type, final ChatMessage chatMessage) {
        StyledDialog.init(mContext);
        StyledDialog.buildIosAlert(title, HtmlCompat.fromHtml(msg, HtmlCompat.FROM_HTML_MODE_LEGACY), new MyDialogListener() {
            @Override
            public void onFirst() {
            }

            @Override
            public void onThird() {
                super.onThird();
            }

            @Override
            public void onSecond() {
                mListPresenter.cancleAppointment(mMap);
            }
        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0)
                .setBtnText("取消", "确定")
                .show();
    }
}