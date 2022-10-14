package com.health.discount.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.discount.contract.LiveListTContract;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LiveListCopyPresenter implements LiveListTContract.Presenter {
    private Context mContext;
    private LiveListTContract.View mView;

    public LiveListCopyPresenter(Context context, LiveListTContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void subVideo(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "9001");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new StringObserver(mView, mContext, false,
//                        false, false, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//
//                        Toast.makeText(mContext, "预约成功！提前10分钟消息提醒", Toast.LENGTH_SHORT).show();
//                        mView.subVideoSucess();
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//                });
    }

    @Override
    public void getLiveLink(Map<String, Object> map) {
        map.put("function", "9136");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getLiveLinkSucess(resolveData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.getLiveLinkSucess(null);
                    }
                });
    }

    @Override
    public void getLiveList(Map<String, Object> map) {
        if(TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))){
            return;
        }
        map.put("function", "9133");
        map.put("isDelete", "0");
        map.put("statusList","1,2,4".split(","));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetLiveList(resolveTabListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetLiveList(null);
                    }
                });
    }

    private List<LiveVideoMain> resolveTabListData(String obj) {
        List<LiveVideoMain> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("list");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<LiveVideoMain>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private LiveVideoMain resolveData(String obj) {
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
