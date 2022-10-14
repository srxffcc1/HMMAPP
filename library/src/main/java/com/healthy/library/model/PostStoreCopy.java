package com.healthy.library.model;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子本地保存
 */
public class PostStoreCopy implements Serializable {
    
        public String postingContent;
        public String imageState;
        public String memberState;
        public String postingAddress;
        public List<String> uploaduls;
        public String postingStatus;
        public String limitsStatus;
        public TopicLimit postingLimit;
        public int anonymousStatus;
        public List<Topic> topicIds;
        public String videoState;

    public String getPostingContent() {
        return postingContent;
    }

    public void setPostingContent(String postingContent) {
        this.postingContent = postingContent;
    }

    public String getImageState() {
        return imageState;
    }

    public void setImageState(String imageState) {
        this.imageState = imageState;
    }

    public String getMemberState() {
        return memberState;
    }

    public void setMemberState(String memberState) {
        this.memberState = memberState;
    }

    public String getPostingAddress() {
        return postingAddress;
    }

    public void setPostingAddress(String postingAddress) {
        this.postingAddress = postingAddress;
    }

    public List<String> getUploaduls() {
        return uploaduls;
    }

    public void setUploaduls(List<String> uploaduls) {
        this.uploaduls = uploaduls;
    }

    public String getPostingStatus() {
        return postingStatus;
    }

    public void setPostingStatus(String postingStatus) {
        this.postingStatus = postingStatus;
    }

    public String getLimitsStatus() {
        return limitsStatus;
    }

    public void setLimitsStatus(String limitsStatus) {
        this.limitsStatus = limitsStatus;
    }

    public TopicLimit getPostingLimit() {
        return postingLimit;
    }

    public void setPostingLimit(TopicLimit postingLimit) {
        this.postingLimit = postingLimit;
    }

    public int getAnonymousStatus() {
        return anonymousStatus;
    }

    public void setAnonymousStatus(int anonymousStatus) {
        this.anonymousStatus = anonymousStatus;
    }

    public List<Topic> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(List<Topic> topicIds) {
        this.topicIds = topicIds;
    }

    public String getVideoState() {
        return videoState;
    }

    public void setVideoState(String videoState) {
        this.videoState = videoState;
    }
}
