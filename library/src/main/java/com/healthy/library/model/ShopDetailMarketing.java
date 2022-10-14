package com.healthy.library.model;

/**
 * 创建日期：2021/10/21 9:31
 *
 * @author LiuWei
 * @version 1.0
 * 包名： com.healthy.library.model
 * 类说明：
 */

public class ShopDetailMarketing {
    public String goodsId;
    public String marketingPrice;
    public String filePath;
    public String goodsTitle;
    public int marketingType;
    public int virtualSales;
    public int salesMin;
    public int salesMax;
    public String beginTime;
    public String endTime;
    public int sales;
    public int maxInventory;
    public Goods2DetailKick.BargainInfo bargainInfo;
    public Goods2DetailPin.Assemble assembleInfo;
    public FlashSaleInfo flashSaleInfo;
    public double appLimitedPrice;

    public String getMarketingPrice() {
        if(appLimitedPrice!=0){
            return appLimitedPrice+"";
        }
        return marketingPrice;
    }
    public class FlashSaleInfo{
        public String id;
        public String userId;
        public String marketingName;
        public String beginTime;
        public String endTime;
        public String status;
        public String marketingRemark;
        public String marketingType;
        public String deptId;
    }
}
