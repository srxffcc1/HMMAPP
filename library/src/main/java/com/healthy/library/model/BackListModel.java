package com.healthy.library.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class BackListModel implements MultiItemEntity {

    public String refundId;
    public String refundNum;
    public String memberId;
    public String memberName;
    public String memberPhone;
    public String memberFaceUrl;
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
    public String deliveryShopId;
    public String deliveryShopName;
    public String deliveryShopChainName;
    public String deliveryShopBrandName;
    public String deliveryShopAppointmentPhone;
    public String deliveryShopAddress;
    public String refundAmount;
    public String refundPoints;
    public String refundReason;
    public String refundComment;
    public int disposeAgree;
    public String disposeAmount;
    public String disposePoints;
    public String disposeMember;
    public String disposeComment;
    public String disposeTime;
    public String payPoints;
    public String payPointsStatus;
    public String payPointsResult;
    public String payPointsTime;
    public String payAmount;
    public int payAmountType;
    public String payAmountStatus;
    public String payAmountResult;
    public String payAmountTime;
    public String payAmountOutTradeNo;
    public String payAmountTransactionNo;
    public String deleted;
    public int canceled;
    public int status;
    public String cancelTime;
    public String deletedTime;
    public String createTime;
    public String latestUpdateTime;

    public List<RefundDetailListBean> refundDetailList;
    public List<RefundAttachListBean> refundAttachList;
    public List<RefundLogListBean> refundLogList;

    @Override
    public int getItemType() {
        if (status == -1 || status == -2 || status == 3) {
            return 5;
        } else {
            return status;
        }
    }

    public class RefundDetailListBean {
        public String refundDetailId;
        public String refundId;
        public String refundNum;
        public String orderDetailId;
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
        public String goodsMarketingGoodsId;
        public String goodsMarketingGoodsSpecId;
        public String goodsMarketingType;
        public String goodsMarketingId;
        public String goodsTitle;
        public String goodsSpecDesc;
        public String goodsCategoryId;
        public String goodsCategoryName;
        public String goodsBrandId;
        public String goodsBrandName;
        public String goodsImage;
        public String goodsBarCode;
        public String goodsExpireDay;
        public String isAddition;
        public String additionType;
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
        public String orderCreateTime;
        public String orderPaySuccessTime;
        public String orderDeliveryStatus;
        public int refundQuantity;
        public String refundAmount;
        public String refundPoints;
        public String createTime;
        public String latestUpdateTime;
    }

    public class RefundAttachListBean {
        public String attachId;
        public String refundId;
        public String attachUrl;
        public String createTime;
        public String latestUpdateTime;
    }

    public class RefundLogListBean {
        public String id;
        public String refundId;
        public String operator;
        public String operateTime;
        public String comment;
        public String createTime;
        public String latestUpdateTime;
    }
}
