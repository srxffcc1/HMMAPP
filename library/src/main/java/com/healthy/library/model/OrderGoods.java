package com.healthy.library.model;

import android.text.TextUtils;

import java.io.Serializable;

public class OrderGoods implements Serializable {
    public String goodsId;
    private String goodsMarketingType;

    public String getGoodsMarketingType() {
        if("null".equals(goodsMarketingType)|| goodsMarketingType==null|| TextUtils.isEmpty(goodsMarketingType.trim())){
            return null;
        }
        return goodsMarketingType;
    }

    private String goodsSpec;

    public String quantity;

    public String goodsSepecStr;//可能是多个

    public String goodsName;

    public String goodsImgPath;

    public String goodsType;

    public String goodsMoney;

    public String memberCartDetailId;

    private String goodsShopListString;

    public String getGoodsShopListString() {
        if("null".equals(goodsShopListString)|| goodsPoint==null||TextUtils.isEmpty(goodsShopListString.trim())){
            return null;
        }
        return goodsShopListString;
    }

    private String goodsMarketingGoodsSpec;


    private String goodsMarketingGoodsId;

    private String goodsPoint;

    public String getGoodsPoint() {
        if("null".equals(goodsPoint)|| goodsPoint==null||TextUtils.isEmpty(goodsPoint.trim())){
            return "0";
        }
        return goodsPoint;
    }

    public String getGoodsMarketingGoodsId() {
        if("null".equals(goodsMarketingGoodsId)|| goodsMarketingGoodsId==null||TextUtils.isEmpty(goodsMarketingGoodsId.trim())){
            return null;
        }
        return goodsMarketingGoodsId;
    }
    public String getGoodsSpec() {
        if("null".equals(goodsSpec)|| goodsSpec==null||TextUtils.isEmpty(goodsSpec.trim())){
            return null;
        }
        return goodsSpec;
    }

    public String getGoodsMarketingGoodsSpec() {
        if("null".equals(goodsMarketingGoodsSpec.trim())||goodsMarketingGoodsSpec==null|| TextUtils.isEmpty(goodsMarketingGoodsSpec.trim())){
            return null;
        }
        return goodsMarketingGoodsSpec;
    }

    public OrderGoods(String goodsId,
                      String goodsSpec,
                      String quantity,
                      String goodsSepecStr,
                      String goodsName,
                      String goodsImgPath,
                      String goodsType,
                      String goodsMoney,
                      String memberCartDetailId,
                      String goodsShopListString,
                      String goodsChildId,
                      String goodsMarketingType,
                      String goodsMarketingGoodsId,
                      String goodsPoint) {
        this.goodsId = goodsId;
        this.goodsSpec = goodsSpec;
        this.quantity = quantity;
        this.goodsSepecStr = goodsSepecStr;
        this.goodsName = goodsName;
        this.goodsImgPath = goodsImgPath;
        this.goodsType = goodsType;
        this.goodsMoney = goodsMoney;
        this.memberCartDetailId = memberCartDetailId;
        this.goodsShopListString = goodsShopListString;
        this.goodsMarketingGoodsSpec = goodsChildId;
        this.goodsMarketingType=goodsMarketingType;
        this.goodsMarketingGoodsId=goodsMarketingGoodsId;
        this.goodsPoint=goodsPoint;
    }
}
