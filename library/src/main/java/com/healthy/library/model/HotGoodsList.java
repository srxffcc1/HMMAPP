package com.healthy.library.model;

public class HotGoodsList {

    /**
     * goodsId : 59
     * filePath : http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/c6dea564-30ca-4fe6-a582-6c4bf3d30a9f.jpg
     * goodsTitle : 小火车积木
     * platformPrice : 185.0
     * skuId : 165
     * skuName : 红色 100 L
     */

    private int goodsId;
    private String filePath;
    private String goodsTitle;
    private double platformPrice;
    private double retailPrice;
    private double storePrice;
    private int skuId;
    private String skuName;

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public double getStorePrice() {
        if(retailPrice!=0){
            return retailPrice;
        }
        return storePrice;
    }

    public void setStorePrice(double storePrice) {
        this.storePrice = storePrice;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public double getPlatformPrice() {
        return platformPrice;
    }

    public void setPlatformPrice(double platformPrice) {
        this.platformPrice = platformPrice;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}
