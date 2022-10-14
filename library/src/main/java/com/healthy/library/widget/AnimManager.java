package com.healthy.library.widget;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.healthy.library.R;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;

import java.lang.ref.WeakReference;

public class AnimManager {
    private WeakReference<Activity> mActivity;
    private AnimListener mListener;
    private long time;
    private final View startView;
    private final View endView;
    private final Object imageUrl;
    private View animView;
    private double scale;
    private float animWidth;
    private float animHeight;
    private ViewGroup animMaskLayout;

    private AnimManager() {
        this(new Builder());
    }

    AnimManager(Builder builder) {
        this.mActivity = builder.activity;
        this.startView = builder.startView;
        this.endView = builder.endView;
        this.time = builder.time;
        this.mListener = builder.listener;
        this.animView = builder.animView;
        this.imageUrl = builder.imageUrl;
        this.scale = builder.scale;
        this.animWidth = builder.animWidth;
        this.animHeight = builder.animHeight;
    }


    /**
     * 开始动画
     */
    public void startAnim() {
        if (startView == null || endView == null) {
            throw new NullPointerException("startView or endView must not null");
        }
        int[] startLocation = new int[2];
        int[] endLocation = new int[2];
        startView.getLocationInWindow(startLocation);
        endView.getLocationInWindow(endLocation);
        if (animView != null) {
            setAnim(startLocation, endLocation);
        } else if (imageUrl!=null) {
            createImageAndAnim(startLocation, endLocation);
        }
    }

    private void createImageAndAnim(final int[] startLocation, final int[] endLocation) {
        final ImageView animImageView = new ImageView(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)TransformUtil.dp2px(getActivity(), animWidth),
                (int)TransformUtil.dp2px(getActivity(), animHeight));
        animImageView.setLayoutParams(layoutParams);
        com.healthy.library.businessutil.GlideCopy.with(getActivity()).load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        setAnim(animImageView, startLocation, endLocation);
                        return false;
                    }
                })
                .into(animImageView);
    }


    private void setAnim(int[] startLocation, int[] endLocation) {
        setAnim(animView, startLocation, endLocation);
    }

    private void setAnim(final View v, int[] startLocation, int[] endLocation) {
        animMaskLayout = createAnimLayout(getActivity());
        // 把动画小球添加到动画层
        animMaskLayout.addView(v);
        final View view = addViewToAnimLayout(v, startLocation);

        //终点位置
        int endX = endLocation[0] - startLocation[0] + 20;
        // 动画位移的y坐标
        int endY = endLocation[1] - startLocation[1];
        TranslateAnimation translateAnimationX = new TranslateAnimation(0, endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        // 动画重复执行的次数
        translateAnimationX.setRepeatCount(0);
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);
        translateAnimationX.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        if (scale == 1) {
            // 计算屏幕最远两个点的直线距离
            double diagonalDef = Math.sqrt(Math.pow(ScreenUtils.getScreenWidth(getActivity()), 2) + Math.pow(ScreenUtils.getScreenHeight(getActivity()), 2));
            // 计算实际两点的距离
            double diagonal = Math.abs(Math.sqrt(Math.pow(startLocation[0] - endLocation[0], 2) + Math.pow(startLocation[1] - endLocation[1], 2)));
            // 计算一个值,不同距离动画执行的时间不同
            scale = diagonal / diagonalDef;
        }
        // 动画的执行时间,计算出的时间小于300ms默认为300ms
        set.setDuration((time * scale) < 300 ? 300 : (long) (time * scale));
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
                if (mListener != null) {
                    mListener.setAnimBegin(AnimManager.this);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // 动画的结束调用的方法
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                animMaskLayout.removeAllViews();
                if (mListener != null) {
                    mListener.setAnimEnd(AnimManager.this);
                }
            }
        });
    }

    public void stopAnim() {
        if (animMaskLayout != null && animMaskLayout.getChildCount() > 0) {
            animMaskLayout.removeAllViews();
        }
    }

    private ViewGroup createAnimLayout(Activity mainActivity) {
        ViewGroup rootView = (ViewGroup) mainActivity.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(mainActivity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(R.id.anim_icon);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToAnimLayout(final View view, int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    /**
     * 自定义时间
     *
     * @param time
     * @return
     */
    public long setTime(long time) {
        this.time = time;
        return time;
    }

    private Activity getActivity() {
        return mActivity.get();
    }

    public void setOnAnimListener(AnimListener listener) {
        mListener = listener;
    }

    //回调监听
    public interface AnimListener {

        void setAnimBegin(AnimManager a);

        void setAnimEnd(AnimManager a);

    }

    public static final class Builder {
        WeakReference<Activity> activity;
        View startView;
        View endView;
        View animView;
        Object imageUrl;
        long time;
        double scale;
        float animWidth;
        float animHeight;
        AnimListener listener;

        public Builder() {
            this.time = 1000;
            this.scale = 1;
            this.animHeight = 25;
            this.animWidth = 25;
        }

        public Builder with(Activity activity) {
            this.activity = new WeakReference<>(activity);
            return this;
        }

        public Builder startView(View startView) {
            if (startView == null) {
                throw new NullPointerException("startView is null");
            }
            this.startView = startView;
            return this;
        }

        public Builder endView(View endView) {
            if (endView == null) {
                throw new NullPointerException("endView is null");
            }
            this.endView = endView;
            return this;
        }

        public Builder animView(View animView) {
            if (animView == null) {
                throw new NullPointerException("animView is null");
            }
            this.animView = animView;
            return this;
        }

        public Builder listener(AnimListener listener) {
            if (listener == null) {
                throw new NullPointerException("listener is null");
            }
            this.listener = listener;
            return this;
        }

        public Builder imageUrl(Object imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder time(long time) {
            if (time <= 0) {
                throw new IllegalArgumentException("time must be greater than zero");
            }
            this.time = time;
            return this;
        }

        public Builder scale(double scale) {
            this.scale = scale;
            return this;
        }

        public Builder animWidth(float width) {
            if (width <= 0) {
                throw new IllegalArgumentException("width must be greater than zero");
            }
            this.animWidth = width;
            return this;
        }

        public Builder animHeight(float height) {
            if (height <= 0) {
                throw new IllegalArgumentException("height must be greater than zero");
            }
            this.animHeight = height;
            return this;
        }

        public AnimManager build() {
            return new AnimManager(this);
        }
    }
}

