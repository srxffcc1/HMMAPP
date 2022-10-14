package com.health.index.model;

import java.io.Serializable;
import java.util.List;

public class IndexAllChat {

        public int id;
        public String topicName;
        public int partInNum;
        public int postingNum;
        public Posting posting;


    public class Posting {
        public int id;
        public String postingContent;
        public int topStatus;
        public int imageState;
        public int videoState;
        public int discussNum;
        public int praiseNum;
        public int praiseState;
        public List<ImageUrl> imgUrls;
        public List<VideoUrl> videoUrls;
        public List<PraiseMember> praiseMemberList;
    }
    public class ImageUrl implements Serializable {

        public int id;
        public int postingId;
        public String url;
        public int urlType;
        public String createTime;
        public String updateTime;
        public int urlStatus;


    }
    public class VideoUrl {

        public int id;
        public int postingId;
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
