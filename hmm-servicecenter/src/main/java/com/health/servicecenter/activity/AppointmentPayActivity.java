package com.health.servicecenter.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alipay.sdk.app.PayTask;
import com.health.servicecenter.R;
import com.health.servicecenter.contract.AppointmentMainContract;
import com.health.servicecenter.presenter.AppointmentMainPresenter;
import com.health.servicecenter.widget.AppointmentSelectDateDialog;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Ids;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.TongLianPhoneContract;
import com.healthy.library.dialog.SelectTechnicianDateDialog;
import com.healthy.library.dialog.TongLianPhoneBindDialog;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.model.AppointmentTimeSettingModel;
import com.healthy.library.model.PayResultModel;
import com.healthy.library.model.ServerDateTool;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.TongLianMemberData;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.presenter.TongLianPhonePresenter;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.CheckUtils;
import com.healthy.library.utils.FastClickUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


/**
 * @author Long
 */
@Route(path = ServiceRoutes.SERVICE_APPOINTMENTPAY)
public class AppointmentPayActivity extends BaseActivity implements View.OnClickListener, AppointmentMainContract.View, TongLianPhoneContract.View {
    private TopBar topBar;
    private ImageView goodsImg;
    private TextView goodsTitle;
    private TextView goodsPrice;
    private TextView goodsSpace;
    private TextView goodsTime;
    private ImageTextView storeName;
    private TextView selectTime;
    private TextView technicianTitle;
    private TextView selectTechnician;
    private ImageTextView appointment;
    private EditText appointmentName;
    private EditText appointmentPhone;
    private TextView remark;
    private EditText remarkEdit;
    private TextView editNum;
    private TextView mPayType;
    private TextView totalPrice;
    private TextView mSubmit;
    private SelectTechnicianDateDialog selectTechnicianDateDialog;
    private AppointmentSelectDateDialog appointmentSelectDateDialog;
    private ImageTextView mWeiXinTitle;
    private CheckBox mWeiXinCheck;
    private ImageTextView mAlPayTitle;
    private CheckBox mAlPayCheck;
    //2	微信	1 支付宝  Long：2021-04-24 修复默认选中微信支付
    private String mPayTypeString = Constants.PAY_IN_WX;
    private int currenIndex = 0, currenPosition = -1;
    // 1 到店支付 2 在线支付
    private int payType;

    @Autowired
    String projectId;
    @Autowired
    String merchantId;
    @Autowired
    ShopDetailModel storeDetail;
    @Autowired
    AppointmentMainItemModel.AttributeList attribute;

    private AppointmentMainPresenter mMainPresenter;
    private Map<String, Object> mMap = new HashMap<>();
    private List<AppointmentMainItemModel.TechnicianList> mTechnicianList;
    private AppointmentMainItemModel.TechnicianList mTechnicianModel;
    private ConstraintLayout mPayLayout;
    private StatusLayout mStatusLayout;
    private ViewGroup mLayout;
    private String mShopName;
    private String mAppointmentDate;
    private String mShopTimeSettingId;
    private long mAppointmentId;
    private String payOrderId;

    private boolean isToPay = false;//标志是否已经跳转到支付页面

    private TongLianPhonePresenter tongLianPhonePresenter = null;
    TongLianPhoneBindDialog tongLianPhoneBindDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appointment_pay;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);

        mMainPresenter = new AppointmentMainPresenter(this, this);
        tongLianPhonePresenter = new TongLianPhonePresenter(this, this);
        if (!TextUtils.isEmpty(storeDetail.chainShopName)) {
            mShopName = storeDetail.shopName + "(" + storeDetail.chainShopName + ")";
        } else {
            mShopName = storeDetail.shopName;
        }
        mMap.put("projectId", projectId);
        mMap.put("shopId", storeDetail.id);
        mMap.put("shopName", mShopName);
        mMap.put("merchantId", merchantId);

        //获取信息赋值并移动光标
        appointmentName.setText(SpUtils.getValue(mContext, SpKey.USER_NICK));
        appointmentPhone.setText(SpUtils.getValue(mContext, SpKey.PHONE));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //获取当天时间 格式（yyyy-MM-dd)
        Calendar calendar = Calendar.getInstance();
        mAppointmentDate = dateFormat.format(calendar.getTime());

        appointmentSelectDateDialog = AppointmentSelectDateDialog.newInstance(currenIndex, currenPosition, projectId, storeDetail.id);
        appointmentSelectDateDialog.setResultTimeListener(new AppointmentSelectDateDialog.getTimeListener() {
            @Override
            public void resultTime(ServerDateTool date, AppointmentTimeSettingModel.ShopTimeSetting shopTimeSetting, int ind, int pos) {
                currenIndex = ind;
                currenPosition = pos;

                mAppointmentDate = date.getDate();
                mShopTimeSettingId = shopTimeSetting.getId();

                String monthAddDay = date.getmMonthAddmDay();
                if (monthAddDay.contains("(")) {
                    int i = monthAddDay.indexOf("(");
                    monthAddDay = monthAddDay.substring(0, i);
                }
                selectTime.setText(monthAddDay + "\u3000" + shopTimeSetting.getDateStr());
            }
        });
        selectTechnicianDateDialog = SelectTechnicianDateDialog.newInstance();
        selectTechnicianDateDialog.setOnConfirmClick(new SelectTechnicianDateDialog.OnSelectConfirm() {
            @Override
            public void onClick(int currentPosition) {
                mTechnicianModel = mTechnicianList.get(currentPosition);
                if (mTechnicianModel == null) {
                    selectTechnician.setHint("请选择技师");
                } else {
                    selectTechnician.setText(mTechnicianModel.getNickname());
                }
            }
        });
        getData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getData() {
        super.getData();
        mMainPresenter.getMainDetail(mMap);
        tongLianPhonePresenter.getTongLianPhoneStatus(new SimpleHashMapBuilder<String, Object>());
    }

    @Override
    protected void findViews() {
        super.findViews();
        topBar = (TopBar) findViewById(R.id.top_bar);
        goodsImg = (ImageView) findViewById(R.id.goodsImg);
        goodsTitle = (TextView) findViewById(R.id.goodsTitle);
        goodsPrice = (TextView) findViewById(R.id.goodsPrice);
        goodsSpace = (TextView) findViewById(R.id.goodsSpace);
        goodsTime = (TextView) findViewById(R.id.goodsTime);
        storeName = (ImageTextView) findViewById(R.id.storeName);
        selectTime = (TextView) findViewById(R.id.selectTime);
        technicianTitle = (TextView) findViewById(R.id.technicianTitle);
        selectTechnician = (TextView) findViewById(R.id.selectTechnician);
        appointment = (ImageTextView) findViewById(R.id.appointment);
        appointmentName = findViewById(R.id.appointmentName);
        appointmentPhone = findViewById(R.id.appointmentPhone);
        remark = (TextView) findViewById(R.id.remark);
        remarkEdit = (EditText) findViewById(R.id.remarkEdit);
        editNum = (TextView) findViewById(R.id.editNum);
        mPayType = (TextView) findViewById(R.id.payType);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        mSubmit = (TextView) findViewById(R.id.tv_submit);
        mPayLayout = findViewById(R.id.appointment_pay_layout);
        mStatusLayout = findViewById(R.id.layout_status);
        setStatusLayout(mStatusLayout);
        mLayout = findViewById(R.id.layout);

        mWeiXinTitle = findViewById(R.id.weixinTitle);
        mWeiXinCheck = findViewById(R.id.weixinCheck);
        mAlPayTitle = findViewById(R.id.alpayTitle);
        mAlPayCheck = findViewById(R.id.alpayCheck);

        initListener();

    }

    private void initListener() {
        selectTime.setOnClickListener(this);
        selectTechnician.setOnClickListener(this);
        mWeiXinTitle.setOnClickListener(this);
        mAlPayTitle.setOnClickListener(this);
        //提交预约
        mSubmit.setOnClickListener(this);

        remarkEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 50) {
                    remarkEdit.setText(s.subSequence(0, 50));
                }
                editNum.setText(s.length() + "/50");
            }
        });
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.selectTechnician) {
            if (!FastClickUtils.isFastClick()) {
                if (ListUtil.isEmpty(mTechnicianList)) {
                    return;
                }
                selectTechnicianDateDialog.setTechnicianList(mTechnicianList);
                selectTechnicianDateDialog.show(getSupportFragmentManager(), "selectDate");
            }
        } else if (vId == R.id.selectTime) {
            if (!FastClickUtils.isFastClick()) {
                appointmentSelectDateDialog.setPosition(currenIndex, currenPosition, mAppointmentDate);
                appointmentSelectDateDialog.show(getSupportFragmentManager(), "serviceDate");
            }
        } else if (vId == R.id.weixinTitle) {
            mPayTypeString = Constants.PAY_IN_WX;
            mWeiXinCheck.setChecked(true);
            mAlPayCheck.setChecked(false);
        } else if (vId == R.id.alpayTitle) {
            mPayTypeString = Constants.PAY_IN_A_LI;
            mWeiXinCheck.setChecked(false);
            mAlPayCheck.setChecked(true);
        } else if (vId == R.id.tv_submit) {
            final String mAppointmentName = appointmentName.getText().toString().trim();
            final String mAppointmentPhone = appointmentPhone.getText().toString().trim();
            final String mRemarkBody = remarkEdit.getText().toString().trim();
            if (TextUtils.isEmpty(mAppointmentDate) || TextUtils.isEmpty(mShopTimeSettingId)) {
                showToast("请选择预约时间");
                return;
            }
            if (TextUtils.isEmpty(mAppointmentName)) {
                showToast("请输入预约人姓名");
                return;
            }
            if (TextUtils.isEmpty(mAppointmentPhone)) {
                showToast("请输入预约人手机号");
                return;
            }
            if (!CheckUtils.checkPhone(mAppointmentPhone)) {
                showToast("请输入正确的手机号");
                return;

            }
            if (payType == 2 && TextUtils.isEmpty(mPayTypeString)) {
                showToast("请选择支付方式");
                return;
            }
            //说明有技师并且他没选
            if (!ListUtil.isEmpty(mTechnicianList) && mTechnicianModel == null) {
                showToast("请选择技师");
                return;
            }
            if (payType == 2) {


                TongLianMemberData tongLianMemberData= ObjUtil.getObj(SpUtils.getValue(LibApplication.getAppContext(),SpKey.TONGLIANBIZUSER),TongLianMemberData.class);
                if(tongLianMemberData!=null&&tongLianMemberData.memberInfo.isPhoneChecked) {//绑定了

                    buildService(mAppointmentName, mAppointmentPhone, mRemarkBody);
                }else {
                    if (tongLianPhoneBindDialog == null) {
                        tongLianPhoneBindDialog = TongLianPhoneBindDialog.newInstance();
                    }
                    tongLianPhoneBindDialog.show(getSupportFragmentManager(), "手机绑定");
                    tongLianPhoneBindDialog.setOnDialogAgreeClickListener(new TongLianPhoneBindDialog.OnDialogAgreeClickListener() {
                        @Override
                        public void onDialogAgree() {
                            buildService(mAppointmentName, mAppointmentPhone, mRemarkBody);
                        }
                    });
                }
            } else {
                buildService(mAppointmentName, mAppointmentPhone, mRemarkBody);
            }


            //模拟跳转结果页面
            /*ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_APPOINTMENTPAYRESULT)
                    .withBoolean("isSuccess", "1".equals(mPayTypeString))
                    .navigation();*/

        }
    }

    private void buildService(String mAppointmentName, String mAppointmentPhone, String mRemarkBody) {
        mMap.clear();
        mMap.put("projectAttributeId", String.valueOf(attribute.getId()));
        mMap.put("shopId", storeDetail.id);
        mMap.put("shopName", mShopName);
        mMap.put("memberName", mAppointmentName);
        mMap.put("memberNickname", SpUtils.getValue(mContext, SpKey.USER_NICK));
        mMap.put("memberPhone", mAppointmentPhone);
        //预约日期
        mMap.put("appointmentDate", mAppointmentDate);
        //预约时间范围
        mMap.put("shopTimeSettingId", mShopTimeSettingId);
        if (mTechnicianModel != null) {
            //技师id
            mMap.put("technicianId", String.valueOf(mTechnicianModel.getId()));
        }
        //备注
        mMap.put("memberRemark", mRemarkBody);
        mMap.put("merchantId", merchantId);
        mMainPresenter.addService(mMap);
    }

    @Override
    public void onGetMainListSuccess(List<AppointmentMainItemModel> modelList) {

    }

    @Override
    public void onGetStoreDetailSuccess(ShopDetailModel shopDetailModel) {

    }

    @Override
    public void onGetMainDetailSuccess(AppointmentMainItemModel detailModel) {

        try {
            payType = detailModel.getPayType();
            if (1 == payType) {
                mPayType.setText("到店付款");
                mPayLayout.setVisibility(View.GONE);
            } else {
                mPayType.setText("在线支付");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        goodsTitle.setText(detailModel.getName());
        String price = FormatUtils.moneyKeep2Decimals(attribute.getPrice());
        goodsPrice.setText("¥" + price);
        totalPrice.setText("¥" + price);
        storeName.setText(mShopName);
        //售价规格（1:单品 2:多规格)
        goodsSpace.setVisibility(TextUtils.isEmpty(detailModel.getPriceType()) || "1".equals(detailModel.getPriceType()) ? View.GONE : View.VISIBLE);
        //规格
        goodsSpace.setText(attribute.getName());

        List<String> imgList = detailModel.getImgList();
        String url = "";
        if (!ListUtil.isEmpty(imgList)) {
            url = imgList.get(0);
        }

        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(url)
                .error(R.drawable.img_1_1_default)
                .placeholder(R.drawable.img_1_1_default)
                .into(goodsImg);

        goodsTime.setText(detailModel.getDuration() + "分钟");

        mTechnicianList = detailModel.getTechnicianList();
        if (ListUtil.isEmpty(mTechnicianList)) {
            technicianTitle.setVisibility(View.GONE);
            selectTechnician.setVisibility(View.GONE);
        } else {
            technicianTitle.setVisibility(View.VISIBLE);
            selectTechnician.setVisibility(View.VISIBLE);
            //如果有默认取第一个
            //mTechnicianModel = mTechnicianList.get(0);
            //selectTechnician.setText(mTechnicianModel.getNickname());
        }

        mLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetTimeSettingSuccess(AppointmentTimeSettingModel timeSettingModel) {

    }

    @Override
    public void onGetPayInfoSuccess(Map<String, Object> payInfoMap) {
        pay(payInfoMap);
    }

    @Override
    public void onAddServiceSuccess(long id, String payOrderId) {
        mAppointmentId = id;
        this.payOrderId = payOrderId;
        //新增预约单成功
        if (payType == 1) {
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_APPOINTMENTRESULT)
                    .withLong("id", id)
                    .navigation();
            finish();
            return;
        }
        if (payOrderId == null || TextUtils.isEmpty(payOrderId)) {
            showToast("获取支付信息失败");
            return;
        }
        if (mPayTypeString.equals(Constants.PAY_IN_WX)) {
            goWeiXinPay(payOrderId);

        } else {
            mMap.clear();
            mMap.put("payOrderId", payOrderId);
            mMap.put("payType", mPayTypeString);
            mMainPresenter.getPayInfo(mMap);
        }
    }

    @Override
    public void getPayStatusSuccess(String status) {
        boolean isSuccess = false;
        if (status != null && status.equals("4")) {
            showToast("支付成功");
            isSuccess = true;
        } else {
            isToPay = false;
            isSuccess = false;
        }
        ARouter.getInstance()
                .build(ServiceRoutes.SERVICE_APPOINTMENTPAYRESULT)
                .withLong("id", mAppointmentId)
                .withBoolean("isSuccess", isSuccess)
                .navigation();
        finish();
    }

    public void goWeiXinPay(String payId) {
        String appId = Ids.WX_APP_ID;
        IWXAPI api = WXAPIFactory.createWXAPI(this, appId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = Ids.WEIXIN_PAY_ID;
        req.path = "/pages/wxpay/wxpay?fromPlace=1&payId=" + payId;
        if (ChannelUtil.isIpRealRelease()) {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        } else {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;
        }
        api.sendReq(req);
        isToPay = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isToPay && payOrderId != null) {
            showLoading();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {//延迟调用  防止通联那边回调不及时
                    mMainPresenter.getPayStatus(payOrderId);
                }
            }, 1000);
        }
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
    }

    private void pay(Map<String, Object> map) {
        //在线支付
        if (Constants.PAY_IN_A_LI.equals(mPayTypeString)) {
            //支付宝
//            payByAli(String.valueOf(map.get("ali")));
            if (String.valueOf(map.get("payInfo")).startsWith("http")) {
                Uri uri = Uri.parse(String.valueOf(map.get("payInfo")));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
                isToPay = true;
            } else {
                showToast("payInfo错误" + String.valueOf(map.get("payInfo")));
            }
        } else if (Constants.PAY_IN_WX.equals(mPayTypeString)) {
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

    public void payByAli(final String orderInfo) {
        AutoDisposeConverter<Map<String, String>> objectAutoDisposeConverter =
                AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                        .from(this, Lifecycle.Event.ON_DESTROY));
        Observable.create(
                new ObservableOnSubscribe<Map<String, String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<Map<String, String>> emitter) {
                        PayTask aliPay = new PayTask(mActivity);
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
                                boolean isSuccess = false;
                                switch (map.get("resultStatus")) {
                                    case "9000":
                                        //成功
                                        showToast("支付成功");
                                        isSuccess = true;
                                        ////System.out.println("进来了 9000");
                                        break;
                                    case "6001":
                                        isSuccess = false;
                                        ////System.out.println("进来了 6001");
                                        break;
                                    default:
                                        isSuccess = false;
                                        ////System.out.println("进来了 default");
                                        break;
                                }
                                ARouter.getInstance()
                                        .build(ServiceRoutes.SERVICE_APPOINTMENTPAYRESULT)
                                        .withLong("id", mAppointmentId)
                                        .withBoolean("isSuccess", isSuccess)
                                        .navigation();
                                finish();
                            }
                        }, 200);

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ////System.out.println("进来了 onError");
                    }

                    @Override
                    public void onComplete() {
                        ////System.out.println("进来了 onComplete");

                    }
                });
    }


    /**
     * 微信支付支付结果通知
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getWxPayResult(PayResultModel model) {
        switch (model.getErrCode()) {
            case 0:
                showToast("支付成功");
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_APPOINTMENTPAYRESULT)
                        .withLong("id", mAppointmentId)
                        .withBoolean("isSuccess", true)
                        .navigation();
                break;
            default:
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_APPOINTMENTPAYRESULT)
                        .withLong("id", mAppointmentId)
                        .withBoolean("isSuccess", false)
                        .navigation();
                break;
        }
        finish();
    }

    @Override
    public void onSucessSendCode(String result) {

    }

    @Override
    public void onSucessBindPhone(String result) {

    }

    @Override
    public void onSuccessTongLianPhoneStatus(TongLianMemberData tongLianMemberData) {
    }
}