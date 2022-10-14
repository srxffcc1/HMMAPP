package com.healthy.library.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Goods2ShopModelKick {

    public BargainInfo bargainInfoDTO;
    @SerializedName("s_goodsDetails")
    public GoodsModel goods;
    public ShopModel shop;
    public String merchantMobile;


    public class BargainInfo {
        public String mapMarketingGoodsId;


        public List<MarketingGoodsChild> marketingGoodsChildDTOS;


        public int id;

        public int merchantId;

        public String merchantName;

        public int goodsId;

        public int goodsType;

        public String goodsTitle;

        public String goodsImage;

        public double goodsStorePrice;

        public double goodsPlatformPrice;

        public double floorPrice;

        public int maxBargainNum;

        public int bargainTimeLength;

        public int bargainInventory;

        public int initialInventory;

        public String startTime;

        public String endTime;

        public String bargainRule;

        public int bargainStatus;

        public String createTime;

        public String updateTime;

        public int joinNum;

        public int sucessNum;

    }
    public class MarketingGoodsChild
    {
        public String id;

        public String goodsId;

        public String goodsChildId;

        public int mapMarketingGoodsId;

        public String barcodeSku;

        public String goodsTitle;

        public String adTitle;

        public String goodsSpec;

        public String goodsSpecStr;

        public String retailPrice;

        public double storePrice;

        public double platformPrice;

        public double marketingPrice;

        public int maxInventory;

        public int availableInventory;

        public int lockedInventory;

        public int totalInventory;

        public int sales;

        public String isOffShelf;
        
    }

}
