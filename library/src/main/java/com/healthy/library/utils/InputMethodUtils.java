package com.healthy.library.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Author: Li
 * Date: 2018/8/27 0027
 * Description:
 */
public class InputMethodUtils {
    public static void hideKeyboard(Context context, View view) {
        try {
            if (view != null) {
                InputMethodManager inputManger = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManger != null) {
                    inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
