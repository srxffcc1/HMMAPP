package com.health.discount.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.discount.contract.KickListContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.Kick;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.model.PageInfoEarly;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class KickListPresenter implements KickListContract.Presenter{
    private Context mContext;
    private KickListContract.View mView;

    public KickListPresenter(Context context, KickListContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getKickList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7050");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetKickList(resolveKickListData(obj),resolveRefreshData(obj));
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
        getKickTopList(map);
    }

    @Override
    public void getKickTopList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7030");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetTopKickList(resolveKickListDataNoPage(obj),resolveRefreshData(obj));
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

    private List<Kick> resolveKickListDataNoPage(String obj) {
        List<Kick> result = new ArrayList<>();
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
            Type type = new TypeToken<List<Kick>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            //System.out.println("错误:"+obj);
            e.printStackTrace();
        }
        return result;
    }

    private List<Kick> resolveKickListData(String obj) {
        List<Kick> result = new ArrayList<>();
        try {
            JSONArray data=new JSONObject(obj).getJSONObject("data").getJSONArray("items");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<Kick>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            //System.out.println("错误:"+obj);
            e.printStackTrace();
        }
        return result;
    }

    private PageInfoEarly resolveRefreshData(String obj) {
        PageInfoEarly result = new PageInfoEarly();
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
            Type type = new TypeToken<PageInfoEarly>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

}
