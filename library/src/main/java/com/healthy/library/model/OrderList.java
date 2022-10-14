package com.healthy.library.model;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class OrderList implements MultiItemEntity {

    public String orderId;
    public String orderNum;
    public String orderSource;
    private String orderRace;
    public String orderType;
    public String orderPlatform;
    public String memberId;
    public String memberName;
    public String memberPhone;
    public String memberFaceUrl;
    public String merchantId;
    public String merchantType;
    public String merchantName;
    public String merchantProvince;
    public String merchantProvinceName;
    public String merchantCity;
    public String merchantCityName;
    public String merchantDistrict;
    public String merchantDistrictName;
    public String merchantAddress;
    public String shopId;
    public String shopName;
    public String shopChainName;
    public String shopBrandName;
    public String shopAppointmentPhone;
    public String shopAddress;
    public String totalPayAmount;
    public String totalPayPoints;
    public String latestPayTime;
    public String payId;
    public int payType;
    public String paySuccessTime;
    public String payStatus;
    public String payPointsYtbAppId;
    public String payPointsSuccessTime;
    public String payPointsStatus;
    public String cancelStatus;
    public String cancelTime;
    public int isMain;
    public String mainId;
    private String orderStatus;
    public String createTime;
    public String latestUpdateTime;
    public OrderChild orderChild;
    public OrderFather orderFather;

    public class OrderChild {

        public String orderId;
        public String orderNum;
        public String orderSource;
        public String orderRace;
        public String orderType;
        public String orderPlatform;
        public String memberId;
        public String memberName;
        public String memberPhone;
        public String memberFaceUrl;
        public String merchantId;
        public String merchantType;
        public String merchantName;
        public String merchantProvince;
        public String merchantProvinceName;
        public String merchantCity;
        public String merchantCityName;
        public String merchantDistrict;
        public String merchantDistrictName;
        public String merchantAddress;
        public String shopId;
        public String shopName;
        public String shopChainName;
        public String shopBrandName;
        public String shopAppointmentPhone;
        public String shopAddress;
        public String totalPayAmount;
        public String totalPayPoints;
        public String latestPayTime;
        public String payId;
        public String refundId;
        public String lastRefundId;
        public String payType;
        public String paySuccessTime;
        public String payStatus;
        public String payPointsYtbAppId;
        public String payPointsSuccessTime;
        public String payPointsStatus;
        public String cancelStatus;
        public String cancelTime;
        public String isMain;
        public String mainId;
        public String orderStatus;
        public String createTime;
        public String latestUpdateTime;
        public String totalGoodsAmount;
        public String totalGoodsPoints;
        public String totalDeliveryAmount;
        public String totalPreferentialAmount;
        public String totalReductionAmount;
        public String totalMerchantLimitCouponAmount;
        public String totalMerchantNoLimitCouponAmount;
        public String totalPlatformLimitCouponAmount;
        public String totalPlatformNoLimitCouponAmount;
        public String deliveryType;
        public String deliveryConsigneeName;
        public String deliveryConsigneePhone;
        public String deliveryConsigneeProvince;
        public String deliveryConsigneeProvinceName;
        public String deliveryConsigneeCity;
        public String deliveryConsigneeCityName;
        public String deliveryConsigneeDistrict;
        public String deliveryConsigneeDistrictName;
        public String deliveryConsigneeAddress;
        public String deliveryLatitude;
        public String deliveryLongitude;
        public String deliveryShopId;
        public String deliveryShopName;
        public String deliveryShopChainName;
        public String deliveryShopBrandName;
        public String deliveryShopAppointmentPhone;
        public String deliveryShopAddress;
        public String deliveryDate;
        public String deliveryTime;
        public String remark;
        public String ticket;
        public String ticketContent;
        public String merchantDeliveryStatus;
        public String merchantDeliveryTime;
        public String takeDeliveryStatus;
        public String takeDeliveryTime;
        public String residuePayTime;
        public int canRefund=1;

        public List<OrderDetailListBean> orderDetailList;

        public int getDeliverType() {
            return deliveryType != null ? Integer.parseInt(deliveryType) : 0;
        }

        public int getOrderType() {
            return orderType != null ? Integer.parseInt(orderType) : 0;
        }

        public int getOrderStatus() {
            return orderStatus != null ? Integer.parseInt(orderStatus) : 0;
        }

        public int getOrderRace() {
            if (orderRace == null || TextUtils.isEmpty(orderRace)) {
                return -1;
            } else if ("1".equals(orderRace) || "2".equals(orderRace)) {
                return 0;
            } else {
                return Integer.parseInt(orderRace);
            }
        }


    }

    public class OrderFather {
        public String orderId;
        public String orderNum;
        public String orderSource;
        public String orderRace;
        public String orderType;
        public String orderPlatform;
        public String memberId;
        public String memberName;
        public String memberPhone;
        public String memberFaceUrl;
        public String merchantId;
        public String merchantType;
        public String merchantName;
        public String merchantProvince;
        public String merchantProvinceName;
        public String merchantCity;
        public String merchantCityName;
        public String merchantDistrict;
        public String merchantDistrictName;
        public String merchantAddress;
        public String shopId;
        public String shopName;
        public String refundId;
        public String lastRefundId;
        public String shopChainName;
        public String shopBrandName;
        public String shopAppointmentPhone;
        public String shopAddress;
        public String totalPayAmount;
        public String totalPayPoints;
        public String latestPayTime;
        public String payId;
        public String payType;
        public String paySuccessTime;
        public String payStatus;
        public String payPointsYtbAppId;
        public String payPointsSuccessTime;
        public String payPointsStatus;
        public String cancelStatus;
        public String cancelTime;
        public String isMain;
        public String mainId;
        public String orderStatus;
        public String createTime;
        public String latestUpdateTime;
        public String totalGoodsAmount;
        public String totalGoodsPoints;
        public String totalDeliveryAmount;
        public String totalPreferentialAmount;
        public String totalReductionAmount;
        public String totalMerchantLimitCouponAmount;
        public String totalMerchantNoLimitCouponAmount;
        public String totalPlatformLimitCouponAmount;
        public String totalPlatformNoLimitCouponAmount;
        public String totalRefundAmount;
        public String totalRefundPoints;
        public String refundStatus;
        public String lastRefundTime;
        public String referralFrom;
        public String referral;
        public String residuePayTime;
        public int canRefund=1;

        public List<SubOrderListBean> subOrderList;

        public int getOrderStatus() {
            return orderStatus != null ? Integer.parseInt(orderStatus) : 0;
        }

        public int getOrderRace() {
            if (orderRace == null || TextUtils.isEmpty(orderRace)) {
                return -1;
            } else if ("1".equals(orderRace) || "2".equals(orderRace)) {
                return 0;
            } else {
                return Integer.parseInt(orderRace);
            }
        }

        public class SubOrderListBean {
            public String orderId;
            public String orderNum;
            public String orderSource;
            public String orderRace;
            public String orderType;
            public String orderPlatform;
            public String memberId;
            public String memberName;
            public String memberPhone;
            public String memberFaceUrl;
            public String merchantId;
            public String merchantType;
            public String merchantName;
            public String merchantProvince;
            public String merchantProvinceName;
            public String merchantCity;
            public String merchantCityName;
            public String merchantDistrict;
            public String merchantDistrictName;
            public String merchantAddress;
            public String shopId;
            public String shopName;
            public String shopChainName;
            public String shopBrandName;
            public String shopAppointmentPhone;
            public String shopAddress;
            public String totalPayAmount;
            public String totalPayPoints;
            public String latestPayTime;
            public String payId;
            public String payType;
            public String paySuccessTime;
            public String payStatus;
            public String payPointsYtbAppId;
            public String payPointsSuccessTime;
            public String payPointsStatus;
            public String cancelStatus;
            public String cancelTime;
            public String isMain;
            public String mainId;
            public String orderStatus;
            public String createTime;
            public String latestUpdateTime;
            public String totalGoodsAmount;
            public String totalGoodsPoints;
            public String totalDeliveryAmount;
            public String totalPreferentialAmount;
            public String totalReductionAmount;
            public String totalMerchantLimitCouponAmount;
            public String totalMerchantNoLimitCouponAmount;
            public String totalPlatformLimitCouponAmount;
            public String totalPlatformNoLimitCouponAmount;
            public String deliveryType;
            public String deliveryConsigneeName;
            public String deliveryConsigneePhone;
            public String deliveryConsigneeProvince;
            public String deliveryConsigneeProvinceName;
            public String deliveryConsigneeCity;
            public String deliveryConsigneeCityName;
            public String deliveryConsigneeDistrict;
            public String deliveryConsigneeDistrictName;
            public String deliveryConsigneeAddress;
            public String deliveryLatitude;
            public String deliveryLongitude;
            public String deliveryShopId;
            public String deliveryShopName;
            public String deliveryShopChainName;
            public String deliveryShopBrandName;
            public String deliveryShopAppointmentPhone;
            public String deliveryShopAddress;
            public String deliveryDate;
            public String deliveryTime;
            public String remark;
            public String ticket;
            public String ticketContent;
            public String merchantDeliveryStatus;
            public String unAllowRefundReason;
            public String merchantDeliveryTime;
            public String takeDeliveryStatus;
            public String takeDeliveryTime;

            public List<OrderDetailListBean> orderDetailList;

            public int getDeliverType() {
                return deliveryType != null ? Integer.parseInt(deliveryType) : 0;
            }

            public int getOrderType() {
                return orderType != null ? Integer.parseInt(orderType) : 0;
            }

            public int getOrderStatus() {
                return orderStatus != null ? Integer.parseInt(orderStatus) : 0;
            }
        }
    }

    public class OrderDetailListBean {
        public String orderDetailId;
        public String mainOrderId;
        public String mainOrderNum;
        public String subOrderId;
        public String subOrderNum;
        public String merchantId;
        public String merchantType;
        public String merchantName;
        public String merchantProvince;
        public String merchantProvinceName;
        public String merchantCity;
        public String merchantCityName;
        public String merchantDistrict;
        public String merchantDistrictName;
        public String merchantAddress;
        public String shopId;
        public String shopName;
        public String shopChainName;
        public String shopBrandName;
        public String shopAppointmentPhone;
        public String shopAddress;
        public String goodsSource;
        public String goodsType;
        public String goodsId;
        public String goodsSpecId;
        public String goodsMarketingType;
        public String goodsMarketingId;
        public String goodsMarketingGoodsId;
        public String goodsMarketingGoodsSpecId;
        public int goodsQuantity;
        public String goodsTitle;
        public String goodsSpecDesc;
        public String goodsCategoryId;
        public String goodsCategoryName;
        public String goodsBrandId;
        public String goodsBrandName;
        public String goodsImage;
        public String goodsBarCode;
        public String goodsExpiredDay;
        public String isAddition;
        public String additionType;
        public String goodsAmountModel;
        public String goodsPlatformAmount;
        public String goodsRetailAmount;
        public String goodsAmount;
        public String goodsPoints;
        public String goodsPreferentialAmount;
        public String goodsReductionAmount;
        public String goodsMerchantLimitCouponAmount;
        public String goodsMerchantNoLimitCouponAmount;
        public String goodsPlatformLimitCouponAmount;
        public String goodsPlatformNoLimitCouponAmount;
        public String goodsPayAmount;
        public String goodsPayPoints;
        public double totalTolerance;//误差价
        public String totalAmount;
        public String totalPoints;
        public String totalPreferentialAmount;
        public String totalReductionAmount;
        public String totalMerchantLimitCouponAmount;
        public String totalMerchantNoLimitCouponAmount;
        public String totalPlatformLimitCouponAmount;
        public String totalPlatformNoLimitCouponAmount;
        public String totalPayAmount;
        public String totalPayPoints;
        public String latestRefundId;
        public int refundTotalQuantity;
        public String refundTotalAmount;
        public String refundTotalPoints;
        public int refundCount;
        public String refundCancelCount;
        public int allowRefund;
        public int orderType = getItemType();
        public String unAllowRefundMessage;
        public String unAllowRefundReason;//如果这个不为空   那么说明提交过售后
        public String createTime;
        public String latestUpdateTime;
        public boolean isSelect = false;
        public int backNum = goodsQuantity;
        //新增的6位小数的字段  用来计算退款时缩小误差价
        public String rawTotalPreferentialAmount;
        public String rawTotalReductionAmount;
        public String rawGoodsPlatformNoLimitCouponAmount;
        public String rawGoodsPayPoints;
        public String rawTotalAmount;
        public String rawTotalPoints;
        public double rawTotalTolerance;
        public String rawRefundTotalAmount;
        public String rawRefundTotalPoints;
        public String rawGoodsPoints;
        public String rawGoodsPreferentialAmount;
        public String rawGoodsReductionAmount;
        public String rawGoodsPayAmount;
        public String rawGoodsMerchantLimitCouponAmount;
        public String rawGoodsMerchantNoLimitCouponAmount;
        public String rawGoodsPlatformLimitCouponAmount;
        public String rawTotalPayAmount;
        public String rawTotalPayPoints;
        public String rawGoodsPlatformAmount;
        public String rawGoodsRetailAmount;
        public String rawGoodsPlusAmount;
        public String rawGoodsAmount;
        public String rawTotalMerchantLimitCouponAmount;
        public String rawTotalMerchantNoLimitCouponAmount;
        public String rawTotalPlatformLimitCouponAmount;
        public String rawTotalPlatformNoLimitCouponAmount;
    }

    @Override
    public int getItemType() {
        if (Integer.parseInt(orderType) == 5) {//5积分 其他共用一个item
            return 5;
        } else {
            return 0;
        }
    }

    public int getOrderRace() {
        return Integer.parseInt(orderRace);
    }

    public int getOrderStatus() {
        return orderStatus != null ? Integer.parseInt(orderStatus) : 0;
    }
}
