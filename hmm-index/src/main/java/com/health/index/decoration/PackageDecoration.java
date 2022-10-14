package com.health.index.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.health.index.adapter.BirthPackageAdapter;
import com.health.index.model.BirthPackageModel;
import com.healthy.library.utils.TransformUtil;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/24 17:48
 * @des 待产包分割线
 */
public class PackageDecoration extends RecyclerView.ItemDecoration {

    private int mTop;
    private int mTopMargin;
    private Paint mRectPaint;
    private Paint mTextPaint;
    private float mLeft;

    public PackageDecoration(Context context) {
        mTop = (int) TransformUtil.dp2px(context, 45);
        mLeft = TransformUtil.dp2px(context, 20);
        mTopMargin = (int) TransformUtil.dp2px(context, 15);

        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setColor(Color.parseColor("#F5F5F9"));
        mRectPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#666666"));
        mTextPaint.setTextSize(TransformUtil.dp2px(context, 13));
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        if (parent.getAdapter() != null && parent.getAdapter() instanceof BirthPackageAdapter) {
            BirthPackageAdapter adapter = (BirthPackageAdapter) parent.getAdapter();
            List<BirthPackageModel> list = adapter.getData();
            boolean isFirst = pos == 0 || (pos > 0 && list.get(pos).getPrepareStatus() !=
                    list.get(pos - 1).getPrepareStatus());
            if (isFirst) {
                outRect.top = mTop + mTopMargin;
            } else {
                outRect.top = mTopMargin;
            }
            outRect.bottom = mTopMargin;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (parent.getAdapter() != null && parent.getAdapter() instanceof BirthPackageAdapter) {
            BirthPackageAdapter adapter = (BirthPackageAdapter) parent.getAdapter();
            List<BirthPackageModel> list = adapter.getData();
            for (int i = 0; i < parent.getChildCount(); i++) {
                View view = parent.getChildAt(i);
                int pos = parent.getChildAdapterPosition(view);
                boolean isFirst = pos == 0 || (pos > 0 && list.get(pos).getPrepareStatus() !=
                        list.get(pos - 1).getPrepareStatus());
                if (isFirst) {
                    c.drawRect(view.getLeft(), view.getTop() - mTop - mTopMargin,
                            view.getRight(), view.getTop() - mTopMargin, mRectPaint);
                    String text = list.get(pos).getPrepareStatus() == BirthPackageModel.PREPARED ?
                            "已准备" : "未准备";
                    float offset = mTop * 0.5f - (mTextPaint.getFontMetrics().ascent -
                            mTextPaint.getFontMetrics().descent) * 0.5f;
                    c.drawText(text, mLeft, view.getTop() - mTop - mTopMargin + offset, mTextPaint);
                }
            }
        }
    }
}