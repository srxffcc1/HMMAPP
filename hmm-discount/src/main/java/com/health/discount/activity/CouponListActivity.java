package com.health.discount.activity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.health.discount.R;
import com.health.discount.fragment.CouponCommonFragment;
import com.health.discount.fragment.CouponFragment;
import com.health.discount.fragment.CouponVipFragment;
import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.message.UpdateCheckInfoBackMsg;
import com.healthy.library.message.UpdateCheckInfoBackReMsg;
import com.healthy.library.message.UpdateCheckTabCoupon;
import com.healthy.library.model.MemberAction;
import com.healthy.library.presenter.ActionPresenterCopy;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.ImageTextView;
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
@Route(path = DiscountRoutes.DIS_COUPONLIST)
public class CouponListActivity extends BaseActivity implements IsFitsSystemWindows {

    @Autowired
    String tabIndex;
    private TopBar topBar;
    private TabLayout tab;
    private ViewPager pager;

    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    private List<String> orgmTitles;
    private CanReplacePageAdapter pageAdapter;
    private boolean showcheck = false;
    private boolean showManager = true;


    private View divider;
    private CheckBox allCheck;
    private TextView allCheckRight;
    private TextView checkCancel;
    private TextView checkDelete;
    private ConstraintLayout couponDeleteBlock;
    private LinearLayout couponBottom;
    private ConstraintLayout couponHistoryLL;
    private ImageTextView couponHistory;
    private ImageTextView couponCenter;


    @Override
    protected int getLayoutId() {
        return R.layout.dis_activity_coupon_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);

        mTitles = Arrays.asList("门店券","通用券");
        orgmTitles = Arrays.asList("门店券","通用券");
        mFragmentList = new ArrayList<>();
//        mFragmentList.add(CouponFragment
//                .newInstance(new SimpleHashMapBuilder<String, Object>().puts("type", 0).puts("status", "0,-1")));
        mFragmentList.add(CouponVipFragment
                .newInstance(null));
        mFragmentList.add(CouponCommonFragment
                .newInstance(new SimpleHashMapBuilder<String, Object>().puts("type", 0).puts("status", "0,-1")));
        pager.setOffscreenPageLimit(mFragmentList.size());
        pageAdapter = new CanReplacePageAdapter(getSupportFragmentManager(), mFragmentList, mTitles);
        pager.setAdapter(pageAdapter);
        tab.setupWithViewPager(pager);
        tab.getTabAt(1).select();
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                showcheck = !showcheck;
                buildManager();
            }
        });
        checkCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcheck = false;
                buildManager();
            }
        });
        couponHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(DiscountRoutes.DIS_COUPONLIST_HISTORY)
                        .navigation();
            }
        });
        buildManagerShow();

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showcheck = false;
                buildManager();
                if (tab.getPosition() == 1) {
                    showManager = true;
                } else {
                    showManager = false;

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
//        tab.setVisibility(View.GONE);
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
    }

    public void buildManagerShow() {
        if (showManager) {
            couponBottom.setVisibility(View.VISIBLE);
            topBar.getmSubmitText().setVisibility(View.VISIBLE);
        } else {
            couponBottom.setVisibility(View.GONE);
            topBar.getmSubmitText().setVisibility(View.INVISIBLE);
        }
        if (showcheck) {
            topBar.setSubmit("完成");
            couponHistoryLL.setVisibility(View.GONE);
            couponDeleteBlock.setVisibility(View.VISIBLE);
        } else {
            topBar.setSubmit("管理");
            allCheck.setChecked(false);
            couponHistoryLL.setVisibility(View.VISIBLE);
            couponDeleteBlock.setVisibility(View.GONE);
        }
    }

    public void buildManager() {
        if (showcheck) {
            topBar.setSubmit("完成");
            couponHistoryLL.setVisibility(View.GONE);
            couponDeleteBlock.setVisibility(View.VISIBLE);
        } else {
            topBar.setSubmit("管理");
            allCheck.setChecked(false);
            couponHistoryLL.setVisibility(View.VISIBLE);
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
                if (!buttonView.isPressed()) {
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
        couponDeleteBlock = (ConstraintLayout) findViewById(R.id.couponDeleteBlock);
        couponBottom = (LinearLayout) findViewById(R.id.couponBottom);
        couponHistoryLL = (ConstraintLayout) findViewById(R.id.couponHistoryLL);
        couponHistory = (ImageTextView) findViewById(R.id.couponHistory);
        couponCenter = (ImageTextView) findViewById(R.id.couponCenter);
        couponCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                        new SimpleHashMapBuilder<>()
                                .putObject(new MemberAction(
                                        BuildConfig.VERSION_NAME,
                                        1,
                                        7,
                                        "CardCenterFromCouponList",
                                        getActivitySimpleName(),
                                        new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                                ));
                ARouter.getInstance()
                        .build(DiscountRoutes.DIS_CARDCENTER)
                        .navigation();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UpdateCheckInfoBackMsg msg) {
        setAllcheckCheck(msg.flag);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfoD(UpdateCheckInfoBackReMsg msg) {
        showcheck = false;
        buildManager();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateCouponTab(UpdateCheckTabCoupon msg) {
        allCheck.setChecked(false);
    }

    private void setAllcheckCheck(boolean flag) {
        if (!flag) {
            allCheck.setChecked(false);
        }
    }

}
