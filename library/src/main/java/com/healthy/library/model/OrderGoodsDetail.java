package com.healthy.library.model;

public class OrderGoodsDetail {
    public OrderGoodsDetail(String goodsId, String goodsSpec, String quantity, String memberCartDetailId, String goodsMarketingGoodsId, String goodsMarketingType, String goodsMarketingGoodsSpec) {
        this.goodsId = goodsId;
        this.goodsSpec = goodsSpec;
        this.quantity = quantity;
        this.memberCartDetailId = memberCartDetailId;
        this.goodsMarketingGoodsId = goodsMarketingGoodsId;
        this.goodsMarketingType = goodsMarketingType;
        this.goodsMarketingGoodsSpec = goodsMarketingGoodsSpec;
    }

    public String goodsId;
    public String goodsSpec;
    public String quantity;
    public String memberCartDetailId;
    public String goodsMarketingGoodsId;
    public String goodsMarketingType;
    public String goodsMarketingGoodsSpec;
}
