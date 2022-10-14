package com.health.client.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.health.client.HDaemonService;
import com.health.client.R;
import com.health.client.presenter.MainDialogPresenter;
import com.health.client.widget.MarketingPendantView;
import com.health.discount.contract.MainDialogContract;
import com.health.discount.widget.GiftDialog;
import com.health.mine.contract.ZxingScanContract;
import com.health.mine.presenter.ZxingScanPresenter;
import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.ADDialogView;
import com.healthy.library.business.DownLoadFragment;
import com.healthy.library.business.GetCouponDialog;
import com.healthy.library.business.GetGiftDialog;
import com.healthy.library.business.MessageDialog;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.GuideViewHelp;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Events;
import com.healthy.library.constant.Ids;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.contract.ActH5Contract;
import com.healthy.library.contract.AdContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedVideo;
import com.healthy.library.interfaces.OnNotificationListener;
import com.healthy.library.message.CanUpdateTab;
import com.healthy.library.message.UpdateAPPIndexCustom;
import com.healthy.library.message.UpdateAPPIndexCustomOther;
import com.healthy.library.message.UpdateGiftInfo;
import com.healthy.library.message.UpdateGuideInfo;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.misc.StateListDrawableBuilder;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.AppIndexCustomItem;
import com.healthy.library.model.AppIndexMarketingPendant;
import com.healthy.library.model.Coupon;
import com.healthy.library.model.MemberAction;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ShareEntity;
import com.healthy.library.model.TabChangeModel;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.model.UpdateVersion;
import com.healthy.library.model.ZxingReferralCodeModel;
import com.healthy.library.presenter.ActH5Presenter;
import com.healthy.library.presenter.ActionPresenterCopy;
import com.healthy.library.presenter.AdPresenter;
import com.healthy.library.presenter.ChangeVipPresenter;
import com.healthy.library.presenter.RefCodePresenterCopy;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.HandlerUtils;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.NotificationRefreshManager;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpUtilsOld;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.net.shoot.sharetracesdk.AppData;
import cn.net.shoot.sharetracesdk.ShareTrace;
import cn.net.shoot.sharetracesdk.ShareTraceInstallListener;

/**
 * @author Li
 * @date 2019/03/08 10:36
 * @des 壳
 */

@Route(path = AppRoutes.APP_MAIN)
public class MainActivity extends BaseActivity implements AdContract.View, MainDialogContract.View,
        IsFitsSystemWindows, ZxingScanContract.View, IsNeedVideo, ActH5Contract.View,
        HandlerUtils.OnReceiveMessageListener, OnNotificationListener {

    private ViewPager mPager;
    private TabLayout mTabBottom;
    private LinearLayout tab_bottomHide;
    private SparseArray<StateListDrawable> mImgResArray = new SparseArray();
    private SparseArray<String> mTxtArray = new SparseArray<>();
    //private boolean isnew = true;
    private long mBackTime;
    private List<Fragment> fragmentList = new ArrayList<>();
    private ZxingScanPresenter zxingScanPresenter;
    private MainDialogPresenter mainDialogPresenter;
    private GiftDialog giftDialog;
    private StringBuilder mStringBuilder = new StringBuilder();
    private HandlerUtils.HandlerHolder mHandler = new HandlerUtils.HandlerHolder(this);

    private String[] mPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    int onresumeTime = 0;
    private View mStubView;
    private Fragment mTempFragment;
    AdPresenter adPresenter;
    private ADDialogView adDialogView;
    private int mTabIconOldSize;
    //以日的维度，用户每天进入首次进入APP时，展示红点，当用户点击“同城热聊/直播”icon时，则当天不再展示
    private String mTabRedDotKey = "redDotKey";
    private boolean isShowScrollTop;
    private MarketingPendantView mMarketingPendantView;
    public List<AppIndexCustomItem> bottomNavigation;
    public int nowPostion = 0;
    public int nowPostionOld = 0;
    boolean isinitBottom = false;//强化底部问题 固定次数
    int changeBottomTime = 0;//强化底部问题 固定次数

    @Autowired
    String passPath;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adPresenter = new AdPresenter(this, this);
        UMConfigure.init(LibApplication.getAppContext(), Ids.UMENG_APP_KEY,
                ChannelUtil.getChannel(LibApplication.getAppContext()), UMConfigure.DEVICE_TYPE_PHONE, "");
        new ActH5Presenter(mContext).getH5();
        if (!SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)) {//非审核版本才去升级
//            mainDialogPresenter.checkVersion(new SimpleHashMapBuilder<>().puts("platform", "1").puts(Functions.FUNCTION, "6000"));
        }
        zxingScanPresenter.getTokerWorkerInfo();
        if (getIntent().getBooleanExtra("ispush", false)) {//说明是推送
            checkPushDes(getIntent());
        } else {
            checkScheme(getIntent());
        }

    }

    private static final int[] STATE_NORMAL = {-android.R.attr.state_selected};
    private static final int[] STATE_SELECTED = {android.R.attr.state_selected};
    private static final int[] STATE_EXCEPTION = {-android.R.attr.state_checkable};
    private static final int[] STATE_RIGHT = {android.R.attr.state_checkable};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        zxingScanPresenter = new ZxingScanPresenter(mContext, this);
        mainDialogPresenter = new MainDialogPresenter(mContext, this);
        NotificationRefreshManager.Companion.getInstance().registerListener(this);
        EventBus.getDefault().register(this);
        MobclickAgent.onEvent(mContext, Events.EVENT_TAB_FAQ);
        if(!TextUtils.isEmpty(passPath)){
            MARouterUtils.passToTarget(this,passPath);
        }
        bottomNavigation = ObjUtil.getObjList(SpUtils.getValue(mContext, SpKey.CUSTOMBOTTOM), AppIndexCustomItem.class);
        if (ListUtil.isEmpty(fragmentList)) {
            mTxtArray.put(0, "首页");
            mTxtArray.put(1, "热聊");
            mTxtArray.put(2, "热播");
            mTxtArray.put(3, "商城");
            mTxtArray.put(4, "我的");

            Fragment indexFragment = (Fragment) ARouter.getInstance()
                    .build(AppRoutes.MODULE_INDEX2).navigation();
            Fragment cityFragment = (Fragment) ARouter.getInstance()
                    .build(AppRoutes.CITY_MODULE).navigation();
            Fragment viedeoFragment = (Fragment) ARouter.getInstance()
                    .build(TencentLiveRoutes.LIVE_Module).navigation();
            Fragment serviceCenterFragment = (Fragment) ARouter.getInstance()
                    .build(DiscountRoutes.DIS_ACT_HOMEZ2).navigation();
            Fragment mineFragment = (Fragment) ARouter.getInstance()
                    .build(AppRoutes.MODULE_MINE).navigation();
            indexFragment = indexFragment == null ? new Fragment() : indexFragment;
            cityFragment = cityFragment == null ? new Fragment() : cityFragment;
            viedeoFragment = viedeoFragment == null ? new Fragment() : viedeoFragment;
            mineFragment = mineFragment == null ? new Fragment() : mineFragment;
            serviceCenterFragment = serviceCenterFragment == null ? new Fragment() : serviceCenterFragment;

            fragmentList.add(indexFragment);
            fragmentList.add(cityFragment);
            fragmentList.add(viedeoFragment);
            fragmentList.add(serviceCenterFragment);
            fragmentList.add(mineFragment);

            //初始化底部导航tab
            setTab(-1);
            setTab(nowPostion);
            mTempFragment = fragmentList.get(0);
            //默认显示第一个
            if (getSupportFragmentManager() != null && getSupportFragmentManager().getFragments().size() > 0) {//先干掉他吧
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                    fragmentTransaction.remove(getSupportFragmentManager().getFragments().get(i));
                }
                fragmentTransaction.commit();
            }

            getSupportFragmentManager().beginTransaction().add(R.id.fl_main, mTempFragment).commit();
            mTabBottom.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    nowPostion = tab.getPosition();
                    MobclickAgent.onEvent(mContext, "event2APPBottomTabShopClick", new SimpleHashMapBuilder<>().puts("soure", "底部tab商城点击量"));
                    switchFragment(fragmentList.get(tab.getPosition()));
                    setTab(tab.getPosition());
                    if (tab.getPosition() == 0 && !ListUtil.isEmpty(appIndexMarketingPendant)) {
                        mMarketingPendantView.setVisibility(View.VISIBLE);
                        mMarketingPendantView.setList(appIndexMarketingPendant);
                    } else {
                        mMarketingPendantView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    TabLayout.Tab mHomeTab = mTabBottom.getTabAt(0);
                    TabLayout.Tab mLiveTab = mTabBottom.getTabAt(2);
                    recoverTab(0, mHomeTab.getCustomView());
                    recoverTab(2, mLiveTab.getCustomView());
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    if(!tab.getCustomView().isPressed()){
                        return;
                    }
                    if (tab.getPosition() == 0 && isShowScrollTop) {
                        NotificationRefreshManager.Companion.getInstance().sendBroadCast("", "IndexFragment");
                    }
                }
            });

            if (SpUtils.getValue(mContext, "isShowZZ", false)) {
                tab_bottomHide.setVisibility(View.GONE);
            } else {
                //System.out.println("禁止滑动");
                tab_bottomHide.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void findViews() {
        if (mStubView == null) {
            ViewStub mViewStub = findViewById(R.id.viewStub);
            mStubView = mViewStub.inflate();
            //mPager = findViewById(R.id.pager);
            mTabBottom = findViewById(R.id.tab_bottom);
            tab_bottomHide = findViewById(R.id.tab_bottomHide);
            mMarketingPendantView = findViewById(R.id.marketingPendantView);
//            ArrayList<String> strings = new ArrayList<>();
//            strings.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg0.ph.126.net%2FUYbKE9f3-fHhv7uk5IgCug%3D%3D%2F1546704997042382890.gif&refer=http%3A%2F%2Fimg0.ph.126.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1641361038&t=03ef7b8c161e3d502d62d287c502bebf");
//            strings.add("https://img2.baidu.com/it/u=1195853901,2803862457&fm=26&fmt=auto");
//            strings.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fdik.img.kttpdq.com%2Fpic%2F72%2F49864%2F97c2dff601ef5b92_1280x1024.jpg&refer=http%3A%2F%2Fdik.img.kttpdq.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1641361580&t=1b4bd925d578f82d56d3115495b7acf5");
//            strings.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fdik.img.kttpdq.com%2Fpic%2F76%2F52729%2F5cb1752962cda9a8_1366x768.jpg&refer=http%3A%2F%2Fdik.img.kttpdq.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1641361580&t=530e484a2e39b298258fa5599d15e0f9");
//            mMarketingPendantView.setList(strings);
        }
    }

    List<AppIndexMarketingPendant> appIndexMarketingPendant;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAPPIndexCustom(UpdateAPPIndexCustom msg) {
        appIndexMarketingPendant = msg.appIndexMarketingPendant;
        try {
            if (mTabBottom.getSelectedTabPosition() == 0 && !ListUtil.isEmpty(appIndexMarketingPendant)) {
                System.out.println("设置底部营销广告");
                mMarketingPendantView.setList(msg.appIndexMarketingPendant);
                mMarketingPendantView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getBottomCustom(UpdateAPPIndexCustomOther msg) {
        this.bottomNavigation = msg.bottomNavigation;
        if (bottomNavigation == null) {
            SpUtils.store(mContext, SpKey.CUSTOMBOTTOM, "");
        } else {
            SpUtils.store(mContext, SpKey.CUSTOMBOTTOM, new Gson().toJson(bottomNavigation));
        }
        if (changeBottomTime == 0) {
            isinitBottom = false;
            buildBottomTabCustom();
        }
        changeBottomTime++;
    }

    private void buildBottomTabCustom() {
        if (isinitBottom) {
            return;
        }
        mImgResArray.clear();
        if (bottomNavigation != null && bottomNavigation.size() == 5) {
            System.out.println("底部有自定义");
            mImgResArray.put(0, new StateListDrawableBuilder(this, bottomNavigation.get(0), R.drawable.ic_tab_index_s, R.drawable.ic_tab_index).build());
            mImgResArray.put(1, new StateListDrawableBuilder(this, bottomNavigation.get(1), R.drawable.ic_tab_city_s, R.drawable.ic_tab_city).build());
            mImgResArray.put(2, new StateListDrawableBuilder(this, bottomNavigation.get(2), R.drawable.ic_tab_live_s, R.drawable.ic_tab_live).build());
            mImgResArray.put(3, new StateListDrawableBuilder(this, bottomNavigation.get(3), R.drawable.ic_tab_service_s, R.drawable.ic_tab_service).build());
            mImgResArray.put(4, new StateListDrawableBuilder(this, bottomNavigation.get(4), R.drawable.ic_tab_mine_s, R.drawable.ic_tab_mine).build());
        } else {
            mImgResArray.put(0, new StateListDrawableBuilder().setSelectorDrawable(STATE_SELECTED, R.drawable.ic_tab_index_s).setSelectorDrawable(STATE_NORMAL, R.drawable.ic_tab_index).build());
            mImgResArray.put(1, new StateListDrawableBuilder().setSelectorDrawable(STATE_SELECTED, R.drawable.ic_tab_city_s).setSelectorDrawable(STATE_NORMAL, R.drawable.ic_tab_city).build());
            mImgResArray.put(2, new StateListDrawableBuilder().setSelectorDrawable(STATE_SELECTED, R.drawable.ic_tab_live_s).setSelectorDrawable(STATE_NORMAL, R.drawable.ic_tab_live).build());
            mImgResArray.put(3, new StateListDrawableBuilder().setSelectorDrawable(STATE_SELECTED, R.drawable.ic_tab_service_s).setSelectorDrawable(STATE_NORMAL, R.drawable.ic_tab_service).build());
            mImgResArray.put(4, new StateListDrawableBuilder().setSelectorDrawable(STATE_SELECTED, R.drawable.ic_tab_mine_s).setSelectorDrawable(STATE_NORMAL, R.drawable.ic_tab_mine).build());
        }
        if (fragmentList.size() > 0) {
            nowPostionOld = nowPostion;
            setTab(-1);
            try {
                mTabBottom.getTabAt(nowPostionOld).select();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isinitBottom = true;
    }

    /**
     * 恢复原有tab样式 只限于 “首页”、“直播”两位特殊使用
     * 适用于tab未选中的时候
     *
     * @param customView
     */
    private void recoverTab(int position, View customView) {
        ImageView mImgTab = customView.findViewById(R.id.img_tab);
        TextView mTxtTab = customView.findViewById(R.id.txt_tab);
        if (mTabIconOldSize == 0) {
            mTabIconOldSize = mImgTab.getWidth();
        }
        if (mImgTab.getWidth() != mTabIconOldSize) {
            ViewGroup.LayoutParams layoutParams = mImgTab.getLayoutParams();
            layoutParams.width = mTabIconOldSize;
            layoutParams.height = mTabIconOldSize;
            mImgTab.setLayoutParams(layoutParams);
        }

        if (position == 0 && isShowScrollTop) {
            try {
                mImgTab.setImageDrawable(mImgResArray.get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mTxtTab.setVisibility(View.VISIBLE);
    }

    /**
     * 设置底部Tab显示图片以及文字
     *
     * @param position -1 首次进去时的参数 其余为tab切换传入
     */
    @SuppressLint("InflateParams")
    private void setTab(int position) {
        String value = SpUtils.getValue(mContext, mTabRedDotKey);
        String mCurrentTime = DateUtils.long2Str(System.currentTimeMillis(), false);
        if (-1 == position) {
            mTabBottom.removeAllTabs();
            for (int i = 0; i < mTxtArray.size(); i++) {
                value = SpUtils.getValue(mContext, mTabRedDotKey);
                View view = LayoutInflater.from(this).inflate(R.layout.item_tab, mTabBottom, false);
                ImageView mImgTab = view.findViewById(R.id.img_tab);
                TextView mTxtTab = view.findViewById(R.id.txt_tab);
                View mRedDot = view.findViewById(R.id.sv_Red_dot);
                int mVisibility = View.GONE;
                if (i == 1 || i == 2) {
                    mVisibility = View.VISIBLE;
                    boolean city = false;
                    boolean live = false;
                    if (!TextUtils.isEmpty(value)) {
                        try {
                            JSONObject jsonObject = new JSONObject(value);
                            //存入的日期
                            String time = jsonObject.getString("time");
                            city = jsonObject.getBoolean("city");
                            live = jsonObject.getBoolean("live");
                            if (mCurrentTime.equals(time)) {
                                if (i == 1 && city) {
                                    mVisibility = View.GONE;
                                }
                                if (i == 2 && live) {
                                    mVisibility = View.GONE;
                                }
                            } else {
                                city = false;
                                live = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (mVisibility == View.VISIBLE) {
                        //也就是当前未展示
                        JsonObject object = new JsonObject();
                        object.addProperty("time", mCurrentTime);
                        object.addProperty("city", city);
                        object.addProperty("live", live);
                        SpUtils.store(mContext, mTabRedDotKey, object.toString());
                    }
                }
                mRedDot.setVisibility(mVisibility);
                try {
                    if (mImgResArray.get(i).isVisible()) {

                    }
                    mImgTab.setImageDrawable(mImgResArray.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mTxtTab.setText(mTxtArray.get(i));
                if (mTabIconOldSize == 0) {
                    mTabIconOldSize = mImgTab.getWidth();
                }
                mTabBottom.addTab(mTabBottom.newTab().setCustomView(view));
            }
        } else {
            boolean city = false;
            boolean live = false;
            JsonObject object = null;
            try {
                JSONObject jsonObject = new JSONObject(value);
                //存入的日期
                String time = jsonObject.getString("time");
                city = jsonObject.getBoolean("city");
                live = jsonObject.getBoolean("live");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            View customView = mTabBottom.getTabAt(position).getCustomView();
            TextView mTxtTab = customView.findViewById(R.id.txt_tab);
            ImageView mImgTab = customView.findViewById(R.id.img_tab);
            View mRedDot = customView.findViewById(R.id.sv_Red_dot);
            if (mTabIconOldSize == 0) {
                mTabIconOldSize = mImgTab.getWidth();
            }
            ViewGroup.LayoutParams layoutParams = mImgTab.getLayoutParams();
            if (position == 0 && isShowScrollTop) { // 首页
                mTxtTab.setVisibility(View.GONE);
                mImgTab.setImageResource(R.drawable.ic_tab_index_to_top);
                int mParams = mTabIconOldSize * 2;
                layoutParams.width = mParams;
                layoutParams.height = mParams;
            } else if (position == 1) { // 同城圈
                //也就是当前未展示
                object = new JsonObject();
                object.addProperty("city", true);
                object.addProperty("live", live);
                mRedDot.setVisibility(View.GONE);
            } else if (position == 2) { // 直播
                object = new JsonObject();
                object.addProperty("city", city);
                object.addProperty("live", true);
                mRedDot.setVisibility(View.GONE);
                mTxtTab.setVisibility(View.GONE);
                int mParams = mTabIconOldSize * 2;
                layoutParams.width = mParams;
                layoutParams.height = mParams;
            } else {
                mTxtTab.setVisibility(View.VISIBLE);
            }
            if (object != null) {
                object.addProperty("time", mCurrentTime);
                SpUtils.store(mContext, mTabRedDotKey, object.toString());
            }
            mImgTab.setLayoutParams(layoutParams);
        }
    }

    /**
     * 首页滑动通知回调
     *
     * @param type      0 隐藏置顶箭头 1 显示置顶箭头 2 是否滑动状态(或许通过此监听完善“营销挂件”）
     * @param className
     */
    @Override
    public void onNotification(String type, String className) {

        if ("2".equals(type) || "3".equals(type)) {
            if (!mMarketingPendantView.isExecute()) {
                if ("2".equals(type) && mMarketingPendantView.isShow()) {
                    mMarketingPendantView.endAnimator();
                }
                if ("3".equals(type) && !mMarketingPendantView.isShow()) {
                    mMarketingPendantView.startAnimator();
                }
            }
            return;
        }

        if (isShowScrollTop == type.equals("1")) {
            return;
        }
        isShowScrollTop = type.equals("1");
        TabLayout.Tab tabAt = mTabBottom.getTabAt(0);
        View customView = tabAt.getCustomView();
        TextView mTxtTab = customView.findViewById(R.id.txt_tab);
        ImageView mImgTab = customView.findViewById(R.id.img_tab);
        ViewGroup.LayoutParams layoutParams = mImgTab.getLayoutParams();
        if (mTabIconOldSize == 0) {
            mTabIconOldSize = mImgTab.getWidth();
        }
        int mParams;
        if (tabAt.isSelected() && isShowScrollTop) {
            mTxtTab.setVisibility(View.GONE);
            mImgTab.setImageResource(R.drawable.ic_tab_index_to_top);
            mParams = mTabIconOldSize * 2;
        } else {
            mTxtTab.setVisibility(View.VISIBLE);
            try {
                mImgTab.setImageDrawable(mImgResArray.get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
            mParams = mTabIconOldSize;
        }

        layoutParams.width = mParams;
        layoutParams.height = mParams;
        mImgTab.setLayoutParams(layoutParams);
    }

    private void switchFragment(Fragment fragment) {

        if (fragment != mTempFragment) {   //如果相等不用做任何操作 所以加了此判断
            try {
                if (!fragment.isAdded()) {  //判断是否添加过该Fragment
                    getSupportFragmentManager().beginTransaction().hide(mTempFragment).add(R.id.fl_main, fragment).commit(); //如果没有添加过则添加
                } else {
                    getSupportFragmentManager().beginTransaction().hide(mTempFragment).show(fragment).commit();//如果添加过则直接隐藏之前的Fragment并显示出来
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mTempFragment = fragment;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //System.out.println("重新进入onNewIntent");
        if (intent.getBooleanExtra("ispush", false)) {//说明是推送
            //System.out.println("重新进入onNewIntent推送");
            checkPushDes(intent);
        } else {
            //System.out.println("是scheme进入的");
            checkScheme(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (mTabBottom.getSelectedTabPosition() == 0) {
            long time = System.currentTimeMillis();
            if (time - mBackTime > 2000) {
                mBackTime = time;
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            } else {
                if (!ChannelUtil.isRealRelease()) {
                    //尝试提交
//                    Toast.makeText(getApplicationContext(), "补丁代偿:" + SpUtils.getValue(LibApplication.getAppContext(), "showKillDialog", false), Toast.LENGTH_SHORT).show();
                }
                super.onBackPressed();
                if (SpUtils.getValue(LibApplication.getAppContext(), "showKillDialog", false)) {
                    delayKillReal();
                }
            }
        } else {
            mTabBottom.getTabAt(0).select();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        EventBus.getDefault().unregister(this);
        NotificationRefreshManager.Companion.getInstance().unRegisterListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void giftHome(UpdateGiftInfo msg) {
        if (msg.isTest) {
            showGiftDialog();
        } else {
            needShowGift();
        }
    }


    /*** 底部导航切换 */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTab(TabChangeModel msg) {
        mTabBottom.getTabAt(msg.tab).select();
    }

    /***  */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void enableTab(CanUpdateTab msg) {
        tab_bottomHide.setVisibility(View.GONE);
    }

    /*** 引导 */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void guideInfo(UpdateGuideInfo msg) {
        if (msg.flag == 2) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    GuideViewHelp.showGuideRoundRelative((Activity) mContext, "同城憨妈Guide", true, mTabBottom.getTabAt(1).getCustomView(), R.layout.view_guide_type3, Gravity.TOP, 50, new OnGuideChangedListener() {
                        @Override
                        public void onShowed(Controller controller) {

                        }

                        @Override
                        public void onRemoved(Controller controller) {

                        }
                    });
                }
            }, 500);

        }
    }

    public void showGiftDialog() {
        Fragment f = getSupportFragmentManager().findFragmentByTag("礼物箱子");
        if (f != null) {
            getSupportFragmentManager().beginTransaction().remove(f).commitNowAllowingStateLoss();
        }
        if (giftDialog != null) {
            giftDialog.dismiss();
        }
        new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                new SimpleHashMapBuilder<>()
                        .putObject(new MemberAction(
                                BuildConfig.VERSION_NAME,
                                1,
                                3,
                                "MainShowNewPeopleGift",
                                getActivitySimpleName(),
                                new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                        ));
        giftDialog = GiftDialog.newInstance().setOnDismissListener(dialog -> {
//                SpUtils.store(mContext, SpKey.GIFT_ALREADYSHOW, true);
            mainDialogPresenter.checkMessageDialog();
        });
        giftDialog.show(getSupportFragmentManager(), "礼物箱子");
    }

    public void needShowGift() {
        checkLocReal();
        if (!TextUtils.isEmpty(LocUtil.getytbAppId())) {
            mainDialogPresenter.checkCouponGift(new SimpleHashMapBuilder()
                    .puts("ytbAppId", LocUtil.getytbAppId())
            );
        }
    }

    /**
     * 安装参数三方sdk处理
     */
    private void buildShareTrace() {
        ShareTrace.getInstallTrace(new ShareTraceInstallListener() {
            @Override
            public void onInstall(AppData data) {//这里说明获取到分享下载的导购码
//                System.out.println("AppData:" + data.toString());
                if (data != null) {
                    String paramsData = data.getParamsData();
                    if (TextUtils.isEmpty(paramsData)) {
                        errorBind(1);
                        SpUtils.store(mContext, "referralCodeInit", true);
                        return;
                    }

                    boolean isHasReCode = false;
                    if (paramsData.contains("referral_code")) {
                        isHasReCode = true;
                    }
                    String tipMessage = "";
                    /*** 导购绑定 */
                    //安装参数 使用
                    if (paramsData.contains("keyFrame") && SpUtilsOld.isFirst(mContext, "keyFrame")) {//这里说明有一个关键帧 出现一个安装意图 有可能是邀请有礼 就不能马上访问省市区绑定了
                        Map<String, String> map = new HashMap<>();
                        String[] resultarray = paramsData.split("&");
                        for (int i = 0; i < resultarray.length; i++) {
                            map.put(resultarray[i].split("=")[0], resultarray[i].split("=").length > 1 ? resultarray[i].split("=")[1] : "");
                        }
                        String code = map.get("keyFrame");
                        mainDialogPresenter.getAppProgram(new SimpleHashMapBuilder<>().puts("key", code));

                        if (!isHasReCode) {
                            errorBind(2);
                            SpUtils.store(mContext, "referralCodeInit", true);
                        }
                        if (!ChannelUtil.isRealRelease()) {
                            //Toast.makeText(mContext, "获得了keyFrame:" + code, Toast.LENGTH_SHORT).show();
                            tipMessage = "获得了keyFrame:" + code;
                        }
                    } else {
                        onresumeTime = 1000;
                        checkLocReal();
                        if (paramsData.contains("referral_code")) {//这里说明获取到分享下载的导购码格式正确
                            Map<String, String> map = new HashMap<>();
                            String[] resultarray = paramsData.split("&");
                            for (int i = 0; i < resultarray.length; i++) {
                                map.put(resultarray[i].split("=")[0], resultarray[i].split("=").length > 1 ? resultarray[i].split("=")[1] : "");
                            }
                            String code = map.get("referral_code");
                            if (!ChannelUtil.isRealRelease()) {
                                //Toast.makeText(mContext, "获得了referral_code:" + code, Toast.LENGTH_SHORT).show();
                                tipMessage = "获得了获得了referral_code:" + code;
                            }
                            SpUtils.store(mContext, SpKey.INSTALL_REL_CODE, code);
                            //System.out.println("获得的邀请导购码:" + code);
                            if (SpUtilsOld.isFirst(mContext, "referralCodeBind")) {
                                zxingScanPresenter.getCodeInfo(code);
                            }
                        }
                    }

                    /*** 页面功能分析（内部跳转功能） */
                    if (!TextUtils.isEmpty(tipMessage)) {
                        Message obtain = Message.obtain();
                        obtain.what = 1;
                        obtain.obj = tipMessage;
                        mHandler.sendMessage(obtain);
                    }
                } else {
                    checkLocReal();
                    errorBind(3);
                    SpUtils.store(mContext, "referralCodeInit", true);
                }
            }

            @Override
            public void onError(int code, String msg) {
                checkLocReal();
                Log.e("TAG", "Get install trace info error. code=" + code + ",msg=" + msg);
            }
        });
    }

    private void errorBind(int index) {//记录下哪些用户没有导购码就安装了
        System.out.println("无安装导购");
//        try {
//            CrashReport.postCatchedException(new Throwable("无安装导购： code==" + index + "   result==" + SpUtils.getValue(mContext, SpKey.PHONE)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }


    @Override
    public void handlerMessage(Message msg) {
        if (msg.what == 1) {
            showToast(msg.obj.toString());
        }
    }

    /**
     * 推送消息处理
     *
     * @param intent
     */
    private void checkPushDes(Intent intent) {
        int pushflag = intent.getIntExtra("pushIndex", 0);
        /*------------------ 完善点击通知栏跳转 START -----------------------*/
        String mJPushExtra = intent.getStringExtra(JPushInterface.EXTRA_EXTRA);
        String mNotificationTitle = intent.getStringExtra(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String mNotificationContent = intent.getStringExtra(JPushInterface.EXTRA_ALERT);
        if (!TextUtils.isEmpty(mJPushExtra)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(mJPushExtra);//后续如需要参数 ：对应的id或者其他数据从里面拿
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (mNotificationContent.contains("预约")) {
//                ARouter.getInstance().build(MineRoutes.MINE_ORDER_SUB_LIST)
//                        .navigation();
                ARouter.getInstance()
                        .build(MineRoutes.MINE_ORDER_SUB_HISTORY_LIST)
                        .navigation();
                return;
            } else if (mNotificationTitle.contains("活动通知")) {
                if (mNotificationContent.contains("已中奖")) {
                    //跳转领奖下单页（调整为跳转报名投票列表页面）
                    ARouter.getInstance()
                            .build(MineRoutes.MINE_VOTELIST)
                            .withString("currentItem", "1")
                            .navigation();
                    return;
                }
            } else if (mNotificationTitle.contains("中奖提醒")) {
                //跳转领奖中心下单页
                ARouter.getInstance()
                        .build(MineRoutes.MINE_AWARD_CENTER)
                        .navigation();
                return;
            } else if (mNotificationTitle.contains("抽奖资格提醒")) {
                String lotteryActivityId = "";
                if (jsonObject != null) {
                    lotteryActivityId = jsonObject.optString("lotteryActivityId");//获取到活动id
                }
                String url = "http://192.168.10.181:8000/lottery.html";
                if (!TextUtils.isEmpty(SpUtils.getValue(mContext, UrlKeys.H5_LOTTERY_URL))) {
                    url = SpUtils.getValue(mContext, UrlKeys.H5_LOTTERY_URL);
                }
                //跳转领奖中心下单页
                ARouter.getInstance().build(IndexRoutes.INDEX_WEBVIEWSINGLE)
                        //78434178651668480 -> 大转盘
                        //78448856115200000 -> 九宫格
                        .withString("url", url + "?id=" + lotteryActivityId + "&token=" + SpUtils.getValue(mContext, SpKey.TOKEN))
                        .withString("title", "")
                        .withBoolean("isShowTopBar", false)
                        .navigation();
                return;
            } else {
                String androidLinkName = jsonObject.optString("androidLinkName");
                String h5LinkUrl = jsonObject.optString("h5LinkUrl");
                if (!TextUtils.isEmpty(androidLinkName)) {
                    MARouterUtils.passToTarget(mContext, androidLinkName);
                    return;
                } else if (!TextUtils.isEmpty(h5LinkUrl)) {
                    MARouterUtils.passToTarget(mContext, h5LinkUrl);
                    return;
                }

            }
        }
        /*------------------ 完善点击通知栏跳转 END -----------------------*/
        switch (pushflag) {
            case 1:
                ARouter.getInstance()
                        .build(AppRoutes.APP_MESSAGEDIS)
                        .withString("type", "评论")
                        .withString("title", "评论")
                        .navigation();
                break;
            case 2:
                ARouter.getInstance()
                        .build(AppRoutes.APP_MESSAGELIKE)
                        .withString("type", "赞")
                        .withString("title", "赞")
                        .navigation();
                break;
            case 3:
                ARouter.getInstance()
                        .build(AppRoutes.APP_MESSAGEHELP2)
                        .withString("type", "母婴服务小助手")
                        .withString("title", "交易通知")
                        .navigation();
                break;
            case 4:
                ARouter.getInstance()
                        .build(AppRoutes.APP_MESSAGEHELP)
                        .withString("type", "同城圈小助手")
                        .withString("title", "同城圈")
                        .navigation();
                break;
            case 5:
                ARouter.getInstance()
                        .build(AppRoutes.APP_MESSAGEHELP2)
                        .withString("type", "母婴商城小助手")
                        .withString("title", "交易通知")
                        .navigation();
                break;
            case 6:
                ARouter.getInstance()
                        .build(AppRoutes.APP_MESSAGEHELP)
                        .withString("type", "问答小助手")
                        .withString("title", "专家答疑")
                        .navigation();
                break;
            case 7:
                ARouter.getInstance()
                        .build(AppRoutes.APP_MESSAGETIP)
                        .withString("type", "通知")
                        .withString("title", "系统通知")
                        .navigation();
                break;
            case 71://直播
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEW)
                        .withString("title", "憨妈直播")
                        .withBoolean("isinhome", false)
                        .withBoolean("leftnow", true)
                        .withBoolean("videoshop", true)
                        .withString("url", intent.getStringExtra("url"))
                        .navigation();
                break;
            case 9:
                String messageData = intent.getStringExtra("messageData");
                showSmallGiftDialog(messageData);
                break;
            case 10:
                String data = intent.getStringExtra("messageData");
                try {
                    String courseId = new JSONObject(data).optString("courseId");
                    ARouter.getInstance()
                            .build(TencentLiveRoutes.LiveNotice)
                            .withString("courseId", courseId)
                            .navigation();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateUserLocationMsg msg) {
//        buildShareTrace();
    }

    /**
     * Scheme 外部跳转处理
     *
     * @param intent
     */
    private void checkScheme(Intent intent) {
        System.out.println("");
        /*if (TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.USER_ID))) { // 暂存优化 先不提（避免没有登录进入到其他页面在返回登录）
            return;
        }*/
        boolean needLocal = true;
        Uri uri = intent.getData();
        Uri uriresult = null;
        if (uri != null) {
            String result = uri.toString();
            if (result != null) {
                result = result.replace("undefined", "");
            }
            String[] resultatrray = result.split("\\?");
            result = resultatrray.length > 1 ? resultatrray[1] : "";
            if (uri.toString().contains("referral_code=")) {
                if (!TextUtils.isEmpty(uri.getQueryParameter("referral_code"))) {
                    new RefCodePresenterCopy(mContext).posRefCode(uri.getQueryParameter("referral_code"));
                    new RefCodePresenterCopy(mContext).binding(uri.getQueryParameter("referral_code"), SpUtils.getValue(mContext, SpKey.CHOSE_MC), "0");
                }
            }
            System.out.println("打开的参数" + uri);
            if (uri.toString().contains("bargaining")) {//说明要跳转砍价
                if (result != null && result.contains("bargainId=false") && result.contains("bargainMemberId=false")) {
                    return;
                }
                uriresult = Uri.parse("hmmpassimp://hmm" + DiscountRoutes.DIS_KICKHELPDETAIL + "?" + result);
            }
            if (uri.toString().contains("assembleing")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + DiscountRoutes.DIS_GROUPDETAIL + "?" + result);
            }
            if (uri.toString().contains("liveVideo")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + ServiceRoutes.SERVICE_DETAIL + "?" + result);
            }
            if (uri.toString().contains("babyName")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_NAME + "?" + result);
            }
            if (uri.toString().contains("babyTest")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_TEST_SEX_MAIN + "?" + result);
            }
            if (uri.toString().contains("babyWeight")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_BABY_WEIGHT + "?" + result);
            }
            if (uri.toString().contains("growth")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_GROW + "?" + result);
            }
            if (uri.toString().contains("slodon")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + ServiceRoutes.SERVICE_MODULEACT + "?" + result);
            }
            if (uri.toString().contains("topicMange")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + CityRoutes.CITY_TIP + "?" + result);
            }
            if (uri.toString().contains("foodType") || uri.toString().contains("foodtype")) {
                if (result.contains("type=null")) {
                    uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_CANEAT + "");
                } else if (result.contains("type=CanEat") || result.contains("foodtype=CanEat") || result.contains("foodType=CanEat")) {
                    uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_CANEAT + "?" + result);
                } else if (result.contains("type=pregnancyFood") || result.contains("foodtype=pregnancyFood") || result.contains("foodType=pregnancyFood")) {
                    uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_FOOD + "?" + "activityType=孕期食谱");
                } else if (result.contains("type=monthFood") || result.contains("foodtype=monthFood") || result.contains("foodType=monthFood")) {
                    uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_FOOD + "?" + "activityType=月子食谱");
                } else if (result.contains("type=babyFood") || result.contains("foodtype=babyFood") || result.contains("foodType=babyFood")) {
                    uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_FOOD + "?" + "activityType=宝宝辅食");
                }
            }
            if (uri.toString().contains("serviceMall")) {
                result = result.replace("id", "shopId");//修正参数名
                uriresult = Uri.parse("hmmpassimp://hmm" + SecondRoutes.SECOND_SHOP_DETAIL + "?" + result);
            }
            //异业聚惠团分享跳转
            if (uri.toString().contains("YYActMain")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + SecondRoutes.MAIN_ACTMODULE + "?" + result);
            }
            if (uri.toString().contains("babyTool")) {
                result = result.replace("id", "shopId");//修正参数名
                if (result.contains("type=1")) {
                    if (result.contains("vcType=1")) {
                        uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_TEST_SEX_EXL + "");
                    } else if (result.contains("vcType=")) {
                        uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_TEST_SEX + "?" + result);
                    } else {
                        uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_TEST_SEX_MAIN + "?" + result);
                    }
                } else if (result.contains("type=2")) {
                    uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_VACC_CHECK + "?" + result);

                }
            }
            if (result.contains("scheme=HMMDEFAULT")) {
                uri = Uri.parse("hmmpassimp://hmm" + ServiceRoutes.SERVICE_DETAIL + "?" + result);
                mainDialogPresenter.getAppProgram(new SimpleHashMapBuilder<>().puts("key", uri.getQueryParameter("keyFrame")));
                //获得真正的意图
                return;
            }
            //异业商品详情
            if (result.contains("scheme=SecondGoodsDetail")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + SecondRoutes.SECOND_SERVICE_DETAIL + "?" + result);
            }
            //签到分享
            if (result.contains("scheme=SignInPage")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + ServiceRoutes.SERVICE_POINTSSIGNIN + "?" + result);
            }
            if (result.contains("scheme=LiveStream")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + TencentLiveRoutes.LiveNotice + "?" + result);
            }
            if (result.contains("scheme=NormGoodsDetail")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + SecondRoutes.SECOND_SERVICE_DETAIL + "?" + result.replace("goodsId=", "id="));
            }
            if (result.contains("scheme=GiftsForSharing")) {
                needLocal = false;
                uriresult = Uri.parse("hmmpassimp://hmm" + MineRoutes.MINE_INVITED + "?" + result);
            }
            //PK详情
            if (result.contains("scheme=PkVoteDetail")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + CityRoutes.CITY_PK_VOTING_DETAIL + "?" + result);
            }
            if (result.contains("scheme=VoteDetail")) {
                String activityId = uri.getQueryParameter("id");
                String token = SpUtils.getValue(mContext, SpKey.TOKEN);
                String title = uri.getQueryParameter("voteTitle");
                String url = String.format("http://192.168.10.181:8000/voteList.html?id=%s&token=%s", activityId, token);
                if (!TextUtils.isEmpty(SpUtils.getValue(mContext, UrlKeys.H5_VOTE_LIST_URL))) {
                    url = String.format(SpUtils.getValue(mContext, UrlKeys.H5_VOTE_LIST_URL) + "?id=%s&token=%s", activityId, token);
                }
                result = "url=" + URLEncoder.encode(url) + "&" + "title=" + title;
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_WEBVIEWSINGLE + "?" + result);
            }
            // 服务预约详情
            if (result.contains("scheme=AppointmentDetail")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + ServiceRoutes.SERVICE_APPOINTMENTDETIAL + "?" + result);
            }
            if (result.contains("scheme=GoodsDetailGroup")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + SecondRoutes.SECOND_SERVICE_DETAIL + "?" + result);
            } else if (result.contains("scheme=GoodsDetail")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + SecondRoutes.SECOND_SERVICE_DETAIL + "?" + result);
            } else if (result.contains("scheme=ActivityList")) {
                //活动列表 （砍价、拼团、秒杀、优惠券）
                String type = uri.getQueryParameter("type");
                String mRouter = "";
                if ("1".equals(type)) {
                    mRouter = DiscountRoutes.DIS_NEWKICKLIST;
                }
                if ("2".equals(type)) {
                    mRouter = DiscountRoutes.DIS_NEWASSEMBLEACTIVITY;
                }
                if ("3".equals(type)) {
                    mRouter = DiscountRoutes.DIS_SECKILLLISTACTIVITY;
                }
                if ("6".equals(type)) {
                    mRouter = DiscountRoutes.DIS_CARDCENTER;
                    new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                            new SimpleHashMapBuilder<>()
                                    .putObject(new MemberAction(
                                            BuildConfig.VERSION_NAME,
                                            1,
                                            7,
                                            "CardCenterFromZxing",
                                            getActivitySimpleName(),
                                            new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                                    ));
                }
                uriresult = Uri.parse("hmmpassimp://hmm" + mRouter + "?" + result);
            } else if (result.contains("scheme=BDetail")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_ANALYZE_B + "");
            } else if (result.contains("scheme=HMMAudioAlbumDetail")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + SoundRoutes.SOUND_DETAIL + "?" + result.replace("index=", "id=") + "&audioType=2");
            } else if (result.contains("scheme=HMMBabyAudioAlbumDetail")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + SoundRoutes.SOUND_DETAIL + "?" + result.replace("index=", "id=") + "&audioType=1");
            } else if (result.contains("scheme=HMMMumAudio")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + SoundRoutes.SOUND_MAIN_MON + "?" + result.replace("index=", "currentIndex=") + "&audioType=2");
            } else if (result.contains("scheme=HMMBabyAudio")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + SoundRoutes.SOUND_MAIN + "?" + result.replace("index=", "currentIndex=") + "&audioType=1");
            } else if (result.contains("scheme=CanEatList")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_CANEAT_TYPE + "?" + result.replace("foodType", "selectType").replace("foodName", "nowtitle") + "&activityType=0");
            } else if (result.contains("scheme=CanEatDetail")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_WEBVIEW_ALL + "?" + "stitle=" + "能不能吃-" + uri.getQueryParameter("foodName"));
            } else if (result.contains("scheme=RecipeInfo")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_TOOLS_FOOD_DETAIL + "?" + result);
            } else if (result.contains("scheme=FoodSuggest")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_WEBVIEW_ALL + "?" + "stitle=" + uri.getQueryParameter("title"));
            } else if (result.contains("scheme=FoodSuggest")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_WEBVIEW_ALL + "?" + "stitle=" + uri.getQueryParameter("title"));
            } else if (result.contains("scheme=HMMExpertHomePage")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + FaqRoutes.FAQ_EXPERT_HOMEPAGE + "?" + result);
            } else if (result.contains("scheme=HMMVideoOnLine")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_VIDEOONLINELIST + "?");
            } else if (result.contains("scheme=HMMVIDEO")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_VIDEOLIST + "?" + result);
            } else if (result.contains("scheme=HMMVideoCategaryList")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_VIDEOONLINELISTBLOCk + "?" + result.replace("type", "videoType").replace("title", "videoTypeName"));
            } else if (result.contains("scheme=HMMVideoDetail")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_HANMOMTEACHINGDETAIL + "?" + result);
            } else if (result.contains("scheme=HMMPostDetail")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + CityRoutes.CITY_POSTDETAIL + "?" + result);
            } else if (result.contains("scheme=NewsMessage")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_WEBVIEWARTICLE + "?" + result);
            } else if (result.contains("scheme=BirthdayGift")) {
                uriresult = Uri.parse("hmmpassimp://hmm" + DiscountRoutes.MINE_NEW_USER_GIFT + "?" + result);
            }

            if (result.contains("scheme=MonthlySpecial")) {//月度专题活动页面
                uriresult = Uri.parse("hmmpassimp://hmm" + IndexRoutes.INDEX_MONTHLY + "?" + result);
            }

            if (uri != null && uri.toString().startsWith("http")) {
                Uri httpuri = uri;
                Intent httpintent = new Intent(Intent.ACTION_VIEW, httpuri);
                startActivity(httpintent);
                return;
            }
            Postcard postcard = null;
            try {
                if (uriresult != null) {
                    postcard = ARouter.getInstance()
                            .build(uriresult);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result.contains("scheme=CanEatDetail")) {
                String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_CAN_EAT);
                String url = String.format("%s?id=%s&foodName=%s&scheme=CanEatDetail&foodId=%s", urlPrefix, uri.getQueryParameter("foodId"), uri.getQueryParameter("foodName"), uri.getQueryParameter("foodId") + "");

                postcard.withString("title", uri.getQueryParameter("foodName"))
                        .withString("url", url)
                        .withBoolean("needShare", true)
                        .withBoolean("isinhome", true)
                        .withBoolean("doctorshop", true);
            }
            if (result.contains("scheme=NewsMessage")) {
                String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_KNOWLEDGE);
                String url = String.format("%s?id=%s&scheme=NewsMessage", urlPrefix, uri.getQueryParameter("id"));
                postcard.withString("title", "资讯")
                        .withString("url", url)
                        .withBoolean("needShare", true)
                        .withBoolean("isinhome", true)
                        .withBoolean("needfindcollect", true)
                        .withBoolean("doctorshop", true);
            }
            if (result.contains("scheme=FoodSuggest")) {
                String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_dietProposeUrl);
                String url = String.format("%s?id=%s&scheme=FoodSuggest&title=%s", urlPrefix, uri.getQueryParameter("id"), uri.getQueryParameter("title"));

                postcard.withString("title", uri.getQueryParameter("title"))
                        .withString("url", url)
                        .withString("sdes", " ")
                        .withBoolean("needShare", true)
                        .withBoolean("isinhome", true)
                        .withBoolean("doctorshop", true);
            }
            if (needLocal) {
                checkLocReal();
            }
            try {
                postcard.navigation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void startGoodService() {
        Intent mForegroundService = new Intent(this, HDaemonService.class);
//            mForegroundService.putExtra("Foreground", "This is a foreground service.");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mForegroundService);
        } else {
            startService(mForegroundService);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mReactInstanceManager != null) {
//            mReactInstanceManager.onHostResume(this, this);
//        }
        if (!SpUtils.getValue(MainActivity.this, SpKey.AUDITSTATUS, true)) {
            startGoodService();
        }
        MobclickAgent.onPageStart("event2LeftLimit"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);
        new Thread(this::buildShareTrace).start();
        if (onresumeTime > 0 && !SpUtils.getValue(mContext, "m_10004", false)) {
            checkLocReal();
        }
        onresumeTime++;
        isinitBottom=false;
        buildBottomTabCustom();
//        if(RomUtils.isEmulator()){//如果是虚拟机
//            if(ChannelUtil.isRealRelease()){
//                Toast.makeText(mContext,"识别到当前是虚拟机即将3秒后关闭程序",Toast.LENGTH_LONG).show();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        finish();
//                    }
//                },3000);
//            }
//        }
//        mainDialogPresenter.checkVersion(new SimpleHashMapBuilder<>().puts("platform", "1").puts(Functions.FUNCTION, "6000"));

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                GiftDialog.newInstance().setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        mainDialogPresenter.checkMessageDialog();
//                    }
//                }).show(getSupportFragmentManager(),"礼物箱子");
//            }
//        },2000);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                checkClipContent();
//            }
//        }, 500);
    }

    private void checkLocReal() {
        new ChangeVipPresenter(this).changeVip(new SimpleHashMapBuilder<>());

    }

    //检查剪切板口令 模拟 帮朋友砍价
    private void checkClipContent() {
//        String result= ClipboardUtils.getClipeBoardContent(mContext);
//        if(!TextUtils.isEmpty(result)){
//            if(result.contains("憨妈妈砍价")){
//                result=result.replace("快来捡漏【憨妈妈砍价】复制这条信息后，打开憨妈妈 hmmm:bargaining?","").trim();
////                result= AesEncryptUtil.decrypt(result,SpKey.KICKPASSWORD);
//                result="hmmpass://hmm/goodsBargain?"+result;
//                Toast.makeText(mContext,result,Toast.LENGTH_LONG).show();
//                Uri uri=Uri.parse(result.replace("hmmpass","hmmpassimp").replace("/goodsBargain","/discount/kickHelpDetail"));
//                try {
//                    ARouter.getInstance()
//                            .build(uri)
//                            .navigation();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                ClipboardUtils.clearClipboard(mContext);
//            }
//
//
//        }

    }


    public void showSmallGiftDialog(String messageData) {
        GetGiftDialog.newInstance().setButtonlickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGetCouponDialog(messageData);
            }
        }).show(getSupportFragmentManager(), "礼物");
    }

    public void showGetCouponDialog(String messageData) {
        GetCouponDialog.newInstance().setListString(messageData).show(getSupportFragmentManager(), "礼物");
    }

    @Override
    public void onSuccessGetCouponList(List<Coupon> coupons, PageInfoEarly pageInfoEarly) {

    }

    public synchronized void checkActGift() {
        if (adDialogView == null) {
            adPresenter.getAd(new SimpleHashMapBuilder<String, Object>()
                    .puts("type", "1")
                    .puts("triggerPage", "23")
                    .puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                    .puts("ytbAppId", LocUtil.getytbAppId())
                    .puts("departId", LocUtil.getHmmDepartId()));
        }
    }

    @Override
    public void onSucessGiftCheck(int result) {
        if (result == -1) {//说明出错了 默认为没有这个礼包 一般新人不会这个礼包出错出错
            SpUtils.store(mContext, SpKey.GIFT_ALREADYSHOW, true);
            checkActGift();
        } else {
            if (result == 0) {//说明未发
                checkGiftRealHas();
//                mainDialogPresenter.getCouponList(new SimpleHashMapBuilder().puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));//获得新人大礼包
//                mainDialogPresenter.updateGift(new SimpleHashMapBuilder().puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));//修改状态 先不修改状态
            } else {
                SpUtils.store(mContext, SpKey.GIFT_ALREADYSHOW, true);
                mainDialogPresenter.checkMessageDialog();
            }
        }
    }

    String mreferralCode;

    private void checkGiftRealHas() {
        ShareTrace.getInstallTrace(new ShareTraceInstallListener() {
            int count = 0;

            @Override
            public void onInstall(AppData data) {//这里说明获取到分享下载的导购码
                if (data != null && count != 1) {
                    count++;
                    String result = data.toString();
                    if (result == null) {
                        return;
                    }
                    if (result.contains("paramsData") && result.contains("referral_code")) {//这里说明获取到分享下载的导购码格式正确
                        Map<String, String> map = new HashMap<>();
                        String[] resultarray = data.getParamsData().split("&");
                        for (int i = 0; i < resultarray.length; i++) {
                            map.put(resultarray[i].split("=")[0], resultarray[i].split("=").length > 1 ? resultarray[i].split("=")[1] : "");
                        }
                        String referralCode = map.get("referral_code");
                        mreferralCode = referralCode;
                        String birthday = SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_BIRTHDAY);
                        String partnerId = SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_PARTNERID);
                        String ytbAppId = LocUtil.getytbAppId();
                        String departId = LocUtil.getHmmDepartId();
                        if (!ChannelUtil.isRealRelease()) {
                            Toast.makeText(LibApplication.getAppContext(), "获得了导购码:" + referralCode, Toast.LENGTH_SHORT).show();
                        }
                        mainDialogPresenter.checkCouponGiftHasCard(new SimpleHashMapBuilder<String, Object>()
                                .puts("referralCode", referralCode)
                                .puts("birthday", birthday)
                                .puts("ytbAppId", ytbAppId)
                                .puts("departId", departId)
                                .puts("partnerId", partnerId)
                                .puts("memberId", new String(Base64.decode(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
                    } else {
                        String referralCode = null;
                        String birthday = SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_BIRTHDAY);
                        String partnerId = SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_PARTNERID);
                        String ytbAppId = LocUtil.getytbAppId();
                        String departId = LocUtil.getHmmDepartId();
                        mainDialogPresenter.checkCouponGiftHasCard(new SimpleHashMapBuilder<String, Object>()
                                .puts("referralCode", referralCode)
                                .puts("birthday", birthday)
                                .puts("ytbAppId", ytbAppId)
                                .puts("departId", departId)
                                .puts("partnerId", partnerId)
                                .puts("memberId", new String(Base64.decode(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
                    }
                }
            }

            @Override
            public void onError(int code, String msg) {
                String referralCode = null;
                String birthday = SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_BIRTHDAY);
                String ytbAppId = LocUtil.getytbAppId();
                String departId = LocUtil.getHmmDepartId();
                mainDialogPresenter.checkCouponGiftHasCard(new SimpleHashMapBuilder<String, Object>()
                        .puts("referralCode", referralCode)
                        .puts("birthday", birthday)
                        .puts("ytbAppId", ytbAppId)
                        .puts("departId", departId)
                        .puts("memberId", new String(Base64.decode(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
            }
        });

    }

    @Override
    public void onSucessGiftCheckHasCard(List<Coupon> coupons) {
        if (coupons != null && coupons.size() > 0) {
            showGiftDialog();
        } else {
            checkActGift();
        }
    }

    @Override
    public void onSucessMessageCheck(boolean result) {
        if (!result) {
            MessageDialog.newInstance().show(getSupportFragmentManager(), "消息提醒");
        } else {
            checkActGift();
        }
    }

    @Override
    public void onSucessUpdateGift(String result) {

    }

    @Override
    public void onSucessGetAppProgram(ShareEntity shareEntity) {
        //String schemeUrl = "hmmm://hmm/corresponding?";
        if (shareEntity == null) {
            return;
        }
        mStringBuilder.setLength(0);
        mStringBuilder.append("hmmm://hmm/corresponding?");
        Map<String, String> shareMap = shareEntity.params;
        if (shareMap != null) {
            System.out.println("有跳转意图");

            //包含androidLinkName的二维码意图  直接跳转 不用往后走
            if ("ShareQRCode".equals(shareMap.get("scheme"))&&!TextUtils.isEmpty(shareMap.get("androidLinkName"))) {
                MARouterUtils.passToTarget(mContext, shareMap.get("androidLinkName"));
                return;
            }

            for (Map.Entry<String, String> entry : shareMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                //schemeUrl += "" + key + "=" + value + "&";
                mStringBuilder.append(key).append("=").append(value).append("&");
            }
            String schemeUrl = mStringBuilder.toString();
            if (schemeUrl.length() > 1) {
                schemeUrl = schemeUrl.substring(0, schemeUrl.length() - 1);
            }
            Intent intent = new Intent();
            Uri content_url = Uri.parse(schemeUrl);
            intent.setData(content_url);
            checkScheme(intent);
        }
    }

    @Override
    public void onSucessCheckVersion(UpdateVersion result) {
        if (result != null) {
            //System.out.println("需要更新2" + result.version);
            SpUtils.store(mContext, SpKey.USE_UPDATE, new Gson().toJson(result));
            if (result.getVersionCode() > com.health.client.BuildConfig.VERSION_CODE) {
                //System.out.println("需要更新");
                long mDownloadId = SpUtils.getValue(mContext, SpKey.USE_DOWN, 0L);
                boolean isTodayShow = SpUtils.getValue(mContext, new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "推荐", false);
                if (result.forceUpate == 1) {//如果是强制 则每次都弹出 只判断是不是在下载中
                    DownLoadFragment.newInstance(new Gson().toJson(result)).show(getSupportFragmentManager(), "升级弹窗");
                } else {//非强制判断下今日弹出次数
                    if (!isTodayShow) {
                        DownLoadFragment.newInstance(new Gson().toJson(result)).show(getSupportFragmentManager(), "升级弹窗");
                    }
                }
            }
        }
    }

    @Override
    public void onGetCodeInfoSuccess(ZxingReferralCodeModel model) {
        if (model != null) {
            zxingScanPresenter.binding(model.referralCode, model.merchantId, "1", "2");
        } else {
            //System.out.println("首页获取下载导购二维码信息失败");
        }
    }

    @Override
    public void onBindingSuccess(String result) {
        SpUtils.store(mContext, "referralCodeInit", true);
    }

    @Override
    public void onGetTokerWorkerInfoSuccess(TokerWorkerInfoModel model) {
        if (model != null) {
            if (model.tokerWorker != null) {
                SpUtils.store(mContext, SpKey.GETTOKEN, model.tokerWorker.referralCode);
                SpUtils.store(mContext, "referralCode", model.tokerWorker.referralCode);
            }
        }
    }

    @Override
    public void ticketCoupon() {

    }

    @Override
    public String getToken() {
        return SpUtils.getValue(mContext, SpKey.LIVETMPCOURSEADDRESS);
    }

    @Override
    public String getCourseId() {
        return SpUtils.getValue(mContext, SpKey.LIVETMPCOURSEID);
    }

    @Override
    public String getLiveStatus() {
        return "2";
    }

    @Override
    public Handler getVideoHandler() {
        return null;
    }

    @Override
    public void onSucessGetAds(List<AdModel> adModels) {
        if (adModels.size() > 0) {//待改造
            final AdModel adModel = adModels.get(0);
            if (adModel.type == 1 && adModel.triggerPage == 23) {
                final String limitTime = adModel.validityEnd;//广告有效时间
                boolean isTimeEndBefore = false;
                try {
                    isTimeEndBefore = new Date().before(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(limitTime));
                } catch (Exception e) {
                    isTimeEndBefore = true;
                    e.printStackTrace();
                }
                if ("1".equals(adModel.isReceive) || !isTimeEndBefore) {//已领取直接返回 不生效时间
                    return;
                }
                String mAdKey = "Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage + adModel.cardGiftType + "_" + adModel.codeName;
                String mUserGiftJson = SpUtils.getValue(mContext, mAdKey);
                JsonObject jsonObject = null;
                int count = 1;
                String time = DateUtils.long2Str(System.currentTimeMillis(), false);
                boolean isShow = true;// 当天次数不为3可弹出
                //Log.e("MainAc", "onSucessGetAds: " + mUserGiftJson);
                try {
                    if (!TextUtils.isEmpty(mUserGiftJson)) {
                        JSONObject object = new JSONObject(mUserGiftJson);
                        time = object.getString("time");
                        String mCurrentTime = DateUtils.long2Str(System.currentTimeMillis(), false);
                        count = object.getInt("showCount");
                        if (mCurrentTime.equals(time)) {
                            count++;
                            isShow = count != 4;
                        } else {
                            time = DateUtils.long2Str(System.currentTimeMillis(), false);
                            count = 1;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (count < 4) {
                    jsonObject = new JsonObject();
                    jsonObject.addProperty("time", time);
                    jsonObject.addProperty("showCount", count);
                    String json = jsonObject.toString();
                    SpUtils.store(mContext, mAdKey, json);
                }
                if (isShow) {
                    System.out.println("需要展示广告:" + "Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage);
                    com.healthy.library.businessutil.GlideCopy.with(this)
                            .load(adModel.photoUrls)

                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    super.onLoadFailed(errorDrawable);
                                }

                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    if (adDialogView == null) {
                                        adDialogView = ADDialogView.newInstance().setAdModel(adModel);
                                        adDialogView.show(getSupportFragmentManager(), "广告");
                                    }
                                    //SpUtils.store(mContext, "Ad" + "_" + adModel.id + "_" + adModel.type + "_" + adModel.triggerPage + adModel.cardGiftType + "_" + adModel.codeName + "&" + showTime, true);
                                }
                            });
                }
            }
        }
    }

}
