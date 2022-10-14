package com.health.city.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.lib_ShapeView.view.ShapeTextView;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.health.city.R;
import com.health.city.adapter.CityHotTipAdapter;
import com.health.city.contract.TipListContract;
import com.health.city.presenter.TipListPresenter;
import com.healthy.library.LibApplication;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.AdContract;
import com.healthy.library.contract.DataStatisticsContract;
import com.healthy.library.contract.QiYeWeiXinContract;
import com.healthy.library.loader.ImageNetAdapter;
import com.healthy.library.message.UpdateUpIcon;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.ColorInfo;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.QiYeWeXin;
import com.healthy.library.model.QiYeWeXinKey;
import com.healthy.library.model.QiYeWeXinWorkShop;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.model.Topic;
import com.healthy.library.presenter.AdPresenter;
import com.healthy.library.presenter.DataStatisticsPresenter;
import com.healthy.library.presenter.QiYeWeiXinPresenter;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.utils.AppBarLayoutStateChangeListener;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.MMiniPass;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentPostNewList extends BaseFragment implements AdContract.View, TipListContract.View, OnRefreshLoadMoreListener, DataStatisticsContract.View,QiYeWeiXinContract.View{

    private SmartRefreshLayout refreshLayout;
    private AppBarLayout appBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout noFollowMessageLL;
    private TextView noFollowMessage;
    private ConstraintLayout hotTip;
    private ConstraintLayout hottipTitleCon;
    private TextView hottipTitle;
    private RecyclerView tipGrid;
    private View divider;
    private StatusLayout layoutStatus;
    private SlidingTabLayout st;
    private View dividerStore;
    private ViewPager vp;
    private ConstraintLayout needS;
    private ImageView passToSendPost;
    private ImageView passToTop;
    private TextView passTmp;
    private int currentPage = 1;
    private TipListPresenter tipListPresenter;
    private Banner banner;
    private ConstraintLayout banner_view;

    private FragmentPostNewListChild leftfragmentPostList;
    private FragmentPostNewListChild rightfragmentPostList;
    private List<Fragment> fragments = new ArrayList<>();
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    int currentIndex = 2;
    public List<QiYeWeXin> qiYeWeXins;
    private String areaNo = "";
    private Banner mallBanner;
    private FragmentPostNewListChild mActivityFragmentPostList;
    private CityHotTipAdapter mCityHotTipAdapter;
    private TextView mHotTipAll;
    private int mSllLeftMargin;
    private LinearLayout mSllContent;
    private float mTopLeftRadius;
    private DataStatisticsPresenter dataStatisticsPresenter;
    private int mSllContentColor;
    private QiYeWeiXinPresenter qiYeWeiXinPresenter;
    private Handler mHandler = new Handler();

    public static FragmentPostNewList newInstance(Map<String, Object> maporg) {
        FragmentPostNewList fragment = new FragmentPostNewList();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.city_fragment_child_new;
    }

    AdPresenter adPresenter;

    @Override
    protected void onLazyData() {
        super.onLazyData();
        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "3").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
        dataStatisticsPresenter = new DataStatisticsPresenter(mContext, this);
        tipListPresenter.getTipList(getBasemap());
    }

    @Override
    protected void findViews() {
        initView();
        adPresenter = new AdPresenter(mContext, this);
        qiYeWeiXinPresenter=new QiYeWeiXinPresenter(mContext,this);

        passToSendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if("关注".equals(fragmentType)){
//                    Map nokmap = new HashMap<String, String>();
//                    nokmap.put("soure","关注列表");
//                    MobclickAgent.onEvent(mContext, "event2PostFrom",nokmap);
//                }
//                if("本地".equals(fragmentType)){
//                    Map nokmap = new HashMap<String, String>();
//                    nokmap.put("soure","本地列表");
//                    MobclickAgent.onEvent(mContext, "event2PostFrom",nokmap);
//                }
//                if("发现".equals(fragmentType)){
//                    Map nokmap = new HashMap<String, String>();
//                    nokmap.put("soure","发现列表");
//                    MobclickAgent.onEvent(mContext, "event2PostFrom",nokmap);
//                }
                ARouter.getInstance()
                        .build(CityRoutes.CITY_POSTSEND)
                        .navigation();
            }
        });
        areaNo = LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE);
        if (TextUtils.isEmpty(areaNo)) {
            areaNo = "0";
        }
        try {
            getBasemap().put("addressCity", (Integer.parseInt(areaNo) / 100 * 100) + "");
            getBasemap().put("addressProvince", (Integer.parseInt(areaNo) / 10000 * 10000) + "");
            getBasemap().put("addressArea", areaNo + "");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        refreshLayout.setEnableLoadMore(false);
        hotTip.setVisibility(View.VISIBLE);
        getBasemap().put("currentPage", currentPage + "");
        getBasemap().put("pageSize", 10 + "");
        getBasemap().put("limitsStatus", "1".equals(get("type")) ? "1" : "0");
        tipListPresenter = new TipListPresenter(mContext, this);
        List<String> titles = Arrays.asList("最新", "热门", "活动");
        Map<String, Object> leftmap = new HashMap<>();
        leftmap.putAll(getBasemap());
        leftmap.put("type", get("type") + "");
        leftmap.put("type2", "1");
        if (TextUtils.isEmpty(areaNo)) {
            areaNo = "0";
        }
        try {
            leftmap.put("addressCity", (Integer.parseInt(areaNo) / 100 * 100) + "");
            leftmap.put("addressProvince", (Integer.parseInt(areaNo) / 10000 * 10000) + "");
            leftmap.put("addressArea", areaNo + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        leftfragmentPostList = FragmentPostNewListChild.newInstance(leftmap);

        Map<String, Object> rightmap = new HashMap<>();
        rightmap.putAll(getBasemap());
        rightmap.put("type", get("type") + "");
        rightmap.put("type2", "2");
        if (TextUtils.isEmpty(areaNo)) {
            areaNo = "0";
        }
        try {
            rightmap.put("addressCity", (Integer.parseInt(areaNo) / 100 * 100) + "");
            rightmap.put("addressProvince", (Integer.parseInt(areaNo) / 10000 * 10000) + "");
            rightmap.put("addressArea", areaNo + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        rightfragmentPostList = FragmentPostNewListChild.newInstance(rightmap);

        Map<String, Object> mActivityMap = new HashMap<>();
        mActivityMap.putAll(getBasemap());
        mActivityMap.put("type", get("type") + "");
        mActivityMap.put("type2", "3");
        if (TextUtils.isEmpty(areaNo)) {
            areaNo = "0";
        }
        try {
            mActivityMap.put("addressCity", (Integer.parseInt(areaNo) / 100 * 100) + "");
            mActivityMap.put("addressProvince", (Integer.parseInt(areaNo) / 10000 * 10000) + "");
            mActivityMap.put("addressArea", areaNo + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mActivityFragmentPostList = FragmentPostNewListChild.newInstance(mActivityMap);

        fragments.add(leftfragmentPostList);
        fragments.add(rightfragmentPostList);
        fragments.add(mActivityFragmentPostList);

        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getChildFragmentManager(), fragments, titles);
            // adapter
            vp.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        vp.setOffscreenPageLimit(fragments.size());
        // bind to SmartTabLayout
        st.setViewPager(vp);
        mSllLeftMargin = ((LinearLayout.LayoutParams) mSllContent.getLayoutParams()).leftMargin;
        //mTopLeftRadius = mSllContent.getShapeDrawableBuilder().getTopLeftRadius();
        vp.setCurrentItem(currentIndex);
        st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentIndex = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        appBar.addOnOffsetChangedListener(new AppBarLayoutStateChangeListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                super.onOffsetChanged(appBarLayout, verticalOffset);
                if (verticalOffset != verticalOffsetold) {
                    if (leftfragmentPostList != null) {
                        leftfragmentPostList.setRefreshEnable(verticalOffset == 0);
                    }
                    if (rightfragmentPostList != null) {
                        rightfragmentPostList.setRefreshEnable(verticalOffset == 0);
                    }
                }
                verticalOffsetold = verticalOffset;
            }

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarLayoutStateChangeListener.State state) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mSllContent.getLayoutParams();
                switch (state) {
                    case EXPANDED:
                        //EXPANDED
                        break;
                    case COLLAPSED:
                        //折叠
                        mSllContentColor = Color.WHITE;
                        mSllContent.setBackgroundColor(mSllContentColor);
                        mSllContent.setLayoutParams(layoutParams);
                        break;
                    case INTERMEDIATE:
                        //中间状态
                        if (mSllContentColor != Color.TRANSPARENT) {
                            mSllContentColor = Color.TRANSPARENT;
                            mSllContent.setBackgroundColor(mSllContentColor);
                        }
                        break;
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBar.setOutlineProvider(null);
            collapsingToolbarLayout.setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
        refreshLayout.setOnRefreshLoadMoreListener(this);

        hottipTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCityTip();
            }
        });
    }

    int verticalOffsetold = 0;

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        //noFollowMessageLL = (LinearLayout) findViewById(R.id.noFollowMessageLL);
        //noFollowMessage = (TextView) findViewById(R.id.noFollowMessage);
        hotTip = (ConstraintLayout) findViewById(R.id.hotTip);
        //hottipTitleCon = (ConstraintLayout) findViewById(R.id.hottipTitleCon);
        hottipTitle = (TextView) findViewById(R.id.hottipTitle);
        tipGrid = findViewById(R.id.tip_grid);
        divider = (View) findViewById(R.id.divider);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        mSllContent = findViewById(R.id.sll_content);
        st = (SlidingTabLayout) findViewById(R.id.st);
        dividerStore = (View) findViewById(R.id.divider_store);
        vp = (ViewPager) findViewById(R.id.vp);
        needS = (ConstraintLayout) findViewById(R.id.need_s);
        passToSendPost = (ImageView) findViewById(R.id.passToSendPost);
        passToTop = (ImageView) findViewById(R.id.passToTop);
        passTmp = (TextView) findViewById(R.id.passTmp);
        banner = findViewById(R.id.city_ad_banner);
        //banner_view = findViewById(R.id.city_ad_banner_view);
        mallBanner = (Banner) findViewById(R.id.mall_banner);
        passToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new UpdateUpIcon(2));
                CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBar.getLayoutParams()).getBehavior();
                if (behavior instanceof AppBarLayout.Behavior) {
                    AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
                    int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();
                    if (topAndBottomOffset != 0) {
                        appBarLayoutBehavior.setTopAndBottomOffset(0);
                    }
                }
            }
        });
    }
    public void buildQWXList(LinearLayout linearLayout, List<QiYeWeXin> qiYeWeXinWorkers) {
        linearLayout.removeAllViews();
        for (int i = 0; i < qiYeWeXinWorkers.size(); i++) {
            final QiYeWeXin qiYeWeXinWorker=qiYeWeXinWorkers.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_qwx_recommand_layout, linearLayout, false);
            TextView groupnName;
            TextView groupnSecondName;
            ShapeTextView groupnButtom;
            LinearLayout pinTitleLfet;
            LinearLayout pinHeadIconLL;
            TextView pinMenber;
            groupnName = (TextView) view.findViewById(R.id.groupnName);
            groupnSecondName = (TextView) view.findViewById(R.id.groupnSecondName);
            groupnButtom = (ShapeTextView) view.findViewById(R.id.groupnButtom);
            pinTitleLfet = (LinearLayout)view. findViewById(R.id.pinTitleLfet);
            pinHeadIconLL = (LinearLayout)view. findViewById(R.id.pinHeadIconLL);
            pinMenber = (TextView) view.findViewById(R.id.pinMenber);
            groupnName.setText(qiYeWeXinWorker.groupName);
            groupnSecondName.setText(qiYeWeXinWorker.groupIntroduce);
            List<String> mUrlList = qiYeWeXinWorker.faceUrlList;
            groupnButtom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MMiniPass.passMini("gh_f9b4fbd9d3b8",String.format("pages/mine/servicer/jionGroup?merchantId=%s&shopId=%s&workcode=%s&type=%s&groupId=%s"
                            , SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_MC)
                            ,SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP)
                            ,""
                            ,"3"
                            ,qiYeWeXinWorker.id
                    ));
                }
            });
            if (!ListUtil.isEmpty(mUrlList)) {
                pinHeadIconLL.removeAllViews();
                int forSize = 3;
                if (forSize > mUrlList.size()) {//避免数组越界
                    forSize = mUrlList.size();
                }
                for (int j = 0; j < forSize; j++) {
                    CornerImageView cornerImageView = new CornerImageView(mContext);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) TransformUtil.dp2px(mContext, 16),
                            (int) TransformUtil.dp2px(mContext, 16));
                    cornerImageView.setIsCircle(true);
                    if (j > 0) {
                        layoutParams.leftMargin = -10;
                    }
                    cornerImageView.setLayoutParams(layoutParams);
                    GlideCopy.with(cornerImageView.getContext())
                            .load(mUrlList.get(j))
                            .error(R.drawable.img_avatar_default)
                            .into(cornerImageView);
                    pinHeadIconLL.addView(cornerImageView);
                }
            }
            linearLayout.addView(view);
        }
    }
    /**
     * 打开全部话题
     */
    private void openCityTip() {
        String fragmentType = "";
        if ("1".equals(get("type") + "")) {
            fragmentType = "本地";
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "本地列表");
            MobclickAgent.onEvent(mContext, "event2TipBoardClick", nokmap);
        }
        if ("2".equals(get("type") + "")) {
            fragmentType = "发现";
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "发现列表");
            MobclickAgent.onEvent(mContext, "event2TipBoardClick", nokmap);
        }

        ARouter.getInstance()
                .build(CityRoutes.CITY_TIPBOARD)
                .withString("fragmentType", fragmentType)
                .navigation();
    }

    @Override
    public void onSuccessGetTipList(List<Topic> topics, PageInfoEarly pageInfoEarly) {
        //addImages(mContext, tipGrid, topics);
        if (mCityHotTipAdapter == null) {
            mCityHotTipAdapter = new CityHotTipAdapter();
            tipGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
            tipGrid.setAdapter(mCityHotTipAdapter);
            mCityHotTipAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (TextUtils.isEmpty(areaNo)) {
                        areaNo = "0";
                    }
                    Topic topic = mCityHotTipAdapter.getData().get(position);
                    if ("1".equals(get("type"))) {
                        ARouter.getInstance()
                                .build(CityRoutes.CITY_TIP)
                                .withString("activitytype", "本地")
                                .withString("cityNo", (Integer.parseInt(areaNo) / 100 * 100) + "")
                                .withString("provinceNo", (Integer.parseInt(areaNo) / 10000 * 10000) + "")
                                .withString("areaNo", areaNo)
                                .withString("topicId", topic.id)
                                .navigation();
                    } else {
                        ARouter.getInstance()
                                .build(CityRoutes.CITY_TIP)
                                .withString("activitytype", "全国")
                                .withString("topicId", topic.id)
                                .navigation();
                    }
                }
            });
        }
        if (!ListUtil.isEmpty(topics) && topics.size() > 8) {
            mCityHotTipAdapter.setNewData(topics.subList(0, 8));
        } else {
            mCityHotTipAdapter.setNewData(topics);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        ishead = false;
        if (mHandler == null) {
            mHandler = new Handler();
        }

        qiYeWeiXinPresenter.getRecommandWeiXinGroup(new SimpleHashMapBuilder<>());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipListPresenter.getTipList(getBasemap());
                adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "3").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG)));
                leftfragmentPostList.onRefresh(null);
                rightfragmentPostList.onRefresh(null);
                mActivityFragmentPostList.onRefresh(null);
            }
        }, 1000);
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    private void successLocation() {

        areaNo = LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE);
        if (TextUtils.isEmpty(areaNo)) {
            areaNo = "0";
        }
        getBasemap().put("addressCity", (Integer.parseInt(areaNo) / 100 * 100) + "");
        getBasemap().put("addressProvince", (Integer.parseInt(areaNo) / 10000 * 10000) + "");
        getBasemap().put("addressArea", areaNo + "");
        tipListPresenter.getTipList(getBasemap());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateUserLocationMsg msg) {
        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "3").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG)));
        if (!isFirstLoad) return;
        onRefresh(null);
    }

    List<AdModel> adModels;

    private boolean ishead = false;

    @Override
    public void onSucessGetAds(List<AdModel> adModels) {

        this.adModels = adModels;
        if (adModels.size() > 0) {
            mallBanner.setVisibility(View.VISIBLE);
            if (!ishead) {
                AdModel adModel = adModels.get(0);
                if (adModel.type == 2 && adModel.triggerPage == 3) {
                    //System.out.println("加载本地发现");
                    buildHeadView(adModels);
                }
                ishead = true;
            }
        } else {
            mallBanner.setVisibility(View.GONE);
        }

    }

    private void buildHeadView(final List<AdModel> bannerimgs) {
        List<ColorInfo> colorList = new ArrayList<>();

        ImageNetAdapter imageLoader;
        int count;
        if (bannerimgs != null && bannerimgs.size() > 0) {
            colorList.clear();
            count = bannerimgs.size();
            for (int j = 0; j < bannerimgs.size(); j++) {
                ColorInfo info = new ColorInfo();
                info.setImgUrl(bannerimgs.get(j).photoUrls);
                colorList.add(info);

            }
            for (int j = 0; j < colorList.size(); j++) {
                try {
                    colorList.get(j).setPerfectColor(Color.parseColor(bannerimgs.get(j).colorValue));
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
            imageLoader = new ImageNetAdapter(new SimpleArrayListBuilder<String>().putList(bannerimgs, new ObjectIteraor<AdModel>() {
                @Override
                public Object getDesObj(AdModel adModel) {
                    return adModel.photoUrls;
                }
            }), TransformUtil.dp2px(mContext, 10f), colorList);
            mallBanner.setAdapter(imageLoader).setIndicator(new RectangleIndicator(mContext))
                    .setIndicatorGravity(IndicatorConfig.Direction.CENTER);//设置指示器位置（左，中，右）
            mallBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(Object data, int position) {
                    AdModel adModel = bannerimgs.get(position);
                    dataStatisticsPresenter.bannerClickNum(new SimpleHashMapBuilder<String, Object>().puts("advertisingId", bannerimgs.get(position).id));
                    MARouterUtils.passToTarget(mContext, adModel);
                }

                @Override
                public void onBannerChanged(int position) {

                }
            });
            //System.out.println("修改背景版333");
//            mallBanner.setIndicator(new CircleIndicator(mContext));
            mallBanner.stop();
            mallBanner.start();

        }
    }

    //    public void setBanner(){
//        List<String> data = new ArrayList<>();
//        data.add("https://hmm-public.oss-cn-beijing.aliyuncs.com/back-config/banner/motherservice_1.png");
//        data.add("https://hmm-public.oss-cn-beijing.aliyuncs.com/back-config/banner/index_1_200326.png");
//        data.add("https://hmm-public.oss-cn-beijing.aliyuncs.com/back-config/banner/motherservice_3.png");
//        List<ColorInfo> colorInfo = new ArrayList<>();
//        for (int i = 0;i<=data.size();i++){
//            ColorInfo info = new ColorInfo();
//            if (i==0){
//                info.setImgUrl(data.get(i));
//            }else if(i==data.size()+1){
//                info.setImgUrl(data.get(0));
//            }else{
//                info.setImgUrl(data.get(i-1));
//            }
//            colorInfo.add(info);
//        }
//
//        banner.setAdapter(new ImageNetAdapter(data,TransformUtil.dp2px(getActivity(), 10f),colorInfo))
//                .setIndicator(new CircleIndicator(getActivity()))
//                .start();
//    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUpIcon(UpdateUpIcon msg) {
        if (msg.flag == 0) {
            passToTop.setVisibility(View.GONE);
        } else {
            passToTop.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSucessGetWeiXinKey(QiYeWeXinKey qiYeWeXinKey) {

    }

    @Override
    public void onSucessGetRecommandWeiXinGroup(List<QiYeWeXin> qiYeWeXins) {
        LinearLayout recommandQWX = (LinearLayout) findViewById(R.id.recommandQWX);
        buildQWXList(recommandQWX,qiYeWeXins);
    }

    @Override
    public void onSucessGetRecommandWorkerGroup(List<QiYeWeXinWorkShop> qiYeWeXins) {

    }

    @Override
    public void onSucessGetMineWorker(TokerWorkerInfoModel tokerWorkerInfoModel) {

    }

    @Override
    public void onSucessGetPublicWorker(TokerWorkerInfoModel tokerWorkerInfoModel) {

    }
}
