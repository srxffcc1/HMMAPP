package com.health.city.presenter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.city.contract.PostDetailContract;
import com.healthy.library.model.Discuss;
import com.healthy.library.model.PostDetail;
import com.health.city.model.UserInfoCityModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDetailPresenter implements PostDetailContract.Presenter {
    private Context mContext;
    private PostDetailContract.View mView;
    private String merchantId;

    public PostDetailPresenter(String merchantId, Context context, PostDetailContract.View view) {
        if (TextUtils.isEmpty(merchantId)) {
            merchantId = SpUtils.getValue(mContext, SpKey.CHOSE_MC);
        }
        this.merchantId = merchantId;
        mContext = context;
        mView = view;
    }

    @Override
    public void getPostDetail(Map<String, Object> map) {
        map.put("merchantId", merchantId);
        map.put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        map.put(Functions.FUNCTION, "new_7002");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetPostDetail(resolveDetailData(obj));
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

    private PostDetail resolveDetailData(String obj) {
        PostDetail result = new PostDetail();
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
            Type type = new TypeToken<PostDetail>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void getDisgussList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7012");
        map.put("merchantId", merchantId);
        map.put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetDiscuss(resolveListData(obj), resolveRefreshData(obj));
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

    @Override
    public void delete(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7021");
        map.put("merchantId", merchantId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessDelete();
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

    @Override
    public void addDiscuss(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7010");
        map.put("merchantId", merchantId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true, true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);

                        SpUtils.store(mContext, SpKey.DISCUSS_TMP, "");
                        mView.onSuccessAdd();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
//                        mView.onGetStoreDetailFail();
                    }
                });
    }

    @Override
    public void addReview(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7016");
        map.put("merchantId", merchantId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true, true, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessAdd();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
//                        mView.onGetStoreDetailFail();
                    }
                });
    }

    @Override
    public void likeChild(final Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7011");
        map.put("merchantId", merchantId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        if ("0".equals(map.get("type"))) {
                            Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
                        }
                        mView.onSuccessLike();
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

    private List<Discuss> resolveListData(String obj) {
        List<Discuss> result = new ArrayList<>();
        try {
            JSONArray data = new JSONObject(obj).getJSONObject("data").getJSONArray("items");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<Discuss>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            //System.out.println("错误:"+obj);
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void like(final Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7003");
        map.put("merchantId", merchantId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        if ("0".equals(map.get("type"))) {
                            Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
                        }
                        mView.onSuccessLike();
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

    @Override
    public void fan(final Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7018");
        map.put("merchantId", merchantId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        if ("0".equals(map.get("type"))) {
                            Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
                        } else {

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "取消关注成功", Toast.LENGTH_SHORT).show();
                                }
                            }, 100);
                        }
                        mView.onSuccessFan();
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

    @Override
    public void warn(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7017");
        map.put("merchantId", merchantId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessFan();
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

    private UserInfoCityModel resolveMineData(String obj) {
        UserInfoCityModel result = new UserInfoCityModel();
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
            Type type = new TypeToken<UserInfoCityModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getMine() {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "1011");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetMine(resolveMineData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
//                        mView.onGetStoreDetailFail();
                    }
                });
    }
}
