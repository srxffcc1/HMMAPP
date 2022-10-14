package com.health.discount.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.health.discount.R;
import com.health.discount.fragment.MyKickFragment;
import com.health.discount.utils.ActivityManager;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.LocUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = DiscountRoutes.DIS_MYKICKACTIVITY)
public class MyKickActivity extends BaseActivity implements IsFitsSystemWindows {
    @Autowired
    String addressProvince;
    @Autowired
    String lng;
    @Autowired
    String lat;

    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    private List<String> orgmTitles;
    private TabLayout tab;
    private android.view.View divider;
    private ImageView img_back;
    private ViewPager pager;
    private CanReplacePageAdapter pageAdapter;
    public Map<String, Object> basemap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_kick;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        addressProvince= LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE);
        lng= LocUtil.getLongitude(mContext, SpKey.LOC_ORG);
        lat= LocUtil.getLatitude(mContext, SpKey.LOC_ORG);

        ActivityManager.addActivity(this);
        ARouter.getInstance().inject(this);
        mTitles = Arrays.asList("全部", "砍价中", "砍价成功", "砍价失败");
        orgmTitles = Arrays.asList("全部", "砍价中", "砍价成功", "砍价失败");
        mFragmentList = new ArrayList<>();
        mFragmentList.add(MyKickFragment
                .newInstance(new SimpleHashMapBuilder().putMap(basemap).puts("type", "0").puts("addressProvince", addressProvince).puts("lat", lat).puts("lng", lng)));
        mFragmentList.add(MyKickFragment
                .newInstance(new SimpleHashMapBuilder().putMap(basemap).puts("type", "1").puts("addressProvince", addressProvince).puts("lat", lat).puts("lng", lng)));
        mFragmentList.add(MyKickFragment
                .newInstance(new SimpleHashMapBuilder().putMap(basemap).puts("type", "2").puts("addressProvince", addressProvince).puts("lat", lat).puts("lng", lng)));
        mFragmentList.add(MyKickFragment
                .newInstance(new SimpleHashMapBuilder().putMap(basemap).puts("type", "3").puts("addressProvince", addressProvince).puts("lat", lat).puts("lng", lng)));
        pager.setOffscreenPageLimit(mFragmentList.size() - 1);
        pageAdapter = new CanReplacePageAdapter(getSupportFragmentManager(), mFragmentList, mTitles);
        pager.setAdapter(pageAdapter);
        tab.setupWithViewPager(pager);
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        tab = (TabLayout) findViewById(R.id.tab);
        divider = (View) findViewById(R.id.divider);
        pager = (ViewPager) findViewById(R.id.pager);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}