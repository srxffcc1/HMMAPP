package com.healthy.library.model;

public class GoodsFee {


    public String mchId;

    public String mchName;

    public String shopId;

    public String shopName;

    public String shopChainName;

    public String shopBrandName;

    public String appointmentPhone;

    public String shopAddress;

    public String addressProvince;

    public String addressCity;

    public String addressArea;

    public double distance;

    public int isSupport;

    public int isReach;

    public double minAmount;

    public double fee;


    public GoodsFee() {
    }

    public GoodsFee(double fee) {
        this.fee = fee;
    }
}
