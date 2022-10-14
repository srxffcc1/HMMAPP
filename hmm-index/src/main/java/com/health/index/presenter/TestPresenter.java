package com.health.index.presenter;//package com.health.index.presenter;

import android.content.Context;
import android.util.Log;

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
import com.health.index.contract.IndexMonContract;
import com.health.index.contract.TestContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.Coupon;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

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
 * @date 2019/04/28 15:16
 * @des
 */
public class TestPresenter implements TestContract.Presenter {
    private Context mContext;
    private TestContract.View mView;
    public TestPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void getTestData(Map<String, Object> map, final String tag) {
        map.put(Functions.FUNCTION, "80019");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        List<Coupon> couponList=resolveCardListData(obj);
                        if(couponList.size()>0){
                            Log.v("新客礼包有无",tag+":有");
                        }else {
                            Log.v("新客礼包有无",tag+":无");
                        }
                    }
                });
    }
    private List<Coupon> resolveCardListData(String obj) {
        List<Coupon> result =new ArrayList<Coupon>();
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
            Type type = new TypeToken<List<Coupon>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}