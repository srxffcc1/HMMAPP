package com.health.discount.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.discount.activity.NewUserActGiftActivity;
import com.health.discount.activity.NewUserGiftActivity;
import com.health.discount.contract.GiftDialogContract;
import com.health.discount.contract.KickListContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.Coupon;
import com.healthy.library.model.KKGroupDetail;
import com.healthy.library.model.Kick;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import autodispose2.ObservableSubscribeProxy;

public class GiftDialogPresenter implements GiftDialogContract.Presenter{
    private Context mContext;
    private GiftDialogContract.View mView;

    public GiftDialogPresenter(Context context, GiftDialogContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getCouponList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "80019");
        ObservableSubscribeProxy<String> mallObservable = ObservableHelper.createObservable(mContext, map);
        if(mView instanceof NewUserGiftActivity || mView instanceof NewUserActGiftActivity){
            mallObservable.subscribe(new StringObserver(mView, mContext, false) {
                @Override
                protected void onGetResultSuccess(String obj) {
                    super.onGetResultSuccess(obj);
                    mView.onSuccessGetCouponList(resolveCardListData(obj));
                }

                @Override
                protected void onFinish() {
                    super.onFinish();
                    mView.onRequestFinish();
                }

                @Override
                protected void onFailure() {
                    super.onFailure();
                    mView.onSuccessGetCouponList(null);
                }
            });
        } else {
            mallObservable.subscribe(new NoStringObserver(mView, mContext, false) {
                @Override
                protected void onGetResultSuccess(String obj) {
                    super.onGetResultSuccess(obj);
                    mView.onSuccessGetCouponList(resolveCardListData(obj));
                }

                @Override
                protected void onFinish() {
                    super.onFinish();
                    mView.onRequestFinish();
                }

                @Override
                protected void onFailure() {
                    super.onFailure();
                    mView.onSuccessGetCouponList(null);
                }
            });
        }
    }

    @Override
    public void checkCouponGift(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "80021");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true,true,true,true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGiftCheck();
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

    private List<Coupon> resolveCardListData(String obj) {
        List<Coupon> result =new ArrayList<Coupon>();
        try {
            JSONArray data=new JSONObject(obj).getJSONArray("data");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<Coupon>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
