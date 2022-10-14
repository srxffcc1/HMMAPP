package com.healthy.library.model;

public class AssembleParams {
    public String assembleId;

    public String assemTeamNum;

    public String assembleType;

    public String goodsId;

    public String goodsChildId;

    public String marketingGoodsId;

    public String marketingGoodsChildId;

    public AssembleParams(String assembleId, String assemTeamNum, String assembleType, String goodsId, String goodsChildId, String marketingGoodsId, String marketingGoodsChildId) {
        this.assembleId = assembleId;
        this.assemTeamNum = assemTeamNum;
        this.assembleType = assembleType;
        this.goodsId = goodsId;
        this.goodsChildId = goodsChildId;
        this.marketingGoodsId = marketingGoodsId;
        this.marketingGoodsChildId = marketingGoodsChildId;
    }
}
