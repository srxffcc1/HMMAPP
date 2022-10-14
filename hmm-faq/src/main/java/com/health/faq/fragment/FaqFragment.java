package com.health.faq.fragment;

import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.health.faq.R;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Events;
import com.healthy.library.routes.AppRoutes;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/06/28 10:47
 * @des
 */
@Route(path = AppRoutes.MODULE_FAQ)
public class FaqFragment extends BaseFragment {

    private TabLayout mTabLayout;
    public ViewPager mPagerFaq;
    private List<Fragment> mFragmentList;
    private List<String> mTitleList;

    public static FaqFragment newInstance() {
        FaqFragment fragment = new FaqFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_faq;
    }

    @Override
    protected void findViews() {
        mTabLayout = getContentView().findViewById(R.id.tab);
        mPagerFaq = getContentView().findViewById(R.id.pager_faq);
        //System.out.println("跳问题创建3");
        mTitleList = new ArrayList<>();
        mTitleList.add("最新求助");
        mTitleList.add("热门求助");
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new LatestQuestionFragment());
        mFragmentList.add(new HotQuestionFragment());
        mPagerFaq.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragmentList.get(i);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitleList.get(position);
            }
        });
        mTabLayout.setupWithViewPager(mPagerFaq);
        mPagerFaq.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    MobclickAgent.onEvent(mContext, Events.EVENT_HOT_QUESTION);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    protected void onFirstVisible() {
        super.onFirstVisible();
    }

}
