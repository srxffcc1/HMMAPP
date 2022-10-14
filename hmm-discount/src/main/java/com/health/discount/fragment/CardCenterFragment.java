package com.health.discount.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.health.discount.R;
import com.health.discount.adapter.CardCenterFragmentAdapter;
import com.health.discount.contract.CardCenterFragmentContract;
import com.health.discount.contract.CouPonGoodsContract;
import com.health.discount.presenter.CardCenterFragmentPresenter;
import com.health.discount.presenter.CouPonGoodsPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.DragLayout;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardCenterFragment extends BaseFragment implements OnRefreshLoadMoreListener, CardCenterFragmentContract.View, CouPonGoodsContract.View {
    private SmartRefreshLayout layoutRefresh;
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    private DragLayout dargF;
    private CardCenterFragmentAdapter cardCenterListAdapter;
    public int page = 1;
    private CardCenterFragmentPresenter cardCenterFragmentPresenter;
    private Map<String, Object> map = new HashMap<>();

    public static CardCenterFragment newInstance(Map<String, Object> maporg) {
        CardCenterFragment fragment = new CardCenterFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void changeFragmentShow() {
        super.changeFragmentShow();
        if (isfragmenthow) {
            page = 1;
            getData();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dis_fragment_child_coupon;
    }

    @Override
    protected void findViews() {
        initView();
        cardCenterFragmentPresenter = new CardCenterFragmentPresenter(mContext, this);
        cardCenterListAdapter = new CardCenterFragmentAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        cardCenterListAdapter.bindToRecyclerView(recycler);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        layoutRefresh.setEnableRefresh(false);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        map.put("pageNum", page + "");
        map.put("channelId", get("id").toString());
        map.put("pageSize", "10");
        cardCenterFragmentPresenter.getList(map);
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishLoadMore();
        layoutRefresh.finishRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        getData();
    }

    private void initView() {
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        dargF = (DragLayout) findViewById(R.id.dargF);
    }

    @Override
    public void onSucessGetList(List<CouponInfoZ> result, PageInfoEarly pageInfoEarly) {
        showContent();
        if (pageInfoEarly == null) {
            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        for (int i = 0; i < (result != null ? result.size() : 0); i++) {//请求优惠券适用的商品
            CouPonGoodsPresenter couPonGoodsPresenter = new CouPonGoodsPresenter(mContext, this);
            couPonGoodsPresenter.getGoodsList(new SimpleHashMapBuilder<String, Object>().puts("couponId",
                    result.get(i).getCouponId()).puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)), result.get(i));
        }
//        page = pageInfoEarly.pageNum;
        if (page == 1) {
            cardCenterListAdapter.setNewData((ArrayList) result);
            onRequestFinish();
            if (result.size() == 0) {
                showEmpty();
            }
        } else {
            cardCenterListAdapter.addData((ArrayList) result);
            onRequestFinish();
        }
        if (pageInfoEarly.nextPage == 0) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
        cardCenterListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CouponInfoZ couponInfoZ = cardCenterListAdapter.getData().get(position);
                if (couponInfoZ.isCanReceive()) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "立即领取按钮点击量");
                    MobclickAgent.onEvent(mContext, "event2APPCardCenterReceiveClick", nokmap);
                    cardCenterFragmentPresenter.receivedCoupon(couponInfoZ.getActivityId(), couponInfoZ.getCouponId());
                } else {
                    if (couponInfoZ.availableCount > 0) {
                        ARouter.getInstance().build(DiscountRoutes.DIS_PUBLICCOUPON)
                                .withString("cardId", couponInfoZ.getCouponId())
                                .withString("cardName", couponInfoZ.getCouponNormalName())
                                .withString("time", couponInfoZ.getTimeValidity())
                                .navigation();
                    } else {

                        cardCenterFragmentPresenter.receivedCoupon(couponInfoZ.getActivityId(), couponInfoZ.getCouponId());
//                        Toast.makeText(mContext, "不可领取", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }


    @Override
    public void onSucessReceivedCoupon(String msg) {
        if (msg.contains("成功")) {
            showToast("领取成功");
            page = 1;
        }
        getData();
    }

    @Override
    public void onSucessGetList() {
        cardCenterListAdapter.notifyDataSetChanged();
    }
}
