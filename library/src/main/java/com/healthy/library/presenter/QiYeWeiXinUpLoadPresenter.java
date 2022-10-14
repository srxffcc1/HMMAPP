package com.healthy.library.presenter;

import android.content.Context;

import com.healthy.library.constant.Functions;
import com.healthy.library.contract.QiYeWeiXinUpLoadContract;
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
 * @date 2019/03/27 11:37
 * @des
 */

public class QiYeWeiXinUpLoadPresenter implements QiYeWeiXinUpLoadContract.Presenter {

    private Context mContext;
    private QiYeWeiXinUpLoadContract.View mView;

    public QiYeWeiXinUpLoadPresenter(Context context, QiYeWeiXinUpLoadContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void uploadQiWeiXin(String imgUrl, String tokerWorkerId) {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "workWx_1000");
        map.put("imgUrl", imgUrl);
        map.put("tokerWorkerId", tokerWorkerId);
        ObservableHelper.createUploadObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true, true,true,false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessUpload();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
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