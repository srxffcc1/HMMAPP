package com.health.servicecenter.activity;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.google.gson.Gson;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.MallGoodsOrderFinalBlockAdapter;
import com.health.servicecenter.adapter.MallGoodsOrderFinalUnderAdapter;
import com.health.servicecenter.contract.CouponGoodsFinalContract;
import com.health.servicecenter.contract.ServiceBasketActDetailContract;
import com.health.servicecenter.contract.ServiceOrderFinalContract;
import com.health.servicecenter.presenter.CouponGoodsFinalPresenter;
import com.health.servicecenter.presenter.ServiceGoodsOrderFinalPresenter;
import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.CouponChoseDialog;
import com.healthy.library.businessutil.CouponUtil;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.ActVipContract;
import com.healthy.library.contract.ActVipOnlineContract;
import com.healthy.library.dialog.TongLianPhoneBindDialog;
import com.healthy.library.interfaces.IHmmCoupon;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnTopBarListener;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.ActVipDefault;
import com.healthy.library.model.ActVipOrder;
import com.healthy.library.model.AddressListModel;
import com.healthy.library.model.AssembleParams;
import com.healthy.library.model.BargainParams;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.DiscountTopModel;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketGroup;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.model.GoodsShop;
import com.healthy.library.model.KeyVExtra;
import com.healthy.library.model.MemberAction;
import com.healthy.library.model.OrderGoodsPackage;
import com.healthy.library.model.OrderPackage;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.TongLianMemberData;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.presenter.ActVipOnlinePresenter;
import com.healthy.library.presenter.ActVipPresenter;
import com.healthy.library.presenter.ActionPresenterCopy;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

@Route(path = ServiceRoutes.SERVICE_ORDER)
public class ServiceGoodsOrderFinalCopy extends BaseActivity implements ActVipOnlineContract.View, ActVipContract.View, IsFitsSystemWindows, BaseAdapter.OnOutClickListener, ServiceOrderFinalContract.View, ServiceBasketActDetailContract.View, CouponGoodsFinalContract.View {
    @Autowired
    List<GoodsBasketCell> goodsbasketlist;
    ArrayList<GoodsBasketStore> goodsBasketStoreList = new ArrayList<>();
    //    ArrayList<GoodsBasketStore> goodsBasketStoreListEx = new ArrayList<>();

    //-----------------------------------------------------
    @Autowired
    String token;
    @Autowired
    String course_id;
    @Autowired
    String liveStatus;
    //-----------------------------------------------------
    public static final int SELECT_ADDRESS_REQUEST = 4014;
    //-----------------------------------------------------
    //砍价
    @Autowired
    String bargainId;
    @Autowired
    String bargainMemberId;
    @Autowired
    String bargainMoney;
    //-----------------------------------------------------
    //拼团
    @Autowired
    String assembleId;
    @Autowired
    String teamNum;
    @Autowired
    String assemblePrice;
    //-----------------------------------------------------
    @Autowired
    String packageMoney;
    @Autowired
    String packageId;
    @Autowired
    String packageQuantity;

    @Autowired
    String goodsMarketingType;// -4 抽奖 -5 直播活动 9投票

    @Autowired
    String live_course;

    @Autowired
    String live_anchor;

    @Autowired
    String visitShopId;

    @Autowired
    String prizeName;//活动类型 -> 奖项名称
    @Autowired
    String activityName;//活动类型 -> 活动名称
    @Autowired
    String activitySignupId;//活动类型 -> 报名活动的记录id

    String mchId;

    @Autowired
    String winType;//活动类型 -> 直播活动中奖类型
    @Autowired
    String winId;//活动类型 -> 直播活动中奖记录ID


    String type = "0";
    String race = "0";

    @Autowired
    ActVip actVip;

    //-----------------------------------------------------
    private TopBar topBar;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerview;
    private ImageView ivBottomShader;
    private ConstraintLayout submitOrderLL;
    private TextView ofderLeft;
    private TextView orderMoney;
    private TextView orderSubmit;
    private ConstraintLayout limitLine;
    private TextView limitLineText;
    private ConstraintLayout limitCon;
    private ImageView limitConTop;
    private Space limitSpace;
    private TextView limitText;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private MallGoodsOrderFinalBlockAdapter mallGoodsOrderFinalBlockAdapter;
    private MallGoodsOrderFinalUnderAdapter mallGoodsOrderFinalUnderAdapter;
    //-----------------------------------------------------
    ServiceGoodsOrderFinalPresenter serviceGoodsOrderFinalPresenter;
    ActVipPresenter actVipPresenter;
    AddressListModel addressListModel;//配送地址
    private TextView discountNotCanUse;
    private TextView orderShopBack;
//    boolean isOnlyYY = false;//单品异业
    TongLianPhoneBindDialog tongLianPhoneBindDialog;
    @Autowired  //如果是这个则下单需要改变
    List<ActVip> actVipResultList;//从购物车进来的话 把这个传递过来 如果是单品的话 不传会自动判断
    private Dialog mDialog;
    private TextView orderMoneyOld;//底部划线价
    private long oldsubmitTime=0;

    @Override
    protected int getLayoutId() {
        return R.layout.service_activity_goodsorder_flow_final_copy;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownDisposable != null && !mCountDownDisposable.isDisposed()) {
            mCountDownDisposable.dispose();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_ADDRESS_REQUEST) {
            if (data != null) {//地址选择
                String result = data.getStringExtra("result");
                AddressListModel addressListModelTmp = new Gson().fromJson(result, AddressListModel.class);
                if (mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow != null) {
                    mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow.deliver.deliveryConsigneeName = addressListModelTmp.getConsigneeName();
                    mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow.deliver.deliveryConsigneePhone = addressListModelTmp.getConfigneePhone();
                    mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow.deliver.deliveryConsigneeProvince = addressListModelTmp.getProvince();
                    mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow.deliver.deliveryConsigneeCity = addressListModelTmp.getCity();
                    mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow.deliver.deliveryConsigneeDistrict = addressListModelTmp.getDistrict();
                    mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow.deliver.deliveryConsigneeProvinceName = addressListModelTmp.getProvinceName();
                    mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow.deliver.deliveryConsigneeCityName = addressListModelTmp.getCityName();
                    mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow.deliver.deliveryConsigneeDistrictName = addressListModelTmp.getDistrictName();
                    mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow.deliver.deliveryConsigneeAddress = addressListModelTmp.getAddress() + addressListModelTmp.getHouseNum();
                    mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow.deliver.deliveryLatitude = addressListModelTmp.getLat();
                    mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow.deliver.deliveryLongitude = addressListModelTmp.getLng();
                    mallGoodsOrderFinalBlockAdapter.buildCheckResult(mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow.checkedId, mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow, mallGoodsOrderFinalBlockAdapter.storeBlockChildHolderNow);
                }

            }
        }

    }

    public String getCellGoodsMarketingType() {// 判断当前列表
        if (goodsbasketlist.size() == 1) {//说明就一个商品 可能是营销活动
            if ("-2".equals(goodsbasketlist.get(0).getGoodsMarketingTypeOrg())) {//线下营销活动
                if (goodsbasketlist.get(0).getPlusPrice() > 0) {//plus
                    return "8";
                }
                return "-2"; //走线下
            } else if (!TextUtils.isEmpty(bargainId)) {//砍价
                return "1";//走线上
            } else if (!TextUtils.isEmpty(assembleId)) {//拼团
                return "2";//走线上
            } else if ("3".equals(goodsbasketlist.get(0).getGoodsMarketingTypeOrg())) {//秒杀
                return "3";//走线上
            } else if ("5".equals(goodsbasketlist.get(0).getGoodsMarketingTypeOrg())) {//积分
                return "5";//走线上
            } else if ("4".equals(goodsbasketlist.get(0).getGoodsMarketingTypeOrg())) {//积分
                return "4";//走线上
            } else if ("-1".equals(goodsbasketlist.get(0).getGoodsMarketingTypeOrg())) {//直播
                return "-1";//走线上
            } else if ("8".equals(goodsbasketlist.get(0).getGoodsMarketingTypeOrg())) {
                return "8";//走线上
            } else if ("9".equals(goodsMarketingType)) {
                return "9";//走线上
            } else if ("-4".equals(goodsMarketingType)) {
                return "-4";//走线上
            } else if ("-5".equals(goodsMarketingType)) {
                return "-5";//走线上
            } else {//普通商品
                if (goodsbasketlist.get(0).getPlusPrice() > 0) {//plus
                    return "8";
                }
                return "0";// 走线下
            }
        } else {//多个商品 可能是购物车 可能是组合套餐
            if (!TextUtils.isEmpty(packageId)) {//套餐
                return "-3";//走线上
            } else {//购物车就当成是线下营销活动
                return "-2";// 走线下
            }
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);

        StyledDialog.init(mContext);
        if ("9".equals(goodsMarketingType) || "-4".equals(goodsMarketingType)) {
            topBar.setTitle("领取奖品");
            orderMoney.setVisibility(View.GONE);
            ofderLeft.setVisibility(View.GONE);
            orderSubmit.setText("领取奖品");
        }
        if (TextUtils.isEmpty(visitShopId)) {
            visitShopId = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP);
        }
        serviceGoodsOrderFinalPresenter = new ServiceGoodsOrderFinalPresenter(this, this);
        actVipPresenter = new ActVipPresenter(this, this);
        if (!TextUtils.isEmpty(goodsMarketingType)) {
            if ("1".equals(goodsMarketingType)) {//砍价
                type = "1";
            }
            if ("2".equals(goodsMarketingType)) {//拼团
                type = "4";
            }
            if ("5".equals(goodsMarketingType)) {//积分
                type = "5";
                race = "4";
            }
            if ("-1".equals(goodsMarketingType)) {//直播
                type = "0";
                race = "5";
            }
            if ("9".equals(goodsMarketingType)) {//投票领奖
                type = "7";
                race = "6";
            }
            if ("-4".equals(goodsMarketingType)) {//抽奖领奖
                type = "8";
                race = "7";
            }
            if ("-5".equals(goodsMarketingType)) {//直播活动
                type = "11";
                race = "8";
            }
        } else {
            type = "0";
            if (goodsbasketlist.size() == 1) {//一个物品 检测是不是积分
                try {
                    if (goodsbasketlist.get(0).goodsMarketingDTO != null) {
                        if ("5".equals(goodsbasketlist.get(0).goodsMarketingDTO.marketingType)) {
                            type = "5";
                            race = "4";
                        }
                        else if ("4".equals(goodsbasketlist.get(0).goodsMarketingDTO.marketingType)) {
                            goodsMarketingType = "4";
                            type = "9";
                            race = "0";
                        }
                        else if ("3".equals(goodsbasketlist.get(0).goodsMarketingDTO.marketingType)) {
                            goodsMarketingType = "3";
                            type = "10";
                            race = "0";
                        }
                        else if ("2".equals(goodsbasketlist.get(0).goodsMarketingDTO.marketingType)) {
                            goodsMarketingType = "2";
                        }
                        else if ("1".equals(goodsbasketlist.get(0).goodsMarketingDTO.marketingType)) {
                            goodsMarketingType = "1";
                        }
                        else if ("8".equals(goodsbasketlist.get(0).goodsMarketingDTO.marketingType)) {
                            goodsMarketingType = "8";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (!TextUtils.isEmpty(bargainId)) {
            type = "1";
        }
        if (!TextUtils.isEmpty(assembleId)) {
            type = "4";
        }
        if (!TextUtils.isEmpty(packageId)) {
            type = "0";
            race = "3";
        }
        buildRecyclerView();
        serviceGoodsOrderFinalPresenter.getIsNewAppMember();
        if (!TextUtils.isEmpty(goodsMarketingType)) {
            if ("1".equals(goodsMarketingType)) {
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("暂无可用");
            }
            if ("2".equals(goodsMarketingType)) {
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("暂无可用");
            }
            if ("5".equals(goodsMarketingType)) {
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("暂无可用");
            }
            if ("-1".equals(goodsMarketingType)) {
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("暂无可用");
            }
        } else {

        }
        if (!TextUtils.isEmpty(bargainId)) {
            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("暂无可用");
        }
        if (!TextUtils.isEmpty(assembleId)) {
            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("暂无可用");
        }
        if (!TextUtils.isEmpty(packageId)) {
            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("暂无可用");
        }
        goodsBasketStoreList.clear();
//        goodsBasketStoreListEx.clear();
        getBaseData();
    }
    public void getBaseData(){
        if(TextUtils.isEmpty(mchId)){
            serviceGoodsOrderFinalPresenter.getShopDetailOnlyVisit(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", visitShopId)
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG)));
            return;
        }
        goodsBasketStoreList.addAll(getGoodsBasketStores());
//        goodsBasketStoreListEx.addAll(getGoodsBasketStoresEx());
        mallGoodsOrderFinalBlockAdapter.setData(goodsBasketStoreList);
        serviceGoodsOrderFinalPresenter.checkMearchantOpenChange(new SimpleHashMapBuilder<String, Object>().puts("visitShopId", visitShopId));
        serviceGoodsOrderFinalPresenter.checkMearchantFeeOpenChange(new SimpleHashMapBuilder<String, Object>().puts("merchantId", mchId));
        serviceGoodsOrderFinalPresenter.getAddressList(new SimpleHashMapBuilder<String, Object>());
        getNowAllCouponInfoList();
        buildCouponWithNoDialog();
        getNowAllActs();
        buildNowPayMoney();
    }

    Map<String, String> imageMap = new HashMap<>();

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerview.setLayoutManager(virtualLayoutManager);
        recyclerview.setAdapter(delegateAdapter);
        mallGoodsOrderFinalBlockAdapter = new MallGoodsOrderFinalBlockAdapter();
        mallGoodsOrderFinalBlockAdapter.setOutClickListener(this);
        mallGoodsOrderFinalBlockAdapter.setExtra(
                bargainId,
                bargainMemberId,
                bargainMoney,
                assembleId,
                teamNum,
                assemblePrice,
                packageMoney,
                packageId,
                packageQuantity,
                goodsMarketingType,
                type,
                race,
                visitShopId, getCellGoodsMarketingType(), prizeName, activityName,actVip);
        delegateAdapter.addAdapter(mallGoodsOrderFinalBlockAdapter);
        mallGoodsOrderFinalBlockAdapter.setImageMap(imageMap);
        mallGoodsOrderFinalBlockAdapter.setIsNtReal(isNtReal+"");
        mallGoodsOrderFinalUnderAdapter = new MallGoodsOrderFinalUnderAdapter();
        mallGoodsOrderFinalUnderAdapter.setOutClickListener(this);
        mallGoodsOrderFinalUnderAdapter.setModel("订单末尾");
        mallGoodsOrderFinalUnderAdapter.setGoodsList(goodsBasketStoreList, goodsBasketStoreList, goodsbasketlist);
        mallGoodsOrderFinalUnderAdapter.setSelectCoupons(selectInfo);
        mallGoodsOrderFinalUnderAdapter.setExtra(
                bargainId,
                bargainMemberId,
                bargainMoney,
                assembleId,
                teamNum,
                assemblePrice,
                packageMoney,
                packageId,
                packageQuantity,
                goodsMarketingType,
                type,
                race);
        if (!("9".equals(goodsMarketingType) || "-4".equals(goodsMarketingType) || "-5".equals(goodsMarketingType))) {
            delegateAdapter.addAdapter(mallGoodsOrderFinalUnderAdapter);
        }
    }

    public boolean checkIllegal() {
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            GoodsBasketStore goodsBasketStore = goodsBasketStoreList.get(i);
            if ("10".equals(goodsBasketStore.deliver.deliveryType) || "20".equals(goodsBasketStore.deliver.deliveryType)) {//到店自提
                if (TextUtils.isEmpty(goodsBasketStore.deliver.deliveryTime) && !"5".equals(race)&&goodsBasketStore.supportNeedTime) {
                    showToastIgnoreLife("请选择提货时间");
                    return false;
                }
            } else {//配送到家
                if (TextUtils.isEmpty(goodsBasketStore.deliver.deliveryConsigneeAddress)) {
                    showToastIgnoreLife("请选择收货地址");
                    return false;
                }
            }
        }

        return true;
    }

    OrderGoodsPackage orderGoodsPackage;


    private Disposable mCountDownDisposable;
    public void submit(View view) {
        submitPass();


    }

    public void submitPass() {
        MobclickAgent.onEvent(mContext, "event2APPOrderConfirmPayClick", new SimpleHashMapBuilder<String, String>().puts("soure", "订单确认页-确认并支付点击量"));
        if (checkIllegal()) {
            showLoading();
            if ("-2".equals(getCellGoodsMarketingType()) || "0".equals(getCellGoodsMarketingType())) {//营销活动下单
                submitFuc("ytb_25000");
            } else {
                if (("9".equals(goodsMarketingType) || "-4".equals(goodsMarketingType)) && !TextUtils.isEmpty(errorMsg) &&
                        errorMsg.contains("库存不足")) {
                    showContent();
                    //判断奖品领取错误
                    isAgree("领奖提醒", "当前奖品已被下架或无库存，暂时无法领奖，详情联系商家咨询商家:" + mallGoodsOrderFinalBlockAdapter.mShopPhone);
                    return;
                }
                submitFuc("25000");
            }
            AutoDisposeConverter<Long> objectAutoDisposeConverter = AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                    .from(this, Lifecycle.Event.ON_DESTROY));
            Observable.intervalRange(0, 6, 0, 1, TimeUnit.SECONDS)
                    .compose(RxThreadUtils.<Long>Obs_io_main())
                    .to(objectAutoDisposeConverter)
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mCountDownDisposable = d;
                        }

                        @Override
                        public void onNext(Long aLong) {
                            long reset = 5 - aLong;
                            if (reset == 0) {
                                orderSubmit.setText("提交订单");
                                orderSubmit.setEnabled(true);
                            } else {
                                orderSubmit.setText("提交订单("+String.format("%ss)", reset));
                                orderSubmit.setEnabled(false);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            //System.out.println("出错了");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    public void shopback(View view) {//回退到初始门店
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            GoodsBasketStore goodsBasketStore = goodsBasketStoreList.get(i);
            goodsBasketStoreList.get(i).shopBack();//回退到初始门店
            for (int j = 0; j < goodsBasketStoreList.get(i).getGoodsBasketCellAllListExpFixGift().size(); j++) {
                if ("-1".equals(goodsBasketStore.goodsCategoryFirstId) && goodsBasketStore.supportOtherdeliveryShop && !"5".equals(type) && !"5".equals(race) && !"3".equals(race)) {//还要判断支持不支持跨店提货
                    GoodsBasketCell basketCell = goodsBasketStoreList.get(i).getGoodsBasketCellAllListExpFixGift().get(j);
                    basketCell.goodsShopId = goodsBasketStoreList.get(i).goodsPickShop.shopId;
                    basketCell.shopBack();//回退到初始门店
                    outClick("buildCouponChange", basketCell);//优惠券重新请求
                }
            }
            goodsBasketStore.changeItem();//对门店绑定的一个门店视图进行刷新
        }
        orderShopBack.setVisibility(View.GONE);
    }

    boolean isTestTong = true;

    private void submitFuc(String fuc) {//计算价格或提交订单
        if ("ytb_25000".equals(fuc) || "25000".equals(fuc)) {

            TongLianMemberData tongLianMemberData= ObjUtil.getObj(SpUtils.getValue(LibApplication.getAppContext(),SpKey.TONGLIANBIZUSER),TongLianMemberData.class);
            if(tongLianMemberData!=null&&tongLianMemberData.memberInfo.isPhoneChecked) {//绑定了

            }else {
                if (tongLianPhoneBindDialog == null) {
                    tongLianPhoneBindDialog = TongLianPhoneBindDialog.newInstance();
                }
                tongLianPhoneBindDialog.show(getSupportFragmentManager(), "手机绑定");
                final String finalFuc = fuc;
                tongLianPhoneBindDialog.setOnDialogAgreeClickListener(new TongLianPhoneBindDialog.OnDialogAgreeClickListener() {
                    @Override
                    public void onDialogAgree() {
                        submitFuc(finalFuc);
                    }
                });
                showContent();
                return;
            }
        }

        if ("ytb_25000".equals(fuc)||"25000".equals(fuc)) {//加判下是否真的参加了营销活动 可能存在线上有 线下无的现象
//            if(!checkHasZpInActVip(goodsBasketStoreList)){
//            }
            //正式下单防呆5秒
            if(oldsubmitTime!=0&&System.currentTimeMillis()-oldsubmitTime<(1000*5)){
                Toast.makeText(LibApplication.getAppContext(), "当前商品太火热请稍后再试!", Toast.LENGTH_SHORT).show();
                showContent();
                return;
            }
            oldsubmitTime=System.currentTimeMillis();//记录下前一次下单的时间
        }
        if ("25000".equals(fuc) || "25025".equals(fuc)) {
            if (!TextUtils.isEmpty(packageId)) {
                orderGoodsPackage = new OrderGoodsPackage(packageId, packageQuantity);
            }
            if (!TextUtils.isEmpty(bargainId) && !"1".equals(goodsbasketlist.get(0).getGoodsMarketingType())) {
                type = "0";
            }
            if (!TextUtils.isEmpty(assembleId) && !"2".equals(goodsbasketlist.get(0).getGoodsMarketingType())) {
                type = "0";
            }
            if ("4".equals(goodsMarketingType) && isNtReal != 1) {//新客商品 但是人不是新客
                type = "0";
            }
            SimpleHashMapBuilder<String, Object> result = new SimpleHashMapBuilder<>();
            result.puts(Functions.FUNCTION, fuc);
            if ("25000".equals(fuc)) {
                int goodscount = getGoodsRealCount();
                if (goodscount <= 0) {
                    //关闭Loading显示文本内容
                    showContent();
                    if ("9".equals(goodsMarketingType) || "-4".equals(goodsMarketingType) || "-5".equals(goodsMarketingType)) {
                        //判断奖品领取错误
                        isAgree("领奖提醒", "当前奖品已被下架或无库存，暂时无法领奖，详情联系商家咨询商家:" + mallGoodsOrderFinalBlockAdapter.mShopPhone);
                    } else {
                        Toast.makeText(LibApplication.getAppContext(), "当前门店无库存，无法下单", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }

            SimpleArrayListBuilder<KeyVExtra> mAttributes = null;
            if (!TextUtils.isEmpty(live_course)) {
                mAttributes = new SimpleArrayListBuilder<>();
                mAttributes.adds(new KeyVExtra("live_course", live_course))
                        .adds(new KeyVExtra("live_anchor", live_anchor));
            }
            if (!TextUtils.isEmpty(activitySignupId)) {
                if (mAttributes == null) {
                    mAttributes = new SimpleArrayListBuilder<>();
                }
                String key = "";
                if ("9".equals(goodsMarketingType)) {
                    key = "activitySignupId";//为报名活动的记录ID
                }
                if ("-4".equals(goodsMarketingType)) {
                    key = "lotteryWinId";//为抽奖活动中奖记录ID
                }
                mAttributes.add(new KeyVExtra(key, activitySignupId));
            }
            if (!TextUtils.isEmpty(winId)) {
                if (mAttributes == null) {
                    mAttributes = new SimpleArrayListBuilder<>();
                }
                mAttributes.adds(new KeyVExtra("winId", winId))
                        .adds(new KeyVExtra("winType", winType));
            }
            result
                    .puts("visitShopId", visitShopId)
                    .puts("source", "1")
                    .puts("race", race)
                    .puts("type", type)
                    .puts("platform", "ANDROID")
                    .puts("coupons", new SimpleArrayListBuilder<String>().putList(selectInfo, new ObjectIteraor<CouponInfoZ>() {
                        @Override
                        public String getDesObj(CouponInfoZ o) {
                            return o.getUseId();
                        }
                    }))
                    .puts("goodsPackage", orderGoodsPackage)
                    .puts("packages", new SimpleArrayListBuilder<OrderPackage>().putList(goodsBasketStoreList, new ObjectIteraor<GoodsBasketStore>() {
                        @Override
                        public OrderPackage getDesObj(GoodsBasketStore goodsBasketStore) {
                            OrderPackage orderPackage = new OrderPackage(goodsBasketStore.deliver.getFlash());

                            orderPackage.details = ((!TextUtils.isEmpty(assembleId) && "2".equals(goodsbasketlist.get(0).getGoodsMarketingType())) || (!TextUtils.isEmpty(bargainId) && "2".equals(goodsbasketlist.get(0).getGoodsMarketingType()))) ? null : goodsBasketStore.getDetails();
                            if (!TextUtils.isEmpty(assembleId) && "2".equals(goodsbasketlist.get(0).getGoodsMarketingType())) {
                                orderPackage.assembleParams = TextUtils.isEmpty(assembleId) ? null : new AssembleParams(
                                        assembleId,
                                        teamNum,
                                        "0",
                                        goodsbasketlist.get(0).goodsId,
                                        goodsbasketlist.get(0).getGoodsSpecId(),
                                        goodsbasketlist.get(0).goodsMarketingDTO.mapMarketingGoodsId,
                                        goodsbasketlist.get(0).goodsMarketingDTO.id
                                );
                            }
                            if (goodsBasketStore.checkAllIsService()) {//检查是不是都是服务
                                orderPackage.delivery.deliveryType = "20";
                            }
                            if (!TextUtils.isEmpty(bargainId) && "1".equals(goodsbasketlist.get(0).getGoodsMarketingType())) {
                                orderPackage.bargainParams = TextUtils.isEmpty(bargainId) ? null : new BargainParams(bargainId, bargainMemberId);
                            }
                            return orderPackage;
                        }
                    }))
                    .puts("attributes", mAttributes);
            if ("25000".equals(fuc)) {

                //判断下 拼团 砍价还是 秒杀
                if("1".equals(type)){//砍价
                    new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                            new SimpleHashMapBuilder<>()
                                    .putObject(new MemberAction(
                                            BuildConfig.VERSION_NAME,
                                            1,
                                            5,
                                            "SubmitOrder",
                                            getActivitySimpleName(),
                                            new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                                    ));
                }else if("4".equals(type)){//4拼团
                    new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                            new SimpleHashMapBuilder<>()
                                    .putObject(new MemberAction(
                                            BuildConfig.VERSION_NAME,
                                            1,
                                            4,
                                            "SubmitOrder",
                                            getActivitySimpleName(),
                                            new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                                    ));
                }else if("10".equals(type)){//秒杀
                    new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                            new SimpleHashMapBuilder<>()
                                    .putObject(new MemberAction(
                                            BuildConfig.VERSION_NAME,
                                            1,
                                            6,
                                            "SubmitOrder",
                                            getActivitySimpleName(),
                                            new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                                    ));
                }


                serviceGoodsOrderFinalPresenter.submitOrder(result);
            } else {
                serviceGoodsOrderFinalPresenter.submitOrderV(result);
            }
        } else {//线下下单接口
            if (vipShop == null) {
                Toast.makeText(LibApplication.getAppContext(), "查询门店信息中稍后再试", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean canSubmit = true;
            for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                GoodsBasketStore goodsBasketStore = goodsBasketStoreList.get(i);
                for (int j = 0; j < goodsBasketStore.getGoodsBasketCellList().size(); j++) {
                    GoodsBasketCell basketCell = goodsBasketStore.getGoodsBasketCellList().get(j);
                    if (!basketCell.isGift) {//判断库存是不是有问题
                        //判断下两个数量是否一致 且不为0
                        int cellCountReal = basketCell.getGoodsQuantityInBasket();//这块会计算库存
                        int cellCountNow = basketCell.getGoodsQuantity();//这块会计算库存
                        basketCell.undo();
                        int cellCountOld = basketCell.getGoodsQuantity();//这块会计算库存
                        basketCell.redo();
                        if (cellCountOld != cellCountNow || cellCountNow == 0 || cellCountOld == 0) {//数量发生了改变 或者库存不足
                            if (cellCountOld == 0 && cellCountNow > 0 && cellCountReal == cellCountNow) {//可以正常下单的

                            } else {
                                canSubmit = false;
                            }
                        }
                    }
                }
            }
            if (!canSubmit) {
                showContent();
                if ("9".equals(goodsMarketingType) || "-4".equals(goodsMarketingType)) {
                    //判断奖品领取错误
                    isAgree("领奖提醒", "当前奖品已被下架或无库存，暂时无法领奖，详情联系商家咨询商家:" + mallGoodsOrderFinalBlockAdapter.mShopPhone);
                } else {
                    Toast.makeText(LibApplication.getAppContext(), "当前门店库存不足，无法下单", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            SimpleHashMapBuilder<String, Object> result = new SimpleHashMapBuilder<>();
            result.puts(Functions.FUNCTION, fuc);
            result.puts("AppID",LocUtil.getytbAppId());
            result.putObject(new ActVipOrder()
                    .setSaleList(new SimpleArrayListBuilder<ActVipOrder.SaleStore>().putList(
                            ListUtil.where(goodsBasketStoreList, new ListUtil.Where<GoodsBasketStore>() {
                                @Override
                                public boolean where(GoodsBasketStore obj) {
                                    return !"1".equals(obj.merchantType);//非滤出异业门店款
                                }
                            })
                            , new ObjectIteraor<GoodsBasketStore>() {
                                @Override
                                public ActVipOrder.SaleStore getDesObj(GoodsBasketStore o) {
                                    return new ActVipOrder.SaleStore(o);
                                }
                            }))
                    .setYYSaleList(new SimpleArrayListBuilder<ActVipOrder.SaleStore>().putList(
                            ListUtil.where(goodsBasketStoreList, new ListUtil.Where<GoodsBasketStore>() {
                                @Override
                                public boolean where(GoodsBasketStore obj) {
                                    return "1".equals(obj.merchantType);//过滤出异业门店款
                                }
                            })
                            , new ObjectIteraor<GoodsBasketStore>() {
                                @Override
                                public ActVipOrder.SaleStore getDesObj(GoodsBasketStore o) {
                                    return new ActVipOrder.SaleStore(o, true);
                                }
                            }))
                    .setVipShop(vipShop).setOnlienInfo(mchId, visitShopId));
            serviceGoodsOrderFinalPresenter.submitOrderU(result);
        }

    }

    private boolean checkHasZpInActVip(ArrayList<GoodsBasketStore> goodsBasketStoreList) {
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            if (goodsBasketStoreList.get(i).actVipResult.isHasZp()) {
                return true;
            }
        }
        return false;
    }

    private int getGoodsRealCount() {//用于验证订单提交的有效性
        int goodscount = 0;
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            GoodsBasketStore goodsBasketStore = goodsBasketStoreList.get(i);
            goodscount += goodsBasketStore.getDetails().size();
        }
        return goodscount;
    }

    Handler handlerSubmit = new Handler();
    Runnable runnableSubmit = new Runnable() {
        @Override
        public void run() {
            if ("-2".equals(getCellGoodsMarketingType()) || "0".equals(getCellGoodsMarketingType())) {
                submitFucU();
            } else {
                submitFuc("25025");
            }
        }
    };
    Runnable runnableAct = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                GoodsBasketStore goodsBasketStore = goodsBasketStoreList.get(i);
                goodsBasketStore.checkAct();//计算营销活动
                goodsBasketStore.changeItem();
            }
        }
    };

    private void submitFucU() {//线下营销活动计算
        if (vipShop == null) {
            actVipPresenter.getVipShopDetail(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", visitShopId));
        } else {
            for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                goodsBasketStoreList.get(i).changeUnderAct();
            }
            sucessSubmitU();
        }
    }

    public void submitV() {//计算价格接口 由于调用位置很多 涉及切换门店之后的问题 所以做了延迟
        if (handlerSubmit != null) {
            handlerSubmit.removeCallbacks(runnableSubmit);
        }
        handlerSubmit.postDelayed(runnableSubmit, 150);
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
//        String mErrorMessage = serviceGoodsOrderFinalPresenter.mErrorMessage;
//        if ("9".equals(goodsMarketingType) && !TextUtils.isEmpty(mErrorMessage) &&
//                mErrorMessage.contains("库存不足")) {
//            showContent();
//            //判断奖品领取错误
//            isAgree("领奖提醒", "当前奖品已被下架或无库存，暂时无法领奖，详情联系商家咨询商家:" + mallGoodsOrderFinalBlockAdapter.mShopPhone);
//            return;
//        }
    }

    private void isAgree(String title, String msg) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        mDialog = StyledDialog.buildIosAlert(
                title,
                msg,
                new MyDialogListener() {
                    @Override
                    public void onFirst() {
                    }

                    @Override
                    public void onThird() {
                        super.onThird();
                    }

                    @Override
                    public void onSecond() {

                    }
                }).setMsgColor(R.color.color_da1818).setBtnColor(0, R.color.colorPrimaryDark, 0)
                .setBtnText("确定")
                .show();
    }

    @Override
    public void sucessSubmit(final String orderId) {
        if (TextUtils.isEmpty(orderId)) {
            return;
        }
        showLoading();
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "下单页【提交订单】");
        MobclickAgent.onEvent(mContext, "btn_APP__MaternalandChildGoods_SubmitOrder_Submit", nokmap);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_CHECKOUT_COUNTER)
                        .withString("orderType", goodsMarketingType)
                        .withString("orderId", orderId)
                        .withString("bargainId", bargainId)
                        .withString("assembleId", assembleId)
                        .withString("teamNum", teamNum)
                        .navigation();
                finish();
            }
        },1000);

    }

    String errorMsg;

    @Override
    public void sucessSubmitV(String totalPayAmount, String totalPayAmountNoFee) {//25025计算
        errorMsg = null;
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            goodsBasketStoreList.get(i).totalDecimaNoFreeString = totalPayAmountNoFee;
//            goodsBasketStoreList.get(i).changeFee();
        }

        mallGoodsOrderFinalUnderAdapter.setTotalPayAmount(totalPayAmount);
        mallGoodsOrderFinalUnderAdapter.buildNowPayMoney();
    }

    @Override
    public void failOrder(String result) {
        errorMsg = result;
        if ("5".equals(getCellGoodsMarketingType())) {
            showToast(errorMsg);
            mallGoodsOrderFinalUnderAdapter.setTotalPayAmount(null);
            mallGoodsOrderFinalUnderAdapter.buildNowPayMoney();
        }
        if ("-1".equals(goodsMarketingType)) {
            showToast(errorMsg);
        }
        if (("9".equals(goodsMarketingType) || "-4".equals(goodsMarketingType)) && result != null && result.contains("库存不足")) {
            showContent();
            //判断奖品领取错误
            isAgree("领奖提醒", "当前奖品已被下架或无库存，暂时无法领奖，详情联系商家咨询商家:" + mallGoodsOrderFinalBlockAdapter.mShopPhone);
        }
    }

    public void sucessSubmitU() {//线下计算
        mallGoodsOrderFinalUnderAdapter.setTotalPayAmount(null);
        mallGoodsOrderFinalUnderAdapter.buildNowPayMoney();
//        System.out.println("展示线下计算结果");
    }

    @Override
    public void onGetAddressListSuccess(List<AddressListModel> listModels) {
        if (listModels != null && listModels.size() > 0) {
            for (int i = 0; i < listModels.size(); i++) {
                AddressListModel addressListModelTmp = listModels.get(i);
                if (addressListModelTmp.getOrderBy() == -1) {//默认地址
                    if (addressListModel == null) {
                        addressListModel = addressListModelTmp;
                    }
                    break;
                }
            }
            if (addressListModel == null) {
                addressListModel = listModels.get(0);//没有默认就取第一个
            }

        } else {
            addressListModel = null;
        }
        buildNowAddress();
    }

    @Override
    public void sucessCheck(boolean crossStore,boolean needDeliveryTime) {
        if (crossStore) {//门店是否支持切换
            for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                goodsBasketStoreList.get(i).supportOtherdeliveryShop = crossStore;
            }
            mallGoodsOrderFinalBlockAdapter.notifyDataSetChanged();
        }
        if(needDeliveryTime){//时间不是必填
            for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                goodsBasketStoreList.get(i).supportNeedTime = needDeliveryTime;
            }
            mallGoodsOrderFinalBlockAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void sucessFeeCheck(boolean result) {
        System.out.println("当前订单邮费结算商品为标准:" + result);
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            goodsBasketStoreList.get(i).supportUseGoodsMondyFree = result;
        }
        mallGoodsOrderFinalBlockAdapter.notifyDataSetChanged();
    }

    int isNtReal = 0;

    @Override
    public void onSucessIsNewAppMember(int result) {//判断是不是新客
        isNtReal = result;
        mallGoodsOrderFinalBlockAdapter.setIsNtReal(result + "");
        mallGoodsOrderFinalBlockAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSucessGetShopDetailOnly(GoodsBasketStore goodsBasketStore) {//首次门店请求回来
        if (actVipResultList != null) {
            for (int i = 0; i < actVipResultList.size(); i++) {
                if (goodsBasketStore.getDepartID().equals(actVipResultList.get(i).DepartID) && goodsBasketStore.actVipResult == null) {
                    goodsBasketStore.actVipResult = actVipResultList.get(i);
                }
            }
        }
        if (goodsBasketStore.actVipResult == null) {//说明刚进来
            goodsBasketStore.actVipReq = new ActVip();
            goodsBasketStore.actVipReq.buildWithBasket(goodsBasketStore.getGoodsBasketCellList());
        } else {//已经有返回结果了 说明刚才有过选择并且可能改变了商品数据了
            goodsBasketStore.actVipReq = goodsBasketStore.actVipResult;
            goodsBasketStore.actVipReq.buildWithBasket(goodsBasketStore.getGoodsBasketCellList());
        }
        goodsBasketStore.actVipReq.Command = "pcPreCalcPop";
        goodsBasketStore.actVipReq.setVipShop(vipShop);
        goodsBasketStore.actVipReq.DepartID = goodsBasketStore.getDepartID();
//        if(goodsBasketStore.deliver!=null){//切换门店不能切 请求 会导致优惠券异常
//            goodsBasketStore.actVipReq.DepartID=goodsBasketStore.deliver.deliveryShopDeptId;
//            goodsBasketStore.DepartID=goodsBasketStore.deliver.deliveryShopDeptId;
//        }
        if (actVipResultList != null) {
            goodsBasketStore.IsChkPopOK = "Y";
        } else {
            goodsBasketStore.IsChkPopOK = "R";
        }
        if ("1".equals(goodsBasketStore.merchantType)) {//异业商家需要特殊处理
            if (goodsbasketlist.size() == 1) {//当品
                //isOnlyYY = true;
            }
            goodsBasketStore.actVipResult = null;//去掉保持他的价格计算没问题 模拟一个
            String Total = goodsBasketStore.getgCurPrice();
            goodsBasketStore.actVipReq = new ActVip();
            goodsBasketStore.actVipReq.buildWithBasket(goodsBasketStore.getGoodsBasketCellList());
            goodsBasketStore.actVipReq.Total = Total;
            goodsBasketStore.actVipReq.DepartID = goodsBasketStore.getDepartID();
            onSucessGetVipActs(goodsBasketStore.actVipReq, goodsBasketStore);
        } else {
            actVipPresenter.getVipActs(new SimpleHashMapBuilder<String, Object>()
                            .puts(Functions.FUNCTION, "YT_100002")
                            .putObject(goodsBasketStore.actVipReq.sortAndExp(goodsBasketStore.IsChkPopOK))
                    , goodsBasketStore);
        }

    }
    ShopDetailModel visitshopDetailModel;
    @Override
    public void onSucessGetShopDetailOnlyVisit(ShopDetailModel shopDetailModel) {
        visitshopDetailModel=shopDetailModel;
        mchId=visitshopDetailModel.userId;
        getBaseData();
    }

    private void buildNowAddress() {//设置每个配送到家的初始地址
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            GoodsBasketStore goodsBasketStore = goodsBasketStoreList.get(i);
            if (addressListModel != null) {
                if (TextUtils.isEmpty(goodsBasketStore.deliver.deliveryConsigneeAddress)) {
                    goodsBasketStore.deliver.deliveryConsigneeName = addressListModel.getConsigneeName();
                    goodsBasketStore.deliver.deliveryConsigneePhone = addressListModel.getConfigneePhone();
                    goodsBasketStore.deliver.deliveryConsigneeProvince = addressListModel.getProvince();
                    goodsBasketStore.deliver.deliveryConsigneeCity = addressListModel.getCity();
                    goodsBasketStore.deliver.deliveryConsigneeDistrict = addressListModel.getDistrict();
                    goodsBasketStore.deliver.deliveryConsigneeProvinceName = addressListModel.getProvinceName();
                    goodsBasketStore.deliver.deliveryConsigneeCityName = addressListModel.getCityName();
                    goodsBasketStore.deliver.deliveryConsigneeDistrictName = addressListModel.getDistrictName();
                    goodsBasketStore.deliver.deliveryConsigneeAddress = addressListModel.getAddress() + addressListModel.getHouseNum();
                    goodsBasketStore.deliver.deliveryLatitude = addressListModel.getLat();
                    goodsBasketStore.deliver.deliveryLongitude = addressListModel.getLng();
                }

            } else {
                goodsBasketStore.deliver.deliveryConsigneeName = null;
                goodsBasketStore.deliver.deliveryConsigneePhone = null;
                goodsBasketStore.deliver.deliveryConsigneeProvince = null;
                goodsBasketStore.deliver.deliveryConsigneeCity = null;
                goodsBasketStore.deliver.deliveryConsigneeDistrict = null;
                goodsBasketStore.deliver.deliveryConsigneeProvinceName = null;
                goodsBasketStore.deliver.deliveryConsigneeCityName = null;
                goodsBasketStore.deliver.deliveryConsigneeDistrictName = null;
                goodsBasketStore.deliver.deliveryConsigneeAddress = null;
                goodsBasketStore.deliver.deliveryLatitude = null;
                goodsBasketStore.deliver.deliveryLongitude = null;
            }

        }
        if (mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow != null) {
            mallGoodsOrderFinalBlockAdapter.buildCheckResult(mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow.checkedId, mallGoodsOrderFinalBlockAdapter.goodsBasketStoreNow, mallGoodsOrderFinalBlockAdapter.storeBlockChildHolderNow);
        }
    }


    private void killgoodsCountChange(int num, GoodsBasketCell goodsBasketCell, ConstraintLayout limitCon, boolean islimitLineTextShow, TextView limitText) {
        limitCon.setVisibility(View.GONE);
        if (goodsBasketCell.goodsMarketingDTO != null && "3".equals(goodsBasketCell.goodsMarketingDTO.marketingType)) {
            if (num > goodsBasketCell.getMarkLimitMaxNowWithInventory() || num < goodsBasketCell.getMarkLimitMin()) {//说明超过限制了
                if (num > goodsBasketCell.getMarkLimitMaxNowWithInventory() || num < goodsBasketCell.getMarkLimitMin()) {
                    limitCon.setVisibility(islimitLineTextShow ? View.VISIBLE : View.VISIBLE);
                    if (goodsBasketCell.getMarkLimitMin() > goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//起购大于可购
                        limitCon.setVisibility(islimitLineTextShow ? View.INVISIBLE : View.GONE);
                    } else {
                        if (num > goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//数量大于可够超了 可能是超过库存了 可能是超过限购了
                            limitText.setText("购买" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "件及以下时享优惠！");
                            if (goodsBasketCell.getMarkLimitMinOrg() > 0) {
                                limitText.setText("购买" + goodsBasketCell.getMarkLimitMinOrg() + "-" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "件时享受优惠！");
                                if (goodsBasketCell.getMarkLimitMin() == goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//起购等于可够
                                    limitText.setText("购买" + goodsBasketCell.getMarkLimitMinOrg() + "件时享优惠！");
                                }
                            } else {
                                if (goodsBasketCell.getMarkLimitMinOrg() == 0 && goodsBasketCell.getMarkLimitMaxNowWithInventory() == 1) {//不限制起购
                                    limitText.setText("购买" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "件时享优惠！");
                                }
                            }
                            if (goodsBasketCell.getHasBuy() > 0) {
                                limitText.setText("您已购" + goodsBasketCell.getHasBuy() + "件，购买" + goodsBasketCell.getMarkLimitMinOrg() + "-" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "件时享优惠！");
                                if (goodsBasketCell.getMarkLimitMin() == goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//起购等于可够
                                    limitText.setText("购买" + goodsBasketCell.getMarkLimitMinOrg() + "件时享优惠！");
                                }
                            }
                        }
                        if (num < goodsBasketCell.getMarkLimitMin() && goodsBasketCell.getMarkLimitMinOrg() > 0) {
                            limitText.setText("购买" + goodsBasketCell.getMarkLimitMin() + "件及以上时享优惠！");
                            if (goodsBasketCell.getMarkLimitMaxNowWithInventory() > 0) {
                                limitText.setText("购买" + goodsBasketCell.getMarkLimitMinOrg() + "-" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "件时享受优惠！");
                                if (goodsBasketCell.getMarkLimitMin() == goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//起购等于可够
                                    limitText.setText("购买" + goodsBasketCell.getMarkLimitMinOrg() + "件时享优惠！");
                                }
                            }
                            if (goodsBasketCell.getHasBuy() > 0) {
                                limitText.setText("您已购" + goodsBasketCell.getHasBuy() + "件，购买" + goodsBasketCell.getMarkLimitMinOrg() + "-" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "件时享优惠！");
                                if (goodsBasketCell.getMarkLimitMin() == goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//起购等于可够
                                    limitText.setText("购买" + goodsBasketCell.getMarkLimitMinOrg() + "件时享优惠！");
                                }
                            }
                        }
                    }
                } else {
                    limitCon.setVisibility(islimitLineTextShow ? View.INVISIBLE : View.GONE);
                }
            } else {
                limitCon.setVisibility(islimitLineTextShow ? View.INVISIBLE : View.GONE);
            }
        }
    }

    public boolean isHasSepcialGoods(List<GoodsBasketCell> goodsbasketlist) {//使用优惠券的条件 主要适用于非-2情况
        if ("-1".equals(goodsMarketingType)) {
            return true;
        }
        if (goodsbasketlist.size() == 1 && !("2".equals(goodsbasketlist.get(0).getGoodsMarketingType()) || "1".equals(goodsbasketlist.get(0).getGoodsMarketingType()) || "5".equals(goodsbasketlist.get(0).getGoodsMarketingType()))) {
            return false;
        }
        for (int i = 0; i < goodsbasketlist.size(); i++) {
            GoodsBasketCell goodsBasketCell = goodsbasketlist.get(i);
            if ("6".equals(goodsBasketCell.getGoodsMarketingType()) || "7".equals(goodsBasketCell.getGoodsMarketingType()) || "3".equals(goodsBasketCell.getGoodsMarketingType()) || "2".equals(goodsBasketCell.getGoodsMarketingType()) || "1".equals(goodsBasketCell.getGoodsMarketingType()) || "5".equals(goodsBasketCell.getGoodsMarketingType())) {
                if (goodsBasketCell.getGoodsQuantity() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSepcialGoods(GoodsBasketCell goodsBasketCell) {//使用优惠券的条件 主要适用于非-2情况
        if ("-1".equals(goodsBasketCell.getGoodsMarketingType()) || "6".equals(goodsBasketCell.getGoodsMarketingType()) || "7".equals(goodsBasketCell.getGoodsMarketingType()) || "3".equals(goodsBasketCell.getGoodsMarketingType()) || "2".equals(goodsBasketCell.getGoodsMarketingType()) || "1".equals(goodsBasketCell.getGoodsMarketingType()) || "5".equals(goodsBasketCell.getGoodsMarketingType())) {
            if (goodsBasketCell.getGoodsQuantity() > 0) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    private ArrayList<GoodsBasketStore> getGoodsBasketStores() {//获取门店体
        Map<String, Object> goodsUseMap = new HashMap<>();
        ArrayList<GoodsBasketGroup> goodsBasketGroups = new ArrayList<>();
        for (int i = 0; i < goodsbasketlist.size(); i++) {
            GoodsBasketCell goodsBasketCell = goodsbasketlist.get(i);
            CouponGoodsFinalPresenter couponGoodsFinalPresenter = new CouponGoodsFinalPresenter(this, this);
            couponGoodsFinalPresenter.getCouponList(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION, "80010")
                            .puts("merchantId", mchId)
                            .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                            .puts("shopId", goodsBasketCell.goodsShopId)
                            .puts("goodsIdList", new String[]{goodsBasketCell.goodsId})
                    , goodsBasketCell);
            if (TextUtils.isEmpty(bargainId) && TextUtils.isEmpty(assembleId)) {//可能存在传错的问题
                if ("1".equals(goodsBasketCell.getGoodsMarketingType())) {
                    goodsBasketCell.goodsMarketingDTO = null;
                }
                if ("2".equals(goodsBasketCell.getGoodsMarketingType())) {
                    goodsBasketCell.goodsMarketingDTO = null;
                }
            }
            System.out.println("当前GroupTZ");
            if (goodsUseMap.get(goodsBasketCell.getGoodsShopId()) != null) {//活动按照门店分一下就行了
                GoodsBasketGroup goodsBasketGroup = (GoodsBasketGroup) goodsUseMap.get(goodsBasketCell.getGoodsShopId());
                goodsBasketGroup.goodsBasketCellList.add(goodsBasketCell);
            } else {
                GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                goodsBasketGroup.groupId = goodsBasketCell.getGroupId();
                goodsBasketGroup.marketingType = goodsBasketCell.getGoodsMarketingTypeOrg();
                goodsBasketGroup.goodsCategoryFirstId = goodsBasketCell.getGoodsCategoryFirstId();
                goodsBasketGroup.shopId = goodsBasketCell.getGoodsShopId();
                goodsBasketGroup.shopName = goodsBasketCell.goodsShopName;
                goodsBasketGroup.shopAddress = goodsBasketCell.goodsShopAddress;
                goodsUseMap.put(goodsBasketCell.getGoodsShopId(), goodsBasketGroup);
                goodsBasketGroups.add(goodsBasketGroup);
                System.out.println("当前GroupT");
            }
        }
        goodsUseMap.clear();
        ArrayList<GoodsBasketStore> GoodsBasketStores = new ArrayList<>();
        for (int i = 0; i < goodsBasketGroups.size(); i++) {
            GoodsBasketGroup goodsBasketCellGroup = goodsBasketGroups.get(i);
            if (goodsUseMap.get(goodsBasketCellGroup.shopId) != null) {//直接按照门店分
                ((GoodsBasketStore) goodsUseMap.get(goodsBasketCellGroup.shopId)).goodsBasketGroupList.add(goodsBasketCellGroup);
            } else {
                GoodsBasketStore goodsBasketGroup = new GoodsBasketStore(goodsBasketCellGroup);
                goodsBasketGroup.packageMoney = packageMoney;
                goodsBasketGroup.assemblePrice = assemblePrice;
                goodsBasketGroup.goodsPickShop = new GoodsShop(goodsBasketCellGroup.shopId, goodsBasketCellGroup.shopName, goodsBasketCellGroup.shopAddress);
                goodsBasketGroup.shopId = goodsBasketGroup.goodsPickShop.shopId;
                goodsBasketGroup.deliver.deliveryShopId = goodsBasketCellGroup.shopId;
                goodsBasketGroup.goodsCategoryFirstId = goodsBasketCellGroup.goodsCategoryFirstId;
                goodsBasketGroup.shopName = goodsBasketCellGroup.shopName;
                goodsUseMap.put(goodsBasketCellGroup.shopId, goodsBasketGroup);
                GoodsBasketStores.add(goodsBasketGroup);
            }
        }
        goodsUseMap.clear();
        for (int i = 0; i < GoodsBasketStores.size(); i++) {//对门店组进行计算营销活动
            final GoodsBasketStore goodsBasketStore = GoodsBasketStores.get(i);
            goodsBasketStore.setOnStoreActChange(new GoodsBasketStore.OnItemChange() {
                @Override
                public void bitNice() {
                    if (vipShop == null) {
                        return;
                    }

                    if ("-2".equals(getCellGoodsMarketingType()) || "0".equals(getCellGoodsMarketingType())) {//开始请求
                        if (goodsBasketStore.actVipResult == null) {//线下结果为空则请求下线下的营销活动
                            serviceGoodsOrderFinalPresenter.getShopDetailOnly(new SimpleHashMapBuilder<String, Object>()
                                    .puts("shopId", goodsBasketStore.shopId)
                                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG)), goodsBasketStore);
                        }
                    } else {
                        goodsBasketStore.setUnder(null, null, goodsBasketStore.IsChkPopOK, goodsBasketStore.getDepartID());
                    }

                }
            });

            if (vipShop != null) {
                if ("-2".equals(getCellGoodsMarketingType()) || "0".equals(getCellGoodsMarketingType())) {//开始请求
                    if (goodsBasketStore.actVipResult == null) {//线下结果为空则请求下线下的营销活动
                        serviceGoodsOrderFinalPresenter.getShopDetailOnly(new SimpleHashMapBuilder<String, Object>()
                                .puts("shopId", goodsBasketStore.shopId)
                                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG)), goodsBasketStore);
                    }
                } else {
                    goodsBasketStore.setUnder(null, null, goodsBasketStore.IsChkPopOK, goodsBasketStore.getDepartID());
                }
            }
        }

        System.out.println("当前GroupStore数量" + GoodsBasketStores.size());
        for (int i = 0; i < GoodsBasketStores.size(); i++) {
            System.out.println("当前Group数量" + GoodsBasketStores.get(i).goodsBasketGroupList.size());
        }
        CouponGoodsFinalPresenter couponGoodsFinalPresenter = new CouponGoodsFinalPresenter(this, this);
        couponGoodsFinalPresenter.getCouponMineList(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION, "80005")
                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))
                .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                .puts("page", "1")
                .puts("pageSize", "999")
                .puts("status", new String[]{"0"}));

        return GoodsBasketStores;
    }


    @Override
    public void onSucessGetBasketActList(DiscountTopModel actDetail) {

    }

    @Override
    public void onSucessGetBasketActListEx(final GoodsBasketCell goodsBasketCell) {//线上活动的 目前应该不会走
//        getNowAllActs();
//        outClick("buildCouponChange", goodsBasketCell);
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        ivBottomShader = (ImageView) findViewById(R.id.iv_bottom_shader);
        submitOrderLL = (ConstraintLayout) findViewById(R.id.submitOrderLL);
        ofderLeft = (TextView) findViewById(R.id.ofderLeft);
        orderMoney = (TextView) findViewById(R.id.orderMoney);
        orderSubmit = (TextView) findViewById(R.id.orderSubmit);
        limitLine = (ConstraintLayout) findViewById(R.id.limitLine);
        limitLineText = (TextView) findViewById(R.id.limitLineText);
        limitCon = (ConstraintLayout) findViewById(R.id.limitCon);
        limitConTop = (ImageView) findViewById(R.id.limitConTop);
        limitSpace = (Space) findViewById(R.id.limitSpace);
        limitText = (TextView) findViewById(R.id.limitText);
        discountNotCanUse = (TextView) findViewById(R.id.discountNotCanUse);
        orderShopBack = (TextView) findViewById(R.id.orderShopBack);
        topBar.setTopBarListener(new OnTopBarListener() {
            @Override
            public void onBackBtnPressed() {
                MobclickAgent.onEvent(mContext, "event2APPOrderConfirmBackClick", new SimpleHashMapBuilder<String, String>().puts("soure", "订单确认页-返回按钮点击量"));
                finish();
            }
        });
        orderMoneyOld = (TextView) findViewById(R.id.orderMoneyOld);
    }

    List<CouponInfoZ> couponInfoLeftZList = new ArrayList<>();//可用优惠券
    List<CouponInfoZ> couponInfoOrgZList = new ArrayList<>();//我的优惠券列表
    List<CouponInfoZ> selectInfo = new ArrayList<>();//选中的优惠券

    @Override
    public void sucessGetCouponList(List<CouponInfoZ> result) {
        ////System.out.println("没有买送了查询优惠券");
        getNowAllCouponInfoList();
        buildCouponOrgWithGoods();
        if ("-2".equals(getCellGoodsMarketingType()) || "0".equals(getCellGoodsMarketingType())) {
            if (couponInfoLeftZList.size() > 0) {//可用优惠券不为空 将优惠券都加入推荐里
                selectInfo.clear();
                for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                    GoodsBasketStore goodsBasketStore = goodsBasketStoreList.get(i);
                    selectInfo.addAll(new SimpleArrayListBuilder<CouponInfoZ>().putList(goodsBasketStore.getUnderCardCanUseSelect(), new ObjectIteraor<GoodsBasketCell>() {
                        @Override
                        public CouponInfoZ getDesObj(GoodsBasketCell o) {
                            return new CouponInfoZ(o);
                        }
                    }));
                }
            }
        } else {
            if (couponInfoLeftZList.size() > 0) {
                selectInfo.clear();
                CouponInfoZ bean = CouponUtil.getMatchGood(couponInfoLeftZList);//最优方案
                showToast("系统已为您选择最优折扣方案");
//            Toast.makeText(mContext, "系统已为您选择最优折扣方案", Toast.LENGTH_SHORT).show();
                selectInfo.add(bean);
            }
        }


        buildCouponWithNoDialog();
        getNowAllActs();
        buildNowSelectCouponToEveryGoods();
        buildNowPayMoney();
    }

    private void buildNowPayMoney() {//计算金额
        submitV();
    }

    private void buildCouponOrgWithGoods() {//处理优惠券组 将优惠券对象中 关联到一组商品
        for (int i = 0; i < couponInfoOrgZList.size(); i++) {
            couponInfoOrgZList.get(i).goodsBasketCellList.clear();
            for (int j = 0; j < goodsbasketlist.size(); j++) {
                GoodsBasketCell goodsBasketCell = goodsbasketlist.get(j);
                if (goodsBasketCell.getCouponInfoZList() != null) {//说明每个商品上的优惠券都初始化了
                    if (ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(goodsBasketCell.getCouponInfoZList(), new ObjectIteraor<CouponInfoZ>() {
                        @Override
                        public Object getDesObj(CouponInfoZ o) {
                            return o.getCouponId();
                        }
                    }), couponInfoOrgZList.get(i).getCouponId())) {//处理优惠券组 将优惠券对象中 关联到一组商品
                        couponInfoOrgZList.get(i).goodsBasketCellList.add(goodsBasketCell);
                    }
                }
            }
            couponInfoOrgZList.get(i).setGoodsCouponKey();
        }

    }

    @Override
    public void sucessGetCouponMineList(List<CouponInfoZ> result) {//获得我的优惠券
        couponInfoOrgZList.clear();
        couponInfoOrgZList.addAll(result);
        getNowAllCouponInfoList();
        buildCouponOrgWithGoods();
        buildCouponWithNoDialog();
    }

    ActVip.VipShop vipShop = null;

    public void getNowAllActs() {//
        mallGoodsOrderFinalBlockAdapter.setExtra(
                bargainId,
                bargainMemberId,
                bargainMoney,
                assembleId,
                teamNum,
                assemblePrice,
                packageMoney,
                packageId,
                packageQuantity,
                goodsMarketingType,
                type,
                race,
                visitShopId, getCellGoodsMarketingType(), prizeName, activityName,actVip);
        if (handlerSubmit != null) {
            handlerSubmit.removeCallbacks(runnableAct);
        }
        handlerSubmit.postDelayed(runnableAct, 150);
    }

    public void getNowAllCouponInfoList() {//构建可选优惠券
        couponInfoLeftZList.clear();
        if ("-2".equals(getCellGoodsMarketingType()) || "0".equals(getCellGoodsMarketingType())) {//是营销活动的话 优惠券 为 营销活动里的
            for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                GoodsBasketStore goodsBasketStore = goodsBasketStoreList.get(i);
                couponInfoLeftZList.addAll(new SimpleArrayListBuilder<CouponInfoZ>().putList(goodsBasketStore.getUnderCardCanUse(), new ObjectIteraor<GoodsBasketCell>() {
                    @Override
                    public CouponInfoZ getDesObj(GoodsBasketCell o) {
                        return new CouponInfoZ(o);
                    }
                }));
            }
            discountNotCanUse.setVisibility(View.GONE);
        } else {
            for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                GoodsBasketStore goodsBasketStore = goodsBasketStoreList.get(i);
                goodsBasketStore.checkAct();//计算营销活动 进行优惠金额平摊
                couponInfoLeftZList.addAll(goodsBasketStore.getNowLeftCouponList());
            }
            discountNotCanUse.setVisibility(View.GONE);
            if (isHasSepcialGoods(goodsbasketlist)) {//营销活动不让用券判断满减活动是不是达标
                discountNotCanUse.setVisibility(View.VISIBLE);
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("与促销不同享");
                couponInfoLeftZList.clear();
                for (int i = 0; i < goodsbasketlist.size(); i++) {
                    goodsbasketlist.get(i).setCouponInfoZList(new ArrayList<CouponInfoZ>());
                }
            }

            if (goodsbasketlist.size() == 1 && ("3".equals(goodsbasketlist.get(0).getGoodsMarketingType()) || "7".equals(goodsbasketlist.get(0).getGoodsMarketingType()) || "6".equals(goodsbasketlist.get(0).getGoodsMarketingType()))) {
                discountNotCanUse.setVisibility(View.VISIBLE);
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("与促销不同享");
                selectInfo.clear();
                outClick("buildNowPayMoney", null);
            }
            if (selectInfo.size() > 0) {
                boolean iscouponchange = false;//增加判断已选券是不是在可选范围
                for (int i = 0; i < selectInfo.size(); i++) {
                    if (!ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(couponInfoLeftZList, new ObjectIteraor<CouponInfoZ>() {
                        @Override
                        public String getDesObj(CouponInfoZ o) {
                            return o.getUseId();
                        }
                    }), selectInfo.get(i).getUseId())) {//不在范围内
                        selectInfo.remove(i);
                        iscouponchange = true;
                        i--;
                    }
                }
                if (iscouponchange && couponInfoLeftZList.size() > 0) {//说明还能选
                    showToast("系统已为您选择最优折扣方案");
                }
                if (couponInfoLeftZList.size() > 0) {

                } else {
                    int goodscount = getGoodsRealCount();
                    if (goodscount <= 0) {
                        //关闭Loading显示文本内容
                        return;
                    } else {
                        if ("与促销不同享".equals(mallGoodsOrderFinalUnderAdapter.couponMore.getText().toString())) {

                        } else {
                            showToast("当前门店不可用优惠券");
                        }
                    }
                }
            }
        }


    }

    private void buildNowSelectCouponToEveryGoods() {//对视图做更新
        for (int k = 0; k < goodsBasketStoreList.size(); k++) {
            GoodsBasketStore goodsBasketStore = goodsBasketStoreList.get(k);
            for (int i = 0; i < goodsBasketStore.goodsBasketGroupList.size(); i++) {
                GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(i);
                for (int j = 0; j < goodsBasketGroup.goodsBasketCellList.size(); j++) {
                    GoodsBasketCell goodsBasketCell = goodsBasketGroup.goodsBasketCellList.get(j);
                    goodsBasketCell.getCouponInfoAllSelectList().clear();
                    goodsBasketCell.getCouponInfoAllSelectList().addAll(selectInfo);
                    goodsBasketCell.changeItem();
                }
            }
        }
    }

    private void buildCouponWithNoDialog() {//修正暂无可用 和 可用 优惠券 tip
        discountNotCanUse.setVisibility(View.GONE);
        if ("-2".equals(getCellGoodsMarketingType())) {
            if (couponInfoLeftZList != null && couponInfoLeftZList.size() > 0) {
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("优惠券");
            } else {
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("暂无可用");
            }
        } else {
            if (couponInfoLeftZList != null && couponInfoLeftZList.size() > 0) {
                if (selectInfo.size() > 0) {
                } else {
                    outClick("buildNowPayMoney", null);
                    mallGoodsOrderFinalUnderAdapter.setCouponMoreText("优惠券");
                    if (getGoodsRealCount() <= 0) {//没商品啊 那干脆就暂无可用吧
                        mallGoodsOrderFinalUnderAdapter.setCouponMoreText("暂无可用");
                    }
                }
            } else {
                if (goodsbasketlist.size() == 1) {//判断是不是1个商品
                    if (goodsbasketlist.get(0).getCouponInfoZList() != null && goodsbasketlist.get(0).getCouponInfoZList().size() > 0 && !"3".equals(goodsbasketlist.get(0).getGoodsMarketingType())) {//一个商品时可以使用优惠券就是数量不满足吧

                        outClick("buildNowPayMoney", null);
                        mallGoodsOrderFinalUnderAdapter.setCouponMoreText("优惠券");
                        if (getGoodsRealCount() <= 0) {//没商品啊 那干脆就暂无可用吧
                            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("暂无可用");
                        }
                        GoodsBasketCell basketCell = goodsbasketlist.get(0);
                        if (basketCell.getGroupDiscount() > 0 || basketCell.getGoodsQuantityGiftNeedFixOrg() > 0) {
                            outClick("buildNowPayMoney", null);
                            discountNotCanUse.setVisibility(View.VISIBLE);
                            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("与促销不同享");
                            selectInfo.clear();
                            buildNowSelectCouponToEveryGoods();
                        }
                    } else {
                        outClick("buildNowPayMoney", null);
                        if (isSepcialGoods(goodsbasketlist.get(0))) {
                            discountNotCanUse.setVisibility(View.VISIBLE);
                            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("与促销不同享");
                        } else {
                            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("暂无可用");
                        }
                    }
                } else {
                    outClick("buildNowPayMoney", null);
                    mallGoodsOrderFinalUnderAdapter.setCouponMoreText("暂无可用");
                    if (isHasSepcialGoods(goodsbasketlist)) {
                        discountNotCanUse.setVisibility(View.VISIBLE);
                        mallGoodsOrderFinalUnderAdapter.setCouponMoreText("与促销不同享");
                        couponInfoLeftZList.clear();
                        for (int i = 0; i < goodsbasketlist.size(); i++) {
                            goodsbasketlist.get(i).setCouponInfoZList(new ArrayList<CouponInfoZ>());
                        }
                    }
                }
            }
        }

    }

    CouponChoseDialog couponChoseDialog;

    private void buildCouponWithDialog() {//构造优惠券dialog 并弹出
        if ("暂无可用".equals(mallGoodsOrderFinalUnderAdapter.couponMore.getText().toString()) || "与促销不同享".equals(mallGoodsOrderFinalUnderAdapter.couponMore.getText().toString())) {
            return;
        }
        if ("-2".equals(getCellGoodsMarketingType()) || "-2".equals(getCellGoodsMarketingType())) {
            if (couponInfoLeftZList != null && couponInfoLeftZList.size() > 0) {
                couponChoseDialog = CouponChoseDialog.newInstance();
                List<CouponInfoZ> couponInfoLeftZRealyList = new ArrayList<>();
                List<CouponInfoZ> couponInfoLeftZRealyRightList = new ArrayList<>();


                for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                    GoodsBasketStore basketStore = goodsBasketStoreList.get(i);
                    for (int j = 0; j < basketStore.getGoodsBasketCellList().size(); j++) {
                        basketStore.getGoodsBasketCellList().get(j).isCardSelect = false;
                        GoodsBasketCell basketCell = basketStore.getGoodsBasketCellList().get(j);
                        if (basketCell.isCardCanUse) {
                            couponInfoLeftZRealyList.add(new CouponInfoZ(basketCell));
                        }
                    }
                }
                List<IHmmCoupon> tmp = new ArrayList<>();
                for (int i = 0; i < selectInfo.size(); i++) {
                    tmp.add((IHmmCoupon) selectInfo.get(i));
                }
                couponChoseDialog.setList(couponInfoLeftZRealyList, null);
                couponChoseDialog.setSelectList(tmp);
                couponChoseDialog.setCouponOkButtonClickenlistener(new CouponChoseDialog.CouponOkButtonClickenlistener() {
                    @Override
                    public void getResult(List<IHmmCoupon> coupons) {
                        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                            GoodsBasketStore basketStore = goodsBasketStoreList.get(i);
                            for (int j = 0; j < basketStore.getGoodsBasketCellList().size(); j++) {
                                basketStore.getGoodsBasketCellList().get(j).isCardSelect = false;
                            }
                        }
                        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                            GoodsBasketStore basketStore = goodsBasketStoreList.get(i);
                            for (int j = 0; j < basketStore.getGoodsBasketCellList().size(); j++) {
                                basketStore.getGoodsBasketCellList().get(j).isCardSelect = false;
                                GoodsBasketCell basketCell = basketStore.getGoodsBasketCellList().get(j);
                                if (ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(coupons, new ObjectIteraor<IHmmCoupon>() {
                                    @Override
                                    public Object getDesObj(IHmmCoupon o) {
                                        return o.getUseId();
                                    }
                                }), basketCell.getOnlyCouponId())) {
                                    basketCell.isCardSelect = true;
                                }
                            }
                        }
                        List<CouponInfoZ> result = new ArrayList<>();
                        for (int i = 0; i < coupons.size(); i++) {
                            result.add((CouponInfoZ) coupons.get(i));
                        }
                        selectInfo.clear();
                        selectInfo.addAll(result);
                        mallGoodsOrderFinalUnderAdapter.setCouponMoreText("优惠券");
                        buildNowPayFeeMoney();
                        buildNowPayMoney();
                    }
                });
                couponChoseDialog.show(getSupportFragmentManager(), "订单优惠券");
            }
        } else {
            if (couponInfoLeftZList != null && couponInfoLeftZList.size() > 0 || (goodsbasketlist.size() == 1 && goodsbasketlist.get(0).getCouponInfoZList() != null && goodsbasketlist.get(0).getCouponInfoZList().size() > 0)) {
                couponChoseDialog = CouponChoseDialog.newInstance();

                List<CouponInfoZ> couponInfoLeftZRealyList = new ArrayList<>();
                List<CouponInfoZ> couponInfoLeftZRealyRightList = new ArrayList<>();
                for (int i = 0; i < couponInfoOrgZList.size(); i++) {//couponInfoOrgZList 我真正拥有的券 couponInfoLeftZList 我当前订单支持的券-已去重 从拥有的券里再过滤
                    if (ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(couponInfoLeftZList, new ObjectIteraor<CouponInfoZ>() {
                        @Override
                        public Object getDesObj(CouponInfoZ o) {
                            return o.getCouponId();
                        }
                    }), couponInfoOrgZList.get(i).getCouponId())) {
                        couponInfoLeftZRealyList.add(couponInfoOrgZList.get(i));
                    } else {
                        couponInfoLeftZRealyRightList.add(couponInfoOrgZList.get(i));
                    }
                }

                couponChoseDialog.setList(couponInfoLeftZRealyList, couponInfoLeftZRealyRightList);
                List<IHmmCoupon> tmp = new ArrayList<>();
                for (int i = 0; i < selectInfo.size(); i++) {
                    tmp.add((IHmmCoupon) selectInfo.get(i));
                }
                couponChoseDialog.setSelectList(tmp);
                couponChoseDialog.setCouponOkButtonClickenlistener(new CouponChoseDialog.CouponOkButtonClickenlistener() {
                    @Override
                    public void getResult(List<IHmmCoupon> org) {

                        List<CouponInfoZ> result = new ArrayList<>();
                        for (int i = 0; i < org.size(); i++) {
                            result.add((CouponInfoZ) org.get(i));
                        }
                        selectInfo.clear();
                        selectInfo.addAll(result);
                        mallGoodsOrderFinalUnderAdapter.setCouponMoreText("优惠券");
                        if (getGoodsRealCount() <= 0) {//没商品啊 那干脆就暂无可用吧
                            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("暂无可用");
                        }
                        buildNowSelectCouponToEveryGoods();
                        buildNowPayMoney();
                    }
                });
                couponChoseDialog.show(getSupportFragmentManager(), "订单优惠券");
            } else {
                Toast.makeText(mContext, "暂无可用优惠券", Toast.LENGTH_SHORT).show();
//            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("暂无可用");
//            coupons.clear();
            }
        }

    }

    private void buildNowPayFeeMoney() {
        for (int j = 0; j < goodsBasketStoreList.size(); j++) {
            GoodsBasketStore basketStore = goodsBasketStoreList.get(j);
            basketStore.changeFee();
        }
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("orderMoney".equals(function)) {
            String result = (String) obj;
            if (result.length() > 4) {
                orderMoney.setTextSize(20);
            } else if (result.length() > 3) {
                orderMoney.setTextSize(23);
            } else if (result.length() > 2) {
                orderMoney.setTextSize(25);
            } else if (result.length() > 1) {
                orderMoney.setTextSize(27);
            } else {
                orderMoney.setTextSize(27);
            }
            orderMoney.setText(result);
            if ("-5".equals(goodsMarketingType)) {
                orderMoneyOld.setVisibility(View.VISIBLE);
                orderMoneyOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                orderMoneyOld.setText(goodsBasketStoreList.get(0).getgCurPrice());
            }
        }
        if ("地址选择页面跳转".equals(function)) {
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_ADDRESS_LIST)
                    .withBoolean("isNeedSelect", true)
                    .navigation((BaseActivity) mContext, SELECT_ADDRESS_REQUEST);

        }
        if ("getNowAllActs".equals(function)) {
            removeAllActs();
            getNowAllActs();
        }
        if ("getNowAllCouponInfoList".equals(function)) {
            getNowAllCouponInfoList();
        }
        if ("buildCouponWithDialog".equals(function)) {
            buildCouponWithDialog();
        }
        if ("buildCouponWithNoDialog".equals(function)) {
            buildCouponWithNoDialog();
        }
        if ("buildCouponChange".equals(function)) {
            GoodsBasketCell goodsBasketCell = (GoodsBasketCell) obj;
            if (goodsBasketCell != null) {
                if ("-2".equals(goodsBasketCell.getGoodsMarketingTypeOrg()) || "0".equals(goodsBasketCell.getGoodsMarketingTypeOrg())) {
                    sucessGetCouponList(null);
                } else {
                    CouponGoodsFinalPresenter couponGoodsFinalPresenter = new CouponGoodsFinalPresenter(this, this);
                    couponGoodsFinalPresenter.getCouponList(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION, "80010")
                                    .puts("merchantId", mchId)
                                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                                    .puts("shopId", goodsBasketCell.getGoodsShopId())
                                    .puts("goodsIdList", new String[]{goodsBasketCell.goodsId})
                            , goodsBasketCell);
                }

            }

        }
        if ("buildActChange".equals(function)) {
            GoodsBasketCell goodsBasketCell = (GoodsBasketCell) obj;
            if (TextUtils.isEmpty(goodsBasketCell.getGoodsMarketingIdOrg())) {
                if (goodsBasketCell.goodsBasketGroup != null) {
                    goodsBasketCell.goodsBasketGroup.setDiscountTopModel(null);
                    ////System.out.println("没有买送了"+goodsBasketCell.goodsTitle);
                    onSucessGetBasketActListEx(goodsBasketCell);
                }
            } else {
                onSucessGetBasketActListEx(goodsBasketCell);
//                ServiceBasketActDetailPresenter serviceBasketActDetailPresenter = new ServiceBasketActDetailPresenter(mContext, this);
//                serviceBasketActDetailPresenter.getActDetailOnlyCell(goodsBasketCell, new SimpleHashMapBuilder<String, Object>()
//                        .puts("id", goodsBasketCell.getGoodsMarketingIdOrg())
//                        .puts("shopId", goodsBasketCell.getGoodsShopId())
//                        .puts("marketingType", goodsBasketCell.getGoodsMarketingTypeOrg())
//                );
            }

        }
        if ("buildNowPayMoney".equals(function)) {
            try {
                buildNowPayMoney();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("buildMatchCoupon".equals(function)) {
            if (selectInfo.size() <= 0 && couponInfoLeftZList.size() > 0) {//二次计算最优解
                CouponInfoZ bean = CouponUtil.getMatchGood(couponInfoLeftZList);
                showToast("系统已为您选择最优折扣方案");
                selectInfo.add(bean);
                for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                    GoodsBasketStore basketStore = goodsBasketStoreList.get(i);
                    for (int j = 0; j < basketStore.getGoodsBasketCellList().size(); j++) {
                        basketStore.getGoodsBasketCellList().get(j).isCardSelect = false;
                        GoodsBasketCell basketCell = basketStore.getGoodsBasketCellList().get(j);
                        if (ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(selectInfo, new ObjectIteraor<IHmmCoupon>() {
                            @Override
                            public Object getDesObj(IHmmCoupon o) {
                                return o.getUseId();
                            }
                        }), basketCell.getOnlyCouponId())) {
                            basketCell.isCardSelect = true;
                        }
                    }
                }
            }
        }
        if ("秒杀浮窗".equals(function)) {
            GoodsBasketCell goodsBasketCell = (GoodsBasketCell) obj;
            killgoodsCountChange(goodsBasketCell.getGoodsQuantity(), goodsBasketCell, limitCon, false, limitText);
        }
        if ("显示回退门店".equals(function)) {
            GoodsBasketStore goodsBasketStore = (GoodsBasketStore) obj;

            String orgShopId = goodsBasketStore.orgGoodsPickShop.shopId;
            String exShopId = goodsBasketStore.goodsPickShop.shopId;
            if (orgShopId.equals(exShopId)) {
                orderShopBack.setVisibility(View.GONE);
            } else {
                orderShopBack.setVisibility(View.VISIBLE);
            }
        }
    }

    private void removeAllActs() {
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            GoodsBasketStore goodsBasketStore = goodsBasketStoreList.get(i);
            goodsBasketStore.actVipResult = null;
        }
    }

    private List<GoodsBasketCell> getUnderCardCanUse() {//会的已选优惠券 进行二次回显
        List<GoodsBasketCell> result = new ArrayList<>();
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            result.addAll(goodsBasketStoreList.get(i).getUnderCardCanUse());
        }
        return result;
    }

    @Override
    public void onSucessGetVipActs(ActVip actVip, GoodsBasketStore goodsBasketStore) {//解析线下营销活动
        if (vipShop != null) {
            if (actVip != null) {
                System.out.println("修改优惠券数量前:" + goodsBasketStore.getUnderCardCanUseSelect().size());
                if (actVipResultList != null) {
                    for (int i = 0; i < actVipResultList.size(); i++) {
                        if (goodsBasketStore.getDepartID().equals(actVipResultList.get(i).DepartID) && goodsBasketStore.actVipResult == null) {
                            goodsBasketStore.actVipResult = actVipResultList.get(i);
                        }
                    }
                }
                if (goodsBasketStore.actVipResult == null) {//初次进来可能没带上推荐的活动
                    goodsBasketStore.actVipResult = actVip;//然后用请求完的这个结果再请求一次 然后的到最终的计算结果
                    goodsBasketStore.actVipResult.Command = "pcPreCalcPop";
                    goodsBasketStore.actVipResult.setVipShop(vipShop);
//                    actVipPresenter.getVipActs(new SimpleHashMapBuilder<String, Object>()
//                                    .puts(Functions.FUNCTION, "YT_100002")
//                                    .putObject(goodsBasketStore.actVipResult.sortAndExp("R"))
//                            , goodsBasketStore);
                    buildNowCell(goodsBasketStore);//营销活动计算之后可能要重新安排cell集合
                    System.out.println("修改优惠券数量后:" + goodsBasketStore.getUnderCardCanUseSelect().size());
                    sucessGetCouponList(null);//重新计算优惠券
                    sucessSubmitU();
                } else {
                    goodsBasketStore.actVipResult = actVip;
                    buildNowCell(goodsBasketStore);//营销活动计算之后可能要重新安排cell集合
                    System.out.println("修改优惠券数量后:" + goodsBasketStore.getUnderCardCanUseSelect().size());
                    sucessGetCouponList(null);//重新计算优惠券
                    sucessSubmitU();
                }
            } else {
                goodsBasketStore.actVipResult = null;
                sucessSubmitU();
            }
        }

    }

    private void buildNowCell(GoodsBasketStore goodsBasketStore) {//使用线下返回结果构造单据列表
        List<GoodsBasketCell> goodsBasketCellsCardSelect = getUnderCardCanUse();
        for (int i = 0; i < goodsBasketStore.goodsBasketGroupList.size(); i++) {
            GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(i);
            for (int j = 0; j < goodsBasketGroup.goodsBasketCellList.size(); j++) {
                GoodsBasketCell goodsBasketCell = goodsBasketGroup.goodsBasketCellList.get(j);
                if (goodsBasketCell.isGift) {
                    System.out.println("当前GroupZ移除" + goodsBasketGroup.goodsBasketCellList.get(j).goodsTitle);
                    goodsBasketGroup.goodsBasketCellList.remove(j);
                    j--;
                } else {
                }
            }
        }
        boolean needRemoveSmallerCard=false;
        if(Double.parseDouble(goodsBasketStore.actVipResult.FactTotal)<0){//优惠券用多了啊
            needRemoveSmallerCard=true;
        }
        for (int k = 0; k < goodsBasketStore.goodsBasketGroupList.size(); k++) {
            GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(k);
            System.out.println("当前Group");
            for (int i = 0; i < goodsBasketStore.actVipResult.SaleInfo.size(); i++) {//对结果进行展示
                ActVip.SaleInfo saleInfo = goodsBasketStore.actVipResult.SaleInfo.get(i);
                if ("Y".equals(saleInfo.IsZP)) {
                    if (goodsBasketGroup.goodsBasketCellList != null) {
                        goodsBasketGroup.goodsBasketCellList.addAll(getGiftCell(saleInfo, goodsBasketStore, goodsBasketCellsCardSelect));
                        if ("Y".equals(saleInfo.IsCardGoods)) {
                            System.out.println("当前GroupZ1券" + saleInfo.GoodsName);
                        }
                    }
                }


            }
        }
        if(needRemoveSmallerCard&&goodsBasketCellsCardSelect.isEmpty()){//如果超出的话 只选择最大面额的券进行使用
            List<GoodsBasketCell> cardCanUseList=goodsBasketStore.getUnderCardCanUse();
            int index=0;
            for (int i = 0; i < cardCanUseList.size(); i++) {
                if(i!=0&&cardCanUseList.get(i).goodsMarketingDTO.marketingPrice<cardCanUseList.get(index).goodsMarketingDTO.marketingPrice){
                    index=i;
                }
            }
            for (int i = 0; i < cardCanUseList.size(); i++) {
                cardCanUseList.get(i).isCardSelect=false;
            }
            cardCanUseList.get(index).isCardSelect=true;
        }
        for (int i = 0; i < goodsBasketStore.goodsBasketGroupList.size(); i++) {
            GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(i);
            for (int j = 0; j < goodsBasketGroup.goodsBasketCellList.size(); j++) {
                GoodsBasketCell goodsBasketCell = goodsBasketGroup.goodsBasketCellList.get(j);
                System.out.println("当前GroupZ剩余" + goodsBasketGroup.goodsBasketCellList.get(j).goodsTitle);
            }
        }
        System.out.println("当前GroupZ剩余结束");
        System.out.println("当前GroupZ结束修改优惠券数量后:" + goodsBasketStore.getUnderCardCanUseSelect().size());
        System.out.println("当前GroupZ结束");
        for (int i = 0; i < goodsBasketStore.goodsBasketGroupList.size(); i++) {
            GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(i);
            for (int j = 0; j < goodsBasketGroup.goodsBasketCellList.size(); j++) {
                GoodsBasketCell goodsBasketCell = goodsBasketGroup.goodsBasketCellList.get(j);
                if (!goodsBasketCell.isGift) {//不是礼物
                    int desindex = ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(goodsBasketStore.actVipResult.SaleInfo,
                            new ObjectIteraor<ActVip.SaleInfo>() {
                                @Override
                                public Object getDesObj(ActVip.SaleInfo o) {
                                    return o.getGoodsID();
                                }
                            })
                            , goodsBasketCell.getGoodsBarCode());
                    if (desindex != -1) {//匹配条码相同 则对购物车数据进行修改 使用-2活动来标记  但是目前发现 如果要修改数量达到变单 还是很难得 无法追踪还原原来的活动
                        System.out.println("匹配商品活动" + goodsBasketCell.goodsTitle + ":" + goodsBasketCell.getGoodsBarCode());
                        if (!"Y".equals(goodsBasketStore.actVipResult.SaleInfo.get(desindex).IsErr)) {
                            if (goodsBasketCell.extraGoodsBasketCell != null) {
                                if (goodsBasketCell.goodsMarketingDTO != null) {
                                    goodsBasketCell.setGoodsMarketingDTOORG(goodsBasketCell.goodsMarketingDTO);
                                }
                                ActVip.SaleInfo saleInfo = goodsBasketStore.actVipResult.SaleInfo.get(desindex);
                                GoodsBasketCell goodsBasketCellEx = goodsBasketCell.extraGoodsBasketCell;

                                goodsBasketCellEx.curGoodsAmount = Double.parseDouble(saleInfo.SalePrice);
                                goodsBasketCellEx.curGoodsRetailAmount = Double.parseDouble(saleInfo.Price);
                                GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(saleInfo.PopNo);
                                goodsMarketing.availableInventory = goodsBasketCellEx.getRealStock();//修改-2时的库存
                                goodsMarketing.marketingType = "-2";
                                System.out.println("修改商品属性获得营销活动类型2");
                                goodsMarketing.id = saleInfo.getGoodsID();
                                goodsMarketing.marketingPrice = Double.parseDouble(saleInfo.FactPrice);
                                try {
                                    goodsMarketing.salesMin = Integer.parseInt(saleInfo.Number);
                                    goodsMarketing.salesMax = Integer.parseInt(saleInfo.Number);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                goodsBasketCellEx.goodsMarketingDTO = goodsMarketing;
                            } else {
                                if (goodsBasketCell.goodsMarketingDTO != null) {
                                    goodsBasketCell.setGoodsMarketingDTOORG(goodsBasketCell.goodsMarketingDTO);
                                }
                                ActVip.SaleInfo saleInfo = goodsBasketStore.actVipResult.SaleInfo.get(desindex);
                                goodsBasketCell.curGoodsAmount = Double.parseDouble(saleInfo.SalePrice);
                                goodsBasketCell.curGoodsRetailAmount = Double.parseDouble(saleInfo.Price);
                                GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(saleInfo.PopNo);
                                goodsMarketing.availableInventory = Integer.parseInt(saleInfo.Number);
                                goodsMarketing.marketingType = "-2";
                                System.out.println("修改商品属性获得营销活动类型1");
                                goodsMarketing.id = saleInfo.getGoodsID();
                                goodsMarketing.marketingPrice = Double.parseDouble(saleInfo.FactPrice);
                                try {
                                    goodsMarketing.salesMin = Integer.parseInt(saleInfo.Number);
                                    goodsMarketing.salesMax = Integer.parseInt(saleInfo.Number);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                            }

                        }

                    }
                }
            }
        }
        mallGoodsOrderFinalBlockAdapter.notifyDataSetChanged();
        goodsBasketStore.changeItem();

    }


    public List<GoodsBasketCell> getGiftCell(ActVip.SaleInfo saleInfo, GoodsBasketStore goodsBasketStore, List<GoodsBasketCell> goodsBasketCellsCardSelect) {
        List<GoodsBasketCell> result = new ArrayList<>();
        //判断下优惠券是不是 多用了
        if ("Y".equals(saleInfo.IsCardGoods)) {//说明是可使用的优惠券 则要拆分张数摊开
            int count = Integer.parseInt(saleInfo.Number);
            for (int i = 0; i < count; i++) {
                GoodsBasketCell goodsBasketCell;
                GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(saleInfo.PopNo);

                goodsMarketing.availableInventory = 1;
//                        goodsMarketing.mapMarketingGoodsId = goodsSpecDetail.mapMarketingGoodsId;
                goodsMarketing.marketingType = "-2";
                goodsMarketing.id = saleInfo.getGoodsID();
                goodsMarketing.marketingPrice = Double.parseDouble(saleInfo.FactPrice);
                try {
                    goodsMarketing.salesMin = 1;
                    goodsMarketing.salesMax = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                goodsBasketCell = new GoodsBasketCell(Double.parseDouble(saleInfo.SalePrice),
                        Double.parseDouble(saleInfo.Price),
                        0,
                        "2",
                        "0", "0", saleInfo.getGoodsID());
                goodsBasketCell.goodsSpecDesc = "";
                goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                goodsBasketCell.mchId = mchId;
                goodsBasketCell.goodsId = "";
                goodsBasketCell.isGift = true;
                goodsBasketCell.isUseCard = !"N".equals(saleInfo.IsCardGoods);//是否是券
                goodsBasketCell.isCardCanUse = "Y".equals(saleInfo.IsCardGoods);//是否是可用的券 用来区分是不是S问题 一般是可用的说明不是S
                goodsBasketCell.isCardSelect = "Y".equals(saleInfo.IsCardGoods);//券选择标志
                goodsBasketCell.CardNo = i;//券编号
                goodsBasketCell.ischeck = true;
                goodsBasketCell.goodsShopId = goodsBasketStore.shopId;
                goodsBasketCell.goodsStock = 1;
//                    goodsBasketCell.setGoodsSpecId(saleInfo.GoodsID);
                goodsBasketCell.setGoodsBarCode(saleInfo.getGoodsID());
                goodsBasketCell.goodsTitle = saleInfo.GoodsName;
//                    goodsBasketCell.goodsImage = saleInfo.goodsImage;
                goodsBasketCell.goodsQuantity = 1;
                System.out.println("设置赠品数量" + goodsBasketCell.goodsQuantity);
                ActVipOnlinePresenter actVipOnlinePresenter = new ActVipOnlinePresenter(this, this);
                actVipOnlinePresenter.getVipOnlineGoodsWithCell(new SimpleHashMapBuilder<String, Object>()
                                .putObject(new ActVipDefault(goodsBasketCell.getGoodsBarCode(),
                                        vipShop.shopId,
                                        "",
                                        goodsBasketCell.goodsTitle,
                                        SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC),
                                        goodsBasketCell.goodsType,
                                        new ActVipDefault.GoodsChildren(goodsBasketCell.getGoodsBarCode(),
                                                goodsBasketCell.curGoodsAmount,
                                                goodsBasketCell.curGoodsRetailAmount
                                        ), new ActVipDefault.GoodsFiles()))
                        , goodsBasketCell);
                int desint = ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(goodsBasketCellsCardSelect, new ObjectIteraor<GoodsBasketCell>() {
                    @Override
                    public Object getDesObj(GoodsBasketCell o) {
                        return o.getGoodsMarketingId() + o.getGoodsBarCode() + o.goodsTitle + o.CardNo;
                    }
                }), goodsBasketCell.getGoodsMarketingId() + goodsBasketCell.getGoodsBarCode() + goodsBasketCell.goodsTitle + goodsBasketCell.CardNo);
                if (desint != -1) {
                    goodsBasketCell.isCardSelect = goodsBasketCellsCardSelect.get(desint).isCardSelect;//券选择标志
                }
                result.add(goodsBasketCell);
            }


        } else {
            GoodsBasketCell goodsBasketCell;
            GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(saleInfo.PopNo);

            goodsMarketing.availableInventory = Integer.parseInt(saleInfo.Number);
//                        goodsMarketing.mapMarketingGoodsId = goodsSpecDetail.mapMarketingGoodsId;
            goodsMarketing.marketingType = "-2";
            goodsMarketing.id = saleInfo.getGoodsID();
            goodsMarketing.marketingPrice = Double.parseDouble(saleInfo.FactPrice);
            try {
                goodsMarketing.salesMin = Integer.parseInt(saleInfo.Number);
                goodsMarketing.salesMax = Integer.parseInt(saleInfo.Number);
            } catch (Exception e) {
                e.printStackTrace();
            }
            goodsBasketCell = new GoodsBasketCell(Double.parseDouble(saleInfo.SalePrice),
                    Double.parseDouble(saleInfo.Price),
                    0,
                    "2",
                    "0", "0", saleInfo.getGoodsID());
            goodsBasketCell.goodsSpecDesc = "";
            goodsBasketCell.goodsMarketingDTO = goodsMarketing;
            goodsBasketCell.mchId = mchId;
            goodsBasketCell.goodsId = "";
            goodsBasketCell.isGift = true;
            goodsBasketCell.isUseCard = !"N".equals(saleInfo.IsCardGoods);//是否是券
            goodsBasketCell.isCardCanUse = "Y".equals(saleInfo.IsCardGoods);//是否是可用的券 用来区分是不是S问题 一般是可用的说明不是S
            goodsBasketCell.ischeck = true;
            goodsBasketCell.goodsShopId = goodsBasketStore.shopId;
            goodsBasketCell.goodsStock = Integer.parseInt(saleInfo.Number);
//                    goodsBasketCell.setGoodsSpecId(saleInfo.GoodsID);
            goodsBasketCell.setGoodsBarCode(saleInfo.getGoodsID());
            goodsBasketCell.goodsTitle = saleInfo.GoodsName;
//                    goodsBasketCell.goodsImage = saleInfo.goodsImage;
            goodsBasketCell.goodsQuantity = Integer.parseInt(saleInfo.Number);
            System.out.println("设置赠品数量" + goodsBasketCell.goodsQuantity);
            ActVipOnlinePresenter actVipOnlinePresenter = new ActVipOnlinePresenter(this, this);
            actVipOnlinePresenter.getVipOnlineGoodsWithCell(new SimpleHashMapBuilder<String, Object>()
                            .putObject(new ActVipDefault(goodsBasketCell.getGoodsBarCode(),
                                    vipShop.shopId,
                                    "",
                                    goodsBasketCell.goodsTitle,
                                    SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC),
                                    goodsBasketCell.goodsType,
                                    new ActVipDefault.GoodsChildren(goodsBasketCell.getGoodsBarCode(),
                                            goodsBasketCell.curGoodsAmount,
                                            goodsBasketCell.curGoodsRetailAmount
                                    ), new ActVipDefault.GoodsFiles()))
                    , goodsBasketCell);
            result.add(goodsBasketCell);
        }


        return result;
    }

    @Override
    public void onSucessGetVipShopDetail(ActVip.VipShop actVip) {
        this.vipShop = actVip;
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            goodsBasketStoreList.get(i).changeUnderAct();
        }
    }

    @Override
    public void onSucessGetVipOnlineGoods() {

    }
    //提交合并
}
