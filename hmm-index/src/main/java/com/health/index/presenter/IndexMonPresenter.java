package com.health.index.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.contract.IndexMonContract;
import com.health.index.model.IndexAllChat2;
import com.healthy.library.model.IndexAllQuestion;
import com.health.index.model.IndexAllSee;
import com.health.index.model.IndexBean;
import com.health.index.model.IndexVideo;
import com.health.index.model.IndexVideoOnline;
import com.health.index.model.UserInfoExModel;
import com.healthy.library.model.UserInfoMonModel;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.utils.JsonUtils;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/04 10:41
 * @des
 */
public class IndexMonPresenter implements IndexMonContract.Presenter {

    private Context mContext;
    private IndexMonContract.View mView;

    public IndexMonPresenter(Context context, IndexMonContract.View view) {
        mContext = context;
        mView = view;
    }
    @Override
    public void warn(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7017");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
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
    @Override
    public void getH5() {
//        Map<String, Object> map = new HashMap<>();
//        map.put(Functions.FUNCTION, "4033");
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        saveUrl(obj);
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                    }
//
//                    @Override
//                    protected void onFailure() {
//                        super.onFailure();
//
//                    }
//                });
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getIndexMain(String queryDate,String cityNo,String areaNo,String longitude,String latitude,String isCurrentCity) {
        //System.out.println("请求");
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "4002");
        try {
            if(TextUtils.isEmpty(areaNo)){
                areaNo="0";
            }
            map.put("cityNo", (Integer.parseInt(areaNo)/100 * 100)+"");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        map.put("areaNo", areaNo);
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("queryDate", queryDate);
        map.put("isCurrentCity", isCurrentCity);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getIndexSuccess(resolveIndexData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.getIndexSuccess(null);
                    }
                });

    }

    private IndexBean resolveIndexData(String obj) {
        IndexBean result = null;
        try {
            JSONObject data=new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<IndexBean>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void changeStatus(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "1015");
        map.put("id", id);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.changeStatusSuccess();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getMine() {
        Map<String, Object> map = new HashMap<>(3);
        map.put(Functions.FUNCTION, "1011");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getMineSuccess(resolveMineData(obj));
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.getMineSuccess(null);
                    }
                });
    }

    @Override
    public void getAllVideo() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, "9000");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getAllVideoSuccess(resolveVideoData(obj),resolveRefreshData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void getAllVideoOnline() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, "8063");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getAllVideoOnlineSuccess(resolveVideoOnlineData(obj),resolveRefreshData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void subVideo(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9001");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        Toast.makeText(mContext,"预约成功！提前10分钟消息提醒",Toast.LENGTH_SHORT).show();
                        mView.subVideoSucess();
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private UserInfoMonModel resolveMineData(String obj) {
        UserInfoMonModel result = null;
        try {
            JSONObject data=new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<UserInfoMonModel>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void getAllStatus() {
        Map<String, Object> map = new HashMap<>(1);
        map.put(Functions.FUNCTION, "1013");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,
                        false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getAllStatusSuccess(resolveListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private List<UserInfoExModel> resolveListData(String obj) {
        List<UserInfoExModel> result = new ArrayList<>();
        try {
            JSONArray data=new JSONObject(obj).getJSONArray("data");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<UserInfoExModel>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    private PageInfoEarly resolveRefreshData(String obj) {
        PageInfoEarly result = new PageInfoEarly();
        try {
            JSONObject data=new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<PageInfoEarly>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        return result;
    }
    @Override
    public void getAllChat(String currentPage) {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "7023");
        map.put("currentPage", currentPage+"");
        map.put("pageSize", 4+"");
        try {
            String areaNo= LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE);
            if(TextUtils.isEmpty(areaNo)){
                areaNo="0";
            }
            map.put("addressCity",(Integer.parseInt(areaNo) / 100 * 100) + "");
            map.put("addressProvince",(Integer.parseInt(areaNo) / 10000 * 10000) + "");
            map.put("addressArea",areaNo + "");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getAllChatSuccess(resolveChatData(obj),resolveRefreshData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }
    private List<IndexVideoOnline> resolveVideoOnlineData(String obj) {
        List<IndexVideoOnline> result = new ArrayList<>();
        try {
            JSONObject data=new JSONObject(obj);
            String userShopInfoDTOS=data.getJSONArray("data").toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<IndexVideoOnline>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    private List<IndexVideo> resolveVideoData(String obj) {
        List<IndexVideo> result = new ArrayList<>();
        try {
            JSONObject data=new JSONObject(obj);
            String userShopInfoDTOS=data.getJSONArray("data").toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<IndexVideo>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<IndexAllChat2> resolveChatData(String obj) {
        List<IndexAllChat2> result = new ArrayList<>();
        try {
            JSONObject data=new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS=data.getJSONArray("items").toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<IndexAllChat2>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getAllQuestion(String currentPage) {
        Map<String, Object> map = new HashMap<>();


        map.put(Functions.FUNCTION, "4035");
        map.put("currentPage", currentPage+"");
        map.put("pageSize", 3+"");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getAllQuestionSuccess(resolveQuestionData(obj),resolveRefreshData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    private List<IndexAllQuestion> resolveQuestionData(String obj) {
        List<IndexAllQuestion> result = new ArrayList<>();
        try {
            JSONObject data=new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS=data.getJSONArray("items").toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<IndexAllQuestion>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void getAllSee(String currentPage, String queryDate) {
        Map<String, Object> map = new HashMap<>();
        map.put(Functions.FUNCTION, "4034");


        try {
            String areaNo= LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE);
            if(TextUtils.isEmpty(areaNo)){
                areaNo="0";
            }
            map.put("addressCity",(Integer.parseInt(areaNo) / 100 * 100) + "");
            map.put("addressProvince",(Integer.parseInt(areaNo) / 10000 * 10000) + "");
            map.put("addressArea",areaNo + "");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        map.put("currentPage", currentPage+"");
        map.put("pageSize", 5+"");
        map.put("queryDate", queryDate);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getAllSeeSuccess(resolveSeeData(obj),resolveRefreshData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }
                });
    }

    @Override
    public void like(final Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7003");
        map.put("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC));
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        if("0".equals(map.get("type"))){
                            Toast.makeText(mContext,"点赞成功",Toast.LENGTH_SHORT).show();
                        }
                        mView.onSuccessLike();
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

    private List<IndexAllSee> resolveSeeData(String obj) {
        List<IndexAllSee> result = new ArrayList<>();
        try {
            JSONObject data=new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS=data.getJSONArray("items").toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<IndexAllSee>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}