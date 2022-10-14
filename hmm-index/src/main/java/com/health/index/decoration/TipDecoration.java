//package com.health.index.decoration;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.DashPathEffect;
//import android.graphics.LinearGradient;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.graphics.Shader;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import android.text.TextPaint;
//import android.util.TypedValue;
//import android.view.View;
//
//import com.health.index.adapter.TipAdapter;
//import com.health.index.model.TipModel;
//
//import java.util.List;
//
///**
// * @author Li
// * @date 2019/03/25 15:04
// * @des 今日贴士分割线
// */
//
//public class TipDecoration extends RecyclerView.ItemDecoration {
//
//    private Context mContext;
//
//    private Paint mCirclePaint;
//    private TextPaint mContentPaint;
//    private TextPaint mPaint;
//    private Paint mDashPaint;
//
//    public TipDecoration(Context context) {
//        mContext = context;
//        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mCirclePaint.setStyle(Paint.Style.FILL);
//
//        mContentPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
//        mContentPaint.setColor(Color.WHITE);
//        mContentPaint.setTextSize(dp2px(mContext, 14));
//        mContentPaint.setTextAlign(Paint.Align.CENTER);
//
//        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
//
//        mDashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mDashPaint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
//        mDashPaint.setColor(Color.parseColor("#A6A7B6"));
//    }
//
//    private float dp2px(Context context, float dp) {
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
//                context.getResources().getDisplayMetrics());
//    }
//
//    @Override
//    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
//                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
//
//        if (parent.getAdapter() != null && parent.getAdapter() instanceof TipAdapter) {
//            TipAdapter tipAdapter = (TipAdapter) parent.getAdapter();
//            List<TipModel> list = tipAdapter.getData();
//            int pos = parent.getChildAdapterPosition(view);
//            if (isFirst(pos, list)) {
//                outRect.top = (int) dp2px(mContext, 60);
//            } else {
//                outRect.top = (int) dp2px(mContext, 19);
//            }
//            outRect.left = (int) dp2px(mContext, 46);
//            outRect.bottom = (int) dp2px(mContext, 19);
//            outRect.right = (int) dp2px(mContext, 10);
//        }
//
//    }
//
//    @Override
//    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent,
//                           @NonNull RecyclerView.State state) {
//        super.onDrawOver(c, parent, state);
//
//    }
//
//    @Override
//    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent,
//                       @NonNull RecyclerView.State state) {
//        super.onDraw(c, parent, state);
//        parent.setLayerType(View.LAYER_TYPE_SOFTWARE, mDashPaint);
//        if (parent.getAdapter() != null && parent.getAdapter() instanceof TipAdapter) {
//            TipAdapter tipAdapter = (TipAdapter) parent.getAdapter();
//            List<TipModel> list = tipAdapter.getData();
//            for (int i = 0; i < parent.getChildCount(); i++) {
//                View view = parent.getChildAt(i);
//                int pos = parent.getChildAdapterPosition(view);
//                int left = view.getLeft();
//                int top = view.getTop();
//                Rect outRect = new Rect();
//                getItemOffsets(outRect, view, parent, state);
//                c.drawLine(left * 0.5f, top - outRect.top,
//                        left * 0.5f, view.getBottom() + outRect.bottom,
//                        mDashPaint);
//                if (isFirst(pos, list)) {
//
//                    float centerX = left * 0.5f;
//
//                    float radius = dp2px(mContext, 13);
//
//                    float centerY = top - outRect.top * 0.5f;
//                    LinearGradient linearGradient = new LinearGradient(0, top - outRect.top,
//                            radius, top - outRect.top + radius, Color.parseColor("#4DE4B6"),
//                            Color.parseColor("#22DDDD"), Shader.TileMode.CLAMP);
//                    mCirclePaint.setShader(linearGradient);
//                    c.drawCircle(centerX, centerY,
//                            radius, mCirclePaint);
//                    c.drawText("孕", centerX, centerY +
//                            Math.abs(mContentPaint.ascent() + mContentPaint.descent()) * 0.5f, mContentPaint);
//
//                    mPaint.setColor(Color.parseColor("#222222"));
//                    mPaint.setTextSize(dp2px(mContext, 16));
//                    mPaint.setFakeBoldText(true);
//
//
//                    float height = mPaint.descent() - mPaint.ascent();
//                    c.drawText("4周3天", left, centerY +
//                            Math.abs(mPaint.ascent() + mPaint.descent()) * 0.5f, mPaint);
//
//                    mPaint.setColor(Color.parseColor("#FF6266"));
//                    mPaint.setFakeBoldText(false);
//                    mPaint.setTextSize(dp2px(mContext, 11));
//                    c.drawText("今天", left,
//                            centerY + height * 0.5f + (mPaint.descent() - mPaint.ascent()) * 0.5f +
//                                    dp2px(mContext, 3),
//                            mPaint);
//                }
//            }
//        }
//
//    }
//
//    private boolean isFirst(int pos, List<TipModel> list) {
//        return true;
////        return pos == 0 || pos >= 1 && list.get(pos - 1).getDay() != list.get(pos).getDay();
//    }
//}
