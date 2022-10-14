package com.health.sound.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.sound.contract.SoundTypeContract;
import com.healthy.library.model.SoundTypeSplit;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;


/**
 * @author Li
 * @date 2019/03/29 14:35
 * @des
 */

public class SoundTypePresenter implements SoundTypeContract.Presenter {
    private Context mContext;
    private SoundTypeContract.View mView;

    public SoundTypePresenter(Context context, SoundTypeContract.View view) {
        mContext = context;
        mView = view;
    }



    private SoundTypeSplit resolveIndexListData(String obj) {
        SoundTypeSplit result = null;
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
            Type type = new TypeToken<SoundTypeSplit>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public void getSoundAlbums(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "8065");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetSoundAlbum(resolveIndexListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }
}
