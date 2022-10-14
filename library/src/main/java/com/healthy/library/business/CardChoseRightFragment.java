package com.healthy.library.business;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.healthy.library.R;
import com.healthy.library.adapter.CardAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.widget.StatusLayout;

import java.util.List;
import java.util.Map;

public class CardChoseRightFragment extends BaseFragment  {
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    CardAdapter cardCenterListAdapter;
    public int page = 1;
    private List<CouponInfoZ> list;

    public static CardChoseRightFragment newInstance(Map<String, Object> maporg) {
        CardChoseRightFragment fragment = new CardChoseRightFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.dis_fragment_child_coupon_single;
    }

    @Override
    protected void findViews() {
        initView();
        cardCenterListAdapter=new CardAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
//        recycler.setNestedScrollingEnabled(false);
        cardCenterListAdapter.bindToRecyclerView(recycler);
        cardCenterListAdapter.setAdapterType(4);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        showContent();
        if(list==null||list.size()==0){
            showEmpty();
        }else {
            cardCenterListAdapter.setNewData(list);
        }
    }
    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
    }

    private void initView() {
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    public void setList(List<CouponInfoZ> list) {
        this.list = list;
    }

    public List<CouponInfoZ> getList() {
        return list;
    }
}
