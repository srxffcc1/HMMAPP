package com.health.servicecenter.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alipay.sdk.app.PayTask;
import com.health.servicecenter.contract.CheckoutCounterContract;
import com.health.servicecenter.presenter.CheckoutCounterPresenter;
import com.healthy.library.R;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Ids;
import com.healthy.library.contract.AppPinCheckContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.AppPinCheck;
import com.healthy.library.model.OrderList;
import com.healthy.library.model.OrderModel;
import com.healthy.library.model.PayResultModel;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.presenter.AppPinCheckPresenter;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.DeepRadioGroup;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


@Route(path = ServiceRoutes.SERVICE_CHECKOUT_COUNTER)
public class CheckOutCounterSevCenterActivity extends BaseActivity implements IsFitsSystemWindows, CheckoutCounterContract.View, AppPinCheckContract.View {
    @Autowired
    String orderType;
    @Autowired
    String orderId;
    @Autowired
    String teamNum;
    @Autowired
    boolean isServiceOrder;

    public static String alipatTypeFinal = "5";//"1"
    public static String weixinpatTypeFinal = "4";//"2"
    private String payOrderId = null;
    private boolean isToPay = false;//标志是否已经跳转到支付页面

    Map<String, Object> payInfoMap = null;
    private TopBar topBar;
    private StatusLayout layoutStatus;
    private LinearLayout llPayTitle;
    private TextView tvPrice;
    private LinearLayout goodsTimeLLS;
    private TextView kickHour;
    private TextView kickMin;
    private TextView kickSec;
    private ImageTextView weixinTitle;
    private CheckBox weixinCheck;
    private TextView centerDiv;
    private ImageTextView alpayTitle;
    private CheckBox alpayCheck;
    private TextView txtSubmit;

    private boolean isNoPin = true;//不是正在拼团
    AppPinCheckPresenter appPinCheckPresenter;
    CheckoutCounterPresenter checkoutCounterPresenter;
    private float mLeftSeconds;
    private Handler mCountHandler = new Handler();
    private Runnable mCountRun = new Runnable() {
        @Override
        public void run() {
            if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
//                String content = String.format(Locale.CHINA,
//                        "支付剩余时间 %d:%d:%d",
//                        Math.max(0, (int) mLeftSeconds / 3600),
//                        Math.max(0, (int) mLeftSeconds / 60%60),
//                        Math.max(0, (int) mLeftSeconds % 60));
                kickHour.setText(String.format("%02d", Math.max(0, (int) mLeftSeconds / 3600)));
                kickMin.setText(String.format("%02d", Math.max(0, (int) mLeftSeconds / 60 % 60)));
                kickSec.setText(String.format("%02d", Math.max(0, (int) mLeftSeconds % 60)));
            }

            mLeftSeconds--;
            if (mLeftSeconds >= 0) {
                mCountHandler.postDelayed(this, 1000);
            } else {
                showToast("订单支付已超时！");
                finish();
            }
        }
    };
    private com.healthy.library.widget.DeepRadioGroup checkGroup;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_checkout_counter_all;
    }


    public void pay(View view) {
        if (payOrderId == null || TextUtils.isEmpty(payOrderId)) {//说明没拿到通联的支付ID  再获取下吧
            checkoutCounterPresenter.getPayOrderId(new SimpleHashMapBuilder<String, Object>().puts("mainOrderId", orderId));
            return;
        }
        if (!isNoPin) {
            Toast.makeText(mContext, appPinCheck.msg, Toast.LENGTH_SHORT).show();
            appPinCheckPresenter.getPinExtra(new SimpleHashMapBuilder<String, Object>().puts("orderId", orderId));
            return;
        }
        if (payInfoMap != null) {
            if (alipatTypeFinal.equals(payInfoMap.get("payType")) && alpayCheck.isChecked()) {//说明重复获取支付凭证
                onGetPayInfoSuccess(payInfoMap);
                return;
            }
            if (weixinpatTypeFinal.equals(payInfoMap.get("payType")) && weixinCheck.isChecked()) {//说明重复获取支付凭证
                onGetPayInfoSuccess(payInfoMap);
                return;
            }
            payInfoMap = null;
        }
        if (alpayCheck.isChecked()) {
            checkoutCounterPresenter.getPayInfo(new SimpleHashMapBuilder<String, Object>()
                    .puts("payOrderId", payOrderId).puts("payType", alipatTypeFinal));
        } else {//微信支付直接跳小程序
            goWeiXinPay(payOrderId);
//            checkoutCounterPresenter.getPayInfo(new SimpleHashMapBuilder<String, Object>()
//                    .puts("payOrderId", payOrderId).puts("payType", weixinpatTypeFinal));
        }
    }

    public void goWeiXinPay(String payId) {
        String appId = Ids.WX_APP_ID;
        IWXAPI api = WXAPIFactory.createWXAPI(CheckOutCounterSevCenterActivity.this, appId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = Ids.WEIXIN_PAY_ID;
        req.path = "/pages/wxpay/wxpay?fromPlace=1&payId=" + payId;
        System.out.println("小程序跳转:"+req.path);
        if (ChannelUtil.isIpRealRelease()) {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        } else {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;
        }
        api.sendReq(req);
        isToPay = true;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
        checkoutCounterPresenter = new CheckoutCounterPresenter(this, this);
        appPinCheckPresenter = new AppPinCheckPresenter(this, this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkoutCounterPresenter.getPayOrderId(new SimpleHashMapBuilder<String, Object>().puts("mainOrderId", orderId));
                if (isServiceOrder) {//说明是服务下单有可能会返回服务型的结果

//            checkoutCounterPresenter.getServerOrderData(new SimpleHashMapBuilder<String, Object>().puts("orderId",orderId));
                } else {

                    checkoutCounterPresenter.getOrderData(new SimpleHashMapBuilder<String, Object>().puts("mainOrderId", orderId));
                }
            }
        },1000);

//        alpayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    buildPayInfo();
//                }
//            }
//        });
//        weixinCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    buildPayInfo();
//
//                }
//            }
//        });
        checkGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                buildPayInfo();
            }
        });
//        appPinCheckPresenter.getPinExtra(new SimpleHashMapBuilder<String, Object>().puts("orderId",orderId));
    }

    private void buildPayInfo() {
        if (isServiceOrder) {
            if (orderServerModel != null) {
                if (alpayCheck.isChecked()) {
                    txtSubmit.setText("支付宝支付 ¥" + FormatUtils.moneyKeep2Decimals(orderServerModel.payMoney));
                } else {
                    txtSubmit.setText("微信支付 ¥" + FormatUtils.moneyKeep2Decimals(orderServerModel.payMoney));
                }
            }
        } else {
            if (orderModel != null) {
                if (alpayCheck.isChecked()) {
                    txtSubmit.setText("支付宝支付 ¥" + FormatUtils.moneyKeep2Decimals(orderModel.totalPayAmount));
                } else {
                    txtSubmit.setText("微信支付 ¥" + FormatUtils.moneyKeep2Decimals(orderModel.totalPayAmount));
                }
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isToPay && payOrderId != null) {
            showLoading();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {//延迟调用  防止通联那边回调不及时
                    checkoutCounterPresenter.getPayStatus(payOrderId);
                }
            }, 500);
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {

        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        llPayTitle = (LinearLayout) findViewById(R.id.ll_pay_title);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        goodsTimeLLS = (LinearLayout) findViewById(R.id.goodsTimeLLS);
        kickHour = (TextView) findViewById(R.id.kickHour);
        kickMin = (TextView) findViewById(R.id.kickMin);
        kickSec = (TextView) findViewById(R.id.kickSec);
        weixinTitle = (ImageTextView) findViewById(R.id.weixinTitle);
        weixinCheck = (CheckBox) findViewById(R.id.weixinCheck);
        centerDiv = (TextView) findViewById(R.id.centerDiv);
        alpayTitle = (ImageTextView) findViewById(R.id.alpayTitle);
        alpayCheck = (CheckBox) findViewById(R.id.alpayCheck);
        txtSubmit = (TextView) findViewById(R.id.txt_submit);
        checkGroup = (DeepRadioGroup) findViewById(R.id.checkGroup);
    }

    OrderList.OrderFather orderModel;
    OrderModel orderServerModel;

    @Override
    public void onServerOrderDataSuccess(OrderModel orderModel) {
//        this.orderServerModel=orderModel;
//        buildPayInfo();
//        String pattern = "yyyy-MM-dd HH:mm:ss";
//        String showPrice = String.format(Locale.CHINA, "¥ %s",
//                FormatUtils.moneyKeep2Decimals(orderModel.payMoney));
//        tvPrice.setText(FormatUtils.moneyKeep2Decimals(orderModel.payMoney)+"");
//        if(orderModel.payMoney==0){
//
//            if(!TextUtils.isEmpty(teamNum)){
//                ARouter.getInstance()
//                        .build(DiscountRoutes.DIS_GROUPDETAIL)
//                        .withString("orderId",orderId)
//                        .withString("teamNum",teamNum)
//                        .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                        .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        .navigation();
//                return;
//            }else {
//                ARouter.getInstance()
//                        .build(ServiceRoutes.SERVICE_ORDER_FINISH)
//                        .withString("orderId",orderId)
//                        .withString("orderType",orderType)
//                        .withString("teamNum",teamNum)
//                        .withBoolean("isService",isServiceOrder)
//                        .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                        .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        .navigation();
//            }
//            finish();
//        }else {
//            long totalTime = 24*60 * 60 * 1000L;
//            mLeftSeconds = (totalTime - (TimestampUtils.string2Date(new SimpleDateFormat(pattern).format(new Date()),
//                    pattern).getTime() - TimestampUtils.string2Date(orderModel.createTime, pattern).getTime())) / 1000;
//            mCountHandler.post(mCountRun);
//        }
    }

    @Override
    public void onOrderDataSuccess(com.healthy.library.model.OrderList.OrderFather orderModel) {
        this.orderModel = orderModel;
        buildPayInfo();
        tvPrice.setText(FormatUtils.moneyKeep2Decimals(orderModel.totalPayAmount) + "");
        if (Double.parseDouble(orderModel.totalPayAmount) == 0) {
            if (!TextUtils.isEmpty(teamNum)) {
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_GROUPDETAIL)
                        .withString("orderId", orderId)
                        .withString("teamNum", teamNum)
                        .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .navigation();
            } else {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDER_FINISH)
                        .withString("orderId", orderId)
                        .withString("orderType", orderType)
                        .withString("teamNum", teamNum)
                        .withBoolean("isService", isServiceOrder)
                        .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .navigation();
            }
            CheckOutCounterSevCenterActivity.super.finish();
        } else {

            long totalTime = 24 * 60 * 60 * 1000L;
            mLeftSeconds = Float.parseFloat(orderModel.residuePayTime);
            mCountHandler.post(mCountRun);
        }

    }

    public void payByAli(final String orderInfo) {
        AutoDisposeConverter<Map<String, String>> objectAutoDisposeConverter =
                AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                        .from(this, Lifecycle.Event.ON_DESTROY));

        Observable.create(
                new ObservableOnSubscribe<Map<String, String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<Map<String, String>> emitter) {
                        PayTask aliPay = new PayTask(CheckOutCounterSevCenterActivity.this);
                        Map<String, String> result = aliPay.payV2(orderInfo, true);
                        emitter.onNext(result);
                        emitter.onComplete();
                    }
                })
                .compose(RxThreadUtils.<Map<String, String>>Obs_io_main())
                .to(objectAutoDisposeConverter)
                .subscribe(new Observer<Map<String, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(final Map<String, String> map) {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                switch (map.get("resultStatus")) {
                                    case "9000":
                                        ////System.out.println("进来了1");
                                        if (!TextUtils.isEmpty(teamNum)) {
                                            ARouter.getInstance()
                                                    .build(DiscountRoutes.DIS_GROUPDETAIL)
                                                    .withString("orderId", orderId)
                                                    .withString("teamNum", teamNum)
                                                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                    .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    .navigation();
                                        } else {
                                            ARouter.getInstance()
                                                    .build(ServiceRoutes.SERVICE_ORDER_FINISH)
                                                    .withString("orderId", orderId)
                                                    .withString("orderType", orderType)
                                                    .withString("teamNum", teamNum)
                                                    .withBoolean("isService", isServiceOrder)
                                                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                    .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    .navigation();
                                        }

                                        CheckOutCounterSevCenterActivity.super.finish();
                                        break;

                                    case "6001":
                                        ////System.out.println("进来了2");
                                    default:
                                        ////System.out.println("进来了3");
                                        ARouter.getInstance()
                                                .build(ServiceRoutes.SERVICE_ORDER_FINISH)
                                                .withString("orderId", orderId)
                                                .withString("orderType", orderType)
                                                .withString("teamNum", teamNum)
                                                .withBoolean("isService", isServiceOrder)
                                                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                .navigation();

                                        CheckOutCounterSevCenterActivity.super.finish();
                                        break;
                                }
                            }
                        }, 200);

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ////System.out.println("进来了4");
                    }

                    @Override
                    public void onComplete() {
                        ////System.out.println("进来了5");

                    }
                });
    }

    @Override
    public void onGetPayInfoSuccess(Map<String, Object> map) {
        this.payInfoMap = map;
        String payType = String.valueOf(map.get("payType"));
        if (Constants.PAY_IN_A_LI.equals(payType)) {
//            payByAli(String.valueOf(map.get("ali")));
            if (String.valueOf(map.get("payInfo")).startsWith("http")) {
                Uri uri = Uri.parse(String.valueOf(map.get("payInfo")));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                isToPay = true;
            } else {
                showToast("payInfo错误" + String.valueOf(map.get("payInfo")));
            }

        } else if (Constants.PAY_IN_WX.equals(payType)) {
//            IWXAPI iwxapi = WXAPIFactory.createWXAPI(mContext, null);
//            iwxapi.registerApp(String.valueOf(map.get("appId")));
//            PayReq request = new PayReq();
//            request.appId = String.valueOf(map.get("appId"));
//            request.partnerId = String.valueOf(map.get("partnerId"));
//            request.prepayId = String.valueOf(map.get("prepayId"));
//            request.packageValue = String.valueOf(map.get("packageValue"));
//            request.nonceStr = String.valueOf(map.get("nonceStr"));
//            request.timeStamp = String.valueOf(map.get("timeStamp"));
//            request.sign = String.valueOf(map.get("sign"));
//            iwxapi.sendReq(request);
        }
    }

    @Override
    public void getPayOrderId(String orderId,String msg) {
        if (orderId != null && !TextUtils.isEmpty(orderId)) {
            payOrderId = orderId;
        } else {
            if("当前订单已付款".equals(msg)){
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDER_FINISH)
                        .withString("orderId", orderId)
                        .withString("orderType", orderType)
                        .withString("teamNum", teamNum)
                        .withBoolean("isService", isServiceOrder)
                        .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .navigation();
                CheckOutCounterSevCenterActivity.super.finish();
            }
//            showToast("获取支付信息失败");//后台报什么错就什么错吧
        }
    }
    int retryTime=0;
    @Override
    public void getPayStatusSuccess(String status) {
        if (status != null && status.equals("4")) {
            if (!TextUtils.isEmpty(teamNum)) {
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_GROUPDETAIL)
                        .withString("orderId", orderId)
                        .withString("teamNum", teamNum)
                        .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .navigation();
            } else {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDER_FINISH)
                        .withString("orderId", orderId)
                        .withString("orderType", orderType)
                        .withString("teamNum", teamNum)
                        .withBoolean("isService", isServiceOrder)
                        .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .navigation();
            }
        } else {
            if(retryTime==0){//重试一次
                retryTime=1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {//延迟调用  防止通联那边回调不及时
                        checkoutCounterPresenter.getPayStatus(payOrderId);
                    }
                }, 500);
                return;
            }
            isToPay = false;
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_ORDER_FINISH)
                    .withString("orderId", orderId)
                    .withString("orderType", orderType)
                    .withString("teamNum", teamNum)
                    .withBoolean("isService", isServiceOrder)
                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation();

        }
        CheckOutCounterSevCenterActivity.super.finish();
    }

    /**
     * 微信支付支付结果通知
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getWxPayResult(PayResultModel model) {
        switch (model.getErrCode()) {
            case 0:
                showToast("支付成功");
                if (!TextUtils.isEmpty(teamNum)) {
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_GROUPDETAIL)
                            .withString("orderId", orderId)
                            .withString("teamNum", teamNum)
                            .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .navigation();
                } else {
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_ORDER_FINISH)
                            .withString("orderId", orderId)
                            .withString("orderType", orderType)
                            .withString("teamNum", teamNum)
                            .withBoolean("isService", isServiceOrder)
                            .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .navigation();
                }

                CheckOutCounterSevCenterActivity.super.finish();
                break;

            case -2:
            default:
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDER_FINISH)
                        .withString("orderId", orderId)
                        .withString("orderType", orderType)
                        .withString("teamNum", teamNum)
                        .withBoolean("isService", isServiceOrder)
                        .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .navigation();

                CheckOutCounterSevCenterActivity.super.finish();
                break;
        }
    }

    @Override
    public void finish() {

        StyledDialog.init(mContext);
        if ("-1".equals(orderType)) {
            StyledDialog.buildIosAlert("确定要离开收银台？", "您的订单在规定时间内未支付将被取消，请尽快完成支付", new MyDialogListener() {
                @Override
                public void onFirst() {

                }

                @Override
                public void onThird() {
                    super.onThird();
                }

                @Override
                public void onSecond() {

                    CheckOutCounterSevCenterActivity.super.finish();
                    ARouter.getInstance().build(ServiceRoutes.SERVICE_USERORDER)
                            .withString("type", Constants.ORDER_TYPE_ALL)
                            .navigation();

                }
            }).setCancelable(true, true).setBtnColor(com.health.servicecenter.R.color.dialogutil_text_black, com.health.servicecenter.R.color.colorPrimaryDark, 0).setBtnText("继续支付", "确认离开").show();
        } else {
            StyledDialog.buildIosAlert("确定要离开收银台？", "您的订单在规定时间内未支付将被取消，请尽快完成支付", new MyDialogListener() {
                @Override
                public void onFirst() {

                }

                @Override
                public void onThird() {
                    super.onThird();
                }

                @Override
                public void onSecond() {

                    CheckOutCounterSevCenterActivity.super.finish();
                    ARouter.getInstance().build(ServiceRoutes.SERVICE_USERORDER)
                            .withString("type", Constants.ORDER_TYPE_ALL)
                            .navigation();

                }
            }).setCancelable(true, true).setBtnColor(com.health.servicecenter.R.color.dialogutil_text_black, com.health.servicecenter.R.color.colorPrimaryDark, 0).setBtnText("继续支付", "确认离开").show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mCountHandler.removeCallbacks(mCountRun);
    }

    AppPinCheck appPinCheck;

    @Override
    public void onSucessGetPinExtra(AppPinCheck appPinCheck) {
        isNoPin = true;
        if (appPinCheck != null) {
            this.appPinCheck = appPinCheck;
            if (appPinCheck.nocanPay) {
                isNoPin = false;
            }
        }
    }
}
