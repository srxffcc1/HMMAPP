package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.KickDetailSContract;
import com.healthy.library.model.Goods2DetailKick;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.KickUser;
import com.healthy.library.model.Kick;
import com.healthy.library.model.KickResult;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.model.PageInfoEarly;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class KickDetailSPresenter implements KickDetailSContract.Presenter{
    private Context mContext;
    private KickDetailSContract.View mView;

    public KickDetailSPresenter(Context context, KickDetailSContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getKickManList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7034");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetKickManList(resolveKickManListData(obj),resolveRefreshData(obj));
                    }
                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();

                    }
                });
    }

    private KickResult resolveKickResultData(String obj) {
        KickResult result = new KickResult();
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
            Type type = new TypeToken<KickResult>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
    @Override
    public void kickHelp(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7032");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,false,false,false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);


                        mView.onSuccessKickHelp(resolveKickResultData(obj));

                    }
                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }

    @Override
    public void getGoodsDetail(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7051");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetGoodsDetail(resolveDetailData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.successGetGoodsDetail(null);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }
    private Goods2DetailKick resolveDetailData(String obj) {
        Goods2DetailKick result = null;
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
            Type type = new TypeToken<Goods2DetailKick>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<KickUser> resolveKickManListData(String obj) {
        List<KickUser> result = new ArrayList<>();
//        for (int i = 0; i < 15; i++) {
//            result.add(new KickUser());
//        }
        try {
            JSONArray data=new JSONObject(obj).getJSONObject("data").getJSONArray("items");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<KickUser>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            ////System.out.println("错误:"+obj);
            e.printStackTrace();
        }
        return result;
    }

    private PageInfoEarly resolveRefreshData(String obj) {
        PageInfoEarly result = new PageInfoEarly();
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
            Type type = new TypeToken<PageInfoEarly>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
    @Override
    public void getKickDetail(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7033");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetKickDetail(resolveKickData(obj));
                    }
                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();

                    }
                });
    }

    private Kick resolveKickData(String obj) {
        Kick result = new Kick();
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
            Type type = new TypeToken<Kick>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void kick(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7031");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,true,false,true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessKick(resolveKickResultData(obj));
                    }
                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onFailKick();
                    }
                });
    }
}
