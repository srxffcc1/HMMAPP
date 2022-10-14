package com.health.index.presenter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.health.index.contract.BirthCheckListContract;
import com.health.index.model.BirthCheckModel;
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
 * @date 2019/04/24 15:35
 * @des
 */
public class BirthCheckListPresenter implements BirthCheckListContract.Presenter {

    private AppCompatActivity mActivity;
    private BirthCheckListContract.View mView;
    private CommonViewModel mViewModel;
    private Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            mView.onGetBirthCheckListSuccess(resolveBirthCheckList(s), getCurrentId(s));
        }
    };

    public BirthCheckListPresenter(AppCompatActivity activity, BirthCheckListContract.View view) {
        mActivity = activity;
        mView = view;
        mViewModel = ViewModelProviders.of(activity).get(CommonViewModel.class);
    }

    @Override
    public void getBirthCheckList() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, Functions.FUNCTION_BIRTH_CHECK_LIST);
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mViewModel.getModel().setValue(obj);
                        mViewModel.getModel().observe(mActivity, mObserver);
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    /**
     * 解析疫苗列表
     *
     * @param s json
     * @return 疫苗列表
     */
    private List<BirthCheckModel> resolveBirthCheckList(String s) {
        List<BirthCheckModel> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
            JSONArray array = JsonUtils.getJsonArray(jsonObject, "antenatalCareDTOList");
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = JsonUtils.getJsonObject(array, i);
                BirthCheckModel model = new BirthCheckModel();
                model.setId(JsonUtils.getInt(item, "id"));
                model.setDate(JsonUtils.getString(item, "day"));
                model.setTitle(JsonUtils.getString(item, "careWeek"));
                model.setKeys(JsonUtils.getString(item, "keyInspectionItems"));
                list.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private int getCurrentId(String s) {
        int id = 0;
        try {
            JSONObject jsonObject = new JSONObject(s);
            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
            id = JsonUtils.getInt(jsonObject, "weekId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
