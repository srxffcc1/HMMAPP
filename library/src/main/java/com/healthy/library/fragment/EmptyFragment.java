package com.healthy.library.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.healthy.library.R;
import com.healthy.library.adapter.EmptyFragmentAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmptyFragment extends BaseFragment implements OnRefreshLoadMoreListener {

    public int page = 1;
    private SmartRefreshLayout refreshLayout;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerQuestion;
    EmptyFragmentAdapter emptyFragmentAdapter;

    public static EmptyFragment newInstance(Map<String, Object> maporg) {
        EmptyFragment fragment = new EmptyFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }
    public static EmptyFragment newInstance(int layoutRes) {
        EmptyFragment fragment = new EmptyFragment();
        Bundle args = new Bundle();
        args.putInt("layoutRes",layoutRes);
        BaseFragment.bundleMap(null, args);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        if(get("layoutRes")!=null){
            return (int) get("layoutRes");
        }
        return R.layout.layout_empty_fragment;
    }

    @Override
    protected void findViews() {
        initView();
        emptyFragmentAdapter=new EmptyFragmentAdapter();
        recyclerQuestion.setLayoutManager(new LinearLayoutManager(mContext));
        emptyFragmentAdapter.bindToRecyclerView(recyclerQuestion);
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.setEnableRefresh(false);
        getData();
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
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    @Override
    public void getData() {
        super.getData();
        showContent();
         List<String> emptylist=new ArrayList<>();
        emptylist.clear();
        for (int i = 0; i <20 ; i++) {
            emptylist.add(""+i);
        }
        if(page==1||page==0){
            emptyFragmentAdapter.setNewData(emptylist);
            onRequestFinish();
        }else {
            emptyFragmentAdapter.addData(emptylist);
            onRequestFinish();
        }
    }

    private void initView() {
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerQuestion = (RecyclerView) findViewById(R.id.recycler_question);
    }
}
