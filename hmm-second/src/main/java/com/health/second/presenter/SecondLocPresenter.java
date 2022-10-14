package com.health.second.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.second.contract.SecondTypeContract;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.LocVipContract;
import com.healthy.library.message.UpdateUserLocationMsg;
import com.healthy.library.model.HttpTmpResult;
import com.healthy.library.model.LocVip;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class SecondLocPresenter implements LocVipContract.Presenter {

    private Context mContext;
    private LocVipContract.View mView;

    public SecondLocPresenter(Context context, LocVipContract.View view) {
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
        if(result!=null){
            if(result.getAllMerchantWithMe().size()>10){//那基本可以判定为运营模式了 大于10个商户 则开启搜索框
                SpUtils.store(mContext, SpKey.OPERATIONSTATUS, true);
                Log.i("SDT", "自动升级为运营模式");
            }
        }
        return result;
    }

    @Override
    public void getLocVip(Map<String, Object> map) {
        HttpTmpResult httpTmpResult= ObjUtil.getObj(SpUtils.getValue(mContext,"fun100001"),HttpTmpResult.class);
        /**
         *  优化老用户没有品牌名称问题
         */
        String mShopBrand = SpUtils.getValue(mContext, SpKey.SHOP_BRAND);
        if(!TextUtils.isEmpty(mShopBrand)  && httpTmpResult!=null){
            mView.onSucessGetLocVip(resolveLocVipData(httpTmpResult.result));
        }else {
            mView.onSucessGetLocVip(resolveLocVipData(httpTmpResult.result));
        }

    }
}
