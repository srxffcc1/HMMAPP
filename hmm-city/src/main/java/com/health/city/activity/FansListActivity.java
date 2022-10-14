package com.health.city.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.city.R;
import com.health.city.adapter.FansAdapter;
import com.health.city.contract.FansListContract;
import com.healthy.library.model.Fans;
import com.health.city.presenter.FansListPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = CityRoutes.CITY_FANLIST)
public class FansListActivity extends BaseActivity implements FansListContract.View , OnRefreshLoadMoreListener, FansAdapter.OnFansClickListener {

    @Autowired
    String type;//我的粉丝 还是我的关注
    private com.healthy.library.widget.TopBar topBar;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private RecyclerView recycler;
    FansAdapter fansAdapter;
    private int currentPage=1;
    private FansListPresenter fansListPresenter;
    private Map<String, Object> fansmap = new HashMap<>();;

    @Override
    protected int getLayoutId() {
        return R.layout.city_activity_fanslist;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        fansListPresenter=new FansListPresenter(mContext,this);
        fansAdapter = new FansAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        fansAdapter.bindToRecyclerView(recycler);
        fansAdapter.setOnFansClickListener(this);

        if("0".equals(type)){
            topBar.setTitle("我的关注");
        }else {
            topBar.setTitle("我的粉丝");
        }
        fansmap.put("pageSize",10+"");
        fansmap.put("type",type+"");
        fansmap.put("currentPage",currentPage+"");
        fansListPresenter.getFanList(fansmap);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    public void onSuccessGetFansList(List<Fans> fansList, PageInfoEarly pageInfoEarly) {
        if (pageInfoEarly == null) {
            showEmpty();
            layoutRefresh.setEnableLoadMore(false);
            return;
        }
        currentPage = pageInfoEarly.currentPage;
        if (currentPage == 1) {
            fansAdapter.setNewData(fansList);
            if (fansList.size() == 0) {
                showEmpty();
            }
        } else {
            fansAdapter.addData(fansList);
        }
        if (pageInfoEarly.isMore != 1) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void onSuccessFans() {
        onRefresh(null);
    }
    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }


    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        currentPage++;
        fansmap.put("currentPage",currentPage+"");
        fansListPresenter.getFanList(fansmap);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        currentPage=1;
        fansmap.put("currentPage",currentPage+"");
        fansListPresenter.getFanList(fansmap);
    }

    @Override
    public void click(View view, String friendId, String type,String frtype) {
        Map<String, Object> map = new HashMap<>();
        map.put("friendId", friendId);
        map.put("friendType", frtype);
        map.put("type", type);
        fansListPresenter.fan(map);
    }
}
