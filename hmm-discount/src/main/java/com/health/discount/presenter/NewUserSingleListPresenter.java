package com.health.discount.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.discount.contract.NewUserListContract;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.NewUserListModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewUserSingleListPresenter implements NewUserListContract.Presenter {
    private Context mContext;
    private NewUserListContract.View mView;

    public NewUserSingleListPresenter(Context context, NewUserListContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getList(final Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9112");
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        List<NewUserListModel> list=resolveTabListData(obj);
                        mView.onSucessGetList(resolveTabListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetList(null);
                    }
                });
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
                .subscribe(new NoStringObserver(mView, mContext, false) {
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

    @Override
    public void addRemind(Map<String, Object> map, final String type) {
        map.put(Functions.FUNCTION, "marketing_9308");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onSuccessAddRemind(new JSONObject(obj).getString("msg"),type);
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
                    }
                });
    }

    private List<NewUserListModel> resolveTabListData(String obj) {
        List<NewUserListModel> result = new ArrayList<>();
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
            Type type = new TypeToken<List<NewUserListModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
