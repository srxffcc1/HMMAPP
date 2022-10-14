package com.health.mine.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.health.mine.R;
import com.health.mine.adapter.JobTypeAdapter;
import com.health.mine.contract.JobContract;
import com.health.mine.model.JobType;
import com.health.mine.presenter.JobPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;

@Route(path = MineRoutes.MINE_JOB_TYPE)
public class JobTypeListActivity extends BaseActivity implements JobContract.View , OnRefreshLoadMoreListener , IsFitsSystemWindows {

    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerList;
    private JobPresenter jobPresenter;
    private JobTypeAdapter jobTypeAdapter;
    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_job_type;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        jobPresenter=new JobPresenter(mContext,this);
        layoutRefresh.setEnableLoadMore(false);
        jobTypeAdapter = new JobTypeAdapter();
        recyclerList.setLayoutManager(new GridLayoutManager(mContext,2));
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        jobTypeAdapter.bindToRecyclerView(recyclerList);

        getData();
    }

    @Override
    public void getData() {
        super.getData();
        jobPresenter.getJobType(new HashMap<String, Object>());
    }

    @Override
    public void onSuccessGetJobType(List<JobType> results) {
        jobTypeAdapter.setNewData(results);
    }

    @Override
    public void onSuccessSendCode() {

    }

    @Override
    public void onGetCodeFail() {

    }

    @Override
    public void onSuccessAdd() {

    }

    @Override
    public void onUpLoadCretSuccess(List<String> urls, int type) {

    }

    @Override
    public void onUpLoadHealthSuccess(List<String> urls, int type) {

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishLoadMore();
        layoutRefresh.finishRefresh();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerList = (RecyclerView) findViewById(R.id.recycler_list);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }
}
