package com.health.servicecenter.model;

import com.healthy.library.utils.FormatUtils;

import java.util.List;

/**
 * @author: long
 * @date: 2021/4/16
 * @des
 */
public class RecommendModel {

    public String id;
    public String themeName;
    public String iconUrl;
    public String themeType;
    public String isThemeShow;

    public List<RecommendGoods> goodsList;


    public class RecommendGoods {
        public String themeId;
        public String id;
        public String goodsTitle;
        public String filePath;
        public double pointsPrice;
        public double marketingPrice;

        public String getPointsRealPrice() {
            if (marketingPrice != 0) {
                return FormatUtils.moneyKeep2Decimals(pointsPrice) + "积分+¥" + FormatUtils.moneyKeep2Decimals(marketingPrice);

            }
            return FormatUtils.moneyKeep2Decimals(pointsPrice) + "积分";
        }

        public String pointsGoodsType;
        public String isSaleOut;
    }

}
