package com.healthy.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthy.library.R;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.interfaces.OnTitleListener;
import com.healthy.library.interfaces.OnTopBarListener;

/**
 * @author Li
 * @date 2019/03/04 15:16
 * @des top bar
 */

public class TopBar extends FrameLayout implements View.OnClickListener {

    private TextView mTxtTitle;
    private ImageView mImgBack;
    private AppCompatImageView mImgSubmit;
    private TextView mBackText;

    public TextView getmSubmitText() {
        return mSubmitText;
    }

    private TextView mSubmitText;
    private View dividerView;

    public View getDividerView() {
        return dividerView;
    }

    public void setTopBarListener(OnTopBarListener topBarListener) {
        mTopBarListener = topBarListener;
    }

    public void setSubmitListener(OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }

    public void setmTitleListener(OnTitleListener mTitleListener) {
        this.mTitleListener = mTitleListener;
    }

    private OnSubmitListener onSubmitListener;
    private OnTopBarListener mTopBarListener;
    private OnTitleListener mTitleListener;

    public TopBar(@NonNull Context context) {
        this(context, null);
    }

    public TopBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_top_bar, this);

        mTxtTitle = findViewById(R.id.txt_title);
        mImgBack = findViewById(R.id.img_back);
        mSubmitText = findViewById(R.id.submit);
        mImgSubmit = findViewById(R.id.submit_image);
        mBackText = findViewById(R.id.back);
        mBackText.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
        mSubmitText.setOnClickListener(this);
        mTxtTitle.setOnClickListener(this);
        mImgSubmit.setOnClickListener(this);
        dividerView = findViewById(R.id.divider);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        int color = array.getColor(R.styleable.TopBar_bg_color, Color.WHITE);
        int titleColor = array.getColor(R.styleable.TopBar_title_color, Color.BLACK);
        int submitColor = array.getColor(R.styleable.TopBar_submit_color, Color.BLACK);
        int backColor = array.getColor(R.styleable.TopBar_back_color, Color.BLACK);


        if (array.getBoolean(R.styleable.TopBar_is_show_back, true)) {
            mImgBack.setVisibility(VISIBLE);
            int backImgSrc = array.getResourceId(R.styleable.TopBar_back_img_src, R.drawable.ic_back);
            mImgBack.setImageResource(backImgSrc);
        } else {
            mImgBack.setVisibility(GONE);
        }

        if (array.getBoolean(R.styleable.TopBar_is_show_back_txt, false)) {
            mBackText.setVisibility(View.VISIBLE);
        } else {
            mBackText.setVisibility(View.GONE);
        }


        if (array.getBoolean(R.styleable.TopBar_is_show_submit, false)) {
            mSubmitText.setVisibility(VISIBLE);
        } else {
            mSubmitText.setVisibility(GONE);
        }


        if (array.getBoolean(R.styleable.TopBar_is_show_submit_image, false)) {
            int submitImgSrc = array.getResourceId(R.styleable.TopBar_submit_img_src, R.drawable.index_message_setting);
            mImgSubmit.setImageResource(submitImgSrc);
            mImgSubmit.setVisibility(VISIBLE);
        } else {
            mImgSubmit.setVisibility(GONE);
        }

        setBackgroundColor(color);
        String title = array.getString(R.styleable.TopBar_title);
        String submit = array.getString(R.styleable.TopBar_submit);
        String back = array.getString(R.styleable.TopBar_back);
        dividerView.setVisibility(array.getBoolean(R.styleable.TopBar_show_moss, true) ?
                VISIBLE : INVISIBLE);
        setTitle(title);
        setSubmit(submit);
        setBack(back);
        mTxtTitle.setTextColor(titleColor);
        mSubmitText.setTextColor(submitColor);
        mBackText.setTextColor(backColor);
        array.recycle();
    }

    public void setSubmitColor(int color) {
        mImgSubmit.setColorFilter(color);
    }

    private void setBack(String back) {
        mBackText.setText(back);

    }

    public void setSubmit(CharSequence charSequence) {
        mSubmitText.setText(charSequence);
    }

    public void setTitle(CharSequence charSequence) {
        mTxtTitle.setText(charSequence);
    }

    public void showMoss(boolean flag) {
        dividerView.setVisibility(flag ?
                VISIBLE : INVISIBLE);
        requestLayout();
    }

    public TextView getTxtTitle() {
        return mTxtTitle;
    }

    public ImageView getImgBack() {
        return mImgBack;
    }

    public ImageView getSubmitBack() {
        return mImgSubmit;
    }

    @Override
    public void onClick(View v) {
        if (mTopBarListener != null && v.getId() == R.id.img_back) {
            mTopBarListener.onBackBtnPressed();
        }
        if (mTopBarListener != null && v.getId() == R.id.back) {
            mTopBarListener.onBackBtnPressed();
        }
        if (onSubmitListener != null && v.getId() == R.id.submit) {
            onSubmitListener.onSubmitBtnPressed();
        }
        if (onSubmitListener != null && v.getId() == R.id.submit_image) {
            onSubmitListener.onSubmitBtnPressed();
        }
        if (mTitleListener != null && v.getId() == R.id.txt_title) {
            mTitleListener.onTitlePressed();
        }
    }
}
