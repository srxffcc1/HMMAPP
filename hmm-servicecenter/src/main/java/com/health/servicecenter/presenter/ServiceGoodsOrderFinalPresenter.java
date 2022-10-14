package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.ServiceOrderFinalContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.AddressListModel;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Li
 * @date 2019/04/29 16:19
 * @des
 */

public class ServiceGoodsOrderFinalPresenter implements ServiceOrderFinalContract.Presenter {

    private Context mContext;
    private ServiceOrderFinalContract.View mView;

    public ServiceGoodsOrderFinalPresenter(Context context, ServiceOrderFinalContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 25000 下单走向
     *
     * @param map
     */
    @Override
    public void submitOrder(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "9522");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            String orderId = JsonUtils.getString(jsonObject, "data");
                            mView.sucessSubmit(orderId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
//                        mErrorMessage = msg;
                        mView.failOrder(msg);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    /**
     * 25025 预下单走向
     *
     * @param map
     */
    @Override
    public void submitOrderV(Map<String, Object> map) {
        //重新请求 还原数据
//        mErrorMessage = "";
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            BigDecimal totalDecimalPay = new BigDecimal(0);
                            BigDecimal totalDecimalFee = new BigDecimal(0);
                            BigDecimal totalDecimalNoFee = new BigDecimal(0);
                            JSONObject jsonObject = new JSONObject(obj);
                            String totalPayAmount = JsonUtils.getString(jsonObject.getJSONObject("data"), "totalPayAmount");
                            totalDecimalPay = new BigDecimal(totalPayAmount);
                            String totalFeeAmount = JsonUtils.getString(jsonObject.getJSONObject("data"), "totalDeliveryAmount");
                            totalDecimalFee = new BigDecimal(totalFeeAmount);
                            totalDecimalNoFee = totalDecimalPay.subtract(totalDecimalFee);
                            mView.sucessSubmitV(totalPayAmount, totalDecimalNoFee.doubleValue() + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
//                        mErrorMessage = msg;
                        mView.failOrder(msg);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }

    @Override
    public void submitOrderU(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            String orderId = JsonUtils.getString(jsonObject, "data");
                            mView.sucessSubmit(orderId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getAddressList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9061");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetAddressListSuccess(resolveAddressListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void checkMearchantOpenChange(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25026");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj).getJSONArray("data").getJSONObject(0);
                            boolean crossStoreOrder = "1".equals(jsonObject.optString("crossStoreOrder"));
                            boolean needDeliveryTime = "1".equals(jsonObject.optString("needDeliveryTime"));
                            mView.sucessCheck(crossStoreOrder,needDeliveryTime);
                        } catch (Exception e) {
                            mView.sucessCheck(false,false);
                            e.printStackTrace();
                        }

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.sucessCheck(false,false);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void checkMearchantFeeOpenChange(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "101000");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        boolean result = true; //支持商品计算 true为支持
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            if ("1".equals(jsonObject.optString("data"))) {
                                result = false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mView.sucessFeeCheck(result);

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.sucessFeeCheck(true);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private ShopDetailModel resolveStoreData(String obj) {
        ShopDetailModel newStoreDetialModel = null;
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
            newStoreDetialModel = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newStoreDetialModel;
    }

    @Override
    public void getShopDetailOnly(Map<String, Object> map, final GoodsBasketStore goodsBasketStore) {
        map.put(Functions.FUNCTION, "101001");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            ShopDetailModel shopDetailModel = resolveStoreData(obj);
                            goodsBasketStore.merchantType = shopDetailModel.merchantType;//商家属性 1异业商家 2合伙人商家
                            for (int i = 0; i < goodsBasketStore.getGoodsBasketCellAllList().size(); i++) {
                                goodsBasketStore.getGoodsBasketCellAllList().get(i).setIsSupportOverSold(shopDetailModel.isSupportOverSold);
                            }
                            goodsBasketStore.mchId=shopDetailModel.userId;
                            goodsBasketStore.setDepartID(shopDetailModel.getYtbDepartID());
                            goodsBasketStore.appointmentPhone = shopDetailModel.appointmentPhone;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mView.onSucessGetShopDetailOnly(goodsBasketStore);

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
//                        mView.onSucessGetShopDetailOnly();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getShopDetailOnlyVisit(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "101001");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetShopDetailOnlyVisit(resolveStoreData(obj));

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private List<AddressListModel> resolveAddressListData(String obj) {
        List<AddressListModel> result = new ArrayList<>();
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
            Type type = new TypeToken<List<AddressListModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getIsNewAppMember() {
//        if(BuildConfig.DEBUG){
//            mView.onSucessIsNewAppMember(1);
//            return;
//        }
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "25021");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onSucessIsNewAppMember(new JSONObject(obj).getJSONObject("data").getInt("isNewAppMember"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessIsNewAppMember(0);
                    }
                });
    }
}
