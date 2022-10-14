package com.health.mine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.health.mine.R;
import com.health.mine.contract.VipConsumeContract;
import com.health.mine.fragment.VipConsumeHistoryFragment;
import com.healthy.library.model.BalanceModel;
import com.healthy.library.model.VipConsume;
import com.health.mine.presenter.VipConsumePresenter;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.model.PageInfoEarly;

import java.util.ArrayList;
import java.util.List;

@Route(path = MineRoutes.MINE_VIPCONSUME_HISTORY)
public class VipConsumeHistoryActivity extends BaseActivity implements IsFitsSystemWindows, VipConsumeContract.View {
    private ImageView img_back;
    private TabLayout tab;
    private ViewPager pager;
    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    VipConsumePresenter vipConsumePresenter;
    private CanReplacePageAdapter pageAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_consumehistory;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        vipConsumePresenter = new VipConsumePresenter(this, this);
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
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void getData() {
        super.getData();
        vipConsumePresenter.getBalanceList();
    }

    @Override
    public void onGetBalanceListSuccess(List<BalanceModel> list) {
        if (list != null && list.size() > 0) {
            mTitles = new ArrayList<>();
            mFragmentList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                BalanceModel model = list.get(i);
                mTitles.add(String.format("会员卡%s", i + 1));
                mFragmentList.add(VipConsumeHistoryFragment
                        .newInstance(model.CardNo, model.ytbAppId));
            }
            pager.setOffscreenPageLimit(mFragmentList.size() - 1);
            pageAdapter = new CanReplacePageAdapter(getSupportFragmentManager(), mFragmentList, mTitles);
            pager.setAdapter(pageAdapter);
            tab.setupWithViewPager(pager);
        }else{
            showEmpty();
        }
    }

    @Override
    public void onGetListSuccess(List<VipConsume> list, PageInfoEarly pageInfoEarly) {

    }
}