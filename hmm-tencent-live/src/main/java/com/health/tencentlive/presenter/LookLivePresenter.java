package com.health.tencentlive.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.tencentlive.contract.LookLiveContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.LiveRoomInfo;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.utils.LogUtils;

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

public class LookLivePresenter implements LookLiveContract.Presenter {

    private Context mContext;
    private LookLiveContract.View mView;

    public LookLivePresenter(Context context, LookLiveContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void addLookLiveNum(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "9137");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onAddLookLiveNumSuccess();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void clickLive(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "9401");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onClickLiveSuccess();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getFollowInfo(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "9406");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onGetFollowInfoSuccess(new JSONObject(obj).getBoolean("data"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onGetFollowInfoSuccess(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onGetFollowInfoSuccess(false);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void clickFollow(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "9404");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onClickFollowSuccess(new JSONObject(obj).getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.getSuccessLiveRoomInfo(null);
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.getSuccessLiveRoomInfo(null);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getClickNum(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "9400");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.getSuccessClickNum(new JSONObject(obj).getJSONObject("data").optInt("count"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getNoSpeakInfo(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "31005");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            if (new JSONObject(obj).optJSONObject("data") == null ) {
                                mView.getSuccessNoSpeakInfo(null, null);
                            } else {
                                mView.getSuccessNoSpeakInfo(new JSONObject(obj).optJSONObject("data").optString("Member_Account"), new JSONObject(obj).optJSONObject("data").optString("ShuttedUntil"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getAnchormanLiveing(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "9144");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getSuccessAgain(resolveLiveRoomData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.getSuccessAgain(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.getSuccessAgain(null);
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