package com.healthy.library.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.healthy.library.R;


/**
 * @author Li
 * @date 2019/06/05 14:48
 * @des
 */

public class SimpleDialog {


    public static class Builder {

        private Context mContext;

        private String mTitle;
        private String mContent;
        private String mSimpleBtn;
        private String mNegativeBtn;
        private String mPositiveBtn;

        private int mTitleColor;
        private float mTitleSize;
        private int mContentColor;
        private float mContentSize;
        private int mSimpleBtnColor;
        private float mSimpleBtnSize;

        private int mNegativeColor;
        private float mNegativeSize;

        private int mPositiveColor;
        private float mPositiveSize;

        private int mTitleGravity;
        private int mContentGravity;

        private float mCornerRadius;
        private float mWidthRatio;
        private View.OnClickListener mSimpleClick;
        private View.OnClickListener mNegativeClick;
        private View.OnClickListener mPositiveClick;

        private boolean mCancelable;
        private boolean mCanceledOnTouchOutside;
        private int mContentMarginTop;

        public Builder(Context context) {
            mContext = context;
            init();
        }


        private float dp2px(Context context, float dp) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                    context.getResources().getDisplayMetrics());
        }

        private void init() {
            mTitleColor = Color.parseColor("#222222");
            mTitleGravity = Gravity.CENTER;
            mTitleSize = dp2px(mContext, 16);

            mContentColor = Color.parseColor("#444444");
            mContentSize = dp2px(mContext, 14);
            mContentGravity = Gravity.START;

            mSimpleBtnColor = Color.parseColor("#FF2D4D");
            mSimpleBtnSize = dp2px(mContext, 16);

            mWidthRatio = 0.83f;
            mCornerRadius = dp2px(mContext, 15);

            mCancelable = true;
            mCanceledOnTouchOutside = true;

            mNegativeColor = Color.parseColor("#444444");
            mNegativeSize = dp2px(mContext, 16);

            mPositiveColor = Color.parseColor("#FF2D4D");
            mPositiveSize = dp2px(mContext, 16);

            mContentMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    17,mContext.getResources().getDisplayMetrics());
        }

        public Builder setRadius(float radius) {
            mCornerRadius = radius;
            return this;
        }

        public Builder setRadius(int unit, float value) {
            mCornerRadius = TypedValue.applyDimension(unit, value,
                    mContext.getResources().getDisplayMetrics());
            return this;
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setTitleColor(int titleColor) {
            mTitleColor = titleColor;
            return this;
        }

        public Builder setTitleSize(float titleSize) {
            mTitleSize = titleSize;
            return this;
        }

        public Builder setTitleSize(int unit, float value) {
            mTitleSize = TypedValue.applyDimension(unit, value,
                    mContext.getResources().getDisplayMetrics());
            return this;
        }

        public Builder setTitleGravity(int gravity) {
            mTitleGravity = gravity;
            return this;
        }

        public Builder setContent(String content) {
            mContent = content;
            return this;
        }

        public Builder setContentColor(int contentColor) {
            mContentColor = contentColor;
            return this;
        }

        public Builder setContentSize(float contentSize) {
            mContentSize = contentSize;
            return this;
        }

        public Builder setContentSize(int unit, float value) {
            mContentSize = TypedValue.applyDimension(unit, value,
                    mContext.getResources().getDisplayMetrics());
            return this;
        }

        public Builder setContentGravity(int contentGravity) {
            mContentGravity = contentGravity;
            return this;
        }

        public Builder setContentMarginTop(float marginTop) {
            mContentMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    marginTop,mContext.getResources().getDisplayMetrics());
            return this;
        }

        public Builder setSimpleBtn(String simpleBtn, View.OnClickListener onClickListener) {
            mSimpleBtn = simpleBtn;
            mSimpleClick = onClickListener;
            return this;
        }

        public Builder setSimpleBtnColor(int simpleBtnColor) {
            mSimpleBtnColor = simpleBtnColor;
            return this;
        }

        public Builder setSimpleBtnSize(float simpleBtnSize) {
            mSimpleBtnSize = simpleBtnSize;
            return this;
        }

        public Builder setSimpleBtnSize(int unit, float value) {
            mSimpleBtnSize = TypedValue.applyDimension(unit, value,
                    mContext.getResources().getDisplayMetrics());
            return this;
        }

        public Builder setNegativeBtn(String negativeBtn, View.OnClickListener onClickListener) {
            mNegativeBtn = negativeBtn;
            mNegativeClick = onClickListener;
            return this;
        }

        public Builder setNegativeColor(int negativeColor) {
            mNegativeColor = negativeColor;
            return this;
        }

        public Builder setNegativeSize(float negativeSize) {
            mNegativeSize = negativeSize;
            return this;
        }


        public Builder setPositiveBtn(String positiveBtn, View.OnClickListener onClickListener) {
            mPositiveBtn = positiveBtn;
            mPositiveClick = onClickListener;
            return this;
        }

        public Builder setPositiveColor(int positiveColor) {
            mPositiveColor = positiveColor;
            return this;
        }

        public Builder setPositiveSize(float positiveSize) {
            mPositiveSize = positiveSize;
            return this;
        }


        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            mCanceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public AlertDialog create() {
            final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog, null);
            dialog.setView(contentView);
            Window window = dialog.getWindow();
            if (window != null) {
                View decorView = window.getDecorView();
                decorView.setPadding(0, 0, 0, 0);
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(Color.WHITE);
                drawable.setCornerRadius(mCornerRadius);
                decorView.setBackground(drawable);
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * mWidthRatio);
                window.setAttributes(params);
            }

            TextView tvTitle = contentView.findViewById(R.id.tv_title);
            TextView tvContent = contentView.findViewById(R.id.tv_content);
            View dividerView = contentView.findViewById(R.id.view_divider);
            TextView tvSimple = contentView.findViewById(R.id.tv_simple);
            TextView tvNegative = contentView.findViewById(R.id.tv_negative);
            TextView tvPositive = contentView.findViewById(R.id.tv_positive);
            Group groupMultiple = contentView.findViewById(R.id.group_multiple);

            if (tvContent.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {
                ConstraintLayout.LayoutParams contentParams= (ConstraintLayout.LayoutParams) tvContent.getLayoutParams();
                contentParams.setMargins(contentParams.leftMargin,mContentMarginTop,
                        contentParams.rightMargin,contentParams.bottomMargin);
            }


            if (TextUtils.isEmpty(mTitle)) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(mTitle);
                tvTitle.setTextColor(mTitleColor);
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
                tvTitle.setGravity(mTitleGravity);
            }

            if (TextUtils.isEmpty(mContent)) {
                tvContent.setVisibility(View.GONE);
            } else {
                tvContent.setVisibility(View.VISIBLE);
                tvContent.setText(mContent);
                tvContent.setTextColor(mContentColor);
                tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContentSize);
                tvContent.setGravity(mContentGravity);
            }

            if (TextUtils.isEmpty(mSimpleBtn) && TextUtils.isEmpty(mNegativeBtn) && TextUtils.isEmpty(mPositiveBtn)) {
                dividerView.setVisibility(View.GONE);
                groupMultiple.setVisibility(View.GONE);

            } else if (!TextUtils.isEmpty(mSimpleBtn)) {
                dividerView.setVisibility(View.VISIBLE);
                groupMultiple.setVisibility(View.GONE);

                tvSimple.setText(mSimpleBtn);
                tvSimple.setTextColor(mSimpleBtnColor);
                tvSimple.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSimpleBtnSize);
                tvSimple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mSimpleClick != null) {
                            mSimpleClick.onClick(v);
                        }
                        dialog.dismiss();
                    }
                });
            } else if (!TextUtils.isEmpty(mNegativeBtn) && !TextUtils.isEmpty(mPositiveBtn)) {
                dividerView.setVisibility(View.VISIBLE);
                tvSimple.setVisibility(View.GONE);
                groupMultiple.setVisibility(View.VISIBLE);

                tvNegative.setText(mNegativeBtn);
                tvNegative.setTextColor(mNegativeColor);
                tvNegative.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNegativeSize);
                tvNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mNegativeClick != null) {
                            mNegativeClick.onClick(v);
                        }
                        dialog.dismiss();
                    }
                });

                tvPositive.setText(mPositiveBtn);
                tvPositive.setTextColor(mPositiveColor);
                tvPositive.setTextSize(TypedValue.COMPLEX_UNIT_PX, mPositiveSize);
                tvPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mPositiveClick != null) {
                            mPositiveClick.onClick(v);
                        }
                        dialog.dismiss();
                    }
                });

            }
            dialog.setCancelable(mCancelable);
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            return dialog;
        }
    }
}