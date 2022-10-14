package com.health.tencentlive.presenter;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.tencentlive.contract.PushLiveContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;

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

public class PushLivePresenter implements PushLiveContract.Presenter {

    private Context mContext;
    private PushLiveContract.View mView;

    public PushLivePresenter(Context context, PushLiveContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void stopPushLive(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "9132");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onStopPushLiveSuccess(0);
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onStopPushLiveSuccess(-2);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onStopPushLiveSuccess(-2);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mView.onStopPushLiveSuccess(-2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },800);
    }

    @Override
    public void stopSpeed(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "31004");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onStopSpeedSuccess();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getLiveRoomInfo(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "9136");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getSuccessLiveRoomInfo(resolveLiveRoomData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void changeLiveStatus(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "9142");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onChangeLiveStatusSuccess();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void againLive(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "9145");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            if (new JSONObject(obj).getJSONObject("data") == null) {
                                mView.getSuccessAgainLive(null, null, null);
                            } else {
                                mView.getSuccessAgainLive(new JSONObject(obj).getJSONObject("data").getString("pushAddress")
                                        , new JSONObject(obj).getJSONObject("data").getString("groupId"), new JSONObject(obj).getJSONObject("data").getString("id"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.getSuccessAgainLive(null, null, null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.getSuccessAgainLive(null, null, null);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private LiveRoomInfo resolveLiveRoomData(String obj) {
        LiveRoomInfo result = null;
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
            Type type = new TypeToken<LiveRoomInfo>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}