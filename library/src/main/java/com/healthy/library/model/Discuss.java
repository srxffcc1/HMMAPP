package com.healthy.library.model;

import java.io.Serializable;
import java.util.List;

public class Discuss implements Serializable {

    public int id;
    public int postingId;
    public String content;
    public String fromMemberId;
    public String fromMemberName;
    public String fromMemberFaceUrl;
    public int memberType;
    public String memberState;
    public int status;
    public String createTime;
    public String createTimeStr;
    public int praiseNum;
    public int praiseState;
    public List<DiscussReply> discussReplyList;
    public boolean isShow = false; // true 展开完毕 false 还有剩余数据
    public boolean isShowContent = false;
    public transient boolean isMore = false; //当前回复评论列表是否有更多
    public transient int currentPage = 1;//当前回复评论列表页码
    public transient int openCount = 0;//当前回复评论打开数量
    public transient int totalNum;//总回复数量
    public transient boolean isClickAction = false;
    /**
     * 徽标id
     */
    public String badgeId;
    /**
     * 徽标名称
     */
    public String badgeName;

    /**
     * 徽标认证类型（0：达人认证，1：官方认证）
     */
    public int badgeType;
    /**
     * 当前用户最后投票立场  1 正方   2 反方
     */
    public String lastVoteStand;

    public class DiscussReply implements Serializable {

        public int id;
        public int postingDiscussId;
        public String content;
        public String fromMemberId;
        public String fromMemberName;
        public int fromMemberType;
        public String toMemberId;
        public String toMemberName;
        public int toMemberType;
        public int fatherId;
        public int status;
        public String createTime;
        public String createTimeStr;
        /**
         * 徽标id
         */
        public String fromBadgeId;
        /**
         * 徽标名称
         */
        public String fromBadgeName;
        /**
         * 徽标认证类型（0：达人认证，1：官方认证）
         */
        public int fromBadgeType;
        public String fromMemberFaceUrl;
        /**
         * 当前用户最后投票立场  1 正方   2 反方
         */
        public String fromVoteStand;


        /**
         * 徽标id
         */
        //public String toBadgeId;
        /**
         * 徽标名称
         */
        //public String toBadgeName;
        /**
         * 徽标认证类型（0：达人认证，1：官方认证）
         */
        //public Integer toBadgeType;

        //public String toMemberFaceUrl;

        /**
         * 当前用户最后投票立场  1 正方   2 反方
         */
        //public String toVoteStand;

    }

}
