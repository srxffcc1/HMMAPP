package com.health.mine.widget;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * 创建日期：2022/9/16 15:10
 *
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.mine.widget
 * 类说明：
 */

public class PointLengthFilter implements InputFilter {

    /**
     * 输入框小数的位数  示例保留2位小数
     */
    private int KEEP_SEVERAL_DECIMAL_PLACES = 2;

    public PointLengthFilter(int index) {
        this.KEEP_SEVERAL_DECIMAL_PLACES = index;
    }

    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
            return null;
        }
        String dValue = dest.toString();
        if(TextUtils.isEmpty(dValue)){
            //如果直接copy过来的数字 之前没有输入数字
            if (source.toString().contains(".")){
                int i = source.toString().indexOf(".");
                if (end > i + KEEP_SEVERAL_DECIMAL_PLACES + 1){
                    return source.subSequence(start, i + KEEP_SEVERAL_DECIMAL_PLACES + 1);
                }
            }
            return source;
        }
        String[] splitArray = dValue.split("\\.");
        if(splitArray != null && splitArray.length > 1){
            int cursorIndex = dValue.indexOf(".");
            if(dend > cursorIndex){
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - KEEP_SEVERAL_DECIMAL_PLACES;
                if(diff > 0){
                    return "";
                }
            }
        }
        //如果是粘贴过来的一串,判断长度,只保留小数点后KEEP_SEVERAL_DECIMAL_PLACES位
        if (end >= KEEP_SEVERAL_DECIMAL_PLACES && dValue.contains(".")){
            if (splitArray != null){
                if (splitArray.length == 1){
                    //小数点后边没有东西,保留KEEP_SEVERAL_DECIMAL_PLACES位
                    return source.subSequence(start, KEEP_SEVERAL_DECIMAL_PLACES);
                }else {
                    //小数点后边有东西
                    int i = KEEP_SEVERAL_DECIMAL_PLACES - splitArray[1].length();
                    if (i > 0){
                        //保留KEEP_SEVERAL_DECIMAL_PLACES - 已经存在的长度
                        return source.subSequence(start, i);
                    }else {
                        //成负数了,说明小数点后边的长度超过了KEEP_SEVERAL_DECIMAL_PLACES.
                        return "";
                    }
                }
            }
        }
        return source;
    }
}
