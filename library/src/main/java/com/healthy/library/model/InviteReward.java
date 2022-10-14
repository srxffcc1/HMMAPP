package com.healthy.library.model;

import com.healthy.library.utils.FormatUtils;

public class InviteReward {
    public long id;
    public String inviteMemberId;
    public String inviteMemberPhone;
    public long activityId;
    public long activityRewardId;
    public String inviteMemberNickName;
    public long couponId;
    public int couponNum;
    public int status;
    public String createTime;
    public String latestUpdateTime;
    public CouponInfoZ coupon;

    public String getNameAndReward(){
        return inviteMemberNickName+"邀请获得"+ FormatUtils.moneyKeep2Decimals(coupon.getCouponDenomination())+"元优惠券";
    }

}
