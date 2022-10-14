package com.health.index.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.contract.WebContract;
import com.health.index.model.CommentModel;
import com.health.index.model.IndexVideoOnline;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.JsonUtils;
import com.healthy.library.utils.SpUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/29 16:19
 * @des
 */

public class WebPresenter implements WebContract.Presenter {

    private Context mContext;
    private WebContract.View mView;

    public WebPresenter(Context context, WebContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void updateCollectedStatus(String knowledgeId, String collectStatus, String collectId) {
        Map<String, Object> map = new HashMap<>(4);
        map.put(Functions.FUNCTION, Functions.FUNCTION_UPDATE_COLLECTED_STATUS);
        map.put("knowledgeId", knowledgeId);
        map.put("isCollect", collectStatus);
        map.put("collectId", collectId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, true, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            mView.onUpdateCollectedStatusSuccess(
                                    JsonUtils.getString(jsonObject, "data"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void checkCollectedStatus(String knowledgeId) {
        //System.out.println("请求收藏");
        if (TextUtils.isEmpty(SpUtils.getValue(mContext, SpKey.TOKEN))) {
            return;
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put(Functions.FUNCTION, Functions.FUNCTION_CHECK_COLLECTED_STATUS);
        map.put("knowledgeId", knowledgeId);
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false, false,
                        false, false, false, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            jsonObject = JsonUtils.getJsonObject(jsonObject, "data");
                            mView.onCheckCollectedStatusSuccess(
                                    JsonUtils.getString(jsonObject, "isCollect"),
                                    JsonUtils.getString(jsonObject, "collectId"),
                                    JsonUtils.getString(jsonObject, "title"),
                                    JsonUtils.getString(jsonObject, "content"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void getRealVideoUrl(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "9002");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            mView.onGetRealUrlSuccess(
                                    JsonUtils.getString(jsonObject, "data"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void addComment(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "KD_10000");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            mView.onAddCommentSuccess(
                                    JsonUtils.getString(jsonObject, "msg"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void getCommentList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "KD_10001");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false,false,false,false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.getCommentListSuccess(resolveCommentListData(obj), resolveRefreshData(obj));
                    }

                    @Override
                    protected void onFailure(String msg) {
                        super.onFailure(msg);
                        mView.getCommentListSuccess(null, null);
                    }
                });
    }

    @Override
    public void addPraise(Map<String, Object> map, final String type) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            if (type.equals("0")) {
                                mView.showToast("取消赞成功");
                            } else {
                                mView.showToast("点赞成功");
                            }
                            JSONObject jsonObject = new JSONObject(obj);
                            mView.onAddPraiseSuccess(JsonUtils.getString(jsonObject, "msg"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void addReply(Map<String, Object> map) {
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new StringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject = new JSONObject(obj);
                            mView.onAddReplySuccess(JsonUtils.getString(jsonObject, "msg"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private List<CommentModel> resolveCommentListData(String obj) {
        List<CommentModel> result = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(obj);
            String userShopInfoDTOS = data.getJSONObject("data").getJSONArray("list").toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<CommentModel>>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private PageInfoEarly resolveRefreshData(String obj) {
        PageInfoEarly result = new PageInfoEarly();
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<PageInfoEarly>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
//            e.printStackTrace();
        }

        return result;
    }
}
