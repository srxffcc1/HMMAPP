package com.healthy.library.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.constant.Functions;
import com.healthy.library.contract.GoodsVideoSContract;
import com.healthy.library.model.VideoResult;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 14:35
 * @des
 */

public class GoodsVideoSPresenter implements GoodsVideoSContract.Presenter {
    private Context mContext;
    private GoodsVideoSContract.View mView;
    public String goodsId;
    public VideoResult videoResult;

    public GoodsVideoSPresenter(Context context, GoodsVideoSContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getVideoS(final Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9106-1");
        goodsId=map.get("goodsId").toString();
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        List<VideoResult> videoResults=resolveData(obj);
                        if(videoResults.size()>0){
                            videoResult=videoResults.get(0);
                            videoResult.courseId=videoResult.id;
                            videoResult.courseFlag=videoResult.status;
                            mView.getSucessGetvideoS(videoResults.get(0));
                        }else {
                            mView.getSucessGetvideoS(null);
                        }
                    }
                });
    }

    @Override
    public void getVideoToken(Map<String, Object> maporg) {
        if(videoResult!=null){
            mView.getSucessGetvideoToken(videoResult.pullAddress);
        }else {
            mView.getSucessGetvideoToken(null);
        }
    }

    private List<VideoResult> resolveData(String obj) {
        List<VideoResult> result =new ArrayList<>();
        try {
            JSONArray data=new JSONObject(obj).getJSONArray("data");
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<VideoResult>>() {
            }.getType();
            result=gson.fromJson(data.toString(),type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
