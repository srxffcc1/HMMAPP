package com.healthy.library.widget;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import androidx.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.healthy.library.utils.TimestampUtils;

import java.util.Date;

/**
 * @author Li
 * @date 2019/03/22 13:17
 * @des 怀孕几率
 */

public class PregnancyView extends View {
    private Paint mBigCirclePaint;
    private RadialGradient mBigRadialGradient;
    private RadialGradient mSmallRadialGradient;
    private Paint mArcPaint;
    private float mSmallCircleRadius;
    private Paint mSmallCirclePaint;
    private float mPercent = 0.38f;
    private float mStrokeWidth;
    private TextPaint mTextPaint;
    private float mCenterX;
    private float mCenterY;
    private RectF mOval;
    private String mDateContent="";
    private Paint blurpaint;

    public PregnancyView(Context context) {
        this(context, null);
    }

    public PregnancyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PregnancyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mOval = new RectF();

        mSmallCircleRadius = dp2px(9);
        mSmallCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSmallCirclePaint.setStyle(Paint.Style.FILL);
        mSmallCirclePaint.setColor(Color.RED);

        mBigCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBigCirclePaint.setStyle(Paint.Style.FILL);


        blurpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blurpaint.setStyle(Paint.Style.FILL);

        mStrokeWidth = dp2px(5);
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mStrokeWidth);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(dp2px(15));


    }

    @Override
    protected void onDraw(Canvas canvas) {


        canvas.translate(getWidth() / 2.0f, getHeight() / 2.0f);
        canvas.rotate(-180);
        if (mBigRadialGradient == null) {
            mBigRadialGradient = new RadialGradient(0, 0, getWidth()/2 - mSmallCircleRadius,
                    Color.parseColor("#FF5266"),
                    Color.parseColor("#FFAFB3"),
                    Shader.TileMode.CLAMP);
            mBigCirclePaint.setShader(mBigRadialGradient);
//            blurpaint.setShader(mBigRadialGradient);
        }

        if (mSmallRadialGradient == null) {
            float degrees = mPercent * 360;
            mCenterX = (float) (Math.cos(Math.toRadians(degrees))
                    * (getWidth() * 0.1f - mSmallCircleRadius));
            mCenterY = (float) (Math.sin(Math.toRadians(degrees))
                    * (getWidth() * 0.1f - mSmallCircleRadius));

            mSmallRadialGradient = new RadialGradient(mCenterX, mCenterY, mSmallCircleRadius,
                    Color.parseColor("#FF8B88"),
                    Color.parseColor("#FF6177"),
                    Shader.TileMode.CLAMP);

            mSmallCirclePaint.setShader(mSmallRadialGradient);
            float dis = getWidth() * 0.5f - mSmallCircleRadius;
            SweepGradient linearGradient = new SweepGradient(0, 0,
                    new int[]{
                            Color.parseColor("#FEE8A2"),
                            Color.parseColor("#4CE3B7"),
                            Color.parseColor("#2596DD"),
                            Color.parseColor("#FF2E41"),
                            Color.parseColor("#FF6C00"),
                            Color.parseColor("#FFD100"),}, null);
            mArcPaint.setShader(linearGradient);
        }


        BlurMaskFilter blurMaskFilter = new BlurMaskFilter(15,BlurMaskFilter.Blur.SOLID);


        blurpaint.setColor(Color.parseColor("#FF7583"));

        blurpaint.setMaskFilter(blurMaskFilter);

        canvas.drawCircle(0, 0,
                getWidth() * 0.50f - mSmallCircleRadius + mStrokeWidth * 0.5f, blurpaint);


        canvas.drawCircle(0, 0,
                getWidth() * 0.5f - mSmallCircleRadius + mStrokeWidth * 0.5f, mBigCirclePaint);

        float dis = getWidth() * 0.5f - mSmallCircleRadius;
        mOval.set(-dis, -dis, dis, dis);
        canvas.drawArc(mOval, 0, mPercent * 360, false, mArcPaint);

        canvas.drawCircle(mCenterX, mCenterY, mSmallCircleRadius, mSmallCirclePaint);


        String txt = String.valueOf((int) (mPercent * 100));
        canvas.save();
        canvas.rotate(180);

        mTextPaint.setTextSize(dp2px(18));

        int xOffset = (int) (mTextPaint.measureText("%") * 0.5f);

        mTextPaint.setTextSize(dp2px(30));

        float y = Math.abs(mTextPaint.getFontMetrics().ascent + mTextPaint.getFontMetrics().descent) * 0.5f;
        canvas.drawText(txt, -xOffset, y, mTextPaint);
        mTextPaint.setFakeBoldText(true);

        float m = mTextPaint.measureText(txt);
        mTextPaint.setTextSize(dp2px(18));
        mTextPaint.setFakeBoldText(false);
        canvas.drawText("%", m * 0.5f + mTextPaint.measureText("%") * 0.5f - xOffset, y, mTextPaint);

        mTextPaint.setTextSize(dp2px(13));
        float pY = Math.abs(mTextPaint.getFontMetrics().ascent + mTextPaint.getFontMetrics().descent) * 0.5f;
        canvas.drawText("怀孕几率", 0, (getHeight() >> 2) + pY, mTextPaint);


        mTextPaint.setTextSize(dp2px("今日".equals(mDateContent) ? 15 : 13));
        pY = Math.abs(mTextPaint.getFontMetrics().ascent + mTextPaint.getFontMetrics().descent);
        canvas.drawText(mDateContent, 0, (-getHeight() >> 2) + pY, mTextPaint);
        canvas.restore();

    }

    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    /**
     * 设置几率
     *
     * @param percent 0-1
     */
    public void setPercent(float percent, Date date) {
        mPercent = percent;
        mSmallRadialGradient = null;
        mDateContent = TimestampUtils.timestamp2String(date.getTime(), "MM月dd日");
        String today = TimestampUtils.timestamp2String(System.currentTimeMillis(), "MM月dd日");
        mDateContent = mDateContent.equals(today) ? "今日" : mDateContent;
        invalidate();
    }

}
