package com.health.servicecenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.health.servicecenter.R;
import com.health.servicecenter.fragment.AppointmentListFragment;
import com.healthy.library.adapter.FragmentStatePagerItemAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.NewStatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 我的预约
 * @author Long
 */
@Route(path = ServiceRoutes.SERVICE_APPOINTMENTLIST)
public class AppointmentListActivity extends BaseActivity implements IsFitsSystemWindows {
    private ConstraintLayout topView;
    private View viewHeaderBg;
    private ImageView imgBack;
    private TextView topBarTitle;
    private TabLayout tab;
    private ViewPager mPager;
    private FragmentStatePagerItemAdapter mFragmentStatePagerItemAdapter;
    private List<Fragment> mFragments;
    private List<String> mTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appointment_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void findViews() {
        super.findViews();
        topView = (ConstraintLayout) findViewById(R.id.topView);
        viewHeaderBg = (View) findViewById(R.id.view_header_bg);
        imgBack = (ImageView) findViewById(R.id.img_back);
        topBarTitle = (TextView) findViewById(R.id.topBar_title);
        tab = (TabLayout) findViewById(R.id.tab);
        mPager = (ViewPager) findViewById(R.id.pager);
        //viewpager/TabLayout 进行关联绑定
        tab.setupWithViewPager(mPager);

        initListener();
        initData();
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        mTitles = Arrays.asList("预约成功", "待接单", "已完成", "已取消");
        mFragments = new ArrayList<>();

        //0	待支付	1待接单	2待到店  3已完成  4已取消

        for (int i = 0; i < mTitles.size(); i++) {
            int status = 2;
            if ("待接单".equals(mTitles.get(i))) status = 1;
            if ("预约成功".equals(mTitles.get(i))) status = 2;
            if ("已完成".equals(mTitles.get(i))) status = 3;
            if ("已取消".equals(mTitles.get(i))) status = 4;
            AppointmentListFragment appointmentListFragment = AppointmentListFragment.newInstance(String.valueOf(status));
            mFragments.add(appointmentListFragment);
        }
        setAdapter(mFragments, mTitles);
    }

    private void setAdapter(List<Fragment> mFragments, List<String> mTitles) {
        if (mFragmentStatePagerItemAdapter == null) {
            mPager.setOffscreenPageLimit(mFragments.size());
            mFragmentStatePagerItemAdapter = new FragmentStatePagerItemAdapter(getSupportFragmentManager(), mFragments, mTitles);
            mPager.setAdapter(mFragmentStatePagerItemAdapter);
        } else {
            mFragmentStatePagerItemAdapter.setDataSource(mFragments, mTitles);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        for (int i = 0; i < mTitles.size(); i++) {
            if ("待接单".equals(mTitles.get(i)) || "已取消".equals(mTitles.get(i))) {
                mFragments.get(i).onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ////System.out.println("AppointmentListActivity 进入onNewIntent");
        if (!ListUtil.isEmpty(mFragments)) {
            for (int i = 0; i < mFragments.size(); i++) {
                //执行刷新数据
                AppointmentListFragment fragment = (AppointmentListFragment) mFragments.get(i);
                fragment.setLoadData(true);
            }
        }

    }
}