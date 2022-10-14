package com.healthy.library.model;

import android.text.TextUtils;

import java.util.List;

public class Goods2DetailPin {
    public Assemble assembleDTO;

    public GoodsDetail goodsDetails;
    public String merchantMobile;
    public class MarketingGoodsChildDTOS
    {
        public String id;

        public String goodsId;

        public String goodsChildId;

        public String mapMarketingGoodsId;

        public String barcodeSku;

        public String goodsTitle;

        public String adTitle;


        public String goodsSpecStr;

        public double retailPrice;

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
    public class Assemble
    {
        public String goodsTitle;

        public Assemble(int id, double assemblePrice, int regimentSize, String startTime, String endTime) {
            this.id = id;
            this.assemblePrice = assemblePrice;
            this.regimentSize = regimentSize;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public int id;

        public String merchantId;

        public String getMerchantName() {
            if(!TextUtils.isEmpty(merchantShortName)){
                return merchantShortName;
            }
            return merchantName;
        }

        private String merchantName;
        private String merchantShortName;

        public int goodsId;

        public double goodsPlatformPrice;

        public double assemblePrice;

        public int regimentSize;

        public int regimentTimeLength;

        public int assembleInventory;

        public String startTime;

        public String goodsImage;

        public String endTime;

        public int assembleStatus;

        public int joinNum;

        public int getJoinNum(GoodsDetail goodsDetails) {
            return joinNum;
        }

        public int virtualSales;

        public List<MarketingGoodsChildDTOS> marketingGoodsChildDTOS;

    }


}
