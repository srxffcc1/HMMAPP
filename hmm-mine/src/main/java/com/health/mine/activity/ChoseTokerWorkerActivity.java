package com.health.mine.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.mine.R;
import com.health.mine.adapter.RecommandWorksAdapter;
import com.healthy.library.adapter.SoundListAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.contract.QiYeWeiXinContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.QiYeWeXin;
import com.healthy.library.model.QiYeWeXinKey;
import com.healthy.library.model.QiYeWeXinWorkShop;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.presenter.QiYeWeiXinPresenter;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;
//选择顾问页面
@Route(path = MineRoutes.MINE_RECOMMANDWORKS)
public class ChoseTokerWorkerActivity extends BaseActivity implements QiYeWeiXinContract.View, IsFitsSystemWindows {
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout refreshLayout;
    private androidx.recyclerview.widget.RecyclerView recycler;
    QiYeWeiXinPresenter qiYeWeiXinPresenter;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    RecommandWorksAdapter recommandWorksAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_title_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        qiYeWeiXinPresenter=new QiYeWeiXinPresenter(this,this);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);
        buildRecyclerView();
        topBar.setTitle("选择顾问");
        getData();
    }
    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recycler.setLayoutManager(virtualLayoutManager);
        recycler.setAdapter(delegateAdapter);
        recommandWorksAdapter=new RecommandWorksAdapter();
        delegateAdapter.addAdapter(recommandWorksAdapter);
    }
    @Override
    public void getData() {
        super.getData();
        qiYeWeiXinPresenter.getRecommandWorkerList(new SimpleHashMapBuilder<String, Object>());
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public void onSucessGetWeiXinKey(QiYeWeXinKey qiYeWeXinKey) {

    }

    @Override
    public void onSucessGetRecommandWeiXinGroup(List<QiYeWeXin> qiYeWeXins) {

    }

    @Override
    public void onSucessGetRecommandWorkerGroup(List<QiYeWeXinWorkShop> qiYeWeXins) {
        recommandWorksAdapter.setData(new SimpleArrayListBuilder<QiYeWeXinWorkShop>().addList(
                ListUtil.where(qiYeWeXins, new ListUtil.Where<QiYeWeXinWorkShop>() {
                    @Override
                    public boolean where(QiYeWeXinWorkShop obj) {
                        return !ListUtil.isEmpty(obj.tokerWorkerList);
                    }
                })
        ));
    }

    @Override
    public void onSucessGetMineWorker(TokerWorkerInfoModel tokerWorkerInfoModel) {

    }

    @Override
    public void onSucessGetPublicWorker(TokerWorkerInfoModel tokerWorkerInfoModel) {

    }
}
