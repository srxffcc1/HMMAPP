package com.health.discount.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.health.discount.R;
import com.health.discount.fragment.CouponCommonFragment;
import com.health.discount.fragment.CouponFragment;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.message.UpdateCheckInfoBackMsg;
import com.healthy.library.message.UpdateCheckInfoBackReMsg;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.widget.TopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 砍价列表
 */
@Route(path = DiscountRoutes.DIS_COUPONLIST_HISTORY)
public class CouponListHistoryActivity extends BaseActivity  {
    private TopBar topBar;
    private TabLayout tab;
    private ViewPager pager;

    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    private List<String> orgmTitles;
    private CanReplacePageAdapter pageAdapter;
    private boolean showcheck=false;


    private View divider;
    private CheckBox allCheck;
    private TextView allCheckRight;
    private TextView checkCancel;
    private TextView checkDelete;
    private TextView couponHistory;
    private ConstraintLayout couponDeleteBlock;
    private LinearLayout couponBottom;
    private boolean showManager = true;


    @Override
    protected int getLayoutId() {
        return R.layout.dis_activity_coupon_list_history;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mTitles = Arrays.asList("通用券");
        orgmTitles=Arrays.asList("通用券");
        mFragmentList = new ArrayList<>();
//        mFragmentList.add(CouponFragment
//                .newInstance(new SimpleHashMapBuilder<String, Object>().puts("type",-1).puts("status","1,2")));
        mFragmentList.add(CouponCommonFragment
                .newInstance(new SimpleHashMapBuilder<String, Object>().puts("type",-1).puts("status","1,2")));
        pager.setOffscreenPageLimit(mFragmentList.size());
        pageAdapter=new CanReplacePageAdapter(getSupportFragmentManager(),mFragmentList,mTitles);
        pager.setAdapter(pageAdapter);
        tab.setupWithViewPager(pager);
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                showcheck=!showcheck;
                buildManager();
            }
        });
        checkCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcheck=false;
                buildManager();
            }
        });
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showcheck=false;
                buildManager();
                if (tab.getPosition() == 1) {
                    showManager = true;
                } else {
                    showManager = true;

                }
                buildManagerShow();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        checkDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragmentList.get(tab.getSelectedTabPosition()) instanceof CouponFragment) {
                    ((CouponFragment) (mFragmentList.get(tab.getSelectedTabPosition()))).deleteCoupon();
                }
                if (mFragmentList.get(tab.getSelectedTabPosition()) instanceof CouponCommonFragment) {
                    ((CouponCommonFragment) (mFragmentList.get(tab.getSelectedTabPosition()))).deleteCoupon();
                }
            }
        });
        buildManagerShow();
    }
    public void buildManagerShow() {
        if (showManager) {
            couponBottom.setVisibility(View.VISIBLE);
            topBar.getmSubmitText().setVisibility(View.VISIBLE);
        } else {
            couponBottom.setVisibility(View.GONE);
            topBar.getmSubmitText().setVisibility(View.INVISIBLE);
        }
        if(showcheck){
            topBar.setSubmit("完成");
            couponDeleteBlock.setVisibility(View.VISIBLE);
        }else {
            topBar.setSubmit("管理");
            allCheck.setChecked(false);
            couponDeleteBlock.setVisibility(View.GONE);
        }
    }
    public void buildManager(){
        if(showcheck){
            topBar.setSubmit("完成");
            couponDeleteBlock.setVisibility(View.VISIBLE);
        }else {
            topBar.setSubmit("管理");
            allCheck.setChecked(false);
            couponDeleteBlock.setVisibility(View.GONE);
        }
        try {
            if (mFragmentList.get(tab.getSelectedTabPosition()) instanceof CouponFragment) {
                ((CouponFragment) (mFragmentList.get(tab.getSelectedTabPosition()))).setAllCheckShow(showcheck);
            }
            if (mFragmentList.get(tab.getSelectedTabPosition()) instanceof CouponCommonFragment) {
                ((CouponCommonFragment) (mFragmentList.get(tab.getSelectedTabPosition()))).setAllCheckShow(showcheck);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
        allCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!buttonView.isPressed()){
                    return;
                }
                if (mFragmentList.get(tab.getSelectedTabPosition()) instanceof CouponFragment) {
                    ((CouponFragment) (mFragmentList.get(tab.getSelectedTabPosition()))).setAllCheck(isChecked);
                }
                if (mFragmentList.get(tab.getSelectedTabPosition()) instanceof CouponCommonFragment) {
                    ((CouponCommonFragment) (mFragmentList.get(tab.getSelectedTabPosition()))).setAllCheck(isChecked);
                }
            }
        });
    }





    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        tab = (TabLayout) findViewById(R.id.tab);
        pager = (ViewPager) findViewById(R.id.pager);
        divider = (View) findViewById(R.id.divider);
        allCheck = (CheckBox) findViewById(R.id.allCheck);
        allCheckRight = (TextView) findViewById(R.id.allCheckRight);
        checkCancel = (TextView) findViewById(R.id.checkCancel);
        checkDelete = (TextView) findViewById(R.id.checkDelete);
        couponHistory = (TextView) findViewById(R.id.couponHistory);
        couponDeleteBlock = (ConstraintLayout) findViewById(R.id.couponDeleteBlock);
        couponBottom = (LinearLayout) findViewById(R.id.couponBottom);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateCheckInfoBackMsg msg) {
        setAllcheckCheck(msg.flag);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfoD(UpdateCheckInfoBackReMsg msg) {
        showcheck=false;
        buildManager();
    }

    private void setAllcheckCheck(boolean flag) {
        if(!flag){
            allCheck.setChecked(false);
        }
    }

}
