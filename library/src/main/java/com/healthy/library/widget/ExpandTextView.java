package com.healthy.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.healthy.library.R;


/**
 * @author: luoyang
 * @verson: [AppZxs，2018/9/6 14:04]
 * @description: 支持动态改变显示行数的 TextView
 */
public class ExpandTextView extends TextView {
    /**
     * true：展开，false：收起
     */
    boolean mExpanded = false;
    /**
     * 状态回调
     */
    Callback mCallback;
    /**
     * 源文字内容
     */
    String mText = "";

    public void setMaxLineCount(int maxLineCount) {
        this.maxLineCount = maxLineCount;
    }

    /**
     * 最多展示的行数
     */
    int maxLineCount = 3;
    boolean isMeasureBack=false;
    /**
     * 省略文字
     */
    final String ellipsizeText = "...";

    public ExpandTextView(Context context) {
        this(context, null);
    }

    public ExpandTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.com_ExpandTextView);
            maxLineCount = a.getInt(R.styleable.com_ExpandTextView_com_maxLines, 3);
            a.recycle();
        }
    }
    public void clearText(){
        isMeasureBack=false;
        mText="";
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mText!=null){
            //System.out.println("计算Line");
            StaticLayout sl = new StaticLayout(mText, getPaint(), getMeasuredWidth() - getPaddingLeft() - getPaddingRight()
                    , Layout.Alignment.ALIGN_CENTER, getLineSpacingMultiplier(), getLineSpacingExtra(), getIncludeFontPadding());
            // 总计行数
            int lineCount = sl.getLineCount();
//            //System.out.println("总计行数:"+lineCount);
//            //System.out.println("开始改变");
            if (lineCount > maxLineCount) {
//                //System.out.println("开始改变1");
                if (mExpanded) {
                    setText(mText);
                    if (mCallback != null) {
                        if(!isMeasureBack){
                            isMeasureBack=true;
                            mCallback.onExpand();
                            isMeasureBack=false;
                        }
                    }
                } else {
//                    //System.out.println("开始改变2");
                    lineCount = maxLineCount;

                    // 省略文字的宽度
                    float dotWidth = getPaint().measureText(ellipsizeText);

                    // 找出第 showLineCount 行的文字
                    int start = sl.getLineStart(lineCount - 1);
                    int end = sl.getLineEnd(lineCount - 1);
                    String lineText = mText.substring(start, end);

                    // 将第 showLineCount 行最后的文字替换为 ellipsizeText
                    int endIndex = 0;
                    for (int i = lineText.length() - 1; i >= 0; i--) {
                        String str = lineText.substring(i, lineText.length());
                        // 找出文字宽度大于 ellipsizeText 的字符
                        if (getPaint().measureText(str) >= dotWidth) {
                            endIndex = i;
                            break;
                        }
                    }

                    // 新的第 showLineCount 的文字
                    String newEndLineText = lineText.substring(0, endIndex) + ellipsizeText;
                    // 最终显示的文字
                    setText(mText.substring(0, start) + newEndLineText);
                    if (mCallback != null) {
                        if(!isMeasureBack){
                            isMeasureBack=true;
                            mCallback.onCollapse();
                            isMeasureBack=false;
                        }
                    }
                }
            } else {
//                //System.out.println("需要消失的内容："+mText);
                setText(mText);
                if (mCallback != null) {
                    if(!isMeasureBack){
                        isMeasureBack=true;
                        mCallback.onLoss();
                        isMeasureBack=false;
                    }
                }
            }

            // 重新计算高度
            int lineHeight = 0;
            for (int i = 0; i < lineCount; i++) {
                Rect lineBound = new Rect();
                sl.getLineBounds(i, lineBound);
                lineHeight += lineBound.height() + 10;
            }
            lineHeight += getPaddingTop() + getPaddingBottom();
            setMeasuredDimension(getMeasuredWidth(), lineHeight);
        }
        // 文字计算辅助工具



    }

    /**
     * 设置要显示的文字以及状态
     *
     * @param callback
     */
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void setText(CharSequence text, BufferType type) {
        if(TextUtils.isEmpty(mText)&&!TextUtils.isEmpty(text)){
            //System.out.println("修改原数据:"+text);
            mText = text.toString();
        }
        super.setText(text, type);
    }

    /**
     * 展开收起状态变化
     *
     * @param expanded
     */
    public void setChanged(boolean expanded) {
        mExpanded = expanded;
        //System.out.println("修改状态点击" + expanded);
        requestLayout();
    }

    public interface Callback {
        /**
         * 展开状态
         */
        void onExpand();

        /**
         * 收起状态
         */
        void onCollapse();

        /**
         * 行数小于最小行数，不满足展开或者收起条件
         */
        void onLoss();
    }
}
