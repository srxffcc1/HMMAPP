package com.healthy.library.model;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/04 17:26
 * @des 商品model
 */

public class Goods2ShopModel {
    public GoodsModel goods;
    public ShopModel shop;
    public List<PlatformCouponInfo> couponInfoByMemberIdAndStatusDTOS;
    public List<CouponInfoByMerchant> appMerchantCoupons;

    public void setGoods(GoodsModel goods) {
        this.goods = goods;
    }

    public void setShop(ShopModel shop) {
        this.shop = shop;
    }
}
