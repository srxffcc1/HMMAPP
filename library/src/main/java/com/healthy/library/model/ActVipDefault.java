package com.healthy.library.model;

import android.util.Base64;

import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;
import com.tencent.bugly.proguard.A;

import java.util.ArrayList;
import java.util.List;

public class ActVipDefault {
    public String barcodeSku;
    public String shopId;
    public String additionNote;
    public String goodsTitle;
    public String userName;
    public String userId;
    public String goodsType;

    public ActVipDefault(String barcodeSku,
                         String shopId,
                         String additionNote,
                         String goodsTitle,
                         String userId,
                         String goodsType,
                         GoodsChildren goodsChildren,
                         GoodsFiles goodsFile) {
        this.barcodeSku = barcodeSku;
        this.shopId = shopId;
        this.additionNote = additionNote;
        this.goodsTitle = goodsTitle;
        this.userId = userId;
        this.goodsType = goodsType;
        this.goodsChildren.add(goodsChildren);
        this.goodsFiles.add(goodsFile);
    }

    public List<GoodsChildren> goodsChildren=new ArrayList<>();
    public List<GoodsFiles> goodsFiles=new ArrayList<>();
    public static class GoodsChildren{
        public GoodsChildren(String barcodeSku, String platformPrice, String retailPrice) {
            this.barcodeSku = barcodeSku;
            this.platformPrice = platformPrice;
            this.retailPrice = retailPrice;
        }
        public GoodsChildren(String barcodeSku, double platformPrice, double retailPrice) {
            this.barcodeSku = barcodeSku;
            this.platformPrice = platformPrice+"";
            this.retailPrice = retailPrice+"";
        }

        public String barcodeSku;
        public String storePrice="99999";
        public String platformPrice="99999";
        public String retailPrice="99999";
    }
    public static class GoodsFiles{
        public GoodsFiles() {

        }
        public String fileType="1";
        public String filePath;
        public String thumbsPath;
        public String imageTitle;
        public String imageDescription;
        public String textDescription;
        public String channelPlatform;
        public String ranking;
    }
}
