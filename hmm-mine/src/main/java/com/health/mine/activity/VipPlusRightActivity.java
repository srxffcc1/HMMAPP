package com.health.mine.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.mine.R;
import com.health.mine.adapter.VipPlusGoodsAdapter;
import com.health.mine.adapter.VipPlusGoodsEmptyAdapter;
import com.health.mine.adapter.VipPlusGoodsTitleAdapter;
import com.health.mine.adapter.VipPlusTopAdapter;
import com.health.mine.contract.VipGoodsContract;
import com.health.mine.presenter.VipGoodsPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.TabEntity;
import com.healthy.library.presenter.PlusPresenterCopy;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

@Route(path = MineRoutes.MINE_VIPPLUSRIGHT)
public class VipPlusRightActivity extends BaseActivity implements IsFitsSystemWindows, OnRefreshLoadMoreListener, VipGoodsContract.View {

    private android.widget.TextView topCardTopBg;
    private com.healthy.library.widget.TopBar topBar;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout refreshLayout;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private androidx.recyclerview.widget.RecyclerView recyclerview;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;


    VipPlusGoodsAdapter vipPlusGoodsAdapter;
    VipPlusGoodsTitleAdapter vipPlusGoodsTitleAdapter;
    VipPlusTopAdapter vipPlusTopAdapter;
    VipPlusGoodsEmptyAdapter vipPlusGoodsEmptyAdapter;
    VipGoodsPresenter vipGoodsPresenter;
    int page=1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip_right;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        buildRecyclerView();
        vipGoodsPresenter=new VipGoodsPresenter(this,this);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        getData();
//        showContent();
    }

    @Override
    public void getData() {
        super.getData();
        new PlusPresenterCopy(this).checkPlus(new SimpleHashMapBuilder<String, Object>()
                .puts("phoneNum", SpUtils.getValue(mContext, SpKey.PHONE))
                .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)));
        vipGoodsPresenter.getVipGoods(new SimpleHashMapBuilder<String, Object>()
                .puts("merchantId",SpUtils.getValue(mContext, SpKey.CHOSE_MC))
                .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                .puts("pageSize",10+"")
                .puts("pageNum",page+""));
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }
    int alldy = 0;
    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerview.setLayoutManager(virtualLayoutManager);
        recyclerview.setAdapter(delegateAdapter);
        vipPlusTopAdapter=new VipPlusTopAdapter();
        delegateAdapter.addAdapter(vipPlusTopAdapter);
        vipPlusTopAdapter.setModel("");
        vipPlusGoodsTitleAdapter=new VipPlusGoodsTitleAdapter();
        delegateAdapter.addAdapter(vipPlusGoodsTitleAdapter);
        vipPlusGoodsTitleAdapter.setModel("");
         vipPlusGoodsAdapter=new VipPlusGoodsAdapter();
        delegateAdapter.addAdapter(vipPlusGoodsAdapter);
        vipPlusGoodsEmptyAdapter=new VipPlusGoodsEmptyAdapter();
        delegateAdapter.addAdapter(vipPlusGoodsEmptyAdapter);

//        vipPlusGoodsAdapter.setData(new SimpleArrayListBuilder<GoodsDetail>()
//                .adds(new GoodsDetail()).adds(new GoodsDetail()).adds(new GoodsDetail())
//                .adds(new GoodsDetail()).adds(new GoodsDetail()).adds(new GoodsDetail())
//                .adds(new GoodsDetail()).adds(new GoodsDetail()).adds(new GoodsDetail())
//                .adds(new GoodsDetail()).adds(new GoodsDetail()).adds(new GoodsDetail()));


    }

    private void initView() {
        topCardTopBg = (TextView) findViewById(R.id.top_card_top_bg);
        topBar = (TopBar) findViewById(R.id.top_bar);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page=1;
        getData();
    }

    @Override
    public void onSucessPlusGetList(List<GoodsDetail> result, PageInfoEarly pageInfoEarly) {
        vipPlusGoodsEmptyAdapter.setModel(null);
        if (result == null) {
            vipPlusGoodsEmptyAdapter.setModel("");
            return;
        }
        if (result.size() == 0) {
            //////System.out.println("目前推荐的长度");
            refreshLayout.finishLoadMoreWithNoMoreData();
            if (page == 1) {
                vipPlusGoodsEmptyAdapter.setModel("");
                vipPlusGoodsAdapter.setModel(null);
            }
        } else {
            if (page == 1) {
                vipPlusGoodsAdapter.setData((ArrayList<GoodsDetail>) result);
            } else {
                vipPlusGoodsAdapter.addDatas((ArrayList<GoodsDetail>) result);
            }
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
    }
}