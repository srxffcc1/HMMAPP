package com.health.index.presenter;

import android.Manifest;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.contract.IndexMainContract;
import com.health.index.model.IndexBean;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.LibApplication;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.ActGoodsItem;
import com.healthy.library.model.AppIndexCustom;
import com.healthy.library.model.AppIndexCustomNews;
import com.healthy.library.model.AppIndexCustomOther;
import com.healthy.library.model.FaqExportQuestion;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.model.TongLianMemberData;
import com.healthy.library.model.UserInfoMonModel;
import com.healthy.library.model.VideoCategory;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.SpUtils;

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
 * @date 2019/04/04 10:41
 * @des
 */
public class IndexMainPresenter implements IndexMainContract.Presenter {

    private Context mContext;
    private IndexMainContract.View mView;
    public int ptype = 0;

    public IndexMainPresenter(Context context, IndexMainContract.View view) {
        mContext = context;
        mView = view;
    }

    private IndexBean resolveIndexData(String obj) {
        IndexBean result = null;
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
            Type type = new TypeToken<IndexBean>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String[] mPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    public void getIndexMain(Map<String, Object> map) {
        map.put("function", "4002");
        if (!PermissionUtils.hasPermissions(mContext, mPermissions)) {
            map.put("latitude", "0");
            map.put("longitude", "0");
            map.put("areaNo", "0");
            map.put("cityNo", "0");
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getIndexSuccess(resolveIndexData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.getIndexSuccess(null);
                    }
                });
    }


    @Override
    public void getVideoOnline(Map<String, Object> map) {
        if (TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))) {
            return;
        }
        map.put("function", "9133");
        map.put("isDelete", "0");
        map.put("statusList", "1,2,4".split(","));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetVideoOnlineList(resolveLiveListData(obj), resolveRefreshData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public void getGoodsHot(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9119");
        if (map.get("shopId") == null) {
            return;
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onSuccessGetGoodsHotList(resolveGoodsListData(obj), new JSONObject(obj).getJSONObject("data").getInt("firstPageSize"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSuccessGetGoodsHotList(null, 0);
                    }
                });
    }

    @Override
    public void changeStatus(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "1015");
        map.put("id", id);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.changeStatusSuccess();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getAllStatus() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, "1013");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getAllStatusSuccess(resolveInfoListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getMine() {
        Map<String, Object> map = new HashMap<>(3);
        map.put(Functions.FUNCTION, "1011");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getMineSuccess(resolveMineData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.getMineSuccess(null);
                    }
                });
    }

    @Override
    public void getQuestionList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "5071");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetQuestionList(resolveQuestionListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getTongLianPhoneStatus(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "allin_10001");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        TongLianMemberData tongLianMemberData=resolveTongData(obj);
                        SpUtils.store(LibApplication.getAppContext(),SpKey.TONGLIANBIZUSER,new Gson().toJson(tongLianMemberData));
                        mView.onSuccessTongLianPhoneStatus(tongLianMemberData);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSuccessTongLianPhoneStatus(new TongLianMemberData());

                    }
                });
    }

    @Override
    public void getVideoTypeTabList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "8095");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetTabListSuccess(resolveData(obj));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onGetTabListSuccess(null);
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.onGetTabListSuccess(null);
                    }
                });
    }

    @Override
    public void getAPPIndexCustom(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "app_index_1000");
        if(!TextUtils.isEmpty(SpUtils.getValue(mContext,SpKey.CHOSE_MC))){
            map.put("merchantId", SpUtils.getValue(mContext,SpKey.CHOSE_MC));
        }else {
            return;
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetAPPIndexCustom(resolveAppIndexCustomData(obj));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onSuccessGetAPPIndexCustom(null);
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.onSuccessGetAPPIndexCustom(null);
                    }
                });
    }

    @Override
    public void getAPPIndexCustomOther(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "home_page_setting_1000");
        if(!TextUtils.isEmpty(SpUtils.getValue(mContext,SpKey.CHOSE_MC))){
            map.put("merchantId", SpUtils.getValue(mContext,SpKey.CHOSE_MC));
        }else {
            return;
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetAPPIndexCustomWithOther(resolveAppIndexCustomOtherData(obj));
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onSuccessGetAPPIndexCustomWithOther(null);
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.onSuccessGetAPPIndexCustomWithOther(null);
                    }
                });
    }

    @Override
    public void getAPPIndexCustomNews(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "homePageHeadline_10001");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetAPPIndexCustomNews(resolveAppIndexCustomNews(obj));
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.onSuccessGetAPPIndexCustomNews(null);
                    }
                });
    }

    private List<LiveVideoMain> resolveLiveListData(String obj) {
        List<LiveVideoMain> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("list");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<LiveVideoMain>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<ActGoodsItem> resolveGoodsListData(String obj) {
        List<ActGoodsItem> result = new ArrayList<>();
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
            Type type = new TypeToken<List<ActGoodsItem>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private List<UserInfoExModel> resolveInfoListData(String obj) {
        List<UserInfoExModel> result = new ArrayList<>();
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
            Type type = new TypeToken<List<UserInfoExModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private UserInfoMonModel resolveMineData(String obj) {
        UserInfoMonModel result = null;
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
            Type type = new TypeToken<UserInfoMonModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private AppIndexCustom resolveAppIndexCustomData(String obj) {
        AppIndexCustom result = null;
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
            Type type = new TypeToken<AppIndexCustom>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private AppIndexCustomNews resolveAppIndexCustomNews(String obj) {
        AppIndexCustomNews result = null;
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
            Type type = new TypeToken<AppIndexCustomNews>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private AppIndexCustomOther resolveAppIndexCustomOtherData(String obj) {
        AppIndexCustomOther result = null;
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
            Type type = new TypeToken<AppIndexCustomOther>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<VideoCategory> resolveData(String s) {
        List<VideoCategory> list = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(s).getJSONArray("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<VideoCategory>>() {
            }.getType();
            list = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<FaqExportQuestion> resolveQuestionListData(String s) {
        List<FaqExportQuestion> list = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(s).getJSONObject("data").getJSONArray("items");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<FaqExportQuestion>>() {
            }.getType();
            list = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private TongLianMemberData resolveTongData(String obj) {
        TongLianMemberData result = null;
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
            Type type = new TypeToken<TongLianMemberData>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private Boolean resolveRefreshData(String obj) {
        boolean isMore = false;
        try {
            isMore = new JSONObject(obj).getJSONObject("data").getInt("isMore") == 1;
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return isMore;
    }
}