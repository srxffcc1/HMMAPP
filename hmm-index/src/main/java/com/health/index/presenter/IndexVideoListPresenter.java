//package com.health.index.presenter;
//
//import android.content.Context;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParseException;
//import com.google.gson.reflect.TypeToken;
//import com.health.index.contract.IndexVideoContract;
//import com.health.index.model.IndexVideo;
//import com.healthy.library.net.ObservableHelper;
//import com.healthy.library.net.StringObserver;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author Li
// * @date 2019/04/04 10:41
// * @des
// */
//public class IndexVideoListPresenter implements IndexVideoContract.Presenter {
//
//    private Context mContext;
//    private IndexVideoContract.View mView;
//
//    public IndexVideoListPresenter(Context context, IndexVideoContract.View view) {
//        mContext = context;
//        mView = view;
//    }
//
//    @Override
//    public void getVideoList(Map<String, Object> map) {
//        ObservableHelper.createObservable(mContext, map)
//                .subscribe(new StringObserver(mView, mContext, false,
//                        false, false, false) {
//                    @Override
//                    protected void onGetResultSuccess(String obj) {
//                        super.onGetResultSuccess(obj);
//                        mView.getVideoSuccess(resolveListData(obj));
//                    }
//
//                    @Override
//                    protected void onFinish() {
//                        super.onFinish();
//                        mView.onRequestFinish();
//                    }
//                });
//    }
//    private List<IndexVideo> resolveListData(String obj) {
//        List<IndexVideo> result = new ArrayList<>();
//        try {
//            JSONArray data=new JSONObject(obj).getJSONArray("data");
//            String userShopInfoDTOS=data.toString();
//            GsonBuilder builder = new GsonBuilder();
//            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                    return new Date(json.getAsJsonPrimitive().getAsLong());
//                }
//            });
//            Gson gson = builder.create();
//            Type type = new TypeToken<List<IndexVideo>>() {
//            }.getType();
//            result=gson.fromJson(userShopInfoDTOS,type);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//}