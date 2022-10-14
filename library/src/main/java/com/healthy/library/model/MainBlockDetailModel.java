package com.healthy.library.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 专题内容
 */
public class MainBlockDetailModel implements MultiItemEntity {
    public int id;
    public String goodsId;
    public int themeId;
    public GoodsDetail goodsDTO = new GoodsDetail();

    public MainBlockDetailModel(AppIndexGoodsList object) {
        goodsDTO=new GoodsDetail();
        goodsDTO.filePath=object.goodsImage;
        goodsDTO.goodsTitle=object.goodsTitle;
        goodsDTO.retailPrice=object.retailPrice;
        goodsDTO.platformPrice=object.platformPrice;
        goodsDTO.id=object.goodsId;
        goodsDTO.differentSymbol=object.differentSymbol;
        goodsDTO.merchantName=object.merchantName;
        goodsDTO.merchantShortName=object.merchantShortName;
        goodsDTO.goodsId=object.goodsId;
    }


    public String getGoodsImgUrl() {
        if (goodsDTO != null) {
            if (goodsDTO.headImages != null && goodsDTO.headImages.size() > 0) {
                return goodsDTO.headImages.get(0).filePath;
            }
            if (goodsDTO.headImage != null) {
                return goodsDTO.headImage;
            }
            return null;
        }
        return null;
    }

    @Override
    public int getItemType() {
        return Integer.parseInt(goodsDTO.goodsType);
    }
}
