package com.healthy.library.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.constant.Functions;
import com.healthy.library.contract.HanMomContract;
import com.healthy.library.model.HanMomInfoModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class HanMomPresenter implements HanMomContract.Presenter {

    private Context mContext;
    private HanMomContract.View mView;

    public HanMomPresenter(Context context, HanMomContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getInfo(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9700");
        if("0".equals(map.get("lat"))){
            return;
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetInfoSuccess(resolveMemberInfoData(obj));
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
    private HanMomInfoModel.MemberInfoModel resolveMemberInfoData(String obj) {
        HanMomInfoModel.MemberInfoModel result = new HanMomInfoModel.MemberInfoModel();
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data").getJSONObject("memberInfo");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<HanMomInfoModel.MemberInfoModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        return result;
    }
}