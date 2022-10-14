package com.healthy.library.model;

import java.util.List;

public class VipConsume {
    public String OperName;

    public String OperMoney;

    public List<CardSaleDet> cardSaleDetList;

    public String SaleNo;

    public String OperType;

    public String OperDate;

    public String DepartName;

    public String Other;
    public class CardSaleDet
    {
        public String Number;

        public String FactPrice;

        public String GoodsID;

        public String GoodsName;

        public String FactTotal;

        public String Other;
    }



}
