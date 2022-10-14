//package com.healthy.library.widget;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import android.text.Editable;
//import android.text.InputFilter;
//import android.text.TextWatcher;
//import android.util.AttributeSet;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.healthy.library.R;
//import com.healthy.library.utils.EditTextUtils;
//import com.healthy.library.utils.TransformUtil;
//
///**
// * Author: Li
// * Date: 2018/10/10 0010
// * Description:
// */
//public class CodeView extends FrameLayout implements TextWatcher {
//
//    private int mLength = 8;
//    private LinearLayout mLayoutCode;
//    private TextWatcher mTextWatcher;
//    private EditText mEditText;
//
//    public CodeView(@NonNull Context context) {
//        this(context, null);
//    }
//
//    public CodeView(@NonNull Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public CodeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        View view = LayoutInflater.from(context).inflate(R.layout.layout_code, this);
//        mLayoutCode = view.findViewById(R.id.layout_code);
//        mLayoutCode.removeAllViews();
//        mLayoutCode.setWeightSum(mLength);
//        mEditText = view.findViewById(R.id.et_reference);
//        mEditText.addTextChangedListener(this);
//        EditTextUtils.shieldOperation(mEditText);
//        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mLength)});
//        int margin = (int) TransformUtil.dp2px(context, 3);
//        int padding = (int) TransformUtil.dp2px(context, 5);
//        for (int i = 0; i < mLength; i++) {
//            TextView textView = new TextView(context);
//            textView.setGravity(Gravity.CENTER);
//            textView.setTextSize(32);
//            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//            textView.setTextColor(Color.parseColor("#FF605F"));
//            textView.setPadding(0, padding, 0, padding);
//            textView.setBackground(context.getResources().getDrawable(R.drawable.selector_code));
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    0, ViewGroup.LayoutParams.MATCH_PARENT,
//                    1.0f);
//            params.setMargins(margin, margin, margin, margin);
//            mLayoutCode.addView(textView, params);
//        }
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        for (int i = 0; i < mLayoutCode.getChildCount(); i++) {
//            TextView textView = (TextView) mLayoutCode.getChildAt(i);
//            if (i < s.length()) {
//                textView.setText(String.valueOf(s.charAt(i)));
//                textView.setSelected(true);
//            } else {
//                textView.setText("");
//                textView.setSelected(false);
//            }
//        }
//        if (mTextWatcher != null) {
//            mTextWatcher.onTextChanged(s, start, before, count);
//        }
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//
//    }
//
//    public void setTextWatcher(TextWatcher textWatcher) {
//        mTextWatcher = textWatcher;
//    }
//
//    public int getLength() {
//        return mLength;
//    }
//
//    public CharSequence getContent() {
//        return mEditText.getText();
//    }
//    public int getContentLength(){
//        return getContent().length();
//    }
//
//    public interface TextWatcher {
//        void onTextChanged(CharSequence s, int start, int before, int count);
//    }
//}
