package com.health.servicecenter.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.MallGoodsDetialChoseAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialDiscountAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialDiscussAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialIntroduceAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialIntroduceEndAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialIntroduceImgAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialIntroducePointAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialRecommendAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialSelectAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialSetAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialStoreAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialTopAllAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialTranAdapter;
import com.health.servicecenter.adapter.MallGoodsDetialUserHeadAdapter;
import com.health.servicecenter.adapter.MallGoodsItemAdapter;
import com.health.servicecenter.adapter.MallGoodsItemNoAdapter;
import com.health.servicecenter.adapter.MallGoodsTitleAdapter;
import com.health.servicecenter.contract.ServiceGoodsDetailAllContract;
import com.health.servicecenter.presenter.ServiceGoodsDetailAllPresenter;
import com.health.servicecenter.widget.GoodsActRuleDialog;
import com.health.servicecenter.widget.GoodsSetDialog;
import com.health.servicecenter.widget.GoodsTranDialog;
import com.health.servicecenter.widget.ShopOrderPickDialog;
import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.adapter.GoodsDetailRankingListAdapter;
import com.healthy.library.adapter.GoodsDetailRankingShareAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.GoodsSpecDialog;
import com.healthy.library.business.NoActDialog;
import com.healthy.library.business.PinWithShop2Dialog;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.contract.GoodsVideoSContract;
import com.healthy.library.contract.PlusContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsMediaWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.IsNeedVideo;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.ActVipDefault;
import com.healthy.library.model.AssemableTeam;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.Goods2DetailKick;
import com.healthy.library.model.Goods2DetailPin;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketGroup;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.GoodsRank;
import com.healthy.library.model.GoodsSetAll;
import com.healthy.library.model.GoodsShop;
import com.healthy.library.model.GoodsSpecCell;
import com.healthy.library.model.GoodsSpecDetail;
import com.healthy.library.model.GoodsSpecLimit;
import com.healthy.library.model.GoodsTran;
import com.healthy.library.model.IntegralModel;
import com.healthy.library.model.MallGroupSimple;
import com.healthy.library.model.MemberAction;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.model.TabEntity;
import com.healthy.library.model.VideoResult;
import com.healthy.library.presenter.ActionPresenterCopy;
import com.healthy.library.presenter.GoodsVideoSPresenter;
import com.healthy.library.presenter.PlusPresenter;
import com.healthy.library.presenter.StoreSharePresenter;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.StringUtils;
import com.healthy.library.widget.AnimManager;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.DragLayout;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = ServiceRoutes.SERVICE_DETAIL)
public class ServiceGoodsDetail extends BaseActivity implements PinWithShop2Dialog.PinOnDialogClickListener, GoodsVideoSContract.View,
        IsNeedVideo, IsNeedShare, IsMediaWindows, IsFitsSystemWindows, ServiceGoodsDetailAllContract.View,
        OnRefreshLoadMoreListener, BaseAdapter.OnOutClickListener, PlusContract.View {

    private String[] titles = {"商品", "详情", "推荐"};
    private String[] titles2 = {"商品", "详情", "推荐"};
    private CommonTabLayout topTab;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private RecyclerView recyclerview;
    private ImageView ivBottomShader;
    private LinearLayout goodsUnder;
    private ImageView basketUnder;
    private ConstraintLayout topTitle;
    private ImageView imgBack;


    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    GoodsSpecDialog goodsSpecDialog;
    GoodsSetDialog goodsSetDialog;


    MallGoodsDetialChoseAdapter mallGoodsDetialChoseAdapter;
    MallGoodsDetialDiscountAdapter mallGoodsDetialDiscountAdapter;
    MallGoodsDetialDiscussAdapter mallGoodsDetialDiscussAdapter;
    MallGoodsDetialRecommendAdapter mallGoodsDetialRecommendAdapter;
    MallGoodsDetialSelectAdapter mallGoodsDetialSelectAdapter;
    MallGoodsDetialTranAdapter mallGoodsDetialTranAdapter;
    MallGoodsDetialSetAdapter mallGoodsDetialSetAdapter;
    MallGoodsDetialTopAllAdapter mallGoodsDetialTopAdapter;
    MallGoodsDetialUserHeadAdapter mallGoodsDetialUserHeadAdapter;

    private MallGoodsDetialIntroduceAdapter mallGoodsDetialIntroduceAdapter;
    private MallGoodsDetialIntroduceImgAdapter mallGoodsDetialIntroduceImgAdapter;
    private MallGoodsDetialIntroduceEndAdapter mallGoodsDetialIntroduceEndAdapter;
    MallGoodsItemNoAdapter mallGoodsItemNoAdapter;
    MallGoodsTitleAdapter mallGoodsTitleAdapter;
    MallGoodsItemAdapter mallGoodsItemAdapter;
    MallGoodsDetialIntroducePointAdapter mallGoodsDetialIntroducePointAdapter;
    private GoodsDetailRankingShareAdapter goodsDetailRankingShareAdapter;
    private GoodsDetailRankingListAdapter goodsDetailRankingListAdapter;
    MallGoodsDetialStoreAdapter mallGoodsDetialStoreAdapter;
    private ImageView imgShare;
    private AutoClickImageView imgBasket;
    @Autowired
    String id;
    @Autowired
    String shopId;
    @Autowired
    String marketingType;//为了新人专享考虑
    @Autowired
    String goodsId;
    @Autowired
    String merchantId;

    String goodsShopId;//选完门店之后的shopid

    @Autowired
    String barcodeSku;//直接传条码


    ShopDetailModel newStoreDetialModelSelect;


    ServiceGoodsDetailAllPresenter serviceGoodsDetailPresenter;
    ActVip.VipShop vipShop;

    List<GoodsSpecCell> goodsSpecCells;
    GoodsDetail goodsDetail;
    GoodsSpecDetail goodsSpecDetail;
    private ImageView imgBack2;

    @Autowired
    String assembleId;

    @Autowired
    String bargainId;
    @Autowired
    String bargainMemberId;

    @Autowired
    String teamNum;


    @Autowired
    String isNtReal = "0";//是否有新人资格


    Goods2DetailKick goods2ModelKick;
    Goods2DetailPin goods2ModelPin;


    boolean isgoodshas = true;
    int page = 1;
    private DragLayout dargF;
    private View dargM;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;

    Map<String, Object> recommandMap = new HashMap<>();
    public int tab1org = 0;
    public int tab2org = 5;
    public int tab3org = 6;
    public int[] poss;
    public int tab1 = tab1org;
    public int tab2 = tab2org;
    public int tab3 = tab3org;
    private GoodsTran goodsTran;
    private RelativeLayout shopCartRel;
    private TextView shopCartNum;
    private ImageView scrollUp;
    public Bitmap sBitmap;

    @Autowired
    String token;
    @Autowired
    String course_id;
    @Autowired
    String liveStatus;

    private TextView goBask;
    private TextView goOrder;
    private LinearLayout pinCallStore;
    private LinearLayout pinSingleBuy;
    private TextView orderGroupSinglePrice;
    private TextView orderGroupSinglePriceT;
    private LinearLayout pinBuy;
    private TextView orderGroupPrice;
    private TextView orderGroupNumber;
    private TextView goodsMoneyV;
    private List<GoodsSpecDetail> exSkuList;
    private LinearLayout pointLL;
    private TextView pointValue;
    private AutoClickImageView goodsShareCoupon;
    ShopOrderPickDialog shopOrderPickDialog;
    StoreSharePresenter storeSharePresenter;
    PlusPresenter plusPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.service_activity_goodsdetail_all;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    public int dxall = 0;

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
        if (TextUtils.isEmpty(shopId)) {//分享的时候 shopId时有值的
            shopId = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP);
            goodsShopId = shopId;
        } else {
            goodsShopId = shopId;
            storeSharePresenter = new StoreSharePresenter(this);
            storeSharePresenter.getShareStoreDetial(shopId);
        }
        if ("0".equals(marketingType)) {
            marketingType = null;
        }
        if ("5".equals(marketingType) || TextUtils.isEmpty(shopId)) {
            goodsShopId = "0";
        }
        if (TextUtils.isEmpty(merchantId)) {
            merchantId = SpUtils.getValue(mContext, SpKey.CHOSE_MC);
        }
        if ("-2".equals(marketingType)) {
            marketingType = null;//-2不作为查询条件
        }
        if (TextUtils.isEmpty(id)) {
            id = goodsId;
        }
//        marketingType="2";
        System.out.println(teamNum);
        goodsVideoSPresenter = new GoodsVideoSPresenter(this, this);
        plusPresenter = new PlusPresenter(this, this);
        //积分兑换锚点不准确 多处理加算额外值
        if ("5".equals(marketingType)) {
            tab2org++;
            tab3org++;
        }
        //锚点不准确 处理加算
        if ("6".equals(marketingType) || "7".equals(marketingType)) {
            tab2org++;
            tab3org--;
        }
        buildTabNow();
        serviceGoodsDetailPresenter = new ServiceGoodsDetailAllPresenter(this, this);
        buildRecyclerView();
        mTabEntities.clear();
        for (int i = 0; i < titles.length; i++) {
            //插入tab标签
            mTabEntities.add(new TabEntity(titles[i], 0, 0));
        }
        topTab.setTabData(mTabEntities);
        recommandMap.put("type", "6");
        recommandMap.put("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
        recommandMap.put("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
        recommandMap.put("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        recommandMap.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        recommandMap.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        recommandMap.put("pageNum", page + "");
        recommandMap.put("pageSize", "10");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    ////System.out.println("滑动到商品:" + tab1);
                    dxall = 0;
                    virtualLayoutManager.scrollToPositionWithOffset(tab1, 0);
                }
                if (position == 1) {
                    ////System.out.println("滑动到详情:" + tab2);
                    virtualLayoutManager.scrollToPositionWithOffset(tab2, 0);
                }
                if (position == 2) {
                    ////System.out.println("滑动到推荐:" + tab3);
                    virtualLayoutManager.scrollToPositionWithOffset(tab3, 0);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        imgBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "母婴商品详情页顶部右上角“购物车\"图标");
                MobclickAgent.onEvent(mContext, "btn_APP__MaternalandChildGoods_ShoppingCart", nokmap);

                MobclickAgent.onEvent(mContext, "event2APPGoodsDetialShopCartClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-购物车入口点击量"));
                if (marketingType != null) {
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialActivityShopCartClick", new SimpleHashMapBuilder<String, String>().puts("soure", "活动商品详情页-购物车入口点击量"));
                    if (marketingType.equals("5")) {
                        MobclickAgent.onEvent(mContext, "event2APPGoodsDetialPointShopCartClick", new SimpleHashMapBuilder<String, String>().puts("soure", "积分商城商品详情页-购物车入口点击量"));
                    }
                }
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_BASKET)
                        .withString("visitShopId", shopId)
                        .navigation();
            }
        });
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                //////System.out.println("dy:" + dy);
                dxall += dy;
                if (dxall < 0) {
                    dxall = 0;
                }
                int firstVisibleItemPosition = virtualLayoutManager.findFirstVisibleItemPosition();
                int firstCompletelyVisibleItemPosition = virtualLayoutManager.findFirstCompletelyVisibleItemPosition();
                buildTopTab((dxall > getResources().getDimension(R.dimen.status_2020) * 2 || firstVisibleItemPosition > 0) && (firstCompletelyVisibleItemPosition != 0) ? 1 : 0);
                if (isScrolled) {
                    int pos = 0;
                    if (firstVisibleItemPosition != -1) {
                        for (int i = 0; i < poss.length; i++) {
                            if (firstVisibleItemPosition >= poss[i]) {
                                pos = i;
                            }
                        }
                        topTab.setCurrentTab(pos);
                    }

                }

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {
                    isScrolled = false;
                } else {
                    isScrolled = true;
                }

            }
        });
        scrollUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topTab.setCurrentTab(0);
                virtualLayoutManager.scrollToPositionWithOffset(tab1, 0);
                buildTopTab(0);
                dxall = 0;
            }
        });
        buildTopTab(0);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        /**
         * 分享商品
         */
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "商品详情页右上角点击”分享“");
                MobclickAgent.onEvent(mContext, "btn_APP__MaternalandChildGoods_CommodityDetails_Share", nokmap);
                MobclickAgent.onEvent(mContext, "event2APPGoodsDetialShareClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-商品分享按钮点击量"));
                if (goodsDetail != null) { //0普通 1砍价 2拼团 3秒杀 4新人专享
                    if (!TextUtils.isEmpty(assembleId)) {//拼团
                        SeckShareDialog dialog = SeckShareDialog.newInstance();
                        dialog.setGoodsDetail(2, 2, goodsDetail, goodsShopId, goodsDetail.userId + "");
                        dialog.setGoodsKickPin(null, goods2ModelPin);
                        dialog.show(getSupportFragmentManager(), "分享");
                    } else if (!TextUtils.isEmpty(bargainId)) {//砍价
                        SeckShareDialog dialog = SeckShareDialog.newInstance();
                        dialog.setGoodsDetail(2, 1, goodsDetail, goodsShopId, goodsDetail.userId + "");
                        dialog.setGoodsKickPin(goods2ModelKick, null);
                        dialog.show(getSupportFragmentManager(), "分享");
                    } else {
                        if ("3".equals(goodsDetail.marketingType)) {//秒杀
                            SeckShareDialog dialog = SeckShareDialog.newInstance();
                            dialog.setGoodsDetail(2, 3, goodsDetail, goodsShopId, goodsDetail.userId + "");
                            dialog.show(getSupportFragmentManager(), "分享");
                        } else if ("4".equals(goodsDetail.marketingType)) {//新客
                            Map nokmap1 = new HashMap<String, String>();
                            nokmap.put("soure", "新客专享商品的分享量（新客专享商品详情页中分享按钮点击量）");
                            MobclickAgent.onEvent(mContext, "event2APPNewUserGoodsShareClick", nokmap1);
                            SeckShareDialog dialog = SeckShareDialog.newInstance();
                            dialog.setGoodsDetail(2, 4, goodsDetail, goodsShopId, goodsDetail.userId + "");
                            dialog.show(getSupportFragmentManager(), "分享");
                        } else if ("5".equals(goodsDetail.marketingType)) {//积分
                            MobclickAgent.onEvent(mContext, "event2APPPointsGoodsDetialShareClick", new SimpleHashMapBuilder<String, String>().puts("soure", "积分商城商品详情页-商品分享按钮点击量"));
                            SeckShareDialog dialog = SeckShareDialog.newInstance();
                            dialog.setGoodsDetail(2, 5, goodsDetail, goodsShopId, goodsDetail.userId + "");
                            dialog.show(getSupportFragmentManager(), "分享");
                        } else if ("8".equals(goodsDetail.marketingType)) {//PLUS 专享商品
                            SeckShareDialog dialog = SeckShareDialog.newInstance();
                            dialog.setGoodsDetail(2, 8, goodsDetail, goodsShopId, goodsDetail.userId + "");
                            dialog.show(getSupportFragmentManager(), "分享");
                        } else {//普通商品
                            SeckShareDialog dialog = SeckShareDialog.newInstance();
                            dialog.setGoodsDetail(2, 0, goodsDetail, goodsShopId, goodsDetail.userId + "");
                            dialog.show(getSupportFragmentManager(), "分享");
                        }
                    }
                }
            }
        });
        getData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (storeSharePresenter != null) {
            storeSharePresenter.back();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mallGoodsDetialIntroduceImgAdapter != null) {
            mallGoodsDetialIntroduceImgAdapter.onDestroy();
        }
        EventBus.getDefault().unregister(this);
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "商品详情页浏览时长");
        nokmap.put("time", (System.currentTimeMillis() - mills) / 1000);
        MobclickAgent.onEvent(mContext, "event_APP_GoodsDetial_Stop", nokmap);
    }

    private long mills = System.currentTimeMillis();

    private void buildTabNow() {
        if (poss == null) {

            poss = new int[]{tab1, tab2, tab3};
        } else {
            poss[0] = tab1;
            poss[1] = tab2;
            poss[2] = tab3;
        }

    }

    private void buildTopTab(int firstCompletelyVisibleItemPosition) {
        if (firstCompletelyVisibleItemPosition == 0) {
            topTab.setVisibility(View.INVISIBLE);
            scrollUp.setVisibility(View.GONE);
            imgBack.setImageResource(R.drawable.index_ic_back_b);
            imgShare.setImageResource(R.drawable.index_ic_share_b);
            imgBasket.setImageResource(R.drawable.index_ic_basket_b);
            topTitle.setBackgroundColor(Color.parseColor("#00000000"));
            if (isgoodshas) {
            } else {
                imgShare.setVisibility(View.GONE);
            }
        } else {
            if (isgoodshas) {
                topTab.setVisibility(View.VISIBLE);

            } else {
                imgShare.setVisibility(View.GONE);
            }
            scrollUp.setVisibility(View.VISIBLE);
            topTitle.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            imgBack.setImageResource(R.drawable.index_ic_back_c);
            imgShare.setImageResource(R.drawable.index_ic_share_c);
            imgBasket.setImageResource(R.drawable.index_ic_basket_c);

        }
    }

    //0普通 1砍价 2拼团 3秒杀 4新人专享
    @Override
    public void getData() {
        super.getData();
        if (mallGoodsDetialChoseAdapter != null) {
            mallGoodsDetialChoseAdapter.setLoadSuccess(false);
        }
        if (mallGoodsDetialRecommendAdapter != null) {
            mallGoodsDetialRecommendAdapter.setLoadSuccess(false);
        }
        if (mallGoodsDetialTopAdapter != null) {
            mallGoodsDetialTopAdapter.setLoadSuccess(false);
        }
        if (mallGoodsDetialSetAdapter != null) {
            mallGoodsDetialSetAdapter.setLoadSuccess(false);
        }

        dxall = 0;
        tab1 = tab1org;
        tab2 = tab2org;
        tab3 = tab3org;
        // ////System.out.println("滑动重设372");
        buildTabNow();
        if (TextUtils.isEmpty(merchantId)) {//可能外部还没定位呢
//            getData();
            return;
        }
        if (TextUtils.isEmpty(goodsShopId)) {
            layoutRefresh.finishRefresh();
            return;
        }
        if (!TextUtils.isEmpty(assembleId)) {
            initActView(2);
            serviceGoodsDetailPresenter.getGoodsDetailPin(new SimpleHashMapBuilder<String, Object>().puts("assembleId", assembleId).puts("shopId", goodsShopId));
            serviceGoodsDetailPresenter.getTeamList(new SimpleHashMapBuilder<String, Object>().puts("assembleId", assembleId).puts("shopId", goodsShopId));
            serviceGoodsDetailPresenter.getGoodsDetailPinSkuEx(new SimpleHashMapBuilder<String, Object>().puts("marketingId", assembleId).puts("shopId", goodsShopId));
        } else if (!TextUtils.isEmpty(bargainId)) {
            initActView(1);
            serviceGoodsDetailPresenter.getGoodsDetailKick(new SimpleHashMapBuilder<String, Object>().puts("bargainId", bargainId).puts("shopId", goodsShopId));
            serviceGoodsDetailPresenter.getGoodsDetailKickSkuEx(new SimpleHashMapBuilder<String, Object>().puts("marketingId", bargainId).puts("shopId", goodsShopId));
        } else {
            if (TextUtils.isEmpty(id) || "null".equals(id)) {
                serviceGoodsDetailPresenter.getGoodsDetailWithCode(new SimpleHashMapBuilder<String, Object>().puts("marketingType", marketingType)
                        .putObject(new ActVipDefault(barcodeSku,
                                goodsShopId,
                                "",
                                null,
                                SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC),
                                "1",
                                new ActVipDefault.GoodsChildren(barcodeSku,
                                        0,
                                        0
                                ), new ActVipDefault.GoodsFiles())));
            } else {
//                if ("5".equals(marketingType)) {
//                    serviceGoodsDetailPresenter.getGoodsDetail(new SimpleHashMapBuilder<String, Object>().puts("marketingType", marketingType)
//                            .puts("shopId", "0")
//                            .puts("id", id + ""));
//                } else {
                serviceGoodsDetailPresenter.getGoodsDetail(new SimpleHashMapBuilder<String, Object>().puts("marketingType", marketingType)
                        .puts("shopId", goodsShopId)
                        .puts("id", id + ""));
//                }
            }


        }
        try {//请求是否是plus
            plusPresenter.checkPlus(new SimpleHashMapBuilder<String, Object>()
                    .puts("phoneNum", SpUtils.getValue(mContext, SpKey.PHONE))
                    .puts("merchantId", merchantId));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    //0普通 1砍价 2拼团 3秒杀 4新人专享
    private void initActView(int actFlag) {
        goodsUnder.removeAllViews();
        if (actFlag == 0) {
            goodsUnder.addView(LayoutInflater.from(mContext).inflate(R.layout.item_include_detail_bottom_normal, goodsUnder, false));
            goodsMoneyV = (TextView) goodsUnder.findViewById(R.id.goodsMoneyV);
            goBask = (TextView) goodsUnder.findViewById(R.id.goBask);
            goOrder = (TextView) goodsUnder.findViewById(R.id.goOrder);
            goBask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页【加入购物车】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_AddShoppingCart", nokmap);
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialAddShopCartClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-加入购物车点击量"));
                    showSku(false);
                }
            });
            goOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页【立即购买】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_Buy", nokmap);
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialExchangeClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-立即购买点击量"));
                    showSku(true);
                }
            });
            goOrder.setText("立即抢购");
        } else if (actFlag == 1) {
            goodsUnder.addView(LayoutInflater.from(mContext).inflate(R.layout.item_include_detail_bottom_kick, goodsUnder, false));
            pinCallStore = (LinearLayout) goodsUnder.findViewById(R.id.pinCallStore);
            pinSingleBuy = (LinearLayout) goodsUnder.findViewById(R.id.pinSingleBuy);
            orderGroupSinglePrice = (TextView) goodsUnder.findViewById(R.id.orderGroupSinglePrice);
            orderGroupSinglePriceT = (TextView) goodsUnder.findViewById(R.id.orderGroupSinglePriceT);
            pinBuy = (LinearLayout) goodsUnder.findViewById(R.id.pinBuy);
            orderGroupPrice = (TextView) goodsUnder.findViewById(R.id.orderGroupPrice);
            orderGroupNumber = (TextView) goodsUnder.findViewById(R.id.orderGroupNumber);
            pinBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showSkuKick(true);
                }
            });
            pinSingleBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSkuKick(false);
                }
            });

        } else if (actFlag == 2) {
            goodsUnder.addView(LayoutInflater.from(mContext).inflate(R.layout.item_include_detail_bottom_pin, goodsUnder, false));

            pinCallStore = (LinearLayout) goodsUnder.findViewById(R.id.pinCallStore);
            pinSingleBuy = (LinearLayout) goodsUnder.findViewById(R.id.pinSingleBuy);
            orderGroupSinglePrice = (TextView) goodsUnder.findViewById(R.id.orderGroupSinglePrice);
            orderGroupSinglePriceT = (TextView) goodsUnder.findViewById(R.id.orderGroupSinglePriceT);
            pinBuy = (LinearLayout) goodsUnder.findViewById(R.id.pinBuy);
            orderGroupPrice = (TextView) goodsUnder.findViewById(R.id.orderGroupPrice);
            orderGroupNumber = (TextView) goodsUnder.findViewById(R.id.orderGroupNumber);
            pinBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSkuPin(true);
                }
            });
            pinSingleBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    teamNum = null;
                    showSkuPin(false);
                }
            });

        } else if (actFlag == 3) {//缺省
            goodsUnder.addView(LayoutInflater.from(mContext).inflate(R.layout.item_include_detail_bottom_normal, goodsUnder, false));
            goodsMoneyV = (TextView) goodsUnder.findViewById(R.id.goodsMoneyV);
            goBask = (TextView) goodsUnder.findViewById(R.id.goBask);
            goOrder = (TextView) goodsUnder.findViewById(R.id.goOrder);
            if (goodsDetail == null) {
                return;
            }
            if (goodsDetail.marketingType != null && "3".equals(goodsDetail.marketingType)) {
                goBask.setVisibility(View.GONE);
            } else {
                goBask.setVisibility(View.VISIBLE);
            }
            goBask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页【加入购物车】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_AddShoppingCart", nokmap);
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialAddShopCartClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-加入购物车点击量"));
                    showSkuAct(false);
                }
            });
            goOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页【立即购买】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_Buy", nokmap);
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialExchangeClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-立即购买点击量"));
                    showSkuAct(true);
                }
            });
            goOrder.setText("立即抢购");
        } else if (actFlag == 4) {
            goodsUnder.addView(LayoutInflater.from(mContext).inflate(R.layout.item_include_detail_bottom_nt, goodsUnder, false));
            goodsMoneyV = (TextView) goodsUnder.findViewById(R.id.goodsMoneyV);
            goBask = (TextView) goodsUnder.findViewById(R.id.goBask);
            goOrder = (TextView) goodsUnder.findViewById(R.id.goOrder);
            goBask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页【加入购物车】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_AddShoppingCart", nokmap);
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialAddShopCartClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-加入购物车点击量"));
                    showSkuNt(false);
                }
            });
            goOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页【立即购买】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_Buy", nokmap);
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialExchangeClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-立即购买点击量"));
                    showSkuNt(true);
                }
            });
            if ("1".equals(isNtReal)) {
                goBask.setVisibility(View.GONE);
                mallGoodsDetialTopAdapter.setNtReal(true);
            } else {
                mallGoodsDetialTopAdapter.setNtReal(false);
                goBask.setVisibility(View.VISIBLE);
            }
            goOrder.setText("立即抢购");
        } else if (actFlag == 3) {//缺省
            goodsUnder.addView(LayoutInflater.from(mContext).inflate(R.layout.item_include_detail_bottom_normal, goodsUnder, false));
            goodsMoneyV = (TextView) goodsUnder.findViewById(R.id.goodsMoneyV);
            goBask = (TextView) goodsUnder.findViewById(R.id.goBask);
            goOrder = (TextView) goodsUnder.findViewById(R.id.goOrder);
            if (goodsDetail == null) {
                return;
            }
            if (goodsDetail.marketingType != null && "3".equals(goodsDetail.marketingType)) {
                goBask.setVisibility(View.GONE);
            } else {
                goBask.setVisibility(View.VISIBLE);
            }
            goBask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页【加入购物车】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_AddShoppingCart", nokmap);
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialAddShopCartClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-加入购物车点击量"));
                    showSkuAct(false);
                }
            });
            goOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页【立即购买】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_Buy", nokmap);
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialExchangeClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-立即购买点击量"));
                    showSkuAct(true);
                }
            });
            goOrder.setText("立即抢购");
        } else if (actFlag == 8) {//缺省
            goodsUnder.addView(LayoutInflater.from(mContext).inflate(R.layout.item_include_detail_bottom_normal, goodsUnder, false));
            goodsMoneyV = (TextView) goodsUnder.findViewById(R.id.goodsMoneyV);
            goBask = (TextView) goodsUnder.findViewById(R.id.goBask);
            goOrder = (TextView) goodsUnder.findViewById(R.id.goOrder);
            if (goodsDetail == null) {
                return;
            }
            if (goodsDetail.marketingType != null && "3".equals(goodsDetail.marketingType)) {
                goBask.setVisibility(View.GONE);
            } else {
                goBask.setVisibility(View.VISIBLE);
            }
            goBask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页【加入购物车】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_AddShoppingCart", nokmap);
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialAddShopCartClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-加入购物车点击量"));
                    showSkuAct(false);
                }
            });
            goOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页【立即购买】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_Buy", nokmap);
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialExchangeClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-立即购买点击量"));
                    showSkuAct(true);
                }
            });
        } else if (actFlag == 5) {
            goodsUnder.addView(LayoutInflater.from(mContext).inflate(R.layout.item_include_detail_bottom_point, goodsUnder, false));
            goOrder = (TextView) goodsUnder.findViewById(R.id.goOrder);
            pointLL = (LinearLayout) goodsUnder.findViewById(R.id.pointLL);
            pointValue = (TextView) goodsUnder.findViewById(R.id.pointValue);
            goOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pointLL.getVisibility() == View.VISIBLE) {
                        Toast.makeText(mContext, goOrder.getText().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        showSkuAct(true);
                    }
                }
            });
        } else if (actFlag == 8) {
            goodsUnder.addView(LayoutInflater.from(mContext).inflate(R.layout.item_include_detail_bottom_normal, goodsUnder, false));

            goodsMoneyV = (TextView) goodsUnder.findViewById(R.id.goodsMoneyV);
            goBask = (TextView) goodsUnder.findViewById(R.id.goBask);
            goOrder = (TextView) goodsUnder.findViewById(R.id.goOrder);
            goBask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页【加入购物车】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_AddShoppingCart", nokmap);
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialAddShopCartClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-加入购物车点击量"));
                    showSkuAct(false);
                }
            });
            goOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页【立即购买】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_Buy", nokmap);
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialExchangeClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-立即购买点击量"));
                    showSkuAct(true);
                }
            });
            goOrder.setText("立即抢购");
        } else {
            goodsUnder.addView(LayoutInflater.from(mContext).inflate(R.layout.item_include_detail_bottom_normal, goodsUnder, false));

            goodsMoneyV = (TextView) goodsUnder.findViewById(R.id.goodsMoneyV);
            goBask = (TextView) goodsUnder.findViewById(R.id.goBask);
            goOrder = (TextView) goodsUnder.findViewById(R.id.goOrder);
            goBask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页【加入购物车】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_AddShoppingCart", nokmap);
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialAddShopCartClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-加入购物车点击量"));
                    showSkuAct(false);
                }
            });
            goOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "商品详情页【立即购买】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_Buy", nokmap);
                    MobclickAgent.onEvent(mContext, "event2APPGoodsDetialExchangeClick", new SimpleHashMapBuilder<String, String>().puts("soure", "商品详情页-立即购买点击量"));
                    showSkuAct(true);
                }
            });
            goOrder.setText("立即抢购");
        }
    }

    boolean isScrolled = false;

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerview.setLayoutManager(virtualLayoutManager);
        recyclerview.setAdapter(delegateAdapter);
//        recyclerview.setItemAnimator(new DefaultItemAnimator());
        mallGoodsItemNoAdapter = new MallGoodsItemNoAdapter();
        mallGoodsItemNoAdapter.setModel(null);
        delegateAdapter.addAdapter(mallGoodsItemNoAdapter);
        mallGoodsDetialTopAdapter = new MallGoodsDetialTopAllAdapter();
        mallGoodsDetialTopAdapter.setType(1);
        mallGoodsDetialTopAdapter.setOnTopLoadMoreListener(new MallGoodsDetialTopAllAdapter.OnTopLoadMoreListener() {
            @Override
            public void onTopLoadMore() {
                virtualLayoutManager.scrollToPositionWithOffset(tab2, 0);
                topTab.setCurrentTab(1);
                buildTopTab(1);
            }
        });
        mallGoodsDetialTopAdapter.setModel(null);
        mallGoodsDetialTopAdapter.setPinOnDialogClickListener(this);
        mallGoodsDetialTopAdapter.setOutClickListener(this);
        delegateAdapter.addAdapter(mallGoodsDetialTopAdapter);
        mallGoodsDetialUserHeadAdapter = new MallGoodsDetialUserHeadAdapter();
        mallGoodsDetialUserHeadAdapter.setModel(null);
        delegateAdapter.addAdapter(mallGoodsDetialUserHeadAdapter);
        mallGoodsDetialDiscountAdapter = new MallGoodsDetialDiscountAdapter();
        delegateAdapter.addAdapter(mallGoodsDetialDiscountAdapter);
        mallGoodsDetialDiscountAdapter.setModel(null);
        mallGoodsDetialTranAdapter = new MallGoodsDetialTranAdapter();
        delegateAdapter.addAdapter(mallGoodsDetialTranAdapter);
//        mallGoodsDetialTranAdapter.setModel("");
        mallGoodsDetialTranAdapter.setOutClickListener(this);
        mallGoodsDetialStoreAdapter = new MallGoodsDetialStoreAdapter();
        delegateAdapter.addAdapter(mallGoodsDetialStoreAdapter);
        mallGoodsDetialStoreAdapter.setOutClickListener(this);
        mallGoodsDetialSelectAdapter = new MallGoodsDetialSelectAdapter();
        delegateAdapter.addAdapter(mallGoodsDetialSelectAdapter);
//        mallGoodsDetialSelectAdapter.setModel("");
        mallGoodsDetialSetAdapter = new MallGoodsDetialSetAdapter();
        mallGoodsDetialSetAdapter.setType(1);
        delegateAdapter.addAdapter(mallGoodsDetialSetAdapter);
        mallGoodsDetialSetAdapter.setId(id);
        mallGoodsDetialSetAdapter.setOutClickListener(this);
        mallGoodsDetialDiscussAdapter = new MallGoodsDetialDiscussAdapter();
        delegateAdapter.addAdapter(mallGoodsDetialDiscussAdapter);
        mallGoodsDetialDiscussAdapter.setModel(null);
        goodsDetailRankingShareAdapter = new GoodsDetailRankingShareAdapter();
        delegateAdapter.addAdapter(goodsDetailRankingShareAdapter);
        goodsDetailRankingShareAdapter.setOutClickListener(this);
        goodsDetailRankingShareAdapter.setModel(null);
        goodsDetailRankingListAdapter = new GoodsDetailRankingListAdapter();
        delegateAdapter.addAdapter(goodsDetailRankingListAdapter);
        goodsDetailRankingListAdapter.setModel(null);
        mallGoodsDetialRecommendAdapter = new MallGoodsDetialRecommendAdapter();
        mallGoodsDetialRecommendAdapter.setType(1);
        delegateAdapter.addAdapter(mallGoodsDetialRecommendAdapter);
//        mallGoodsDetialRecommendAdapter.setModel("");
        mallGoodsDetialRecommendAdapter.setOutClickListener(this);
        mallGoodsDetialChoseAdapter = new MallGoodsDetialChoseAdapter();
        mallGoodsDetialChoseAdapter.setType(1);
        delegateAdapter.addAdapter(mallGoodsDetialChoseAdapter);
        mallGoodsDetialChoseAdapter.setOutClickListener(this);
        mallGoodsDetialIntroducePointAdapter = new MallGoodsDetialIntroducePointAdapter();
        delegateAdapter.addAdapter(mallGoodsDetialIntroducePointAdapter);
        mallGoodsDetialIntroducePointAdapter.setModel(null);
        mallGoodsDetialIntroduceAdapter = new MallGoodsDetialIntroduceAdapter();
        delegateAdapter.addAdapter(mallGoodsDetialIntroduceAdapter);
        mallGoodsDetialIntroduceImgAdapter = new MallGoodsDetialIntroduceImgAdapter();
        mallGoodsDetialIntroduceImgAdapter.setType(1);
        delegateAdapter.addAdapter(mallGoodsDetialIntroduceImgAdapter);
        mallGoodsDetialIntroduceEndAdapter = new MallGoodsDetialIntroduceEndAdapter();
        delegateAdapter.addAdapter(mallGoodsDetialIntroduceEndAdapter);
        mallGoodsTitleAdapter = new MallGoodsTitleAdapter();
        delegateAdapter.addAdapter(mallGoodsTitleAdapter);
        mallGoodsTitleAdapter.setModel(null);
        mallGoodsItemAdapter = new MallGoodsItemAdapter();
        delegateAdapter.addAdapter(mallGoodsItemAdapter);
        mallGoodsItemAdapter.setModel(null);
        mallGoodsItemAdapter.setOnBasketClickListener(new MallGoodsItemAdapter.OnBasketClickListener() {
            @Override
            public void onBasketClick(View view) {

                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "商品详情页商家推荐列表商品点击【加入购物车】");
                MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_AddShoppingCart", nokmap);

                AnimManager animManager = new AnimManager.Builder()
                        .with((Activity) mContext)
                        .startView(view)
                        .endView(imgBasket)
                        .imageUrl(R.drawable.basket_red)
                        .build();
                animManager.startAnim();
            }
        });
        mallGoodsItemAdapter.setOutClickListener(new BaseAdapter.OnOutClickListener() {
            @Override
            public void outClick(@NotNull String function, @NotNull Object obj) {
                if ("addShopCat".equals(function)) {
                    RecommendList goodsListModel = (RecommendList) obj;
                    if (goodsListModel != null) {
                        serviceGoodsDetailPresenter.addGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                                .puts("shopId", shopId)
                                .puts("goodsShopId", goodsShopId)
                                .puts("goodsSource", "1")
                                .puts("goodsType", goodsListModel.goodsType + "")
                                .puts("goodsId", goodsListModel.goodsId + "")
                                .puts("goodsSpecId", goodsListModel.skuId + "")
                                .puts("goodsQuantity", 1 + "")
                        );
                    }

                }
            }
        });
    }

    private void showSkuAct(final boolean isrightbuy) {
        if (newStoreDetialModelSelect == null) {
            Toast.makeText(mContext, "未获取到门店配置请联系商家", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isZero) {
            showToast("该商品已售罄，看看其他商品吧~");
            return;
        }
        if ("2".equals(goodsDetail.goodsType) && !"5".equals(goodsDetail.marketingType)) {

        } else {
            if (newStoreDetialModelSelect == null) {
                Toast.makeText(mContext, "服务商品未配置服务门店请联系商家", Toast.LENGTH_LONG).show();
                return;
            }
        }

        goodsDetail.isErrorCount = false;
        if (goodsDetail.cheIsNoSku()) {
            if ("8".equals(goodsDetail.marketingType) && "1".equals(goodsDetail.isPlusOnly) && !SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false)) {//专享且非会员
                Toast.makeText(mContext, "本商品为PLUS专享商品,您还不是PLUS会员。", Toast.LENGTH_LONG).show();
                return;
            }
            if (!isrightbuy) {//加入购物车左边
                if (goodsDetail.getMarketingSalesMin() > goodsDetail.getRealCanBuy(goodsDetail.marketingSalesMax, goodsSpecDetail.nowOrderBuyCount, goodsDetail.marketingGoodsChildren.get(0).getAvailableInventoryMark(goodsDetail.goodsChildren.get(0)))) {
                    if (goodsDetail.getMarketingSalesMin() > goodsDetail.marketingGoodsChildren.get(0).getAvailableInventoryMark(goodsDetail.goodsChildren.get(0))) {
                        Toast.makeText(mContext, "活动库存不足", Toast.LENGTH_LONG).show();
                    } else if (goodsDetail.marketingSalesMax - goodsSpecDetail.nowOrderBuyCount <= 0) {
                        Toast.makeText(mContext, "您已购" + goodsSpecDetail.nowOrderBuyCount + "件，" + "该活动商品限购" + goodsDetail.marketingSalesMax + "件", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, "起购" + goodsDetail.marketingSalesMin + "件，" + "您已购" + goodsSpecDetail.nowOrderBuyCount + "件，" + "该活动商品限购" + goodsDetail.marketingSalesMax + "件", Toast.LENGTH_LONG).show();
                    }
                    goodsDetail.isErrorCount = true;
                }
                AnimManager animManager = new AnimManager.Builder()
                        .with(ServiceGoodsDetail.this)
                        .startView(goBask)
                        .endView(imgBasket)
                        .imageUrl(R.drawable.basket_red)
                        .build();
                animManager.startAnim();
                serviceGoodsDetailPresenter.addGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                        .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                        .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                        .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                        .puts("goodsSource", "1")
                        .puts("shopId", shopId)
                        .puts("goodsShopId", goodsShopId)
                        .puts("packageType", "SINGLE_PACKAGE")
                        .puts("goodsId", goodsDetail.id + "")
                        .puts("goodsType", goodsDetail.goodsType + "")
                        .puts("goodsSpecId", goodsDetail.marketingGoodsChildren.get(0).getId(goodsDetail.marketingType))
                        .puts("goodsQuantity", goodsDetail.getMarketingSalesMin() + "")
                );
                if (mallGoodsDetialSelectAdapter.getModel() == null) {

                }
            } else {//直接下单
                if (goodsDetail.getMarketingSalesMin() > goodsDetail.getRealCanBuy(goodsDetail.marketingSalesMax, goodsSpecDetail.nowOrderBuyCount, goodsDetail.marketingGoodsChildren.get(0).getAvailableInventoryMark(goodsDetail.goodsChildren.get(0)))) {
                    if (goodsDetail.getMarketingSalesMin() > goodsDetail.marketingGoodsChildren.get(0).getAvailableInventoryMark(goodsDetail.goodsChildren.get(0))) {
                        Toast.makeText(mContext, "活动库存不足", Toast.LENGTH_LONG).show();
                    } else if (goodsDetail.marketingSalesMax - goodsSpecDetail.nowOrderBuyCount <= 0) {
                        Toast.makeText(mContext, "您已购" + goodsSpecDetail.nowOrderBuyCount + "件，" + "该活动商品限购" + goodsDetail.marketingSalesMax + "件", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, "起购" + goodsDetail.marketingSalesMin + "件，" + "您已购" + goodsSpecDetail.nowOrderBuyCount + "件，" + "该活动商品限购" + goodsDetail.marketingSalesMax + "件", Toast.LENGTH_LONG).show();
                    }
                    return;
                }
                GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(goodsDetail.marketingId);
                if (goodsDetail.marketingType == null) {
                    goodsMarketing = null;
                } else if ("8".equals(goodsDetail.marketingType) && !"1".equals(goodsDetail.isPlusOnly) && !SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false)) {//
                    goodsMarketing = null;
                } else {
                    goodsMarketing.availableInventory = goodsDetail.marketingGoodsChildren.get(0).getAvailableInventoryMark(goodsDetail.goodsChildren.get(0));
                    goodsMarketing.mapMarketingGoodsId = goodsDetail.mapMarketingGoodsId;
                    goodsMarketing.marketingType = goodsDetail.marketingType;
                    goodsMarketing.id = goodsDetail.marketingGoodsChildren.get(0).id;
                    goodsMarketing.marketingPrice = goodsDetail.marketingGoodsChildren.get(0).marketingPrice;
                    goodsMarketing.pointsPrice = goodsDetail.marketingGoodsChildren.get(0).pointsPrice;
                    goodsMarketing.salesMin = goodsDetail.getMarketingSalesMin();
                    goodsMarketing.salesMax = goodsDetail.getMarketingSalesMax();
                }
                GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsDetail.marketingGoodsChildren.get(0).platformPrice,
                        goodsDetail.marketingGoodsChildren.get(0).retailPrice,
                        goodsDetail.marketingGoodsChildren.get(0).getPlusPrice(),
                        goodsDetail.goodsType,
                        goodsDetail.isPlusOnly, newStoreDetialModelSelect.isSupportOverSold, goodsDetail.goodsChildren.get(0).barcodeSku);
                if (goodsMarketing == null) {
                    goodsBasketCell = new GoodsBasketCell(goodsDetail.goodsChildren.get(0).platformPrice,
                            goodsDetail.goodsChildren.get(0).retailPrice,
                            goodsDetail.goodsChildren.get(0).getPlusPrice(),
                            goodsDetail.goodsType,
                            goodsDetail.isPlusOnly, newStoreDetialModelSelect.isSupportOverSold, goodsDetail.goodsChildren.get(0).barcodeSku);
                }
                goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                goodsBasketCell.mchId = goodsDetail.userId + "";
                goodsBasketCell.goodsId = goodsDetail.id;
                goodsBasketCell.goodsStock = goodsDetail.goodsChildren.get(0).getAvailableInventory();
                goodsBasketCell.setGoodsSpecId(goodsDetail.marketingGoodsChildren.get(0).goodsChildId);
                goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
                goodsBasketCell.goodsImage = goodsDetail.headImage;
                goodsBasketCell.setGoodsQuantity(goodsDetail.getMarketingSalesMin());
                GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                List<GoodsBasketCell> list = new ArrayList<>();
                goodsBasketCell.shopIdList = goodsDetail.getGoodsShopIdListStringArray();
                goodsBasketCell.ischeck = true;
                goodsBasketCell.goodsShopId = newStoreDetialModelSelect.id;
                goodsBasketCell.goodsShopName = newStoreDetialModelSelect.shopName;
                goodsBasketCell.goodsShopAddress = newStoreDetialModelSelect.addressDetails;
                //goodsBasketCell.setDiscountTopModel(goodsDetail.discountTopModel);
                list.add(goodsBasketCell);


                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDER)
                        .withString("visitShopId", shopId)
                        .withString("token", mFloatingView == null ? null : token)
                        .withString("course_id", mFloatingView == null ? null : course_id)
                        .withString("liveStatus", mFloatingView == null ? null : liveStatus)
                        .withObject("goodsbasketlist", list)
                        .withObject("actVip", goodsDetail.actVip)
                        .navigation();
            }
            return;
        }
        if (goodsSpecDialog == null) {
            goodsSpecDialog = GoodsSpecDialog.newInstance();
        }
        goodsSpecDialog.setVipInfo(mvipInfo);
        goodsSpecDialog.show(getSupportFragmentManager(), "优惠券");
        if (goodsSpecCells != null) {//2021/5/17 为了切换门店去掉这块判断
            goodsSpecDialog.setMarketing("8".equals(goodsDetail.marketingType) && !SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false) ? null : goodsDetail.marketingType, goodsDetail.mapMarketingGoodsId);
            //////System.out.println("设置规格");
            goodsSpecDialog.setGoodsId(goodsDetail.id, goodsShopId);
            goodsSpecDialog.setGoodsSpecDetail(goodsSpecDetail);
            goodsSpecDialog.initSpec(goodsSpecCells);
            goodsSpecDialog.setMode(isrightbuy ? 1 : 2);
            goodsSpecDialog.setOnSpecSubmitListener(new GoodsSpecDialog.OnSpecSubmitListener() {
                @Override
                public void onSpecSubmit(GoodsSpecDetail goodsSpecDetail) {
                    if ("8".equals(goodsSpecDetail.marketingType) && "1".equals(goodsDetail.isPlusOnly) && !SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false)) {//专享且非会员
                        Toast.makeText(mContext, "本商品为PLUS专享商品,您还不是PLUS会员。", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!isrightbuy) {//加入购物车
                        AnimManager animManager = new AnimManager.Builder()
                                .with(ServiceGoodsDetail.this)
                                .startView(goBask)
                                .endView(imgBasket)
                                .imageUrl(R.drawable.basket_red)
                                .build();
                        animManager.startAnim();
                        serviceGoodsDetailPresenter.addGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                                .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                                .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                                .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                                .puts("goodsSource", "1")
                                .puts("shopId", shopId)
                                .puts("goodsShopId", goodsShopId)
                                .puts("packageType", "SINGLE_PACKAGE")
                                .puts("goodsType", goodsDetail.goodsType + "")
                                .puts("goodsId", goodsDetail.id + "")
                                .puts("goodsSpecId", goodsSpecDetail.getGoodsSpec())
                                .puts("goodsQuantity", goodsSpecDetail.getCount())
                        );
                        if (mallGoodsDetialSelectAdapter.getModel() == null) {

                        }
                    } else {
                        GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(goodsSpecDetail.marketingId);
                        if ("8".equals(goodsSpecDetail.marketingType) && !"1".equals(goodsDetail.isPlusOnly) && !SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false)) {//是专享活动但是没开专享开关并且是会员  要走普通商品
                            goodsMarketing = null;
                        } else if (goodsSpecDetail.marketingType == null) {
                            goodsMarketing = null;
                        } else {
                            goodsMarketing.availableInventory = goodsSpecDetail.getAvailableInventoryMark();
                            goodsMarketing.mapMarketingGoodsId = goodsSpecDetail.mapMarketingGoodsId;
                            goodsMarketing.marketingType = goodsSpecDetail.marketingType;
                            goodsMarketing.id = goodsSpecDetail.getgoodsMarketingGoodsSpec();
                            goodsMarketing.marketingPrice = goodsSpecDetail.marketingPrice;
                            goodsMarketing.pointsPrice = goodsSpecDetail.pointsPrice;
                            goodsMarketing.salesMin = goodsDetail.getMarketingSalesMin();
                            goodsMarketing.salesMax = goodsDetail.getMarketingSalesMax();
                        }
                        GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsSpecDetail.platformPrice,
                                goodsSpecDetail.retailPrice, goodsSpecDetail.getPlusPrice(),
                                goodsDetail.goodsType,
                                goodsSpecDetail.isPlusOnly,
                                newStoreDetialModelSelect.isSupportOverSold, goodsSpecDetail.barcodeSku);
                        goodsBasketCell.goodsSpecDesc = goodsSpecDetail.goodsSpecStr;
                        goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                        goodsBasketCell.mchId = goodsDetail.userId + "";
                        goodsBasketCell.goodsId = goodsDetail.id;
                        goodsBasketCell.goodsStock = goodsSpecDetail.getRealAvailableInventory();
                        goodsBasketCell.setGoodsSpecId(goodsSpecDetail.getGoodsSpec());
                        goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
                        goodsBasketCell.goodsImage = goodsDetail.headImage;
                        goodsBasketCell.setGoodsQuantity(Integer.parseInt(goodsSpecDetail.getCount()));
                        GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                        List<GoodsBasketCell> list = new ArrayList<>();
                        goodsBasketCell.shopIdList = goodsDetail.getGoodsShopIdListStringArray();
                        goodsBasketCell.ischeck = true;
                        goodsBasketCell.goodsShopId = newStoreDetialModelSelect.id;
                        goodsBasketCell.goodsShopName = newStoreDetialModelSelect.shopName;
                        goodsBasketCell.goodsShopAddress = newStoreDetialModelSelect.addressDetails;
                        //  goodsBasketCell.setDiscountTopModel(goodsSpecDetail.marketingType == null ? null : goodsDetail.discountTopModel);
                        list.add(goodsBasketCell);
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_ORDER)
                                .withString("visitShopId", shopId)
                                .withString("token", mFloatingView == null ? null : token)
                                .withString("course_id", mFloatingView == null ? null : course_id)
                                .withString("liveStatus", mFloatingView == null ? null : liveStatus)
                                .withObject("goodsbasketlist", list)
                                .withObject("actVip", goodsDetail.actVip)
                                .navigation();
                    }
                }
            });
        }
    }

    private void showSkuNt(final boolean isrightbuy) {
        if (isZero) {
            showToast("该商品已售罄，看看其他商品吧~");
            return;
        }
        if (newStoreDetialModelSelect == null) {
            Toast.makeText(mContext, "商品未配置服务门店请联系商家", Toast.LENGTH_LONG).show();
            return;
        }
        if ("2".equals(goodsDetail.goodsType) && !"5".equals(goodsDetail.marketingType)) {

        } else {
            if (newStoreDetialModelSelect == null) {
                Toast.makeText(mContext, "服务商品未配置服务门店请联系商家", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (goodsDetail.cheIsNoSku()) {
            if (!isrightbuy) {//加入购物车
                AnimManager animManager = new AnimManager.Builder()
                        .with(ServiceGoodsDetail.this)
                        .startView(goBask)
                        .endView(imgBasket)
                        .imageUrl(R.drawable.basket_red)
                        .build();
                animManager.startAnim();
                serviceGoodsDetailPresenter.addGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                        .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                        .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                        .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                        .puts("goodsSource", "1")
                        .puts("packageType", "SINGLE_PACKAGE")
                        .puts("shopId", shopId)
                        .puts("goodsShopId", goodsShopId)
                        .puts("goodsType", goodsDetail.goodsType + "")
                        .puts("goodsId", goodsDetail.id)
                        .puts("goodsSpecId", ("1".equals(isNtReal) ? goodsDetail.marketingGoodsChildren.get(0).goodsChildId : goodsDetail.goodsChildren.get(0).id) + "")
                        .puts("goodsQuantity", "1")
                );
                if (mallGoodsDetialSelectAdapter.getModel() == null) {

                }
            } else {
                GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(goodsDetail.marketingId);
                if ("1".equals(isNtReal) && "4".equals(goodsDetail.marketingType)) {
                    goodsMarketing.availableInventory = goodsDetail.marketingGoodsChildren.get(0).getAvailableInventoryMark(goodsDetail.goodsChildren.get(0));
                    goodsMarketing.mapMarketingGoodsId = goodsDetail.mapMarketingGoodsId;
                    goodsMarketing.marketingType = goodsDetail.marketingType;
                    goodsMarketing.id = goodsDetail.marketingGoodsChildren.get(0).id;
                    goodsMarketing.marketingPrice = goodsDetail.marketingGoodsChildren.get(0).marketingPrice;
                    goodsMarketing.pointsPrice = goodsSpecDetail.pointsPrice;
                    goodsMarketing.salesMin = goodsDetail.getMarketingSalesMin();
                    goodsMarketing.salesMax = goodsDetail.getMarketingSalesMax();
                } else {
                    goodsMarketing = null;
                }
                GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsDetail.goodsChildren.get(0).platformPrice,
                        goodsDetail.goodsChildren.get(0).retailPrice,
                        goodsDetail.goodsChildren.get(0).getPlusPrice(),
                        goodsDetail.goodsType,
                        goodsDetail.isPlusOnly,
                        newStoreDetialModelSelect.isSupportOverSold, goodsDetail.goodsChildren.get(0).barcodeSku);

                if ("1".equals(isNtReal) && "4".equals(goodsDetail.marketingType)) {
                    //新客走向
                    goodsBasketCell.goodsStock = 1;
                } else {
                    //不是新客
                    goodsBasketCell.goodsStock = goodsDetail.goodsChildren.get(0).getAvailableInventory();
                }
                goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                goodsBasketCell.mchId = goodsDetail.userId + "";
                goodsBasketCell.goodsId = goodsDetail.id;
                goodsBasketCell.setGoodsSpecId(("1".equals(isNtReal) ? goodsDetail.marketingGoodsChildren.get(0).goodsChildId + "" : goodsDetail.goodsChildren.get(0).id + ""));
                goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
                goodsBasketCell.goodsImage = goodsDetail.headImage;
                goodsBasketCell.setGoodsQuantity(Integer.parseInt("1"));
                GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                List<GoodsBasketCell> list = new ArrayList<>();
                goodsBasketCell.shopIdList = goodsDetail.getGoodsShopIdListStringArray();
                goodsBasketCell.ischeck = true;
                goodsBasketCell.goodsShopId = newStoreDetialModelSelect.id;
                goodsBasketCell.goodsShopName = newStoreDetialModelSelect.shopName;
                goodsBasketCell.goodsShopAddress = newStoreDetialModelSelect.addressDetails;
                list.add(goodsBasketCell);
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDER)
                        .withString("visitShopId", shopId)
                        .withString("token", mFloatingView == null ? null : token)
                        .withString("course_id", mFloatingView == null ? null : course_id)
                        .withString("liveStatus", mFloatingView == null ? null : liveStatus)
                        .withObject("goodsbasketlist", list)
                        .withObject("actVip", goodsDetail.actVip)
                        .navigation();
            }
            return;
        }
        if (goodsSpecDialog == null) {
            goodsSpecDialog = GoodsSpecDialog.newInstance();
        }
        goodsSpecDialog.setNtReal("1".equals(isNtReal) ? true : false);
        goodsSpecDialog.setMarketing(goodsDetail.marketingType, goodsDetail.mapMarketingGoodsId);
        goodsSpecDialog.show(getSupportFragmentManager(), "优惠券");
        if (goodsSpecCells != null) {
            goodsSpecDialog.setMarketing(goodsDetail.marketingType, goodsDetail.mapMarketingGoodsId);
            goodsSpecDialog.setGoodsId(goodsDetail.id, goodsShopId);
            goodsSpecDialog.setGoodsSpecDetail(goodsSpecDetail);
            goodsSpecDialog.initSpec(goodsSpecCells);
            goodsSpecDialog.setOnSpecSubmitListener(new GoodsSpecDialog.OnSpecSubmitListener() {
                @Override
                public void onSpecSubmit(GoodsSpecDetail goodsSpecDetail) {
                    if (!isrightbuy) {
                        AnimManager animManager = new AnimManager.Builder()
                                .with(ServiceGoodsDetail.this)
                                .startView(goBask)
                                .endView(imgBasket)
                                .imageUrl(R.drawable.basket_red)
                                .build();
                        animManager.startAnim();
                        serviceGoodsDetailPresenter.addGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                                .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                                .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                                .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                                .puts("goodsSource", "1")
                                .puts("shopId", shopId)
                                .puts("goodsShopId", goodsShopId)
                                .puts("packageType", "SINGLE_PACKAGE")
                                .puts("goodsType", goodsDetail.goodsType + "")
                                .puts("goodsId", goodsDetail.id + "")
                                .puts("goodsSpecId", goodsSpecDetail.getGoodsChildId() + "")
                                .puts("goodsQuantity", goodsSpecDetail.count)
                        );
                        if (mallGoodsDetialSelectAdapter.getModel() == null) {

                        }
                    } else {
                        GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(goodsSpecDetail.marketingId);
                        if ("1".equals(isNtReal) && "4".equals(goodsSpecDetail.marketingType)) {
                            goodsMarketing.availableInventory = goodsSpecDetail.getAvailableInventoryMark();
                            goodsMarketing.mapMarketingGoodsId = goodsSpecDetail.mapMarketingGoodsId;
                            goodsMarketing.marketingType = goodsDetail.marketingType;
                            goodsMarketing.id = goodsSpecDetail.getgoodsMarketingGoodsSpec();
                            goodsMarketing.marketingPrice = goodsSpecDetail.marketingPrice;
                            goodsMarketing.pointsPrice = goodsSpecDetail.pointsPrice;
                            goodsMarketing.salesMin = goodsDetail.getMarketingSalesMin();
                            goodsMarketing.salesMax = goodsDetail.getMarketingSalesMax();
                        } else {
                            goodsMarketing = null;
                        }
                        GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsSpecDetail.platformPrice,
                                goodsSpecDetail.retailPrice, goodsSpecDetail.getPlusPrice(),
                                goodsDetail.goodsType,
                                goodsSpecDetail.isPlusOnly,
                                newStoreDetialModelSelect.isSupportOverSold, goodsSpecDetail.barcodeSku);
                        goodsBasketCell.goodsSpecDesc = goodsSpecDetail.goodsSpecStr;
                        goodsBasketCell.goodsStock = goodsSpecDetail.getRealAvailableInventory();
                        goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                        goodsBasketCell.mchId = goodsDetail.userId + "";
                        goodsBasketCell.goodsId = goodsDetail.id;
                        goodsBasketCell.setGoodsSpecId("1".equals(isNtReal) ? goodsSpecDetail.getGoodsSpec() : goodsSpecDetail.getGoodsChildId());
                        goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
                        goodsBasketCell.goodsImage = goodsDetail.headImage;
                        goodsBasketCell.setGoodsQuantity(Integer.parseInt(goodsSpecDetail.getCount()));
                        GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                        List<GoodsBasketCell> list = new ArrayList<>();
                        goodsBasketCell.shopIdList = goodsDetail.getGoodsShopIdListStringArray();
                        goodsBasketCell.ischeck = true;
                        goodsBasketCell.goodsShopId = newStoreDetialModelSelect.id;
                        goodsBasketCell.goodsShopName = newStoreDetialModelSelect.shopName;
                        goodsBasketCell.goodsShopAddress = newStoreDetialModelSelect.addressDetails;
                        list.add(goodsBasketCell);
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_ORDER)
                                .withString("visitShopId", shopId)
                                .withString("token", mFloatingView == null ? null : token)
                                .withString("course_id", mFloatingView == null ? null : course_id)
                                .withString("liveStatus", mFloatingView == null ? null : liveStatus)
                                .withObject("goodsbasketlist", list)
                                .withObject("actVip", goodsDetail.actVip)
                                .navigation();
                    }
                }
            });
        }

    }

    private void showSkuKick(final boolean isrightbuy) {
        if (isZero) {
            showToast("该商品已售罄，看看其他商品吧~");
            return;
        }
        if (newStoreDetialModelSelect == null) {
            Toast.makeText(mContext, "商品未配置服务门店请联系商家", Toast.LENGTH_LONG).show();
            return;
        }
        if ("2".equals(goodsDetail.goodsType) && !"5".equals(goodsDetail.marketingType)) {

        } else {
            if (newStoreDetialModelSelect == null) {
                Toast.makeText(mContext, "服务商品未配置服务门店请联系商家", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (goods2ModelKick == null) {
            showToast("商品数据载入中~");
            return;
        }
        if (goods2ModelKick.bargainInfoDTO == null) {
            showToast("商品数据载入中~");
            return;
        }
        if (goodsDetail.cheIsNoSku()) {
            if (isrightbuy) {//直接砍价
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_KICKDETAIL)
                        .withString("bargainId", bargainId + "")
                        .withString("marketingShopId", shopId)
                        .withString("deliveryShopId", goodsShopId)
                        .withString("bargainMemberId", bargainMemberId)
                        .withString("marketingGoodsChildId", (goods2ModelKick.bargainInfoDTO.marketingGoodsChildDTOS != null && goods2ModelKick.bargainInfoDTO.marketingGoodsChildDTOS.size() > 0) ? goods2ModelKick.bargainInfoDTO.marketingGoodsChildDTOS.get(0).id : goodsDetail.goodsChildren.get(0).id)
                        .navigation();
                finish();
            } else {//单独买
                GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(goodsDetail.marketingId);
                goodsMarketing = null;
                GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsDetail.goodsChildren.get(0).platformPrice,
                        goodsDetail.goodsChildren.get(0).retailPrice, goodsDetail.goodsChildren.get(0).getPlusPrice(),
                        goodsDetail.goodsType,
                        goodsDetail.isPlusOnly,
                        newStoreDetialModelSelect.isSupportOverSold, goodsDetail.goodsChildren.get(0).barcodeSku);
                goodsBasketCell.goodsStock = 999999;
                goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                goodsBasketCell.mchId = goodsDetail.userId + "";
                goodsBasketCell.goodsId = goodsDetail.id;
                goodsBasketCell.setGoodsSpecId(goodsDetail.goodsChildren.get(0).id);
                goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
                goodsBasketCell.goodsImage = goodsDetail.headImage;
                goodsBasketCell.setGoodsQuantity(Integer.parseInt("1"));
                GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                List<GoodsBasketCell> list = new ArrayList<>();
                goodsBasketCell.shopIdList = goodsDetail.getGoodsShopIdListStringArray();
                goodsBasketCell.ischeck = true;
                goodsBasketCell.goodsShopId = newStoreDetialModelSelect.id;
                goodsBasketCell.goodsShopName = newStoreDetialModelSelect.shopName;
                goodsBasketCell.goodsShopAddress = newStoreDetialModelSelect.addressDetails;
                list.add(goodsBasketCell);
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDER)
                        .withString("visitShopId", shopId)
                        .withObject("goodsbasketlist", list)
                        .withObject("actVip", goodsDetail.actVip)
                        .navigation();
            }
            return;
        }
        if (goodsSpecDialog == null) {
            goodsSpecDialog = GoodsSpecDialog.newInstance();
        }
        goodsSpecDialog.setSkuListEx(exSkuList);
        goodsSpecDialog.show(getSupportFragmentManager(), "优惠券");
        goodsSpecDialog.setMode(isrightbuy ? 2 : 1);
        goodsSpecDialog.setSingleSelectAct(isrightbuy);
        goodsSpecDialog.setMarketing(goodsDetail.marketingType, goodsDetail.mapMarketingGoodsId);
        if (goodsSpecCells != null) {
            goodsSpecDialog.setGoodsId(goodsDetail.id, goodsShopId);
            goodsSpecDialog.setGoodsSpecDetail(goodsSpecDetail);
            goodsSpecDialog.initSpec(goodsSpecCells);
            goodsSpecDialog.setOnSpecSubmitListener(new GoodsSpecDialog.OnSpecSubmitListener() {
                @Override
                public void onSpecSubmit(GoodsSpecDetail goodsSpecDetail) {
                    if (isrightbuy && goodsSpecDetail.marketingType != null) {//选的活动商品 直接去砍价
                        ARouter.getInstance()
                                .build(DiscountRoutes.DIS_KICKDETAIL)
                                .withString("bargainId", bargainId + "")
                                .withString("bargainMemberId", bargainMemberId)
                                .withString("marketingGoodsChildId", goodsSpecDetail.id)
                                .withString("marketingShopId", shopId)
                                .withString("deliveryShopId", goodsShopId)
                                .navigation();
                        finish();
                    } else {
                        GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(goodsDetail.marketingId);
                        goodsMarketing = null;
                        GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsSpecDetail.platformPrice,
                                goodsSpecDetail.retailPrice,
                                goodsSpecDetail.getPlusPrice(),
                                goodsDetail.goodsType,
                                goodsSpecDetail.isPlusOnly,
                                newStoreDetialModelSelect.isSupportOverSold, goodsSpecDetail.barcodeSku);
                        goodsBasketCell.goodsSpecDesc = goodsSpecDetail.goodsSpecStr;
                        goodsBasketCell.goodsStock = goodsSpecDetail.getAvailableInventory();
                        goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                        goodsBasketCell.mchId = goodsDetail.userId + "";
                        goodsBasketCell.goodsId = goodsDetail.id;
                        goodsBasketCell.setGoodsSpecId(goodsSpecDetail.id);
                        goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
                        goodsBasketCell.goodsImage = goodsDetail.headImage;
                        goodsBasketCell.setGoodsQuantity(Integer.parseInt(goodsSpecDetail.getCount()));
                        GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                        List<GoodsBasketCell> list = new ArrayList<>();
                        goodsBasketCell.shopIdList = goodsDetail.getGoodsShopIdListStringArray();
                        goodsBasketCell.ischeck = true;
                        goodsBasketCell.goodsShopId = newStoreDetialModelSelect.id;
                        goodsBasketCell.goodsShopName = newStoreDetialModelSelect.shopName;
                        goodsBasketCell.goodsShopAddress = newStoreDetialModelSelect.addressDetails;
                        list.add(goodsBasketCell);
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_ORDER)
                                .withString("visitShopId", shopId)
                                .withObject("goodsbasketlist", list)
                                .withObject("actVip", goodsDetail.actVip)
                                .navigation();
                    }
                }
            });
        }
    }

    private void showSkuPin(final boolean isrightbuy) {
        if (isZero) {
            showToast("该商品已售罄，看看其他商品吧~");
            return;
        }
        if (goods2ModelPin == null || goods2ModelPin.assembleDTO == null) {
            return;
        }
        try {
            if (isrightbuy && goods2ModelPin.assembleDTO.assembleInventory == 0) {
                showToast("该商品已售罄，看看其他商品吧~");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newStoreDetialModelSelect == null) {
            Toast.makeText(mContext, "商品未配置服务门店请联系商家", Toast.LENGTH_LONG).show();
            return;
        }
        if ("2".equals(goodsDetail.goodsType) && !"5".equals(goodsDetail.marketingType)) {

        } else {
            if (newStoreDetialModelSelect == null) {
                Toast.makeText(mContext, "服务商品未配置服务门店请联系商家", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (goodsDetail.cheIsNoSku()) {
            if (isrightbuy) {//参加拼团
                String singleNumber = new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)) + new Date().getTime();
                GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(goodsDetail.marketingId);
                goodsMarketing.availableInventory = (goods2ModelPin.assembleDTO.marketingGoodsChildDTOS != null && goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.size() > 0) ? goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.get(0).availableInventory : 0;
                goodsMarketing.mapMarketingGoodsId = (goods2ModelPin.assembleDTO.marketingGoodsChildDTOS != null && goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.size() > 0) ? goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.get(0).mapMarketingGoodsId : "0";
                goodsMarketing.marketingType = "2";
                goodsMarketing.id = ((goods2ModelPin.assembleDTO.marketingGoodsChildDTOS != null && goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.size() > 0) ? goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.get(0).id : null);
                goodsMarketing.marketingPrice = ((goods2ModelPin.assembleDTO.marketingGoodsChildDTOS != null && goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.size() > 0) ? goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.get(0).marketingPrice : 0);
                goodsMarketing.pointsPrice = goodsSpecDetail.pointsPrice;
                goodsMarketing.salesMin = goodsDetail.getMarketingSalesMin();
                goodsMarketing.salesMax = goodsDetail.getMarketingSalesMax();
                GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.get(0).platformPrice,
                        goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.get(0).platformPrice,
                        0,
                        goodsDetail.goodsType,
                        goodsDetail.isPlusOnly,
                        newStoreDetialModelSelect.isSupportOverSold, goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.get(0).barcodeSku);
                goodsBasketCell.goodsStock = 1;
                goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                goodsBasketCell.mchId = goodsDetail.userId;
                goodsBasketCell.goodsId = goodsDetail.id;
                goodsBasketCell.setGoodsSpecId(((goods2ModelPin.assembleDTO.marketingGoodsChildDTOS != null && goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.size() > 0) ? goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.get(0).goodsChildId : "0"));
                goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
                goodsBasketCell.goodsImage = goodsDetail.headImage;
                goodsBasketCell.setGoodsQuantity(Integer.parseInt("1"));
                GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                List<GoodsBasketCell> list = new ArrayList<>();
                goodsBasketCell.shopIdList = goodsDetail.getGoodsShopIdListStringArray();
                goodsBasketCell.ischeck = true;
                goodsBasketCell.goodsShopId = newStoreDetialModelSelect.id;
                goodsBasketCell.goodsShopName = newStoreDetialModelSelect.shopName;
                goodsBasketCell.goodsShopAddress = newStoreDetialModelSelect.addressDetails;
                list.add(goodsBasketCell);
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDER)
                        .withString("visitShopId", shopId)
                        .withString("assembleId", assembleId)
                        .withString("teamNum", (TextUtils.isEmpty(teamNum) ? singleNumber : teamNum) + "")
                        .withString("assemblePrice", goods2ModelPin.assembleDTO.assemblePrice + "")
                        .withObject("goodsbasketlist", list)
                        .withObject("actVip", goodsDetail.actVip)
                        .navigation();
            } else {//单独买
                GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(goodsDetail.marketingId);
                goodsMarketing = null;
                GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsDetail.goodsChildren.get(0).platformPrice,
                        goodsDetail.goodsChildren.get(0).retailPrice,
                        goodsDetail.goodsChildren.get(0).getPlusPrice(),
                        goodsDetail.goodsType,
                        goodsDetail.isPlusOnly,
                        newStoreDetialModelSelect.isSupportOverSold, goodsDetail.goodsChildren.get(0).barcodeSku);
                goodsBasketCell.goodsStock = goodsDetail.goodsChildren.get(0).getAvailableInventory();
                goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                goodsBasketCell.mchId = goodsDetail.userId + "";
                goodsBasketCell.goodsId = goodsDetail.id;
                goodsBasketCell.setGoodsSpecId(goodsDetail.goodsChildren.get(0).id);
                goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
                goodsBasketCell.goodsImage = goodsDetail.headImage;
                goodsBasketCell.setGoodsQuantity(Integer.parseInt("1"));
                GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                List<GoodsBasketCell> list = new ArrayList<>();
                goodsBasketCell.shopIdList = goodsDetail.getGoodsShopIdListStringArray();
                goodsBasketCell.ischeck = true;
                goodsBasketCell.goodsShopId = newStoreDetialModelSelect.id;
                goodsBasketCell.goodsShopName = newStoreDetialModelSelect.shopName;
                goodsBasketCell.goodsShopAddress = newStoreDetialModelSelect.addressDetails;
                list.add(goodsBasketCell);
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDER)
                        .withString("visitShopId", shopId)
                        .withString("token", mFloatingView == null ? null : token)
                        .withString("course_id", mFloatingView == null ? null : course_id)
                        .withString("liveStatus", mFloatingView == null ? null : liveStatus)
                        .withObject("goodsbasketlist", list)
                        .withObject("actVip", goodsDetail.actVip)
                        .navigation();
            }
            return;
        }
        if (goodsSpecDialog == null) {
            goodsSpecDialog = GoodsSpecDialog.newInstance();
        }
        goodsSpecDialog.setSkuListEx(exSkuList);
        goodsSpecDialog.show(getSupportFragmentManager(), "优惠券");
        goodsSpecDialog.setMode(isrightbuy ? 2 : 1);
        goodsSpecDialog.setSkuListEx(exSkuList);
        goodsSpecDialog.setSingleSelectAct(isrightbuy);
        if (goodsSpecCells != null) {
            goodsSpecDialog.setGoodsId(goodsDetail.id, goodsShopId);
            goodsSpecDialog.setGoodsSpecDetail(goodsSpecDetail);
            goodsSpecDialog.setRegimentSize(goods2ModelPin.assembleDTO.regimentSize);
            goodsSpecDialog.initSpec(goodsSpecCells);
            goodsSpecDialog.setOnSpecSubmitListener(new GoodsSpecDialog.OnSpecSubmitListener() {
                @Override
                public void onSpecSubmit(GoodsSpecDetail goodsSpecDetail) {
                    if (isrightbuy && goodsSpecDetail.marketingType != null) {//可以参加拼团
                        String singleNumber = new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)) + new Date().getTime();
                        GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(goodsSpecDetail.marketingId);
                        if (goodsSpecDetail.marketingType == null) {
                            goodsMarketing = null;
                        } else {
                            goodsMarketing.availableInventory = goodsSpecDetail.getAvailableInventory();
                            goodsMarketing.mapMarketingGoodsId = goodsSpecDetail.mapMarketingGoodsId;
                            goodsMarketing.marketingType = goodsSpecDetail.marketingType;
                            goodsMarketing.id = goodsSpecDetail.id;
                            goodsMarketing.marketingPrice = goodsSpecDetail.marketingPrice;
                            goodsMarketing.pointsPrice = goodsSpecDetail.pointsPrice;
                            goodsMarketing.salesMin = goodsDetail.getMarketingSalesMin();
                            goodsMarketing.salesMax = goodsDetail.getMarketingSalesMax();
                        }
                        GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsSpecDetail.platformPrice,
                                goodsSpecDetail.retailPrice,
                                goodsSpecDetail.getPlusPrice(),
                                goodsDetail.goodsType,
                                goodsSpecDetail.isPlusOnly,
                                newStoreDetialModelSelect.isSupportOverSold, goodsSpecDetail.barcodeSku);
                        goodsBasketCell.goodsSpecDesc = goodsSpecDetail.goodsSpecStr;
                        goodsBasketCell.goodsStock = goodsSpecDetail.getRealAvailableInventory();
                        goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                        goodsBasketCell.mchId = goodsDetail.userId + "";
                        goodsBasketCell.goodsId = goodsDetail.id;
                        goodsBasketCell.setGoodsSpecId(goodsSpecDetail.getGoodsChildId());
                        goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
                        goodsBasketCell.goodsImage = goodsDetail.headImage;
                        goodsBasketCell.setGoodsQuantity(Integer.parseInt(goodsSpecDetail.getCount()));
                        GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                        List<GoodsBasketCell> list = new ArrayList<>();
                        goodsBasketCell.shopIdList = goodsDetail.getGoodsShopIdListStringArray();
                        goodsBasketCell.ischeck = true;
                        goodsBasketCell.goodsShopId = newStoreDetialModelSelect.id;
                        goodsBasketCell.goodsShopName = newStoreDetialModelSelect.shopName;
                        goodsBasketCell.goodsShopAddress = newStoreDetialModelSelect.addressDetails;
                        list.add(goodsBasketCell);
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_ORDER)
                                .withString("visitShopId", shopId)
                                .withString("assembleId", assembleId)
                                .withString("teamNum", (TextUtils.isEmpty(teamNum) ? singleNumber : teamNum) + "")
                                .withString("assemblePrice", goodsSpecDetail.getMarketingPrice() + "")
                                .withObject("goodsbasketlist", list)
                                .withString("goodsMarketingType", goodsSpecDetail.marketingType)
                                .withObject("actVip", goodsDetail.actVip)
                                .navigation();
                    } else {//单独买
                        GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(goodsDetail.marketingId);
                        goodsMarketing = null;
                        GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsSpecDetail.platformPrice,
                                goodsSpecDetail.retailPrice,
                                goodsSpecDetail.getPlusPrice(),
                                goodsDetail.goodsType,
                                goodsSpecDetail.isPlusOnly,
                                newStoreDetialModelSelect.isSupportOverSold, goodsSpecDetail.barcodeSku);
                        goodsBasketCell.goodsSpecDesc = goodsSpecDetail.goodsSpecStr;
                        goodsBasketCell.goodsStock = goodsSpecDetail.getRealAvailableInventory();
                        goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                        goodsBasketCell.mchId = goodsDetail.userId + "";
                        goodsBasketCell.goodsId = goodsDetail.id;
                        goodsBasketCell.setGoodsSpecId(goodsSpecDetail.id);
                        goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
                        goodsBasketCell.goodsImage = goodsDetail.headImage;
                        goodsBasketCell.setGoodsQuantity(Integer.parseInt(goodsSpecDetail.getCount()));
                        GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                        List<GoodsBasketCell> list = new ArrayList<>();
                        goodsBasketCell.shopIdList = goodsDetail.getGoodsShopIdListStringArray();
                        goodsBasketCell.ischeck = true;
                        goodsBasketCell.goodsShopId = newStoreDetialModelSelect.id;
                        goodsBasketCell.goodsShopName = newStoreDetialModelSelect.shopName;
                        goodsBasketCell.goodsShopAddress = newStoreDetialModelSelect.addressDetails;
                        list.add(goodsBasketCell);
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_ORDER)
                                .withString("visitShopId", shopId)
                                .withString("token", mFloatingView == null ? null : token)
                                .withString("course_id", mFloatingView == null ? null : course_id)
                                .withString("liveStatus", mFloatingView == null ? null : liveStatus)
                                .withObject("goodsbasketlist", list)
                                .withObject("actVip", goodsDetail.actVip)
                                .navigation();
                    }
                }
            });
        }
    }


    private void showSku(final boolean isrightbuy) {
        if (isZero) {
            showToast("该商品已售罄，看看其他商品吧~");
            return;
        }
        if (newStoreDetialModelSelect == null) {
            Toast.makeText(mContext, "商品未配置服务门店请联系商家", Toast.LENGTH_LONG).show();
            return;
        }
        if ("2".equals(goodsDetail.goodsType) && !"5".equals(goodsDetail.marketingType)) {

        } else {
            if (newStoreDetialModelSelect == null) {
                Toast.makeText(mContext, "服务商品未配置服务门店请联系商家", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (goodsDetail.cheIsNoSku()) {
            if (!isrightbuy) {//购物车
                AnimManager animManager = new AnimManager.Builder()
                        .with(ServiceGoodsDetail.this)
                        .startView(goBask)
                        .endView(imgBasket)
                        .imageUrl(R.drawable.basket_red)
                        .build();
                animManager.startAnim();
                serviceGoodsDetailPresenter.addGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                        .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                        .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                        .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                        .puts("goodsSource", "1")
                        .puts("packageType", "SINGLE_PACKAGE")
                        .puts("shopId", shopId)
                        .puts("goodsShopId", goodsShopId)
                        .puts("goodsId", goodsDetail.id + "")
                        .puts("goodsSpecId", goodsDetail.goodsChildren.get(0).id + "")
                        .puts("goodsQuantity", "1")
                        .puts("goodsType", goodsDetail.goodsType + "")
                );
                if (mallGoodsDetialSelectAdapter.getModel() == null) {

                }
            } else {//直接下单
                GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(goodsDetail.marketingId);
                goodsMarketing = null;
                GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsDetail.goodsChildren.get(0).platformPrice,
                        goodsDetail.goodsChildren.get(0).retailPrice, goodsDetail.goodsChildren.get(0).getPlusPrice(),
                        goodsDetail.goodsType,
                        goodsDetail.isPlusOnly,
                        newStoreDetialModelSelect.isSupportOverSold, goodsDetail.goodsChildren.get(0).barcodeSku);
                goodsBasketCell.goodsStock = goodsDetail.goodsChildren.get(0).getAvailableInventory();
                goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                goodsBasketCell.mchId = goodsDetail.userId + "";
                goodsBasketCell.goodsId = goodsDetail.id;
                goodsBasketCell.setGoodsSpecId(goodsDetail.goodsChildren.get(0).id);
                goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
                goodsBasketCell.goodsImage = goodsDetail.headImage;
                goodsBasketCell.setGoodsQuantity(Integer.parseInt("1"));
                GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                List<GoodsBasketCell> list = new ArrayList<>();
                goodsBasketCell.shopIdList = goodsDetail.getGoodsShopIdListStringArray();
                goodsBasketCell.ischeck = true;
                goodsBasketCell.goodsShopId = newStoreDetialModelSelect.id;
                goodsBasketCell.goodsShopName = newStoreDetialModelSelect.shopName;
                goodsBasketCell.goodsShopAddress = newStoreDetialModelSelect.addressDetails;
                list.add(goodsBasketCell);
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDER)
                        .withString("visitShopId", shopId)
                        .withString("token", mFloatingView == null ? null : token)
                        .withString("course_id", mFloatingView == null ? null : course_id)
                        .withString("liveStatus", mFloatingView == null ? null : liveStatus)
                        .withObject("goodsbasketlist", list)
                        .withObject("actVip", goodsDetail.actVip)
                        .navigation();
            }
            return;
        }
        if (goodsSpecDialog == null) {
            goodsSpecDialog = GoodsSpecDialog.newInstance();
        }
        goodsSpecDialog.show(getSupportFragmentManager(), "优惠券");
        if (goodsSpecCells != null) {
            goodsSpecDialog.setMarketing(null, null);//强制普通商品
            goodsSpecDialog.setGoodsId(goodsDetail.id, goodsShopId);
            goodsSpecDialog.setGoodsSpecDetail(goodsSpecDetail);
            goodsSpecDialog.initSpec(goodsSpecCells);
            goodsSpecDialog.setOnSpecSubmitListener(new GoodsSpecDialog.OnSpecSubmitListener() {
                @Override
                public void onSpecSubmit(GoodsSpecDetail goodsSpecDetail) {
                    if (!isrightbuy) {//购物车
                        AnimManager animManager = new AnimManager.Builder()
                                .with(ServiceGoodsDetail.this)
                                .startView(goBask)
                                .endView(imgBasket)
                                .imageUrl(R.drawable.basket_red)
                                .build();
                        animManager.startAnim();
                        serviceGoodsDetailPresenter.addGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                                .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                                .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                                .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                                .puts("goodsSource", "1")
                                .puts("shopId", shopId)
                                .puts("goodsShopId", goodsShopId)
                                .puts("packageType", "SINGLE_PACKAGE")
                                .puts("goodsId", goodsDetail.id + "")
                                .puts("goodsSpecId", goodsSpecDetail.id + "")
                                .puts("goodsQuantity", goodsSpecDetail.count)
                                .puts("goodsType", goodsDetail.goodsType + "")
                        );
                        if (mallGoodsDetialSelectAdapter.getModel() == null) {

                        }
                    } else {
                        GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(goodsDetail.marketingId);
                        goodsMarketing = null;
                        GoodsBasketCell goodsBasketCell = new GoodsBasketCell(goodsSpecDetail.platformPrice,
                                goodsSpecDetail.retailPrice,
                                goodsSpecDetail.getPlusPrice(),
                                goodsDetail.goodsType,
                                goodsSpecDetail.isPlusOnly,
                                newStoreDetialModelSelect.isSupportOverSold, goodsSpecDetail.barcodeSku);
                        goodsBasketCell.goodsSpecDesc = goodsSpecDetail.goodsSpecStr;
                        goodsBasketCell.goodsStock = goodsSpecDetail.getRealAvailableInventory();
                        goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                        goodsBasketCell.mchId = goodsDetail.userId + "";
                        goodsBasketCell.goodsId = goodsDetail.id;
                        goodsBasketCell.setGoodsSpecId(goodsSpecDetail.id);
                        goodsBasketCell.goodsTitle = goodsDetail.goodsTitle;
                        goodsBasketCell.goodsImage = goodsDetail.headImage;
                        goodsBasketCell.setGoodsQuantity(Integer.parseInt(goodsSpecDetail.getCount()));
                        GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                        List<GoodsBasketCell> list = new ArrayList<>();
                        goodsBasketCell.shopIdList = goodsDetail.getGoodsShopIdListStringArray();
                        goodsBasketCell.ischeck = true;
                        goodsBasketCell.goodsShopId = newStoreDetialModelSelect.id;
                        goodsBasketCell.goodsShopName = newStoreDetialModelSelect.shopName;
                        goodsBasketCell.goodsShopAddress = newStoreDetialModelSelect.addressDetails;
                        list.add(goodsBasketCell);
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_ORDER)
                                .withString("visitShopId", shopId)
                                .withString("token", mFloatingView == null ? null : token)
                                .withString("course_id", mFloatingView == null ? null : course_id)
                                .withString("liveStatus", mFloatingView == null ? null : liveStatus)
                                .withObject("goodsbasketlist", list)
                                .withObject("actVip", goodsDetail.actVip)
                                .navigation();
                    }
                }
            });
        }
    }

    private void showSet() {
        if (goodsSetDialog == null) {
            goodsSetDialog = GoodsSetDialog.newInstance();
        }
        goodsSetDialog.show(getSupportFragmentManager(), "套餐");
    }

    private void initView() {
        topTab = (CommonTabLayout) findViewById(R.id.topTab);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        ivBottomShader = (ImageView) findViewById(R.id.iv_bottom_shader);
        goodsUnder = (LinearLayout) findViewById(R.id.goodsUnder);
        basketUnder = (ImageView) findViewById(R.id.basketUnder);
        topTitle = (ConstraintLayout) findViewById(R.id.topTitle);
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgShare = (ImageView) findViewById(R.id.img_share);
        imgBasket = (AutoClickImageView) findViewById(R.id.img_basket);
        imgBack2 = (ImageView) findViewById(R.id.img_back2);
        dargF = (DragLayout) findViewById(R.id.dargF);
        dargM = findViewById(R.id.dargM);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        shopCartRel = (RelativeLayout) findViewById(R.id.shop_cart_rel);
        shopCartNum = (TextView) findViewById(R.id.shop_cart_num);
        scrollUp = (ImageView) findViewById(R.id.scrollUp);
        goodsShareCoupon = (AutoClickImageView) findViewById(R.id.goodsShareCoupon);
    }

    @Override
    protected void onResume() {
        super.onResume();
        serviceGoodsDetailPresenter.getGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                .puts("shopId", shopId)
                .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
        if (goodsDetail != null) {
            if ("5".equals(marketingType)) {
                serviceGoodsDetailPresenter.getGoodsLimitResult(new SimpleHashMapBuilder<String, Object>().puts("includeNoPay", "1").putObject(
                        new GoodsSpecLimit(
                                "1",
                                goodsDetail.id,
                                null,
                                goodsDetail.mapMarketingGoodsId,
                                goodsDetail.marketingType,
                                null,
                                goodsDetail.marketingId, goodsDetail.goodsType, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))));
            } else {
                serviceGoodsDetailPresenter.getGoodsLimitResult(new SimpleHashMapBuilder<String, Object>().puts("includeNoPay", "1").putObject(
                        new GoodsSpecLimit(
                                "1",
                                goodsDetail.id,
                                null,
                                goodsDetail.mapMarketingGoodsId,
                                goodsDetail.marketingType,
                                null,
                                goodsDetail.marketingId, goodsDetail.goodsType)));
            }
        }
        if (!TextUtils.isEmpty(assembleId)) {
            isRunshow = false;
            runShowEveryMan();
        }
        isStop = false;

    }

    @Override
    public void successAddGoodsBasket(String result) {
        serviceGoodsDetailPresenter.getGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                .puts("shopId", shopId)
                .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
    }


    @Override
    public void successGetGoodsBasket(ShopCartModel result) {
        if (result == null) {
            shopCartNum.setVisibility(View.INVISIBLE);
            return;
        }
        if (result.total == 0) {
            shopCartNum.setVisibility(View.INVISIBLE);
        } else {
            shopCartNum.setVisibility(View.VISIBLE);
            shopCartNum.setText(result.total + "");
        }
    }

    boolean isZero = false;

    @Override
    public void successGetGoodsDetail(GoodsDetail goodsDetail) {//0普通 1砍价 2拼团 3秒杀 4新人专享
        if (goodsDetail != null && "1".equals(goodsDetail.differentSymbol)) {//异业门店的东西
            ARouter.getInstance()
                    .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                    .withString("shopId", shopId)
                    .withString("marketingType", marketingType)
                    .withString("assembleId", assembleId)
                    .withString("bargainId", bargainId)
                    .withString("bargainMemberId", bargainMemberId)
                    .withString("merchantId", merchantId)
                    .withString("goodsId", id)
                    .navigation();
            finish();
            return;
        }
        if(goodsDetail==null&&!TextUtils.isEmpty(marketingType)&&!"null".equals(marketingType)&&!"0".equals(marketingType)){
            marketingType=null;
            getData();
            return;
        }
        if ("0".equals(goodsShopId)) {//需要进行初始  兼容特殊情况
            try {
                if (ListUtil.checkObjIsInList(Arrays.asList(goodsDetail.getGoodsShopIdListStringArray()), shopId)) {
                    goodsShopId = shopId;
                } else {
                    goodsShopId = goodsDetail.getGoodsShopIdListStringArray()[0];
                }
            } catch (Exception e) {
                goodsShopId = shopId;
                e.printStackTrace();
            }
            getData();
            return;
        }
        recommandMap.put("shopId", goodsShopId);
        if (goodsDetail != null) {
            if (!TextUtils.isEmpty(marketingType) && !marketingType.equals(goodsDetail.marketingType)) {//进来活动和得到得活动不是一个类型
                if (chechNowShopIdInCanChose(goodsDetail))
                    if (showNoActTime == 0) {
                        if (!TextUtils.isEmpty(marketingType)) {
                            if ("1".equals(marketingType)) {//砍价活动则进行砍价布置
                                NoActDialog.newInstance("抱歉！您来晚了\n砍价活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                            } else if ("2".equals(marketingType)) {//
                                NoActDialog.newInstance("抱歉！您来晚了\n拼团活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                            } else if ("3".equals(marketingType)) {//
                                NoActDialog.newInstance("抱歉！您来晚了\n秒杀活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                            } else if ("4".equals(marketingType)) {//
                                NoActDialog.newInstance("抱歉！您来晚了\n新客活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                            } else if ("5".equals(marketingType)) {//
                                NoActDialog.newInstance("抱歉！您来晚了\n积分活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                            } else {
                                NoActDialog.newInstance("抱歉！您来晚了\n活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                            }
                        }
                        showNoActTime++;
                    }

            }
            if ("1".equals(goodsDetail.marketingType)) {
//                successGetGoodsDetailNormal(goodsDetail);
                bargainId=goodsDetail.marketingId;
                getData();
                return;
//                serviceGoodsDetailPresenter.getGoodsDetailKick(new SimpleHashMapBuilder<String, Object>().puts("bargainId", bargainId).puts("shopId", goodsShopId));
//                serviceGoodsDetailPresenter.getGoodsDetailKickSkuEx(new SimpleHashMapBuilder<String, Object>().puts("marketingId", bargainId).puts("shopId", goodsShopId));
            } else if ("2".equals(goodsDetail.marketingType)) {
                //拼团得 则
                assembleId=goodsDetail.marketingId;
                getData();
                return;
//                successGetGoodsDetailNormal(goodsDetail);
//                serviceGoodsDetailPresenter.getGoodsDetailPin(new SimpleHashMapBuilder<String, Object>().puts("assembleId", assembleId).puts("shopId", goodsShopId));
            } else if ("3".equals(goodsDetail.marketingType)) {
                successGetGoodsDetailKill(goodsDetail);
            } else if ("4".equals(goodsDetail.marketingType)) {
                successGetGoodsDetailNt(goodsDetail);
            } else if ("5".equals(goodsDetail.marketingType)) {
                successGetGoodsDetailPoint(goodsDetail);
            } else if ("6".equals(goodsDetail.marketingType)) {
                successGetGoodsDetailAct(goodsDetail);
            } else if ("7".equals(goodsDetail.marketingType)) {
                successGetGoodsDetailAct(goodsDetail);
            } else if ("8".equals(goodsDetail.marketingType)) {
                successGetGoodsDetailPlus(goodsDetail);
            } else {
                successGetGoodsDetailNormal(goodsDetail);
            }
        } else {
            recommandMap.put("type", "6");
            buildNoGoods();
        }
        if (goodsDetail!=null&&!ListUtil.isEmpty(goodsDetail.shareGiftDTOS)) {
            getGroupAll();
        }

    }

    @Override
    public void successGetGoodsDetailWithCode(GoodsDetail goodsDetail) {
        if (goodsDetail != null) {
            id = goodsDetail.id;
            getData();
        } else {
            recommandMap.put("type", "6");
            buildNoGoods();
        }
    }

    private void successGetGoodsDetailPoint(GoodsDetail goodsDetail) {
        this.goodsDetail = goodsDetail;
        initActView(5);//积分


        if (goodsDetail != null) {
            //////System.out.println("滑动到初始");
            if (newStoreDetialModelSelect != null) {
                this.goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
                isErrorOversold();
            }
            goodsSpecCells = goodsDetail.getSpecCell();
            goodsVideoSPresenter.getVideoS(new SimpleHashMapBuilder<String, Object>().puts("goodsId", goodsDetail.id + ""));
            goodsSpecDetail = new GoodsSpecDetail(goodsDetail.getStorePrice(),
                    goodsDetail.getPlatformPrice(),
                    goodsDetail.headImages.get(0).getSplash(),
                    goodsDetail.goodsType,
                    goodsDetail.marketingSalesMax,
                    goodsDetail.marketingSalesMin,
                    "1".equals(isNtReal),
                    goodsDetail.isPlusOnly
            );
            goodsSpecDetail.setShopDetailModelSelect(newStoreDetialModelSelect);
            if (sBitmap == null) {
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(goodsDetail.headImage)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(new SimpleTarget<Drawable>() {

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                sBitmap = DrawableUtils.drawableToBitmap(resource);
                            }
                        });
            }
            if (goodsDetail != null) {
                buildHasGoods();
            }
            if (newStoreDetialModelSelect == null) {
                serviceGoodsDetailPresenter.getStoreDetial(goodsShopId);
            }
            serviceGoodsDetailPresenter.getVipInfo(new SimpleHashMapBuilder<String, Object>());
            serviceGoodsDetailPresenter.getGoodsRecommendUnder(recommandMap);
            serviceGoodsDetailPresenter.getGoodsLimitResult(new SimpleHashMapBuilder<String, Object>().puts("includeNoPay", "1").putObject(
                    new GoodsSpecLimit(
                            "1",
                            goodsDetail.id,
                            null,
                            goodsDetail.mapMarketingGoodsId,
                            goodsDetail.marketingType,
                            null,
                            goodsDetail.marketingId, goodsDetail.goodsType, new SimpleDateFormat("yyyy-MM-dd").format(new Date()))));
            serviceGoodsDetailPresenter.getGoodsRecommend(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("position", "2")
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageNum", 1 + "")
                    .puts("pageSize", "10"));
            serviceGoodsDetailPresenter.getGoodsChose(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("position", "3")
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageNum", 1 + "")
                    .puts("pageSize", "10"));
            serviceGoodsDetailPresenter.getGoodsSet(new SimpleHashMapBuilder<String, Object>().puts("goodsId", goodsDetail.id)
                    .puts("shopId", goodsShopId));
//            serviceGoodsDetailPresenter.getGoodsTran(new SimpleHashMapBuilder<String, Object>()
//                    .puts("userId", goodsDetail.userId + "")
//                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
//                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG)));
        } else {
            recommandMap.put("position", "6");
            buildNoGoods();
        }
        serviceGoodsDetailPresenter.getGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                .puts("shopId", shopId)
                .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
    }

    public void successGetGoodsDetailNormal(GoodsDetail goodsDetail) {
        this.goodsDetail = goodsDetail;
        this.goodsDetail.marketingType = null;
        assembleId = null;
        bargainId = null;
        goods2ModelKick = null;
        goods2ModelPin = null;
        id = goodsDetail.getRealGoodsId();
        System.out.println("变成普通商品");
        marketingType = null;
        initActView(0);
        serviceGoodsDetailPresenter.getGoodsActShop(new SimpleHashMapBuilder<String, Object>()
                .puts("shopId", goodsShopId));
        if (goodsDetail != null) {
            // 优化跳转页面返回按钮样式
            if (newStoreDetialModelSelect != null) {
                this.goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
                isErrorOversold();
            }

            //////System.out.println("滑动到初始");
            goodsSpecCells = goodsDetail.getSpecCell();
            goodsVideoSPresenter.getVideoS(new SimpleHashMapBuilder<String, Object>().puts("goodsId", goodsDetail.id + ""));
            goodsSpecDetail = new GoodsSpecDetail(goodsDetail.getStorePrice(),
                    goodsDetail.getPlatformPrice(),
                    goodsDetail.headImages.get(0).getSplash(),
                    goodsDetail.goodsType,
                    999,
                    1,
                    "1".equals(isNtReal),
                    goodsDetail.isPlusOnly
            );

            goodsSpecDetail.setShopDetailModelSelect(newStoreDetialModelSelect);
            if (sBitmap == null) {
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(goodsDetail.headImage)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(new SimpleTarget<Drawable>() {

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                sBitmap = DrawableUtils.drawableToBitmap(resource);
                            }
                        });
            }
            if (goodsDetail != null) {

                buildHasGoods();
            }
            if (newStoreDetialModelSelect == null) {
                serviceGoodsDetailPresenter.getStoreDetial(goodsShopId);
            }
            serviceGoodsDetailPresenter.getGoodsRecommendUnder(recommandMap);
            serviceGoodsDetailPresenter.getGoodsCouponList(new SimpleHashMapBuilder<String, Object>()
                    .puts("merchantId", goodsDetail.userId)
                    .puts("shopId", goodsShopId)
                    .puts("page", "1")
                    .puts("pageSize", "3")
                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                    .puts("goodsId", this.goodsDetail.id + ""));
            serviceGoodsDetailPresenter.getGoodsRecommend(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("position", "2")
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageNum", 1 + "")
                    .puts("pageSize", "10"));
            serviceGoodsDetailPresenter.getGoodsChose(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("position", "3")
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageNum", 1 + "")
                    .puts("pageSize", "10"));
            serviceGoodsDetailPresenter.getGoodsSet(new SimpleHashMapBuilder<String, Object>().puts("goodsId", goodsDetail.id)
                    .puts("shopId", goodsShopId));
//            serviceGoodsDetailPresenter.getGoodsTran(new SimpleHashMapBuilder<String, Object>()
//                    .puts("shopId", shopId)
//                    .puts("userId", goodsDetail.userId + "")
//                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
//                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG)));
//            serviceGoodsDetailPresenter.getGoodsActRule(new SimpleHashMapBuilder<String, Object>()
//                    .puts("marketingId", goodsDetail.marketingId)
//                    .puts("marketingType", goodsDetail.marketingType ));
        } else {
            recommandMap.put("type", "6");
            buildNoGoods();
        }
        serviceGoodsDetailPresenter.getGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                .puts("shopId", shopId)
                .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
    }

    public void successGetGoodsDetailPlus(GoodsDetail goodsDetail) {
        this.goodsDetail = goodsDetail;
        initActView(8);
        serviceGoodsDetailPresenter.getGoodsActShop(new SimpleHashMapBuilder<String, Object>()
                .puts("shopId", goodsShopId));
        if (goodsDetail != null) {
            //////System.out.println("滑动到初始");
            if (newStoreDetialModelSelect != null) {
                this.goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
                isErrorOversold();
            }
            goodsSpecCells = goodsDetail.getSpecCell();
            goodsVideoSPresenter.getVideoS(new SimpleHashMapBuilder<String, Object>().puts("goodsId", goodsDetail.id + ""));
            goodsSpecDetail = new GoodsSpecDetail(goodsDetail.getStorePrice(),
                    goodsDetail.getPlatformPrice(),
                    goodsDetail.headImages.get(0).getSplash(),
                    goodsDetail.goodsType,
                    999,
                    1,
                    "1".equals(isNtReal),
                    goodsDetail.isPlusOnly);

            goodsSpecDetail.setShopDetailModelSelect(newStoreDetialModelSelect);
            if (sBitmap == null) {
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(goodsDetail.headImage)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(new SimpleTarget<Drawable>() {

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                sBitmap = DrawableUtils.drawableToBitmap(resource);
                            }
                        });
            }
            if (goodsDetail != null) {

                buildHasGoods();
            }
            if (newStoreDetialModelSelect == null) {
                serviceGoodsDetailPresenter.getStoreDetial(goodsShopId);
            }
            serviceGoodsDetailPresenter.getGoodsRecommendUnder(recommandMap);
            serviceGoodsDetailPresenter.getGoodsCouponList(new SimpleHashMapBuilder<String, Object>()
                    .puts("merchantId", goodsDetail.userId + "")
                    .puts("shopId", goodsShopId)
                    .puts("page", "1")
                    .puts("pageSize", "3")
                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                    .puts("goodsId", this.goodsDetail.id + ""));
            serviceGoodsDetailPresenter.getGoodsRecommend(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("position", "2")
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageNum", 1 + "")
                    .puts("pageSize", "10"));
            serviceGoodsDetailPresenter.getGoodsChose(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("position", "3")
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageNum", 1 + "")
                    .puts("pageSize", "10"));
            serviceGoodsDetailPresenter.getGoodsSet(new SimpleHashMapBuilder<String, Object>().puts("goodsId", goodsDetail.id)
                    .puts("shopId", goodsShopId));
            serviceGoodsDetailPresenter.getGoodsTran(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("userId", goodsDetail.userId + "")
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG)));
            serviceGoodsDetailPresenter.getGoodsActRule(new SimpleHashMapBuilder<String, Object>()
                    .puts("marketingId", goodsDetail.marketingId)
                    .puts("marketingType", goodsDetail.marketingType));
        } else {
            recommandMap.put("type", "6");
            buildNoGoods();
        }
        serviceGoodsDetailPresenter.getGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                .puts("shopId", shopId)
                .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
    }

    @Override
    public void successGetGoodsDetailKill(GoodsDetail goodsDetail) {

        new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                new SimpleHashMapBuilder<>()
                        .putObject(new MemberAction(
                                BuildConfig.VERSION_NAME,
                                1,
                                6,
                                "GoodsDetailActivity",
                                getActivitySimpleName(),
                                new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                        ));

        Date endTime = null;
        try {
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(goodsDetail.getMarketingEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (endTime.before(new Date())) {//活动结束了
            if (chechNowShopIdInCanChose(goodsDetail))
                if (showNoActTime == 0) {
                    if (!TextUtils.isEmpty(marketingType)) {
                        if ("1".equals(marketingType)) {//砍价活动则进行砍价布置
                            NoActDialog.newInstance("抱歉！您来晚了\n砍价活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        } else if ("2".equals(marketingType)) {//
                            NoActDialog.newInstance("抱歉！您来晚了\n拼团活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        } else if ("3".equals(marketingType)) {//
                            NoActDialog.newInstance("抱歉！您来晚了\n秒杀活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        } else if ("4".equals(marketingType)) {//
                            NoActDialog.newInstance("抱歉！您来晚了\n新客活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        } else {
                            NoActDialog.newInstance("抱歉！您来晚了\n活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        }
                    }
                    showNoActTime++;
                }
            successGetGoodsDetailNormal(goodsDetail);
            return;
        }

        this.goodsDetail = goodsDetail;
        initActView(3);
        if (goodsDetail != null) {
            if (newStoreDetialModelSelect != null) {
                this.goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
                isErrorOversold();
                System.out.println("原因分析:2254");
            }
            //////System.out.println("滑动到初始");
            goodsSpecCells = goodsDetail.getSpecCell();
            goodsVideoSPresenter.getVideoS(new SimpleHashMapBuilder<String, Object>().puts("goodsId", goodsDetail.id + ""));
            goodsSpecDetail = new GoodsSpecDetail(goodsDetail.getStorePrice(),
                    goodsDetail.getPlatformPrice(),
                    goodsDetail.headImages.get(0).getSplash(),
                    goodsDetail.goodsType,
                    goodsDetail.marketingSalesMax,
                    goodsDetail.marketingSalesMin,
                    "1".equals(isNtReal),
                    goodsDetail.isPlusOnly);

            goodsSpecDetail.setShopDetailModelSelect(newStoreDetialModelSelect);
            serviceGoodsDetailPresenter.getGoodsLimitResult(new SimpleHashMapBuilder<String, Object>().puts("includeNoPay", "1").putObject(
                    new GoodsSpecLimit(
                            "1",
                            goodsDetail.id + "",
                            null,
                            goodsDetail.mapMarketingGoodsId,
                            goodsDetail.marketingType,
                            null,
                            goodsDetail.marketingId + "", goodsDetail.goodsType)));
            if (sBitmap == null) {
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(goodsDetail.headImage)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(new SimpleTarget<Drawable>() {

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                sBitmap = DrawableUtils.drawableToBitmap(resource);
                            }
                        });
            }
            if (goodsDetail != null) {
                buildHasGoods();
            }
            if (newStoreDetialModelSelect == null) {
                serviceGoodsDetailPresenter.getStoreDetial(goodsShopId);
            }
            serviceGoodsDetailPresenter.getGoodsRecommendUnder(recommandMap);
//                serviceGoodsDetailPresenter.getGoodsCouponList(new SimpleHashMapBuilder<String, Object>()
//                        .puts("shopId", shopId)
//                        .puts("page", "1")
//                        .puts("pageSize", "3")
//                        .puts("memberId", shopId)
//                        .puts("goodsId", this.goodsDetail.id + ""));
            serviceGoodsDetailPresenter.getGoodsRecommend(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("position", "2")
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageNum", 1 + "")
                    .puts("pageSize", "10"));
            serviceGoodsDetailPresenter.getGoodsChose(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("position", "3")
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageNum", 1 + "")
                    .puts("pageSize", "10"));
            serviceGoodsDetailPresenter.getGoodsSet(new SimpleHashMapBuilder<String, Object>().puts("goodsId", goodsDetail.id)
                    .puts("shopId", goodsShopId));
            serviceGoodsDetailPresenter.getGoodsTran(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("userId", goodsDetail.userId + "")
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG)));
            serviceGoodsDetailPresenter.getGoodsActRule(new SimpleHashMapBuilder<String, Object>()
                    .puts("marketingId", goodsDetail.marketingId)
                    .puts("marketingType", goodsDetail.marketingType));
        } else {
            recommandMap.put("type", "6");
            buildNoGoods();
        }
        serviceGoodsDetailPresenter.getGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                .puts("shopId", shopId)
                .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
    }

    //营销活动吧 大概
    @Override
    public void successGetGoodsDetailAct(GoodsDetail goodsDetail) {
        Date endTime = null;
        try {
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(goodsDetail.getMarketingEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (endTime.before(new Date())) {//活动结束了
            if (chechNowShopIdInCanChose(goodsDetail))
                if (showNoActTime == 0) {
                    if (!TextUtils.isEmpty(marketingType)) {
                        if ("1".equals(marketingType)) {//砍价活动则进行砍价布置
                            NoActDialog.newInstance("抱歉！您来晚了\n砍价活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        } else if ("2".equals(marketingType)) {//
                            NoActDialog.newInstance("抱歉！您来晚了\n拼团活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        } else if ("3".equals(marketingType)) {//
                            NoActDialog.newInstance("抱歉！您来晚了\n秒杀活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        } else if ("4".equals(marketingType)) {//
                            NoActDialog.newInstance("抱歉！您来晚了\n新客活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        } else {
                            NoActDialog.newInstance("抱歉！您来晚了\n活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        }
                    }
                    showNoActTime++;
                }
            successGetGoodsDetailNormal(goodsDetail);
            return;
        }
        {
            this.goodsDetail = goodsDetail;
            initActView(Integer.parseInt(goodsDetail.marketingType));
            if (goodsDetail != null) {
                if (newStoreDetialModelSelect != null) {
                    this.goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
                    isErrorOversold();
                }
                //////System.out.println("滑动到初始");
                goodsSpecCells = goodsDetail.getSpecCell();
                goodsVideoSPresenter.getVideoS(new SimpleHashMapBuilder<String, Object>().puts("goodsId", goodsDetail.id + ""));
                goodsSpecDetail = new GoodsSpecDetail(goodsDetail.getStorePrice(),
                        goodsDetail.getPlatformPrice(),
                        goodsDetail.headImages.get(0).getSplash(),
                        goodsDetail.goodsType,
                        goodsDetail.marketingSalesMax,
                        goodsDetail.marketingSalesMin,
                        "1".equals(isNtReal),
                        goodsDetail.isPlusOnly);

                goodsSpecDetail.setShopDetailModelSelect(newStoreDetialModelSelect);
                serviceGoodsDetailPresenter.getGoodsLimitResult(new SimpleHashMapBuilder<String, Object>().puts("includeNoPay", "1").putObject(
                        new GoodsSpecLimit(
                                "1",
                                goodsDetail.id + "",
                                null,
                                goodsDetail.mapMarketingGoodsId,
                                goodsDetail.marketingType,
                                null,
                                goodsDetail.marketingId, goodsDetail.goodsType)));
                if (sBitmap == null) {
                    com.healthy.library.businessutil.GlideCopy.with(mContext).load(goodsDetail.headImage)
                            .placeholder(R.drawable.img_1_1_default2)
                            .error(R.drawable.img_1_1_default)

                            .into(new SimpleTarget<Drawable>() {

                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    sBitmap = DrawableUtils.drawableToBitmap(resource);
                                }
                            });
                }
                if (goodsDetail != null) {

                    buildHasGoods();
                }
                if (newStoreDetialModelSelect == null) {
                    serviceGoodsDetailPresenter.getStoreDetial(goodsShopId);
                }
                serviceGoodsDetailPresenter.getGoodsRecommendUnder(recommandMap);
//                serviceGoodsDetailPresenter.getGoodsCouponList(new SimpleHashMapBuilder<String, Object>()
//                        .puts("shopId", shopId)
//                        .puts("page", "1")
//                        .puts("pageSize", "3")
//                        .puts("memberId", shopId)
//                        .puts("goodsId", this.goodsDetail.id + ""));
                serviceGoodsDetailPresenter.getGoodsRecommend(new SimpleHashMapBuilder<String, Object>()
                        .puts("shopId", goodsShopId)
                        .puts("position", "2")
                        .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                        .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                        .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                        .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                        .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                        .puts("pageNum", 1 + "")
                        .puts("pageSize", "10"));
                serviceGoodsDetailPresenter.getGoodsChose(new SimpleHashMapBuilder<String, Object>()
                        .puts("shopId", goodsShopId)
                        .puts("position", "3")
                        .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                        .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                        .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                        .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                        .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                        .puts("pageNum", 1 + "")
                        .puts("pageSize", "10"));
                serviceGoodsDetailPresenter.getGoodsSet(new SimpleHashMapBuilder<String, Object>().puts("goodsId", goodsDetail.id)
                        .puts("shopId", goodsShopId));
                serviceGoodsDetailPresenter.getGoodsTran(new SimpleHashMapBuilder<String, Object>()
                        .puts("shopId", goodsShopId)
                        .puts("userId", goodsDetail.userId + "")
                        .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                        .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG)));
                serviceGoodsDetailPresenter.getGoodsActRule(new SimpleHashMapBuilder<String, Object>()
                        .puts("marketingId", goodsDetail.marketingId)
                        .puts("marketingType", goodsDetail.marketingType));
            } else {
                recommandMap.put("type", "6");
                buildNoGoods();
            }
            serviceGoodsDetailPresenter.getGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                    .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("shopId", shopId)
                    .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
        }
    }

    @Override
    public void successGetGoodsDetailNt(GoodsDetail goodsDetail) {
        serviceGoodsDetailPresenter.getIsNewAppMember();
        this.goodsDetail = goodsDetail;
        initActView(4);
        if (goodsDetail != null) {
            //////System.out.println("滑动到初始");
            if (newStoreDetialModelSelect != null) {
                this.goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
                isErrorOversold();
            }
            goodsSpecCells = goodsDetail.getSpecCell();
            goodsVideoSPresenter.getVideoS(new SimpleHashMapBuilder<String, Object>().puts("goodsId", goodsDetail.id + ""));
            goodsSpecDetail = new GoodsSpecDetail(goodsDetail.getStorePrice(),
                    goodsDetail.getPlatformPrice(),
                    goodsDetail.headImages.get(0).getSplash(),
                    goodsDetail.goodsType,
                    goodsDetail.marketingSalesMax,
                    goodsDetail.marketingSalesMin,
                    "1".equals(isNtReal),
                    goodsDetail.isPlusOnly);

            goodsSpecDetail.setShopDetailModelSelect(newStoreDetialModelSelect);
            if (sBitmap == null) {
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(goodsDetail.headImage)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(new SimpleTarget<Drawable>() {

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                sBitmap = DrawableUtils.drawableToBitmap(resource);
                            }
                        });
            }
            if (goodsDetail != null) {

                buildHasGoods();
            }
            if (newStoreDetialModelSelect == null) {
                serviceGoodsDetailPresenter.getStoreDetial(goodsShopId);
            }
            serviceGoodsDetailPresenter.getGoodsRecommendUnder(recommandMap);
//            serviceGoodsDetailPresenter.getGoodsCouponList(new SimpleHashMapBuilder<String, Object>()
//                    .puts("shopId", shopId)
//                    .puts("page", "1")
//                    .puts("pageSize", "3")
//                    .puts("memberId", shopId)
//                    .puts("goodsId", this.goodsDetail.id + ""));
            serviceGoodsDetailPresenter.getGoodsRecommend(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("position", "2")
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageNum", 1 + "")
                    .puts("pageSize", "10"));
            serviceGoodsDetailPresenter.getGoodsChose(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("position", "3")
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageNum", 1 + "")
                    .puts("pageSize", "10"));
            serviceGoodsDetailPresenter.getGoodsSet(new SimpleHashMapBuilder<String, Object>().puts("goodsId", goodsDetail.id)
                    .puts("shopId", goodsShopId));
            serviceGoodsDetailPresenter.getGoodsTran(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("userId", goodsDetail.userId + "")
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG)));
            serviceGoodsDetailPresenter.getGoodsActRule(new SimpleHashMapBuilder<String, Object>()
                    .puts("marketingId", goodsDetail.marketingId)
                    .puts("marketingType", goodsDetail.marketingType));
        } else {
            recommandMap.put("type", "6");
            buildNoGoods();
        }
        serviceGoodsDetailPresenter.getGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                .puts("shopId", shopId)
                .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));

    }

    @Override
    public void successGetGoodsDetailPin(Goods2DetailPin goods2DetailPin) {

        new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                new SimpleHashMapBuilder<>()
                        .putObject(new MemberAction(
                                BuildConfig.VERSION_NAME,
                                1,
                                4,
                                "GoodsDetailActivity",
                                getActivitySimpleName(),
                                new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                        ));

        this.goods2ModelPin = goods2DetailPin;
        initActView(2);
        if (goods2DetailPin != null) {
            this.goodsDetail = goods2DetailPin.goodsDetails;
        } else {
            buildNoGoods();
            return;
        }
        Date endTime = null;
        try {
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(goods2DetailPin.assembleDTO.endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (endTime.before(new Date())) {//活动结束了
            if (showNoActTime == 0) {
                NoActDialog.newInstance("抱歉！您来晚了\n拼团活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                showNoActTime++;
            }
            successGetGoodsDetailNormal(this.goodsDetail);
            return;
        }


        if (goods2DetailPin != null) {
            //////System.out.println("滑动到初始");
            //////System.out.println("滑动到初始");
            getGroupAll();
            if (newStoreDetialModelSelect != null) {
                this.goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
                isErrorOversold();
            }
            goodsVideoSPresenter.getVideoS(new SimpleHashMapBuilder<String, Object>().puts("goodsId", goods2DetailPin.goodsDetails.id + ""));
            goodsSpecCells = this.goodsDetail.getSpecCell();
            goodsSpecDetail = new GoodsSpecDetail(this.goodsDetail.getStorePrice(),
                    goodsDetail.getPlatformPrice(),
                    goodsDetail.headImages.get(0).getSplash(),
                    goodsDetail.goodsType,
                    1,
                    1,
                    "1".equals(isNtReal),
                    goodsDetail.isPlusOnly);

            goodsSpecDetail.setShopDetailModelSelect(newStoreDetialModelSelect);
            if (sBitmap == null) {
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(this.goodsDetail.headImage)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(new SimpleTarget<Drawable>() {

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                sBitmap = DrawableUtils.drawableToBitmap(resource);
                            }
                        });
            }
            orderGroupSinglePrice.setText("¥" + StringUtils.getResultPrice(goods2DetailPin.goodsDetails.platformPrice + ""));
            orderGroupPrice.setText("¥" + StringUtils.getResultPrice(goods2DetailPin.assembleDTO.assemblePrice + ""));
            orderGroupNumber.setText(goods2DetailPin.assembleDTO.regimentSize + "人团");
            pinCallStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (goods2ModelPin != null & !TextUtils.isEmpty(goods2ModelPin.merchantMobile)) {

                        IntentUtils.dial(mContext, goods2ModelPin.merchantMobile);
                    } else {
                        Toast.makeText(mContext, "暂无联系电话", Toast.LENGTH_SHORT).show();
                    }
                }
            });

//        serviceGoodsDetailPresenter.getGoodsTran(new SimpleHashMapBuilder<String, Object>().puts("shopId", goodsDetail.userId + ""));
            if (goods2DetailPin != null) {
//                if (goodsDetail.goodsStatus == 2) {
//
//                } else {
//                    buildNoGoods();
//                }


                buildHasGoods();
            }
            mallGoodsDetialSetAdapter.setId(this.goodsDetail.id + "");
            if (newStoreDetialModelSelect == null) {
                serviceGoodsDetailPresenter.getStoreDetial(goodsShopId);
            }
            serviceGoodsDetailPresenter.getGoodsRecommendUnder(recommandMap);
//            serviceGoodsDetailPresenter.getGoodsCouponList(new SimpleHashMapBuilder<String, Object>()
//                    .puts("shopId", shopId)
//                    .puts("page", "1")
//                    .puts("pageSize", "3")
//                    .puts("memberId", shopId)
//                    .puts("goodsId", this.goodsDetail.id + ""));
            serviceGoodsDetailPresenter.getGoodsRecommend(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("position", "2")
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageNum", 1 + "")
                    .puts("pageSize", "10"));
            serviceGoodsDetailPresenter.getGoodsChose(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("position", "3")
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageNum", 1 + "")
                    .puts("pageSize", "10"));
            serviceGoodsDetailPresenter.getGoodsSet(new SimpleHashMapBuilder<String, Object>().puts("goodsId", this.goodsDetail.id + "")
                    .puts("shopId", goodsShopId));
            serviceGoodsDetailPresenter.getGoodsTran(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("userId", this.goodsDetail.userId + "")
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG)));
            serviceGoodsDetailPresenter.getGoodsActRule(new SimpleHashMapBuilder<String, Object>()
                    .puts("marketingId", this.goodsDetail.marketingId)
                    .puts("marketingType", this.goodsDetail.marketingType));
        } else {
            recommandMap.put("type", "6");
            buildNoGoods();
        }
        serviceGoodsDetailPresenter.getGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                .puts("shopId", shopId)
                .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
    }

    @Override
    public void successGetGoodsDetailKick(Goods2DetailKick goods2DetailKick) {
        new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                new SimpleHashMapBuilder<>()
                        .putObject(new MemberAction(
                                BuildConfig.VERSION_NAME,
                                1,
                                5,
                                "GoodsDetailActivity",
                                getActivitySimpleName(),
                                new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                        ));
        this.goods2ModelKick = goods2DetailKick;
        initActView(1);
        if (goods2DetailKick != null) {
            this.goodsDetail = goods2DetailKick.goodsDetails;
        } else {
            buildNoGoods();
            return;
        }
        Date endTime = null;
        try {
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(goods2DetailKick.bargainInfoDTO.endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (endTime.before(new Date())) {//活动结束了
            if (showNoActTime == 0) {
                NoActDialog.newInstance("抱歉！您来晚了\n砍价活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                showNoActTime++;
            }
            successGetGoodsDetailNormal(this.goodsDetail);
            return;
        }


        if (goods2DetailKick != null) {
            //////System.out.println("滑动到初始");
            if (newStoreDetialModelSelect != null) {
                this.goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
                isErrorOversold();
            }
            goodsVideoSPresenter.getVideoS(new SimpleHashMapBuilder<String, Object>().puts("goodsId", goods2DetailKick.goodsDetails.id + ""));
            pinCallStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(goods2ModelKick.merchantMobile)) {

                        IntentUtils.dial(mContext, goods2ModelKick.merchantMobile);
                    } else {
                        Toast.makeText(mContext, "暂无联系电话", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            goodsSpecCells = this.goodsDetail.getSpecCell();
            goodsSpecDetail = new GoodsSpecDetail(this.goodsDetail.getStorePrice(),
                    this.goodsDetail.getPlatformPrice(),
                    this.goodsDetail.headImages.get(0).getSplash(),
                    goods2DetailKick.goodsDetails.goodsType,
                    1,
                    1,
                    "1".equals(isNtReal),
                    goodsDetail.isPlusOnly
            );

            goodsSpecDetail.setShopDetailModelSelect(newStoreDetialModelSelect);
            if (sBitmap == null) {
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(this.goodsDetail.headImage)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(new SimpleTarget<Drawable>() {

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                sBitmap = DrawableUtils.drawableToBitmap(resource);
                            }
                        });
            }
            orderGroupSinglePrice.setText("¥" + StringUtils.getResultPrice(goods2DetailKick.goodsDetails.platformPrice + ""));
            orderGroupPrice.setText("¥" + StringUtils.getResultPrice(goods2DetailKick.bargainInfoDTO.floorPrice + ""));
            orderGroupNumber.setText("发起砍价");

            if (goods2DetailKick != null) {
                buildHasGoods();
            }
            mallGoodsDetialSetAdapter.setId(this.goodsDetail.id + "");
            if (newStoreDetialModelSelect == null) {
                serviceGoodsDetailPresenter.getStoreDetial(goodsShopId);
            }
            serviceGoodsDetailPresenter.getGoodsRecommendUnder(recommandMap);
//            serviceGoodsDetailPresenter.getGoodsCouponList(new SimpleHashMapBuilder<String, Object>()
//                    .puts("shopId", shopId)
//                    .puts("page", "1")
//                    .puts("pageSize", "3")
//                    .puts("memberId", shopId)
//                    .puts("goodsId", this.goodsDetail.id + ""));
            serviceGoodsDetailPresenter.getGoodsRecommend(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("position", "2")
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageNum", 1 + "")
                    .puts("pageSize", "10"));
            serviceGoodsDetailPresenter.getGoodsChose(new SimpleHashMapBuilder<String, Object>()
                    .puts("shopId", goodsShopId)
                    .puts("position", "3")
                    .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                    .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                    .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                    .puts("pageNum", 1 + "")
                    .puts("pageSize", "10"));
            serviceGoodsDetailPresenter.getGoodsSet(new SimpleHashMapBuilder<String, Object>().puts("goodsId", this.goodsDetail.id + "")
                    .puts("shopId", goodsShopId));
            serviceGoodsDetailPresenter.getGoodsActRule(new SimpleHashMapBuilder<String, Object>()
                    .puts("marketingId", this.goodsDetail.marketingId)
                    .puts("marketingType", this.goodsDetail.marketingType));
        } else {
            recommandMap.put("type", "6");
            buildNoGoods();
        }
        serviceGoodsDetailPresenter.getGoodsBasket(new SimpleHashMapBuilder<String, Object>()
                .puts("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                .puts("shopId", shopId)
                .puts("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                .puts("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
    }

    private void buildError() {
        try {
            if ("6".equals(goodsDetail.marketingType) || "7".equals(goodsDetail.marketingType)
                    || "8".equals(goodsDetail.marketingType) || "0".equals(goodsDetail.marketingType)
                    || "4".equals(goodsDetail.marketingType) || goodsDetail.marketingType == null || "3".equals(goodsDetail.marketingType)) {
                goBask.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_basket_error);
                goOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_order_error);
            }

//            if ("3".equals(goodsDetail.marketingType)) {
//                goBask.setBackgroundResource(R.drawable.shape_discount_pin_button_left_error);
//                goOrder.setBackgroundResource(R.drawable.shape_discount_pin_button_right_error);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildNoError() {
        try {
            if ("6".equals(goodsDetail.marketingType) || "7".equals(goodsDetail.marketingType)
                    || "8".equals(goodsDetail.marketingType) || "0".equals(goodsDetail.marketingType)
                    || "4".equals(goodsDetail.marketingType) || goodsDetail.marketingType == null || "3".equals(goodsDetail.marketingType)) {
                goBask.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_basket);
                goOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_order);
            }

//            if ("3".equals(goodsDetail.marketingType)) {
//                goBask.setBackgroundResource(R.drawable.shape_discount_pin_button_left);
//                goOrder.setBackgroundResource(R.drawable.shape_discount_pin_button_right);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void successGetGoodsRecommendUnder(List<RecommendList> result) {
        if (result == null) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
            return;
        }
        if (result.size() == 0) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
            if (page == 1) {
                mallGoodsTitleAdapter.setModel(null);
                mTabEntities.clear();
                for (int i = 0; i < titles2.length; i++) {
                    //插入tab标签
                    mTabEntities.add(new TabEntity(titles2[i], 0, 0));
                }

                topTab.setTabData(mTabEntities);
            }
        } else {
            if (isgoodshas) {

                mallGoodsTitleAdapter.setModel("为您精选");
            } else {

                mallGoodsTitleAdapter.setModel("相关推荐");
            }
            if (page == 1) {
                mallGoodsItemAdapter.setData((ArrayList<RecommendList>) result);
                //////System.out.println("目前推荐的长度设置数据");
            } else {
                mallGoodsItemAdapter.addDatas((ArrayList<RecommendList>) result);
            }
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }

    }

    @Override
    public void successGetGoodsRecommend(List<RecommendList> result) {
        if (result == null) {
            tab2--;
            tab3--;
            buildTabNow();
            mallGoodsDetialRecommendAdapter.setModel(null);
            return;
        }
        if (result.size() == 0) {
            tab2--;
            tab3--;
            buildTabNow();
            mallGoodsDetialRecommendAdapter.setModel(null);
            return;
        }
        ArrayList<ArrayList<RecommendList>> tmp = new ArrayList<>();
        tmp.add((ArrayList<RecommendList>) result);
        mallGoodsDetialRecommendAdapter.setData(tmp);
    }

    @Override
    public void successGetGoodsChose(List<RecommendList> result) {
        if (result == null) {
            tab2--;
            tab3--;
            buildTabNow();
            mallGoodsDetialChoseAdapter.setModel(null);
            return;
        }
        if (result.size() == 0) {
            tab2--;
            tab3--;
            buildTabNow();
            mallGoodsDetialChoseAdapter.setModel(null);
            return;
        }
        ArrayList<ArrayList<RecommendList>> tmp = new ArrayList<>();
        tmp.add((ArrayList<RecommendList>) result);
        mallGoodsDetialChoseAdapter.setData(tmp);
    }

    private void buildNoGoods() {
        isZero = true;
        recommandMap.put("goodsId", id);
        isgoodshas = false;
        mallGoodsDetialChoseAdapter.setModel(null);
        mallGoodsDetialDiscountAdapter.setModel(null);
        mallGoodsDetialTranAdapter.setModel(null);
        mallGoodsDetialIntroducePointAdapter.setModel(null);
        mallGoodsDetialIntroduceImgAdapter.setModel(null);
        mallGoodsDetialDiscussAdapter.setModel(null);
        mallGoodsDetialIntroduceAdapter.setModel(null);
        mallGoodsDetialIntroduceEndAdapter.setModel(null);
        mallGoodsDetialRecommendAdapter.setModel(null);
        mallGoodsDetialSelectAdapter.setModel(null);
        mallGoodsDetialSetAdapter.setModel(null);
        mallGoodsDetialTopAdapter.setModel(null);
        mallGoodsItemAdapter.setKey("商品已下架");
        mallGoodsItemNoAdapter.setModel("原商品已下架，为您推荐相关商品");
        page = 1;
        recommandMap.put("pageNum", page + "");
        recommandMap.put("goodsId", "");//这里IOS没传这个   但是我们传了也没数据  怀疑是辉龙的问题 辉龙已经回家了 先跟ios保持一致 都不传  保证有数据   等辉龙后面看看
        serviceGoodsDetailPresenter.getGoodsRecommendUnder(recommandMap);
    }

    int showNoActTime = 0;

    private void buildHasGoods() {
        isgoodshas = true;
        layoutRefresh.setEnableRefresh(true);
        goodsUnder.setVisibility(View.VISIBLE);
        mallGoodsItemNoAdapter.setModel(null);
//        mallGoodsTitleAdapter.setModel("为您精选");
//        mallGoodsItemAdapter.setModel(null);
        if (goodsSpecDialog != null) {
            goodsSpecDialog.setGoodsId(goodsDetail.id, goodsShopId);

            goodsSpecDialog.setGoodsSpecDetail(goodsSpecDetail);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goodsSpecDialog.initSpec(goodsSpecCells);
                }
            }, 500);
        }
        mallGoodsDetialTopAdapter.setModel(goodsDetail);
        boolean isActFirst = false;//标记当前是不是已经有活动布置了
        if (goods2ModelKick != null) {
            mallGoodsDetialTopAdapter.setActType(1);
            mallGoodsDetialTopAdapter.setActGoods(goods2ModelKick.bargainInfoDTO);
            isActFirst = true;
        }
        if (goods2ModelPin != null) {//0普通 1砍价 2拼团 3秒杀 4新人专享
            mallGoodsDetialTopAdapter.setActType(2);
            mallGoodsDetialTopAdapter.setActGoods(goods2ModelPin.assembleDTO);
            isActFirst = true;
        }
        if (!TextUtils.isEmpty(marketingType)) {//说明有目的的活动期望
            if (!marketingType.equals(goodsDetail.marketingType)) {//说明期望出现问题 可能活动结束了
                if (chechNowShopIdInCanChose(goodsDetail))
                    if (showNoActTime == 0) {
                        if ("1".equals(marketingType)) {//砍价活动则进行砍价布置
                            NoActDialog.newInstance("抱歉！您来晚了\n砍价活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        } else if ("2".equals(marketingType)) {//

                            NoActDialog.newInstance("抱歉！您来晚了\n拼团活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        } else if ("3".equals(marketingType)) {//
                            NoActDialog.newInstance("抱歉！您来晚了\n秒杀活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        } else if ("4".equals(marketingType)) {//
                            NoActDialog.newInstance("抱歉！您来晚了\n新客活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        } else {
                            NoActDialog.newInstance("抱歉！您来晚了\n活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                        }
                        showNoActTime++;
                    }
            }
        }

        mallGoodsDetialIntroducePointAdapter.setModel(null);
        if (!isActFirst) {//没有优先活动布置则使用商品本身活动进行活动布置 //0普通 1砍价 2拼团 3秒杀 4新人专享
            if ("1".equals(goodsDetail.marketingType)) {//砍价活动则进行砍价布置
                mallGoodsDetialIntroduceEndAdapter.setModel(goodsDetail);
            } else if ("2".equals(goodsDetail.marketingType)) {//
                mallGoodsDetialIntroduceEndAdapter.setModel(goodsDetail);
            } else if ("3".equals(goodsDetail.marketingType)) {//

                goodsMoneyV.setText(FormatUtils.moneyKeep2Decimals(goodsDetail.marketingGoodsChildren.get(0).marketingPrice));
                mallGoodsDetialTopAdapter.setActType(3);
                mallGoodsDetialIntroduceEndAdapter.setModel(goodsDetail);
                ////System.out.println("Kadapter3");
            } else if ("4".equals(goodsDetail.marketingType)) {//
                if ("1".equals(isNtReal)) {
                    mallGoodsDetialTopAdapter.setNtReal(true);
                    goodsMoneyV.setText(FormatUtils.moneyKeep2Decimals(goodsDetail.marketingGoodsChildren.get(0).marketingPrice));
                } else {
                    mallGoodsDetialTopAdapter.setNtReal(false);
                    goodsMoneyV.setText(FormatUtils.moneyKeep2Decimals(goodsDetail.getPlusOrPlatformPrice()));
                }
                mallGoodsDetialTopAdapter.setActType(4);
                mallGoodsDetialIntroduceEndAdapter.setModel(goodsDetail);
            } else if ("5".equals(goodsDetail.marketingType)) {
                mallGoodsDetialIntroducePointAdapter.setModel(goodsDetail);
                mallGoodsDetialIntroduceEndAdapter.setModel(null);
                mallGoodsDetialTopAdapter.setActType(5);
                if (mvipInfo != null) {
                    pointValue.setText(mvipInfo.SYJFTOT);
                }
                checkPointUnder();
            } else if (!TextUtils.isEmpty(goodsDetail.marketingType)) {
                try {
                    goodsMoneyV.setText(FormatUtils.moneyKeep2Decimals(goodsDetail.getPlusOrPlatformPrice()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                if (goodsDetail.discountTopModel != null) {
//                    goodsMoneyV.setText(FormatUtils.moneyKeep2Decimals(goodsDetail.getStandPrice(goodsDetail.discountTopModel.standardPriceType)));
//                }

                mallGoodsDetialTopAdapter.setActType(Integer.parseInt(goodsDetail.marketingType));
                ////System.out.println("Kadapter0");
                mallGoodsDetialIntroduceEndAdapter.setModel(goodsDetail);
            } else {
                goodsMoneyV.setText(FormatUtils.moneyKeep2Decimals(goodsDetail.getPlusOrPlatformPrice()));
                mallGoodsDetialTopAdapter.setActType(0);
                ////System.out.println("Kadapter0");
                mallGoodsDetialIntroduceEndAdapter.setModel(goodsDetail);
            }
        }
        mallGoodsDetialIntroduceAdapter.setModel(goodsDetail);
        tab3++;
        tab3++;
        buildTabNow();
        //接口富文本内容不为空且内容不相同的话进行更新内容 否则不做任何刷新操作
        if (!TextUtils.isEmpty(goodsDetail.additionNote)
                && !goodsDetail.additionNote.equals(mallGoodsDetialIntroduceImgAdapter.getModel())) {
            mallGoodsDetialIntroduceImgAdapter.setModel(goodsDetail.additionNote);
            //需重新加载webView
            mallGoodsDetialIntroduceImgAdapter.setLoadSuccess(false);
        }
        if (newStoreDetialModelSelect != null) {
            this.goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
            isErrorOversold();
            System.out.println("原因分析:3109");
        }
        if (!ListUtil.isEmpty(goodsDetail.shareGiftDTOS)) {
            goodsDetailRankingShareAdapter.setRankData(goodsDetail.shareGiftDTOS);
            goodsDetailRankingShareAdapter.setModel("data");
            serviceGoodsDetailPresenter.getGoodsRankList();
        }else {
            goodsDetailRankingShareAdapter.clear();
        }
    }

    private void checkPointUnder() {
        if ("5".equals(marketingType)) {
            if (newStoreDetialModelSelect != null) {
                pointLL.setVisibility(View.GONE);
                goOrder.setText("立即兑换");
                goOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_order);
                if (goodsDetail.cheIsNoSku()) {//如果是无规格的需要底部变动
                    if (Double.parseDouble(mvipInfo.SYJFTOT) < goodsDetail.pointsPrice) {//积分不足
                        pointLL.setVisibility(View.VISIBLE);
                        goOrder.setText("积分不足");
                        goOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_order_no);
                    } else {
                        if (goodsDetail.marketingGoodsChildren.get(0).getAvailableInventoryMark(goodsDetail.goodsChildren.get(0)) <= 0) {//判断超卖
                            showToast("该商品已兑完，看看其他商品吧~");
                            pointLL.setVisibility(View.VISIBLE);
                            goOrder.setText("已兑完");
                            goOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_order_no);
                        } else if (goodsDetail.pointsTodayInventory == 0 && goodsDetail.pointsEverydayInventory != 0) {//今日兑换0了
                            pointLL.setVisibility(View.VISIBLE);
                            goOrder.setText("已兑完");
                            showToast("该商品已兑完，看看其他商品吧~");
                            goOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_order_no);
                        } else if (goodsSpecDetail.nowOrderBuyCount >= goodsDetail.marketingSalesMax && goodsDetail.marketingSalesMax != 0) {//增加判断
                            pointLL.setVisibility(View.VISIBLE);
                            goOrder.setText("已兑完");
                            showToast("该商品已兑完，看看其他商品吧~");
                            goOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_order_no);
                        }
                    }
                } else {
                    if (goodsDetail.pointsTodayInventory == 0 && goodsDetail.pointsEverydayInventory != 0) {//今日兑换0了
                        pointLL.setVisibility(View.VISIBLE);
                        goOrder.setText("已兑完");
                        showToast("该商品已兑完，看看其他商品吧~");
                        goOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_order_no);
                    } else if (goodsSpecDetail.nowOrderBuyCount >= goodsDetail.marketingSalesMax && goodsDetail.marketingSalesMax != 0) {//增加判断
                        pointLL.setVisibility(View.VISIBLE);
                        goOrder.setText("已兑完");
                        showToast("该商品已兑完，看看其他商品吧~");
                        goOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_order_no);
                    } else if (Double.parseDouble(mvipInfo.SYJFTOT) <= 0) {
                        pointLL.setVisibility(View.VISIBLE);
                        goOrder.setText("积分不足");
                        goOrder.setBackgroundResource(R.drawable.shape_mall_goods_ol_vid_btn_go_order_no);
                    }
                }
            }
        }
    }


    @Override
    public void successGetGoodsSet(List<GoodsSetAll> goodsSetCellList) {
        if (goodsSetCellList == null) {
            tab2--;
            tab3--;
            buildTabNow();
            return;
        }
        if (goodsSetCellList.size() > 0) {

            mallGoodsDetialSetAdapter.setSetCount(goodsSetCellList.size());
            mallGoodsDetialSetAdapter.setModel(goodsSetCellList.get(0));
        } else {
            tab2--;
            tab3--;
            buildTabNow();
        }
    }

    String actrule;

    @Override
    public void successGetActRule(String result) {
        if (!TextUtils.isEmpty(result) && !"null".equals(result)) {
            this.actrule = result;
            mallGoodsDetialTranAdapter.setModel("goodsTran");
            mallGoodsDetialTranAdapter.setActRule(result);
        }
    }

    @Override
    public void successGetGoodsTran(GoodsTran goodsTran) {
        if (goodsTran == null) {
            tab2--;
            tab3--;
            buildTabNow();
            return;
        }
        this.goodsTran = goodsTran;
        if (goodsTran != null) {
            mallGoodsDetialTranAdapter.setModel("goodsTran");
            mallGoodsDetialTranAdapter.setGoodsTran(goodsTran);
        }
    }

    @Override
    public void getSucessTeamList(List<AssemableTeam> couponInfoByMerchants) {
        mallGoodsDetialTopAdapter.setTeamList(couponInfoByMerchants);

    }

    List<MallGroupSimple> kkGroupSimples = new ArrayList<>();

    @Override
    public void onSuccessGetGroupAlreadyList(List<MallGroupSimple> kkGroupSimples) {
        if (this.kkGroupSimples.size() > 0) {

        } else {
            this.kkGroupSimples.addAll(kkGroupSimples);
            runShowEveryMan();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!TextUtils.isEmpty(assembleId)) {
            isStop = true;
        }
    }


    @Override
    public void successGetGoodsSkuFinalResult(GoodsSpecLimit result) {
        if (result != null) {
            goodsSpecDetail.nowOrderBuyCount = result.totalQuantity;
            if ("5".equals(goodsDetail.marketingType)) {
                checkPointUnder();
            }
        } else {
            goodsSpecDetail.nowOrderBuyCount = 0;
        }
    }

    @Override
    public void onSucessIsNewAppMember(int result) {
        if (result == 0 || result == 1) {
            isNtReal = result + "";
            if (goodsDetail != null) {
                buildHasGoods();
            }
        }
    }

    @Override
    public void onSucessGetSkuExList(List<GoodsSpecDetail> list) {
        if (bargainId != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).marketingType = "1";
            }
        }
        if (assembleId != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).marketingType = "2";
            }
        }
        if (goodsSpecDialog != null) {
            goodsSpecDialog.setSkuListEx(list);
        }
        this.exSkuList = list;
    }

    IntegralModel mvipInfo = new IntegralModel();

    @Override
    public void onVipInfoSuccess(IntegralModel vipInfo) {
        mvipInfo = vipInfo;
//        if(BuildConfig.DEBUG){
//            mvipInfo.SYJFTOT="9999";
//        }
        if (goodsSpecDialog != null) {
            goodsSpecDialog.setVipInfo(vipInfo);
        }
        if (pointValue != null && mvipInfo != null) {
            pointValue.setText(vipInfo.SYJFTOT);
            checkPointUnder();
        }
    }

    @Override
    public void sucessGetCouponList(List<CouponInfoZ> result) {
        mallGoodsDetialDiscountAdapter.setId(goodsShopId, goodsDetail.id, goodsDetail.userId);
        if (result != null && result.size() > 0) {
            mallGoodsDetialDiscountAdapter.setModel("");
            mallGoodsDetialDiscountAdapter.setCouponList(result);
        }

    }

    @Override
    public void sucessGetActShop(ActVip.VipShop result) {
        if (result != null) {
            vipShop = result;
            try {
                serviceGoodsDetailPresenter.getActVip(new SimpleHashMapBuilder<String, Object>()
                        .puts("Command", "pcPreCalcGoodsPop")
                        .puts("AppID", result.ytbAppId)
                        .puts("GoodsID", goodsDetail.goodsChildren.get(0).barcodeSku)
                        .puts("LoginSequence", "7B2978F6")
                        .puts("DepartID", result.ytbDepartID));
            } catch (Exception e) {
                e.printStackTrace();
            }
            buildHasGoods();
        } else {
            if (!TextUtils.isEmpty(marketingType)) {
                if (chechNowShopIdInCanChose(goodsDetail))
                    if (showNoActTime == 0) {
                        if (!TextUtils.isEmpty(marketingType)) {
                            if ("1".equals(marketingType)) {//砍价活动则进行砍价布置
                                NoActDialog.newInstance("抱歉！您来晚了\n砍价活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                            } else if ("2".equals(marketingType)) {//
                                NoActDialog.newInstance("抱歉！您来晚了\n拼团活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                            } else if ("3".equals(marketingType)) {//
                                NoActDialog.newInstance("抱歉！您来晚了\n秒杀活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                            } else if ("4".equals(marketingType)) {//
                                NoActDialog.newInstance("抱歉！您来晚了\n新客活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                            } else {
                                NoActDialog.newInstance("抱歉！您来晚了\n活动已经结束~").show(getSupportFragmentManager(), "过期活动");
                            }
                        }
                        showNoActTime++;
                    }
            }
        }

    }

    @Override
    public void successGetActVip(ActVip result) {
        if (result != null && result.PopInfo != null && result.PopInfo.size() > 0) {
            result.setVipShop(vipShop);
            mallGoodsDetialDiscountAdapter.setModel("");
            mallGoodsDetialDiscountAdapter.setActVip(result);
            goodsDetail.actVip = result;
        }
    }


    /**
     * 门店详情回调
     *
     * @param storeDetialModel 门店详情数据模型
     */
    @Override
    public void onGetStoreDetialSuccess(ShopDetailModel storeDetialModel) {
        if (newStoreDetialModelSelect != null) {
            storeDetialModel.distance = newStoreDetialModelSelect.distance;
        }
        if (storeDetialModel != null) {
            newStoreDetialModelSelect = storeDetialModel;
            goodsSpecDetail.setShopDetailModelSelect(newStoreDetialModelSelect);
        }
        if ("2".equals(goodsDetail.goodsType) && !"5".equals(goodsDetail.marketingType)) {
            goodsShopId = newStoreDetialModelSelect.id;
            goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
            isErrorOversold();
            System.out.println("原因分析:3352");
        } else {
            if (newStoreDetialModelSelect != null && storeDetialModelList != null && storeDetialModelList.size() > 0) {
                mallGoodsDetialStoreAdapter.setModel(newStoreDetialModelSelect);
                mallGoodsDetialStoreAdapter.notifyDataSetChanged();
                goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
                System.out.println("原因分析:3358");
                isErrorOversold();
                System.out.println("原因分析:3359");
                return;
            }
            if (!ListUtil.checkObjIsInList(Arrays.asList(goodsDetail.getGoodsShopIdListStringArray()),
                    storeDetialModel.id)) {//判断在不在适用门店里面 切换门店不在适用门店里
                if (goodsDetail.getGoodsShopIdListStringArray() == null || goodsDetail.getGoodsShopIdListStringArray().length == 0) {
//                    Toast.makeText(mContext,"未配置服务门店",Toast.LENGTH_SHORT).show();
                }
                goodsShopId = null;
                newStoreDetialModelSelect = null;
                serviceGoodsDetailPresenter.getStoreList(shopId);
            } else {
                goodsShopId = storeDetialModel.id;
                newStoreDetialModelSelect = null;
                mallGoodsDetialStoreAdapter.setModel(storeDetialModel);
                mallGoodsDetialStoreAdapter.setOnlyOnePiece(true);
                serviceGoodsDetailPresenter.getStoreList(goodsShopId);
            }
            if (newStoreDetialModelSelect != null) {
                goodsShopId = newStoreDetialModelSelect.id;
                goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
                isErrorOversold();
                System.out.println("原因分析:3380");
            }

        }

    }

    /**
     * 单规格用于判断是否负库存超卖
     */
    private void isErrorOversold() {
        isZero = false;
        buildNoError();
        if (newStoreDetialModelSelect == null) {
            return;
        }
        serviceGoodsDetailPresenter.getGoodsTran(new SimpleHashMapBuilder<String, Object>()
                .puts("shopId", newStoreDetialModelSelect.id)
                .puts("userId", goodsDetail.userId)
                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG)));
        if (goodsDetail.cheIsNoSku()) {//说明无规格 //则需要判断是不是0库存
//            if(BuildConfig.DEBUG){
//                if(goodsDetail.marketingGoodsChildren!=null){
//                    goodsDetail.marketingGoodsChildren.get(0).availableInventory=0;
//                }
//            }
            if (TextUtils.isEmpty(marketingType) && goodsDetail.marketingType == null) {//普通商品
                if (goodsDetail.goodsChildren == null || goodsDetail.goodsChildren.size() == 0) {
                    showToast("商品数据出错");
                    buildError();
                    return;
                }
                if (goodsDetail.goodsChildren.get(0).getAvailableInventory() <= 0) {//0库存了 说明要改了
                    isZero = true;
                    showToast("该商品已售罄，看看其他商品吧~");
                    buildError();
                }
            } else {
                if (!TextUtils.isEmpty(bargainId)) {//砍价拼团
                    if (goods2ModelKick.bargainInfoDTO == null || goods2ModelKick.bargainInfoDTO.marketingGoodsChildDTOS == null || goods2ModelKick.bargainInfoDTO.marketingGoodsChildDTOS.size() == 0 || goods2ModelKick.bargainInfoDTO.marketingGoodsChildDTOS.get(0).availableInventory <= 0) {
                        if (showNoActTime == 0) {
                            NoActDialog.newInstance("抱歉！您来晚了\n砍价商品已抢完~").show(getSupportFragmentManager(), "过期活动");
                            showNoActTime++;
                            goods2ModelKick = null;
                        }
                        return;
                    }
                } else if (!TextUtils.isEmpty(assembleId)) {
                    if (goods2ModelPin == null || goods2ModelPin.assembleDTO == null || goods2ModelPin.assembleDTO.marketingGoodsChildDTOS == null || goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.size() == 0 || goods2ModelPin.assembleDTO.marketingGoodsChildDTOS.get(0).availableInventory <= 0) {
                        if (showNoActTime == 0) {
                            NoActDialog.newInstance("抱歉！您来晚了\n拼团商品已抢完~").show(getSupportFragmentManager(), "过期活动");
                            showNoActTime++;
                            goods2ModelPin = null;
                        }
                        return;
                    }
                } else if ("5".equals(goodsDetail.marketingType)) {
                    checkPointUnder();
                } else {//普通商品进来的需要变商品为普通主要体现在秒杀
                    if (goodsDetail.marketingGoodsChildren.get(0).getAvailableInventoryMark(goodsDetail.goodsChildren.get(0)) <= 0) {
                        if (chechNowShopIdInCanChose(goodsDetail)) {
                            if (showNoActTime == 0) {
                                if (!TextUtils.isEmpty(marketingType)) {
                                    if ("1".equals(marketingType)) {//砍价活动则进行砍价布置
                                        NoActDialog.newInstance("抱歉！您来晚了\n砍价商品已抢完~").show(getSupportFragmentManager(), "过期活动");
                                    } else if ("2".equals(marketingType)) {//
                                        NoActDialog.newInstance("抱歉！您来晚了\n拼团商品已抢完~").show(getSupportFragmentManager(), "过期活动");
                                    } else if ("3".equals(marketingType)) {//
                                        NoActDialog.newInstance("抱歉！您来晚了\n秒杀商品已抢完~").show(getSupportFragmentManager(), "过期活动");
                                    } else if ("4".equals(marketingType)) {//
                                        NoActDialog.newInstance("抱歉！您来晚了\n新客商品已抢完~").show(getSupportFragmentManager(), "过期活动");
                                    } else {
                                        NoActDialog.newInstance("抱歉！您来晚了\n活动商品已抢完~").show(getSupportFragmentManager(), "过期活动");
                                    }
                                }
                                showNoActTime++;
                            }
                            if (TextUtils.isEmpty(marketingType)) {
                                successGetGoodsDetailNormal(this.goodsDetail);
                            }
                            return;
                        }
                    }
                }
            }
        } else {
            if ("5".equals(goodsDetail.marketingType)) {
                checkPointUnder();
            }
        }
    }

    private boolean chechNowShopIdInCanChose(GoodsDetail goodsDetail) {
        if (goodsDetail != null) {//然后判断
            String[] choselist = goodsDetail.getGoodsShopIdListStringArray();
            if (choselist == null || choselist.length == 0) {
                return true;
            }
            for (int i = 0; i < choselist.length; i++) {
                if (choselist[i].equals(goodsShopId)) {
                    return true;
                }
            }
        }
        return false;
    }

    List<ShopDetailModel> storeDetialModelList;
    boolean isShowServiceToast = false;

    @Override
    public void onGetStoreListSuccess(List<ShopDetailModel> list) {
        this.storeDetialModelList = buildFlashListWithStore(list, goodsDetail.getGoodsShopIdListStringArray());
        if (storeDetialModelList.size() > 0 && (("1".equals(goodsDetail.goodsType) || "5".equals(goodsDetail.marketingType)))) {
            if (newStoreDetialModelSelect == null) {
                if (!isShowServiceToast) {
                    if ("1".equals(goodsDetail.goodsType)) {
                        Toast.makeText(mContext, "憨妈妈将切换至离您最近的服务门店", Toast.LENGTH_LONG).show();
                        isShowServiceToast = true;
                    }
                }
                Collections.sort(storeDetialModelList);
                if ("5".equals(goodsDetail.marketingType)) {
                    int goodshopindex = ListUtil.checkObjIsInListWithResult(new SimpleArrayListBuilder<String>().putList(storeDetialModelList, new ObjectIteraor<ShopDetailModel>() {
                        @Override
                        public String getDesObj(ShopDetailModel o) {
                            return o.id;
                        }
                    }), shopId);
                    if (-1 != goodshopindex) {
                        goodsShopId = storeDetialModelList.get(goodshopindex).id;//进行赋值然后重新getData
                        newStoreDetialModelSelect = storeDetialModelList.get(goodshopindex);
                        mallGoodsDetialStoreAdapter.setModel(newStoreDetialModelSelect);
                        mallGoodsDetialStoreAdapter.setOnlyOnePiece(buildFlashList(new SimpleArrayListBuilder<GoodsShop>().putList(storeDetialModelList, new ObjectIteraor<ShopDetailModel>() {
                            @Override
                            public GoodsShop getDesObj(ShopDetailModel o) {
                                return new GoodsShop(o);
                            }
                        }), goodsDetail.getGoodsShopIdListStringArray()).size() <= 1);
                        getData();
                        return;
                    }

                }
                if (!storeDetialModelList.get(0).id.equals(goodsShopId)) {//存在门店不一致的情况 直接对
                    goodsShopId = storeDetialModelList.get(0).id;//进行赋值然后重新getData
                    newStoreDetialModelSelect = storeDetialModelList.get(0);
                    mallGoodsDetialStoreAdapter.setModel(newStoreDetialModelSelect);
                    mallGoodsDetialStoreAdapter.setOnlyOnePiece(buildFlashList(new SimpleArrayListBuilder<GoodsShop>().putList(storeDetialModelList, new ObjectIteraor<ShopDetailModel>() {
                        @Override
                        public GoodsShop getDesObj(ShopDetailModel o) {
                            return new GoodsShop(o);
                        }
                    }), goodsDetail.getGoodsShopIdListStringArray()).size() <= 1);
                    getData();
                    return;
                }

                goodsShopId = storeDetialModelList.get(0).id;
                newStoreDetialModelSelect = storeDetialModelList.get(0);
                serviceGoodsDetailPresenter.getStoreDetial(goodsShopId);
            }
            mallGoodsDetialStoreAdapter.setOnlyOnePiece(buildFlashList(new SimpleArrayListBuilder<GoodsShop>().putList(storeDetialModelList, new ObjectIteraor<ShopDetailModel>() {
                @Override
                public GoodsShop getDesObj(ShopDetailModel o) {
                    return new GoodsShop(o);
                }
            }), goodsDetail.getGoodsShopIdListStringArray()).size() <= 1);
        }
        try {
            goodsShopId = newStoreDetialModelSelect.id;
            mallGoodsDetialStoreAdapter.setModel(newStoreDetialModelSelect);
            goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isErrorOversold();


    }

    @Override
    public void onGetGoodsRankListSuccess(List<GoodsRank> list) {
        if (!ListUtil.isEmpty(list)) {
            goodsDetailRankingListAdapter.setRankData(list);
            goodsDetailRankingListAdapter.setModel("data");
            goodsShareCoupon.setVisibility(View.VISIBLE);
            goodsShareCoupon.startLoopScaleAuto();
            goodsShareCoupon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeckShareDialog dialog = SeckShareDialog.newInstance();
                    dialog.setGoodsDetail(2, 3, goodsDetail, goodsShopId, goodsDetail.userId + "");
                    dialog.show(getSupportFragmentManager(), "分享");
                }
            });
        } else {
            goodsShareCoupon.setVisibility(View.GONE);
            goodsDetailRankingListAdapter.clear();
        }
    }

    boolean isRunshow = false;
    boolean isStop = false;

    private void runShowEveryMan() {
        if (isRunshow || isStop) {
            return;
        }
        isRunshow = true;
        mallGoodsDetialTopAdapter.setMallGroupSimple(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < kkGroupSimples.size(); i++) {
                    final MallGroupSimple item = kkGroupSimples.get(i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing()) {
                                mallGoodsDetialTopAdapter.setMallGroupSimple(item);

                            }

                        }
                    });
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing()) {
                                mallGoodsDetialTopAdapter.setMallGroupSimple(null);
                            }
                        }
                    });
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            kkGroupSimples.clear();
                            serviceGoodsDetailPresenter.getGroupAlreadyList(new SimpleHashMapBuilder<String, Object>()
                                    .puts("assembleId", assembleId)
                                    .puts("type",goodsDetail.shareGiftDTOS.size()>0?"1":null)
                            );
                            isRunshow = false;
                        }

                    }
                });


            }
        }).start();
    }

    public void getGroupAll() {
        serviceGoodsDetailPresenter.getGroupAlreadyList(new SimpleHashMapBuilder<String, Object>()
                .puts("assembleId", assembleId)
                .puts("type",(goodsDetail!=null&&goodsDetail.shareGiftDTOS.size()>0)?"1":null)
        );
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishLoadMore();
        layoutRefresh.finishRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        recommandMap.put("shopId", goodsShopId);
        recommandMap.put("pageNum", page + "");
        serviceGoodsDetailPresenter.getGoodsRecommendUnder(recommandMap);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

        page = 1;
        recommandMap.put("pageNum", page + "");
        getData();
    }


    GoodsTranDialog goodsTranDialog;
    GoodsActRuleDialog goodsActRuleDialog;

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("更多套餐".equals(function)) {
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_GOODS_SET)
                    .withString("id", goodsDetail.id + "")
                    .withString("shopId", goodsShopId)
                    .navigation();
        }
        if ("可选门店".equals(function)) {
            if (shopOrderPickDialog == null) {
                shopOrderPickDialog = ShopOrderPickDialog.newInstance();
            }
            try {
                shopOrderPickDialog.setSelectId(newStoreDetialModelSelect.id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            shopOrderPickDialog.show(getSupportFragmentManager(), "选择门店");
            shopOrderPickDialog.setGoodsShopList(buildFlashList(new SimpleArrayListBuilder<GoodsShop>().putList(storeDetialModelList, new ObjectIteraor<ShopDetailModel>() {
                @Override
                public GoodsShop getDesObj(ShopDetailModel o) {
                    return new GoodsShop(o);
                }
            }), goodsDetail.getGoodsShopIdListStringArray()));
            shopOrderPickDialog.setTitle("选择门店");
            shopOrderPickDialog.setOnDialogShopClickListener(new ShopOrderPickDialog.OnDialogShopClickListener() {
                @Override
                public void onDialogShopClick(GoodsShop goodsShop) {
                    ShopDetailModel newStoreDetialModelNow = new ShopDetailModel(goodsShop);
                    newStoreDetialModelSelect = newStoreDetialModelNow;
                    goodsShopId = newStoreDetialModelSelect.id;
                    mallGoodsDetialStoreAdapter.setModel(newStoreDetialModelSelect);
                    mallGoodsDetialStoreAdapter.notifyDataSetChanged();
                    getData();
//                    goodsDetail.setStoreDetialModel(newStoreDetialModelSelect);
//                    isErrorOversold();
                }
            });
        }
        if ("更多邮费".equals(function)) {
            if (goodsTranDialog == null) {
                goodsTranDialog = GoodsTranDialog.newInstance();
            }

            goodsTranDialog.show(getSupportFragmentManager(), "更多邮费");
            goodsTranDialog.setTranDetail(goodsTran);
        }
        if ("更多规则".equals(function)) {
            if (goodsActRuleDialog == null) {
                goodsActRuleDialog = GoodsActRuleDialog.newInstance();
            }

            goodsActRuleDialog.show(getSupportFragmentManager(), "更多规则");
            goodsActRuleDialog.setTranDetail(actrule);
        }
        if ("更多推荐".equals(function)) {
            topTab.setCurrentTab(2);
            virtualLayoutManager.scrollToPositionWithOffset(tab3, 0);
        }
        if ("更多服务".equals(function)) {
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_GOODS_SERVICE)
                    .navigation();
        }
        if ("倒计时结束".equals(function)) {
            getData();
        }
        if ("shareRank".equals(function)){
            SeckShareDialog dialog = SeckShareDialog.newInstance();
            dialog.setGoodsDetail(2, 3, goodsDetail, goodsShopId, goodsDetail.userId + "");
            dialog.show(getSupportFragmentManager(), "分享");
        }
    }

    @Override
    public String getSurl() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_BargainUrl);
        String url = String.format("%s?type=8&scheme=%s&goodsId=%s", urlPrefix, "NormGoodsDetail", id);
        return url;
    }

    @Override
    public String getSdes() {
        if ("3".equals(goodsDetail.marketingType)) {
            return "超低价格秒杀，尽在憨妈妈，赶紧来抢>>";
        }
        return "我在憨妈妈发现了一个不错的商品，赶快来看看吧。";
    }

    @Override
    public String getStitle() {
        if ("3".equals(goodsDetail.marketingType)) {
            return "【限时秒杀 " + FormatUtils.moneyKeep2Decimals(goodsDetail.marketingGoodsChildren.get(0).marketingPrice) + "元】" + goodsDetail.goodsTitle;
        }
        return goodsDetail.goodsTitle;
    }

    @Override
    public Bitmap getsBitmap() {
        return sBitmap;
    }

    @Override
    public void shareResult(SHARE_MEDIA shareMedia) {
//        super.shareResult(shareMedia);
        if (shareMedia == SHARE_MEDIA.WEIXIN) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "商品详情页分享微信");
            MobclickAgent.onEvent(mContext, "btn_APP__MaternalandChildGoods_CommodityDetails_ShareWeChat", nokmap);
        }
        if (shareMedia == SHARE_MEDIA.QQ) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "商品详情页分享QQ");
            MobclickAgent.onEvent(mContext, "btn_APP__MaternalandChildGoods_CommodityDetails_ShareQQ", nokmap);
        }
        if (shareMedia == SHARE_MEDIA.QZONE) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "商品详情页分享QQ空间");
            MobclickAgent.onEvent(mContext, "btn_APP__MaternalandChildGoods_CommodityDetails_ShareQQZone", nokmap);
        }
        if (shareMedia == SHARE_MEDIA.WEIXIN_CIRCLE) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "商品详情页分享朋友圈");
            MobclickAgent.onEvent(mContext, "btn_APP__MaternalandChildGoods_CommodityDetails_ShareCircleOfFriends", nokmap);
        }
    }

    GoodsVideoSPresenter goodsVideoSPresenter;

    public void getSucessGetvideoS(VideoResult videoResult) {

        dargF = findViewById(R.id.dargF);
        dargF.setVisibility(View.GONE);
        if (videoResult != null) {
            course_id = videoResult.courseId;
            liveStatus = videoResult.courseFlag + "";
            if (videoResult.courseFlag == 2) {
                dargF.setVisibility(View.VISIBLE);
                dargM.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(course_id)) {
                            stopOnlineVideoFloat();

                            ARouter.getInstance()
                                    .build(TencentLiveRoutes.LiveNotice)
                                    .withString("courseId", course_id)
                                    .navigation();

//                            ARouter.getInstance()
//                                    .build(TencentLiveRoutes.LIVE_LOOK)
//                                    .withString("courseId", course_id)
//                                    .navigation();
                        }

                    }
                });

            }
            goodsVideoSPresenter.getVideoToken(new SimpleHashMapBuilder<String, Object>()
                    .puts("course_id", videoResult.courseId)
                    .puts("nickname", SpUtils.getValue(mContext, SpKey.USER_NICK))
                    .puts("liveStatus", videoResult.courseFlag + ""));
        }

    }

    @Override
    public void getSucessGetvideoToken(String token) {
        this.token = token;
        startOnlineVideoFloat();
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getCourseId() {
        return course_id;
    }

    @Override
    public String getLiveStatus() {
        return liveStatus;
    }

    @Override
    public Handler getVideoHandler() {
        return null;
    }


    @Override
    public void onGoDialogClick(View v, AssemableTeam firstcoupon) {
        teamNum = firstcoupon.teamNum;
        showSkuPin(true);
    }

    private List<GoodsShop> buildFlashList(List<GoodsShop> goodsShopServiceList, String[] shopIdList) {//获取门店过滤列表

        List<GoodsShop> result = new ArrayList<>();
        for (int i = 0; i < goodsShopServiceList.size(); i++) {
            GoodsShop goodsShop = goodsShopServiceList.get(i);
            if (checkNowServiceShopIsRealy(goodsShop.shopId, shopIdList)) {
                result.add(goodsShopServiceList.get(i));
            }
        }
        return result;
    }

    private List<ShopDetailModel> buildFlashListWithStore(List<ShopDetailModel> goodsShopServiceList, String[] shopIdList) {//获取门店过滤列表

        List<ShopDetailModel> result = new ArrayList<>();
        for (int i = 0; i < goodsShopServiceList.size(); i++) {
            ShopDetailModel goodsShop = goodsShopServiceList.get(i);
            if (checkNowServiceShopIsRealy(goodsShop.id, shopIdList)) {
                System.out.println("可以选择的门店有:" + goodsShop.shopName);
                result.add(goodsShopServiceList.get(i));
            }
//            if((shopIdList==null||shopIdList.length == 0)&&goodsShop.shopType==1){
//                result.add(goodsShopServiceList.get(i));
//            }
        }
        return result;
    }

    private boolean checkNowServiceShopIsRealy(String shopId, String[] shopIdList) {
        if (shopIdList == null || shopIdList.length == 0) {
            return false;
        }
        for (int i = 0; i < shopIdList.length; i++) {
            if (shopId.equals(shopIdList[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSucessCheckPlus(boolean isplus) {
        SpUtils.store(mContext, SpKey.PLUSSTATUS, isplus);
    }
}
