package com.health.mine.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.mine.contract.OrderListContract;
import com.health.mine.model.OrderDetailModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.model.PageInfoEarly;

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
 * @date 2019/04/10 09:34
 * @des
 */
public class OrderListPresenter implements OrderListContract.Presenter {


    private Context mContext;
    private OrderListContract.View mView;
//    private CommonViewModel mCommonViewModel;
//    private Fragment mFragment;
//    private String mLoadType;
//    private int mCurrentPage;
//    private boolean hasMore;
//    private Observer<String> mObserver = new Observer<String>() {
//        @Override
//        public void onChanged(@Nullable String s) {
//            List<OrderDetailModel> list = resolveData(s);
//            mView.onGetOrderListSuccess(mLoadType, list, mCurrentPage, hasMore);
//        }
//    };

    public OrderListPresenter(Context context, OrderListContract.View view) {
        mContext = context;
        mView = view;
//        mFragment = fragment;
//        mCommonViewModel = ViewModelProviders.of(fragment).get(CommonViewModel.class);
    }

    @Override
    public void getOrderList(int currentPage, String type) {
//        mLoadType = currentPage == 1 ? Constants.DATA_REFRESH : Constants.DATA_LOAD;
//        mCurrentPage = currentPage;
        Map<String, Object> map = new HashMap<>(4);
        map.put(Functions.FUNCTION, "60004");
        map.put("pageSize", 10);
        map.put("currentPage", currentPage+"");
        map.put("searchType", type);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        currentPage == 1) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetOrderListSuccess(resolveData(obj),resolveRefreshData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void cancelOrder(String orderId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, Functions.FUNCTION_CANCEL_ORDER);
        map.put("orderId", orderId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onCancelOrderSuccess();
                    }
                });
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
    private List<OrderDetailModel> resolveData(String obj) {
        List<OrderDetailModel> result = new ArrayList<>();
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
            Type type = new TypeToken<List<OrderDetailModel>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
//    private List<OrderDetailModel> resolveData(String s) {
//        List<OrderDetailModel> list = new ArrayList<>();
//        try {
//            JSONObject jsonObject = new JSONObject(s);
//            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
//
//            hasMore = JsonUtils.getInt(jsonObject, "isMore") == 1;
//            JSONArray orderArray = JsonUtils.getJsonArray(jsonObject, "items");
//            for (int i = 0; i < orderArray.length(); i++) {
//                OrderDetailModel model = new OrderDetailModel();
//                JSONObject orderObj = orderArray.getJSONObject(i);
//
//                model.setOrderId(JsonUtils.getString(orderObj, "orderId"));
//                model.setStoreName(JsonUtils.getString(orderObj, "shopName"));
//
//                model.setPaid(Constants.ORDER_STATUS_PAID
//                        .equals(String.valueOf(JsonUtils.getInt(orderObj, "payStatus"))));
//                model.setQuashed(Constants.ORDER_STATUS_CANCELLED
//                        .equals(String.valueOf(JsonUtils.getInt(orderObj, "quashStatus"))));
//                model.setCommented(Constants.ORDER_STATUS_COMMENTED
//                        .equals(String.valueOf(JsonUtils.getInt(orderObj, "commentStatus"))));
//
//                model.setShopBrand(JsonUtils.getString(orderObj, "shopBrand"));
//                model.setShopId(JsonUtils.getString(orderObj, "shopId"));
//                model.setTotalPrice(JsonUtils.getString(orderObj, "paidInAmount"));
//
//                JSONArray goodsArray = JsonUtils.getJsonArray(orderObj, "orderGoodsList");
//                if (goodsArray.length() >= 1) {
//                    JSONObject goodsObj = goodsArray.getJSONObject(0);
//                    model.setGoodsName(JsonUtils.getString(goodsObj, "goodsTitle"));
//                    model.setGoodsNum(JsonUtils.getString(goodsObj, "saleNumber"));
//                    model.setGoodsPrice(JsonUtils.getString(goodsObj, "salePrice"));
//                    model.setGoodsIntroduction(JsonUtils.getString(goodsObj, "introduction"));
//                    JSONArray fileArray = JsonUtils.getJsonArray(goodsObj, "files");
//                    if (fileArray.length() > 0) {
//                        JSONObject fileObj = fileArray.getJSONObject(0);
//                        model.setProductUrl(JsonUtils.getString(fileObj, "file"));
//                    }
//                }
//                list.add(model);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
}