package com.health.tencentlive.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.health.tencentlive.R;
import com.health.tencentlive.fragment.LiveNoticeFragment;
import com.health.tencentlive.fragment.LiveVideoListFragment;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.message.LiveHistoryInfo;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.widget.SlidingTabLayout;
import com.healthy.library.widget.TopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(path = TencentLiveRoutes.LiveHostLiveList)
public class LiveMineListActivity extends BaseActivity implements IsFitsSystemWindows {

    @Autowired
    String anchormanId;
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.SlidingTabLayout slidingTabLayout;
    private androidx.viewpager.widget.ViewPager vp;
    @Autowired
    int currentIndex = 0;
    List<String> titles = Arrays.asList("直播预告", "往期直播");
    private List<Fragment> fragments = new ArrayList<>();
    private CanReplacePageAdapter fragmentPagerItemAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_minelist;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
        fragments.add(LiveNoticeFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId)));
        fragments.add(LiveVideoListFragment.newInstance(new SimpleHashMapBuilder<String, Object>()
                .puts("statusList", "4")
                .puts("anchormanId", anchormanId)));
        fragmentPagerItemAdapter = new CanReplacePageAdapter(getSupportFragmentManager(), fragments, titles);
        // adapter
        vp.setAdapter(fragmentPagerItemAdapter);
        fragmentPagerItemAdapter.notifyDataSetChanged();
        vp.setOffscreenPageLimit(fragments.size() - 1);

        slidingTabLayout.setupWithViewPager(vp);
        for (int i = 0; i < slidingTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = slidingTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
                if (tab.getCustomView() != null) {
                    View tabView = (View) tab.getCustomView().getParent();
                    tabView.setTag(i);
                }
            }
        }
        vp.setCurrentItem(currentIndex);
        slidingTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView tv_tab = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_title);
                tv_tab.setTextColor(Color.parseColor("#FF535E"));
                tv_tab.getPaint().setFakeBoldText(true);//加粗
                tv_tab.setTextSize(17);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tv_tab = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_title);
                tv_tab.setTextColor(Color.parseColor("#999999"));
                tv_tab.getPaint().setFakeBoldText(false);//加粗
                tv_tab.setTextSize(15);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                slidingTabLayout.redrawIndicator(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        slidingTabLayout.resetTabParams();
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_item, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_tab_title);
        tv.setTextColor(position == currentIndex ? Color.parseColor("#FF535E") : Color.parseColor("#999999"));
        tv.getPaint().setFakeBoldText(position == currentIndex ? true : false);//加粗
        tv.setText(titles.get(position));
        return view;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {

        topBar = (TopBar) findViewById(R.id.top_bar);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingTabLayout);
        vp = (ViewPager) findViewById(R.id.vp);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(LiveHistoryInfo msg) {
        vp.setCurrentItem(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
