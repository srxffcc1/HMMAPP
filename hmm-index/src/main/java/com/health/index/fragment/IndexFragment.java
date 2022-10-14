package com.health.index.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.donkingliang.consecutivescroller.ConsecutiveScrollerLayout;
import com.example.lib_ShapeView.builder.ShapeDrawableBuilder;
import com.example.lib_ShapeView.builder.TextColorBuilder;
import com.example.lib_ShapeView.layout.ShapeRelativeLayout;
import com.example.lib_ShapeView.view.ShapeTextView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.health.index.BuildConfig;
import com.health.index.R;
import com.health.index.adapter.IndexBannerAdapter;
import com.health.index.adapter.IndexFeaturedAdapter;
import com.health.index.adapter.IndexFunctionGridAdapter;
import com.health.index.adapter.IndexHeadlinesAdapter;
import com.health.index.adapter.IndexRecommendAdapter;
import com.health.index.adapter.IndexServiceProjectAdapter;
import com.health.index.adapter.IndexStatusAdapter;
import com.health.index.adapter.IndexTabClassListAdapter;
import com.health.index.contract.IndexMainContract;
import com.health.index.model.IndexBean;
import com.health.index.model.UserInfoExModel;
import com.health.index.presenter.IndexMainPresenter;
import com.health.index.widget.ChosePopupWindow;
import com.healthy.library.LibApplication;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.adapter.IndexAsKingAdapter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.ADDialogView;
import com.healthy.library.business.DownLoadFragment;
import com.healthy.library.business.MessageDialog;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.GuideViewHelp;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Ids;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.AdContract;
import com.healthy.library.contract.ChannelContract;
import com.healthy.library.contract.DataStatisticsContract;
import com.healthy.library.contract.HanMomContract;
import com.healthy.library.contract.LocVipContract;
import com.healthy.library.dialog.UpdateUserInfoDialog;
import com.healthy.library.interfaces.IRouterLink;
import com.healthy.library.interfaces.IsFirstFragment;
import com.healthy.library.interfaces.OnCustomRetryListener;
import com.healthy.library.interfaces.OnNotificationListener;
import com.healthy.library.message.CanUpdateTab;
import com.healthy.library.message.UpdateAPPIndexCustom;
import com.healthy.library.message.UpdateAPPIndexCustomOther;
import com.healthy.library.message.UpdateDownLoadInfoMsg;
import com.healthy.library.message.UpdateGiftInfo;
import com.healthy.library.message.UpdateGuideInfo;
import com.healthy.library.message.UpdateMessageInfo;
import com.healthy.library.message.UpdatePatchHasMsg;
import com.healthy.library.message.UpdateUserInfoMsg;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.message.UpdateUserShop;
import com.healthy.library.model.AMapLocationBd;
import com.healthy.library.model.ActGoodsItem;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.AppIndexCustom;
import com.healthy.library.model.AppIndexCustomItem;
import com.healthy.library.model.AppIndexCustomNews;
import com.healthy.library.model.AppIndexCustomOther;
import com.healthy.library.model.FaqExportQuestion;
import com.healthy.library.model.HanMomInfoModel;
import com.healthy.library.model.IndexMenu;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.model.LocVip;
import com.healthy.library.model.ServiceTabChangeModel;
import com.healthy.library.model.TabChangeModel;
import com.healthy.library.model.TongLianMemberData;
import com.healthy.library.model.UpdateVersion;
import com.healthy.library.model.UserInfoMonModel;
import com.healthy.library.model.VideoCategory;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.presenter.ActH5Presenter;
import com.healthy.library.presenter.AdPresenter;
import com.healthy.library.presenter.ChangeVipPresenter;
import com.healthy.library.presenter.ChannelPresenter;
import com.healthy.library.presenter.DataStatisticsPresenter;
import com.healthy.library.presenter.HanMomPresenter;
import com.healthy.library.presenter.LocVipPresenter;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.utils.CheckUtils;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.NotificationRefreshManager;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StatusLayout;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.liys.view.WaterWaveProView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


/**
 * @author Li
 * @date 2019/03/08 13:26
 * @des 首页
 */
@Route(path = IndexRoutes.INDEX_MODULE2)
public class IndexFragment extends BaseFragment implements
        IndexMainContract.View,
        OnRefreshLoadMoreListener,
        AMapLocationListener,
        HanMomContract.View,
        AdContract.View,
        BaseAdapter.OnOutClickListener,
        LocVipContract.View,
        OnNotificationListener,
        DataStatisticsContract.View , ChannelContract.View, IsFirstFragment {
    IndexBannerAdapter indexBannerAdapter;
    IndexStatusAdapter indexStatusAdapter;
    IndexFunctionGridAdapter indexFunctionAdapter;
    IndexHeadlinesAdapter indexHeadlinesAdapter;
    IndexAsKingAdapter indexAsKingAdapter;
    IndexFeaturedAdapter indexFeaturedAdapter;
    IndexRecommendAdapter indexRecommendAdapter;
    IndexServiceProjectAdapter indexServiceProjectAdapter;
    private VirtualLayoutManager virtualLayoutManager;
    ChannelPresenter channelPresenter;

    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();
    private List<VideoCategory> mFirstClassList;
    private IndexTabClassListAdapter mIndexTabClassListAdapter = null;
    //    --------------------------------------------------------------------
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerIndex;
    private RelativeLayout topTitle;
    private ImageTextView tvLoc;
    private LinearLayout tvAreall;
    private TextView selectClass;
    private ImageView selectDown;
    private ImageView passMessage;
    //    -------------------------------------------------------------------
    public int page = 1;
    public String pageSize = "10";
    public int firstPageSize = 0;
    private String[] mPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private int RC_LOCATION = 1020;
    private int RC_PROVINCE_CITY = 4697;
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;
    private int reLocTime = 0;//定位失败重试次数
    IndexMainPresenter indexMainPresenter;
    HanMomPresenter hanMomPresenter;
    private ChosePopupWindow chosePopupWindow;
    private String selectDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private String mDate;
    private UpdateUserInfoDialog mUserInfoDialog;
    AdPresenter adPresenter;
    private View topSpace;
    private WaterWaveProView passPrograss;
    private DataStatisticsPresenter dataStatisticsPresenter;
    private String sendNotificationType = "3";
    private Disposable disposable;
    private ImageView mPointsSignIn;
    private float RL_SEARCH_MIN_TOP_MARGIN, RL_SEARCH_MAX_TOP_MARGIN, RL_SEARCH_MAX_LEFT_MARGIN, RL_SEARCH_MAX_WIDTH, RL_SEARCH_MIN_WIDTH;
    private float mScrollMinLeftMargin = 0;
    private ShapeRelativeLayout mSearchLayout;
    private RelativeLayout.LayoutParams mSearchLayoutParams;
    private ImageView mHmmIp;
    private ImageView mFoldHmmIp;
    private int mTopMargin;
    private Map<String, Object> mParams = new HashMap<>();

    private IndexBean mIndexBean;// IndexStatusAdapter 的刷新状态 和这个有关 所以需要重新请求
    private List<IndexMenu> indexMenus = new ArrayList<>();
    private int dialogWidth = 230;
    private List<UserInfoExModel> userInfoExModels = new ArrayList<>();
    private ConsecutiveScrollerLayout mScrollerLayout;
    private RecyclerView mTabClassRecycler;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ConstraintLayout mTabCl;
    private int mCLTabColor;
    private IndexTabFragment mCurrentTabFragment;
    private ShapeTextView mMessageTag;
    private AppIndexCustom appIndexCustom;//首页配置
    private AppIndexCustomOther appIndexCustomOther;
    private AppCompatTextView tvLocBg1;
    private AppCompatTextView tvLocBg2;
    public boolean isInit=false;

    @Override
    protected int getLayoutId() {
        return R.layout.index_fragment_mon;
    }

    @Override
    protected void findViews() {
        indexMainPresenter = new IndexMainPresenter(getActivity(), this);
        hanMomPresenter = new HanMomPresenter(getActivity(), this);
        adPresenter = new AdPresenter(getActivity(), this);
        dataStatisticsPresenter = new DataStatisticsPresenter(mContext, this);
        channelPresenter=new ChannelPresenter(mContext,this);
        initView();
        buildRecyclerView();
        initListener();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isInit){
                    getData();
                }
            }
        },900);
    }

    @Override
    protected void onLazyData() {
        super.onLazyData();
        isInit=true;
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {//判断定位权限
            SpUtils.store(mContext,SpKey.VERSION_CHECK_FLAG,true);
            SpUtils.store(mContext,SpKey.INITSTATUS,true);
            locate();//开始定位
            channelPresenter.getIsAuditing(new SimpleHashMapBuilder<String, Object>());
        } else {
            requestPermissions(mPermissions, RC_LOCATION);
        }
    }

    private void initView() {
        NotificationRefreshManager.Companion.getInstance().registerListener(this);
        topSpace = findViewById(R.id.topSpace);
        passPrograss = (WaterWaveProView) findViewById(R.id.passPrograss);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        mScrollerLayout = findViewById(R.id.scrollerLayout);
        mScrollerLayout.setStickyOffset(TransformUtil.newDp2px(mContext, 70));
        mTabCl = findViewById(R.id.cl_tab);
        mTabLayout = findViewById(R.id.st);
        mTabClassRecycler = findViewById(R.id.recyclerView);
        mViewPager = findViewById(R.id.viewPage);
        showContent();
        setStatusLayout(layoutStatus);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerIndex = (RecyclerView) findViewById(R.id.recycler_index);
        recyclerIndex.setNestedScrollingEnabled(false);
        topTitle = (RelativeLayout) findViewById(R.id.topTitle);
        tvLoc = (ImageTextView) findViewById(R.id.tv_loc);
        tvAreall = (LinearLayout) findViewById(R.id.tv_areall);
        selectClass = (TextView) findViewById(R.id.selectClass);
        selectDown = (ImageView) findViewById(R.id.selectDown);
        passMessage = (ImageView) findViewById(R.id.passMessage);
        mMessageTag = findViewById(R.id.messageTag);
        mPointsSignIn = findViewById(R.id.pointsSignIn);
        mSearchLayout = findViewById(R.id.srl_searchLayout);
        mHmmIp = findViewById(R.id.iv_hmmIp);
        mFoldHmmIp = findViewById(R.id.foldHmmIp);
        tvLocBg1 = (AppCompatTextView) findViewById(R.id.tv_loc_bg1);
        tvLocBg2 = (AppCompatTextView) findViewById(R.id.tv_loc_bg2);
    }

    /**
     * 页码滑动 通知
     *
     * @param newState 0 悬停状态 ，其余滑动状态
     */
    private void NotificationRefresh(final int newState) {
        if (newState == 0) {
            if (!"3".equals(sendNotificationType)) {
                Observable.intervalRange(0, 1, 2, 0, TimeUnit.SECONDS)
                        .compose(RxThreadUtils.<Long>Obs_io_main())
                        .to(RxLifecycleUtils.<Long>bindLifecycle(this))
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                disposable = d;
                            }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Long aLong) {
                                //Log.e("IndexFragment", "onNext: " + aLong);
                                sendNotificationType = "3";
                                //静止状态
//                                NotificationRefreshManager.Companion.getInstance().sendBroadCast(sendNotificationType, "MainActivity");
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        } else {
            if (disposable != null) {
                disposable.dispose();
                disposable = null;
            }
            if (!"2".equals(sendNotificationType)) {
                sendNotificationType = "2";
                //滚动状态
                NotificationRefreshManager.Companion.getInstance().sendBroadCast(sendNotificationType, "MainActivity");
            }
        }
    }
    float centerX;//获得生娃养娃的中间的位置信息
    float ORG_LEFT_MARGIN;
    private void initListener() {
        passMessage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    mSearchLayoutParams = (RelativeLayout.LayoutParams) mSearchLayout.getLayoutParams();
                    RL_SEARCH_MAX_WIDTH = ScreenUtils.getScreenWidth(mContext) - topTitle.getPaddingLeft() * 2;
                    RL_SEARCH_MIN_WIDTH = RL_SEARCH_MAX_WIDTH - mFoldHmmIp.getDrawable().getMinimumWidth() - mPointsSignIn.getDrawable().getMinimumWidth()
                            - passMessage.getDrawable().getMinimumWidth() - TransformUtil.newDp2px(mContext, 30);
                    RL_SEARCH_MAX_LEFT_MARGIN = mFoldHmmIp.getDrawable().getMinimumWidth() + TransformUtil.newDp2px(mContext, 4f);
                    RL_SEARCH_MAX_TOP_MARGIN = TransformUtil.newDp2px(mContext, 36);
                    RL_SEARCH_MIN_TOP_MARGIN = 0;
                    passMessage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        tvLocBg2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(tvLocBg2.getLeft()>0){
                    centerX=tvLocBg2.getLeft();
                    System.out.println("这个tvLocBg2:"+tvLocBg2.getLeft());
                    tvLocBg2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        limitLocTree();

        layoutRefresh.setOnRefreshLoadMoreListener(this);
        layoutStatus.setOnNetRetryListener(this);
        layoutRefresh.setOnMultiListener(new SimpleMultiListener() {
            @Override
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {
                // 上拉加载时，保证吸顶头部不被推出屏幕。
                mScrollerLayout.setStickyOffset(TransformUtil.newDp2px(mContext, 70));
            }
        });

        mScrollerLayout.setOnVerticalScrollChangeListener(new ConsecutiveScrollerLayout.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollY, int oldScrollY, int scrollState) {
                if (oldScrollY < 0) {//可能是负数 属于刚进来就触发了这个调用方法也不清楚为啥
                    return;
                }
                NotificationRefresh(scrollState);

                if (mScrollerLayout.theChildIsStick(mTabCl)) {
                    if (mCLTabColor != Color.WHITE) {
                        mCLTabColor = Color.WHITE;
                        mTabCl.setBackgroundColor(mCLTabColor);
                    }
                } else {
                    if (mCLTabColor != Color.TRANSPARENT) {
                        mCLTabColor = Color.TRANSPARENT;
                        mTabCl.setBackgroundColor(mCLTabColor);
                    }
                }

                ShapeDrawableBuilder shapeDrawableBuilder = mSearchLayout.getShapeDrawableBuilder();

                float searchLayoutNewTopMargin = RL_SEARCH_MAX_TOP_MARGIN - scrollY * 0.65f;
                float searchLayoutNewLeftMargin = mScrollMinLeftMargin + scrollY * 1.65f;

                if (searchLayoutNewTopMargin > 0) {
                    if (mScrollMinLeftMargin != 0) {
                        mScrollMinLeftMargin--;
                    }
                } else {
                    if (searchLayoutNewTopMargin > RL_SEARCH_MIN_TOP_MARGIN) {
                        mScrollMinLeftMargin++;
                    }
                }

                float searchLayoutNewWidth = RL_SEARCH_MAX_WIDTH - scrollY * 4.0f;//搜索框宽度缩放的速率
                //处理布局的边界问题
                searchLayoutNewWidth = searchLayoutNewWidth < RL_SEARCH_MIN_WIDTH ? RL_SEARCH_MIN_WIDTH : searchLayoutNewWidth;

                if (searchLayoutNewTopMargin < RL_SEARCH_MIN_TOP_MARGIN) {
                    searchLayoutNewTopMargin = RL_SEARCH_MIN_TOP_MARGIN;
                }

                if (searchLayoutNewTopMargin >= RL_SEARCH_MAX_TOP_MARGIN) {
                    searchLayoutNewTopMargin = RL_SEARCH_MAX_TOP_MARGIN;
                }

                if (searchLayoutNewLeftMargin >= RL_SEARCH_MAX_LEFT_MARGIN) {
                    searchLayoutNewLeftMargin = RL_SEARCH_MAX_LEFT_MARGIN;
                }
                if (searchLayoutNewLeftMargin <= 0) {
                    searchLayoutNewLeftMargin = 0;
                }

                if (searchLayoutNewWidth < RL_SEARCH_MIN_WIDTH) {
                    searchLayoutNewWidth = RL_SEARCH_MIN_WIDTH;
                }

                if (topTitle.getHeight() != 0) {
                    float alpha = Math.min(scrollY * 1.0f / (topTitle.getHeight() * 2), 1);
                    tvAreall.setAlpha((1 - alpha));
//                    tvLocBg1.setAlpha((1 - alpha));
//                    tvLocBg2.setAlpha(alpha);
                    RelativeLayout.LayoutParams tvLocBg1Parm=(RelativeLayout.LayoutParams) tvLocBg1.getLayoutParams();
                    if(ORG_LEFT_MARGIN*(1 - alpha)<=20){
                        tvLocBg1Parm.leftMargin=(int)(20);
                    }else {
                        tvLocBg1Parm.leftMargin=(int)(ORG_LEFT_MARGIN*(1 - alpha));
                    }
//                    System.out.println("这个tvLocMargin:"+tvLocBg1Parm.leftMargin);
                    tvLocBg1.setLayoutParams(tvLocBg1Parm);
                    float ipAlpha = (float) Math.min(scrollY * 1.0f / (topTitle.getHeight() / 1.2), 1);
                    mHmmIp.setAlpha((1 - ipAlpha));
                    mFoldHmmIp.setAlpha(ipAlpha);
                    topSpace.setAlpha(ipAlpha);
                    if (ipAlpha > 0.8) {
                        if (shapeDrawableBuilder.getSolidColor() != Color.parseColor("#F2F2F5")) {
                            shapeDrawableBuilder.setSolidColor(Color.parseColor("#F2F2F5"))
                                    .intoBackground();
                        }
                        passMessage.setImageResource(R.drawable.icon_message_black);
                        TextColorBuilder textColorBuilder = mMessageTag.getTextColorBuilder();
                        ShapeDrawableBuilder messageShapeBuilder = mMessageTag.getShapeDrawableBuilder();
                        if (messageShapeBuilder.getSolidColor() == Color.WHITE) {
                            textColorBuilder.setTextGradientColors(Color.WHITE, Color.WHITE).intoTextColor();
                            messageShapeBuilder.setSolidColor(0).setGradientColors(Color.parseColor("#FF6060"),
                                    Color.parseColor("#FF426C")).intoBackground();
                        }
                    } else {
                        if (shapeDrawableBuilder.getSolidColor() != Color.WHITE) {
                            shapeDrawableBuilder.setSolidColor(Color.WHITE)
                                    .intoBackground();
                        }
                        passMessage.setImageResource(R.drawable.icon_message_white);
                        TextColorBuilder textColorBuilder = mMessageTag.getTextColorBuilder();
                        ShapeDrawableBuilder messageShapeBuilder = mMessageTag.getShapeDrawableBuilder();
                        if (messageShapeBuilder.getSolidColor() == 0) {
                            textColorBuilder.setTextGradientColors(Color.parseColor("#FF6060"),
                                    Color.parseColor("#FF426C")).intoTextColor();
                            messageShapeBuilder.setSolidColor(Color.WHITE).intoBackground();
                        }
                    }
                }
                if (mSearchLayoutParams != null && oldScrollY >= 0) {
//                    System.out.println("进行过滑动了" +searchLayoutNewTopMargin);
                    mSearchLayoutParams.topMargin = (int) searchLayoutNewTopMargin;
                    mSearchLayoutParams.leftMargin = (int) searchLayoutNewLeftMargin;
                    mSearchLayoutParams.width = (int) searchLayoutNewWidth;
                    mSearchLayout.setLayoutParams(mSearchLayoutParams);
                }


                /*------------------------------- 发送通知（内部消息回调）START -----------------------------------*/
                String mSendNotificationType;
                if(scrollY==0&&oldScrollY>0){
                    sendNotificationType = "3";
                    NotificationRefreshManager.Companion.getInstance().sendBroadCast(sendNotificationType, "MainActivity");
                }else {
                    if (scrollY > ScreenUtils.getScreenHeight(mContext)) {
                        mSendNotificationType = "1";
                    } else {
                        mSendNotificationType = "0";
                    }
                    if (!sendNotificationType.equals(mSendNotificationType)) {
                        //记录不同状态下在进行发送
                        sendNotificationType = mSendNotificationType;
                        NotificationRefreshManager.Companion.getInstance().sendBroadCast(sendNotificationType, "MainActivity");
                    }
                }

                /*------------------------------- 发送通知（内部消息回调）END -----------------------------------*/

            }
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                if (tab.getPosition() == -1) {//防止出错
                    return;
                }
                changeTabStatus(tab.getCustomView(), true);

                mTabLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (appIndexCustomOther.featuredFirst != null && appIndexCustomOther.featuredFirst.get(tab.getPosition()).initialName.equals("经验")) {
                                mTabClassRecycler.setVisibility(View.VISIBLE);
                            } else {
                                if ((appIndexCustomOther.featuredFirst == null || appIndexCustomOther.featuredFirst.size() == 0) && tab.getPosition() == 1) {//后台没配
                                    mTabClassRecycler.setVisibility(View.VISIBLE);
                                } else {
                                    mTabClassRecycler.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            mTabClassRecycler.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                }, 300);
                mCurrentTabFragment = (IndexTabFragment) mFragments.get(tab.getPosition());
                if (mCurrentTabFragment.getMIsNoMoreData()) {
                    layoutRefresh.finishLoadMoreWithNoMoreData();
                } else {
                    layoutRefresh.resetNoMoreData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabStatus(tab.getCustomView(), false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mIndexTabClassListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mTabClassRecycler.scrollToPosition(position);
                mIndexTabClassListAdapter.setMSelectPosition(position);
                VideoCategory videoCategory = mFirstClassList.get(position);
                mCurrentTabFragment.setCategoryCode(videoCategory.categoryCode);
            }
        });

        topSpace.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mTopMargin != 0) {
                    topSpace.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                }
                mTopMargin = topSpace.getHeight();
                if (indexBannerAdapter != null && mTopMargin != 0) {
                    mTopMargin += TransformUtil.newDp2px(mContext, 4f);
                    indexBannerAdapter.setTopMargin(mTopMargin);
                }
            }
        });
        tvAreall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topTitle.getAlpha() <= 0.5) {
                    return;
                }
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                if (chosePopupWindow != null) {
                    chosePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            selectDown.setImageResource(R.drawable.ic_triangle_down_white);
                            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                            lp.alpha = 1f;
                            getActivity().getWindow().setAttributes(lp);
                        }
                    });
                    if (userInfoExModels.size() > 0 && !"无阶段".equals(selectClass.getText().toString())) {
                        chosePopupWindow.showAsDropDown(v, -(int) (chosePopupWindow.getWidth() / 2 - tvAreall.getWidth() / 2), 0);
                        selectDown.setImageResource(R.drawable.ic_triangle_up_white);
                        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                        lp.alpha = 0.4f;
                        getActivity().getWindow().setAttributes(lp);
                    } else {
                        MobclickAgent.onEvent(mContext, "event2ClassEnoughtDetail", new SimpleHashMapBuilder().puts("soure", "首页"));
                        Toast.makeText(LibApplication.getAppContext(), "请完善阶段信息", Toast.LENGTH_SHORT).show();
                        ARouter.getInstance()
                                .build(MineRoutes.MINE_PERSONAL_INFO_LIST)
                                .navigation();
                    }
                }
            }
        });
        mSearchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(IndexRoutes.INDEX_SEARCH).navigation();
            }
        });
        passMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topTitle.getAlpha() <= 0.5) {
                    return;
                }
                ARouter.getInstance()
                        .build(AppRoutes.APP_MESSAGE)
                        .navigation();
            }
        });
        tvLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topTitle.getAlpha() <= 0.5) {
                    return;
                }
                ARouter.getInstance().build(LibraryRoutes.LIBRARY_LOC)
                        .navigation(mActivity, RC_PROVINCE_CITY);
            }
        });
        passPrograss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownLoadFragment.newInstance(SpUtils.getValue(mContext, SpKey.USE_UPDATE)).show(getChildFragmentManager(), "升级弹窗");
            }
        });
        mPointsSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appIndexCustom != null) {
                    MARouterUtils.passToTarget(mContext, appIndexCustom.appIndexTopMarketing);
                }
            }
        });
        mHmmIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BuildConfig.DEBUG) {
                    showTest();
                }
            }
        });
    }

    private void showTest() {//需要测试的东西放这里


        MARouterUtils.passToTarget(mContext,"/index/MonthlyActivity?specialld=2");

//        ARouter.getInstance()
//                .build("")
//                .navigation();
//        ARouter.getInstance()
//                .build(IndexRoutes.INDEX_HEALTHTESTMAIN)
//                .navigation();
    }

    private void limitLocTree() {
        tvLoc.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(centerX>0&&tvLoc.getWidth()>0){
                    RelativeLayout.LayoutParams tvLocBg1Parm=(RelativeLayout.LayoutParams) tvLocBg1.getLayoutParams();
                    ORG_LEFT_MARGIN=(centerX-tvLoc.getWidth()-(TransformUtil.dp2px(mContext,10)));
                    tvLocBg1Parm.leftMargin=(int)(ORG_LEFT_MARGIN);
//                    System.out.println("这个tvLocMarginY:"+tvLoc.getWidth());
//                    System.out.println("这个tvLocMarginX:"+centerX);
//                    System.out.println("这个tvLocMargin:"+tvLocBg1Parm.leftMargin);
                    tvLocBg1.setLayoutParams(tvLocBg1Parm);
                    tvLocBg1.setVisibility(View.VISIBLE);
                    tvLocBg2.setVisibility(View.INVISIBLE);
                    tvLoc.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerIndex.setLayoutManager(virtualLayoutManager);
        recyclerIndex.setAdapter(delegateAdapter);
        indexBannerAdapter = new IndexBannerAdapter();
        indexBannerAdapter.setTopMargin(mTopMargin);
        indexBannerAdapter.setOutClickListener(this);
        delegateAdapter.addAdapter(indexBannerAdapter);
        indexStatusAdapter = new IndexStatusAdapter(mActivity);
        delegateAdapter.addAdapter(indexStatusAdapter);
        indexFunctionAdapter = new IndexFunctionGridAdapter();
        delegateAdapter.addAdapter(indexFunctionAdapter);
        indexHeadlinesAdapter = new IndexHeadlinesAdapter();
        delegateAdapter.addAdapter(indexHeadlinesAdapter);
        indexAsKingAdapter = new IndexAsKingAdapter();
        delegateAdapter.addAdapter(indexAsKingAdapter);
        indexFeaturedAdapter = new IndexFeaturedAdapter();
        delegateAdapter.addAdapter(indexFeaturedAdapter);
        indexRecommendAdapter = new IndexRecommendAdapter();
        delegateAdapter.addAdapter(indexRecommendAdapter);
        indexServiceProjectAdapter = new IndexServiceProjectAdapter();
        delegateAdapter.addAdapter(indexServiceProjectAdapter);

        mIndexTabClassListAdapter = new IndexTabClassListAdapter();
        mTabClassRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mTabClassRecycler.setAdapter(mIndexTabClassListAdapter);

        /*---------------------------- tab栏一下内容 END ---------------------------------*/
        indexStatusAdapter.setOutClickListener(this);
        indexFunctionAdapter.setOutClickListener(this);
    }

    /**
     * 初始化 tab切换标签
     *
     * @param menus
     */
    private void buildTab(List<AppIndexCustomItem> menus) {
        mFragments.clear();
        if (menus.size() > 3) {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        List<Fragment> tmpFragments = fragmentManager.getFragments();
        for (int i = 0; i < tmpFragments.size(); i++) {
            transaction.remove(tmpFragments.get(i)).commitNowAllowingStateLoss();
        }
        for (int i = 0; i < menus.size(); i++) {
            IndexTabFragment indexTabFragment = IndexTabFragment.Companion.newInstance(menus.get(i).initialName, menus.get(i).h5Url);
            indexTabFragment.setRecyclerHelper(layoutStatus, recyclerIndex.getRecycledViewPool(), layoutRefresh);
            mFragments.add(indexTabFragment);
        }
        if (!ListUtil.isEmpty(mFragments)) {
            mCurrentTabFragment = (IndexTabFragment) mFragments.get(0);
            CanReplacePageAdapter canReplacePageAdapter = new CanReplacePageAdapter(getChildFragmentManager(), mFragments, new SimpleArrayListBuilder<String>().putList(menus, new ObjectIteraor<AppIndexCustomItem>() {
                @Override
                public String getDesObj(AppIndexCustomItem object) {
                    return object.settingName;
                }
            }));
            canReplacePageAdapter.notifyDataSetChanged();
            try {
                mViewPager.setOffscreenPageLimit(mFragments.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mViewPager.setAdapter(canReplacePageAdapter);
            mTabLayout.setupWithViewPager(mViewPager, false);
        }
        mTabLayout.removeAllTabs();
        for (int i = 0; i < menus.size(); i++) {
            TabLayout.Tab tab = mTabLayout.newTab();
            //插入tab标签
            View result =
                    LayoutInflater.from(mContext).inflate(R.layout.index_function_tab, mTabLayout, false);
            TextView titleFirst = result.findViewById(R.id.titleFirst);
            TextView titleFirstTmpBlock = result.findViewById(R.id.titleFirstTmpBlock);
            titleFirst.setText(menus.get(i).settingName);
            titleFirstTmpBlock.setText(menus.get(i).settingName);
            if (i == 0) {
                tab.select();
                changeTabStatus(result, true);
            } else {
                changeTabStatus(result, false);
            }
            tab.setCustomView(result);
            mTabLayout.addTab(tab);
        }
    }

    /**
     * tab切换
     *
     * @param view
     * @param selected
     */
    private void changeTabStatus(View view, Boolean selected) {
        if (view != null) {
            TextView titleFirst = view.findViewById(R.id.titleFirst);
            ImageView titleSecond = view.findViewById(R.id.titleSecond);
            if (selected) {
                titleFirst.setTextSize(18f);
                titleSecond.setVisibility(View.VISIBLE);
                titleFirst.setTextColor(Color.parseColor("#333333"));
            } else {
                titleFirst.setTextSize(16f);
                titleSecond.setVisibility(View.INVISIBLE);
                titleFirst.setTextColor(Color.parseColor("#999999"));
            }
        }
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if (obj != null && obj instanceof IRouterLink) {
            MARouterUtils.passToTarget(mContext, (IRouterLink) obj);
            return;
        }

        if ("更多".equals(function)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_INDEXTOOLSMAIN)
                    .withString("planId", SpUtils.getValue(mContext, SpKey.INDEXPLANID))
                    .navigation();
        }

        if ("保险服务".equals(function)) {
            String url = "https://cps.qixin18.com/m/lj1059667/media.html";
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withString("title", "保险服务")
                    .withBoolean("doctorshop", true)
                    .withBoolean("isinhome", false)
                    .withString("url", url)
                    .navigation();
        }
        if ("母婴商品".equals(function)) {
            EventBus.getDefault().post(new TabChangeModel(3));
            EventBus.getDefault().post(new ServiceTabChangeModel(1));
        }
        if ("妈妈听听".equals(function)) {
            ARouter.getInstance()
                    .build(SoundRoutes.SOUND_MAIN_MON)
                    .withString("audioType", "2")
                    .navigation();
        }
        if ("宝宝爱听".equals(function)) {
            ARouter.getInstance()
                    .build(SoundRoutes.SOUND_MAIN)
                    .withString("audioType", "1")
                    .navigation();
        }
        if ("憨妈课堂".equals(function)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_VIDEOONLINELIST)
                    .withString("type", "0")
                    .navigation();
        }
        if ("hanVideo".equals(function)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_VIDEOONLINELIST)
                    .withString("type", "1")
                    .navigation();
        }
        if ("育儿资讯".equals(function)) {
            MobclickAgent.onEvent(mContext, "event2HomeSeeMoreClick", new SimpleHashMapBuilder().puts("soure", "首页"));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_MAINPASSNEWS)
                    .withString("knowOrInfoStatus", "2")
                    .withString("queryDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                    .navigation();
        }
        if ("新人礼包".equals(function)) {
            ARouter.getInstance().build(DiscountRoutes.MINE_NEW_USER_GIFT)
                    .navigation();
        }
        if ("憨妈直播".equals(function)) {
            EventBus.getDefault().post(new TabChangeModel(2));
        }
        if ("同城圈".equals(function)) {
            EventBus.getDefault().post(new TabChangeModel(1));
        }
        if ("技师招募".equals(function)) {
            ARouter.getInstance()
                    .build(MineRoutes.MINE_JOB_TYPE)
                    .navigation();
        }
        if ("母婴商城".equals(function)) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "憨妈妈首页导航商城点击量");
            MobclickAgent.onEvent(mContext, "event2APPHomeNavigationShopClick", nokmap);
            EventBus.getDefault().post(new TabChangeModel(3));
            EventBus.getDefault().post(new ServiceTabChangeModel(0));
        }
        if ("孕育工具".equals(function)) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_TOOLS_TYPE)
                    .navigation();
        }
        if ("专家答疑".equals(function)) {
            ARouter.getInstance()
                    .build(AppRoutes.MODULE_EXPERT_FAQ2)
                    .withString("cityNo", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                    .navigation();
        }
        if ("母婴教育".equals(function)) {
            EventBus.getDefault().post(new TabChangeModel(3));
            EventBus.getDefault().post(new ServiceTabChangeModel(2));
        }
        if ("憨妈赚".equals(function)) {
            if (SpUtils.getValue(mContext, "isHanMomVip", false) == true) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String appId = Ids.WX_APP_ID; // 本应用微信AppId
                        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);
                        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                        req.userName = "gh_93d673cec6a8"; // 小程序原始id
                        req.path = "";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                        if (ChannelUtil.isRealRelease()) {
                            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                        } else {
                            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
                        }
                        api.sendReq(req);
                    }
                }, 500);

            } else {
                ARouter.getInstance().build(MineRoutes.MINE_HANMOM)
                        .navigation();
            }
        }
        if (function.equals("极速问诊")) {
            String url = "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/ConsultQuick";
            url = String.format(url,
                    new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withBoolean("isNeedRef", true)
                    .withString("title", "极速问诊")
                    .withBoolean("isinhome", false)
                    .withBoolean("doctorshop", true)
                    .withString("url", url)
                    .navigation();
        }
        if (function.equals("品质优选")) {
            EventBus.getDefault().post(new TabChangeModel(3));
        }
        if (function.equals("名医在线")) {
            String url = "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/DoctorList?deptType=3&sortType=0&titleLv=0&labelStr=";
            url = String.format(url,
                    new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_WEBVIEW)
                    .withBoolean("isNeedRef", true)
                    .withString("title", "名医在线")
                    .withBoolean("doctorshop", true)
                    .withBoolean("isinhome", false)
                    .withString("url", url)
                    .navigation();
        }

        if (function.equals("banner")) {
            dataStatisticsPresenter.bannerClickNum(new SimpleHashMapBuilder<String, Object>().puts("advertisingId", (String) obj));
        }
        if (function.equals("学育儿")) {
            ARouter.getInstance()
                    .build(IndexRoutes.INDEX_INDEXBABY)
                    .navigation();
        }
        if (function.equals("购好物")) {
            EventBus.getDefault().post(new TabChangeModel(3));
        }
        if (function.equals("找服务")) {
            ARouter.getInstance()
                    .build(SecondRoutes.MAIN_MODULE)
                    .navigation();
        }
        if (function.contains("热聊")) {
            EventBus.getDefault().post(new TabChangeModel(1));
        }
    }

    @Override
    public void onRetryClick() {
        super.onRetryClick();
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTab(UpdateMessageInfo msg) {
        if (!isFirstLoad) return;
        int messageCount = CheckUtils.getMessageCount(mContext);
        mMessageTag.setVisibility(messageCount == 0 ? View.VISIBLE : View.INVISIBLE);
        mMessageTag.setText(messageCount > 99 ? "99" : String.valueOf(messageCount));
    }

    @Override
    public void onNotification(String type, String className) {
        //首页此通知只会触发列表滚动置顶
        recyclerIndex.smoothScrollToPosition(0);
        mScrollerLayout.scrollTo(0, TransformUtil.newDp2px(mContext, 600));
        mScrollerLayout.smoothScrollToChild(recyclerIndex);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotificationRefreshManager.Companion.getInstance().unRegisterListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_LOCATION) {
            SpUtils.store(mContext,SpKey.INITSTATUS,true);
            if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
                LibApplication.initMMKVReal(getActivity().getApplication());
                SpUtils.store(mContext,SpKey.VERSION_CHECK_FLAG,true);
                SpUtils.store(mContext,SpKey.INITSTATUS,true);
                locate();
//                Toast.makeText(mContext, "开始下载补丁指令", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new UpdatePatchHasMsg());
            } else {
                Toast.makeText(mContext, "您已关闭定位将为您使用演示站数据", Toast.LENGTH_SHORT).show();
                locate();

                showDebugCenterDataDialog();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMessageTag != null) {
            int messageCount = CheckUtils.getMessageCount(mContext);
            mMessageTag.setVisibility(messageCount == 0 ? View.INVISIBLE : View.VISIBLE);
            mMessageTag.setText(messageCount > 99 ? "99" : String.valueOf(messageCount));
        }
        if (!TextUtils.isEmpty(LocUtil.getLatitude(mContext, SpKey.LOC_ORG))) {
            if (hanMomPresenter != null) {
                mParams.clear();
                mParams.put("lat", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
                mParams.put("lng", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
                hanMomPresenter.getInfo(mParams);
            }
        }
        if (PermissionUtils.hasPermissions(mContext, mPermissions) && SpUtils.getValue(LibApplication.getAppContext(), SpKey.IS_LOCERROR, false)) {
            try {
                mLocationClient.startLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))){
            new ChangeVipPresenter(mContext).getLocVip(new SimpleHashMapBuilder<String, Object>());
        }

    }
    public boolean isChanged=false;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeLocShop(UpdateUserShop msg) {
//        if(!isChanged){
//            showLocDialog(msg);
//        }
        showLocDialog(msg);
    }

    private void showLocDialog(final UpdateUserShop updateUserShop) {
        StyledDialog.init(mContext);
        StyledDialog.buildIosAlert("当前访问的门店关店中!", "可能对您购买造成不便,我们将为您切换最近的营业中的门店,是否立即切换?", new MyDialogListener() {
            @Override
            public void onFirst() {

            }

            @Override
            public void onThird() {
                super.onThird();
            }

            @Override
            public void onSecond() {
                isChanged=true;
                try {
                    LocUtil.storeLocationChose(mContext,updateUserShop.aMapLocation);
                    LocUtil.setNowShop(updateUserShop.nowShop);
                    updateUserInfoZ(new UpdateUserLocationMsg());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setBtnColor(com.health.discount.R.color.dialogutil_text_black, com.health.discount.R.color.colorPrimaryDark, 0).setBtnText("否", "是").show();
        SpUtils.store(mContext,SpKey.KILLSETTINGTIME,System.currentTimeMillis());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PROVINCE_CITY && resultCode == Activity.RESULT_OK) {
            successLocation();
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateUserInfoMsg msg) {
        if (!isFirstLoad) return;
        selectDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        page = 1;
        mParams.clear();
        mParams.put("isCurrentCity", LocUtil.getCityNameOld(mContext, SpKey.LOC_CHOSE).equals(LocUtil.getCityNameOld(mContext, SpKey.LOC_ORG)) ? "1" : "0");
        mParams.put("queryDate", selectDate);
        mParams.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        mParams.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        mParams.put("areaNo", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        mParams.put("cityNo", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
        indexMainPresenter.getIndexMain(mParams);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfoZ(UpdateUserLocationMsg msg) {
        if (!isFirstLoad) return;
        selectDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        page = 1;
        mParams.clear();
        mParams.put("isCurrentCity", LocUtil.getCityNameOld(mContext, SpKey.LOC_CHOSE).equals(LocUtil.getCityNameOld(mContext, SpKey.LOC_ORG)) ? "1" : "0");
        mParams.put("queryDate", selectDate);
        mParams.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        mParams.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        mParams.put("areaNo", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        mParams.put("cityNo", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
        indexMainPresenter.getIndexMain(mParams);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateDownLoadPrograss(UpdateDownLoadInfoMsg msg) {
        if (passPrograss != null) {
            try {
                passPrograss.setVisibility(View.VISIBLE);
                passPrograss.setProgress(msg.prograss);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateLocData() {
        getIndexSuccess(mIndexBean);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserLocation(UpdateUserLocationMsg msg) {
        if (!isFirstLoad) return;
        selectDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        page = 1;
        updateLocData();
    }

    private UserInfoMonModel muserInfoMonModel;

    @Override
    public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
        page++;
        if (mCurrentTabFragment != null) {
            mCurrentTabFragment.onLoadMore();
        } else {
            showContent();
            onRequestFinish();
        }
    }

    @Override
    public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
        page = 1;
        successLocation();
        if (mCurrentTabFragment != null) {
            mCurrentTabFragment.onRefresh();
            showContent();
        } else {
            showContent();
            onRequestFinish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void guideInfo(UpdateGuideInfo msg) {
        if (!isFirstLoad) return;
        if (msg.flag == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    GuideViewHelp.showGuideRoundRectangleRelativeSpecial(mActivity, "完善资料Guide", true,
                            tvAreall, R.layout.view_guide_type1, Gravity.BOTTOM, 10, new OnGuideChangedListener() {
                                @Override
                                public void onShowed(Controller controller) {
                                }

                                @Override
                                public void onRemoved(Controller controller) {
                                    EventBus.getDefault().post(new UpdateGuideInfo(1));

                                }
                            });
                }
            }, 800);

        }
        /*if (msg.flag == 1) {
            if (SpUtils.getValue(mContext, SpKey.AUDITSTATUS, false)) {
                SpUtils.store(mContext, "isShowZZ", true);
                EventBus.getDefault().post(new CanUpdateTab(2));
                EventBus.getDefault().post(new UpdateGuideInfo(2));
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (virtualLayoutManager.findViewByPosition(1) == null) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        while (virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid) == null) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        while (((GridLayout) virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid)).getChildAt(posFaq) == null) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        ((GridLayout) virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid)).getChildAt(posFaq).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                GuideViewHelp.showGuideRoundRelativeSpecial(getActivity(), "专家答疑", true, ((GridLayout) virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid)).getChildAt(posFaq), R.layout.view_guide_type2, Gravity.BOTTOM, 10, new OnGuideChangedListener() {
                                    @Override
                                    public void onShowed(Controller controller) {

                                    }

                                    @Override
                                    public void onRemoved(Controller controller) {
                                        SpUtils.store(mContext, "isShowZZ", true);
                                        EventBus.getDefault().post(new CanUpdateTab(2));
                                        EventBus.getDefault().post(new UpdateGuideInfo(2));
                                    }
                                });
                            }
                        }, 800);
                    } catch (Exception e) {

                        SpUtils.store(mContext, "isShowZZ", true);
                        EventBus.getDefault().post(new CanUpdateTab(2));
                        EventBus.getDefault().post(new UpdateGuideInfo(2));
                        e.printStackTrace();
                    }
                }
            }).start();

        }*/
        /*if (msg.flag == 3) {
            if (SpUtils.getValue(mContext, SpKey.AUDITSTATUS, false)) {
                SpUtils.store(mContext, "isShowZZ", true);
                EventBus.getDefault().post(new CanUpdateTab(2));
                EventBus.getDefault().post(new UpdateGuideInfo(2));
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (virtualLayoutManager.findViewByPosition(1) == null) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        while (virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid) == null) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        while (((GridLayout) virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid)).getChildAt(posFaq) == null) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        ((GridLayout) virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid)).getChildAt(posFaq).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                View desview = ((GridLayout) virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid)).getChildAt(posFaq);
                                if (desview == null) {
                                    System.out.println("引导查看:出问题");
                                    SpUtils.store(mContext, "isShowZZ", true);
                                    EventBus.getDefault().post(new CanUpdateTab(2));
                                    EventBus.getDefault().post(new UpdateGuideInfo(2));
                                } else {
                                    System.out.println("引导查看:正常");
                                    GuideViewHelp.showGuideRoundRelativeSpecial(getActivity(), "专家答疑", true, desview, R.layout.view_guide_type2, Gravity.BOTTOM, 10, new OnGuideChangedListener() {
                                        @Override
                                        public void onShowed(Controller controller) {

                                        }

                                        @Override
                                        public void onRemoved(Controller controller) {
                                            SpUtils.store(mContext, "isShowZZ", true);
                                            EventBus.getDefault().post(new CanUpdateTab(2));
                                            EventBus.getDefault().post(new UpdateGuideInfo(2));
                                        }
                                    });
                                }

                            }
                        }, 800);
                    } catch (Exception e) {
                        SpUtils.store(mContext, "isShowZZ", true);
                        EventBus.getDefault().post(new CanUpdateTab(2));
                        EventBus.getDefault().post(new UpdateGuideInfo(2));
                        e.printStackTrace();
                    }
                }
            }).start();

        }*/
    }

    private void showDialog() {
        //获取当天时间 格式（yyyy-MM-dd)
        String format = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        if (!format.equals(mDate)) {
            mUserInfoDialog = null;
        }
        mDate = format;
        String mUserDialogTime = SpUtils.getValue(mContext, SpKey.USER_DIALOG_TIME);
        //存入的时间不和当天时间一致 切 dialog为空
        if (mUserInfoDialog == null) {
            String birthday = SpUtils.getValue(mContext, SpKey.USER_BIRTHDAY);
            String mUserCreateDate = SpUtils.getValue(mContext, SpKey.USER_CREATE_DATE);
            SpUtils.store(mContext, SpKey.USER_DIALOG_TIME, mDate);
            //如果生日为空并且不是当天注册的 进行提醒用户
            if (TextUtils.isEmpty(birthday)
                    && !TextUtils.isEmpty(mUserCreateDate) //多加一层判断创建时间为空的情况（阶段未选择时间为空）
                    && !format.equals(mUserCreateDate)
                    && !mDate.equals(mUserDialogTime)) {
                //完善用户信息提示弹框
                mUserInfoDialog = UpdateUserInfoDialog.newInstance();
                mUserInfoDialog.show(getChildFragmentManager(), "UpdateUserInfo");
            } else {
                if (!TextUtils.isEmpty(birthday)) {//生日不等于空 则要开始告诉首页去算优惠券了
                }
            }
        }

        EventBus.getDefault().post(new UpdateGiftInfo(false));
    }

    private void locate() {
        mLocationClient = new AMapLocationClient(mContext);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    @Override
    public void getIndexSuccess(IndexBean indexBean) {
        tvLoc.setText(LocUtil.getCityNameWithLimlt(mContext, SpKey.LOC_CHOSE, 4));//设置左上角城市
        limitLocTree();
        if (indexBean == null) {
            return;
        }
        try {
            if (chosePopupWindow != null) {
                chosePopupWindow.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mIndexBean = indexBean;
        indexFunctionAdapter.setModel("");
        indexMainPresenter.getMine();
        //changeTab(false, nowTab);
        mParams.clear();
        mParams.put("type", "2");
        mParams.put("pageNum", "1");
        mParams.put("pageSize", pageSize);
        mParams.put("firstPageSize", "0");
        mParams.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        indexMainPresenter.getGoodsHot(mParams);
        mParams.clear();
        mParams.put("page", new SimpleHashMapBuilder<String, Object>()
                .puts("pageSize", "4")
                .puts("pageNum", "1"));
        mParams.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        indexMainPresenter.getVideoOnline(mParams);
        indexMainPresenter.getAPPIndexCustom(new SimpleHashMapBuilder<String, Object>());
        indexMainPresenter.getAPPIndexCustomNews(new SimpleHashMapBuilder<String, Object>());
        indexMainPresenter.getQuestionList(new SimpleHashMapBuilder<String, Object>().puts("pageSize", "8").puts("pageNum", 1));
    }

    @Override
    public void onSucessGetAds(List<AdModel> adModels) {
        if (adModels.size() > 0) {
            final AdModel adModel = adModels.get(0);
            if (adModel.type == 2 && adModel.triggerPage == 26) {
                indexBannerAdapter.setBannerImgs(adModels);
                indexBannerAdapter.setModel("");
            }
            if (adModel.type == 2 && adModel.triggerPage == 24) {
                //indexSumAdapter.setBannerimgs(adModels);
            }
            if (adModel.type == 2 && adModel.triggerPage == 25) {
                //indexBannerAdapter.setBannerimgs(adModels);
            }
            if (adModel.type == 1 && adModel.triggerPage == 1) {
                SpUtils.store(mContext, SpKey.USER_Main_AD_Bean, new Gson().toJson(adModel));
            }
            if (adModel.type == 1 && adModel.triggerPage == 1) {
                final String showTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                final String limitTime = adModel.validityEnd;
                boolean isTimeEndBefore = false;
                try {
                    isTimeEndBefore = new Date().before(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(limitTime));
                } catch (Exception e) {
                    isTimeEndBefore = true;
                    e.printStackTrace();
                }
                if (isTimeEndBefore && !SpUtils.getValue(mContext, "AdMain" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage + "&" + showTime, false)) {
//                    System.out.println("需要展示广告:"+"Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage);
                    GlideCopy.with(this)
                            .load(adModel.photoUrls)

                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    super.onLoadFailed(errorDrawable);
                                }

                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    if (isfragmenthow) {
                                        if (!SpUtils.getValue(mContext, "AdMain" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage + "&" + showTime, false)) {
//                                            System.out.println("需要展示广告Z:"+"Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage);
                                            ADDialogView.newInstance().setAdModel(adModel).show(getFragmentManager(), "广告");
                                            SpUtils.store(mContext, "AdMain" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage + "&" + showTime, true);
                                        }
                                    }
                                }
                            });
                }
            }
        }
    }


    /**
     * 直播列表回调
     *
     * @param result
     * @param isMore
     */
    @Override
    public void onSuccessGetVideoOnlineList(List<LiveVideoMain> result, boolean isMore) {
        indexRecommendAdapter.setRightVideoList(result);
//        if (result != null && result.size() > 0) {
//            //indexSumAdapter.setVideoOnlineList(result);
//            //indexSumAdapter.notifyDataSetChanged();
//        }
    }

    /**
     * 推荐商品回调
     *
     * @param result
     * @param firstPageSize
     */
    @Override
    public void onSuccessGetGoodsHotList(List<ActGoodsItem> result, int firstPageSize) {
        if (result != null && result.size() > 0) {
            //indexSumAdapter.setHotGoodsList(result);
            //indexSumAdapter.notifyDataSetChanged();
        } else {//说明没精选服务  那么请求自动推荐
            mParams.clear();
            mParams.put("type", "1");
            mParams.put("pageNum", "1");
            mParams.put("pageSize", pageSize);
            mParams.put("firstPageSize", "0");
            mParams.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
            //indexMainPresenter.getGoodsHot2(mParams);
        }
    }

    @Override
    public void getAllStatusSuccess(List<UserInfoExModel> userInfoExModels) {
        this.userInfoExModels.clear();
        this.userInfoExModels.addAll(userInfoExModels);
        for (int i = 0; i < this.userInfoExModels.size(); i++) {
            if (this.userInfoExModels.get(i).useStatus == 1) {//说明被选中
                SpUtils.storeJson(mContext, SpKey.USE_TOKEN, this.userInfoExModels.get(i));
            }
        }
        try {
            if (chosePopupWindow != null) {
                chosePopupWindow.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        chosePopupWindow = new ChosePopupWindow(mContext, (int) TransformUtil.dp2px(mContext, dialogWidth), userInfoExModels, new ChosePopupWindow.OnStatusClickListener() {
            @Override
            public void onClick(UserInfoExModel bean, View view) {
                if (view.getId() == R.id.tv_edit) {
                    ARouter.getInstance()
                            .build(MineRoutes.MINE_PERSONAL_INFO_LIST)
                            .navigation();
                } else {
                    indexMainPresenter.changeStatus(bean.id + "");
                    chosePopupWindow.dismiss();
                }
            }
        });

    }

    @Override
    public void changeStatusSuccess() {
        selectDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        successLocation();
    }

    @Override
    public void getSucessIsAuditing() {
        EventBus.getDefault().post(new UpdatePatchHasMsg());
    }

    @Override
    public void onSucessCheckVersion(UpdateVersion result) {

    }

    @Override
    public void getMineSuccess(UserInfoMonModel userInfoMonModel) {
        indexMainPresenter.getTongLianPhoneStatus(new SimpleHashMapBuilder<String, Object>());
        indexStatusAdapter.clear();
        if (userInfoMonModel == null) {
            indexBannerAdapter.setStatus(0);
            indexBannerAdapter.setModel("");
            indexStatusAdapter.setModel(mIndexBean == null ? null : mIndexBean);
            SpUtils.store(mContext, "isShowZZ", true);
            EventBus.getDefault().post(new CanUpdateTab(2));
            return;
        }
        indexMainPresenter.getAPPIndexCustomOther(new SimpleHashMapBuilder<String, Object>().puts("memberStatus", userInfoMonModel.status));
        muserInfoMonModel = userInfoMonModel;
        SpUtils.store(mContext, SpKey.USER_NICK, muserInfoMonModel.nickName);
        SpUtils.store(mContext, SpKey.USER_ICON, muserInfoMonModel.faceUrl);
        SpUtils.store(mContext, SpKey.USER_BIRTHDAY, muserInfoMonModel.birthday);
        SpUtils.store(mContext, SpKey.STATUS_USER, muserInfoMonModel.status);

        indexStatusAdapter.setUserInfoMonModel(userInfoMonModel);
        indexStatusAdapter.setMemberSex(userInfoMonModel.memberSex + "");
        if (mIndexBean == null) {
            mIndexBean = new IndexBean(userInfoMonModel.status);
        }
        if (mIndexBean.status > 0 && mIndexBean.status <= 4) {
            if ((mIndexBean.status == 1 && userInfoMonModel.memberSex == 1) || mIndexBean.status == 4) {
                //男性备孕就不显示状态了。
                indexBannerAdapter.setStatus(0);
            } else {
                indexBannerAdapter.setStatus(mIndexBean.status);
                indexStatusAdapter.setModel(mIndexBean);
            }
            indexBannerAdapter.setModel("");
        } else {
            indexBannerAdapter.setModel("");
            indexBannerAdapter.setStatus(0);
        }
        //接口请求成功情况下进行弹框
        showDialog();
        if (TextUtils.isEmpty(userInfoMonModel.dateContent) || userInfoMonModel.status == 4
                || userInfoMonModel.dateContent.contains("岁") || userInfoMonModel.dateContent.contains("年")
                || (userInfoMonModel.memberSex == 1 && userInfoMonModel.status == 1)) {
            selectClass.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            selectClass.setTextColor(Color.parseColor("#FFFFFF"));
        }
        selectDown.setImageResource(R.drawable.ic_triangle_down_white);
        if (userInfoMonModel.dateContent != null && userInfoMonModel.dateContent.contains("宝宝")) {
            selectClass.setText(userInfoMonModel.babyName);
        } else {
            selectClass.setText(userInfoMonModel.statusName);
        }
        if (!SpUtils.getValue(mContext, "isShowZZ", false)) {
            if (TextUtils.isEmpty(userInfoMonModel.dateContent) && TextUtils.isEmpty(userInfoMonModel.statusName)) {
                selectClass.setText("无阶段");
                if (SpUtils.isFirst(mContext, "指导出现")) {
                    EventBus.getDefault().post(new UpdateGuideInfo(0));
                } else {
                    SpUtils.store(mContext, "isShowZZ", true);
                    EventBus.getDefault().post(new CanUpdateTab(2));
                }
            } else {
                if (SpUtils.isFirst(mContext, "指导出现")) {
                    EventBus.getDefault().post(new UpdateGuideInfo(3));
                } else {
                    SpUtils.store(mContext, "isShowZZ", true);
                    EventBus.getDefault().post(new CanUpdateTab(2));
                }
            }
        } else {
            if (TextUtils.isEmpty(userInfoMonModel.dateContent) && TextUtils.isEmpty(userInfoMonModel.statusName)) {
                selectClass.setText("无阶段");
            }
        }
    }

    @Override
    public void onSuccessTongLianPhoneStatus(TongLianMemberData isPhoneChecked) {
    }

    @Override
    public void onGetTabListSuccess(List<VideoCategory> result) {
        try {
            mFirstClassList = result;
            VideoCategory videoCategory = new VideoCategory();
            videoCategory.categoryCode = "0";
            videoCategory.categoryName = "全部";
            mFirstClassList.add(0, videoCategory);
            mIndexTabClassListAdapter.setNewData(mFirstClassList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessGetAPPIndexCustom(AppIndexCustom appIndexCustom) {
        this.appIndexCustom = appIndexCustom;
        if (appIndexCustom != null) {
            buildAppIndexCustom();
            EventBus.getDefault().post(new UpdateAPPIndexCustom(appIndexCustom.appIndexMarketingPendant));
        } else {
            EventBus.getDefault().post(new UpdateAPPIndexCustom(null));
        }

    }


    @Override
    public void onSuccessGetAPPIndexCustomWithOther(AppIndexCustomOther appIndexCustom) {
        appIndexCustomOther = appIndexCustom;
        if (appIndexCustom != null) {
            SpUtils.store(mContext, SpKey.INDEXPLANID, appIndexCustom.id);
        }
        if (appIndexCustom != null && appIndexCustom.appSmallTool != null && appIndexCustom.appSmallTool.size() > 0) {
            indexMenus.clear();
            indexMenus.addAll(new SimpleArrayListBuilder<IndexMenu>().putList(appIndexCustom.appSmallTool, new ObjectIteraor<AppIndexCustomItem>() {
                @Override
                public IndexMenu getDesObj(AppIndexCustomItem object) {
                    return new IndexMenu(object);
                }
            }));
            if(SpUtils.getValue(mContext,SpKey.AUDITSTATUS,false)) {//true 为审核中 fasle为通过
                if(ChannelUtil.getChannel(mContext).contains("oppo")){
                    for (int i = 0; i < indexMenus.size(); i++) {
                        if("专家答疑".equals(indexMenus.get(i).name)
                                ||indexMenus.get(i).name.contains("咨询")
                                ||indexMenus.get(i).name.contains("保险")){
                            indexMenus.remove(i);
                            i--;
                        }
                    }
                }
            }

            if (indexMenus.size() > 9) {
                indexMenus = indexMenus.subList(0, 9);
            }
            if (indexMenus.size() > 1) {
                indexMenus.add(new IndexMenu("更多", R.drawable.index_more));
            }

            indexFunctionAdapter.setIndexMenus(indexMenus);
            indexFunctionAdapter.setModel("");
        } else {
            indexMenus.clear();
            indexMenus.add(new IndexMenu("同城圈", R.drawable.index_more));
            if (SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)) {//如果在审核
            } else {
                indexMenus.add(new IndexMenu("专家答疑", R.drawable.index_more));
            }
            indexMenus.add(new IndexMenu("母婴商城", R.drawable.index_more));
            indexMenus.add(new IndexMenu("新人礼包", R.drawable.index_more));
            indexMenus.add(new IndexMenu("妈妈听听", R.drawable.index_more));
//        indexMenus.add(new IndexMenu("憨妈直播", R.drawable.index_zb));
            if (SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)) {//如果在审核

            } else {
                indexMenus.add(new IndexMenu("保险服务", R.drawable.index_more));
            }
//        indexMenus.add(new IndexMenu("技师招募", R.drawable.index_jszm));
            indexMenus.add(new IndexMenu("憨妈赚", R.drawable.index_more));
            indexMenus.add(new IndexMenu("孕育工具", R.drawable.index_more));
            indexMenus.add(new IndexMenu("憨妈课堂", R.drawable.index_more));
            indexMenus.add(new IndexMenu("育儿资讯", R.drawable.index_more));
            if (indexMenus.size() > 9) {
                indexMenus = indexMenus.subList(0, 9);
            }
            indexMenus.add(new IndexMenu("更多", R.drawable.index_more));
            indexFunctionAdapter.setIndexMenus(indexMenus);
            indexFunctionAdapter.setModel("");
        }
        if (appIndexCustom != null && appIndexCustom.bottomNavigation != null && appIndexCustom.bottomNavigation.size() > 0) {
            EventBus.getDefault().post(new UpdateAPPIndexCustomOther(appIndexCustom.bottomNavigation));

        }
        if (appIndexCustom != null && appIndexCustom.featuredFirst != null && appIndexCustom.featuredFirst.size() > 0) {
            buildTab(appIndexCustom.featuredFirst);
        } else {
            List<AppIndexCustomItem> item = new ArrayList<>();
            item.add(new AppIndexCustomItem("推荐", "推荐", ""));
            item.add(new AppIndexCustomItem("经验", "经验", ""));
            item.add(new AppIndexCustomItem("问答", "问答", ""));
            item.add(new AppIndexCustomItem("知识", "知识", ""));
            item.add(new AppIndexCustomItem("爆款", "爆款", ""));
            buildTab(item);
        }

        if (appIndexCustom != null && appIndexCustom.vajraDistrict != null && appIndexCustom.vajraDistrict.size() > 0) {
            indexBannerAdapter.setIndexMenus(appIndexCustom.vajraDistrict);
            indexBannerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSuccessGetAPPIndexCustomNews(AppIndexCustomNews result) {

        indexHeadlinesAdapter.setModel(result);

    }

    @Override
    public void onSuccessGetQuestionList(List<FaqExportQuestion> result) {
        indexAsKingAdapter.clear();
        if (!ListUtil.isEmpty(result)) {
            indexAsKingAdapter.setQusetionList(result);
            indexAsKingAdapter.setModel("");
        }
    }

    private void buildAppIndexCustom() {
        if (appIndexCustom != null) {
            indexFeaturedAdapter.clear();
            indexRecommendAdapter.clear();
            indexServiceProjectAdapter.clear();
            mPointsSignIn.setVisibility(View.INVISIBLE);
            if (appIndexCustom.appIndexTopMarketing != null) {
                mPointsSignIn.setVisibility(View.VISIBLE);
                GlideCopy.with(mContext)
                        .load(appIndexCustom.appIndexTopMarketing.imageUrl)
                        .into(mPointsSignIn);
//                System.out.println("隐藏掉头部了加载页面"+appIndexCustom.appIndexTopMarketing.imageUrl);
            } else {
//                System.out.println("隐藏掉头部了");
            }
            indexFeaturedAdapter.setModel(appIndexCustom.appIndexSelected);
            if (appIndexCustom.appIndexService != null && appIndexCustom.appIndexService.list != null && appIndexCustom.appIndexService.list.size() > 0) {
                indexServiceProjectAdapter.setModel(appIndexCustom.appIndexService);
            } else {
                indexServiceProjectAdapter.setModel(null);
            }
            if (appIndexCustom.appIndexRecommend != null) {
                indexRecommendAdapter.setModel("");
                indexRecommendAdapter.setLeftGoodsList(appIndexCustom.appIndexRecommend.list);
                indexRecommendAdapter.setTitleList(appIndexCustom.appIndexRecommend.title);
            }
        }

    }

    int retryTime = 0;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        System.out.println("关于定位重新开始");
        if (!checkGPSIsOpen()) {
            if (retryTime == 0) {
                layoutStatus.updateStatus(StatusLayout.Status.STATUS_CUSTOM);
                layoutStatus.setmOnCustomNetRetryListener(new OnCustomRetryListener() {
                    @Override
                    public void onRetryClick() {
                        mLocationClient.startLocation();
                    }
                });
                retryTime = 1;
            }

        }
        if (aMapLocation == null) {//120.575758,31.302379
            return;
        }
        if (aMapLocation.getErrorCode() != 0) {//120.575758,31.302379 定位失败了嘛
            if (!PermissionUtils.hasPermissions(mContext, mPermissions)) {
                System.out.println("定位没开");
                SpUtils.store(LibApplication.getAppContext(), SpKey.IS_LOCERROR, true);
                aMapLocation = new AMapLocationBd("憨妈妈", "虎丘区", "苏州市", "江苏省", "320505", 31.302379, 120.575758).build();
            } else {
//                System.out.println("定位权限开了 但是可能恶意把定位关了 ");
//                SpUtils.store(LibApplication.getAppContext(), SpKey.IS_LOCERROR, true);
//                aMapLocation = new AMapLocationBd("憨妈妈", "虎丘区", "苏州市", "江苏省", "320505", 31.302379, 120.575758).build();
            }
        }
        if (aMapLocation.getErrorCode() == 0) {
            System.out.println("关于定位重新开始获得的坐标"+aMapLocation.getLatitude()+":"+aMapLocation.getLongitude());
            LocUtil.storeLocation(mContext, aMapLocation);
            System.out.println("定位没开吗设置定位地点" + SpUtils.getValue(LibApplication.getAppContext(), SpKey.IS_LOCERROR, false));
            if (LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE) == null || SpUtils.getValue(LibApplication.getAppContext(), SpKey.IS_LOCERROR, false)) {
                LocUtil.storeLocationChose(mContext, aMapLocation);
                if (PermissionUtils.hasPermissions(mContext, mPermissions) && SpUtils.getValue(LibApplication.getAppContext(), SpKey.IS_LOCERROR, false)) {//定位开了 但是之前定位没开
                    System.out.println("定位没开又开了重新设置");
                    SpUtils.store(LibApplication.getAppContext(), SpKey.IS_LOCERROR, false);
                    LocUtil.clearNowShop();//先不改
                }
            }
            successLocation();
//            EventBus.getDefault().post(new UpdateUserLocationMsg());
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
                        }).setMessageCancelClickListener(new MessageDialog.MessageCancelClickListener() {
                    @Override
                    public void onMessageCancelClick(View view) {
                        showDebugCenterDataDialog();
                    }
                }).show(getChildFragmentManager(), "打开定位");

            }
        }
    }

    public void showDebugCenterDataDialog() {
        StyledDialog.init(getActivity());
        StyledDialog.buildIosAlert("询问!", "不开启定位将使用演示站数据\n是否开启定位？", new MyDialogListener() {
            @Override
            public void onFirst() {
                System.out.println("定位权限开了 但是可能恶意把定位关了 ");
                SpUtils.store(LibApplication.getAppContext(), SpKey.IS_LOCERROR, true);
                AMapLocation aMapLocation = new AMapLocationBd("憨妈妈", "虎丘区", "苏州市", "江苏省", "320505", 31.302379, 120.575758).build();
                onLocationChanged(aMapLocation);
            }

            @Override
            public void onThird() {
                super.onThird();
            }

            @Override
            public void onSecond() {
                IntentUtils.toSelfSetting(mActivity);
            }
        }).setCancelable(false, false).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("否", "是").show();
    }

    private void successLocation() {
        tvLoc.setText(LocUtil.getCityNameWithLimlt(mContext, SpKey.LOC_CHOSE,4));
        limitLocTree();

        new ActH5Presenter(mContext).getH5();
        new LocVipPresenter(mContext, this).getLocVip(new SimpleHashMapBuilder<String, Object>());
        indexMainPresenter.getAllStatus();
        indexStatusAdapter.setAreaNo(LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        mParams.clear();
        mParams.put("scope", "0");
        indexMainPresenter.getVideoTypeTabList(mParams);
        mParams.clear();
        mParams.put("isCurrentCity", LocUtil.getCityNameOld(mContext, SpKey.LOC_CHOSE).equals(LocUtil.getCityNameOld(mContext, SpKey.LOC_ORG)) ? "1" : "0");
        mParams.put("queryDate", selectDate);
        mParams.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE));
        mParams.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE));
        mParams.put("areaNo", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        mParams.put("cityNo", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
        indexMainPresenter.getIndexMain(mParams);
        mParams.clear();
        mParams.put("lat", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        mParams.put("lng", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        hanMomPresenter.getInfo(mParams);
        mParams.clear();
        mParams.put("type", "2");
        mParams.put("triggerPage", "26");
        mParams.put("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        adPresenter.getAd(mParams);
        mParams.clear();
        mParams.put("type", "1");
        mParams.put("triggerPage", "1");
        mParams.put("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        adPresenter.getAd(mParams);
//        mParams.clear();
//        mParams.put("type", "2");
//        mParams.put("triggerPage", "24");
//        mParams.put("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
//        adPresenter.getAd(mParams);
//        mParams.clear();
//        mParams.put("type", "2");
//        mParams.put("triggerPage", "25");
//        mParams.put("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
//        adPresenter.getAd(mParams);
    }

    private boolean checkGPSIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    @Override
    public void onGetInfoSuccess(HanMomInfoModel.MemberInfoModel memberInfoModel) {
        if (memberInfoModel != null && memberInfoModel.partnerId != null && !TextUtils.isEmpty(memberInfoModel.partnerId)) {
            //System.out.println("设置HmmVip");
            SpUtils.store(mContext, "isHanMomVip", true);
            //isHanMomVip = true;// 6. 当memberInfo存在，且memberInfo的partnerId不为空或NULL时，跳转小程序。
        } else {
            SpUtils.store(mContext, "isHanMomVip", false);
        }
    }

    @Override
    public void onSucessGetLocVip(LocVip locVip) {

    }

}