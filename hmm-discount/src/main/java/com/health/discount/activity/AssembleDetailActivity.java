package com.health.discount.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.health.discount.contract.GroupDetailContract;
import com.health.discount.presenter.GroupDetailPresenter;
import com.health.discount.utils.ActivityManager;
import com.health.discount.widget.KKGroupDialog;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.MessageDialog;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnCustomRetryListener;
import com.healthy.library.model.Goods2DetailPin;
import com.healthy.library.model.KKGroupDetail;
import com.healthy.library.model.KKGroupSimple;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.ResUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.utils.StringUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.SectionView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = DiscountRoutes.DIS_GROUPDETAIL)
public class AssembleDetailActivity extends BaseActivity implements IsFitsSystemWindows, GroupDetailContract.View, OnRefreshLoadMoreListener, AMapLocationListener {
    @Autowired
    String teamNum;

    @Autowired
    String shopId;

    Map<String, Object> detailmap = new HashMap<>();

    String type;//拼团支付成功，拼团成功，拼团失败，拼团结束，参与别人分享的团
    GroupDetailPresenter groupDetailPresenter;
    KKGroupDetail kkGroupDetail;
    private com.healthy.library.widget.TopBar topBar;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private android.widget.LinearLayout groupFrame;
    private ConstraintLayout goodsView;
    private com.healthy.library.widget.CornerImageView goodsIcon;
    private android.widget.TextView goodsName;
    private android.widget.TextView goodsPrice;
    private android.widget.TextView goodsGroupPin;
    private android.widget.TextView goodsGroupPinMoney;
    private android.widget.LinearLayout helpIconRightLL;
    private android.widget.ImageView helpIconLeft;
    private android.widget.TextView groupMoneyTip;
    private android.widget.TextView goodsAddress;
    private android.widget.LinearLayout goodsTimeLL;
    private android.widget.TextView kickFinish;
    private android.widget.TextView kickHour;
    private android.widget.TextView kickMin;
    private android.widget.TextView kickSec;
    private LinearLayout personFrame;
    private CornerImageView personIcon;
    private TextView personText;
    private TextView goodsTip;
    private TextView goodsAddressSmall;
    private ImageView groupOk;
    private TextView groupTitle;
    private TextView groupTitleTip;
    private TextView groupButton;
    private com.healthy.library.widget.SectionView groupGoodsName;
    private com.healthy.library.widget.SectionView groupStartTime;
    private LinearLayout manList1;
    private LinearLayout manList2;
    private LinearLayout goodsTimeLLS;
    private View groupGoodsTop;
    CountDownTimer countDownTimer;
    String surl;
    String sdes;
    String stitle;
    String spath;
    private Bitmap sBitmap;
    boolean isshare = false;


    private Map<String, SHARE_MEDIA> mPlatformMap = new HashMap<>();
    private String lessman;


    private String cityNo;
    private String provinceNo;
    private String areaNo;
    private String longitude;
    private String latitude;
    private String locCityName;
    private String newCityName;
    private int RC_LOCATION = 11025;
    private int RC_PROVINCE_CITY = 14691;
    private int reLocTime = 0;

    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;
    private TextView personNameText;


    private LinearLayout groupTT;
    private TextView backButton;


    @Override
    protected int getLayoutId() {
        return R.layout.dis_activity_group_detail;
    }

    private String[] mPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private void locateCheck() {
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
            locate();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(mPermissions, RC_LOCATION);
            }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void locate() {
        if (LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE) != null) {
            successLocation();
        } else {
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
    protected void init(Bundle savedInstanceState) {
        ActivityManager.addActivity(this);
        ARouter.getInstance().inject(this);
        if(TextUtils.isEmpty(shopId)){
            shopId=SpUtils.getValue(mContext,SpKey.CHOSE_SHOP);
        }
        groupDetailPresenter = new GroupDetailPresenter(mContext, this);
        detailmap.put("teamNum", teamNum);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        layoutRefresh.setEnableLoadMore(false);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_wx), SHARE_MEDIA.WEIXIN);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_circle), SHARE_MEDIA.WEIXIN_CIRCLE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qq), SHARE_MEDIA.QQ);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qzone), SHARE_MEDIA.QZONE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_sina), SHARE_MEDIA.SINA);
        locateCheck();
        getData();

    }

    private void buildDes() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);

        String url = String.format("%s?type=8&scheme="+"assembleing"+"&shopId=%s&referral_code=%s&teamNum=%s", urlPrefix,shopId,SpUtils.getValue(mContext,SpKey.GETTOKEN),teamNum + "");
        surl = url;
        String path = String.format("/pages/home/markting/marketStatus/index?shopId=%s&teamNum=%s&referral_code=%s",
                shopId,teamNum + "",SpUtils.getValue(mContext,SpKey.GETTOKEN));
        spath = path;
        //System.out.println("分享的连接" + surl);
        stitle = "【仅剩" + lessman + "个名额】跟我拼团￥" + StringUtils.getResultPrice(kkGroupDetail.assemblePrice + "") + "买“" + kkGroupDetail.goodsTitle + "”";
        sdes = "活动火热进行中，快快加入吧";
    }


    @Override
    protected void findViews() {
        super.findViews();
        initView();
        groupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("拼团支付成功".equals(type)) {

                    buildDes();
//                    shareMin(SHARE_MEDIA.WEIXIN, surl, sdes, stitle, spath);
                    share(SHARE_MEDIA.WEIXIN,surl,sdes,stitle);

                }
                if ("拼团成功".equals(type)) {
                    ARouter.getInstance()
                            .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                            .withString("shopId", shopId)
                            .withString("marketingType", "2")
                            .withString("assembleId", kkGroupDetail.assembleId)
                            .withString("bargainId", null)
                            .withString("bargainMemberId", null)
                            .withString("goodsId", kkGroupDetail.goodsId)
                            .navigation();
                    finish();
                }
                if ("拼团失败".equals(type)) {
                    Date end = null;
                    try {
                        end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(kkGroupDetail.endTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ARouter.getInstance()
                            .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                            .withString("shopId", shopId)
                            .withString("marketingType", "2")
                            .withString("assembleId", kkGroupDetail.assembleId)
                            .withString("bargainId", null)
                            .withString("bargainMemberId", null)
                            .withString("goodsId", kkGroupDetail.goodsId)
                            .navigation();
                    finish();
                }
                if ("拼团结束".equals(type)) {
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_NEWASSEMBLEACTIVITY)
                            .navigation();
                    finish();
                }
                if ("参与别人分享的团".equals(type)) {
//                    showSkuAct(true);
                    //可能要跳详情了
                    ARouter.getInstance()
                            .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                            .withString("shopId", shopId)
                            .withString("marketingType", "2")
                            .withString("teamNum", teamNum)
                            .withString("assembleId", kkGroupDetail.assembleId)
                            .withString("bargainId", null)
                            .withString("bargainMemberId", null)
                            .withString("goodsId", kkGroupDetail.goodsId)
                            .navigation();
                }
            }
        });
    }

//    GoodsDetail goodsDetail;
//    Goods2DetailPin goods2Model;
//    String selectSku;
//    private List<GoodsDetail.GoodsChildren> goodsSpecList;
//    private OnlyActGoodsSpecDialog goodsSpecDialog;

//    private void showSkuAct(final boolean isrightbuy) {
//
//        if(goodsSpecList!=null&&goodsSpecList.size()>1){//多规格
//            if (goodsSpecDialog == null) {
//                goodsSpecDialog=OnlyActGoodsSpecDialog.newInstance();
//            }
//            goodsSpecDialog.show(getSupportFragmentManager(), "优惠券");
//            goodsSpecDialog.setGoodsSpecDetail(goodsSpecDetail);
//            goodsSpecDialog.initSpec(goodsSpecList);
//            goodsSpecDialog.setMarketingType("2");
//            goodsSpecDialog.setRegimentSize(goods2Model.assembleDTO.regimentSize);
//            goodsSpecDialog.setSelectSku(selectSku);
//            goodsSpecDialog.setOnSpecSubmitListener(new OnlyActGoodsSpecDialog.OnSpecSubmitListener() {
//                @Override
//                public void onSpecSubmit(GoodsSpecDetail goodsSpecDetail) {
//                    goodsSpecDetailNew =goodsSpecDetail;
//                    selectSku= AssembleDetailActivity.this.goodsSpecDetailNew.getGoodsSpecStr();
//                    buildNowMoney();
//                }
//            });
//        }else {//单规格
//            buildNowMoney();
//        }
//    }

//    private void buildNowMoney() {
//        if(goodsSpecDetailNew==null){
//            Toast.makeText(mContext,"拼团数据有误请联系商家",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        String singleNumber = new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)) + new Date().getTime();
//        GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(goodsSpecDetailNew.marketingId);
//        goodsMarketing.availableInventory = goodsSpecDetailNew.getAvailableInventory();
//        goodsMarketing.mapMarketingGoodsId = goodsSpecDetailNew.mapMarketingGoodsId;
//        goodsMarketing.marketingType = "2";
//        goodsMarketing.id = goodsSpecDetailNew.id;
//        goodsMarketing.marketingPrice = goodsSpecDetailNew.marketingPrice;
//        goodsMarketing.pointsPrice = goodsSpecDetailNew.pointsPrice;
//        goodsMarketing.salesMin = goodsDetail.getMarketingSalesMin();
//        goodsMarketing.salesMax = goodsDetail.getMarketingSalesMax();
//        GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsSpecDetailNew.platformPrice,
//                goodsSpecDetailNew.retailPrice,
//                goodsSpecDetailNew.getPlusPrice(),
//                goodsDetail.goodsType,
//                goodsSpecDetailNew.isPlusOnly,
//                "0", goodsSpecDetailNew.barcodeSku);
//        goodsBasketCell.goodsSpecDesc = goodsSpecDetailNew.goodsSpecStr;
//        goodsBasketCell.goodsStock = goodsSpecDetailNew.getRealAvailableInventory();
//        goodsBasketCell.goodsMarketingDTO = goodsMarketing;
//        goodsBasketCell.mchId = goodsDetail.userId + "";
//        goodsBasketCell.goodsId = goodsDetail.id;
//        goodsBasketCell.setGoodsSpecId(goodsSpecDetailNew.getGoodsChildId());
//        goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
//        goodsBasketCell.goodsImage = goodsDetail.headImage;
//        goodsBasketCell.setGoodsQuantity(Integer.parseInt(goodsSpecDetailNew.getCount()));
//        GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
//        List<GoodsBasketCell> list = new ArrayList<>();
//        goodsBasketCell.shopIdList = goodsDetail.getGoodsShopIdListStringArray();
//        goodsBasketCell.ischeck = true;
//        goodsBasketCell.goodsShopId = shopId;
//        list.add(goodsBasketCell);
//        ARouter.getInstance()
//                .build(ServiceRoutes.SERVICE_ORDER)
//                .withString("visitShopId", shopId)
//                .withString("assembleId", kkGroupDetail.assembleId)
//                .withString("teamNum", (TextUtils.isEmpty(teamNum) ? singleNumber : teamNum) + "")
//                .withString("assemblePrice", goodsSpecDetailNew.getMarketingPrice() + "")
//                .withObject("goodsbasketlist", list)
//                .withString("goodsMarketingType", goodsSpecDetailNew.marketingType)
//                .navigation();
//    }

    @Override
    public void getData() {
        super.getData();
        groupDetailPresenter.getGroupDetail(detailmap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isshare) {//判断是不是已经分享超过2人了这时候可以弹出额外的弹窗了
            if (kkGroupDetail != null && kkGroupDetail.teamMemberList != null) {
//                if(kkGroupDetail.teamMemberList.size()==1){
//                    KKGroupDialog.newInstance().setCanfirst(false).setResult(lessman).setOnDialogShareclickListener(new KKGroupDialog.DialogShareclickListener() {
//                        @Override
//                        public void onClick(SHARE_MEDIA share_media) {
//                            buildDes();
//                            shareMin(SHARE_MEDIA.WEIXIN, surl, sdes, stitle,spath);
//
//                        }
//                    }).show(getSupportFragmentManager(), "kkOkDialog");
//                }else {
//                    KKGroupDialog.newInstance().setResult(lessman).setOnDialogShareclickListener(new KKGroupDialog.DialogShareclickListener() {
//                        @Override
//                        public void onClick(SHARE_MEDIA share_media) {
//                            buildDes();
//                            shareMin(SHARE_MEDIA.WEIXIN, surl, sdes, stitle,spath);
//
//                        }
//                    }).show(getSupportFragmentManager(), "kkOkDialog");
//                }
                KKGroupDialog.newInstance().setResult(lessman).setOnDialogShareclickListener(new KKGroupDialog.DialogShareclickListener() {
                    @Override
                    public void onClick(SHARE_MEDIA share_media) {
                        buildDes();
//                        shareMin(SHARE_MEDIA.WEIXIN, surl, sdes, stitle, spath);
                        share(SHARE_MEDIA.WEIXIN,surl,sdes,stitle);

                    }
                }).show(getSupportFragmentManager(), "kkOkDialog");
                isshare = false;
            }


        }
    }
    int retryTime=0;
    @Override
    public void onSuccessGetGroupDetail(KKGroupDetail result) {
        if(result==null){
            showLoading();
            if(retryTime<3){
                retryTime++;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                },3000);
                return;
            }

        }
        if(result==null){
            return;
        }
        kkGroupDetail = result;
        if(!TextUtils.isEmpty(result.shopId)){
            shopId=result.shopId;
        }
//        groupDetailPresenter.getGoodsDetail(new SimpleHashMapBuilder<String, Object>().puts("shopId", 0 + "").puts("assembleId", kkGroupDetail.assembleId));
        if (kkGroupDetail.assembleStatus == 1) {//进行中
            if (kkGroupDetail.regimentStatus == 0) {//正常进行
                if (kkGroupDetail.joinStatus == 0) {//我是否参与
                    type = "参与别人分享的团";//我没参加
                } else {
                    type = "拼团支付成功";//我参加
                }
            }
            if (kkGroupDetail.regimentStatus == 1) {//库存不足
                if (kkGroupDetail.joinStatus == 0) {//我是否参与
                    type = "拼团结束";//我没参加
                } else {
                    type = "拼团失败";//我参加
                }
            }
            if (kkGroupDetail.regimentStatus == 2) {//活动结束
                if (kkGroupDetail.joinStatus == 0) {//我是否参与
                    type = "拼团结束";//我没参加
                } else {
                    type = "拼团失败";//我参加
                }
            }
            if (kkGroupDetail.regimentStatus == 3) {//成团时间到
                if (kkGroupDetail.joinStatus == 0) {//我是否参与
                    type = "拼团结束";//我没参加
                } else {
                    type = "拼团失败";//我参加
                }
            }
            if (kkGroupDetail.regimentStatus == 4) {//拼团成功
                if (kkGroupDetail.joinStatus == 0) {//我是否参与
                    type = "拼团结束";//我没参加
                } else {
                    type = "拼团成功";//我参加
                }
            }
        }
        if (kkGroupDetail.assembleStatus == 2) {//已经结束
            if (kkGroupDetail.regimentStatus == 0) {//正常进行
                if (kkGroupDetail.joinStatus == 0) {//我是否参与
                    type = "参与别人分享的团";//我没参加
                } else {
                    type = "拼团支付成功";//我参加
                }
            }
            if (kkGroupDetail.regimentStatus == 1) {//库存不足
                if (kkGroupDetail.joinStatus == 0) {//我是否参与
                    type = "拼团结束";//我没参加
                } else {
                    type = "拼团失败";//我参加
                }
            }
            if (kkGroupDetail.regimentStatus == 2) {//活动结束
                if (kkGroupDetail.joinStatus == 0) {//我是否参与
                    type = "拼团结束";//我没参加
                } else {
                    type = "拼团失败";//我参加
                }
            }
            if (kkGroupDetail.regimentStatus == 3) {//成团时间到
                if (kkGroupDetail.joinStatus == 0) {//我是否参与
                    type = "拼团结束";//我没参加
                } else {
                    type = "拼团失败";//我参加
                }
            }
            if (kkGroupDetail.regimentStatus == 4) {//拼团成功
                if (kkGroupDetail.joinStatus == 0) {//我是否参与
                    type = "拼团结束";//我没参加
                } else {
                    type = "拼团成功";//我参加
                }
            }
        }
        buildView();
    }
//    public GoodsSpecDetail goodsSpecDetail;
//    public GoodsSpecDetail goodsSpecDetailNew;
//    private void buildHasGoods() {
//        goodsSpecList=new ArrayList<>();
//        try {
//            goodsSpecList.addAll(new SimpleArrayListBuilder<GoodsDetail.GoodsChildren>().putList(goods2Model.assembleDTO.marketingGoodsChildDTOS, new ObjectIteraor<Goods2DetailPin.MarketingGoodsChildDTOS>() {
//                @Override
//                public GoodsDetail.GoodsChildren getDesObj(Goods2DetailPin.MarketingGoodsChildDTOS o) {
//                    return new GoodsDetail.GoodsChildren(o);
//                }
//            }));
//            if(goodsSpecList.size()>0){
//                selectSku=goodsSpecList.get(0).getGoodsSpecStr();
//            }
//            goodsSpecDetail = new GoodsSpecDetail(goodsDetail.getStorePrice(),
//                    goodsSpecList.get(0).platformPrice,
//                    goodsDetail.headImages.get(0).getSplash(),
//                    goodsDetail.goodsType,
//                    goodsDetail.marketingSalesMax,
//                    goodsDetail.marketingSalesMin,
//                    false,
//                    goodsDetail.isPlusOnly
//            );
//            goodsSpecDetail.goodsTitle=goods2Model.assembleDTO.goodsTitle;
//            goodsSpecDetail.marketingType="2";
//            goodsSpecDetailNew =new GoodsSpecDetail(goodsSpecList.get(0),"2").setFilePath(goodsSpecDetail.filePath)
//                    .setCount(1 + "");
//            goodsSpecDetailNew.setGoodsTitle(goodsSpecDetailNew.goodsTitle.replace(goodsSpecDetailNew.goodsSpecStr, ""));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void successGetGoodsDetail(Goods2DetailPin goods2DetailPin) {

//        this.goods2Model = goods2DetailPin;
//        if (goods2DetailPin != null) {
//            this.goodsDetail = goods2DetailPin.goodsDetails;
//        }
//        if (goodsDetail != null) {
//            buildHasGoods();
//        } else {
//
//        }

    }

    private void buildView() {
        personFrame.setVisibility(View.GONE);
//        goodsView.setVisibility(View.GONE);
        groupOk.setVisibility(View.GONE);
        groupGoodsTop.setVisibility(View.GONE);
        goodsTimeLL.setVisibility(View.GONE);
        groupTitleTip.setVisibility(View.GONE);
        groupGoodsName.setVisibility(View.GONE);
        groupStartTime.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        manList1.setVisibility(View.GONE);
        manList2.setVisibility(View.GONE);
        groupTitle.setVisibility(View.GONE);
        if (kkGroupDetail != null) {
            lessman = (kkGroupDetail.regimentSize - kkGroupDetail.teamMemberList.size()) + "";
            com.healthy.library.businessutil.GlideCopy.with(goodsIcon.getContext())
                    .load(kkGroupDetail.goodsImage)
                    .placeholder(R.drawable.img_avatar_default_q)
                    .error(R.drawable.img_avatar_default_q)

                    .into(goodsIcon);

            com.healthy.library.businessutil.GlideCopy.with(mContext).load(kkGroupDetail.goodsImage)
                    .placeholder(R.drawable.img_1_1_default)
                    .error(R.drawable.img_1_1_default)

                    .into(new SimpleTarget<Drawable>() {

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            sBitmap = DrawableUtils.drawableToBitmap(resource);
                        }
                    });
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) goodsIcon.getLayoutParams();
            if (kkGroupDetail.goodsType == 1) {
                layoutParams.height = (int) TransformUtil.dp2px(mContext, 90);
                goodsIcon.setLayoutParams(layoutParams);
            } else {
                layoutParams.height = (int) TransformUtil.dp2px(mContext, 100);
                goodsIcon.setLayoutParams(layoutParams);
            }
            goodsName.setText(kkGroupDetail.goodsTitle);
            goodsTip.setText("原价 ￥" + FormatUtils.moneyKeep2Decimals(kkGroupDetail.goodsPlatformPrice));
            goodsGroupPinMoney.setText("￥" + StringUtils.getResultPrice(kkGroupDetail.assemblePrice + ""));
            groupMoneyTip.setText("省" + StringUtils.getResultPrice((kkGroupDetail.goodsPlatformPrice - kkGroupDetail.assemblePrice) + "") + "元");
            goodsAddress.setText(kkGroupDetail.shopName);
            goodsAddressSmall.setText("(" + kkGroupDetail.addressDetails + ")");
            if ("拼团支付成功".equals(type)) {
                goodsView.setVisibility(View.VISIBLE);
                goodsTimeLL.setVisibility(View.VISIBLE);
//                groupGoodsTop.setVisibility(View.VISIBLE);
//                groupGoodsName.setVisibility(View.VISIBLE);
//                groupStartTime.setVisibility(View.VISIBLE);
                groupTitleTip.setVisibility(View.VISIBLE);
                groupTitleTip.setText(SpanUtils.getBuilder(mContext, "还差").setForegroundColor(Color.parseColor("#222222"))
                        .append(lessman + "").setForegroundColor(Color.parseColor("#F02846"))
                        .append("人，赶紧邀请好友来参团吧！").setForegroundColor(Color.parseColor("#222222"))
                        .create());

//                groupTitle.setText("支付成功！快去邀请好友来参团吧！");
                groupButton.setText("邀请好友");
                groupButton.setVisibility(View.VISIBLE);
                checkTimeOut(kkGroupDetail, true);
                buildManList(manList1, manList2, kkGroupDetail.teamMemberList, true);
//                groupGoodsName.setDes(kkGroupDetail.goodsTitle);
//                groupStartTime.setDes(kkGroupDetail.regimentTime);
                groupDetailPresenter.getGroupAlreadyList(new SimpleHashMapBuilder<String, Object>().puts("assembleId", kkGroupDetail.assembleId));


            }
            if ("拼团成功".equals(type)) {
                groupOk.setVisibility(View.VISIBLE);
                //backButton.setVisibility(View.VISIBLE);
                groupTitle.setVisibility(View.VISIBLE);
//                groupGoodsTop.setVisibility(View.VISIBLE);
//                groupGoodsName.setVisibility(View.VISIBLE);
//                groupStartTime.setVisibility(View.VISIBLE);
                groupTitleTip.setVisibility(View.VISIBLE);
                groupOk.setImageResource(R.drawable.group_ok2);
                groupTitle.setText("拼团成功");
                groupButton.setText("再开一团");
                groupButton.setVisibility(View.VISIBLE);
                //backButton.setText("返回拼团首页");
                groupTitleTip.setText("恭喜您！还可以再开一团哦～");
                checkTimeOut(kkGroupDetail, false);
                buildManList(manList1, manList2, kkGroupDetail.teamMemberList, true);
//                groupGoodsName.setDes(kkGroupDetail.goodsTitle);
//                groupStartTime.setDes(kkGroupDetail.regimentTime);
                groupDetailPresenter.getGroupAlreadyList(new SimpleHashMapBuilder<String, Object>().puts("assembleId", kkGroupDetail.assembleId));
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityManager.finishAllActivity();
                    }
                });
            }
            if ("拼团失败".equals(type)) {
                goodsView.setVisibility(View.VISIBLE);
                groupTitleTip.setVisibility(View.VISIBLE);
                groupOk.setVisibility(View.VISIBLE);
                groupOk.setImageResource(R.drawable.group_no);
                groupTitle.setVisibility(View.VISIBLE);
                groupTitle.setText("拼团失败");
                groupButton.setText("重新开团");
                groupButton.setVisibility(View.VISIBLE);
                groupTitleTip.setText("很遗憾！我们为您尽快退款～");
                checkTimeOut(kkGroupDetail, false);
                buildManList(manList1, manList2, kkGroupDetail.teamMemberList, true);
            }
            if ("拼团结束".equals(type)) {

                goodsView.setVisibility(View.VISIBLE);
                groupTitleTip.setVisibility(View.GONE);
                groupOk.setVisibility(View.VISIBLE);
                groupOk.setImageResource(R.drawable.group_no);
                groupTitle.setVisibility(View.VISIBLE);
                groupTitle.setText("该拼团已结束");
                groupButton.setText("我来开个团");
                groupButton.setVisibility(View.VISIBLE);
                checkTimeOut(kkGroupDetail, false);
                buildManList(manList1, manList2, kkGroupDetail.teamMemberList, true);
            }
            if ("参与别人分享的团".equals(type)) {
                goodsView.setVisibility(View.VISIBLE);
                goodsTimeLL.setVisibility(View.VISIBLE);
                groupTitle.setText(SpanUtils.getBuilder(mContext, "还差").setForegroundColor(Color.parseColor("#666666"))
                        .append((kkGroupDetail.regimentSize - kkGroupDetail.teamMemberList.size()) + "").setForegroundColor(Color.parseColor("#FC2347"))
                        .append("人拼团成功！").setForegroundColor(Color.parseColor("#666666"))
                        .create());
                groupButton.setText("参与“" + kkGroupDetail.getWhomaster() + "”的拼团");
                groupButton.setVisibility(View.VISIBLE);
                checkTimeOut(kkGroupDetail, true);
                buildManList(manList1, manList2, kkGroupDetail.teamMemberList, true);
            }
        }


    }

    /**
     * 分享
     *
     * @param shareMedia 分享平台
     * @param url        链接地址
     * @param des        描述
     * @param title      标题
     */
    private void share(SHARE_MEDIA shareMedia, String url, String des, String title) {
        isshare = true;
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

    /**
     * 分享
     *
     * @param shareMedia 分享平台
     * @param url        链接地址
     * @param des        描述
     * @param title      标题
     */
    private void shareMin(SHARE_MEDIA shareMedia, String url, String des, String title, String path) {
        isshare = true;
        //System.out.println(url);
        UMMin umMin = new UMMin(url);
        umMin.setTitle(title);
        umMin.setThumb(new UMImage(mContext, sBitmap));
        umMin.setDescription(des);
        umMin.setPath(path);
        umMin.setUserName("gh_f9b4fbd9d3b8");
        if (ChannelUtil.isRealRelease()) {

        } else {
            com.umeng.socialize.Config.setMiniPreView();
        }
//        com.umeng.socialize.Config.setMiniTest();
        new ShareAction(this)
                .withMedia(umMin)
                .setPlatform(shareMedia)
                .setCallback(this)
                .share();
    }

    private void buildManList(LinearLayout manList1, LinearLayout manList2, List<KKGroupDetail.TeamMember> teamMemberList, boolean b) {
        manList1.setVisibility(View.VISIBLE);
        manList2.setVisibility(View.VISIBLE);
        manList1.removeAllViews();
        manList2.removeAllViews();
        if (b) {
            if (teamMemberList != null && teamMemberList.size() > 0) {
                List<KKGroupDetail.TeamMember> desList = new ArrayList<>();
                for (int i = 0; i < kkGroupDetail.regimentSize; i++) {
                    if (i > teamMemberList.size() - 1) {
                        desList.add(null);
                    } else {
                        desList.add(teamMemberList.get(i));
                    }
                }//重新填充一个数组
                if (kkGroupDetail.regimentSize == 2) {
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                }
                if (kkGroupDetail.regimentSize == 3) {
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                    manList1.addView(buildManListChild(desList.get(2)));
                }
                if (kkGroupDetail.regimentSize == 4) {
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                    manList1.addView(buildManListChild(desList.get(2)));
                    manList1.addView(buildManListChild(desList.get(3)));
                }
                if (kkGroupDetail.regimentSize == 5) {
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                    manList1.addView(buildManListChild(desList.get(2)));
                    manList1.addView(buildManListChild(desList.get(3)));
                    manList1.addView(buildManListChild(desList.get(4)));
                }
                if (kkGroupDetail.regimentSize == 6) {
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                    manList1.addView(buildManListChild(desList.get(2)));
                    manList1.addView(buildManListChild(desList.get(3)));
                    manList2.addView(buildManListChild(desList.get(4)));
                    manList2.addView(buildManListChild(desList.get(5)));
                }
                if (kkGroupDetail.regimentSize == 7) {
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                    manList1.addView(buildManListChild(desList.get(2)));
                    manList1.addView(buildManListChild(desList.get(3)));
                    manList2.addView(buildManListChild(desList.get(4)));
                    manList2.addView(buildManListChild(desList.get(5)));
                    manList2.addView(buildManListChild(desList.get(6)));
                }
                if (kkGroupDetail.regimentSize == 8) {
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                    manList1.addView(buildManListChild(desList.get(2)));
                    manList1.addView(buildManListChild(desList.get(3)));
                    manList1.addView(buildManListChild(desList.get(4)));

                    manList2.addView(buildManListChild(desList.get(5)));
                    manList2.addView(buildManListChild(desList.get(6)));
                    manList2.addView(buildManListChild(desList.get(7)));

                }
                if (kkGroupDetail.regimentSize == 9) {
                    manList1.addView(buildManListChild(desList.get(0)));
                    manList1.addView(buildManListChild(desList.get(1)));
                    manList1.addView(buildManListChild(desList.get(2)));
                    manList1.addView(buildManListChild(desList.get(3)));
                    manList1.addView(buildManListChild(desList.get(4)));

                    manList2.addView(buildManListChild(desList.get(5)));
                    manList2.addView(buildManListChild(desList.get(6)));
                    manList2.addView(buildManListChild(desList.get(7)));
                    manList2.addView(buildManListChild(desList.get(8)));

                }


            } else {
                manList1.setVisibility(View.GONE);
                manList2.setVisibility(View.GONE);
            }
        }
    }

    public View buildManListChild(KKGroupDetail.TeamMember teamMember) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dis_item_group_detail_icon, null);
        ImageView groupIconMaster = view.findViewById(R.id.groupIconMaster);
        TextView groupTipMaster = view.findViewById(R.id.groupTipMaster);
        groupTipMaster.setVisibility(View.INVISIBLE);
        if (teamMember == null) {
            com.healthy.library.businessutil.GlideCopy.with(groupIconMaster.getContext())
                    .load(R.drawable.img_avatar_default_q)
                    .placeholder(R.drawable.img_avatar_default_q)
                    .error(R.drawable.img_avatar_default_q)

                    .into(groupIconMaster);
        } else {
            com.healthy.library.businessutil.GlideCopy.with(groupIconMaster.getContext())
                    .load(teamMember.memberFaceUrl)
                    .placeholder(R.drawable.img_avatar_default_q)
                    .error(R.drawable.img_avatar_default_q)

                    .into(groupIconMaster);
            if (teamMember.commanderStatus == 1) {
                groupTipMaster.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }


    private void checkTimeOut(KKGroupDetail url, boolean needtimer) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        kickFinish.setText("后拼团结束");
        goodsTimeLLS.setVisibility(View.VISIBLE);
        if (needtimer) {
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.regimentTime);
                endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.endTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long nd = 1000 * url.regimentTimeLength * 60 * 60;//加入时间之后需要多少小时
            long desorg = startTime.getTime() + nd;
            long timer = startTime.getTime() + nd;
            if (endTime != null && endTime.getTime() <= timer) {
                timer = endTime.getTime();
                desorg = endTime.getTime();
            }
            timer = timer - System.currentTimeMillis();

            if (timer > 0) {
                //System.out.println("开始计时");
                final long finalTimer = timer;
                final long finalDesorg = desorg;
                countDownTimer = new CountDownTimer(finalTimer, 1000) {
                    public void onTick(long millisUntilFinished) {
                        String[] array = DateUtils.getDistanceTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
                        kickHour.setText(array[1] + "");
                        kickMin.setText(array[2] + "");
                        kickSec.setText(array[3] + "");
                    }

                    public void onFinish() {
                        kickFinish.setText("过期");
                        goodsTimeLLS.setVisibility(View.GONE);
                        getData();
                    }
                }.start();
            } else {
                kickFinish.setText("过期");
                goodsTimeLLS.setVisibility(View.GONE);
            }
        }

    }

    List<KKGroupSimple> kkGroupSimples = new ArrayList<>();

    @Override
    public void onSuccessGetGroupAlreadyList(List<KKGroupSimple> kkGroupSimples) {
        if (this.kkGroupSimples.size() > 0) {

        } else {
            this.kkGroupSimples.addAll(kkGroupSimples);
            runShowEveryMan();
        }
    }

    private void runShowEveryMan() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < kkGroupSimples.size(); i++) {
                    final KKGroupSimple item = kkGroupSimples.get(i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing()) {
                                personFrame.setVisibility(View.VISIBLE);
                                personNameText.setText(item.memberNickName);
                                personText.setText(item.regimentStatusStr);
                                com.healthy.library.businessutil.GlideCopy.with(personIcon.getContext())
                                        .load(item.memberFaceUrl)
                                        .placeholder(R.drawable.img_1_1_default)
                                        .error(R.drawable.img_1_1_default)

                                        .into(personIcon);
                            }

                        }
                    });
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing()) {

                                personFrame.setVisibility(View.GONE);
                            }
                        }
                    });
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            kkGroupSimples.clear();
                            groupDetailPresenter.getGroupAlreadyList(new SimpleHashMapBuilder<String, Object>().puts("assembleId", kkGroupDetail.assembleId + ""));
                        }

                    }
                });


            }
        }).start();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        groupFrame = (LinearLayout) findViewById(R.id.group_frame);
        goodsView = (ConstraintLayout) findViewById(R.id.goodsView);
        goodsIcon = (CornerImageView) findViewById(R.id.goodsIcon);
        goodsName = (TextView) findViewById(R.id.goodsName);
        goodsPrice = (TextView) findViewById(R.id.goodsPrice);
        goodsGroupPin = (TextView) findViewById(R.id.goodsGroupPin);
        goodsGroupPinMoney = (TextView) findViewById(R.id.goodsGroupPinMoney);
        helpIconRightLL = (LinearLayout) findViewById(R.id.helpIconRightLL);
        helpIconLeft = (ImageView) findViewById(R.id.helpIconLeft);
        groupMoneyTip = (TextView) findViewById(R.id.groupMoneyTip);
        goodsAddress = (TextView) findViewById(R.id.goodsAddress);
        goodsTimeLL = (LinearLayout) findViewById(R.id.goodsTimeLL);
        kickFinish = (TextView) findViewById(R.id.kickFinish);
        kickHour = (TextView) findViewById(R.id.kickHour);
        kickMin = (TextView) findViewById(R.id.kickMin);
        kickSec = (TextView) findViewById(R.id.kickSec);
        personFrame = (LinearLayout) findViewById(R.id.personFrame);
        personIcon = (CornerImageView) findViewById(R.id.personIcon);
        personText = (TextView) findViewById(R.id.personText);
        goodsTip = (TextView) findViewById(R.id.goodsTip);
        goodsAddressSmall = (TextView) findViewById(R.id.goodsAddressSmall);
        groupOk = (ImageView) findViewById(R.id.groupOk);
        groupTitle = (TextView) findViewById(R.id.groupTitle);
        groupTitleTip = (TextView) findViewById(R.id.groupTitleTip);
        groupButton = (TextView) findViewById(R.id.groupButton);
        groupGoodsName = (SectionView) findViewById(R.id.groupGoodsName);
        groupStartTime = (SectionView) findViewById(R.id.groupStartTime);
        groupGoodsTop = (View) findViewById(R.id.groupGoodsTop);
        goodsTimeLLS = (LinearLayout) findViewById(R.id.goodsTimeLLS);
        manList1 = (LinearLayout) findViewById(R.id.manList1);
        manList2 = (LinearLayout) findViewById(R.id.manList2);
        personNameText = (TextView) findViewById(R.id.personNameText);
        groupTT = (LinearLayout) findViewById(R.id.groupTT);
        backButton = (TextView) findViewById(R.id.backButton);

        goodsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kkGroupDetail != null) {
                    ARouter.getInstance()
                            .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                            .withString("shopId", kkGroupDetail.shopId + "")
                            .withString("assembleId", kkGroupDetail.assembleId)
                            .navigation();
                }
            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation.getErrorCode() == 0) {
            LocUtil.storeLocation(mContext, aMapLocation);
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
                        .setMessageTopRes(R.drawable.dialog_message_icon_loc, "开启定位", "为给您提供更好的本地化服务，请您到系统设置中打开GPS定位")
                        .setMessageOkClickListener(new MessageDialog.MessageOkClickListener() {
                            @Override
                            public void onMessageOkClick(View view) {
                                IntentUtils.toLocationSetting(mContext);
                            }
                        })
                        .show(getSupportFragmentManager(), "打开定位");

            }
        }
    }

    private void successLocation() {
        latitude = LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE);
        longitude = LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE);
        areaNo = LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE);
        cityNo = LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE);
        locCityName = LocUtil.getCityNameOld(mContext, SpKey.LOC_ORG);
        newCityName = LocUtil.getCityNameOld(mContext, SpKey.LOC_CHOSE);
    }
}
