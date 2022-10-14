package com.health.tencentlive.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.tencentlive.R;
import com.health.tencentlive.adapter.LiveMainEmptyAdapter;
import com.health.tencentlive.adapter.LiveMainTitleAdapter;
import com.health.tencentlive.adapter.LiveMainVideoAdapter;
import com.healthy.library.contract.LiveGoodsContract;
import com.health.tencentlive.contract.LiveMainBodyContract;
import com.healthy.library.model.LiveVideoGoods;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.presenter.LiveGoodsPresenter;
import com.health.tencentlive.presenter.LiveMainBodyPresenter;
import com.healthy.library.business.LivePassWordDialog;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LiveMainBodyFragment extends BaseFragment implements LiveMainBodyContract.View, OnRefreshLoadMoreListener, LiveGoodsContract.View {

    LiveMainTitleAdapter liveMainTitleAdapter;
    LiveMainVideoAdapter liveMainVideoAdapter;
    LiveMainEmptyAdapter liveMainEmptyAdapter;

    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerview;
    private LiveMainBodyPresenter liveMainBodyPresenter;
    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_main_body;
    }

    @Override
    protected void findViews() {
        initView();
        buildRecyclerView();
        liveMainBodyPresenter = new LiveMainBodyPresenter(mContext, this);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.setEnableRefresh(get("refresh") == null ? true : false);
    }

    @Override
    protected void onLazyData() {
        super.onLazyData();
        getData();
    }

    public static LiveMainBodyFragment newInstance(Map<String, Object> maporg) {
        LiveMainBodyFragment fragment = new LiveMainBodyFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void changeFragmentShow() {
        super.changeFragmentShow();
        if (isFirstLoad) {
            onRefresh(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void getData() {
        super.getData();
//        List<String> emptylist=new ArrayList<>();
//        emptylist.clear();
//        for (int i = 0; i <20 ; i++) {
//            emptylist.add(""+i);
//        }
//        if(page==1||page==0){
//            liveMainVideoAdapter.setData((ArrayList<String>) emptylist);
//            onRequestFinish();
//        }else {
//            liveMainVideoAdapter.addDatas((ArrayList<String>) emptylist);
//            onRequestFinish();
//        }
        liveMainBodyPresenter.getLiveList(new SimpleHashMapBuilder<String, Object>()
                        .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))
                        .puts("anchormanId", get("anchormanId"))
//                .puts("roomId",get("roomId"))
//                .puts("courseTitle",get("courseTitle"))
                        .puts("type", get("type"))
                        .puts("category", get("category"))
                        .puts("statusList", get("statusList").toString().split(","))
                        .puts("isDelete", "0")
                        .puts("sortType", get("sortType"))
                        .puts("page", new SimpleHashMapBuilder<String, Object>()
                                .puts("pageSize", 10 + "")
                                .puts("pageNum", page + "")
                        )
        );
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerview.setLayoutManager(virtualLayoutManager);
        recyclerview.setAdapter(delegateAdapter);

        liveMainTitleAdapter = new LiveMainTitleAdapter();
        delegateAdapter.addAdapter(liveMainTitleAdapter);
        liveMainTitleAdapter.setModel(get("courseTitle") == null ? null : get("courseTitle").toString());
        liveMainVideoAdapter = new LiveMainVideoAdapter();
        delegateAdapter.addAdapter(liveMainVideoAdapter);
        liveMainEmptyAdapter = new LiveMainEmptyAdapter();
        delegateAdapter.addAdapter(liveMainEmptyAdapter);
        liveMainEmptyAdapter.setType(get("emptyType"));
    }

    @Override
    public void onSucessGetLiveList(List<LiveVideoMain> result, PageInfoEarly pageInfoEarly) {
        showContent();
        if (result == null) {
            return;
        }
        if (result.size() == 0) {
            //System.out.println("目前推荐的长度");
            refreshLayout.finishLoadMoreWithNoMoreData();
            if (page == 1 || page == 0) {
                liveMainEmptyAdapter.setModel("暂无数据");
                liveMainVideoAdapter.setModel(null);
            }
        } else {
            liveMainEmptyAdapter.setModel(null);
            for (int i = 0; i < result.size(); i++) {
                LiveGoodsPresenter liveGoodsPresenter = new LiveGoodsPresenter(mContext, this);
                if (result.get(i).activityIdList != null) {
                    liveGoodsPresenter.getLiveGoods(result.get(i), new SimpleHashMapBuilder<String, Object>().puts("liveActivityIds", result.get(i).activityIdList));
                } else {
                    result.get(i).liveVideoGoodsList = new ArrayList<>();
                }

            }

            if (page == 1) {
                liveMainVideoAdapter.setData((ArrayList<LiveVideoMain>) result);
                //System.out.println("目前推荐的长度设置数据");
            } else {
                liveMainVideoAdapter.addDatas((ArrayList<LiveVideoMain>) result);
            }
            if (pageInfoEarly.nextPage == 0) {
                refreshLayout.finishLoadMoreWithNoMoreData();
            } else {
                refreshLayout.setNoMoreData(false);
                refreshLayout.setEnableLoadMore(true);
            }
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    @Override
    public void onSucessGetLiveGoods(List<LiveVideoGoods> liveVideoGoods) {
        liveMainVideoAdapter.notifyDataSetChanged();
    }
}
