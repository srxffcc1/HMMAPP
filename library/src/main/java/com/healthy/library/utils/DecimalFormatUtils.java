package com.healthy.library.utils;

import java.text.DecimalFormat;

/**
 * Author: Li
 * Date: 2018/12/18 0018
 * Description:
 */
public class DecimalFormatUtils {
    public static String formatMoney(double m) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(m);
    }

    public static String formatMoney(String m) {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            return decimalFormat.format(Double.parseDouble(m));
        } catch (Exception e) {
            return m;
        }
    }
}
