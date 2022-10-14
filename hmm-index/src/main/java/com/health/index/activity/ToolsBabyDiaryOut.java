package com.health.index.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
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
import com.health.index.fragment.ToolsFeedMaxFragment;
import com.health.index.fragment.ToolsFeedMinFragment;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.model.TabEntity;
import com.healthy.library.widget.TopBar;
import com.hyb.library.PreventKeyboardBlockUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(path = IndexRoutes.INDEX_TOOLS_BABY_DIARY_OUT)
public class ToolsBabyDiaryOut extends BaseActivity implements IsFitsSystemWindows {
    private TopBar topBar;
    private CommonTabLayout mTabLayout_2;
    private TextView tlDiv;
    private String[] mTitles = {"小便", "大便"};
    private int[] mIconUnselectIds = {
            R.drawable.tools_menu5, R.drawable.tools_menu6};
    private int[] mIconSelectIds = {
            R.drawable.tools_menu5, R.drawable.tools_menu6};
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private FrameLayout vp;
    @Autowired
    String childId;
    @Autowired
    int postion=-1;
    @Autowired
    String recordId;
    @Autowired
    String init;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_diary_feed;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        List<String> titles = Arrays.asList(mTitles);
        fragments.add(ToolsFeedMinFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("init",init).puts("recordId",recordId).puts("childId",childId)));
        fragments.add(ToolsFeedMaxFragment.newInstance(new SimpleHashMapBuilder<String, Object>().puts("init",init).puts("recordId",recordId).puts("childId",childId)));

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        if(postion!=-1){
            mTabLayout_2.setVisibility(View.GONE);
            tlDiv.setVisibility(View.GONE);
        }
        mTabLayout_2.setTabData(mTabEntities,this,R.id.vp,fragments);
        mTabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(final int position) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ToolsFeedBaseFragment feedBaseFragment= (ToolsFeedBaseFragment) fragments.get(position);
                            PreventKeyboardBlockUtil.getInstance().setContext(mActivity).unRegister();
                            PreventKeyboardBlockUtil.getInstance().setContext(mActivity).setBtnView(feedBaseFragment.getBottomView()).register();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },300);

                topBar.setTitle(mTitles[position]);
                for (int i = 0; i < mTabLayout_2.getTabCount(); i++) {
                    if (position == i) {
                        if(position == 0){
                            mTabLayout_2.setIndicatorColor(Color.parseColor("#35CFC9"));
                            mTabLayout_2.getTitleView(i).setTextColor(Color.parseColor("#35CFC9"));
                        }
                        if(position == 1){
                            mTabLayout_2.setIndicatorColor(Color.parseColor("#C49481"));
                            mTabLayout_2.getTitleView(i).setTextColor(Color.parseColor("#C49481"));
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
            public void onTabReselect(final int position) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ToolsFeedBaseFragment feedBaseFragment= (ToolsFeedBaseFragment) fragments.get(position);
                            PreventKeyboardBlockUtil.getInstance().setContext(mActivity).unRegister();
                            PreventKeyboardBlockUtil.getInstance().setContext(mActivity).setBtnView(feedBaseFragment.getBottomView()).register();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },300);
            }
        });
        mTabLayout_2.setCurrentTab(postion==-1?1:postion);

        for (int i = 0; i < mTabLayout_2.getTabCount(); i++) {
            if (mTabLayout_2.getCurrentTab() == i) {
                if(mTabLayout_2.getCurrentTab() == 0){
                    mTabLayout_2.setIndicatorColor(Color.parseColor("#35CFC9"));
                    mTabLayout_2.getTitleView(i).setTextColor(Color.parseColor("#35CFC9"));
                }
                if(mTabLayout_2.getCurrentTab() == 1){
                    mTabLayout_2.setIndicatorColor(Color.parseColor("#C49481"));
                    mTabLayout_2.getTitleView(i).setTextColor(Color.parseColor("#C49481"));
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
        topBar.setTitle(mTitles[postion==-1?1:postion]);
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
