package com.health.mine.presenter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.mine.contract.PersonalDetailContract;
import com.health.mine.model.UserInfoExModel;
import com.healthy.library.model.UserInfoModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.CommonViewModel;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/05/09 11:25
 * @des
 */

public class PersonalDetailPresenter implements PersonalDetailContract.Presenter {
    private AppCompatActivity mActivity;
    private PersonalDetailContract.View mView;
    private CommonViewModel mViewModel;
    private Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            resolveData(s);
        }
    };


    @Override
    public void delete(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "1017");
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false,
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



    public PersonalDetailPresenter(AppCompatActivity activity, PersonalDetailContract.View view) {
        mActivity = activity;
        mView = view;
        mViewModel = ViewModelProviders.of(activity).get(CommonViewModel.class);
    }

//    @Override
//    public void getUserInfo() {
////        Map<String, Object> map = new HashMap<>(1);
////        map.put(Functions.FUNCTION, Functions.FUNCTION_USER_INFO);
////        ObservableHelper.createObservable(mActivity, map)
////                .subscribe(new StringObserver(mView, mActivity, false) {
////                    @Override
////                    protected void onGetResultSuccess(String obj) {
////                        super.onGetResultSuccess(obj);
////                        mViewModel.getModel().setValue(obj);
////                        mViewModel.getModel().observe(mActivity, mObserver);
////                    }
////                });
//    }

    @Override
    public void getUserInfo(String id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, "1016");
        map.put("id", id);
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetUserInfoExSuccess(resolveInfoData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });


    }

    private UserInfoExModel resolveInfoData(String obj) {
        UserInfoExModel result = null;
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
            Type type = new TypeToken<UserInfoExModel>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void updateUserInfo(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "1006");
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onUpdateUserInfoSuccess();
                    }
                });
    }

    @Override
    public void updateUserInfoEx(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "1014");
        //System.out.println("开船修改");
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onUpdateUserInfoSuccessEx();
                    }
                });
    }

    @Override
    public void updateUserInfoWithF(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "1006");
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onUpdateUserInfoSuccessF();
                    }
                });
    }

    @Override
    public void updateUserInfoExWithF(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "1014");
        //System.out.println("开船修改");
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onUpdateUserInfoSuccessExF();
                    }
                });
    }

    private void resolveData(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            UserInfoModel model = new UserInfoModel();
            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
            model.setStatus(JsonUtils.getInt(jsonObject, "status"));
            model.setStatusName(JsonUtils.getString(jsonObject, "statusName"));
            model.setBabyName(JsonUtils.getString(jsonObject, "babyName"));
            model.setMenLatelyDate(JsonUtils.getString(jsonObject, "latelyDate"));
            model.setMenCycleDurationDes(JsonUtils.getString(jsonObject, "cycleAndDays"));

            model.setBirthExpectedDateDes(JsonUtils.getString(jsonObject, "childbirth"));

            model.setBabySex(JsonUtils.getInt(jsonObject, "babySex"));
            model.setBirthType(JsonUtils.getInt(jsonObject, "deliveryMode"));
            model.setBabyAgeDes(JsonUtils.getString(jsonObject, "babyAge"));
            mView.onGetUserInfoSuccess(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
