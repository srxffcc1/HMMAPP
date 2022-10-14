package com.health.index.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.healthy.library.routes.IndexRoutes;
import com.health.index.R;
import com.health.index.constants.Changes;
import com.health.index.fragment.ChangeFragment;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.utils.FragmentUtils;
import com.healthy.library.widget.TopBar;

/**
 * @author Li
 * @date 2019/03/25 17:59
 * @des 宝宝变化/妈妈变化
 */

@Route(path = IndexRoutes.INDEX_CHANGES)
public class BabyMomChangeActivity extends BaseActivity {

    @Autowired
    int type;
    @Autowired
    int period;
    @Autowired
    String queryDate;
    private TopBar mTopBar;
    private TextView mTvMomChange;
    private TextView mTvBabyChange;
    private View mViewMom;
    private View mViewBaby;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_baby_mom_change;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);
        mTvMomChange = findViewById(R.id.tv_mom);
        mTvBabyChange = findViewById(R.id.tv_baby);
        mViewMom = findViewById(R.id.bottom_mom);
        mViewBaby = findViewById(R.id.bottom_baby);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setTopBar(mTopBar);
        if (type == Changes.CHANGE_TYPE_BABY) {
            mTvBabyChange.performClick();
        } else {
            mTvMomChange.performClick();
        }

    }

    public void showMomChange(View view) {
        changeStatus(true);
        FragmentUtils.showFragment(getSupportFragmentManager(), "momChange",
                R.id.layout_content,
                ChangeFragment.newInstance(period, Changes.CHANGE_TYPE_MOM,queryDate));
    }

    public void showBabyChange(View view) {
        changeStatus(false);
        FragmentUtils.showFragment(getSupportFragmentManager(), "babyChange",
                R.id.layout_content,
                ChangeFragment.newInstance(period, Changes.CHANGE_TYPE_BABY,queryDate));
    }

    private void changeStatus(boolean momStatus) {
        mTvBabyChange.setTypeface(momStatus ? Typeface.DEFAULT : Typeface.DEFAULT_BOLD);
        mTvMomChange.setTypeface(momStatus ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        mViewBaby.setVisibility(momStatus ? View.GONE : View.VISIBLE);
        mViewMom.setVisibility(momStatus ? View.VISIBLE : View.GONE);
    }
}
