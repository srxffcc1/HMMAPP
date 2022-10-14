package com.healthy.library.model;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostActivityList {

    public int id;
    public int postingId;
    public int goodsId;
    public int goodsSource;
    public String createTime;
    public String updateTime;
    public int sortNum;
    public List<ActivityCoupon> activityCoupon;
    public NewGoodsDTO newGoodsDTO;

    public class ActivityCoupon {
        public String id;
        public String code;
        public String name;
        public int type;
        public String denomination;
        public int limitType;
        public String limitAmount;
        public int criterionType;
        public int overlay;
        public int usableGoodsRestrict;
        public int usableShopRestrict;
        public int usableMemberLevelRestrict;
        public String comment;
        public int merchantId;
        public String merchantName;
        public int status;
        public String createTime;
        public String latestUpdateTime;
        public String flag;
        public String goodsIds;
        public String usableStartTime;
        public String usableEndTime;
        public String popNo;
        public String departId;
        public String goodsId;
        public int syncStatus;
        private String activityId;
        private String couponId;
        private String couponName;
        private int couponType = 2;
        private String couponCode;

        public String getComment() {//获得使用说明
            if (usableShopRestrict == 1) {//限制门店的
                if (TextUtils.isEmpty(comment)) {
                    return "限:" + shopName;
                }
                return comment + "\n限:" + shopName;
            }
            return comment;
        }

        private int quotaType;
        private int quota;
        private String categoryName;
        private String brandName;
        private String shopName;
        private int todayReceivedCount;
        private int allReceivedCount;
        private String goodsType;
        public int couponQuantity;
        public int availableCount;//可用数量 去除了 已用的后台进行筛选了
        public String termOfValidity;

        public String getCouponId() {
            return couponId + "";
        }

        public int getCouponType() {
            return couponType;
        }

        public String getCouponTypeName() {
            if (couponType == 1) {//通用券
                return "平台券";
            } else if (couponType == 2) {//指定品牌可用
                return "商品券";
            } else {
                return "通用券";
            }
        }

        public String getActivityId() {
            return activityId;
        }

        public int getCouponQuantity() {
            return couponQuantity;
        }

        public String getCouponLimitName() {
            if (usableGoodsRestrict == 0) {//通用券
                return "母婴商城全场通用";
            } else if (usableGoodsRestrict == 1) {//指定商品可用
                return "仅限购买指定商品";
            } else if (usableGoodsRestrict == 2) {//指定类别可用
                return "仅限购买" + categoryName + "可用";
            } else if (usableGoodsRestrict == 3) {//指定品牌可用
                return "仅限购买" + brandName + "品牌可用";
            } else {//指定门店可用
                return "仅限" + shopName + "可用";
            }
        }

        public boolean isSupportSuperposition() {
            return true;
        }

        public String getCouponName() {
            if (limitType == 0) {
                return "无门槛 " + "减" + denomination + "元";
            } else {
                return "满" + limitAmount + "减" + denomination + "元";
            }
        }

        public String getCouponUseTip() {
            return null;
        }

        public String getCouponRemark() {
            return null;
        }

        public String getCouponDenomination() {
            return denomination;
        }

        public double getCouponDenominationDouble() {
            return Double.parseDouble(denomination);
        }

        public String getOverPayment() {
            return limitAmount;
        }

        public String getRequirement() {
            if (limitType == 0) {
                return "无门槛";
            } else {
                return "满" + limitAmount + "元可用";
            }
        }

        public String getTimeValidityStart() {
            return usableStartTime;
        }

        public String getTimeValidityEnd() {
            return usableEndTime;
        }

        public String getTimeValidity() {
            if (TextUtils.isEmpty(getTimeValidityStart())) {
                if (!TextUtils.isEmpty(termOfValidity)) {//识别领取后几天有效
                    return termOfValidity;
                }
                return getTimeValidityEnd() == null ? "" : new SimpleDateFormat("yyyy.MM.dd").format(new Date()) + "-" + getTimeValidityEnd().split(" ")[0].replace("-", ".") + "";
            } else {

                return getTimeValidityStart().split(" ")[0].replace("-", ".") + "-" + getTimeValidityEnd().split(" ")[0].replace("-", ".");
            }
        }

        public boolean isCanReceive() {//判断能不能领
            if (couponQuantity == 0) {//当前券为0那就不能领了
                return false;
            } else {
                if (quotaType == 0) {
                    return true;
                } else if (quotaType == 1 && quota > allReceivedCount) {
                    return true;
                } else if (quotaType == 2 && quota > todayReceivedCount) {
                    return true;
                }
            }
            return false;
        }
    }

    public class NewGoodsDTO {
        public String id;
        public String goodsNo;
        public String goodsName;
        public String goodsTitle;
        public String specName;
        public String specValue;
        public String retailPrice;
        public String storePrice;
        public String platformPrice;
        public String goodsType;
        public String headImage;
        public String categoryName;
        public String brandName;
    }
}
