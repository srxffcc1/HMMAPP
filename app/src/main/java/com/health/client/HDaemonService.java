package com.health.client;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.health.client.activity.MainActivity;

public class HDaemonService  extends Service {

    private static final String TAG = HDaemonService.class.getSimpleName();

    /**
     * 标记服务是否启动
     */
    public static boolean serviceIsLive = false;

    /**
     * 唯一前台通知ID
     */
    private static final int NOTIFICATION_ID = 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        // 获取服务通知
        Notification notification = createForegroundNotification();
        //将服务置于启动状态 ,NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIFICATION_ID, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        // 标记服务启动
        HDaemonService.serviceIsLive = true;
        // 数据获取
//        String data = intent.getStringExtra("Foreground");
//        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        // 标记服务关闭
        HDaemonService.serviceIsLive = false;
        // 移除通知
        stopForeground(true);
        super.onDestroy();
    }

    /**
     * 创建服务通知
     */
    private Notification createForegroundNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 唯一的通知通道的id.
        String notificationChannelId = "notification_channel_id_01";

        // Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //用户可见的通道名称
            String channelName = "持续服务憨妈妈";
            //通道的重要程度
            int importance = NotificationManager.IMPORTANCE_NONE;//此属性 会导致 这个通知会不会提示你 如果修改会出现 直接提示到前台
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, channelName, importance);
            notificationChannel.setDescription("持续服务");
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            //LED灯
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //震动
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannelId);
        //通知小图标
        builder.setSmallIcon(R.mipmap.app_ic_launcher);
        //通知标题
        builder.setContentTitle("憨妈妈正持续为您提供服务");
        //通知内容
        builder.setContentText("为保证您能及时收到新消息，请勿移除");
        //设定通知显示的时间
        builder.setWhen(System.currentTimeMillis());
        //设定启动的内容
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        //创建通知并返回
        return builder.build();
    }
}
