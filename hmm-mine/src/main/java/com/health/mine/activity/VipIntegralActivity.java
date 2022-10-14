package com.health.mine.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.health.mine.R;
import com.health.mine.contract.VipIntegralContract;
import com.health.mine.fragment.VipIntegralFragment;
import com.healthy.library.model.BalanceModel;
import com.health.mine.model.IntegralListModel;
import com.healthy.library.model.IntegralModel;
import com.health.mine.presenter.VipIntegralPresenter;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.FormatUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Route(path = MineRoutes.MINE_VIPINTEGRALACTIVITY)
public class VipIntegralActivity extends BaseActivity implements IsFitsSystemWindows, VipIntegralContract.View {

    private ImageView img_back;
    private TabLayout tab;
    private ViewPager pager;
    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    private CanReplacePageAdapter pageAdapter;

    private VipIntegralPresenter presenter;
    private TextView topTotalIntegral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        init(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip_integral;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        presenter = new VipIntegralPresenter(this, this);
        getData();

    }

    @Override
    public void getData() {
        super.getData();
        presenter.getBalanceList();
        presenter.getIntegral();
    }

    @Override
    protected void findViews() {
        super.findViews();
        img_back = findViewById(R.id.img_back);
        tab = findViewById(R.id.tab);
        pager = findViewById(R.id.pager);
        topTotalIntegral = findViewById(R.id.topTotalIntegral);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onGetBalanceListSuccess(List<BalanceModel> list) {
        if (list != null && list.size() > 0) {
            mTitles = new ArrayList<>();
            mFragmentList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                BalanceModel model = list.get(i);
                mTitles.add(String.format("会员卡%s积分%s", i + 1, FormatUtils.moneyKeep2Decimals(model.CardJFTot)));
                mFragmentList.add(VipIntegralFragment
                        .newInstance(model.CardNo, model.ytbAppId));
            }
            pager.setOffscreenPageLimit(mFragmentList.size());
            pageAdapter = new CanReplacePageAdapter(getSupportFragmentManager(), mFragmentList, mTitles);
            pager.setAdapter(pageAdapter);
            tab.setupWithViewPager(pager);
        } else {
            showEmpty();
        }
    }

    @Override
    public void onGetIntegralSuccess(IntegralModel model) {
        if (model != null) {
            if (model.SYJFTOT != null && !TextUtils.isEmpty(model.SYJFTOT)) {
                //model.SYJFTOT = model.SYJFTOT + ".-1";
                if (model.SYJFTOT.contains(".")) {
                    String[] split = model.SYJFTOT.split("\\.");
                    int lastIntegral = Integer.parseInt(split[split.length - 1]);
                    if (lastIntegral != 0) {
                        DecimalFormat df = new DecimalFormat(",###,##0.00");
                        BigDecimal bd = new BigDecimal(model.SYJFTOT);
                        model.SYJFTOT = df.format(bd);
                    } else {
                        model.SYJFTOT = split[0];
                    }
                }
                topTotalIntegral.setText(model.SYJFTOT);
            } else {
                topTotalIntegral.setText("0");
            }
        }

    }

    @Override
    public void onGetIntegralListSuccess(PageInfoEarly pageInfoEarly, List<IntegralListModel> list) {

    }
}