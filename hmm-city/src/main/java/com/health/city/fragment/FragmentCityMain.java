package com.health.city.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.tabs.TabLayout;
import com.health.city.R;
import com.health.city.widget.IndicatorView;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.message.UpdateGuideInfo;
import com.healthy.library.message.UpdateGuideInfoTotal;
import com.healthy.library.message.UpdateMessageInfo;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.utils.CheckUtils;
import com.healthy.library.utils.TransformUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = AppRoutes.CITY_MODULE)
public class FragmentCityMain extends BaseFragment {
    private TabLayout st;
    private android.widget.ImageView passMessage;
    private ViewPager vp;
    private AppCompatImageView searchImg;
    private FragmentPostFocusList leftfragmentPostList;
    private FragmentPostNewList centerfragmentPostList;
    private RankingListFragment rankingListFragment;
    //    private FragmentPostNewList rightfragmentPostList;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    private int currentIndex = 1;
    private List<String> titles = Arrays.asList("关注", "本地", "排行榜");
    private Map<String, Object> leftmap = new HashMap<>();
    private Map<String, Object> centermap = new HashMap<>();
    private ConstraintLayout mSearch;
    private IndicatorView indicatorView;


    @Override
    protected int getLayoutId() {
        return R.layout.city_fragment_main;
    }

    @Override
    protected void onLazyData() {
        super.onLazyData();
    }

    @Override
    protected void findViews() {
        initView();
//        List<String> titles = Arrays.asList("关注", "本地","发现");
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("event2CityHomeTimeLimit");//统计页面，"MainScreen"为页面名称，可自定义
        CheckUtils.checkMessageCount(mContext, passMessage);
//        if(passMessage != null) {
//            CheckUtils.checkMessageCount(mContext, passMessage);
//        }
//        EventBus.getDefault().post(new UpdateGuideInfo(4));
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("event2CityHomeTimeLimit");//统计

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTab(UpdateMessageInfo msg) {

        CheckUtils.checkMessageCount(mContext, passMessage);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void guideInfo(UpdateGuideInfo msg) {
        if (msg.flag == 4) {
            EventBus.getDefault().post(new UpdateGuideInfoTotal(44));
        }

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

    private void initView() {
        st = (TabLayout) findViewById(R.id.tab);
        searchImg = (AppCompatImageView) findViewById(R.id.searchImg);
        indicatorView = (IndicatorView) findViewById(R.id.indicatorView);

        mSearch = findViewById(R.id.search);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(LibraryRoutes.LIBRARY_HMM_SEARCH)
                        .withInt("searchType", 1)
                        .navigation();
            }
        });
        passMessage = (ImageView) findViewById(R.id.passMessage);
        vp = (ViewPager) findViewById(R.id.vp);
        passMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(AppRoutes.APP_MESSAGE)
                        .navigation();
            }
        });
        leftmap.put("type", "0");
        leftfragmentPostList = FragmentPostFocusList.newInstance(leftmap);
        centermap.put("type", "1");
        centerfragmentPostList = FragmentPostNewList.newInstance(centermap);
        rankingListFragment = RankingListFragment.newInstance("", "");
//        Map<String, Object> rightmap=new HashMap<>();
//        rightmap.put("type","2");
//        rightfragmentPostList=FragmentPostNewList.newInstance(rightmap);
        fragments.add(leftfragmentPostList);
        fragments.add(centerfragmentPostList);
        fragments.add(rankingListFragment); //  18号提测先隐藏排行榜
//        fragments.add(rightfragmentPostList);
        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getChildFragmentManager(), fragments, titles);
            // adapter
            vp.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        vp.setOffscreenPageLimit(fragments.size());
        // bind to SmartTabLayout
        st.setupWithViewPager(vp);
        vp.setCurrentItem(currentIndex);
        for (int i = 0; i < st.getTabCount(); i++) {
            TabLayout.Tab tab = st.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
                if (tab.getCustomView() != null) {
                    View tabView = (View) tab.getCustomView().getParent();
                    tabView.setTag(i);
                }
            }
        }
        indicatorView.setIndicatorWidth(TransformUtil.dp2px(mContext, 20f));
        // 指示器颜色
        indicatorView.setIndicatorColor(Color.parseColor("#FF6060"));
        // 指示器绑定TabLayout
        indicatorView.setupWithTabLayout(st);
        st.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentIndex = tab.getPosition();
                TextView tv_tab = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_title);
                tv_tab.getPaint().setFakeBoldText(true);//加粗
                if (tab.getPosition() == 2) {
                    tv_tab.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    tv_tab.setTextColor(Color.parseColor("#222222"));
                }
                tv_tab.setTextSize(18);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                for (int i = 0; i < st.getTabCount(); i++) {
                    TabLayout.Tab sTab = st.getTabAt(i);
                    TextView tv_tab = (TextView) sTab.getCustomView().findViewById(R.id.tv_tab_title);
                    tv_tab.getPaint().setFakeBoldText(false);
                    tv_tab.setTextSize(16);
                    if (sTab != null && st.getSelectedTabPosition() == 2) {
                        tv_tab.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        tv_tab.setTextColor(Color.parseColor("#222222"));
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    indicatorView.setIndicatorColor(Color.parseColor("#FFFFFF"));
                    passMessage.setImageResource(R.drawable.mine_right_message);
                    searchImg.setImageResource(R.drawable.ic_search_white);
                } else {
                    indicatorView.setIndicatorColor(Color.parseColor("#FF6060"));
                    passMessage.setImageResource(R.drawable.index_message);
                    searchImg.setImageResource(R.drawable.ic_search_black);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        showContent();
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_tab_item, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_tab_title);
        if (position == currentIndex) {
            tv.getPaint().setFakeBoldText(true);//加粗
            if (position == 2) {
                tv.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                tv.setTextColor(Color.parseColor("#222222"));
            }
            tv.setTextSize(18);
        } else {
            tv.getPaint().setFakeBoldText(false);
            if (position == 2) {
                tv.setTextColor(Color.parseColor("#222222"));
            } else {
                tv.setTextColor(Color.parseColor("#9596A4"));
            }
            tv.setTextSize(16);
        }
        tv.setText(titles.get(position));
        return view;
    }
}
