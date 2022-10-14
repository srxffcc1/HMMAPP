package com.healthy.library.model;

import java.util.List;

public class ActKill {
    public String goodsId;
    public String goodsTitle;
    public int goodsType;
    public String filePath;
    public int inventory;
    public int sales;
    public double storePrice;
    public double platformPrice;
    public double retailPrice;
    public double marketingPrice;
    public int width=-1;
    public long endTimestamp;
    public List<ShareGiftDTO> shareGiftDTOS;

    public double getPrice() {
        if(goodsType==1){//
            return storePrice;
        }else {
            return retailPrice;
        }
    }
}
