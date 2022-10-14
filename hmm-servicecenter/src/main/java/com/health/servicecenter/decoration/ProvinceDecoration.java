package com.health.servicecenter.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Li
 * @date 2019/03/04 16:40
 * @des 省列表分割线
 */

public class ProvinceDecoration extends RecyclerView.ItemDecoration {



    private Paint mPaint;
    private float mDividerHeight;
    public ProvinceDecoration(){
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#D1D8E2"));
        mPaint.setStyle(Paint.Style.FILL);
        mDividerHeight = 1;
    }
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view=parent.getChildAt(i);
            int left=parent.getPaddingLeft();
            int bottom=view.getBottom();
            int right=view.getRight();
            c.drawRect(left,bottom,right,bottom+mDividerHeight,mPaint);
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom= (int) mDividerHeight;
    }
}
