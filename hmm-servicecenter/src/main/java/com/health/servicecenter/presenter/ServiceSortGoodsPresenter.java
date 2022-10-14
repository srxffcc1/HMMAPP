package com.health.servicecenter.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.ServiceSortGoodsContract;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.message.UpdateSeachInfo;
import com.healthy.library.model.MainSearchModel;
import com.healthy.library.model.OrderListPageInfo;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.model.SortGoodsListModel;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
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
 * @date 2019/04/29 16:19
 * @des
 */

public class ServiceSortGoodsPresenter implements ServiceSortGoodsContract.Presenter {

    private Context mContext;
    private ServiceSortGoodsContract.View mView;

    public ServiceSortGoodsPresenter(Context context, ServiceSortGoodsContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getGoodsList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9201");
        if (map.get("goodsTitle") != null && !TextUtils.isEmpty(map.get("goodsTitle").toString())) {
            List<MainSearchModel> mainSearchModelList = SpUtils.resolveHistoryArrayData(SpUtils.getValue(mContext, SpKey.USE_SEACHTIP), MainSearchModel.class);
            if (mainSearchModelList.size() > 0) {
                if (!ListUtil.checkObjIsInList(new SimpleArrayListBuilder<String>().putList(mainSearchModelList, new ObjectIteraor<MainSearchModel>() {
                    @Override
                    public String getDesObj(MainSearchModel o) {
                        return o.keyword;
                    }
                }), map.get("goodsTitle").toString())) {
                    if (mainSearchModelList.size() >= 10) {//说明超出10个了
                        mainSearchModelList.remove(mainSearchModelList.size() - 1);
                    }
                    mainSearchModelList.add(0, new MainSearchModel(map.get("goodsTitle").toString()));
                }
            } else {
                mainSearchModelList.add(0, new MainSearchModel(map.get("goodsTitle").toString()));
            }
            SpUtils.store(mContext, SpKey.USE_SEACHTIP, new Gson().toJson(mainSearchModelList));
            EventBus.getDefault().post(new UpdateSeachInfo(1));
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetGoodsListSuccess(resolveStoreData(obj), resolveRefreshData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void addShopCat(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "25013");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.successAddShopCat(new JSONObject(obj).getString("msg"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getShopCart() {
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        map.put(Functions.FUNCTION, "25012");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetShopCartSuccess(resolveShopCartlData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getRecommendList(Map<String, Object> map) {
        map.put("type", "5");
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        map.put("pageSize", "10");
        map.put(Functions.FUNCTION, "9119");
        if(map.get("shopId")==null){
            return;
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetRecommendListSuccess(resolveRecommendListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void addSearchRecord(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private ShopCartModel resolveShopCartlData(String obj) {
        ShopCartModel result = null;
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
            Type type = new TypeToken<ShopCartModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private OrderListPageInfo resolveRefreshData(String obj) {
        OrderListPageInfo result = new OrderListPageInfo();
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
            Type type = new TypeToken<OrderListPageInfo>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private List<SortGoodsListModel> resolveStoreData(String obj) {
        List<SortGoodsListModel> result = null;
        try {
            JSONObject object = new JSONObject(obj).getJSONObject("data");
            JSONArray data = object.getJSONArray("list");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<SortGoodsListModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;

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
        return result;
    }

}
