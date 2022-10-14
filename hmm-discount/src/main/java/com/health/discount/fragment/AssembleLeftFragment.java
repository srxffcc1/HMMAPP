//package com.health.discount.fragment;
//
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.health.discount.R;
//import com.health.discount.adapter.GroupListAdapter;
//import com.health.discount.contract.GroupListContract;
//import com.healthy.library.model.KKGroup;
//import com.health.discount.presenter.GroupListPresenter;
//import com.healthy.library.base.BaseFragment;
//import com.healthy.library.model.PageInfoEarly;
//import com.healthy.library.widget.StatusLayout;
//import com.scwang.smart.refresh.layout.SmartRefreshLayout;
//import com.scwang.smart.refresh.layout.api.RefreshLayout;
//import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
//
//import java.io.Serializable;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class AssembleLeftFragment extends BaseFragment implements GroupListContract.View, OnRefreshLoadMoreListener {
//    GroupListAdapter groupListAdapter;
//    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
//    private com.healthy.library.widget.StatusLayout layoutStatus;
//    private RecyclerView recycler;
//    GroupListPresenter groupListPresenter;
//    Map<String,Object> listmap=new HashMap<>();
//    private int page=1;
//    @Override
//    protected int getLayoutId() {
//        return R.layout.dis_fragment_group_left;
//    }
//
//    @Override
//    protected void findViews() {
//        initView();
//        groupListAdapter=new GroupListAdapter();
//        groupListPresenter=new GroupListPresenter(mContext,this);
//        recycler.setLayoutManager(new LinearLayoutManager(mContext));
//        groupListAdapter.bindToRecyclerView(recycler);
//        groupListAdapter.setLocation(get("areaNo").toString(),get("lat").toString(),get("lng").toString());
//        layoutRefresh.setOnRefreshLoadMoreListener(this);
//        getData();
//    }
//
//    @Override
//    public void getData() {
//        super.getData();
//        listmap.put("addressProvince", (Integer.parseInt(get("areaNo").toString()) / 10000 * 10000) + "");
//        listmap.put("pageSize", "10");
//        listmap.put("currentPage",page+"");
//        listmap.put("longitude", get("lng").toString());
//        listmap.put("latitude", get("lat").toString());
//        groupListPresenter.getGroupList(listmap);
//    }
//
//    public static AssembleLeftFragment newInstance(Map<String, Object> maporg) {
//        AssembleLeftFragment fragment = new AssembleLeftFragment();
//        Bundle args = new Bundle();
//        for (Map.Entry<String, Object> entry : maporg.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            if (value instanceof Integer) {
//                args.putInt(key, (Integer) value);
//            } else if (value instanceof Boolean) {
//                args.putBoolean(key, (Boolean) value);
//            } else if (value instanceof String) {
//                args.putString(key, (String) value);
//            } else {
//                args.putSerializable(key, (Serializable) value);
//            }
//        }
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onSuccessGetGroupList(List<KKGroup> kickList, PageInfoEarly pageInfoEarly) {
//        if (pageInfoEarly == null) {
//            showEmpty();
//            layoutRefresh.setEnableLoadMore(false);
//            return;
//        }
//        page = pageInfoEarly.currentPage;
//        if (page == 1) {
//            groupListAdapter.setNewData(kickList);
//            if (kickList.size() == 0) {
//                showEmpty();
//            }
//        } else {
//            groupListAdapter.addData(kickList);
//        }
//        if (pageInfoEarly.isMore != 1) {
//
//            layoutRefresh.finishLoadMoreWithNoMoreData();
//        } else {
//            layoutRefresh.setNoMoreData(false);
//            layoutRefresh.setEnableLoadMore(true);
//        }
//    }
//
//    @Override
//    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//        page++;
//        listmap.put("currentPage",page+"");
//        groupListPresenter.getGroupList(listmap);
//    }
//
//    @Override
//    public void onRequestFinish() {
//        super.onRequestFinish();
//        layoutRefresh.finishRefresh();
//        layoutRefresh.finishLoadMore();
//    }
//
//    @Override
//    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//        page=1;
//        listmap.put("currentPage",page+"");
//        groupListPresenter.getGroupList(listmap);
//    }
//
//    private void initView() {
//        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
//        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
//        recycler = (RecyclerView) findViewById(R.id.recycler);
//    }
//}
