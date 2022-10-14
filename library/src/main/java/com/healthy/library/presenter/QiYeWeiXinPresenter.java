package com.healthy.library.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.QiYeWeiXinContract;
import com.healthy.library.model.QiYeWeXin;
import com.healthy.library.model.QiYeWeXinKey;
import com.healthy.library.model.QiYeWeXinWorkShop;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 私域流量
 */

public class QiYeWeiXinPresenter implements QiYeWeiXinContract.Presenter {

    private Context mContext;
    private QiYeWeiXinContract.View mView;
    QiYeWeXinKey qiYeWeXinKey;
    public QiYeWeiXinPresenter(Context context, QiYeWeiXinContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getWeiXinKey(Map<String, Object> map,String destination) {
        qiYeWeXinKey=null;
        map.put("function", "wx_Setting_1003");
        map.put("merchantId", SpUtils.getValue(mContext,SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        qiYeWeXinKey=resolveWeiXinKey(obj);
                        if(qiYeWeXinKey!=null&&destination!=null){
                            if(destination.contains("orderOnOff")){
                                if("1".equals(qiYeWeXinKey.orderOnOff)){
                                    getMineWorker(new SimpleHashMapBuilder<String, Object>());
                                    return;
                                }
                            }
                            if(destination.contains("workerOnOff")){
                                if("1".equals(qiYeWeXinKey.workerOnOff)){

                                }
                            }
                            if(destination.contains("topicOnOff")){
                                if("1".equals(qiYeWeXinKey.topicOnOff)){
                                    getRecommandWeiXinGroup(new SimpleHashMapBuilder<String, Object>());
                                    return;
                                }
                            }
                        }
                        mView.onSucessGetWeiXinKey(qiYeWeXinKey);
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetWeiXinKey(null);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private QiYeWeXinKey resolveWeiXinKey(String obj) {
        QiYeWeXinKey result = null;
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
            Type type = new TypeToken<QiYeWeXinKey>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<QiYeWeXin> resolveWeiXinGroup(String obj) {
        List<QiYeWeXin> result = null;
        try {
            JSONArray data = new JSONObject(obj).getJSONArray("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<QiYeWeXin>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<QiYeWeXinWorkShop> resolveWorkerGroup(String obj) {
        List<QiYeWeXinWorkShop> result = null;
        try {
            JSONArray data = new JSONObject(obj).getJSONArray("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<QiYeWeXinWorkShop>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private TokerWorkerInfoModel resolveTokerWorker(String obj) {
        TokerWorkerInfoModel result = null;
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
            Type type = new TypeToken<TokerWorkerInfoModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    private TokerWorkerInfoModel.BindingListBean.BindingTokerWorkerBean resolveTokerWorkerZ(String obj) {
        TokerWorkerInfoModel.BindingListBean.BindingTokerWorkerBean result = null;
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
            Type type = new TypeToken<TokerWorkerInfoModel.BindingListBean.BindingTokerWorkerBean>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getRecommandWeiXinGroup(Map<String, Object> map) {
        if(qiYeWeXinKey==null){
            getWeiXinKey(new SimpleHashMapBuilder<>(),"topicOnOff");
            return;
        }
        map.put(Functions.FUNCTION, "wx_group_1001");
        map.put("merchantId", SpUtils.getValue(mContext,SpKey.CHOSE_MC));
        map.put("sortType", "asc");
        map.put("limitNum", "4");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetRecommandWeiXinGroup(resolveWeiXinGroup(obj));
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

    @Override
    public void getRecommandWorkerList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "workWx_1001");
        map.put("merchantId", SpUtils.getValue(mContext,SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetRecommandWorkerGroup(resolveWorkerGroup(obj));
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

    @Override
    public void getMineWorker(Map<String, Object> map) {
        if(qiYeWeXinKey==null){//开关参数没有的话 先走开关接口
            getWeiXinKey(new SimpleHashMapBuilder<String, Object>(),"orderOnOff");
            return;
        }
        map.put(Functions.FUNCTION, "20016");
        map.put("merchantId", SpUtils.getValue(mContext,SpKey.CHOSE_MC));
        map.put("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
        map.put("city", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
        map.put("district", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        TokerWorkerInfoModel tokerWorkerInfoModel=resolveTokerWorker(obj);
                        if(tokerWorkerInfoModel!=null&&!ListUtil.isEmpty(tokerWorkerInfoModel.bindingList)){
                            mView.onSucessGetMineWorker(resolveTokerWorker(obj));
                        }else {
                            getPublicWorker(new SimpleHashMapBuilder<String, Object>());
                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        getPublicWorker(new SimpleHashMapBuilder<String, Object>());
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void getPublicWorker(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "toker_10001");
        map.put("merchantId", SpUtils.getValue(mContext,SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        TokerWorkerInfoModel.BindingListBean.BindingTokerWorkerBean bindingTokerWorkerBean=resolveTokerWorkerZ(obj);
                        if(bindingTokerWorkerBean!=null){
                            TokerWorkerInfoModel tokerWorkerInfoModel=new TokerWorkerInfoModel();
                            tokerWorkerInfoModel.bindingList.add(new TokerWorkerInfoModel.BindingListBean(bindingTokerWorkerBean));
                            mView.onSucessGetPublicWorker(tokerWorkerInfoModel);
                        }

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