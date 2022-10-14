package com.health.faq.decoration;

import android.content.Context;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.healthy.library.utils.TransformUtil;

/**
 * @author Li
 * @date 2019/06/28 14:21
 * @des 问题列表分割线
 */
public class QuestionDecoration extends RecyclerView.ItemDecoration {


    private int mGap;

    public QuestionDecoration(Context context) {
        mGap = (int) TransformUtil.dp2px(context, 10);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        if (pos == 0) {
            outRect.top = mGap;
        } else {
            outRect.top = 0;
        }
        outRect.left = outRect.right = outRect.bottom = mGap;
    }
}
