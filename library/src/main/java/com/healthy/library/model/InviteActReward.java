package com.healthy.library.model;

public class InviteActReward {
    public static final long serialVersionUID = 1L;

    public long id;

    /**
     * 活动id
     */
    public long activityId;

    public int inviteNumber;

    /**
     * 优惠券id
     */
    public long couponId;

    /**
     * 优惠券面额
     */
    public String denomination;

    /**
     * 0表示无门槛，优惠券门槛金额
     */
    public String limitAmount;

    /**
     * 优惠券数量
     */
    public int couponNumber;

    /**
     * 邀请类型：1-邀请人，2-被邀请人
     */
    public int inviteType;

    /**
     * 创建时间
     */
    public String createTime;

    /**
     * 更新时间
     */
    public String updateTime;

    public CouponInfoZ coupon;


}
