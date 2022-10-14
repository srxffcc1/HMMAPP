package com.health.index.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.index.R;
import com.health.index.fragment.ToolsFeedSumFragment;
import com.health.index.fragment.ToolsFeedSumTimeFragment;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.widget.TopBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(path = IndexRoutes.INDEX_TOOLS_BABY_DIARY_SUM)
public class ToolsBabySumDiary extends BaseActivity implements IsFitsSystemWindows {
    private TopBar topBar;
    private com.flyco.tablayout.SlidingTabLayout st;
    private androidx.viewpager.widget.ViewPager vp;
    private List<Fragment> fragments = new ArrayList<>();
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    @Autowired
    int postion=-1;
    @Autowired
    String childId;
    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_diary_main_sum;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);

        List<String> titles = Arrays.asList("食物","药物","便便","睡觉");
        fragments.add(ToolsFeedSumFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("childId",childId).puts("type","1")));
        fragments.add(ToolsFeedSumFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("childId",childId).puts("type","2")));
        fragments.add(ToolsFeedSumFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("childId",childId).puts("type","3")));
        fragments.add(ToolsFeedSumTimeFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("childId",childId).puts("type","4")));


        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), fragments, titles);
            // adapter
            vp.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        vp.setOffscreenPageLimit(fragments.size());
        // bind to SmartTabLayout
        st.setViewPager(vp);
        vp.setCurrentItem(postion==-1?0:postion);
        st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                postion = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_TOOLS_BABY_DIARY_SUM_CHART)
                        .withString("childId",childId)
                        .navigation();
            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }


    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        st = (SlidingTabLayout) findViewById(R.id.st);
        vp = (ViewPager) findViewById(R.id.vp);
    }
}
