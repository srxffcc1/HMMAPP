package com.healthy.library.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.constant.Functions;
import com.healthy.library.contract.ActVipContract;
import com.healthy.library.contract.ActVipOnlineContract;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.GoodsBasketCell;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class ActVipOnlinePresenter implements ActVipOnlineContract.Presenter {

    private Context mContext;
    private ActVipOnlineContract.View mView;

    public ActVipOnlinePresenter(Context context, ActVipOnlineContract.View view) {
        mContext = context;
        mView = view;
    }
    private GoodsDetail resolveDetailData(String obj) {
        GoodsDetail result = null;
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
            Type type = new TypeToken<GoodsDetail>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void getVipOnlineGoodsWithCell(Map<String, Object> map, final GoodsBasketCell goodsBasketCell) {
        if(!TextUtils.isEmpty(goodsBasketCell.goodsImage)){
            return;
        }
        map.put(Functions.FUNCTION,"9202-1");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        goodsBasketCell.goodsImage="";
                        GoodsDetail result=resolveDetailData(obj);
                        if(result!=null){
                            goodsBasketCell.goodsId=result.id;
                            try {
                                goodsBasketCell.goodsImage=result.headImages.get(0).filePath;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mView.onSucessGetVipOnlineGoods();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        goodsBasketCell.goodsImage="";
                        mView.onSucessGetVipOnlineGoods();
                    }

                });
    }

    @Override
    public void getVipOnlineGoodsWithVipCell(Map<String, Object> map, final ActVip.PopDetail popDetail) {
        if(!TextUtils.isEmpty(popDetail.filePath)){
            return;
        }
        map.put(Functions.FUNCTION,"9202-1");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        GoodsDetail result=resolveDetailData(obj);
                        popDetail.filePath="";
                        if(result!=null){
                            try {
//                                popDetail.GoodsName=result.goodsTitle;
                                popDetail.filePath=result.headImages.get(0).filePath;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mView.onSucessGetVipOnlineGoods();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        popDetail.filePath="";
                        mView.onSucessGetVipOnlineGoods();
                    }

                });
    }

    @Override
    public void getVipOnlineGoodsWithVipSale(Map<String, Object> map, final ActVip.SaleInfo saleInfo) {
        if(!TextUtils.isEmpty(saleInfo.filePath)){
            return;
        }
        map.put(Functions.FUNCTION,"9202-1");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        saleInfo.filePath="";
                        GoodsDetail result=resolveDetailData(obj);
                        if(result!=null){
                            try {
//                                saleInfo.GoodsName=result.goodsTitle;
                                saleInfo.filePath=result.headImages.get(0).filePath;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mView.onSucessGetVipOnlineGoods();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        saleInfo.filePath="";
                        mView.onSucessGetVipOnlineGoods();
                    }

                });
    }
}