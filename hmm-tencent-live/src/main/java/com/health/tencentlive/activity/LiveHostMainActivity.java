package com.health.tencentlive.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.health.tencentlive.R;
import com.health.tencentlive.contract.LikeManContract;
import com.health.tencentlive.contract.LiveHostMainContract;
import com.health.tencentlive.fragment.LiveVideoListFragment;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.LiveFans;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.model.LiveVideoMain;
import com.health.tencentlive.presenter.LikeManPresenter;
import com.health.tencentlive.presenter.LiveHostMainPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = TencentLiveRoutes.LiveHostMain)
public class LiveHostMainActivity extends BaseActivity implements IsFitsSystemWindows, LiveHostMainContract.View, OnRefreshLoadMoreListener, LikeManContract.View {

    @Autowired
    String anchormanId;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout refreshLayout;
    private com.google.android.material.appbar.AppBarLayout appBar;
    private com.google.android.material.appbar.CollapsingToolbarLayout collapsingToolbarLayout;
    private androidx.constraintlayout.widget.ConstraintLayout avTopLL;
    private com.healthy.library.widget.CornerImageView hostIcon;
    private android.widget.TextView hostName;
    private android.widget.TextView hostTimeTitle;
    private android.widget.TextView hostTime;
    private android.widget.LinearLayout avLL;
    private android.widget.LinearLayout avDesLL;
    private android.widget.LinearLayout underLL;
    private android.widget.FrameLayout fragmentParent;
    private com.healthy.library.widget.TopBar topBar;
    LiveHostMainPresenter liveHostMainPresenter;
    LikeManPresenter likeManPresenter;
    private LiveVideoListFragment liveVideoListFragment;
    int verticalOffsetold = 0;
    private TextView fansCount;
    private TextView videoCount;
    private TextView hostDes;
    private android.view.View bottomDiv;
    private com.healthy.library.widget.ImageTextView focusMan;
    private String memberId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_hoster;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        if (!TextUtils.isEmpty(anchormanId) && anchormanId.equals(SpUtils.getValue(mContext, SpKey.LIVEHOSTID))) {
            focusMan.setVisibility(View.GONE);
        }
        topBar.getTxtTitle().setAlpha(0);
        liveHostMainPresenter = new LiveHostMainPresenter(this, this);
        likeManPresenter = new LikeManPresenter(this, this);
        Map<String, Object> maporg = new HashMap<>();
        liveVideoListFragment = LiveVideoListFragment.newInstance(new SimpleHashMapBuilder<String, Object>()
                .puts("statusList", "4")
                .puts("anchormanId", anchormanId).puts("refresh", "0"));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentParent, liveVideoListFragment).commitAllowingStateLoss();
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.setEnableLoadMore(false);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {


                if (verticalOffset != verticalOffsetold) {

//                    //System.out.println("测试bar:" + appBarLayout.getHeight() + ":" + verticalOffset + ":" + collapsingToolbarLayout.getMinimumHeight());
                    if (topBar.getHeight() + verticalOffset > 0) {
                        float alpha = Math.min((verticalOffset * -1) * 1.0f / topBar.getHeight(), 1);
//                        //System.out.println("测试baralpha:" + alpha);
                        topBar.getTxtTitle().setAlpha(alpha);
                        topBar.setBackgroundColor(Color.argb((int) (255 * alpha),
                                255, 255, 255));
                    } else {
                        topBar.getTxtTitle().setAlpha(1);
                        topBar.setBackgroundColor(Color.argb((int) (255 * 1),
                                255, 255, 255));
                    }
//
//
//                    float alpha2 = Math.min(((appBarLayout.getHeight() + verticalOffset - collapsingToolbarLayout.getMinimumHeight())) * 1.0f / (appBarLayout.getHeight() - collapsingToolbarLayout.getMinimumHeight()), 1);
//                    topBar.setAlpha(alpha2);
                }
                verticalOffsetold = verticalOffset;

//                //System.out.println("测试zbarZZ:" + (appBarLayout.getHeight() + verticalOffset - collapsingToolbarLayout.getMinimumHeight()));


            }
        });
        focusMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeManPresenter.likeMan(new SimpleHashMapBuilder<String, Object>()
                        .puts("fansId", new String(Base64.decode(SpUtils.getValue(getApplicationContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                        .puts("followedId", memberId)
                        .puts("status", isFans ? "2" : "1"));
            }
        });
        initAppBarStatus();
        getData();
    }

    private void initAppBarStatus() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return true;
            }
        });
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    public void getData() {
        super.getData();
        liveHostMainPresenter.getHost(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId));
    }

    private void initView() {


        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        avTopLL = (ConstraintLayout) findViewById(R.id.avTopLL);
        hostIcon = (CornerImageView) findViewById(R.id.hostIcon);
        hostName = (TextView) findViewById(R.id.hostName);
        hostTimeTitle = (TextView) findViewById(R.id.hostTimeTitle);
        hostTime = (TextView) findViewById(R.id.hostTime);
        avLL = (LinearLayout) findViewById(R.id.avLL);
        avDesLL = (LinearLayout) findViewById(R.id.avDesLL);
        underLL = (LinearLayout) findViewById(R.id.underLL);
        fragmentParent = (FrameLayout) findViewById(R.id.fragmentParent);
        topBar = (TopBar) findViewById(R.id.top_bar);
        fansCount = (TextView) findViewById(R.id.fansCount);
        videoCount = (TextView) findViewById(R.id.videoCount);
        hostDes = (TextView) findViewById(R.id.hostDes);
        bottomDiv = (View) findViewById(R.id.bottomDiv);
        focusMan = (ImageTextView) findViewById(R.id.focusMan);

    }

    @Override
    public void onSucessGetHost(AnchormanInfo anchormanInfo) {
        if (anchormanInfo != null) {
            memberId = anchormanInfo.memberId;
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .load(anchormanInfo.avatarUrl)
                    
                    .placeholder(R.drawable.img_avatar_default)
                    .error(R.drawable.img_avatar_default)
                    .into(hostIcon);
            hostName.setText(anchormanInfo.memberName);
            hostDes.setText(anchormanInfo.liveRoom.roomIntroduce);
            fansCount.setText(anchormanInfo.fansCount + "");
            videoCount.setText(anchormanInfo.courseCount + "");
            hostTime.setText(anchormanInfo.latestLiveTime);
            topBar.setTitle(anchormanInfo.memberName);
        }
        likeManPresenter.getMineIsFans(new SimpleHashMapBuilder<String, Object>()
                .puts("fansId", new String(Base64.decode(SpUtils.getValue(getApplicationContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                .puts("followedId", memberId)
        );
    }

    @Override
    public void onSucessGetHostSetting(LiveVideoMain anchormanInfo) {

    }

    @Override
    public void onSucessAddLive(String courseId) {

    }

    @Override
    public void getSuccessAgain(LiveRoomInfo result) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        onRequestFinish();

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    boolean isFans = false;

    @Override
    public void onSucessGetFansStatus(boolean result) {
        isFans = result;
        if (result) {
            focusMan.setBackgroundResource(R.drawable.shape_video_focus_has);
            focusMan.setText("已关注");
            focusMan.setDrawable(-1, mContext);
            focusMan.setTextColor(Color.parseColor("#ff999999"));
        } else {
            focusMan.setBackgroundResource(R.drawable.shape_video_focus);
            focusMan.setText("关注");
            focusMan.setTextColor(Color.parseColor("#ffffffff"));
            focusMan.setDrawable(R.drawable.add_white, mContext);

        }
    }

    @Override
    public void onSucessLikeMan() {
        getData();
    }

    @Override
    public void onSuccessGetMemberId(AnchormanInfo result) {

    }

    @Override
    public void onSucessGetFansList(List<LiveFans> fansList, PageInfoEarly pageInfoEarly) {

    }
}
