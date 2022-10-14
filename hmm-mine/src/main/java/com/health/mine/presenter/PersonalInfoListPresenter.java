package com.health.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.mine.contract.PersonalInfoListContract;
import com.health.mine.model.UserInfoExModel;
import com.healthy.library.model.UserInfoModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/05/09 09:48
 * @des
 */
public class PersonalInfoListPresenter implements PersonalInfoListContract.Presenter {

    private Context mContext;
    private PersonalInfoListContract.View mView;

    public PersonalInfoListPresenter(Context activity, PersonalInfoListContract.View view) {
        mContext = activity;
        mView = view;
    }

    private void resolveData(String s) {

    }

    @Override
    public void getUserInfoList() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, "1013");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetUserInfoListSuccess(resolveListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private List<UserInfoExModel> resolveListData(String obj) {
        List<UserInfoExModel> result = new ArrayList<>();
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
            Type type = new TypeToken<List<UserInfoExModel>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void getUserInfo() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, Functions.FUNCTION_USER_INFO);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            UserInfoModel model = new UserInfoModel();
                            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
                            model.setMemberSex(JsonUtils.getInt(jsonObject, "memberSex",2));
                            model.setFaceUrl(JsonUtils.getString(jsonObject, "faceUrl"));
                            model.setDateContent(JsonUtils.getString(jsonObject, "dateContent"));
                            model.setNickname(JsonUtils.getString(jsonObject, "nickName"));
                            model.setIncome(FormatUtils.formatRewardMoney(
                                    JsonUtils.getString(jsonObject, "incomeBalance", "0")));
                            model.setBalance(FormatUtils.formatRewardMoney(
                                    JsonUtils.getString(jsonObject, "virtualBalance", "0")));
                            mView.onGetUserInfoSuccess(model);
                        } catch (Exception e) {
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
    public void delete(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "1017");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onDeleteUserInfoSuccess();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }
}
