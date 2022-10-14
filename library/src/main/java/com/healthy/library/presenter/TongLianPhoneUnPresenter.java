package com.healthy.library.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.LibApplication;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.TongLianPhoneContract;
import com.healthy.library.model.TongLianMemberData;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class TongLianPhoneUnPresenter implements TongLianPhoneContract.Presenter {

    private Context mContext;
    private TongLianPhoneContract.View mView;

    public TongLianPhoneUnPresenter(Context context, TongLianPhoneContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void sendCode(Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "allin_10002");
//        map.put("verificationCodeType","9");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessSendCode("获取短信验证码成功！");
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessSendCode(null);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }
    private TongLianMemberData resolveTongData(String obj) {
        TongLianMemberData result = null;
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
            Type type = new TypeToken<TongLianMemberData>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void getTongLianPhoneStatus(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "allin_10001");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        TongLianMemberData tongLianMemberData=resolveTongData(obj);
                        SpUtils.store(LibApplication.getAppContext(),SpKey.TONGLIANBIZUSER,new Gson().toJson(tongLianMemberData));
                        mView.onSuccessTongLianPhoneStatus(tongLianMemberData);
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
    public void bindPhone(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false,false,false,false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        TongLianMemberData tongLianMemberData=ObjUtil.getObj(SpUtils.getValue(LibApplication.getAppContext(),SpKey.TONGLIANBIZUSER),TongLianMemberData.class);
                        if(tongLianMemberData!=null){
                            tongLianMemberData.memberInfo.isPhoneChecked=true;
                        }
                        SpUtils.store(LibApplication.getAppContext(),SpKey.TONGLIANBIZUSER,new Gson().toJson(tongLianMemberData));
                        mView.onSucessBindPhone("成功");
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.onSucessBindPhone(msg);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }
}