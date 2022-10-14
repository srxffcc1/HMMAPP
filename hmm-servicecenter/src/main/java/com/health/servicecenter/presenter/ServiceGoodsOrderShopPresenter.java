package com.health.servicecenter.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.R;
import com.health.servicecenter.activity.StoreBlockChildHolder;
import com.health.servicecenter.contract.ServiceOrderShopContract;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketGroup;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.model.GoodsFee;
import com.healthy.library.model.GoodsShop;
import com.healthy.library.model.GoodsSpecDetail;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.utils.FormatUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author Li
 * @date 2019/04/29 16:19
 * @des
 */

public class ServiceGoodsOrderShopPresenter implements ServiceOrderShopContract.Presenter {

    private Context mContext;
    private ServiceOrderShopContract.View mView;
    private String isNtReal;

    public ServiceGoodsOrderShopPresenter(Context context, ServiceOrderShopContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getShopDetailOnly(Map<String, Object> map, final GoodsBasketStore goodsBasketStore, final StoreBlockChildHolder storeBlockChildHolder) {
        map.put(Functions.FUNCTION, "101001");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetShopDetailOnly(resolveStoreData(obj), goodsBasketStore, storeBlockChildHolder);

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetShopDetailOnly(null, goodsBasketStore, storeBlockChildHolder);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private ShopDetailModel resolveStoreData(String obj) {
        ShopDetailModel shopDetailModel = null;
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<ShopDetailModel>() {
            }.getType();
            shopDetailModel = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopDetailModel;
    }

    @Override
    public void getPickShopOnly(final Map<String, Object> map, final GoodsBasketStore goodsBasketStore, final StoreBlockChildHolder storeBlockChildHolder) {
//        map.put(Functions.FUNCTION, "9519");
//        map.put("source","1");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoStringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        List<GoodsShop> shopList=resolveShopListData(obj);
//                        if(shopList.size()==0){
//                            goodsBasketStore.msg=resolveMessageData(map,obj);
//                            goodsBasketStore.notcheck= R.id.checkB;
//                        }else {
//                            goodsBasketStore.msg=null;
//                            goodsBasketStore.notcheck= -1;
//                        }
//                        mView.onSucessGetPickShopOnly(resolveShopListData(obj),goodsBasketStore.msg,goodsBasketStore,storeBlockChildHolder);
//                    }
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.onSucessGetPickShopOnly(null,goodsBasketStore.msg,goodsBasketStore,storeBlockChildHolder);
//
//                    }
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//                });
    }

    private String resolveMessageData(Map<String, Object> map, String obj) {
        GoodsFee goodsFee = resolveShopFee(obj);
        String result = "";
        if (goodsFee.isReach == 0) {//支持配送
            return "当前地址不支持配送";
        }
        if (goodsFee.isSupport == 0) {//
            double amount = Double.parseDouble(map.get("amount").toString());
            if (amount >= goodsFee.minAmount) {//说明起送金额够的 那就有可能门店不支持配送
                return "当前门店不支持配送";
            } else {
                return "订单金额未到达起送标准" + FormatUtils.moneyKeep2Decimals(goodsFee.minAmount) + "元";
            }
        }
        return result;
    }

    private List<GoodsShop> resolveShopListData(String obj) {
        List<GoodsShop> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONArray("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<GoodsShop>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<ShopDetailModel> resolveStoreListData(String obj) {
        List<ShopDetailModel> shopDetailModel = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONArray("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<ShopDetailModel>>() {
            }.getType();
            shopDetailModel = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopDetailModel;
    }

    @Override
    public void getPickShop(Map<String, Object> map, final GoodsBasketStore goodsBasketStore, final StoreBlockChildHolder storeBlockChildHolder) {
        map.put(Functions.FUNCTION, "101004-1");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);

                        List<ShopDetailModel> tmp = resolveStoreListData(obj);
                        List<GoodsShop> shopList = new SimpleArrayListBuilder<GoodsShop>().putList(tmp, new ObjectIteraor<ShopDetailModel>() {
                            @Override
                            public GoodsShop getDesObj(ShopDetailModel o) {
                                return new GoodsShop(o);
                            }
                        });
                        goodsBasketStore.goodsPickShopList = shopList;
                        if (goodsBasketStore.goodsPickShop == null) {
                            try {
                                goodsBasketStore.goodsPickShop = shopList.get(0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mView.onSucessGetPickShop(shopList, goodsBasketStore, storeBlockChildHolder);
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetPickShop(null, goodsBasketStore, storeBlockChildHolder);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private GoodsFee resolveShopFee(String obj) {
        GoodsFee result = null;
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<GoodsFee>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getFeeOnly(final Map<String, Object> map, final GoodsBasketStore goodsBasketStore, final StoreBlockChildHolder storeBlockChildHolder) {
        map.put(Functions.FUNCTION, "25022");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        GoodsFee goodsFee = resolveShopFee(obj);
                        if (goodsFee == null) {
                            mView.onSucessGetFeeOnly(null, goodsBasketStore, storeBlockChildHolder);
                        } else {
                            if (goodsFee.isSupport == 1 && goodsFee.isReach == 1) {
                                goodsBasketStore.msg = null;
                                goodsBasketStore.notcheck = -1;
                            } else {
                                goodsBasketStore.msg = resolveMessageData(map, obj);
                                goodsBasketStore.notcheck = R.id.checkB;
                            }
                            mView.onSucessGetFeeOnly(goodsFee, goodsBasketStore, storeBlockChildHolder);
                        }

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetFeeOnly(null, goodsBasketStore, storeBlockChildHolder);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getFee(Map<String, Object> map, final GoodsBasketStore goodsBasketStore, final StoreBlockChildHolder storeBlockChildHolder) {
        map.put(Functions.FUNCTION, "25022");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        GoodsFee goodsFee = resolveShopFee(obj);
                        mView.onSucessGetFee(goodsFee, goodsBasketStore, storeBlockChildHolder);

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetFee(null, goodsBasketStore, storeBlockChildHolder);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private List<GoodsSpecDetail> resolveDetailData(String obj) {
        List<GoodsSpecDetail> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONArray("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<GoodsSpecDetail>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getSpecDetail(String cellGoodsMarketingType, Map<String, Object> map, final GoodsBasketStore goodsBasketStore, final GoodsBasketCell goodsBasketCellOrg, final GoodsBasketCell.OnItemChange onItemChange) {
        if(map.get("shopId")==null){
            return;
        }
        if(TextUtils.isEmpty(map.get("shopId").toString())){
            return;
        }
        if (goodsBasketCellOrg.isGift) {//营销活动切门店 则商品原样返回
            goodsBasketCellOrg.setExtraGoodsBasketCell(null);
            GoodsBasketCell exgoodsBasketCell = new GoodsBasketCell(goodsBasketCellOrg);
            goodsBasketCellOrg.setExtraGoodsBasketCell(exgoodsBasketCell);
            onItemChange.bitNice();
            return;
        } else {
            final String marketingType = goodsBasketCellOrg.getGoodsMarketingTypeOrgExpUnder();//
            if ("0".equals(marketingType)
                    || "-5".equals(marketingType)
                    || "-4".equals(marketingType)
                    || "-1".equals(marketingType)
            ) {//10的时候拦截请求 然后返回
                map.put("marketingType", null);
                map.put("mapMarketingGoodsId", null);
                map.put("mapMarketingGoodsChildId", null);
            }
            map.put(Functions.FUNCTION, "9203-1");
            ObservableHelper.createObservable(mContext, map)
                    .subscribe(new NoStringObserver(mView, mContext, false) {
                        @Override
                        protected void onGetResultSuccess(String obj) {
                            super.onGetResultSuccess(obj);
                            GoodsBasketCell goodsBasketCell = null;
                            List<GoodsSpecDetail> goodsSpecDetailList = resolveDetailData(obj);
                            GoodsSpecDetail goodsSpecDetail = null;
                            goodsBasketCellOrg.setExtraGoodsBasketCell(null);
                            try {
                                goodsSpecDetail = goodsSpecDetailList.get(0);
                                goodsSpecDetail.setNtReal("1".equals(isNtReal));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (goodsSpecDetail != null) {
                                GoodsBasketCell.GoodsMarketing goodsMarketing = new GoodsBasketCell.GoodsMarketing(goodsBasketCellOrg.getGoodsMarketingIdOrg());
                                if ("-4".equals(marketingType) || "-5".equals(marketingType) ) {//10的时候修改
                                    goodsSpecDetail.marketingType = marketingType;
                                }
                                if (goodsSpecDetail.marketingType == null) {
                                    goodsMarketing = null;
                                } else {
                                    goodsMarketing.availableInventory = goodsSpecDetail.getAvailableInventoryMark();
                                    if ("-4".equals(marketingType) || "-5".equals(marketingType) ) {//10的时候修改
                                        goodsMarketing.availableInventory = 999;
                                    }
                                    goodsMarketing.mapMarketingGoodsId = goodsSpecDetail.mapMarketingGoodsId;
                                    goodsMarketing.marketingType = goodsSpecDetail.marketingType;
                                    goodsMarketing.id = goodsSpecDetail.getgoodsMarketingGoodsSpec();
                                    goodsMarketing.marketingPrice = goodsSpecDetail.getMarketingPriceInOrder();
                                    if ("-4".equals(marketingType) || "-5".equals(marketingType) ) {//10的时候修改
                                        goodsMarketing.marketingPrice = goodsSpecDetail.platformPrice;
                                    }
                                    goodsMarketing.pointsPrice = goodsSpecDetail.pointsPrice;
                                    try {
                                        goodsMarketing.salesMin = goodsBasketCellOrg.goodsMarketingDTO.salesMin;
                                        goodsMarketing.salesMax = goodsBasketCellOrg.goodsMarketingDTO.salesMax;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                                goodsBasketCell = new GoodsBasketCell(goodsSpecDetail.platformPrice,
                                        goodsSpecDetail.retailPrice,
                                        goodsSpecDetail.getPlusPrice(),
                                        goodsSpecDetail.goodsType,
                                        goodsSpecDetail.isPlusOnly, "0", goodsBasketCellOrg.getGoodsBarCode());
                                goodsBasketCell.goodsSpecDesc = goodsSpecDetail.goodsSpecStr;
                                goodsBasketCell.goodsMarketingDTO = goodsMarketing;
                                goodsBasketCell.mchId = goodsBasketCellOrg.mchId;
                                goodsBasketCell.goodsId = goodsBasketCellOrg.goodsId;
                                goodsBasketCell.goodsStock = goodsSpecDetail.getRealAvailableInventory();
                                if ("-4".equals(marketingType) || "-5".equals(marketingType)) {//10的时候修改
                                    goodsBasketCell.goodsStock = 999;
                                }
                                System.out.println("9203-1:" + goodsSpecDetail.getGoodsSpec());
                                goodsBasketCell.setGoodsSpecId(goodsSpecDetail.getGoodsSpec());
                                goodsBasketCell.goodsTitle = goodsBasketCellOrg.goodsTitle;
                                goodsBasketCell.goodsImage = goodsBasketCellOrg.goodsImage;
                                goodsBasketCell.goodsQuantity = (goodsBasketCellOrg.goodsQuantity);
                                GoodsBasketGroup goodsBasketGroup = new GoodsBasketGroup(goodsBasketCell);
                                List<GoodsBasketCell> list = new ArrayList<>();
                                goodsBasketCell.shopIdList = goodsBasketCellOrg.shopIdList;
                                goodsBasketCell.ischeck = true;
                                goodsBasketCell.goodsShopId = goodsBasketStore.goodsPickShop.shopId;
                                goodsBasketCell.goodsShopName = goodsBasketStore.goodsPickShop.shopName;
                                goodsBasketCell.goodsShopAddress = goodsBasketStore.goodsPickShop.getShopAddressDetail();
                                if ("6".equals(goodsBasketCell.getGoodsMarketingTypeOrg()) || "7".equals(goodsBasketCell.getGoodsMarketingTypeOrg())) {
                                    goodsBasketCell.setDiscountTopModel(goodsBasketCellOrg.getDiscountTopModel());
                                    goodsBasketCell.setGoodsQuantityGiftNeedFixOrg(0);
                                } else {
                                    ////System.out.println("重置买送活动");
                                    goodsBasketCell.setDiscountTopModel(null);
                                    goodsBasketCell.setGoodsQuantityGiftNeedFixOrg(0);
                                }
                            }
                            if (goodsBasketCell == null) {//返回可能是个null //原门店是秒杀，新门店是满减，不会返回商品信息
                                goodsBasketCell = new GoodsBasketCell(goodsBasketCellOrg);
                                goodsBasketCell.isError = true;//不会返回得话 就不卖了 标记下error吧
                                goodsBasketCell.goodsMarketingDTO = goodsBasketCellOrg.goodsMarketingDTO;//抹除商品里的活动
                                goodsBasketCell.goodsQuantity = 0;
                                goodsBasketCell.goodsStock = 0;
                            }
                            goodsBasketCell.goodsShopId = goodsBasketStore.goodsPickShop.shopId;
                            if("-1".equals(marketingType)){//直播的商品的话
                                goodsBasketCellOrg.goodsStock=goodsBasketCell.getStock();
                                goodsBasketCellOrg.setGoodsSpecId(goodsBasketCell.getGoodsSpecId());
                                goodsBasketCellOrg.goodsMarketingDTO.availableInventory=goodsBasketCellOrg.goodsStock;
                            }else {

                                goodsBasketCellOrg.setExtraGoodsBasketCell(goodsBasketCell);
                            }
                            onItemChange.bitNice();
                        }

                        @Override
                        protected void onFailure() {
                            super.onFailure();
                            //查询失败的话 标志位置为true 拷贝原商品数据

                        }

                        @Override
                        protected void onFailure(String msg) {
                            super.onFailure(msg);
                            goodsBasketCellOrg.setExtraGoodsBasketCell(null);
                            GoodsBasketCell exgoodsBasketCell = new GoodsBasketCell(goodsBasketCellOrg);
                            exgoodsBasketCell.isError = true;
                            exgoodsBasketCell.goodsShopId = goodsBasketStore.goodsPickShop.shopId;
                            exgoodsBasketCell.goodsQuantity = 0;
                            exgoodsBasketCell.goodsStock = 0;
                            exgoodsBasketCell.goodsMarketingDTO = null;
                            if (msg.contains("系统忙")) {
                                exgoodsBasketCell.goodsMarketingDTO = goodsBasketCellOrg.goodsMarketingDTO;
                                return;
                            }
                            exgoodsBasketCell.setDiscountTopModel(null);
                            exgoodsBasketCell.setGoodsQuantityGiftNeedFixOrg(0);
                            goodsBasketCellOrg.setExtraGoodsBasketCell(exgoodsBasketCell);
                            onItemChange.bitNice();
                        }

                        @Override
                        protected void onFinish() {
                            super.onFinish();
                            mView.onRequestFinish();
                        }
                    });
        }
    }

    @Override
    public void getReceiveShop(Map<String, Object> map, final GoodsBasketStore goodsBasketStore, final StoreBlockChildHolder storeBlockChildHolder) {
//        map.put(Functions.FUNCTION, "9519");
//        map.put("source","1");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoStringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        List<GoodsShop> shopList=resolveShopListData(obj);
//                        goodsBasketStore.goodsReceiveShopList=shopList;
//                        if(goodsBasketStore.goodsReceiveShop ==null){
//                            try {
//                                goodsBasketStore.goodsReceiveShop =shopList.get(0);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        mView.onSucessGetReceiveShop(resolveShopListData(obj),goodsBasketStore,storeBlockChildHolder);
//                    }
//
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.onSucessGetReceiveShop(null,goodsBasketStore,storeBlockChildHolder);
//
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//                });
    }

    public void setIsNtReal(String isNtReal) {
        this.isNtReal = isNtReal;
    }

    public String getIsNtReal() {
        return isNtReal;
    }
}
