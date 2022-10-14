package com.healthy.library.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.contract.StoreShareContract;
import com.healthy.library.model.ShopDetailModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author jin   分享P层 用于统一访问门店
 * @date 2019/03/27 11:37
 * @des
 */

public class StoreSharePresenter implements StoreShareContract.Presenter {

    private Context mContext;

    String CHOSE_MC;
    String CHOSE_SHOP;
    String CHOSE_SHOPNAME;
    String CHOSE_SHOPNAMEDETAIL;
    String CHOSE_SHOPADDRESS;
    String CHOSE_SHOPDISTANCE;

    public StoreSharePresenter(Context context) {
        mContext = context;
    }

    @Override
    public void getShareStoreDetial(String shopId) {

//        CHOSE_MC=SpUtils.getValue(mContext,SpKey.CHOSE_MC);
//        CHOSE_SHOP=SpUtils.getValue(mContext,SpKey.CHOSE_SHOP);
//        CHOSE_SHOPNAME=SpUtils.getValue(mContext,SpKey.CHOSE_SHOPNAME);
//        CHOSE_SHOPNAMEDETAIL=SpUtils.getValue(mContext,SpKey.CHOSE_SHOPNAMEDETAIL);
//        CHOSE_SHOPADDRESS=SpUtils.getValue(mContext,SpKey.CHOSE_SHOPADDRESS);
//        CHOSE_SHOPDISTANCE=SpUtils.getValue(mContext,SpKey.CHOSE_SHOPDISTANCE);
//
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("shopId", shopId);
//        map.put(Functions.FUNCTION, "101001");
//        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
//        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoStringObserver(null, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        NewStoreDetialModel newStoreDetialModel = resolveStoreData(obj);
//                        if(newStoreDetialModel!=null){
//
//                            AMapLocation aMapLocation = LocUtil.getLocationBean(mContext, SpKey.LOC_CHOSE);
//                            aMapLocation.setAdCode(newStoreDetialModel.addressArea);//默认市级加5 得到模糊区县
//                            aMapLocation.setProvince(newStoreDetialModel.provinceName);
//                            aMapLocation.setCity(newStoreDetialModel.cityName);
//                            aMapLocation.setDistrict(newStoreDetialModel.addressAreaName);
//                            SpUtils.store(mContext,SpKey.CHOSE_MC,newStoreDetialModel.userId);
//                            SpUtils.store(mContext,SpKey.CHOSE_SHOP,newStoreDetialModel.id);
//                            SpUtils.store(mContext,SpKey.CHOSE_SHOPNAME,newStoreDetialModel.shopName);
//                            SpUtils.store(mContext, SpKey.CHOSE_SHOPNAMEDETAIL, getFormatShopName(newStoreDetialModel.shopName,newStoreDetialModel.shopName));
//                            SpUtils.store(mContext, SpKey.CHOSE_SHOPADDRESS, newStoreDetialModel.provinceName+newStoreDetialModel.cityName+newStoreDetialModel.addressAreaName+newStoreDetialModel.addressDetails);
//                            SpUtils.store(mContext, SpKey.CHOSE_SHOPDISTANCE, newStoreDetialModel.distance+"");
//                            LocUtil.storeLocationChose(mContext, aMapLocation);
//                            EventBus.getDefault().post(new UpdateUserLocationMsg());
////                            EventBus.getDefault().post(new DialogDissmiss());
//
//
//                        }
//                    }
//
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                    }
//                });
    }
    public static String getFormatShopName(String left, String right) {
        String leftresult = left.substring(0, left.length() > 5 ? 5 : left.length());
        String rightresult = left.substring(left.length() > 3 ? left.length() - 3 : 0, left.length());
        if (left.length() < 8) {
            return leftresult;
        }
        return leftresult + "..." + rightresult;
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

    public void back() {
//        SpUtils.store(mContext,SpKey.CHOSE_MC,CHOSE_MC);
//        SpUtils.store(mContext,SpKey.CHOSE_SHOP,CHOSE_SHOP);
//        SpUtils.store(mContext,SpKey.CHOSE_SHOPNAME,CHOSE_SHOPNAME);
//        SpUtils.store(mContext, SpKey.CHOSE_SHOPNAMEDETAIL, CHOSE_SHOPNAMEDETAIL);
//        SpUtils.store(mContext, SpKey.CHOSE_SHOPADDRESS, CHOSE_SHOPADDRESS);
//        SpUtils.store(mContext, SpKey.CHOSE_SHOPDISTANCE, CHOSE_SHOPDISTANCE);
    }
}