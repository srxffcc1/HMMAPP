package com.health.tencentlive.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.tencentlive.contract.CreateLiveContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class CreateLivePresenter implements CreateLiveContract.Presenter {

    private Context mContext;
    private CreateLiveContract.View mView;

    public CreateLivePresenter(Context context, CreateLiveContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getAnchormanInfo(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "9130");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getSuccessAnchormanInfo(resolveData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getLiveRoomConfig(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "9138");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getSuccessLiveRoomConfig(resolveLiveRoomData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void startPushLive(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "9146");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onStartPushLiveSuccess(new JSONObject(obj).getJSONObject("data").getString("pushAddress")
                                    , new JSONObject(obj).getJSONObject("data").getString("groupId"), new JSONObject(obj).getJSONObject("data").getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.showToast(msg);
                        mView.onStartPushLiveSuccess(null, null, null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onStartPushLiveSuccess(null, null, null);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private AnchormanInfo resolveData(String obj) {
        AnchormanInfo result = null;
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
            Type type = new TypeToken<AnchormanInfo>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private LiveVideoMain resolveLiveRoomData(String obj) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}