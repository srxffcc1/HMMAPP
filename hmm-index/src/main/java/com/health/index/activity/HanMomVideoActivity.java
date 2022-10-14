package com.health.index.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.health.index.R;
import com.health.index.fragment.HanMomVideoFragment;
import com.health.index.fragment.HanMomVideoTeachingFragment;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.SpUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(path = IndexRoutes.INDEX_VIDEOONLINELIST)
public class HanMomVideoActivity extends BaseActivity implements IsFitsSystemWindows, IsNeedShare {

    @Autowired
    String type;
    private ConstraintLayout topView;
    private View viewHeaderBg;
    private ImageView imgBack;
    private TabLayout tab;
    private ImageView shareImg;
    private ViewPager pager;

    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    private CanReplacePageAdapter pageAdapter;

    String surl;
    String sdes;
    String stitle;
    private ImageView mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_han_mom_video;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mTitles = Arrays.asList("发现 ", "课堂 ");
        mFragmentList = new ArrayList<>();
        mFragmentList.add(HanMomVideoFragment.newInstance(null, null));
        mFragmentList.add(HanMomVideoTeachingFragment.newInstance(null, null));
        pager.setOffscreenPageLimit(mFragmentList.size() - 1);
        pageAdapter = new CanReplacePageAdapter(getSupportFragmentManager(), mFragmentList, mTitles);
        pager.setAdapter(pageAdapter);
        if (type != null && type.equals("1")) {
            pager.setCurrentItem(1);
        } else {
            pager.setCurrentItem(0);
        }
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == null || tab.getText() == null) {
                    return;
                }
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
                spStr.setSpan(new AbsoluteSizeSpan(20, true), 0, trim.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spStr.setSpan(styleSpan_B, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == null || tab.getText() == null) {
                    return;
                }
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan styleSpan_B = new StyleSpan(Typeface.NORMAL);
                spStr.setSpan(new AbsoluteSizeSpan(16, true), 0, trim.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spStr.setSpan(styleSpan_B, 0, trim.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        tab.setupWithViewPager(pager);

    }

    @Override
    protected void findViews() {
        super.findViews();
        topView = (ConstraintLayout) findViewById(R.id.topView);
        viewHeaderBg = (View) findViewById(R.id.view_header_bg);
        imgBack = (ImageView) findViewById(R.id.img_back);
        tab = (TabLayout) findViewById(R.id.tab);
        shareImg = (ImageView) findViewById(R.id.shareImg);
        mSearch = findViewById(R.id.iv_search);
        pager = (ViewPager) findViewById(R.id.pager);
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(LibraryRoutes.LIBRARY_HMM_SEARCH)
                        .withInt("searchType", 2)
                        .navigation();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public String getSurl() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_expertClassListUrl);
        String url = String.format("%s?scheme=HMMVideoOnLine", urlPrefix);
        return url;
    }

    @Override
    public String getSdes() {
        sdes = "智慧孕育·憨妈妈";
        return sdes;
    }

    @Override
    public String getStitle() {
        stitle = "憨妈妈专家课堂";
        return stitle;
    }

    @Override
    public Bitmap getsBitmap() {
        return BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.index_share_humb));
    }
}