package com.healthy.library.model;

import java.util.List;

public class BalanceModel {

    public String CardName;
    public String Address;
    public String Birthday;
    public String E_Mail;
    public String CardTJR;
    public String CardJFTot;
    public String Babyday1;
    public String Babyday;
    public String PreparaDate;
    public String RegDate;
    public String CardSex;
    public String BabySex;
    public String DepartName;
    public String CardQQ;
    public String CardNo;
    public String CardWX;
    public String BabyName;
    public String BabySex1;
    public String LastBuyDate;
    public String State;
    public String CardTel;
    public String BabyName1;
    public String CardYe;
    public String Other;
    public String ytbAppId;
    public String CardTypeName;
    public String NormalCardYe;//普通充值余额
    public String ClassCardYe;//分类充值余额
    public List<VipClassInfoList> VipClassInfo;

    public class VipClassInfoList {
        public String LimiteDepartID;
        public String RegDate;
        public String RuleID;
        public String DayNumber;
        public String RuleType;
        public String EndDate;
        public String StartDate;
        public String State;
        public String SeqID;
        public String CardYe;
        public String RuleName;
        public String Other;
    }

}
