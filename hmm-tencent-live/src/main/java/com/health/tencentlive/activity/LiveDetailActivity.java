package com.health.tencentlive.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.health.tencentlive.R;
import com.health.tencentlive.contract.LiveDetailContract;
import com.health.tencentlive.presenter.LiveDetailPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Route(path = TencentLiveRoutes.LIVEDETAIL)
public class LiveDetailActivity extends BaseActivity implements IsFitsSystemWindows, LiveDetailContract.View, OnRefreshListener {

    @Autowired
    String courseId;

    private StatusLayout layoutStatus;
    private SmartRefreshLayout layoutRefresh;
    private TopBar topBar;
    private TextView videoTitle;
    private TextView videoTime;
    private TextView videoTimeDetail;
    private ConstraintLayout dataLL;
    private TextView dataTitle;
    private TextView dataDiv;
    private LinearLayout dataLine1;
    private LinearLayout dataLine2;
    private ConstraintLayout dataVideoLL;
    private TextView dataVideoTitle;
    private TextView dataVideoDiv;
    private CornerImageView dataVideoImg;
    private TextView dataVideoName;
    private TextView startTime;
    private TextView endTime;
    private TextView lookNum;
    private TextView newFanceNum;
    private TextView lookVideo;


    private LiveDetailPresenter liveDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        liveDetailPresenter = new LiveDetailPresenter(this, this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        liveDetailPresenter.getLiveRoomInfo(new SimpleHashMapBuilder<String, Object>().puts("courseId", courseId));
        layoutRefresh.finishRefresh();
    }

    @Override
    public void onSuccessGetLiveInfo(AnchormanInfo result) {

    }

    @Override
    public void onSuccessGetLiveRoomInfo(LiveRoomInfo result) {
        if (result != null) {
            if (result.actualBeginTime != null && result.endTime != null) {
                videoTimeDetail.setText(getTime(result.actualBeginTime, result.endTime));
                startTime.setText(result.actualBeginTime.split(" ")[1]);
                endTime.setText(result.endTime.split(" ")[1]);
            }
            lookNum.setText(result.timesWatched * 17 + "");
            newFanceNum.setText(result.fansCount != null ? result.fansCount : "0" + "");
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .load(result.picUrl)
                    .placeholder(R.drawable.img_1_1_default)
                    .error(R.drawable.img_1_1_default)
                    .into(dataVideoImg);
            dataVideoName.setText(result.courseTitle + " " + result.liveAnchorman.memberName);
            if (result.status != 4) {
                lookVideo.getBackground().setAlpha(170);
                lookVideo.setText("回放生成中...");
                lookVideo.setClickable(false);
            } else {
                lookVideo.getBackground().setAlpha(255);
                lookVideo.setText("观看回放");
                lookVideo.setClickable(true);
                dataVideoLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance().build(TencentLiveRoutes.LiveNotice)
                                .withString("courseId", courseId)
                                .navigation();
                    }
                });
                lookVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance().build(TencentLiveRoutes.LiveNotice)
                                .withString("courseId", courseId)
                                .navigation();
                    }
                });
            }
        } else {
            showEmpty();
        }
    }

    private String getTime(String beginTime, String endTime) {
        //时间处理类
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long currentTime = 0;
        long createTime = 0;
        try {
            createTime = df.parse(beginTime).getTime();
            currentTime = df.parse(endTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = (currentTime - createTime) / 1000;
        if (diff > 0) {
            return FormatRunTime(diff);
        } else {
            return "0";
        }
    }

    public String FormatRunTime(long runTime) {
        if (runTime < 0) {
            return "00:00:00";
        }

        long hour = runTime / 3600;
        long minute = (runTime % 3600) / 60;
        long second = runTime % 60;

        return unitTimeFormat(hour) + ":" + unitTimeFormat(minute) + ":" +
                unitTimeFormat(second);
    }

    private String unitTimeFormat(long number) {
        return String.format("%02d", number);
    }

    @Override
    protected void findViews() {
        super.findViews();
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        topBar = (TopBar) findViewById(R.id.top_bar);
        videoTitle = (TextView) findViewById(R.id.videoTitle);
        videoTime = (TextView) findViewById(R.id.videoTime);
        videoTimeDetail = (TextView) findViewById(R.id.videoTimeDetail);
        dataLL = (ConstraintLayout) findViewById(R.id.dataLL);
        dataTitle = (TextView) findViewById(R.id.dataTitle);
        dataDiv = (TextView) findViewById(R.id.dataDiv);
        dataLine1 = (LinearLayout) findViewById(R.id.dataLine1);
        dataLine2 = (LinearLayout) findViewById(R.id.dataLine2);
        dataVideoLL = (ConstraintLayout) findViewById(R.id.dataVideoLL);
        dataVideoTitle = (TextView) findViewById(R.id.dataVideoTitle);
        dataVideoDiv = (TextView) findViewById(R.id.dataVideoDiv);
        dataVideoImg = (CornerImageView) findViewById(R.id.dataVideoImg);
        dataVideoName = (TextView) findViewById(R.id.dataVideoName);
        startTime = (TextView) findViewById(R.id.startTime);
        endTime = (TextView) findViewById(R.id.endTime);
        lookNum = (TextView) findViewById(R.id.lookNum);
        newFanceNum = (TextView) findViewById(R.id.newFanceNum);
        lookVideo = (TextView) findViewById(R.id.lookVideo);
        layoutRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

}