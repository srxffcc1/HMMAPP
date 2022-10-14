package com.healthy.library.model;


/**
 * 创建日期：2022/7/19 15:09
 *
 * @author LiuWei
 * @version 1.0
 * 包名： com.healthy.library.model
 * 类说明：
 */

public class GoodsRank {
    public String id;
    public String shareMemberCount;
    public String rankNum;
    public String shareMemberPic;
    public String shareMemberName;
    public String shareMemberId;
    public String memberId;
    public String orderId;
    public String shareResultId;
    public double shareResult;
    public double shareCouponMoney;
    public double shareCash;
    public String shareResultStatus;
    public String createTime;
    public String updateTime;
    public String shareMemberPhone;

    public GoodsRank(String shareMemberCount, String rankNum, String shareMemberPic, String shareMemberName, String shareMemberId, double shareResult) {
        this.shareMemberCount = shareMemberCount;
        this.rankNum = rankNum;
        this.shareMemberPic = shareMemberPic;
        this.shareMemberName = shareMemberName;
        this.shareMemberId = shareMemberId;
        this.shareResult = shareResult;
    }

    public GoodsRank() {
    }
}
