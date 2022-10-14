package com.healthy.library.model;

import android.text.TextUtils;

public class MallCoupon {

        public int merchantCouponId;
        public int denomination;
        public int couponType;
        public int havingStatus;
        public int superposition;
        public String overPayment;
        public String couponTimeStart;
        public String couponTimeEnd;
        public int limitCollar;
        public int limitCollarCount;
        public String shopName;
        public String shopId;
        public String tagStatus;
        public String availableDays;
        public String getRequirement() {

                if(TextUtils.isEmpty(overPayment)){

                        return "无门槛";
                }else {
                        return "满"+overPayment+"可用";
                }

        }
}
