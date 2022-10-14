package com.health.tencentlive.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.tencentlive.contract.LiveMainBodyDetailContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class LiveMainBodyDetailPresenter implements LiveMainBodyDetailContract.Presenter {

    private Context mContext;
    private LiveMainBodyDetailContract.View mView;

    public LiveMainBodyDetailPresenter(Context context, LiveMainBodyDetailContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getLive(Map<String, Object> map) {
        map.put("function", "9136");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetLive(resolveLiveData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetLive(null);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void startLive(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9134");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onStartLiveSuccess(new JSONObject(obj).getJSONObject("data").getString("pushAddress")
                                    , new JSONObject(obj).getJSONObject("data").getString("groupId"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.showToast(msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private LiveVideoMain resolveLiveData(String obj) {
        LiveVideoMain result = null;
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
            Type type = new TypeToken<LiveVideoMain>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}