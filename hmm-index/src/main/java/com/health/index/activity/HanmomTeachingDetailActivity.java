package com.health.index.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.health.index.R;
import com.health.index.contract.HanMomVideoContract;
import com.health.index.fragment.HanmomTeachingDetailCommentFragment;
import com.health.index.fragment.HanmomTeachingDetailFragment;
import com.healthy.library.interfaces.AppBarStateChangeListener;
import com.healthy.library.model.VideoListModel;
import com.health.index.presenter.HanMomVideoPresenter;
import com.healthy.library.adapter.CanReplacePageAdapter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.message.CommentEvent;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.VideoCategory;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.SpUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Route(path = IndexRoutes.INDEX_HANMOMTEACHINGDETAIL)
public class HanmomTeachingDetailActivity extends BaseActivity implements IsFitsSystemWindows, HanMomVideoContract.View, IsNeedShare, OnRefreshListener {

    @Autowired
    String id;
    @Autowired
    int type;

    private SmartRefreshLayout refreshLayout;
    private AppBarLayout appBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView videoImg;
    private ImageView livePlayerImg;
    private TextView videoTitle;
    private TextView videoContent;
    private TextView videoLable;
    private TextView seeNum;
    private TextView videoLableLine;
    private LinearLayout underLL;
    private TabLayout st;
    private ViewPager pager;
    private ConstraintLayout topView;
    private View viewHeaderBg;
    private View viewTop;
    private ImageView imgBack;
    private ImageView shareImg;
    private ImageView praiseImg;
    private ConstraintLayout bottomCommentLayout;
    private LinearLayout commentLayout;
    private LinearLayout commentNumLayout;
    private TextView commentNum;
    private LinearLayout clickLayout;
    private TextView clickNum;

    private List<Fragment> mFragmentList;
    private List<String> mTitles;
    private CanReplacePageAdapter pageAdapter;
    private HanMomVideoPresenter hanMomVideoPresenter;
    String surl;
    String sdes;
    String stitle;
    Bitmap sBitmap;
    private boolean isPraise = false;//是否点赞
    private VideoListModel videoListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hanmom_teaching_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
        mTitles = new ArrayList<>();
        mFragmentList = new ArrayList<>();
        hanMomVideoPresenter = new HanMomVideoPresenter(this, this);
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getData() {
        super.getData();
        hanMomVideoPresenter.getVideoDetail(new SimpleHashMapBuilder<String, Object>().puts("id", id)
                .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))), 1);
        hanMomVideoPresenter.getUserInfo(new SimpleHashMapBuilder<String, Object>());
    }

    public void initTab(List<String> actTabInfos) {
        mTitles.clear();
        mFragmentList.clear();
        for (int i = 0; i < actTabInfos.size(); i++) {
            st.addTab(st.newTab());
            mTitles.add(actTabInfos.get(i));
        }
        mFragmentList.add(HanmomTeachingDetailFragment.newInstance(id, null));
        mFragmentList.add(HanmomTeachingDetailCommentFragment.newInstance(id, null));
        pager.setOffscreenPageLimit(mFragmentList.size() - 1);
        pageAdapter = new CanReplacePageAdapter(getSupportFragmentManager(), mFragmentList, mTitles);
        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(0);
        st.setupWithViewPager(pager);
        st.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabStatus(tab.getCustomView(), true);
                if (tab == null || tab.getText() == null) {
                    return;
                }
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan styleSpan_B = new StyleSpan(Typeface.NORMAL);
                spStr.setSpan(new AbsoluteSizeSpan(18, true), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spStr.setSpan(styleSpan_B, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                if (spStr.length() > 2) {
                    spStr.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 3, spStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spStr.setSpan(new AbsoluteSizeSpan(12, true), 3, spStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tab.setText(spStr);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabStatus(tab.getCustomView(), false);
                if (tab == null || tab.getText() == null) {
                    return;
                }
                String trim = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(trim);
                StyleSpan styleSpan_B = new StyleSpan(Typeface.NORMAL);
                spStr.setSpan(new AbsoluteSizeSpan(16, true), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spStr.setSpan(styleSpan_B, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                if (spStr.length() > 2) {
                    spStr.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 3, spStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spStr.setSpan(new AbsoluteSizeSpan(12, true), 3, spStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tab.setText(spStr);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        buildTab(actTabInfos, st);
    }

    private void buildTab(List<String> actTabInfos, TabLayout st) {
        for (int i = 0; i < st.getTabCount(); i++) {
            //插入tab标签
            TabLayout.Tab tab = st.getTabAt(i);
            if (tab != null) {
                View result = LayoutInflater.from(mContext).inflate(R.layout.item_tab_layout, st, false);
                TextView titleFirst = result.findViewById(R.id.titleFirst);
                View indicatorView = result.findViewById(R.id.indicatorView);
                titleFirst.setText(actTabInfos.get(i));
                if (i == 0) {
                    indicatorView.setVisibility(View.VISIBLE);
                    changeTabStatus(result, true);
                } else {
                    indicatorView.setVisibility(View.GONE);
                    changeTabStatus(result, false);
                }
                tab.setCustomView(result);
            }
        }
    }

    private void changeTabStatus(View view, boolean selected) {
        if (view != null) {
            TextView titleFirst = view.findViewById(R.id.titleFirst);
            View indicatorView = view.findViewById(R.id.indicatorView);
            if (selected) {
                indicatorView.setVisibility(View.VISIBLE);
                titleFirst.setTextColor(Color.parseColor("#333333"));
            } else {
                indicatorView.setVisibility(View.GONE);
                titleFirst.setTextColor(Color.parseColor("#666666"));
            }
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        videoImg = (ImageView) findViewById(R.id.videoImg);
        livePlayerImg = (ImageView) findViewById(R.id.livePlayerImg);
        videoTitle = (TextView) findViewById(R.id.videoTitle);
        videoContent = (TextView) findViewById(R.id.videoContent);
        videoLable = (TextView) findViewById(R.id.videoLable);
        seeNum = (TextView) findViewById(R.id.seeNum);
        underLL = (LinearLayout) findViewById(R.id.underLL);
        st = (TabLayout) findViewById(R.id.tab);
        pager = (ViewPager) findViewById(R.id.pager);
        topView = (ConstraintLayout) findViewById(R.id.topView);
        viewHeaderBg = (View) findViewById(R.id.view_header_bg);
        viewTop = (View) findViewById(R.id.viewTop);
        imgBack = (ImageView) findViewById(R.id.img_back);
        shareImg = (ImageView) findViewById(R.id.shareImg);
        praiseImg = (ImageView) findViewById(R.id.praiseImg);
        bottomCommentLayout = (ConstraintLayout) findViewById(R.id.bottomCommentLayout);
        commentLayout = (LinearLayout) findViewById(R.id.commentLayout);
        commentNumLayout = (LinearLayout) findViewById(R.id.commentNumLayout);
        commentNum = (TextView) findViewById(R.id.commentNum);
        videoLableLine = (TextView) findViewById(R.id.videoLableLine);
        clickLayout = (LinearLayout) findViewById(R.id.clickLayout);
        clickNum = (TextView) findViewById(R.id.clickNum);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnRefreshListener(this);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInput();
                finish();
            }
        });
        videoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1) {
                    hideInput();
                    finish();
                } else {
                    if (videoListModel != null) {
                        ARouter.getInstance()
                                .build(IndexRoutes.INDEX_HANMOMVIDEODETAIL)
                                .withString("id", id)
                                .withString("categoryCode", videoListModel.categoryCode)
                                .navigation();
                    }
                }
            }
        });
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
        commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new CommentEvent(1));
            }
        });
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    viewTop.setVisibility(View.GONE);
                    //展开状态
                } else if (state == State.COLLAPSED) {
                    viewTop.setVisibility(View.VISIBLE);
                    //折叠状态
                } else {
                    //中间状态
                }
            }
        });
    }

    @Override
    public void onGetTabListSuccess(List<VideoCategory> result) {

    }

    @Override
    public void onGetVideoListSuccess(List<VideoListModel> result, PageInfoEarly pageInfoEarly) {

    }

    @Override
    public void onGetVideoDetailSuccess(final VideoListModel result, int type) {
        if (result != null) {
            this.videoListModel = result;
            if (type == 1) {//1表示初次请求需要初始化  2表示刷新数据
                refreshLayout.finishRefresh();
                buildDes(result);
                Glide.with(mContext).load(result.photo)
                        .placeholder(R.drawable.hanmom_video_list_default)
                        .error(R.drawable.hanmom_video_list_default)
                        
                        .into(new SimpleTarget<Drawable>() {

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                sBitmap = DrawableUtils.drawableToBitmap(resource);
                            }
                        });
                Glide.with(mContext).load(result.photo)
                        .placeholder(R.drawable.hanmom_video_list_default)
                        .error(R.drawable.hanmom_video_list_default)
                        
                        .into(videoImg);
                videoTitle.setText(result.videoName);
                videoContent.setText(result.videoRemark);
                if (result.isFree == 1) {
                    videoLable.setText("免费");
                } else if (result.isFree == 2) {
                    videoLable.setText("限时免费");
                    videoLableLine.setText("￥" + result.videoPrice);
                    videoLableLine.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                } else if (result.isFree == 3) {
                    videoLable.setText("￥" + result.videoPrice);
                }
                seeNum.setText((result.realView + result.virtualView) + "人已看");

                List<String> list = new ArrayList<>();
                list.add("介绍");
                list.add("留言 (" + result.discussCount + ")");
                initTab(list);
                pageAdapter.setPageTitle(0, "介绍");
                pageAdapter.setPageTitle(1, "留言 (" + result.discussCount + ")");
                commentNum.setText(result.discussCount + "");
                clickNum.setText(result.praiseCount + "");
                if (result.praise) {
                    isPraise = true;
                    praiseImg.setImageResource(R.drawable.hanmom_videodetial_clicksuccess_icon);
                } else {
                    isPraise = false;
                    praiseImg.setImageResource(R.drawable.hanmom_videodetial_click_grey);
                }
                clickLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isPraise) {
                            hanMomVideoPresenter.addPraise(new SimpleHashMapBuilder<String, Object>()
                                            .puts(Functions.FUNCTION, "8098")
                                            .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                                            .puts("videoId", result.id),
                                    2);
                        } else {
                            hanMomVideoPresenter.addPraise(new SimpleHashMapBuilder<String, Object>()
                                            .puts(Functions.FUNCTION, "8097")
                                            .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                                            .puts("videoId", result.id),
                                    1);
                        }
                    }
                });
            } else {
                pageAdapter.setPageTitle(1, "留言 (" + result.discussCount + ")");
            }
        }
    }

    @Override
    public void onAddPraiseSuccess(String result, int type) {
        if (type == 1) {
            showToast("点赞成功");
            isPraise = true;
            praiseImg.setImageResource(R.drawable.hanmom_videodetial_clicksuccess_icon);
        } else {
            showToast("取消赞成功");
            isPraise = false;
            praiseImg.setImageResource(R.drawable.hanmom_videodetial_click_grey);
        }
        if (result != null && !TextUtils.isEmpty(result)) {
            clickNum.setText(result + "");
        }
    }

    @Override
    public String getSurl() {
        return surl;
    }

    @Override
    public String getSdes() {
        return sdes;
    }

    @Override
    public String getStitle() {
        return stitle;
    }

    @Override
    public Bitmap getsBitmap() {
        return sBitmap;
    }

    private void buildDes(VideoListModel result) {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_classVideoContUrl);
        String url = String.format("%s?id=%s&scheme=HMMVideoDetail&videoId=%s", urlPrefix, id, id);
        surl = url;
        stitle = "憨妈妈专家课堂";
        sdes = result.videoName;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeComment(CommentEvent msg) {
        if (msg.type == 2) {
            hanMomVideoPresenter.getVideoDetail(new SimpleHashMapBuilder<String, Object>().puts("id", id)
                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))), 2);
        }
    }

    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
        getData();
    }
}