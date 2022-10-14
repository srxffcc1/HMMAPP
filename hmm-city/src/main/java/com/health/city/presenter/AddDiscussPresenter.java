package com.health.city.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.city.contract.AddDiscussContract;
import com.health.city.model.UserInfoCityModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddDiscussPresenter implements AddDiscussContract.Presenter{
    private Context mContext;
    private AddDiscussContract.View mView;

    public AddDiscussPresenter(Context context, AddDiscussContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void addDiscuss(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7010");
        map.put("merchantId",SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        SpUtils.store(mContext, SpKey.DISCUSS_TMP, "");
                        mView.onSuccessAdd();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
//                        mView.onGetStoreDetailFail();
                    }
                });
    }

    @Override
    public void addDiscuss2(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7010");
        map.put("merchantId",SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        SpUtils.store(mContext, SpKey.DISCUSS_TMP, "");
                        mView.onSuccessAdd();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
//                        mView.onGetStoreDetailFail();
                    }
                });
    }

    @Override
    public void addReview(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7016");
        map.put("merchantId",SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessAdd();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
//                        mView.onGetStoreDetailFail();
                    }
                });
    }

    @Override
    public void addReview2(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7016");
        map.put("merchantId",SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessAdd();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
//                        mView.onGetStoreDetailFail();
                    }
                });
    }

    private UserInfoCityModel resolveMineData(String obj) {
        UserInfoCityModel result = new UserInfoCityModel();
        try {
            JSONObject data=new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<UserInfoCityModel>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void getMine() {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "1011");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetMine(resolveMineData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
//                        mView.onGetStoreDetailFail();
                    }
                });
    }
}
