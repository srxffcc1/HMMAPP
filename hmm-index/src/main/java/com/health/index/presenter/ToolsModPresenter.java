package com.health.index.presenter;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.contract.ToolsModContract;
import com.healthy.library.model.UserInfoMonModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.ObservableHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/04 10:41
 * @des
 */
public class ToolsModPresenter implements ToolsModContract.Presenter {

    private Activity mContext;
    private ToolsModContract.View mView;

    public ToolsModPresenter(Activity context, ToolsModContract.View view) {
        mContext = context;
        mView = view;
    }
    private UserInfoMonModel resolveMineData(String obj) {
        UserInfoMonModel result = null;
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
            Type type = new TypeToken<UserInfoMonModel>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void getNowStatus() {
        Map<String, Object> map = new HashMap<>(3);
        map.put(Functions.FUNCTION, "1011");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onsucessGetMine(resolveMineData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
//                        mView.onGetStoreDetailFail();
                    }
                });
    }
    @Override
    public void getType(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessgetType(obj);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getTop(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessgetTop(obj);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getMine(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessgetMine(obj);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getCenter(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessgetCenter(obj);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getBottom(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false,
                        true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessgetBottom(obj);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }
}