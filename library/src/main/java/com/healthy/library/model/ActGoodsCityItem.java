package com.healthy.library.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ActGoodsCityItem implements MultiItemEntity {
    public String goodsId;

    public int goodsType;
    public int distance;

    public String shopId;
    public String shopName;
    public String shopAddress;

    public String isActivityGoods;

    public String addressProvince;
    public String addressCity;
    public String addressArea;
    public String filePath;

    public String goodsTitle;

    public double getPrice() {
        if (goodsType == 1) {//
            return storePrice;
        } else {
            return retailPrice;
        }
    }

    public String skuId;


    public String skuName;
    public String marketingType;
    public String marketingPrice;
    public String tagName;

    public double platformPrice;

    public double storePrice;

    public double retailPrice;

    public String courseFlag;

    public String courseId;


    @Override
    public int getItemType() {
        return 3;
    }
}
