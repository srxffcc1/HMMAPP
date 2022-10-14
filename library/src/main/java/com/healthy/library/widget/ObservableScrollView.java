package com.healthy.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
@Deprecated
public class ObservableScrollView extends HorizontalScrollView {

    public void setOnObservableScrollListener(OnObservableScrollListener onObservableScrollListener) {
        this.onObservableScrollListener = onObservableScrollListener;
    }

    private OnObservableScrollListener onObservableScrollListener = null;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (onObservableScrollListener != null) {
            onObservableScrollListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
    public interface OnObservableScrollListener {
        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
    }
}
