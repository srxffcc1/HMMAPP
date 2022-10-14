package com.healthy.library.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Li
 * @date 2019/03/23 16:25
 * @des 正则
 */

public class PatternUtils {


    private static Pattern NUM_PATTER = Pattern.compile("[^0-9]");

    public static String getNum(String s) {
        Matcher matcher = NUM_PATTER.matcher(s);
        return matcher.replaceAll("").trim();
    }

}
