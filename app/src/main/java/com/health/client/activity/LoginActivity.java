package com.health.client.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.health.client.R;
import com.health.client.fragment.CodeLoginFragment;
import com.health.client.fragment.PasswordLoginFragment;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.ChangeIPDialog;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNoNeedPatchShow;
import com.healthy.library.net.RetrofitHelper;
import com.healthy.library.presenter.ChannelPresenterCopy;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.LoginSlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/03/13 14:57
 * @des 登录界面
 */

@Route(path = AppRoutes.APP_LOGIN)
public class LoginActivity extends BaseActivity implements IsFitsSystemWindows,IsNoNeedPatchShow {

    private ImageView ivClose;
    private LoginSlidingTabLayout tl3;
    private ViewPager tabViewPager;
    private TextView changeIP;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
    private CanReplacePageAdapter fragmentPagerItemAdapter;
    int currentIndex = 0;

    @Autowired
    String shanyanType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findViews() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        tl3 = (LoginSlidingTabLayout) findViewById(R.id.tl_3);
        tabViewPager = (ViewPager) findViewById(R.id.tabViewPager);
        changeIP = (TextView) findViewById(R.id.changeIP);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (ChannelUtil.isRealRelease()) {
            changeIP.setVisibility(View.GONE);
        } else {
            changeIP.setVisibility(View.VISIBLE);
            setIpTxt();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ChannelPresenterCopy(this).getIsAuditing(new SimpleHashMapBuilder<>());
    }

    public void onLogoutSuccess() {
        SpUtils.store(mContext, SpKey.USER_ID, null);
        SpUtils.store(mContext, SpKey.STATUS, null);
        String testIP = SpUtils.getValue(mContext, SpKey.TEST_IP);
        boolean isShowUserGift = SpUtils.getValue(mContext, "isShowUserGift", false);
        boolean isShowUserActGift = SpUtils.getValue(mContext, "isShowUserActGift", false);
        String value = SpUtils.getValue(mContext, "redDotKey");
        SpUtils.clear(mContext);
        SpUtils.store(mContext,"redDotKey",value);
        //新客礼包首次进入引导 -> 2021/11/02 跟手机进行绑定 不根据当前用户绑定
        SpUtils.store(mContext, "isShowUserGift", isShowUserGift);
        //节日礼包首次进入引导
        SpUtils.store(mContext, "isShowUserActGift", isShowUserActGift);
        SpUtils.store(mContext, SpKey.TEST_IP, testIP);
        SpUtils.store(mContext, SpKey.SHOW_GUIDE, false);
        SpUtils.store(mContext, "isShowZZ", true);
        SpUtils.store(mContext, "专家答疑Guide", 1);
        SpUtils.store(mContext, "完善资料Guide", 1);
        SpUtils.store(mContext, "同城憨妈Guide", 1);
        SpUtils.isFirst(mContext, "floatMall2");
        SpUtils.isFirst(mContext, "floatPost");
        SpUtils.isFirst(mContext, "floatFaq");
        RetrofitHelper.clear();
        setIpTxt();
    }

    private ChangeIPDialog changeIPDialog;

    public void setIp(View view) {
        changeIPDialog = ChangeIPDialog.newInstance();
        changeIPDialog.show(getSupportFragmentManager(), "IP");
        changeIPDialog.setResultListener(new ChangeIPDialog.getContentListener() {
            @Override
            public void resultContent(String result) {
                SpUtils.store(mContext, SpKey.TEST_IP, result + "");
                Toast.makeText(mContext, "修改IP成功等30秒再登录\n以免短信收不到", Toast.LENGTH_LONG).show();
                onLogoutSuccess();
            }
        });
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setTab();
        if (shanyanType != null && "0".equals(shanyanType)) {
            ivClose.setImageResource(R.drawable.ic_back);
        } else {
            ivClose.setImageResource(R.drawable.index_ic_close);
        }
    }

    private void setTab() {
        mTitles.clear();
        mFragments.clear();
        mTitles.add("手机快捷登录");
        mTitles.add("账号密码登录");
        mFragments.add(CodeLoginFragment.newInstance());
        mFragments.add(PasswordLoginFragment.newInstance());
        fragmentPagerItemAdapter = new CanReplacePageAdapter(getSupportFragmentManager(), mFragments, mTitles);
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
                tv_tab.setTextColor(Color.parseColor("#999999"));
                tv_tab.setTextSize(16);
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tab_item, null);
        TextView tv = (TextView) view.findViewById(com.health.discount.R.id.tv_tab_title);
        if (position == currentIndex) {
            tv.getPaint().setFakeBoldText(true);//加粗
            tv.setTextColor(Color.parseColor("#333333"));
        } else {
            tv.getPaint().setFakeBoldText(false);
            tv.setTextColor(Color.parseColor("#999999"));
        }
        tv.setText(mTitles.get(position));
        return view;
    }

    private void setIpTxt() {
        if (RetrofitHelper.getIp(mContext).contains("47.111.169.73")) {
            changeIP.setText("切换ip 当前:47");
        } else if (RetrofitHelper.getIp(mContext).contains("capi.hanmama.com")) {
            changeIP.setText("切换ip 当前:线上");
        } else if (RetrofitHelper.getIp(mContext).contains("192.168.10.100")) {
            changeIP.setText("切换ip 当前:100");
        } else if (RetrofitHelper.getIp(mContext).contains("192.168.10.101")) {
            changeIP.setText("切换ip 当前:101");
        } else if (RetrofitHelper.getIp(mContext).contains("192.168.10.102")) {
            changeIP.setText("切换ip 当前:102");
        } else if (RetrofitHelper.getIp(mContext).contains("192.168.10.103")) {
            changeIP.setText("切换ip 当前:103");
        } else {
            changeIP.setText("切换ip 当前:" + RetrofitHelper.getIp(mContext)
                    .replace("/cserver/public/","")
            );
        }
    }
}
