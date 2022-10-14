package com.healthy.library.model;

import android.text.TextUtils;

import com.healthy.library.LibApplication;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;

import java.io.Serializable;
import java.util.List;

public class Kick implements Serializable {

    public String bargainMemberId;
    public String id;
    public int goodsId;
    public String goodsTitle;
    public String goodsImage;
    public double goodsStorePrice;
    public double goodsPlatformPrice;
    public double floorPrice;
    public double retailPrice;
    public int bargainTimeLength;
    public int goodsType;
    public int joinNum;
    public int joinStatus;
    public double discountMoney;
    public String bargainNum;
    public String joinTime;
    //    public int shopId;
    public String endTime;
    public String distance;
    public String addressDetails;
    public int bargainStatus;
    public int bargainInventory;
    public String goodsChildId;
    public String goodsSpecStr;
    public String merchantId;

    public String getMarketingShopId() {
        if(TextUtils.isEmpty(marketingShopId)){//访问门店
            return SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP);
        }else {
            return marketingShopId;
        }
    }

    private String marketingShopId;
    public String marketingGoodsChildId;
    public String mapMarketingGoodsId;
    public String getMerchantName() {
        if(!TextUtils.isEmpty(merchantShortName)){
            return merchantShortName;
        }
        return merchantName;
    }

    private String merchantName;
    private String merchantShortName;

    public List<String> faceUrlList;

    public String getDeliveryShopId() {
        if(TextUtils.isEmpty(deliveryShopId)){
            return getMarketingShopId();
        }
        return deliveryShopId;
    }



    private String deliveryShopId;
    public String deliveryShopName;

}
