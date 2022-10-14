package com.health.index.presenter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.health.index.contract.BirthPackageContract;
import com.health.index.model.BirthPackageModel;
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
import java.util.Locale;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/24 16:45
 * @des
 */
public class BirthPackagePresenter implements BirthPackageContract.Presenter {

    private AppCompatActivity mActivity;
    private BirthPackageContract.View mView;
    private CommonViewModel mCommonViewModel;
    private int mType;
    private int mPos;
    private int mStatus;
    private Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            mView.onGetBirthPackageSuccess(resolvePackages(s));
        }
    };

    public BirthPackagePresenter(AppCompatActivity activity, BirthPackageContract.View view) {
        mActivity = activity;
        mView = view;
        mCommonViewModel = ViewModelProviders.of(activity).get(CommonViewModel.class);
    }

    @Override
    public void getBirthPackage(int type) {
        Map<String, Object> map = new HashMap<>(1);
        mType = type;
        map.put(Functions.FUNCTION, Functions.FUNCTION_PACKAGE_LIST);
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mCommonViewModel.getModel().setValue(obj);
                        mCommonViewModel.getModel().observe(mActivity, mObserver);
                    }
                });
    }

    @Override
    public void changePackage(String key, int id, int pos, int status) {
        mPos = pos;
        mStatus = status;
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, Functions.FUNCTION_CHANGE_PACKAGE);
        map.put(key, String.valueOf(id));
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onChangePackageSuccess(mPos,mStatus);
                    }
                });
    }

    private List<BirthPackageModel> resolvePackages(String s) {
        List<BirthPackageModel> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
            if (mType == BirthPackageModel.PACKAGE_MOM) {
                addData(list, jsonObject, "momStatusOneList",
                        BirthPackageModel.UNPREPARED, BirthPackageModel.PACKAGE_MOM);
                addData(list, jsonObject, "momStatusTwoList",
                        BirthPackageModel.PREPARED, BirthPackageModel.PACKAGE_MOM);
            } else if (mType == BirthPackageModel.PACKAGE_BABY) {
                addData(list, jsonObject, "babyStatusOneList",
                        BirthPackageModel.UNPREPARED, BirthPackageModel.PACKAGE_BABY);
                addData(list, jsonObject, "babyStatusTwoList",
                        BirthPackageModel.PREPARED, BirthPackageModel.PACKAGE_BABY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 按需添加数据
     *
     * @param list          源数据
     * @param jsonObject    json
     * @param name          key
     * @param prepareStatus 准备状态
     * @param packageType   用品状态
     */
    private void addData(List<BirthPackageModel> list, JSONObject jsonObject, String name,
                         int prepareStatus, int packageType) {
        JSONArray array = JsonUtils.getJsonArray(jsonObject, name);
        for (int i = 0; i < array.length(); i++) {
            JSONObject j = JsonUtils.getJsonObject(array, i);
            BirthPackageModel model = new BirthPackageModel();
            model.setPrepareStatus(prepareStatus);
            model.setPackageType(packageType);
            model.setId(JsonUtils.getInt(j, "id"));
            model.setTitle(JsonUtils.getString(j, "goods"));
            model.setHidden(true);
            model.setIntroduction(JsonUtils.getString(j, "introduction"));
            model.setCountDes(String.format(Locale.CHINA, "%d%s",
                    JsonUtils.getInt(j, "count"),
                    JsonUtils.getString(j, "set")));
            list.add(model);
        }
    }
}