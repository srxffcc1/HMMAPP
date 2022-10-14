//package com.health.index.fragment;
//
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.location.LocationManager;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.util.Base64;
//import android.view.Gravity;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.GridLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.graphics.drawable.RoundedBitmapDrawable;
//import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.alibaba.android.vlayout.DelegateAdapter;
//import com.alibaba.android.vlayout.VirtualLayoutManager;
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.app.hubert.guide.core.Controller;
//import com.app.hubert.guide.listener.OnGuideChangedListener;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.BitmapImageViewTarget;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.request.transition.Transition;
//import com.chad.library.adapter.base.entity.MultiItemEntity;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParseException;
//import com.google.gson.reflect.TypeToken;
//import com.health.index.BuildConfig;
//import com.health.index.R;
//import com.health.index.adapter.IndexBannerAdapter;
//import com.health.index.adapter.IndexFunctionGridAdapter;
//import com.health.index.adapter.IndexStatusAdapter;
//import com.health.index.adapter.IndexSumAdapter;
//import com.health.index.adapter.IndexTabAdapter;
//import com.health.index.adapter.IndexTabEmptyAdapter;
//import com.health.index.adapter.IndexTabFoundAdapter;
//import com.health.index.adapter.IndexTabQuestionAdapter;
//import com.health.index.adapter.IndexTabRecommandAdapter;
//import com.health.index.adapter.IndexTabSalesAdapter;
//import com.health.index.contract.IndexMainContract;
//import com.health.index.interfaces.IndexListener;
//import com.health.index.model.IndexBean;
//import com.health.index.model.NewsInfo;
//import com.health.index.model.UserInfoExModel;
//import com.healthy.library.model.UserInfoMonModel;
//import com.health.index.presenter.IndexMainPresenter;
//import com.health.index.widget.ChosePopupWindow;
//import com.healthy.library.LibApplication;
//import com.healthy.library.adapter.PostAdapter;
//import com.healthy.library.base.BaseAdapter;
//import com.healthy.library.base.BaseFragment;
//import com.healthy.library.builder.SimpleHashMapBuilder;
//import com.healthy.library.business.ADDialogView;
//import com.healthy.library.business.DownLoadFragment;
//import com.healthy.library.business.MessageDialog;
//import com.healthy.library.businessutil.ChannelUtil;
//import com.healthy.library.businessutil.GuideViewHelp;
//import com.healthy.library.businessutil.LocUtil;
//import com.healthy.library.constant.Ids;
//import com.healthy.library.constant.SpKey;
//import com.healthy.library.contract.AdContract;
//import com.healthy.library.contract.DataStatisticsContract;
//import com.healthy.library.contract.HanMomContract;
//import com.healthy.library.contract.LocVipContract;
//import com.healthy.library.contract.PostListSingleContract;
//import com.healthy.library.dialog.PostCouponDialog;
//import com.healthy.library.dialog.UpdateUserInfoDialog;
//import com.healthy.library.interfaces.IsNeedShare;
//import com.healthy.library.interfaces.OnCustomRetryListener;
//import com.healthy.library.message.CanUpdateTab;
//import com.healthy.library.message.UpdateDownLoadInfoMsg;
//import com.healthy.library.message.UpdateGiftInfo;
//import com.healthy.library.message.UpdateGuideInfo;
//import com.healthy.library.message.UpdateMessageInfo;
//import com.healthy.library.message.UpdateUserInfoMsg;
//import com.healthy.library.message.UpdateUserLocationMsg;
//import com.healthy.library.model.AMapLocationBd;
//import com.healthy.library.model.ActGoodsItem;
//import com.healthy.library.model.AdModel;
//import com.healthy.library.model.FaqExportQuestion;
//import com.healthy.library.model.HanMomInfoModel;
//import com.healthy.library.model.IndexMenu;
//import com.healthy.library.model.LiveVideoMain;
//import com.healthy.library.model.LocVip;
//import com.healthy.library.model.PageInfoEarly;
//import com.healthy.library.model.PostDetail;
//import com.healthy.library.model.ServiceTabChangeModel;
//import com.healthy.library.model.TabChangeModel;
//import com.healthy.library.model.VideoListModel;
//import com.healthy.library.presenter.ActH5Presenter;
//import com.healthy.library.presenter.AdPresenter;
//import com.healthy.library.presenter.DataStatisticsPresenter;
//import com.healthy.library.presenter.HanMomPresenter;
//import com.healthy.library.presenter.LocVipPresenter;
//import com.healthy.library.presenter.PostListSinglePresenter;
//import com.healthy.library.routes.AppRoutes;
//import com.healthy.library.routes.DiscountRoutes;
//import com.healthy.library.routes.IndexRoutes;
//import com.healthy.library.routes.LibraryRoutes;
//import com.healthy.library.routes.MineRoutes;
//import com.healthy.library.routes.SoundRoutes;
//import com.healthy.library.utils.CheckUtils;
//import com.healthy.library.utils.IntentUtils;
//import com.healthy.library.utils.NavigateUtils;
//import com.healthy.library.utils.PermissionUtils;
//import com.healthy.library.utils.SpUtils;
//import com.healthy.library.utils.TransformUtil;
//import com.healthy.library.widget.CornerImageView;
//import com.healthy.library.widget.ImageTextView;
//import com.healthy.library.widget.StatusLayout;
//import com.hss01248.dialog.StyledDialog;
//import com.hss01248.dialog.interfaces.MyDialogListener;
//import com.hss01248.dialog.interfaces.MyItemDialogListener;
//import com.liys.view.WaterWaveProView;
//import com.scwang.smart.refresh.layout.SmartRefreshLayout;
//import com.scwang.smart.refresh.layout.api.RefreshLayout;
//import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
//import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;
//import com.umeng.analytics.MobclickAgent;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//import org.jetbrains.annotations.NotNull;
//import org.json.JSONObject;
//
//import java.lang.reflect.Type;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
///**
// * @author Li
// * @date 2019/03/08 13:26
// * @des 首页
// */
//
//public class IndexOldFragment extends BaseFragment implements
//        IndexListener,
//        IsNeedShare,
//        View.OnClickListener,
//        IndexMainContract.View,
//        OnRefreshLoadMoreListener,
//        AMapLocationListener,
//        HanMomContract.View,
//        AdContract.View,
//        BaseAdapter.OnOutClickListener,
//        LocVipContract.View,
//        PostAdapter.OnPostLikeClickListener,
//        PostAdapter.OnShareClickListener,
//        PostListSingleContract.View,
//        DataStatisticsContract.View {
//    IndexStatusAdapter indexStatusAdapter;
//    IndexFunctionGridAdapter indexFunctionAdapter;
//    IndexSumAdapter indexSumAdapter;
//    IndexBannerAdapter indexBannerAdapter;
//    IndexTabAdapter indexTabAdapter;
//    IndexTabEmptyAdapter indexTabEmptyAdapter;
//    IndexTabFoundAdapter indexTabFoundAdapter;
//    PostAdapter indexTabIntercatAdapter;
//    IndexTabQuestionAdapter indexTabQuestionAdapter;
//    IndexTabRecommandAdapter indexTabRecommandAdapter;
//    IndexTabSalesAdapter indexTabSalesAdapter;
//    private VirtualLayoutManager virtualLayoutManager;
//    private DelegateAdapter delegateAdapter;
//    //    --------------------------------------------------------------------
//    private StatusLayout layoutStatus;
//    private SmartRefreshLayout layoutRefresh;
//    private RecyclerView recyclerIndex;
//    private RelativeLayout topTitle;
//    private ImageTextView tvLoc;
//    private LinearLayout tvAreall;
//    private LinearLayout tvArealll;
//    private CornerImageView selectIcon;
//    private TextView selectClass;
//    private TextView selectDate;
//    private ImageView selectDown;
//    private ImageView passMessage;
//    private ImageView ivBottomShader;
//    //    -------------------------------------------------------------------
//    private int isinit = 0;
//    private int totalDy = 0;
//    public int page = 1;
//    public int pageSize = 10;
//    public int firstPageSize = 0;
//    public String nowTab = "互动";
//    public boolean isTabClick = false;//
//    private String[] mPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
//    private int RC_LOCATION = 1020;
//    private int RC_PROVINCE_CITY = 4697;
//    public AMapLocationClient mLocationClient;
//    public AMapLocationClientOption mLocationOption = null;
//    private int reLocTime = 0;//定位失败重试次数
//    IndexMainPresenter indexMainPresenter;
//    HanMomPresenter hanMomPresenter;
//    private ChosePopupWindow chosePopupWindow;
//    private String selectdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//    AdPresenter adPresenter;
//    private int posFaq = 1;
//    private Dialog reviewandwarndialog;
//    private Dialog warndialog;
//    private ImageView passToTop;
//    private RelativeLayout topSpace;
//    private WaterWaveProView passPrograss;
//    private int position = -1;
//    private PostCouponDialog couponDialog;
//    private int clickCount = 0;//TODO 后续删除
//    private DataStatisticsPresenter dataStatisticsPresenter;
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.index_fragment_mon;
//    }
//
//    @Override
//    protected void findViews() {
//        initView();
//        buildRecyclerView();
//        indexMainPresenter = new IndexMainPresenter(getActivity(), this);
//        hanMomPresenter = new HanMomPresenter(getActivity(), this);
//        adPresenter = new AdPresenter(getActivity(), this);
//        dataStatisticsPresenter = new DataStatisticsPresenter(mContext, this);
//    }
//
//    @Override
//    protected void onLazyData() {
//        super.onLazyData();
//        getData();
//    }
//
//    private void initView() {
//        topSpace = (RelativeLayout) findViewById(R.id.topSpace);
//        passPrograss = (WaterWaveProView) findViewById(R.id.passPrograss);
//        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
//        showLoading();
//        setStatusLayout(layoutStatus);
//        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
//        recyclerIndex = (RecyclerView) findViewById(R.id.recycler_index);
//        topTitle = (RelativeLayout) findViewById(R.id.topTitle);
//        tvLoc = (ImageTextView) findViewById(R.id.tv_loc);
//        tvAreall = (LinearLayout) findViewById(R.id.tv_areall);
//        tvArealll = (LinearLayout) findViewById(R.id.tv_arealll);
//        selectIcon = (CornerImageView) findViewById(R.id.selectIcon);
//        selectClass = (TextView) findViewById(R.id.selectClass);
//        selectDate = (TextView) findViewById(R.id.selectDate);
//        selectDown = (ImageView) findViewById(R.id.selectDown);
//        passMessage = (ImageView) findViewById(R.id.passMessage);
//        ivBottomShader = (ImageView) findViewById(R.id.iv_bottom_shader);
//        passToTop = (ImageView) findViewById(R.id.passToTop);
//        layoutRefresh.setOnRefreshLoadMoreListener(this);
//        layoutStatus.setOnNetRetryListener(this);
//        passPrograss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DownLoadFragment.newInstance(SpUtils.getValue(mContext, SpKey.USE_UPDATE)).show(getChildFragmentManager(), "升级弹窗");
//            }
//        });
//        passToTop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                recyclerIndex.scrollToPosition(0);
//                topSpace.setVisibility(View.GONE);
//            }
//        });
//        recyclerIndex.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//
//                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
////                System.out.println("到达顶部了"+layoutManager.findFirstVisibleItemPosition());
//                if (indexTabAdapter.isStickyNow()&&layoutManager.findFirstVisibleItemPosition()>0) {
////                    System.out.println("到达顶部了");
//                    topSpace.setVisibility(View.VISIBLE);
//                    topTitle.setAlpha((0));
//                } else {
////                    System.out.println("到达下面了");
//                    topSpace.setVisibility(View.GONE);
//                }
//
//                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//                if (firstVisibleItemPosition > 1) {
//                    passToTop.setVisibility(View.VISIBLE);
//                } else {
//                    passToTop.setVisibility(View.GONE);
//                }
//            }
//        });
//    }
//
//    private void buildRecyclerView() {
//        isinit = 1;
//        virtualLayoutManager = new VirtualLayoutManager(mContext);
//        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
//        recyclerIndex.setLayoutManager(virtualLayoutManager);
//        recyclerIndex.setAdapter(delegateAdapter);
//        indexStatusAdapter = new IndexStatusAdapter();
//        indexStatusAdapter.setmContext(mActivity);
//        delegateAdapter.addAdapter(indexStatusAdapter);
//        indexStatusAdapter.setOnIndexListener(this);
//        indexFunctionAdapter = new IndexFunctionGridAdapter();
//        delegateAdapter.addAdapter(indexFunctionAdapter);
//        indexSumAdapter = new IndexSumAdapter();
//        delegateAdapter.addAdapter(indexSumAdapter);
//        indexBannerAdapter = new IndexBannerAdapter();
//        delegateAdapter.addAdapter(indexBannerAdapter);
//        indexTabAdapter = new IndexTabAdapter(getActivity());
//        delegateAdapter.addAdapter(indexTabAdapter);
//        indexTabEmptyAdapter = new IndexTabEmptyAdapter();
//        delegateAdapter.addAdapter(indexTabEmptyAdapter);
//        indexTabRecommandAdapter = new IndexTabRecommandAdapter();
//        delegateAdapter.addAdapter(indexTabRecommandAdapter);
//        indexTabIntercatAdapter = new PostAdapter();
//        delegateAdapter.addAdapter(indexTabIntercatAdapter);
//        indexTabFoundAdapter = new IndexTabFoundAdapter();
//        delegateAdapter.addAdapter(indexTabFoundAdapter);
//        indexTabQuestionAdapter = new IndexTabQuestionAdapter();
//        delegateAdapter.addAdapter(indexTabQuestionAdapter);
//        indexTabSalesAdapter = new IndexTabSalesAdapter();
//        delegateAdapter.addAdapter(indexTabSalesAdapter);
//        recyclerIndex.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (!recyclerView.canScrollVertically(-1)) {
//                    totalDy = 0;
//                }
//                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//                totalDy += dy;
//                if (topTitle.getHeight() > 0) {
//                    float alpha = Math.min(totalDy * 1.0f / (topTitle.getHeight() * 2), 1);
//                    topTitle.setAlpha((1 - alpha));
//                }
//                if(layoutManager.findFirstVisibleItemPosition()>=1){
//                    topTitle.setAlpha((0));
//                }
//            }
//        });
//        tvAreall.setOnClickListener(this);
//        passMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (topTitle.getAlpha() <= 0.5) {
//                    return;
//                }
//                ARouter.getInstance()
//                        .build(AppRoutes.APP_MESSAGE)
//                        .navigation();
//            }
//        });
//        tvLoc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (topTitle.getAlpha() <= 0.5) {
//                    return;
//                }
//                ARouter.getInstance().build(LibraryRoutes.LIBRARY_LOC)
//                        .navigation(mActivity, RC_PROVINCE_CITY);
//            }
//        });
//        indexTabAdapter.setOutClickListener(this);
//        indexStatusAdapter.setOutClickListener(this);
//        //indexTabIntercatAdapter.setOnChatLikeClickListener(this);
//        indexTabIntercatAdapter.setOnPostLikeClickListener(this);
//        //indexTabIntercatAdapter.setOnChatShareClickListener(this);
//        indexTabIntercatAdapter.setOnShareClickListener(this);
//        indexTabIntercatAdapter.setOutClickListener(this);
//        indexFunctionAdapter.setOutClickListener(this);
//        indexSumAdapter.setOutClickListener(this);
//        layoutRefresh.finishLoadMoreWithNoMoreData();
//    }
//
//    @Override
//    public void onRetryClick() {
//        super.onRetryClick();
//        getData();
//    }
//
//    @Override
//    public void getData() {
//        super.getData();
//        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {//判断定位权限
//            locate();//开始定位
//        } else {
//            requestPermissions(mPermissions, RC_LOCATION);
//        }
//    }
//
//    @Override
//    public void onRequestFinish() {
//        super.onRequestFinish();
//        layoutRefresh.finishRefresh();
//        layoutRefresh.finishLoadMore();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateTab(UpdateMessageInfo msg) {
//        if (!isFirstLoad) return;
//        CheckUtils.checkMessageCount(mContext, passMessage);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == RC_LOCATION) {
//            if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
//                locate();
//            } else {
//                Toast.makeText(mContext, "您已关闭定位将为您使用演示站数据", Toast.LENGTH_SHORT).show();
//                locate();
//            }
//        }
//    }
//
//    private void locate() {
//        mLocationClient = new AMapLocationClient(mContext);
//        mLocationOption = new AMapLocationClientOption();
//        mLocationOption.setOnceLocation(true);
//        mLocationClient.setLocationListener(this);
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        mLocationClient.setLocationOption(mLocationOption);
//        mLocationClient.startLocation();
//    }
//
//    private IndexBean mindexBean;// IndexStatusAdapter 的刷新状态 和这个有关 所以需要重新请求
//    private List<IndexMenu> indexMenus = new ArrayList<>();
//
//    @Override
//    public void getIndexSuccess(IndexBean indexBean) {
//        tvLoc.setText(LocUtil.getCityName(mContext, SpKey.LOC_CHOSE));//设置左上角城市
//        if (indexBean == null) {
//            return;
//        }
//        try {
//            if (chosePopupWindow != null) {
//                chosePopupWindow.dismiss();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mindexBean = indexBean;
//        indexMenus.clear();
//        indexMenus.add(new IndexMenu("同城圈", R.drawable.index_tchm));
//        if (!SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)) {//如果在审核
//            indexMenus.add(new IndexMenu("专家答疑", R.drawable.index_zjdy));
//        }
//        indexMenus.add(new IndexMenu("母婴商城", R.drawable.index_mysc));
//        indexMenus.add(new IndexMenu("新人礼包", R.drawable.index_xrlb));
//        if (!SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)) {//如果在审核
//        }
//        indexMenus.add(new IndexMenu("妈妈听听", R.drawable.index_mmat));
////        indexMenus.add(new IndexMenu("憨妈直播", R.drawable.index_zb));
//        if (!SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)) {//如果在审核
//            indexMenus.add(new IndexMenu("保险服务", R.drawable.index_lcbx));
//        }
////        indexMenus.add(new IndexMenu("技师招募", R.drawable.index_jszm));
//        indexMenus.add(new IndexMenu("憨妈赚", R.drawable.index_hmz));
//        indexMenus.add(new IndexMenu("孕育工具", R.drawable.index_hmbds));
//        indexMenus.add(new IndexMenu("憨妈课堂", R.drawable.index_hmkt));
//        indexMenus.add(new IndexMenu("育儿资讯", R.drawable.index_yezx));
//        if (!SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)) {//如果在审核
//        }
//        indexMenus.add(new IndexMenu("宝宝爱听", R.drawable.index_bbat));
//        if (BuildConfig.DEBUG || "test".equals(ChannelUtil.getChannel(getActivity()))) {
//            indexMenus.add(new IndexMenu("测试跳转", R.drawable.index_jszm));
//            indexMenus.add(new IndexMenu("Test", R.drawable.index_jszm));
//        }
//        List tmpsmens = new ArrayList();
//        tmpsmens.clear();
//        tmpsmens.add(indexMenus);
//        indexBannerAdapter.setModel("");
//        indexFunctionAdapter.setData((ArrayList) tmpsmens);
//        indexSumAdapter.setModel("");
//        indexTabAdapter.setModel("");
////        indexStatusAdapter.clear();
//        indexMainPresenter.getMine();
//        changeTab(false, nowTab);
////        indexMainPresenter.getVideoClassRoom(new SimpleHashMapBuilder<String, Object>());
//        indexMainPresenter.getGoodsHot(new SimpleHashMapBuilder<String, Object>()
//                .puts("type", 2 + "")
//                .puts("pageNum", 1 + "")
//                .puts("pageSize", "10")
//                .puts("firstPageSize", 0 + "")
//                .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)));
//        indexMainPresenter.getVideoOnline(new SimpleHashMapBuilder<String, Object>()
//                .puts("page", new SimpleHashMapBuilder<String, Object>()
//                        .puts("pageSize", 10 + "")
//                        .puts("pageNum", 1 + ""))
//                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)));
//        indexMainPresenter.getRecommandVideoClassRoom2(new SimpleHashMapBuilder<String, Object>());
//    }
//
//    @Override
//    public void onSucessGetAds(List<AdModel> adModels) {
//        if (adModels.size() > 0) {
//            final AdModel adModel = adModels.get(0);
//            if (adModel.type == 2 && adModel.triggerPage == 2) {
//                indexStatusAdapter.setBannerimgs(adModels);
//            }
//            if (adModel.type == 2 && adModel.triggerPage == 24) {
//                indexSumAdapter.setBannerimgs(adModels);
//            }
//            if (adModel.type == 2 && adModel.triggerPage == 25) {
//                indexBannerAdapter.setBannerimgs(adModels);
//            }
//            if (adModel.type == 1 && adModel.triggerPage == 1) {
//                SpUtils.store(mContext, SpKey.USER_Main_AD_Bean, new Gson().toJson(adModel));
//            }
//            if (adModel.type == 1 && adModel.triggerPage == 1) {
//                final String showTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//                final String limitTime = adModel.validityEnd;
//                boolean isTimeEndBefore = false;
//                try {
//                    isTimeEndBefore = new Date().before(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(limitTime));
//                } catch (Exception e) {
//                    isTimeEndBefore = true;
//                    e.printStackTrace();
//                }
//                if (isTimeEndBefore && !SpUtils.getValue(mContext, "Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage + "&" + showTime, false)) {
////                    System.out.println("需要展示广告:"+"Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage);
//                    com.healthy.library.businessutil.GlideCopy.with(this)
//                            .load(adModel.photoUrls)
//                            .dontAnimate()
//                            .into(new SimpleTarget<Drawable>() {
//                                @Override
//                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                                    super.onLoadFailed(errorDrawable);
//                                }
//
//                                @Override
//                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                    if (isfragmenthow) {
//                                        if (!SpUtils.getValue(mContext, "Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage + "&" + showTime, false)) {
////                                            System.out.println("需要展示广告Z:"+"Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage);
//                                            ADDialogView.newInstance().setAdModel(adModel).show(getChildFragmentManager(), "广告");
//                                            SpUtils.store(mContext, "Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage + "&" + showTime, true);
//                                        }
//                                    }
//                                }
//                            });
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onSucessGetBannerAds(List<AdModel> adModels) {
//
//    }
//
//    @Override
//    public void onSucessGetVideoOnlineList(List<LiveVideoMain> result, PageInfoEarly pageInfoEarly) {
//        if (result != null && result.size() > 0) {
//            indexSumAdapter.setVideoOnlineList(result);
//            indexSumAdapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void onSucessGetGoodsHotList(List<ActGoodsItem> result, int firstPageSize) {
//        if (result != null && result.size() > 0) {
//            indexSumAdapter.setHotGoodsList(result);
//            indexSumAdapter.notifyDataSetChanged();
//        } else {//说明没精选服务  那么请求自动推荐
//            indexMainPresenter.getGoodsHot2(new SimpleHashMapBuilder<String, Object>()
//                    .puts("type", 1 + "")
//                    .puts("pageNum", "1")
//                    .puts("pageSize", "10")
//                    .puts("firstPageSize", 0 + "")
//                    .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)));
//        }
//    }
//
//    @Override
//    public void onSucessGetGoodsHotList2(List<ActGoodsItem> result, int firstPageSize) {
//        if (result != null && result.size() > 0) {
//            indexSumAdapter.setHotGoodsList(result);
//            indexSumAdapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void onSuccessGetChatList(List<PostDetail> result, PageInfoEarly pageInfoEarly) {
//        indexTabEmptyAdapter.setModel(null);
//        if (result == null || result.size() == 0) {
//            layoutRefresh.finishLoadMoreWithNoMoreData();
//            if (page == 1 || page == 0) {
//                showAdpaterEmpty();
//            }
//        } else {
//            if (page == 1) {
//                clearAllUnderAdpaterEmpty();
//                ArrayList<PostDetail> tmp = new ArrayList<>();
//                for (int i = 0; i < result.size(); i++) {
//                    tmp.add(result.get(i));
//                    if (result.get(i).postingType == 5 || result.get(i).postingType == 6) {
//                        PostDetail postDetail = result.get(i);
//                        if (postDetail.postActivityList != null && postDetail.postActivityList.size() > 0) {
//
//                        } else {
//                            PostListSinglePresenter postListSinglePresenter = new PostListSinglePresenter(getContext(), this);
//                            postListSinglePresenter.getActivityList(new SimpleHashMapBuilder<String, Object>().puts("postingId", postDetail.id), postDetail);
//                        }
//                    }
//                }
//                indexTabIntercatAdapter.setData(tmp);
//
//            } else {
//                ArrayList<PostDetail> tmp = new ArrayList<>();
//                for (int i = 0; i < result.size(); i++) {
//                    tmp.add(result.get(i));
//                    if (result.get(i).postingType == 5 || result.get(i).postingType == 6) {
//                        PostDetail postDetail = result.get(i);
//                        if (postDetail.postActivityList != null && postDetail.postActivityList.size() > 0) {
//
//                        } else {
//                            PostListSinglePresenter postListSinglePresenter = new PostListSinglePresenter(getContext(), this);
//                            postListSinglePresenter.getActivityList(new SimpleHashMapBuilder<String, Object>().puts("postingId", postDetail.id), postDetail);
//                        }
//                    }
//                }
//                indexTabIntercatAdapter.addDatas(tmp);
//
//            }
//            layoutRefresh.setNoMoreData(false);
//            layoutRefresh.setEnableLoadMore(true);
//            if (pageInfoEarly.isMore != 1) {
//                layoutRefresh.finishLoadMoreWithNoMoreData();
//            }
//        }
//    }
//
//    @Override
//    public void onSuccessGetQuestion(List<FaqExportQuestion> result, PageInfoEarly pageInfoEarly) {
//        indexTabEmptyAdapter.setModel(null);
//        if (result == null || result.size() == 0) {
//            layoutRefresh.finishLoadMoreWithNoMoreData();
//            if (page == 1 || page == 0) {
//                showAdpaterEmpty();
//            }
//        } else {
//            if (page == 1) {
//                clearAllUnderAdpaterEmpty();
//                ArrayList<FaqExportQuestion> tmp = new ArrayList<>();
//                for (int i = 0; i < result.size(); i++) {
//                    tmp.add(result.get(i));
//                }
//                indexTabQuestionAdapter.setData(tmp);
//
//            } else {
//                ArrayList<FaqExportQuestion> tmp = new ArrayList<>();
//                for (int i = 0; i < result.size(); i++) {
//                    tmp.add(result.get(i));
//                }
//                indexTabQuestionAdapter.addDatas(tmp);
//
//            }
//            layoutRefresh.setNoMoreData(false);
//            layoutRefresh.setEnableLoadMore(true);
//            if (pageInfoEarly.isMore != 1) {
//                layoutRefresh.finishLoadMoreWithNoMoreData();
//            }
//        }
//    }
//
//    @Override
//    public void onSucessGetGoodsRecommandList(List<ActGoodsItem> result, int firstPageSize) {
//        indexTabEmptyAdapter.setModel(null);
//        if (result == null || result.size() == 0) {
//            layoutRefresh.finishLoadMoreWithNoMoreData();
//            if (page == 1 || page == 0) {
//                showAdpaterEmpty();
//            }
//        } else {
//            if (page == 1) {
//                this.firstPageSize = firstPageSize;
//                ArrayList<MultiItemEntity> tmp = new ArrayList<>();
//                for (int i = 0; i < result.size(); i++) {
//                    tmp.add(result.get(i));
//                }
//                clearAllUnderAdpaterEmpty();
//                indexTabSalesAdapter.setData(tmp);
//            } else {
//                ArrayList<MultiItemEntity> tmp = new ArrayList<>();
//                for (int i = 0; i < result.size(); i++) {
//                    tmp.add(result.get(i));
//                }
//                indexTabSalesAdapter.addDatas(tmp);
//            }
//            layoutRefresh.setNoMoreData(false);
//            layoutRefresh.setEnableLoadMore(true);
//        }
//
//    }
//
//    @Override
//    public void onSucessGetNewsList(List<NewsInfo> result, PageInfoEarly pageInfoEarly) {
//        indexTabEmptyAdapter.setModel(null);
//        if (result == null || result.size() == 0) {
//            layoutRefresh.finishLoadMoreWithNoMoreData();
//            if (page == 1 || page == 0) {
//                if (page == 1) {
//                    showAdpaterEmpty();
//                }
//            }
//        } else {
//            if (page == 1) {
//                clearAllUnderAdpaterEmpty();
//                ArrayList<NewsInfo> tmp = new ArrayList<>();
//                for (int i = 0; i < result.size(); i++) {
//                    tmp.add(result.get(i));
//                }
//                indexTabRecommandAdapter.setData(tmp);
//
//            } else {
//                ArrayList<NewsInfo> tmp = new ArrayList<>();
//                for (int i = 0; i < result.size(); i++) {
//                    tmp.add(result.get(i));
//                }
//                indexTabRecommandAdapter.addDatas(tmp);
//
//            }
//            layoutRefresh.setNoMoreData(false);
//            layoutRefresh.setEnableLoadMore(true);
//            if (pageInfoEarly.isMore != 1) {
//                layoutRefresh.finishLoadMoreWithNoMoreData();
//            }
//        }
//    }
//
//    private void showAdpaterEmpty() {
//        indexTabEmptyAdapter.setModel("null");
//        clearAllUnderAdpaterEmpty();
//    }
//
//    private void clearAllUnderAdpaterEmpty() {
//        indexTabRecommandAdapter.clear();
//        indexTabIntercatAdapter.clear();
//        indexTabFoundAdapter.clear();
//        indexTabQuestionAdapter.clear();
//        indexTabSalesAdapter.clear();
//        if (isTabClick) {
//            virtualLayoutManager.scrollToPositionWithOffset(delegateAdapter.getItemCount()-1, 0);
//            isTabClick = false;
//        }
//    }
//
//    private int dialogwidth = 220;
//    private List<UserInfoExModel> userInfoExModels = new ArrayList<>();
//
//    @Override
//    public void getAllStatusSuccess(List<UserInfoExModel> userInfoExModels) {
//        this.userInfoExModels.clear();
//        this.userInfoExModels.addAll(userInfoExModels);
//        for (int i = 0; i < this.userInfoExModels.size(); i++) {
//            if (this.userInfoExModels.get(i).useStatus == 1) {//说明被选中
//                SpUtils.storeJson(mContext, SpKey.USE_TOKEN, this.userInfoExModels.get(i));
//            }
//        }
//        try {
//            if (chosePopupWindow != null) {
//                chosePopupWindow.dismiss();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        chosePopupWindow = new ChosePopupWindow(mContext, (int) TransformUtil.dp2px(mContext, dialogwidth), userInfoExModels, new ChosePopupWindow.OnStatusClickListener() {
//            @Override
//            public void onClick(UserInfoExModel bean, View view) {
//                if (view.getId() == R.id.icon || view.getId() == R.id.stutll) {
//                    indexMainPresenter.changeStatus(bean.id + "");
//                    chosePopupWindow.dismiss();
//                } else {
//                    ARouter.getInstance()
//                            .build(MineRoutes.MINE_PERSONAL_INFO_DETAIL)
//                            .withString("id", bean.id + "")
//                            .navigation();
//                    chosePopupWindow.dismiss();
//                }
//
//
//            }
//        });
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (passMessage != null) {
//            CheckUtils.checkMessageCount(mContext, passMessage);
//        }
//        if (!TextUtils.isEmpty(LocUtil.getLatitude(mContext, SpKey.LOC_ORG))) {
//            if (hanMomPresenter != null) {
//                hanMomPresenter.getInfo(new SimpleHashMapBuilder<String, Object>()
//                        .puts("lat", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
//                        .puts("lng", LocUtil.getLongitude(mContext, SpKey.LOC_ORG)));
//            }
//        }
//        if (PermissionUtils.hasPermissions(mContext, mPermissions) && SpUtils.getValue(LibApplication.getAppContext(), SpKey.IS_LOCERROR, false)) {
//            try {
//                mLocationClient.startLocation();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void changeStatusSuccess() {
//        selectdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        successLocation();
//    }
//
//    @Override
//    public void onDateDecrease(Date date) {
//        selectdate = new SimpleDateFormat("yyyy-MM-dd").format(date);
//        indexMainPresenter.getIndexMain(new SimpleHashMapBuilder<String, Object>()
//                .puts("isCurrentCity", LocUtil.getCityNameOld(mContext, SpKey.LOC_CHOSE).equals(LocUtil.getCityNameOld(mContext, SpKey.LOC_ORG)) ? "1" : "0")
//                .puts("queryDate", selectdate)
//                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE))
//                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE))
//                .puts("areaNo", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
//                .puts("cityNo", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
//        );
//    }
//
//    @Override
//    public void onDateIncrease(Date date) {
//        selectdate = new SimpleDateFormat("yyyy-MM-dd").format(date);
//        indexMainPresenter.getIndexMain(new SimpleHashMapBuilder<String, Object>()
//                .puts("isCurrentCity", LocUtil.getCityNameOld(mContext, SpKey.LOC_CHOSE).equals(LocUtil.getCityNameOld(mContext, SpKey.LOC_ORG)) ? "1" : "0")
//                .puts("queryDate", selectdate)
//                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE))
//                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE))
//                .puts("areaNo", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
//                .puts("cityNo", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
//        );
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_PROVINCE_CITY && resultCode == Activity.RESULT_OK) {
//            successLocation();
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateUserInfo(UpdateUserInfoMsg msg) {
//        if (!isFirstLoad) return;
//        selectdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        page = 0;
//        indexMainPresenter.getIndexMain(new SimpleHashMapBuilder<String, Object>()
//                .puts("isCurrentCity", LocUtil.getCityNameOld(mContext, SpKey.LOC_CHOSE).equals(LocUtil.getCityNameOld(mContext, SpKey.LOC_ORG)) ? "1" : "0")
//                .puts("queryDate", selectdate)
//                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE))
//                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE))
//                .puts("areaNo", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
//                .puts("cityNo", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
//        );
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateDownLoadPrograss(UpdateDownLoadInfoMsg msg) {
//        if (passPrograss != null) {
//            try {
//                passPrograss.setVisibility(View.VISIBLE);
//                passPrograss.setProgress(msg.prograss);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void updateLocData() {
//        getIndexSuccess(mindexBean);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateUserLocation(UpdateUserLocationMsg msg) {
//        if (!isFirstLoad) return;
//        selectdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        page = 0;
//        updateLocData();
//    }
//
//    private UserInfoMonModel muserInfoMonModel;
//    private boolean isBlack = false;
//
//    @Override
//    public void getMineSuccess(UserInfoMonModel userInfoMonModel) {
//        if (userInfoMonModel == null) {
//            indexStatusAdapter.setModel(mindexBean == null ? new IndexBean(-1) : mindexBean);
//        } else {
//            indexStatusAdapter.setModel(mindexBean == null ? new IndexBean(userInfoMonModel.status) : mindexBean);
//        }
//        indexMainPresenter.getTongLianPhoneStatus(new SimpleHashMapBuilder<String, Object>());
//        if (userInfoMonModel == null) {
//            SpUtils.store(mContext, "isShowZZ", true);
//            EventBus.getDefault().post(new CanUpdateTab(2));
//            return;
//        }
//        muserInfoMonModel = userInfoMonModel;
//        if (muserInfoMonModel != null) {
//            SpUtils.store(mContext, SpKey.USER_NICK, muserInfoMonModel.nickName);
//            SpUtils.store(mContext, SpKey.USER_ICON, muserInfoMonModel.faceUrl);
//            SpUtils.store(mContext, SpKey.USER_BIRTHDAY, muserInfoMonModel.birthday);
//            //接口请求成功情况下进行弹框
//            if (userInfoMonModel != null) {
//                showDialog();
//            }
//        }
//        indexStatusAdapter.setUserInfoMonModel(userInfoMonModel);
//        if (userInfoMonModel.faceUrl != null) {
//            Glide.with(selectIcon.getContext())
//                    .asBitmap()
//                    .load(userInfoMonModel.faceUrl)
//                    .placeholder(userInfoMonModel.memberSex == 2 ? R.drawable.img_avatar_default : R.drawable.img_avatar_default_man)
//                    .error(userInfoMonModel.memberSex == 2 ? R.drawable.img_avatar_default : R.drawable.img_avatar_default_man)
//                    .dontAnimate()
//                    .into(new BitmapImageViewTarget(selectIcon) {
//                        @Override
//                        protected void setResource(Bitmap resource) {
//                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
//                            circularBitmapDrawable.setCircular(true);
//                            selectIcon.setImageDrawable(circularBitmapDrawable);
//                        }
//                    });
//        }
//        indexStatusAdapter.setMemberSex(userInfoMonModel.memberSex + "");
//        if (TextUtils.isEmpty(userInfoMonModel.dateContent) || userInfoMonModel.status == 4 || userInfoMonModel.dateContent.contains("岁") || userInfoMonModel.dateContent.contains("年") || (userInfoMonModel.memberSex == 1 && userInfoMonModel.status == 1)) {
//            isBlack = true;
//            selectClass.setTextColor(Color.parseColor("#FFFFFF"));
//        } else {
//            selectClass.setTextColor(Color.parseColor("#FFFFFF"));
//            isBlack = false;
//        }
//        selectDown.setImageResource(isBlack ? R.drawable.ic_triangle_down_green : R.drawable.ic_triangle_down_white);
//        if (userInfoMonModel.dateContent != null && userInfoMonModel.dateContent.contains("宝宝")) {
//            selectClass.setText(userInfoMonModel.babyName);
//            Glide.with(selectIcon.getContext())
//                    .asBitmap()
//                    .load(userInfoMonModel.babySex == 0 ? R.drawable.app_status_girl : R.drawable.app_status_boy)
//                    .placeholder(userInfoMonModel.babySex == 0 ? R.drawable.app_status_girl : R.drawable.app_status_boy)
//                    .error(userInfoMonModel.babySex == 0 ? R.drawable.app_status_girl : R.drawable.app_status_boy)
//                    .dontAnimate()
//                    .into(new BitmapImageViewTarget(selectIcon) {
//                        @Override
//                        protected void setResource(Bitmap resource) {
//                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
//                            circularBitmapDrawable.setCircular(true);
//                            selectIcon.setImageDrawable(circularBitmapDrawable);
//                        }
//                    });
//            if (userInfoMonModel.dateContent.contains("年") || userInfoMonModel.dateContent.contains("岁")) {
//                try {
//                    selectDate.setText(userInfoMonModel.dateContent.split(" ")[1]);
//                } catch (Exception e) {
//                    selectDate.setText(userInfoMonModel.dateContent.replace("宝宝", ""));
//                    e.printStackTrace();
//                }
//            } else {
//                selectDate.setText("");
//            }
//        } else {
//            selectClass.setText(userInfoMonModel.statusName);
//            selectDate.setText("");
//        }
//        if (!SpUtils.getValue(mContext, "isShowZZ", false)) {
//            if (TextUtils.isEmpty(userInfoMonModel.dateContent) && TextUtils.isEmpty(userInfoMonModel.statusName)) {
//                selectClass.setText("资料越全面，服务更贴心");
//                if (SpUtils.isFirst(mContext, "指导出现")) {
//                    EventBus.getDefault().post(new UpdateGuideInfo(0));
//                } else {
//                    SpUtils.store(mContext, "isShowZZ", true);
//                    EventBus.getDefault().post(new CanUpdateTab(2));
//                }
//            } else {
//                if (SpUtils.isFirst(mContext, "指导出现")) {
//                    EventBus.getDefault().post(new UpdateGuideInfo(3));
//                } else {
//                    SpUtils.store(mContext, "isShowZZ", true);
//                    EventBus.getDefault().post(new CanUpdateTab(2));
//                }
//            }
//        } else {
//            if (TextUtils.isEmpty(userInfoMonModel.dateContent) && TextUtils.isEmpty(userInfoMonModel.statusName)) {
//                selectClass.setText("资料越全面，服务更贴心");
//            }
//        }
//        indexStatusAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onSuccessOther() {
//
//    }
//
//    @Override
//    public void onSuccessGetRecommandVideoClassRoom(List<VideoListModel> result, PageInfoEarly pageInfoEarly) {
//        indexTabEmptyAdapter.setModel(null);
//        if (result == null || result.size() == 0) {
//            layoutRefresh.finishLoadMoreWithNoMoreData();
//            if (page == 1 || page == 0) {
//                showAdpaterEmpty();
//            }
//        } else {
//            if (page == 1) {
//                clearAllUnderAdpaterEmpty();
//                ArrayList<VideoListModel> tmp = new ArrayList<>();
//                for (int i = 0; i < result.size(); i++) {
//                    tmp.add(result.get(i));
//                }
//                indexTabFoundAdapter.setData(tmp);
//            } else {
//                ArrayList<VideoListModel> tmp = new ArrayList<>();
//                for (int i = 0; i < result.size(); i++) {
//                    tmp.add(result.get(i));
//                }
//                indexTabFoundAdapter.addDatas(tmp);
//
//            }
//            if (pageInfoEarly.nextPage == 0) {
//                layoutRefresh.finishLoadMoreWithNoMoreData();
//            } else {
//                layoutRefresh.setNoMoreData(false);
//                layoutRefresh.setEnableLoadMore(true);
//            }
//        }
//    }
//
//    @Override
//    public void onSuccessGetRecommandVideoClassRoom2(List<VideoListModel> result, PageInfoEarly pageInfoEarly) {
//        if (result != null && result.size() > 0) {
//            indexSumAdapter.setFindVideoList(result);
//            indexSumAdapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void onSuccessGetPostList(List<PostDetail> posts, PageInfoEarly pageInfoEarly) {
//
//    }
//
//    @Override
//    public void onSuccessLike() {
//
//    }
//
//    @Override
//    public void onSuccessGetActivityList() {
//        if (indexTabIntercatAdapter != null) {
//            indexTabIntercatAdapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void onSuccessFan(Object result) {
//        if (indexTabIntercatAdapter != null && position > -1 && result != null) {
//            if (indexTabIntercatAdapter.getDatas() == null) {
//                return;
//            }
//            try {
//                PostDetail postDetail = indexTabIntercatAdapter.getDatas().get(position);
//                postDetail.focusStatus = "0".equals(result) ? 1 : 0;
//                indexTabIntercatAdapter.notifyItemChanged(position, "fans");
//                position = -1;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void onSuccessFan() {
//
//    }
//
//    @Override
//    public void onSuccessTongLianPhoneStatus(boolean isPhoneChecked) {
//        SpUtils.store(LibApplication.getAppContext(),SpKey.TONGLIANPHONEHAS,isPhoneChecked);
//    }
//
//    private String mDate;
//    private UpdateUserInfoDialog mUserInfoDialog;
//
//    private void showDialog() {
//        //获取当天时间 格式（yyyy-MM-dd)
//        String format = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
//
//        if (!format.equals(mDate)) {
//            mUserInfoDialog = null;
//        }
//        mDate = format;
//        String mUserDialogTime = SpUtils.getValue(mContext, SpKey.USER_DIALOG_TIME);
//        //存入的时间不和当天时间一致 切 dialog为空
//        if (mUserInfoDialog == null) {
//            String birthday = SpUtils.getValue(mContext, SpKey.USER_BIRTHDAY);
//            String mUserCreateDate = SpUtils.getValue(mContext, SpKey.USER_CREATE_DATE);
//            SpUtils.store(mContext, SpKey.USER_DIALOG_TIME, mDate);
//            //如果生日为空并且不是当天注册的 进行提醒用户
//            if (TextUtils.isEmpty(birthday)
//                    && !TextUtils.isEmpty(mUserCreateDate) //多加一层判断创建时间为空的情况（阶段未选择时间为空）
//                    && !format.equals(mUserCreateDate)
//                    && !mDate.equals(mUserDialogTime)) {
//                //完善用户信息提示弹框
//                mUserInfoDialog = UpdateUserInfoDialog.newInstance();
//                mUserInfoDialog.show(getChildFragmentManager(), "UpdateUserInfo");
//            } else {
//                if (!TextUtils.isEmpty(birthday)) {//生日不等于空 则要开始告诉首页去算优惠券了
//                }
//            }
//        }
//
//        EventBus.getDefault().post(new UpdateGiftInfo(false));
//    }
//
//    @Override
//    public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
//        page++;
//        changeTab(false, nowTab);
//    }
//
//    @Override
//    public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
//        page = 1;
//        successLocation();
//    }
//
//    public void changeTab(boolean needTabTop, String index) {
//        if ("头条".equals(nowTab) && 0 == page && indexTabRecommandAdapter.getDatas().size() > 0) {//就不重复请求了 如果是1
//            return;
//        }
//        if ("问答".equals(nowTab) && 0 == page && indexTabQuestionAdapter.getDatas().size() > 0) {//就不重复请求了 如果是1
//            return;
//        }
//        if (page == 0) {
//            page = 1;
//        }
//        nowTab = index;
//        this.isTabClick = needTabTop;
//        if (page == 1 || page == 0) {
//            layoutRefresh.setNoMoreData(false);
//            layoutRefresh.setEnableLoadMore(true);
//            firstPageSize = 0;
//        }
//        if ("头条".equals(nowTab)) {//请求新闻
//            indexMainPresenter.getRecommandNews(new SimpleHashMapBuilder<String, Object>()
//                    .puts("addressCity", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
//                    .puts("addressProvince", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
//                    .puts("addressArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
//                    .puts("currentPage", page + "")
//                    .puts("pageSize", pageSize + "")
//                    .puts("queryDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
//            );
//        } else if ("互动".equals(nowTab)) {
//            indexMainPresenter.getRecommandChat(new SimpleHashMapBuilder<String, Object>()
//                    .puts("addressCity", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
//                    .puts("addressProvince", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
//                    .puts("addressArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
//                    .puts("currentPage", page + "")
//                    .puts("pageSize", pageSize + "")
//                    .puts("type", 1 + "")
//                    .puts("type2", 1 + "")
//            );
//        } else if ("知识".equals(nowTab)) {
//            indexMainPresenter.getRecommandVideoClassRoom(new SimpleHashMapBuilder<String, Object>()
//                    .puts("pageNum", page).puts("pageSize", "8")
//                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
//        } else if ("问答".equals(nowTab)) {
//            indexMainPresenter.getRecommandQuestion(new SimpleHashMapBuilder<String, Object>()
//                    .puts("currentPage", page + "")
//                    .puts("pageSize", pageSize + "")
//            );
//        } else {
//            indexMainPresenter.getRecommandGoods(new SimpleHashMapBuilder<String, Object>()
//                    .puts("type", 1 + "")
//                    .puts("pageNum", page + "")
//                    .puts("pageSize", pageSize + "")
//                    .puts("firstPageSize", firstPageSize + "")
//                    .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
//            );
//        }
//    }
//
//    int retryTime = 0;
//
//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
////        System.out.println("定位没开吗");
//        if (!checkGPSIsOpen()) {
//            if (retryTime == 0) {
//                layoutStatus.updateStatus(StatusLayout.Status.STATUS_CUSTOM);
//                layoutStatus.setmOnCustomNetRetryListener(new OnCustomRetryListener() {
//                    @Override
//                    public void onRetryClick() {
//                        mLocationClient.startLocation();
//                    }
//                });
//                retryTime = 1;
//            }
//
//        }
//        if (aMapLocation == null) {//120.575758,31.302379
//            return;
//        }
//        if (aMapLocation.getErrorCode() != 0) {//120.575758,31.302379
//            if (!PermissionUtils.hasPermissions(mContext, mPermissions)) {
//                System.out.println("定位没开");
//                SpUtils.store(LibApplication.getAppContext(), SpKey.IS_LOCERROR, true);
//                aMapLocation = new AMapLocationBd("憨妈妈", "虎丘区", "苏州市", "江苏省", "320505", 31.302379, 120.575758).build();
//            }
//        }
//        if (aMapLocation.getErrorCode() == 0) {
//            LocUtil.storeLocation(mContext, aMapLocation);
////            System.out.println("定位没开吗设置定位地点" + SpUtils.getValue(LibApplication.getAppContext(), SpKey.IS_LOCERROR, false));
//            if (LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE) == null || SpUtils.getValue(LibApplication.getAppContext(), SpKey.IS_LOCERROR, false)) {
//                LocUtil.storeLocationChose(mContext, aMapLocation);
//                if (PermissionUtils.hasPermissions(mContext, mPermissions) && SpUtils.getValue(LibApplication.getAppContext(), SpKey.IS_LOCERROR, false)) {//定位开了 但是之前定位没开
//                    System.out.println("定位没开又开了重新设置");
//                    SpUtils.store(LibApplication.getAppContext(), SpKey.IS_LOCERROR, false);
//                    SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_MC, null);
//                    SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOP, null);
//                    SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOPNAME, null);
//                    SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOPNAMEDETAIL, null);
//                    SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOPADDRESS, null);
//                    SpUtils.store(LibApplication.getAppContext(), SpKey.CHOSE_SHOPDISTANCE, null);
//                    SpUtils.store(LibApplication.getAppContext(), SpKey.SHOP_BRAND, null);
//                }
//            }
//            EventBus.getDefault().post(new UpdateUserLocationMsg());
//            successLocation();
//        } else {
//            layoutStatus.updateStatus(StatusLayout.Status.STATUS_CUSTOM);
//            layoutStatus.setmOnCustomNetRetryListener(new OnCustomRetryListener() {
//                @Override
//                public void onRetryClick() {
//                    mLocationClient.startLocation();
//                }
//            });
//            if (NavigateUtils.openGPSSettings(mContext) && reLocTime <= 1) {
//                mLocationClient.startLocation();
//                reLocTime++;
//            } else {
//                MessageDialog.newInstance()
//                        .setMessageTopRes(R.drawable.dialog_message_icon_loc, "开启定位", "为给您提供更好的本地化服务，请您到系统设置中打开GPS定位")
//                        .setMessageOkClickListener(new MessageDialog.MessageOkClickListener() {
//                            @Override
//                            public void onMessageOkClick(View view) {
//                                IntentUtils.toLocationSetting(mContext);
//                            }
//                        })
//                        .show(getChildFragmentManager(), "打开定位");
//
//            }
//        }
//    }
//
//    private void successLocation() {
//        tvLoc.setText(LocUtil.getCityName(mContext, SpKey.LOC_CHOSE));
//        new ActH5Presenter(mContext).getH5();
//        new LocVipPresenter(mContext, this).getLocVip(new SimpleHashMapBuilder<String, Object>());
//        indexMainPresenter.getAllStatus();
//        indexStatusAdapter.setAreaNo(LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
//        indexMainPresenter.getIndexMain(new SimpleHashMapBuilder<String, Object>()
//                .puts("isCurrentCity", LocUtil.getCityNameOld(mContext, SpKey.LOC_CHOSE).equals(LocUtil.getCityNameOld(mContext, SpKey.LOC_ORG)) ? "1" : "0")
//                .puts("queryDate", selectdate)
//                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE))
//                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE))
//                .puts("areaNo", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
//                .puts("cityNo", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
//        );
//        hanMomPresenter.getInfo(new SimpleHashMapBuilder<String, Object>()
//                .puts("lat", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
//                .puts("lng", LocUtil.getLongitude(mContext, SpKey.LOC_ORG)));
//        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>()
//                .puts("type", "2").puts("triggerPage", "2")
//                .puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
//        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>()
//                .puts("type", "1").puts("triggerPage", "1")
//                .puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
//        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>()
//                .puts("type", "2").puts("triggerPage", "24")
//                .puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
//        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>()
//                .puts("type", "2").puts("triggerPage", "25")
//                .puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
//    }
//
//    private boolean checkGPSIsOpen() {
//        boolean isOpen;
//        LocationManager locationManager = (LocationManager) getActivity()
//                .getSystemService(Context.LOCATION_SERVICE);
//        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        return isOpen;
//    }
//
//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//        if (v.getId() == R.id.tv_areall) {
//            if (topTitle.getAlpha() <= 0.5) {
//                return;
//            }
//            int[] location = new int[2];
//            v.getLocationOnScreen(location);
//            if (chosePopupWindow != null) {
//                chosePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        selectDown.setImageResource(isBlack ? R.drawable.ic_triangle_down_green : R.drawable.ic_triangle_down_white);
//                        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                        lp.alpha = 1f;
//                        getActivity().getWindow().setAttributes(lp);
//                    }
//                });
//                if (userInfoExModels.size() > 0 && !"资料越全面，服务更贴心".equals(selectClass.getText().toString())) {
//                    chosePopupWindow.showAsDropDown(v, -(int) (chosePopupWindow.getWidth() / 2 - tvAreall.getWidth() / 2), 0);
//                    selectDown.setImageResource(isBlack ? R.drawable.ic_triangle_up_green : R.drawable.ic_triangle_up_white);
//                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                    lp.alpha = 0.4f;
//                    getActivity().getWindow().setAttributes(lp);
//                } else {
//                    MobclickAgent.onEvent(mContext, "event2ClassEnoughtDetail", new SimpleHashMapBuilder().puts("soure", "首页"));
//                    Toast.makeText(LibApplication.getAppContext(), "请完善阶段信息", Toast.LENGTH_SHORT).show();
//                    ARouter.getInstance()
//                            .build(MineRoutes.MINE_PERSONAL_INFO_LIST)
//                            .navigation();
//                }
//            }
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void guideInfo(UpdateGuideInfo msg) {
//        if (!isFirstLoad) return;
//        if (msg.flag == 0) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    GuideViewHelp.showGuideRoundRectangleRelativeSpecial(mActivity, "完善资料Guide", true, tvArealll, R.layout.view_guide_type1, Gravity.BOTTOM, 10, new OnGuideChangedListener() {
//                        @Override
//                        public void onShowed(Controller controller) {
//                        }
//
//                        @Override
//                        public void onRemoved(Controller controller) {
//                            EventBus.getDefault().post(new UpdateGuideInfo(1));
//
//                        }
//                    });
//                }
//            }, 800);
//
//        }
//        if (msg.flag == 1) {
//            if (SpUtils.getValue(mContext, SpKey.AUDITSTATUS, false)) {
//                SpUtils.store(mContext, "isShowZZ", true);
//                EventBus.getDefault().post(new CanUpdateTab(2));
//                EventBus.getDefault().post(new UpdateGuideInfo(2));
//                return;
//            }
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        while (virtualLayoutManager.findViewByPosition(1) == null) {
//                            try {
//                                Thread.sleep(200);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        while (virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid) == null) {
//                            try {
//                                Thread.sleep(200);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        while (((GridLayout) virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid)).getChildAt(posFaq) == null) {
//                            try {
//                                Thread.sleep(200);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        ((GridLayout) virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid)).getChildAt(posFaq).postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                GuideViewHelp.showGuideRoundRelativeSpecial(getActivity(), "专家答疑", true, ((GridLayout) virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid)).getChildAt(posFaq), R.layout.view_guide_type2, Gravity.BOTTOM, 10, new OnGuideChangedListener() {
//                                    @Override
//                                    public void onShowed(Controller controller) {
//
//                                    }
//
//                                    @Override
//                                    public void onRemoved(Controller controller) {
//                                        SpUtils.store(mContext, "isShowZZ", true);
//                                        EventBus.getDefault().post(new CanUpdateTab(2));
//                                        EventBus.getDefault().post(new UpdateGuideInfo(2));
//                                    }
//                                });
//                            }
//                        }, 800);
//                    } catch (Exception e) {
//
//                        SpUtils.store(mContext, "isShowZZ", true);
//                        EventBus.getDefault().post(new CanUpdateTab(2));
//                        EventBus.getDefault().post(new UpdateGuideInfo(2));
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//
//        }
//        if (msg.flag == 3) {
//            if (SpUtils.getValue(mContext, SpKey.AUDITSTATUS, false)) {
//                SpUtils.store(mContext, "isShowZZ", true);
//                EventBus.getDefault().post(new CanUpdateTab(2));
//                EventBus.getDefault().post(new UpdateGuideInfo(2));
//                return;
//            }
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    try {
//                        while (virtualLayoutManager.findViewByPosition(1) == null) {
//                            try {
//                                Thread.sleep(200);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        while (virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid) == null) {
//                            try {
//                                Thread.sleep(200);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        while (((GridLayout) virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid)).getChildAt(posFaq) == null) {
//                            try {
//                                Thread.sleep(200);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        ((GridLayout) virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid)).getChildAt(posFaq).postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                View desview = ((GridLayout) virtualLayoutManager.findViewByPosition(1).findViewById(R.id.functionGrid)).getChildAt(posFaq);
//                                if (desview == null) {
//                                    System.out.println("引导查看:出问题");
//                                    SpUtils.store(mContext, "isShowZZ", true);
//                                    EventBus.getDefault().post(new CanUpdateTab(2));
//                                    EventBus.getDefault().post(new UpdateGuideInfo(2));
//                                } else {
//                                    System.out.println("引导查看:正常");
//                                    GuideViewHelp.showGuideRoundRelativeSpecial(getActivity(), "专家答疑", true, desview, R.layout.view_guide_type2, Gravity.BOTTOM, 10, new OnGuideChangedListener() {
//                                        @Override
//                                        public void onShowed(Controller controller) {
//
//                                        }
//
//                                        @Override
//                                        public void onRemoved(Controller controller) {
//                                            SpUtils.store(mContext, "isShowZZ", true);
//                                            EventBus.getDefault().post(new CanUpdateTab(2));
//                                            EventBus.getDefault().post(new UpdateGuideInfo(2));
//                                        }
//                                    });
//                                }
//
//                            }
//                        }, 800);
//                    } catch (Exception e) {
//                        SpUtils.store(mContext, "isShowZZ", true);
//                        EventBus.getDefault().post(new CanUpdateTab(2));
//                        EventBus.getDefault().post(new UpdateGuideInfo(2));
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//
//        }
//    }
//
//    @Override
//    public void onGetInfoSuccess(HanMomInfoModel.MemberInfoModel memberInfoModel) {
//        if (memberInfoModel != null && memberInfoModel.partnerId != null && !TextUtils.isEmpty(memberInfoModel.partnerId)) {
//            //System.out.println("设置HmmVip");
//            SpUtils.store(mContext, "isHanMomVip", true);
//            //isHanMomVip = true;// 6. 当memberInfo存在，且memberInfo的partnerId不为空或NULL时，跳转小程序。
//        } else {
//            SpUtils.store(mContext, "isHanMomVip", false);
//        }
//    }
//
//    private LocVip resolveLocVipData(String obj) {
//        LocVip result = new LocVip();
//        try {
//            JSONObject data = new JSONObject(obj).getJSONObject("data");
//            String userShopInfoDTOS = data.toString();
//            GsonBuilder builder = new GsonBuilder();
//            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                    return new Date(json.getAsJsonPrimitive().getAsLong());
//                }
//            });
//            Gson gson = builder.create();
//            Type type = new TypeToken<LocVip>() {
//            }.getType();
//            result = gson.fromJson(userShopInfoDTOS, type);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public void outClick(@NotNull String function, @NotNull Object obj) {
//        if (function.contains("测试")) {
//            ARouter.getInstance()
//                    .build(LibraryRoutes.LIBRARY_TMP)
//                    .navigation();
//        }
//
//        if ("Test".equals(function)) {
////            ARouter.getInstance()
////                    .build(SecondRoutes.SECOND_SHOP_DETAIL)
////                    .withString("shopId", "5")
////                    .navigation();
//        }
//
//        if ("测试跳转".equals(function)) {
////            HttpTmpResult httpTmpResult = ObjUtil.getObj(SpUtils.getValue(mContext, "fun100001"), HttpTmpResult.class);
////            if (httpTmpResult != null && !TextUtils.isEmpty(LocUtil.getHmmDepartId()) && System.currentTimeMillis() - httpTmpResult.timeStamp < 3 * 60 * 1000) {//门店列表处于有效期
////                LocVip locVip = resolveLocVipData(httpTmpResult.result);
////                List<LocVip.Local.MerchantsShop> shops = locVip.getAllMerchantShopWithMe();
////                Log.v("新客礼包有无", "开始");
////                for (int i = 0; i < shops.size(); i++) {
////                    LocVip.Local.MerchantsShop shop = shops.get(i);
////                    new TestPresenter(mContext).getTestData(new SimpleHashMapBuilder<String, Object>()
////                                    .puts("departId", shop.departId)
////                                    .puts("ytbAppId", shop.ytbAppId)
////                                    .puts("birthday", SpUtils.getValue(mContext, SpKey.USER_BIRTHDAY))
////                                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
////
////                            , shop.merchantName);
////                }
////            }
////            PkVotingDialog pkVotingDialog = PkVotingDialog.Companion.newInstance();
////            pkVotingDialog.show(getChildFragmentManager(), "SharePkDialog");
//        }
//
//
//        if ("切换Tab".equals(function)) {
//            String pos = (String) obj;
//            page = 1;
//            changeTab(true, pos);
//        }
//
//        if ("保险服务".equals(function)) {
//            String url = "https://cps.qixin18.com/m/lj1059667/media.html";
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_WEBVIEW)
//                    .withString("title", "保险服务")
//                    .withBoolean("doctorshop", true)
//                    .withBoolean("isinhome", false)
//                    .withString("url", url)
//                    .navigation();
//        }
//        if ("母婴商品".equals(function)) {
//            EventBus.getDefault().post(new TabChangeModel(3));
//            EventBus.getDefault().post(new ServiceTabChangeModel(1));
//        }
//        if ("妈妈听听".equals(function)) {
//            ARouter.getInstance()
//                    .build(SoundRoutes.SOUND_MAIN_MON)
//                    .withString("audioType", "2")
//                    .navigation();
//        }
//        if ("宝宝爱听".equals(function)) {
//            ARouter.getInstance()
//                    .build(SoundRoutes.SOUND_MAIN)
//                    .withString("audioType", "1")
//                    .navigation();
//        }
//        if ("憨妈课堂".equals(function)) {
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_VIDEOONLINELIST)
//                    .withString("type", "0")
//                    .navigation();
//        }
//        if ("hanVideo".equals(function)) {
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_VIDEOONLINELIST)
//                    .withString("type", "1")
//                    .navigation();
//        }
//        if ("育儿资讯".equals(function)) {
//            MobclickAgent.onEvent(mContext, "event2HomeSeeMoreClick", new SimpleHashMapBuilder().puts("soure", "首页"));
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_MAINPASSNEWS)
//                    .withString("knowOrInfoStatus", "2")
//                    .withString("queryDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
//                    .navigation();
//        }
//        if ("新人礼包".equals(function)) {
//            if(TextUtils.isEmpty(LocUtil.getytbAppId())){
//                Toast.makeText(mContext,"为您精心配置中,请稍后...",Toast.LENGTH_LONG).show();
//                return;
//            }
//            ARouter.getInstance().build(DiscountRoutes.MINE_NEW_USER_GIFT)
//                    .navigation();
//        }
//        if ("憨妈直播".equals(function)) {
//            EventBus.getDefault().post(new TabChangeModel(2));
//        }
//        if ("同城圈".equals(function)) {
//            EventBus.getDefault().post(new TabChangeModel(1));
//        }
//        if ("技师招募".equals(function)) {
//            ARouter.getInstance()
//                    .build(MineRoutes.MINE_JOB_TYPE)
//                    .navigation();
//        }
//        if ("母婴商城".equals(function)) {
//            Map nokmap = new HashMap<String, String>();
//            nokmap.put("soure", "憨妈妈首页导航商城点击量");
//            MobclickAgent.onEvent(mContext, "event2APPHomeNavigationShopClick", nokmap);
//            EventBus.getDefault().post(new TabChangeModel(3));
//            EventBus.getDefault().post(new ServiceTabChangeModel(0));
//        }
//        if ("孕育工具".equals(function)) {
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_TOOLS_TYPE)
//                    .navigation();
//        }
//        if ("专家答疑".equals(function)) {
//            ARouter.getInstance()
//                    .build(AppRoutes.MODULE_EXPERT_FAQ2)
//                    .withString("cityNo", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
//                    .navigation();
//        }
//        if ("母婴教育".equals(function)) {
//            EventBus.getDefault().post(new TabChangeModel(3));
//            EventBus.getDefault().post(new ServiceTabChangeModel(2));
//        }
//        if ("憨妈赚".equals(function)) {
//            if (SpUtils.getValue(mContext, "isHanMomVip", false) == true) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        String appId = Ids.WX_APP_ID; // 本应用微信AppId
//                        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);
//                        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//                        req.userName = "gh_93d673cec6a8"; // 小程序原始id
//                        req.path = "";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
//                        if (ChannelUtil.isIpRealRelease()) {
//                            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
//                        } else {
//                            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
//                        }
//                        api.sendReq(req);
//                    }
//                }, 500);
//
//            } else {
//                ARouter.getInstance().build(MineRoutes.MINE_HANMOM)
//                        .navigation();
//            }
//        }
//        if (function.equals("极速问诊")) {
//            String url = "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/ConsultQuick";
//            url = String.format(url,
//                    new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_WEBVIEW)
//                    .withBoolean("isNeedRef", true)
//                    .withString("title", "极速问诊")
//                    .withBoolean("isinhome", false)
//                    .withBoolean("doctorshop", true)
//                    .withString("url", url)
//                    .navigation();
//        }
//        if (function.equals("品质优选")) {
//            EventBus.getDefault().post(new TabChangeModel(3));
//        }
//        if (function.equals("名医在线")) {
//            String url = "https://h5.yewyw.com/index.html?bmark=hmama&appid=977&flag=app&appUserId=%s&appUserPhone=18511557625#/DoctorList?deptType=3&sortType=0&titleLv=0&labelStr=";
//            url = String.format(url,
//                    new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_WEBVIEW)
//                    .withBoolean("isNeedRef", true)
//                    .withString("title", "名医在线")
//                    .withBoolean("doctorshop", true)
//                    .withBoolean("isinhome", false)
//                    .withString("url", url)
//                    .navigation();
//        }
//        if (function.equals("coupon")) {
//            if (couponDialog == null) {
//                couponDialog = PostCouponDialog.newInstance();
//            }
//            couponDialog.setId((String) obj);
//            couponDialog.show(getChildFragmentManager(), "");
//            couponDialog.setOnConfirmClick(new PostCouponDialog.OnSelectConfirm() {
//                @Override
//                public void onClick(int result) {
//                    page = 1;
//                    indexMainPresenter.getRecommandChat(new SimpleHashMapBuilder<String, Object>()
//                            .puts("addressCity", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
//                            .puts("addressProvince", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
//                            .puts("addressArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
//                            .puts("currentPage", page + "")
//                            .puts("pageSize", pageSize + "")
//                            .puts("type", 1 + "")
//                            .puts("type2", 1 + "")
//                    );
//                }
//            });
//        }
//        if (function.equals("submit")) {
//            showReviewWarnDialog((String) obj);
//        }
//        if (function.equals("position")) {
//            position = (Integer) obj;//要刷新的item
//        }
//        if (function.equals("sharePk")) {
//            dataStatisticsPresenter.shareAndLook(new SimpleHashMapBuilder<String, Object>().puts("sourceId", (String) obj).puts("sourceType", 2).puts("type", 2));
//        }
//        if (function.equals("focus")) {
//            final PostDetail postDetail = (PostDetail) obj;
//            if (postDetail.focusStatus == 0) {
//                indexMainPresenter.fan(new SimpleHashMapBuilder<String, Object>()
//                        .puts("friendId", postDetail.memberId)
//                        .puts("", postDetail.createSource)
//                        .puts("type", postDetail.focusStatus == 0 ? "0" : "1"));
//            } else {
//                StyledDialog.init(mContext);
//                StyledDialog.buildIosAlert("", "确认取消关注吗?", new MyDialogListener() {
//                    @Override
//                    public void onFirst() {
//
//                    }
//
//                    @Override
//                    public void onThird() {
//                        super.onThird();
//                    }
//
//                    @Override
//                    public void onSecond() {
//                        indexMainPresenter.fan(new SimpleHashMapBuilder<String, Object>()
//                                .puts("friendId", postDetail.memberId)
//                                .puts("", postDetail.createSource)
//                                .puts("type", postDetail.focusStatus == 0 ? "0" : "1"));
//                    }
//                }).setCancelable(true, true).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
//            }
//        }
//        if (function.equals("banner")){
//            dataStatisticsPresenter.bannerClickNum(new SimpleHashMapBuilder<String, Object>().puts("advertisingId",(String) obj));
//        }
//    }
//
//    public void showReviewWarnDialog(final String warnid) {
//        final List<String> strings = new ArrayList<>();
//        final List<Integer> stringsColors = new ArrayList<>();
//        stringsColors.add(Color.parseColor("#444444"));
//        strings.add("举报");
//        StyledDialog.init(getContext());
//        reviewandwarndialog = StyledDialog.buildBottomItemDialog(strings, stringsColors, "取消", new MyItemDialogListener() {
//            @Override
//            public void onItemClick(CharSequence text, int position) {
//                if ("举报".equals(text.toString())) {
//                    showWarnDialog(warnid);
//                }
//            }
//
//            @Override
//            public void onBottomBtnClick() {
//
//            }
//        }).setCancelable(true, true).show();
//        reviewandwarndialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                reviewandwarndialog = null;
//            }
//        });
//    }
//
//    public void showWarnDialog(final String warnid) {
//        final List<Integer> stringsColors = new ArrayList<>();
//        final List<String> strings = new ArrayList<>();
//        strings.add("垃圾广告");
//        stringsColors.add(Color.parseColor("#29BDA9"));
//        strings.add("淫秽色情");
//        stringsColors.add(Color.parseColor("#29BDA9"));
//        strings.add("诈骗信息");
//        stringsColors.add(Color.parseColor("#29BDA9"));
//        strings.add("不实违法");
//        stringsColors.add(Color.parseColor("#29BDA9"));
//        strings.add("违规侵权");
//        stringsColors.add(Color.parseColor("#29BDA9"));
//        strings.add("其他");
//        stringsColors.add(Color.parseColor("#29BDA9"));
//        StyledDialog.init(getContext());
//        warndialog = StyledDialog.buildBottomItemDialog(strings, stringsColors, "取消", new MyItemDialogListener() {
//            @Override
//            public void onItemClick(CharSequence text, int position) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("type", "1");
//                map.put("sourceId", warnid);
//                map.put("reason", text.toString());
//                indexMainPresenter.warn(map);
//            }
//
//            @Override
//            public void onBottomBtnClick() {
//
//            }
//        }).setTitle("举报内容问题").setTitleColor(R.color.color_444444).setTitleSize(15).setCancelable(true, true).show();
//        warndialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                warndialog = null;
//            }
//        });
//    }
//
//    @Override
//    public void postlikeclick(View view, String postingId, String type) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("postingId", postingId);
//        map.put("type", type);
//        indexMainPresenter.like(map);
//    }
//
//    String surl;
//    String sdes;
//    String stitle;
//
//    @Override
//    public void postshareclick(View view, String url, String des, String title, String postId) {
//        this.surl = url;
//        this.sdes = des;
//        this.stitle = title;
//        showShare();
//        dataStatisticsPresenter.shareAndLook(new SimpleHashMapBuilder<String, Object>().puts("sourceId", postId).puts("sourceType", 2).puts("type", 2));
//    }
//
//    @Override
//    public String getSurl() {
//        return surl;
//    }
//
//    @Override
//    public String getSdes() {
//        return sdes;
//    }
//
//    @Override
//    public String getStitle() {
//        return stitle;
//    }
//
//    @Override
//    public Bitmap getsBitmap() {
//        return null;
//    }
//
//    @Override
//    public void onSucessGetLocVip(LocVip locVip) {
//
//    }
//}
