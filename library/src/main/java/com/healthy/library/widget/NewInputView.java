package com.healthy.library.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.healthy.library.R;

/**
 * @author: long
 * @date: 2021/5/10
 * @des
 */
public class NewInputView extends ConstraintLayout {

    private int mRightSrc;
    private String mTitle;
    private int mTitleColor;
    private String mRightBody;
    private String mRightEditBody;
    private int mRightColor;
    private boolean isEditModel;
    private AppCompatTextView mTitleView;
    private AppCompatEditText mRightEditText;
    private AppCompatTextView mRightTextView;
    private AppCompatImageView mRightImageView;
    private Context mContext;
    private String mRightHint;
    private int mWidgetInputBg;
    private boolean isNotBg;
    private int mViewBackgroundColor;
    private float mHeight;

    public NewInputView(Context context) {
        this(context, null);
        mContext = context;
    }

    public NewInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public NewInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Resources resources = context.getResources();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NewInputView);

        mRightSrc = array.getResourceId(R.styleable.NewInputView_newInput_right_src, R.drawable.mine_ic_more);
        mHeight = array.getDimension(R.styleable.NewInputView_newInput_height, 0);
        mTitle = array.getString(R.styleable.NewInputView_newInput_title);
        mTitleColor = array.getColor(R.styleable.NewInputView_newInput_title_color, resources.getColor(R.color.color_333333));
        mRightBody = array.getString(R.styleable.NewInputView_newInput_right);
        mRightHint = array.getString(R.styleable.NewInputView_newInput_right_hint);
        mRightEditBody = array.getString(R.styleable.NewInputView_newInput_right_edit);
        mRightColor = array.getColor(R.styleable.NewInputView_newInput_right_color, resources.getColor(R.color.color_999999));
        isEditModel = array.getBoolean(R.styleable.NewInputView_newInput_isEdit, false);
        isNotBg = array.getBoolean(R.styleable.NewInputView_newInput_isNot_bg, false);
        mViewBackgroundColor = array.getColor(R.styleable.NewInputView_newInput_bgColor, resources.getColor(R.color.white));

        View inflate = LayoutInflater.from(context).inflate(R.layout.widget_new_input_layout, this);

        ConstraintLayout mClContent = inflate.findViewById(R.id.widget_new_input_content);
        mTitleView = inflate.findViewById(R.id.widget_new_input_title);
        mRightEditText = inflate.findViewById(R.id.widget_new_input_EditText);
        mRightTextView = inflate.findViewById(R.id.widget_new_input_right);
        mRightImageView = inflate.findViewById(R.id.widget_new_input_end);
        View mNewInputLine = inflate.findViewById(R.id.widget_new_input_line);

        if (mHeight > 0) {
            ViewGroup.LayoutParams layoutParams = mClContent.getLayoutParams();
            layoutParams.height = (int) mHeight;
            mClContent.setLayoutParams(layoutParams);
        }

        if (isNotBg) {
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                setBackgroundDrawable(null);
            } else {
                setBackground(null);
            }
            ConstraintLayout.LayoutParams layoutParams = (LayoutParams) mTitleView.getLayoutParams();
            layoutParams.leftMargin = 0;
            mTitleView.setLayoutParams(layoutParams);
            setBackgroundColor(mViewBackgroundColor);
        } else {
            setBackground(resources.getDrawable(R.drawable.shape_widget_new_input_bg));
        }
        mRightImageView.setImageResource(mRightSrc);
        mNewInputLine.setVisibility(isNotBg ? VISIBLE : GONE);
        setModel(isEditModel);
        setTitle(mTitle);
        setTitleColor(mTitleColor);
        setRightEditHint(mRightHint);
        setRightEditBody(mRightEditBody);
        setRightBody(mRightBody);
        setRightColor(mRightColor);
        setInputType(4);
    }

    /**
     * 设置EditText输入类型
     *
     * @param inputType 1 == 数字 2 == 手机号 3 == 文本密码 4 == 纯文本
     */
    public void setInputType(int inputType) {
        EditText editText = getEditText();
        if (editText != null) {
            if (1 == inputType || 2 == inputType) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            if (3 == inputType) {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            if (4 == inputType) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        }
    }

    /**
     * 设置是否为EditText样式模式
     *
     * @param isEditModel
     */
    public void setModel(boolean isEditModel) {
        this.isEditModel = isEditModel;
        mRightEditText.setVisibility(isEditModel ? VISIBLE : GONE);
        int mHindImg;
        int mShowImg;
        if (isNotBg) {
            mHindImg = GONE;
            mShowImg = GONE;
        } else {
            mHindImg = INVISIBLE;
            mShowImg = VISIBLE;
        }
        mRightImageView.setVisibility(isEditModel ? mHindImg : mShowImg);
        mRightTextView.setVisibility(isEditModel ? GONE : VISIBLE);
    }

    /**
     * 获取输入控件
     *
     * @return
     */
    public EditText getEditText() {
        return mRightEditText;
    }

    public ImageView getRightImageView() {
        return mRightImageView;
    }


    public AppCompatTextView getRightTextView() {
        return mRightTextView;
    }

    /**
     * 设置左侧标题
     *
     * @param title
     */
    public void setTitle(String title) {
        this.mTitle = title;
        if (mTitleView != null) {
            mTitleView.setText(mTitle);
        }
    }

    /**
     * 设置标题文字颜色
     *
     * @param color
     */
    public void setTitleColor(@ColorInt int color) {
        this.mTitleColor = color;
        if (mTitleView != null) {
            mTitleView.setTextColor(color);
        }
    }

    /**
     * 设置右侧内容
     *
     * @param rightBody
     */
    public void setRightBody(String rightBody) {
        this.mRightBody = rightBody;
        if (mRightTextView != null) {
            mRightTextView.setText(mRightBody);
        }
    }

    /**
     * 设置右侧输入框内容
     *
     * @param rightEditBody
     */
    public void setRightEditBody(String rightEditBody) {
        this.mRightEditBody = rightEditBody;
        if (mRightEditText != null) {
            mRightEditText.setText(mRightEditBody);
            //光标移动到文字最后
            mRightEditText.setSelection(TextUtils.isEmpty(mRightEditBody) ? 0 : mRightEditBody.length());
        }
    }

    /**
     * 设置右侧内容
     *
     * @param rightHint
     */
    public void setRightEditHint(String rightHint) {
        this.mRightHint = rightHint;
        if (mRightEditText != null) {
            mRightEditText.setHint(mRightHint);
        }
    }


    /**
     * 设置右侧文字颜色
     *
     * @param color
     */
    public void setRightColor(@ColorInt int color) {
        this.mRightColor = color;
        if (mRightTextView != null) {
            mRightTextView.setTextColor(mRightColor);
        }
    }

}
