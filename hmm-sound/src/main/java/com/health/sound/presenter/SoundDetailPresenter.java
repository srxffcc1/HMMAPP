package com.health.sound.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.sound.contract.SoundDetailContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.SoundAlbum;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author Li
 * @date 2019/03/29 14:35
 * @des
 */

public class SoundDetailPresenter implements SoundDetailContract.Presenter {
    private Context mContext;
    private SoundDetailContract.View mView;

    public SoundDetailPresenter(Context context, SoundDetailContract.View view) {
        mContext = context;
        mView = view;
    }


    private List<SoundAlbum> resolveIndexListData(String obj) {
        List<SoundAlbum> result = new ArrayList<>();
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
            Type type = new TypeToken<List<SoundAlbum>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public void subAlbums(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "8068");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject=new JSONObject(obj).getJSONObject("data");
                            mView.successSubAlbums(jsonObject.optString("status"));
                        } catch (Exception e) {
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
    public void getSoundAlbums(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "8073");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetSoundAlbums(resolveIndexListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getAlbumsCollectStatus(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "8070");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject=new JSONObject(obj).getJSONObject("data");
                            mView.successGetSoundAlbumsCollectStatus(jsonObject.optString("status"));
                        } catch (Exception e) {
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
}
