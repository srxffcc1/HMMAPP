package com.healthy.library.model;

import android.text.TextUtils;

public class GoodsSpecCell {
    public String id;
    public String specName;
    public String realSpecName;
    public String[] specValue;

    public GoodsSpecCell(String specName, String[] specValue) {
        this.specName = specName;
        this.specValue = specValue;
    }

    public String getSpecName() {
        if(TextUtils.isEmpty(realSpecName)){
            return specName;
        }
        return realSpecName;
    }
}
