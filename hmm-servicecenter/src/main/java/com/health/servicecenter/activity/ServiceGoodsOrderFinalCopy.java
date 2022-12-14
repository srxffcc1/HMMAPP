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
    //??????
    @Autowired
    String bargainId;
    @Autowired
    String bargainMemberId;
    @Autowired
    String bargainMoney;
    //-----------------------------------------------------
    //??????
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
    String goodsMarketingType;// -4 ?????? -5 ???????????? 9??????

    @Autowired
    String live_course;

    @Autowired
    String live_anchor;

    @Autowired
    String visitShopId;

    @Autowired
    String prizeName;//???????????? -> ????????????
    @Autowired
    String activityName;//???????????? -> ????????????
    @Autowired
    String activitySignupId;//???????????? -> ?????????????????????id

    String mchId;

    @Autowired
    String winType;//???????????? -> ????????????????????????
    @Autowired
    String winId;//???????????? -> ????????????????????????ID


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
    AddressListModel addressListModel;//????????????
    private TextView discountNotCanUse;
    private TextView orderShopBack;
//    boolean isOnlyYY = false;//????????????
    TongLianPhoneBindDialog tongLianPhoneBindDialog;
    @Autowired  //????????????????????????????????????
    List<ActVip> actVipResultList;//???????????????????????? ????????????????????? ????????????????????? ?????????????????????
    private Dialog mDialog;
    private TextView orderMoneyOld;//???????????????
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
            if (data != null) {//????????????
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

    public String getCellGoodsMarketingType() {// ??????????????????
        if (goodsbasketlist.size() == 1) {//????????????????????? ?????????????????????
            if ("-2".equals(goodsbasketlist.get(0).getGoodsMarketingTypeOrg())) {//??????????????????
                if (goodsbasketlist.get(0).getPlusPrice() > 0) {//plus
                    return "8";
                }
                return "-2"; //?????????
            } else if (!TextUtils.isEmpty(bargainId)) {//??????
                return "1";//?????????
            } else if (!TextUtils.isEmpty(assembleId)) {//??????
                return "2";//?????????
            } else if ("3".equals(goodsbasketlist.get(0).getGoodsMarketingTypeOrg())) {//??????
                return "3";//?????????
            } else if ("5".equals(goodsbasketlist.get(0).getGoodsMarketingTypeOrg())) {//??????
                return "5";//?????????
            } else if ("4".equals(goodsbasketlist.get(0).getGoodsMarketingTypeOrg())) {//??????
                return "4";//?????????
            } else if ("-1".equals(goodsbasketlist.get(0).getGoodsMarketingTypeOrg())) {//??????
                return "-1";//?????????
            } else if ("8".equals(goodsbasketlist.get(0).getGoodsMarketingTypeOrg())) {
                return "8";//?????????
            } else if ("9".equals(goodsMarketingType)) {
                return "9";//?????????
            } else if ("-4".equals(goodsMarketingType)) {
                return "-4";//?????????
            } else if ("-5".equals(goodsMarketingType)) {
                return "-5";//?????????
            } else {//????????????
                if (goodsbasketlist.get(0).getPlusPrice() > 0) {//plus
                    return "8";
                }
                return "0";// ?????????
            }
        } else {//???????????? ?????????????????? ?????????????????????
            if (!TextUtils.isEmpty(packageId)) {//??????
                return "-3";//?????????
            } else {//???????????????????????????????????????
                return "-2";// ?????????
            }
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);

        StyledDialog.init(mContext);
        if ("9".equals(goodsMarketingType) || "-4".equals(goodsMarketingType)) {
            topBar.setTitle("????????????");
            orderMoney.setVisibility(View.GONE);
            ofderLeft.setVisibility(View.GONE);
            orderSubmit.setText("????????????");
        }
        if (TextUtils.isEmpty(visitShopId)) {
            visitShopId = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP);
        }
        serviceGoodsOrderFinalPresenter = new ServiceGoodsOrderFinalPresenter(this, this);
        actVipPresenter = new ActVipPresenter(this, this);
        if (!TextUtils.isEmpty(goodsMarketingType)) {
            if ("1".equals(goodsMarketingType)) {//??????
                type = "1";
            }
            if ("2".equals(goodsMarketingType)) {//??????
                type = "4";
            }
            if ("5".equals(goodsMarketingType)) {//??????
                type = "5";
                race = "4";
            }
            if ("-1".equals(goodsMarketingType)) {//??????
                type = "0";
                race = "5";
            }
            if ("9".equals(goodsMarketingType)) {//????????????
                type = "7";
                race = "6";
            }
            if ("-4".equals(goodsMarketingType)) {//????????????
                type = "8";
                race = "7";
            }
            if ("-5".equals(goodsMarketingType)) {//????????????
                type = "11";
                race = "8";
            }
        } else {
            type = "0";
            if (goodsbasketlist.size() == 1) {//???????????? ?????????????????????
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
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("????????????");
            }
            if ("2".equals(goodsMarketingType)) {
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("????????????");
            }
            if ("5".equals(goodsMarketingType)) {
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("????????????");
            }
            if ("-1".equals(goodsMarketingType)) {
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("????????????");
            }
        } else {

        }
        if (!TextUtils.isEmpty(bargainId)) {
            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("????????????");
        }
        if (!TextUtils.isEmpty(assembleId)) {
            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("????????????");
        }
        if (!TextUtils.isEmpty(packageId)) {
            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("????????????");
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
        mallGoodsOrderFinalUnderAdapter.setModel("????????????");
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
            if ("10".equals(goodsBasketStore.deliver.deliveryType) || "20".equals(goodsBasketStore.deliver.deliveryType)) {//????????????
                if (TextUtils.isEmpty(goodsBasketStore.deliver.deliveryTime) && !"5".equals(race)&&goodsBasketStore.supportNeedTime) {
                    showToastIgnoreLife("?????????????????????");
                    return false;
                }
            } else {//????????????
                if (TextUtils.isEmpty(goodsBasketStore.deliver.deliveryConsigneeAddress)) {
                    showToastIgnoreLife("?????????????????????");
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
        MobclickAgent.onEvent(mContext, "event2APPOrderConfirmPayClick", new SimpleHashMapBuilder<String, String>().puts("soure", "???????????????-????????????????????????"));
        if (checkIllegal()) {
            showLoading();
            if ("-2".equals(getCellGoodsMarketingType()) || "0".equals(getCellGoodsMarketingType())) {//??????????????????
                submitFuc("ytb_25000");
            } else {
                if (("9".equals(goodsMarketingType) || "-4".equals(goodsMarketingType)) && !TextUtils.isEmpty(errorMsg) &&
                        errorMsg.contains("????????????")) {
                    showContent();
                    //????????????????????????
                    isAgree("????????????", "??????????????????????????????????????????????????????????????????????????????????????????:" + mallGoodsOrderFinalBlockAdapter.mShopPhone);
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
                                orderSubmit.setText("????????????");
                                orderSubmit.setEnabled(true);
                            } else {
                                orderSubmit.setText("????????????("+String.format("%ss)", reset));
                                orderSubmit.setEnabled(false);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            //System.out.println("?????????");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    public void shopback(View view) {//?????????????????????
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            GoodsBasketStore goodsBasketStore = goodsBasketStoreList.get(i);
            goodsBasketStoreList.get(i).shopBack();//?????????????????????
            for (int j = 0; j < goodsBasketStoreList.get(i).getGoodsBasketCellAllListExpFixGift().size(); j++) {
                if ("-1".equals(goodsBasketStore.goodsCategoryFirstId) && goodsBasketStore.supportOtherdeliveryShop && !"5".equals(type) && !"5".equals(race) && !"3".equals(race)) {//???????????????????????????????????????
                    GoodsBasketCell basketCell = goodsBasketStoreList.get(i).getGoodsBasketCellAllListExpFixGift().get(j);
                    basketCell.goodsShopId = goodsBasketStoreList.get(i).goodsPickShop.shopId;
                    basketCell.shopBack();//?????????????????????
                    outClick("buildCouponChange", basketCell);//?????????????????????
                }
            }
            goodsBasketStore.changeItem();//????????????????????????????????????????????????
        }
        orderShopBack.setVisibility(View.GONE);
    }

    boolean isTestTong = true;

    private void submitFuc(String fuc) {//???????????????????????????
        if ("ytb_25000".equals(fuc) || "25000".equals(fuc)) {

            TongLianMemberData tongLianMemberData= ObjUtil.getObj(SpUtils.getValue(LibApplication.getAppContext(),SpKey.TONGLIANBIZUSER),TongLianMemberData.class);
            if(tongLianMemberData!=null&&tongLianMemberData.memberInfo.isPhoneChecked) {//?????????

            }else {
                if (tongLianPhoneBindDialog == null) {
                    tongLianPhoneBindDialog = TongLianPhoneBindDialog.newInstance();
                }
                tongLianPhoneBindDialog.show(getSupportFragmentManager(), "????????????");
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

        if ("ytb_25000".equals(fuc)||"25000".equals(fuc)) {//?????????????????????????????????????????? ????????????????????? ??????????????????
//            if(!checkHasZpInActVip(goodsBasketStoreList)){
//            }
            //??????????????????5???
            if(oldsubmitTime!=0&&System.currentTimeMillis()-oldsubmitTime<(1000*5)){
                Toast.makeText(LibApplication.getAppContext(), "????????????????????????????????????!", Toast.LENGTH_SHORT).show();
                showContent();
                return;
            }
            oldsubmitTime=System.currentTimeMillis();//?????????????????????????????????
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
            if ("4".equals(goodsMarketingType) && isNtReal != 1) {//???????????? ?????????????????????
                type = "0";
            }
            SimpleHashMapBuilder<String, Object> result = new SimpleHashMapBuilder<>();
            result.puts(Functions.FUNCTION, fuc);
            if ("25000".equals(fuc)) {
                int goodscount = getGoodsRealCount();
                if (goodscount <= 0) {
                    //??????Loading??????????????????
                    showContent();
                    if ("9".equals(goodsMarketingType) || "-4".equals(goodsMarketingType) || "-5".equals(goodsMarketingType)) {
                        //????????????????????????
                        isAgree("????????????", "??????????????????????????????????????????????????????????????????????????????????????????:" + mallGoodsOrderFinalBlockAdapter.mShopPhone);
                    } else {
                        Toast.makeText(LibApplication.getAppContext(), "????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    key = "activitySignupId";//????????????????????????ID
                }
                if ("-4".equals(goodsMarketingType)) {
                    key = "lotteryWinId";//???????????????????????????ID
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
                            if (goodsBasketStore.checkAllIsService()) {//???????????????????????????
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

                //????????? ?????? ???????????? ??????
                if("1".equals(type)){//??????
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
                }else if("4".equals(type)){//4??????
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
                }else if("10".equals(type)){//??????
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
        } else {//??????????????????
            if (vipShop == null) {
                Toast.makeText(LibApplication.getAppContext(), "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean canSubmit = true;
            for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                GoodsBasketStore goodsBasketStore = goodsBasketStoreList.get(i);
                for (int j = 0; j < goodsBasketStore.getGoodsBasketCellList().size(); j++) {
                    GoodsBasketCell basketCell = goodsBasketStore.getGoodsBasketCellList().get(j);
                    if (!basketCell.isGift) {//??????????????????????????????
                        //????????????????????????????????? ?????????0
                        int cellCountReal = basketCell.getGoodsQuantityInBasket();//?????????????????????
                        int cellCountNow = basketCell.getGoodsQuantity();//?????????????????????
                        basketCell.undo();
                        int cellCountOld = basketCell.getGoodsQuantity();//?????????????????????
                        basketCell.redo();
                        if (cellCountOld != cellCountNow || cellCountNow == 0 || cellCountOld == 0) {//????????????????????? ??????????????????
                            if (cellCountOld == 0 && cellCountNow > 0 && cellCountReal == cellCountNow) {//?????????????????????

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
                    //????????????????????????
                    isAgree("????????????", "??????????????????????????????????????????????????????????????????????????????????????????:" + mallGoodsOrderFinalBlockAdapter.mShopPhone);
                } else {
                    Toast.makeText(LibApplication.getAppContext(), "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                                    return !"1".equals(obj.merchantType);//????????????????????????
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
                                    return "1".equals(obj.merchantType);//????????????????????????
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

    private int getGoodsRealCount() {//????????????????????????????????????
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
                goodsBasketStore.checkAct();//??????????????????
                goodsBasketStore.changeItem();
            }
        }
    };

    private void submitFucU() {//????????????????????????
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

    public void submitV() {//?????????????????? ???????????????????????? ????????????????????????????????? ??????????????????
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
//                mErrorMessage.contains("????????????")) {
//            showContent();
//            //????????????????????????
//            isAgree("????????????", "??????????????????????????????????????????????????????????????????????????????????????????:" + mallGoodsOrderFinalBlockAdapter.mShopPhone);
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
                .setBtnText("??????")
                .show();
    }

    @Override
    public void sucessSubmit(final String orderId) {
        if (TextUtils.isEmpty(orderId)) {
            return;
        }
        showLoading();
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "???????????????????????????");
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
    public void sucessSubmitV(String totalPayAmount, String totalPayAmountNoFee) {//25025??????
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
        if (("9".equals(goodsMarketingType) || "-4".equals(goodsMarketingType)) && result != null && result.contains("????????????")) {
            showContent();
            //????????????????????????
            isAgree("????????????", "??????????????????????????????????????????????????????????????????????????????????????????:" + mallGoodsOrderFinalBlockAdapter.mShopPhone);
        }
    }

    public void sucessSubmitU() {//????????????
        mallGoodsOrderFinalUnderAdapter.setTotalPayAmount(null);
        mallGoodsOrderFinalUnderAdapter.buildNowPayMoney();
//        System.out.println("????????????????????????");
    }

    @Override
    public void onGetAddressListSuccess(List<AddressListModel> listModels) {
        if (listModels != null && listModels.size() > 0) {
            for (int i = 0; i < listModels.size(); i++) {
                AddressListModel addressListModelTmp = listModels.get(i);
                if (addressListModelTmp.getOrderBy() == -1) {//????????????
                    if (addressListModel == null) {
                        addressListModel = addressListModelTmp;
                    }
                    break;
                }
            }
            if (addressListModel == null) {
                addressListModel = listModels.get(0);//???????????????????????????
            }

        } else {
            addressListModel = null;
        }
        buildNowAddress();
    }

    @Override
    public void sucessCheck(boolean crossStore,boolean needDeliveryTime) {
        if (crossStore) {//????????????????????????
            for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                goodsBasketStoreList.get(i).supportOtherdeliveryShop = crossStore;
            }
            mallGoodsOrderFinalBlockAdapter.notifyDataSetChanged();
        }
        if(needDeliveryTime){//??????????????????
            for (int i = 0; i < goodsBasketStoreList.size(); i++) {
                goodsBasketStoreList.get(i).supportNeedTime = needDeliveryTime;
            }
            mallGoodsOrderFinalBlockAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void sucessFeeCheck(boolean result) {
        System.out.println("???????????????????????????????????????:" + result);
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            goodsBasketStoreList.get(i).supportUseGoodsMondyFree = result;
        }
        mallGoodsOrderFinalBlockAdapter.notifyDataSetChanged();
    }

    int isNtReal = 0;

    @Override
    public void onSucessIsNewAppMember(int result) {//?????????????????????
        isNtReal = result;
        mallGoodsOrderFinalBlockAdapter.setIsNtReal(result + "");
        mallGoodsOrderFinalBlockAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSucessGetShopDetailOnly(GoodsBasketStore goodsBasketStore) {//????????????????????????
        if (actVipResultList != null) {
            for (int i = 0; i < actVipResultList.size(); i++) {
                if (goodsBasketStore.getDepartID().equals(actVipResultList.get(i).DepartID) && goodsBasketStore.actVipResult == null) {
                    goodsBasketStore.actVipResult = actVipResultList.get(i);
                }
            }
        }
        if (goodsBasketStore.actVipResult == null) {//???????????????
            goodsBasketStore.actVipReq = new ActVip();
            goodsBasketStore.actVipReq.buildWithBasket(goodsBasketStore.getGoodsBasketCellList());
        } else {//???????????????????????? ????????????????????????????????????????????????????????????
            goodsBasketStore.actVipReq = goodsBasketStore.actVipResult;
            goodsBasketStore.actVipReq.buildWithBasket(goodsBasketStore.getGoodsBasketCellList());
        }
        goodsBasketStore.actVipReq.Command = "pcPreCalcPop";
        goodsBasketStore.actVipReq.setVipShop(vipShop);
        goodsBasketStore.actVipReq.DepartID = goodsBasketStore.getDepartID();
//        if(goodsBasketStore.deliver!=null){//????????????????????? ?????? ????????????????????????
//            goodsBasketStore.actVipReq.DepartID=goodsBasketStore.deliver.deliveryShopDeptId;
//            goodsBasketStore.DepartID=goodsBasketStore.deliver.deliveryShopDeptId;
//        }
        if (actVipResultList != null) {
            goodsBasketStore.IsChkPopOK = "Y";
        } else {
            goodsBasketStore.IsChkPopOK = "R";
        }
        if ("1".equals(goodsBasketStore.merchantType)) {//??????????????????????????????
            if (goodsbasketlist.size() == 1) {//??????
                //isOnlyYY = true;
            }
            goodsBasketStore.actVipResult = null;//??????????????????????????????????????? ????????????
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

    private void buildNowAddress() {//???????????????????????????????????????
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
            if (num > goodsBasketCell.getMarkLimitMaxNowWithInventory() || num < goodsBasketCell.getMarkLimitMin()) {//?????????????????????
                if (num > goodsBasketCell.getMarkLimitMaxNowWithInventory() || num < goodsBasketCell.getMarkLimitMin()) {
                    limitCon.setVisibility(islimitLineTextShow ? View.VISIBLE : View.VISIBLE);
                    if (goodsBasketCell.getMarkLimitMin() > goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//??????????????????
                        limitCon.setVisibility(islimitLineTextShow ? View.INVISIBLE : View.GONE);
                    } else {
                        if (num > goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//???????????????????????? ???????????????????????? ????????????????????????
                            limitText.setText("??????" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "???????????????????????????");
                            if (goodsBasketCell.getMarkLimitMinOrg() > 0) {
                                limitText.setText("??????" + goodsBasketCell.getMarkLimitMinOrg() + "-" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "?????????????????????");
                                if (goodsBasketCell.getMarkLimitMin() == goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//??????????????????
                                    limitText.setText("??????" + goodsBasketCell.getMarkLimitMinOrg() + "??????????????????");
                                }
                            } else {
                                if (goodsBasketCell.getMarkLimitMinOrg() == 0 && goodsBasketCell.getMarkLimitMaxNowWithInventory() == 1) {//???????????????
                                    limitText.setText("??????" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "??????????????????");
                                }
                            }
                            if (goodsBasketCell.getHasBuy() > 0) {
                                limitText.setText("?????????" + goodsBasketCell.getHasBuy() + "????????????" + goodsBasketCell.getMarkLimitMinOrg() + "-" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "??????????????????");
                                if (goodsBasketCell.getMarkLimitMin() == goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//??????????????????
                                    limitText.setText("??????" + goodsBasketCell.getMarkLimitMinOrg() + "??????????????????");
                                }
                            }
                        }
                        if (num < goodsBasketCell.getMarkLimitMin() && goodsBasketCell.getMarkLimitMinOrg() > 0) {
                            limitText.setText("??????" + goodsBasketCell.getMarkLimitMin() + "???????????????????????????");
                            if (goodsBasketCell.getMarkLimitMaxNowWithInventory() > 0) {
                                limitText.setText("??????" + goodsBasketCell.getMarkLimitMinOrg() + "-" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "?????????????????????");
                                if (goodsBasketCell.getMarkLimitMin() == goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//??????????????????
                                    limitText.setText("??????" + goodsBasketCell.getMarkLimitMinOrg() + "??????????????????");
                                }
                            }
                            if (goodsBasketCell.getHasBuy() > 0) {
                                limitText.setText("?????????" + goodsBasketCell.getHasBuy() + "????????????" + goodsBasketCell.getMarkLimitMinOrg() + "-" + goodsBasketCell.getMarkLimitMaxNowWithInventory() + "??????????????????");
                                if (goodsBasketCell.getMarkLimitMin() == goodsBasketCell.getMarkLimitMaxNowWithInventory()) {//??????????????????
                                    limitText.setText("??????" + goodsBasketCell.getMarkLimitMinOrg() + "??????????????????");
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

    public boolean isHasSepcialGoods(List<GoodsBasketCell> goodsbasketlist) {//???????????????????????? ??????????????????-2??????
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

    public boolean isSepcialGoods(GoodsBasketCell goodsBasketCell) {//???????????????????????? ??????????????????-2??????
        if ("-1".equals(goodsBasketCell.getGoodsMarketingType()) || "6".equals(goodsBasketCell.getGoodsMarketingType()) || "7".equals(goodsBasketCell.getGoodsMarketingType()) || "3".equals(goodsBasketCell.getGoodsMarketingType()) || "2".equals(goodsBasketCell.getGoodsMarketingType()) || "1".equals(goodsBasketCell.getGoodsMarketingType()) || "5".equals(goodsBasketCell.getGoodsMarketingType())) {
            if (goodsBasketCell.getGoodsQuantity() > 0) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    private ArrayList<GoodsBasketStore> getGoodsBasketStores() {//???????????????
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
            if (TextUtils.isEmpty(bargainId) && TextUtils.isEmpty(assembleId)) {//???????????????????????????
                if ("1".equals(goodsBasketCell.getGoodsMarketingType())) {
                    goodsBasketCell.goodsMarketingDTO = null;
                }
                if ("2".equals(goodsBasketCell.getGoodsMarketingType())) {
                    goodsBasketCell.goodsMarketingDTO = null;
                }
            }
            System.out.println("??????GroupTZ");
            if (goodsUseMap.get(goodsBasketCell.getGoodsShopId()) != null) {//????????????????????????????????????
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
                System.out.println("??????GroupT");
            }
        }
        goodsUseMap.clear();
        ArrayList<GoodsBasketStore> GoodsBasketStores = new ArrayList<>();
        for (int i = 0; i < goodsBasketGroups.size(); i++) {
            GoodsBasketGroup goodsBasketCellGroup = goodsBasketGroups.get(i);
            if (goodsUseMap.get(goodsBasketCellGroup.shopId) != null) {//?????????????????????
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
        for (int i = 0; i < GoodsBasketStores.size(); i++) {//????????????????????????????????????
            final GoodsBasketStore goodsBasketStore = GoodsBasketStores.get(i);
            goodsBasketStore.setOnStoreActChange(new GoodsBasketStore.OnItemChange() {
                @Override
                public void bitNice() {
                    if (vipShop == null) {
                        return;
                    }

                    if ("-2".equals(getCellGoodsMarketingType()) || "0".equals(getCellGoodsMarketingType())) {//????????????
                        if (goodsBasketStore.actVipResult == null) {//???????????????????????????????????????????????????
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
                if ("-2".equals(getCellGoodsMarketingType()) || "0".equals(getCellGoodsMarketingType())) {//????????????
                    if (goodsBasketStore.actVipResult == null) {//???????????????????????????????????????????????????
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

        System.out.println("??????GroupStore??????" + GoodsBasketStores.size());
        for (int i = 0; i < GoodsBasketStores.size(); i++) {
            System.out.println("??????Group??????" + GoodsBasketStores.get(i).goodsBasketGroupList.size());
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
    public void onSucessGetBasketActListEx(final GoodsBasketCell goodsBasketCell) {//??????????????? ?????????????????????
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
                MobclickAgent.onEvent(mContext, "event2APPOrderConfirmBackClick", new SimpleHashMapBuilder<String, String>().puts("soure", "???????????????-?????????????????????"));
                finish();
            }
        });
        orderMoneyOld = (TextView) findViewById(R.id.orderMoneyOld);
    }

    List<CouponInfoZ> couponInfoLeftZList = new ArrayList<>();//???????????????
    List<CouponInfoZ> couponInfoOrgZList = new ArrayList<>();//?????????????????????
    List<CouponInfoZ> selectInfo = new ArrayList<>();//??????????????????

    @Override
    public void sucessGetCouponList(List<CouponInfoZ> result) {
        ////System.out.println("??????????????????????????????");
        getNowAllCouponInfoList();
        buildCouponOrgWithGoods();
        if ("-2".equals(getCellGoodsMarketingType()) || "0".equals(getCellGoodsMarketingType())) {
            if (couponInfoLeftZList.size() > 0) {//???????????????????????? ??????????????????????????????
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
                CouponInfoZ bean = CouponUtil.getMatchGood(couponInfoLeftZList);//????????????
                showToast("???????????????????????????????????????");
//            Toast.makeText(mContext, "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                selectInfo.add(bean);
            }
        }


        buildCouponWithNoDialog();
        getNowAllActs();
        buildNowSelectCouponToEveryGoods();
        buildNowPayMoney();
    }

    private void buildNowPayMoney() {//????????????
        submitV();
    }

    private void buildCouponOrgWithGoods() {//?????????????????? ????????????????????? ?????????????????????
        for (int i = 0; i < couponInfoOrgZList.size(); i++) {
            couponInfoOrgZList.get(i).goodsBasketCellList.clear();
            for (int j = 0; j < goodsbasketlist.size(); j++) {
                GoodsBasketCell goodsBasketCell = goodsbasketlist.get(j);
                if (goodsBasketCell.getCouponInfoZList() != null) {//????????????????????????????????????????????????
                    if (ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(goodsBasketCell.getCouponInfoZList(), new ObjectIteraor<CouponInfoZ>() {
                        @Override
                        public Object getDesObj(CouponInfoZ o) {
                            return o.getCouponId();
                        }
                    }), couponInfoOrgZList.get(i).getCouponId())) {//?????????????????? ????????????????????? ?????????????????????
                        couponInfoOrgZList.get(i).goodsBasketCellList.add(goodsBasketCell);
                    }
                }
            }
            couponInfoOrgZList.get(i).setGoodsCouponKey();
        }

    }

    @Override
    public void sucessGetCouponMineList(List<CouponInfoZ> result) {//?????????????????????
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

    public void getNowAllCouponInfoList() {//?????????????????????
        couponInfoLeftZList.clear();
        if ("-2".equals(getCellGoodsMarketingType()) || "0".equals(getCellGoodsMarketingType())) {//????????????????????? ????????? ??? ??????????????????
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
                goodsBasketStore.checkAct();//?????????????????? ????????????????????????
                couponInfoLeftZList.addAll(goodsBasketStore.getNowLeftCouponList());
            }
            discountNotCanUse.setVisibility(View.GONE);
            if (isHasSepcialGoods(goodsbasketlist)) {//?????????????????????????????????????????????????????????
                discountNotCanUse.setVisibility(View.VISIBLE);
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("??????????????????");
                couponInfoLeftZList.clear();
                for (int i = 0; i < goodsbasketlist.size(); i++) {
                    goodsbasketlist.get(i).setCouponInfoZList(new ArrayList<CouponInfoZ>());
                }
            }

            if (goodsbasketlist.size() == 1 && ("3".equals(goodsbasketlist.get(0).getGoodsMarketingType()) || "7".equals(goodsbasketlist.get(0).getGoodsMarketingType()) || "6".equals(goodsbasketlist.get(0).getGoodsMarketingType()))) {
                discountNotCanUse.setVisibility(View.VISIBLE);
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("??????????????????");
                selectInfo.clear();
                outClick("buildNowPayMoney", null);
            }
            if (selectInfo.size() > 0) {
                boolean iscouponchange = false;//?????????????????????????????????????????????
                for (int i = 0; i < selectInfo.size(); i++) {
                    if (!ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(couponInfoLeftZList, new ObjectIteraor<CouponInfoZ>() {
                        @Override
                        public String getDesObj(CouponInfoZ o) {
                            return o.getUseId();
                        }
                    }), selectInfo.get(i).getUseId())) {//???????????????
                        selectInfo.remove(i);
                        iscouponchange = true;
                        i--;
                    }
                }
                if (iscouponchange && couponInfoLeftZList.size() > 0) {//???????????????
                    showToast("???????????????????????????????????????");
                }
                if (couponInfoLeftZList.size() > 0) {

                } else {
                    int goodscount = getGoodsRealCount();
                    if (goodscount <= 0) {
                        //??????Loading??????????????????
                        return;
                    } else {
                        if ("??????????????????".equals(mallGoodsOrderFinalUnderAdapter.couponMore.getText().toString())) {

                        } else {
                            showToast("??????????????????????????????");
                        }
                    }
                }
            }
        }


    }

    private void buildNowSelectCouponToEveryGoods() {//??????????????????
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

    private void buildCouponWithNoDialog() {//?????????????????? ??? ?????? ????????? tip
        discountNotCanUse.setVisibility(View.GONE);
        if ("-2".equals(getCellGoodsMarketingType())) {
            if (couponInfoLeftZList != null && couponInfoLeftZList.size() > 0) {
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("?????????");
            } else {
                mallGoodsOrderFinalUnderAdapter.setCouponMoreText("????????????");
            }
        } else {
            if (couponInfoLeftZList != null && couponInfoLeftZList.size() > 0) {
                if (selectInfo.size() > 0) {
                } else {
                    outClick("buildNowPayMoney", null);
                    mallGoodsOrderFinalUnderAdapter.setCouponMoreText("?????????");
                    if (getGoodsRealCount() <= 0) {//???????????? ???????????????????????????
                        mallGoodsOrderFinalUnderAdapter.setCouponMoreText("????????????");
                    }
                }
            } else {
                if (goodsbasketlist.size() == 1) {//???????????????1?????????
                    if (goodsbasketlist.get(0).getCouponInfoZList() != null && goodsbasketlist.get(0).getCouponInfoZList().size() > 0 && !"3".equals(goodsbasketlist.get(0).getGoodsMarketingType())) {//????????????????????????????????????????????????????????????

                        outClick("buildNowPayMoney", null);
                        mallGoodsOrderFinalUnderAdapter.setCouponMoreText("?????????");
                        if (getGoodsRealCount() <= 0) {//???????????? ???????????????????????????
                            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("????????????");
                        }
                        GoodsBasketCell basketCell = goodsbasketlist.get(0);
                        if (basketCell.getGroupDiscount() > 0 || basketCell.getGoodsQuantityGiftNeedFixOrg() > 0) {
                            outClick("buildNowPayMoney", null);
                            discountNotCanUse.setVisibility(View.VISIBLE);
                            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("??????????????????");
                            selectInfo.clear();
                            buildNowSelectCouponToEveryGoods();
                        }
                    } else {
                        outClick("buildNowPayMoney", null);
                        if (isSepcialGoods(goodsbasketlist.get(0))) {
                            discountNotCanUse.setVisibility(View.VISIBLE);
                            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("??????????????????");
                        } else {
                            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("????????????");
                        }
                    }
                } else {
                    outClick("buildNowPayMoney", null);
                    mallGoodsOrderFinalUnderAdapter.setCouponMoreText("????????????");
                    if (isHasSepcialGoods(goodsbasketlist)) {
                        discountNotCanUse.setVisibility(View.VISIBLE);
                        mallGoodsOrderFinalUnderAdapter.setCouponMoreText("??????????????????");
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

    private void buildCouponWithDialog() {//???????????????dialog ?????????
        if ("????????????".equals(mallGoodsOrderFinalUnderAdapter.couponMore.getText().toString()) || "??????????????????".equals(mallGoodsOrderFinalUnderAdapter.couponMore.getText().toString())) {
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
                        mallGoodsOrderFinalUnderAdapter.setCouponMoreText("?????????");
                        buildNowPayFeeMoney();
                        buildNowPayMoney();
                    }
                });
                couponChoseDialog.show(getSupportFragmentManager(), "???????????????");
            }
        } else {
            if (couponInfoLeftZList != null && couponInfoLeftZList.size() > 0 || (goodsbasketlist.size() == 1 && goodsbasketlist.get(0).getCouponInfoZList() != null && goodsbasketlist.get(0).getCouponInfoZList().size() > 0)) {
                couponChoseDialog = CouponChoseDialog.newInstance();

                List<CouponInfoZ> couponInfoLeftZRealyList = new ArrayList<>();
                List<CouponInfoZ> couponInfoLeftZRealyRightList = new ArrayList<>();
                for (int i = 0; i < couponInfoOrgZList.size(); i++) {//couponInfoOrgZList ????????????????????? couponInfoLeftZList ???????????????????????????-????????? ???????????????????????????
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
                        mallGoodsOrderFinalUnderAdapter.setCouponMoreText("?????????");
                        if (getGoodsRealCount() <= 0) {//???????????? ???????????????????????????
                            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("????????????");
                        }
                        buildNowSelectCouponToEveryGoods();
                        buildNowPayMoney();
                    }
                });
                couponChoseDialog.show(getSupportFragmentManager(), "???????????????");
            } else {
                Toast.makeText(mContext, "?????????????????????", Toast.LENGTH_SHORT).show();
//            mallGoodsOrderFinalUnderAdapter.setCouponMoreText("????????????");
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
                orderMoneyOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //?????????
                orderMoneyOld.setText(goodsBasketStoreList.get(0).getgCurPrice());
            }
        }
        if ("????????????????????????".equals(function)) {
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
                    ////System.out.println("???????????????"+goodsBasketCell.goodsTitle);
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
            if (selectInfo.size() <= 0 && couponInfoLeftZList.size() > 0) {//?????????????????????
                CouponInfoZ bean = CouponUtil.getMatchGood(couponInfoLeftZList);
                showToast("???????????????????????????????????????");
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
        if ("????????????".equals(function)) {
            GoodsBasketCell goodsBasketCell = (GoodsBasketCell) obj;
            killgoodsCountChange(goodsBasketCell.getGoodsQuantity(), goodsBasketCell, limitCon, false, limitText);
        }
        if ("??????????????????".equals(function)) {
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

    private List<GoodsBasketCell> getUnderCardCanUse() {//????????????????????? ??????????????????
        List<GoodsBasketCell> result = new ArrayList<>();
        for (int i = 0; i < goodsBasketStoreList.size(); i++) {
            result.addAll(goodsBasketStoreList.get(i).getUnderCardCanUse());
        }
        return result;
    }

    @Override
    public void onSucessGetVipActs(ActVip actVip, GoodsBasketStore goodsBasketStore) {//????????????????????????
        if (vipShop != null) {
            if (actVip != null) {
                System.out.println("????????????????????????:" + goodsBasketStore.getUnderCardCanUseSelect().size());
                if (actVipResultList != null) {
                    for (int i = 0; i < actVipResultList.size(); i++) {
                        if (goodsBasketStore.getDepartID().equals(actVipResultList.get(i).DepartID) && goodsBasketStore.actVipResult == null) {
                            goodsBasketStore.actVipResult = actVipResultList.get(i);
                        }
                    }
                }
                if (goodsBasketStore.actVipResult == null) {//??????????????????????????????????????????
                    goodsBasketStore.actVipResult = actVip;//???????????????????????????????????????????????? ?????????????????????????????????
                    goodsBasketStore.actVipResult.Command = "pcPreCalcPop";
                    goodsBasketStore.actVipResult.setVipShop(vipShop);
//                    actVipPresenter.getVipActs(new SimpleHashMapBuilder<String, Object>()
//                                    .puts(Functions.FUNCTION, "YT_100002")
//                                    .putObject(goodsBasketStore.actVipResult.sortAndExp("R"))
//                            , goodsBasketStore);
                    buildNowCell(goodsBasketStore);//?????????????????????????????????????????????cell??????
                    System.out.println("????????????????????????:" + goodsBasketStore.getUnderCardCanUseSelect().size());
                    sucessGetCouponList(null);//?????????????????????
                    sucessSubmitU();
                } else {
                    goodsBasketStore.actVipResult = actVip;
                    buildNowCell(goodsBasketStore);//?????????????????????????????????????????????cell??????
                    System.out.println("????????????????????????:" + goodsBasketStore.getUnderCardCanUseSelect().size());
                    sucessGetCouponList(null);//?????????????????????
                    sucessSubmitU();
                }
            } else {
                goodsBasketStore.actVipResult = null;
                sucessSubmitU();
            }
        }

    }

    private void buildNowCell(GoodsBasketStore goodsBasketStore) {//??????????????????????????????????????????
        List<GoodsBasketCell> goodsBasketCellsCardSelect = getUnderCardCanUse();
        for (int i = 0; i < goodsBasketStore.goodsBasketGroupList.size(); i++) {
            GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(i);
            for (int j = 0; j < goodsBasketGroup.goodsBasketCellList.size(); j++) {
                GoodsBasketCell goodsBasketCell = goodsBasketGroup.goodsBasketCellList.get(j);
                if (goodsBasketCell.isGift) {
                    System.out.println("??????GroupZ??????" + goodsBasketGroup.goodsBasketCellList.get(j).goodsTitle);
                    goodsBasketGroup.goodsBasketCellList.remove(j);
                    j--;
                } else {
                }
            }
        }
        boolean needRemoveSmallerCard=false;
        if(Double.parseDouble(goodsBasketStore.actVipResult.FactTotal)<0){//?????????????????????
            needRemoveSmallerCard=true;
        }
        for (int k = 0; k < goodsBasketStore.goodsBasketGroupList.size(); k++) {
            GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(k);
            System.out.println("??????Group");
            for (int i = 0; i < goodsBasketStore.actVipResult.SaleInfo.size(); i++) {//?????????????????????
                ActVip.SaleInfo saleInfo = goodsBasketStore.actVipResult.SaleInfo.get(i);
                if ("Y".equals(saleInfo.IsZP)) {
                    if (goodsBasketGroup.goodsBasketCellList != null) {
                        goodsBasketGroup.goodsBasketCellList.addAll(getGiftCell(saleInfo, goodsBasketStore, goodsBasketCellsCardSelect));
                        if ("Y".equals(saleInfo.IsCardGoods)) {
                            System.out.println("??????GroupZ1???" + saleInfo.GoodsName);
                        }
                    }
                }


            }
        }
        if(needRemoveSmallerCard&&goodsBasketCellsCardSelect.isEmpty()){//?????????????????? ???????????????????????????????????????
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
                System.out.println("??????GroupZ??????" + goodsBasketGroup.goodsBasketCellList.get(j).goodsTitle);
            }
        }
        System.out.println("??????GroupZ????????????");
        System.out.println("??????GroupZ??????????????????????????????:" + goodsBasketStore.getUnderCardCanUseSelect().size());
        System.out.println("??????GroupZ??????");
        for (int i = 0; i < goodsBasketStore.goodsBasketGroupList.size(); i++) {
            GoodsBasketGroup goodsBasketGroup = goodsBasketStore.goodsBasketGroupList.get(i);
            for (int j = 0; j < goodsBasketGroup.goodsBasketCellList.size(); j++) {
                GoodsBasketCell goodsBasketCell = goodsBasketGroup.goodsBasketCellList.get(j);
                if (!goodsBasketCell.isGift) {//????????????
                    int desindex = ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(goodsBasketStore.actVipResult.SaleInfo,
                            new ObjectIteraor<ActVip.SaleInfo>() {
                                @Override
                                public Object getDesObj(ActVip.SaleInfo o) {
                                    return o.getGoodsID();
                                }
                            })
                            , goodsBasketCell.getGoodsBarCode());
                    if (desindex != -1) {//?????????????????? ????????????????????????????????? ??????-2???????????????  ?????????????????? ????????????????????????????????? ??????????????? ?????????????????????????????????
                        System.out.println("??????????????????" + goodsBasketCell.goodsTitle + ":" + goodsBasketCell.getGoodsBarCode());
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
                                goodsMarketing.availableInventory = goodsBasketCellEx.getRealStock();//??????-2????????????
                                goodsMarketing.marketingType = "-2";
                                System.out.println("??????????????????????????????????????????2");
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
                                System.out.println("??????????????????????????????????????????1");
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
        //??????????????????????????? ?????????
        if ("Y".equals(saleInfo.IsCardGoods)) {//?????????????????????????????? ????????????????????????
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
                goodsBasketCell.isUseCard = !"N".equals(saleInfo.IsCardGoods);//????????????
                goodsBasketCell.isCardCanUse = "Y".equals(saleInfo.IsCardGoods);//????????????????????? ?????????????????????S?????? ??????????????????????????????S
                goodsBasketCell.isCardSelect = "Y".equals(saleInfo.IsCardGoods);//???????????????
                goodsBasketCell.CardNo = i;//?????????
                goodsBasketCell.ischeck = true;
                goodsBasketCell.goodsShopId = goodsBasketStore.shopId;
                goodsBasketCell.goodsStock = 1;
//                    goodsBasketCell.setGoodsSpecId(saleInfo.GoodsID);
                goodsBasketCell.setGoodsBarCode(saleInfo.getGoodsID());
                goodsBasketCell.goodsTitle = saleInfo.GoodsName;
//                    goodsBasketCell.goodsImage = saleInfo.goodsImage;
                goodsBasketCell.goodsQuantity = 1;
                System.out.println("??????????????????" + goodsBasketCell.goodsQuantity);
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
                    goodsBasketCell.isCardSelect = goodsBasketCellsCardSelect.get(desint).isCardSelect;//???????????????
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
            goodsBasketCell.isUseCard = !"N".equals(saleInfo.IsCardGoods);//????????????
            goodsBasketCell.isCardCanUse = "Y".equals(saleInfo.IsCardGoods);//????????????????????? ?????????????????????S?????? ??????????????????????????????S
            goodsBasketCell.ischeck = true;
            goodsBasketCell.goodsShopId = goodsBasketStore.shopId;
            goodsBasketCell.goodsStock = Integer.parseInt(saleInfo.Number);
//                    goodsBasketCell.setGoodsSpecId(saleInfo.GoodsID);
            goodsBasketCell.setGoodsBarCode(saleInfo.getGoodsID());
            goodsBasketCell.goodsTitle = saleInfo.GoodsName;
//                    goodsBasketCell.goodsImage = saleInfo.goodsImage;
            goodsBasketCell.goodsQuantity = Integer.parseInt(saleInfo.Number);
            System.out.println("??????????????????" + goodsBasketCell.goodsQuantity);
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
    //????????????
}
