package com.health.discount.model;

import com.healthy.library.model.MarketingGoodsList;
import com.healthy.library.utils.FormatUtils;

import java.util.List;

public class FlashBuyListModel {

    public String id;
    public String userId;
    public String marketingName;
    public String begStringime;
    public String endTime;
    public String merchantName;
    public String supportCoupons;
    public String standardPriceType;
    public String activityRestrict;
    public String overlying;
    public String ranking;
    public String ruleType;
    public String inventoryType;
    public String equalGoods;
    public List<ActivityRuleListBean> activityRuleList;
    public List<MarketingGoodsList> marketingGoodsList;

    public String getDisActRulesOnlyOne(String marketingType) {
        String result="";
        if("6".equals(marketingType)){
            if(getRuleType()==1){//满额度
                for (int i = 0; i <1 ; i++) {
                    try {
                        result=result+"满"+ FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).requirement)+"元减"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).discountMoney)+"元";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else {
                for (int i = 0; i <1 ; i++) {
                    try {
                        result=result+"满"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).requirement)+"件减"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).discountMoney)+"元";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        if("7".equals(marketingType)){
            for (int i = 0; i <1 ; i++) {
                try {
                    result=result+"买"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).requirement)+"件送"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).discountNum)+"件";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public int getRuleType() {
        try {
            return activityRuleList.get(0).ruleType;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static class ActivityRuleListBean {
        public String id;
        public String marketingId;
        public int ruleType;
        public String requirement;
        public String discountNum;
        public String discountMoney;
    }
}
