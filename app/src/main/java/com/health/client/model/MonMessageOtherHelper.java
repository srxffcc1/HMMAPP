package com.health.client.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MonMessageOtherHelper implements Serializable {
    @SerializedName(value = "hmmCityRecordByMemberIdDTOS", alternate = {"resourceInfoByFrontIdDTO"})
    public MonMessageHelperSingle hmmCityRecordByMemberIdDTOS;
    public String getTimes;
    public MessagePosting postingInfoByUrlStatusAndPostingIdDTO=new MessagePosting();
    public MessageMenberStatus memberStateDTO;



    public class MonMessageHelperSingle {
            public int id;
            public String frontId;
            public int replyType;
            public int upvoteType;//1就是帖子
            public String nickName;
            public String resourceNickname;
            public String createTime;
            public String icon;
            public int postingStatus;
            public int allTypeId=-1;
            public String memberId;
            public String replyContent;
            public String resourceContent;
    }
    public class MessagePosting{

        public String id;
        public String postingContent;
        public String createUserName;
        public String imageState;
        public String videoState;
        public String memberId;
        public String url;
        public String urlType;

    }

    public class MessageMenberStatus{

        public String memberState;

    }
}
