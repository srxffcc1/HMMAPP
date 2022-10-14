package com.healthy.library.model;

import android.text.TextUtils;

public class DisGoodsSpecCell {

    public String goodsId;
    public String goodsChildId;
    public String mapMarketingGoodsId;
    public String barcodeSku;
    public String goodsTitle;
    public String adTitle;
    public String goodsSpec;
    public String goodsSpecStr;
    public String filePath;
    public String marketingType;
    public double retailPrice;
    public double storePrice;
    public double platformPrice;
    public double marketingPrice;
    public String isSupportOverSold="0";

    public void setIsSupportOverSold(String isSupportOverSold) {
        this.isSupportOverSold = isSupportOverSold;
    }

    public int getAvailableInventory() {
        if(marketingType!=null&&!"0".equals(marketingType)&&!TextUtils.isEmpty(marketingType)&&!"null".equals(marketingType)){//活动
            if(availableInventory>0){
                return availableInventory<getRealAvailableInventory()?availableInventory:getRealAvailableInventory();
            }else {
                return 0;
            }
        }else {
            if("1".equals(isSupportOverSold)){
                return 99999;
            }else {
                return getRealAvailableInventory();
            }
        }
    }
    public int getRealAvailableInventory() {
        if(marketingType!=null){
            return TextUtils.isEmpty(realAvailableInventory)?999:Integer.parseInt(realAvailableInventory);
        }
        return availableInventory;
    }
    private int availableInventory;
    public String realAvailableInventory;
    public int sales;
    public String mapMarketingGoodsChildId;
    public boolean isErrorCount = false;

    public String getCount() {
        if (isErrorCount) {
            return "1";
        }
        return count;
    }

    public String count = "1";

    public DisGoodsSpecCell setCount(String count) {
        this.count = count;
        return this;
    }
}
