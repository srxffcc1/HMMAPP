package com.healthy.library.utils;

import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.TextView;

public class StringUtils {
    public static String getResultPrice(String orgprice) {
        if (TextUtils.isEmpty(orgprice)) {
            return "0";
        }
        String result = FormatUtils.moneyKeep2Decimals(orgprice);
        String[] array = result.split("\\.");
        if (array.length > 1) {
            if (Long.parseLong(array[1].trim()) == 0) {
                result = array[0].trim();
            }
        }
        return result;
    }
    public static String autoSplitText(final TextView tv) {
        final String rawText = tv.getText().toString(); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度
        //将原始文本按行拆分
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }
        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }
        return sbNewText.toString();
    }
    public static String getHideName(String orgprice) {//对原始字符串加密处理
        String result="";
        int orglength=orgprice.length()/3;
        result=orgprice.substring(0,orgprice.length()-orglength)+"...";
        return result;
    }
    public static String getPhoneHide(String configneePhone) {

        String hidenPhone = configneePhone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        return hidenPhone;
    }

    public static String trimTrailingWhitespace(CharSequence source) {

        if (source == null) {
            return "";
        }

        int i = source.length();

        // loop back to the first non-whitespace character
        while (--i >= 0 && Character.isWhitespace(source.charAt(i))) {
        }

        return source.subSequence(0, i + 1).toString();
    }
    public static String int2chineseNum(int src) {
        final String num[] = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        final String unit[] = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
        String dst = "";
        int count = 0;
        while(src > 0) {
            dst = (num[src % 10] + unit[count]) + dst;
            src = src / 10;
            count++;
        }
        return dst.replaceAll("零[千百十]", "零").replaceAll("零+万", "万")
                .replaceAll("零+亿", "亿").replaceAll("亿万", "亿零")
                .replaceAll("零+", "零").replaceAll("零$", "");
    }

    public static String noEndLnString(String org){
        if(org!=null&&org.endsWith("\n")){
            org=org.substring(0,org.length()-1);
            return noEndLnString(org);
        }else {
            return org;
        }
    }
}
