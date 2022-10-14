package com.health.faq.presenter;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.faq.contract.MeetProfessorContract;
import com.healthy.library.model.Faq;
import com.healthy.library.model.FaqHotExpertFieldChose;
import com.healthy.library.model.FaqVideo;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

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
 * @date 2019/07/22 14:47
 * @des
 */

public class MeetProfessorPresenter implements MeetProfessorContract.Presenter {

    private Context mContext;
    private MeetProfessorContract.View mView;

    public MeetProfessorPresenter(Context context, MeetProfessorContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getHome() {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "5025");
        map.put("pageSize", "10");
        map.put("currentPage", 1+"");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
//                        mCommonViewModel.getModel().setValue(obj);
//                        mCommonViewModel.getModel().observe(mActivity, mObserver);
                        mView.onGetHomeSuccess(resolveData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getType() {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "5059");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
//                        mCommonViewModel.getModel().setValue(obj);
//                        mCommonViewModel.getModel().observe(mActivity, mObserver);
                        mView.onGetFieldList(resolveFieldData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getHotType() {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "5062");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
//                        mCommonViewModel.getModel().setValue(obj);
//                        mCommonViewModel.getModel().observe(mActivity, mObserver);
                        mView.onGetHotFieldList(resolveHotFieldData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }
    private List<FaqVideo> resolveVideoData(String obj) {
        List<FaqVideo> result = new ArrayList<>();
        try {
            JSONObject data=new JSONObject(obj);
            String userShopInfoDTOS=data.getJSONArray("data").toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<FaqVideo>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void getVideoOnline() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, "9000");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetVideoSuccess(resolveVideoData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void subVideo(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9001");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);

                        Toast.makeText(mContext,"预约成功！提前10分钟消息提醒",Toast.LENGTH_SHORT).show();
                        mView.subVideoSucess();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private List<FaqHotExpertFieldChose> resolveFieldData(String obj) {
        List<FaqHotExpertFieldChose> result = null;
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
            Type type = new TypeToken<List<FaqHotExpertFieldChose>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private List<FaqHotExpertFieldChose> resolveHotFieldData(String obj) {
        List<FaqHotExpertFieldChose> result = null;
        try {
            JSONArray data=new JSONObject(obj).getJSONObject("data").getJSONArray("eightExpertCategoryResult");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<FaqHotExpertFieldChose>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private Faq resolveData(String obj) {
        Faq result = null;
        try {
            JSONObject data=new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS=data.getJSONObject("rewardQuestionFirstPageDTO").toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<Faq>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
