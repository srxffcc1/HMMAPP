package com.health.mall.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.healthy.library.utils.TransformUtil;

/**
 * @author Li
 * @date 2019/03/12 16:08
 * @des 评论列表分割线
 */

public class CommentDecoration extends RecyclerView.ItemDecoration {

    private int mLeftPadding;
    private int mRightPadding;
    private int mTopPadding;
    private int mBottomPadding;
    private Paint mDividerPaint;
    private int mOffset;

    public CommentDecoration(Context context, int offset) {
        mLeftPadding = mRightPadding = (int) TransformUtil.dp2px(context, 0);
        mTopPadding = (int) TransformUtil.dp2px(context, 0);
        mBottomPadding = (int) TransformUtil.dp2px(context, 0);
        mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDividerPaint.setColor(Color.parseColor("#00000000"));
        mDividerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDividerPaint.setStrokeWidth(TransformUtil.dp2px(context, 0.7f));
        mOffset = offset;
    }
    public CommentDecoration(Context context, int offset,int color) {//#E4E8EE
        mLeftPadding = mRightPadding = (int) TransformUtil.dp2px(context, 0);
        mTopPadding = (int) TransformUtil.dp2px(context, 0);
        mBottomPadding = (int) TransformUtil.dp2px(context, 0);
        mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDividerPaint.setColor(color);
        mDividerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDividerPaint.setStrokeWidth(TransformUtil.dp2px(context, 0.7f));
        mOffset = offset;
    }
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) >= mOffset) {
            outRect.left = mLeftPadding;
            outRect.right = mRightPadding;
            outRect.top = mTopPadding;
            outRect.bottom = mBottomPadding;
        }

    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(view);
            int left = parent.getPaddingLeft();
            int right = parent.getRight() - parent.getPaddingRight();
            int top = view.getBottom() + mBottomPadding;
            int bottom = view.getBottom() + mBottomPadding;
            if (state.getItemCount() != pos + 1 && pos >= mOffset) {
                c.drawLine(left, top, right, bottom, mDividerPaint);
            }
        }
    }
}
