package com.health.client.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.client.R;
import com.health.client.contract.MessageContract;
import com.healthy.library.model.MonMessage;
import com.healthy.library.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Li
 * @date 2019/03/27 11:37
 * @des
 */

public class MessagePresenter implements MessageContract.Presenter {

    private Context mContext;
    private MessageContract.View mView;

    public MessagePresenter(Context context, MessageContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void getMessage() {
        mView.showLoading();
        List<MonMessage> messagelist=new ArrayList<>();
        List<MonMessage> messagelistresult=new ArrayList<>();
        {

            MonMessage messagemysczs = resolveMessageData(SpUtils.getValue(mContext, "母婴商城小助手"));
            messagemysczs.type = "母婴服务小助手";
            messagemysczs.itemType = 2;
            messagemysczs.name = "交易通知";
            messagemysczs.imageRes = R.drawable.message_type3;
            messagemysczs.needShow=false;
            messagelist.add(messagemysczs);

            MonMessage messagemyfwzs = resolveMessageData(SpUtils.getValue(mContext, "母婴服务小助手"));
            messagemyfwzs.type = "母婴服务小助手";
            messagemyfwzs.itemType = 4;
            messagemyfwzs.name = "交易通知";
            messagemyfwzs.imageRes = R.drawable.message_type5;
//            messagemyfwzs.newmessagecontent="测试";
//            messagemyfwzs.num=10;
            messagelist.add(messagemyfwzs);


            MonMessage messagemyhd = resolveMessageData(SpUtils.getValue(mContext, "活动优惠"));
            messagemyhd.type = "活动优惠";
            messagemyhd.itemType = 4;
            messagemyhd.imageRes = R.drawable.message_type8;
            messagemyhd.needShow=false;
            messagelist.add(messagemyhd);
        }
        {
            MonMessage messagetchmzs = resolveMessageData(SpUtils.getValue(mContext, "同城圈小助手"));
            messagetchmzs.type = "同城圈小助手";
            messagetchmzs.itemType = 2;
            messagetchmzs.name = "同城圈";
            messagetchmzs.imageRes = R.drawable.message_type4;
            messagelist.add(messagetchmzs);
            MonMessage messagepl = resolveMessageData(SpUtils.getValue(mContext, "评论"));
            messagepl.type = "评论";
            messagepl.itemType = 1;
            messagepl.name = "收到的评论及回复";
            messagepl.imageRes = R.drawable.message_type1;
            messagelist.add(messagepl);
            MonMessage messagez = resolveMessageData(SpUtils.getValue(mContext, "赞"));
            messagez.type = "赞";
            messagez.itemType = 1;
            messagez.name = "收到的赞";
            messagez.imageRes = R.drawable.message_type2;
            messagelist.add(messagez);
        }

        {
            MonMessage messagetz = resolveMessageData(SpUtils.getValue(mContext, "通知"));
            messagetz.type = "通知";
            messagetz.itemType = 4;
            messagetz.name = "系统通知";
            messagetz.imageRes = R.drawable.message_type7;
            messagelist.add(messagetz);
            MonMessage messagewdzs = resolveMessageData(SpUtils.getValue(mContext, "问答小助手"));
            messagewdzs.type = "问答小助手";
            messagewdzs.itemType = 4;
            messagewdzs.name = "专家答疑";
            messagewdzs.imageRes = R.drawable.message_type6;
            messagelist.add(messagewdzs);
        }

        for (int i = 0; i <messagelist.size() ; i++) {
            MonMessage tmpmessage=messagelist.get(i);
            messagelistresult.add(tmpmessage);
        }


        mView.showContent();
        mView.onRequestFinish();
        mView.onSuccessGetMessage(messagelistresult);
    }

    @Override
    public void getMessageSepcial() {//用来获得另一种布局

        mView.showLoading();
        List<MonMessage> messagelist=new ArrayList<>();
        {

//            MonMessage messagemysczs = resolveMessageData(SpUtils.getValue(mContext, "母婴商城小助手"));
//            messagemysczs.type = "母婴商城小助手";
//            messagemysczs.itemType = 3;
//            messagemysczs.name = "交易通知";
//            messagemysczs.imageRes = R.drawable.message_type5;
//            messagelist.add(messagemysczs);

            MonMessage messagemyfwzs = resolveMessageData(SpUtils.getValue(mContext, "母婴服务小助手"));
            messagemyfwzs.type = "母婴服务小助手";
            messagemyfwzs.itemType = 3;
            messagemyfwzs.name = "交易通知";
            messagemyfwzs.imageRes = R.drawable.message_type5;
            messagelist.add(messagemyfwzs);


//            MonMessage messagemyhd = resolveMessageData(SpUtils.getValue(mContext, "活动优惠"));
//            messagemyhd.type = "活动优惠";
//            messagemyhd.itemType = 3;
//            messagemyhd.imageRes = R.drawable.message_type8;
//            messagelist.add(messagemyhd);
        }
        {
            MonMessage messagetchmzs = resolveMessageData(SpUtils.getValue(mContext, "同城圈小助手"));
            messagetchmzs.type = "同城圈小助手";
            messagetchmzs.itemType = 3;
            messagetchmzs.name = "同城圈";
            messagetchmzs.imageRes = R.drawable.message_type4;
            messagelist.add(messagetchmzs);
            MonMessage messagepl = resolveMessageData(SpUtils.getValue(mContext, "评论"));
            messagepl.type = "评论";
            messagepl.itemType = 3;
            messagepl.name = "收到的评论及回复";
            messagepl.imageRes = R.drawable.message_type1;
            messagelist.add(messagepl);
            MonMessage messagez = resolveMessageData(SpUtils.getValue(mContext, "赞"));
            messagez.type = "赞";
            messagez.itemType = 3;
            messagez.name = "收到的赞";
            messagez.imageRes = R.drawable.message_type2;
            messagelist.add(messagez);
        }

        {
//            MonMessage messagetz = resolveMessageData(SpUtils.getValue(mContext, "通知"));
//            messagetz.type = "通知";
//            messagetz.itemType = 3;
//            messagetz.name = "系统通知";
//            messagetz.imageRes = R.drawable.message_type7;
//            messagelist.add(messagetz);
            MonMessage messagewdzs = resolveMessageData(SpUtils.getValue(mContext, "问答小助手"));
            messagewdzs.type = "问答小助手";
            messagewdzs.itemType = 3;
            messagewdzs.name = "专家答疑";
            messagewdzs.imageRes = R.drawable.message_type6;
            messagelist.add(messagewdzs);


            MonMessage messagegg= resolveMessageData(SpUtils.getValue(mContext, "广告"));
            messagegg.type = "广告";
            messagegg.itemType = 3;
            messagegg.name = "个性化推送";
            messagegg.imageRes = R.drawable.message_type7;
            messagelist.add(messagegg);
        }
        mView.showContent();
        mView.onRequestFinish();
        mView.onSuccessGetMessage(messagelist);
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
//            e.printStackTrace();
        }
        return result;
    }
}