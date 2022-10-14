package com.health.index.model;

/**
 * @author Li
 * @date 2019/03/25 15:02
 * @des 今日贴士
 */

public class TipModel {
    private String id;
    private String title;
    private int readNum;
    private String imgUrl;

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public String getImgUrl() {
        return imgUrl == null ? "" : imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
