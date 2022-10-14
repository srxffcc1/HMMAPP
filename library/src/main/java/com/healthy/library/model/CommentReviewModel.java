package com.healthy.library.model;

import java.util.List;

/**
 * @author Li
 * @date 2019/04/12 17:39
 * @des 评价
 */

public class CommentReviewModel {
        public int id;
        public int shopCommentId;
        public String content;
        public String fromMemberId;
        public String memberState;
        public String fromMemberName;
        public String memberFaceUrl;
        public String createTime;
        public int praiseNum=0;
        public boolean isshow=false;
        public int praiseState;
        public List<DiscussReply> discussReplyList;
    public boolean isShow=false;
    public boolean isShowContent=false;
        

    public class DiscussReply {

        public int id;
        public int commentDiscussId;
        public String content;
        public String fromMemberId;
        public String fromMemberName;
        public String toMemberId;
        public String toMemberName;
        public String createTime;

    }
}
