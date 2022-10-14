package com.health.index.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.contract.HanMomVideoContract;
import com.healthy.library.model.VideoListModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.VideoCategory;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
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
 * @date 2019/04/25 15:09
 * @des B超
 */

public class HanMomVideoPresenter implements HanMomVideoContract.Presenter {
    private Context mContext;
    private HanMomVideoContract.View mView;

    public HanMomVideoPresenter(Context context, HanMomVideoContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getTabList(Map<String, Object> map) {
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
    public void getVideoList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "8096");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetVideoListSuccess(resolveListData(obj), resolveRefreshData(obj));
                    }
                });
    }

    @Override
    public void getVideoDetail(Map<String, Object> map, final int type) {
        map.put(Functions.FUNCTION, "8062");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetVideoDetailSuccess(resolveDetailData(obj), type);
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.onGetVideoDetailSuccess(null, type);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onGetVideoDetailSuccess(null, type);
                    }
                });
    }

    @Override
    public void addPlayVolume(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "8077");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);

                    }
                });
    }

    @Override
    public void addPraise(Map<String, Object> map, final int type) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onAddPraiseSuccess(new JSONObject(obj).optString("data"), type);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void getUserInfo(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "1011");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            if (new JSONObject(obj).getJSONObject("data").optString("status").equals("3")) {
                                SpUtils.store(mContext, SpKey.STATUS_STR, new JSONObject(obj).getJSONObject("data").optString("babyAge"));
                            } else {
                                SpUtils.store(mContext, SpKey.STATUS_STR, new JSONObject(obj).getJSONObject("data").optString("statusName"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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

    private List<VideoListModel> resolveListData(String s) {
        List<VideoListModel> list = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(s).getJSONObject("data").getJSONArray("list");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<VideoListModel>>() {
            }.getType();
            list = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private PageInfoEarly resolveRefreshData(String obj) {
        PageInfoEarly result = new PageInfoEarly();
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
            Type type = new TypeToken<PageInfoEarly>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private VideoListModel resolveDetailData(String obj) {
        VideoListModel result = new VideoListModel();
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
            Type type = new TypeToken<VideoListModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
