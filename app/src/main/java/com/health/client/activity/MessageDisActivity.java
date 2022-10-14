package com.health.client.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.client.R;
import com.health.client.fragment.MessageDisFragment;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.widget.TopBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = AppRoutes.APP_MESSAGEDIS)
public class MessageDisActivity extends BaseActivity  {
    @Autowired
    String fragmentType;
    private com.healthy.library.widget.TopBar topBar;
    private com.flyco.tablayout.SlidingTabLayout st;
    private ViewPager vp;
    private List<Fragment> fragments = new ArrayList<>();
    MessageDisFragment fragmentTipListleft;
    MessageDisFragment fragmentTipListright;
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    int currentIndex = 0;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_messagedis;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        List<String> titles = Arrays.asList("收到的", "发出的");
        Map<String,Object> mapleft=new HashMap<>();
        mapleft.put("fragmentType","收到的");
        fragmentTipListleft= MessageDisFragment.newInstance(mapleft);
        Map<String,Object> mapright=new HashMap<>();
        mapright.put("fragmentType","发出的");
        fragmentTipListright=MessageDisFragment.newInstance(mapright);
        fragments.add(fragmentTipListleft);
        fragments.add(fragmentTipListright);

        if (fragmentPagerItemAdapter == null) {
            fragmentPagerItemAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), fragments, titles);
            // adapter
            vp.setAdapter(fragmentPagerItemAdapter);
        } else {
            fragmentPagerItemAdapter.setDataSource(fragments, titles);
        }
        fragmentPagerItemAdapter.notifyDataSetChanged();
        vp.setOffscreenPageLimit(fragments.size());
        // bind to SmartTabLayout
        st.setViewPager(vp);
        st.setCurrentTab(currentIndex);
        st.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentIndex = position;
//                if(position!=0){
//                    moreHot.setVisibility(View.VISIBLE);
//                    tvArea.setVisibility(View.GONE);
//                }else {
//                    moreHot.setVisibility(View.GONE);
//                    tvArea.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }




    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        st = (SlidingTabLayout) findViewById(R.id.st);
        vp = (ViewPager) findViewById(R.id.vp);

    }
}
