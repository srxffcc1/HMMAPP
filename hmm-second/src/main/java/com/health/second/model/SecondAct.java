package com.health.second.model;

import android.text.TextUtils;

import com.healthy.library.model.Goods2DetailKick;
import com.healthy.library.model.Goods2DetailPin;
import com.healthy.library.model.NewUserListModel;

public class SecondAct {
    public String goodsId;
    public String marketingPrice;
    public double appLimitedPrice;

    public String getMarketingPrice() {
        if(appLimitedPrice!=0){
            return appLimitedPrice+"";
        }
        return marketingPrice;
    }

    public int marketingType;
    public int virtualSales;
    public int salesMin;
    public int salesMax;
    public String beginTime;
    public String endTime;
    public int sales;
    public int maxInventory;
    public int availableInventory;
    public String goodsTitle;
    public String filePath;
    public double retailPrice;
    public double storePrice;
    public double platformPrice;
    public Goods2DetailKick.BargainInfo bargainInfo;
    public Goods2DetailPin.Assemble assembleInfo;
    public NewUserListModel flashSaleInfo;
    public String getMerchantName() {
        if(!TextUtils.isEmpty(merchantShortName)){
            return merchantShortName;
        }
        return merchantName;
    }

    private String merchantName;
    private String merchantShortName;
}
