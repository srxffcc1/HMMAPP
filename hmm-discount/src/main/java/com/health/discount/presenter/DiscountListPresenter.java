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
import com.health.discount.contract.DiscountListContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.GoodsBasketAll;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.PopDetailInfo;
import com.healthy.library.model.PopListInfo;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
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

public class DiscountListPresenter implements DiscountListContract.Presenter {
    private Context mContext;
    private DiscountListContract.View mView;

    public DiscountListPresenter(Context context, DiscountListContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getGoodsList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "ytb_9003");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetList(resolveTabListData(obj));
                        mView.onSucessGetTopTitle(resolveTopData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetList(null);
                        mView.onSucessGetTopTitle(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onSucessGetList(null);
                        mView.onSucessGetTopTitle(null);
                    }
                });
    }

    @Override
    public void getGoodsSpec(String goodsId) {
        Map<String, Object> map = new HashMap<>();
        map.put("mapMarketingGoodsId", goodsId);
        map.put(Functions.FUNCTION, "marketing_9302");
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        //mView.onSucessGetTopTitle(resolveTopData(obj));
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
    public void addShopCat(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25013");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.successAddShopCat(new JSONObject(obj).getString("msg"));
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

    @Override
    public void getBasketList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25015");
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetBasketList(resolveBasketData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private List<PopDetailInfo.GoodsDTOListBean> resolveTabListData(String obj) {
        List<PopDetailInfo.GoodsDTOListBean> result = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(obj);
            if (data.getJSONObject("data") == null) {
                return result;
            }
            if (data.getJSONObject("data").getJSONArray("PopListInfo") == null || data.getJSONObject("data").getJSONArray("PopListInfo").length() == 0) {
                return result;
            }
            JSONArray jsonObject = new JSONObject(obj)
                    .getJSONObject("data")
                    .getJSONArray("PopListInfo")
                    .optJSONObject(0)
                    .getJSONObject("PopDetailInfo")
                    .getJSONArray("goodsDTOList");
            String userShopInfoDTOS = jsonObject.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<PopDetailInfo.GoodsDTOListBean>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private PopListInfo resolveTopData(String obj) {
        PopListInfo result = null;
        if (obj != null && !TextUtils.isEmpty(obj)) {
            try {
                JSONObject data = new JSONObject(obj);
                if (data.getJSONObject("data") == null) {
                    return null;
                }
                if (data.getJSONObject("data").getJSONArray("PopListInfo") == null || data.getJSONObject("data").getJSONArray("PopListInfo").length() == 0) {
                    return null;
                }
                JSONObject jsonObject = data.getJSONObject("data").getJSONArray("PopListInfo").optJSONObject(0);
                String userShopInfoDTOS = jsonObject.toString();
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                });
                Gson gson = builder.create();
                Type type = new TypeToken<PopListInfo>() {
                }.getType();
                result = gson.fromJson(userShopInfoDTOS, type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
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
            e.printStackTrace();
        }

        return result;
    }

    private GoodsBasketAll resolveBasketData(String obj) {
        GoodsBasketAll result = null;
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
            Type type = new TypeToken<GoodsBasketAll>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
