package com.healthy.library.utils;

import android.widget.EditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Author: Li
 * Date: 2018/10/12 0012
 * Description:
 */
public class FormatUtils {
    /**
     * 格式化手机号，添加空格
     *
     * @param s         原内容
     * @param count     操作 0：删除
     * @param selection 光标位置
     * @param editText  编辑框
     */
    public static void formatPhoneNumber(CharSequence s, int count, int selection, EditText editText) {
        String origin = String.valueOf(s);
        String format;
        if (count == 0) {
            format = origin.substring(0, origin.length());
            editText.setText(format);
            editText.setSelection(selection + count);
        } else {
            format = origin.replaceAll("\\s", "");
            if (format.length() > 3 && format.length() < 7) {
                String part1 = format.substring(0, 3);
                String part2 = format.substring(3, format.length());
                format = String.format("%s %s", part1, part2);
            } else if (format.length() >= 7) {
                String part1 = format.substring(0, 3);
                String part2 = format.substring(3, 7);
                String part3 = format.substring(7, format.length());
                format = String.format("%s %s %s", part1, part2, part3);
            }
            editText.setText(format);
            editText.setSelection(format.length());
        }
    }


    private static DecimalFormat sKeep2DecimalsFormat = new DecimalFormat("0.##");
    private static DecimalFormat sMintFormat = new DecimalFormat("###,##0.00");

    /**
     * 保留两位小数
     *
     * @param money 金额
     * @return 金额字符
     */
    public static String moneyKeep2Decimals(double money) {
        return sKeep2DecimalsFormat.format(money);
    }

    /**
     * 保留两位小数
     *
     * @param money 金额
     * @return 金额字符
     */
    public static String moneyKeep2Decimals(String money) {
        try {
            return sKeep2DecimalsFormat.format(new BigDecimal(money).doubleValue());
        } catch (Exception e) {
            return money;
        }
    }

    public static String formatRewardMoney(String money) {
        if (money.contains(".")) {
            money = money
                    .replaceAll("0+?$", "")
                    .replaceAll("[.]$", "");
        }
        return money;
    }
}
