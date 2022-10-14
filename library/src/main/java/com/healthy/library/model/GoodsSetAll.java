package com.healthy.library.model;

import java.util.List;

public class GoodsSetAll {

    public String id;

    public double combinationPrice;

    public double saveAmountOf;

    public String merchantId;

    public boolean isShow=false;
    public long openToBookingCountdown;

    public double getSaveAmountOf(){
        double orgPrice=0;
        for (int i = 0; i <goodsList.size() ; i++) {
            orgPrice+=goodsList.get(i).platformPrice;
        }
        double result=0;
        result=orgPrice-combinationPrice;
        if(result<0){
            return 0;
        }
        return result;
    }

    public List<GoodsSetCell> goodsList;

}
