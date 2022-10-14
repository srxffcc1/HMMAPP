package com.health.index.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.health.index.R;
import com.health.index.fragment.ToolsFeedBaseFragment;
import com.health.index.fragment.ToolsFeedSumChartFragment;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.model.TabEntity;
import com.healthy.library.widget.TopBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(path = IndexRoutes.INDEX_TOOLS_BABY_DIARY_SUM_CHART)
public class ToolsBabyDiarySumChart extends BaseActivity implements IsFitsSystemWindows {
    private TopBar topBar;
    private CommonTabLayout mTabLayout_2;
    private TextView tlDiv;
    private String[] mTitles = {"母乳", "配方奶", "辅食","小便", "大便","睡觉"};
    private int[] mIconUnselectIds = {
            R.drawable.tools_menu1, R.drawable.tools_menu2,
            R.drawable.tools_menu3,
            R.drawable.tools_menu5, R.drawable.tools_menu6,R.drawable.tools_menu7};
    private int[] mIconSelectIds = {
            R.drawable.tools_menu1, R.drawable.tools_menu2,
            R.drawable.tools_menu3,
            R.drawable.tools_menu5, R.drawable.tools_menu6,R.drawable.tools_menu7};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private FrameLayout vp;
    @Autowired
    String childId;
    @Autowired
    int postion=0;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_diary_sum_chart;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        topBar.setTitle("统计");
        List<String> titles = Arrays.asList(mTitles);
        fragments.add(ToolsFeedSumChartFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("recordType","1").puts("childId",childId)));
        fragments.add(ToolsFeedSumChartFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("recordType","2").puts("childId",childId)));
        fragments.add(ToolsFeedSumChartFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("recordType","3").puts("childId",childId)));
        fragments.add(ToolsFeedSumChartFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("recordType","5").puts("childId",childId)));
        fragments.add(ToolsFeedSumChartFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("recordType","6").puts("childId",childId)));
        fragments.add(ToolsFeedSumChartFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("recordType","7").puts("childId",childId)));

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        mTabLayout_2.setTabData(mTabEntities,this,R.id.vp,fragments);
        mTabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                for (int i = 0; i < mTabLayout_2.getTabCount(); i++) {
                    if (position == i) {
                        if(position == 0){
                            mTabLayout_2.setIndicatorColor(Color.parseColor("#FF8E99"));
                            mTabLayout_2.getTitleView(i).setTextColor(Color.parseColor("#FF8E99"));
                        }
                        if(position == 1){
                            mTabLayout_2.setIndicatorColor(Color.parseColor("#FF9E6D"));
                            mTabLayout_2.getTitleView(i).setTextColor(Color.parseColor("#FF9E6D"));
                        }
                        if(position == 2){
                            mTabLayout_2.setIndicatorColor(Color.parseColor("#7BBCEE"));
                            mTabLayout_2.getTitleView(i).setTextColor(Color.parseColor("#7BBCEE"));
                        }
//                        if(position == 3){
//                            mTabLayout_2.setIndicatorColor(Color.parseColor("#5FE1C4"));
//                            mTabLayout_2.getTitleView(i).setTextColor(Color.parseColor("#5FE1C4"));
//                        }
                        if(position == 3){
                            mTabLayout_2.setIndicatorColor(Color.parseColor("#35CFC9"));
                            mTabLayout_2.getTitleView(i).setTextColor(Color.parseColor("#35CFC9"));
                        }
                        if(position == 4){
                            mTabLayout_2.setIndicatorColor(Color.parseColor("#C49481"));
                            mTabLayout_2.getTitleView(i).setTextColor(Color.parseColor("#C49481"));
                        }
                        if(position == 5){
                            mTabLayout_2.setIndicatorColor(Color.parseColor("#6C7997"));
                            mTabLayout_2.getTitleView(i).setTextColor(Color.parseColor("#6C7997"));
                        }
                        mTabLayout_2.getTitleView(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
                        mTabLayout_2.getIconView(i).setScaleX(1.2f);
                        mTabLayout_2.getIconView(i).setScaleY(1.2f);
                    } else {
                        mTabLayout_2.getTitleView(i).setTextColor(Color.parseColor("#444444"));
                        mTabLayout_2.getTitleView(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                        mTabLayout_2.getIconView(i).setScaleX(1f);
                        mTabLayout_2.getIconView(i).setScaleY(1f);
                    }

                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mTabLayout_2.setCurrentTab(postion);
        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                ToolsFeedBaseFragment toolsFeedBaseFragment= (ToolsFeedBaseFragment) fragments.get(mTabLayout_2.getCurrentTab());
                toolsFeedBaseFragment.updateGrow();
            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        mTabLayout_2 = (CommonTabLayout) findViewById(R.id.tl);
        tlDiv = (TextView) findViewById(R.id.tlDiv);
        vp = (FrameLayout) findViewById(R.id.vp);
    }
}
