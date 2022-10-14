package com.health.servicecenter.presenter;

import android.content.Context;

import com.health.servicecenter.contract.SubmitBackContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Li
 * @date 2019/04/29 16:19
 * @des
 */

public class SubmitBackPresenter implements SubmitBackContract.Presenter {

    private Context mContext;
    private SubmitBackContract.View mView;

    public SubmitBackPresenter(Context context, SubmitBackContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void addBack(final Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25008");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject object = new JSONObject(obj);
                            if (object.getInt("code") == 0) {
                                mView.onAddBackSuccess(object.getString("msg"), object.getString("data"));
                            }
                        } catch (JSONException e) {
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
    public void uploadFile(List<String> base64List, final int type) {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, Functions.FUNCTION_UPLOAD_PIC);
        map.put("images", base64List);
        map.put("type", type + "");
        if (type == 1) {
            ObservableHelper.createUploadObservable(mContext, map)
                    .subscribe(new NoInsertStringObserver(mView, mContext, false, false) {
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

                        @Override
                        protected void onFailure() {
                            super.onFailure();
                            mView.onFailPost();
                        }
                    });
        } else {
            ObservableHelper.createUploadObservable(mContext, map)
                    .subscribe(new StringObserver(mView, mContext, false, false) {
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

                        @Override
                        protected void onFailure() {
                            super.onFailure();
                            mView.onFailPost();
                        }
                    });
        }
    }
}
