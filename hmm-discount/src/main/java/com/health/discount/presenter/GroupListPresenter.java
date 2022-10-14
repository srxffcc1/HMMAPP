package com.health.discount.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.discount.contract.GroupListContract;
import com.healthy.library.model.KKGroup;
import com.healthy.library.constant.Functions;
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

public class GroupListPresenter implements GroupListContract.Presenter{
    private Context mContext;
    private GroupListContract.View mView;

    public GroupListPresenter(Context context, GroupListContract.View view) {
        mContext = context;
        mView = view;
    }




    private List<KKGroup> resolveCouponListData(String obj) {
        List<KKGroup> result = new ArrayList<>();
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
            Type type = new TypeToken<List<KKGroup>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
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
    @Override
    public void getGroupList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9010");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetGroupList(resolveCouponListData(obj),resolveRefreshData(obj));
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
}
