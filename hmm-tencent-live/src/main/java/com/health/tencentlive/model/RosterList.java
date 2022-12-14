package com.health.tencentlive.model;

import android.text.TextUtils;

import java.util.List;

public class RosterList {

    public String courseId;
    public LiveCourseDtoBean liveCourseDto;
    public List<LiveBenefitAllItemDtosBean> liveBenefitAllItemDtos;

    public class LiveCourseDtoBean {
        public String id;
        public String merchantId;
        public String merchantName;
        public String shopId;
        public String anchormanId;
        public String roomId;
        public String groupId;
        public String courseTitle;
        public String courseIntroduce;
        public String picUrl;
        public int type;
        public String category;
        public String courseLivePrice;
        public String courseOrderPrice;
        public int isBringGoods;
        public int status;
        public int isClosed;
        public String beginTime;
        public String actualBeginTime;
        public String endTime;
        public String pushAddress;
        public String pullAddress;
        public String[] activityIdList;
        public String videoUrl;
        public String fileId;
        public int livePlatform;
        public int timesWatched;
        public int timesWatchedReplay;
        public String numWatched;
        public String numWatchedReplay;
        public int isDebug;
        public int isPlayback;
        public int version;
        public int isDelete;
        public String createUser;
        public String createTime;
        public String updateUser;
        public String updateTime;
        public String liveAnchorman;
        public String liveRoom;
        public String subscribeCount;
        public String fansCount;
        public String shareCount;
        public String personIds;
        public String countLiveHelpFromMember;
        public String countLiveHelpMember;
    }

    public class LiveBenefitAllItemDtosBean {
        public int num;
        public int itemType;
        public String courseId;
        public int itemRealId;
        public DetailBean detail;
        public int status;
        public int liveActivityType;
        public String winId;
        public String helpId;
        public String helpItemId;
        public String liveBenefitId;
        public String benefitId;

        public class DetailBean {
            public BasicBean basic;
            public int id;
            public String code;
            public String name;
            public int type;
            public String goodsType;
            public int availableInventory;
            public double platformPrice;
            public int count;
            public String denomination;
            public String goodsSpecStr;
            public int limitType;
            public int couponType;
            public String limitAmount;
            public int criterionType;
            public int overlay;
            public int usableGoodsRestrict;
            public int usableShopRestrict;
            public int usableMemberLevelRestrict;
            public String comment;
            public String merchantId;
            public String shopId;
            public String merchantName;
            public int status;
            public String createTime;
            public String couponName;
            public String latestUpdateTime;
            public String flag;
            public String goodsIds;
            public String usableStartTime;
            public String usableEndTime;
            public String popNo;
            public String categoryName;
            public String departId;
            public String brandName;
            public String goodsId;
            public String goodsChildId;
            public String goodsImage;
            public String[] shopIds;
            public String goodsTitle;

            public class BasicBean {
                public int id;
                public String code;
                public String name;
                public int type;
                public String goodsType;
                public int availableInventory;
                public double platformPrice;
                public int count;
                public String denomination;
                public String goodsSpecStr;
                public int limitType;
                public int couponType;
                public String limitAmount;
                public int criterionType;
                public int overlay;
                public int usableGoodsRestrict;
                public int usableShopRestrict;
                public int usableMemberLevelRestrict;
                public String comment;
                public String merchantId;
                public String merchantName;
                public int status;
                public String createTime;
                public String couponName;
                public String latestUpdateTime;
                public String flag;
                public String goodsIds;
                public String usableStartTime;
                public String usableEndTime;
                public String popNo;
                public String categoryName;
                public String departId;
                public String brandName;
                public String goodsId;
                public String goodsChildId;
                public String goodsImage;
                public String[] shopIds;
                public String goodsTitle;
                public String shopName;
                public int syncStatus;

                public String getCouponDenomination() {
                    return (Double.parseDouble(denomination) * (count == 0 ? 1 : count)) + "";
                }

                public String getCouponName() {
                    if (limitType == -2) {
                        return "";
                    }
                    if (limitType == 0) {
                        return "????????? " + "???" + denomination + "???";
                    } else {
                        return "???" + limitAmount + "???" + denomination + "???";
                    }
                }
                public String getCouponUseName() {
                    if(limitType==-2){
                        return couponName;
                    }
                    if(!TextUtils.isEmpty(name)){
                        return name;
                    }
                    if (usableGoodsRestrict == 0) {//?????????

                        return "????????????????????????";
                    } else if (usableGoodsRestrict == 1) {//??????????????????
                        return "????????????????????????";
                    } else if (usableGoodsRestrict == 2) {//??????????????????
                        return "????????????" + categoryName + "??????";
                    } else if (usableGoodsRestrict == 3) {//??????????????????
                        return "????????????" + brandName + "????????????";
                    } else {//??????????????????
                        return "??????" + shopName + "??????";
                    }
                }

                public String getCouponTypeName() {
                    if (couponType == 1) {//?????????
                        return "?????????";
                    } else if (couponType == 2) {//??????????????????
                        return "?????????";
                    } else {
                        return "?????????";
                    }
                }

                public String getCouponLimitName() {
                    if (limitType == -2) {
                        return couponName;
                    }
                    if (usableGoodsRestrict == 0) {//?????????
                        return "????????????????????????";
                    } else if (usableGoodsRestrict == 1) {//??????????????????
                        return "????????????????????????";
                    } else if (usableGoodsRestrict == 2) {//??????????????????
                        return "????????????" + categoryName + "??????";
                    } else if (usableGoodsRestrict == 3) {//??????????????????
                        return "????????????" + brandName + "????????????";
                    } else {//??????????????????
                        return "??????" + shopName + "??????";
                    }
                }
            }
        }
    }
}
