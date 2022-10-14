package com.healthy.library.model;

import android.text.TextUtils;

import com.healthy.library.interfaces.PlatformMerchantCoupon;

public class PlatformCouponInfo implements PlatformMerchantCoupon {

        public int id;
        public String ruleIntroduce;
        public String activityName;
        public String activityId;
        public String couponType;
        public String denomination;
        public String scope;
        public String memberId;
        public String validityTermStart;
        public String validityTermEnd;
        public String categoryNo;
        public String categoryName;
        public String couponMemberId;
        @Override
        public String getMerchantCouponId722() {
                return couponMemberId;
        }

        public String getOverPayment() {
                return overPayment;
        }

        public String overPayment;
        public String couponCode;
        public int itemType=1;
        public boolean isShow=false;
        public String availableDays;
        public String availableDayStart;
        public String availableDayEnd;

        @Override
        public String getId() {
                return id+"";
        }

        @Override
        public String getMerchantCouponId() {
                return activityId;
        }

        @Override
        public int getCouponType() {
                return 1;
        }

        @Override
        public int getSuperposition() {
                return 0;
        }

        @Override
        public String getCouponName() {
                return activityName;
        }

        @Override
        public String getDenomination() {
                return denomination;
        }

        @Override
        public String getRequirement() {
                if(TextUtils.isEmpty(overPayment)){
                        return "无门槛";
                }else {
                        return "满"+overPayment+"可用";
                }
        }

        @Override
        public String getTimeStartAndEnd() {
                if(TextUtils.isEmpty(validityTermStart)){

                        return "领取后"+availableDays+"天有效";
                }else {

                        return ""+validityTermStart+" 至 "+validityTermEnd;
                }
        }

        @Override
        public String getTimeStart() {
                return validityTermStart;
        }

        @Override
        public String getTimeEnd() {
                return validityTermEnd;
        }

        @Override
        public String getCouponTip() {
                return scope;
        }
        String couponNote="此券不允许叠加使用";
        @Override
        public String getCouponNote() {
                return couponNote;
        }

        @Override
        public void setCouponNote(String couponNote) {
                this.couponNote=couponNote;
        }

        @Override
        public String getCouponMemberIdOfDel() {
                return couponCode;
        }
}
