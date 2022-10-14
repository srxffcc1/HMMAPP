package com.health.mine.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.mine.R;
import com.health.mine.adapter.VipDespositAdapter;
import com.health.mine.contract.VipDepositContract;
import com.healthy.library.model.BalanceModel;
import com.healthy.library.model.DepositList;
import com.healthy.library.model.OrderList;
import com.healthy.library.model.VipDeposit;
import com.health.mine.presenter.VipDepositPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;


public class VipDepositChildFragment extends BaseFragment implements OnRefreshLoadMoreListener, VipDepositContract.View {

    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private androidx.recyclerview.widget.RecyclerView recyclerList;
    VipDespositAdapter vipDespositAdapter;
    public int page = 1;
    VipDepositPresenter vipDepositPresenter;

    private String cardNo;
    private String ytbAppId;
    private String goodsType;


    public static VipDepositChildFragment newInstance(String cardNo, String ytbAppId, String goodsType) {
        VipDepositChildFragment fragment = new VipDepositChildFragment();
        Bundle args = new Bundle();
        args.putString("cardNo", cardNo);
        args.putString("ytbAppId", ytbAppId);
        args.putString("goodsType", goodsType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cardNo = getArguments().getString("cardNo");
            ytbAppId = getArguments().getString("ytbAppId");
            goodsType = getArguments().getString("goodsType");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vip_deposit_child;
    }

    @Override
    protected void findViews() {
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerList = (RecyclerView) findViewById(R.id.recycler_list);

        vipDespositAdapter = new VipDespositAdapter();
        recyclerList.setLayoutManager(new LinearLayoutManager(mContext));
        vipDespositAdapter.bindToRecyclerView(recyclerList);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        vipDepositPresenter = new VipDepositPresenter(mContext, this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        if (cardNo != null && ytbAppId != null) {
            vipDepositPresenter.getList(new SimpleHashMapBuilder<String, Object>()
                    .puts("pageSize", 10 + "")
                    .puts("page", page + "")
                    .puts("goodsType", goodsType)
                    .puts("cardNo", cardNo)
                    .puts("ytbAppId", ytbAppId));
        } else {
            showEmpty();
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

    @Override
    public void onGetBalanceListSuccess(List<BalanceModel> list) {

    }

    @Override
    public void onGetListSuccess(List<VipDeposit> list, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        page = pageInfoEarly.currentPage;
        if (page == 1) {

            if (list.size() == 0) {
                showEmpty();
            } else {
                vipDespositAdapter.setNewData(list);
                for (int i = 0; i < list.size(); i++) {
                    VipDeposit vipDeposit = list.get(i);
                    VipDepositPresenter presenter = new VipDepositPresenter(mContext, this);
                    presenter.getDepositList(new SimpleHashMapBuilder<String, Object>()
                            .puts("cardNo", cardNo)
                            .puts("ytbAppId", ytbAppId)
                            .puts("goodsID", vipDeposit.GoodsID)
                            .puts("pageSize", 10)
                            .puts("page", 1), vipDeposit);
                }
            }

        } else {
            vipDespositAdapter.addData(list);
            for (int i = 0; i < list.size(); i++) {
                VipDeposit vipDeposit = list.get(i);
                VipDepositPresenter presenter = new VipDepositPresenter(mContext, this);
                presenter.getDepositList(new SimpleHashMapBuilder<String, Object>()
                        .puts("cardNo", cardNo)
                        .puts("ytbAppId", ytbAppId)
                        .puts("goodsID", vipDeposit.GoodsID)
                        .puts("pageSize", 10)
                        .puts("page", 1), vipDeposit);
            }
        }
        if (pageInfoEarly.isMore != 1) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
        vipDespositAdapter.setAPPId(cardNo, ytbAppId);
    }

    @Override
    public void onGetDepositListSuccess(List<DepositList> list, PageInfoEarly pageInfoEarly) {

    }

    @Override
    public void onGetDepositGoodsSuccess() {
        if (vipDespositAdapter != null) {
            vipDespositAdapter.notifyDataSetChanged();
        }
    }
}