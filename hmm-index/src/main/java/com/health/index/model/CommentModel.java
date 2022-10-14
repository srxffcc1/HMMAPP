package com.health.index.model;

import java.util.List;

/**
 * 创建日期：2022/1/6 13:48
 *
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.model
 * 类说明：
 */

public class CommentModel {

    public String id;
    public String knowledgeId;
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
    public boolean isShow = false; // true 展开完毕 false 还有剩余数据
    public boolean isShowContent = false;
    public boolean praise;
    public List<ReplyListBean> replyList;

    public  class ReplyListBean {
        public String id;
        public String knowledgeDiscussId;
        public String content;
        public String fromMemberId;
        public int fromMemberType;
        public String toMemberId;
        public int toMemberType;
        public String parentId;
        public int status;
        public String createTime;
        public String updateTime;
        public String fromMemberName;
        public String fromMemberFaceUrl;
        public String toMemberName;
        public String toMemberFaceUrl;
        public String timeRemark;
    }
}
