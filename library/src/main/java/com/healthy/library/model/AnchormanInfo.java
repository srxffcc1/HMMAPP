package com.healthy.library.model;

public class AnchormanInfo {
    public String id;
    public String merchantId;
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
    public String avatarUrl;
    public String latestLiveTime;
    public String createTime;
    public String updateUser;
    public String updateTime;
    public String liveCourseList;
    public LiveRoom liveRoom;
    public int courseCount;
    public int fansCount;


    public class LiveRoom {
        public String id;
        public String merchantId;
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
}
