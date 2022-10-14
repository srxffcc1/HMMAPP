package com.healthy.library.model;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.healthy.library.LibApplication;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;

import java.io.Serializable;
import java.util.List;

public class RecommendList implements MultiItemEntity, Serializable {

    public String getId() {
        return goodsId;
    }

    public String goodsId;
    public String filePath;
    public String goodsTitle;
    public String platformPrice;//平台价
    public String marketingPrice;//活动价
    public String skuId;
    public String barcodeSku;
    public int isActivityGoods;//是否为活动商品0 否 1是
    public String skuName;
    public int goodsType;//商品类型 1服务 2标品
    public int marketingType;//活动类型  1砍价 2拼团 3秒杀 4新人专享 5积分商品 6满减 7买送
    public String categoryNo;
    public String shopId;
    public ActVip actVip;
    public String[] goodsShopIds;
    public String bargainId;
    public String assembleId;

    public String getShopId() {
        if(goodsShopIds!=null&&goodsShopIds.length>0){
            return goodsShopIds[0];
        }
        if(!TextUtils.isEmpty(shopId)){
            return shopId;
        }
        return SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP);
    }

    public String shopName;
    public String shopAddress;
    public int distance;
    public float score;
    public String categoryNos;
    public List<ListBean> list;
    public String retailPrice;
    private String storePrice;
    public String tagName;//标签名称（逗号分隔）

    public String getTagFirst(){
        if(tagName==null||"null".equals(tagName)||"".equals(tagName)){
            return null;
        }
        return tagName.split(",")[0];
    }

    public String getRetailPrice() {
        if(retailPrice==null){
            return storePrice;
        }
        return retailPrice;
    }

    public String courseId;
    public int courseFlag;

    public int getGoodsType() {
        if (goodsType == 1 || goodsType == 2) {
            return 2;
        }
        return goodsType;
    }

    public void setType(int type) {
        this.goodsType = type;
    }

    @Override
    public int getItemType() {
        return getGoodsType();
    }

    public class ListBean {

        public String goodsId;
        public String shopId;
        public String filePath;
        public String goodsTitle;
        public String platformPrice;
        public int type;
        public String categoryNo;
        public String retailPrice;
        public String bargainId;
        public String assembleId;
    }

}
