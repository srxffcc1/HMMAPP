package com.healthy.library.model;

import java.util.List;

public class ToolsFoodDiscuss {
    public long id;

    public long recipeId;

    public String content;

    public String fromMemberId;

    public String fromMemberName;

    public int memberType;

    public String memberState;

    public int status;

    public String createTime;

    public List<Reply> replyDTOList;

    public int praiseNum;

    public int praiseStatus;

    public String fromMemberFaceUrl;

    public int replyNum;
    public boolean isShowContent;
    public boolean isShow;

    public class Reply {
        public long id;

        public long recipeDiscussId;

        public String content;

        public String fromMemberId;

        public String fromMemberName;

        public int fromMemberType;

        public int status;

        public String createTime;

    }


}
