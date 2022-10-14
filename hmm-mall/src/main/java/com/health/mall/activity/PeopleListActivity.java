package com.health.mall.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.mall.R;
import com.health.mall.adapter.PeopleListAdapter;
import com.health.mall.contract.PeopleListContract;
import com.health.mall.model.PeopleListModel;
import com.health.mall.presenter.PeopleListPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.routes.MallRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Route(path = MallRoutes.MALL_PEOPLELIST)
public class PeopleListActivity extends BaseActivity implements OnRefreshLoadMoreListener, BaseAdapter.OnOutClickListener, PeopleListContract.View {

    @Autowired
    String shopId;

    private TopBar topBar;
    private SmartRefreshLayout layoutRefresh;
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    private PeopleListPresenter peopleListPresenter;
    private int currentPage = 1;
    private PeopleListAdapter peopleListAdapter;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_people_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        peopleListPresenter = new PeopleListPresenter(this, this);

        layoutRefresh.setOnLoadMoreListener(this);
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recycler.setLayoutManager(virtualLayoutManager);
        recycler.setAdapter(delegateAdapter);

        peopleListAdapter = new PeopleListAdapter();
        delegateAdapter.addAdapter(peopleListAdapter);
        peopleListAdapter.setOutClickListener(this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        peopleListPresenter.getPeopleList(new SimpleHashMapBuilder<String, Object>().puts("shopId", shopId)
                .puts("currentPage", currentPage + "").puts("pageSize", "10"));
    }

    @Override
    protected void findViews() {
        super.findViews();
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
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
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {

    }

    @Override
    public void onGetPeopleListSuccess(List<PeopleListModel> result, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        currentPage = pageInfoEarly.currentPage;
        if (currentPage == 1) {
            peopleListAdapter.setData((ArrayList) result);
            if (result.size() == 0) {
                showEmpty();
            }
        } else {
            peopleListAdapter.addDatas((ArrayList) result);
        }
        if (pageInfoEarly.isMore != 1) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }
}