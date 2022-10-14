package com.health.tencentlive.model;

public class LiveHelpList {
    public String countMember;
    public String createTime;
    public FromMember fromMember;
    public Member member;
    public String ranking;
    public int memberStatus;

    public class FromMember {
        public String name;
        public String nickName;
        public String faceUrl;
    }

    public class Member {
        public String name;
        public String nickName;
        public String faceUrl;
    }
}
