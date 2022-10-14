package com.health.discount.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.discount.R;
import com.health.discount.adapter.VipCardFragmentAdapter;
import com.health.discount.contract.CouponVipListContract;
import com.health.discount.model.CouponVip;
import com.health.discount.presenter.CouponVipListPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.BalanceModel;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public class CouponVipChildFragment extends BaseFragment implements CouponVipListContract.View,
        OnRefreshLoadMoreListener {
    CouponVipListPresenter couponVipListPresenter;
    private SmartRefreshLayout layoutRefresh;
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    VipCardFragmentAdapter vipCardFragmentAdapter;
    public int page = 1;

    private String cardNo;
    private String ytbAppId;

    public static CouponVipChildFragment newInstance(String cardNo, String ytbAppId) {
        CouponVipChildFragment fragment = new CouponVipChildFragment();
        Bundle args = new Bundle();
        args.putString("cardNo", cardNo);
        args.putString("ytbAppId", ytbAppId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cardNo = getArguments().getString("cardNo");
            ytbAppId = getArguments().getString("ytbAppId");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dis_fragment_child_coupon;
    }

    @Override
    protected void findViews() {
        initView();
        vipCardFragmentAdapter = new VipCardFragmentAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        vipCardFragmentAdapter.bindToRecyclerView(recycler);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        couponVipListPresenter = new CouponVipListPresenter(mContext, this);
        getData();
    }

    private void initView() {
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public void getData() {
        super.getData();
        if (cardNo != null && ytbAppId != null) {
            couponVipListPresenter.getCouponList(new SimpleHashMapBuilder<String, Object>()
                    .puts("pageSize", 10 + "")
                    .puts("page", page + "")
                    .puts("cardNo", cardNo)
                    .puts("ytbAppId", ytbAppId)
                    .puts(Functions.FUNCTION, "20008"));
        } else {
            showEmpty();
        }


    }

    @Override
    public void onGetBalanceListSuccess(List<BalanceModel> list) {

    }

    @Override
    public void onSuccessGetCouponList(List<CouponVip> list, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        page = pageInfoEarly.currentPage;
        if (page == 1) {
            vipCardFragmentAdapter.setNewData(list);
            if (list.size() == 0) {
                showEmpty();
            }
        } else {
            vipCardFragmentAdapter.addData(list);
        }
        if (pageInfoEarly.isMore != 1) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
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

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishLoadMore();
        layoutRefresh.finishRefresh();
    }
}
