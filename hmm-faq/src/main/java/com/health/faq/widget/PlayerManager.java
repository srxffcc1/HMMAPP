package com.health.faq.widget;

import android.annotation.SuppressLint;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * @author Li
 * @date 2019/07/10 11:01
 * @des
 */

public class PlayerManager implements LifecycleObserver {
    private MediaPlayer mediaPlayer;
    private String mOriginPath = "";
    private Handler mHandler = new Handler();
    private VoiceLayout mVoiceLayout;
    private ExpertVoiceView mExpertVoiceView;
    private OnPlayerLife mOnPlayerLife;
    @SuppressLint("HandlerLeak")
    private Handler mCallBackHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mVoiceLayout != null) {
                if (msg.what == 0) {
                    mVoiceLayout.setState(VoiceLayout.STATE_PAUSE);
                } else if (msg.what == 1) {
                    mVoiceLayout.setState(VoiceLayout.STATE_PLAY);
                }
            }

            if (mExpertVoiceView != null) {
                if (msg.what == 0) {
                    mExpertVoiceView.setState(ExpertVoiceView.STATE_PLAY);
                } else if (msg.what == 1) {
                    mExpertVoiceView.setState(ExpertVoiceView.STATE_PAUSE);
                }
            }

        }
    };
    private int mSeekMillSeconds = 0;

    public void setOnPlayerLife(OnPlayerLife onPlayerLife) {
        mOnPlayerLife = onPlayerLife;
    }

    public void setPath(String path) {
        mOriginPath = path;
    }

    public static PlayerManager newInstance() {
        return new PlayerManager();
    }

    public void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setScreenOnWhilePlaying(true);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mSeekMillSeconds = 0;
                if (mVoiceLayout != null) {
                    mVoiceLayout.setState(VoiceLayout.STATE_PLAY);
                }
                if (mExpertVoiceView != null) {
                    mExpertVoiceView.setState(ExpertVoiceView.STATE_PAUSE);
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mHandler.removeCallbacks(mCountdownRunnable);
                mSeekMillSeconds = 0;
                if (mVoiceLayout != null) {
                    mVoiceLayout.setState(VoiceLayout.STATE_PAUSE);
                }
                if (mExpertVoiceView != null) {
                    mExpertVoiceView.setState(ExpertVoiceView.STATE_PLAY);
                }
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (mVoiceLayout != null) {
                    mVoiceLayout.setState(VoiceLayout.STATE_PAUSE);
                    if (mVoiceLayout.getContext() != null) {
                        Toast.makeText(mVoiceLayout.getContext(), "播放出错", Toast.LENGTH_SHORT).show();
                    }
                }
                if (mExpertVoiceView != null) {
                    mExpertVoiceView.setState(ExpertVoiceView.STATE_PLAY);
                    if (mExpertVoiceView.getContext() != null) {
                        Toast.makeText(mExpertVoiceView.getContext(), "播放出错", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }

    private PlayerManager() {
        initMediaPlayer();
    }

    public void play(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!mOriginPath.equals(path)) {
                        mOriginPath = path;
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(path);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        startCountdown();

                    } else {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                            mediaPlayer.seekTo(mSeekMillSeconds);
                            startCountdown();
                            mCallBackHandler.sendEmptyMessage(1);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mVoiceLayout != null) {
                        mCallBackHandler.sendEmptyMessage(0);
                    }
                    if (mExpertVoiceView != null) {
                        mCallBackHandler.sendEmptyMessage(0);
                    }
                }
            }
        }).start();


    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mSeekMillSeconds = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            mHandler.removeCallbacks(mCountdownRunnable);
            if (mVoiceLayout != null) {
                mVoiceLayout.setState(VoiceLayout.STATE_PAUSE);
            }
            if (mExpertVoiceView != null) {
                mExpertVoiceView.setState(ExpertVoiceView.STATE_PLAY);
            }
        }

    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mSeekMillSeconds = 0;
            mediaPlayer.stop();
            mHandler.removeCallbacks(mCountdownRunnable);
            if (mVoiceLayout != null) {
                mVoiceLayout.setState(VoiceLayout.STATE_PAUSE);
            }
        }
    }

    public void attachVoiceLayout(VoiceLayout voiceLayout) {
        mVoiceLayout = voiceLayout;
    }

    public void attachExpertVoiceView(ExpertVoiceView expertVoiceView) {
        mExpertVoiceView = expertVoiceView;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        pause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mHandler.removeCallbacks(mCountdownRunnable);

    }

    private void startCountdown() {
        mHandler.postDelayed(mCountdownRunnable, 0);
    }

    private Runnable mCountdownRunnable = new Runnable() {
        @Override
        public void run() {
            if (mOnPlayerLife != null) {
                mOnPlayerLife.onPlayProgressChanged(mediaPlayer.getCurrentPosition(),
                        mediaPlayer.getDuration());
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    public interface OnPlayerLife {


        void onPlayProgressChanged(int current, int total);


    }
}