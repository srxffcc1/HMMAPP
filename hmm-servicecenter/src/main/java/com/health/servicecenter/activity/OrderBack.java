package com.health.servicecenter.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.health.servicecenter.R;
import com.health.servicecenter.fragment.OrderBackListFragment;
import com.health.servicecenter.utils.ActivityManager;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Constants;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.widget.TopBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = ServiceRoutes.SERVICE_ORDERBACK)
public class OrderBack extends BaseActivity {
    @Autowired
    String type;

    private TopBar mTopBar;
    private TabLayout mTabLayout;
    private ViewPager mPager;
    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    private CanReplacePageAdapter pageAdapter;

    @Override
    protected void findViews() {
        super.findViews();
        mTabLayout = findViewById(R.id.tab);
        mTopBar = findViewById(R.id.top_bar);
        mPager = findViewById(R.id.pager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_back;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTopBar(mTopBar);

        mTitles = Arrays.asList("全部", "待处理");
        mFragmentList = new ArrayList<>();
        mFragmentList.add(OrderBackListFragment
                .newInstance("-1"));

        mFragmentList.add(OrderBackListFragment
                .newInstance("0"));

        mPager.setOffscreenPageLimit(mFragmentList.size() - 1);
        pageAdapter = new CanReplacePageAdapter(getSupportFragmentManager(), mFragmentList, mTitles);
        mPager.setAdapter(pageAdapter);

        Map<String, Integer> map = new HashMap<>(2);
        map.put(Constants.ORDER_TYPE_ALL, 0);
        map.put(Constants.ORDER_TYPE_USE, 1);

        mPager.setCurrentItem(map.get(type) == null ? 0 : map.get(type), false);


        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == null || tab.getText() == null) {
                    return;
                }
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
                spStr.setSpan(styleSpan_B, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == null || tab.getText() == null) {
                    return;
                }
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan styleSpan_B = new StyleSpan(Typeface.NORMAL);
                spStr.setSpan(styleSpan_B, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mTabLayout.setupWithViewPager(mPager);
    }
}