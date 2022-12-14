package com.health.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.client.activity.MainActivity;
import com.health.client.activity.WelcomeActivity;
import com.health.client.model.PushMessageBean;
import com.healthy.library.constant.SpKey;
import com.healthy.library.message.CouponShowBean;
import com.healthy.library.message.UpdateMessageInfo;
import com.healthy.library.message.UpdateOrderByInfo;
import com.healthy.library.message.UpdatePatchHasMsg;
import com.healthy.library.model.MonMessage;
import com.healthy.library.presenter.DeleteJiGuangPresenter;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.NotificationUtils;
import com.healthy.library.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

//import cn.jiguang.plugins.push.receiver.JPushModuleReceiver;

public class JCopyReceiver extends JPushMessageReceiver {

    private static final String TAG = "PushMessageReceiver";

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.e(TAG, "[onMessage] " + customMessage);
        super.onMessage(context, customMessage);
        //????????????????????????
        //?????????????????????
        //??????????????????SP//
//        processCustomMessage(context,customMessage);
        buildNotifyCustom(context, customMessage);
        EventBus.getDefault().post(new UpdateMessageInfo());
    }

    @Override
    public void onNotifyMessageOpened(Context mContext, NotificationMessage message) {
        //Log.e(TAG, "[onNotifyMessageOpened] " + message);
        super.onNotifyMessageOpened(mContext, message);
        openTipIntent(mContext, message);
    }

    private void openTipIntent(Context mContext, NotificationMessage message) {
        String extra = message.notificationExtras;
        try {
            JSONObject jsonObject = new JSONObject(extra);
            if (jsonObject.has("course_id")) {
                try {

//            ??????????????????Activity
                    Intent intent = new Intent(mContext, MainActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, message.notificationTitle);
                    bundle.putString(JPushInterface.EXTRA_ALERT, message.notificationContent);
                    intent.putExtras(bundle);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("ispush", true);
                    intent.putExtra("pushIndex", 71);
                    intent.putExtra("url", jsonObject.getString("url"));
                    mContext.startActivity(intent);
                } catch (Throwable throwable) {

                }
            } else {
                try {
                    String id = SpUtils.getValue(mContext, SpKey.USER_ID);
                    if (TextUtils.isEmpty(id)) { //?????????
                        return;
                    }
//            ??????????????????Activity
                    Intent intent = new Intent(mContext, MainActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, message.notificationTitle);
                    bundle.putString(JPushInterface.EXTRA_ALERT, message.notificationContent);
                    bundle.putString(JPushInterface.EXTRA_EXTRA, extra);
                    intent.putExtras(bundle);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("ispush", true);
                    intent.putExtra("pushIndex", 7);
                    mContext.startActivity(intent);
                } catch (Throwable throwable) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        super.onMultiActionClicked(context, intent);
//        Log.e(TAG, "[onMultiActionClicked] ??????????????????????????????");
        String nActionExtra = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);

        //????????????????????? Action ????????? extra ?????????????????????????????????
        if (nActionExtra == null) {
//            Log.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null");
            return;
        }
//        if ("my_extra1".equals(nActionExtra)) {
//            Log.e(TAG, "[onMultiActionClicked] ??????????????????????????????");
//        } else if ("my_extra2".equals(nActionExtra)) {
//            Log.e(TAG, "[onMultiActionClicked] ??????????????????????????????");
//        } else if ("my_extra3".equals(nActionExtra)) {
//            Log.e(TAG, "[onMultiActionClicked] ??????????????????????????????");
//        } else {
//            Log.e(TAG, "[onMultiActionClicked] ????????????????????????????????????");
//        }
    }

    @Override
    public void onNotifyMessageArrived(Context mContext, NotificationMessage message) {
        super.onNotifyMessageArrived(mContext, message);
        Log.e(TAG, "[onNotifyMessageArrived] " + message);
        //???????????????????????????
        buildNotify(mContext, message);
        EventBus.getDefault().post(new UpdateMessageInfo());

    }

    private void buildNotifyCustom(Context mContext, CustomMessage customMessage) {

        List<MonMessage> messagelist = new ArrayList<>();
        MonMessage messagepl = resolveMessageData(SpUtils.getValue(mContext, "??????"));
        messagepl.type = "??????";
        messagepl.itemType = 1;
        messagepl.imageRes = R.drawable.message_type1;
        messagelist.add(messagepl);

        MonMessage messagez = resolveMessageData(SpUtils.getValue(mContext, "???"));
        messagez.type = "???";
        messagez.itemType = 1;
        messagez.imageRes = R.drawable.message_type2;
        messagelist.add(messagez);

        MonMessage messagemyfwzs = resolveMessageData(SpUtils.getValue(mContext, "?????????????????????"));
        messagemyfwzs.type = "?????????????????????";
        messagemyfwzs.itemType = 2;
        messagemyfwzs.imageRes = R.drawable.message_type3;
        messagelist.add(messagemyfwzs);

        MonMessage messagetchmzs = resolveMessageData(SpUtils.getValue(mContext, "??????????????????"));
        messagetchmzs.type = "??????????????????";
        messagetchmzs.itemType = 2;
        messagetchmzs.imageRes = R.drawable.message_type4;
        messagelist.add(messagetchmzs);

        MonMessage messagemysczs = resolveMessageData(SpUtils.getValue(mContext, "?????????????????????"));
        messagemysczs.type = "?????????????????????";
        messagemysczs.itemType = 2;
        messagemysczs.imageRes = R.drawable.message_type5;
        messagelist.add(messagemysczs);

        MonMessage messagewdzs = resolveMessageData(SpUtils.getValue(mContext, "???????????????"));
        messagewdzs.type = "???????????????";
        messagewdzs.itemType = 2;
        messagewdzs.imageRes = R.drawable.message_type6;
        messagelist.add(messagewdzs);

        MonMessage messagetz = resolveMessageData(SpUtils.getValue(mContext, "??????"));
        messagetz.type = "??????";
        messagetz.itemType = 2;
        messagetz.imageRes = R.drawable.message_type7;
        messagelist.add(messagetz);

        PushMessageBean pushMessageBean = resolvePushData(customMessage.extra);


        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("ispush", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int notifactiontype = -1;
        try {
            notifactiontype = Integer.parseInt(pushMessageBean.pushType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationUtils notificationUtils = new NotificationUtils(mContext, notifactiontype, R.mipmap.app_ic_launcher, customMessage.title.replace("?????????????????????", "????????????"), customMessage.message.replace("?????????????????????", "????????????"));
        //System.out.println("??????????????????");
        if (notifactiontype - 1 >= 0 && notifactiontype - 1 <= 7) {
            //System.out.println("???????????????????????????");
            MonMessage monMessageNow = null;
            try {
                monMessageNow = messagelist.get(notifactiontype - 1);
            } catch (Exception e) {
//                e.printStackTrace();
            }
            switch (notifactiontype) {
                case 1:
                    intent.putExtra("pushIndex", 1);
                    break;
                case 2:
                    intent.putExtra("pushIndex", 2);
                    break;
                case 3:
                    intent.putExtra("pushIndex", 3);
                    if (customMessage.message.contains("??????")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                EventBus.getDefault().post(new UpdateOrderByInfo());
                            }
                        }, 1000);
                    }
                    break;
                case 4:
                    intent.putExtra("pushIndex", 4);
                    break;
                case 5:
                    intent.putExtra("pushIndex", 5);
                    break;
                case 6:
                    intent.putExtra("pushIndex", 6);
                    break;
                case 7:
                    intent.putExtra("pushIndex", 7);
                    break;
                case 8:
                    intent.putExtra("pushIndex", 8);
                    //System.out.println("???????????????");
                    EventBus.getDefault().post(new CouponShowBean(pushMessageBean.id, pushMessageBean.extraId));
                    break;
            }
            if (monMessageNow != null) {
                if (monMessageNow.istoast) {//?????????????????????????????????
                    //Log.e(TAG, "[onNotifyMessageArrived] " + "???????????????");
                    notificationUtils.notify(intent);
                }
                monMessageNow.newmessagecontent = customMessage.message.replace("?????????????????????", "????????????");
                monMessageNow.num = monMessageNow.num + 1;
                monMessageNow.messageTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                SpUtils.store(mContext, monMessageNow.type, new Gson().toJson(monMessageNow));
            }
        } else {//??????????????? ????????????????????? //????????????
            MonMessage monMessageNow = resolveMessageData(SpUtils.getValue(mContext, "??????"));
            monMessageNow.type = "??????";
            monMessageNow.itemType = 2;
            monMessageNow.imageRes = R.drawable.message_type7;
            if (monMessageNow != null) {
                monMessageNow.newmessagecontent = customMessage.message.replace("?????????????????????", "????????????");
                monMessageNow.num = monMessageNow.num + 1;
                monMessageNow.messageTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                SpUtils.store(mContext, monMessageNow.type, new Gson().toJson(monMessageNow));
            }


            //System.out.println("???????????????????????????");
            if (notifactiontype == 9) {
                //System.out.println("???????????????");
                intent.putExtra("pushIndex", 9);
                intent.putExtra("messageData", customMessage.extra);
            } else if (notifactiontype == 10) {
                //System.out.println("??????????????????");
                intent.putExtra("pushIndex", 10);
                intent.putExtra("messageData", customMessage.extra);
            }else if(notifactiontype == 777){//????????? ??????????????????
                EventBus.getDefault().post(new UpdatePatchHasMsg());
            }else {
                intent.putExtra("pushIndex", -1);
            }
            intent.putExtra(JPushInterface.EXTRA_EXTRA, customMessage.extra);
            intent.putExtra(JPushInterface.EXTRA_NOTIFICATION_TITLE, customMessage.title);
            intent.putExtra(JPushInterface.EXTRA_ALERT, customMessage.message);
            notificationUtils.notify(intent);
        }
    }

    private void buildNotify(Context mContext, NotificationMessage message) {
        int notifactiontype = -1;
        try {
            PushMessageBean pushMessageBean = resolvePushData(message.notificationExtras);

            notifactiontype = Integer.parseInt(pushMessageBean.pushType);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (notifactiontype == -1 || notifactiontype == 7) {
            try {
                MonMessage messagetz = resolveMessageData(SpUtils.getValue(mContext, "??????"));
                messagetz.type = "??????";
                messagetz.itemType = 2;
                messagetz.imageRes = R.drawable.message_type7;
                MonMessage monMessageNow = messagetz;
                monMessageNow.newmessagecontent = message.notificationContent;
                monMessageNow.num = monMessageNow.num + 1;
                monMessageNow.messageTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                SpUtils.store(mContext, monMessageNow.type, new Gson().toJson(monMessageNow));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private PushMessageBean resolvePushData(String obj) {
        PushMessageBean result = new PushMessageBean();
        try {
            JSONObject data = new JSONObject(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<PushMessageBean>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private MonMessage resolveMessageData(String obj) {
        MonMessage result = new MonMessage();
        try {
            JSONObject data = new JSONObject(obj);
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<MonMessage>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        super.onNotifyMessageDismiss(context, message);
        //Log.e(TAG, "[onNotifyMessageDismiss] " + message);
    }

    @Override
    public void onRegister(Context context, String registrationId) {
        super.onRegister(context, registrationId);
        //Log.e(TAG, "[onRegister] " + registrationId);
    }

    @Override
    public void onConnected(Context context, boolean isConnected) {
        super.onConnected(context, isConnected);
        //Log.e(TAG, "[onConnected] " + isConnected);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        super.onCommandResult(context, cmdMessage);
        //Log.e(TAG, "[onCommandResult] " + cmdMessage);
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onTagOperatorResult(context, jPushMessage);
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onCheckTagOperatorResult(context, jPushMessage);
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);

        Log.e(TAG, "[onAliasOperatorResult] " + jPushMessage.getErrorCode() + ":????????????");
        if (jPushMessage.getErrorCode() == 6017 || jPushMessage.getErrorCode() == 6027) {
            new DeleteJiGuangPresenter(context).deleteJiGuang();
        }
        if (jPushMessage.getErrorCode() == 6002) {
            //?????? "6002" ???????????????????????????????????????
            String phone = SpUtils.getValue(context, SpKey.PHONE);
            int phoneed = 0;
            if (!TextUtils.isEmpty(phone)) {
                phoneed = Integer.parseInt(phone.substring(phone.length() - 4, phone.length()));
            }
            JPushInterface.setAlias(context,
                    phoneed,
                    new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        }
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onMobileNumberOperatorResult(context, jPushMessage);
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, CustomMessage customMessage) {
//        if (MainActivity.isForeground) {
//            String message = customMessage.message;
//            String extras = customMessage.extra;
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (Exception e) {
//
//                }
//
//            }
//            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//        }
    }

    @Override
    public void onNotificationSettingsCheck(Context context, boolean isOn, int source) {
        super.onNotificationSettingsCheck(context, isOn, source);
        //Log.e(TAG, "[onNotificationSettingsCheck] isOn:" + isOn + ",source:" + source);
    }

}
