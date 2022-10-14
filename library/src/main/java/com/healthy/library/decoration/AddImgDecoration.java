package com.healthy.library.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Li
 * @date 2019/04/16 10:37
 * @des 评论界面添加图片分割线
 */
public class AddImgDecoration extends RecyclerView.ItemDecoration {

    private int mVerticalGap;
    private int mHorizontalGap;

    public AddImgDecoration(Context context) {
        mVerticalGap = dp2px(context, 1);
        mHorizontalGap = dp2px(context, 1);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = outRect.right = mVerticalGap;
        outRect.top = outRect.bottom = mHorizontalGap;

    }

    private int dp2px(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value, context.getResources().getDisplayMetrics());
    }
}
