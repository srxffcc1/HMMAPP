package com.healthy.library.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.BasketCountContract;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class BasketCountPresenter implements BasketCountContract.Presenter {

    private Context mContext;
    private BasketCountContract.View mView;

    public BasketCountPresenter(Context context, BasketCountContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getShopCart() {
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        map.put(Functions.FUNCTION, "25012");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        if(mView == null){
                            return;
                        }
                        mView.onGetShopCartSuccess(resolveShopCartlData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        if(mView == null){
                            return;
                        }
                        mView.onRequestFinish();
                    }
                });
    }
    private ShopCartModel resolveShopCartlData(String obj) {
        ShopCartModel result = null;
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
            Type type = new TypeToken<ShopCartModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void detach(){
        mView = null;
    }
}