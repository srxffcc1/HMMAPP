package com.health.servicecenter.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.health.servicecenter.R;
import com.health.servicecenter.contract.OrderListContract;
import com.health.servicecenter.fragment.OrderListFragment;
import com.health.servicecenter.utils.ActivityManager;
import com.healthy.library.model.OrderNum;
import com.health.servicecenter.presenter.OrderListPresenter;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Constants;
import com.healthy.library.message.UpdateOrderNum;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.widget.TopBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = ServiceRoutes.SERVICE_USERORDER)
public class OrderList extends BaseActivity implements OrderListContract.View {
    @Autowired
    String type;

    private TopBar mTopBar;
    private TabLayout mTabLayout;
    private ViewPager mPager;
    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    private List<String> orgmTitles;
    private CanReplacePageAdapter pageAdapter;
    private OrderListPresenter orderListPresenter;

    @Override
    protected void findViews() {
        super.findViews();
        mTabLayout = findViewById(R.id.tab);
        mTopBar = findViewById(R.id.top_bar);
        mPager = findViewById(R.id.pager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        ActivityManager.addActivity(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_order;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setTopBar(mTopBar);
        orderListPresenter = new OrderListPresenter(this, this);
        mTitles = Arrays.asList("全部", "待付款", "待确认", "待评价");
        orgmTitles = Arrays.asList("全部", "待付款", "待确认", "待评价");
        mFragmentList = new ArrayList<>();
        mFragmentList.add(OrderListFragment
                .newInstance("0"));

        mFragmentList.add(OrderListFragment
                .newInstance("1"));

        mFragmentList.add(OrderListFragment
                .newInstance("2"));

        mFragmentList.add(OrderListFragment
                .newInstance("3"));
        mPager.setOffscreenPageLimit(mFragmentList.size() - 1);
        pageAdapter = new CanReplacePageAdapter(getSupportFragmentManager(), mFragmentList, mTitles);
        mPager.setAdapter(pageAdapter);

        Map<String, Integer> map = new HashMap<>(4);
        map.put(Constants.ORDER_TYPE_ALL, 0);
        map.put(Constants.ORDER_TYPE_TO_BE_PAID, 1);
        map.put(Constants.ORDER_TYPE_USE, 2);
        map.put(Constants.ORDER_TYPE_COMMENT, 3);

        mPager.setCurrentItem(map.get(type) == null ? 0 : map.get(type), false);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == null || tab.getText() == null) {
                    return;
                }
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
                spStr.setSpan(styleSpan_B, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == null || tab.getText() == null) {
                    return;
                }
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan styleSpan_B = new StyleSpan(Typeface.NORMAL);
                spStr.setSpan(styleSpan_B, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mTabLayout.setupWithViewPager(mPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        orderListPresenter.getOrderNum();
    }

    @Override
    public void onGetOrderNumSuccess(OrderNum orderNum) {
        if (orderNum != null) {
            pageAdapter.setPageTitle(0, orgmTitles.get(0));
            pageAdapter.setPageTitle(1, orgmTitles.get(1) + ("0".equals(orderNum.noPayCount) ? "" : "(" + orderNum.noPayCount + ")"));
            pageAdapter.setPageTitle(2, orgmTitles.get(2) + ("0".equals(orderNum.noTakeCount) ? "" : "(" + orderNum.noTakeCount + ")"));
            pageAdapter.setPageTitle(3, orgmTitles.get(3) + ("0".equals(orderNum.noCommentCount) ? "" : "(" + orderNum.noCommentCount + ")"));

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getTabResult(UpdateOrderNum model) {
        if (model.flag == 1) {
            orderListPresenter.getOrderNum();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}