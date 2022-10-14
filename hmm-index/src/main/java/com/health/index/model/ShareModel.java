package com.health.index.model;

/**
 * @author Li
 * @date 2019/04/26 14:17
 * @des 分享model
 */

public class ShareModel {
    /**
     * 分享标题
     */
    private String title;
    /**
     * 分享描述
     */
    private String des;
    /**
     * 分享链接
     */
    private String url;

    /**
     * 分享平台
     * 1 微信 2 朋友圈 3 qq 4 空间
     */
    private String platform;

    public String getPlatform() {
        return platform == null ? "" : platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des == null ? "" : des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
