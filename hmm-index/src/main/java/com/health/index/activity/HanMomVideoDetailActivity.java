//package com.health.index.activity;
//
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.util.Base64;
//import android.util.DisplayMetrics;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.constraintlayout.widget.ConstraintLayout;
//
//import com.alibaba.android.arouter.facade.annotation.Autowired;
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.request.transition.Transition;
//import com.health.index.R;
//import com.health.index.contract.HanMomVideoContract;
//import com.healthy.library.model.VideoListModel;
//import com.health.index.presenter.HanMomVideoPresenter;
//import com.healthy.library.base.BaseActivity;
//import com.healthy.library.builder.SimpleHashMapBuilder;
//import com.healthy.library.business.VideoCommentDialog;
//import com.healthy.library.constant.Functions;
//import com.healthy.library.constant.SpKey;
//import com.healthy.library.constant.UrlKeys;
//import com.healthy.library.interfaces.IsFitsSystemWindows;
//import com.healthy.library.interfaces.IsNeedShare;
//import com.healthy.library.message.CommentEvent;
//import com.healthy.library.model.PageInfoEarly;
//import com.healthy.library.model.VideoCategory;
//import com.healthy.library.routes.IndexRoutes;
//import com.healthy.library.utils.DrawableUtils;
//import com.healthy.library.utils.SpUtils;
//import com.tencent.rtmp.ITXVodPlayListener;
//import com.tencent.rtmp.TXLiveConstants;
//import com.tencent.rtmp.TXVodPlayer;
//import com.tencent.rtmp.ui.TXCloudVideoView;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.List;
//
////@Route(path = IndexRoutes.INDEX_HANMOMVIDEODETAIL)
//public class HanMomVideoDetailActivity extends BaseActivity implements IsFitsSystemWindows, HanMomVideoContract.View, SeekBar.OnSeekBarChangeListener, IsNeedShare {
//
//
//    @Autowired
//    String id;
//
//    private ConstraintLayout activityView;
//    private ConstraintLayout topView;
//    private View viewHeaderBg;
//    private ImageView imgBack;
//    private ImageView shareImg;
//    private TXCloudVideoView videoView;
//    private TextView videoTitle;
//    private TextView videoContent;
//    private ConstraintLayout bottomPlayerLayout;
//    private ImageView livePlayer;
//    private TextView startTime;
//    private SeekBar seekBar;
//    private TextView endTime;
//    private ConstraintLayout bottomCommentLayout;
//    private LinearLayout commentLayout;
//    private LinearLayout commentNumLayout;
//    private TextView commentNum;
//    private LinearLayout clickLayout;
//    private TextView clickNum;
//    private ImageView livePlayerImg;
//    private ImageView praiseImg;
//    private VideoCommentDialog videoCommentDialog;
//    private HanMomVideoPresenter hanMomVideoPresenter;
//
//    private TXVodPlayer mVodPlayer;
//    private int totalSecond = 0;//???????????????  ??????
//    private int currentSecond = 0;//??????????????????  ??????
//    private boolean isPraise = false;//????????????
//    private boolean isSetSeekBar = false;
//
//    String surl;
//    String sdes;
//    String stitle;
//    Bitmap sBitmap;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_han_mom_video_detail;
//    }
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//        ARouter.getInstance().inject(this);
//        EventBus.getDefault().register(this);
//        hanMomVideoPresenter = new HanMomVideoPresenter(this, this);
//        livePlayer.setVisibility(View.VISIBLE);
//        //?????? player ??????
//        mVodPlayer = new TXVodPlayer(this);
//        //?????? player ??????????????? view
//        mVodPlayer.setPlayerView(videoView);
//        mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
//        getData();
//    }
//
//    @Override
//    public void getData() {
//        super.getData();
//        hanMomVideoPresenter.getVideoDetail(new SimpleHashMapBuilder<String, Object>().puts("id", id)
//                .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))), 1);
//        hanMomVideoPresenter.getUserInfo(new SimpleHashMapBuilder<String, Object>());
//    }
//
//    @Override
//    public void onGetVideoDetailSuccess(final VideoListModel result, int type) {
//        if (result != null) {
//            showContent();
//            if (type == 1) {
//                buildDes(result);
//                hanMomVideoPresenter.addPlayVolume(new SimpleHashMapBuilder<String, Object>().puts("id", result.id));
//                if (result.getVideoUrl() == null) {
//                    showToast("??????????????????????????????~");
//                } else {
//                    mVodPlayer.startPlay(result.getVideoUrl());
//                    mVodPlayer.setVodListener(new ITXVodPlayListener() {
//                        @Override
//                        public void onPlayEvent(TXVodPlayer txVodPlayer, int i, Bundle bundle) {
//                            totalSecond = bundle.getInt(TXLiveConstants.EVT_PLAY_DURATION);//???????????????  ??????
//                            currentSecond = bundle.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);//??????????????????  ??????
//                            //LogUtils.e("??????????????????" + totalSecond);
//                            //LogUtils.e("??????????????????" + currentSecond);
//                            if (totalSecond > 0 && currentSecond > 0) {
//                                setProgress();
//                            }
//                            if (i == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
//                                int width = bundle.getInt("EVT_PARAM1");
//                                int height = bundle.getInt("EVT_PARAM2");
//                                setInsideModel(width, height);
//                            }
//                        }
//
//                        @Override
//                        public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {
//
//                        }
//                    });
//                }
//                videoTitle.setText(result.videoName);
//                videoContent.setText(result.videoRemark);
//                commentNum.setText(result.discussCount + "");
//                clickNum.setText(result.praiseCount + "");
//                if (result.praise) {
//                    isPraise = true;
//                    praiseImg.setImageResource(R.drawable.hanmom_videodetial_clicksuccess_icon);
//                } else {
//                    isPraise = false;
//                    praiseImg.setImageResource(R.drawable.hanmom_videodetial_click_icon);
//                }
//                clickLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (isPraise) {
//                            hanMomVideoPresenter.addPraise(new SimpleHashMapBuilder<String, Object>()
//                                            .puts(Functions.FUNCTION, "8098")
//                                            .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
//                                            .puts("videoId", result.id),
//                                    2);
//                        } else {
//                            hanMomVideoPresenter.addPraise(new SimpleHashMapBuilder<String, Object>()
//                                            .puts(Functions.FUNCTION, "8097")
//                                            .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
//                                            .puts("videoId", result.id),
//                                    1);
//                        }
//                    }
//                });
//                commentLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (videoCommentDialog == null) {
//                            videoCommentDialog = videoCommentDialog.newInstance();
//                        }
//                        videoCommentDialog.setId(result.id);
//                        videoCommentDialog.show(getSupportFragmentManager(), "");
//                    }
//                });
//                commentNumLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (videoCommentDialog == null) {
//                            videoCommentDialog = videoCommentDialog.newInstance();
//                        }
//                        videoCommentDialog.setId(result.id);
//                        videoCommentDialog.show(getSupportFragmentManager(), "");
//                    }
//                });
//                Glide.with(mContext).load(result.photo)
//                        .placeholder(R.drawable.img_1_1_default2)
//                        .error(R.drawable.img_1_1_default)
//
//                        .into(new SimpleTarget<Drawable>() {
//
//                            @Override
//                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                sBitmap = DrawableUtils.drawableToBitmap(resource);
//                            }
//                        });
//            } else {
//                commentNum.setText(result.discussCount + "");
//                clickNum.setText(result.praiseCount + "");
//            }
//        } else {
//            livePlayer.setVisibility(View.GONE);
//            showEmpty();
//            findViewById(R.id.layout_status).setBackgroundColor(Color.parseColor("#FFFFFF"));
//        }
//    }
//
//    private void setInsideModel(int width, int height) {
//        // ??????????????????
//        if (height > 0) {
//            double size = (double) width / height;
//            DisplayMetrics outMetrics = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
//            double mPixelsSize = (double) outMetrics.widthPixels / outMetrics.heightPixels;
//            if (size > 1.0 || size > mPixelsSize) {
//                mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
//            } else {
//                mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
//            }
//        } else {
//            mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
//        }
//    }
//
//    @Override
//    public void onAddPraiseSuccess(String result, int type) {
//        if (type == 1) {
//            showToast("????????????");
//            isPraise = true;
//            praiseImg.setImageResource(R.drawable.hanmom_videodetial_clicksuccess_icon);
//        } else {
//            showToast("???????????????");
//            isPraise = false;
//            praiseImg.setImageResource(R.drawable.hanmom_videodetial_click_icon);
//        }
//        if (result != null && !TextUtils.isEmpty(result)) {
//            clickNum.setText(result + "");
//        }
//    }
//
//    @Override
//    protected void findViews() {
//        super.findViews();
//        activityView = (ConstraintLayout) findViewById(R.id.activityView);
//        topView = (ConstraintLayout) findViewById(R.id.topView);
//        viewHeaderBg = (View) findViewById(R.id.view_header_bg);
//        imgBack = (ImageView) findViewById(R.id.img_back);
//        shareImg = (ImageView) findViewById(R.id.shareImg);
//        videoView = (TXCloudVideoView) findViewById(R.id.video_view);
//        videoTitle = (TextView) findViewById(R.id.videoTitle);
//        videoContent = (TextView) findViewById(R.id.videoContent);
//        bottomPlayerLayout = (ConstraintLayout) findViewById(R.id.bottomPlayerLayout);
//        livePlayer = (ImageView) findViewById(R.id.livePlayer);
//        startTime = (TextView) findViewById(R.id.startTime);
//        seekBar = (SeekBar) findViewById(R.id.seekBar);
//        endTime = (TextView) findViewById(R.id.endTime);
//        bottomCommentLayout = (ConstraintLayout) findViewById(R.id.bottomCommentLayout);
//        commentLayout = (LinearLayout) findViewById(R.id.commentLayout);
//        commentNumLayout = (LinearLayout) findViewById(R.id.commentNumLayout);
//        commentNum = (TextView) findViewById(R.id.commentNum);
//        clickLayout = (LinearLayout) findViewById(R.id.clickLayout);
//        clickNum = (TextView) findViewById(R.id.clickNum);
//        livePlayerImg = (ImageView) findViewById(R.id.livePlayerImg);
//        praiseImg = (ImageView) findViewById(R.id.praiseImg);
//        imgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        seekBar.setOnSeekBarChangeListener(this);
//        livePlayer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mVodPlayer.isPlaying()) {//????????????
//                    livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_pause));
//                    livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
//                    mVodPlayer.pause();
//                } else {
//                    livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_plear));
//                    livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_play));
//                    mVodPlayer.resume();
//                }
//            }
//        });
//        activityView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mVodPlayer.isPlaying()) {//????????????
//                    livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_play));
//                    livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_plear));
//                } else {
//                    livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
//                    livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_pause));
//                }
//                livePlayerImg.setVisibility(View.VISIBLE);
//                if (livePlayerImg.getVisibility() == View.VISIBLE) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            livePlayerImg.setVisibility(View.GONE);
//                        }
//                    }, 4000);
//
//                }
//            }
//        });
//        livePlayerImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mVodPlayer != null) {
//                    if (mVodPlayer.isPlaying()) {//????????????
//                        livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
//                        livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_pause));
//                        mVodPlayer.pause();
//                    } else {
//                        livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_play));
//                        livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_plear));
//                        mVodPlayer.resume();
//                    }
//                    if (livePlayerImg.getVisibility() == View.VISIBLE) {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                livePlayerImg.setVisibility(View.GONE);
//                            }
//                        }, 4000);
//
//                    }
//                }
//            }
//        });
//        shareImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showShare();
//            }
//        });
//    }
//
//    @Override
//    public void onGetTabListSuccess(List<VideoCategory> result) {
//
//    }
//
//    @Override
//    public void onGetVideoListSuccess(List<VideoListModel> result, PageInfoEarly pageInfoEarly) {
//
//    }
//
//    private void setProgress() {
//        if (!isSetSeekBar && totalSecond > 0) {
//            seekBar.setMax(totalSecond);
//            startTime.setText(FormatRunTime(currentSecond));
//            endTime.setText(FormatRunTime(totalSecond));
//            isSetSeekBar = true;
//        }
//        seekBar.setProgress(currentSecond);
//        startTime.setText(FormatRunTime(currentSecond));
//        if (mVodPlayer != null && !mVodPlayer.isPlaying()) {//????????????????????????
//            livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_pause));
//            livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
//            mVodPlayer.pause();
//            isSetSeekBar = false;
//        } else {
//            livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_plear));
//            livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_play));
//            if (mVodPlayer != null) {
//                mVodPlayer.resume();
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mVodPlayer != null) {
//            mVodPlayer.stopPlay(true);
//            mVodPlayer = null;
//        }
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mVodPlayer != null) {
//            mVodPlayer.resume();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mVodPlayer != null) {
//            mVodPlayer.pause();
//        }
//    }
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        seekBar.setProgress(seekBar.getProgress());
//        startTime.setText(FormatRunTime(seekBar.getProgress()));
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//        if (mVodPlayer != null) {
//            mVodPlayer.seek(seekBar.getProgress());
//            mVodPlayer.resume();//????????????
//        }
//    }
//
//    public String FormatRunTime(long runTime) {
//        if (runTime <= 0) {
//            return "00:00";
//        }
//        long hour = 0;
//        long minute = 0;
//        if (runTime > 3600) {
//            hour = runTime / 3600;
//        }
//        if (runTime > 60) {
//            minute = (runTime % 3600) / 60;
//        }
//        long second = runTime % 60;
//
//        if (hour <= 0) {
//            return unitTimeFormat(minute) + ":" +
//                    unitTimeFormat(second);
//
//        } else {
//            return unitTimeFormat(hour) + ":" + unitTimeFormat(minute) + ":" +
//                    unitTimeFormat(second);
//        }
//
//    }
//
//    private String unitTimeFormat(long number) {
//        return String.format("%02d", number);
//    }
//
//    @Override
//    public String getSurl() {
//        return surl;
//    }
//
//    @Override
//    public String getSdes() {
//        return sdes;
//    }
//
//    @Override
//    public String getStitle() {
//        return stitle;
//    }
//
//    @Override
//    public Bitmap getsBitmap() {
//        return sBitmap;
//    }
//
//    private void buildDes(VideoListModel result) {
//        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_classVideoContUrl);
//        String url = String.format("%s?id=%s&scheme=HMMVideoDetail&videoId=%s", urlPrefix, id, id);
//        surl = url;
//        stitle = "?????????????????????";
//        sdes = result.videoName;
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void refresh(CommentEvent msg) {
//        if (msg.type == 3) {
//            hanMomVideoPresenter.getVideoDetail(new SimpleHashMapBuilder<String, Object>().puts("id", id)
//                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))), 2);
//        }
//    }
//}