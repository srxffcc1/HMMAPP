package com.health.city.presenter;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.city.contract.PostSendContract;
import com.health.city.model.UserInfoCityModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostSendPresenter implements PostSendContract.Presenter{
    private Context mContext;
    private PostSendContract.View mView;

    public PostSendPresenter(Context context, PostSendContract.View view) {
        mContext = context;
        mView = view;
    }



    @Override
    public void sendPost(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7005");
        if(SpUtils.getValue(mContext, SpKey.HSOT_STATUS,false)){
            map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        }
        checkCanPost(map);
    }
    public void sendPostReal(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7005");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true, true,true,false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessSendPost();
                    }
                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onFailPost();
                    }
                });
    }
    public void checkCanPost(final Map<String, Object> map) {
        map.put(Functions.FUNCTION, "forbidden_1100");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, false,false,false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        boolean result= false;
                        try {
                            result = new JSONObject(obj).optBoolean("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mView.onFailSendPost();
                        }
                        if(result){//被禁言了
                            Toast.makeText(mContext,"你已经被管理员禁言，禁止发帖",Toast.LENGTH_SHORT).show();
                            mView.onFailSendPost();
                        }else {
                            sendPostReal(map);
                        }
                    }
                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        sendPostReal(map);
                    }
                });
    }

    @Override
    public void uploadFile(List<String> base64List, final int type) {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, Functions.FUNCTION_UPLOAD_PIC);
        map.put("images", base64List);
        map.put("type", type+"");
        if(type==1){
            ObservableHelper.createUploadObservable(mContext, map)
                    .subscribe(new NoInsertStringObserver(mView, mContext, false, true,true,false) {
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

                        @Override
                        protected void onFailure() {
                            super.onFailure();
                            mView.onFailPost();
                        }
                    });
        }else {
            ObservableHelper.createUploadObservable(mContext, map)
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
                        @Override
                        protected void onFailure() {
                            super.onFailure();
                            mView.onFailPost();
                        }
                    });
        }
    }

    @Override
    public void uploadFile2(List<String> base64List, final int type) {
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
                                mView.onUpLoadSuccess2(urls,type);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        protected void onFailure() {
                            super.onFailure();
                            mView.onFailPost();
                        }
                    });
        }else {
            ObservableHelper.createUploadObservable(mContext, map)
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
                                mView.onUpLoadSuccess2(urls,type);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        protected void onFailure() {
                            super.onFailure();
                            mView.onFailPost();
                        }
                    });
        }
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
    private UserInfoCityModel resolveMineData(String obj) {
        UserInfoCityModel result = null;
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
            Type type = new TypeToken<UserInfoCityModel>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
