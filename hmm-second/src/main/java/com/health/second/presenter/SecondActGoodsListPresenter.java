package com.health.second.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.second.contract.SecondActGoodsListContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.KKGroup;
import com.healthy.library.model.Kick;
import com.healthy.library.model.Kill;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class SecondActGoodsListPresenter implements SecondActGoodsListContract.Presenter {

    private Context mContext;
    private SecondActGoodsListContract.View mView;

    public SecondActGoodsListPresenter(Context context, SecondActGoodsListContract.View view) {
        mContext = context;
        mView = view;
    }

    private List<Kill> resolveKillGoodsData(String obj) {
        List<Kill> result = new ArrayList<>();
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
            Type type = new TypeToken<List<Kill>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private List<KKGroup> resolvePinGoodsData(String obj) {
        List<KKGroup> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("items");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<KKGroup>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private List<Kick> resolveKickGoodsData(String obj) {
        List<Kick> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("items");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<Kick>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private List<Kick> resolveAlradyKickGoodsData(String obj) {
        List<Kick> result = new ArrayList<>();
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
            Type type = new TypeToken<List<Kick>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void getKillGoodsList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9112");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetKillGoodsSucess(resolveKillGoodsData(obj),resolveRefreshData(obj));

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }
    private PageInfoEarly resolveRefreshData(String obj) {
        PageInfoEarly result = new PageInfoEarly();
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
            Type type = new TypeToken<PageInfoEarly>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        return result;
    }
    @Override
    public void getPinGoodsList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9010");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetPinGoodsSucess(resolvePinGoodsData(obj),resolveRefreshData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }

    @Override
    public void getKickGoodsList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7050");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetKickGoodsSucess(resolveKickGoodsData(obj),resolveRefreshData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }

    @Override
    public void getAlreadyKillGoodsList(Map<String, Object> map) {
        mView.onGetActsGoodsTitleSucess(null);
    }

    @Override
    public void getAlreadyPinGoodsList(Map<String, Object> map) {
        map.put("type", "1");
        map.put(Functions.FUNCTION, "9011");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        List<KKGroup> kickList=resolvePinGoodsData(obj);
                        String result=null;
                        if(kickList!=null&&kickList.size()>0){
                            result="您有正在进行的拼团";
                        }
                        mView.onGetActsGoodsTitleSucess(result);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });

    }

    @Override
    public void getAlreadyKickGoodsList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7030");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        List<Kick> kickList=resolveAlradyKickGoodsData(obj);
//                        kickList= ListUtil.where(kickList, new ListUtil.Where<Kick>() {
//                            @Override
//                            public boolean where(Kick obj) {
//                                return false;
//                            }
//                        });
                        String result=null;
                        if(kickList!=null&&kickList.size()>0){
                            result="您有正在进行的砍价";
                        }
                        mView.onGetActsGoodsTitleSucess(result);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
//        mView.onGetActsGoodsTitleSucess(null);

    }
}
