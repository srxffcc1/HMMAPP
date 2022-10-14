package com.health.mine.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.health.mine.R;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class VipDepositFragment extends BaseFragment {

    private String cardNo;
    private String ytbAppId;
    private TabLayout depositTab;
    private ViewPager depositPager;
    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    private CanReplacePageAdapter pageAdapter;

    public VipDepositFragment() {

    }

    public static VipDepositFragment newInstance(String cardNo, String ytbAppId) {
        VipDepositFragment fragment = new VipDepositFragment();
        Bundle args = new Bundle();
        args.putString("cardNo", cardNo);
        args.putString("ytbAppId", ytbAppId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cardNo = getArguments().getString("cardNo");
            ytbAppId = getArguments().getString("ytbAppId");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vip_deposit;
    }

    @Override
    protected void findViews() {
        depositTab = (TabLayout) findViewById(R.id.deposit_tab);
        depositPager = (ViewPager) findViewById(R.id.deposit_pager);
        initView();
    }

    private void initView() {
        mTitles = new ArrayList<>();
        mFragmentList = new ArrayList<>();
        mTitles.add("商品");
        mTitles.add("服务");
        mFragmentList.add(VipDepositChildFragment.newInstance(cardNo, ytbAppId, "1"));
        mFragmentList.add(VipDepositChildFragment.newInstance(cardNo, ytbAppId, "0"));
        depositPager.setOffscreenPageLimit(mFragmentList.size() - 1);
        pageAdapter = new CanReplacePageAdapter(getChildFragmentManager(), mFragmentList, mTitles);
        depositPager.setAdapter(pageAdapter);
        depositTab.setupWithViewPager(depositPager);
    }
}