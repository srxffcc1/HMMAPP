package com.healthy.library.decoration;

import android.content.Context;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.healthy.library.utils.TransformUtil;

/**
 * @author Li
 * @date 2019/04/10 14:06
 * @des
 */

public class OrderDecoration extends RecyclerView.ItemDecoration {

    private int mHorizontalOffsets;
    private int mBigVerticalOffsets;
    private int mNormalVerticalOffsets;

    public OrderDecoration(Context context) {
        mBigVerticalOffsets = (int) TransformUtil.dp2px(context, 18);
        mNormalVerticalOffsets = (int) TransformUtil.dp2px(context, 10);
        mHorizontalOffsets = (int) TransformUtil.dp2px(context, 10);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildLayoutPosition(view);
        if (pos == 0) {
            outRect.top = mBigVerticalOffsets;
        } else {
            outRect.top = 0;
        }
        outRect.left = outRect.right = mHorizontalOffsets;
        outRect.bottom = mNormalVerticalOffsets;
    }
}
