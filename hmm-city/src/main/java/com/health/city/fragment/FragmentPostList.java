package com.health.city.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.health.city.R;
import com.health.city.adapter.FansAdapter;
import com.healthy.library.adapter.PostAdapter;
import com.health.city.contract.PostListContract;
import com.healthy.library.model.Fans;
import com.healthy.library.model.PostDetail;
import com.healthy.library.model.Topic;
import com.health.city.model.UserInfoCityModel;
import com.health.city.presenter.PostListPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnCustomRetryListener;
import com.healthy.library.message.UpdateGuideInfoTotal;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.business.MessageDialog;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentPostList extends BaseFragment implements IsNeedShare, PostListContract.View, PostAdapter.OnShareClickListener, OnRefreshLoadMoreListener, AMapLocationListener, PostAdapter.OnPostLikeClickListener, PostAdapter.OnPostFansClickListener, FansAdapter.OnFansClickListener {
    private String fragmentType = "";//关注 本地 发现
    private android.widget.TextView noFollowMessage;
    private android.view.View divider;
    private ConstraintLayout hotTip;
    private ConstraintLayout hottipTitleCon;
    private android.widget.TextView hottipTitle;
    private android.widget.GridLayout tipGrid;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout refreshLayout;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private RecyclerView recyclerOther;
    private RecyclerView recycler;
    private ConstraintLayout needS;
    private android.widget.ImageView passToSendPost;
    private android.widget.ImageView passToTop;
    private PostListPresenter postPresenter;
    private int currentPage = 1;
    private PostAdapter postAdapter;
    private FansAdapter fansAdapter;

    private String cityNo;
    private String provinceNo;
    private String areaNo;
    private double longitude;
    private double latitude;
    private String locCityName;
    private String newCityName;
    private int RC_LOCATION = 11020;
    private int RC_PROVINCE_CITY = 14697;
    private int reLocTime = 0;
    private boolean isNoFollow = false;

    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;


    String surl;
    String sdes;
    String stitle;

    private AlertDialog mShareDialog;
    private Map<String, Object> fansmap = new HashMap<>();
    private android.widget.LinearLayout noFollowMessageLL;

    public static FragmentPostList newInstance(Map<String, Object> maporg) {
        FragmentPostList fragment = new FragmentPostList();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onSuccessGetPostList(List<PostDetail> posts, PageInfoEarly pageInfoEarly) {
        isNoFollow = false;
        if (isNoFollow) {

        }
        //System.out.println("fragmentType:" + fragmentType);
        if (pageInfoEarly == null) {
            if (currentPage == 1) {
                if ("关注".equals(fragmentType)) {
                    locateCheck();
                } else {
                    showEmpty();
                }
                refreshLayout.setEnableLoadMore(false);
            } else {
                refreshLayout.setEnableLoadMore(false);
                refreshLayout.finishLoadMoreWithNoMoreData();
            }

            return;
        }
        currentPage = pageInfoEarly.currentPage;
        if (currentPage == 1) {
            postAdapter.setData((ArrayList) posts);
            if (posts.size() == 0) {
                if ("关注".equals(fragmentType)) {
                    isNoFollow = true;
                    passToTop.setVisibility(View.GONE);
                    passToSendPost.setVisibility(View.GONE);
                    locateCheck();
                } else {
                    showEmpty();
                }
            } else {
                if ("关注".equals(fragmentType)) {
                    passToSendPost.setVisibility(View.VISIBLE);
                }
            }
        } else {
            postAdapter.addDatas((ArrayList)posts);
        }
        if (pageInfoEarly.isMore != 1) {
//            refreshLayout.setEnableLoadMore(false);
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
        noFollowMessageLL.setVisibility(View.GONE);
        recyclerOther.setVisibility(View.GONE);
        recycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessGetFans(List<Fans> fans) {
        noFollowMessageLL.setVisibility(View.VISIBLE);
        recyclerOther.setVisibility(View.VISIBLE);
        recycler.setVisibility(View.GONE);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.finishLoadMoreWithNoMoreData();
        isNoFollow = true;
        fansAdapter.setNewData(fans);
    }

    @Override
    public void onSuccessGetHotTopic(List<Topic> topics) {
        //System.out.println("话题OK了");
        addImages(mContext, tipGrid, topics);
    }

    private int mMargin;
    private int mCornerRadius;

    private void addImages(final Context context, final GridLayout gridLayout, final List<Topic> urls) {
        if (mMargin == 0) {
            mMargin = (int) TransformUtil.dp2px(mContext, 2);
            mCornerRadius = (int) TransformUtil.dp2px(mContext, 3);
        }
        gridLayout.removeAllViews();
        if (urls != null && urls.size() > 0) {
            gridLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int row = 2;
                    int w = (gridLayout.getWidth() - gridLayout.getPaddingLeft() - gridLayout.getPaddingRight() - (2 + 2 * (row - 1)) * mMargin) / row;
                    gridLayout.removeAllViews();
                    int needsize = 8;
                    if (urls != null && urls.size() < 8) {
                        needsize = urls.size();
                    }
                    for (int i = 0; i < needsize; i++) {
                        final Topic url = urls.get(i);
                        final int pos = i;
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = w;
                        params.setMargins(mMargin, mMargin, mMargin, mMargin);
                        View imageparent = LayoutInflater.from(mContext).inflate(R.layout.city_item_fragment_hottip, gridLayout, false);
                        TextView tipName = imageparent.findViewById(R.id.tipName);
                        TextView tipNo = imageparent.findViewById(R.id.tipNo);
                        tipNo.setText(String.format("%02d", (i + 1)));
                        if (i == 0) {
                            tipNo.setTextColor(Color.parseColor("#FF544F"));
                        }
                        if (i == 1) {

                            tipNo.setTextColor(Color.parseColor("#FA8800"));
                        }
                        if (i == 2) {

                            tipNo.setTextColor(Color.parseColor("#F7C700"));
                        }
                        tipName.setText(url.topicName);
                        gridLayout.addView(imageparent, params);
                        imageparent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(TextUtils.isEmpty(areaNo)){
                                    areaNo="0";
                                }
                                if ("本地".equals(fragmentType)) {
                                    ARouter.getInstance()
                                            .build(CityRoutes.CITY_TIP)
                                            .withString("activitytype", "本地")
                                            .withString("cityNo", (Integer.parseInt(areaNo) / 100 * 100) + "")
                                            .withString("provinceNo", (Integer.parseInt(areaNo) / 10000 * 10000) + "")
                                            .withString("areaNo", areaNo + "")
                                            .withString("topicId", url.id + "")
                                            .navigation();
                                } else {
                                    ARouter.getInstance()
                                            .build(CityRoutes.CITY_TIP)
                                            .withString("activitytype", "全国")
                                            .withString("topicId", url.id + "")
                                            .navigation();
                                }

                            }
                        });
                    }
                }
            }, 500);
        }


    }


    @Override
    public void onSuccessLike() {
//        onRefresh(null);
    }

    @Override
    public void onSuccessFan(Object result) {
        if ("0".equals(result)) {
            if (isNoFollow) {
                Toast.makeText(mContext, "关注成功,可继续关注或刷新页面", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void getData() {
        super.getData();
        currentPage = 1;
        getBasemap().put("currentPage", currentPage + "");
        postPresenter.getPostList(getBasemap());
        if ("本地".equals(fragmentType)) {
            postPresenter.getHotTopicList(getBasemap());
        }
    }

    @Override
    public void onGetMine(UserInfoCityModel userInfoCityModel) {
        getBasemap().put("addressCity", (Integer.parseInt(areaNo) / 100 * 100) + "");
        getBasemap().put("addressArea", areaNo + "");
        getBasemap().put("currentStatus", userInfoCityModel.status + "");
        postPresenter.getRecommendFans(getBasemap());
        postPresenter.getUserFans(fansmap);
    }

    @Override
    public void onGetUserFanSucess(Fans mineFans) {
        if (mineFans != null) {
            if (mineFans.friendNum > 0) {
                noFollowMessage.setText("关注的好友最近没有发帖，喜欢的人可以点关注");
            } else {
                noFollowMessage.setText("还没有关注好友哦，喜欢的人可以点关注");
            }
        } else {
            noFollowMessage.setText("还没有关注好友哦，喜欢的人可以点关注");
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.city_fragment_child;
    }

    @Override
    protected void findViews() {
        initView();
        postPresenter = new PostListPresenter(mContext, this);
        fragmentType = get("fragmentType");
        getBasemap().remove("fragmentType");
        postAdapter = new PostAdapter();
        postAdapter.setOnPostFansClickListener(this);
        postAdapter.setOnPostLikeClickListener(this);
        postAdapter.setOnShareClickListener(this);

        fansAdapter = new FansAdapter();
        fansAdapter.setOnFansClickListener(this);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.setAdapter(postAdapter);

        recyclerOther.setLayoutManager(new LinearLayoutManager(mContext));
        fansAdapter.bindToRecyclerView(recyclerOther);


        if ("关注".equals(fragmentType)) {
            getBasemap().put("currentPage", currentPage + "");
            getBasemap().put("pageSize", 10 + "");
            getBasemap().put("type", "0");
            postPresenter.getPostList(getBasemap());
            fansmap.put("type", "0");
            postPresenter.getUserFans(fansmap);
        }
        if ("本地".equals(fragmentType)) {
            hotTip.setVisibility(View.VISIBLE);
//            passToTop
            locateCheck();


        }
        if ("发现".equals(fragmentType)) {

            hotTip.setVisibility(View.VISIBLE);
            getBasemap().put("currentPage", currentPage + "");
            getBasemap().put("pageSize", 10 + "");
            getBasemap().put("type", "2");
            postPresenter.getPostList(getBasemap());
            getBasemap().put("limitsStatus", "0");
            postPresenter.getHotTopicList(getBasemap());

        }

    }

    @Override
    protected void onInvisible() {
        super.onInvisible();

    }


    @Override
    public void onResume() {
        super.onResume();
        if ("关注".equals(fragmentType)) {
            MobclickAgent.onPageStart("event2CityFocusTimeLimit");//统计页面，"MainScreen"为页面名称，可自定义
        }
        if ("本地".equals(fragmentType)) {
            MobclickAgent.onPageStart("event2CityLocTimeLimit");//统计页面，"MainScreen"为页面名称，可自定义
        }
        if ("发现".equals(fragmentType)) {
            MobclickAgent.onPageStart("event2CityFindTimeLimit");//统计页面，"MainScreen"为页面名称，可自定义
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if ("关注".equals(fragmentType)) {
            MobclickAgent.onPageEnd("event2CityFocusTimeLimit");//统计页面，"MainScreen"为页面名称，可自定义
        }
        if ("本地".equals(fragmentType)) {
            MobclickAgent.onPageEnd("event2CityLocTimeLimit");//统计页面，"MainScreen"为页面名称，可自定义
        }
        if ("发现".equals(fragmentType)) {
            MobclickAgent.onPageEnd("event2CityFindTimeLimit");//统计页面，"MainScreen"为页面名称，可自定义
        }
    }


    private String[] mPermissions = new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private void locateCheck() {
        locate();
        if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
        } else {
            requestPermissions(mPermissions, RC_LOCATION);
        }
    }

    private void locate() {
        showLoading();
        mLocationClient = new AMapLocationClient(mContext);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        //设置定位监听
        mLocationClient.setLocationListener(this);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    private void initView() {
        noFollowMessage = (TextView) findViewById(R.id.noFollowMessage);
        divider = (View) findViewById(R.id.divider);
        hotTip = (ConstraintLayout) findViewById(R.id.hotTip);
        hottipTitleCon = (ConstraintLayout) findViewById(R.id.hottipTitleCon);
        hottipTitle = (TextView) findViewById(R.id.hottipTitle);
        tipGrid = (GridLayout) findViewById(R.id.tip_grid);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerOther = (RecyclerView) findViewById(R.id.recycler_other);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        needS = (ConstraintLayout) findViewById(R.id.need_s);
        passToSendPost = (ImageView) findViewById(R.id.passToSendPost);
        passToTop = (ImageView) findViewById(R.id.passToTop);
        passToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recycler.smoothScrollToPosition(0);
            }
        });
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();

                if (firstCompletelyVisibleItemPosition <= 3 || isNoFollow) {
                    passToTop.setVisibility(View.GONE);
                } else {
                    //System.out.println("互动修改显示");
                    passToTop.setVisibility(View.VISIBLE);
                }


            }
        });
        passToSendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("关注".equals(fragmentType)) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "关注列表");
                    MobclickAgent.onEvent(mContext, "event2PostFrom", nokmap);
                }
                if ("本地".equals(fragmentType)) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "本地列表");
                    MobclickAgent.onEvent(mContext, "event2PostFrom", nokmap);
                }
                if ("发现".equals(fragmentType)) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "发现列表");
                    MobclickAgent.onEvent(mContext, "event2PostFrom", nokmap);
                }


                ARouter.getInstance()
                        .build(CityRoutes.CITY_POSTSEND)
                        .navigation();
            }
        });
        hottipTitleCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("本地".equals(fragmentType)) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "本地列表");
                    MobclickAgent.onEvent(mContext, "event2TipBoardClick", nokmap);
                }
                if ("发现".equals(fragmentType)) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "发现列表");
                    MobclickAgent.onEvent(mContext, "event2TipBoardClick", nokmap);
                }

                ARouter.getInstance()
                        .build(CityRoutes.CITY_TIPBOARD)
                        .withString("fragmentType", fragmentType)
                        .navigation();
            }
        });
        noFollowMessageLL = (LinearLayout) findViewById(R.id.noFollowMessageLL);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        getBasemap().put("currentPage", (++currentPage) + "");
        if (isNoFollow) {
            postPresenter.getRecommendFans(getBasemap());
        } else {
            postPresenter.getPostList(getBasemap());
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        currentPage = 1;
        getBasemap().put("currentPage", currentPage + "");
        postPresenter.getPostList(getBasemap());
        if ("本地".equals(fragmentType)) {
            postPresenter.getHotTopicList(getBasemap());
        }
        if ("发现".equals(fragmentType)) {
            postPresenter.getHotTopicList(getBasemap());
        }
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation.getErrorCode() == 0) {
            locCityName = aMapLocation.getCity();
            newCityName = aMapLocation.getCity();
            latitude = aMapLocation.getLatitude();
            longitude = aMapLocation.getLongitude();
            provinceNo = aMapLocation.getProvince();
            areaNo = aMapLocation.getAdCode();
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
                        .show(getChildFragmentManager(), "打开定位");

            }
        }
    }

    private void successLocation() {
        if(TextUtils.isEmpty(areaNo)){
            areaNo="0";
        }
        if ("本地".equals(fragmentType)) {
            getBasemap().put("currentPage", currentPage + "");
            getBasemap().put("pageSize", 10 + "");
            getBasemap().put("type", "1");
            getBasemap().put("addressProvince", (Integer.parseInt(areaNo) / 10000 * 10000) + "");
            getBasemap().put("addressCity", (Integer.parseInt(areaNo) / 100 * 100) + "");
            getBasemap().put("addressArea", areaNo + "");
            postPresenter.getPostList(getBasemap());
            getBasemap().put("limitsStatus", "1");
            postPresenter.getHotTopicList(getBasemap());
        } else {
            getBasemap().put("addressProvince", (Integer.parseInt(areaNo) / 10000 * 10000) + "");
            getBasemap().put("addressCity", (Integer.parseInt(areaNo) / 100 * 100) + "");
            getBasemap().put("addressArea", areaNo + "");
            postPresenter.getMine();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_LOCATION) {
            if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
                locate();
            } else {
                if (PermissionUtils.somePermissionPermanentlyDenied(mActivity, mPermissions)) {
                    PermissionUtils.showRationale(mActivity);
                } else {
                    requestPermissions(mPermissions, RC_LOCATION);
                }
            }
        }
    }

    @Override
    public void click(View view, String friendId, String type, String frtype) {
        Map<String, Object> map = new HashMap<>();
        map.put("friendId", friendId);
        map.put("friendType", frtype);
        map.put("type", type);
        postPresenter.fan2(map);
    }


    @Override
    public void postfansclick(View view, String friendId, String type, String frtype) {
        Map<String, Object> map = new HashMap<>();
        map.put("friendId", friendId);
        map.put("friendType", frtype);
        map.put("type", type);
        postPresenter.fan(map);
    }

    @Override
    public void postlikeclick(View view, String postingId, String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("postingId", postingId);
        map.put("type", type);
        postPresenter.like(map);
    }

    @Override
    public void postshareclick(View view, String url, String des, String title,String postId) {
        this.surl = url;
        this.sdes = des;
        this.stitle = title;

        if ("关注".equals(fragmentType)) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "关注列表");
            MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap);
        }
        if ("本地".equals(fragmentType)) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "本地列表");
            MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap);
        }
        if ("发现".equals(fragmentType)) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "发现列表");
            MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap);
        }
        showShare();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void guideInfo(UpdateGuideInfoTotal msg) {
        if (msg.flag == 44) {

            if ("发现".equals(fragmentType)) {
                String main = " 快速发帖<br>更多关注";
                if (SpUtils.isFirst(mContext, "floatPost")) {
                    ViewTooltip.on(passToSendPost)
                            .autoHide(true, 7000)
                            .corner(19)
                            .withShadow(false)
                            .color(Color.parseColor("#FF7A8B"))
                            .distanceWithView(5)
                            .position(ViewTooltip.Position.TOP)
                            .padding(20, 20, 20, 20)
                            .text(main)
                            .textSize(TypedValue.COMPLEX_UNIT_SP, 12)
                            .textColor(Color.WHITE)
                            .show();
                }


//                ViewTooltip
//                        .on(passToSendPost)
//                        .align(ViewTooltip.ALIGN.CENTER)
//                        .corner(30)
//                        .color(Color.parseColor("#FF7A8B"))
//                        .position(ViewTooltip.Position.TOP)
//                        .textColor(Color.WHITE)
//                        .textSize(TypedValue.COMPLEX_UNIT_SP,12)
//                        .text(main)
//                        .show();

            }


        }
    }

    @Override
    public String getSurl() {
        return surl;
    }

    @Override
    public String getSdes() {
        return sdes;
    }

    @Override
    public String getStitle() {
        return stitle;
    }

    @Override
    public Bitmap getsBitmap() {
        return null;
    }
}
