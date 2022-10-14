package com.healthy.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.healthy.library.R;

/**
 * @author Li
 * @date 2019/03/05 14:11
 * @des 通用section
 */

public class CommonInsertSection extends ConstraintLayout {

    public TextView getmTxtTitle() {
        return mTxtTitle;
    }

    private TextView mTxtTitle;
    private TextView mTxtContent;
    private EditText mEtContent;

    public View getRightEnd() {
        return rightend;
    }

    private View rightend;
    private View rightend2;
    private LinearLayout end_ll;
    private LinearLayout end_ll2;
    private ConstraintLayout end_ll_bg;

    private static final int MODE_TXT = 1;
    private static final int MODE_ET = 2;
    private static final int MODE_NONE = 3;
    private TextView.OnEditorActionListener onEditorActionListener;
    private int imeOptions;

    public CommonInsertSection(@NonNull Context context) {
        this(context, null);
    }

    public CommonInsertSection(@NonNull Context context, @Nullable AttributeSet attrs) {
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

    public CommonInsertSection(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_insert_section, this);
        mTxtTitle = findViewById(R.id.txt_title);
        mTxtContent = findViewById(R.id.txt_content);
        mEtContent = findViewById(R.id.et_content);
        end_ll_bg=findViewById(R.id.rightLayout);
        end_ll=findViewById(R.id.end_ll);
        end_ll2=findViewById(R.id.end_ll2);
        mEtContent.setSingleLine();
        mEtContent.setOnEditorActionListener(onEditorActionListener);
        mEtContent.setImeOptions(imeOptions);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CommonInsertSection);
        int rightendres=array.getResourceId(R.styleable.CommonInsertSection_right_end, -1);
        int rightendres2=array.getResourceId(R.styleable.CommonInsertSection_right_end2, -1);
        int rightendresBg=array.getResourceId(R.styleable.CommonInsertSection_right_end_bg, -1);



        boolean showDivider = array.getBoolean(R.styleable.CommonInsertSection_show_divider, true);
        boolean showDividerFull = array.getBoolean(R.styleable.CommonInsertSection_section_divider_full_width, false);


        int txtContentColor = array.getColor(R.styleable.CommonInsertSection_txt_content_color, Color.BLACK);
        int titleContentColor = array.getColor(R.styleable.CommonInsertSection_title_color, Color.BLACK);

        mTxtTitle.setText(array.getString(R.styleable.CommonInsertSection_title));
        mTxtTitle.setCompoundDrawablesWithIntrinsicBounds(array.getDrawable(
                R.styleable.CommonInsertSection_title_drawable_start), null, null, null);
        mTxtTitle.setCompoundDrawablePadding(array.getDimensionPixelSize(
                R.styleable.CommonInsertSection_title_drawable_padding, 5
        ));
        //System.out.println("看一下获得的参数:"+rightendres);
        if(rightendres!=-1){
            end_ll.setVisibility(View.VISIBLE);
            end_ll.removeAllViews();
            rightend=LayoutInflater.from(context).inflate(rightendres, end_ll,false);
            end_ll.addView(rightend);
        }else {
            end_ll.setVisibility(View.INVISIBLE);
        }

        if(rightendres2!=-1){
            end_ll2.setVisibility(View.VISIBLE);
            end_ll2.removeAllViews();
            rightend2=LayoutInflater.from(context).inflate(rightendres2, end_ll2,false);
            end_ll2.addView(rightend2);
        }else {
            end_ll2.setVisibility(View.GONE);
        }

        if(rightendresBg!=-1){
            end_ll_bg.setBackgroundResource(rightendresBg);
        }
        int mode = array.getInteger(R.styleable.CommonInsertSection_section_mode, MODE_NONE);
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
        mTxtContent.setTextColor(txtContentColor);
        mTxtTitle.setTextColor(titleContentColor);

        String txtContentHint = array.getString(R.styleable.CommonInsertSection_txt_content_hint);
        String etContentHint = array.getString(R.styleable.CommonInsertSection_et_content_hint);
        String txtContent = array.getString(R.styleable.CommonInsertSection_txt_content);
        String etContent = array.getString(R.styleable.CommonInsertSection_et_content);
        Drawable txtContentDrawableRight = array
                .getDrawable(R.styleable.CommonInsertSection_txt_content_drawable_right);
        Drawable etContentDrawableRight = array
                .getDrawable(R.styleable.CommonInsertSection_et_content_drawable_right);

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
            if(showDividerFull){
                setBackgroundResource(R.drawable.layer_list_section_bg);
            }else {
                setBackgroundResource(R.drawable.layer_list_section_bg_nofull);
            }
        }
    }

    public TextView getTxtContent() {
        return mTxtContent;
    }

    public EditText getEtContent() {
        return mEtContent;
    }
}
