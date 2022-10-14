package com.health.mine.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.adapter.OrderSubAdapter;
import com.health.mine.contract.OrderSubListContract;
import com.health.mine.model.OrderSubDetailModel;
import com.health.mine.presenter.OrderSubListPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.decoration.OrderDecoration;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = MineRoutes.MINE_ORDER_SUB_LIST)
public class OrderSubListActivity extends BaseActivity implements OrderSubListContract.View, OnRefreshLoadMoreListener, OrderSubAdapter.OnSubOrderClicker {
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private androidx.recyclerview.widget.RecyclerView recyclerNews;
    OrderSubAdapter orderSubAdapter;
    OrderSubListPresenter orderSubListPresenter;
    private int mCurrentPage=1;

    Map<String, Object> map = new HashMap<>();
    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_order_sub_list;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        orderSubAdapter = new OrderSubAdapter();
        orderSubAdapter.setOnSubOrderClicker(this);
        recyclerNews.setLayoutManager(new LinearLayoutManager(mContext));
        orderSubAdapter.bindToRecyclerView(recyclerNews);
        recyclerNews.addItemDecoration(new OrderDecoration(mContext));
        layoutRefresh.finishRefreshWithNoMoreData();
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        orderSubListPresenter=new OrderSubListPresenter(mContext,this);
        map.put("pageSize", 10+"");
        getData();


    }

    @Override
    public void getData() {
        super.getData();
        map.put("currentPage", mCurrentPage+"");
        orderSubListPresenter.getOrderSubList(map);

    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);
    }

    @Override
    public void onSucessGetSubList(List<OrderSubDetailModel> orderSubDetailModels, PageInfoEarly pageInfo) {
        mCurrentPage = pageInfo.currentPage;
        if (mCurrentPage==1||mCurrentPage==0) {
            orderSubAdapter.setNewData(orderSubDetailModels);
            if (orderSubDetailModels.size() == 0) {
                showEmpty();
            }
        } else {
            orderSubAdapter.addData(orderSubDetailModels);
        }
        if (pageInfo.isMore!=1) {
            ////System.out.println("没有更多了");
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void onSucessSubUpdate() {
        onRefresh(null);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage=1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Override
    public void onSubClick(View view, String id,String appointmentStatus) {

        Map<String, Object> map = new HashMap<>();
        map.put("id", id+"");
        map.put("appointmentStatus", appointmentStatus+"");
        orderSubListPresenter.subUpdate(map);
    }
}
