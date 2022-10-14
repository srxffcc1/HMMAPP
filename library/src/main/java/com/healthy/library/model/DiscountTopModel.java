package com.healthy.library.model;

import com.healthy.library.utils.FormatUtils;

import java.util.List;

public class DiscountTopModel {

    public String getDisTypeName(){
        if("6".equals(marketingType)){
            return "满减";
        }
        if("7".equals(marketingType)){
            return "多买多送";

        }
        return null;
    }
        public String groupDiscountRule;

    public String getGroupDiscountRule() {
        return groupDiscountRule;
    }

    public String extraDiscount;
        public String marketingId;


        public String id;

        public int userId;

        public String marketingName;

        public String beginTime;

        public String endTime;

    public String getBeginTime() {
        try {
            return beginTime.split(" ")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getEndTime() {
        try {
            return endTime.split(" ")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public int status;

        public String num;

        public String marketingType;

        public String merchantName;

        public String areaId;

        public String areaCode;

        public String areaName;

        public String marketingRemark;

        public int supportCoupons;

        public int standardPriceType;

        public int activityRestrict;

        public int overlying;

        public int ranking;

//        private int ruleType;

    public int getRuleType() {
        try {
            return activityRuleList.get(0).ruleType;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int inventoryType;

        public int equalGoods=1;

        public String deptId;

        public List<ActivityRuleList> activityRuleList;

    public String getDisActRules() {
        String result="";
        if(activityRuleList==null){
            return "";
        }
        if("6".equals(marketingType)){
            if(getRuleType()==1){//满额度
                for (int i = 0; i <activityRuleList.size() ; i++) {
                    result=result+"满"+ FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).requirement)+"元减"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).discountMoney)+"元,";
                }
            }else {
                for (int i = 0; i <activityRuleList.size() ; i++) {
                    result=result+"满"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).requirement)+"件减"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).discountMoney)+"元,";

                }
            }
        }
        if("7".equals(marketingType)){
            for (int i = 0; i <activityRuleList.size() ; i++) {
                result=result+"买"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).requirement)+"件送"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).discountNum)+"件,";
            }
        }
        if(result.length()>0){
            result=result.substring(0,result.length()-1);
        }
        return result;
    }
    public String getDisActRulesOnlyOne() {
        String result="";
        if(activityRuleList==null){
            return "";
        }
        if("6".equals(marketingType)){
            if(getRuleType()==1){//满额度
                for (int i = 0; i <1 ; i++) {
                    result=result+"满"+ FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).requirement)+"元减"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).discountMoney)+"元";
                }
            }else {
                for (int i = 0; i <1 ; i++) {
                    result=result+"满"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).requirement)+"件减"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).discountMoney)+"元";

                }
            }
        }
        if("7".equals(marketingType)){
            for (int i = 0; i <1 ; i++) {
                result=result+"买"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).requirement)+"件送"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).discountNum)+"件";
            }
        }
        return result;
    }
    public String getDisActRulesIndex(int i) {
        String result="";
        if(activityRuleList==null){
            return "";
        }
        if("6".equals(marketingType)){
            if(getRuleType()==1){//满额度
                    result=result+"满"+ FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).requirement)+"元减"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).discountMoney)+"元";
            }else {
                    result=result+"满"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).requirement)+"件减"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).discountMoney)+"元";
            }
        }
        if("7".equals(marketingType)){
                result=result+"买"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).requirement)+"件送"+FormatUtils.moneyKeep2Decimals(activityRuleList.get(i).discountNum)+"件";
        }
        return result;
    }
    public class ActivityRuleList
    {
        public int id;

        public int marketingId;

        public int ruleType;

        public double requirement;

        public int discountNum;

        public double discountMoney;
    }
}
