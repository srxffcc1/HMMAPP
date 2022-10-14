package com.healthy.library.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ActStoreItem implements MultiItemEntity {

    public int shopId;

    public String categoryNos;

    public int shopType;

    public String shopName;

    public String filePath;

    public String shopAddress;

    public double longitude;

    public double latitude;

    public double score;

    public double distance;

    public String isSelected;

    public String appointmentPhone;

    public String businessTime;
    public String addressProvince;
    public String addressCity;
    public String addressArea;

    @Override
    public int getItemType() {
        return 1;
    }
}
