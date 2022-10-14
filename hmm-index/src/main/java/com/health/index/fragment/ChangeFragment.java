package com.health.index.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health.index.R;
import com.health.index.adapter.BabyChangeAdapter;
import com.health.index.constants.Changes;
import com.health.index.contract.ChangeContract;
import com.health.index.model.ChangeModel;
import com.health.index.presenter.ChangePresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.Functions;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.StatusLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/26 10:38
 * @des 宝宝变化、妈妈变化
 */

public class ChangeFragment extends BaseFragment implements TabLayout.BaseOnTabSelectedListener,
        ChangeContract.View {


    private TabLayout mTabLayout;
    private ChangePresenter mPresenter;
    private int mPeriod;
    private int mType;
    private RecyclerView mRecyclerChange;
    private NestedScrollView mScrollMomChange;
    private LinearLayout mLayoutMomChange;
    private StatusLayout mStatusLayout;
    private BabyChangeAdapter mAdapter;
    private String mQueryDate;
    private LinearLayoutManager mLinearLayoutManager;
    private TabLayout.Tab mSelectedTab;

    @Override
    protected int getLayoutId() {
        return R.layout.index_fragment_change;
    }

    @Override
    protected void findViews() {
        mTabLayout = getContentView().findViewById(R.id.tab);
        mRecyclerChange = getContentView().findViewById(R.id.recycler_change);
        mScrollMomChange = getContentView().findViewById(R.id.scroll_mom_change);
        mLayoutMomChange = getContentView().findViewById(R.id.layout_mom_change);
        mStatusLayout = getContentView().findViewById(R.id.layout_status);
    }

    public static ChangeFragment newInstance(int period, int type, String queryDate) {

        Bundle args = new Bundle();
        ChangeFragment fragment = new ChangeFragment();
        args.putInt("period", period);
        args.putInt("type", type);
        args.putString("queryDate", queryDate);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void onCreate() {
        super.onCreate();
        if (getArguments() != null) {
            setStatusLayout(mStatusLayout);
            mLinearLayoutManager = new LinearLayoutManager(mContext);
            mPresenter = new ChangePresenter(this, this);
            mPeriod = getArguments().getInt("period");
            mQueryDate = getArguments().getString("queryDate");
            mType = getArguments().getInt("type");
            Map<String, Object> map = new HashMap<>(2);
            map.put("queryDate", mQueryDate);
            mScrollMomChange.setVisibility(mType == Changes.CHANGE_TYPE_MOM ? View.VISIBLE : View.GONE);
            mRecyclerChange.setVisibility(mType == Changes.CHANGE_TYPE_MOM ? View.GONE : View.VISIBLE);
            if (mPeriod == Changes.CHANGE_PERIOD_PREGNANCY) {
                if (mType == Changes.CHANGE_TYPE_MOM) {
                    for (int i = 0; i <= 41; i++) {
                        mTabLayout.addTab(mTabLayout.newTab().setText(i + "周"));
                    }
                    map.put(Functions.FUNCTION, Functions.FUNCTION_PREGNANCY_MOM_CHANGE);
                } else {
                    mAdapter = new BabyChangeAdapter();
                    mRecyclerChange.setLayoutManager(mLinearLayoutManager);
                    mAdapter.bindToRecyclerView(mRecyclerChange);
                    for (int i = 0; i <= 40; i++) {
                        mTabLayout.addTab(mTabLayout.newTab().setText(i + "周"));
                    }
                    map.put(Functions.FUNCTION, Functions.FUNCTION_PREGNANCY_BABY_CHANGE);
                }
            } else {
                if (mType == Changes.CHANGE_TYPE_MOM) {
                    for (int i = 1; i <= 52; i++) {
                        mTabLayout.addTab(mTabLayout.newTab().setText(i + "周"));
                    }
                    map.put(Functions.FUNCTION, Functions.FUNCTION_PARENTING_MOM_CHANGE);
                } else {
                    for (int i = 1; i <= 12; i++) {
                        mTabLayout.addTab(mTabLayout.newTab().setText(i + "月"));
                    }
                    mAdapter = new BabyChangeAdapter();
                    mRecyclerChange.setLayoutManager(mLinearLayoutManager);
                    mAdapter.bindToRecyclerView(mRecyclerChange);
                    map.put(Functions.FUNCTION, Functions.FUNCTION_PARENTING_BABY_CHANGE);
                }
            }
            mTabLayout.addOnTabSelectedListener(this);
            mPresenter.getChange(map);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mSelectedTab = tab;
        getChangeData(tab);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        mSelectedTab = tab;
        getChangeData(tab);
    }

    @Override
    public void getData() {
        if (mSelectedTab != null) {
            getChangeData(mSelectedTab);
        } else {
            Map<String, Object> map = new HashMap<>(2);
            map.put("queryDate", mQueryDate);
            String function;
            if (mPeriod == Changes.CHANGE_PERIOD_PREGNANCY) {
                function = mType == Changes.CHANGE_TYPE_MOM ?
                        Functions.FUNCTION_PREGNANCY_MOM_CHANGE :
                        Functions.FUNCTION_PREGNANCY_BABY_CHANGE;
            } else {
                function = mType == Changes.CHANGE_TYPE_MOM ?
                        Functions.FUNCTION_PARENTING_MOM_CHANGE :
                        Functions.FUNCTION_PARENTING_BABY_CHANGE;
            }
            map.put(Functions.FUNCTION, function);
            mPresenter.getChange(map);
        }
    }

    /**
     * 根据选择的tab获取值
     *
     * @param tab tab
     */
    private void getChangeData(TabLayout.Tab tab) {
        Map<String, Object> map = new HashMap<>(4);
        if (mPeriod == Changes.CHANGE_PERIOD_PREGNANCY) {
            map.put(Functions.FUNCTION, mType == Changes.CHANGE_TYPE_MOM ?
                    Functions.FUNCTION_PREGNANCY_MOM_CHANGE :
                    Functions.FUNCTION_PREGNANCY_BABY_CHANGE);
            map.put("chooseWeek", String.valueOf(tab.getPosition()));
            mPresenter.getChange(map);

        } else {
            String key = mType == Changes.CHANGE_TYPE_MOM ? "chooseWeek" : "chooseMonth";

            map.put(Functions.FUNCTION, mType == Changes.CHANGE_TYPE_MOM ?
                    Functions.FUNCTION_PARENTING_MOM_CHANGE :
                    Functions.FUNCTION_PARENTING_BABY_CHANGE);
            map.put(key, String.valueOf(tab.getPosition() + 1));
            mPresenter.getChange(map);
        }
    }

    @Override
    public void onGetChangesSuccess(List<ChangeModel> list, int weekPos) {
        if (mPeriod == Changes.CHANGE_PERIOD_PREGNANCY) {
            mTabLayout.setScrollPosition(weekPos, 0, true);

            if (mType == Changes.CHANGE_TYPE_MOM) {
                if (list.size() > 0) {
                    ChangeModel model = list.get(0);
                    setMomChange(model);
                }
            } else {
                mAdapter.setNewData(list);
                mLinearLayoutManager.scrollToPosition(findPos(mQueryDate, list));
            }
        } else {
            mTabLayout.setScrollPosition(weekPos - 1, 0, true);
            if (mType == Changes.CHANGE_TYPE_MOM) {
                if (list.size() > 0) {
                    ChangeModel model = list.get(0);
                    setMomChange(model);
                }
            } else {
                mAdapter.setNewData(list);
                mLinearLayoutManager.scrollToPosition(findPos(mQueryDate, list));
            }
        }
    }

    private void setMomChange(ChangeModel model) {
        mLayoutMomChange.removeAllViews();
        if (!TextUtils.isEmpty(model.getMomChange())) {
            addChange("妈妈变化", model.getMomChange());
        }
        if (!TextUtils.isEmpty(model.getFoodPoints())) {
            addChange("营养指导", model.getFoodPoints());
        }
        if (!TextUtils.isEmpty(model.getReminder())) {
            addChange("注意事项", model.getReminder());
        }
    }

    private void addChange(String title, String content) {
        TextView titleView = new TextView(mContext);
        titleView.setText(title);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        titleView.setTextColor(Color.parseColor("#222222"));
        titleView.setTypeface(Typeface.DEFAULT_BOLD);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.topMargin = (int) TransformUtil.dp2px(mContext, 19);
        mLayoutMomChange.addView(titleView, titleParams);

        TextView contentView = new TextView(mContext);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        contentView.setTextColor(Color.parseColor("#222222"));
        contentView.setTypeface(Typeface.DEFAULT);
        contentView.setText(content);
        contentView.setLineSpacing(0, 1.2f);
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.topMargin = (int) TransformUtil.dp2px(mContext, 11);
        mLayoutMomChange.addView(contentView, contentParams);
    }

    private int findPos(String queryDate, List<ChangeModel> list) {
        for (ChangeModel changeModel : list) {
            if (queryDate.equals(changeModel.getShowDate())) {
                return list.indexOf(changeModel);
            }
        }
        return 0;
    }
}
