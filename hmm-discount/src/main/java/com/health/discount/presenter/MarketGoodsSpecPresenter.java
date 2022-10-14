package com.health.discount.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.discount.contract.MarketGoodsSpecContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.DisGoodsSpecCell;
import com.healthy.library.model.MarketingGoodsList;
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

public class MarketGoodsSpecPresenter implements MarketGoodsSpecContract.Presenter {
    private Context mContext;
    private MarketGoodsSpecContract.View mView;

    public MarketGoodsSpecPresenter(Context context, MarketGoodsSpecContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getGoodsSpec(String mapMarketingGoodsId, final MarketingGoodsList marketingGoodsListBean) {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "marketing_9302");
        map.put("mapMarketingGoodsId", mapMarketingGoodsId);
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        marketingGoodsListBean.goodsSpecCellList = resolveListData(obj);
                        mView.onSucessGetList();
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

    private List<DisGoodsSpecCell> resolveListData(String obj) {
        List<DisGoodsSpecCell> result = new ArrayList<>();
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
            Type type = new TypeToken<List<DisGoodsSpecCell>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
