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
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.contract.ActH5Contract;
import com.healthy.library.contract.ActVipContract;
import com.healthy.library.model.ActVip;
import com.healthy.library.model.GoodsBasketStore;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.SpUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class ActH5Presenter implements ActH5Contract.Presenter {

    private Context mContext;
    private ActH5Contract.View mView;

    public ActH5Presenter(Context context, ActH5Contract.View view) {
        mContext = context;
        mView = view;
    }

    public ActH5Presenter(Context context) {
        mContext = context;
    }

    @Override
    public void getH5() {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "4033");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(null, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        saveUrl(obj);
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

    private void saveUrl(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject urlJson = JsonUtils.getJsonObject(jsonObject, "data");

            SpUtils.store(mContext, UrlKeys.H5_BargainUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_BargainUrl));
            SpUtils.store(mContext, UrlKeys.H5_proAnswer,
                    JsonUtils.getString(urlJson, UrlKeys.H5_proAnswer));
            SpUtils.store(mContext, UrlKeys.H5_recipeStepContUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_recipeStepContUrl));
            SpUtils.store(mContext, UrlKeys.H5_recipeInfoUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_recipeInfoUrl));
            SpUtils.store(mContext, UrlKeys.H5_dietProposeUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_dietProposeUrl));
            SpUtils.store(mContext, UrlKeys.H5_tableDetailUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_tableDetailUrl));
            SpUtils.store(mContext, UrlKeys.H5_liveVideoUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_liveVideoUrl));
            SpUtils.store(mContext, UrlKeys.H5_serviceStaff,
                    JsonUtils.getString(urlJson, UrlKeys.H5_serviceStaff));
            SpUtils.store(mContext, UrlKeys.H5_babySexUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_babySexUrl));
            SpUtils.store(mContext, UrlKeys.H5_babyWeightUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_babyWeightUrl));
            SpUtils.store(mContext, UrlKeys.H5_babyGrowthUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_babyGrowthUrl));
            SpUtils.store(mContext, UrlKeys.H5_babyNameUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_babyNameUrl));
            SpUtils.store(mContext, UrlKeys.H5_expertClassListUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_expertClassListUrl));
            SpUtils.store(mContext, UrlKeys.H5_classVideoUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_classVideoUrl));
            SpUtils.store(mContext, UrlKeys.H5_classVideoContUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_classVideoContUrl));
            SpUtils.store(mContext, UrlKeys.H5_rewardNoticeUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_rewardNoticeUrl));
            SpUtils.store(mContext, UrlKeys.H5_expertNoticeUrl,
                    JsonUtils.getString(urlJson, UrlKeys.H5_expertNoticeUrl));
            SpUtils.store(mContext, UrlKeys.H5_KNOWLEDGE,
                    JsonUtils.getString(urlJson, UrlKeys.H5_KNOWLEDGE));
            SpUtils.store(mContext, UrlKeys.H5_POSTURL,
                    JsonUtils.getString(urlJson, UrlKeys.H5_POSTURL));
            SpUtils.store(mContext, UrlKeys.H5_CAN_EAT,
                    JsonUtils.getString(urlJson, UrlKeys.H5_CAN_EAT));
            SpUtils.store(mContext, UrlKeys.H5_CAN_EAT_LIST,
                    JsonUtils.getString(urlJson, UrlKeys.H5_CAN_EAT_LIST));
            SpUtils.store(mContext, UrlKeys.H5_VACCINE_DETAIL,
                    JsonUtils.getString(urlJson, UrlKeys.H5_VACCINE_DETAIL));
            SpUtils.store(mContext, UrlKeys.H5_B,
                    JsonUtils.getString(urlJson, UrlKeys.H5_B));
            SpUtils.store(mContext, UrlKeys.H5_MEAL,
                    JsonUtils.getString(urlJson, UrlKeys.H5_MEAL));
            SpUtils.store(mContext, UrlKeys.H5_FOOD_LIST,
                    JsonUtils.getString(urlJson, UrlKeys.H5_FOOD_LIST));
            SpUtils.store(mContext, UrlKeys.H5_FOOD_DETAIL,
                    JsonUtils.getString(urlJson, UrlKeys.H5_FOOD_DETAIL));
            SpUtils.store(mContext, UrlKeys.H5_FOOD_LEARN,
                    JsonUtils.getString(urlJson, UrlKeys.H5_FOOD_LEARN));
            SpUtils.store(mContext, UrlKeys.H5_TABLE_DETAIL,
                    JsonUtils.getString(urlJson, UrlKeys.H5_TABLE_DETAIL));
            SpUtils.store(mContext, UrlKeys.H5_CAN_EAT_ALL,
                    JsonUtils.getString(urlJson, UrlKeys.H5_CAN_EAT_ALL));
            /*---------------------- 3.6.0需求新增h5地址 START -----------------------*/
            SpUtils.store(mContext, UrlKeys.H5_VOTE_APPLY_URL,
                    JsonUtils.getString(urlJson, UrlKeys.H5_VOTE_APPLY_URL));
            SpUtils.store(mContext, UrlKeys.H5_VOTE_LIST_URL,
                    JsonUtils.getString(urlJson, UrlKeys.H5_VOTE_LIST_URL));
            SpUtils.store(mContext, UrlKeys.H5_LOTTERY_URL,
                    JsonUtils.getString(urlJson, UrlKeys.H5_LOTTERY_URL));
            /*---------------------- 3.6.0需求新增h5地址 END -----------------------*/

            /*---------------------- 3.6.1需求新增h5地址 START ---------------------*/
            /*** 新人礼包 */
            SpUtils.store(mContext, UrlKeys.H5_COUPONS_VIEW_URL,
                    JsonUtils.getString(urlJson, UrlKeys.H5_COUPONS_VIEW_URL));
            /*---------------------- 3.6.1需求新增h5地址 END ---------------------*/

            /*---------------------- 3.6.3需求新增h5地址 START ---------------------*/
            /** 签到分享 */
            SpUtils.store(mContext, UrlKeys.H5_SHARE_SIGN_URL,
                    JsonUtils.getString(urlJson, UrlKeys.H5_SHARE_SIGN_URL));
            /*---------------------- 3.6.3需求新增h5地址 END ---------------------*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}