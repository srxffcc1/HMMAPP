package com.healthy.library.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class LiveVideoMain implements MultiItemEntity {
    public String id;
    public String merchantId;
    public String shopId;
    public String anchormanId;
    public String roomId;
    public String groupId;
    public String courseTitle;
    public String activityId;
    public String courseIntroduce;
    public String actualBeginTime;
    public String picUrl;
    public int type;
    public int category;
    public String courseLivePrice;
    public String courseOrderPrice;
    public String coursePwd;
    public int isBringGoods;
    public int status;
    public String beginTime;
    public String endTime;
    public String pushAddress;
    public String pullAddress;
    public List<String> activityIdList;
    public String videoUrl;
    public int livePlatform;
    public long timesWatched;
    public long virtualTimesWatchedReplay;
    public long popularity;
    public int isDelete;
    public String createUser;
    public String createTime;
    public String updateUser;
    public String updateTime;
    public String merchantName;
    public long subscribeCount;//预约数
    public LiveAnchorman liveAnchorman;
    public LiveRoom liveRoom;

    @Override
    public int getItemType() {
        return 4;
    }

    public class LiveAnchorman {
        public String id;
        public String merchantId;
        public String merchantName;
        public String shopId;
        public String roomId;
        public String relevanceRoom;
        public String memberId;
        public String memberName;
        public String memberPhone;
        public int isForbidLive;
        public int livePlatform;
        public int isDelete;
        public String createUser;
        public String createTime;
        public String updateUser;
        public String updateTime;
        public String liveRoom;
        public String liveCourseList;
        public String avatarUrl;
    }

    public class LiveRoom {
        public String id;
        public String merchantId;
        public String merchantName;
        public String shopId;
        public String roomName;
        public String roomIntroduce;
        public String picUrl;
        public int livePlatform;
        public int isDelete;
        public String createUser;
        public String createTime;
        public String updateUser;
        public String updateTime;
    }

    public List<LiveVideoGoods> liveVideoGoodsList;//为null就是没初始化 如果初始化就是空
}
