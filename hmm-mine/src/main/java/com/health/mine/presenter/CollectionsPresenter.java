package com.health.mine.presenter;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.health.mine.contract.CollectionsContract;
import com.health.mine.model.CollectionModel;
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
 * @date 2019/04/29 17:24
 * @des
 */

public class CollectionsPresenter implements CollectionsContract.Presenter {

    private AppCompatActivity mActivity;
    private CollectionsContract.View mView;
    private CommonViewModel mViewModel;
    private Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            mView.onGetCollectionsSuccess(resolveData(s));
        }
    };

    private List<CollectionModel> resolveData(String s) {
        List<CollectionModel> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray array = JsonUtils.getJsonArray(jsonObject, "data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = JsonUtils.getJsonObject(array, i);
                CollectionModel model = new CollectionModel();
                model.setId(JsonUtils.getString(object, "id"));
                model.setTitle(JsonUtils.getString(object, "title"));
                model.setReadNum(JsonUtils.getInt(object, "readQuantity"));
                model.setImgUrl(JsonUtils.getString(object, "images"));
                list.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public CollectionsPresenter(AppCompatActivity activity, CollectionsContract.View view) {
        mActivity = activity;
        mView = view;
        mViewModel = ViewModelProviders.of(activity).get(CommonViewModel.class);
    }

    @Override
    public void getCollections() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, Functions.FUNCTION_COLLECTIONS);
        ObservableHelper.createObservable(mActivity, map)
                .subscribe(new StringObserver(mView, mActivity, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mViewModel.getModel().setValue(obj);
                        mViewModel.getModel().observe(mActivity, mObserver);
                    }
                });
    }
}
