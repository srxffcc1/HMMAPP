package com.health.discount.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.google.android.material.tabs.TabLayout;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.ActTabInfo;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.utils.TransformUtil;

import java.util.List;

public class ActZTabAdapter extends BaseAdapter<String> {
    Context mcontext;
    boolean isInit=false;
    private StickyLayoutHelper stickyLayoutHelper;
    private int mTopHeight;
    public void setTopheight(int height) {
        this.mTopHeight = height;
        if(stickyLayoutHelper != null) {
            //242 展示一排品牌logo名称情况
            if(mTopHeight == 242){
            }
            mTopHeight = (int)TransformUtil.dp2px(mcontext,90f);
            stickyLayoutHelper.setOffset(mTopHeight);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return 20;
    }
    public ActZTabAdapter(Context mcontext,Fragment fragment) {
        this(R.layout.dis_item_fragment_tab);
        this.mcontext=mcontext;
    }
    private ActZTabAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        stickyLayoutHelper = new StickyLayoutHelper();
        stickyLayoutHelper.setStickyStart(true);
        stickyLayoutHelper.setOffset((int)TransformUtil.dp2px(mcontext,90f));
        return stickyLayoutHelper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        TabLayout st=baseHolder.itemView.findViewById(R.id.st);
        if(!isInit){
            initTab(new SimpleArrayListBuilder<ActTabInfo>()
                            .adds(new ActTabInfo("热门推荐", "猜你喜欢", ""))
                            .adds(new ActTabInfo("服务精选", "贴心呵护", ""))
                            .adds(new ActTabInfo("惠购同城", "为你精选", ""))
                            .adds(new ActTabInfo("憨妈直播", "精彩不断", ""))
                    ,st);
            isInit=true;

        }

    }
    public void initTab(List<ActTabInfo> actTabInfos,TabLayout st) {
        for (int i = 0; i <actTabInfos.size() ; i++) {
            st.addTab(st.newTab());
        }
        buildTab(actTabInfos,st);
        st.selectTab(st.getTabAt(1));
        st.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabStatus(tab.getCustomView(), true);
                moutClickListener.outClick("切换Tab",tab.getPosition()+"");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabStatus(tab.getCustomView(), false);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void buildTab(List<ActTabInfo> actTabInfos,TabLayout st) {
        if (actTabInfos.size() > 4) {
            st.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            st.setTabMode(TabLayout.MODE_FIXED);
        }
        for (int i = 0; i < st.getTabCount(); i++) {
            //插入tab标签
            TabLayout.Tab tab = st.getTabAt(i);
            if (tab != null) {
                View result = LayoutInflater.from(context).inflate(R.layout.dis_function_tab, st, false);
                if (i == 1) {
                    changeTabStatus(result, true);
                } else {
                    changeTabStatus(result, false);
                }
                TextView titleFirst = result.findViewById(R.id.titleFirst);
                ImageView titleBg = result.findViewById(R.id.titleBg);
                TextView titleSecond = result.findViewById(R.id.titleSecond);
                titleFirst.setText(actTabInfos.get(i).tabTitle);
                titleSecond.setText(actTabInfos.get(i).tabSubTitle);
                tab.setCustomView(result);
            }
        }
    }
    private void changeTabStatus(View view, boolean selected) {
        if (view != null) {
            TextView titleFirst = view.findViewById(R.id.titleFirst);
            ImageView titleBg = view.findViewById(R.id.titleBg);
            TextView titleSecond = view.findViewById(R.id.titleSecond);
            if (selected) {
                titleBg.setVisibility(View.VISIBLE);
                titleFirst.setTextColor(Color.parseColor("#F02846"));
                titleSecond.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                titleBg.setVisibility(View.INVISIBLE);
                titleFirst.setTextColor(Color.parseColor("#454649"));
                titleSecond.setTextColor(Color.parseColor("#666666"));

            }
        }
    }

}
