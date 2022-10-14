package com.health.index.presenter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;

import com.health.index.contract.AnalyzeContract;
import com.health.index.model.AnalyzeModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.CommonViewModel;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/25 15:09
 * @des B超
 */

public class AnalyzePresenter implements AnalyzeContract.Presenter {
    private AppCompatActivity mContext;
    private AnalyzeContract.View mView;
    private CommonViewModel mViewModel;
    private String mWeekId;
    private String mStatus;
    private Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            List<AnalyzeModel> list = resolveData(s);
            mView.onGetAnalysisProjectsSuccess(list, resolveAnalysisData(s), mWeekId, mStatus);
        }
    };


    public AnalyzePresenter(AppCompatActivity context, AnalyzeContract.View view) {
        mContext = context;
        mView = view;
        mViewModel = ViewModelProviders.of(context).get(CommonViewModel.class);
    }

    @Override
    public void getAnalysis(String weekId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, Functions.FUNCTION_ANALYZE);
        if (!TextUtils.isEmpty(weekId)) {
            map.put("id", weekId);
        }
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mViewModel.getModel().setValue(obj);
                        mViewModel.getModel().observe(mContext, mObserver);
                    }
                });
    }

    private List<AnalyzeModel> resolveData(String s) {
        List<AnalyzeModel> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject dataObject = JsonUtils.getJsonObject(jsonObject, "data");
            mWeekId = JsonUtils.getString(dataObject, "genWeekId");
            mStatus = JsonUtils.getString(dataObject, "isStatus");
            JSONArray array = JsonUtils.getJsonArray(dataObject, "typeBDTOList");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = JsonUtils.getJsonObject(array, i);
                AnalyzeModel model = new AnalyzeModel();
                model.setId(JsonUtils.getString(object, "maxId"));
                model.setName(JsonUtils.getString(object, "typeBItem"));
                list.add(model);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<AnalyzeModel> resolveAnalysisData(String s) {
        List<AnalyzeModel> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject dataObject = JsonUtils.getJsonObject(jsonObject, "data");
            JSONArray array = JsonUtils.getJsonArray(dataObject, "typeBUnscrambleList");
            for (int i = 0; i < array.length(); i++) {
                JSONObject kvObject = JsonUtils.getJsonObject(array, i);
                AnalyzeModel model = new AnalyzeModel();
                model.setName(JsonUtils.getString(kvObject, "correspondingMapKey"));
                model.setValue(JsonUtils.getString(kvObject, "correspondingMapValue"));
                list.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
