package com.healthy.library.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    /**
     * 格式化
     */
    private static DecimalFormat dfs = null;
    public static DecimalFormat format(String pattern) {
        if (dfs == null) {
            dfs = new DecimalFormat();
        }
        dfs.setRoundingMode(RoundingMode.FLOOR);
        dfs.applyPattern(pattern);
        return dfs;
    }

    public static List get2List(List orglist){
        if(orglist!=null){
            if(orglist.size()>1){
                List result=new ArrayList();
                for (int i = 1; i <orglist.size() ; i++) {
                    result.add(orglist.get(i));
                }
                return result;
            }else {
                return new ArrayList();
            }
        }else {
            return new ArrayList();
        }
    }
}
