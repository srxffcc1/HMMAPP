package com.health.tencentlive.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.tencentlive.R;
import com.health.tencentlive.contract.LiveHostMainContract;
import com.health.tencentlive.presenter.LiveHostMainPresenter;
import com.health.tencentlive.weight.LiveTestTipsDialog;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.SectionView;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.liys.doubleclicklibrary.ViewDoubleHelper;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

@Route(path = TencentLiveRoutes.LiveHostROOM)
public class LiveHostRoomActivity extends BaseActivity implements IsFitsSystemWindows, LiveHostMainContract.View,
        OnRefreshLoadMoreListener {
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.CornerImageView hostIcon;
    private android.widget.TextView hostName;
    private android.widget.TextView hostFansCount;
    private android.widget.LinearLayout hostLeftRight;
    private androidx.constraintlayout.widget.ConstraintLayout hostLeftLL;
    private android.widget.TextView hostLeftTitle;
    private android.widget.TextView hostLeftTitleSecond;
    private androidx.constraintlayout.widget.ConstraintLayout hostRightLL;
    private android.widget.TextView hostRightTitle;
    private android.widget.TextView createLive;
    private android.widget.TextView hostRightTitleSecond;
    private com.healthy.library.widget.SectionView userMangerLL;
    private com.healthy.library.widget.SectionView userDataLL;
    private com.healthy.library.widget.SectionView testLiveLL;
    private com.healthy.library.widget.SectionView netTestLL;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout refreshLayout;
    LiveHostMainPresenter liveHostMainPresenter;
    private LiveVideoMain liveVideoMain;
    private LiveRoomInfo liveRoomInfo;
    private String anchormanId = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_hosteroom;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        liveHostMainPresenter = new LiveHostMainPresenter(mContext, this);
        hostLeftLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveHostLiveList)
                        .withString("anchormanId", SpUtils.getValue(mContext, SpKey.LIVEHOSTID))
                        .withInt("currentIndex", 1)
                        .navigation();
            }
        });
        hostRightLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveHostLiveList)
                        .withString("anchormanId", SpUtils.getValue(mContext, SpKey.LIVEHOSTID))
                        .withInt("currentIndex", 0)
                        .navigation();
            }
        });
        userMangerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveUserManager)
                        .withString("anchormanId", anchormanId)
                        .withString("type", "1")
                        .navigation();
            }
        });
        ViewDoubleHelper.hookView(createLive, 1000);
        createLive.setVisibility(View.VISIBLE);
        createLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liveRoomInfo != null && new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)).equals(liveRoomInfo.liveAnchorman.memberId)) {
                    isAgree("提示","亲爱的主播，您有一场正在直播中的直播，是否接着继续直播？");
                }else {
                    createLive.setVisibility(View.GONE);
                    if (liveVideoMain != null) {
                        ARouter.getInstance()
                                .build(TencentLiveRoutes.LIVE_CREATE)
                                .withString("anchormanId", anchormanId)
                                .withString("isDebug", "0")
                                .navigation();
//                    finish();
                        createLive.setVisibility(View.VISIBLE);
                    } else {
                        createLive.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, "当前没有直播配置", Toast.LENGTH_SHORT).show();
                        liveHostMainPresenter.getHostSetting(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", SpUtils.getValue(mContext, SpKey.LIVEHOSTID)));
                    }
                }


            }
        });
        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.setEnableLoadMore(false);
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.LIVEHOSTID))) {
            liveHostMainPresenter.getHost(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", SpUtils.getValue(mContext, SpKey.LIVEHOSTID)));
            liveHostMainPresenter.getHostSetting(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", SpUtils.getValue(mContext, SpKey.LIVEHOSTID)));
        }

    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        hostIcon = (CornerImageView) findViewById(R.id.hostIcon);
        hostName = (TextView) findViewById(R.id.hostName);
        hostFansCount = (TextView) findViewById(R.id.hostFansCount);
        hostLeftRight = (LinearLayout) findViewById(R.id.hostLeftRight);
        hostLeftLL = (ConstraintLayout) findViewById(R.id.hostLeftLL);
        hostLeftTitle = (TextView) findViewById(R.id.hostLeftTitle);
        hostLeftTitleSecond = (TextView) findViewById(R.id.hostLeftTitleSecond);
        createLive = (TextView) findViewById(R.id.createLive);
        hostRightLL = (ConstraintLayout) findViewById(R.id.hostRightLL);
        hostRightTitle = (TextView) findViewById(R.id.hostRightTitle);
        hostRightTitleSecond = (TextView) findViewById(R.id.hostRightTitleSecond);
        userMangerLL = (SectionView) findViewById(R.id.userMangerLL);
        userDataLL = (SectionView) findViewById(R.id.userDataLL);
        testLiveLL = (SectionView) findViewById(R.id.testLiveLL);
        netTestLL = (SectionView) findViewById(R.id.netTestLL);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        userDataLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LIVEDATACHART)
                        .withString("anchormanId", anchormanId)
                        .navigation();
            }
        });
        netTestLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LIVESPEEDTEST)
                        .navigation();
            }
        });
        testLiveLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liveVideoMain != null) {
                    showDialog();
                } else {
                    Toast.makeText(mContext, "当前没有直播配置", Toast.LENGTH_SHORT).show();
                    liveHostMainPresenter.getHostSetting(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", SpUtils.getValue(mContext, SpKey.LIVEHOSTID)));
                }

            }
        });
    }

    private void showDialog() {
        LiveTestTipsDialog liveTestTipsDialog = LiveTestTipsDialog.newInstance();
        liveTestTipsDialog.setClickListener(new LiveTestTipsDialog.OnClickListener() {
            @Override
            public void onClick() {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LIVE_CREATE)
                        .withString("anchormanId", anchormanId)
                        .withString("isDebug", "1")
                        .navigation();
            }
        });
        liveTestTipsDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onSucessGetHost(AnchormanInfo anchormanInfo) {
        if (anchormanInfo != null) {
            hostName.setText(anchormanInfo.memberName);
            hostFansCount.setText(anchormanInfo.fansCount + "粉丝");

            anchormanId = anchormanInfo.id;
            liveHostMainPresenter.getAnchormanLiveing(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId));
            hostFansCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(TencentLiveRoutes.LiveUserManager)
                            .withString("anchormanId", anchormanId)
                            .withString("type", "1")
                            .navigation();
                }
            });
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .asBitmap()
                    .load(anchormanInfo.avatarUrl)
                    .placeholder(R.drawable.img_avatar_default)
                    .error(R.drawable.img_avatar_default)

                    .into(hostIcon);
        }
    }

    @Override
    public void onSucessGetHostSetting(LiveVideoMain liveVideoMain) {
        this.liveVideoMain = liveVideoMain;

    }

    @Override
    public void onSucessAddLive(String courseId) {
        if (!TextUtils.isEmpty(courseId) && liveVideoMain != null) {
//            Toast.makeText(mContext,"当前的直播Id:"+courseId,Toast.LENGTH_SHORT).show();
            ARouter.getInstance()
                    .build(TencentLiveRoutes.LIVE_CREATE)
                    .withString("anchormanId", anchormanId)
                    .navigation();
        } else {
            Toast.makeText(mContext, "直播创建不成功", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getSuccessAgain(LiveRoomInfo result) {
        if (result != null) {
            this.liveRoomInfo = result;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

//    @Override
//    public void onSucessGetLiveList(List<LiveVideoMain> liveVideoMains, PageInfoEarly pageInfoEarly) {
//        this.liveVideoMain = liveVideoMains == null || liveVideoMains.size() == 0 ? null : liveVideoMains.get(0);
//    }

    private void isAgree(String title, String msg) {
        StyledDialog.init(this);
        StyledDialog.buildIosAlert(title, "\n" + msg, new MyDialogListener() {
            @Override
            public void onFirst() {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LIVE_PUSH)
                        .withString("pushAddress", liveRoomInfo.pushAddress)
                        .withString("groupId", liveRoomInfo.groupId)
                        .withString("courseId", liveRoomInfo.id)
                        .withString("anchormanId", liveRoomInfo.anchormanId)
                        .withObject("activityIdList", liveRoomInfo.activityIdList)
                        .navigation();
            }

            @Override
            public void onThird() {
                super.onThird();
            }

            @Override
            public void onSecond() {
                createLive.setVisibility(View.GONE);
                if (liveVideoMain != null) {
                    ARouter.getInstance()
                            .build(TencentLiveRoutes.LIVE_CREATE)
                            .withString("anchormanId", anchormanId)
                            .withString("isDebug", "0")
                            .navigation();
//                    finish();
                    createLive.setVisibility(View.VISIBLE);
                } else {
                    createLive.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, "当前没有直播配置", Toast.LENGTH_SHORT).show();
                    liveHostMainPresenter.getHostSetting(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", SpUtils.getValue(mContext, SpKey.LIVEHOSTID)));
                }

            }
        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("继续直播", "不了").show();
    }
}
