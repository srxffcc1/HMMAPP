package com.health.city.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.city.R;
import com.healthy.library.utils.LogUtils;
import com.healthy.library.utils.TransformUtil;

public class FloatingManager {
    private View mAnchorView;
    private String mTitle;
    private float x;
    private float y;
    private ViewGroup mRootView;
    private Context context;

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder {
        private FloatingManager mManager;

        public FloatingManager build() {
            return mManager;
        }

        public Builder() {
            mManager = new FloatingManager();
        }

        public Builder setAnchorView(View view) {
            mManager.setAnchorView(view);
            return this;
        }

        public Builder setTitle(String title) {
            mManager.setTitle(title);
            return this;
        }

        public Builder setX(float x) {
            mManager.setX(x);
            return this;
        }

        public Builder setY(float y) {
            mManager.setY(y);
            return this;
        }

        public Builder setContext(Context context) {
            mManager.setContext(context);
            return this;
        }
    }

    public void setAnchorView(View view) {
        mAnchorView = view;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void addCenterView() {
        if (mAnchorView == null) {
            return;
        }
        mRootView = mAnchorView.findViewById(R.id.remarkLiner);

        Rect anchorRect = new Rect();
        Rect rootViewRect = new Rect();

        mAnchorView.getGlobalVisibleRect(anchorRect);
        mRootView.getGlobalVisibleRect(rootViewRect);

        // 创建 TextView
        TextView textView = new TextView(context);
        textView.setText(mTitle);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextSize(11f);
        textView.setGravity(Gravity.CENTER);
        textView.setBackground(context.getResources().getDrawable(R.drawable.shape_sound_float_bg));
        textView.setX(x);
        textView.setY(y / 2);
        LogUtils.e("x" + x);
        LogUtils.e("y" + y);
        textView.setPadding((int) TransformUtil.dp2px(context, 15f), (int) TransformUtil.dp2px(context, 3f),
                (int) TransformUtil.dp2px(context, 15f), (int) TransformUtil.dp2px(context, 3f));
        mRootView.addView(textView);
    }

}
