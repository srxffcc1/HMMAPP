package com.healthy.library.watcher;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
@Deprecated
/**
 * Author: Li
 * Date: 2018/10/10 0010
 * Description:
 */
public class PhoneNumberFormatFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Log.e("tag", "filter: "+source);
        //删除
        if (end==0) {
            Log.e("tag", "filter: "+source+"|"+dest);
            return "";
        }
        //输入
        else {
            if (dest.length()==3) {
                return " "+source;
            }
            if (dest.length()==8) {
                return " "+source;
            }
        }


        return null;
    }
}
