package com.health.index.presenter;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.contract.AnalyzeContract;
import com.health.index.contract.IndexToolsMainContract;
import com.health.index.model.AnalyzeModel;
import com.health.index.model.IndexVideo;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.AppIndexTopMarketing;
import com.healthy.library.model.CommonViewModel;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

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
 * @date 2019/04/25 15:09
 * @des B超
 */

public class IndexToolsMainPresenter implements IndexToolsMainContract.Presenter {
    private Context mContext;
    private IndexToolsMainContract.View mView;


    public IndexToolsMainPresenter(Context context, IndexToolsMainContract.View view) {
        mContext = context;
        mView = view;
    }

    private List<AppIndexTopMarketing> resolveListData(String obj) {
        List<AppIndexTopMarketing> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONArray("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<AppIndexTopMarketing>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void getTools(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "home_page_setting_1001");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,false,false,false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetToolsSuccess(resolveListData(obj));
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.onGetToolsSuccess(null);
                    }
                });
    }
}
