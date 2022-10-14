//package com.health.index.presenter;
//
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProviders;
//import android.content.Context;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.health.index.contract.IndexContract;
//import com.health.index.indexconstants.IndexTypes;
//import com.health.index.model.IndexDataModel;
//import com.health.index.model.UserInfoModel;
//import com.healthy.library.constant.Functions;
//import com.healthy.library.constant.UrlKeys;
//import com.healthy.library.model.CommonViewModel;
//import com.healthy.library.net.ObservableHelper;
//import com.healthy.library.net.StringObserver;
//import com.healthy.library.utils.DateUtils;
//import com.healthy.library.utils.JsonUtils;
//import com.healthy.library.utils.SpUtils;
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
// * @date 2019/04/04 10:41
// * @des
// */
//public class IndexPresenter implements IndexContract.Presenter {
//
//    private Context mContext;
//    private IndexContract.View mView;
//    private CommonViewModel mCommonViewModel;
//    private Fragment mFragment;
//    private Date mDate;
//    private Observer<String> mObserver = new Observer<String>() {
//        @Override
//        public void onChanged(@Nullable String s) {
//            saveUrl(s);
//            mView.onGetDataSuccess(resolveData(s), resolveUserInfo(s), mDate);
//        }
//    };
//
//    public IndexPresenter(Context context, IndexContract.View view, Fragment fragment) {
//        mContext = context;
//        mView = view;
//        mFragment = fragment;
//        mCommonViewModel = ViewModelProviders.of(fragment).get(CommonViewModel.class);
//    }
//
//    @Override
//    public void getIndexInfo(Date queryDate) {
//        Map<String, Object> map = new HashMap<>(2);
//        map.put(Functions.FUNCTION, Functions.FUNCTION_INDEX_INDEX);
//        mDate = queryDate;
//        map.put("queryDate", DateUtils.formatTime2String("yyyy-MM-dd", queryDate));
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new StringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mCommonViewModel.getModel().setValue(obj);
//                        mCommonViewModel.getModel().observe(mFragment, mObserver);
//
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onGetDataFinish();
//                    }
//                });
//
//    }
//
//    private List<IndexDataModel> resolveData(String data) {
//        List<IndexDataModel> list = new ArrayList<>();
//        list.add(new IndexDataModel(IndexTypes.TYPE_HEADER_DATE));
//        try {
//            JSONObject jsonObject = new JSONObject(data);
//            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
//            int type = JsonUtils.getInt(jsonObject, "status");
//            switch (type) {
//                case IndexTypes.TYPE_HEADER_FOR_PREGNANCY:
//                    resolveForPregnancyData(list, jsonObject);
//                    break;
//                case IndexTypes.TYPE_HEADER_PREGNANCY:
//                    resolvePregnancyData(list, jsonObject);
//                    break;
//                case IndexTypes.TYPE_HEADER_PARENTING:
//                    resolveParentingData(list, jsonObject);
//                    break;
//                case IndexTypes.TYPE_HEADER_ONE_YEAR:
//                    resolveOneYearData(list, jsonObject);
//                    break;
//                default:
//                    break;
//            }
//
//            resolveTips(list, jsonObject);
//            //添加小工具
//            if (type == IndexTypes.TYPE_HEADER_PREGNANCY) {
//                list.add(new IndexDataModel(IndexTypes.TYPE_HEADER_FUNCTION_PREGNANCY));
//            } else if (type == IndexTypes.TYPE_HEADER_PARENTING) {
//                list.add(new IndexDataModel(IndexTypes.TYPE_HEADER_FUNCTION_PARENTING));
//            }
//            resolveRecommend(list, jsonObject);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//
//    /**
//     * 解析备孕期数据
//     *
//     * @param list       源数据集
//     * @param dataObject json
//     */
//    private void resolveForPregnancyData(List<IndexDataModel> list, JSONObject dataObject) {
//        IndexDataModel model = new IndexDataModel(IndexTypes.TYPE_HEADER_FOR_PREGNANCY);
//        JSONObject jsonObject = JsonUtils.getJsonObject(dataObject, "pregnancyPrepareHomeDataResult");
//        model.setPregnancyProbability(JsonUtils.getString(jsonObject, "pregnancyProbability"));
//        model.setPregnancyContent(JsonUtils.getString(jsonObject, "content"));
//        model.setMomUrl(JsonUtils.getString(jsonObject, "dayUrl"));
//        JSONArray remindArray = JsonUtils.getJsonArray(dataObject, "dailyMessageResultList");
//        model.setHasRemind(remindArray.length() > 0);
//        if (model.isHasRemind()) {
//            List<IndexDataModel.RemindInfo> remindInfoList = new ArrayList<>();
//            for (int i = 0; i < remindArray.length(); i++) {
//                JSONObject remindObject = JsonUtils.getJsonObject(remindArray, i);
//                IndexDataModel.RemindInfo remindInfo = new IndexDataModel.RemindInfo();
//                remindInfo.setId(JsonUtils.getString(remindObject, "id"));
//                remindInfo.setContent(JsonUtils.getString(remindObject, "content"));
//                remindInfo.setRemindType(JsonUtils.getString(remindObject, "serviceType"));
//
//                remindInfoList.add(remindInfo);
//            }
//            model.setRemindInfoList(remindInfoList);
//        }
//        list.add(model);
//    }
//
//    /**
//     * 解析孕期数据
//     *
//     * @param list       源数据集
//     * @param dataObject json
//     */
//    private void resolvePregnancyData(List<IndexDataModel> list, JSONObject dataObject) {
//        IndexDataModel model = new IndexDataModel(IndexTypes.TYPE_HEADER_PREGNANCY);
//        JSONObject jsonObject = JsonUtils.getJsonObject(dataObject, "babyAndMomChangeResult");
//        model.setGestationDay(JsonUtils.getString(jsonObject, "gestationDay"));
//        model.setBabyContent(JsonUtils.getString(jsonObject, "babyContent"));
//        model.setMomContent(JsonUtils.getString(jsonObject, "momContent"));
//        model.setWeight(JsonUtils.getString(jsonObject, "weight"));
//        model.setCrownRumpLength(JsonUtils.getString(jsonObject, "crownRumpLength"));
//        model.setBabyUrl(JsonUtils.getString(jsonObject, "babyImage"));
//        model.setMomUrl(JsonUtils.getString(jsonObject, "momImage"));
//
//        JSONArray remindArray = JsonUtils.getJsonArray(dataObject, "dailyMessageResultList");
//        model.setHasRemind(remindArray.length() > 0);
//        if (model.isHasRemind()) {
//            List<IndexDataModel.RemindInfo> remindInfoList = new ArrayList<>();
//            for (int i = 0; i < remindArray.length(); i++) {
//                JSONObject remindObject = JsonUtils.getJsonObject(remindArray, i);
//                IndexDataModel.RemindInfo remindInfo = new IndexDataModel.RemindInfo();
//                remindInfo.setId(JsonUtils.getString(remindObject, "id"));
//                remindInfo.setContent(JsonUtils.getString(remindObject, "content"));
//                remindInfo.setRemindType(JsonUtils.getString(remindObject, "serviceType"));
//
//                remindInfoList.add(remindInfo);
//            }
//            model.setRemindInfoList(remindInfoList);
//        }
//        list.add(model);
//    }
//
//    /**
//     * 解析育儿期数据：1岁前
//     *
//     * @param list   源数据集
//     * @param object json
//     */
//    private void resolveParentingData(List<IndexDataModel> list, JSONObject object) {
//        IndexDataModel model = new IndexDataModel();
//        model.setType(IndexTypes.TYPE_HEADER_PARENTING);
//        JSONObject jsonObject = JsonUtils.getJsonObject(object, "postpartumRecoveryResult");
//        model.setWeight(JsonUtils.getString(jsonObject, "weight"));
//        model.setCrownRumpLength(JsonUtils.getString(jsonObject, "height"));
//        model.setBabyContent(JsonUtils.getString(jsonObject, "content"));
//        model.setBornDays(JsonUtils.getString(jsonObject, "babyDay"));
//        model.setMomContent(JsonUtils.getString(jsonObject, "momContent"));
//        model.setBabyUrl(JsonUtils.getString(jsonObject, "babyImage"));
//        model.setMomUrl(JsonUtils.getString(jsonObject, "momImage"));
//
//        JSONArray remindArray = JsonUtils.getJsonArray(object, "dailyMessageResultList");
//        model.setHasRemind(remindArray.length() > 0);
//        if (model.isHasRemind()) {
//            List<IndexDataModel.RemindInfo> remindInfoList = new ArrayList<>();
//            for (int i = 0; i < remindArray.length(); i++) {
//                JSONObject remindObject = JsonUtils.getJsonObject(remindArray, i);
//                IndexDataModel.RemindInfo remindInfo = new IndexDataModel.RemindInfo();
//                remindInfo.setId(JsonUtils.getString(remindObject, "id"));
//                remindInfo.setContent(JsonUtils.getString(remindObject, "content"));
//                remindInfo.setRemindType(JsonUtils.getString(remindObject, "serviceType"));
//
//                remindInfoList.add(remindInfo);
//            }
//            model.setRemindInfoList(remindInfoList);
//        }
//
//        list.add(model);
//    }
//
//    /***
//     * 解析育儿期数据：1岁之后
//     * @param list 源数据集
//     * @param dataObject json
//     */
//    private void resolveOneYearData(List<IndexDataModel> list, JSONObject dataObject) {
//        IndexDataModel model = new IndexDataModel(IndexTypes.TYPE_HEADER_ONE_YEAR);
//        JSONObject jsonObject = JsonUtils.getJsonObject(dataObject, "postpartumRecoveryResult");
//        model.setBornDays(JsonUtils.getString(jsonObject, "childDay"));
//
//        JSONObject userObject = JsonUtils.getJsonObject(dataObject, "memberInfoResult");
//
//        model.setNickname(JsonUtils.getString(userObject, "nickName"));
//        model.setFaceUrl(JsonUtils.getString(userObject, "faceUrl"));
//
//        JSONArray remindArray = JsonUtils.getJsonArray(dataObject, "dailyMessageResultList");
//        model.setHasRemind(remindArray.length() > 0);
//        if (model.isHasRemind()) {
//            List<IndexDataModel.RemindInfo> remindInfoList = new ArrayList<>();
//            for (int i = 0; i < remindArray.length(); i++) {
//                JSONObject remindObject = JsonUtils.getJsonObject(remindArray, i);
//                IndexDataModel.RemindInfo remindInfo = new IndexDataModel.RemindInfo();
//                remindInfo.setId(JsonUtils.getString(remindObject, "id"));
//                remindInfo.setContent(JsonUtils.getString(remindObject, "content"));
//                remindInfo.setRemindType(JsonUtils.getString(remindObject, "serviceType"));
//
//                remindInfoList.add(remindInfo);
//            }
//            model.setRemindInfoList(remindInfoList);
//        }
//
//        list.add(model);
//
//    }
//
//
//    /**
//     * 解析今日贴士
//     *
//     * @param list       源数据集
//     * @param jsonObject json
//     */
//    private void resolveTips(List<IndexDataModel> list, JSONObject jsonObject) {
//        JSONArray array = JsonUtils.getJsonArray(jsonObject, "listNew");
//        if (array.length() == 0) {
//            return;
//        }
//        IndexDataModel tipModel = new IndexDataModel(IndexTypes.TYPE_HEADER_TIPS_TITLE);
//        tipModel.setTitle("今日贴士");
//        list.add(tipModel);
//        IndexDataModel model = new IndexDataModel(IndexTypes.TYPE_HEADER_TIPS);
//        List<IndexDataModel.TodayKnowledge> knowledgeList = new ArrayList<>();
//        for (int i = 0; i < array.length(); i++) {
//            JSONObject object = JsonUtils.getJsonObject(array, i);
//            IndexDataModel.TodayKnowledge knowledge = new IndexDataModel.TodayKnowledge();
//            knowledge.setId(JsonUtils.getString(object, "id"));
//            knowledge.setTitle(JsonUtils.getString(object, "title"));
//            knowledge.setCoverUrl(JsonUtils.getString(object, "images"));
//            knowledge.setUrlPrefix(JsonUtils.getString(object, "knowledgeUrl"));
//            knowledgeList.add(knowledge);
//        }
//        model.setKnowledgeList(knowledgeList);
//        list.add(model);
//    }
//
//
//    /**
//     * 解析推荐阅读
//     *
//     * @param list       源数据集
//     * @param jsonObject json
//     */
//    private void resolveRecommend(List<IndexDataModel> list, JSONObject jsonObject) {
//        JSONArray array = JsonUtils.getJsonArray(jsonObject, "informationInfoList");
//        if (array.length() == 0) {
//            return;
//        }
//        IndexDataModel titleModel = new IndexDataModel(IndexTypes.TYPE_HEADER_RECOMMEND_TITLE);
//        titleModel.setTitle("憨妈妈推荐");
//        list.add(titleModel);
//        List<IndexDataModel> recommendList = new ArrayList<>();
//        for (int i = 0; i < array.length(); i++) {
//            JSONObject object = JsonUtils.getJsonObject(array, i);
//            IndexDataModel model = new IndexDataModel(IndexTypes.TYPE_HEADER_RECOMMEND);
//            model.setArticleTitle(JsonUtils.getString(object, "title"));
//            model.setArticleId(JsonUtils.getString(object, "id"));
//            model.setArticleReadNum(JsonUtils.getInt(object, "readQuantity") + "");
//            model.setArticleImgUrl(JsonUtils.getString(object, "images"));
//            recommendList.add(model);
//        }
//        list.addAll(recommendList);
//    }
//
//    /**
//     * 解析用户信息数据
//     *
//     * @param json json
//     * @return 用户信息
//     */
//    private UserInfoModel resolveUserInfo(String json) {
//        UserInfoModel model = new UserInfoModel();
//        try {
//            JSONObject jsonObject = new JSONObject(json);
//            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
//            JSONObject userInfoJson = JsonUtils.getJsonObject(jsonObject, "memberInfoResult");
//            model.setNickname(JsonUtils.getString(userInfoJson, "nickName"));
//            model.setAvatarUrl(JsonUtils.getString(userInfoJson, "faceUrl"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return model;
//    }
//
//    /**
//     * 保存H5页面地址
//     *
//     * @param json json
//     */
//    private void saveUrl(String json) {
//        try {
//            JSONObject jsonObject = new JSONObject(json);
//            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
//            JSONObject urlJson = JsonUtils.getJsonObject(jsonObject, "helpResult");
//
//
//            SpUtils.store(mContext, UrlKeys.H5_KNOWLEDGE,
//                    JsonUtils.getString(urlJson, UrlKeys.H5_KNOWLEDGE));
//
//            SpUtils.store(mContext, UrlKeys.H5_CAN_EAT,
//                    JsonUtils.getString(urlJson, UrlKeys.H5_CAN_EAT));
//
//            SpUtils.store(mContext, UrlKeys.H5_CAN_EAT_LIST,
//                    JsonUtils.getString(urlJson, UrlKeys.H5_CAN_EAT_LIST));
//
//            SpUtils.store(mContext, UrlKeys.H5_VACCINE_DETAIL,
//                    JsonUtils.getString(urlJson, UrlKeys.H5_VACCINE_DETAIL));
//
//            SpUtils.store(mContext, UrlKeys.H5_B,
//                    JsonUtils.getString(urlJson, UrlKeys.H5_B));
//
//            SpUtils.store(mContext, UrlKeys.H5_MEAL,
//                    JsonUtils.getString(urlJson, UrlKeys.H5_MEAL));
//
//            SpUtils.store(mContext, UrlKeys.H5_FOOD_LIST,
//                    JsonUtils.getString(urlJson, UrlKeys.H5_FOOD_LIST));
//
//            SpUtils.store(mContext, UrlKeys.H5_FOOD_DETAIL,
//                    JsonUtils.getString(urlJson, UrlKeys.H5_FOOD_DETAIL));
//
//            SpUtils.store(mContext, UrlKeys.H5_FOOD_LEARN,
//                    JsonUtils.getString(urlJson, UrlKeys.H5_FOOD_LEARN));
//
//            SpUtils.store(mContext, UrlKeys.H5_TABLE_DETAIL,
//                    JsonUtils.getString(urlJson, UrlKeys.H5_TABLE_DETAIL));
//
//            SpUtils.store(mContext, UrlKeys.H5_CAN_EAT_ALL,
//                    JsonUtils.getString(urlJson, UrlKeys.H5_CAN_EAT_ALL));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}