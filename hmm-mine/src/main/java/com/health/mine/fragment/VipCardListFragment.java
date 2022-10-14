package com.health.mine.fragment;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.mine.R;
import com.health.mine.adapter.VipCardFragmentAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/04/10 09:29
 * @des 订单列表
 */

public class VipCardListFragment extends BaseFragment implements
        OnRefreshLoadMoreListener {

    public int page = 1;

    private SmartRefreshLayout layoutRefresh;
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    VipCardFragmentAdapter vipCardFragmentAdapter;
    public static VipCardListFragment newInstance(String type) {
        Bundle args = new Bundle();
        VipCardListFragment fragment = new VipCardListFragment();
        fragment.setArguments(args);
        args.putString("type", type);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_fragment_vipcard;
    }

    @Override
    protected void findViews() {
        initView();
        vipCardFragmentAdapter=new VipCardFragmentAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        vipCardFragmentAdapter.bindToRecyclerView(recycler);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
//        layoutRefresh.setEnableRefresh(false);
        recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(final @NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (e != null) {
                    //找到被点击位置的item的rootView
                    View view = rv.findChildViewUnder(e.getX(), e.getY());
                    if (view != null) {
                        VipCardFragmentAdapter.ScrollViewHolder holder = (VipCardFragmentAdapter.ScrollViewHolder) rv.getChildViewHolder(view);
                        //由ViewHolder决定要不要请求不拦截,如果不拦截的话event就回一路传到rootView中.否则被rv消费.
                        rv.requestDisallowInterceptTouchEvent(holder.isTouchNsv(e.getRawX(), e.getRawY()));
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
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
        layoutRefresh.finishLoadMore();
        layoutRefresh.finishRefresh();
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
            vipCardFragmentAdapter.setNewData(emptylist);
            onRequestFinish();
        }else {
            vipCardFragmentAdapter.addData(emptylist);
            onRequestFinish();
        }
    }
    private void initView() {
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }
}