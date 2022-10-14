package com.healthy.library.model;

import android.text.TextUtils;

import com.healthy.library.LibApplication;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;

public class GoodsSpecDetail {

    public String id;
    public String goodsType;
    public String isPlusOnly;
    public boolean isErrorCount = false;
    public String isSupportOverSold = "0";
    public ShopDetailModel shopDetailModelSelect;
    public int salesMax = 0;
    public int salesMin = 0;
    public String goodsChildId;//对应的是spec本身的id
    public int goodsId;

    public GoodsSpecDetail setShopDetailModelSelect(ShopDetailModel shopDetailModelSelect) {
        this.shopDetailModelSelect = shopDetailModelSelect;
        if(shopDetailModelSelect !=null){
           isSupportOverSold= shopDetailModelSelect.isSupportOverSold;
        }
        return this;
    }

    public String getgoodsMarketingGoodsSpec() {
        if (!TextUtils.isEmpty(marketingType)) {
            return id;
        }
        return null;
    }

    public String getGoodsChildId() {
        if (TextUtils.isEmpty(goodsChildId)) {
            return id;
        }
        return goodsChildId;
    }

    public String getGoodsSpec() {
        if (!TextUtils.isEmpty(marketingType)) {
            if (Integer.parseInt(marketingType) < 0) {
                return id;
            }
            return goodsChildId;
        }
        return id;
    }
    public int getAvailableInventory() {//获得表库存 就是最终用的那个库存 如果时活动就看活动
        if("0".equals(marketingType)||marketingType==null){//普通商品
            return getRealAvailableInventory();
        }else {//活动商品 双库存判断
            return getAvailableInventoryMark() < getRealAvailableInventory() ? getAvailableInventoryMark() : getRealAvailableInventory();
        }
    }

    public String realAvailableInventory;

    public int getRealAvailableInventory() {//获得里库存  门店库存 涉及负库存销售
        if(TextUtils.isEmpty(realAvailableInventory)){
            realAvailableInventory=availableInventory+"";
        }
        if(!TextUtils.isEmpty(realAvailableInventory)&&Integer.parseInt(realAvailableInventory)<0){
            realAvailableInventory="0";
        }
        if ("1".equals(isSupportOverSold)) {
            return 999;
        }else {
            return Integer.parseInt(realAvailableInventory);
        }
    }

    public int channelPlatform;
    public String barcodeSku;

    public String getMarketingType() {
        return marketingType;
    }

    public String marketingType;
    public double marketingPrice;
    public int nowOrderBuyCount = 0;

    public double getPlatformPrice() {//需要识别是不是plus
        if ("8".equals(marketingType)) {//判断是不是isplusonly
            if (getPlusPrice() > 0 && "1".equals(isPlusOnly)) {
                return getPlusPrice();
            }
        }
        if (getPlusPrice() > 0 && !"8".equals(marketingType)) {
            return getPlusPrice();
        }
        return platformPrice;
    }

    public double getLivePrice() {//需要识别是不是plus
        return marketingPrice;
    }

    public double getMarketingPrice() {
        if ("4".equals(marketingType)) {
            if (isNtReal) {
                return marketingPrice;
            } else {
                return platformPrice;
            }
        }
        return marketingPrice;
    }

    public double getMarketingPriceInOrder() {
        if ("4".equals(marketingType)) {
            if (isNtReal) {
                return marketingPrice;
            } else {
                return platformPrice;
            }
        }
        if ("1".equals(marketingType)) {
            return platformPrice;
        }
        return marketingPrice;
    }

    public String mapMarketingGoodsId;

    public GoodsSpecDetail setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
        return this;
    }

    public double getPlusPrice() {//获得plus价格 为0就没有嘛
        if (SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false)) {
            return plusPrice;
        }
        return 0;
    }

    private double plusPrice;
    public String goodsTitle = "";
    public String adTitle;
    public String goodsSpec;



    public int getAvailableInventoryMark() {//直接返回活动库存 不进行判断
        return availableInventory>0?availableInventory:0;
    }

    public void setAvailableInventory(int availableInventory) {
        this.availableInventory = availableInventory;
    }

    private int availableInventory = 999999;
    public double retailPrice;
    public double platformPrice;

    public double getPointsPrice() {
        return pointsPrice;
    }

    public double pointsPrice;
    public String filePath;
    public String marketingId;

    public GoodsSpecDetail setFilePath(String filePath) {
        if (TextUtils.isEmpty(this.filePath)) {
            this.filePath = filePath;
        }
        return this;
    }

    public String count = "1";

    public String getCount() {
        if (isErrorCount) {
            return "1";
        }
        return count;
    }

    public String goodsSpecStr = "";

    public GoodsSpecDetail setCount(String count) {
        this.count = count;
        return this;
    }

    public GoodsSpecDetail(double storePrice, double platformPrice, String filePath, String goodsType, int salesMax, int salesMin, boolean isNtReal, String isPlusOnly) {
        this.retailPrice = storePrice;
        this.platformPrice = platformPrice;
        this.filePath = filePath;
        this.goodsType = goodsType;
        this.salesMax = salesMax;
        this.salesMin = salesMin;
        this.isNtReal = isNtReal;
        this.isPlusOnly = isPlusOnly;
    }

    public void setParent(GoodsSpecDetail goodsSpecDetail) {
        this.salesMax = goodsSpecDetail.salesMax;
        this.salesMin = goodsSpecDetail.salesMin;
        this.isNtReal = goodsSpecDetail.isNtReal;
        this.isPlusOnly = goodsSpecDetail.isPlusOnly;
    }

    public GoodsSpecDetail(GoodsDetail.GoodsChildren goodsChildren) {
        this(goodsChildren, "-1");
    }

    public GoodsSpecDetail(GoodsDetail.GoodsChildren goodsChildren, String marketingType) {

        this.retailPrice = goodsChildren.retailPrice;
        this.platformPrice = goodsChildren.platformPrice;
        this.goodsTitle = goodsChildren.goodsTitle;
        this.goodsSpecStr = goodsChildren.goodsSpecStr;
        this.id = goodsChildren.id;

        this.mapMarketingGoodsId = goodsChildren.mapMarketingGoodsId;
        this.availableInventory = goodsChildren.availableInventory;
        this.realAvailableInventory = goodsChildren.availableInventory + "";
        this.goodsChildId = goodsChildren.goodsChildId;
        if (this.goodsSpecStr == null) {
            this.goodsSpecStr = "";
        }
//        this.salesMax=salesMax;
//        this.salesMin=salesMin;
        if ("1".equals(marketingType)) {
            this.marketingType = "1";
            this.marketingPrice = goodsChildren.marketingPrice;
            this.salesMax = 1;
            this.salesMin = 1;
        } else if ("2".equals(marketingType)) {
            this.marketingType = "2";
            this.marketingPrice = goodsChildren.marketingPrice;
            this.salesMax = 1;
            this.salesMin = 1;
        } else {
            this.marketingType = "-1";
            this.marketingPrice = goodsChildren.livePrice;
        }
    }

    public void setNtReal(boolean ntReal) {
        isNtReal = ntReal;
    }

    private boolean isNtReal = false;

    public int getMarkLimitMax() {
        if ("1".equals(marketingType) || "2".equals(marketingType) || ("4".equals(marketingType) && isNtReal)) {
            return 1;
        }
        if (salesMax > 0) {//设置了限购
            return salesMax>getAvailableInventory()?getAvailableInventory():salesMax;
        }
        return getAvailableInventory();
    }

    public int getMarkLimitMaxOrg() {
        return salesMax>0?salesMax:999;
    }

    public int getMarkLimitMin() {
        if ("1".equals(marketingType) || "2".equals(marketingType) || ("4".equals(marketingType) && isNtReal)) {
            return 1;
        }
        if (salesMin > 0) {
            return getMarkLimitMinOrg() > getRealCanBuy() ? getRealCanBuy() : getMarkLimitMinOrg();
        }
        return getMarkLimitMinOrg();
    }

    public int getMarkLimitMinOrg() {
        return salesMin>0?salesMin:1;
    }

    public int getRealCanBuy() {//判断真实可购买
        if (salesMax > 0) {//有限购
            int realcanbuy = salesMax - nowOrderBuyCount;
            realcanbuy = realcanbuy > 0 ? realcanbuy : 0;
            return realcanbuy < availableInventory ? realcanbuy : availableInventory;
        } else {
            return availableInventory;
        }
    }

    public String getGoodsSpecStr() {
        if (TextUtils.isEmpty(goodsSpecStr)) {
            goodsSpecStr = goodsTitle;
        }
        return goodsSpecStr;
    }
}
