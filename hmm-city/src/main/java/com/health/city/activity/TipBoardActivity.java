package com.health.city.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.city.R;
import com.health.city.contract.TipListContract;
import com.health.city.fragment.FragmentTipBoardList;
import com.healthy.library.model.Topic;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.widget.TopBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 话题榜
 */
@Route(path = CityRoutes.CITY_TIPBOARD)
public class TipBoardActivity extends BaseActivity implements TipListContract.View{
    @Autowired
    String fragmentType;
    private com.healthy.library.widget.TopBar topBar;
    private com.flyco.tablayout.SlidingTabLayout st;
    private ViewPager vp;
    private List<Fragment> fragments = new ArrayList<>();
    FragmentTipBoardList fragmentTipListleft;
    FragmentTipBoardList fragmentTipListright;
    FragmentStatePagerItemAdapter fragmentPagerItemAdapter;
    int currentIndex = 0;
    @Override
    protected int getLayoutId() {
        return R.layout.city_activity_tip_board;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        if("本地".equals(fragmentType)){
            currentIndex=0;
        }else{
            currentIndex=1;
        }
        List<String> titles = Arrays.asList("本地", "全国");
        Map<String,Object> mapleft=new HashMap<>();
        mapleft.put("fragmentType","本地");
        fragmentTipListleft=FragmentTipBoardList.newInstance(mapleft);
        Map<String,Object> mapright=new HashMap<>();
        mapright.put("fragmentType","全国");
        fragmentTipListright=FragmentTipBoardList.newInstance(mapright);
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
        vp.setCurrentItem(currentIndex);
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


    @Override
    public void onSuccessGetTipList(List<Topic> topics, PageInfoEarly pageInfoEarly) {

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
