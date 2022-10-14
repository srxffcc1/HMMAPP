package com.healthy.library.model;

import java.util.List;

public class LiveRoomInfo {
    public String courseId;//直播id
    public String id;
    public String merchantId;
    public String shopId;
    public String anchormanId;
    public String roomId;
    public String groupId;
    public String courseTitle;
    public String courseIntroduce;
    public String picUrl;
    public int type;
    public int category;
    public int isDebug;
    public int isPlayback;
    public String courseLivePrice;
    public String courseOrderPrice;
    public String coursePwd;
    public int isBringGoods;
    public int status;
    public String beginTime;
    public String actualBeginTime;
    public String endTime;
    public String pushAddress;
    public String pullAddress;
    public List<String> activityIdList;
    public String videoUrl;
    public String fileId;
    public int livePlatform;
    public int timesWatched;
    public int isDelete;
    public int isClosed;//等于1表示主播主动结束了直播
    public String createUser;
    public String createTime;
    public String updateUser;
    public String updateTime;
    public String subscribeCount;//订阅数
    public String fansCount;//新增粉丝数
    public LiveAnchorman liveAnchorman;
    public LiveRoom liveRoom;

    public void setActualBeginTime(String actualBeginTime) {
        this.actualBeginTime = actualBeginTime;
    }

    public class LiveAnchorman {
        public String id;
        public String memberId;
        public String memberName;
        public String memberPhone;
    }

    public class LiveRoom {
        public String id;
        public String roomName;
        public String roomIntroduce;
        public String picUrl;
    }

}
