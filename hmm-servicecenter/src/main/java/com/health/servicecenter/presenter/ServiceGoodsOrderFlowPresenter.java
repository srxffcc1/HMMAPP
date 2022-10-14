package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.ServiceOrderContract;
import com.healthy.library.model.AddressListModel;
import com.healthy.library.model.GoodsShop;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

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

public class ServiceGoodsOrderFlowPresenter implements  ServiceOrderContract.Presenter {

    private Context mContext;
    private ServiceOrderContract.View mView;

    public ServiceGoodsOrderFlowPresenter(Context context, ServiceOrderContract.View view) {
        mContext = context;
        mView = view;
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
    private List<AddressListModel> resolveAddressListData(String obj) {
        List<AddressListModel> result = new ArrayList<>();
        try {
            JSONArray data=new JSONObject(obj).getJSONArray("data");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<AddressListModel>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String resolveMessageData(String obj) {
        String result= null;
        try {
            result = new JSONObject(obj).optString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<GoodsShop> resolveShopListData(String obj) {
        List<GoodsShop> result = new ArrayList<>();
        try {
            JSONArray data=new JSONObject(obj).getJSONArray("data");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<GoodsShop>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public void getPickShopOnly(Map<String, Object> map) {//为了获得最低配送费
        map.put(Functions.FUNCTION, "9519");
        map.put("source","1");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetPickShopOnly(resolveShopListData(obj),resolveMessageData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getPickShop(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9519");
//        map.put("shopType","2");
        map.put("source","1");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetPickShop(resolveShopListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getPickServiceShop(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9519");
        map.put("shopType","1");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetPickServiceShop(resolveShopListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getPickShopFee(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9520");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

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
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }
}
