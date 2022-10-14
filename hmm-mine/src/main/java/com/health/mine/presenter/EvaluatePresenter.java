package com.health.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.mine.contract.EvaluateContract;
import com.health.mine.model.UserInfoMineModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/13 15:49
 * @des
 */

public class EvaluatePresenter implements EvaluateContract.Presenter {

    private Context mContext;
    private EvaluateContract.View mView;

    public EvaluatePresenter(Context context, EvaluateContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void uploadFile(List<String> base64List, final int type) {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, Functions.FUNCTION_UPLOAD_PIC);
        map.put("images", base64List);
        map.put("type", type+"");
        if(type==1){
            ObservableHelper.createUploadObservable(mContext, map)
                    .subscribe(new NoInsertStringObserver(mView, mContext, false, false) {
                        @Override
                        protected void onGetResultSuccess(String obj) {
                            super.onGetResultSuccess(obj);
                            try {
                                JSONObject jsonObject = new JSONObject(obj);
                                JSONArray array = JsonUtils.getJsonArray(jsonObject, "data");
                                List<String> urls = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    urls.add(array.getString(i));
                                }
                                mView.onUpLoadSuccess(urls,type);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }else {
            ObservableHelper.createObservable(mContext, map)
                    .subscribe(new StringObserver(mView, mContext, false, false) {
                        @Override
                        protected void onGetResultSuccess(String obj) {
                            super.onGetResultSuccess(obj);
                            try {
                                JSONObject jsonObject = new JSONObject(obj);
                                JSONArray array = JsonUtils.getJsonArray(jsonObject, "data");
                                List<String> urls = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    urls.add(array.getString(i));
                                }
                                mView.onUpLoadSuccess(urls,type);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

    }

    @Override
    public void evaluate(Map<String, Object> map) {
        map.put(Functions.FUNCTION, Functions.FUNCTION_ADD_COMMENT);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onEvaluateSuccess();
                    }
                });
    }

    @Override
    public void getMine() {
        Map<String, Object> map = new HashMap<>(3);
        map.put(Functions.FUNCTION, "1011");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onMineSuccess(resolveMineData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
//                        mView.onGetStoreDetailFail();
                    }
                });
    }
    private UserInfoMineModel resolveMineData(String obj) {
        UserInfoMineModel result = null;
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
            Type type = new TypeToken<UserInfoMineModel>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
