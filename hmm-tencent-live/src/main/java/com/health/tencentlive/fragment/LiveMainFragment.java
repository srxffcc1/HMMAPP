package com.health.tencentlive.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.health.servicecenter.widget.GoodsSimpleDialog;
import com.health.tencentlive.R;
import com.health.tencentlive.contract.LiveMainContract;
import com.health.tencentlive.weight.LiveHelpMarketingDialog;
import com.health.tencentlive.weight.LiveHelpMarketingTipsDialog;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.constant.Functions;
import com.healthy.library.interfaces.OnDialogButtonListener;
import com.healthy.library.model.AnchormanInfo;
import com.health.tencentlive.presenter.LiveMainPresenter;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.AdContract;
import com.healthy.library.loader.ImageNetAdapter;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.ColorInfo;
import com.healthy.library.presenter.AdPresenter;
import com.healthy.library.presenter.AdPresenterCopy;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.NoScrollViewPager;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(path = TencentLiveRoutes.LIVE_Module)
public class LiveMainFragment extends BaseFragment implements AdContract.View, OnRefreshLoadMoreListener, LiveMainContract.View {


    private TopBar topBar;
    private ImageTextView topTitle;
    private ImageView homeAnimImg;
    private TextView topRoom;
    private SmartRefreshLayout layoutRefresh;
    private AppBarLayout appBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Banner actBanner;
    private LinearLayout fun1;
    private LinearLayout fun2;
    private LinearLayout fun3;
    private LinearLayout fun4;
    private NoScrollViewPager vp;
    private SlidingTabLayout st;
    private List<Fragment> fragments = new ArrayList<>();
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    int verticalOffsetold = 0;
    private LinearLayout stLL;
    private AdPresenter adPresenter;
    private LiveMainPresenter liveMainPresenter;
    private List<String> titles = Arrays.asList("热门直播", "好物秒杀", "产后护理", "育儿教学", "憨妈学堂");
    private LiveMainBodyFragment mCurrentFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_main;
    }

    @Override
    protected void findViews() {
        initView();
        adPresenter = new AdPresenter(mContext, this);
        liveMainPresenter = new LiveMainPresenter(mContext, this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateUserLocationMsg msg) {
        if (!isFirstLoad) return;
        getData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onLazyData() {
        super.onLazyData();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "22"));
        liveMainPresenter.getActLocVip(new SimpleHashMapBuilder<String, Object>());
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        topTitle = (ImageTextView) findViewById(R.id.topTitle);
        topRoom = (TextView) findViewById(R.id.topRoom);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        actBanner = (Banner) findViewById(R.id.act_banner);
        fun1 = (LinearLayout) findViewById(R.id.fun1);
        fun2 = (LinearLayout) findViewById(R.id.fun2);
        fun3 = (LinearLayout) findViewById(R.id.fun3);
        fun4 = (LinearLayout) findViewById(R.id.fun4);
        vp = (NoScrollViewPager) findViewById(R.id.vp);
        st = (SlidingTabLayout) findViewById(R.id.st);
        stLL = (LinearLayout) findViewById(R.id.stLL);
        homeAnimImg = (ImageView) findViewById(R.id.homeAnimImg);
        findViewById(R.id.live_main_gift_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LIVERROSTERLIST)
                        .navigation();
            }
        });
        AnimationDrawable anim = (AnimationDrawable) homeAnimImg.getBackground();// 获取到动画资源
        anim.setOneShot(false); // 设置是否重复播放
        anim.start();// 开始动画

        for (int i = 0; i < titles.size(); i++) {
            if (i == 0) {
                fragments.add(LiveMainBodyFragment.newInstance(
                        new SimpleHashMapBuilder<String, Object>().puts("emptyType", "0").puts("refresh", "0").puts("statusList", "1,2,4").puts("courseTitle", titles.get(i))));
            } else {
                fragments.add(LiveMainBodyFragment.newInstance(
                        new SimpleHashMapBuilder<String, Object>().puts("emptyType", "0").puts("refresh", "0").puts("statusList", "1,2,4").puts("courseTitle", titles.get(i)).puts("category", (i) + "")));
            }

        }
        mCurrentFragment = (LiveMainBodyFragment) fragments.get(0);
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
        vp.setCurrentItem(0);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentFragment = (LiveMainBodyFragment) fragments.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {


                if (verticalOffset != verticalOffsetold) {
                    float alpha = Math.min((stLL.getHeight() / 2 - (appBarLayout.getHeight() + verticalOffset - collapsingToolbarLayout.getMinimumHeight())) * 1.0f / (stLL.getHeight() / 2), 1);
                    stLL.setAlpha(alpha);
                    if (alpha >= 1) {
                        stLL.setVisibility(View.VISIBLE);
                    } else {
                        stLL.setVisibility(View.GONE);
                    }
                }
                verticalOffsetold = verticalOffset;


            }
        });
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        layoutRefresh.setEnableLoadMore(false);
        topRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GoodsSimpleDialog goodsSimpleDialog = new GoodsSimpleDialog();
//                goodsSimpleDialog
//                        .setErrMsg("等待主播开抢")
//                        .setSeachMap(new SimpleHashMapBuilder<String, Object>().puts(Functions.FUNCTION, "9292").puts("liveGoodsId", "548"))
//                        .show(getChildFragmentManager(), "商品弹窗");
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveHostROOM)
                        .navigation();
            }
        });
        fun1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveMainBody)
                        .withString("category", "1")
                        .withString("categoryName", "好物秒杀")
                        .navigation();
            }
        });
        fun2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveMainBody)
                        .withString("category", "2")
                        .withString("categoryName", "产后护理")
                        .navigation();
            }
        });
        fun3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveMainBody)
                        .withString("category", "3")
                        .withString("categoryName", "育儿教学")
                        .navigation();
            }
        });
        fun4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveMainBody)
                        .withString("category", "4")
                        .withString("categoryName", "憨妈学堂")
                        .navigation();
            }
        });

        showContent();
    }

    private void buildBannerView(Banner banner, final List<AdModel> bannerimgs) {
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
            banner.setAdapter(imageLoader)
                    .setIndicator(new RectangleIndicator(mContext))
                    .setIndicatorGravity(IndicatorConfig.Direction.CENTER);//设置指示器位置（左，中，右）
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(Object data, int position) {
                    AdModel adModel = bannerimgs.get(position);
                    MARouterUtils.passToTarget(mContext,adModel);
                }

                @Override
                public void onBannerChanged(int position) {

                }
            });
            //System.out.println("修改背景版333");
//            banner.setIndicator(new CircleIndicator(mContext));
            banner.stop();
            banner.start();

        }
    }

    @Override
    public void onSucessGetAds(List<AdModel> adModels) {
        actBanner.setVisibility(View.GONE);
        if (adModels.size() > 0) {
            actBanner.setVisibility(View.VISIBLE);
            buildBannerView(actBanner, adModels);
        }
        showContent();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
        try {
            mCurrentFragment.onRefresh(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onRequestFinish();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Override
    public void onSucessGetHost(AnchormanInfo archinfo) {
        if (archinfo != null) {
            SpUtils.store(mContext, SpKey.LIVEHOSTID, archinfo.id);
            topRoom.setVisibility(View.VISIBLE);
        } else {
            SpUtils.store(mContext, SpKey.LIVEHOSTID, "");
//            topRoom.setVisibility(View.VISIBLE);
            topRoom.setVisibility(View.GONE);
        }
    }
}
