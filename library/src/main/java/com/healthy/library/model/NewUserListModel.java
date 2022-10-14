package com.healthy.library.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class NewUserListModel implements MultiItemEntity {
    /**
     * 新客专享列表
     */
    public String goodsId;
    public String mapMarketingGoodsId;
    public String totalInventory;
    public int goodsType;
    public String filePath;
    public String goodsTitle;
    public double marketingPrice;
    public double storePrice;
    public String retailPrice;
    public String courseFlag;
    public String courseId;
    public String endTime;
    public String userId;

    public double platformPrice;
    public int sales;
    public int inventory;
    public int width = -1;
    public String marketingType;
    public String tagName;
    public long endTimestamp;//距离活动结束剩余秒数
    public int remindState;//提醒状态 0未提醒 1已提醒

    public List<ShareGiftDTO> shareGiftDTOS;

    public String getTagFirst(){
        if(tagName==null||"null".equals(tagName)||"".equals(tagName)){
            return null;
        }
        return tagName.split(",")[0];
    }
    @Override
    public int getItemType() {
        return 2;
    }
}
