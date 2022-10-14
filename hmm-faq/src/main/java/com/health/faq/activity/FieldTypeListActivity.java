package com.health.faq.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.health.faq.R;
import com.health.faq.adapter.TypeFullAdapter;
import com.health.faq.contract.TypeListContract;
import com.healthy.library.model.FaqHotExpertFieldChose;
import com.health.faq.presenter.TypeListPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/04 17:24
 * @des 列表
 */

@Route(path = FaqRoutes.FAQ_EXPERT_TYPELIST)
public class FieldTypeListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener,
        TypeListContract.View, View.OnClickListener, OnRefreshLoadMoreListener {

    @Autowired
    String cityNo;

    private TopBar mTopBar;
    private RecyclerView mRecyclerList;
    private StatusLayout mStatusLayout;
    private TypeListPresenter mPresenter;
    private SmartRefreshLayout mRefreshLayout;
    private int page=1;
    private TypeFullAdapter manAdapter;
    private GridLayoutManager layoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fieldtype_list;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);
        mRecyclerList = findViewById(R.id.recycler_list);
        mStatusLayout = findViewById(R.id.layout_status);
        mRefreshLayout=findViewById(R.id.layout_refresh);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setTopBar(mTopBar);
        setStatusLayout(mStatusLayout);
        mPresenter = new TypeListPresenter(mContext, this);
        manAdapter = new TypeFullAdapter();
        manAdapter.setLocation(cityNo);
        manAdapter.setWidth(50);
        layoutManager = new GridLayoutManager(mContext,4);
        mRecyclerList.setLayoutManager(layoutManager);
        mRecyclerList.setAdapter(manAdapter);
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
        getData();
    }

    @Override
    public void getData() {
        mPresenter.getList();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }


//    @Override
//    public void onGetListSuccess(List<StoreDetailModel.TechnicianResults> list, PageInfoEarly refreshLoadModel) {
//
//        mRefreshLayout.setEnableLoadMore(false);
//        mRefreshLayout.finishLoadMoreWithNoMoreData();
//    }

    @Override
    public void onGetListSuccess(List<FaqHotExpertFieldChose> list) {
        manAdapter.setNewData(list);
    }

    @Override
    public void onGetStoreListFail() {

    }
    @Override
    public void onRequestFinish() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
//        mPresenter.getStoreList(shopId,page+"");
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
//        mPresenter.getStoreList(shopId,page+"");
    }
}
