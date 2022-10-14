package com.healthy.library.presenter;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.VoteCouponDetailContract;
import com.healthy.library.model.ShopDetailModel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class VoteCouponDetailPresenter implements VoteCouponDetailContract.Presenter {

    private Context mContext;
    private VoteCouponDetailContract.View mView;

    public VoteCouponDetailPresenter(Context context, VoteCouponDetailContract.View view) {
        mContext = context;
        mView = view;
    }



    @Override
    public void save(Map<String, Object> map) {
        map.put("function", "save_user_info_001");
        map.put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext,
                        false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessSubmit();
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
    public void getStoreList(HashMap<String, Object> map) {
        map.put(Functions.FUNCTION, "101004-1");
        map.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
        map.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onGetStoreListSuccess(resolveStoreListData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                    }
                });
    }
    private List<ShopDetailModel> resolveStoreListData(String obj) {
        List<ShopDetailModel> shopDetailModel = new ArrayList<>();
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
            Type type = new TypeToken<List<ShopDetailModel>>() {
            }.getType();
            shopDetailModel = gson.fromJson(userShopInfoDTOS, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return shopDetailModel;
    }
}