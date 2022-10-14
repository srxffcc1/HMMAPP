package com.healthy.library.watcher;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Li
 * @date 2019/03/19 10:47
 * @des 按压时改变透明度
 */

public class AlphaTextView extends androidx.appcompat.widget.AppCompatTextView {
    public AlphaTextView(Context context) {
        this(context, null);
    }

    public AlphaTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        setAlpha(pressed ? 0.5f : 1.0f);
    }
}
