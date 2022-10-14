package com.health.servicecenter.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.servicecenter.contract.ServiceBasketActDetailContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.DiscountTopModel;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsBasketGroup;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;


/**
 * @author Li
 * @date 2019/04/29 16:19
 * @des
 */

public class ServiceBasketActDetailPresenter implements ServiceBasketActDetailContract.Presenter {

    private Context mContext;
    private ServiceBasketActDetailContract.View mView;

    public ServiceBasketActDetailPresenter(Context context, ServiceBasketActDetailContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getActDetailOnline(GoodsBasketCell goodsBasketCell, Map<String, Object> map) {

    }

    @Override
    public void getActDetail(final GoodsBasketGroup goodsBasketGroup, Map<String, Object> map) {
//        map.put(Functions.FUNCTION, "marketing_9306");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoStringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        ////System.out.println("成功获得活动");
//                        goodsBasketGroup.setDiscountTopModel(resolveTopData(obj));
//                        mView.onSucessGetBasketActList(goodsBasketGroup.getDiscountTopModel());
//
//                    }
//
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//                        mView.onSucessGetBasketActList(null);
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//                });
    }

    @Override
    public void getActDetailOnlyCell(final GoodsBasketCell goodsBasketCell, Map<String, Object> map) {
        map.put(Functions.FUNCTION, "marketing_9306");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        ////System.out.println("成功获得活动");
                        try {
                            goodsBasketCell.setDiscountTopModel(resolveTopData(obj));
                            goodsBasketCell.goodsBasketGroup.setDiscountTopModel(resolveTopData(obj));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mView.onSucessGetBasketActListEx(goodsBasketCell);

                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        try {
                            goodsBasketCell.setDiscountTopModel(null);
                            goodsBasketCell.goodsBasketGroup.setDiscountTopModel(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mView.onSucessGetBasketActListEx(goodsBasketCell);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private DiscountTopModel resolveTopData(String obj) {
        DiscountTopModel result = null;
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
            Type type = new TypeToken<DiscountTopModel>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
