package com.healthy.library.model;


import java.util.List;

public class Goods2ShopModelPin {

    public Assemble assembleDTO;
    public GoodsModel goods;
    public ShopModel shop;
    public String merchantMobile;

    public class Assemble {
        public List<MarketingGoodsChild> marketingGoodsChildDTOS;
        public String mapMarketingGoodsId;
        public String id;
        public int merchantId;
        public String merchantName;
        public int goodsId;
        public double goodsPlatformPrice;
        public double assemblePrice;
        public int regimentSize;
        public int regimentTimeLength;
        public int assembleInventory;
        public String startTime;
        public String endTime;
        public int assembleStatus;
        public int joinNum;

    }
    public class MarketingGoodsChild
    {
        public String id;

        public String goodsId;

        public String goodsChildId;

        public String mapMarketingGoodsId;

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
