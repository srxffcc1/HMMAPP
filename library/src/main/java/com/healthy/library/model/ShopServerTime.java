package com.healthy.library.model;

public class ShopServerTime {
    private String dateStr;
    private int num;//	预约人数
    private int state;    //预约状态 0未满 1已满
    private int dateState;//	过期状态 0未过期 1已过期
    private int timeFlag;//1代表上午,2代表下午
    private Boolean isTag;//是否是分割线
    private Boolean isClick;//是否选中

    public ShopServerTime(String dateStr, int num, int state, int dateState, int timeFlag, Boolean isTag, Boolean isClick) {
        this.dateStr = dateStr;
        this.num = num;
        this.state = state;
        this.dateState = dateState;
        this.timeFlag = timeFlag;
        this.isTag = isTag;
        this.isClick = isClick;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDateState() {
        return dateState;
    }

    public void setDateState(int dateState) {
        this.dateState = dateState;
    }

    public int getTimeFlag() {
        return timeFlag;
    }

    public void setTimeFlag(int timeFlag) {
        this.timeFlag = timeFlag;
    }

    public Boolean getTag() {
        return isTag;
    }

    public void setTag(Boolean tag) {
        isTag = tag;
    }

    public Boolean getClick() {
        return isClick;
    }

    public void setClick(Boolean click) {
        isClick = click;
    }
}
