package com.healthy.library.model;

public class OrderGoodsDTO {
    public String goodsId;
    public String goodsNumber;

    public OrderGoodsDTO(String goodsId, String goodsNumber) {
        this.goodsId = goodsId;
        this.goodsNumber = goodsNumber;
    }
}
