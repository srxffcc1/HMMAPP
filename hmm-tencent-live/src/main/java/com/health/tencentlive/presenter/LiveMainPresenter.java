package com.health.tencentlive.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.tencentlive.contract.LiveMainContract;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.model.AnchormanInfo;
import com.healthy.library.model.HttpTmpResult;
import com.healthy.library.model.LocVip;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class LiveMainPresenter implements LiveMainContract.Presenter {

    private Context mContext;
    private LiveMainContract.View mView;

    public LiveMainPresenter(Context context, LiveMainContract.View view) {
        mContext = context;
        mView = view;
    }
    private LocVip resolveLocVipData(String obj) {
        LocVip result = new LocVip();
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
            Type type = new TypeToken<LocVip>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    boolean hasInit = false;
    @Override
    public void getActLocVip(final Map<String, Object> map) {
        if("0".equals(LocUtil.getLongitude(mContext, SpKey.LOC_ORG))){
            return;
        }
        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP)) && !hasInit) {
            checkHost(new SimpleHashMapBuilder<String, Object>().puts("merchantId",SpUtils.getValue(mContext,SpKey.CHOSE_MC)));
            hasInit = true;
            return;
        }
        HttpTmpResult httpTmpResult= ObjUtil.getObj(SpUtils.getValue(mContext,"fun100001"),HttpTmpResult.class);
        if( httpTmpResult!=null){//门店列表处于有效期
            LocVip locVip = resolveLocVipData(httpTmpResult.result);
            LocUtil.getRealShop(locVip);
            checkHost(new SimpleHashMapBuilder<String, Object>().puts("merchantId",SpUtils.getValue(mContext,SpKey.CHOSE_MC)));
            return;
        }
        map.put("function", "100001");
        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        map.put("province", LocUtil.getProvinceNo(mContext, SpKey.LOC_ORG));
        map.put("city", LocUtil.getCityNo(mContext, SpKey.LOC_ORG));
        map.put("district", LocUtil.getAreaNo(mContext, SpKey.LOC_ORG));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        LocVip locVip = resolveLocVipData(obj);
                        LocUtil.getRealShop(locVip);
                        checkHost(new SimpleHashMapBuilder<String, Object>().puts("merchantId",SpUtils.getValue(mContext,SpKey.CHOSE_MC)));

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
    private AnchormanInfo resolveAnchormanData(String obj) {
        AnchormanInfo result = null;
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
            Type type = new TypeToken<AnchormanInfo>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void checkHost(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9135");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetHost(resolveAnchormanData(obj));

                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGetHost(null);
                    }
                });
    }
}