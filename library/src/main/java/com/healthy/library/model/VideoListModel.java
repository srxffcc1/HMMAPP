package com.healthy.library.model;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.healthy.library.utils.AES2020;

public class VideoListModel implements MultiItemEntity {

    @Override
    public int getItemType() {
        return isTop;
    }

    public String id;
    public int videoType;
    public String categoryId;
    public String categoryCode;
    public String videoName;
    public String videoRemark;
    private String videoUrl;
    public String photo;
    public String videoIntroduce;
    public String videoIntroducePhoto;
    public int isFree;
    public double videoPrice;
    public int virtualView;
    public int pushToIndex;
    public int realView;
    public int videoPublic;
    public String publicTime;
    public String createTime;
    public String updateTime;
    public String operator;
    public int isTop;
    public String topTime;
    public String topStartTime;
    public String topEndTime;
    public int praiseCount;
    public int discussCount;
    public boolean praise;

    public String getVideoUrl() {
        if (videoUrl != null && !TextUtils.isEmpty(videoUrl)) {
            return AES2020.getInstance().decrypt(videoUrl, "http");
        } else {
            return null;
        }
    }

    public String getVideoPraiseType() {
        if (praise) {
            return "2";
        } else {
            return "1";
        }
    }

}
