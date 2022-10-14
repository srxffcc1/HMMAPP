package com.health.tencentlive.presenter;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.tencentlive.contract.LiveMainBodyDetailSubContract;
import com.healthy.library.model.LiveVideoSub;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class LiveMainBodyDetailSubPresenter implements LiveMainBodyDetailSubContract.Presenter {

    private Context mContext;
    private LiveMainBodyDetailSubContract.View mView;

    public LiveMainBodyDetailSubPresenter(Context context, LiveMainBodyDetailSubContract.View view) {
        mContext = context;
        mView = view;
    }



    private LiveVideoSub resolveSubData(String obj) {
        LiveVideoSub result = null;
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
            Type type = new TypeToken<LiveVideoSub>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void addSub(Map<String, Object> map) {
        map.put("function", "9140");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        Toast.makeText(mContext,"预约成功",Toast.LENGTH_SHORT).show();
                        mView.onSucessSub();

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessSub();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void getSubDetail(Map<String, Object> map) {
        map.put("function", "9141");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetSub(resolveSubData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }
}