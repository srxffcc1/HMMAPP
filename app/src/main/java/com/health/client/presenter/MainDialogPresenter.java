package com.health.client.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.discount.R;
import com.health.discount.contract.MainDialogContract;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.Coupon;
import com.healthy.library.model.MonMessage;
import com.healthy.library.model.ShareEntity;
import com.healthy.library.model.UpdatePatchVersion;
import com.healthy.library.model.UpdateVersion;
import com.healthy.library.net.NoInsertStringObserver;
import com.healthy.library.net.NoStringObserver;
import com.healthy.library.net.ObservableHelper;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.net.StringObserver;
import com.healthy.library.utils.NotificationUtil;
import com.healthy.library.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainDialogPresenter implements MainDialogContract.Presenter{
    private Context mContext;
    private MainDialogContract.View mView;

    public MainDialogPresenter(Context context, MainDialogContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getCouponList(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7040");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSuccessGetCouponList(resolveCouponListData(obj),new PageInfoEarly());
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
    private UpdateVersion resolveUpdate(String obj) {
        UpdateVersion result = null;
        try {
            JSONObject data=new JSONObject(obj).getJSONObject("data").getJSONObject("updateSetup");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<UpdateVersion>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private UpdatePatchVersion resolveUpdatePatch(String obj) {
        UpdatePatchVersion result = null;
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
            Type type = new TypeToken<UpdatePatchVersion>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void checkCouponGift(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "80020");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        try {
                            JSONObject jsonObject=new JSONObject(obj);
                            mView.onSucessGiftCheck(jsonObject.getInt("data"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    protected void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGiftCheck(-1);
                        checkMessageDialog();

                    }
                });
    }

    @Override
    public void checkCouponGiftHasCard(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "80019");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGiftCheckHasCard(resolveCardListData(obj));
                    }

                    @Override
                    protected void onFinish() {
                        super.onFinish();
                        mView.onRequestFinish();
                    }

                    @Override
                    protected void onFailure() {
                        super.onFailure();
                        mView.onSucessGiftCheckHasCard(null);
                    }
                });
    }
    private List<Coupon> resolveCardListData(String obj) {
        List<Coupon> result =new ArrayList<Coupon>();
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
            Type type = new TypeToken<List<Coupon>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void checkVersion(Map<String, Object> map) {
        //System.out.println("检测需要更新");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessCheckVersion(resolveUpdate(obj));

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
    public void getAppProgram(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "mini_program_9800");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessGetAppProgram(resolveShareData(obj));

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
    private ShareEntity resolveShareData(String obj) {
        ShareEntity result = null;
        try {
            JSONObject org=new JSONObject(obj);
            JSONObject data=new JSONObject(org.getString("data"));
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<ShareEntity>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private MonMessage resolveMessageData(String obj) {
        MonMessage result = new MonMessage();
        try {
            JSONObject data=new JSONObject(obj);
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<MonMessage>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public List<MonMessage> getMessageNowList(){
        List<MonMessage> messagelist=new ArrayList<>();
        MonMessage messagepl= resolveMessageData(SpUtils.getValue(mContext,"评论"));
        messagepl.type="评论";
        messagepl.itemType=1;
        messagepl.imageRes= R.drawable.message_type1;
        messagelist.add(messagepl);
        MonMessage messagez= resolveMessageData(SpUtils.getValue(mContext,"赞"));
        messagez.type="赞";
        messagez.itemType=1;
        messagez.imageRes= R.drawable.message_type2;
        messagelist.add(messagez);
        MonMessage messagemyfwzs= resolveMessageData(SpUtils.getValue(mContext,"母婴服务小助手"));
        messagemyfwzs.type="母婴服务小助手";
        messagemyfwzs.itemType=2;
        messagemyfwzs.imageRes= R.drawable.message_type3;
        messagelist.add(messagemyfwzs);
        MonMessage messagetchmzs= resolveMessageData(SpUtils.getValue(mContext,"同城圈小助手"));
        messagetchmzs.type="同城圈小助手";
        messagetchmzs.itemType=2;
        messagetchmzs.imageRes= R.drawable.message_type4;
        messagelist.add(messagetchmzs);
        MonMessage messagemysczs= resolveMessageData(SpUtils.getValue(mContext,"母婴商城小助手"));
        messagemysczs.type="母婴商城小助手";
        messagemysczs.itemType=2;
        messagemysczs.imageRes= R.drawable.message_type5;
        messagelist.add(messagemysczs);
        MonMessage messagewdzs= resolveMessageData(SpUtils.getValue(mContext,"问答小助手"));
        messagewdzs.type="问答小助手";
        messagewdzs.itemType=2;
        messagewdzs.imageRes= R.drawable.message_type6;
        messagelist.add(messagewdzs);
        MonMessage messagetz= resolveMessageData(SpUtils.getValue(mContext,"通知"));
        messagetz.type="通知";
        messagetz.itemType=2;
        messagetz.imageRes= R.drawable.message_type7;
        messagelist.add(messagetz);
        return messagelist;
    }
    @Override
    public void checkMessageDialog() {
//        List<MonMessage> messagelist=getMessageNowList();
//        int result=0;
//        for (int i = 0; i <messagelist.size() ; i++) {
//            MonMessage tmp=messagelist.get(i);
//            result=result+(tmp.istoast?1:0);
//        }
//        if(result==0){
//            if(new SimpleDateFormat("yyyy-MM-dd").format(new Date()).equals(SpUtils.getValue(mContext, SpKey.MESSAGE_KEY))){//判断是否手动关闭了 等于说明今天已经提醒过了
//                mView.onSucessMessageCheck(true);
//            }else {
//                mView.onSucessMessageCheck(false);
//            }
//
//        }else {
//            mView.onSucessMessageCheck(true);
//        }
        //System.out.println("查看推送权限："+NotificationUtil.isNotificationEnabled(mContext)); //只用查看权限
        if(!NotificationUtil.isNotificationEnabled(mContext)){
            if(new SimpleDateFormat("yyyy-MM-dd").format(new Date()).equals(SpUtils.getValue(mContext, SpKey.MESSAGE_KEY))){//判断是否手动关闭了 等于说明今天已经提醒过了
                mView.onSucessMessageCheck(true);
            }else {
                mView.onSucessMessageCheck(false);
                SpUtils.store(mContext,SpKey.MESSAGE_KEY,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            }
        }else {
            mView.onSucessMessageCheck(true);
        }



    }

    @Override
    public void updateGift(Map<String, Object> map) {
        map.put(Functions.FUNCTION, "7041");
        ObservableHelper.createObservable(mContext, map)
                .subscribe(new NoInsertStringObserver(mView, mContext, false) {
                    @Override
                    protected void onGetResultSuccess(String obj) {
                        super.onGetResultSuccess(obj);
                        mView.onSucessUpdateGift(null);
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

    private List<Coupon> resolveCouponListData(String obj) {
        List<Coupon> result = new ArrayList<>();
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
            Type type = new TypeToken<List<Coupon>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
