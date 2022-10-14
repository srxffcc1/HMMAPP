package com.healthy.library.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

public class GoodsShop implements Serializable, Comparable<GoodsShop> {
    public String ytbDepartID;
    public String PersonID;

    public GoodsShop(String shopId, String shopName, String shopAddress) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
    }

    public String isSupportOverSold = "0";

    public GoodsShop() {
    }
    public GoodsShop(ShopDetailModel shopDetailModel) {
        this.shopId = shopDetailModel.id;
        this.shopName = shopDetailModel.shopName;
        this.shopAddress = shopDetailModel.addressDetails;
        this.distance= shopDetailModel.distance;
        this.cityName= shopDetailModel.cityName;
        this.provinceName= shopDetailModel.provinceName;
        this.addressAreaName= shopDetailModel.addressAreaName;
        this.longitude= shopDetailModel.longitude;
        this.latitude= shopDetailModel.latitude;
        this.chainShopName= shopDetailModel.chainShopName;
        this.userShopImgs= shopDetailModel.userShopImgs;
        this.envPicUrl = shopDetailModel.envPicUrl;
        this.isSupportOverSold= shopDetailModel.isSupportOverSold;
        this.appointmentPhone= shopDetailModel.appointmentPhone;
        this.merchantLogoUrl = shopDetailModel.merchantLogoUrl;
        this.merchantBrand = shopDetailModel.merchantBrand;
        this.ytbDepartID= shopDetailModel.getYtbDepartID();
        this.shopType= shopDetailModel.shopType+"";
        this.shopFlag=shopDetailModel.shopFlag;
    }
    public List<ShopDetailModel.UserShopImgsBean> userShopImgs;
    public String cityName;
    public String provinceName;
    public String addressAreaName;
    public double longitude;
    public double latitude;
    public String chainShopName;
    public String envPicUrl;
    public String shopFlag;// 1：总部统管门店，2：自营门店，3：异业门店


    public int mchId;
    public String mchName;

    public String appointmentPhone;

    public String shopId;

    public String shopName;

    public String shopChainName;

    public String shopType;

    public String shopBrandName;
    private String contactPhone;

    public double distance;

    public double fee;
    private double postageFee;

    /**
     * 门店品牌名称
     */
    public String merchantBrand;
    /**
     * 门店品牌logo
     */
    public String merchantLogoUrl;

    public double getFee() {
        if (fee == 0) {
            return postageFee;
        } else {
            return fee;
        }
    }

    public String shopAddress;

    public String getShopAddressDetail() {
        if(provinceName!=null){

            return provinceName + cityName + addressAreaName + shopAddress;
        }else {
            return "";
        }
    }

    public double minAmount;

    public String getAppointmentPhone() {
        if (TextUtils.isEmpty(appointmentPhone)) {
            return contactPhone;
        }
        return appointmentPhone;//转为服务电话
    }


    @Override
    public int compareTo(GoodsShop o) {
        return (int) (distance - o.distance);
    }
}
