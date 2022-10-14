package com.health.mine.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alipay.sdk.app.PayTask;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.mine.R;
import com.health.mine.adapter.AutoPollAdapter;
import com.health.mine.contract.HanMomContract;
import com.health.mine.fragment.HanMomFragment;
import com.health.mine.model.HanMomInfoModel;
import com.health.mine.presenter.HanMomPresenter;
import com.health.mine.widget.CustomViewPager;
import com.health.mine.widget.HanMomPayDialog;
import com.healthy.library.LibApplication;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.HanMomNoStoreDialog;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.Ids;
import com.healthy.library.constant.SpKey;
import com.healthy.library.dialog.TongLianPhoneBindDialog;
import com.healthy.library.model.PayResultModel;
import com.healthy.library.model.TongLianMemberData;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.AutoPollRecyclerView;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

@Route(path = MineRoutes.MINE_HANMOM)
public class HanMonActivity extends BaseActivity implements HanMomContract.View {
    @Autowired
    String teamNum;

    private SlidingTabLayout slidingTabLayout;
    private CustomViewPager viewPager;
    private TextView priceTxt, goodsPrice, goodsDays, bottomDays, goWx, userNickName,
            userNickName2, userLabel, userLabel2, userContent, userContent2, addBtn, userContentPrice, userContentPrice2,
            userContentPriceEnd, userContentPriceEnd2;
    private ConstraintLayout bottom_toPay, top, top2, rightsLayout;
    private CornerImageView userImg, userImg2;
    private LinearLayout rightsLiner;


    private List<Fragment> fragments = new ArrayList<>();
    private FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    private int currentIndex = 0;
    private AutoPollRecyclerView autoPollRecyclerView;
    private HanMomPresenter hanMomPresenter;
    private Map<String, Object> map = new HashMap<>();
    private String WEIXIN_XIAOCHENGXU_ID = "gh_93d673cec6a8";
    private HanMomPayDialog hanMomPayDialog;
    private HanMomInfoModel.SettingModel model;

    private Map<String, Object> payInfoMap = null;
    private ImageTextView bottomLocalTxt, localTxt;
    private boolean isVip = false;
    private long mills = System.currentTimeMillis();
    public String partnerId;
    public String payOrderId = null;
    private boolean isToPay = false;//标志是否已经跳转到支付页面
    TongLianPhoneBindDialog tongLianPhoneBindDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_han_mon;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "憨妈赚宣传页浏览时长");
        nokmap.put("time", (System.currentTimeMillis() - mills) / 1000);
        MobclickAgent.onEvent(mContext, "event_APP_Hanmom_Stop", nokmap);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        hanMomPresenter = new HanMomPresenter(this, this);
        getData();
    }

    private void buildFragment(String partnerId) {
        List<String> titles = Arrays.asList("爆款商品", "精选服务");
        fragments.clear();
        fragments.add(HanMomFragment.newInstance("2", partnerId));//1服务 2标品
        fragments.add(HanMomFragment.newInstance("1", partnerId));
        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), fragments, titles);
            viewPager.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
            fragmentPagerItemAdapter.notifyDataSetChanged();
        }
        viewPager.setOffscreenPageLimit(fragments.size());
        slidingTabLayout.setViewPager(viewPager);
        viewPager.setCurrentItem(currentIndex);
        slidingTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentIndex = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }


    @Override
    public void getData() {
        super.getData();
        showLoading();
        map.put("lat", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        map.put("lng", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
//        map.put("lat", "36.322778");
//        map.put("lng", "102.834437");
        hanMomPresenter.getInfo(map);
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
    public void getPayInfoSuccess(String payOrderId, String payType) {
        if (payOrderId != null && payType != null) {
            this.payOrderId = payOrderId;
            if (payType.equals(Constants.PAY_IN_A_LI)) {
                showLoading();
                hanMomPresenter.getAliPayUrl(new SimpleHashMapBuilder<String, Object>().puts("payOrderId", payOrderId).puts("payType", payType));
            }
            if (payType.equals(Constants.PAY_IN_WX)) {
                showLoading();
                goWeiXinPay(payOrderId);
            }
            if (hanMomPayDialog != null) {
                hanMomPayDialog.dismiss();
            }
        } else {
            showToast("获取支付信息失败");
        }
//        this.payInfoMap = map;
//        String payType = String.valueOf(map.get("payType"));
//        if (Constants.PAY_IN_A_LI.equals(payType)) {
//            payByAli(String.valueOf(map.get("ali")));
//        } else if (Constants.PAY_IN_WX.equals(payType)) {
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
//        }
    }

    @Override
    public void getAliPayUrlSuccess(String url) {
        if (url != null) {
            if (url.startsWith("http") || url.startsWith("https")) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
                isToPay = true;
            } else {
                showToast("payInfo错误" + url);
            }
        } else {
            showToast("获取支付信息错误");
        }
    }

    @Override
    public void getPayStatusSuccess(String status) {
        if (status != null && status.equals("4")) {
            showToast("支付成功");
            if (hanMomPayDialog != null) {
                hanMomPayDialog.dismiss();
            }
            goWeiXinAPP();
            finish();
        } else {
            isToPay = false;
            showToast("支付失败");
            if (hanMomPayDialog != null) {
                hanMomPayDialog.dismiss();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isToPay) {
            showLoading();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {//延迟调用  防止通联那边回调不及时
                    hanMomPresenter.getPayStatus(payOrderId);
                }
            }, 1000);
        }
    }

    @Override
    public void onGetInfoSuccess(HanMomInfoModel.MemberInfoModel memberInfoModel,
                                 List<HanMomInfoModel.StarListModel> starList,
                                 List<HanMomInfoModel.ScrollListModel> scrollList,
                                 HanMomInfoModel.SettingModel settingModel,
                                 HanMomInfoModel.PartnerModel partnerModel,
                                 final HanMomInfoModel.LastPartnerModel lastPartnerModel,
                                 List<HanMomInfoModel.RightsListModel> rights) {
        showContent();
        if (memberInfoModel != null) {
            if (memberInfoModel.partnerId != null && !TextUtils.isEmpty(memberInfoModel.partnerId)) {
                isVip = true;// 6. 当memberInfo存在，且memberInfo的partnerId不为空或NULL时，跳转小程序。
            } else {
                isVip = false;
            }
        }
        buildStartList(starList);
        buildRightsList(rights);
        if (settingModel != null) {
            model = settingModel;
            AutoPollAdapter autoPollAdapter = new AutoPollAdapter(mContext, scrollList);
            autoPollRecyclerView.setAdapter(autoPollAdapter);
            //启动滚动
            autoPollRecyclerView.start();
            SpannableStringBuilder spanString = new SpannableStringBuilder("¥" + model.feePrice);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan((int) TransformUtil.dp2px(this, 15)); //绝对尺寸
            spanString.setSpan(absoluteSizeSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            priceTxt.setText(spanString);
            goodsPrice.setText("¥" + model.feePrice);
            goodsDays.setText(model.feeValidityDays + "天");
            bottomDays.setText(model.feeValidityDays + "天");
        }
        if (partnerModel != null) {
            partnerId = partnerModel.id;
            localTxt.setText(partnerModel.cityName);
            bottomLocalTxt.setText(partnerModel.cityName);
        } else {
            if (lastPartnerModel != null) {
                // 5. 当partner为NULL时，需要弹出切换地区对话框。
                HanMomNoStoreDialog.newInstance().setCity(lastPartnerModel.getCityName()).setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        partnerId = lastPartnerModel.id;
                        localTxt.setText(lastPartnerModel.getCityName());
                        bottomLocalTxt.setText(lastPartnerModel.getCityName());
                        buildFragment(partnerId);
                    }
                }).setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        finish();
                    }
                }).show(getSupportFragmentManager(), "入驻");
            }
        }
        if (model != null && model.partnerId != null && !TextUtils.isEmpty(model.partnerId)) {
            partnerId = model.partnerId;
            buildFragment(partnerId);
        } else {
            if (lastPartnerModel != null) {
                HanMomNoStoreDialog.newInstance().setCity(lastPartnerModel.getCityName()).setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        partnerId = lastPartnerModel.id;
                        localTxt.setText(lastPartnerModel.getCityName());
                        bottomLocalTxt.setText(lastPartnerModel.getCityName());
                        buildFragment(partnerId);
                    }
                }).setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    finish();
                    }
                }).show(getSupportFragmentManager(), "入驻");
            }

        }
        if (isVip) {
            goWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "憨妈赚上面立即加入按钮点击量");
                    MobclickAgent.onEvent(mContext, "event2APPHanMomTopJoinClick", nokmap);
                    toWeiXinAPP();
                }
            });
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toWeiXinAPP();
                }
            });
            bottom_toPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "憨妈赚底部立即加入按钮点击量");
                    MobclickAgent.onEvent(mContext, "event2APPHanMomBottomJoinClick", nokmap);
                    toWeiXinAPP();
                }
            });
        } else {
            goWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "憨妈赚上面立即加入按钮点击量");
                    MobclickAgent.onEvent(mContext, "event2APPHanMomTopJoinClick", nokmap);
                    click();
                }
            });
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click();
                }
            });
            bottom_toPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "憨妈赚底部立即加入按钮点击量");
                    MobclickAgent.onEvent(mContext, "event2APPHanMomBottomJoinClick", nokmap);
                    click();
                }
            });
        }
    }

    private void buildRightsList(List<HanMomInfoModel.RightsListModel> rights) {
        if (ListUtil.isEmpty(rights)) {
            rightsLayout.setVisibility(View.GONE);
        } else {
            rightsLayout.setVisibility(View.VISIBLE);
            rightsLiner.removeAllViews();
            for (int i = 0; i < rights.size(); i++) {
                final HanMomInfoModel.RightsListModel model = rights.get(i);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View view = inflater.inflate(R.layout.item_hanmom_rights_layout, null);
                TextView numTxt = (TextView) view.findViewById(R.id.numTxt);
                TextView contentTxt = (TextView) view.findViewById(R.id.contentTxt);
                numTxt.setText((i + 1) + "");
                contentTxt.setText(model.comment);
                rightsLiner.addView(view);
            }
        }
    }

    public void buildStartList(List<HanMomInfoModel.StarListModel> starList) {
        if (starList != null && starList.size() > 0) {
            if (starList.size() == 1) {
                top.setVisibility(View.VISIBLE);
                top2.setVisibility(View.GONE);
                com.healthy.library.businessutil.GlideCopy.with(mContext)
                        .load(starList.get(0).memberAvatarUrl)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_avatar_default)

                        .into(userImg);
                userNickName.setText(starList.get(0).memberName);
                userLabel.setText(starList.get(0).memberAlias);
                String time = "";
                if (starList.get(0).memberEntryTime != null || !TextUtils.isEmpty(starList.get(0).memberEntryTime)) {
                    time = starList.get(0).memberEntryTime;
                } else {
                    time = starList.get(0).createTime;
                }
                try {
                    userContent.setText("加入憨妈赚合伙人" + getDays(time) + "天，收入");
                    userContentPrice.setText(starList.get(0).totalAmount + "");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (starList.size() > 1) {
                top.setVisibility(View.VISIBLE);
                top2.setVisibility(View.VISIBLE);
                com.healthy.library.businessutil.GlideCopy.with(mContext)
                        .load(starList.get(0).memberAvatarUrl)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_avatar_default)

                        .into(userImg);
                userNickName.setText(starList.get(0).memberName);
                userLabel.setText(starList.get(0).memberAlias);
                String time = "";
                if (starList.get(0).memberEntryTime != null || !TextUtils.isEmpty(starList.get(0).memberEntryTime)) {
                    time = starList.get(0).memberEntryTime;
                } else {
                    time = starList.get(0).createTime;
                }


                com.healthy.library.businessutil.GlideCopy.with(mContext)
                        .load(starList.get(1).memberAvatarUrl)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_avatar_default)

                        .into(userImg2);
                userNickName2.setText(starList.get(1).memberName);
                userLabel2.setText(starList.get(1).memberAlias);
                String time2 = "";
                if (starList.get(1).memberEntryTime != null || !TextUtils.isEmpty(starList.get(1).memberEntryTime)) {
                    time2 = starList.get(1).memberEntryTime;
                } else {
                    time2 = starList.get(1).createTime;
                }
                try {
                    userContent.setText("加入憨妈赚合伙人" + getDays(time) + "天，收入");
                    userContentPrice.setText(starList.get(0).totalAmount + "");
                    userContent2.setText("加入憨妈赚合伙人" + getDays(time2) + "天，收入");
                    userContentPrice2.setText(starList.get(1).totalAmount + "");
                    userContentPriceEnd.setText("元");
                    userContentPriceEnd2.setText("元");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onGetBuyIdSuccess(final String buyId) {
        hanMomPayDialog = HanMomPayDialog.newInstance();
        hanMomPayDialog.setPrice(model.feePrice + "");
        hanMomPayDialog.show(getSupportFragmentManager(), "支付");
        hanMomPayDialog.setPayTypeListener(new HanMomPayDialog.onGetPayTypeListener() {
            @Override
            public void setPayType(String type) {
                if (payOrderId != null) {
                    if (Constants.PAY_IN_A_LI.equals(payOrderId) && Constants.PAY_IN_A_LI.equals(type)) {//说明重复下单
                        getPayInfoSuccess(payOrderId, type);
                        return;
                    }
                    if (Constants.PAY_IN_WX.equals(payOrderId) && Constants.PAY_IN_WX.equals(type)) {//说明重复下单
                        getPayInfoSuccess(payOrderId, type);
                        return;
                    }
                    payInfoMap = null;
                }
                showLoading();
                hanMomPresenter.getPayInfo(new SimpleHashMapBuilder<String, Object>().puts("buyId", buyId).puts("payType", type));

            }

        });

    }

    public void payByAli(final String orderInfo) {
        Observable.create(
                new ObservableOnSubscribe<Map<String, String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<Map<String, String>> emitter) {
                        PayTask aliPay = new PayTask(HanMonActivity.this);
                        Map<String, String> result = aliPay.payV2(orderInfo, true);
                        emitter.onNext(result);
                        emitter.onComplete();
                    }
                })
                .compose(RxThreadUtils.<Map<String, String>>Obs_io_main())
                .to(RxLifecycleUtils.<Map<String, String>>bindLifecycle(this))
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
                                        showToast("支付成功");
                                        hanMomPayDialog.dismiss();
                                        goWeiXinAPP();
                                        finish();
                                        break;
                                    case "6001":
                                        showToast("取消支付");
                                        hanMomPayDialog.dismiss();
                                        break;
                                    default:
                                        hanMomPayDialog.dismiss();
                                        break;
                                }
                            }
                        }, 200);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

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
                hanMomPayDialog.dismiss();
                goWeiXinAPP();
                finish();
                break;
            case -2:
                showToast("支付失败");
                hanMomPayDialog.dismiss();
                break;
            default:
                hanMomPayDialog.dismiss();
                break;
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        slidingTabLayout = findViewById(R.id.slidingTabLayout);
        viewPager = findViewById(R.id.viewPager);
        priceTxt = findViewById(R.id.priceTxt);
        goodsPrice = findViewById(R.id.goodsPrice);
        goodsDays = findViewById(R.id.goodsDays);
        bottomDays = findViewById(R.id.bottomDays);
        goWx = findViewById(R.id.goWx);
        bottom_toPay = findViewById(R.id.bottom_toPay);
        userImg = findViewById(R.id.userImg);
        userImg2 = findViewById(R.id.userImg2);
        userNickName = findViewById(R.id.userNickName);
        userNickName2 = findViewById(R.id.userNickName2);
        userLabel = findViewById(R.id.userLabel);
        userLabel2 = findViewById(R.id.userLabel2);
        userContent = findViewById(R.id.userContent);
        userContent2 = findViewById(R.id.userContent2);
        top = findViewById(R.id.top);
        top2 = findViewById(R.id.top2);
        bottomLocalTxt = findViewById(R.id.bottomLocalTxt);
        localTxt = findViewById(R.id.localTxt);
        addBtn = findViewById(R.id.addBtn);
        userContentPrice = findViewById(R.id.userContentPrice);
        userContentPrice2 = findViewById(R.id.userContentPrice2);
        userContentPriceEnd = findViewById(R.id.userContentPriceEnd);
        userContentPriceEnd2 = findViewById(R.id.userContentPriceEnd2);
        rightsLiner = findViewById(R.id.rightsLiner);
        rightsLayout = findViewById(R.id.rightsLayout);
        autoPollRecyclerView = findViewById(R.id.autoPollRecyclerView);
        autoPollRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));//设置LinearLayoutManager.HORIZONTAL  则水平滚动

    }

    public void click() {
        TongLianMemberData tongLianMemberData= ObjUtil.getObj(SpUtils.getValue(LibApplication.getAppContext(),SpKey.TONGLIANBIZUSER),TongLianMemberData.class);
        if(tongLianMemberData!=null&&tongLianMemberData.memberInfo.isPhoneChecked) {//绑定了
            if (partnerId != null && !TextUtils.isEmpty(partnerId)) {
                hanMomPresenter.getBuyId(partnerId);
            }
        }else {
            if (tongLianPhoneBindDialog == null) {
                tongLianPhoneBindDialog = TongLianPhoneBindDialog.newInstance();
            }
            tongLianPhoneBindDialog.show(getSupportFragmentManager(), "手机绑定");
            tongLianPhoneBindDialog.setOnDialogAgreeClickListener(new TongLianPhoneBindDialog.OnDialogAgreeClickListener() {
                @Override
                public void onDialogAgree() {
                    if (partnerId != null && !TextUtils.isEmpty(partnerId)) {
                        hanMomPresenter.getBuyId(partnerId);
                    }
                }
            });
        }

    }

    public void goWeiXinAPP() {
        String appId = Ids.WX_APP_ID; // 本应用微信AppId
        IWXAPI api = WXAPIFactory.createWXAPI(HanMonActivity.this, appId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = WEIXIN_XIAOCHENGXU_ID; // 小程序原始id
        req.path = "";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        if (ChannelUtil.isIpRealRelease()) {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        } else {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
        }
        api.sendReq(req);
    }

    public long getDays(String time) throws ParseException {
        //设置转换的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //开始时间
        Date startDate = sdf.parse(time);
        //结束时间
        Date endDate = new Date();

        //得到相差的天数 betweenDate
        long betweenDate = (endDate.getTime() - startDate.getTime()) / (60 * 60 * 24 * 1000);
        return betweenDate;
    }

    public void toWeiXinAPP() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String appId = Ids.WX_APP_ID; // 本应用微信AppId
                IWXAPI api = WXAPIFactory.createWXAPI(HanMonActivity.this, appId);
                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                req.userName = "gh_93d673cec6a8"; // 小程序原始id
                req.path = ""; //拉起小程序
                if (ChannelUtil.isIpRealRelease()) {
                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                } else {
                    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
                }
                api.sendReq(req);
            }
        }, 500);
    }
}