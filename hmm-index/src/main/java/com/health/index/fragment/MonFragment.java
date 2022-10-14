//package com.health.index.fragment;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.GridLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.graphics.drawable.RoundedBitmapDrawable;
//import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
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
//import com.google.gson.Gson;
//import com.health.index.BuildConfig;
//import com.health.index.R;
//import com.health.index.adapter.IndexChatAdapter2;
//import com.health.index.adapter.IndexFunctionGridAdapter;
//import com.health.index.adapter.IndexOnlineVideoListAdapter;
//import com.health.index.adapter.IndexQuestionAdapter;
//import com.health.index.adapter.IndexSeeAdapter;
//import com.health.index.adapter.IndexStatusAdapter;
//import com.health.index.adapter.IndexTitleAdapter;
//import com.health.index.adapter.IndexTitleAdapterWord;
//import com.health.index.adapter.IndexTitleVideoAdapter;
//import com.health.index.adapter.IndexWordAdapter;
//import com.health.index.contract.IndexMonContract;
//import com.health.index.interfaces.IndexListener;
//import com.health.index.model.IndexAllChat2;
//import com.healthy.library.model.IndexAllQuestion;
//import com.health.index.model.IndexAllSee;
//import com.health.index.model.IndexBean;
//import com.health.index.model.IndexDailyMessageResult;
//import com.health.index.model.IndexDisCountShop;
//import com.health.index.model.IndexVideo;
//import com.health.index.model.IndexVideoOnline;
//import com.health.index.model.IndexWarmWord;
//import com.health.index.model.UserInfoExModel;
//import com.healthy.library.model.UserInfoMonModel;
//import com.health.index.presenter.IndexMonPresenter;
//import com.health.index.widget.ChosePopupWindow;
//import com.healthy.library.base.BaseAdapter;
//import com.healthy.library.base.BaseFragment;
//import com.healthy.library.base.BaseMultiItemAdapter;
//import com.healthy.library.builder.SimpleHashMapBuilder;
//import com.healthy.library.business.ADDialogView;
//import com.healthy.library.business.MessageDialog;
//import com.healthy.library.businessutil.ChannelUtil;
//import com.healthy.library.businessutil.GuideViewHelp;
//import com.healthy.library.businessutil.LocUtil;
//import com.healthy.library.constant.Ids;
//import com.healthy.library.constant.SpKey;
//import com.healthy.library.contract.AdContract;
//import com.healthy.library.contract.HanMomContract;
//import com.healthy.library.contract.LimitContract;
//import com.healthy.library.dialog.PostCouponDialog;
//import com.healthy.library.dialog.UpdateUserInfoDialog;
//import com.healthy.library.interfaces.IsNeedShare;
//import com.healthy.library.interfaces.OnCustomRetryListener;
//import com.healthy.library.message.CanUpdateTab;
//import com.healthy.library.message.UpdateGuideInfo;
//import com.healthy.library.message.UpdateMessageInfo;
//import com.healthy.library.message.UpdateUserInfoMsg;
//import com.healthy.library.message.UpdateUserLocationMsg;
//import com.healthy.library.model.AdModel;
//import com.healthy.library.model.HanMomInfoModel;
//import com.healthy.library.model.IndexMenu;
//import com.healthy.library.model.PageInfoEarly;
//import com.healthy.library.model.ServiceTabChangeModel;
//import com.healthy.library.model.TabChangeModel;
//import com.healthy.library.presenter.AdPresenter;
//import com.healthy.library.presenter.HanMomPresenter;
//import com.healthy.library.presenter.LimitPresenter;
//import com.healthy.library.routes.AppRoutes;
//import com.healthy.library.routes.DiscountRoutes;
//import com.healthy.library.routes.IndexRoutes;
//import com.healthy.library.routes.LibraryRoutes;
//import com.healthy.library.routes.MallRoutes;
//import com.healthy.library.routes.MineRoutes;
//import com.healthy.library.routes.ServiceRoutes;
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
//import com.hss01248.dialog.interfaces.MyItemDialogListener;
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
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
////import com.health.discount.widget.KKHelpDialog;
//
//@Route(path = IndexRoutes.INDEX_MODULE2)
//
//public class MonFragment extends BaseFragment implements LimitContract.View, AdContract.View, IsNeedShare,
//        IndexTitleVideoAdapter.SubListener, IndexChatAdapter2.OnChatShareClickListener, IndexChatAdapter2.OnChatLikeClickListener, IndexListener,
//        IndexMonContract.View, OnRefreshLoadMoreListener, AMapLocationListener, BaseAdapter.OnOutClickListener, HanMomContract.View, BaseAdapter.OnOutClickListener {
//
//
//    private String[] mPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
//
//    private IndexMonPresenter indexMonPresenter;
//    private com.healthy.library.widget.StatusLayout layoutStatus;
//    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
//    private com.healthy.library.widget.ImageTextView tvLoc;
//    private LinearLayout tvAreall;
//    private RecyclerView recyclerIndex;
//    private android.widget.RelativeLayout topTitle;
//
//
//    private VirtualLayoutManager virtualLayoutManager;
//    private DelegateAdapter delegateAdapter;
//    private IndexChatAdapter2 indexChatAdapter;
//    private IndexFunctionGridAdapter indexFunctionAdapter;
//    //    private IndexGoodsAdapter indexGoodsAdapter;
////    private IndexGoodsTitleAdapter indexGoodsTitleAdapter;
//    private IndexQuestionAdapter indexQuestionAdapter;
//    private IndexSeeAdapter indexSeeAdapter;
//    private IndexStatusAdapter indexStatusAdapter;
//    private IndexTitleAdapterWord indexTitleAdapterWord;
//    private IndexTitleAdapter indexTitleAdapterChat;
//    private IndexTitleAdapter indexTitleAdapterSee;
//    private IndexTitleAdapter indexTitleAdapterQuestion;
//    private IndexOnlineVideoListAdapter indexOnlineVideoListAdapter;
//
//
//    private IndexTitleVideoAdapter indexTitleVideoAdapter;
//
//
//    private IndexWordAdapter indexWordAdapter;
////    private IndexTitleRefreshAdapter indexTitleRefreshAdapterChat;
////    private IndexTitleRefreshAdapter indexTitleRefreshAdapterSee;
////    private IndexTitleRefreshAdapter indexTitleRefreshAdapterQuestion;
//
//    private ChosePopupWindow chosePopupWindow;
//    private int chatPage = 1;
//    private int questionPage = 1;
//    private int seePage = 1;
//    private int totalDy = 0;
//    private int reLink1 = 0;
//    private int reLink2 = 0;
//    private int reLink3 = 0;
//
//    private String selectdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//    private int RC_LOCATION = 1020;
//    private int RC_PROVINCE_CITY = 4697;
//
//    public AMapLocationClient mLocationClient;
//    public AMapLocationClientOption mLocationOption = null;
//    private int reLocTime = 0;
//    private IndexBean mindexBean;
//    private int isinit = 0;
//    private List<IndexMenu> indexMenus = new ArrayList<>();
//    private List<IndexAllChat2> mindexAllChats = new ArrayList<>();
//    private List<IndexVideo> mindexVideos = new ArrayList<>();
//    private List<IndexAllQuestion> mindexAllQuestions = new ArrayList<>();
//    private List<IndexAllSee> mindexAllSees = new ArrayList<>();
//    private List<UserInfoExModel> userInfoExModels = new ArrayList<>();
//    private com.healthy.library.widget.CornerImageView selectIcon;
//    private android.widget.TextView selectClass;
//    private android.widget.TextView selectDate;
//    private android.widget.ImageView selectDown;
//    private UserInfoMonModel muserInfoMonModel;
//    private int dialogwidth = 220;
//    private LinearLayout tvArealll;
//    private ImageView ivBottomShader;
//    private ImageView passMessage;
//    private List<IndexWarmWord> indexWarmWords = new ArrayList<>();
//    private boolean isBlack = false;
//    private String latitude;
//    private String longitude;
//    private String areaNo;
//    private String cityNo;
//    private String locCityName;
//    private String newCityName;
//
//    private int posFaq = 1;
//    AdPresenter adPresenter;
//    LimitPresenter limitPresenter;
//    boolean choseLocCan = true;
//    HanMomPresenter hanMomPresenter;
//    private UpdateUserInfoDialog mUserInfoDialog;
//    private String mDate;
//    //    private TextView mTvNicknameTmp=null;
//    //    private TextView mTvNicknameTmp=null;
//    private Dialog reviewandwarndialog;
//    private Dialog warndialog;
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.index_fragment_mon;
//    }
//
//    @Override
//    protected void findViews() {
//        initView();
//
//        adPresenter = new AdPresenter(mContext, this);
//        indexMonPresenter = new IndexMonPresenter(mContext, this);
//        layoutRefresh.setOnRefreshLoadMoreListener(this);
////        layoutRefresh.setEnableLoadMore(false);
//        layoutRefresh.finishLoadMoreWithNoMoreData();
//        layoutStatus.setOnNetRetryListener(this);
//        tvLoc.setOnClickListener(this);
//        if (("check".equals(ChannelUtil.getChannel(null)) || "debug".equals(ChannelUtil.getChannel(null)))) {
//            tvLoc.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
////                    LocTestDialog.newInstance().show(getChildFragmentManager(),"模拟定位");
//                    ARouter.getInstance().build(MallRoutes.MALL_CHOOSE_MAP_ADDRESS).navigation();
//                    return true;
//                }
//            });
//        }
//        buildRecyclerView();
//        hanMomPresenter = new HanMomPresenter(mContext, this);
//        limitPresenter = new LimitPresenter(mContext, this);
//        limitPresenter.getMineLimit(new SimpleHashMapBuilder<String, Object>());
////        mTvNicknameTmp.setText("");
//    }
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    protected void onCreate() {
//        super.onCreate();
//        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
//            locate();
//        } else {
//            requestPermissions(mPermissions, RC_LOCATION);
//        }
//
//    }
//
//
//    @Override
//    public void onRetryClick() {
//        locate();
//    }
//
//    private void buildRecyclerView() {
//        isinit = 1;
//        virtualLayoutManager = new VirtualLayoutManager(mContext);
//        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
//        recyclerIndex.setLayoutManager(virtualLayoutManager);
//        recyclerIndex.setAdapter(delegateAdapter);
//
//        indexStatusAdapter = new IndexStatusAdapter();
//        delegateAdapter.addAdapter(indexStatusAdapter);
//        indexStatusAdapter.setOnIndexListener(this);
//
//        indexFunctionAdapter = new IndexFunctionGridAdapter();
//        delegateAdapter.addAdapter(indexFunctionAdapter);
//        indexFunctionAdapter.setTmprecyclerView(recyclerIndex);
//        indexFunctionAdapter.setOutClickListener(this);
//
//
//        indexTitleVideoAdapter = new IndexTitleVideoAdapter();
//        indexTitleVideoAdapter.setSubListener(this);
//        delegateAdapter.addAdapter(indexTitleVideoAdapter);
//
//
//        indexTitleAdapterWord = new IndexTitleAdapterWord();
//        delegateAdapter.addAdapter(indexTitleAdapterWord);
//        indexTitleAdapterWord.setOutClickListener(this);
//
//        indexWordAdapter = new IndexWordAdapter();
//        delegateAdapter.addAdapter(indexWordAdapter);
//
//
//        indexOnlineVideoListAdapter = new IndexOnlineVideoListAdapter();
//        delegateAdapter.addAdapter(indexOnlineVideoListAdapter);
//
//
//        indexTitleAdapterChat = new IndexTitleAdapter();
//        delegateAdapter.addAdapter(indexTitleAdapterChat);
//        indexTitleAdapterChat.setOutClickListener(this);
//
//        indexChatAdapter = new IndexChatAdapter2();
//        delegateAdapter.addAdapter(indexChatAdapter);
//        indexChatAdapter.setOutClickListener(this);
//        indexChatAdapter.setOnChatLikeClickListener(this);
//        indexChatAdapter.setOnChatShareClickListener(this);
////        indexTitleRefreshAdapterChat = new IndexTitleRefreshAdapter();
////        indexTitleRefreshAdapterChat.setData("都在聊换一批");
////        indexTitleRefreshAdapterChat.setOutClickListener(this);
////        delegateAdapter.addAdapter(indexTitleRefreshAdapterChat);
//
//        indexTitleAdapterSee = new IndexTitleAdapter();
//        delegateAdapter.addAdapter(indexTitleAdapterSee);
//        indexTitleAdapterSee.setOutClickListener(this);
//
//        indexSeeAdapter = new IndexSeeAdapter();
//        delegateAdapter.addAdapter(indexSeeAdapter);
//
//
//        indexTitleAdapterQuestion = new IndexTitleAdapter();
//        delegateAdapter.addAdapter(indexTitleAdapterQuestion);
//        indexTitleAdapterQuestion.setOutClickListener(this);
//
//        indexQuestionAdapter = new IndexQuestionAdapter();
//        delegateAdapter.addAdapter(indexQuestionAdapter);
//
//
////        indexTitleRefreshAdapterSee = new IndexTitleRefreshAdapter();
////        delegateAdapter.addAdapter(indexTitleRefreshAdapterSee);
////        indexTitleRefreshAdapterSee.setOutClickListener(this);
////        indexTitleRefreshAdapterSee.setData("都在看换一批");
//
//
////        indexTitleRefreshAdapterQuestion = new IndexTitleRefreshAdapter();
////        delegateAdapter.addAdapter(indexTitleRefreshAdapterQuestion);
////        indexTitleRefreshAdapterQuestion.setOutClickListener(this);
////        indexTitleRefreshAdapterQuestion.setData("都在问换一批");
//
//
////        indexGoodsTitleAdapter = new IndexGoodsTitleAdapter();
////        delegateAdapter.addAdapter(indexGoodsTitleAdapter);
////
////        indexGoodsAdapter = new IndexGoodsAdapter();
////        delegateAdapter.addAdapter(indexGoodsAdapter);
////        indexGoodsAdapter.setOutClickListener(this);
//
//
//        recyclerIndex.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (recyclerView.canScrollVertically(-1)) {
//
//                } else {
//                    totalDy = 0;
//                }
//
//                totalDy += dy;
////                //System.out.println("滑动的:"+totalDy);
////                //System.out.println("标题:"+topTitle.getHeight());
//                if (topTitle.getHeight() > 0) {
//                    float alpha = Math.min(totalDy * 1.0f / (topTitle.getHeight() * 2), 1);
////                    //System.out.println("浮点:"+(1-alpha));
////                    topTitle.setBackgroundColor(Color.argb((int) (255 * (1-alpha)),
////                            255, 255, 255));
////                    //System.out.println("设置的alpha"+alphaz);
//                    topTitle.setAlpha((1 - alpha));
////                    if (alpha > 0.5f) {
////
////                    } else {
////
////                    }
//                }
//            }
//        });
//        tvAreall.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//        if (v.getId() == R.id.tv_loc) {
//            if (!choseLocCan) {
//                return;
//            }
//            ARouter.getInstance().build(LibraryRoutes.LIBRARY_LOC)
//                    .navigation(mActivity, RC_PROVINCE_CITY);
//        }
//        if (v.getId() == R.id.tv_areall) {
//            int[] location = new int[2];
//            v.getLocationOnScreen(location);
//            if (chosePopupWindow != null) {
//                chosePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//
//                        //System.out.println("宽度计算2:" + chosePopupWindow.getWidth());
//                        selectDown.setImageResource(isBlack ? R.drawable.ic_triangle_down_green : R.drawable.ic_triangle_down_white);
//                        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                        lp.alpha = 1f;
//                        getActivity().getWindow().setAttributes(lp);
//                    }
//                });
//                if (userInfoExModels.size() > 0 && !"资料越全面，服务更贴心".equals(selectClass.getText().toString())) {
//                    //System.out.println("宽度计算:" + chosePopupWindow.getWidth() / 2 + ":" + tvAreall.getWidth() / 2);
//                    chosePopupWindow.showAsDropDown(v, -(int) (chosePopupWindow.getWidth() / 2 - tvAreall.getWidth() / 2), 0);
//                    selectDown.setImageResource(isBlack ? R.drawable.ic_triangle_up_green : R.drawable.ic_triangle_up_white);
//                    WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                    lp.alpha = 0.4f;
//                    getActivity().getWindow().setAttributes(lp);
//                } else {
//                    MobclickAgent.onEvent(mContext, "event2ClassEnoughtDetail", new SimpleHashMapBuilder().puts("soure", "首页"));
//                    ARouter.getInstance()
//                            .build(MineRoutes.MINE_PERSONAL_INFO_LIST)
//                            .navigation();
//                }
////                if(muserInfoMonModel!=null&&muserInfoMonModel.status!=-1){
////
////                }
//
//
//            }
//
//            ;
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == RC_LOCATION) {
//            if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
//                locate();
//            } else {
//                if (PermissionUtils.somePermissionPermanentlyDenied(mActivity, mPermissions)) {
//                    PermissionUtils.showRationale(mActivity);
//                } else {
//                    requestPermissions(mPermissions, RC_LOCATION);
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void onFirstVisible() {
//        super.onFirstVisible();
//
//    }
//
//    @Override
//    public void onLoadMore(RefreshLayout refreshLayout) {
//
//    }
//
//    @Override
//    public void onRequestFinish() {
//        super.onRequestFinish();
//        layoutRefresh.finishRefresh();
//        layoutRefresh.finishLoadMore();
//    }
//
//    @Override
//    public void onRefresh(RefreshLayout refreshLayout) {
//        successLocation();
//    }
//
//    private void successLocation() {
//        latitude = LocUtil.getLatitude(mContext, SpKey.LOC_CHOSE);
//        longitude = LocUtil.getLongitude(mContext, SpKey.LOC_CHOSE);
//        areaNo = LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE);
//        cityNo = LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE);
//        locCityName = LocUtil.getCityNameOld(mContext, SpKey.LOC_ORG);
//        newCityName = LocUtil.getCityNameOld(mContext, SpKey.LOC_CHOSE);
//        tvLoc.setText(LocUtil.getCityName(mContext, SpKey.LOC_CHOSE));
//
//        indexMonPresenter.getH5();
//        indexMonPresenter.getAllStatus();
//        indexStatusAdapter.setAreaNo(areaNo);
//        indexMonPresenter.getIndexMain(selectdate, cityNo, areaNo, longitude + "", latitude + "", locCityName.equals(newCityName) ? "1" : "0");
//        indexQuestionAdapter.setCityNo((Integer.parseInt(areaNo) / 100 * 100) + "");
//        hanMomPresenter.getInfo(new SimpleHashMapBuilder<String, Object>().puts("lat", LocUtil.getLatitude(mContext, SpKey.LOC_ORG)).puts("lng", LocUtil.getLongitude(mContext, SpKey.LOC_ORG)));
//
//
//    }
//
//    private boolean checkIsInMessage(int id) {
//        boolean result = false;
//        if (id != -1) {
//            for (int i = 0; i < indexWarmWords.size(); i++) {
//
//                if (indexWarmWords.get(i) != null) {
//                    if (indexWarmWords.get(i).id == id) {
//                        return true;
//                    }
//                }
//
//            }
//        } else {
//            return false;
//        }
//        return result;
//    }
//
//    @Override
//    public void getIndexSuccess(IndexBean indexBean) {
//
//        layoutRefresh.finishLoadMoreWithNoMoreData();
//        //System.out.println("获得了");
//        try {
//            chosePopupWindow.dismiss();
//            //System.out.println("尝试消失");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mindexBean = indexBean;
//        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "2").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
//        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "1").puts("triggerPage", "1").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
////        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "1").puts("triggerPage", "16").puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
//        indexMenus.clear();
//
//        indexMenus.add(new IndexMenu("同城圈", R.drawable.index_tchm));
//
//        if ("huawei".equals(ChannelUtil.getChannel(mContext)) && SpUtils.getValue(mContext, SpKey.AUDITSTATUS, false)) {//如果在审核
//
//        } else {
//            indexMenus.add(new IndexMenu("专家答疑", R.drawable.index_zjdy));
//        }
//        indexMenus.add(new IndexMenu("母婴商城", R.drawable.index_mysc));
//        indexMenus.add(new IndexMenu("憨妈直播", R.drawable.index_zb));
//
//        if (SpUtils.getValue(mContext, SpKey.AUDITSTATUS, false)) {//如果在审核
//
//        } else {
//            indexMenus.add(new IndexMenu("保险服务", R.drawable.index_lcbx));
//        }
//        indexMenus.add(new IndexMenu("技师招募", R.drawable.index_jszm));
//        indexMenus.add(new IndexMenu("憨妈赚", R.drawable.index_hmz));
//        indexMenus.add(new IndexMenu("孕育工具", R.drawable.index_hmbds));
//        indexMenus.add(new IndexMenu("憨妈课堂", R.drawable.index_hmkt));
//        indexMenus.add(new IndexMenu("育儿资讯", R.drawable.index_yezx));
//        indexMenus.add(new IndexMenu("宝宝爱听", R.drawable.index_bbat));
//        indexMenus.add(new IndexMenu("妈妈听听", R.drawable.index_mmat));
//
//
//        if (BuildConfig.DEBUG) {
//
//            indexMenus.add(new IndexMenu("测试跳转", R.drawable.index_jszm));
//        }
//
//        indexTitleAdapterWord.setModel("憨言厚语");
//        indexTitleAdapterChat.setModel("大家都在聊");
//        indexTitleAdapterSee.setModel("大家都在看");
//
//        indexTitleAdapterQuestion.setModel("大家都在问");
//        if ("huawei".equals(ChannelUtil.getChannel(mContext)) && SpUtils.getValue(mContext, SpKey.AUDITSTATUS, false)) {
//            indexTitleAdapterQuestion.setModel(null);
//        }
////            indexGoodsTitleAdapter.setData("憨妈优选");
//        indexWarmWords.clear();
//        if (mindexBean != null) {
//            if (mindexBean.dailyMessageResultList != null && mindexBean.dailyMessageResultList.size() > 0) {
//                for (int i = 0; i < mindexBean.dailyMessageResultList.size(); i++) {
//                    IndexDailyMessageResult indexDailyMessageResult = mindexBean.dailyMessageResultList.get(i);
//                    IndexWarmWord indexWarmWord = new IndexWarmWord(indexDailyMessageResult.content);
//                    indexWarmWord.type = 1;
//                    indexWarmWord.id = indexDailyMessageResult.id;
//                    if (!checkIsInMessage(indexDailyMessageResult.id)) {
//                        indexWarmWords.add(indexWarmWord);
//                    }
//                }
//            }
//            if (mindexBean.warmWordsList != null && mindexBean.warmWordsList.size() > 0) {
//
//                indexWarmWords.addAll(mindexBean.warmWordsList);
//            }
//        }
//
//        if (indexWarmWords.size() > 0) {
//            indexTitleAdapterWord.setVisible(true);
//            indexWordAdapter.setVisible(true);
//        } else {
//            indexTitleAdapterWord.setModel(null);
//            indexTitleAdapterWord.setVisible(false);
//            indexWordAdapter.setVisible(false);
//        }
//        List tmpsmens = new ArrayList();
//        tmpsmens.clear();
//        tmpsmens.add(indexMenus);
//        indexFunctionAdapter.setData((ArrayList) tmpsmens);
//        indexWordAdapter.setData((ArrayList) indexWarmWords);
////            if (indexBean.disCountShops != null && indexBean.disCountShops.size() > 0) {
////                indexGoodsTitleAdapter.setModel("憨妈优选");
////                indexGoodsAdapter.setData((ArrayList) indexBean.disCountShops);
////            } else {
////                indexGoodsTitleAdapter.setModel(null);
////                indexGoodsAdapter.setData(new ArrayList<IndexDisCountShop>());
////            }
//
//        indexMonPresenter.getAllChat(chatPage + "");
//        indexMonPresenter.getAllSee(seePage + "", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//        if ("huawei".equals(ChannelUtil.getChannel(mContext)) && SpUtils.getValue(mContext, SpKey.AUDITSTATUS, false)) {
////            indexTitleAdapterQuestion.setModel(null);
//        } else {
//
//            indexMonPresenter.getAllQuestion(questionPage + "");
//        }
//
////            new Handler().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    GuideViewHelp.showGuideRoundRelativeSpecial(getActivity(), "专家答疑TT", true, ((RecyclerView)virtualLayoutManager.findViewByPosition(1)).getLayoutManager().findViewByPosition(1), R.layout.view_guide_type2, Gravity.BOTTOM,10, new OnGuideChangedListener() {
////                        @Override
////                        public void onShowed(Controller controller) {
////
////                        }
////
////                        @Override
////                        public void onRemoved(Controller controller) {
////
//////                            SpUtils.store(mContext,"isShowZZ",true);
//////                            EventBus.getDefault().post(new CanUpdateTab(2));
//////                            EventBus.getDefault().post(new UpdateGuideInfo(2));
////                        }
////                    });
////                }
////            },1000);
//
//
//        indexMonPresenter.getMine();
//
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateTab(UpdateMessageInfo msg) {
//
//        CheckUtils.checkMessageCount(mContext, passMessage);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        CheckUtils.checkMessageCount(mContext, passMessage);
//        if (!TextUtils.isEmpty(LocUtil.getLatitude(mContext, SpKey.LOC_ORG))) {
//
//            hanMomPresenter.getInfo(new SimpleHashMapBuilder<String, Object>().puts("lat", LocUtil.getLatitude(mContext, SpKey.LOC_ORG)).puts("lng", LocUtil.getLongitude(mContext, SpKey.LOC_ORG)));
//
//        }
//    }
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
////        if(userInfoExModels.size()<=1){
////            selectDown.setVisibility(View.INVISIBLE);
////        }else {
////            selectDown.setVisibility(View.VISIBLE);
////        }
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
//                    indexMonPresenter.changeStatus(bean.id + "");
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
//    }
//
//    @Override
//    public void getIndexFail() {
//        //System.out.println("查询失败了时间不对了");
//        selectdate = new SimpleDateFormat("yyyy-MM-dd").format(indexStatusAdapter.getOldDate());
//        indexStatusAdapter.revertDate();
//        SpUtils.store(mContext, "isShowZZ", true);
//        EventBus.getDefault().post(new CanUpdateTab(2));
//    }
//
//    @Override
//    public void onSuccessLike() {
//
//    }
//
//    @Override
//    public void changeStatusSuccess() {
//        selectdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        successLocation();
//    }
//
//    @Override
//    public void getAllVideoOnlineSuccess(List<IndexVideoOnline> indexVideos, PageInfoEarly pageInfoEarly) {
//        if (indexVideos != null && indexVideos.size() > 0) {
//            List tmpsmens = new ArrayList();
//            tmpsmens.clear();
//            tmpsmens.add(indexVideos);
//            indexOnlineVideoListAdapter.setData((ArrayList) tmpsmens);
//        }
//
//    }
//
//    @Override
//    public void getAllVideoSuccess(List<IndexVideo> list, PageInfoEarly pageInfoEarly) {
//        List<IndexVideo> listtmp = new ArrayList<>();
//        if (list != null) {
//            if (list.size() > 3) {
//                listtmp.add(list.get(0));
//                listtmp.add(list.get(1));
//                listtmp.add(list.get(2));
//            } else {
//                for (int i = 0; i < list.size(); i++) {
//                    listtmp.add(list.get(i));
//                }
//            }
//        }
//
//
//        mindexVideos = listtmp;
//        indexTitleVideoAdapter.setData((ArrayList) listtmp);
//    }
//
//    @Override
//    public void getAllChatSuccess(List<IndexAllChat2> indexAllChats, PageInfoEarly pageInfoEarly) {
//        mindexAllChats = indexAllChats;
//        chatPage = pageInfoEarly.currentPage;
//        indexChatAdapter.setData((ArrayList) indexAllChats);
////        if (pageInfoEarly == null || pageInfoEarly.totalNum < 4) {
////            indexTitleRefreshAdapterChat.setIsshow(false);
////        } else {
////            indexTitleRefreshAdapterChat.setIsshow(true);
////        }
//        if (pageInfoEarly.isMore == 0) {
//            chatPage = 0;
//            if (indexAllChats == null || indexAllChats.size() == 0) {
//                if (reLink1 != 0) {
//                    indexMonPresenter.getAllChat(chatPage + "");
//                }
//
//            }
//        }
//        if (reLink1 != 0) {
//            int top = virtualLayoutManager.findFirstVisibleItemPosition();
//            if (indexAllChats.size() < 4) {
//                virtualLayoutManager.scrollToPositionWithOffset(top - (4 - indexAllChats.size()) - 1, 0);
//
//            }
//            reLink1 = 0;
//        }
//    }
//
//    @Override
//    public void getAllSeeSuccess(List<IndexAllSee> indexAllSees, PageInfoEarly pageInfoEarly) {
//        mindexAllSees = indexAllSees;
//        seePage = pageInfoEarly.currentPage;
////        if (pageInfoEarly == null || pageInfoEarly.totalNum < 5) {
////            indexTitleRefreshAdapterSee.setIsshow(false);
////        } else {
////            indexTitleRefreshAdapterSee.setIsshow(true);
////        }
//        indexSeeAdapter.setData((ArrayList) indexAllSees);
//        if (pageInfoEarly.isMore == 0) {
//            seePage = 0;
//            if (indexAllSees == null || indexAllSees.size() == 0) {
//                if (reLink2 != 0) {
//                    indexMonPresenter.getAllSee(seePage + "", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//                }
//            }
//        }
//        if (reLink2 != 0) {
//            int top = virtualLayoutManager.findFirstVisibleItemPosition();
//            if (indexAllSees.size() < 5) {
//                virtualLayoutManager.scrollToPositionWithOffset(top - (5 - indexAllSees.size()), 0);
//
//            }
//            reLink2 = 0;
//        }
//    }
//
//    @Override
//    public void getAllQuestionSuccess(List<IndexAllQuestion> indexAllQuestions, PageInfoEarly pageInfoEarly) {
//        mindexAllQuestions = indexAllQuestions;
//        questionPage = pageInfoEarly.currentPage;
//
////        if(pageInfoEarly==null||pageInfoEarly.totalNum<5){
////            indexTitleRefreshAdapterQuestion.setIsshow(false);
////        }else {
////            indexTitleRefreshAdapterQuestion.setIsshow(true);
////        }
//        indexQuestionAdapter.setData((ArrayList) indexAllQuestions);
//        if (pageInfoEarly.isMore == 0) {
//            questionPage = 0;
//            if (indexAllQuestions == null || indexAllQuestions.size() == 0) {
//                if (reLink3 != 0) {
//                    indexMonPresenter.getAllQuestion(questionPage + "");
//                }
//            }
//        }
//        if (reLink3 != 0) {
//            int top = virtualLayoutManager.findFirstVisibleItemPosition();
//            if (indexAllQuestions.size() < 3) {
//                virtualLayoutManager.scrollToPositionWithOffset(top - (3 - indexAllQuestions.size()), 0);
//
//            }
//            reLink3 = 0;
//        }
//    }
//
//    @Override
//    public void getMineSuccess(UserInfoMonModel userInfoMonModel) {
//        if (userInfoMonModel == null) {
//            indexStatusAdapter.setModel(mindexBean == null ? new IndexBean(-1) : mindexBean);
//        } else {
//            indexStatusAdapter.setModel(mindexBean == null ? new IndexBean(userInfoMonModel.status) : mindexBean);
//        }
//        if (userInfoMonModel == null) {
//            SpUtils.store(mContext, "isShowZZ", true);
//            EventBus.getDefault().post(new CanUpdateTab(2));
//            return;
//        }
//
//        muserInfoMonModel = userInfoMonModel;
////        if (mindexBean.disCountShops != null && mindexBean.disCountShops.size() > 0) {
////            indexGoodsTitleAdapter.setModel(userInfoMonModel.faceUrl);
////        } else {
////            indexGoodsTitleAdapter.setModel(null);
////        }
//        if (muserInfoMonModel != null) {
//            SpUtils.store(mContext, SpKey.USER_NICK, muserInfoMonModel.nickName);
//            SpUtils.store(mContext, SpKey.USER_ICON, muserInfoMonModel.faceUrl);
//            SpUtils.store(mContext, SpKey.USER_BIRTHDAY, muserInfoMonModel.birthday);
//            //接口请求成功情况下进行弹框
//            if (userInfoMonModel != null) {
//                showDialog();
//            }
//        }
//
//
//        indexStatusAdapter.setUserInfoMonModel(userInfoMonModel);
//        //System.out.println("我的性别是啥:" + userInfoMonModel.getMemberSex());
//        if (userInfoMonModel.faceUrl != null) {
////            indexGoodsTitleAdapter.setModel(userInfoMonModel.faceUrl);
////            indexGoodsTitleAdapter.notifyDataSetChanged();
//            Glide.with(selectIcon.getContext())
//                    .asBitmap()
//                    .load(userInfoMonModel.faceUrl)
//                    .placeholder(userInfoMonModel.memberSex == 2 ? R.drawable.img_avatar_default : R.drawable.img_avatar_default_man)
//                    .error(userInfoMonModel.memberSex == 2 ? R.drawable.img_avatar_default : R.drawable.img_avatar_default_man)
//                    
//                    .into(new BitmapImageViewTarget(selectIcon) {
//                        @Override
//                        protected void setResource(Bitmap resource) {
//                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
//                            circularBitmapDrawable.setCircular(true);
//                            selectIcon.setImageDrawable(circularBitmapDrawable);
//                        }
//                    });
//        }
//        //System.out.println("返回的状态" + userInfoMonModel.dateContent);
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
//
//            Glide.with(selectIcon.getContext())
//                    .asBitmap()
//                    .load(userInfoMonModel.babySex == 0 ? R.drawable.app_status_girl : R.drawable.app_status_boy)
//                    .placeholder(userInfoMonModel.babySex == 0 ? R.drawable.app_status_girl : R.drawable.app_status_boy)
//                    .error(userInfoMonModel.babySex == 0 ? R.drawable.app_status_girl : R.drawable.app_status_boy)
//                    
//                    .into(new BitmapImageViewTarget(selectIcon) {
//                        @Override
//                        protected void setResource(Bitmap resource) {
//                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
//                            circularBitmapDrawable.setCircular(true);
//                            selectIcon.setImageDrawable(circularBitmapDrawable);
//                        }
//                    });
//
//
//            if (userInfoMonModel.dateContent.contains("年") || userInfoMonModel.dateContent.contains("岁")) {
//                try {
//                    selectDate.setText(userInfoMonModel.dateContent.split(" ")[1]);
//                } catch (Exception e) {
//                    selectDate.setText(userInfoMonModel.dateContent.replace("宝宝", ""));
//                    e.printStackTrace();
//                }
//
//
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
//
//            }
//        } else {
//            if (TextUtils.isEmpty(userInfoMonModel.dateContent) && TextUtils.isEmpty(userInfoMonModel.statusName)) {
//                selectClass.setText("资料越全面，服务更贴心");
//            }
//        }
//
//        indexMonPresenter.getAllVideo();
//        if (BuildConfig.VERSION_CODE >= 2152) {
//
//            indexMonPresenter.getAllVideoOnline();
//        }
//
//    }
//
//    /**
//     * 完善个人信息弹框
//     * 取消锁机制 外层加入判断
//     */
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
//            }
//        }
//    }
//
//    @Override
//    public void subVideoSucess() {
//        indexMonPresenter.getAllVideo();
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
//
//    /**
//     * 定位
//     */
//    private void locate() {
////        showLoading();
//        mLocationClient = new AMapLocationClient(mContext);
//        mLocationOption = new AMapLocationClientOption();
//        mLocationOption.setOnceLocation(true);
//        //设置定位监听
//        mLocationClient.setLocationListener(this);
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        mLocationClient.setLocationOption(mLocationOption);
//        mLocationClient.startLocation();
////        if (LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE) != null) {
////            //System.out.println("定位成功了");
////            successLocation();
////        } else {
////
////        }
//
//    }
//
//
//    private void initView() {
//        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
//        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
//        tvLoc = (ImageTextView) findViewById(R.id.tv_loc);
//        tvAreall = (LinearLayout) findViewById(R.id.tv_areall);
//        recyclerIndex = (RecyclerView) findViewById(R.id.recycler_index);
//        topTitle = (RelativeLayout) findViewById(R.id.topTitle);
//        selectIcon = (CornerImageView) findViewById(R.id.selectIcon);
//        selectClass = (TextView) findViewById(R.id.selectClass);
//        selectDate = (TextView) findViewById(R.id.selectDate);
//        selectDown = (ImageView) findViewById(R.id.selectDown);
//        tvArealll = (LinearLayout) findViewById(R.id.tv_arealll);
//        ivBottomShader = (ImageView) findViewById(R.id.iv_bottom_shader);
//        passMessage = (ImageView) findViewById(R.id.passMessage);
//        passMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ARouter.getInstance()
//                        .build(AppRoutes.APP_MESSAGE)
//                        .navigation();
//            }
//        });
//    }
//
//    private boolean checkGPSIsOpen() {
//        boolean isOpen;
//        LocationManager locationManager = (LocationManager) getActivity()
//                .getSystemService(Context.LOCATION_SERVICE);
//        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
//        return isOpen;
//    }
//
//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (!checkGPSIsOpen()) {
//            layoutStatus.updateStatus(StatusLayout.Status.STATUS_CUSTOM);
//            layoutStatus.setmOnCustomNetRetryListener(new OnCustomRetryListener() {
//                @Override
//                public void onRetryClick() {
//                    mLocationClient.startLocation();
//                }
//            });
////            MessageDialog.newInstance()
////                    .setMessageTopRes(R.drawable.dialog_message_icon_loc, "开启定位", "为给您提供更好的本地化服务，请您到系统设置中打开GPS定位")
////                    .setMessageOkClickListener(new MessageDialog.MessageOkClickListener() {
////                        @Override
////                        public void onMessageOkClick(View view) {
////                            IntentUtils.toLocationSetting(mContext);
////                        }
////                    })
////                    .show(getChildFragmentManager(), "打开定位");
////            return;
//        }
//        //System.out.println("定位进来2：" + aMapLocation.getErrorCode());
//        if (aMapLocation == null) {
//            return;
//        }
//        //System.out.println("定位进来3：" + aMapLocation.getErrorCode());
//        if (aMapLocation.getErrorCode() == 0) {
//
//            //System.out.println("定位进来4：" + aMapLocation.getErrorCode());
//
//            if (BuildConfig.VERSION_CODE == 2309) {
////                aMapLocation=new AMapLocationBd("青岛北站", "李沧区", "青岛市", "山东省", "370213", 36.175661, 120.380807).build();
////                aMapLocation=new AMapLocationBd("宁波地", "市辖区", "宁波市", "浙江省", "330201", 29.8642, 121.543299).build();
//            }
//
//            LocUtil.storeLocation(mContext, aMapLocation);
//            //System.out.println("重新定位经纬度");
//            if (LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE) == null) {
//                LocUtil.storeLocationChose(mContext, aMapLocation);
//            }
//            EventBus.getDefault().post(new UpdateUserLocationMsg());
//            //System.out.println("定位成功了2");
//            successLocation();
//        } else {
//            layoutStatus.updateStatus(StatusLayout.Status.STATUS_CUSTOM);
//            layoutStatus.setmOnCustomNetRetryListener(new OnCustomRetryListener() {
//                @Override
//                public void onRetryClick() {
//                    mLocationClient.startLocation();
//                }
//            });
//            //System.out.println("定位失败：" + aMapLocation.getErrorCode());
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
////                new AlertDialog.Builder(mContext)
////                        .setTitle("提示")
////                        .setMessage("是否去设置打开GPS")
////                        .setNegativeButton("取消", null)
////                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////
////                                IntentUtils.toLocationSetting(mContext);
////
////                            }
////                        }).create().show();
//
//            }
//        }
//    }
////            indexTitleAdapterWord.setData("憨言厚语");
////            indexTitleAdapterChat.setData("大家都在聊");
////            indexTitleAdapterSee.setData("大家都在看");
////            indexTitleAdapterQuestion.setData("大家都在问");
////            indexGoodsTitleAdapter.setData("憨妈优选");
//
//    @Override
//    public void outClick(@NotNull String function, @NotNull Object obj) {
//
//        //System.out.println("进来了");
//        if (function.contains("测试")) {
//
//            ARouter.getInstance()
//                    .build(DiscountRoutes.DIS_SECKILLLISTACTIVITY)
//                    .navigation();
////            ARouter.getInstance()
////                    .build(DiscountRoutes.DIS_PLUSAREA)
////                    .withString("function", "9112-1")
////                    .navigation();
////            ARouter.getInstance()
////                    .build(DiscountRoutes.DIS_PLUSAREA)
////                    .withString("function","9112-0")
////                    .navigation();
////            StringFullDialog.newInstance().setUrl(SpanUtils.getBuilder(mContext,"需要您的以下权限便于为您提供更好的服务 允许为您保障账户安全 授权设备信息，便于 我们使⽤设备标识码保障您 的账户登录、使 ⽤处于安全的环境，同时可为您 推送专属权 益或服务。").setForegroundColor(Color.parseColor("#FF666666"))
////                    .create()
////            ).setTitle("欢迎使用憨妈妈").show(getChildFragmentManager(),"欢迎使用憨妈妈");
////            StringFullDialog.newInstance().setUrl(SpanUtils.getBuilder(mContext,"亲爱的憨妈妈的粉丝们，感谢您选择及使⽤憨 妈妈平台服务！我们深知个⼈信息对您的重要 性。为此，我们已采取了相应地安全保护措施 ，竭⼒保护您的个⼈ 信息安全。 同时，为了 提升您的应⽤体验、保障您的权益，在您浏览 及使⽤憨妈妈平台服务的过程中，根据您使⽤ 的具体功能，可能需要对您相关信息进⾏收集 ，例如设备信息、存储、位置等相关权限。 您可以通过阅读完整版").setForegroundColor(Color.parseColor("#FF666666"))
////                    .append("《憨妈妈⽤户协议》").setForegroundColor(Color.parseColor("#FF6468"))
////                    .setClickSpan(new ClickableSpan() {
////                        @Override
////                        public void onClick(@NonNull View widget) {
////                            ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
////                                    .withString("url", UrlKeys.URL_PROTOCOL)
////                                    .withString("title", "")
////                                    .navigation();
////                        }
////                    })
////                    .append("及").setForegroundColor(Color.parseColor("#666666"))
////                    .append("《隐私政策》").setForegroundColor(Color.parseColor("#FF3653")).setClickSpan(new ClickableSpan() {
////                        @Override
////                        public void onClick(@NonNull View widget) {
////                            ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEW)
////                                    .withString("url", UrlKeys.URL_HMMPTYSZC)
////                                    .withString("title", "")
////                                    .navigation();
////                        }
////                    })
////                    .append("详细了解我们提供的服务以及所 需 的权限具体情况。 对于上述内容中免除我 们责任和对您权利进⾏限 制的条款，我们将 以加粗字体及下划线的⽅式提 醒您注意").setForegroundColor(Color.parseColor("#666666"))
////                    .create()
////            ).setTextString("跳过","继续").setTitle("欢迎使用憨妈妈").show(getChildFragmentManager(),"感谢使用憨妈妈");
////            ARouter.getInstance().build(TencentLiveRoutes.LIVE_CREATE).navigation();
////            ARouter.getInstance().build(LibraryRoutes.LIBRARY_TMP).withInt("layoutAdapterItemRes",R.layout.dis_item_card).navigation();
////            ARouter.getInstance()
////                    .build(MallRoutes.MALL_STORE_DETAIL)
////                    .navigation();
////            DialogFragment dialogFragment= (DialogFragment) ARouter.getInstance().build(DiscountRoutes.DIS_CARDCHOSE).navigation();
////            dialogFragment.show(getChildFragmentManager(),"测试");
//            /*ARouter.getInstance()
//                    .build(ServiceRoutes.SERVICE_APPOINTMENTMAIN)
//                    .navigation();*/
//        }
//        if ("大家都在看".equals(function)) {
//
//            MobclickAgent.onEvent(mContext, "event2HomeSeeMoreClick", new SimpleHashMapBuilder().puts("soure", "首页"));
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_MAINPASSNEWS)
//                    .withString("knowOrInfoStatus", "2")
//                    .withString("queryDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
//                    .navigation();
//        }
//        if ("超值拼团".equals(function)) {
////            ARouter.getInstance()
////                    .build(DiscountRoutes.DIS_KICKLIST)
////                    .withString("addressProvince",(Integer.parseInt(areaNo) / 10000 * 10000) + "")
////                    .withString("lng",longitude+"")
////                    .withString("lat",latitude+"")
////                    .navigation();
//        }
//        if ("保险服务".equals(function)) {
//            String url = "https://cps.qixin18.com/m/lj1059667/media.html";
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_WEBVIEW)
//                    .withString("title", "保险服务")
//                    .withBoolean("doctorshop", true)
//                    .withBoolean("isinhome", false)
//                    .withString("url", url)
//                    .navigation();
////            NoFucDialog.newInstance().show(getFragmentManager(),"保险服务");
//
//        }
//        if ("母婴商品".equals(function)) {
//            //System.out.println("跳转母婴商品");
//
////            ARouter.getInstance()
////                    .build(SoundRoutes.SOUND_MAIN)
////                    .navigation();
//
//            EventBus.getDefault().post(new TabChangeModel(3));
//            EventBus.getDefault().post(new ServiceTabChangeModel(1));
////            ARouter.getInstance()
////                    .build(ServiceRoutes.SERVICE_MODULEACT)
////                    .navigation();
////            NoFucDialog.newInstance().show(getFragmentManager(), "保险服务");
//
//        }
//        if ("妈妈听听".equals(function)) {
//
//            ARouter.getInstance()
//                    .build(SoundRoutes.SOUND_MAIN_MON)
//                    .withString("audioType", "2")
//                    .navigation();
//        }
//        if ("宝宝爱听".equals(function)) {
//
//            ARouter.getInstance()
//                    .build(SoundRoutes.SOUND_MAIN)
//                    .withString("audioType", "1")
//                    .navigation();
//        }
//        if ("憨妈课堂".equals(function)) {
//
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_VIDEOONLINELIST)
//                    .navigation();
//        }
//        if ("大家都在聊".equals(function)) {
//
//            MobclickAgent.onEvent(mContext, "event2HomeChatMoreClick", new SimpleHashMapBuilder().puts("soure", "首页"));
//            EventBus.getDefault().post(new TabChangeModel(1));
//        }
//        if ("育儿资讯".equals(function)) {
//            MobclickAgent.onEvent(mContext, "event2HomeSeeMoreClick", new SimpleHashMapBuilder().puts("soure", "首页"));
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_MAINPASSNEWS)
//                    .withString("knowOrInfoStatus", "2")
//                    .withString("queryDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
//                    .navigation();
//        }
//
//
//        if ("憨妈直播".equals(function)) {
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_VIDEOLIST)
//                    .withString("function", "9004")
//                    .navigation();
//        }
//        if ("大家都在问".equals(function)) {
//
//            MobclickAgent.onEvent(mContext, "event2HomeQuestMoreClick", new SimpleHashMapBuilder().puts("soure", "首页"));
//            ARouter.getInstance()
//                    .build(AppRoutes.MODULE_EXPERT_RIGHT)
//                    .withString("cityNo", (Integer.parseInt(areaNo) / 100 * 100) + "")
//                    .navigation();
//        }
//        if ("同城圈".equals(function)) {
//            EventBus.getDefault().post(new TabChangeModel(1));
//        }
//        if ("技师招募".equals(function)) {
//
//            ARouter.getInstance()
//                    .build(MineRoutes.MINE_JOB_TYPE)
//                    .navigation();
//
//
////            KKHelpDialog dialog = KKHelpDialog.newInstance();
////            dialog.show(getFragmentManager(), "improve");
//        }
//        if ("母婴商城".equals(function)) {
//            Map nokmap = new HashMap<String, String>();
//            nokmap.put("soure", "憨妈妈首页导航商城点击量");
//            MobclickAgent.onEvent(mContext, "憨妈妈首页导航商城点击量", nokmap);
//            EventBus.getDefault().post(new TabChangeModel(3));
//            EventBus.getDefault().post(new ServiceTabChangeModel(0));
//        }
//        if ("孕育工具".equals(function)) {
//            if (BuildConfig.VERSION_CODE >= 2205) {
//                ARouter.getInstance()
//                        .build(IndexRoutes.INDEX_TOOLS_TYPE)
//                        .navigation();
//            } else {
//                ARouter.getInstance()
//                        .build(IndexRoutes.INDEX_TOOLS)
//                        .navigation();
//            }
//
//        }
//        if ("专家答疑".equals(function)) {
//            ARouter.getInstance()
//                    .build(AppRoutes.MODULE_EXPERT_FAQ2)
//                    .withString("cityNo", (Integer.parseInt(areaNo) / 100 * 100) + "")
//                    .navigation();
//        }
//        if ("母婴教育".equals(function)) {
////            ARouter.getInstance()
////                    .build(AppRoutes.MODULE_MALLTEACH)
////                    .navigation();
//            EventBus.getDefault().post(new TabChangeModel(3));
//            EventBus.getDefault().post(new ServiceTabChangeModel(2));
//        }
//        if ("憨妈优选".equals(function)) {
//
//            Map nokmap = new HashMap<String, String>();
//            nokmap.put("soure", "APP首页-憨妈优惠");
//            MobclickAgent.onEvent(mContext, "event2GoodsDetailClick", nokmap);
//
//            IndexDisCountShop item = (IndexDisCountShop) obj;
//            ARouter.getInstance()
//                    .build(MallRoutes.MALL_STORE_DETAIL)
//                    .withString("cityNo", (Integer.parseInt(areaNo) / 100 * 100) + "")
//                    .withString("areaNo", areaNo + "")
//                    .withString("categoryNo", item.categoryNoLevel2)
//                    .withString("lng", longitude + "")
//                    .withString("lat", latitude + "")
//                    .withString("shopId", item.id + "")
//                    .navigation();
//        }
//        if ("都在聊换一批".equals(function)) {
//            reLink1 = 1;
//            chatPage++;
//            indexMonPresenter.getAllChat(chatPage + "");
//        }
//        if ("都在看换一批".equals(function)) {
//            reLink2 = 1;
//            seePage++;
//            indexMonPresenter.getAllSee(seePage + "", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//        }
//        if ("都在问换一批".equals(function)) {
//            reLink3 = 1;
//            questionPage++;
//            indexMonPresenter.getAllQuestion(questionPage + "");
//        }
//
//        if ("憨妈赚".equals(function)) {
//            // NoFucDialog.newInstance().show(getChildFragmentManager(),"憨妈赚");
//            if (SpUtils.getValue(mContext, "isHanMomVip", false) == true) {
////                PackageManager packageManager = mContext.getPackageManager();
////                Intent intent = packageManager.getLaunchIntentForPackage("com.tencent.mm");
////                mContext.startActivity(intent);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        String appId = Ids.WX_APP_ID; // 本应用微信AppId
//                        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);
//                        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//                        req.userName = "gh_93d673cec6a8"; // 小程序原始id
//                        req.path = "";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
//                        if (ChannelUtil.isRealRelease()) {
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
//
//        }
//        if (function.equals("coupon")) {
//            PostCouponDialog.newInstance().setId((String) obj).show(getChildFragmentManager(), "");
//        }
//        if (function.equals("submit")) {
//            showReviewWarnDialog((String) obj);
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
//    /**
//     * 回复举报
//     */
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
//                indexMonPresenter.warn(map);
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
//    public void onDateDecrease(Date date) {
//        selectdate = new SimpleDateFormat("yyyy-MM-dd").format(date);
//        indexMonPresenter.getIndexMain(selectdate, cityNo, areaNo, longitude + "", latitude + "", locCityName.equals(newCityName) ? "1" : "0");
//    }
//
//    @Override
//    public void onDateIncrease(Date date) {
//        selectdate = new SimpleDateFormat("yyyy-MM-dd").format(date);
//        indexMonPresenter.getIndexMain(selectdate, cityNo, areaNo, longitude + "", latitude + "", locCityName.equals(newCityName) ? "1" : "0");
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_PROVINCE_CITY && resultCode == Activity.RESULT_OK) {
//            //System.out.println("开始修改");
//            successLocation();
////            if (data != null && data.getExtras() != null) {
//////                Bundle extras = data.getExtras();
//////                if (extras != null && extras.getString("prov") != null) {
//////
//////                    if (extras.getString("city").contains("县") || extras.getString("city").contains("市辖区")) {
//////
//////                        tvLoc.setText(extras.getString("prov") + extras.getString("city"));
//////                    } else {
//////
//////                        tvLoc.setText(extras.getString("city"));
//////                    }
//////                } else {
//////
//////                    tvLoc.setText(extras.getString("city"));
//////                }
////            }
//        }
//
//
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
//        //System.out.println("首页进行了变化");
//        selectdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        successLocation();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateUserLocation(UpdateUserLocationMsg msg) {
//        //System.out.println("首页进行了变化");
//        selectdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        successLocation();
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void guideInfo(UpdateGuideInfo msg) {
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
//            if ("huawei".equals(ChannelUtil.getChannel(mContext)) && SpUtils.getValue(mContext, SpKey.AUDITSTATUS, false)) {
//                SpUtils.store(mContext, "isShowZZ", true);
//                EventBus.getDefault().post(new CanUpdateTab(2));
//                EventBus.getDefault().post(new UpdateGuideInfo(2));
//                return;
//            }
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    //System.out.println("测试GGK2");
////                    GuideViewHelp.showGuideRoundRelativeSpecial(getActivity(), "专家答疑", true, ((RecyclerView)virtualLayoutManager.findViewByPosition(1).findViewById(R.id.recycler_fun)).getLayoutManager().findViewByPosition(1), R.layout.view_guide_type2, Gravity.BOTTOM,10, new OnGuideChangedListener() {
////                        @Override
////                        public void onShowed(Controller controller) {
////
////                        }
////
////                        @Override
////                        public void onRemoved(Controller controller) {
////
////                            SpUtils.store(mContext,"isShowZZ",true);
////                            EventBus.getDefault().post(new CanUpdateTab(2));
////                            EventBus.getDefault().post(new UpdateGuideInfo(2));
////                        }
////                    });
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
//            if ("huawei".equals(ChannelUtil.getChannel(mContext)) && SpUtils.getValue(mContext, SpKey.AUDITSTATUS, false)) {
//                SpUtils.store(mContext, "isShowZZ", true);
//                EventBus.getDefault().post(new CanUpdateTab(2));
//                EventBus.getDefault().post(new UpdateGuideInfo(2));
//                return;
//            }
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    //System.out.println("测试GGK1");
//                    while (virtualLayoutManager.findViewByPosition(1) == null) {
//                        try {
//                            Thread.sleep(200);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    try {
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
//                        SpUtils.store(mContext, "isShowZZ", true);
//                        EventBus.getDefault().post(new CanUpdateTab(2));
//                        EventBus.getDefault().post(new UpdateGuideInfo(2));
//                        e.printStackTrace();
//                    }
//
//
////                    while (virtualLayoutManager.findViewByPosition(1) == null) {
////                        try {
////
////                            //System.out.println("尝试沉睡1");
////                            Thread.sleep(200);
////                        } catch (InterruptedException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                    while (virtualLayoutManager.findViewByPosition(1).getParent() == null) {
////                        try {
////                            //System.out.println("尝试沉睡2");
////                            Thread.sleep(200);
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                        }
////                    }
////                    virtualLayoutManager.findViewByPosition(1).postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
////                            GuideViewHelp.showGuideRoundRelativeSpecial(getActivity(), "专家答疑Guide", true, virtualLayoutManager.findViewByPosition(1), R.layout.view_guide_type2, Gravity.BOTTOM, 10, new OnGuideChangedListener() {
////                                @Override
////                                public void onShowed(Controller controller) {
////
////                                }
////
////                                @Override
////                                public void onRemoved(Controller controller) {
////                                    SpUtils.store(mContext, "isShowZZ", true);
////                                    //System.out.println("尝试删除掉");
////                                    EventBus.getDefault().post(new CanUpdateTab(2));
////                                    EventBus.getDefault().post(new UpdateGuideInfo(2));
////                                }
////                            });
////                        }
////                    }, 800);
//                }
//            }).start();
//
//        }
//    }
//
//    @Override
//    public void chatlikeclick(View view, String postingId, String type) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("postingId", postingId);
//        map.put("type", type);
//        indexMonPresenter.like(map);
//    }
//
//    @Override
//    public void clickSub(IndexVideo indexVideo) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("course_id", indexVideo.course_id + "");
//        indexMonPresenter.subVideo(map);
//    }
//
//    @Override
//    public void chatshareclick(View view, String url, String des, String title) {
//        this.surl = url;
//        this.sdes = des;
//        this.stitle = title;
//
//        showShare();
//    }
//
//    String surl;
//    String sdes;
//    String stitle;
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
//    public void onSucessGetAds(List<AdModel> adModels) {
//        if (adModels.size() > 0) {
//            final AdModel adModel = adModels.get(0);
//            if (adModel.type == 2 && adModel.triggerPage == 2) {
//                indexStatusAdapter.setBannerimgs(adModels);
//            }
//            if (adModel.type == 1 && adModel.triggerPage == 1) {
//                SpUtils.store(mContext, SpKey.USER_Main_AD_Bean, new Gson().toJson(adModel));
//                //System.out.println("保存开屏信息");
//            }
//            if (adModel.type == 1 && adModel.triggerPage == 1) {
//                //System.out.println("获得开屏广告");
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
//                    SpUtils.store(mContext, "Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage + "&" + showTime, true);
//                    ADDialogView.newInstance().setAdModel(adModel).show(getChildFragmentManager(), "广告");
//                }
//            }
////            if (adModel.type == 1 && adModel.triggerPage == 16) {
////                final String showTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
////                final String limitTime = adModel.validityEnd;
////                boolean isTimeEndBefore = false;
////                try {
////                    isTimeEndBefore = new Date().before(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(limitTime));
////                } catch (Exception e) {
////                    isTimeEndBefore = true;
////                    e.printStackTrace();
////                }
////                if (isTimeEndBefore && !SpUtils.getValue(mContext, "Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage + "&" + showTime, false)) {
////
////                    new Handler().postDelayed(new Runnable() {
////                        @Override
////                        public void run() {
////
////                            ADDialogView.newInstance().setAdModel(adModel).show(getChildFragmentManager(), "广告");
////                            SpUtils.store(mContext, "Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage + "&" + showTime, true);
////                        }
////                    },2000);
////                }
////            }
//        }
//    }
//
//    @Override
//    public void onSucessGetLimit(boolean limit) {
//        choseLocCan = limit;
//        if (!choseLocCan) {
//            tvLoc.setDrawable(R.drawable.mine_ic_null, mContext);
//        }
//    }
//}
