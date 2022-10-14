package com.healthy.library.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.healthy.library.R;


/**
 * @ClassName: FilterImageView
 * @Description:  点击时显示明暗变化(滤镜效果)的ImageView
 * @author LinJ
 * @date 2015-1-6 下午2:13:46
 *
 */
public class AutoAnimatioView extends FrameLayout {


    public AutoAnimatioView(@NonNull Context context) {
        this(context,null);
    }

    public AutoAnimatioView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AutoAnimatioView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        startScaleAuto();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AutoAnimatioView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void startScaleAuto(){
        scaleToSmaller();
        scaleToNormal();
    }

    private void scaleToSmaller()
    {
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_xysize_smaller_anim);
        clearAnimation();
        startAnimation(loadAnimation);
    }
    private void scaleToNormal()
    {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_xysize_normal_anim);
                clearAnimation();
                startAnimation(loadAnimation);
                startScaleAuto();
            }
        },200);

    }
}