package com.health.index.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.healthy.library.model.PostActivityList;

import java.util.List;

public class IndexAllChat2 implements MultiItemEntity {

    public String id;

    public List<TopicList> topicList;

    public String postingContent;

    public String postingRichContent;
    public String createUserFaceUrl;
    public String memberState;

    public int topStatus;
    public int createSource;
    public String memberId;
    public String createTime;

    public int imageState;
    public String postingAddress;
    public String accountNickname;
    public String videoFramePicture;
    public String createTimeStr;

    public List<ImgUrl> imgUrls;

    public int videoState;

    public List<ImgUrl> videoUrls;

    public int praiseState;
    public int postingType;//1:普通帖子 2:新品首发 3:种草清单 4:图文搭配 5:视频搭配 6:会员专享券

    public List<PraiseMember> praiseMemberList;
    public long partInNum;
    public long discussNum;
    public long praiseNum;
    public int anonymousStatus;
    public List<PostActivityList> postActivityList;

    @Override
    public int getItemType() {
        if (postingType == 1 || postingType == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    public class TopicList {
        public int id;

        public String topicName;

    }

    public class ImgUrl {
        public String id;

        public String postingId;

        public String url;

        public int urlType;

        public String createTime;

        public String updateTime;

        public int urlStatus;

    }

    public class PraiseMember {
        public String memberId;

        public String nickName;

        public String faceUrl;

    }
}
