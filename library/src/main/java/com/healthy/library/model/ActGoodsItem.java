package com.healthy.library.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ActGoodsItem implements MultiItemEntity {
    public String goodsId;

    public int goodsType;

    public String shopId;

    public String isActivityGoods;


    public String filePath;

    public String goodsTitle;
    public String bargainId;
    public String assembleId;

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
    public String tagName;//标签名称（逗号分隔）

    public String getTagFirst(){
        if(tagName==null||"null".equals(tagName)||"".equals(tagName)){
            return null;
        }
        return tagName.split(",")[0];
    }

    public double platformPrice;

    public double storePrice;

    public double retailPrice;

    public String courseFlag;

    public String courseId;


    @Override
    public int getItemType() {
        return goodsType;
    }
}
