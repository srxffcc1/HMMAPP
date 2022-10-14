package com.health.city.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.city.R;
import com.health.city.adapter.FansCardAdapter;
import com.health.city.contract.FansRecommendContract;
import com.healthy.library.model.Fans;
import com.health.city.presenter.FansRecommabeListPresenter;
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

@Route(path = CityRoutes.CITY_FANRECLIST)
public class FansRecListActivity extends BaseActivity implements FansRecommendContract.View , OnRefreshLoadMoreListener, FansCardAdapter.OnFansClickListener {


    private TopBar topBar;
    private SmartRefreshLayout layoutRefresh;
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    FansCardAdapter fansAdapter;
    private int currentPage=1;
    private FansRecommabeListPresenter fansListPresenter;
    private Map<String, Object> fansmap = new HashMap<>();;

    @Override
    protected int getLayoutId() {
        return R.layout.city_activity_fansreclist;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        topBar.setTitle("推荐关注");
        fansListPresenter=new FansRecommabeListPresenter(mContext,this);
        fansAdapter = new FansCardAdapter();
        recycler.setLayoutManager(new GridLayoutManager(mContext,2));
        fansAdapter.bindToRecyclerView(recycler);
        fansAdapter.setOnFansClickListener(this);
        fansmap.put("pageSize",10+"");
        fansmap.put("currentPage",currentPage+"");
        fansListPresenter.getFansList(fansmap);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }


    @Override
    public void onSuccessFans() {
//        onRefresh(null);
    }

    @Override
    public void onSuccessFansAll() {

    }

    @Override
    public void onSuccessGetFans(List<Fans> fansList, PageInfoEarly pageInfoEarly) {
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
        fansListPresenter.getFansList(fansmap);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        currentPage=1;
        fansmap.put("currentPage",currentPage+"");
        fansListPresenter.getFansList(fansmap);
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
