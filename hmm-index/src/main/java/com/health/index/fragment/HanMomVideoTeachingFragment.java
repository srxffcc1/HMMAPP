package com.health.index.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.health.index.R;
import com.health.index.contract.HanMomVideoContract;
import com.healthy.library.model.VideoListModel;
import com.health.index.presenter.HanMomVideoPresenter;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.VideoCategory;
import com.healthy.library.widget.HanMomVideoSlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class HanMomVideoTeachingFragment extends BaseFragment  implements HanMomVideoContract.View  {

    private String mParam1;
    private String mParam2;

    private HanMomVideoSlidingTabLayout mTabLayout_3;
    private ViewPager tabViewPager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
    private CanReplacePageAdapter fragmentPagerItemAdapter;
    private HanMomVideoPresenter hanMomVideoPresenter;

    public HanMomVideoTeachingFragment() {

    }

    public static HanMomVideoTeachingFragment newInstance(String param1, String param2) {
        HanMomVideoTeachingFragment fragment = new HanMomVideoTeachingFragment();
        Bundle args = new Bundle();
        args.putString("mParam1", param1);
        args.putString("mParam2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("mParam1");
            mParam2 = getArguments().getString("mParam2");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_han_mom_video_teaching;
    }

    @Override
    protected void findViews() {
        mTabLayout_3 = (HanMomVideoSlidingTabLayout) findViewById(R.id.tl_3);
        tabViewPager = (ViewPager) findViewById(R.id.tabViewPager);
        hanMomVideoPresenter = new HanMomVideoPresenter(mContext, this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        hanMomVideoPresenter.getTabList(new SimpleHashMapBuilder<String, Object>().puts("scope", "1"));
    }

    private void setTab(final List<VideoCategory> result) {
        for (int i = 0; i < result.size(); i++) {
            mTitles.add(result.get(i).categoryName);
            mFragments.add(HanMomVideoTeachingChildFragment.newInstance(result.get(i).categoryCode,result.get(i).children));
        }
        if (!isAdded()) return;
        fragmentPagerItemAdapter = new CanReplacePageAdapter(getChildFragmentManager(), mFragments, mTitles);
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
                spStr.setSpan(new AbsoluteSizeSpan(18, true), 0, trim.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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

    @Override
    public void onGetTabListSuccess(List<VideoCategory> result) {
        showContent();
        if (result != null && result.size() > 0) {
            setTab(result);
        } else {
            showEmpty();
        }
    }

    @Override
    public void onGetVideoListSuccess(List<VideoListModel> result, PageInfoEarly pageInfoEarly) {

    }

    @Override
    public void onGetVideoDetailSuccess(VideoListModel result,int type) {

    }

    @Override
    public void onAddPraiseSuccess(String result, int type) {

    }

}