//package com.health.discount.fragment;
//
//import android.os.Bundle;
//import com.google.android.material.tabs.TabLayout;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager.widget.ViewPager;
//import android.view.View;
//
//import com.health.discount.R;
//import com.healthy.library.adapter.CanReplacePageAdapter;
//import com.healthy.library.base.BaseFragment;
//import com.healthy.library.builder.SimpleHashMapBuilder;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//public class AssembleRightFragment extends BaseFragment {
//
//    private List<Fragment> mFragmentList;
//    private List<String> mTitles;
//    private List<String> orgmTitles;
//    private TabLayout tab;
//    private android.view.View divider;
//    private ViewPager pager;
//    private CanReplacePageAdapter pageAdapter;
//
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.dis_fragment_group_right;
//    }
//
//    @Override
//    protected void findViews() {
//        initView();
//        mTitles = Arrays.asList("全部", "拼团中","拼团成功","拼团失败");
//        orgmTitles = Arrays.asList("全部", "拼团中","拼团成功", "拼团失败");
//        mFragmentList = new ArrayList<>();
//        mFragmentList.add(AssembleRightChildFragment
//                .newInstance(new SimpleHashMapBuilder().putMap(basemap).puts("type","0")));
//        mFragmentList.add(AssembleRightChildFragment
//                .newInstance(new SimpleHashMapBuilder().putMap(basemap).puts("type","1")));
//        mFragmentList.add(AssembleRightChildFragment
//                .newInstance(new SimpleHashMapBuilder().putMap(basemap).puts("type","2")));
//        mFragmentList.add(AssembleRightChildFragment
//                .newInstance(new SimpleHashMapBuilder().putMap(basemap).puts("type","3")));
//        pager.setOffscreenPageLimit(mFragmentList.size() - 1);
//        pageAdapter=new CanReplacePageAdapter(getChildFragmentManager(),mFragmentList,mTitles);
//        pager.setAdapter(pageAdapter);
//        tab.setupWithViewPager(pager);
//        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//
//
//
//
//    }
//    public static AssembleRightFragment newInstance(Map<String, Object> maporg) {
//        AssembleRightFragment fragment = new AssembleRightFragment();
//        Bundle args = new Bundle();
//        for (Map.Entry<String, Object> entry : maporg.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            if (value instanceof Integer) {
//                args.putInt(key, (Integer) value);
//            } else if (value instanceof Boolean) {
//                args.putBoolean(key, (Boolean) value);
//            } else if (value instanceof String) {
//                args.putString(key, (String) value);
//            } else {
//                args.putSerializable(key, (Serializable) value);
//            }
//        }
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    private void initView() {
//        tab = (TabLayout) findViewById(R.id.tab);
//        divider = (View) findViewById(R.id.divider);
//        pager = (ViewPager) findViewById(R.id.pager);
//    }
//}
