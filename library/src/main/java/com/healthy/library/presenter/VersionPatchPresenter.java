package com.healthy.library.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.HanMomContract;
import com.healthy.library.contract.VersionPatchContract;
import com.healthy.library.model.HanMomInfoModel;
import com.healthy.library.model.UpdatePatchVersion;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.utils.SpUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class VersionPatchPresenter implements VersionPatchContract.Presenter {

    private Context mContext;
    private VersionPatchContract.View mView;

    public VersionPatchPresenter(Context context, VersionPatchContract.View view) {
        mContext = context;
        mView = view;
    }

    public void checkPatchVersion(Map<String, Object> map) {
        map.put("architecture", BuildConfig.versionLib);
        if(SpUtils.getValue(LibApplication.getAppContext(), SpKey.VERSION_CHECK_FLAG,false)
                &&SpUtils.getValue(mContext, SpKey.INITSTATUS, false)){
            ObservableHelper.createObservable(mContext, map)
                    .subscribe(new NoStringObserver(mView, mContext, false) {
                        @Override
                        protected void onGetResultSuccess(String obj) {
                            super.onGetResultSuccess(obj);
//                        mView.onSucessGetAppProgram(resolveShareData(obj));
                            mView.onSucessCheckPatchVersion(resolveUpdatePatch(obj));

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

    }
    private UpdatePatchVersion resolveUpdatePatch(String obj) {
        UpdatePatchVersion result = null;
        try {
            JSONObject data=new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<UpdatePatchVersion>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return result;
    }
}