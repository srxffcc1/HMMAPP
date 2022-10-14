package com.healthy.library.model;

import android.text.TextUtils;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/04 17:26
 * @des 商品model
 */

public class GoodsModel {

    public String id;
    public String applicationNo;
    public String goodsTitle;
    public String categoryId;
    public String description;
    public double storePrice;
    public double platformPrice;

    public int gQuantity=1;
    public int nowOrderBuyCount;
    public String marketingGoodsChildId;
    public String marketingGoodsId;

    public int getRealCanBuy(int maxActLimit,int nowBuy,int inventory){//判断真实可购买
        if(maxActLimit>0){//有限购
            int realcanbuy=maxActLimit-nowBuy;
            realcanbuy=realcanbuy>0?realcanbuy:0;
            return realcanbuy<inventory?realcanbuy:inventory;
        }else {
            return inventory;
        }
    }

    public String getgCurPrice() {
        if (marketingPrice != 0 && "3".equals(marketingType) && gQuantity <= getMarkLimitMaxNowWithInventory() && gQuantity >= getMarkLimitMin()) {//说明是秒杀
            return marketingPrice + "";
        }
        if (marketingPrice != 0 && "4".equals(marketingType)) {//说明是新客专享
            return marketingPrice + "";
        }
        return platformPrice + "";
    }

    private int getMarkLimitMaxNow() {
        if (getMarkLimitMax() > 0) {
            return getMarkLimitMax() - nowOrderBuyCount;
        }
        return 999999;
    }

    /**
     * 和库存比较换算正确的可购
     * @return
     */
    public int getMarkLimitMaxNowWithInventory() {
        if(marketingMaxInventory>getMarkLimitMaxNow()){
            return getMarkLimitMaxNow();
        }else {
            return marketingMaxInventory;
        }
    }
    public int getMarkLimitMaxOrg() {
        if(marketingSalesMax>0){
            return marketingSalesMax;
        }else {
            return 0;
        }
    }

    public double storePlatformPriceDiscount;

    public int getMaxInventory() {
        if(!TextUtils.isEmpty(marketingType)){//是活动
            if(getMarkLimitMaxNowWithInventory()>maxInventory){//活动可够大于 原始库存 会发生 无法变单问题 需要限制最大可选为
                return getMarkLimitMaxNowWithInventory();
            }else {
                return maxInventory;
            }
        }
        return maxInventory;
    }

    public int maxInventory;

    public String expiredDate;
    public String salesMax;
    public int isReservation;
    public int isHomeService;
    public String reservationHours;
    public int sameOffer;
    public String additionNote;
    public String manual;
    public int totalSales;

    public int getTotalSales() {
        if("3".equals(marketingType)||"4".equals(marketingType)){
                return marketingSales;
        }
        return totalSales;
    }

    public String reviewStatus;
    public String reviewNote;
    public int goodsStatus;
    public String applicationType;
    public String normalId;
    public String createTime;
    public String updateTime;
    public String reviewUser;
    public String reviewTime;
    public int goodsShopsCount;
    public String tagOfCoupon;
    public GoodsCategory goodsCategory;
    public List<String> headImages;
    public List<GoodsFile> goodsFiles;
    public List<String> goodsDescription;
    public List<GoodsPublish> goodsPublishes;
    public List<GoodsShop> goodsShops;
    public List<Attribute> attributes;


    public String mapMarketingGoodsId;
    public String marketingType;
    public String marketingTypeStr;

    public String marketingId;

    public int marketingSales;

    public int marketingMaxInventory;

    public double marketingPrice;

    public String getMarketingPrice() {
        if (marketingPrice == 0) {
            return null;
        }
        return marketingPrice + "";
    }

    public int marketingSalesMin;

    public int marketingSalesMax;

    public String marketingBeginTime;
    private String marketingEndTime;

    public int getMarkLimitMax() {
        return marketingSalesMax;
    }

    public int getMarkLimitMin() {
        return marketingSalesMin == 0 ? 1 : marketingSalesMin;
    }
    public int getMarkLimitMinOrg() {
        return marketingSalesMin;
    }

    public String getMarketingEndTime() {//返回的"marketingEndTime": "2020-09-20T11:10:00"
        if (marketingEndTime.contains("T")) {//因为时有时无 很诡异 所以要判断下 以免出现问题
            return marketingEndTime.replace("T", " ");
        } else {
            return marketingEndTime;
        }
    }

    public List<GoodsChildren> goodsChildren;

    public String getGoodsSpec() {
        if(goodsChildren!=null&&goodsChildren.size()>0){
            return goodsChildren.get(0).id;
        }
        return null;
    }

    public class GoodsShop {
        public int id;
        public int goodsId;
        public int shopId;
        public String shopName;
    }

    public class GoodsPublish {
        public int id;
        public int goodsId;
        public int publish;
    }

    public class Attribute {
        public long id;
        public int goodsId;
        public String attributeName;
        public int attributeType;
        public String attributeValue;
        public String placeHolder;
        public String goodsValue;
    }

    public class GoodsFile {
        public int id;
        public int fileType;
        public String filePath;
        public String imageTitle;
        public String imageDescription;
    }


    class FatherCategory {
        public int id;
        public String goodsCategoryNo;
        public String categoryName;
        public int categoryLevel;
        public int ranking;
        public int status;
    }

    class GoodsCategory {
        public int id;
        public String goodsCategoryNo;
        public String categoryName;
        public int categoryLevel;
        public int fatherId;
        public int status;
        public FatherCategory fatherCategory;
    }


    public static class ServiceItem {
        public String serviceId;
        public String serviceName;
    }

    public class GoodsChildren {
        public String id;

        public String getId(String marketingType) {
            if(!TextUtils.isEmpty(marketingType)){
                return goodsChildId;
            }
            return id;
        }

        public String goodsChildId;

        public int channelPlatform;

        public String barcodeSku;

        public String goodsTitle;

        public String adTitle;

        public String goodsSpec;

        public int availableInventory;

        public double storePrice;

        public double platformPrice;
        public double marketingPrice;

        public String beginTime;
        public String endTime;

    }
}
