package com.healthy.library.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.contract.AppPinContract;
import com.healthy.library.model.AssemableTeam;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:32
 * @des
 */

public class AppPinPresenter implements AppPinContract.Presenter {

    private Context mContext;
    private AppPinContract.View mView;

    public AppPinPresenter(Context context, AppPinContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getAppPinList(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetAppCouponList(resolveTeamListData(obj));
                    }
                });
    }

    private List<AssemableTeam> resolveTeamListData(String obj) {
        List<AssemableTeam> result = new ArrayList<>();
        try {
            JSONObject data=new JSONObject(obj);
            String userShopInfoDTOS=data.getJSONObject("data").getJSONArray("items").toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<AssemableTeam>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}