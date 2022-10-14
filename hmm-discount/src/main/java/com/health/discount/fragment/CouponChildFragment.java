package com.health.discount.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.discount.R;
import com.healthy.library.adapter.CardAdapter;
import com.health.discount.contract.CouponListContract;
import com.health.discount.presenter.CouponListPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.SpKey;
import com.healthy.library.message.UpdateCheckInfoBackReMsg;
import com.healthy.library.message.UpdateCheckInfoMsg;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponChildFragment extends BaseFragment implements CouponListContract.View, OnRefreshLoadMoreListener {
    private SmartRefreshLayout layoutRefresh;
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    private CardAdapter couponAdapter;
    private CouponListPresenter couponListPresenter;
    private int mCurrentPage = 1;
    private String couponType = "1";//服务券，母婴券，问答券


    public static CouponChildFragment newInstance(Map<String, Object> maporg) {
        CouponChildFragment fragment = new CouponChildFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dis_fragment_child_coupon;
    }

    @Override
    protected void findViews() {
        initView();
        couponAdapter = new CardAdapter();
        couponAdapter.setAdapterType((int) get("type"));
        couponListPresenter = new CouponListPresenter(mContext, this);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        couponAdapter.bindToRecyclerView(recycler);
        couponType = get("couponType");
        couponAdapter.setNowtype(Integer.parseInt(couponType));
        getBasemap().put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        getBasemap().put("status", get("status").toString().split(","));
        getBasemap().put("couponType", get("couponType"));
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        getData();


    }

    @Override
    public void getData() {
        super.getData();
        getBasemap().put("page", mCurrentPage + "");
        getBasemap().put("pageSize", 10 + "");
        couponListPresenter.getCouponList(getBasemap());
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurrentPage = 1;
        getData();
    }

    private void initView() {
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
    }

    public void setAllCheckShow(boolean allcheckshow) {
        try {
            couponAdapter.setAllcheckshow(allcheckshow);
            couponAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAllCheck(boolean allcheckshow) {
        try {
            couponAdapter.setAllCheck(allcheckshow);
            couponAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCoupon() {
        if (!TextUtils.isEmpty(couponAdapter.getCheckIds())) {
            Map<String, Object> map = new HashMap<>();
            map.put("idList", couponAdapter.getCheckIds().split(","));
            couponListPresenter.deleteCouponS(map);
        } else {
            Toast.makeText(mContext, "未选择优惠券", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onSuccessGetCouponList(List<CouponInfoZ> coupons, PageInfoEarly pageInfoEarly) {
        if (coupons == null) {
            return;
        }
        for (int i = 0; i < coupons.size(); i++) {
            coupons.get(i).couponImg = CodeUtils.createImage("veryCouponId=" + coupons.get(i).id, 350, 350, null);
        }
        mCurrentPage = pageInfoEarly.pageNum;
        if (mCurrentPage == 1 || mCurrentPage == 0) {
            couponAdapter.setNewData(coupons);
            if (coupons.size() == 0) {
                showEmpty();
            }
        } else {
            couponAdapter.addData(coupons);
        }
        if (pageInfoEarly.nextPage == 0) {
            //System.out.println("没有更多了");
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void onSucessDelete() {
        couponAdapter.setAllcheckshow(false);
        EventBus.getDefault().post(new UpdateCheckInfoBackReMsg(false));
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage = 1;
        getData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateCheckInfoMsg msg) {
        setAllCheckShow(msg.flag);
    }
}
