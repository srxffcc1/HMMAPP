package com.healthy.library.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.contract.LiveGoodsContract;
import com.healthy.library.model.LiveVideoGoods;
import com.healthy.library.model.LiveVideoMain;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;

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
 * @date 2019/03/27 11:37
 * @des
 */

public class LiveGoodsPresenter implements LiveGoodsContract.Presenter {

    private Context mContext;
    private LiveGoodsContract.View mView;

    public LiveGoodsPresenter(Context context, LiveGoodsContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getLiveGoods(final LiveVideoMain liveVideoMain, Map<String, Object> map) {
        List<String> stringList= (List<String>) map.get("liveActivityIds");
        if(stringList!=null&&stringList.size()>0){
            map.put(Functions.FUNCTION, "9291");
            ObservableHelper.createObservable(mContext, map)
                    .subscribe(new NoStringObserver(mView, mContext, false) {
                        @Override
                        protected void onGetResultSuccess(String obj) {
                            super.onGetResultSuccess(obj);
                            List<LiveVideoGoods> liveVideoGoodsList=resolveLiveGoodsData(obj);
                            if(liveVideoMain!=null){
                                liveVideoMain.liveVideoGoodsList=liveVideoGoodsList;
                            }
                            if(map.get("isEncore")!=null&&1==Integer.parseInt(map.get("isEncore").toString())){
                                if(liveVideoGoodsList!=null){
                                    for (int i = 0; i <liveVideoGoodsList.size() ; i++) {
                                        if(liveVideoGoodsList.get(i).isEncore==0){
                                            liveVideoGoodsList.remove(i);
                                            i--;
                                        }
                                    }
                                }
                            }
                            mView.onSucessGetLiveGoods(liveVideoGoodsList);
                        }

                        @Override
                        protected void onFinish() {
                            super.onFinish();
                            mView.onRequestFinish();
                        }

                        @Override
                        protected void onFailure() {
                            super.onFailure();
                            if(liveVideoMain!=null){
                                liveVideoMain.liveVideoGoodsList=new ArrayList<LiveVideoGoods>();
                            }
                            mView.onSucessGetLiveGoods(new ArrayList<LiveVideoGoods>());
                        }
                    });
        }else {

            mView.onSucessGetLiveGoods(new ArrayList<LiveVideoGoods>());
        }

    }

    private List<LiveVideoGoods> resolveLiveGoodsData(String obj) {
        List<LiveVideoGoods> result = new ArrayList<>();
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
            Type type = new TypeToken<List<LiveVideoGoods>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}