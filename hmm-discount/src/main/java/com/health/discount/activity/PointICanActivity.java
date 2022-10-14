package com.health.discount.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.health.discount.BuildConfig;
import com.health.discount.R;
import com.health.discount.adapter.PointIcanAdapter;
import com.health.discount.contract.PointIcanContract;
import com.health.discount.model.PointGoodsList;
import com.health.discount.presenter.PointIcanPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.IntegralModel;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Route(path = DiscountRoutes.DIS_POINTICAN)
public class PointICanActivity extends BaseActivity implements IsFitsSystemWindows,OnRefreshLoadMoreListener, PointIcanContract.View {

    private PointIcanAdapter pointIcanAdapter;
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
    private androidx.recyclerview.widget.RecyclerView recyclerPoint;
    private ImageTextView userPonitLeft;
    private int sortType = 1;//1综合 2销量 3积分
    private int sort = 2;//1正序 2倒序  默认倒序
    private Map<String, Object> map = new HashMap<>();
    private int pageNum = 1;
    private String SYJFTOT = null;
    private PointIcanPresenter pointIcanPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.dis_activity_point_ican;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        pointIcanPresenter = new PointIcanPresenter(this, this);
        recyclerPoint.setLayoutManager(new LinearLayoutManager(this));
        pointIcanAdapter = new PointIcanAdapter();
        recyclerPoint.setAdapter(pointIcanAdapter);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        pointIcanPresenter.getVipInfo();
    }

    @Override
    public void getData() {
        super.getData();
        map.put("pageNum", pageNum + "");
        map.put("pageSize", "10");
        map.put("integral", SYJFTOT);
        if(BuildConfig.DEBUG){
            map.put("integral", "99999");
        }
        map.put("sortType", sortType + "");
        map.put("sort", sort + "");
        map.put("filterSoldOut",  "0");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        pointIcanPresenter.getPointGoodsList(map);
    }

    @Override
    public void onVipInfoSuccess(IntegralModel vipInfo) {
        if (vipInfo != null) {
            if (vipInfo.SYJFTOT != null && !TextUtils.isEmpty(vipInfo.SYJFTOT)) {
                SYJFTOT = vipInfo.SYJFTOT;
                userPonitLeft.setText(vipInfo.SYJFTOT + "");
                changeType(1);
            } else {
                userPonitLeft.setText("0");
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pointIcanPresenter != null) {
            pointIcanPresenter.getVipInfo();
        }
    }

    @Override
    public void onSucessGetList(List<PointGoodsList> result) {
        if (pageNum == 1) {
            if (result == null || result.size() == 0) {
                showEmpty();
                layoutRefresh.finishLoadMoreWithNoMoreData();
            } else {
                pointIcanAdapter.setData((ArrayList) result);
            }
        } else {
            if (result == null || result.size() == 0) {
                layoutRefresh.finishLoadMoreWithNoMoreData();
            } else {
                pointIcanAdapter.addDatas((ArrayList) result);
            }
        }
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
        recyclerPoint = (RecyclerView) findViewById(R.id.recycler_point);
        userPonitLeft = findViewById(R.id.userPonitLeft);
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

}
