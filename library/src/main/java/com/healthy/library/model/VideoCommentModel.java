package com.healthy.library.model;

import java.util.List;

public class VideoCommentModel {

    public String id;
    public String videoId;
    public String content;
    public String fromMemberId;
    public int memberType;
    public String memberState;
    public int status;
    public String createTime;
    public String updateTime;
    public int praiseCount;
    public String fromMemberName;
    public String fromMemberFaceUrl;
    public String timeRemark;
    public boolean praise;
    public List<ReplyListBean> replyList;
    public boolean isShow=false;
    public boolean isShowContent=false;

    public class ReplyListBean {
        public String id;
        public String videoDiscussId;
        public String content;
        public String fromMemberId;
        public int fromMemberType;
        public String toMemberId;
        public String fromMemberFaceUrl;
        public int toMemberType;
        public String parentId;
        public int status;
        public String createTime;
        public String updateTime;
        public String fromMemberName;
        public String toMemberName;
        public String timeRemark;
    }
}
