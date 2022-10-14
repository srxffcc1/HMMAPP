package com.health.discount.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.health.discount.R;
import com.health.discount.contract.SeckillListContract;
import com.health.discount.fragment.SeckillListFragment;
import com.health.discount.model.SeckillTab;
import com.health.discount.presenter.SeckillListPresenter;
import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.adapter.CanReplaceStatePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.MemberAction;
import com.healthy.library.presenter.ActionPresenterCopy;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.utils.SpUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = DiscountRoutes.DIS_SECKILLLISTACTIVITY)//秒杀列表
public class SeckillListActivity extends BaseActivity implements SeckillListContract.View, IsFitsSystemWindows, OnRefreshListener {

    @Autowired
    String shopId;
    @Autowired
    String merchantId;

    private ConstraintLayout killTop;
    private ConstraintLayout topView;
    private View viewHeaderBg;
    private ImageView imgBack;
    private ImageView imgShare;
    private LinearLayout lableLiner;
    private RelativeLayout tabLL;
    private TabLayout st;
    private ViewPager pagers;
    private SeckillListPresenter seckillListPresenter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
    private CanReplaceStatePageAdapter pageAdapter;
    private SmartRefreshLayout layoutRefresh;
    private int isRefresh = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seckill_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        seckillListPresenter = new SeckillListPresenter(this, this);

        if (TextUtils.isEmpty(shopId)) {
            shopId = SpUtils.getValue(mContext, SpKey.CHOSE_SHOP);
        }
        if (TextUtils.isEmpty(merchantId)) {
            merchantId = SpUtils.getValue(mContext, SpKey.CHOSE_MC);
        }
        getData();
        new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                new SimpleHashMapBuilder<>()
                        .putObject(new MemberAction(
                                BuildConfig.VERSION_NAME,
                                1,
                                6,
                                getActivitySimpleName(),
                                getActivitySimpleName(),
                                new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                        ));
    }

    @Override
    protected void findViews() {
        super.findViews();
        killTop = (ConstraintLayout) findViewById(R.id.killTop);
        topView = (ConstraintLayout) findViewById(R.id.topView);
        viewHeaderBg = (View) findViewById(R.id.view_header_bg);
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgShare = (ImageView) findViewById(R.id.imgShare);
        lableLiner = (LinearLayout) findViewById(R.id.lableLiner);
        tabLL = (RelativeLayout) findViewById(R.id.tabLL);
        st = (TabLayout) findViewById(R.id.tab);
        pagers = (ViewPager) findViewById(R.id.pagers);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "event2APPKillListShareClick", new SimpleHashMapBuilder<String, String>().puts("soure", "秒杀活动列表顶部分享按钮点击量"));
                SeckShareDialog dialog = SeckShareDialog.newInstance();
                dialog.setActivityDialog(3, 3, null);
                dialog.show(getSupportFragmentManager(), "分享");
            }
        });
        layoutRefresh = findViewById(R.id.layout_refresh);
        layoutRefresh.setOnRefreshListener(this);
        layoutRefresh.setEnableLoadMore(false);
        st.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabStatus(tab.getCustomView(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabStatus(tab.getCustomView(), false);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pagers.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                isRefresh = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void getData() {
        seckillListPresenter.getTabList(new SimpleHashMapBuilder<String, Object>()
                .puts("merchantId", merchantId)
                .puts("marketingType", "3")
                .puts("shopId", shopId)
                .puts(Functions.FUNCTION, "marketing_9307"));
    }

    @Override
    public void onSuccessGetTabList(SeckillTab result) {
        layoutRefresh.finishRefresh();
        mFragmentList.clear();
        mTitles.clear();
        pagers.setAdapter(null);
        pagers.removeAllViewsInLayout();
        if (result != null && result.todayActivityNew != null && result.todayActivityNew.size() > 0) { //活动列表 有 但是存在活动没有的情况
            st.setVisibility(View.VISIBLE);
            initTab(result.todayActivityNew, st, result);
        } else {//说明没设置新的活动  只有查历史活动了
            if(result.history==0){
                st.setVisibility(View.GONE);
                mTitles.add("1");
                mFragmentList.add(SeckillListFragment
                        .newInstance("0", null, "2"));
                pageAdapter = new CanReplaceStatePageAdapter(getSupportFragmentManager(), mFragmentList, mTitles);
                pagers.setAdapter(pageAdapter);
                st.setupWithViewPager(pagers);
            }else {
                st.setVisibility(View.GONE);
                mTitles.add("1");
                mFragmentList.add(SeckillListFragment
                        .newInstance("1", null, "2"));
                pageAdapter = new CanReplaceStatePageAdapter(getSupportFragmentManager(), mFragmentList, mTitles);
                pagers.setAdapter(pageAdapter);
                st.setupWithViewPager(pagers);
            }

        }
    }

    public void initTab(List<SeckillTab.TodayActivityNew> actTabInfos, final TabLayout st, SeckillTab seckillTab) {
        if (seckillTab.history == 1) {
            actTabInfos.add(0, new SeckillTab.TodayActivityNew("", "", "热卖中", 2, 0));
        }
        for (int i = 0; i < actTabInfos.size(); i++) {
            st.addTab(st.newTab());
            mTitles.add(String.format(i + ""));
            if (actTabInfos.get(i).status == 2) {
                mFragmentList.add(SeckillListFragment
                        .newInstance("1", null, "2"));
            } else {
                mFragmentList.add(SeckillListFragment
                        .newInstance(null, actTabInfos.get(i).id, actTabInfos.get(i).status + ""));
            }
        }
        pageAdapter = new CanReplaceStatePageAdapter(getSupportFragmentManager(), mFragmentList, mTitles);
        pagers.setAdapter(pageAdapter);
        st.setupWithViewPager(pagers);
        pagers.setOffscreenPageLimit(mFragmentList.size());
        buildTab(actTabInfos, st);
    }

    private void buildTab(List<SeckillTab.TodayActivityNew> actTabInfos, TabLayout st) {
        if (isRefresh > -1) {//说明是在刷新  则选中刷新前选中的那个
            for (int i = 0; i < actTabInfos.size(); i++) {
                if (i == isRefresh) {
                    actTabInfos.get(i).setCurrentNew(1);
                } else {
                    actTabInfos.get(i).setCurrentNew(0);
                }
            }
        }
        int currentNewSize = 0;//开始的活动数量  如果都没开始  则选中第一个
        for (int i = 0; i < actTabInfos.size(); i++) {
            if (actTabInfos.get(i).currentNew == 1) {
                currentNewSize++;
            }
        }
        if (currentNewSize == 0) {//说明没有正在开始的 要选中第一个
            actTabInfos.get(0).setCurrentNew(1);
        }
        for (int i = 0; i < actTabInfos.size(); i++) {
            //插入tab标签
            TabLayout.Tab tab = st.getTabAt(i);
            if (tab != null) {
                View result = LayoutInflater.from(mContext).inflate(R.layout.item_seckill_tab_layout, st, false);
                TextView titleFirst = result.findViewById(R.id.titleFirst);
                TextView titleSecond = result.findViewById(R.id.titleSecond);
                titleFirst.setText(actTabInfos.get(i).beginTimeHi);
                if (actTabInfos.get(i).currentNew == 1) {
                    changeTabStatus(result, true);
                    tab.select();
                    titleSecond.setBackground(getResources().getDrawable(R.drawable.shape_seckill_list_tab_bg));
                    if (isRefresh == -1) {
                        isRefresh = i;
                    }
                } else {
                    changeTabStatus(result, false);
                    titleSecond.setBackground(null);
                }
                if (actTabInfos.get(i).status == 0) {//未开始
                    titleSecond.setText("即将开始");
                } else if (actTabInfos.get(i).status == 1) {//抢购中
                    titleSecond.setText("抢购中");
                } else {//历史活动
                    titleSecond.setText("不要错过");
                }
                tab.setCustomView(result);
            }
        }
    }

    private void changeTabStatus(View view, boolean selected) {
        if (view != null) {
            TextView titleFirst = view.findViewById(R.id.titleFirst);
            TextView titleSecond = view.findViewById(R.id.titleSecond);
            if (selected) {
                titleSecond.setBackground(getResources().getDrawable(R.drawable.shape_seckill_list_tab_bg));
                titleFirst.setTextColor(Color.parseColor("#FF2768"));
                titleSecond.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                titleSecond.setBackground(null);
                titleFirst.setTextColor(Color.parseColor("#333333"));
                titleSecond.setTextColor(Color.parseColor("#666666"));
            }
        }

    }

    private long getDifference(String beginTime) {
        if (beginTime == null) {
            return 0;
        }
        long mill = 0;
        try {
            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date fromDate = simpleFormat.parse(beginTime);
            Date toDate = new Date();
            mill = fromDate.getTime() - toDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mill;
    }

    @Override
    public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
        getData();
        layoutRefresh.finishRefresh();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "秒杀活动列表页浏览时长");
        nokmap.put("time", (System.currentTimeMillis() - mills) / 1000);
        MobclickAgent.onEvent(mContext, "event_APP_KillLis_Stop", nokmap);

    }

    private long mills = System.currentTimeMillis();
}