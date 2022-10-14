package com.healthy.library.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Author: Li
 * Date: 2017/10/18 0018
 * Description: 联系人审批列表悬浮分割线
 */

public class StickyItemDecoration extends RecyclerView.ItemDecoration {

    private ISticky mISticky;
    private int mRectHeight;
    private Paint mTxtPaint;
    private Paint mRectPaint;
    private float mPadding;

    public StickyItemDecoration(float textSize, float padding, ISticky iSticky) {
        mISticky = iSticky;
        mPadding = padding;
        mRectHeight = (int) (textSize * 2);
        mTxtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTxtPaint.setColor(Color.parseColor("#999999"));
        mTxtPaint.setTextSize(textSize);

        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setColor(Color.parseColor("#ffffff"));

    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int childCount = parent.getChildCount();
        int itemCount = state.getItemCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        String preGroupTitle;
        String groupTitle = "";
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int pos = parent.getChildLayoutPosition(child);
            preGroupTitle = groupTitle;
            groupTitle = mISticky.getGroupName(pos);
            if (groupTitle.equals(preGroupTitle)) {
                continue;
            }
            //边框底部
            int rectBottom = Math.max(mRectHeight, child.getTop());

            int viewBottom = child.getBottom();
            String title = mISticky.getGroupName(pos);
            //防止数组越界
            if (pos + 1 < itemCount) {
                String nextGroupTitle = mISticky.getGroupName(pos + 1);
                //当下一组标题和当前这组标题不一样 并且下一组往上滚动时
                if (!nextGroupTitle.equals(groupTitle) && viewBottom < rectBottom) {
                    //将上一个往上移动
                    rectBottom = viewBottom;
                }
            }
            c.drawRect(left, rectBottom - mRectHeight, right, rectBottom, mRectPaint);
            float value = mTxtPaint.getFontMetrics().ascent + mTxtPaint.getFontMetrics().descent;
            c.drawText(title, left + mPadding,
                    rectBottom - mRectHeight / 2 - value / 2, mTxtPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildLayoutPosition(view);

        if (mISticky.isFirstPosition(pos)) {
            outRect.top = mRectHeight;
            outRect.bottom = 1;
        } else {
            outRect.bottom = 1;
        }
    }

    public interface ISticky {
        boolean isFirstPosition(int pos);
        String getGroupName(int pos);
    }

}
