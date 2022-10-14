package com.healthy.library.model;

import android.text.TextUtils;

import java.io.Serializable;

public class CouponUnder implements Serializable {

    public CouponUnder(Coupon coupon) {
        Number = coupon.Number;
        GoodsID = coupon.GoodsID;
        SendMode = coupon.SendMode;
        shopName = TextUtils.isEmpty(coupon.shopName) ? null : coupon.shopName;
        DepartID = coupon.DepartID;
        PersonID = coupon.PersonID;
    }

    public String Number;

    public String GoodsID;

    public String SendMode;
    public String shopName;
    public String DepartID;
    public String PersonID;
    public String CardGiftType;//会员触达礼包分类代码,从9605接口里面获取

}