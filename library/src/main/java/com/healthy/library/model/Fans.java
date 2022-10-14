package com.healthy.library.model;

/**
 * 个人主页 用户信息 7008 7001推荐
 */
public class Fans {
    public String friendId;
    public String memberId;//只在7008中用
    public String memberStatus;
    public String nickName;
    public String faceUrl;
    public int postingNum;
    public long friendNum;
    public long fansNum;
    public int focusStatus;
    public String praiseNum;
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


    public int friendType;
    public String currentStatus;


}
