package com.healthy.library.model;

public class GoodsSpecLimit {
    public GoodsSpecLimit(String sourceId, String goodsId, String goodsSpecId, String marketingGoodsId, String marketingType, String marketingGoodsSpecId, String marketingId,String goodsType) {
        this.goodsSource = sourceId;
        this.goodsId = goodsId;
        this.goodsSpecId = goodsSpecId;
        this.marketingGoodsId = marketingGoodsId;
        this.marketingType = marketingType;
        this.marketingGoodsSpecId = marketingGoodsSpecId;
        this.marketingId = marketingId;
        this.goodsType=goodsType;
    }
    public GoodsSpecLimit(String goodsSource, String goodsId, String goodsSpecId, String marketingGoodsId, String marketingType, String marketingGoodsSpecId, String marketingId,String goodsType,String startDate) {
        this.goodsSource = goodsSource;
        this.goodsId = goodsId;
        this.goodsSpecId = goodsSpecId;
        this.marketingGoodsId = marketingGoodsId;
        this.marketingType = marketingType;
        this.marketingGoodsSpecId = marketingGoodsSpecId;
        this.marketingId = marketingId;
        this.goodsType=goodsType;
        this.startDate=startDate;
    }
    private String goodsSource;

    private String startDate;

        private String goodsId;

        private String goodsSpecId;

        private String marketingGoodsId;

        private String marketingType;

        private String marketingGoodsSpecId;

        private String marketingId;
        public int totalQuantity;

    private String goodsType;

}
