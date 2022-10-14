package com.health.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.mine.contract.InviteContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.InviteAct;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;



public class InvitePresenter implements InviteContract.Presenter {
    private Context mContext;
    private InviteContract.View mView;

    public InvitePresenter(Context context, InviteContract.View view) {
        mContext = context;
        mView = view;
    }

    private InviteAct resolveInviteAct(String obj) {
        InviteAct result = null;
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
            Type type = new TypeToken<InviteAct>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    @Override
    public void getMerchantInvite(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "invite_10002");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetMerchantInvite(resolveInviteAct(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSuccessGetMerchantInvite(null);
                    }
                });
    }

    @Override
    public void getMerchantInviteDetailJoinList(Map<String, Object> map) {

    }

    @Override
    public void subMerchantInvite(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,true,true,true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessSubMerchantInvite(obj);
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSuccessSubMerchantInvite( null);

                    }
                });
    }
}
