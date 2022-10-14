package com.healthy.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.healthy.library.R;
import com.healthy.library.utils.ParseUtils;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Li
 * @date 2019/03/06 15:11
 * @des 增减控件
 */

public class IncreaseDecreaseView extends FrameLayout implements View.OnClickListener, TextWatcher {

    private ImageView mImgDecrease;
    private ImageView mImgIncrease;
    private TextView mTxtNum;
    boolean needListener = true;
    boolean checkTextDialog = false;

    public void setNeedToastAndButtonEnable(boolean needToastAndButtonEnable) {
        this.needToastAndButtonEnable = needToastAndButtonEnable;
    }

    boolean needToastAndButtonEnable = true;//是否需要在置灰状态可以点击 有上下限提示

    public void setCheckTextDialog(boolean checkTextDialog) {
        this.checkTextDialog = checkTextDialog;
    }

    //顺序为 setMaxCount setMinCount setNoCount
    public void setMinCount(int minCount) {
        if(minCount>maxCount&&maxCount!=-1){
            minCount=maxCount;
        }
        this.minCount = minCount<0?0:minCount;
        needListener = false;
        initButtonStatus();
        needListener = true;
    }
    //顺序为 setMaxCount setMinCount setNoCount
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        needListener = false;
        initButtonStatus();
        needListener = true;
//        //System.out.println("重置"+needListener);
    }

    //顺序为 setMaxCount setMinCount setNoCount
    public void setNoCount(int result) {
        if(result<0){
            result=0;
        }
        if(result>maxCount&&maxCount!=-1){
            System.out.println("当前设置的数量大于最大可购:"+result+":"+maxCount);
            result=maxCount;
            if(maxCount<=0){
                result=1;
            }
        }else if(result<minCount&&minCount>0){
            System.out.println("当前设置的数量小于最小起购:"+result+":"+minCount);
            result=minCount;
        }
        needListener = false;
        mTxtNum.setText(result + "");
        System.out.println("选择器最中"+result);
        needListener = true;
        if(maxCount==1){
            setAddSubVisable(false);
        }else {
            setAddSubVisable(true);

        }
//        //System.out.println("重置"+needListener);
    }

    public int minCount;
    public int maxCount;
    private OnNumChangedListener mOnNumChangedListener;

    public void setOnNumClickListener(OnNumClickListener onNumClickListener) {
        this.onNumClickListener = onNumClickListener;
    }

    private OnNumClickListener onNumClickListener;

    public IncreaseDecreaseView(@NonNull Context context) {
        this(context, null);
    }

    public IncreaseDecreaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setChangeVisable(boolean flag) {
        mImgDecrease.setEnabled(flag ? true: false);
        mImgIncrease.setEnabled(flag ? true : false);
        mTxtNum.setEnabled(flag ? true : false);
    }

    public void setAddSubVisable(boolean flag) {
//        mImgDecrease.setVisibility(flag ? View.VISIBLE: View.INVISIBLE);
//        mImgIncrease.setVisibility(flag ? View.VISIBLE: View.INVISIBLE);
    }

    public IncreaseDecreaseView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_increase_decrease, this);
        mImgDecrease = findViewById(R.id.img_decrease);
        mTxtNum = findViewById(R.id.txt_num);
        mImgIncrease = findViewById(R.id.img_increase);

        mImgDecrease.setOnClickListener(this);
        mTxtNum.addTextChangedListener(this);
        mImgIncrease.setOnClickListener(this);
        mTxtNum.setOnClickListener(this);


        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.IncreaseDecreaseView);
        minCount = array.getInteger(R.styleable.IncreaseDecreaseView_min_count, 0);
        maxCount = array.getInteger(R.styleable.IncreaseDecreaseView_max_count, -1);
        Drawable decreaseDrawable = array.getDrawable(R.styleable.IncreaseDecreaseView_decrease_src);
        Drawable decreaseDisableDrawable = array.getDrawable(
                R.styleable.IncreaseDecreaseView_decrease_disable_src);
        Drawable increaseDrawable = array.getDrawable(R.styleable.IncreaseDecreaseView_increase_src);
        Drawable increaseDisableDrawable = array.getDrawable(
                R.styleable.IncreaseDecreaseView_increase_disable_src);
        array.recycle();

        decreaseDrawable = decreaseDrawable == null ?
                ContextCompat.getDrawable(context, R.drawable.mall_ic_decrease) : decreaseDrawable;
        decreaseDisableDrawable = decreaseDisableDrawable == null ?
                ContextCompat.getDrawable(context, R.drawable.mall_ic_decrease_disabled) : decreaseDisableDrawable;
        increaseDrawable = increaseDrawable == null ?
                ContextCompat.getDrawable(context, R.drawable.mall_ic_increase) : increaseDrawable;
        increaseDisableDrawable = increaseDisableDrawable == null ? ContextCompat.getDrawable(context, R.drawable.mall_ic_increase_disabled) : increaseDisableDrawable;


        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(new int[]{android.R.attr.state_selected}, decreaseDisableDrawable);
        listDrawable.addState(new int[]{}, decreaseDrawable);


        StateListDrawable listDrawableadd = new StateListDrawable();
        listDrawableadd.addState(new int[]{android.R.attr.state_selected}, increaseDisableDrawable);
        listDrawableadd.addState(new int[]{}, increaseDrawable);

        mImgIncrease.setImageDrawable(listDrawableadd);
        mImgDecrease.setImageDrawable(listDrawable);

        mTxtNum.setText(String.valueOf("1"));

        initButtonStatus();

    }

    public void initButtonStatus() {
        int num = ParseUtils.parseInt(mTxtNum.getText().toString());
        mImgIncrease.setSelected(true);
        mImgIncrease.setClickable(true);
        mImgDecrease.setSelected(true);
        mImgDecrease.setClickable(true);
        if (num > minCount) {
//            //System.out.println("初始化设置数量大于最小数量");
            if (maxCount != -1 && num >= maxCount) {
                mImgIncrease.setSelected(false);

                if (!needToastAndButtonEnable) {
                    System.out.println("选择器不可按1");
                    mImgIncrease.setClickable(false);
                }
            } else {
                mImgIncrease.setSelected(true);

                if (!needToastAndButtonEnable) {
                    mImgIncrease.setClickable(true);
                }
            }
            if (!mImgDecrease.isClickable()) {//
                {
//                    //System.out.println("初始化设置数量大于最小数量2");
                    mImgDecrease.setSelected(true);

                    if (!needToastAndButtonEnable) {
                        mImgDecrease.setClickable(true);
                    }
                }
            }
        } else {
            if (maxCount != -1 && num >= maxCount) {
                mImgIncrease.setSelected(false);

                if (!needToastAndButtonEnable) {
                    System.out.println("选择器不可按2");
                    mImgIncrease.setClickable(false);
                }
            }
            mImgDecrease.setSelected(false);

            if (!needToastAndButtonEnable) {
                System.out.println("选择器不可按3");
                mImgDecrease.setClickable(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_num) {
            if (!checkTextDialog) {
                return;
            }
            StyledDialog.init(getContext());
            StyledDialog.buildMdInput("修改数量", "输入数量", "",
                    mTxtNum.getText().toString(), "", new InputFilter[]{
                            new InputFilter() {
                                @Override
                                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                                if(source.toString().getBytes().length>1){//是中文
//                                    return null;
//                                }
//                                if(source.toString().equals("_")||source.toString().equals("-")) {
//                                    return null;
//                                }
                                    //[\u4E00-\u9FA5]|[\\w]|[_\\\-]
                                    String regEx = "[0-9]";
                                    Pattern p = Pattern.compile(regEx);
                                    Matcher m = p.matcher(source);
                                    if (m.find()) {
                                        return null;
                                    }
                                    return "";
                                }
                            }}, null, new MyDialogListener() {
                        @Override
                        public void onFirst() {

                        }

                        @Override
                        public void onSecond() {

                        }

                        @Override
                        public boolean onInputValid(CharSequence input1, CharSequence input2, EditText editText1, EditText editText2) {
                            int now = 0;
                            try {
                                now = Integer.parseInt(input1.toString());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            if (now < minCount) {
                                showToast("请输入大于等于" + minCount + "的整数");
                                return false;
                            }
                            if (now > maxCount && maxCount != -1) {
                                showToast("请输入小于等于" + maxCount + "的整数");
                                return false;
                            }
                            mTxtNum.setText(now + "");
                            return true;
                        }

                        @Override
                        public void onGetInput(CharSequence input1, CharSequence input2) {
                            super.onGetInput(input1, input2);
                        }
                    })
                    .setInput2HideAsPassword(true)
                    .setCancelable(true, true)
                    .show();
        } else {

            try {
                int count = Integer.parseInt(mTxtNum.getText().toString());
                count += v == mImgDecrease ? -1 : 1;
                checkToastOrOnlyClick(count,v);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
    private static Toast toast;
    public void showToast(String result){
        try {
            if (toast == null) {//toast 是否为null
                //创建一个toast
                toast = Toast.makeText(getContext(), result, Toast.LENGTH_SHORT);
            } else {
                //直接setText
                toast.setText(result);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkToastOrOnlyClick(int count,View v) {
        if(!needToastAndButtonEnable){
            mTxtNum.setText(String.valueOf(count));
            if (onNumClickListener != null) {
                if (v == mImgDecrease) {
                    onNumClickListener.onsub();
                } else {
                    onNumClickListener.onadd();
                }
            }
            return;
        }
        if(maxCount>=minCount&&maxCount-minCount==0){//非常特殊得情况
            if(v.getId()==R.id.img_increase){
                showToast(TextUtils.isEmpty(limitMaxString)?"达到可选上限":limitMaxString);
            }else {
                showToast(TextUtils.isEmpty(limitMinString)?"达到可选下限":limitMinString);
            }
            return;
        }
        if (count < minCount&&v.getId()!=R.id.img_increase) {
            showToast(TextUtils.isEmpty(limitMinString)?"达到可选下限":limitMinString);
        } else {
            if (maxCount != -1 && count > maxCount&&v.getId()==R.id.img_increase) {
                showToast(TextUtils.isEmpty(limitMaxString)?"达到可选上限":limitMaxString);
            } else {
                mTxtNum.setText(String.valueOf(count));
                if (onNumClickListener != null) {
                    if (v == mImgDecrease) {
                        onNumClickListener.onsub();
                    } else {
                        onNumClickListener.onadd();
                    }
                }
            }
        }
    }

    public String limitMaxString;
    public String limitMinString;

    public void setLimitMaxString(String limitMaxString) {
        this.limitMaxString = limitMaxString;
    }

    public void setLimitMinString(String limitMinString) {
        this.limitMinString = limitMinString;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            int num = ParseUtils.parseInt(String.valueOf(s));
            System.out.println("选择器最设置"+num);
            mImgIncrease.setSelected(true);
            mImgIncrease.setClickable(true);
            mImgDecrease.setSelected(true);
            mImgDecrease.setClickable(true);
            if (mOnNumChangedListener != null && needListener) {//要先回调 再设置下面的拦截
                mOnNumChangedListener.onNumChanged(num);
            }
            if(maxCount>=minCount&&maxCount-minCount==0){//非常特殊得情况
                if(maxCount==1){
                    mImgIncrease.setSelected(false);
                    mImgDecrease.setSelected(false);
                }else {
                    mImgIncrease.setSelected(false);
                    mImgIncrease.setClickable(false);
                    mImgDecrease.setSelected(false);
                    mImgDecrease.setClickable(false);
                }

            }else {
                if (num > minCount) {
                    if (maxCount != -1 && num >= maxCount) {
                        if (needListener&&num > maxCount) {
                            showToast(TextUtils.isEmpty(limitMaxString)?"达到可选上限":limitMaxString);
                        }
                        mImgIncrease.setSelected(false);
                        if (!needToastAndButtonEnable) {

                            mImgIncrease.setClickable(false);
                        }
                    } else {
                        mImgIncrease.setSelected(true);
                        if (!needToastAndButtonEnable) {
                            mImgIncrease.setClickable(true);
                        }
                    }
                    if (!mImgDecrease.isClickable()) {
                        {
                            mImgDecrease.setSelected(true);

                            if (!needToastAndButtonEnable) {
                                mImgDecrease.setClickable(true);
                            }
                        }

                    }
                } else if (num == minCount && minCount == maxCount) {//如果maxCount和minCount相等的时候
                    mImgDecrease.setSelected(false);

                    if (!needToastAndButtonEnable) {
                        mImgDecrease.setClickable(false);
                    }
                    mImgIncrease.setSelected(false);

                    if (!needToastAndButtonEnable) {
                        mImgIncrease.setClickable(false);
                    }
                } else {
//                    //System.out.println("设置不可以按减少2");
                    mImgDecrease.setSelected(false);

                    if (!needToastAndButtonEnable) {
                        mImgDecrease.setClickable(false);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void reset() {
          minCount=0;
          maxCount=-1;
    }

    public interface OnNumChangedListener {
        /**
         * 数量发生变化
         *
         * @param num 现有数量
         */
        void onNumChanged(int num);

    }

    public interface OnNumClickListener {

        void onadd();

        void onsub();
    }

    public void setOnNumChangedListener(OnNumChangedListener onNumChangedListener) {
        mOnNumChangedListener = onNumChangedListener;
    }

    public int getNum() {
        return ParseUtils.parseInt(mTxtNum.getText().toString());
    }
}
