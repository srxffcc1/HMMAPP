package com.health.index.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.health.index.R;
import com.health.index.fragment.HanMomVideoFindChildFragment;
import com.health.index.fragment.IndexToolsMainFragment;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.model.VideoCategory;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.widget.HanMomVideoSlidingTabLayout;
import com.healthy.library.widget.IndexToolsSlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

@Route(path = IndexRoutes.INDEX_INDEXTOOLSMAIN)
public class IndexToolsMainActivity extends BaseActivity {

    @Autowired
    String planId;

    private IndexToolsSlidingTabLayout mTabLayout_3;
    private ViewPager tabViewPager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
    private CanReplacePageAdapter fragmentPagerItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_index_tools_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setTab();
    }

    @Override
    protected void findViews() {
        super.findViews();
        mTabLayout_3 = (IndexToolsSlidingTabLayout) findViewById(R.id.tl_3);
        tabViewPager = (ViewPager) findViewById(R.id.tabViewPager);
    }

    private void setTab() {
        mTitles.clear();
        mFragments.clear();

        mTitles.add("备孕");
        mTitles.add("怀孕");
        mTitles.add("育儿");
        mTitles.add("其他");
        mFragments.add(IndexToolsMainFragment.newInstance(planId, "1"));
        mFragments.add(IndexToolsMainFragment.newInstance(planId, "2"));
        mFragments.add(IndexToolsMainFragment.newInstance(planId, "3"));
        mFragments.add(IndexToolsMainFragment.newInstance(planId, "-1"));

        fragmentPagerItemAdapter = new CanReplacePageAdapter(getSupportFragmentManager(), mFragments, mTitles);
        // adapter
        tabViewPager.setAdapter(fragmentPagerItemAdapter);
        fragmentPagerItemAdapter.notifyDataSetChanged();
        tabViewPager.setOffscreenPageLimit(mFragments.size() - 1);
        mTabLayout_3.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == null || tab.getText() == null) {
                    return;
                }
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
                spStr.setSpan(new AbsoluteSizeSpan(16, true), 0, trim.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                spStr.setSpan(new AbsoluteSizeSpan(14, true), 0, trim.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spStr.setSpan(styleSpan_B, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTabLayout_3.redrawIndicator(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout_3.setupWithViewPager(tabViewPager);
        tabViewPager.setCurrentItem(0);
        mTabLayout_3.resetTabParams();
    }
}