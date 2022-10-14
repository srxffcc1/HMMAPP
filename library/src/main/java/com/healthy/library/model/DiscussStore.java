package com.healthy.library.model;

import java.io.Serializable;

/**
 * 帖子本地保存
 */
public class DiscussStore implements Serializable {

    public String getDiscussContent() {
        return discussContent;
    }

    public void setDiscussContent(String discussContent) {
        this.discussContent = discussContent;
    }

    public String discussContent;

}
