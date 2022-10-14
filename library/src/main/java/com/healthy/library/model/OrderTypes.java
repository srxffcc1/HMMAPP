package com.healthy.library.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class OrderTypes implements MultiItemEntity {
    public int type;

    public OrderTypes(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
