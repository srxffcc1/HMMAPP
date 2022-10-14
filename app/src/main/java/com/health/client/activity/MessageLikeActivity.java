package com.health.client.activity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.client.R;
import com.health.client.adapter.MessageLikeAdapter;
import com.health.client.contract.MessageHelperOtherContract;
import com.health.client.model.MonMessageOtherHelper;
import com.health.client.presenter.MessageHelperOtherPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = AppRoutes.APP_MESSAGELIKE)
public class MessageLikeActivity extends BaseActivity implements MessageHelperOtherContract.View, OnRefreshLoadMoreListener {
    private TopBar topBar;
    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerNews;
    private MessageHelperOtherPresenter messageHelperPresenter;
    private MessageLikeAdapter adapter;
    private int currentPage=1;
    private Map<String, Object> fansmap = new HashMap<>();;
    @Autowired
    String type;//我的粉丝 还是我的关注
    @Override
    protected int getLayoutId() {
        return R.layout.activity_messagelist;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }
    @Override
    public void getData() {
        super.getData();
        messageHelperPresenter.getMessage(fansmap);
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        messageHelperPresenter=new MessageHelperOtherPresenter(mContext,this);
        adapter = new MessageLikeAdapter();
        recyclerNews.setLayoutManager(new LinearLayoutManager(mContext));
        adapter.bindToRecyclerView(recyclerNews);
        fansmap.put("memberId",new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        fansmap.put("pageNum",currentPage+"");
        fansmap.put("pageSize",10+"");
        if("赞".equals(type)){
            fansmap.put("replyType",new String[]{"3"});
        }
        topBar.setTitle(type);
        messageHelperPresenter.getMessage(fansmap);
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        fansmap.put("pageNum",currentPage+"");
        messageHelperPresenter.getMessage(fansmap);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage=1;
        fansmap.put("pageNum",currentPage+"");
        messageHelperPresenter.getMessage(fansmap);
    }
    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }
    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerNews = (RecyclerView) findViewById(R.id.recycler_news);
    }

    @Override
    public void onSuccessGetMessageList(List<MonMessageOtherHelper> results, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        currentPage = pageInfoEarly.currentPage;
        if (currentPage == 1) {
            adapter.setNewData(results);
            if (results.size() == 0) {
                showEmpty();
            }
        } else {
            adapter.addData(results);
        }
        if (pageInfoEarly.isMore != 1) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }
}
