package com.healthy.library.model;

import java.util.List;

public class MarketingGoodsList {
    public String mapMarketingGoodsId;
    public String goodsId;
    public String sales;
    public String endTimestamp;
    public String goodsType;
    public String marketingType;
    public String filePath;
    public String goodsTitle;
    public double marketingPrice;
    public double platformPrice;
    public double storePrice;
    public double retailPrice;
    public String courseFlag;
    public String courseId;
    public String tagName;
    public String getTagFirst(){
        if(tagName==null||"null".equals(tagName)||"".equals(tagName)){
            return null;
        }
        return tagName.split(",")[0];
    }
    public int standardPriceType;//促销标准价（1平台价 2零售价）
    public double getStandPrice(int standardPriceType) {
        if(standardPriceType==1){
            return platformPrice;
        }else {
            return retailPrice;
        }
    }
    private int availableInventory;
    public int realAvailableInventory;
    public List<ActivityRuleListBean> activityRuleList;
    public List<DisGoodsSpecCell> goodsSpecCellList;

    public static class ActivityRuleListBean {
        public String id;
        public String marketingId;
        public String ruleType;
        public String requirement;
        public String discountNum;
        public String discountMoney;
    }
}
