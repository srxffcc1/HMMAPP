//package com.health.index.presenter;
//
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProviders;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.health.index.contract.TipsContract;
//import com.health.index.model.TipModel;
//import com.healthy.library.constant.Functions;
//import com.healthy.library.model.CommonViewModel;
//import com.healthy.library.net.ObservableHelper;
//import com.healthy.library.net.StringObserver;
//import com.healthy.library.utils.JsonUtils;
//import com.healthy.library.utils.TimestampUtils;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author Li
// * @date 2019/04/28 15:16
// * @des
// */
//public class TipPresenter implements TipsContract.Presenter {
//    private AppCompatActivity mActivity;
//    private TipsContract.View mView;
//    private CommonViewModel mViewModel;
//    private Date mQueryDate;
//    private Observer<String> mObserver = new Observer<String>() {
//        @Override
//        public void onChanged(@Nullable String s) {
//            resolveData(s);
//        }
//    };
//
//    public TipPresenter(AppCompatActivity activity, TipsContract.View view) {
//        mActivity = activity;
//        mView = view;
//        mViewModel = ViewModelProviders.of(activity).get(CommonViewModel.class);
//    }
//
//    @Override
//    public void getTipList(Date date) {
//        mQueryDate = date;
//        Map<String, Object> map = new HashMap<>(2);
//        map.put(Functions.FUNCTION, Functions.FUNCTION_TIPS_LIST);
//        map.put("queryDate",
//                TimestampUtils.timestamp2String(mQueryDate.getTime(), "yyyy-MM-dd"));
//        ObservableHelper.createObservable(mActivity, map)
//                .subscribe(new StringObserver(mView, mActivity, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mViewModel.getModel().setValue(obj);
//                        mViewModel.getModel().observe(mActivity, mObserver);
//
//                    }
//                });
//    }
//
//    private void resolveData(String obj) {
//        try {
//            List<TipModel> list = new ArrayList<>();
//            JSONObject jsonObject = new JSONObject(obj);
//            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
//            JSONArray tipArray = JsonUtils.getJsonArray(jsonObject, "knowledgeInfoList");
//            for (int i = 0; i < tipArray.length(); i++) {
//                JSONObject tipObject = JsonUtils.getJsonObject(tipArray, i);
//                TipModel model = new TipModel();
//                model.setId(JsonUtils.getString(tipObject, "id"));
//                model.setTitle(JsonUtils.getString(tipObject, "title"));
//                model.setImgUrl(JsonUtils.getString(tipObject, "images"));
//                model.setReadNum(JsonUtils.getInt(tipObject, "readQuantity"));
//                list.add(model);
//            }
//            mView.onGetTipListSuccess(list, JsonUtils.getString(jsonObject, "content"),
//                    JsonUtils.getString(jsonObject, "status"), mQueryDate);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}