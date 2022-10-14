package com.health.index.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.index.R;
import com.health.index.adapter.IndexToolsMainAdapter;
import com.health.index.contract.IndexToolsMainContract;
import com.health.index.presenter.IndexToolsMainPresenter;
import com.healthy.library.adapter.EmptyAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.AppIndexTopMarketing;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;


public class IndexToolsMainFragment extends BaseFragment implements IndexToolsMainContract.View, OnRefreshListener {

    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private RecyclerView recyclerview;
    private String planId;
    private String memberStatus;
    private IndexToolsMainAdapter indexToolsMainAdapter;
    private IndexToolsMainPresenter indexToolsMainPresenter;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private EmptyAdapter emptyAdapter;

    public IndexToolsMainFragment() {
        // Required empty public constructor
    }


    public static IndexToolsMainFragment newInstance(String param1, String param2) {
        IndexToolsMainFragment fragment = new IndexToolsMainFragment();
        Bundle args = new Bundle();
        args.putString("planId", param1);
        args.putString("memberStatus", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            planId = getArguments().getString("planId");
            memberStatus = getArguments().getString("memberStatus");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index_tools_main;
    }

    @Override
    protected void findViews() {
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        layoutRefresh.setOnRefreshListener(this);
        layoutRefresh.setEnableLoadMore(false);
        indexToolsMainPresenter = new IndexToolsMainPresenter(mContext, this);
        buildAdapter();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        indexToolsMainPresenter.getTools(new SimpleHashMapBuilder<String, Object>()
                .puts("planId", planId)
                .puts("memberStatus", memberStatus)
                .puts("settingType", "2"));
    }

    private void buildAdapter() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerview.setLayoutManager(virtualLayoutManager);
        recyclerview.setAdapter(delegateAdapter);

        indexToolsMainAdapter = new IndexToolsMainAdapter();
        delegateAdapter.addAdapter(indexToolsMainAdapter);
        indexToolsMainAdapter.setType(memberStatus);

//        emptyAdapter = new EmptyAdapter();
//        delegateAdapter.addAdapter(emptyAdapter);
    }

    @Override
    public void onGetToolsSuccess(List<AppIndexTopMarketing> result) {
        layoutRefresh.finishRefresh();
        indexToolsMainAdapter.clear();
        if(SpUtils.getValue(mContext, SpKey.AUDITSTATUS,false)) {//true 为审核中 fasle为通过
            if(ChannelUtil.getChannel(mContext).contains("oppo")){
                for (int i = 0; i < result.size(); i++) {
                    if("专家答疑".equals(result.get(i).initialName)
                            ||result.get(i).initialName.contains("咨询")
                            ||result.get(i).initialName.contains("保险")){
                        result.remove(i);
                        i--;
                    }
                }
            }
        }
        if (ListUtil.isEmpty(result)) {
           showEmpty();
        } else {
            indexToolsMainAdapter.setList(result);
            indexToolsMainAdapter.setModel("");
        }
    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }
}