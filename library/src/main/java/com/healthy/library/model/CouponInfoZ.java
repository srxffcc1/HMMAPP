package com.healthy.library.model;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.healthy.library.interfaces.IHmmCoupon;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponInfoZ implements IHmmCoupon, Serializable {
    private String activityId;

    public String id;

    private int couponId;

    private String couponName;

    private int couponType = 2;

    private String couponCode;

    private String denomination;

    private int limitType;

    public String limitAmount;

    private String comment;//使用说明

    public int count = 1;

    public int isShareGift=0;

    public int isVoteGift=0;

    public CouponInfoZ() {
    }

    public CouponInfoZ(GoodsBasketCell o) {
        id = o.getGoodsShopId()+o.getGoodsMarketingId() + o.getGoodsBarCode() + o.goodsTitle + o.CardNo;
        couponId = 137;
        limitType = -2;
        couponType = 3;
        couponName = o.goodsTitle;
        count = o.getGoodsQuantityInBasket();
        denomination = new BigDecimal(o.goodsMarketingDTO.marketingPrice).multiply(new BigDecimal(-1)).doubleValue() + "";
    }

    public String getCouponUseTip() {//获得使用说明
        if (usableShopRestrict == 1) {//限制门店的
            if (TextUtils.isEmpty(comment)) {
                return "限:" + shopName;
            }
            return comment + "\n限:" + shopName;
        }
        return comment;
    }

    public String getCouponComment() {
        if (comment == null || TextUtils.isEmpty(comment)) {
            return "";
        }
        return comment;
    }

    private int usableGoodsRestrict;

    private int quotaType;

    private int quota;

    private String categoryName;

    private String brandName;

    private String shopName;

    private int todayReceivedCount;

    private int allReceivedCount;

    public String usableStartTime;

    public int status;

    public CouponInfoZ setStatus(int status) {
        this.status = status;
        return this;
    }

    public String usableEndTime;

    public int usableShopRestrict;

    private String goodsType;

    private int couponQuantity;

    public int criterionType;//零售价还是平台价

    public int availableCount;//可用数量 去除了 已用的后台进行筛选了

    public String termOfValidity;

    public Bitmap couponImg;//优惠券核销二维码


    public List<GoodsBasketCell> goodsBasketCellList = new ArrayList<>();

    @Override
    public String getCouponId() {
        return couponId + "";
    }

    @Override
    public String getUseId() {
        return id;
    }

    @Override
    public int getCouponType() {
        return couponType;
    }

    @Override
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

    @Override
    public String getCouponUseName() {
        if (limitType == -2) {
            return couponName;
        }
        if (!TextUtils.isEmpty(couponName)) {
            return couponName;
        }
        if (usableGoodsRestrict == 0) {//通用券
//            if(usableShopRestrict==1){
//
//            }
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

    @Override
    public String getCouponUseLimitName() {
        if (limitType == -2) {
            return couponName;
        }
        if (usableGoodsRestrict == 0) {//通用券
//            if(usableShopRestrict==1){
//
//            }
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

    @Override
    public boolean isSupportSuperposition() {
        return true;
    }

    @Override
    public String getCouponNormalName() {
        if (limitType == -2) {
            return "";
        }
        if (limitType == 0) {
            return "无门槛 " + "减" + denomination + "元";
        } else {
            return "满" + limitAmount + "减" + denomination + "元";
        }
    }

    @Override
    public String getCouponRemark() {
        return null;
    }

    @Override
    public String getCouponDenomination() {
        return (Double.parseDouble(denomination) * (count == 0 ? 1 : count)) + "";
    }

    public double getCouponDenominationDouble() {
        return Double.parseDouble(denomination) * (count == 0 ? 1 : count);
    }

    @Override
    public String getOverPayment() {
        return limitAmount;
    }

    @Override
    public String getRequirement() {
        if (limitType == -2) {
            return "";
        }
        if (limitType == 0) {
            return "无门槛";
        } else {
            return "满" + limitAmount + "元可用";
        }
    }

    @Override
    public String getTimeValidityStart() {
        return usableStartTime;
    }

    @Override
    public String getTimeValidityEnd() {
        return usableEndTime;
    }

    @Override
    public String getTimeValidity() {
        if (limitType == -2) {
            return "";
        }
        if (TextUtils.isEmpty(getTimeValidityStart())) {
            if (!TextUtils.isEmpty(termOfValidity)) {//识别领取后几天有效
                return termOfValidity;
            }
            return getTimeValidityEnd() == null ? "" : new SimpleDateFormat("yyyy.MM.dd").format(new Date()) + "-" + getTimeValidityEnd().split(" ")[0].replace("-", ".") + "";
        } else {

            return getTimeValidityStart().split(" ")[0].replace("-", ".") + "-" + getTimeValidityEnd().split(" ")[0].replace("-", ".");
        }
    }

    @Override
    public boolean isHold() {
        return false;
    }

    @Override
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

    @Override
    public int getCriterionType() {
        return criterionType;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public List<CouponGoodsModel> couponGoodsModelList = new ArrayList<>();

    public void setGoodsCouponKey() {
        if (goodsBasketCellList != null && goodsBasketCellList.size() > 0) {
            for (int i = 0; i < goodsBasketCellList.size(); i++) {
                goodsBasketCellList.get(i).getCouponInfoKeyList().add(this);
            }
        }
    }

    public boolean isCouponIsKey() {
        if (getRequirement().contains("无门槛")) {
            return true;
        } else {
            double limitAmount = Double.parseDouble(getOverPayment());
            BigDecimal moneyALL = new BigDecimal(0);
            for (int i = 0; i < goodsBasketCellList.size(); i++) {
                GoodsBasketCell goodsBasketCell = goodsBasketCellList.get(i);
                moneyALL = moneyALL.add(new BigDecimal(goodsBasketCell.getStandPrice(criterionType)).multiply(new BigDecimal(goodsBasketCell.getGoodsQuantity())));
            }
            if (limitAmount <= moneyALL.doubleValue()) {//满足需求 则视为可以直接用的优惠券
                return true;
            }

        }
        return false;
    }
}
