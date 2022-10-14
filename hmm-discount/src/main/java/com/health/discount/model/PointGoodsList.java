package com.health.discount.model;

import com.healthy.library.utils.FormatUtils;

public class PointGoodsList {

    public String themeId;
    public String id;
    public String goodsTitle;
    public String filePath;
    public String pointsPrice;
    public double marketingPrice;
    public String pointsGoodsType;
    public String isSaleOut;
    public String retailPrice;
    public String storePrice;

    public String getPointsRealPrice() {
        if (marketingPrice != 0) {
            return FormatUtils.moneyKeep2Decimals(pointsPrice) + "积分+¥" + FormatUtils.moneyKeep2Decimals(marketingPrice) + "";

        }
        return FormatUtils.moneyKeep2Decimals(pointsPrice) + "积分";
    }
}
