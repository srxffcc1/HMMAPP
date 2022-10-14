package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.ServiceGoodsServiceContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.RecommendList;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.utils.SpUtils;

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
 * @date 2019/04/29 16:19
 * @des
 */

public class ServiceGoodsSerivicePresenter implements ServiceGoodsServiceContract.Presenter {

    private Context mContext;
    private ServiceGoodsServiceContract.View mView;

    public ServiceGoodsSerivicePresenter(Context context, ServiceGoodsServiceContract.View view) {
        mContext = context;
        mView = view;
    }



    private List<RecommendList> resolveRecommendListData(String obj) {
        List<RecommendList> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("goodsList");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<RecommendList>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i <result.size() ; i++) {
            result.get(i).setType(2);
        }
        return result;
    }

    @Override
    public void getGoodsRecommend(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9119");
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        if(map.get("shopId")==null){
            return;
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.successGetGoodsRecommend(resolveRecommendListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }
}
