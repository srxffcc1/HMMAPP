package com.health.discount.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.discount.R;
import com.health.discount.adapter.KickListAdapter;
import com.health.discount.contract.KickHelpDetailContract;
import com.health.discount.presenter.KickHelpDetailPresenter;
import com.health.discount.widget.KKHelpDialog;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.MessageDialog;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.OnCustomRetryListener;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketGroup;
import com.healthy.library.model.Kick;
import com.healthy.library.model.KickResult;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.ResUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 砍价详情
 */
@Route(path = DiscountRoutes.DIS_KICKHELPDETAIL)
public class KickHelpDetailActivity extends BaseActivity implements OnRefreshLoadMoreListener, KickHelpDetailContract.View, AMapLocationListener {
    @Autowired
    String bargainId;
    @Autowired
    String bargainMemberId;
    @Autowired
    String memberId;
    @Autowired
    String shopId;//访问门店
    String marketingGoodsChildId;

    private TopBar topBar;
    private SmartRefreshLayout layoutRefresh;
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    private KickHelpDetailPresenter kickDetailPresenter;
    private KickListAdapter kickManListAdapter;
    private Map<String, Object> detailmap = new HashMap<>();
    private Map<String, Object> smap = new HashMap<>();
    private boolean isheadadd=false;
    private int currentPage = 1;
    private Kick mkick;
    public int helptype=0;//0不显示我也要参加 1显示我也要参加 2显示继续砍价 233


    private String cityNo;
    private String provinceNo;
    private String areaNo;
    private String longitude;
    private String latitude;
    private String locCityName;
    private String newCityName;
    private int RC_LOCATION = 11020;
    private int RC_PROVINCE_CITY = 14697;
    private int reLocTime = 0;
    private Bitmap sBitmap;

    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;
    private String[] mPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private Map<String, Object> kickmap=new HashMap<>();

    private void locateCheck() {
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
            locate();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(mPermissions, RC_LOCATION);
            }
        }
    }
    private void locate() {
        if(LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE)!=null){
            successLocation();
        }else {
            mLocationClient = new AMapLocationClient(mContext);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setOnceLocation(true);
            //设置定位监听
            mLocationClient.setLocationListener(this);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_LOCATION) {
            if (PermissionUtils.hasPermissions(mContext, permissions)) {
                locate();
            } else {
                if (PermissionUtils.somePermissionPermanentlyDenied(mActivity, permissions)) {
                    PermissionUtils.showRationale(mActivity);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(mPermissions, RC_LOCATION);
                    }
                }
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dis_activity_disactdetailhelp;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_wx), SHARE_MEDIA.WEIXIN);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_circle), SHARE_MEDIA.WEIXIN_CIRCLE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qq), SHARE_MEDIA.QQ);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qzone), SHARE_MEDIA.QZONE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_sina), SHARE_MEDIA.SINA);

        if(TextUtils.isEmpty(shopId)){
            shopId = SpUtils.getValue(mContext,SpKey.CHOSE_SHOP);
        }
        smap.put("shopId",shopId);//暂时先用这个 详情返回之后替换为提货门店

        kickmap.put("bargainId",bargainId);
        kickmap.put("bargainType","1");
        kickmap.put("bargainMemberId",bargainMemberId);

        kickDetailPresenter=new KickHelpDetailPresenter(mContext,this);
        kickManListAdapter=new KickListAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        kickManListAdapter.bindToRecyclerView(recycler);
        kickManListAdapter.setShopId(shopId);
        detailmap.put("bargainId",bargainId);
        detailmap.put("bargainMemberId",bargainMemberId);
        detailmap.put("type","1");

//        manmap.put("currentPage",currentPage+"");
//        manmap.put("pageSize",10+"");
//        manmap.put("bargainId",bargainId+"");
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        locateCheck();
    }

    @Override
    public void getData() {
        super.getData();
        currentPage=1;
        smap.put("currentPage",currentPage+"");
        kickDetailPresenter.kick(kickmap);//自动帮砍一下看看
    }

    @Override
    public void onSuccessGetKickList(List<Kick> kickList, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            if (currentPage == 1) {
                layoutRefresh.setEnableLoadMore(false);
            } else {
                layoutRefresh.setEnableLoadMore(false);
                layoutRefresh.finishLoadMoreWithNoMoreData();
            }

            return;
        }
        currentPage = pageInfoEarly.currentPage;
        if(currentPage==1||currentPage==0){
            isheadadd=false;
        }
        if (!isheadadd) {
            if (mkick != null) {
                addHeaderView();
                isheadadd=true;
            }
            kickManListAdapter.setNewData(kickList);
        } else {
            if(currentPage==1||currentPage==0){
                kickManListAdapter.setNewData(kickList);
            }else {
                kickManListAdapter.addData(kickList);
            }
        }
        if (pageInfoEarly.isMore != 1) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    private void addHeaderView() {
        kickManListAdapter.removeAllHeaderView();
        View headerView = LayoutInflater.from(mContext)
                .inflate(R.layout.dis_item_activity_disact_go_helptop, null);
         ConstraintLayout kickLL;
         ImageView goodsIcon;
         TextView goodsName;
         TextView goodsPrice;
         TextView goodsJoinNum;
         TextView limitMoney;
         TextView alreadyMoney;
         ProgressBar goodsPro;
         TextView shareText;
         final TextView buttomYellow;
         TextView buttomRed;
         TextView limitTime;
         LinearLayout manLL;
         TextView helpNum;


        kickLL = (ConstraintLayout) headerView.findViewById(R.id.kickLL);
        goodsIcon = (ImageView) headerView.findViewById(R.id.goodsIcon);
        goodsName = (TextView) headerView.findViewById(R.id.goodsName);
        goodsPrice = (TextView) headerView.findViewById(R.id.goodsPrice);
        goodsJoinNum = (TextView) headerView.findViewById(R.id.goodsJoinNum);
        limitMoney = (TextView) headerView.findViewById(R.id.limitMoney);
        alreadyMoney = (TextView) headerView.findViewById(R.id.alreadyMoney);
        goodsPro = (ProgressBar) headerView.findViewById(R.id.goodsPro);
        shareText = (TextView) headerView.findViewById(R.id.shareText);
        buttomYellow = (TextView) headerView.findViewById(R.id.buttomYellow);
        buttomRed = (TextView) headerView.findViewById(R.id.buttomRed);
        limitTime = (TextView) headerView.findViewById(R.id.limitTime);
        manLL = (LinearLayout) headerView.findViewById(R.id.manLL);
        helpNum = (TextView) headerView.findViewById(R.id.helpNum);

        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .asBitmap()
                .load(mkick.goodsImage)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_avatar_default)

                .into(goodsIcon);
        com.healthy.library.businessutil.GlideCopy.with(mContext).load(mkick.goodsImage)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(new SimpleTarget<Drawable>() {

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        sBitmap= DrawableUtils.drawableToBitmap(resource);
                    }
                });

        goodsName.setText(mkick.goodsTitle);
        goodsPrice.setText(String.format(Locale.CHINA, "¥ %s",
                FormatUtils.moneyKeep2Decimals(mkick.goodsPlatformPrice)));
        limitMoney.setText(String.format(Locale.CHINA, "¥%s",
                FormatUtils.moneyKeep2Decimals(mkick.floorPrice)));
        int bili= (int) (mkick.discountMoney*1.0/(mkick.goodsPlatformPrice-mkick.floorPrice)*1000);

        final double less=new BigDecimal(mkick.goodsPlatformPrice+"").subtract(new BigDecimal(mkick.floorPrice+"")).subtract(new BigDecimal(mkick.discountMoney+"")).doubleValue();



        SpannableStringBuilder alreadyMoneyBuilder;
        if(less/(mkick.goodsPlatformPrice-mkick.floorPrice)<=0.1){
            alreadyMoneyBuilder=SpanUtils.getBuilder(mContext,"距离目标仅剩").setForegroundColor(Color.parseColor("#666666"))
                    .append(new  java.text.DecimalFormat("0.00").format(less)).setForegroundColor(Color.parseColor("#FC2347"))
                    .append("元了,加油！").setForegroundColor(Color.parseColor("#666666"))
                    .create();
        }else {
            alreadyMoneyBuilder=SpanUtils.getBuilder(mContext,"已砍").setForegroundColor(Color.parseColor("#666666"))
                    .append(new  java.text.DecimalFormat("0.00").format(mkick.discountMoney)).setForegroundColor(Color.parseColor("#FC2347"))
                    .append("元，还差").setForegroundColor(Color.parseColor("#666666"))
                    .append(new  java.text.DecimalFormat("0.00").format(less)).setForegroundColor(Color.parseColor("#FC2347"))
                    .append("元").setForegroundColor(Color.parseColor("#666666"))
                    .create();
        }

        alreadyMoney.setText(alreadyMoneyBuilder);
        goodsJoinNum.setText("库存："+mkick.bargainInventory);
        goodsPro.setProgress(bili);


        TextView goodsProFFL=headerView.findViewById(R.id.goodsProFFL);
        TextView goodsProFFR=headerView.findViewById(R.id.goodsProFFR);
        LinearLayout.LayoutParams layoutParamsl= (LinearLayout.LayoutParams) goodsProFFL.getLayoutParams();
        LinearLayout.LayoutParams layoutParamsr= (LinearLayout.LayoutParams) goodsProFFR.getLayoutParams();
        layoutParamsl.weight=bili;
        layoutParamsr.weight=1000-bili;
        goodsProFFL.setLayoutParams(layoutParamsl);
        goodsProFFR.setLayoutParams(layoutParamsr);


        limitTime.setVisibility(View.GONE);
        buttomRed.setVisibility(View.GONE);
        buttomYellow.setVisibility(View.VISIBLE);
        if(helptype==0){//看过了 消失黄色按钮
            buttomYellow.setVisibility(View.GONE);
        }else if(helptype==1){
            buttomYellow.setText("我也要参加");
            if(new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)).equals(memberId)){
                //说明是我自己点开的
                buttomYellow.setText("喊好友继续砍，越砍价越低");
            }
        }else if(helptype==2){
            buttomYellow.setText("继续砍价");
            if(mkick.bargainStatus==5){//已经支付了 说明 活动就过期了
                helptype=3;
                buttomYellow.setText("活动过期");
            }
        }else if(helptype==3){
            buttomYellow.setText("活动过期");
        }else if(helptype==4){
            buttomYellow.setText("更多砍价活动");
            if(new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)).equals(memberId)){
                //说明是我自己点开的
                buttomYellow.setText("喊好友继续砍，越砍价越低");

            }
        }
        if(less<=0){//说明砍到最低价了
            //System.out.println("看到最低价了");
            alreadyMoney.setText("恭喜，终于砍到最低价！");
            if(new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)).equals(memberId)){
                //说明是我自己点开的
                buttomYellow.setText("立即购买 (¥"+mkick.floorPrice+")");
                if(mkick.bargainStatus==3||mkick.bargainStatus==4){
                    alreadyMoney.setText("已下单，待支付");
                    buttomYellow.setText("去订单列表查看");
                }
            }
        }

        buttomYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(areaNo)){
                    areaNo="0";
                }
                if("更多砍价活动".equals(buttomYellow.getText().toString())){
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_NEWKICKLIST)
                            .withString("addressProvince",(Integer.parseInt(areaNo) / 10000 * 10000)+"")
                            .withString("lng",longitude+"")
                            .withString("lat",latitude+"")
                            .withString("shopId",shopId)
                            .navigation();
                    finish();
                }else if(buttomYellow.getText().toString().contains("立即购买")) {
                    goOrder();
                }else if(buttomYellow.getText().toString().contains("订单列表")){
                    ARouter.getInstance().build(ServiceRoutes.SERVICE_USERORDER)
                            .withString("type",Constants.ORDER_TYPE_ALL)
                            .navigation();
                }else {
                    if(helptype==3){
                        Toast.makeText(mContext,"活动过期",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(mkick.bargainInventory<1){
                        Toast.makeText(mContext,"库存不足",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(helptype==1){//说明没砍过
                        ARouter.getInstance()
                                .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                .withString("shopId", mkick.getMarketingShopId() + "")
                                .withString("bargainId", bargainId)
                                .withString("bargainMemberId", bargainMemberId)
                                .navigation();
                    }else {
                        buildDes();
//                stitle = "真的很棒，我就快成功啦";
                        showShareBottomDialog2();
                    }
                }

            }
        });
        //System.out.println("进入头部");
        kickManListAdapter.addHeaderView(headerView);

        kickManListAdapter.removeAllFooterView();
        kickManListAdapter.addFooterView(LayoutInflater.from(mContext).inflate(R.layout.dis_item_activity_disact_single_bottom,null));
    }

    private void goOrder() {

        GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(null);
        goodsMarketing.availableInventory = mkick.bargainInventory;
        goodsMarketing.mapMarketingGoodsId = mkick.mapMarketingGoodsId;
        goodsMarketing.marketingType = "1";
        goodsMarketing.id = mkick.marketingGoodsChildId;
        goodsMarketing.marketingPrice = mkick.goodsPlatformPrice - mkick.discountMoney;
        goodsMarketing.salesMin = 1;
        goodsMarketing.salesMax = 1;
        GoodsBasketCell goodsBasketCell = new GoodsBasketCell(mkick.goodsPlatformPrice, mkick.goodsPlatformPrice, 0, mkick.goodsType + "", "0", "0", null);
        goodsBasketCell.goodsStock = 1;
        goodsBasketCell.goodsMarketingDTO = goodsMarketing;
        goodsBasketCell.mchId = mkick.merchantId + "";
        goodsBasketCell.goodsId = mkick.goodsId + "";
        goodsBasketCell.setGoodsSpecId(mkick.goodsChildId);
        goodsBasketCell.goodsTitle = mkick.goodsTitle;
        goodsBasketCell.goodsImage = mkick.goodsImage;
        goodsBasketCell.setGoodsQuantity(Integer.parseInt("1"));
        GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
        List<GoodsBasketCell> list = new ArrayList<>();
        goodsBasketCell.shopIdList = null;
        goodsBasketCell.ischeck = true;
        goodsBasketCell.goodsShopId = mkick.getDeliveryShopId();
        goodsBasketCell.goodsShopName = mkick.deliveryShopName;
        goodsBasketCell.goodsShopAddress = mkick.addressDetails;
        list.add(goodsBasketCell);

        ARouter.getInstance()
                .build(ServiceRoutes.SERVICE_ORDER)
                .withString("bargainId", bargainId)
                .withString("bargainMemberId", bargainMemberId)
                .withString("bargainMoney", mkick.discountMoney + "")
                .withString("visitShopId", mkick.getMarketingShopId())
                .withObject("goodsbasketlist", list)
                .navigation();
    }


    @Override
    public void onSuccessGetKickDetail(Kick kickDetail) {
        mkick=kickDetail;
        smap.put("shopId",mkick.getDeliveryShopId());
        kickDetailPresenter.getKickList(smap);
        kickDetailPresenter.getShopDetailOnly(new SimpleHashMapBuilder<String, Object>()
                .puts("shopId", mkick.getDeliveryShopId())
                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG)));
    }

    @Override
    public void onSuccessKick(KickResult result) {
//        locateCheck();
        if(result.joinStatus==1){//说明我发起过这个活动了
            helptype=2;
        }else {
            helptype=1;
        }
        KKHelpDialog.newInstance().setRedOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_KICKDETAIL)
                        .withString("bargainId",bargainId)
                        .withString("marketingShopId",mkick.getMarketingShopId())
                        .withString("deliveryShopId",mkick.getDeliveryShopId())
                        .withString("marketingGoodsChildId",mkick.marketingGoodsChildId)
                        .navigation();
            }
        }).setYellowOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDes();
//                stitle = "真的很棒，我就快成功啦";
                showShareBottomDialog2();
            }
        }).setKickResult(result).show(getSupportFragmentManager(),"kkOkDialogHelp");
        kickDetailPresenter.getKickDetail(detailmap);

    }

    private void buildDes() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
        String url = String.format("%s?type=8&scheme="+"bargaining"+"&shopId=%s&bargainId=%s&bargainMemberId=%s&memberId=%s&referral_code=%s", urlPrefix,shopId, bargainId, bargainMemberId,
                memberId,SpUtils.getValue(mContext, SpKey.GETTOKEN));
        surl=url;
        try {
            stitle="【"+mkick.goodsTitle+"】真的很棒，我就快成功啦";
        } catch (Exception e) {
            stitle="真的很棒，我就快成功啦";
            e.printStackTrace();
        }
        sdes="活动火热进行中，快快加入吧";
    }

    @Override
    public void onFailKick(String result) {
        //帮砍失败
        if("该砍价活动已结束".equals(result)){
            helptype=3;
        }else {
            helptype=4;
        }
        kickDetailPresenter.getKickDetail(detailmap);
    }
    ShopDetailModel shopDetailModel;
    @Override
    public void onSucessGetShopDetailOnly(ShopDetailModel shopDetailModel) {
        this.shopDetailModel=shopDetailModel;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        smap.put("currentPage",currentPage+"");
        kickDetailPresenter.getKickList(smap);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage=1;
        smap.put("currentPage",currentPage+"");
        kickDetailPresenter.getKickDetail(detailmap);
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation.getErrorCode() == 0) {
            LocUtil.storeLocation(mContext,aMapLocation);
            successLocation();
        } else {
            layoutStatus.updateStatus(StatusLayout.Status.STATUS_CUSTOM);
            layoutStatus.setmOnCustomNetRetryListener(new OnCustomRetryListener() {
                @Override
                public void onRetryClick() {
                    mLocationClient.startLocation();
                }
            });
            if (NavigateUtils.openGPSSettings(mContext) && reLocTime <= 1) {
                mLocationClient.startLocation();
                reLocTime++;
            } else {
                MessageDialog.newInstance()
                        .setMessageTopRes(R.drawable.dialog_message_icon_loc,"开启定位","为给您提供更好的本地化服务，请您到系统设置中打开GPS定位")
                        .setMessageOkClickListener(new MessageDialog.MessageOkClickListener() {
                            @Override
                            public void onMessageOkClick(View view) {
                                IntentUtils.toLocationSetting(mContext);
                            }
                        })
                        .show(getSupportFragmentManager(),"打开定位");

            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
    private void successLocation() {

        latitude = LocUtil.getLatitude(mContext,SpKey.LOC_CHOSE);
        longitude = LocUtil.getLongitude(mContext,SpKey.LOC_CHOSE);
        areaNo = LocUtil.getAreaNo(mContext,SpKey.LOC_CHOSE);
        cityNo = LocUtil.getCityNo(mContext,SpKey.LOC_CHOSE);
        locCityName = LocUtil.getCityNameOld(mContext,SpKey.LOC_ORG);
        newCityName = LocUtil.getCityNameOld(mContext,SpKey.LOC_CHOSE);

        if(TextUtils.isEmpty(areaNo)){
            areaNo="0";
        }
        smap.put("pageSize",10+"");
        smap.put("addressProvince",(Integer.parseInt(areaNo) / 10000 * 10000)+"");
        smap.put("longitude",longitude+"");
        smap.put("latitude",latitude+"");
        smap.put("currentPage",currentPage+"");
        getData();
    }

    private View.OnClickListener mShareClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mShareDialog.dismiss();
            //System.out.println("开启分享");
            share(mPlatformMap.get(String.valueOf(v.getTag())), surl, sdes, stitle);
//            shareText(mPlatformMap.get(String.valueOf(v.getTag())), surl, sdes, stitle);
        }
    };
    /**
     * 分享
     *
     * @param shareMedia 分享平台
     * @param url        链接地址
     * @param des        描述
     * @param title      标题
     */
    private void share(SHARE_MEDIA shareMedia, String url, String des, String title) {
        //System.out.println(url);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setThumb(new UMImage(mContext, sBitmap));
        web.setDescription(des);
        new ShareAction(this)
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(this)
                .share();
    }

//    /**
//     * 分享
//     *
//     * @param shareMedia 分享平台
//     * @param url        链接地址
//     * @param des        描述
//     * @param title      标题
//     */
//    private void shareText(SHARE_MEDIA shareMedia, String url, String des, String title) {
//        //System.out.println(url);
////        UMWeb web = new UMWeb(url);
////        web.setTitle(title);
////        web.setThumb();
////        web.setDescription(des);
//        new ShareAction(this).withText(des)
////                .withMedia(new UMImage(mContext, R.drawable.index_share_humb))
//                .setPlatform(shareMedia)
//                .setCallback(this)
//                .share();
//    }
    private AlertDialog mShareDialog;
    String surl;
    String sdes;
    String stitle;
    boolean isshare=false;
    private Map<String, SHARE_MEDIA> mPlatformMap = new HashMap<>();
    public void showShareBottomDialog2() {
        isshare=true;
        if (mShareDialog == null) {
            try {
                mShareDialog = new AlertDialog.Builder(mContext).create();
                View shareSheet = LayoutInflater.from(mContext)
                        .inflate(R.layout.dis_dialog_share_sheet, null);
                shareSheet.findViewById(R.id.iv_share_close)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mShareDialog.dismiss();
                            }
                        });
                shareSheet.findViewById(R.id.tv_wx).setOnClickListener(mShareClick);
                shareSheet.findViewById(R.id.tv_timeline).setOnClickListener(mShareClick);
                shareSheet.findViewById(R.id.tv_qq).setOnClickListener(mShareClick);
                shareSheet.findViewById(R.id.tv_qzone).setOnClickListener(mShareClick);

                mShareDialog.show();
                mShareDialog.setContentView(shareSheet);
                Window window = mShareDialog.getWindow();
                if (window != null) {
                    window.setWindowAnimations(R.style.DialogAnim);
                    View decorView = window.getDecorView();
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    params.gravity = Gravity.BOTTOM;
                    decorView.setPadding(0, 0, 0, 0);
                    decorView.setBackgroundResource(R.drawable.index_shape_share_dialog);
                    window.setAttributes(params);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {

            mShareDialog.show();
        }

    }
}
