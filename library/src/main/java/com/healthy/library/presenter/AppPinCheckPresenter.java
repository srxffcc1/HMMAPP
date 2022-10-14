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
import com.healthy.library.contract.AppPinCheckContract;
import com.healthy.library.model.AppPinCheck;
import com.healthy.library.model.AssemableTeam;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 17:32
 * @des
 */

public class AppPinCheckPresenter implements AppPinCheckContract.Presenter {

    private Context mContext;
    private AppPinCheckContract.View mView;

    public AppPinCheckPresenter(Context context, AppPinCheckContract.View view) {
        mContext = context;
        mView = view;
    }

    private AppPinCheck resolvePinExtraData(String obj) {
        AppPinCheck result = null;
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

    @Override
    public void getPinExtra(Map<String, Object> map) {
        map.put(Functions.FUNCTION,"9016");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetPinExtra(new AppPinCheck("",false));
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        if(msg!=null&&msg.contains("服务器忙")){

                            mView.onSucessGetPinExtra(new AppPinCheck(msg,false));
                        }else {

                            mView.onSucessGetPinExtra(new AppPinCheck(msg,true));
                        }

                    }
                });
    }
}