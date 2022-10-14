package com.health.faq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.faq.R;

import java.util.Locale;

/**
 * @author Li
 * @date 2019/07/09 15:16
 * @des
 */
public class VoiceLayout extends FrameLayout implements View.OnClickListener {
    private ImageView mIvStatus;
    private ImageView mIvLoading;
    private TextView mTvDuration;
    private ImageView mIvDel;
    private OnSelfClickListener mOnClickListener;
    public static final int STATE_LOADING = 1;
    public static final int STATE_PLAY = 2;
    public static final int STATE_PAUSE = 3;
    private Animation mAnimation;
    private Context mContext;
    private VoiceView mVoiceView;

    public VoiceLayout(Context context) {
        this(context, null);
    }

    public VoiceLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_voice, this, true);
        mIvStatus = findViewById(R.id.iv_status);
        mTvDuration = findViewById(R.id.tv_duration);
        mIvDel = findViewById(R.id.iv_del);
        mIvLoading = findViewById(R.id.iv_loading);
        mVoiceView = findViewById(R.id.view_voice);

        mIvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onDelClick();
                }
            }
        });
    }


    public void setDuration(long duration) {
        long seconds = duration / 1000;
        String content;
        if (seconds <= 0) {
            content = "";
        } else if (seconds < 60) {
            content = String.format(Locale.CHINA, "%d''", seconds);
        } else {
            content = String.format(Locale.CHINA, "%d'%02d''", seconds / 60, seconds % 60);
        }
        mTvDuration.setText(content);
    }

    @Override
    public void onClick(View v) {

    }

    public ImageView getIvStatus() {
        return mIvStatus;
    }

    public void setOnSelfClickListener(OnSelfClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                mIvLoading.setVisibility(VISIBLE);
                mIvStatus.setVisibility(GONE);
                mVoiceView.stopJump();
                startAnim();
                break;
            case STATE_PAUSE:
                cancelAnim();
                mIvLoading.setVisibility(GONE);
                mIvStatus.setVisibility(VISIBLE);
                mIvStatus.setSelected(false);
                mVoiceView.stopJump();
                break;
            case STATE_PLAY:
                cancelAnim();
                mIvLoading.setVisibility(GONE);
                mIvStatus.setVisibility(VISIBLE);
                mIvStatus.setSelected(true);
                mVoiceView.startJump();
                break;
            default:
                break;
        }
    }

    public ImageView getIvDel() {
        return mIvDel;
    }

    private void startAnim() {
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_voice_loading);
        mAnimation.setInterpolator(new LinearInterpolator());
        mIvLoading.startAnimation(mAnimation);
    }

    private void cancelAnim() {
        if (mAnimation != null) {
            if (mAnimation.hasStarted()) {
                mAnimation.cancel();
            }
        }
    }

    public interface OnSelfClickListener {
        void onDelClick();
    }
}
