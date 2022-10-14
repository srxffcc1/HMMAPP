package com.healthy.library.interfaces;

public interface PlatformMerchantCoupon {


    public String getId();

    public String getMerchantCouponId();

    public String getMerchantCouponId722();//王振 2020。7.22增加平台券需要的字段
    public int getCouponType();

    public int getSuperposition();

    public String getCouponName();

    public String getDenomination();

    public String getRequirement();

    public String getTimeStartAndEnd();

    public String getTimeStart();

    public String getTimeEnd();

    public String getOverPayment();
    public String getCouponTip();

    public String getCouponNote();
    public void setCouponNote(String couponNote);
    public String getCouponMemberIdOfDel();

//    public String id;
//    public int couponType;//平台 商家 1 2
//    public int superposition;//1 2
//    public String couponName;//券名称
//    public String denomination;//券面值
//    public String requirement;//券条件 满减 无门槛
//    public String timeStart;//时间开始
//    public String timeEnd;//时间结束
//    public String couponTip;//券提示
//
//
//    public String couponNote;//注意事项
//    public String couponMemberIdOfDel;//商家券特有参数







}
