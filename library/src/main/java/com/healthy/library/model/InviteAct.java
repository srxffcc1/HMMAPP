package com.healthy.library.model;

import java.util.ArrayList;
import java.util.List;

public class InviteAct {
    public ArrayList<InviteReward> inviteRewardRank=new ArrayList<>();//获奖排名
    public ArrayList<InviteJoinInfo> inviteJoinInfoRank=new ArrayList<>();//邀请排名
    public InviteActDetail activity;
    public int inviteJoinCount;


    public class InviteActDetail{
        public String id;
        public String title;
        public int target;
        public String pic;

        public String getBeginTime() {
            return beginTime.split(" ")[0];
        }

        public String beginTime;

        public String getEndTime() {
            return endTime.split(" ")[0];
        }

        public String endTime;
        public String createUser;
        public int status;
        public String comment;
        public String createTime;
        public String updateTime;
        public long createId;
        public long merchantId;
        public int isInviteTargetReward;
        public int deleteFlag;
        public String startDate;
        public String endDate;
        public int pageSize=10;
        public int pageNum=1;
        //邀请人奖励
        public List<InviteActReward> inviteRewardList=new ArrayList<>();

        //被邀请人奖励
        public List<InviteActReward> inviteTargetRewardList=new ArrayList<>();
    }
}
