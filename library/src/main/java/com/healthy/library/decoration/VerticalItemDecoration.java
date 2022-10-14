package com.healthy.library.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Author: Li
 * Date: 2018/10/17 0017
 * Description:
 */
public class VerticalItemDecoration extends RecyclerView.ItemDecoration {
    private int mDividerHeight;
    private Paint mPaint;

    public VerticalItemDecoration(int dividerHeight, int dividerColor) {
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);

    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            c.drawRect(0, view.getBottom(), view.getRight(),
                    view.getBottom() + mDividerHeight, mPaint);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mDividerHeight;
    }
}
