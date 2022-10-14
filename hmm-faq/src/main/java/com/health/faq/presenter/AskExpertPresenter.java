package com.health.faq.presenter;

import android.content.Context;

import com.health.faq.contract.AskExpertContract;
import com.health.faq.model.ExpertInfoModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/07/22 14:47
 * @des
 */

public class AskExpertPresenter implements AskExpertContract.Presenter {

    private Context mContext;
    private AskExpertContract.View mView;

    public AskExpertPresenter(Context context, AskExpertContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getExpertInfo(String id) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, Functions.FUNCTION_EXPERT_HOMEPAGE);
        map.put("userId", id);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, false
                        , false, true, true, true) {

                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            ExpertInfoModel infoModel = new ExpertInfoModel();
                            JSONObject dataObject = JsonUtils.getJsonObject(jsonObject, "data");
                            infoModel.faceUrl = JsonUtils.getString(dataObject, "faceUrl");
                            infoModel.name = JsonUtils.getString(dataObject, "realName");
                            infoModel.rank = JsonUtils.getString(dataObject, "rank");
                            infoModel.cost = JsonUtils.getString(dataObject, "consultingFees");
                            mView.onGetExpertInfoSuccess(infoModel);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
