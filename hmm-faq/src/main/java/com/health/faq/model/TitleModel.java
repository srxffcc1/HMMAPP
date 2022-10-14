package com.health.faq.model;

/**
 * @author Li
 * @date 2019/07/03 15:31
 * @des
 */

public class TitleModel {
    private String title;
    private String titleSub;
    private String des;
    private boolean showBadge;

    public boolean isShowBadge() {
        return showBadge;
    }

    public void setShowBadge(boolean showBadge) {
        this.showBadge = showBadge;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleSub() {
        return titleSub == null ? "" : titleSub;
    }

    public void setTitleSub(String titleSub) {
        this.titleSub = titleSub;
    }

    public String getDes() {
        return des == null ? "" : des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
