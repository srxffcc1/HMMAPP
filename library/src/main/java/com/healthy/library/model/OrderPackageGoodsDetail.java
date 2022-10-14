package com.healthy.library.model;

public class OrderPackageGoodsDetail implements Comparable<OrderPackageGoodsDetail>{
    public OrderPackageGoodsDetail(String goodsId,
                                   String goodsSpec,
                                   String quantity,
                                   String memberCartDetailId,
                                   String goodsMarketingGoodsId,
                                   String goodsMarketingType,
                                   String goodsMarketingGoodsSpec,
                                   String marketingId,
                                   String shopId,
                                   String isAddition,
                                   String additionType

    ) {
        this.goodsId = goodsId;
        this.goodsSpecId = goodsSpec;
        this.quantity = quantity;
        this.memberCartDetailId = memberCartDetailId;
        this.marketingGoodsId = goodsMarketingGoodsId;
        this.marketingType = goodsMarketingType;
        this.marketingGoodsSpecId = goodsMarketingGoodsSpec;
        this. marketingId=marketingId;
        this. shopId=shopId;
        this. isAddition=isAddition;
        this. additionType=additionType;
    }

//    public String goodsId;
//    public String goodsSpec;
//    public String quantity;
//    public String memberCartDetailId;
//    public String goodsMarketingGoodsId;
//    public String goodsMarketingType;
//    public String goodsMarketingGoodsSpec;

    public String memberCartDetailId;//

    public String goodsSource="1";//

    public String goodsId;//

    public String goodsSpecId;//

    public String marketingType;//

    public String marketingGoodsId;//

    public String marketingGoodsSpecId;//

    public String quantity;//

    public String marketingId;

    public String shopId;

    public String isAddition;

    public String additionType;

    @Override
    public int compareTo(OrderPackageGoodsDetail o) {
        return (int) (Integer.parseInt(goodsId) - Integer.parseInt(o.goodsId));
    }
}
