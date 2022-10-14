package com.healthy.library.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Author: Li
 * Date: 2018/8/31 0031
 * Description:
 */
public class InputFilterUtils {
    /**
     * 过滤Emoji字符
     */
    public static class NoEmojiFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dStart, int dEnd) {
            String regex = "[\ud83c\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
            String content = String.valueOf(source);
            return content.replaceAll(regex, "");
        }
    }
}
