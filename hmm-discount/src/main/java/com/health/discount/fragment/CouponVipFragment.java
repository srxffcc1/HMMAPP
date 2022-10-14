package com.health.discount.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.health.discount.R;
import com.health.discount.contract.CouponVipListContract;
import com.health.discount.model.CouponVip;
import com.health.discount.presenter.CouponVipListPresenter;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.model.BalanceModel;
import com.healthy.library.model.PageInfoEarly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CouponVipFragment extends BaseFragment implements CouponVipListContract.View {

    private TabLayout tab;
    private ViewPager pager;
    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    private CouponVipListPresenter vipDepositPresenter;
    private CanReplacePageAdapter pageAdapter;

    public static CouponVipFragment newInstance(Map<String, Object> maporg) {
        CouponVipFragment fragment = new CouponVipFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_coupon_vip;
    }

    @Override
    protected void findViews() {
        tab = findViewById(R.id.tab);
        pager = findViewById(R.id.pager);
        vipDepositPresenter = new CouponVipListPresenter(mContext, this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        vipDepositPresenter.getBalanceList();
    }

    @Override
    public void onGetBalanceListSuccess(List<BalanceModel> list) {
        if (list != null && list.size() > 0) {
            mTitles = new ArrayList<>();
            mFragmentList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                BalanceModel model = list.get(i);
                mTitles.add(String.format("会员卡%s", i + 1));
                mFragmentList.add(CouponVipChildFragment
                        .newInstance(model.CardNo, model.ytbAppId));
            }
            pager.setOffscreenPageLimit(mFragmentList.size() - 1);
            pageAdapter = new CanReplacePageAdapter(getChildFragmentManager(), mFragmentList, mTitles);
            pager.setAdapter(pageAdapter);
            tab.setupWithViewPager(pager);
        } else {
            showEmpty();
        }
    }

    @Override
    public void onSuccessGetCouponList(List<CouponVip> coupons, PageInfoEarly pageInfoEarly) {

    }
}