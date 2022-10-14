package com.health.mine.presenter;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.mine.contract.JobContract;
import com.health.mine.model.JobType;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class JobPresenter implements JobContract.Presenter {

    private Context mContext;
    private JobContract.View mView;

    public JobPresenter(Context context, JobContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void addJobDetail(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7043");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, true,true,false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        Toast.makeText(mContext,"提交成功",Toast.LENGTH_SHORT).show();
                        mView.onSuccessAdd();
                    }
                });
    }

    @Override
    public void sendJobCode(Map<String, Object> map) {
        map.put("function", "7044");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        true, true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessSendCode();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onGetCodeFail();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getJobType(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7045");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetJobType(resolveTypeData(obj));
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
    public void uploadCretFile(List<String> base64List, final int type) {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, Functions.FUNCTION_UPLOAD_PIC);
        map.put("images", base64List);
        map.put("type", type+"");
        if(type==1){
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
                                mView.onUpLoadCretSuccess(urls,type);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }else {
            ObservableHelper.createObservable(mContext, map)
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
                                mView.onUpLoadCretSuccess(urls,type);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    @Override
    public void uploadHealthFile(List<String> base64List, final int type) {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, Functions.FUNCTION_UPLOAD_PIC);
        map.put("images", base64List);
        map.put("type", type+"");
        if(type==1){
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
                                mView.onUpLoadHealthSuccess(urls,type);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }else {
            ObservableHelper.createObservable(mContext, map)
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
                                mView.onUpLoadHealthSuccess(urls,type);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    private List<JobType> resolveTypeData(String obj) {
        List<JobType> result = new ArrayList<>();
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
            Type type = new TypeToken<List<JobType>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}