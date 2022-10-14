package com.health.mine.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.mine.R;
import com.health.mine.adapter.VipIntegralAdapter;
import com.health.mine.contract.VipIntegralContract;
import com.healthy.library.model.BalanceModel;
import com.health.mine.model.IntegralListModel;
import com.healthy.library.model.IntegralModel;
import com.health.mine.presenter.VipIntegralPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VipIntegralFragment extends BaseFragment implements OnRefreshLoadMoreListener, VipIntegralContract.View {

    private RecyclerView recycler;
    private StatusLayout layout_status;
    private SmartRefreshLayout layout_refresh;
    private VipIntegralAdapter footerAdapter;
    private VipIntegralPresenter presenter;
    private int currentPage = 1;
    private Map<String, Object> map = new HashMap<>();
    private String cardNo;
    private String ytbAppId;

    public static VipIntegralFragment newInstance(String cardNo, String ytbAppId) {
        VipIntegralFragment fragment = new VipIntegralFragment();
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
        return R.layout.fragment_vip_integral;
    }

    @Override
    protected void findViews() {
        recycler = findViewById(R.id.recycler);
        layout_status = findViewById(R.id.layout_status);
        layout_refresh = findViewById(R.id.layout_refresh);
        presenter = new VipIntegralPresenter(mContext, this);
        layout_refresh.setOnRefreshLoadMoreListener(this);
        footerAdapter = new VipIntegralAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.setAdapter(footerAdapter);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        if (cardNo != null && ytbAppId != null) {
            map.put("cardNo", cardNo + "");
            map.put("ytbAppId", ytbAppId + "");
            map.put("page", currentPage + "");
            map.put("pageSize", "10");
            presenter.getIntegralList(map);
        } else {
            showEmpty();
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage = 1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layout_refresh.finishRefresh();
        layout_refresh.finishLoadMore();
    }

    @Override
    public void onGetBalanceListSuccess(List<BalanceModel> list) {

    }

    @Override
    public void onGetIntegralSuccess(IntegralModel model) {

    }

    @Override
    public void onGetIntegralListSuccess(PageInfoEarly pageInfoEarly, List<IntegralListModel> list) {
        if (currentPage == 1) {
            footerAdapter.setData((ArrayList) list);
            if (list.size() == 0) {
                showEmpty();
            }
        } else {
            footerAdapter.addDatas((ArrayList) list);
        }
        if (pageInfoEarly.isMore != 1) {
            layout_refresh.finishLoadMoreWithNoMoreData();
        } else {
            layout_refresh.setNoMoreData(false);
            layout_refresh.setEnableLoadMore(true);
        }
    }
}