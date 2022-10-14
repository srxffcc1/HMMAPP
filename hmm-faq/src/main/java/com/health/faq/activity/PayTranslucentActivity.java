package com.health.faq.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.Lifecycle;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alipay.sdk.app.PayTask;
import com.health.faq.R;
import com.health.faq.contract.PayTranslucentContract;
import com.health.faq.presenter.PayTranslucentPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Events;
import com.healthy.library.dialog.SimpleDialog;
import com.healthy.library.interfaces.IsTranslucent;
import com.healthy.library.model.PayResultModel;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.DecimalFormatUtils;
import com.healthy.library.widget.PayDialog;
import com.healthy.library.interfaces.PaySuccessFinish;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
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
 * @author Li
 * @date 2019/07/04 13:44
 * @des 支付界面
 */
@Route(path = FaqRoutes.FAQ_PAY_TRANSLUCENT)
public class PayTranslucentActivity extends BaseActivity implements View.OnClickListener,
        PayTranslucentContract.View, PaySuccessFinish, IsTranslucent {

    @Autowired
    String id;
    @Autowired
    int payType;
    @Autowired
    String title;
    @Autowired
    String content;
    @Autowired
    String[] filePaths;
    @Autowired
    String price;
    @Autowired
    boolean isAnonymous;
    @Autowired
    String questionId;

    private TextView mTvTitle;
    private TextView mTvPrice;
    private Group mGroupBalance;
    private TextView mTvBalance;
    private ImageView mIvWx;
    private ImageView mIvZfb;
    private ImageView mIvHdd;
    private View mWxView;
    private View mZfbView;
    private View mHddView;
    private PayTranslucentPresenter mPresenter;
    private List<ImageView> mPayWayIvs;
    private boolean mNeedSetPwd;
    private PayDialog payDialog;
    private long mClickTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_translucent;
    }

    @Override
    protected void findViews() {
        mTvTitle = findViewById(R.id.tv_title);
        mTvPrice = findViewById(R.id.tv_price);
        mGroupBalance = findViewById(R.id.group_balance);
        mTvBalance = findViewById(R.id.tv_pay_hdd);
        mIvWx = findViewById(R.id.iv_wx);
        mIvZfb = findViewById(R.id.iv_zfb);
        mIvHdd = findViewById(R.id.iv_hdd);

        mWxView = findViewById(R.id.view_wx_click);
        mZfbView = findViewById(R.id.view_ali_click);
        mHddView = findViewById(R.id.view_hdd_click);

        mWxView.setOnClickListener(this);
        mZfbView.setOnClickListener(this);
        mHddView.setOnClickListener(this);
        findViewById(R.id.tv_pay).setOnClickListener(this);
        findViewById(R.id.view_finish).setOnClickListener(this);
        mIvWx.setSelected(true);

        mPayWayIvs = new ArrayList<>();
        mPayWayIvs.add(mIvWx);
        mPayWayIvs.add(mIvZfb);
        mPayWayIvs.add(mIvHdd);

    }

    @Override
    public void showDataErr() {

    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onCheckBalanceFail() {
        finish();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
        mPresenter = new PayTranslucentPresenter(PayTranslucentActivity.this, this);
        mPresenter.checkBalanceEnough(price);

        String prefix = Constants.PAY_ASK_EXPERT == payType ? "问专家：" : "悬赏求助：";
        SpannableString spannableString = new SpannableString(
                String.format("%s%s", prefix, title));
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(styleSpan, 0, prefix.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTvTitle.setText(spannableString);
        mTvPrice.setText(String.format("¥ %s", price));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_pay) {
            if (System.currentTimeMillis() - mClickTime < 1000) {
                return;
            }
            mClickTime = System.currentTimeMillis();
            MobclickAgent.onEvent(mContext, Events.EVENT_PAY);
            if (Constants.REWARD_PAY_IN_WX.equals(String.valueOf(getPayType())) ||
                    Constants.REWARD_PAY_IN_ALI.equals(String.valueOf(getPayType()))) {
                if (Constants.PAY_REWARD == payType) {
                    submitRewardWithPic();
                } else if (Constants.PAY_ASK_EXPERT == payType) {
                    submitExpertWithPic();
                } else {
                    mPresenter.submitImproveReward(price, questionId, String.valueOf(getPayType()));
                }

            } else {
                verifyHdd();
            }


        } else if (v.getId() == R.id.view_finish) {
            finish();
        } else {
            mIvWx.setSelected(v == mWxView);
            mIvZfb.setSelected(v == mZfbView);
            mIvHdd.setSelected(v == mHddView);
        }

    }

    private void verifyHdd() {
        if (mNeedSetPwd) {
            //密码未设置
            payDialog = PayDialog.newInstance(2);
        } else {
            //密码已设置
            payDialog = PayDialog.newInstance(1);
        }
        payDialog.paySuccessFinish(this);
        payDialog.show(getSupportFragmentManager(), "payDialog");

    }

    @Override
    public void onCheckBalanceSuccess(String balance, boolean canPay, boolean needSetPwd) {
        mNeedSetPwd = needSetPwd;
        if (canPay) {
            mGroupBalance.setVisibility(View.VISIBLE);
            mTvBalance.setText(String.format("憨豆豆余额 %s",
                    DecimalFormatUtils.formatMoney(balance)));
        } else {
            mGroupBalance.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUploadPictureSuccess(String[] urls) {
        if (Constants.PAY_REWARD == payType) {
            submitReward(urls);
        } else if (Constants.PAY_ASK_EXPERT == payType) {
            submitExpertAsk(urls);
        }
    }

    @Override
    public void onGetOrderInfoSuccess(String payType, String aliInfo, Map<String, String> wxInfo) {
        switch (payType) {
            case Constants.REWARD_PAY_IN_WX:
                payByWx(wxInfo);
                break;
            case Constants.REWARD_PAY_IN_ALI:
                payByAli(aliInfo);
                break;
            case Constants.REWARD_PAY_IN_HDD:
                setResult(RESULT_OK);
                showToastIgnoreLife("支付成功");
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 微信支付
     *
     * @param map 支付信息
     */
    private void payByWx(Map<String, String> map) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(mContext, null);
        iwxapi.registerApp(String.valueOf(map.get("appId")));
        PayReq request = new PayReq();
        request.appId = String.valueOf(map.get("appId"));
        request.partnerId = String.valueOf(map.get("partnerId"));
        request.prepayId = String.valueOf(map.get("prepayId"));
        request.packageValue = String.valueOf(map.get("packageValue"));
        request.nonceStr = String.valueOf(map.get("nonceStr"));
        request.timeStamp = String.valueOf(map.get("timeStamp"));
        request.sign = String.valueOf(map.get("sign"));
        iwxapi.sendReq(request);
    }

    /**
     * 支付宝支付
     *
     * @param orderInfo 订单信息
     */
    public void payByAli(final String orderInfo) {
        AutoDisposeConverter<Map<String, String>> objectAutoDisposeConverter = AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
        Observable.create(
                new ObservableOnSubscribe<Map<String, String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<Map<String, String>> emitter) {
                        PayTask aliPay = new PayTask(PayTranslucentActivity.this);
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
                    public void onNext(Map<String, String> map) {
                        switch (map.get("resultStatus")) {
                            case "9000":
                                showToastIgnoreLife("支付成功");
                                setResult(RESULT_OK);
                                finish();
                                break;
                            case "6001":
                                if (Constants.PAY_REWARD == payType) {
                                    showPayConfirmDialog();
                                } else {
                                    showToastIgnoreLife("支付失败,请重新支付");
                                }
                                break;
                            default:
                                showToastIgnoreLife("支付失败,请重新支付");
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 提交问答
     *
     * @param fileUrl 问答图片
     */
    private void submitReward(String[] fileUrl) {
        mPresenter.submitReward(price, title,
                content, isAnonymous ? "2" : "1",
                fileUrl, String.valueOf(getPayType()));
    }

    /**
     * 提交专家问答
     */
    private void submitExpertAsk(String[] fileUrl) {
        mPresenter.submitExpert(id, price, content, isAnonymous ? "2" : "1", fileUrl,
                String.valueOf(getPayType()));
    }

    /**
     * 提交时判断是否有图片
     */
    private void submitRewardWithPic() {
        if (filePaths.length == 0) {
            submitReward(new String[0]);
        } else {
            String[] base64 = new String[filePaths.length];
            for (int i = 0; i < filePaths.length; i++) {
                base64[i] = BitmapUtils.bitmap2Base64(filePaths[i]);
            }
            mPresenter.uploadPictures(base64);
        }
    }

    /**
     * 问专家判断是否有图片
     */
    private void submitExpertWithPic() {
        if (filePaths.length == 0) {
            submitExpertAsk(new String[0]);
        } else {
            String[] base64 = new String[filePaths.length];
            for (int i = 0; i < filePaths.length; i++) {
                base64[i] = BitmapUtils.bitmap2Base64(filePaths[i]);
            }
            mPresenter.uploadPictures(base64);
        }
    }

    /**
     * 微信支付支付结果通知
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getWxPayResult(PayResultModel model) {
        if (model.getErrCode() == 0) {
            showToastIgnoreLife("支付成功");
            setResult(RESULT_OK);
            finish();
        } else {
            if (Constants.PAY_REWARD == payType) {
                showPayConfirmDialog();
            } else {
                showToastIgnoreLife("支付失败,请重新支付");
            }
        }
    }

    private void showPayConfirmDialog() {
        new SimpleDialog.Builder(mContext)
                .setTitle("离求助成功还差一步,确定离开支付？")
                .setTitleGravity(Gravity.CENTER)
                .setPositiveBtn("继续支付", null)
                .setNegativeBtn("放弃支付", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).create().show();
    }

    private int getPayType() {
        for (ImageView payWayIv : mPayWayIvs) {
            if (payWayIv.isSelected()) {
                return mPayWayIvs.indexOf(payWayIv);
            }
        }
        return 0;
    }


    @Override
    public void passwordRight() {
        payDialog.dismiss();
        if (Constants.PAY_REWARD == payType) {
            submitRewardWithPic();
        } else if (Constants.PAY_ASK_EXPERT == payType) {
            submitExpertWithPic();
        } else {
            mPresenter.submitImproveReward(price, questionId, String.valueOf(getPayType()));
        }
    }

    @Override
    public void onBackPressed() {
        MobclickAgent.onEvent(mContext, Events.EVENT_CLOSE_PAY);
        super.onBackPressed();
    }
}
