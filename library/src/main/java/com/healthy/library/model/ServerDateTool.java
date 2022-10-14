package com.healthy.library.model;

public class ServerDateTool {
    private String date;//日期
    private String mYear; // 当前年
    private String mMonth; // 月
    private String mDay;//日
    private String mMonthAddmDay;//月和日拼在一起

    public ServerDateTool(String date, String mYear, String mMonth, String mDay, String mMonthAddmDay) {
        this.date = date;
        this.mYear = mYear;
        this.mMonth = mMonth;
        this.mDay = mDay;
        this.mMonthAddmDay = mMonthAddmDay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getmYear() {
        return mYear;
    }

    public void setmYear(String mYear) {
        this.mYear = mYear;
    }

    public String getmMonth() {
        return mMonth;
    }

    public void setmMonth(String mMonth) {
        this.mMonth = mMonth;
    }

    public String getmDay() {
        return mDay;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
    }

    public String getmMonthAddmDay() {
        return mMonthAddmDay;
    }

    public void setmMonthAddmDay(String mMonthAddmDay) {
        this.mMonthAddmDay = mMonthAddmDay;
    }
}
