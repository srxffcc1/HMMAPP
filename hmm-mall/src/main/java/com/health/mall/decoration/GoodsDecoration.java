//package com.health.mall.decoration;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.View;
//
//import com.healthy.library.utils.TransformUtil;
//
///**
// * @author Li
// * @date 2019/03/04 17:38
// * @des 商品列表分割线
// */
//
//public class GoodsDecoration extends RecyclerView.ItemDecoration {
//    private Paint mPaint;
//    private float mDividerWidth;
//    private static final float DIVIDER_WIDTH_DP = 1;
//    private static final float OFFSET_DP = 15;
//    private float mOffset;
//    private int mDashGaps = 61;
//
//    public GoodsDecoration(Context context) {
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setColor(Color.parseColor("#D1D8E2"));
//        mPaint.setStyle(Paint.Style.STROKE);
//        mDividerWidth = TransformUtil.dp2px(context, DIVIDER_WIDTH_DP);
//        mPaint.setStrokeWidth(mDividerWidth);
//        mOffset = TransformUtil.dp2px(context, OFFSET_DP);
//    }
//
//    @Override
//    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        super.onDrawOver(c, parent, state);
//        for (int i = 0; i < parent.getChildCount(); i++) {
//            View view = parent.getChildAt(i);
//            int pos = parent.getChildAdapterPosition(view);
//            if (parent.getAdapter() != null && pos == parent.getAdapter().getItemCount() - 1) {
//                continue;
//            }
//            int bottom = view.getBottom();
//            int left = (int) (view.getLeft() + parent.getPaddingLeft() + mOffset);
//            int right = (int) (view.getRight() - parent.getPaddingRight() - mOffset);
//            int width = right - left;
//            float singleWidth = width * 1.0f / mDashGaps;
//            for (int j = 0; j < mDashGaps; j++) {
//                if (j % 2 == 0) {
//                    float startX = singleWidth * j + mOffset;
//                    float endX = singleWidth * (j + 1) + mOffset;
//                    c.drawLine(startX, bottom, endX, bottom, mPaint);
//                }
//            }
//        }
//    }
//}
