package com.health.mine.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.mine.R;
import com.health.mine.adapter.VipConsumeHistoryAdapter;
import com.health.mine.contract.VipConsumeContract;
import com.healthy.library.model.BalanceModel;
import com.healthy.library.model.VipConsume;
import com.health.mine.presenter.VipConsumePresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;


public class VipConsumeHistoryFragment extends BaseFragment implements OnRefreshLoadMoreListener, VipConsumeContract.View {
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private androidx.recyclerview.widget.RecyclerView recyclerList;
    public int page = 1;
    VipConsumeHistoryAdapter vipConsumeHistoryAdapter;
    VipConsumePresenter vipConsumePresenter;
    private String cardNo;
    private String ytbAppId;

    public static VipConsumeHistoryFragment newInstance(String cardNo, String ytbAppId) {
        VipConsumeHistoryFragment fragment = new VipConsumeHistoryFragment();
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
        return R.layout.fragment_vip_consume_history;
    }

    @Override
    protected void findViews() {
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerList = (RecyclerView) findViewById(R.id.recycler_list);
        vipConsumeHistoryAdapter = new VipConsumeHistoryAdapter();
        recyclerList.setLayoutManager(new LinearLayoutManager(mContext));
        vipConsumeHistoryAdapter.bindToRecyclerView(recyclerList);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        vipConsumePresenter = new VipConsumePresenter(mContext, this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        if (cardNo != null && ytbAppId != null) {
            vipConsumePresenter.getList(new SimpleHashMapBuilder<String, Object>()
                    .puts("pageSize", 10 + "")
                    .puts("page", page + "")
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
    public void onGetListSuccess(List<VipConsume> list, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        page = pageInfoEarly.currentPage;
        if (page == 1) {
            vipConsumeHistoryAdapter.setNewData(list);
            if (list.size() == 0) {
                showEmpty();
            }
        } else {
            vipConsumeHistoryAdapter.addData(list);
        }
        if (pageInfoEarly.isMore != 1) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }
}