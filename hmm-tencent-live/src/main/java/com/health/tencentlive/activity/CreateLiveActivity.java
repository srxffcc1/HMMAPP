package com.health.tencentlive.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.health.tencentlive.R;
import com.health.tencentlive.contract.CreateLiveContract;
import com.healthy.library.model.AnchormanInfo;
import com.health.tencentlive.presenter.CreateLivePresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.Date;

@Route(path = TencentLiveRoutes.LIVE_CREATE)
public class CreateLiveActivity extends BaseActivity implements IsFitsSystemWindows,
        CreateLiveContract.View, OnRefreshListener {

    @Autowired
    String anchormanId;//主播Id
    @Autowired
    String isDebug;//0不是调试  1是调试

    private TopBar topBar;
    private CornerImageView liveBackImg;
    private TextView liveTitle;
    private TextView liveTeacher;
    private ConstraintLayout liveTypeLine;
    private TextView liveTypeTxt;
    private ConstraintLayout liveChannelLine;
    private TextView channelName;
    private TextView goodsLive;
    private TextView liveContent;
    private TextView createLiveBtn;
    private SmartRefreshLayout layout_refresh;

    private CreateLivePresenter createLivePresenter;
    private LiveVideoMain liveVideoMain;

    String backImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_live;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        createLivePresenter = new CreateLivePresenter(this, this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        createLivePresenter.getLiveRoomConfig(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", anchormanId));
        createLivePresenter.getAnchormanInfo(new SimpleHashMapBuilder<String, Object>().puts("anchormanId", SpUtils.getValue(mContext, SpKey.LIVEHOSTID)));
    }

    @Override
    protected void findViews() {
        super.findViews();
        topBar = (TopBar) findViewById(R.id.top_bar);
        liveBackImg = (CornerImageView) findViewById(R.id.liveBackImg);
        liveTitle = (TextView) findViewById(R.id.liveTitle);
        liveTeacher = (TextView) findViewById(R.id.liveTeacher);
        liveTypeLine = (ConstraintLayout) findViewById(R.id.liveTypeLine);
        liveTypeTxt = (TextView) findViewById(R.id.liveTypeTxt);
        liveChannelLine = (ConstraintLayout) findViewById(R.id.liveChannelLine);
        channelName = (TextView) findViewById(R.id.channelName);
        goodsLive = (TextView) findViewById(R.id.goodsLive);
        liveContent = (TextView) findViewById(R.id.liveContent);
        createLiveBtn = (TextView) findViewById(R.id.createLiveBtn);
        layout_refresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layout_refresh.setOnRefreshListener(this);
        layout_refresh.setEnableLoadMore(false);
    }

    @Override
    public void getSuccessAnchormanInfo(final AnchormanInfo result) {
        if (result != null) {
            if (result.memberName != null) {
                liveTeacher.setText("主讲人：" + result.memberName);
            } else {
                liveTeacher.setText("");
            }
            createLiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (liveVideoMain != null) {
                        if (result.isForbidLive == 0) {
                            createLiveBtn.setVisibility(View.GONE);
                            showLoading();
                            createLivePresenter.startPushLive(new SimpleHashMapBuilder<String, Object>()
                                    .putObject(liveVideoMain)
                                    .puts("livePlatform", "1")
                                    .puts("beginTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                                    .puts("clientType", "1")
                                    .puts("isDebug", isDebug));
                        } else {
                            showToast("你已被禁止直播,暂时不能开播！");
                        }
                    } else {
                        showToast("未查询到直播配置");
                    }
                }
            });
        } else {
            showEmpty();
            showToast("未查询到主播信息");
        }
    }

    @Override
    public void getSuccessLiveRoomConfig(LiveVideoMain result) {
        if (result != null) {
            liveVideoMain = result;
            bulidView(result);
        } else {
            showEmpty();
            showToast("未查询到直播配置");
        }
    }

    private void bulidView(LiveVideoMain result) {
        backImgUrl = result.picUrl;
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(result.picUrl)
                .placeholder(R.drawable.img_1_1_default)
                .error(R.drawable.img_1_1_default)
                .into(liveBackImg);
        liveTitle.setText(result.courseTitle);
        if (result.type == 1) {
            liveTypeTxt.setText("公开直播");
        } else if (result.type == 2) {
            liveTypeTxt.setText("私密直播");
        } else if (result.type == 3) {
            liveTypeTxt.setText("付费直播");
        }
        if (result.category == 1) {
            channelName.setText("好物秒杀");
        } else if (result.category == 2) {
            channelName.setText("孕妇护理");
        } else if (result.category == 3) {
            channelName.setText("育儿教学");
        } else if (result.category == 4) {
            channelName.setText("憨妈课堂");
        }
        if (result.isBringGoods == 0) {
            goodsLive.setText("否");
        } else {
            goodsLive.setText("是");
        }
        liveContent.setText(result.courseIntroduce);

    }

    @Override
    public void onStartPushLiveSuccess(String pushAddress, String groupId, String id) {
        if (groupId != null && pushAddress != null && id != null) {
            if (liveVideoMain != null) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LIVE_PUSH)
                        .withString("pushAddress", pushAddress)
                        .withString("groupId", groupId)
                        .withString("courseId", id)
                        .withString("anchormanId", liveVideoMain.anchormanId)
                        .withObject("activityIdList", liveVideoMain.activityIdList)
                        .navigation();
                finish();
            }
        } else {
            showToast("获取推流地址失败！");
            createLiveBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }
}