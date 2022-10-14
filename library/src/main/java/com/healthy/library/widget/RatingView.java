package com.healthy.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.healthy.library.R;

/**
 * @author Li
 * @date 2019/03/12 14:51
 * @des 评分控件
 */

public class RatingView extends View {

    private static final int DEFAULT_NUM_STARS = 5;
    private static final int DEFAULT_HEIGHT = 15;
    private static final int DEFAULT_RADIUS = 5;
    private static final int DEFAULT_PADDING = 10;
    private static final float DEFAULT_INNER_PADDING = 5;
    private static final int DEFAULT_MIN_RATING = 0;
    private static final int DEFAULT_MAX_RATING = 5;


    private Paint mLeftPaint;
    private Paint mRightPaint;
    private Paint mStarPaint;
    private Matrix mMatrix;
    private RectF mLeftRectF;
    private RectF mRightRectF;
    private PorterDuffXfermode mXfermode;
    private Paint mCirclePaint;

    private Bitmap mBitmap;
    private int mNumStars;
    private boolean mIsIndicator;
    private float mRating;
    private float mRadius;
    private float mPadding;
    private float mInnerPadding;
    private int mMinRating;
    private int mMaxRating;
    private float mRatingStep = 1.0f;

    private float mHeight;
    private RectF mRoundRectF;
    private RectF mLayerRectF;
    private OnRatingChangedListener mRatingChangedListener;

    public RatingView(Context context) {
        this(context, null);
    }

    public RatingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public RatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRoundRectF = new RectF();
        mLayerRectF = new RectF();

        mHeight = dp2px(DEFAULT_HEIGHT, context);

        mLeftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLeftPaint.setColor(Color.parseColor("#ff6266"));
        mLeftPaint.setStyle(Paint.Style.FILL);

        mStarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mRightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRightPaint.setColor(Color.parseColor("#E0E0E6"));
        mRightPaint.setStyle(Paint.Style.FILL);

        mBitmap = BitmapFactory.decodeResource(getResources(), 1);
        mMatrix = new Matrix();

        mLeftRectF = new RectF();
        mRightRectF = new RectF();
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.WHITE);
        mRadius = dp2px(5, context);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatingView);
        mBitmap = BitmapFactory.decodeResource(getResources(),
                array.getResourceId(R.styleable.RatingView_rating_res, R.drawable.ic_default_rating));
        mNumStars = array.getInteger(R.styleable.RatingView_rating_num, DEFAULT_NUM_STARS);
        mIsIndicator = array.getBoolean(R.styleable.RatingView_is_indicator, true);
        mRating = array.getFloat(R.styleable.RatingView_rating, 0);
        mRadius = array.getDimensionPixelSize(R.styleable.RatingView_rating_radius, DEFAULT_RADIUS);
        mPadding = array.getDimensionPixelSize(R.styleable.RatingView_rating_padding, DEFAULT_PADDING);
        mInnerPadding = array.getDimensionPixelSize(R.styleable.RatingView_rating_inner_padding,
                (int) DEFAULT_INNER_PADDING);
        mMinRating = array.getInteger(R.styleable.RatingView_rating_min, DEFAULT_MIN_RATING);
        mMaxRating = array.getInteger(R.styleable.RatingView_rating_max, DEFAULT_MAX_RATING);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height;
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                height = (int) mHeight;
                break;
            case MeasureSpec.EXACTLY:
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
            default:
                height = 0;
                break;
        }
        int width = (int) (height * mNumStars + mPadding * (mNumStars - 1));
        setMeasuredDimension(width, height);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        mLayerRectF.set(0, 0, getWidth(), getHeight());

        for (int i = 0; i < mNumStars; i++) {
            canvas.save();
            int storeCount = canvas.saveLayerAlpha(mLayerRectF, 255, Canvas.ALL_SAVE_FLAG);

            float offset = i + 1;
            float ratio;
            float tmpRating = mRating / ((mMaxRating - mMinRating) / mNumStars);
            if (tmpRating >= offset) {
                ratio = 1;
            } else if (tmpRating > offset - 1 && tmpRating < offset) {
                ratio = (tmpRating + 1) % offset;
            } else {
                ratio = 0;
            }
            float left = i * mPadding + i * getHeight();
            float top = 0;
            float right = left + getHeight();
            float bottom = top + getHeight();
            mRoundRectF.set(left, top, right, bottom);
            canvas.drawRoundRect(mRoundRectF, mRadius, mRadius, mCirclePaint);
            mCirclePaint.setXfermode(mXfermode);

            Bitmap starBitmap = starBitmap(ratio);
            canvas.drawBitmap(starBitmap, i * starBitmap.getWidth() + (i) * mPadding,
                    0, mCirclePaint);

            mCirclePaint.setXfermode(null);
            canvas.restoreToCount(storeCount);
        }

    }

    private Bitmap starBitmap(float ratio) {
        Bitmap bitmap = Bitmap.createBitmap(getHeight(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mLeftRectF.set(0, 0, getHeight() * ratio, getHeight());
        mRightRectF.set(getHeight() * ratio, 0, getHeight(), getBottom());

        canvas.drawRect(mLeftRectF, mLeftPaint);
        canvas.drawRect(mRightRectF, mRightPaint);


        float scale = (getHeight() - mInnerPadding * 2) / mBitmap.getHeight();
        mMatrix.setScale(scale, scale);

        mMatrix.postTranslate(mInnerPadding, mInnerPadding);
        canvas.drawBitmap(mBitmap, mMatrix, mStarPaint);
        return bitmap;
    }

    private float dp2px(float dp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

    public void setRating(float rating) {
        mRating = rating;
        invalidate();
    }

    public float getRating() {
        return mRating;
    }

    private float fixRating(float rating) {
        return Math.max(mMinRating, Math.min(rating, mMaxRating));
    }

    private float getRatingScale() {
        return mRating / (mMaxRating - mMinRating);
    }

    public void setRatingChangedListener(OnRatingChangedListener ratingChangedListener) {
        mRatingChangedListener = ratingChangedListener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsIndicator) {
            return super.onTouchEvent(event);
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    float x = event.getX();
                    float rating = x / getWidth() * (mMaxRating - mMinRating);
                    float mod = rating % mRatingStep;
                    if (mod > mRatingStep * 0.5f) {
                        rating = (int) (rating / mRatingStep) + mRatingStep;
                    } else {
                        rating = (int) (rating / mRatingStep);
                    }
                    setRating(fixRating(rating));
                    break;
                case MotionEvent.ACTION_UP:
                    if (mRatingChangedListener != null) {
                        mRatingChangedListener.onRatingChanged(this, getRatingScale());
                        mRatingChangedListener.onGetRating(this, mRating);
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    public interface OnRatingChangedListener {
        /**
         * 评分发生变化时
         *
         * @param ratingView rating
         * @param scale      评分占比0-1
         */
        void onRatingChanged(RatingView ratingView, float scale);

        /**
         * 获取评分
         *
         * @param ratingView rating
         * @param rating     评分
         */
        void onGetRating(RatingView ratingView, float rating);
    }
}