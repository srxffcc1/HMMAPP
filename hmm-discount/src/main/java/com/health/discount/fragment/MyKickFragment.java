package com.health.discount.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.discount.R;
import com.health.discount.adapter.MyKickAdapter;
import com.health.discount.contract.MyKickContract;
import com.healthy.library.model.MyKickList;
import com.health.discount.presenter.MyKickPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyKickFragment extends BaseFragment implements MyKickContract.View, OnRefreshLoadMoreListener {
    private SmartRefreshLayout layoutRefresh;
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    private MyKickAdapter kickAdapter;
    private Map<String, Object> listMap = new HashMap<>();
    private MyKickPresenter presenter;
    private int currentPage = 1;

    public MyKickFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }
    }

    @Override
    public void onResume() {
        super.onResume();
        currentPage = 1;
        getData();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_kick;
    }

    @Override
    protected void findViews() {
        initView();
        presenter = new MyKickPresenter(getContext(), this);
        kickAdapter = new MyKickAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(kickAdapter);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        listMap.put("currentPage", currentPage + "");
        listMap.put("pageSize", 10 + "");
        listMap.put("searchType", get("type") + "");
        //listMap.put("addressProvince", get("addressProvince") + "");
        //listMap.put("longitude", get("lng") + "");
        //listMap.put("latitude", get("lat") + "");
        presenter.getKickTopList(listMap);
    }

    @Override
    public void onSuccessGetTopKickList(List<MyKickList> kickList, PageInfoEarly pageInfoEarly) {
        if (kickList == null || kickList.size() == 0) {
            showEmpty();
            return;
        }
        if (pageInfoEarly == null) {
//            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        currentPage = pageInfoEarly.currentPage;
        if (currentPage == 1) {
            kickAdapter.setData((ArrayList) kickList);
        } else {
            kickAdapter.addDatas((ArrayList) kickList);
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
        currentPage++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage = 1;
        getData();
    }

    private void initView() {
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    public static MyKickFragment newInstance(Map<String, Object> maporg) {
        MyKickFragment fragment = new MyKickFragment();
        Bundle args = new Bundle();
        for (Map.Entry<String, Object> entry : maporg.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Integer) {
                args.putInt(key, (Integer) value);
            } else if (value instanceof Boolean) {
                args.putBoolean(key, (Boolean) value);
            } else if (value instanceof String) {
                args.putString(key, (String) value);
            } else {
                args.putSerializable(key, (Serializable) value);
            }
        }
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

}