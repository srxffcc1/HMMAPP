package com.health.discount.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.discount.contract.ActBlockContract;
import com.healthy.library.model.ActGoodsItem;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ActBlockPresenter implements ActBlockContract.Presenter {
    private Context mContext;
    private ActBlockContract.View mView;

    public ActBlockPresenter(Context context, ActBlockContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getActGoodsList(Map<String, Object> map) {
        if(map.get("shopId")==null){
            return;
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onSucessGetList(resolveTabListData(obj), new JSONObject(obj).getJSONObject("data").getInt("firstPageSize"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetList(null, 0);
                    }
                });
    }

    private List<ActGoodsItem> resolveTabListData(String obj) {
        List<ActGoodsItem> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("goodsList");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<ActGoodsItem>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
