package com.health.second.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.second.contract.SecondBlockContract;
import com.health.second.contract.SecondBlockMainContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.AppIndexCustom;
import com.healthy.library.model.AppIndexGoodsList;
import com.healthy.library.model.MainBlockDetailModel;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.ShopCartModel;
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


public class SecondBlockWithMainPresenter implements SecondBlockMainContract.Presenter {

    private Context mContext;
    private SecondBlockMainContract.View mView;

    public SecondBlockWithMainPresenter(Context context, SecondBlockMainContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void addShopCat(Map<String, Object> map) {

    }

    @Override
    public void getShopCart() {

    }
    private List<AppIndexGoodsList> resolveAppIndexCustomData(String obj) {
        List<AppIndexGoodsList> result=null;
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
            Type type = new TypeToken<List<AppIndexGoodsList>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void getAPPIndexCustom(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "app_index_1002");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetAPPIndexCustom(resolveAppIndexCustomData(obj));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onSuccessGetAPPIndexCustom(null);
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.onSuccessGetAPPIndexCustom(null);
                    }
                });
    }
}
