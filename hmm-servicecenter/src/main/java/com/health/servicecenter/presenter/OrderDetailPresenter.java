package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.OrderDetailContract;
import com.health.servicecenter.model.OrderDetailModel;
import com.health.servicecenter.model.StoreSimpleModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.model.PageInfoEarly;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/10 16:29
 * @des 订单详情
 */

public class OrderDetailPresenter implements OrderDetailContract.Presenter {

    private Context mContext;
    private OrderDetailContract.View mView;


    public OrderDetailPresenter(Context context, OrderDetailContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getOrderDetail(String orderId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("orderId", orderId);
        //map.put(Functions.FUNCTION, "60005");
        map.put(Functions.FUNCTION, "9534");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
//                        mCommonViewModel.getModel().setValue(obj);
//                        mCommonViewModel.getModel().observe(mActivity, mObserver);
                        mView.onGetOrderDetailSuccess(resolveData(obj));
                    }
                });
    }

    @Override
    public void getStoreDetail(String shopId, String goodsId, String cityNo, Double lng, Double lat) {

        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", goodsId);
        map.put("shopId", shopId + "");
        map.put("cityNo", cityNo + "");
        map.put("longitude", lng);
        map.put("latitude", lat);
        map.put(Functions.FUNCTION, "5058");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
//                        mCommonViewModel.getModel().setValue(obj);
//                        mCommonViewModel.getModel().observe(mActivity, mObserver);
                        mView.onGetStoreSimpleSuccess(resolveSimpleData(obj));
                    }
                });


    }

    @Override
    public void cancleOrder(String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put(Functions.FUNCTION, "9525");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onCancleOrderSuccess(new JSONObject(obj).getString("msg"));
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
    public void deleteOrder(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", id);
        map.put(Functions.FUNCTION, "9532");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            mView.onDeleteOrderSuccess(new JSONObject(obj).getString("msg"));
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

    private StoreSimpleModel resolveSimpleData(String obj) {
        StoreSimpleModel result = null;
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
            Type type = new TypeToken<StoreSimpleModel>() {
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

    private OrderDetailModel resolveData(String obj) {
        OrderDetailModel result = null;
        try {
//            obj="{\n" +
//                    "  \"code\": 0,\n" +
//                    "  \"msg\": \"操作成功\",\n" +
//                    "  \"data\": {\n" +
//                    "    \"id\": 1337263954853920,\n" +
//                    "    \"merchantId\": 17,\n" +
//                    "    \"merchantName\": \"憨妈妈母婴生活馆\",\n" +
//                    "    \"shopId\": 87,\n" +
//                    "    \"shopName\": \"憨妈妈母婴生活馆\",\n" +
//                    "    \"chainShopName\": \"高新区分馆1\",\n" +
//                    "    \"shopBrandName\": \"憨妈妈\",\n" +
//                    "    \"buyChannelCode\": 2,\n" +
//                    "    \"payMoney\": 0.02,\n" +
//                    "    \"goodsMoney\": 0.02,\n" +
//                    "    \"discountMoney\": 0.0,\n" +
//                    "    \"memberId\": \"2003130000004100\",\n" +
//                    "    \"memberTelephone\": \"13404217659\",\n" +
//                    "    \"memberName\": null,\n" +
//                    "    \"payStatus\": 1,\n" +
//                    "    \"payTime\": \"2020-03-16 14:59:13\",\n" +
//                    "    \"cancelStatus\": 0,\n" +
//                    "    \"commentStatus\": 0,\n" +
//                    "    \"status\": 2,\n" +
//                    "    \"createTime\": \"2020-03-16 14:59:04\",\n" +
//                    "    \"currentTime\": \"2020-03-16 15:00:13\",\n" +
//                    "    \"commentScore\": 5.0,\n" +
//                    "    \"orderGoodsList\": [\n" +
//                    "      {\n" +
//                    "        \"id\": 1337263954853952,\n" +
//                    "        \"merchantId\": 17,\n" +
//                    "        \"memberId\": \"2003130000004100\",\n" +
//                    "        \"orderId\": 1337263954853920,\n" +
//                    "        \"goodsId\": 10198,\n" +
//                    "        \"goodsTitle\": \"骨盆聚合单次体验\",\n" +
//                    "        \"goodsImage\": \"http://hmm-public.oss-cn-beijing.aliyuncs.com/merchant-user-dev/images/f2bcd934-690d-4b30-9c64-8c453e223ad9.png\",\n" +
//                    "        \"description\": null,\n" +
//                    "        \"goodsNo\": null,\n" +
//                    "        \"goodsNumber\": 2,\n" +
//                    "        \"goodsPrice\": 0.01,\n" +
//                    "        \"expiredDate\": 30,\n" +
//                    "        \"payStatus\": 1,\n" +
//                    "        \"payTime\": \"2020-03-16 14:59:13\",\n" +
//                    "        \"expiredTime\": \"2020-04-15\",\n" +
//                    "        \"payMoney\": 0.02,\n" +
//                    "        \"ticketList\": [\n" +
//                    "          {\n" +
//                    "            \"id\": 479,\n" +
//                    "            \"orderId\": null,\n" +
//                    "            \"orderGoodsId\": null,\n" +
//                    "            \"goodsId\": null,\n" +
//                    "            \"ticketNo\": \"95234775\",\n" +
//                    "            \"isUse\": 0,\n" +
//                    "            \"isRefund\": 1,\n" +
//                    "            \"qrcodeBase64\": null,\n" +
//                    "            \"ticketShopName\": null,\n" +
//                    "            \"ticketTime\": null\n" +
//                    "          },\n" +
//                    "          {\n" +
//                    "            \"id\": 480,\n" +
//                    "            \"orderId\": null,\n" +
//                    "            \"orderGoodsId\": null,\n" +
//                    "            \"goodsId\": null,\n" +
//                    "            \"ticketNo\": \"53330302\",\n" +
//                    "            \"isUse\": 1,\n" +
//                    "            \"isRefund\": 0,\n" +
//                    "            \"qrcodeBase64\": null,\n" +
//                    "            \"ticketShopName\": \"憨妈妈母婴生活馆\",\n" +
//                    "            \"ticketTime\": \"2020-03-16 15:00:12\"\n" +
//                    "          }\n" +
//                    "        ],\n" +
//                    "        \"useTicketList\": [\n" +
//                    "          {\n" +
//                    "            \"id\": 480,\n" +
//                    "            \"orderId\": null,\n" +
//                    "            \"orderGoodsId\": null,\n" +
//                    "            \"goodsId\": null,\n" +
//                    "            \"ticketNo\": \"53330302\",\n" +
//                    "            \"isUse\": 1,\n" +
//                    "            \"isRefund\": 0,\n" +
//                    "            \"qrcodeBase64\": null,\n" +
//                    "            \"ticketShopName\": \"憨妈妈母婴生活馆\",\n" +
//                    "            \"ticketTime\": \"2020-03-16 15:00:12\"\n" +
//                    "          }\n" +
//                    "        ],\n" +
//                    "        \"useNum\": 1,\n" +
//                    "        \"notUseTicketList\": [\n" +
//                    "          \n" +
//                    "        ],\n" +
//                    "        \"notUseNum\": 0,\n" +
//                    "        \"goodsCategory\": null\n" +
//                    "      }\n" +
//                    "    ],\n" +
//                    "    \"applyShops\": null,\n" +
//                    "    \"payDetails\": null,\n" +
//                    "    \"updateType\": null,\n" +
//                    "    \"orderType\": 0,\n" +
//                    "    \"bargainId\": null,\n" +
//                    "    \"bargainMemberId\": null,\n" +
//                    "    \"denomination\": null,\n" +
//                    "    \"cityNo\": \"320500\",\n" +
//                    "    \"orderRefundsList\": [\n" +
//                    "      {\n" +
//                    "        \"refundId\": \"20200316145925730\",\n" +
//                    "        \"orderId\": null,\n" +
//                    "        \"refundStatus\": 3,\n" +
//                    "        \"merchantId\": 17,\n" +
//                    "        \"merchantName\": null,\n" +
//                    "        \"orderGoodsRefundsList\": [\n" +
//                    "          {\n" +
//                    "            \"orderGoodsId\": 1337263954853952,\n" +
//                    "            \"goodsId\": 10198,\n" +
//                    "            \"goodsTitle\": \"骨盆聚合单次体验\",\n" +
//                    "            \"goodsImage\": null,\n" +
//                    "            \"ticketNoList\": [\n" +
//                    "              {\n" +
//                    "                \"id\": 479,\n" +
//                    "                \"ticketNo\": \"95234775\"\n" +
//                    "              }\n" +
//                    "            ],\n" +
//                    "            \"refundNumber\": 1,\n" +
//                    "            \"refundMoney\": 0.01\n" +
//                    "          }\n" +
//                    "        ],\n" +
//                    "        \"refundNumber\": 1\n" +
//                    "      }\n" +
//                    "    ]\n" +
//                    "  }\n" +
//                    "}";

            JSONObject data = new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<OrderDetailModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}