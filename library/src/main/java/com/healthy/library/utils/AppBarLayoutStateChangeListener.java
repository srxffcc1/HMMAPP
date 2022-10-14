package com.healthy.library.utils;

import android.util.Log;

import com.google.android.material.appbar.AppBarLayout;

/**
 * author : long
 * Time   :2021/11/9
 * desc   :
 */
public abstract class AppBarLayoutStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
    public enum State {
        /**
         * 展开
         */
        EXPANDED,
        /**
         * 折叠
         */
        COLLAPSED,
        /**
         * 中间状态
         */
        INTERMEDIATE
    }

    private State mCurrentState = State.INTERMEDIATE;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.INTERMEDIATE) {
                onStateChanged(appBarLayout, State.INTERMEDIATE);
            }
            mCurrentState = State.INTERMEDIATE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}
