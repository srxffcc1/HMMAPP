package com.health.client.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.health.client.R;
import com.health.client.adapter.MessageAdapter;
import com.health.client.contract.MessageContract;
import com.healthy.library.model.MonMessage;
import com.health.client.presenter.MessagePresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

@Route(path = AppRoutes.APP_MESSAGESETTING)
public class MessageSettingActivity extends BaseActivity implements MessageContract.View, OnRefreshLoadMoreListener {
//    private StatusLayout layoutStatus;
//    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerNews;
    private MessagePresenter messagePresenter;
    private MessageAdapter messageAdapter;
    private com.healthy.library.widget.TopBar topBar;

    @Override
    public void onSuccessGetMessage(List<MonMessage> results) {
        messageAdapter.setNewData(results);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_activity_message_setting;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        layoutRefresh.setEnableLoadMore(false);
//        layoutRefresh.setOnRefreshLoadMoreListener(this);
        recyclerNews.setLayoutManager(new LinearLayoutManager(mContext));
        messageAdapter=new MessageAdapter();
        messageAdapter.bindToRecyclerView(recyclerNews);
        messagePresenter=new MessagePresenter(mContext,this);
        messagePresenter.getMessageSepcial();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//        messagePresenter.getMessageSepcial();
    }
    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
//        layoutRefresh.finishLoadMore();
//        layoutRefresh.finishRefresh();
    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//        messagePresenter.getMessageSepcial();
    }

    private void initView() {
//        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
//        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);
        topBar = (TopBar) findViewById(R.id.top_bar);
    }
}
