package com.health.faq.activity;


import android.os.Bundle;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.health.faq.R;
import com.health.faq.fragment.AskTheExpertsFragment;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.routes.FaqRoutes;
import com.healthy.library.widget.TopBar;

@Route(path = FaqRoutes.FAQ_REWARDINDEX)
public class FaqExportActivity extends BaseActivity {
    private TopBar topBar;
    private FrameLayout childFrame;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reward_index;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        setTopBar(topBar);
        getSupportFragmentManager().beginTransaction().replace(R.id.childFrame,new AskTheExpertsFragment()).commitAllowingStateLoss();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        childFrame = (FrameLayout) findViewById(R.id.childFrame);
    }
}
