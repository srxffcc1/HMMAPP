package com.health.discount.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.health.discount.R;
import com.health.discount.contract.CardCenterActivityContract;
import com.health.discount.fragment.CardCenterFragment;
import com.health.discount.model.ChannelModel;
import com.health.discount.presenter.CardCenterActivityPresenter;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.AdContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.loader.ImageNetAdapter;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.ColorInfo;
import com.healthy.library.presenter.AdPresenter;
import com.healthy.library.presenter.AdPresenterCopy;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.DragLayout;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = DiscountRoutes.DIS_CARDCENTER)
public class CardCenterActivity extends BaseActivity implements IsFitsSystemWindows, CardCenterActivityContract.View, OnRefreshLoadMoreListener, AdContract.View {

    @Autowired
    String merchantId;

    private com.healthy.library.widget.TopBar topBar;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout refreshLayout;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.google.android.material.appbar.AppBarLayout appBar;
    private com.google.android.material.appbar.CollapsingToolbarLayout collapsingToolbarLayout;
    private androidx.constraintlayout.widget.ConstraintLayout bannerHLL;
    private com.youth.banner.Banner actTopBanner;
    private android.widget.LinearLayout underLL;
    private android.widget.RelativeLayout tabLL;
    private com.flyco.tablayout.SlidingTabLayout st;
    private androidx.viewpager.widget.ViewPager vp;
    private com.healthy.library.widget.DragLayout dargF;
    private List<Fragment> fragments = new ArrayList<>();
    CanReplacePageAdapter fragmentPagerItemAdapter;
    private CardCenterActivityPresenter cardCenterActivityPresenter;
    AdPresenter adPresenter;
    private long mills = System.currentTimeMillis();

    @Override
    protected int getLayoutId() {
        return R.layout.dis_activity_cardcenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if(TextUtils.isEmpty(merchantId)){
            merchantId = SpUtils.getValue(mContext, SpKey.CHOSE_MC);
        }
        cardCenterActivityPresenter = new CardCenterActivityPresenter(this, this);
        adPresenter = new AdPresenter(this, this);
        getData();
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void getData() {
        super.getData();
        cardCenterActivityPresenter.getList(new SimpleHashMapBuilder<String, Object>()
        .puts("merchantId",merchantId));

        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>()
                .puts("type", "2")
                .puts("triggerPage", "21")
                .puts("advertisingArea", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE)));
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void buildBannerView(Banner banner, final List<AdModel> bannerimgs) {
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "各轮播图、广告图曝光量");
        MobclickAgent.onEvent(mContext, "event2APPShopBannerShow", nokmap);
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
                    e.printStackTrace();
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
                    .setIndicatorGravity(IndicatorConfig.Direction.RIGHT);//设置指示器位置（左，中，右）
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(Object data, int position) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "各轮播图、广告图曝光量");
                    MobclickAgent.onEvent(mContext, "event2APPShopBannerShow", nokmap);
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

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        bannerHLL = (ConstraintLayout) findViewById(R.id.bannerHLL);
        actTopBanner = (Banner) findViewById(R.id.act_top_banner);
        underLL = (LinearLayout) findViewById(R.id.underLL);
        tabLL = (RelativeLayout) findViewById(R.id.tabLL);
        st = (SlidingTabLayout) findViewById(R.id.st);
        vp = (ViewPager) findViewById(R.id.vp);
        dargF = (DragLayout) findViewById(R.id.dargF);
        ImageView mall_scrollUp = (ImageView) findViewById(R.id.mall_scrollUp);
        mall_scrollUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("type", "我的优惠券点击量");
                MobclickAgent.onEvent(mContext, "event2APPMyCardCenterClick", nokmap);
                ARouter.getInstance().build(DiscountRoutes.DIS_COUPONLIST).navigation();
            }
        });
        ImageView shareBuddy = findViewById(R.id.share_buddy);
        shareBuddy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeckShareDialog dialog = SeckShareDialog.newInstance();
                dialog.setActivityDialog(3, 6, null);
                dialog.show(getSupportFragmentManager(), "分享");
            }
        });
    }

    @Override
    public void onSucessGetList(List<ChannelModel> result) {
        if (result == null || result.size() == 0) {
            return;
        }
        fragments.clear();
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            titles.add(result.get(i).name);
            fragments.add(CardCenterFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("id", result.get(i).id + "")));
        }
        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new CanReplacePageAdapter(getSupportFragmentManager(), fragments, titles);
            // adapter
            vp.setAdapter(fragmentPagerItemAdapter);
        }
        try {
            vp.setOffscreenPageLimit(fragments != null ? fragments.size() : 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentPagerItemAdapter.notifyDataSetChanged();
        // bind to SmartTabLayout
        st.setViewPager(vp);
        showContent();
//        buildBannerView(actTopBanner, new SimpleArrayListBuilder<AdModel>().adds(new AdModel("")));
        st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "顶部tab点击量/滑屏切换量");
                MobclickAgent.onEvent(mContext, "event2APPCardCenterTabClick", nokmap);
            }

            @Override
            public void onTabReselect(int position) {

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
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onSucessGetAds(List<AdModel> adModels) {
        bannerHLL.setVisibility(View.GONE);
        if (adModels != null && adModels.size() > 0) {
            bannerHLL.setVisibility(View.VISIBLE);
            buildBannerView(actTopBanner,adModels);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "领券中心页浏览时长");
        nokmap.put("time", (System.currentTimeMillis() - mills) / 1000 + "秒");
        MobclickAgent.onEvent(mContext, "event_APP_CardCenter_Stop", nokmap);
    }
}
