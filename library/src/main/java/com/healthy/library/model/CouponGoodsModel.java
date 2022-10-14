package com.healthy.library.model;

import com.healthy.library.LibApplication;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;

import java.util.List;

public class CouponGoodsModel {

    public String id;
    public String goodsId;
    public String goodsName;
    public int goodsType;
    public String goodstitle;
    public String brandId;
    public String brandName;
    public String categoryId;
    public String categoryName;
    public String weight;
    public double platformPrice;
    public double retailPrice;
    public String barcodeSpu;
    public String headImage;
    public String[] goodsShopIds;
    public String getGoodsShopId(){
        if(goodsShopIds !=null){
            try {
                return goodsShopIds[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP);
    }
    public List<GoodsChildrenBean> goodsChildren;

    public class GoodsChildrenBean {
        public int lockedInventory;
        public int maxInventory;
        public String limitPrice;
        public int commissionRateUserIsOff;
        public String goodsId;
        public String adTitle;
        public String barcodeSku;
        public double priceGradeNum;
        public double platformPrice;
        public int totalInventory;
        public String commissionRateShop;
        public int commissionRateShopIsOff;
        public double plusPrice;
        public String goodsSpec;
        public int availableInventory;
        public double storePrice;
        public String commissionRateUser;
        public String goodsTitle;
        public String goodsSpecStr;
        public String id;
        public String shopId;
        public double retailPrice;
        public int status;

    }
}
