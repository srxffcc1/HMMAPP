package com.healthy.library.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.LibApplication;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GoodsDetail {

    private ShopDetailModel storeDetialModel;
    public String goodsId;

    public String isPlusOnly;
    public String goodsChildId;
    public List<ShareGiftDTO> shareGiftDTOS=new ArrayList<>();

    public void setStoreDetialModel(ShopDetailModel storeDetialModel) {
        System.out.println("修改storeDetialModel" + (storeDetialModel == null));
        this.storeDetialModel = storeDetialModel;
        try {
            for (int i = 0; i < goodsChildren.size(); i++) {
                goodsChildren.get(i).storeDetialModel = storeDetialModel;
                goodsChildren.get(i).marketingType = marketingType;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < marketingGoodsChildren.size(); i++) {
                marketingGoodsChildren.get(i).storeDetialModel = storeDetialModel;
                marketingGoodsChildren.get(i).marketingType = marketingType;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getRealCanBuy(int maxActLimit, int nowBuy, int inventory) {//判断真实可购买
        if (maxActLimit > 0) {//有限购
            int realcanbuy = maxActLimit - nowBuy;
            realcanbuy = realcanbuy > 0 ? realcanbuy : 0;
            return realcanbuy < inventory ? realcanbuy : inventory;
        } else {
            return inventory;
        }
    }

    //public DiscountTopModel discountTopModel;

    public ActVip actVip;

    public double getStandPrice(int standardPriceType) {
        if (standardPriceType == 1) {
            return getPlatformPrice();
        } else {
            return getRetailPrice();
        }
    }

    public String id;

    public String getRealGoodsId() {
        if (!TextUtils.isEmpty(goodsId)) {
            return goodsId;
        }
        return id;
    }

    public String applicationNo;

    public String filePath;

    public String getFilePath() {
        if (!TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        try {
            return headImages.get(0).filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String userId;

    public String merchantName;

    public String getMerchantName() {
        if(!TextUtils.isEmpty(merchantShortName)){
            return merchantShortName;
        }
        return merchantName;
    }

    public String merchantShortName;

    public String goodsNo;


    public String adTitle;


    public String specValue;
    public String headImage;
    public String goodsTitle;
    /**
     * 活动奖品图片
     */
    public String goodsImage;
    /**
     * 活动奖品规格
     */
    public String goodsSpecStr;
    /**
     * 活动奖品库存
     */
    public int availableInventory;
    private List<GoodsSpecCell> goodsSpecCells;
    public String tagName;

    public String getTagFirst() {
        if (tagName == null || "null".equals(tagName) || "".equals(tagName)) {
            return null;
        }
        return tagName.split(",")[0];
    }

    public String mapMarketingGoodsId;
    /**
     * 1砍价、2拼团、3限时秒杀、4新人专享、5积分商城商品，8、PLUS专享商品（非营销活动商品可不传此字段）
     */
    public String marketingType;
    public double pointsPrice;
    public int pointsTodayInventory;//今日可兑 9
    public int pointsEverydayInventory;//每日可兑

    public List<GoodsSpecCell> getSpecCell() {
        if (goodsSpecCells != null) {
            return goodsSpecCells;
        }
        if (TextUtils.isEmpty(specValue)) {
            return new ArrayList<>();
        } else {
            goodsSpecCells = resolveSpecListData(specValue);
            return goodsSpecCells;
        }
    }

    public boolean cheIsNoSku() {
        boolean result = true;
        List<GoodsSpecCell> goodsSpecCells = getSpecCell();
        for (int i = 0; i < goodsSpecCells.size(); i++) {
            GoodsSpecCell goodsSpecCell = goodsSpecCells.get(i);
            if (goodsSpecCell.specValue.length > 0) {
                result = false;
                return result;
            }
        }
        return result;
    }

    private List<GoodsSpecCell> resolveSpecListData(String obj) {
        List<GoodsSpecCell> result = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<GoodsSpecCell>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public double getStorePrice() {
        if (retailPrice != 0) {
            return retailPrice;
        }
        return retailPrice;
    }

    public double retailPrice;
    public double platformPrice;
    public double marketingPrice;
    public int differentSymbol;

    public double getPlusPrice() {//获得plus价格 为0就没有嘛
        try {
            if (SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false)) {
                if (marketingGoodsChildren != null && marketingGoodsChildren.size() > 0) {
                    return marketingGoodsChildren.get(0).marketingPrice;
                }
                if (goodsChildren != null && goodsChildren.size() > 0) {
                    return goodsChildren.get(0).getPlusPrice();

                }
                return plusPrice;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getShopId() {
        if(goodsShopIds!=null&&goodsShopIds.length>0){
            return goodsShopIds[0];
        }
        return SpUtils.getValue(LibApplication.getAppContext(), SpKey.CHOSE_SHOP);
    }

    public double getPlusPriceShow() {//获得plus价格 为0就没有嘛
        if (marketingGoodsChildren != null && marketingGoodsChildren.size() > 0 && "8".equals(marketingType)) {
            return marketingGoodsChildren.get(0).marketingPrice;
        }
        if (marketingGoodsChildren != null && goodsChildren.size() > 0) {
            return goodsChildren.get(0).plusPrice;
        }
        if ("8".equals(marketingType)) {
            return marketingPrice;
        }
        return plusPrice;
    }

    private double plusPrice;

    public double getPlusOrPlatformPrice(){
        if(getPlusPrice()>0){
            return getPlusPrice();
        }else {
            return getPlatformPrice();
        }
    }
    public double getPlatformPrice() {
        if (cheIsNoSku()) {
            try {
                if (marketingType != null) {
                    if (marketingGoodsChildren != null && marketingGoodsChildren.size() > 0) {


                        return marketingGoodsChildren.get(0).platformPrice;
                    }
                    return goodsChildren.get(0).platformPrice;
                } else {
                    return goodsChildren.get(0).platformPrice;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return platformPrice;
    }


    public double getRetailPrice() {
        if (cheIsNoSku()) {
            if (marketingType != null) {
                if (marketingGoodsChildren != null && marketingGoodsChildren.size() > 0) {
                    return marketingGoodsChildren.get(0).platformPrice;
                }
                return goodsChildren.get(0).retailPrice;
            } else {
                return goodsChildren.get(0).retailPrice;
            }
        }
        return retailPrice;
    }

    public double getLivePrice() {

        return goodsChildren.get(0).livePrice;
    }

    public int salesMin;

    public int marketingSales;

    public int marketingSalesMin;
    public boolean isErrorCount = false;

    public int getMarketingSalesMin() {
        if (isErrorCount) {
            return 1;
        }
        if (marketingSalesMin > 0) {
            return marketingSalesMin;
        }
        if (salesMin > 0) {
            return salesMin;
        }
        return 1;
    }

    public int marketingSalesMax;

    public int getMarketingSalesMax() {
        if (marketingSalesMax > 0) {
            return marketingSalesMax;
        }
        if (salesMax > 0) {
            return salesMax;
        }
        return 99999;
    }

    public String marketingId;

    public int salesMax;

    /**
     * 1服务、2标品、3积分商品
     */
    public String goodsType;

    public int getGoodsTypeInt() {
        return Integer.parseInt(goodsType);
    }







    public String additionNote;



//    public int availableInventory;//此字段暂时屏蔽掉 因此层返回的库存就是有问题的 建议使用999 交给后台去判断


    public int appSales;

    public int virtualSales;

    public List<HeadImages> headImages;

    public String getGoodsIcon() {
        if (headImages != null && headImages.size() > 0) {
            return headImages.get(0).filePath;
        }
        return null;
    }


    public void change() {
        if (headVideos.size() > 0) {
            headImages.add(0, headVideos.get(0));
            headVideos.clear();
        }
    }

    public List<HeadImages> headVideos = new ArrayList<>();

    public List<HeadImages> contentVideos = new ArrayList<>();


    public String goodsFiles;

    public List<GoodsChildren> goodsChildren;
    public List<GoodsChildren> marketingGoodsChildren;
    private String[] marketingGoodsShopIds=new String[]{};
    private String[] goodsShopIds=new String[]{};
    public String[] shopIds;

//    public String getGoodsShopIdListString() {
//        return new SimpleStringBuilder<String>().putList(goodsShopIds,null).text();
////        if(marketingType==null){
////        }else {
////            return new SimpleStringBuilder<String>().putList(marketingGoodsShopIds,null).text();
////        }
//    }

    public String[] getGoodsShopIdListStringArray() {
        if(shopIds!=null){
            return shopIds;
        }
        if (marketingType == null) {
            if("2".equals(goodsType)){
                return new String[]{};
            }
            return goodsShopIds;
        } else {
            if ("5".equals(marketingType)) {//积分需要特殊处理下
                return marketingGoodsShopIds;
            }
            if("2".equals(goodsType)){
                return new String[]{};
            }
            return goodsShopIds;
//            return intersect(marketingGoodsShopIds,goodsShopIds);
        }
    }

    public static String[] intersect(String[] arr1, String[] arr2) {
        Map<String, Boolean> map = new HashMap<>();
        LinkedList<String> list = new LinkedList<>();
        for (String str : arr1) {
            if (!map.containsKey(str)) {
                map.put(str, Boolean.FALSE);
            }
        }
        for (String str : arr2) {
            if (map.containsKey(str)) {
                map.put(str, Boolean.TRUE);
            }
        }

        for (Map.Entry<String, Boolean> e : map.entrySet()) {
            if (e.getValue().equals(Boolean.TRUE)) {
                list.add(e.getKey());
            }
        }

        String[] result = {};
        return list.toArray(result);
    }

    public String pointsGoodsNote;

    public boolean checkMarkChildrenIsZero() {
        if (marketingGoodsChildren != null) {
            for (int i = 0; i < marketingGoodsChildren.size(); i++) {
                if (marketingGoodsChildren.get(i).availableInventory > 0) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }

    }
    private String marketingEndTime;

    public String getMarketingEndTime() {//返回的"marketingEndTime": "2020-09-20T11:10:00"
        if (marketingEndTime.contains("T")) {//因为时有时无 很诡异 所以要判断下 以免出现问题
            return marketingEndTime.replace("T", " ");
        } else {
            return marketingEndTime;
        }
    }

    public void setMarketingEndTime(String marketingEndTime) {
        this.marketingEndTime = marketingEndTime;
    }

    public class HeadImages {
        public long id;

        public int fileType;

        public String getSplash() {
            if (!TextUtils.isEmpty(thumbsPath)) {
                return thumbsPath;
            }
            return filePath;
        }

        public String filePath;


        public String thumbsPath;

        public String imageTitle;

        public String imageDescription;


        public long goodsId;


        public String ranking;


    }

    public static class GoodsChildren {
        public ShopDetailModel storeDetialModel;
        public String id;
        public String marketingType;
        public String mapMarketingGoodsId;
        public String shopId;//异业商品门店id

        public GoodsChildren(Goods2DetailKick.MarketingGoodsChildDTOS marketingGoodsChildDTOS) {
            this.id = marketingGoodsChildDTOS.id;
            this.goodsChildId = marketingGoodsChildDTOS.goodsChildId;
            this.goodsTitle = marketingGoodsChildDTOS.goodsTitle;
            this.platformPrice = marketingGoodsChildDTOS.platformPrice;
            this.goodsSpecStr=marketingGoodsChildDTOS.goodsSpecStr;
            this.marketingPrice = marketingGoodsChildDTOS.marketingPrice;
            this.availableInventory = marketingGoodsChildDTOS.availableInventory;
            this.retailPrice = marketingGoodsChildDTOS.retailPrice;
            this.id = marketingGoodsChildDTOS.id;
            this.mapMarketingGoodsId=marketingGoodsChildDTOS.mapMarketingGoodsId;
            this.marketingType="1";
        }

        public GoodsChildren(Goods2DetailPin.MarketingGoodsChildDTOS marketingGoodsChildDTOS) {
            this.id = marketingGoodsChildDTOS.id;
            this.goodsChildId = marketingGoodsChildDTOS.goodsChildId;
            this.goodsTitle = marketingGoodsChildDTOS.goodsTitle;
            this.goodsSpecStr=marketingGoodsChildDTOS.goodsSpecStr;
            this.platformPrice = marketingGoodsChildDTOS.platformPrice;
            this.marketingPrice = marketingGoodsChildDTOS.marketingPrice;
            this.availableInventory = marketingGoodsChildDTOS.availableInventory;
            this.retailPrice = marketingGoodsChildDTOS.retailPrice;
            this.id = marketingGoodsChildDTOS.id;
            this.mapMarketingGoodsId=marketingGoodsChildDTOS.mapMarketingGoodsId;
            this.marketingType="2";
        }


        public String getId(String marketingType) {
            if (!TextUtils.isEmpty(marketingType)) {
                return goodsChildId;
            }
            return id;
        }

        public double getPlusPrice() {//获得plus价格 为0就没有嘛
            if (SpUtils.getValue(LibApplication.getAppContext(), SpKey.PLUSSTATUS, false)) {
                return plusPrice;
            }
            return 0;
        }

        private double plusPrice;

        public String goodsChildId;


        public String barcodeSku;

        public String goodsTitle;
        public double livePrice;

        public String adTitle;

        public String goodsSpec;

        public String getGoodsSpecStr() {


            if(TextUtils.isEmpty(goodsSpecStr)){
                goodsSpecStr=goodsTitle;
            }
            return goodsSpecStr;
        }

        public String goodsSpecStr = "";



        public int availableInventory;

        public int getAvailableInventory() {
            if (storeDetialModel != null && "1".equals(storeDetialModel.isSupportOverSold)) {//允许超卖就返回可售卖999
                return 999999;
            }
            return availableInventory;
        }

        public int getAvailableInventoryMark(GoodsChildren goodsChildren) {
            //2021-07-10 修改活动库存不依赖于门店库存直接去活动库存返回  2021年11.12 移除秒杀判断


            if ("1".equals(marketingType) || "2".equals(marketingType)
                    || "4".equals(marketingType) || "6".equals(marketingType)
                    || "7".equals(marketingType) || "8".equals(marketingType) || "9".equals(marketingType)) {
                return availableInventory;
            }
            if (availableInventory <= 0) {//活动库存小于0
                return 0;
            } else {
                if (storeDetialModel != null && "1".equals(storeDetialModel.isSupportOverSold)) { //
                    return availableInventory;//负库存销售
                }else {//
                    int result = availableInventory < goodsChildren.availableInventory ? availableInventory : goodsChildren.availableInventory;
                    return result > 0 ? result : 0;
                }

            }
        }

        public double retailPrice;

        public double platformPrice;
        public double marketingPrice;

        public String beginTime;
        public String endTime;

        public double pointsPrice;

    }


}
