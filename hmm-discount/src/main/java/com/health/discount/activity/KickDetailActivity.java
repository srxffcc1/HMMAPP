package com.health.discount.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import com.healthy.library.adapter.KickManListAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.GoodsSpecDialog;
import com.healthy.library.business.KKDialog;
import com.healthy.library.business.MessageDialog;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Constants;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.contract.KickDetailContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnCustomRetryListener;
import com.healthy.library.model.Goods2DetailKick;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketGroup;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.GoodsSpecCell;
import com.healthy.library.model.GoodsSpecDetail;
import com.healthy.library.model.Kick;
import com.healthy.library.model.KickResult;
import com.healthy.library.model.KickResultFail;
import com.healthy.library.model.KickUser;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.presenter.KickDetailPresenter;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.ResUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.utils.TransformUtil;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 砍价详情
 */
@Route(path = DiscountRoutes.DIS_KICKDETAIL)
public class KickDetailActivity extends BaseActivity implements IsFitsSystemWindows, OnRefreshLoadMoreListener, KickDetailContract.View, AMapLocationListener {
    @Autowired
    String bargainId;
    @Autowired
    String bargainMemberId;
    @Autowired
    String marketingGoodsChildId;
    @Autowired
    String marketingShopId;//访问门店
    @Autowired
    String deliveryShopId;//可能为异业门店


    private TopBar topBar;
    private SmartRefreshLayout layoutRefresh;
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    private KickDetailPresenter kickDetailPresenter;
    private KickManListAdapter kickManListAdapter;
    private Map<String, Object> detailmap = new HashMap<>();
    private Map<String, Object> manmap = new HashMap<>();
    private boolean isheadadd = false;
    private int currentPage = 1;
    private int total = 0;
    private Kick mkick;
    CountDownTimer countDownTimer;
    private AlertDialog mShareDialog;
    String surl;
    String sdes;
    String stitle;
    String spath;
    boolean isshare = false;
    private Bitmap sBitmap;


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

    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;


    private Map<String, SHARE_MEDIA> mPlatformMap = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.dis_activity_disactdetail2;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        if ("null".equals(bargainMemberId)) {
            bargainMemberId = null;
        }
        if (TextUtils.isEmpty(marketingShopId)) {
            marketingShopId = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP);
        }
        if (TextUtils.isEmpty(deliveryShopId)) {
            deliveryShopId = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP);
        }
        kickDetailPresenter = new KickDetailPresenter(mContext, this);
        kickManListAdapter = new KickManListAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        kickManListAdapter.bindToRecyclerView(recycler);
        detailmap.put("bargainId", bargainId);
        detailmap.put("type", "0");
        detailmap.put("marketingGoodsChildId", marketingGoodsChildId);
        detailmap.put("marketingShopId", marketingShopId);
        detailmap.put("deliveryShopId", deliveryShopId);

        manmap.put("currentPage", currentPage + "");
        manmap.put("pageSize", 10 + "");
        manmap.put("bargainId", bargainId + "");
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        locateCheck();

        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_wx), SHARE_MEDIA.WEIXIN);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_circle), SHARE_MEDIA.WEIXIN_CIRCLE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qq), SHARE_MEDIA.QQ);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qzone), SHARE_MEDIA.QZONE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_sina), SHARE_MEDIA.SINA);
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
    protected void onResume() {
        super.onResume();
        getData();
        if (kickList != null && kickList.size() >= 2) {
            if (!isFinishing()) {
//                KKDialog.newInstance().setRedOnclickListener(new KKDialog.DialogKickShareclickListener() {
//                    @Override
//                    public void onClick(SHARE_MEDIA share_media) {
//                        buildDes();
//                        shareMin(SHARE_MEDIA.WEIXIN, surl, sdes, stitle,spath);
////                    share(SHARE_MEDIA.WEIXIN, surl, sdes, stitle);
//                    }
//                }).setResult(mkick.discountMoney+"").show(getSupportFragmentManager(),"kkOkDialog");
                isshare = false;
            }
        }
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

    List<KickUser> kickList;

    @Override
    public void onSuccessGetKickManList(List<KickUser> kickList, PageInfoEarly pageInfoEarly) {
        this.kickList = kickList;
        if (mkick != null && mkick.joinStatus == 1 && total == 1 && isshare && mkick.bargainInventory > 0) {//说明参加过活动 且现在人数为1人 且现在是分享状态 且库存还有
            //System.out.println("自砍情况1");
            Map<String, Object> kickmap = new HashMap<>();
            kickmap.put("bargainId", bargainId);
            kickmap.put("bargainType", "0");
            kickmap.put("bargainMemberId", bargainMemberId);
            kickDetailPresenter.kickHelp(kickmap);
        }
        if (pageInfoEarly == null) {
            if (currentPage == 1) {
                layoutRefresh.setEnableLoadMore(false);
            } else {
                layoutRefresh.setEnableLoadMore(false);
                layoutRefresh.finishLoadMoreWithNoMoreData();
            }

            return;
        }
        total = pageInfoEarly.totalNum;
        currentPage = pageInfoEarly.currentPage;
        if (mkick != null && mkick.joinStatus == 1 && total == 2) {//说明参加过活动 且现在人数为2人可能是之前状态退出了
            //System.out.println("自砍情况2");
            Map<String, Object> kickmap = new HashMap<>();
            kickmap.put("bargainId", bargainId);
            kickmap.put("bargainType", "0");
            kickmap.put("bargainMemberId", bargainMemberId);
            kickDetailPresenter.kickHelp(kickmap);
        }
        if (currentPage == 1 || currentPage == 0) {
            isheadadd = false;
            if (kickList == null || kickList.size() == 0) {
                kickList = new ArrayList<>();
                kickList.add(null);
            }
        }
        if (!isheadadd) {
            if (mkick != null) {
                addHeaderView();
                isheadadd = true;
            }
            kickManListAdapter.setNewData(kickList);
        } else {
            if (currentPage == 1 || currentPage == 0) {
                kickManListAdapter.setNewData(kickList);
            } else {
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
        //System.out.println("加头");
        View headerView = LayoutInflater.from(mContext)
                .inflate(R.layout.dis_item_activity_disact_go_top2, null);
        ConstraintLayout kickLL;
        ImageView goodsIcon;
        ImageView double_marks_left;
        ImageView double_marks_right;
        TextView goodsName;
        TextView goodsPrice;
        TextView goodsJoinNum;
        TextView limitMoney;
        TextView alreadyMoney;
        ProgressBar goodsPro;
        TextView shareText;
        final TextView buttomYellow;
        TextView buttomRed;
//         final TextView limitTime;
        LinearLayout manLL;
        TextView helpNum;
        ImageView userIcon = headerView.findViewById(R.id.userIcon);
        TextView userName = headerView.findViewById(R.id.userName);

        android.widget.LinearLayout goodsTimeLL;
        android.widget.TextView kickFinish;
        android.widget.TextView kickHour;
        android.widget.TextView kickMin;
        android.widget.TextView kickSec;


        kickLL = (ConstraintLayout) headerView.findViewById(R.id.kickLL);
        goodsIcon = (ImageView) headerView.findViewById(R.id.goodsIcon);
        double_marks_left = (ImageView) headerView.findViewById(R.id.double_marks_left);
        double_marks_right = (ImageView) headerView.findViewById(R.id.double_marks_right);
        goodsName = (TextView) headerView.findViewById(R.id.goodsName);
        goodsPrice = (TextView) headerView.findViewById(R.id.goodsPrice);
        goodsJoinNum = (TextView) headerView.findViewById(R.id.goodsJoinNum);
        limitMoney = (TextView) headerView.findViewById(R.id.limitMoney);
        alreadyMoney = (TextView) headerView.findViewById(R.id.alreadyMoney);
        goodsPro = (ProgressBar) headerView.findViewById(R.id.goodsPro);
        shareText = (TextView) headerView.findViewById(R.id.shareText);
        buttomYellow = (TextView) headerView.findViewById(R.id.buttomYellow);
        buttomRed = (TextView) headerView.findViewById(R.id.buttomRed);


        goodsTimeLL = (LinearLayout) headerView.findViewById(R.id.goodsTimeLL);
        kickFinish = (TextView) headerView.findViewById(R.id.kickFinish);
        kickHour = (TextView) headerView.findViewById(R.id.kickHour);
        kickMin = (TextView) headerView.findViewById(R.id.kickMin);
        kickSec = (TextView) headerView.findViewById(R.id.kickSec);
//        mkick.discountMoney=200;
        goodsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ARouter.getInstance()
                        .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                        .withString("shopId", mkick.getMarketingShopId())
                        .withString("bargainId", bargainId)
                        .withString("bargainMemberId", bargainMemberId)
                        .navigation();




            }
        });

        manLL = (LinearLayout) headerView.findViewById(R.id.manLL);
        helpNum = (TextView) headerView.findViewById(R.id.helpNum);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) goodsIcon.getLayoutParams();
        if (mkick.goodsType == 1) {
            layoutParams.height = (int) TransformUtil.dp2px(mContext, 75);
            goodsIcon.setLayoutParams(layoutParams);
        } else {
            layoutParams.height = (int) TransformUtil.dp2px(mContext, 100);
            goodsIcon.setLayoutParams(layoutParams);
        }
        com.healthy.library.businessutil.GlideCopy.with(goodsIcon.getContext())
                .asBitmap()
                .load(mkick.goodsImage)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(goodsIcon);

        try {
            userName.setText(SpUtils.getValue(mContext, SpKey.USER_NICK));
            com.healthy.library.businessutil.GlideCopy.with(userIcon.getContext())
                    .asBitmap()
                    .load(SpUtils.getValue(mContext, SpKey.USER_ICON))
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(userIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mkick.goodsType == 2) {

//            kickDetailPresenter.getGoodsDetail(new SimpleHashMapBuilder<String, Object>().puts("bargainId", bargainId));
        }
        com.healthy.library.businessutil.GlideCopy.with(mContext).load(mkick.goodsImage)
                .placeholder(R.drawable.img_1_1_default)
                .error(R.drawable.img_1_1_default)

                .into(new SimpleTarget<Drawable>() {

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        sBitmap = DrawableUtils.drawableToBitmap(resource);
                    }
                });
        if (mkick.goodsType == 1) {
            goodsName.setMaxLines(1);
        } else {
            goodsName.setMaxLines(2);
        }
        goodsName.setText(mkick.goodsTitle);
        goodsPrice.setText(String.format(Locale.CHINA, "¥ %s",
                FormatUtils.moneyKeep2Decimals(mkick.goodsPlatformPrice)));
        limitMoney.setText(String.format(Locale.CHINA, "%s",
                FormatUtils.moneyKeep2Decimals(mkick.floorPrice)));
        int bili = (int) (mkick.discountMoney * 1.0 / (mkick.goodsPlatformPrice - mkick.floorPrice) * 1000);
        if (bili > 1000) {
            bili = 1000;
        }
        final double less = new BigDecimal(mkick.goodsPlatformPrice + "").subtract(new BigDecimal(mkick.floorPrice + "")).subtract(new BigDecimal(mkick.discountMoney + "")).doubleValue();
        SpannableStringBuilder alreadyMoneyBuilder;


        if (less / (mkick.goodsPlatformPrice - mkick.floorPrice) <= 0.1) {
            alreadyMoneyBuilder = SpanUtils.getBuilder(mContext, "距离目标仅剩").setForegroundColor(Color.parseColor("#ff222222"))
                    .append(new java.text.DecimalFormat("0.00").format(less)).setForegroundColor(Color.parseColor("#F02846"))
                    .append("元了,加油！").setForegroundColor(Color.parseColor("#ff222222"))
                    .create();
        } else {
            alreadyMoneyBuilder = SpanUtils.getBuilder(mContext, "已砍").setForegroundColor(Color.parseColor("#ff222222"))
                    .append(new java.text.DecimalFormat("0.00").format(mkick.discountMoney)).setForegroundColor(Color.parseColor("#F02846"))
                    .append("元，还差").setForegroundColor(Color.parseColor("#ff222222"))
                    .append(new java.text.DecimalFormat("0.00").format(less)).setForegroundColor(Color.parseColor("#F02846"))
                    .append("元").setForegroundColor(Color.parseColor("#ff222222"))
                    .create();
        }


        shareText.setText(alreadyMoneyBuilder);
//        helpNum.setText("已有"+total+"位好友助力");
        goodsJoinNum.setText("仅剩" + mkick.bargainInventory + "件");
//        goodsPro.setProgress(bili);

        TextView goodsProFFL = headerView.findViewById(R.id.goodsProFFL);
        TextView goodsProFFR = headerView.findViewById(R.id.goodsProFFR);


        TextView goodsProFFLU = headerView.findViewById(R.id.goodsProFFLU);
        TextView goodsProFFRU = headerView.findViewById(R.id.goodsProFFRU);


        LinearLayout goodsProLetL = headerView.findViewById(R.id.goodsProLetL);
        LinearLayout goodsProLetR = headerView.findViewById(R.id.goodsProLetR);


        LinearLayout.LayoutParams layoutParamsl = (LinearLayout.LayoutParams) goodsProFFL.getLayoutParams();
        LinearLayout.LayoutParams layoutParamsr = (LinearLayout.LayoutParams) goodsProFFR.getLayoutParams();

        //System.out.println("砍价问题:"+less);

        int goodsProFFWith = (int) (ScreenUtils.getScreenWidth(mContext) - TransformUtil.dp2px(mContext, 25));


        int ld = (int) (goodsProFFWith * (bili * 1.0 / 1000) - TransformUtil.dp2px(mContext, 10));
        int rd = (int) (goodsProFFWith * ((1000 - bili) * 1.0 / 1000) - TransformUtil.dp2px(mContext, 10));
        layoutParamsl.weight = ld;
        layoutParamsr.weight = less <= 0 ? 0 : rd;
        goodsProFFL.setLayoutParams(layoutParamsl);
        goodsProFFR.setLayoutParams(layoutParamsr);


        LinearLayout.LayoutParams layoutParamslU = (LinearLayout.LayoutParams) goodsProFFLU.getLayoutParams();
        LinearLayout.LayoutParams layoutParamsrU = (LinearLayout.LayoutParams) goodsProFFRU.getLayoutParams();

        layoutParamslU.weight = bili;
        layoutParamsrU.weight = 1000 - bili;


        goodsProFFLU.setLayoutParams(layoutParamslU);
        goodsProFFRU.setLayoutParams(layoutParamsrU);


        LinearLayout.LayoutParams layoutParamslT = (LinearLayout.LayoutParams) goodsProLetL.getLayoutParams();
        LinearLayout.LayoutParams layoutParamsrT = (LinearLayout.LayoutParams) goodsProLetR.getLayoutParams();

        layoutParamslT.weight = bili;
        layoutParamsrT.weight = 1000 - bili;
        if (less <= 0) {
            int ldt = (int) (goodsProFFWith - TransformUtil.dp2px(mContext, 10));
            int rdt = (int) (TransformUtil.dp2px(mContext, 10));
            layoutParamslT.weight = ldt;
            layoutParamsrT.weight = rdt;
        }


        goodsProLetL.setLayoutParams(layoutParamslT);
        goodsProLetR.setLayoutParams(layoutParamsrT);

        goodsProLetL.removeAllViews();
        goodsProLetR.removeAllViews();
        if (bili > 800) {
            View leftTip = LayoutInflater.from(mContext).inflate(R.layout.dis_item_activity_disact_let_l, goodsProLetL, false);
            TextView nowMoney = leftTip.findViewById(R.id.nowMoney);
            nowMoney.setText("￥" + FormatUtils.moneyKeep2Decimals(mkick.goodsPlatformPrice - mkick.discountMoney));
            goodsProLetL.addView(leftTip);
        } else {
            View rightTip = LayoutInflater.from(mContext).inflate(R.layout.dis_item_activity_disact_let_r, goodsProLetR, false);
            TextView nowMoney = rightTip.findViewById(R.id.nowMoney);
            nowMoney.setText("￥" + FormatUtils.moneyKeep2Decimals(mkick.goodsPlatformPrice - mkick.discountMoney));
            goodsProLetR.addView(rightTip);
        }


        goodsTimeLL.setVisibility(View.VISIBLE);
        buttomRed.setVisibility(View.VISIBLE);

        alreadyMoney.setText("我发现一件好货，快来帮我砍一刀");
        double_marks_left.setVisibility(View.VISIBLE);
        double_marks_right.setVisibility(View.VISIBLE);
//        buttomRed.setText("实在砍不动了，就这个价吧");
        if (less <= 0) {//说明砍到最低价了
            alreadyMoney.setText("恭喜您！终于砍到最低价了～");
            shareText.setText("数量有限，立即下单");
            buttomYellow.setText("立即购买");
            goodsTimeLL.setVisibility(View.GONE);
            double_marks_left.setVisibility(View.GONE);
            double_marks_right.setVisibility(View.GONE);
        }
        if (mkick.bargainInventory < 1) {
            alreadyMoney.setText("非常抱歉！已经抢光了，砍价失败~");
            buttomYellow.setText("更多砍价");
            buttomRed.setVisibility(View.GONE);
            double_marks_left.setVisibility(View.GONE);
            double_marks_right.setVisibility(View.GONE);
        }
        if (mkick.bargainStatus == 3) {
            alreadyMoney.setText("恭喜你，心仪好物已到手~");
            shareText.setText("已成功下单，去看更多砍价活动");
            buttomYellow.setText("更多砍价");
            buttomRed.setVisibility(View.GONE);
            goodsTimeLL.setVisibility(View.GONE);
            if (mkick.bargainInventory < 1) {
                alreadyMoney.setText("非常抱歉！已经抢光了，砍价失败~");
                buttomYellow.setText("更多砍价");
            }
            double_marks_left.setVisibility(View.GONE);
            double_marks_right.setVisibility(View.GONE);
        } else if (mkick.bargainStatus == 4) {
            alreadyMoney.setText("恭喜你，心仪好物已到手~");
            buttomYellow.setText("更多砍价");
            buttomRed.setVisibility(View.GONE);
            goodsTimeLL.setVisibility(View.GONE);
            shareText.setText("已成功下单，去看更多砍价活动");
            double_marks_left.setVisibility(View.GONE);
            double_marks_right.setVisibility(View.GONE);
        }
        if (total < 3 || less <= 0) {//说明砍的人还没到3人
            buttomRed.setVisibility(View.GONE);
        } else {

        }

        if (total >= 1) {
            checkTimeOut(mkick, kickHour, kickMin, kickSec);
        } else {
            goodsTimeLL.setVisibility(View.GONE);
        }
        if (mkick.bargainStatus == 2) {
            alreadyMoney.setText("非常抱歉！时间到了，砍价失败~");
            buttomYellow.setText("更多砍价");
            buttomRed.setVisibility(View.GONE);
            double_marks_left.setVisibility(View.GONE);
            double_marks_right.setVisibility(View.GONE);
        }

        buttomRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mkick.bargainStatus == 2) {
                    Toast.makeText(mContext, "活动过期", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mkick.bargainInventory < 1) {
                    Toast.makeText(mContext, "库存不足", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mkick.bargainStatus == 3) {
                    ARouter.getInstance().build(ServiceRoutes.SERVICE_USERORDER)
                            .withString("type", Constants.ORDER_TYPE_ALL)
                            .navigation();

                    finish();
                } else {
                    goOrder();
                }


            }
        });
        buttomYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("更多砍价".equals(buttomYellow.getText().toString())) {
                    ARouter.getInstance()
                            .build(DiscountRoutes.DIS_NEWKICKLIST)
                            .navigation();
                    finish();
                } else {
                    if (mkick.bargainStatus == 2) {
                        Toast.makeText(mContext, "活动过期", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mkick.bargainInventory < 1) {
                        Toast.makeText(mContext, "库存不足", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (less <= 0) {
                        if (mkick.bargainStatus == 3) {
                            ARouter.getInstance().build(ServiceRoutes.SERVICE_USERORDER)
                                    .withString("type", Constants.ORDER_TYPE_ALL)
                                    .navigation();
                            finish();
                        } else {
                            goOrder();
                        }

                    } else {
//                    surl = "http://www.baidu.com";
                        buildDes();
//                        shareMin(SHARE_MEDIA.WEIXIN, surl, sdes, stitle,spath);
                        share(SHARE_MEDIA.WEIXIN, surl, sdes, stitle);
//                    stitle = "真的很棒，我就快成功啦";
//                        showShareBottomDialog2();
                    }
                }


            }
        });
        kickManListAdapter.addHeaderView(headerView);
        kickManListAdapter.removeAllFooterView();
        kickManListAdapter.addFooterView(LayoutInflater.from(mContext).inflate(R.layout.dis_item_activity_disact_single_bottom, null));
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
        mkick = kickDetail;
//        if(mkick!=null&&mkick.joinStatus==0){//发现用户没参加则自动参加本次活动
//
//        }
        kickDetailPresenter.getKickManList(manmap);
    }

    @Override
    public void onSuccessKick(KickResult result) {
        currentPage = 1;
        manmap.put("currentPage", currentPage + "");
        bargainMemberId = result.bargainMemberId;
        manmap.put("bargainMemberId", bargainMemberId);
        detailmap.put("bargainMemberId", result.bargainMemberId);
        kickDetailPresenter.getKickDetail(detailmap);
        //并且弹出成功的dialog
        KKDialog.newInstance().setRedOnclickListener(new KKDialog.DialogKickShareclickListener() {
            @Override
            public void onClick(SHARE_MEDIA share_media) {
                if (mkick != null && mkick.bargainInventory < 1) {//说明这一刀砍完库存就不足了
                    Toast.makeText(mContext, "库存不足", Toast.LENGTH_SHORT).show();
                } else {
                    if(mkick==null){
                        Toast.makeText(mContext,"数据异常稍后再试",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    buildDes();
//                    shareMin(SHARE_MEDIA.WEIXIN, surl, sdes, stitle,spath);
                    share(SHARE_MEDIA.WEIXIN, surl, sdes, stitle);
                }

//                share(SHARE_MEDIA.WEIXIN, surl, sdes, stitle);
            }
        }).setResult(result.discountMoney + "").show(getSupportFragmentManager(), "kkOkDialog");
    }

    @Override
    public void onFailKick(String msg) {
        try {
//            if(msg.contains("正在发起")){
//                ARouter.getInstance().build(DiscountRoutes.DIS_MYKICKACTIVITY).navigation();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },2000);
    }

    @Override
    public void onFailKick(String result, KickResultFail kickResultFail) {
        bargainMemberId = kickResultFail.id;
        marketingShopId=kickResultFail.marketingShopId;
        deliveryShopId=kickResultFail.deliveryShopId;
        manmap.put("currentPage", currentPage + "");
        manmap.put("bargainMemberId", kickResultFail.id);
        detailmap.put("bargainMemberId", bargainMemberId);
        kickDetailPresenter.getKickDetail(detailmap);
    }

    @Override
    public void onSuccessKickHelp(KickResult result) {//自砍第二刀
        kickDetailPresenter.getKickDetail(detailmap);
        if (!isFinishing()) {
            KKDialog.newInstance().setRedOnclickListener(new KKDialog.DialogKickShareclickListener() {
                @Override
                public void onClick(SHARE_MEDIA share_media) {
                    buildDes();
//                    shareMin(SHARE_MEDIA.WEIXIN, surl, sdes, stitle,spath);
                    share(SHARE_MEDIA.WEIXIN, surl, sdes, stitle);
                }
            }).setCanfirst(false).setResult(result.discountMoney + "").show(getSupportFragmentManager(), "kkOkDialog");
        }

    }

    GoodsDetail goodsDetail;
    Goods2DetailKick goods2Model;
    boolean isZero = false;
    List<GoodsSpecCell> goodsSpecCells;
    GoodsSpecDetail goodsSpecDetail;

    GoodsSpecDialog goodsSpecDialog;


//    private void showSku(final boolean isrightbuy) {
//        if (isZero) {
//            Toast.makeText(mContext, "该商品已售罄，看看其他商品吧~", Toast.LENGTH_LONG).show();
//            return;
//        }
//        if(isrightbuy&&goods2Model.bargainInfoDTO.bargainInventory==0){
//            Toast.makeText(mContext, "该商品已售罄，看看其他商品吧~", Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (goodsDetail.cheIsNoSku()) {
//            if (isrightbuy) {
//                String singleNumber=new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))+new Date().getTime();
//                ARouter.getInstance()
//                        .build(ServiceRoutes.SERVICE_ORDER)
//                        .withString("bargainId",bargainId)
//                        .withString("bargainMemberId", bargainMemberId)
//                        .withString("bargainMoney",mkick.discountMoney+"")
//                        .withString("goodsId", goodsDetail.id + "")
//                        .withString("goodsSepec", goodsDetail.goodsChildren.get(0).id + "")
//                        .withString("goodsPrice", goodsDetail.platformPrice + "")
//                        .withString("goodsName", goodsDetail.goodsTitle + "")
//                        .withString("goodsSepecStr", "" + "")
//                        .withString("goodsImgPath", goodsDetail.headImage + "")
//                        .withString("goodsType", 2 + "")
//                        .withString("goodsSize", "1")
//                        .withString("mchId", goodsDetail.userId + "")
//                        .withInt("is_home_send", 1)
//                        .navigation();
//
//                finish();
//            } else {
//                ARouter.getInstance()
//                        .build(ServiceRoutes.SERVICE_ORDER)
//                        .withString("goodsId", goodsDetail.id + "")
//                        .withString("goodsSepec", goodsDetail.goodsChildren.get(0).id + "")
//                        .withString("goodsPrice", goodsDetail.platformPrice + "")
//
//
//                        .withString("goodsName", goodsDetail.goodsTitle + "")
//                        .withString("goodsSepecStr", "" + "")
//                        .withString("goodsImgPath", goodsDetail.headImage + "")
//                        .withString("goodsType", 2 + "")
//                        .withString("goodsSize", "1")
//                        .withString("mchId", goodsDetail.userId + "")
//                        .withInt("is_home_send", 1)
//                        .navigation();
//
//                finish();
//            }
//            return;
//        }
//        if (goodsSpecDialog == null) {
//            goodsSpecDialog = GoodsSpecDialog.newInstance();
//        }
//        goodsSpecDialog.show(getSupportFragmentManager(), "优惠券");
//        if(isrightbuy){
//            goodsSpecDialog.setMaxActInventorySpecial(goods2Model.bargainInfoDTO.bargainInventory);
//            goodsSpecDialog.setSingleSelectAct(true);
//            goodsSpecDialog.setActPrice(goodsDetail.platformPrice-mkick.discountMoney);
//        }else {
//            goodsSpecDialog.setMaxActInventorySpecial(-1);
//            goodsSpecDialog.setSingleSelectAct(false);
//            goodsSpecDialog.setActPrice(-1);
//        }
//        if (goodsSpecCells != null) {
//            ////System.out.println("设置规格");
//            goodsSpecDialog.setGoodsId(goodsDetail.id + "");
//            goodsSpecDialog.setGoodsSpecDetail(goodsSpecDetail);
//            goodsSpecDialog.initSpec(goodsSpecCells);
//            goodsSpecDialog.setOnSpecSubmitListener(new GoodsSpecDialog.OnSpecSubmitListener() {
//                @Override
//                public void onSpecSubmit(GoodsSpecDetail goodsSpecDetail) {
//                    if (isrightbuy) {
//                        String singleNumber=new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))+new Date().getTime();
//                        ARouter.getInstance()
//                                .build(ServiceRoutes.SERVICE_ORDER)
//                                .withString("bargainId",bargainId)
//                                .withString("bargainMemberId", bargainMemberId)
//                                .withString("bargainMoney",mkick.discountMoney+"")
//                                .withString("goodsId", goodsSpecDetail.goodsId + "")
//                                .withString("goodsSepec", goodsSpecDetail.id + "")
//                                .withString("goodsPrice", goodsSpecDetail.platformPrice + "")
//                                .withString("goodsName", goodsSpecDetail.goodsTitle + "")
//                                .withString("goodsSepecStr", goodsSpecDetail.goodsSpecStr + "")
//                                .withString("goodsImgPath", goodsSpecDetail.filePath + "")
//                                .withString("goodsType", 2 + "")
//                                .withString("goodsSize", goodsSpecDetail.count)
//                                .withString("mchId", goodsDetail.userId + "")
//                                .withInt("is_home_send", 1)
//                                .navigation();
//
//                        finish();
//                    } else {
//                        ARouter.getInstance()
//                                .build(ServiceRoutes.SERVICE_ORDER)
//                                .withString("goodsId", goodsSpecDetail.goodsId + "")
//                                .withString("goodsSepec", goodsSpecDetail.id + "")
//                                .withString("goodsPrice", goodsSpecDetail.platformPrice + "")
//                                .withString("goodsName", goodsSpecDetail.goodsTitle + "")
//                                .withString("goodsSepecStr", goodsSpecDetail.goodsSpecStr + "")
//                                .withString("goodsImgPath", goodsSpecDetail.filePath + "")
//                                .withString("goodsType", 2 + "")
//                                .withString("goodsSize", goodsSpecDetail.count)
//                                .withString("mchId", goodsDetail.userId + "")
//                                .withInt("is_home_send", 1)
//                                .navigation();
//
//                        finish();
//                    }
//                }
//            });
//        }
//    }


    @Override
    public void successGetGoodsDetail(Goods2DetailKick goodsDetail) {
//        this.goods2Model=goodsDetail;
//        if(goodsDetail!=null){
//            this.goodsDetail = goodsDetail.goodsDetails;
//        }
//        if (goodsDetail != null) {
//            ////System.out.println("滑动到初始");
//            goodsSpecCells = this.goodsDetail.getSpecCell();
//            goodsSpecDetail = new GoodsSpecDetail(this.goodsDetail.getStorePrice(), this.goodsDetail.platformPrice, this.goodsDetail.headImages.get(0).getSplash());
//            if (goodsDetail != null) {
//                buildHasGoods();
//            }
//            if (this.goodsDetail.cheIsNoSku()) {//说明无规格 //则需要判断是不是0库存
//                if (this.goodsDetail.goodsChildren.get(0).availableInventory == 0) {//0库存了 说明要改了
//                    isZero = true;
//                    Toast.makeText(mContext, "该商品已售罄，看看其他商品吧~", Toast.LENGTH_LONG).show();
//                }
//
//            }
//
//        } else {
//
//        }

    }
    ShopDetailModel shopDetailModel;
    @Override
    public void onSucessGetShopDetailOnly(ShopDetailModel shopDetailModel) {
        this.shopDetailModel=shopDetailModel;
    }

    private void buildHasGoods() {

    }

    private void buildDes() {

        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
        String url = String.format("%s?type=8&scheme="+"bargaining"+"&shopId=%s&bargainId=%s&bargainMemberId=%s&memberId=%s&referral_code=%s", urlPrefix,SpUtils.getValue(mContext,SpKey.CHOSE_SHOP), bargainId, bargainMemberId,
                new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)),SpUtils.getValue(mContext, SpKey.GETTOKEN));
        surl = url;
        //System.out.println("分享的连接"+surl);
        String path = String.format("/pages/home/toBargain/index?shopId=%s&id=%s&bargainMemberId=%s&friendType=1&memberId=%s&referral_code=%s", marketingShopId,
                bargainId + "", bargainMemberId, SpUtils.getValue(mContext, SpKey.USER_ID), SpUtils.getValue(mContext, SpKey.GETTOKEN));
        spath = path;
        //System.out.println("分享的砍价path:"+path);
        stitle = "【帮我砍一刀吧】" + "" + mkick.goodsTitle + "";
        sdes = "活动火热进行中，快快加入吧";


//        String result = " hmmm:bargaining?"+"bargainId=" + bargainId + "&bargainMemberId=" + bargainMemberId + "";
//        sdes = "快来捡漏【憨妈妈砍价】复制这条信息后，打开憨妈妈" + result;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        manmap.put("currentPage", currentPage + "");
        kickDetailPresenter.getKickManList(manmap);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage = 1;
        manmap.put("currentPage", currentPage + "");
        kickDetailPresenter.getKickDetail(detailmap);
        kickDetailPresenter.getUserInfo();
    }

    @Override
    public void getData() {
        super.getData();
        currentPage = 1;
        manmap.put("currentPage", currentPage + "");
        if (TextUtils.isEmpty(bargainMemberId)) {
            kickDetailPresenter.kick(detailmap);
        } else {
            manmap.put("bargainMemberId", bargainMemberId);
            detailmap.put("bargainMemberId", bargainMemberId);
            kickDetailPresenter.getKickDetail(detailmap);
        }
        kickDetailPresenter.getUserInfo();
        kickDetailPresenter.getShopDetailOnly(new SimpleHashMapBuilder<String, Object>()
                .puts("shopId", deliveryShopId)
                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG)));
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);

    }

    private void checkTimeOut(Kick url, final TextView kickHour, final TextView kickMin, final TextView kickSec) {

        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.joinTime);
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long nd = 1000 * url.bargainTimeLength * 60 * 60;//加入时间之后需要多少小时
        long desorg = startTime.getTime() + nd;
        long timer = startTime.getTime() + nd;
        if (endTime != null && endTime.getTime() < timer) {
            timer = endTime.getTime();
            desorg = endTime.getTime();
        }
        timer = timer - System.currentTimeMillis();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (timer > 0) {
            //System.out.println("开始计时");
            final long finalTimer = timer;
            final long finalDesorg = desorg;
            countDownTimer = new CountDownTimer(finalTimer, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] array = DateUtils.getDistanceTimeNoDay(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
                    kickHour.setText(array[0]);
                    kickMin.setText(array[1]);
                    kickSec.setText(array[2]);
                }

                public void onFinish() {
                    kickHour.setText("00");
                    kickMin.setText("00");
                    kickSec.setText("00");
                }
            }.start();
            //将此 countDownTimer 放入list.
        } else {
            kickHour.setText("00");
            kickMin.setText("00");
            kickSec.setText("00");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private View.OnClickListener mShareClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mShareDialog.dismiss();
            //System.out.println("开启分享");
            share(mPlatformMap.get(String.valueOf(v.getTag())), surl, sdes, stitle);
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
//        if(true){
//            isshare=true;
//            //System.out.println(url);
//            UMWeb web = new UMWeb(url);
//            web.setTitle(title);
//            web.setThumb(new UMImage(mContext, sBitmap));
//            web.setDescription(des);
//            new ShareAction(this)
//                    .withMedia(web)
//                    .setPlatform(shareMedia)
//                    .setCallback(this)
//                    .share();
//            return;
//        }
        isshare = true;
        //System.out.println(url);
        UMMin umMin = new UMMin(url);
        umMin.setTitle(title);
        if (sBitmap == null) {
            sBitmap = BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.index_share_humb));
        }
        umMin.setThumb(new UMImage(mContext, sBitmap));
        umMin.setDescription(des);
        umMin.setPath(path);
        umMin.setUserName("gh_f9b4fbd9d3b8");
        if (ChannelUtil.isRealRelease()) {

        } else {
            com.umeng.socialize.Config.setMiniPreView();
        }
        new ShareAction(this)
                .withMedia(umMin)
                .setPlatform(shareMedia)
                .setCallback(this)
                .share();
    }

    public void showShareBottomDialog2() {
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
                shareSheet.findViewById(R.id.tv_sina).setOnClickListener(mShareClick);

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
        } else {

            mShareDialog.show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
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
