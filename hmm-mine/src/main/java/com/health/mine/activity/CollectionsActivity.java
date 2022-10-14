package com.health.mine.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.health.mine.R;
import com.health.mine.adapter.CollectionAdapter;
import com.health.mine.contract.CollectionsContract;
import com.health.mine.model.CollectionModel;
import com.health.mine.presenter.CollectionsPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/29 17:06
 * @des 收藏夹界面
 */
@Route(path = MineRoutes.MINE_COLLECTIONS)
public class CollectionsActivity extends BaseActivity implements CollectionsContract.View,
        BaseQuickAdapter.OnItemClickListener {


    private TopBar mTopBar;
    private RecyclerView mRecyclerCollections;
    private StatusLayout mStatusLayout;
    private CollectionAdapter mAdapter;
    private CollectionsPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_collections;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);
        mRecyclerCollections = findViewById(R.id.recycler_collections);
        mStatusLayout = findViewById(R.id.layout_status);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTopBar(mTopBar);
        setStatusLayout(mStatusLayout);
        mAdapter = new CollectionAdapter();
        mRecyclerCollections.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter.bindToRecyclerView(mRecyclerCollections);
        mAdapter.setOnItemClickListener(this);
        mPresenter = new CollectionsPresenter(CollectionsActivity.this, this);
    }

    @Override
    public void getData() {
        mPresenter.getCollections();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_KNOWLEDGE);
        String url = String.format("%s?id=%s&scheme=NewsMessage",
                urlPrefix, mAdapter.getData().get(position).getId());
        ARouter.getInstance()
                .build(IndexRoutes.INDEX_WEBVIEWARTICLE)
                .withString("title", "")
                .withString("url", url)
                .withBoolean("needShare",true)
                .withBoolean("isinhome",true)
                .withBoolean("needfindcollect",true)
                .navigation();
    }

    @Override
    public void onGetCollectionsSuccess(List<CollectionModel> list) {
        if (list.size() == 0) {
            showEmpty();
        } else {
            showContent();
            mAdapter.setNewData(list);
        }
    }
}
