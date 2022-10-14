package com.health.index.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.index.R;
import com.health.index.contract.HanMomVideoContract;
import com.health.index.presenter.HanMomVideoPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.VideoCommentDialog;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.message.CommentEvent;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.VideoCategory;
import com.healthy.library.model.VideoListModel;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class HanMomVideoPlayerFragment extends BaseFragment implements HanMomVideoContract.View, SeekBar.OnSeekBarChangeListener, IsNeedShare {

    private String url;//视频地址
    private String videoId;//视频id
    private String photo;//视频封面

    private TXCloudVideoView videoView;
    private TXVodPlayer mVodPlayer;
    private ConstraintLayout activityView;
    private TextView videoTitle;
    private TextView videoContent;
    private ConstraintLayout bottomPlayerLayout;
    private ImageView livePlayer;
    private TextView startTime;
    private SeekBar seekBar;
    private TextView endTime;
    private ConstraintLayout bottomCommentLayout;
    private LinearLayout commentLayout;
    private LinearLayout commentNumLayout;
    private LinearLayout scroll;
    private TextView commentNum;
    private LinearLayout clickLayout;
    private ImageView praiseImg;
    private TextView clickNum;
    private ImageView livePlayerImg;
    private ImageView photoImg;
    private ImageView shareImg;

    private int totalSecond = 0;//视频总时长  秒数
    private int currentSecond = 0;//当前播放时长  秒数
    private boolean isPraise = false;//是否点赞
    private boolean isSetSeekBar = false;
    String surl;
    String sdes;
    String stitle;
    Bitmap sBitmap;

    private VideoCommentDialog videoCommentDialog;
    private HanMomVideoPresenter hanMomVideoPresenter;

    public HanMomVideoPlayerFragment() {
    }

    public static HanMomVideoPlayerFragment newInstance(String url, String Id, String photo) {
        HanMomVideoPlayerFragment fragment = new HanMomVideoPlayerFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("Id", Id);
        args.putString("photo", photo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString("url");
            videoId = getArguments().getString("Id");
            photo = getArguments().getString("photo");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_han_mom_video_player;
    }

    @Override
    protected void findViews() {
        videoView = (TXCloudVideoView) findViewById(R.id.video_view);
        activityView = (ConstraintLayout) findViewById(R.id.activityView);
        videoTitle = (TextView) findViewById(R.id.videoTitle);
        videoContent = (TextView) findViewById(R.id.videoContent);
        bottomPlayerLayout = (ConstraintLayout) findViewById(R.id.bottomPlayerLayout);
        livePlayer = (ImageView) findViewById(R.id.livePlayer);
        startTime = (TextView) findViewById(R.id.startTime);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        endTime = (TextView) findViewById(R.id.endTime);
        bottomCommentLayout = (ConstraintLayout) findViewById(R.id.bottomCommentLayout);
        commentLayout = (LinearLayout) findViewById(R.id.commentLayout);
        scroll = (LinearLayout) findViewById(R.id.scroll);
        commentNumLayout = (LinearLayout) findViewById(R.id.commentNumLayout);
        commentNum = (TextView) findViewById(R.id.commentNum);
        clickLayout = (LinearLayout) findViewById(R.id.clickLayout);
        praiseImg = (ImageView) findViewById(R.id.praiseImg);
        clickNum = (TextView) findViewById(R.id.clickNum);
        livePlayerImg = (ImageView) findViewById(R.id.livePlayerImg);
        photoImg = (ImageView) findViewById(R.id.photo);
        shareImg = (ImageView) findViewById(R.id.shareImg);
        hanMomVideoPresenter = new HanMomVideoPresenter(mContext, this);
        photoImg.setVisibility(View.VISIBLE);
        initPlayer();
        initListener();
    }

    private void initListener() {
        seekBar.setOnSeekBarChangeListener(this);
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
        livePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVodPlayer.isPlaying()) {//正在播放
                    livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_pause));
                    livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
                    mVodPlayer.pause();
                } else {
                    livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_plear));
                    livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_play));
                    mVodPlayer.resume();
                }
            }
        });
        activityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVodPlayer.isPlaying()) {//正在播放
                    livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_play));
                    livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_plear));
                } else {
                    livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
                    livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_pause));
                }
                if (SpUtils.getValue(mContext, "videoIsScroll", false)) {
                    livePlayerImg.setVisibility(View.VISIBLE);
                }
                if (livePlayerImg.getVisibility() == View.VISIBLE) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            livePlayerImg.setVisibility(View.GONE);
                        }
                    }, 4000);

                }
            }
        });
        livePlayerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVodPlayer != null) {
                    if (mVodPlayer.isPlaying()) {//正在播放
                        livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
                        livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_pause));
                        mVodPlayer.pause();
                    } else {
                        livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_play));
                        livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_plear));
                        mVodPlayer.resume();
                    }
                    if (livePlayerImg.getVisibility() == View.VISIBLE) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                livePlayerImg.setVisibility(View.GONE);
                            }
                        }, 4000);

                    }
                }
            }
        });
    }

    private void initPlayer() {
        //创建 player 对象
        mVodPlayer = new TXVodPlayer(mContext);
        //关联 player 对象与界面 view
        mVodPlayer.setPlayerView(videoView);
        mVodPlayer.setLoop(true);//是否循环播放
        mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        mVodPlayer.setVodListener(new ITXVodPlayListener() {
            @Override
            public void onPlayEvent(TXVodPlayer txVodPlayer, int i, Bundle bundle) {
                totalSecond = bundle.getInt(TXLiveConstants.EVT_PLAY_DURATION);//视频总时长  秒数
                currentSecond = bundle.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);//当前播放进度  秒数
                //LogUtils.e("视频总时长：" + totalSecond);
                //LogUtils.e("当前播放到：" + currentSecond);
                if (totalSecond > 0 && currentSecond > 0) {
                    setProgress();
                }
                if (i == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
                    int width = bundle.getInt("EVT_PARAM1");
                    int height = bundle.getInt("EVT_PARAM2");
                    setInsideModel(width, height);
                }
                if (i == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                    photoImg.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

            }
        });
    }

    private void setInsideModel(int width, int height) {
        // 设置填充模式
        if (height > 0) {
            double size = (double) width / height;
            DisplayMetrics outMetrics = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            double mPixelsSize = (double) outMetrics.widthPixels / outMetrics.heightPixels;
            if (size > 1.0 || size > mPixelsSize) {
                mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
            } else {
                mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
            }
        } else {
            mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        }
    }

    @Override
    protected void onLazyData() {
        super.onLazyData();
        EventBus.getDefault().register(HanMomVideoPlayerFragment.this);
        if (mVodPlayer != null) {
            mVodPlayer.startPlay(url);
        }
        if (photo != null) {
            GlideCopy.with(mContext).load(photo).into(photoImg);
        }
        if (hanMomVideoPresenter != null) {
            hanMomVideoPresenter.getVideoDetail(new SimpleHashMapBuilder<String, Object>().puts("id", videoId)
                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))), 1);
        }
        if (SpUtils.getValue(mContext, "videoIsScroll", false)) {
            scroll.setVisibility(View.GONE);
        } else {
            scroll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mVodPlayer == null) {
            return;
        }
        if (isVisibleToUser) {
            mVodPlayer.resume();
        } else {
            mVodPlayer.pause();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVodPlayer != null) {
            mVodPlayer.stopPlay(true);
            videoView.onDestroy();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(HanMomVideoPlayerFragment.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVodPlayer != null) {
            mVodPlayer.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVodPlayer != null) {
            mVodPlayer.pause();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekBar.setProgress(seekBar.getProgress());
        startTime.setText(FormatRunTime(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mVodPlayer != null) {
            mVodPlayer.seek(seekBar.getProgress());
            mVodPlayer.resume();//恢复播放
        }
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
            if (type == 1) {
                buildDes(result);
                hanMomVideoPresenter.addPlayVolume(new SimpleHashMapBuilder<String, Object>().puts("id", result.id));
                videoTitle.setText(result.videoName);
                videoContent.setText(result.videoRemark);
                commentNum.setText(result.discussCount + "");
                clickNum.setText(result.praiseCount + "");
                if (result.praise) {
                    isPraise = true;
                    praiseImg.setImageResource(R.drawable.hanmom_videodetial_clicksuccess_icon);
                } else {
                    isPraise = false;
                    praiseImg.setImageResource(R.drawable.hanmom_videodetial_click_icon);
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
                commentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (videoCommentDialog == null) {
                            videoCommentDialog = videoCommentDialog.newInstance();
                        }
                        videoCommentDialog.setId(result.id);
                        videoCommentDialog.show(getChildFragmentManager(), "");
                    }
                });
                commentNumLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (videoCommentDialog == null) {
                            videoCommentDialog = videoCommentDialog.newInstance();
                        }
                        videoCommentDialog.setId(result.id);
                        videoCommentDialog.show(getChildFragmentManager(), "");
                    }
                });
                Glide.with(mContext).load(result.photo)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(new SimpleTarget<Drawable>() {

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                sBitmap = DrawableUtils.drawableToBitmap(resource);
                            }
                        });
            } else {
                commentNum.setText(result.discussCount + "");
                clickNum.setText(result.praiseCount + "");
            }
        } else {
            livePlayer.setVisibility(View.GONE);
            showEmpty();
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
            praiseImg.setImageResource(R.drawable.hanmom_videodetial_click_icon);
        }
        if (result != null && !TextUtils.isEmpty(result)) {
            clickNum.setText(result + "");
        }
    }

    private void setProgress() {
        if (!isSetSeekBar && totalSecond > 0) {
            seekBar.setMax(totalSecond);
            startTime.setText(FormatRunTime(currentSecond));
            endTime.setText(FormatRunTime(totalSecond));
            isSetSeekBar = true;
        }
        seekBar.setProgress(currentSecond);
        startTime.setText(FormatRunTime(currentSecond));
        if (mVodPlayer != null && !mVodPlayer.isPlaying()) {//说明进度条走完了
            livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_pause));
            livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_stop));
            mVodPlayer.pause();
            isSetSeekBar = false;
        } else {
            livePlayer.setImageDrawable(getResources().getDrawable(R.drawable.live_plear));
            livePlayerImg.setImageDrawable(getResources().getDrawable(R.drawable.live_player_play));
            if (mVodPlayer != null) {
                mVodPlayer.resume();
            }
        }
    }

    public String FormatRunTime(long runTime) {
        if (runTime <= 0) {
            return "00:00";
        }
        long hour = 0;
        long minute = 0;
        if (runTime > 3600) {
            hour = runTime / 3600;
        }
        if (runTime > 60) {
            minute = (runTime % 3600) / 60;
        }
        long second = runTime % 60;

        if (hour <= 0) {
            return unitTimeFormat(minute) + ":" +
                    unitTimeFormat(second);

        } else {
            return unitTimeFormat(hour) + ":" + unitTimeFormat(minute) + ":" +
                    unitTimeFormat(second);
        }

    }

    private String unitTimeFormat(long number) {
        return String.format("%02d", number);
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
        String url = String.format("%s?id=%s&scheme=HMMVideoDetail&videoId=%s", urlPrefix, videoId, videoId);
        surl = url;
        stitle = "憨妈妈专家课堂";
        sdes = result.videoName;
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void share(CommentEvent msg) {
//        if (msg.type == 4) {
//            showShare();
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scroll(CommentEvent msg) {
        if (msg.type == 5) {
            scroll.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(CommentEvent msg) {
        if (msg.type == 3) {
            hanMomVideoPresenter.getVideoDetail(new SimpleHashMapBuilder<String, Object>().puts("id", videoId)
                    .puts("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))), 2);
        }
    }
}