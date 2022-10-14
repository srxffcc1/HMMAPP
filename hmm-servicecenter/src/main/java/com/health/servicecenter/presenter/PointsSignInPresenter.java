package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.PointsSignInContract;
import com.health.servicecenter.model.PointsSignInModel;
import com.health.servicecenter.model.RecommendModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.RecommendList;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.disposables.Disposable;


public class PointsSignInPresenter implements PointsSignInContract.Presenter {
    private Context mContext;
    private PointsSignInContract.View mView;

    public PointsSignInPresenter(Context context, PointsSignInContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getSignInData(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "msr_1001");
        map.put("memberId", SpUtils.getValue(mContext, SpKey.USER_ID));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetSignInDataSuccess(resolveStoreSignIn(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void signIn(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "msr_1002");
        map.put("memberId", SpUtils.getValue(mContext, SpKey.USER_ID));
        map.put("phone", SpUtils.getValue(mContext, SpKey.PHONE));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, true, false, false, false, true) {

                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetSignInSuccess();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });

    }

    @Override
    public void getRecommend(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9117");
        map.put("pageNum", "1");
        map.put("pageSize", "3");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetRecommendListSuccess(resolveRecommendListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getGoodsList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9119");
        map.put("pageSize", "10");
        map.put("type", "6");
        if(map.get("shopId")==null){
            return;
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mView.onRequestStart(d);
                        mView.showLoading();
                    }

                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.showContent();
                        try {
                            mView.onGetGoodsListSuccess(resolveStoreData(obj), new JSONObject(obj).getJSONObject("data").getInt("firstPageSize"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onRequestFinish();
                        if (e instanceof ConnectException || e instanceof SocketTimeoutException) {
                            try {
                                mView.showNetErr();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            mView.showDataErr();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void addShopCat(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25013");
        map.put("goodsQuantity", "1");
        map.put("goodsSource", "1");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.addShopCatSuccess(new JSONObject(obj).getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private PointsSignInModel resolveStoreSignIn(String obj) {
        PointsSignInModel result = null;
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
            Type type = new TypeToken<PointsSignInModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<RecommendList> resolveStoreData(String obj) {
        List<RecommendList> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("goodsList");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<RecommendList>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<RecommendModel.RecommendGoods> resolveRecommendListData(String obj) {
        List<RecommendModel.RecommendGoods> result = new ArrayList<>();
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
            Type type = new TypeToken<List<RecommendModel.RecommendGoods>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

}
