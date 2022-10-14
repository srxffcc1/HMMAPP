package com.health.index.decoration;

import android.content.Context;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.healthy.library.utils.TransformUtil;

/**
 * @author Li
 * @date 2019/04/25 15:48
 * @des B超常见项目分割线
 */
public class AnalyzeDecoration extends RecyclerView.ItemDecoration {
    private int mVerticalGap;
    private int mBigGap;
    private int mSmallGap;
    private int mHeaderNum;

    public AnalyzeDecoration(Context context, int headerNum) {
        mVerticalGap = (int) TransformUtil.dp2px(context, 10);
        mBigGap = (int) TransformUtil.dp2px(context, 15);
        mSmallGap = (int) TransformUtil.dp2px(context, 7.5f);
        mHeaderNum = headerNum;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getLayoutManager() != null &&
                parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            int pos = parent.getChildAdapterPosition(view);
            int spanCount = layoutManager.getSpanCount();
            if (pos < mHeaderNum) {
                outRect.set(0, 0, 0, 0);
            } else {
                if (pos - mHeaderNum < spanCount) {
                    outRect.top = mVerticalGap;
                } else {
                    outRect.top = 0;
                }
                outRect.bottom = mVerticalGap;

                if ((pos - mHeaderNum) % spanCount == 0) {
                    outRect.left = mBigGap;
                    outRect.right = mSmallGap;
                } else if ((pos - mHeaderNum) % spanCount == spanCount - 1) {
                    outRect.left = mSmallGap;
                    outRect.right = mBigGap;
                } else {
                    outRect.left = mSmallGap;
                    outRect.right = mSmallGap;
                }
            }


        }
    }
}
