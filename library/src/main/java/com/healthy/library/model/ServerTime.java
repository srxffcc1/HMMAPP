package com.healthy.library.model;

public class ServerTime {
    private int timeFlag;//1代表上午,2代表下午
    private String timetxt;
    private Boolean isTag;//是否是分割线
    private Boolean isClick;//是否可约

    public ServerTime(int timeFlag, String timetxt, Boolean isTag, Boolean isClick) {
        this.timeFlag = timeFlag;
        this.timetxt = timetxt;
        this.isTag = isTag;
        this.isClick = isClick;
    }

    @Override
    public String toString() {
        return "ServerTime{" +
                "timeFlag=" + timeFlag +
                ", timetxt='" + timetxt + '\'' +
                ", isTag=" + isTag +
                ", isClick=" + isClick +
                '}';
    }

    public int getTimeFlag() {
        return timeFlag;
    }

    public void setTimeFlag(int timeFlag) {
        this.timeFlag = timeFlag;
    }

    public String getTimetxt() {
        return timetxt;
    }

    public void setTimetxt(String timetxt) {
        this.timetxt = timetxt;
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
