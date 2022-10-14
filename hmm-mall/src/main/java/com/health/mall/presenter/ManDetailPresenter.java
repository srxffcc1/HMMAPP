package com.health.mall.presenter;

import androidx.lifecycle.Lifecycle;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.mall.contract.ManDetailContract;
import com.healthy.library.model.TechnicianResult;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/29 14:35
 * @des
 */

public class ManDetailPresenter implements ManDetailContract.Presenter {
    private Context mContext;
    private ManDetailContract.View mView;

    public ManDetailPresenter(Context context, ManDetailContract.View view, Lifecycle lifecycle) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getManDetail(String shopId) {
        Map<String, Object> map = new HashMap<>(5);
        //map.put(Functions.FUNCTION, "5040");
        map.put(Functions.FUNCTION, "101003");
        map.put("userId", shopId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
//                        mCommonViewModel.getModel().setValue(obj);
//                        mCommonViewModel.getModel().observe(mActivity, mObserver);
                        mView.onGetFirstSuccess(resolveData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private TechnicianResult resolveData(String obj) {
        List<TechnicianResult> result = null;
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
            Type type = new TypeToken<List<TechnicianResult>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.get(0);
    }
}
