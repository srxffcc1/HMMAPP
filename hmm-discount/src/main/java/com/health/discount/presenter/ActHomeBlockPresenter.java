package com.health.discount.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.discount.contract.ActHomeBlockContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.MainBlockDetailModel;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ActHomeBlockPresenter implements ActHomeBlockContract.Presenter{
    private Context mContext;
    private ActHomeBlockContract.View mView;

    public ActHomeBlockPresenter(Context context, ActHomeBlockContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getBlockDetail(final android.view.View view, final MainBlockModel itemOrg,final MainBlockModel itemDes, final int position, Map<String, Object> map) {
        map.put(Functions.FUNCTION, "101008");
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        itemDes.detailList=resolveBlockDetailData(obj);
                        mView.onSucessGetBlockDetailList(view,itemOrg,position);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        itemDes.detailList=new ArrayList<>();
                        mView.onSucessGetBlockDetailList(view,itemOrg,position);
                    }
                });
    }

    @Override
    public void getBlockDetailCity(final android.view.View view, final MainBlockModel itemOrg,final MainBlockModel.AllianceMerchant itemDes, final int position, Map<String, Object> map) {
        map.put(Functions.FUNCTION, "101008");
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        itemDes.detailList=resolveBlockDetailData(obj);
                        mView.onSucessGetBlockDetailList(view,itemOrg,position);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        itemDes.detailList=new ArrayList<>();
                        mView.onSucessGetBlockDetailList(view,itemOrg,position);
                    }
                });
    }

    private List<MainBlockDetailModel> resolveBlockDetailData(String obj) {
        List<MainBlockDetailModel> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("list");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<MainBlockDetailModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
