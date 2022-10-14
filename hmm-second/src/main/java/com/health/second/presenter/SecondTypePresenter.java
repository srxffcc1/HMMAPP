package com.health.second.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.second.contract.SecondTypeContract;
import com.health.second.model.SecondType;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.ProvinceCityModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SecondTypePresenter implements SecondTypeContract.Presenter {

    private Context mContext;
    private SecondTypeContract.View mView;

    public SecondTypePresenter(Context context, SecondTypeContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getFirstTypeMenu(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "t_100200");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetFirstTypeMenuSucess(resolveType(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }

    private List<SecondType.SecondTypeMenu> resolveTypeMenus(String obj) {
        List<SecondType.SecondTypeMenu> result = new ArrayList<>();
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
            Type type = new TypeToken<List<SecondType.SecondTypeMenu>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<SecondType> resolveType(String obj) {
        List<SecondType> result = new ArrayList<>();
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
            Type type = new TypeToken<List<SecondType>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getRecommendTypeMenu(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "t_100201");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetRecommendTypeMenuSucess(resolveTypeMenus(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }

    @Override
    public void getTypeMenu(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "t_100202");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetTypeMenuSucess(resolveTypeMenus(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onGetTypeMenuSucess(null);
                    }
                });
    }

    @Override
    public void getTypeMenuBindType(Map<String, Object> map, final SecondType secondType) {
        map.put(Functions.FUNCTION, "t_100202");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetTypeMenuBindTypeSucess(resolveTypeMenus(obj),secondType);
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }

    @Override
    public void getLocationList(String fatherId) {
        Map<String, Object> map=new HashMap();
        map.put(Functions.FUNCTION, "5075");
        map.put("fatherId",fatherId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            List<ProvinceCityModel> list = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(obj);
                            JSONArray array = JsonUtils.getJsonArray(jsonObject, "data");
                            for (int i = 0; i < array.length(); i++) {
                                ProvinceCityModel model = new ProvinceCityModel();
                                JSONObject object = array.getJSONObject(i);
                                model.setName(JsonUtils.getString(object, "name"));
                                model.setId(JsonUtils.getString(object, "id"));
                                model.setAreaNo(JsonUtils.getString(object, "areaNo"));
                                list.add(model);
                            }
                            mView.onGetLocationListSuccess(list);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }
}
