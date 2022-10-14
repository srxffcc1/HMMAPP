package com.health.tencentlive.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.health.tencentlive.R;
import com.health.tencentlive.contract.LiveMainBodyContract;
import com.health.tencentlive.contract.LiveMainBodyDetailContract;
import com.health.tencentlive.contract.LiveMainBodyDetailSubContract;
import com.health.tencentlive.presenter.LiveMainBodyDetailPresenter;
import com.health.tencentlive.presenter.LiveMainBodyDetailSubPresenter;
import com.health.tencentlive.presenter.LiveMainBodyPresenter;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.LivePassWordDialog;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.constant.SpKey;
import com.healthy.library.message.LiveHistoryInfo;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.model.LiveVideoSub;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.routes.TencentLiveRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LiveNoticeFragment extends BaseFragment implements LiveMainBodyContract.View, LiveMainBodyDetailContract.View, LiveMainBodyDetailSubContract.View {

    private ConstraintLayout pastHostLive;
    private ConstraintLayout pastHostMain;
    private CornerImageView hostIcon;
    private TextView hostName;
    private TextView hostDes;
    private LinearLayout layoutBottom;
    private LinearLayout pointLL;
    private ImageTextView pointName;
    private TextView goOrder;
    private LiveMainBodyPresenter liveMainBodyPresenter;
    private LiveMainBodyDetailPresenter liveMainBodyDetailPresenter;
    private LiveVideoMain liveVideoMain;
    private CornerImageView noticeImg;
    private TextView noticeTime;
    private TextView noticeIntroduce;
    private TextView startTime;
    private ImageView live_notice_share_icon;
    CountDownTimer countDownTimer;
    private LinearLayout noMsgCon;
    private NestedScrollView nestScroll;
    LiveMainBodyDetailSubPresenter liveMainBodyDetailSubPresenter;


    String courseId;//直播ID

    String merchantId;//商户id

    String shopId;//门店id

    String liveShareType;//可能分享类型

    String fromMemberId;

    String referral_code;

    String shareLiveGoodsId;//可能分享商品了


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_notice_detail;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void findViews() {
        initView();
        courseId = get("courseId");
        merchantId = get("merchantId");
        shopId = get("shopId");
        liveShareType = get("liveShareType");
        fromMemberId = get("fromMemberId");
        referral_code = get("referral_code");
        shareLiveGoodsId=get("shareLiveGoodsId");
        liveMainBodyDetailSubPresenter = new LiveMainBodyDetailSubPresenter(mContext, this);
        liveMainBodyPresenter = new LiveMainBodyPresenter(mContext, this);
        liveMainBodyDetailPresenter = new LiveMainBodyDetailPresenter(mContext, this);
        pastHostMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liveVideoMain == null) {
                    return;
                }
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LiveHostMain)
                        .withString("anchormanId", liveVideoMain.anchormanId)
                        .navigation();
            }
        });
        pastHostLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liveVideoMain == null) {
                    return;
                }
                if (get("isActivity") != null && liveVideoMain != null) {
                    ARouter.getInstance()
                            .build(TencentLiveRoutes.LiveHostMain)
                            .withString("anchormanId", liveVideoMain.anchormanId != null ? liveVideoMain.anchormanId : liveVideoMain.liveAnchorman.id)
                            .navigation();
                } else {

                    EventBus.getDefault().post(new LiveHistoryInfo());
                }

            }
        });
        goOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liveVideoMain == null) {
                    return;
                }
                if ("预约".equals(goOrder.getText().toString())) {
                    if (liveVideoMain != null) {
                        liveMainBodyDetailSubPresenter.addSub(new SimpleHashMapBuilder<String, Object>()
                                .puts("opType", "1")
                                .puts("courseId", liveVideoMain.id));
                    }
                } else if ("已预约".equals(goOrder.getText().toString())) {
                    Toast.makeText(mContext, "已经预约", Toast.LENGTH_SHORT).show();
                } else {
                    goOrder.setVisibility(View.GONE);
                    if (liveVideoMain != null) {
                        if(liveVideoMain.isBringGoods==0||liveVideoMain.activityIdList==null||liveVideoMain.activityIdList.size()==0){//非带货直播让直播确认下是否继续开播
                            StyledDialog.init(mContext);
                            StyledDialog.buildIosAlert("", "当前直播为非带货或未关联直播活动是否确认开播?", new MyDialogListener() {
                                @Override
                                public void onFirst() {
                                    liveMainBodyDetailPresenter.startLive(new SimpleHashMapBuilder<String, Object>().puts("courseId", liveVideoMain.id));
                                }

                                @Override
                                public void onThird() {
                                    super.onThird();
                                }

                                @Override
                                public void onSecond() {
                                    goOrder.setVisibility(View.VISIBLE);
                                }
                            }).setCancelable(true,true).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark,0).setBtnText("确定", "取消").show();
                        }else {
                            liveMainBodyDetailPresenter.startLive(new SimpleHashMapBuilder<String, Object>().puts("courseId", liveVideoMain.id));
                        }
                    }
                }

            }
        });
        noticeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    protected void onLazyData() {
        super.onLazyData();
        getData();
    }

    public static LiveNoticeFragment newInstance(Map<String, Object> maporg) {
        LiveNoticeFragment fragment = new LiveNoticeFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void changeFragmentShow() {
        super.changeFragmentShow();
        if (isfragmenthow) {
        }
    }

    @Override
    public void getData() {
        super.getData();
        if (get("courseId") != null) {
            liveMainBodyDetailPresenter.getLive(new SimpleHashMapBuilder<String, Object>().puts("courseId", get("courseId")));
        } else {
            liveMainBodyPresenter.getLiveList(new SimpleHashMapBuilder<String, Object>()
                            .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC))
                            .puts("anchormanId", get("anchormanId"))
//                .puts("roomId",get("roomId"))
//                .puts("courseTitle",get("courseTitle"))
//                            .puts("type",get("type"))
//                            .puts("category",get("category"))
                            .puts("statusList", "1".split(","))
                            .puts("isDelete", "0")
                            .puts("sortType", get("sortType"))
                            .puts("page", new SimpleHashMapBuilder<String, Object>()
                                    .puts("pageSize", 10 + "")
                                    .puts("pageNum", 1 + "")
                            )
            );
        }

    }

    private void initView() {
        pastHostLive = (ConstraintLayout) findViewById(R.id.pastHostLive);
        pastHostMain = (ConstraintLayout) findViewById(R.id.pastHostMain);
        hostIcon = (CornerImageView) findViewById(R.id.hostIcon);
        hostName = (TextView) findViewById(R.id.hostName);
        hostDes = (TextView) findViewById(R.id.hostDes);
        layoutBottom = (LinearLayout) findViewById(R.id.layout_bottom);
        pointLL = (LinearLayout) findViewById(R.id.pointLL);
        pointName = (ImageTextView) findViewById(R.id.pointName);
        goOrder = (TextView) findViewById(R.id.goOrder);
        noticeImg = (CornerImageView) findViewById(R.id.noticeImg);
        noticeTime = (TextView) findViewById(R.id.noticeTime);
        noticeIntroduce = (TextView) findViewById(R.id.noticeIntroduce);
        startTime = (TextView) findViewById(R.id.startTime);
        noMsgCon = (LinearLayout) findViewById(R.id.noMsgCon);
        nestScroll = (NestedScrollView) findViewById(R.id.nestScroll);
        live_notice_share_icon = (ImageView) findViewById(R.id.live_notice_share_icon);
        live_notice_share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liveVideoMain != null) {
                    SeckShareDialog seckShareDialog = SeckShareDialog.newInstance();
                    seckShareDialog.setActivityDialog(8, 7, liveVideoMain);
                    seckShareDialog.setGroupId(null);
                    seckShareDialog.setExtraMap(new SimpleHashMapBuilder<String, String>()
                            .puts("scheme", "LiveStream")
                            .puts("courseId", courseId)
                            .puts("fromMemberId", new String(Base64.decode(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))//助力分享记得填
                            .puts("liveShareType", "zbzl")//普通分享 zbzl助力分享记得改
                            .puts("merchantId", merchantId)
                            .puts("shopId", shopId)
                    );
                    seckShareDialog.show(getChildFragmentManager(),"");
                }
            }
        });
    }

    @Override
    public void onSucessGetLiveList(List<LiveVideoMain> liveVideoMains, PageInfoEarly pageInfoEarly) {
        if(get("anchormanId")!=null){//主播通过主播入口进来
            if(liveVideoMains != null){
                for (int i = 0; i < liveVideoMains.size(); i++) {
                    if(liveVideoMains.get(i).status==1&&liveVideoMains.get(i).anchormanId.equals(get("anchormanId"))){//是预告且是主播进来
                        this.liveVideoMain=liveVideoMains.get(i);
                        break;
                    }
                }
            }
        }else {
            this.liveVideoMain = liveVideoMains == null || liveVideoMains.size() == 0 ? null : liveVideoMains.get(0);
        }
        buildView();
    }

    private void buildView() {
        pointLL.setVisibility(View.GONE);
        noMsgCon.setVisibility(View.GONE);
        nestScroll.setVisibility(View.VISIBLE);


        if (liveVideoMain != null) {
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .load(liveVideoMain.picUrl)

                    .placeholder(R.drawable.img_1_1_default)
                    .error(R.drawable.img_1_1_default)
                    .into(noticeImg);
            noticeIntroduce.setText(liveVideoMain.courseIntroduce);
            checkTimeOut(liveVideoMain, noticeTime);
            hostName.setText(liveVideoMain.liveAnchorman.memberName);
            hostDes.setText(liveVideoMain.liveRoom.roomIntroduce);
            if (liveVideoMain.status == 2) {//预告变成正在直播了 则自动跳转到直播页面
                if (liveVideoMain.type == 2&&!SpUtils.getValue(LibApplication.getAppContext(),liveVideoMain.id+"Pass",false)) {//判断是不是私密直播
                    LivePassWordDialog.newInstance()
                            .setNeedCancelable(false)
                            .setLivePassCancelListener(new LivePassWordDialog.LivePassCancelListener() {
                                @Override
                                public void onClose() {
//                                    mActivity.finish();
                                }
                            })
                            .setLivePassWordListener(new LivePassWordDialog.LivePassWordListener() {
                                @Override
                                public void onPassSucess() {
                                    ((BaseActivity) mContext).stopOnlineVideoFloat();
                                    ARouter.getInstance()
                                            .build(TencentLiveRoutes.LIVE_LOOK)
                                            .withString("courseId", liveVideoMain.id)
                                            .withString("merchantId", merchantId)
                                            .withString("shopId", shopId)
                                            .withString("liveShareType", liveShareType)
                                            .withString("fromMemberId", fromMemberId)
                                            .withString("shareLiveGoodsId",shareLiveGoodsId)
                                            .withString("referral_code", referral_code)
                                            .navigation();
                                    mActivity.finish();
                                }
                            })
                            .setCourseId(liveVideoMain.id)
                            .show(getChildFragmentManager(), "私密支付");
                } else {
                    ((BaseActivity) mContext).stopOnlineVideoFloat();
                    ARouter.getInstance()
                            .build(TencentLiveRoutes.LIVE_LOOK)
                            .withString("courseId", liveVideoMain.id)
                            .withString("merchantId", merchantId)
                            .withString("shopId", shopId)
                            .withString("liveShareType", liveShareType)
                            .withString("fromMemberId", fromMemberId)
                            .withString("shareLiveGoodsId",shareLiveGoodsId)
                            .withString("referral_code", referral_code)
                            .navigation();
                    mActivity.finish();
                }
            } else if (liveVideoMain.status == 1) {
                if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.LIVEHOSTID)) && liveVideoMain.anchormanId.equals(SpUtils.getValue(mContext, SpKey.LIVEHOSTID))) {
                    layoutBottom.setVisibility(View.VISIBLE);
                    if (liveVideoMain.subscribeCount > 0) {
                        pointLL.setVisibility(View.GONE);
                        pointName.setText("已经预约" + liveVideoMain.subscribeCount + "人");
                    }
                    goOrder.setText("开启直播");
                } else {
                    layoutBottom.setVisibility(View.VISIBLE);
                    if (liveVideoMain.subscribeCount > 0) {
                        pointLL.setVisibility(View.VISIBLE);
                        pointName.setText("已经预约" + liveVideoMain.subscribeCount + "人");
                    }
                    liveMainBodyDetailSubPresenter.getSubDetail(new SimpleHashMapBuilder<String, Object>().puts("courseId", liveVideoMain.id));
                }
                try {
                    if (liveVideoMain.beginTime != null & !TextUtils.isEmpty(liveVideoMain.beginTime)) {
                        startTime.setVisibility(View.VISIBLE);
                        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                        String result = format.format(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(liveVideoMain.beginTime));
                        startTime.setText(result + " 开播");
                    } else {
                        startTime.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                com.healthy.library.businessutil.GlideCopy.with(mContext)
                        .load(liveVideoMain.liveAnchorman.avatarUrl)

                        .placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.img_1_1_default)
                        .into(hostIcon);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 5000);
            } else {
                if (liveVideoMain.type == 2&&!SpUtils.getValue(LibApplication.getAppContext(),liveVideoMain.id+"Pass",false)) {//判断是不是私密直播
                    LivePassWordDialog.newInstance()
                            .setNeedCancelable(false)
                            .setLivePassCancelListener(new LivePassWordDialog.LivePassCancelListener() {
                                @Override
                                public void onClose() {
//                                    mActivity.finish();
                                }
                            })
                            .setLivePassWordListener(new LivePassWordDialog.LivePassWordListener() {
                                @Override
                                public void onPassSucess() {
                                    ((BaseActivity) mContext).stopOnlineVideoFloat();
                                    ARouter.getInstance()
                                            .build(TencentLiveRoutes.LIVE_LOOK)
                                            .withString("courseId", liveVideoMain.id)
                                            .withString("merchantId", merchantId)
                                            .withString("shopId", shopId)
                                            .withString("liveShareType", liveShareType)
                                            .withString("fromMemberId", fromMemberId)
                                            .withString("shareLiveGoodsId",shareLiveGoodsId)
                                            .withString("referral_code", referral_code)
                                            .navigation();
                                    mActivity.finish();
                                }
                            })
                            .setCourseId(liveVideoMain.id)
                            .show(getChildFragmentManager(), "私密支付");
                } else {
                    ((BaseActivity) mContext).stopOnlineVideoFloat();
                    ARouter.getInstance()
                            .build(TencentLiveRoutes.LIVE_LOOK)
                            .withString("courseId", liveVideoMain.id)
                            .withString("merchantId", merchantId)
                            .withString("shopId", shopId)
                            .withString("liveShareType", liveShareType)
                            .withString("fromMemberId", fromMemberId)
                            .withString("shareLiveGoodsId",shareLiveGoodsId)
                            .withString("referral_code", referral_code)
                            .navigation();
                    mActivity.finish();
                }
            }

        } else {
            noMsgCon.setVisibility(View.VISIBLE);
            nestScroll.setVisibility(View.GONE);
        }

    }

    private void checkTimeOut(LiveVideoMain url, final TextView kickDay) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        if (url.beginTime == null) {
            return;
        }
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long desorg = startTime.getTime();
        long timer = startTime.getTime();
        timer = timer - System.currentTimeMillis();
        if (timer > 0) {
            final long finalTimer = timer;
            final long finalDesorg = desorg;
            countDownTimer = new CountDownTimer(finalTimer, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] array = DateUtils.getDistanceTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), DateUtils.formatLongAll(finalDesorg + ""));
                    kickDay.setText(SpanUtils.getBuilder(mContext, "")
                            .append(array[0]).setBold().setForegroundColor(Color.parseColor("#333333"))
                            .append("天").setForegroundColor(Color.parseColor("#999999"))
                            .append(array[1]).setBold().setForegroundColor(Color.parseColor("#333333"))
                            .append("时").setForegroundColor(Color.parseColor("#999999"))
                            .append(array[2]).setBold().setForegroundColor(Color.parseColor("#333333"))
                            .append("分").setForegroundColor(Color.parseColor("#999999"))
                            .append(array[3]).setBold().setForegroundColor(Color.parseColor("#333333"))
                            .append("秒").setForegroundColor(Color.parseColor("#999999"))
                            .create());
                }

                public void onFinish() {
                    getData();
                }
            }.start();
        } else {
            kickDay.setText("已经开播可点击图片进入");
//            kickDay.setText(SpanUtils.getBuilder(mContext, "")
//                    .append("00").setBold().setForegroundColor(Color.parseColor("#333333"))
//                    .append("天").setForegroundColor(Color.parseColor("#999999"))
//                    .append("00").setBold().setForegroundColor(Color.parseColor("#333333"))
//                    .append("时").setForegroundColor(Color.parseColor("#999999"))
//                    .append("00").setBold().setForegroundColor(Color.parseColor("#333333"))
//                    .append("分").setForegroundColor(Color.parseColor("#999999"))
//                    .append("00").setBold().setForegroundColor(Color.parseColor("#333333"))
//                    .append("秒").setForegroundColor(Color.parseColor("#999999"))
//                    .create());
        }


    }

    @Override
    public void onSucessGetLive(LiveVideoMain liveVideoMain) {
        this.liveVideoMain = liveVideoMain;
        buildView();

    }

    @Override
    public void onStartLiveSuccess(String pushAddress, String groupId) {
        if (groupId == null || pushAddress == null) {
            showToast("获取推流地址失败！");
            goOrder.setVisibility(View.VISIBLE);
        } else {
            if (liveVideoMain != null) {
                ARouter.getInstance()
                        .build(TencentLiveRoutes.LIVE_PUSH)
                        .withString("pushAddress", pushAddress)
                        .withString("groupId", groupId)
                        .withString("courseId", liveVideoMain.id)
                        .withString("anchormanId", liveVideoMain.anchormanId)
                        .withObject("activityIdList", liveVideoMain.activityIdList)
                        .navigation();
                mActivity.finish();
            }
        }
    }

    @Override
    public void onSucessGetSub(LiveVideoSub liveVideoSub) {
        if(liveVideoMain==null){
            return;
        }
        if (liveVideoMain.status == 1) {
            if (liveVideoSub != null) {
                if (liveVideoSub.subscribeCount > 0) {
                    goOrder.setText("已预约");
                    pointLL.setVisibility(View.VISIBLE);
                    pointName.setText("已经预约" + liveVideoSub.subscribeCount + "人");
                }
            } else {
                if (liveVideoMain.subscribeCount > 0) {
                    pointLL.setVisibility(View.VISIBLE);
                    pointName.setText("已经预约" + liveVideoMain.subscribeCount + "人");
                }

            }
        }

    }

    @Override
    public void onSucessSub() {
        getData();
    }
}
