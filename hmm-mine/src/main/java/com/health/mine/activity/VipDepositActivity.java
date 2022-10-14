package com.health.mine.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.health.mine.R;
import com.health.mine.contract.VipDepositContract;
import com.health.mine.fragment.VipDepositFragment;
import com.health.mine.presenter.VipDepositPresenter;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.business.DepositTabPopoWindow;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.BalanceModel;
import com.healthy.library.model.DepositList;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.VipDeposit;
import com.healthy.library.routes.MineRoutes;

import java.util.ArrayList;
import java.util.List;

@Route(path = MineRoutes.MINE_VIPDEPOSIT)
public class VipDepositActivity extends BaseActivity implements IsFitsSystemWindows, VipDepositContract.View {

    private ImageView img_back;
    private TabLayout tab;
    private ViewPager pager;
    private LinearLayout upPop;
    private RelativeLayout tabLL;
    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    private VipDepositPresenter vipDepositPresenter;
    private CanReplacePageAdapter pageAdapter;
    private DepositTabPopoWindow depositTabPopoWindow;
    private boolean isShow = false;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_detposit;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        vipDepositPresenter = new VipDepositPresenter(mContext, this);
        getData();

    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        img_back = findViewById(R.id.img_back);
        tab = findViewById(R.id.tab);
        pager = findViewById(R.id.pager);
        upPop = findViewById(R.id.upPop);
        tabLL = findViewById(R.id.tabLL);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    }

    @Override
    public void getData() {
        super.getData();
        vipDepositPresenter.getBalanceList();
    }

    @Override
    public void onGetBalanceListSuccess(List<BalanceModel> list) {
        if (list != null && list.size() > 0) {
            mTitles = new ArrayList<>();
            mFragmentList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                BalanceModel model = list.get(i);
                mTitles.add(String.format("会员卡%s", i + 1));
                mFragmentList.add(VipDepositFragment
                        .newInstance(model.CardNo, model.ytbAppId));
            }
            pager.setOffscreenPageLimit(mFragmentList.size() - 1);
            pageAdapter = new CanReplacePageAdapter(getSupportFragmentManager(), mFragmentList, mTitles);
            pager.setAdapter(pageAdapter);
            tab.setupWithViewPager(pager);
            setPop(list);
        } else {
            showEmpty();
        }
    }

    private void setPop(final List<BalanceModel> list) {
        upPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow) {
                    isShow = false;
                    if (depositTabPopoWindow != null) {
                        depositTabPopoWindow.dismiss();
                    }
                    tab.setVisibility(View.VISIBLE);
                    upPop.setVisibility(View.VISIBLE);
                } else {
                    isShow = true;
                    tab.setVisibility(View.GONE);
                    upPop.setVisibility(View.GONE);
                    depositTabPopoWindow = new DepositTabPopoWindow(mContext, new DepositTabPopoWindow.ItemClickCallBack() {
                        @Override
                        public void click(int id, String name) {
                            tab.getTabAt(id).select();
                        }

                        @Override
                        public void dismiss() {
                            depositTabPopoWindow.dismiss();
                            tab.setVisibility(View.VISIBLE);
                            upPop.setVisibility(View.VISIBLE);
                        }
                    });
                    depositTabPopoWindow.setData(list, tab.getSelectedTabPosition());
                    depositTabPopoWindow.showPopupWindow(tabLL);
                }
            }
        });
    }

    @Override
    public void onGetListSuccess(List<VipDeposit> list, PageInfoEarly pageInfoEarly) {

    }

    @Override
    public void onGetDepositListSuccess(List<DepositList> list, PageInfoEarly pageInfoEarly) {

    }

    @Override
    public void onGetDepositGoodsSuccess() {

    }
}