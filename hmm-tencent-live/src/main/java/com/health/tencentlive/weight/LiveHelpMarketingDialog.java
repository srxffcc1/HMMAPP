package com.health.tencentlive.weight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.health.tencentlive.R;
import com.health.tencentlive.contract.LiveHelpContract;
import com.health.tencentlive.fragment.HelpListFragment;
import com.health.tencentlive.fragment.MyHelpListFragment;
import com.health.tencentlive.fragment.MyPrizeFragment;
import com.health.tencentlive.impl.OnHelpClickListener;
import com.health.tencentlive.model.JackpotList;
import com.health.tencentlive.model.LiveHelpList;
import com.health.tencentlive.presenter.LiveHelpPresenter;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.LiveSlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;


public class LiveHelpMarketingDialog extends DialogFragment implements LiveHelpContract.View {
    private ImageView closeImg;
    private ConstraintLayout tabLayout;
    private LiveSlidingTabLayout tl3;
    private ImageView tips;
    private ViewPager tabViewPager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
    private CanReplacePageAdapter fragmentPagerItemAdapter;
    private int currentIndex = 0;
    private String courseId;
    private String shopId;
    private String merchantId;
    private String liveShareType;
    private OnClickListener onClickListener;
    private LiveRoomInfo mLiveRoomInfo;
    private LiveHelpPresenter liveHelpPresenter;
    private boolean isHeight = false;//当前高度是否是最大
    private String groupId;
    private HelpListFragment helpListFragment;
    private MyHelpListFragment myHelpListFragment;
    private MyPrizeFragment myPrizeFragment;

    public LiveHelpMarketingDialog setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public LiveHelpMarketingDialog setClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public static LiveHelpMarketingDialog newInstance() {

        Bundle args = new Bundle();
        LiveHelpMarketingDialog fragment = new LiveHelpMarketingDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public LiveHelpMarketingDialog setCourseId(String courseId, LiveRoomInfo mLiveRoomInfo, String shopId, String merchantId) {
        this.courseId = courseId;
        this.mLiveRoomInfo = mLiveRoomInfo;
        this.merchantId = merchantId;
        this.shopId = shopId;
        return this;
    }

    public LiveHelpMarketingDialog setLiveShareType(String liveShareType) {
        this.liveShareType = liveShareType;
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setAttribute(0.7f);//默认设置dialog高度为屏幕高度一半
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    private void setAttribute(float heightScale) {
        if (heightScale > 0.6) {
            isHeight = true;
        } else {
            isHeight = false;
        }
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            Display display = window.getWindowManager().getDefaultDisplay();
            params.dimAmount = 0.5f;
            params.gravity = Gravity.BOTTOM;
            params.width = display.getWidth();
            params.height = (int) (int) (display.getHeight() * heightScale);
            window.setAttributes(params);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_live_help_marketing_layout, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View view) {
        liveHelpPresenter = new LiveHelpPresenter(getContext(), this);
        closeImg = (ImageView) view.findViewById(R.id.closeImg);
        tabLayout = (ConstraintLayout) view.findViewById(R.id.tabLayout);
        tl3 = (LiveSlidingTabLayout) view.findViewById(R.id.tl_3);
        tips = (ImageView) view.findViewById(R.id.tips);
        tabViewPager = (ViewPager) view.findViewById(R.id.tabViewPager);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setTab();
        getData();
    }

    public void refresh() {
        if (myPrizeFragment != null) {
            myPrizeFragment.refresh();
        }
    }

    private void setTab() {
        mTitles.clear();
        mFragments.clear();
        mTitles.add("助力榜单");
        mTitles.add("我的助力");
        mTitles.add("奖品池");
        helpListFragment = HelpListFragment.newInstance(courseId, null);
        helpListFragment.setClickListener(new OnHelpClickListener() {
            @Override
            public void onShare() {
                share();
            }
        });
        mFragments.add(helpListFragment);
        myHelpListFragment = MyHelpListFragment.newInstance(courseId, null);
        myHelpListFragment.setClickListener(new OnHelpClickListener() {
            @Override
            public void onShare() {
                share();
            }
        });
        mFragments.add(myHelpListFragment);
        myPrizeFragment = MyPrizeFragment.newInstance(courseId, shopId);
        myPrizeFragment.setClickListener(new OnHelpClickListener() {
            @Override
            public void onShare() {
                share();
            }
        });
        mFragments.add(myPrizeFragment);
        fragmentPagerItemAdapter = new CanReplacePageAdapter(getChildFragmentManager(), mFragments, mTitles);
        // adapter
        tabViewPager.setAdapter(fragmentPagerItemAdapter);
        fragmentPagerItemAdapter.notifyDataSetChanged();
        tabViewPager.setOffscreenPageLimit(mFragments.size() - 1);
        tl3.setupWithViewPager(tabViewPager);
        tl3.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < tl3.getTabCount(); i++) {
            TabLayout.Tab tab = tl3.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
                if (tab.getCustomView() != null) {
                    View tabView = (View) tab.getCustomView().getParent();
                    tabView.setTag(i);
                }
            }
        }
        tabViewPager.setCurrentItem(currentIndex);
        tl3.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentIndex = tab.getPosition();
                TextView tv_tab = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_title);
                tv_tab.getPaint().setFakeBoldText(true);//加粗
                tv_tab.setTextColor(Color.parseColor("#333333"));
                tv_tab.setTextSize(16);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tv_tab = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_title);
                tv_tab.getPaint().setFakeBoldText(false);
                tv_tab.setTextColor(Color.parseColor("#333333"));
                tv_tab.setTextSize(14);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tl3.redrawIndicator(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tl3.resetTabParams();
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_tab_item, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_tab_title);
        if (position == currentIndex) {
            tv.getPaint().setFakeBoldText(true);//加粗
            tv.setTextColor(Color.parseColor("#333333"));
            tv.setTextSize(16);
        } else {
            tv.getPaint().setFakeBoldText(false);
            tv.setTextColor(Color.parseColor("#333333"));
            tv.setTextSize(14);
        }
        tv.setText(mTitles.get(position));
        return view;
    }

    public void share() {
        SeckShareDialog.newInstance()
                .setActivityDialog(8, 0, mLiveRoomInfo)
                .setGroupId(groupId)
                .setExtraMap(new SimpleHashMapBuilder<String, String>()
                        .puts("scheme", "LiveStream")
                        .puts("courseId", courseId)
                        .puts("fromMemberId", new String(Base64.decode(SpUtils.getValue(getContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))//助力分享记得填
                        .puts("liveShareType", "zbzl")//普通分享 zbzl助力分享记得改
                        .puts("merchantId", merchantId)
                        .puts("shopId", shopId)
                )
                .show(this.getChildFragmentManager(), "LookLiveShare");
    }

    public void scroll() {
        if (isHeight) {
            setAttribute(0.9f);
        } else {
            setAttribute(0.6f);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showToast(CharSequence msg) {

    }

    @Override
    public void showNetErr() {

    }

    @Override
    public void onRequestStart(Disposable disposable) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void getData() {
        liveHelpPresenter.getJackpotList(new SimpleHashMapBuilder<String, Object>()
                .puts("pageNum", 1)
                .puts("pageSize", 100)
                .puts("courseId", courseId)
                .puts("memberId", new String(Base64.decode(SpUtils.getValue(getContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT))));
    }

    @Override
    public void showDataErr() {

    }

    @Override
    public void getSuccessHelpRankingList(List<LiveHelpList> result, PageInfoEarly pageInfo) {

    }

    @Override
    public void getSuccessLiveHelpList(List<LiveHelpList> result, PageInfoEarly pageInfo) {

    }

    @Override
    public void getSuccessLiveHelpInfo(LiveHelpList result) {

    }

    @Override
    public void getSuccessCoupon(String result, String couponName) {

    }


    @Override
    public void getSuccessWinId(String result) {

    }

    @Override
    public void getSuccessHelpStatus(int result) {

    }

    @Override
    public void getSuccessJackpotList(final List<JackpotList> result, PageInfoEarly pageInfo) {
        if (result != null && result.size() > 0) {
            tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LiveHelpMarketingTipsDialog.newInstance().setDetail(result).show(getChildFragmentManager(), "tips");
                }
            });
        } else {
            Toast.makeText(getContext(), "未获取到活动规则", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnClickListener {
        void onHelpShare();
    }
}
