package com.health.sound.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.sound.R;
import com.health.sound.fragment.SoundListHistoryFragment;
import com.health.sound.fragment.SoundListSubFragment;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Route(path = SoundRoutes.SOUND_HISTORY)
public class SoundMainHistoryActivity extends BaseActivity implements IsFitsSystemWindows, OnRefreshLoadMoreListener {

    @Autowired
    String audioType;


    private List<Fragment> fragments = new ArrayList<>();
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
//    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;

    private SlidingTabLayout st;
    private View dividerStore;
    private ViewPager vp;
    private TopBar topBar;
    @Autowired
    int currentIndex;
    private ImageView imgBackTmp;
    private androidx.constraintlayout.widget.ConstraintLayout tabTop;
    private ImageView imgBackTmpRight;
    private SoundListSubFragment soundListSubFragment;
    private SoundListHistoryFragment soundListHistoryFragment;

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        onRefresh(null);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.sound_activity_history;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);

        soundListSubFragment = SoundListSubFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("audioType",audioType));
        soundListHistoryFragment = SoundListHistoryFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("audioType",audioType));
        List<String> titles = Arrays.asList("订阅","历史");
        fragments.clear();
        fragments.add(soundListSubFragment);
        fragments.add(soundListHistoryFragment);
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
        vp.setCurrentItem(currentIndex);
        st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentIndex = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

//        layoutRefresh.setOnRefreshLoadMoreListener(this);
    }
   int verticalOffsetold=0;
    private void initView() {
//        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        st = (SlidingTabLayout) findViewById(R.id.st);
        dividerStore = (View) findViewById(R.id.divider_store);
        vp = (ViewPager) findViewById(R.id.vp);
        topBar = (TopBar) findViewById(R.id.top_bar);
        imgBackTmp = (ImageView) findViewById(R.id.img_back_tmp);
        tabTop = (ConstraintLayout) findViewById(R.id.tabTop);
        imgBackTmpRight = (ImageView) findViewById(R.id.img_back_tmp_right);
        imgBackTmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        onRequestFinish();

    }
    int initcount=0;

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRequestFinish();
        if(initcount!=0){
            soundListHistoryFragment.onRefresh(null);
            soundListSubFragment.onRefresh(null);
        }
        initcount++;


    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
//        layoutRefresh.finishRefresh();
//        layoutRefresh.finishLoadMore();
    }
}
