package com.health.mine.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.healthy.library.utils.TransformUtil;

/**
 * @author Li
 * @date 2019/05/28 16:03
 * @des 调理记录
 */
public class ServiceRecordDecoration extends RecyclerView.ItemDecoration {

    private int mPadding;
    private Paint mSolidLinePaint;
    private Paint mDashPaint;

    public ServiceRecordDecoration(Context context) {
        mPadding = (int) TransformUtil.dp2px(context, 15);
        mSolidLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSolidLinePaint.setStyle(Paint.Style.FILL);
        mSolidLinePaint.setColor(Color.parseColor("#E4E8EE"));

        mDashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDashPaint.setStyle(Paint.Style.FILL);
        mDashPaint.setColor(Color.parseColor("#D1D8E2"));
        mDashPaint.setStrokeWidth(TransformUtil.dp2px(context, 1));
        mDashPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = outRect.right = mPadding;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        parent.setLayerType(View.LAYER_TYPE_SOFTWARE, mDashPaint);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(view);
            if (parent.getAdapter() != null) {
                int lastPos = parent.getAdapter().getItemCount() - 1;
                if (pos == 0 || pos == lastPos) {
                    c.drawLine(parent.getLeft(), view.getBottom(), parent.getRight(),
                            view.getBottom(), mSolidLinePaint);
                } else {
                    c.drawLine(view.getLeft(), view.getBottom(), view.getRight(),
                            view.getBottom(), mDashPaint);
                }
            }

        }
    }
}
