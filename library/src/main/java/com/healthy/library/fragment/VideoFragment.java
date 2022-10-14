package com.healthy.library.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.healthy.library.R;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.utils.AES2020;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.Map;

import static com.hss01248.dialog.ScreenUtil.getWindowManager;

public class VideoFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener {

    private TXCloudVideoView videoView;
    private ImageView livePlayerImg;
    private ConstraintLayout activityView;
    private ImageView livePlayer;
    private TextView startTime;
    private SeekBar seekBar;
    private TextView endTime;

    private TXVodPlayer mVodPlayer;
    private int totalSecond = 0;//视频总时长  秒数
    private int currentSecond = 0;//当前播放时长  秒数
    private boolean isSetSeekBar = false;

    public static VideoFragment newInstance(Map<String, Object> maporg) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void findViews() {
        String videoUrl = get("url");
        initView();
        initVideo(videoUrl);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (get("clickPlay") != null) {
            if (isfragmenthow) {
                if (mVodPlayer != null) {
                    mVodPlayer.resume();
                }
            } else {
                if (mVodPlayer != null) {
                    mVodPlayer.pause();
                }
            }
        }

    }

    private void initVideo(String videoUrl) {
        livePlayer.setVisibility(View.VISIBLE);
        //创建 player 对象
        mVodPlayer = new TXVodPlayer(mContext);
        //关联 player 对象与界面 view
        mVodPlayer.setPlayerView(videoView);
        mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        if (videoUrl == null || TextUtils.isEmpty(videoUrl)) {
            showToast("获取视频播放地址失败~");
        } else {
            mVodPlayer.startPlay(AES2020.getInstance().decrypt(videoUrl, "http"));
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
                }

                @Override
                public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

                }
            });
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
    public void onDestroy() {
        super.onDestroy();
        if (mVodPlayer != null) {
            mVodPlayer.stopPlay(true);
            mVodPlayer = null;
        }
    }

    private void setInsideModel(int width, int height) {
        // 设置填充模式
        try {
            if (height > 0) {
                double size = (double) width / height;
                DisplayMetrics outMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
                double mPixelsSize = (double) outMetrics.widthPixels / outMetrics.heightPixels;
                if (size > 1.0 || size > mPixelsSize) {
                    mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
                } else {
                    mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                }
            } else {
                mVodPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        videoView = (TXCloudVideoView) findViewById(R.id.video_view);
        livePlayerImg = (ImageView) findViewById(R.id.livePlayerImg);
        activityView = (ConstraintLayout) findViewById(R.id.activityView);
        livePlayer = (ImageView) findViewById(R.id.livePlayer);
        startTime = (TextView) findViewById(R.id.startTime);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        endTime = (TextView) findViewById(R.id.endTime);
        seekBar.setOnSeekBarChangeListener(this);
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
                livePlayerImg.setVisibility(View.VISIBLE);
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
}
