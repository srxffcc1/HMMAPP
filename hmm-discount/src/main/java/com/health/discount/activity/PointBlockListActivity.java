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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.health.discount.R;
import com.health.discount.adapter.PointBlockHomeRecommendAdapter;
import com.health.discount.contract.PointThemeContract;
import com.health.discount.model.PointTab;
import com.health.discount.presenter.PointThemePresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.ObjectIteraor;
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
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.TransformUtil;
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

/**
 *
 */
@Route(path = DiscountRoutes.DIS_POINTBLOCK)
public class PointBlockListActivity extends BaseActivity implements IsFitsSystemWindows, OnRefreshLoadMoreListener, PointThemeContract.View, AdContract.View {
    @Autowired
    String themeId;
    @Autowired
    String themeIdName;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private com.google.android.material.appbar.AppBarLayout appBar;
    private com.google.android.material.appbar.CollapsingToolbarLayout collapsingToolbarLayout;
    private com.youth.banner.Banner actTopBanner;
    private android.widget.LinearLayout underLL;
    private android.widget.RelativeLayout tabLL;
    private android.widget.LinearLayout sortLiner;
    private android.widget.TextView txtDefault;
    private android.widget.TextView txtSalesVolume;
    private android.widget.TextView txtPrice;
    private android.widget.ImageView priceUpImg;
    private android.widget.ImageView priceDownImg;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private androidx.recyclerview.widget.RecyclerView recyclerQuestion;
    private TopBar top_bar;
    private int sortType = 1;//1综合 2销量 3积分
    private int sort = 2;//1正序 2倒序
    private Map<String, Object> map = new HashMap<>();
    private int pageNum = 1;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private PointBlockHomeRecommendAdapter pointBlockHomeRecommendAdapter;
    //    private PointBlockHomeTopBannerAdapter pointBlockHomeTopBannerAdapter;
    private PointThemePresenter pointThemePresenter;
    private AdPresenter adPresenter;
    private ImageView topIcon;

    @Override
    protected int getLayoutId() {
        return R.layout.dis_activity_point_block;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        pointThemePresenter = new PointThemePresenter(this, this);
        adPresenter = new AdPresenter(this, this);
        top_bar.setTitle(themeIdName);
        changeType(1);
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
        buildRecyclerView();
    }

    @Override
    public void getData() {
        super.getData();
//        adPresenter.getAd(new SimpleHashMapBuilder<String, Object>().puts("type", "2").puts("triggerPage", "19").puts("advertisingArea", LocUtil.getAreaNo(this, SpKey.LOC_CHOSE)));
        map.put("pageNum", pageNum + "");
        map.put("themeId", themeId);
        map.put("pageSize", "10");
        map.put("sortType", sortType + "");
        map.put("sort", sort + "");
        map.put("filterSoldOut", "0");
        pointThemePresenter.getGoodsList(map);
    }

    @Override
    public void onSucessGetList(PointTab bean) {
        topIcon.setVisibility(View.GONE);
        if(bean!=null){
            if(!TextUtils.isEmpty(bean.iconUrl)){
                topIcon.setVisibility(View.VISIBLE);
                com.healthy.library.businessutil.GlideCopy.with(mContext)
                        .load(bean.iconUrl)
                        .error(R.drawable.img_1_1_default)
                        .placeholder(R.drawable.img_1_1_default2)
                        .into(topIcon);
//                        .into(new SimpleTarget<Drawable>() {
//                            @Override
//                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                int swidth = ScreenUtils.getScreenWidth(mContext);
//                                int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);
//                                ViewGroup.LayoutParams layoutParams= (ViewGroup.LayoutParams) topIcon.getLayoutParams();
//                                layoutParams.height=height;
//                                layoutParams.width=swidth;
//                                topIcon.setLayoutParams(layoutParams);
//                                topIcon.setImageDrawable(resource);
//                            }
//                        });
            }

            List<PointTab.PointGoods> result=bean.goodsList;
            if (pageNum == 1) {
                if (result == null || result.size() == 0) {
                    showEmpty();
                    layoutRefresh.finishLoadMoreWithNoMoreData();
                } else {
                    pointBlockHomeRecommendAdapter.setData((ArrayList) result);
                }
            } else {
                if (result == null || result.size() == 0) {
                    layoutRefresh.finishLoadMoreWithNoMoreData();
                } else {
                    pointBlockHomeRecommendAdapter.addDatas((ArrayList) result);
                }
            }
        }

    }

    private void buildRecyclerView() {
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerQuestion.setLayoutManager(virtualLayoutManager);
        recyclerQuestion.setAdapter(delegateAdapter);
        pointBlockHomeRecommendAdapter = new PointBlockHomeRecommendAdapter();
        delegateAdapter.addAdapter(pointBlockHomeRecommendAdapter);

    }

    private void initView() {
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        actTopBanner = (Banner) findViewById(R.id.act_top_banner);
        underLL = (LinearLayout) findViewById(R.id.underLL);
        tabLL = (RelativeLayout) findViewById(R.id.tabLL);
        sortLiner = (LinearLayout) findViewById(R.id.sort_liner);
        txtDefault = (TextView) findViewById(R.id.txt_default);
        txtSalesVolume = (TextView) findViewById(R.id.txt_sales_volume);
        txtPrice = (TextView) findViewById(R.id.txt_price);
        priceUpImg = (ImageView) findViewById(R.id.price_up_img);
        priceDownImg = (ImageView) findViewById(R.id.price_down_img);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
        top_bar = findViewById(R.id.top_bar);
        txtDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(1);
            }
        });
        txtSalesVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(2);
            }
        });
        txtPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeType(3);
            }
        });
        topIcon = (ImageView) findViewById(R.id.topIcon);
    }

    public void changeType(int type) {
        priceUpImg.setImageResource(R.mipmap.service_price_sort_black);
        priceDownImg.setImageResource(R.mipmap.service_price_sort_black);
        switch (type) {
            case 1:
                txtDefault.setTextColor(Color.parseColor("#F02846"));
                txtDefault.getPaint().setFakeBoldText(true);
                txtSalesVolume.setTextColor(Color.parseColor("#444444"));
                txtSalesVolume.getPaint().setFakeBoldText(false);
                txtPrice.setTextColor(Color.parseColor("#444444"));
                txtPrice.getPaint().setFakeBoldText(false);
                sort = 2;
                pageNum = 1;
                sortType = 1;
                map.clear();
                if (pointBlockHomeRecommendAdapter != null) {
                    pointBlockHomeRecommendAdapter.clear();
                }
                getData();
                break;
            case 2:
                txtDefault.setTextColor(Color.parseColor("#444444"));
                txtDefault.getPaint().setFakeBoldText(false);
                txtSalesVolume.setTextColor(Color.parseColor("#F02846"));
                txtSalesVolume.getPaint().setFakeBoldText(true);
                txtPrice.setTextColor(Color.parseColor("#444444"));
                txtPrice.getPaint().setFakeBoldText(false);
                sort = 2;
                sortType = 2;
                pageNum = 1;
                map.clear();
                if (pointBlockHomeRecommendAdapter != null) {
                    pointBlockHomeRecommendAdapter.clear();
                }
                getData();
                break;
            case 3:
                txtDefault.setTextColor(Color.parseColor("#444444"));
                txtDefault.getPaint().setFakeBoldText(false);
                txtSalesVolume.setTextColor(Color.parseColor("#444444"));
                txtSalesVolume.getPaint().setFakeBoldText(false);
                txtPrice.setTextColor(Color.parseColor("#F02846"));
                txtPrice.getPaint().setFakeBoldText(true);
                if (sort != 2) {
                    sort = 2;
                    priceUpImg.setImageResource(R.mipmap.service_price_sort_black);
                    priceDownImg.setImageResource(R.mipmap.service_price_sort_red);
                } else {
                    sort = 1;
                    priceUpImg.setImageResource(R.mipmap.service_price_sort_red);
                    priceDownImg.setImageResource(R.mipmap.service_price_sort_black);
                }
                sortType = 3;
                pageNum = 1;
                map.clear();
                if (pointBlockHomeRecommendAdapter != null) {
                    pointBlockHomeRecommendAdapter.clear();
                }
                getData();
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onSucessGetAds(List<AdModel> adModels) {
//        actTopBanner.setVisibility(View.VISIBLE);
//        if (adModels.size() > 0) {
//            buildBannerView(actTopBanner, adModels);
//
//        } else {
//            actTopBanner.setVisibility(View.GONE);
//        }
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
                    colorList.get(j).setPerfectColor(Color.parseColor(bannerimgs.get(j).getColorValue()));
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
    protected void onDestroy() {
        super.onDestroy();
    }
}
