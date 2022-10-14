package com.health.servicecenter.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.healthy.library.utils.TransformUtil;

@SuppressLint("AppCompatCustomView")
public class SDAdaptiveTextView extends TextView {
    public SDAdaptiveTextView(Context context) {
        super(context);
    }

    public SDAdaptiveTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SDAdaptiveTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 使用该方法设置TextView的文本内容,改方法不能再主线程中执行
     * @param text
     */
    public void setAdaptiveText(String text) {

        this.setText(adaptiveText(this,text));
    }

    private String adaptiveText(final TextView textView,String content) {
        final String originalText = content; //原始文本
        final Paint tvPaint = textView.getPaint();//获取TextView的Paint
        final float tvWidth = textView.getWidth() - ((ViewGroup)textView.getParent()).getPaddingLeft() - ((ViewGroup)textView.getParent()).getPaddingRight(); //TextView的可用宽度
        //将原始文本按行拆分
        String[] originalTextLines = originalText.replaceAll("\r", "").split("\n");
        StringBuilder newTextBuilder = new StringBuilder();
        for (String originalTextLine : originalTextLines) {
            //文本内容小于TextView宽度，即不换行，不作处理
            if (tvPaint.measureText(originalTextLine) <= tvWidth) {
                newTextBuilder.append(originalTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int i = 0; i != originalTextLine.length(); ++i) {
                    char charAt = originalTextLine.charAt(i);
                    lineWidth += tvPaint.measureText(String.valueOf(charAt));
                    if (lineWidth <= tvWidth) {
                        newTextBuilder.append(charAt);
                    } else {
                        //单行超过TextView可用宽度，换行
                        newTextBuilder.append("\n");
                        lineWidth = 0;
                        --i;//该代码作用是将本轮循环回滚，在新的一行重新循环判断该字符
                    }
                }
            }
        }
        return newTextBuilder.toString();
    }
}
