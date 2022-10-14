package com.health.discount.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.health.discount.R;
import com.health.discount.contract.CouponVipListContract;
import com.health.discount.model.CouponVip;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.message.UpdateCheckTabCoupon;
import com.healthy.library.model.BalanceModel;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.widget.StatusLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CouponFragment extends BaseFragment implements CouponVipListContract.View {


    private StatusLayout layoutStatus;
    private TabLayout tab;
    private ViewPager pager;
    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    private CanReplacePageAdapter pageAdapter;
    CouponChildFragment couponChildLeftFragment;
    //    CouponChildFragment couponChildRightFragment;
    @Override
    public void onGetBalanceListSuccess(List<BalanceModel> list) {

    }
    public static CouponFragment newInstance(Map<String, Object> maporg) {
        CouponFragment fragment = new CouponFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onSuccessGetCouponList(List<CouponVip> coupons, PageInfoEarly pageInfoEarly) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_coupon_vip;
    }

    @Override
    protected void findViews() {
        initView();
        getData();
    }
    @Override
    public void getData() {
        super.getData();
        mTitles = new ArrayList<>();
        mFragmentList = new ArrayList<>();
        mTitles.add("商家券");
//        mTitles.add("平台券");
        couponChildLeftFragment= CouponChildFragment.newInstance(new SimpleHashMapBuilder().puts("type",get("type")).puts("couponType","2").puts("status",get("status")));
//        couponChildRightFragment= CouponChildFragment.newInstance(new SimpleHashMapBuilder().puts("type",get("type")).puts("couponType","1").puts("status",get("status")));
        mFragmentList.add(couponChildLeftFragment);
//        mFragmentList.add(couponChildRightFragment);
        pager.setOffscreenPageLimit(mFragmentList.size() - 1);
        pageAdapter = new CanReplacePageAdapter(getChildFragmentManager(), mFragmentList, mTitles);
        pager.setAdapter(pageAdapter);
        tab.setupWithViewPager(pager);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                EventBus.getDefault().post(new UpdateCheckTabCoupon(false));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tab.setVisibility(View.GONE);
        showContent();
    }

    private void initView() {
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        tab = (TabLayout) findViewById(R.id.tab);
        pager = (ViewPager) findViewById(R.id.pager);
    }

    public void setAllCheckShow(boolean allcheckshow) {
        couponChildLeftFragment.setAllCheckShow(allcheckshow);
//        couponChildRightFragment.setAllCheckShow(allcheckshow);
    }

    public void setAllCheck(boolean allcheckshow) {
        ((CouponChildFragment) (mFragmentList.get(tab.getSelectedTabPosition()))).setAllCheck(allcheckshow);

    }
    public void deleteCoupon(){
        ((CouponChildFragment) (mFragmentList.get(tab.getSelectedTabPosition()))).deleteCoupon();
    }
}
