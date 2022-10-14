package com.healthy.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.healthy.library.R;

/**
 * @author Li
 * @date 2019/03/05 14:11
 * @des 通用section
 */

public class CommonSection extends ConstraintLayout {

    private TextView mTxtTitle;
    private TextView mTxtContent;
    private EditText mEtContent;

    private static final int MODE_TXT = 1;
    private static final int MODE_ET = 2;
    private static final int MODE_NONE = 3;
    private TextView.OnEditorActionListener onEditorActionListener;
    private int imeOptions;

    public CommonSection(@NonNull Context context) {
        this(context, null);
    }

    public CommonSection(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public void setOnEditorActionListener(TextView.OnEditorActionListener onEditorActionListener){
        mEtContent.setSingleLine(true);
        mEtContent.setOnEditorActionListener(onEditorActionListener);
    }
    public void setImeOptions(int imeOptions){
        mEtContent.setSingleLine(true);
        mEtContent.setImeOptions(imeOptions);
    }

    public CommonSection(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_section, this);
        mTxtTitle = findViewById(R.id.txt_title);
        mTxtContent = findViewById(R.id.txt_content);
        mEtContent = findViewById(R.id.et_content);

        mEtContent.setSingleLine();
//        mEtContent.setOnEditorActionListener(onEditorActionListener);
//        mEtContent.setImeOptions(imeOptions);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CommonSection);

        boolean showDivider = array.getBoolean(R.styleable.CommonSection_show_divider, true);
        mTxtTitle.setText(array.getString(R.styleable.CommonSection_title));
        mTxtTitle.setCompoundDrawablesWithIntrinsicBounds(array.getDrawable(
                R.styleable.CommonSection_title_drawable_start), null, null, null);
        mTxtTitle.setCompoundDrawablePadding(array.getDimensionPixelSize(
                R.styleable.CommonSection_title_drawable_padding, 5
        ));


        int mode = array.getInteger(R.styleable.CommonSection_section_mode, MODE_NONE);
        if (mode == MODE_TXT) {
            mTxtContent.setVisibility(VISIBLE);
            mEtContent.setVisibility(GONE);
        } else if (mode == MODE_ET) {
            mTxtContent.setVisibility(GONE);
            mEtContent.setVisibility(VISIBLE);
        } else {
            mTxtContent.setVisibility(GONE);
            mEtContent.setVisibility(GONE);
        }


        String txtContentHint = array.getString(R.styleable.CommonSection_txt_content_hint);
        String etContentHint = array.getString(R.styleable.CommonSection_et_content_hint);
        String txtContent = array.getString(R.styleable.CommonSection_txt_content);
        String etContent = array.getString(R.styleable.CommonSection_et_content);
        Drawable txtContentDrawableRight = array
                .getDrawable(R.styleable.CommonSection_txt_content_drawable_right);
        Drawable etContentDrawableRight = array
                .getDrawable(R.styleable.CommonSection_et_content_drawable_right);

        if (!TextUtils.isEmpty(txtContentHint)) {
            mTxtContent.setHint(txtContentHint);
        }
        if (!TextUtils.isEmpty(txtContent)) {
            mTxtContent.setText(txtContent);
        }
        if (txtContentDrawableRight != null) {
            mTxtContent.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    txtContentDrawableRight, null);
        }


        if (!TextUtils.isEmpty(etContentHint)) {
            mEtContent.setHint(etContentHint);
        }
        if (!TextUtils.isEmpty(etContent)) {
            mEtContent.setText(etContent);
        }
        if (etContentDrawableRight != null) {
            mEtContent.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    etContentDrawableRight, null);
        }

        array.recycle();

        if (showDivider) {
            setBackgroundResource(R.drawable.layer_list_section_bg);
        }
    }

    public TextView getTxtContent() {
        return mTxtContent;
    }

    public EditText getEtContent() {
        return mEtContent;
    }
}
