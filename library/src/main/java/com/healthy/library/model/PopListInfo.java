package com.healthy.library.model;

import android.text.TextUtils;

import java.io.Serializable;

public class PopListInfo implements Serializable {
    public String PopNo;
    public String PopDesc;
    public String InputDate;
    public String StartDate;
    public String EndDate;
    public String ImportLevel;
    public String DepartID;
    public String PopType;
    public String PopLabelID;
    public String PopLabelName;
    public PopDetailInfo popDetail;
    public int adapterNumber;
    public boolean hasGoods;

    public String getBeginTime() {
        try {
            return StartDate.split(" ")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getEndTime() {
        try {
            return EndDate.split(" ")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public String getPopNameByPopType() {
        if (PopType != null && !TextUtils.isEmpty(PopType)) {
            if (PopType.equals("0")) {
                return "金额促销";
            } else if (PopType.equals("1")) {
                return "单品促销";
            } else if (PopType.equals("2")) {
                return "数量促销";
            } else if (PopType.equals("3")) {
                return "限量促销";
            } else if (PopType.equals("4")) {
                return "选单金额促销";
            } else if (PopType.equals("5")) {
                return "选单单品促销";
            } else if (PopType.equals("6")) {
                return "选单数量促销";
            } else if (PopType.equals("7")) {
                return "满量折扣";
            } else if (PopType.equals("8")) {
                return "选单多类金额促销";
            } else if (PopType.equals("9")) {
                return "满量促销折扣";
            } else if (PopType.equals("A")) {
                return "唯一码促销";
            } else if (PopType.equals("B")) {
                return "满量送本品";
            } else if (PopType.equals("C")) {
                return "第二件半价";
            } else if (PopType.equals("D")) {
                return "捆绑定价";
            } else if (PopType.equals("E")) {
                return "满额折扣";
            } else if (PopType.equals("F")) {
                return "PLUS特价";
            } else if (PopType.equals("G")) {
                return "满量立减";
            } else if (PopType.equals("H")) {
                return "满额立减";
            }
        } else {
            return "";
        }
        return null;
    }
}
