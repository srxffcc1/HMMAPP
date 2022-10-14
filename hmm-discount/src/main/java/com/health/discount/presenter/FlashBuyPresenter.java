package com.health.discount.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.discount.contract.FlashBuyContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.FlashBuyTab;
import com.healthy.library.model.PopDetailInfo;
import com.healthy.library.model.PopListInfo;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.ObjUtil;
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

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class FlashBuyPresenter implements FlashBuyContract.Presenter {
    private Context mContext;
    private FlashBuyContract.View mView;

    public FlashBuyPresenter(Context context, FlashBuyContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getTabList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "ytb_9001");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        if(mView == null){
                            return;
                        }
                        mView.onSuccessGetTabList(resolveTabListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        if(mView == null){
                            return;
                        }
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }

    @Override
    public void getActList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "ytb_9002");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        if(mView == null){
                            return;
                        }
                        mView.onSuccessGetActList(resolveListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        if(mView == null){
                            return;
                        }
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
                            if(mView == null){
                                return;
                            }
                            mView.successAddShopCat(new JSONObject(obj).getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        if(mView == null){
                            return;
                        }
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getYTBShopDetail(Map<String, Object> map) {
        ActVip.VipShop vipShop = ObjUtil.getObj(SpUtils.getValue(mContext, SpKey.VIPSHOP), ActVip.VipShop.class);
        if (vipShop != null && vipShop.checkAllIsRight()) {
            mView.onSuccessGetYTBShopDetail(vipShop);
            return;
        }
        map.put(Functions.FUNCTION, "YT_100003");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        if(mView == null){
                            return;
                        }
                        ActVip.VipShop vipShop = resolveShopData(obj);
                        SpUtils.store(mContext, SpKey.VIPSHOP, new Gson().toJson(vipShop));
                        mView.onSuccessGetYTBShopDetail(resolveShopData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        if(mView == null){
                            return;
                        }
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getGoodsList(Map<String, Object> map, final PopListInfo popListInfo, final List<PopListInfo> result) {
        map.put(Functions.FUNCTION, "ytb_9003");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        if (popListInfo.popDetail == null) {
                            popListInfo.popDetail = resolveGoodsListData(obj);
                            mView.onSuccessGetGoodsList(popListInfo, result);
                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        if(mView == null){
                            return;
                        }
                        mView.onSuccessGetGoodsList(popListInfo, result);

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        if(mView == null){
                            return;
                        }
                        mView.onRequestFinish();
                    }
                });
    }

    private List<FlashBuyTab> resolveTabListData(String obj) {
        List<FlashBuyTab> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("PopLabelInfo");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<FlashBuyTab>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private PopDetailInfo resolveGoodsListData(String obj) {
        PopDetailInfo result = null;
        if (obj != null && !TextUtils.isEmpty(obj)) {
            try {
                JSONObject data = new JSONObject(obj);
                if (data.getJSONObject("data") == null) {
                    return null;
                }
                if (data.getJSONObject("data").getJSONArray("PopListInfo") == null || data.getJSONObject("data").getJSONArray("PopListInfo").length() == 0) {
                    return null;
                }
                JSONObject jsonObject = data.getJSONObject("data").getJSONArray("PopListInfo").optJSONObject(0).getJSONObject("PopDetailInfo");
                String userShopInfoDTOS = jsonObject.toString();
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        return new Date(json.getAsJsonPrimitive().getAsLong());
                    }
                });
                Gson gson = builder.create();
                Type type = new TypeToken<PopDetailInfo>() {
                }.getType();
                result = gson.fromJson(userShopInfoDTOS, type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (result.goodsDTOList == null) {
            result.goodsDTOList = new ArrayList<>();
        }
        return result;
    }

    private List<PopListInfo> resolveListData(String obj) {
        List<PopListInfo> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("PopListInfo");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<PopListInfo>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private ActVip.VipShop resolveShopData(String obj) {
        ActVip.VipShop result = null;
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
            Type type = new TypeToken<ActVip.VipShop>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void detach() {
        mView = null;
    }
}
