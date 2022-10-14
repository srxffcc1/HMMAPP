package com.healthy.library.model;

import android.text.TextUtils;

import java.util.List;

public class Goods2DetailKick {

    public BargainInfo bargainInfoDTO;

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

        public String goodsSpec;

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


    public class BargainInfo
    {
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

        public List<MarketingGoodsChildDTOS> marketingGoodsChildDTOS;

        public String startTime;

        public String endTime;

        public String bargainRule;

        public int bargainStatus;

        public String createTime;

        public String updateTime;

        public int getJoinNum(GoodsDetail goodsDetails) {
            return joinNum;
        }

        public int joinNum;

        public int virtualSales;

        public int sucessNum;


    }


}
