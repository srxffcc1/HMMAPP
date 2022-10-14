package com.health.city.presenter;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.city.contract.TipPostContract;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.model.PostDetail;
import com.healthy.library.model.TipPost;
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

public class TipPostPresenter implements TipPostContract.Presenter{
    private Context mContext;
    private TipPostContract.View mView;

    public TipPostPresenter(Context context, TipPostContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getPostList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7000");
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,false,false,false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        List<PostDetail> postDetails = resolveListData(obj);
                        if("1".equals(map.get("currentPage")) && !ListUtil.isEmpty(postDetails)){
                            postDetails.get(0).isFirst = true;
                        }
                        mView.onSuccessGetPostList(postDetails,resolveRefreshData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
//                        mView.onGetStoreDetailFail();
                    }
                });
    }
    private List<PostDetail> resolveListData(String obj) {
        List<PostDetail> result = new ArrayList<>();
        try {
            JSONArray data=new JSONObject(obj).getJSONObject("data").getJSONArray("items");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<PostDetail>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            //System.out.println("错误:"+obj);
            e.printStackTrace();
        }

        return result;
    }
    private UserInfoCityModel resolveMineData(String obj) {
        UserInfoCityModel result = new UserInfoCityModel();
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
            Type type = new TypeToken<UserInfoCityModel>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
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

    @Override
    public void getTipPost(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7007");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetTipPost(resolveTipPostData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();

                    }
                });
    }

    private TipPost resolveTipPostData(String obj) {
        TipPost result = new TipPost();
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
            Type type = new TypeToken<TipPost>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private PageInfoEarly resolveRefreshData(String obj) {
        PageInfoEarly result = new PageInfoEarly();
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
            Type type = new TypeToken<PageInfoEarly>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void like(final Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7003");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        if("0".equals(map.get("type"))){
                            Toast.makeText(mContext,"点赞成功",Toast.LENGTH_SHORT).show();
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
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        if("0".equals(map.get("type"))){
                            Toast.makeText(mContext,"关注成功",Toast.LENGTH_SHORT).show();
                        }else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext,"取消关注成功",Toast.LENGTH_SHORT).show();
                                }
                            },100);
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
}
