package com.gongwen.marqueen;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by GongWen on 17/9/15.
 */
public class SimpleMarqueeFactory<E extends CharSequence> extends MarqueeFactory<TextView, E> {
    public SimpleMarqueeFactory(Context mContext) {
        super(mContext);
    }

    @Override
    public TextView generateMarqueeItemView(E data) {
        TextView mView = new TextView(mContext);
        mView.setText(data);
        fixTextView(mView);
        return mView;
    }

    public void fixTextView(TextView mView) {
    }
}