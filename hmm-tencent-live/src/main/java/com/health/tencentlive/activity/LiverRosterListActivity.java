package com.health.tencentlive.activity;

import android.os.Bundle;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.tencentlive.R;
import com.health.tencentlive.adapter.RosterListFragmentAdapter;
import com.health.tencentlive.contract.LiveRosterListContract;
import com.health.tencentlive.model.RosterList;
import com.health.tencentlive.presenter.LiveRosterListPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

@Route(path = TencentLiveRoutes.LIVERROSTERLIST)
public class LiverRosterListActivity extends BaseActivity implements IsFitsSystemWindows,
        OnRefreshLoadMoreListener, LiveRosterListContract.View, BaseAdapter.OnOutClickListener {

    private TopBar topBar;
    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerview;
    private RosterListFragmentAdapter rosterListFragmentAdapter;
    private int pageNum = 1;
    private LiveRosterListPresenter liveRosterListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_liver_roster_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        liveRosterListPresenter = new LiveRosterListPresenter(this, this);
        rosterListFragmentAdapter = new RosterListFragmentAdapter();
        rosterListFragmentAdapter.setOutClickListener(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(rosterListFragmentAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void findViews() {
        super.findViews();
        topBar = (TopBar) findViewById(R.id.top_bar);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        refreshLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    public void getData() {
        super.getData();
        liveRosterListPresenter.getRosterList(new SimpleHashMapBuilder<String, Object>());
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
    public void getSuccessRosterList(List<RosterList> result, PageInfoEarly pageInfo) {
        rosterListFragmentAdapter.clear();
        showContent();
        if (pageInfo == null) {
            showEmpty();
            refreshLayout.setEnableLoadMore(false);
            return;
        }
        if (pageNum == 1) {
            if (result == null || result.size() == 0) {//没助力的状态
                rosterListFragmentAdapter.setModel(null);
                showEmpty();
            } else {
                rosterListFragmentAdapter.setData((ArrayList) result);
            }
        } else {
            rosterListFragmentAdapter.addDatas((ArrayList) result);
        }
        if (pageInfo.isMore != 1) {
            refreshLayout.setNoMoreData(true);
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(false);
        }
    }

    @Override
    public void getSuccessCoupon(String result, String couponName) {
        if (result.contains("成功")) {
            showToast(String.format("您的奖品%s已发放到我的-优惠券", couponName));
            getData();
        }
    }

    @Override
    public void onSuccessAddGift(String result, String couponName) {
        if (result.contains("成功")) {
            showToast(String.format("您的奖品%s已发放到我的-优惠券", couponName));
            getData();
        }
    }

    @Override
    public void outClick(@NonNull String function, @NonNull Object obj) {
        if (function.equals("coupon")) {
            RosterList.LiveBenefitAllItemDtosBean bean = (RosterList.LiveBenefitAllItemDtosBean) obj;
            if (bean.liveActivityType == 1) {//1助力
                liveRosterListPresenter.getCoupon(new SimpleHashMapBuilder<String, Object>()
                        .puts("helpId", bean.helpId)
                        .puts("helpItemId", bean.helpItemId)
                        .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                        .puts("courseId", bean.courseId), bean.detail.basic.getCouponName());
            } else if (bean.liveActivityType == 3) {//红包
                liveRosterListPresenter.addGift(new SimpleHashMapBuilder<String, Object>().puts("liveBenefitId", bean.benefitId), bean.detail.basic.getCouponName());
            }

        }

        if (function.equals("goods")) {
            buildGoods((RosterList.LiveBenefitAllItemDtosBean) obj);
        }
    }

    private void buildGoods(final RosterList.LiveBenefitAllItemDtosBean bean) {

        if (bean.detail.availableInventory <= 0) {//已抢光
            showToast("当前奖品已抢光");
            return;
        }
        GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(null);
        goodsMarketing.availableInventory = bean.detail.availableInventory;
        goodsMarketing.mapMarketingGoodsId = "";
        goodsMarketing.marketingType = "-5";
        goodsMarketing.id = "";
        goodsMarketing.pointsPrice = 0;
        GoodsBasketCell goodsBasketCell = new GoodsBasketCell(bean.detail.platformPrice, bean.detail.platformPrice,
                bean.detail.platformPrice,
                bean.detail.goodsType,
                "0",
                "0", null);
        goodsBasketCell.goodsSpecDesc = bean.detail.goodsSpecStr;
        goodsBasketCell.goodsStock = bean.detail.availableInventory;
        goodsBasketCell.goodsMarketingDTO = goodsMarketing;
        goodsBasketCell.mchId = bean.detail.merchantId;
        goodsBasketCell.goodsId = bean.detail.goodsId;
        try {
            goodsBasketCell.setGoodsSpecId(bean.detail.goodsChildId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        goodsBasketCell.goodsTitle = bean.detail.goodsTitle;
        goodsBasketCell.goodsImage = bean.detail.goodsImage;
        goodsBasketCell.setGoodsQuantity(1);
        goodsBasketCell.shopIdList = bean.detail.shopIds;
        goodsBasketCell.goodsShopId = "";
        List<GoodsBasketCell> list = new ArrayList<>();
        list.add(goodsBasketCell);

        ARouter.getInstance()
                .build(ServiceRoutes.SERVICE_ORDER)
                .withString("visitShopId", bean.detail.shopId)
                .withString("token", null)
                .withString("course_id", null)
                .withString("liveStatus", null)
                .withString("live_anchor", null)
                .withString("live_course", null)
                .withObject("goodsbasketlist", list)
                .withString("goodsMarketingType", "-5")
                .withString("winType", bean.liveActivityType + "")
                .withString("winId", bean.winId)
                .navigation();
    }
}