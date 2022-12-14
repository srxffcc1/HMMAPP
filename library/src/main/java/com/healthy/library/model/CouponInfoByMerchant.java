package com.healthy.library.model;

import android.text.TextUtils;

import com.healthy.library.interfaces.PlatformMerchantCoupon;

public class CouponInfoByMerchant implements PlatformMerchantCoupon {
        public int id;
        public int denomination;
        public int couponType;
        public int dayHandType;
        public int dayHandCount;
        public int limitCollar;
        public int limitCollarCount;
        public String activityStartTime;
        public String activityEndTime;
        public int activityTime;
        public String couponTimeStart;
        public String couponTimeEnd;
        public String availableDays;
        public int superposition;
        public int preferentialSharing;
        public String activityId;
        public String ruleIntroduce;
        public String activityName;
        public String scope;
        public String memberId;
        public String validityTermStart;
        public String validityTermEnd;
        public String categoryNo;
        public String categoryName;
        public String overPayment;
        public boolean isShow=false;
        public String availableDayStart;
        public String availableDayEnd;
        public int itemType=1;
        public String merchantCouponId;
        public String shopName;
        public String couponMemberIdOfDel;

        public String getOverPayment() {
                return overPayment;
        }
        @Override
        public String getId() {
                return couponMemberIdOfDel+"";
        }

        @Override
        public String getMerchantCouponId() {
                return merchantCouponId;
        }

        @Override
        public String getMerchantCouponId722() {
                return merchantCouponId;
        }


        @Override
        public int getCouponType() {
                return 2;
        }

        @Override
        public int getSuperposition() {
                return superposition;
        }

        @Override
        public String getCouponName() {
                return shopName;
        }

        @Override
        public String getDenomination() {
                return denomination+"";
        }

        @Override
        public String getRequirement() {

                if(TextUtils.isEmpty(overPayment)){

                        return "?????????";
                }else {
                        return "???"+overPayment+"??????";
                }

        }

        @Override
        public String getTimeStartAndEnd() {
                if(TextUtils.isEmpty(validityTermStart)){
                        return "?????????"+availableDays+"?????????";
                }else {
                        return ""+couponTimeStart+" ??? "+couponTimeEnd;
                }
        }

        @Override
        public String getTimeStart() {
                return couponTimeStart;
        }

        @Override
        public String getTimeEnd() {
                return couponTimeEnd;
        }

        @Override
        public String getCouponTip() {
                return "?????????????????????";
        }

        String couponNote="???????????????????????????";
        @Override
        public String getCouponNote() {
                if(superposition==1){
                        couponNote="";
                }else {
                        couponNote="???????????????????????????";
                }
                return couponNote;
        }

        @Override
        public void setCouponNote(String couponNote) {
                this.couponNote=couponNote;
        }

        @Override
        public String getCouponMemberIdOfDel() {
                return couponMemberIdOfDel;
        }
}
