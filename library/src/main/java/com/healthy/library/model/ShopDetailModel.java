package com.healthy.library.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

public class ShopDetailModel implements Comparable<ShopDetailModel>, Serializable {
    public ShopDetailModel() {
    }

    public String id;
    public String userId;
    public String merchantCode;
    public String brandName;
    public String shopName;
    public String chainShopName;
    public String addressProvince;
    public String addressCity;
    public String addressArea;
    public String cityName;
    public String provinceName;
    public String addressAreaName;
    public String addressDetails;
    public String businessHourStart;
    public String businessHourEnd;
    public String businessStatus;
    public String appointmentPhone;
    public String enterExpense;
    public String remark;
    public List<String> shopBusinessOfArea;
    public int shopType;
    public double longitude;
    public double latitude;
    public String createTime;
    public String updateTime;
    public String categoryNameOnlyOne;
    public String addressDetailsNew;
    public String addRemark;
    public String categoryNoLevel1;
    public String categoryNoLevel2;
    public String auditStatus;
    public String maxDiscountGoods;
    public String topSaleGoods;
    public double distance = 9999999;
    public String maxGoodsDiscount;
    public String overallRatingAvg;
    public String courseId;
    public String courseFlag;
    public String merchantCouponInfoByMerchantCouponIdDTOS;
    public String shopFlag;// 1：总部统管门店，2：自营门店，3：异业门店
    public String shopTag;
    public String principal;
    //是否支持超卖 0-不支持 1-支持
    public String isSupportOverSold = "0";
    public String departId;
    public String headerImg;
    public String shopIntroduce;//门店介绍
    public String envPicUrl;//门店照片
    public String businessLicensePicUrl;//门店资质照片
    public List<UserShopImgsBean> userShopImgs;//门店照片合集
    public List<BusinessLicenseImgBean> businessLicenseImg;

    public String getYtbDepartID() {
        if(TextUtils.isEmpty(ytbDepartID)){
            return id;
        }
        return ytbDepartID;
    }

    public void setYtbDepartID(String ytbDepartID) {
        this.ytbDepartID = ytbDepartID;
    }

    private String ytbDepartID;
    public String merchantType;//1.异业商家  2.合伙人商家
    public String PersonID;//新人、节日礼包适用此参数

    /**
     * 门店品牌名称
     */
    public String merchantBrand;
    /**
     * 门店品牌logo
     */
    public String merchantLogoUrl;

    /**
     * 商家手机号
     */
    public String mobile;

    public ShopDetailModel(GoodsShop newStoreDetialModel) {
        this.id = newStoreDetialModel.shopId;
        this.shopName = newStoreDetialModel.shopName;
        this.addressDetails = newStoreDetialModel.shopAddress;
        this.distance = newStoreDetialModel.distance;
        this.cityName = newStoreDetialModel.cityName;
        this.provinceName = newStoreDetialModel.provinceName;
        this.addressAreaName = newStoreDetialModel.addressAreaName;
        this.longitude = newStoreDetialModel.longitude;
        this.latitude = newStoreDetialModel.latitude;
        this.chainShopName = newStoreDetialModel.chainShopName;
        this.envPicUrl = newStoreDetialModel.envPicUrl;
        this.userShopImgs = newStoreDetialModel.userShopImgs;
        this.isSupportOverSold = newStoreDetialModel.isSupportOverSold;
        this.appointmentPhone = newStoreDetialModel.appointmentPhone;
        this.merchantLogoUrl = newStoreDetialModel.merchantLogoUrl;
        this.merchantBrand = newStoreDetialModel.merchantBrand;
        this.ytbDepartID=newStoreDetialModel.ytbDepartID;
        this.shopFlag=newStoreDetialModel.shopFlag;
        this.ytbDepartID = newStoreDetialModel.ytbDepartID;
        this.PersonID = newStoreDetialModel.PersonID;
    }

    @Override
    public int compareTo(ShopDetailModel o) {
        return (int) (this.distance - o.distance);
    }

    public class UserShopImgsBean implements Serializable {
        public String id;
        public String url;
        public String type;
    }

    public class BusinessLicenseImgBean implements Serializable {
        public String id;
        public String url;
        public String type;
    }

    public class ShopBusinessesDateBean implements Serializable {
        public String timeType;
        public String week;
        public String businessMonthStart;
        public String businessMonthEnd;
        public String noBusiness;
        public String businessHourStart;
        public String businessHourEnd;
    }
}
