package com.health.index.fragment;

import android.os.Bundle;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.index.R;
import com.healthy.library.adapter.HanMomVideoFindChildAdapter;
import com.health.index.contract.HanMomVideoContract;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.model.VideoListModel;
import com.health.index.presenter.HanMomVideoPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.VideoCategory;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HanMomVideoFindChildFragment extends BaseFragment implements HanMomVideoContract.View, OnRefreshLoadMoreListener, BaseAdapter.OnOutClickListener {


    private String categoryCode;
    private String mParam2;

    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerList;

    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private HanMomVideoFindChildAdapter hanMomVideoFindChildAdapter;
    private int pageNum = 1;
    private HanMomVideoPresenter hanMomVideoPresenter;

    public HanMomVideoFindChildFragment() {

    }

    public static HanMomVideoFindChildFragment newInstance(String categoryCode, String param2) {
        HanMomVideoFindChildFragment fragment = new HanMomVideoFindChildFragment();
        Bundle args = new Bundle();
        args.putString("categoryCode", categoryCode);
        args.putString("mParam2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryCode = getArguments().getString("categoryCode");
            mParam2 = getArguments().getString("mParam2");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_han_mom_video_find_child;
    }

    @Override
    protected void findViews() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerList = (RecyclerView) findViewById(R.id.recyclerList);

        hanMomVideoPresenter = new HanMomVideoPresenter(mContext, this);
        initList();
        getData();
    }

    private void initList() {
        refreshLayout.setOnRefreshLoadMoreListener(this);
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerList.setLayoutManager(virtualLayoutManager);
        recyclerList.setAdapter(delegateAdapter);

        hanMomVideoFindChildAdapter = new HanMomVideoFindChildAdapter();
        delegateAdapter.addAdapter(hanMomVideoFindChildAdapter);
        hanMomVideoFindChildAdapter.setOutClickListener(this);
    }

    @Override
    public void getData() {
        super.getData();
        hanMomVideoPresenter.getVideoList(new SimpleHashMapBuilder<String, Object>()
                .puts("pageNum", pageNum)
                .puts("pageSize", "10")
                .puts("categoryCode", categoryCode)
                .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
    }

    @Override
    public void onGetTabListSuccess(List<VideoCategory> result) {

    }

    @Override
    public void onGetVideoListSuccess(List<VideoListModel> result, PageInfoEarly pageInfoEarly) {
        showContent();
        if (result == null) {
            return;
        }
        pageNum = pageInfoEarly.pageNum;
        if (pageNum == 1 || pageNum == 0) {
            hanMomVideoFindChildAdapter.setData((ArrayList) result);
            refreshLayout.finishRefresh();
            if (result.size() == 0) {
                showEmpty();
            }
        } else {
            hanMomVideoFindChildAdapter.addDatas((ArrayList) result);
            refreshLayout.finishLoadMore();
        }
        if (pageInfoEarly.nextPage == 0) {
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.setNoMoreData(false);
            refreshLayout.setEnableLoadMore(true);
        }
    }

    @Override
    public void onGetVideoDetailSuccess(VideoListModel result, int type) {

    }

    @Override
    public void onAddPraiseSuccess(String result, int type) {

    }

    @Override
    public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
        pageNum++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
        pageNum = 1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void outClick(String function, Object obj) {
//        if (function.equals("click")) {
//            ARouter.getInstance()
//                    .build(IndexRoutes.INDEX_HANMOMVIDEODETAIL)
//                    .withString("id", (String) obj)
//                    .withString("categoryCode", categoryCode)
//                    .navigation();
//        }
    }
}