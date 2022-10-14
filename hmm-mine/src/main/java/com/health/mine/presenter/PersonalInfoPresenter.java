package com.health.mine.presenter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.health.mine.contract.PersonalInfoContract;
import com.healthy.library.model.UserInfoModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.CommonViewModel;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/05/09 09:48
 * @des
 */
public class PersonalInfoPresenter implements PersonalInfoContract.Presenter {
    private AppCompatActivity mActivity;
    private PersonalInfoContract.View mView;
    private CommonViewModel mViewModel;
    private Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            resolveData(s);
        }
    };
    private boolean isSuccess;

    public boolean isSuccess() {
        return isSuccess;
    }

    public PersonalInfoPresenter(AppCompatActivity activity, PersonalInfoContract.View view) {
        mActivity = activity;
        mView = view;
        mViewModel = ViewModelProviders.of(activity).get(CommonViewModel.class);
    }

    @Override
    public void getUserInfo() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, Functions.FUNCTION_USER_INFO);
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mViewModel.getModel().setValue(obj);
                        mViewModel.getModel().observe(mActivity, mObserver);
                    }
                });
    }

    private void resolveData(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            UserInfoModel model = new UserInfoModel();
            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
            model.setFaceUrl(JsonUtils.getString(jsonObject, "faceUrl"));
            model.setStatusName(JsonUtils.getString(jsonObject, "statusName"));
            model.setStatus(JsonUtils.getInt(jsonObject, "status"));
            model.setNickname(JsonUtils.getString(jsonObject, "nickName"));
            model.setPhone(JsonUtils.getString(jsonObject, "phone"));
            model.setIsSetPayPassword(JsonUtils.getInt(jsonObject, "isSetPayPassword"));
            model.setBirthday(JsonUtils.getString(jsonObject, "birthday"));
            mView.onGetUserInfoSuccess(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadFile(List<String> base64List, final int type) {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, Functions.FUNCTION_UPLOAD_PIC);
        map.put("images", base64List);
        map.put("type", type + "");
        if (type == 1) {
            ObservableHelper.createUploadObservable(mActivity, map)
                    .subscribe(new NoInsertStringObserver(mView, mActivity, false, false) {
                        @Override
                        protected void onGetResultSuccess(String obj) {
                            super.onGetResultSuccess(obj);
                            try {
                                JSONObject jsonObject = new JSONObject(obj);
                                JSONArray array = JsonUtils.getJsonArray(jsonObject, "data");
                                List<String> urls = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    urls.add(array.getString(i));
                                }
                                mView.onUpLoadSuccess(urls, type);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ObservableHelper.createObservable(mActivity, map)
                    .subscribe(new StringObserver(mView, mActivity, false, false) {
                        @Override
                        protected void onGetResultSuccess(String obj) {
                            super.onGetResultSuccess(obj);
                            try {
                                JSONObject jsonObject = new JSONObject(obj);
                                JSONArray array = JsonUtils.getJsonArray(jsonObject, "data");
                                List<String> urls = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    urls.add(array.getString(i));
                                }
                                mView.onUpLoadSuccess(urls, type);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    @Override
    public void updateUserInfoPic(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "1027");
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        isSuccess = true;
                        mView.onupdateSucess();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        isSuccess = false;
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void updateUserInfoNick(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "1026");
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        isSuccess = true;
                        mView.onupdateSucess();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        isSuccess = false;
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void updateUserBirthday(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "birth_1004");
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        isSuccess = true;
                        mView.onUpdateBirthday(isSuccess);
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        isSuccess = false;
                        mView.onRequestFinish();
                    }
                });
    }
}
