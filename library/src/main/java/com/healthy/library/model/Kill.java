package com.healthy.library.model;

import android.text.TextUtils;

import java.io.Serializable;

public class Kill implements Serializable {
    public String goodsId;
    public String mapMarketingGoodsId;
    public String totalInventory;
    public int goodsType;
    public String filePath;
    public String goodsTitle;
    public double marketingPrice;
    public double storePrice;
    public double retailPrice;
    public String courseFlag;
    public String courseId;
    public String endTime;
    public String getMerchantName() {
        if(!TextUtils.isEmpty(merchantShortName)){
            return merchantShortName;
        }
        return merchantName;
    }

    private String merchantName;
    private String merchantShortName;

    public double platformPrice;
    public int sales;
    public String userId;
    public int inventory;
    public int width = -1;
    public String marketingType;
    public String tagName;
    public long endTimestamp;//距离活动结束剩余秒数
    public int remindState;//提醒状态 0未提醒 1已提醒
}
