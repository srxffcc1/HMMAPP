package com.health.city.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.city.contract.AdPostDetialContract;
import com.healthy.library.model.PostImgDetial;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.ObservableHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

public class AdPostDetialPresenter implements AdPostDetialContract.Presenter {
    private Context mContext;
    private AdPostDetialContract.View mView;

    public AdPostDetialPresenter(Context context, AdPostDetialContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getImgDetial(Map<String, Object> map,final PostImgDetial postImgDetial) {
        map.put(Functions.FUNCTION, "p_70021");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        PostImgDetial result=resolveData(obj);
                        postImgDetial.id=result.id;
                        postImgDetial.postingId=result.postingId;
                        postImgDetial.url=result.url;
                        postImgDetial.annexUrl=result.annexUrl;
                        postImgDetial.urlType=result.urlType;
                        postImgDetial.createTime=result.createTime;
                        postImgDetial.updateTime=result.updateTime;
                        postImgDetial.urlStatus=result.urlStatus;
                        postImgDetial.sortNum=result.sortNum;
                        postImgDetial.postingGoodsList=result.postingGoodsList;
                        postImgDetial.postingImgTagList=result.postingImgTagList;
                        mView.onSuccessGetDetial(resolveData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }


    private PostImgDetial resolveData(String obj) {
        PostImgDetial result = new PostImgDetial();
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
            Type type = new TypeToken<PostImgDetial>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
