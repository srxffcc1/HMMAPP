package com.health.index.activity;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.healthy.library.routes.IndexRoutes;
import com.health.index.R;
import com.health.index.adapter.BirthCheckAdapter;
import com.healthy.library.constant.UrlKeys;
import com.health.index.contract.BirthCheckListContract;
import com.health.index.model.BirthCheckModel;
import com.health.index.presenter.BirthCheckListPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import java.util.List;
import java.util.Locale;

/**
 * @author Li
 * @date 2019/04/24 14:51
 * @des 产检表
 */
@Route(path = IndexRoutes.INDEX_BIRTH_CHECK_LIST)
public class BirthCheckListActivity extends BaseActivity implements BirthCheckListContract.View,
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener {

    @Autowired
    int id;
    private RecyclerView mRecyclerCheck;
    private StatusLayout mStatusLayout;
    private TopBar mTopBar;
    private SwipeRefreshLayout mRefreshLayout;
    private BirthCheckListPresenter mPresenter;
    private BirthCheckAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_birth_check_list;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);
        mStatusLayout = findViewById(R.id.layout_status);
        mRecyclerCheck = findViewById(R.id.recycler_check);
        mRefreshLayout = findViewById(R.id.layout_refresh);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setTopBar(mTopBar);
        setStatusLayout(mStatusLayout);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRefreshLayout.setOnRefreshListener(this);
        mPresenter = new BirthCheckListPresenter(this, this);
        mAdapter = new BirthCheckAdapter();
        mRecyclerCheck.setLayoutManager(mLayoutManager);
        mAdapter.bindToRecyclerView(mRecyclerCheck);
        mAdapter.setOnItemClickListener(this);
        getData();
    }

    @Override
    public void onGetBirthCheckListSuccess(List<BirthCheckModel> list, int weekId) {
        mAdapter.setNewData(list);
        if (id != -1) {
            weekId = findPos(list, id);
        } else {
            weekId = Math.min(list.size(), weekId - 1);
            weekId = Math.max(0, weekId);
        }
        mLayoutManager.scrollToPosition(weekId);
    }

    @Override
    public void getData() {
        mPresenter.getBirthCheckList();
    }

    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void onRequestFinish() {
        mRefreshLayout.setRefreshing(false);
    }

    /**
     * 根据列表和id来决定跳转的位置
     */
    private int findPos(List<BirthCheckModel> birthCheckModelList, int id) {
        for (BirthCheckModel model : birthCheckModelList) {
            if (model.getId() == id) {
                return birthCheckModelList.indexOf(model);
            }
        }
        return 0;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        int id = mAdapter.getData().get(position).getId();
        String url = String.format(Locale.CHINA,
                "%s?id=%d&date=%s", SpUtils.getValue(mContext, UrlKeys.H5_TABLE_DETAIL), id,
                mAdapter.getData().get(position).getDate());
        ARouter.getInstance()
                .build(IndexRoutes.INDEX_WEBVIEW)
                .withString("url", url)
                .withString("title", "产检详情")
                .navigation();
    }
}
