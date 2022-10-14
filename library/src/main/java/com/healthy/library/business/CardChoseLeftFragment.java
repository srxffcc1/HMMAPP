package com.healthy.library.business;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.healthy.library.R;
import com.healthy.library.adapter.CardAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.interfaces.IHmmCoupon;
import com.healthy.library.widget.StatusLayout;

import java.util.List;
import java.util.Map;

public class CardChoseLeftFragment extends BaseFragment {
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    CardAdapter cardCenterListAdapter;
    public int page = 1;
    private List<CouponInfoZ> list;
    List<IHmmCoupon> selectInfo;
    private CouponChoseDialog.OnCouponCheckChangeListener onCouponCheckChangeListener;

    public static CardChoseLeftFragment newInstance(Map<String, Object> maporg) {
        CardChoseLeftFragment fragment = new CardChoseLeftFragment();
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
        cardCenterListAdapter.setOnCouponCheckChangeListener(onCouponCheckChangeListener);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
//        recycler.setNestedScrollingEnabled(false);
        cardCenterListAdapter.bindToRecyclerView(recycler);
        cardCenterListAdapter.setAdapterType(3);
        cardCenterListAdapter.setSelectList(selectInfo);
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
    public void setAllCheckShow(boolean flag){
        cardCenterListAdapter.setAllcheckshow(flag);
        cardCenterListAdapter.notifyDataSetChanged();
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

    public List<IHmmCoupon> getResult() {
        return cardCenterListAdapter.getSelectInfo();
    }

    public void setSelectList(List<IHmmCoupon> selectInfo) {
        this.selectInfo=selectInfo;
    }

    public void setOnCouponCheckChangeListener(CouponChoseDialog.OnCouponCheckChangeListener onCouponCheckChangeListener) {
        this.onCouponCheckChangeListener = onCouponCheckChangeListener;
    }

    public CouponChoseDialog.OnCouponCheckChangeListener getOnCouponCheckChangeListener() {
        return onCouponCheckChangeListener;
    }
}
