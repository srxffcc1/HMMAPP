package com.health.discount.model;

import com.healthy.library.utils.FormatUtils;

import java.util.List;

public class PointTab {
    public String id;
    public String themeName;
    public String iconUrl;
    public String themeType;
    public String isThemeShow;

    public List<PointGoods> goodsList;


    public class PointGoods{
        public String themeId;
        public String id;
        public String goodsTitle;
        public String filePath;
        public double pointsPrice;
        public double marketingPrice;

        public String getPointsRealPrice() {
            if(marketingPrice!=0){
                return FormatUtils.moneyKeep2Decimals(pointsPrice)+"积分+¥"+ FormatUtils.moneyKeep2Decimals(marketingPrice)+"";

            }
            return FormatUtils.moneyKeep2Decimals(pointsPrice)+"积分";
        }

        public String pointsGoodsType;
        public String isSaleOut;
    }

}
