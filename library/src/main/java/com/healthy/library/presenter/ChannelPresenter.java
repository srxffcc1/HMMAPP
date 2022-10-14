package com.healthy.library.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.BuildConfig;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.ChannelContract;
import com.healthy.library.contract.GoodsVideoSContract;
import com.healthy.library.model.UpdateVersion;
import com.healthy.library.model.UserInfoMonModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class ChannelPresenter implements ChannelContract.Presenter{

    private Context mContext;
    private ChannelContract.View mView;

    public ChannelPresenter(Context context,ChannelContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getIsAuditing(Map<String, Object> map) {
        map.put("function", "6002");
        map.put("channel", ChannelUtil.getChannel(mContext));
        map.put("version", BuildConfig.VERSION_NAME);
        //System.out.println("获取审核信息");
        ObservableHelper.createObservableNoLife(mContext, map)
                .subscribe(new NoStringObserver(null, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        boolean result=false;
                        try {
                            result=(0==(new JSONObject(obj).optJSONObject("data").optInt("auditStatus")));// 0为审核中 1为通过
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SpUtils.store(mContext, SpKey.AUDITSTATUS,result); //true 为审核中 fasle为通过
                        try {
                            mView.getSucessIsAuditing();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        SpUtils.store(mContext, SpKey.AUDITSTATUS,true);
                        try {
                            mView.getSucessIsAuditing();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }
    private UpdateVersion resolveUpdate(String obj) {
        UpdateVersion result = null;
        try {
            JSONObject data=new JSONObject(obj).getJSONObject("data").getJSONObject("updateSetup");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<UpdateVersion>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void checkVersion(Map<String, Object> map) {
        map.put("architecture", BuildConfig.versionLib);
        ObservableHelper.createObservableNoLife(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onSucessCheckVersion(resolveUpdate(obj));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        try {
                            mView.onSucessCheckVersion(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
    private UserInfoMonModel resolveMineData(String obj) {
        UserInfoMonModel result = null;
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
            Type type = new TypeToken<UserInfoMonModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void getMine() {
        Map<String, Object> map = new HashMap<>(3);
        map.put(Functions.FUNCTION, "1011");
        ObservableHelper.createObservableNoLife(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.getMineSuccess(resolveMineData(obj));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        try {
                            mView.getMineSuccess(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}