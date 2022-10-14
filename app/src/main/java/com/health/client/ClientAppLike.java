//package com.health.client;
//
//import android.app.Activity;
//import android.app.Application;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Base64;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.webkit.WebView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//
//import androidx.emoji.bundled.BundledEmojiCompatConfig;
//import androidx.emoji.text.EmojiCompat;
//import androidx.multidex.MultiDex;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
//import com.chuanglan.shanyan_sdk.listener.InitListener;
//import com.health.sound.model.SoundHolder;
//import com.healthy.library.LibApplication;
//import com.healthy.library.businessutil.ChannelUtil;
//import com.healthy.library.constant.Functions;
//import com.healthy.library.constant.Ids;
//import com.healthy.library.constant.SpKey;
//import com.healthy.library.interfaces.IsMediaWindows;
//import com.healthy.library.net.NoInsertStringObserver;
//import com.healthy.library.net.ObservableHelper;
//import com.healthy.library.utils.FrameActivityManager;
//import com.healthy.library.utils.ResUtil;
//import com.healthy.library.utils.ScreenUtils;
//import com.healthy.library.utils.SpUtils;
//import com.hss01248.dialog.StyledDialog;
//import com.tencent.bugly.Bugly;
//import com.tencent.bugly.beta.Beta;
//import com.tencent.bugly.beta.interfaces.BetaPatchListener;
//import com.tencent.bugly.crashreport.CrashReport;
//import com.tencent.rtmp.TXLiveBase;
//import com.umeng.analytics.MobclickAgent;
//import com.umeng.commonsdk.UMConfigure;
//import com.umeng.socialize.PlatformConfig;
//import com.uuzuche.lib_zxing.activity.ZXingLibrary;
//import com.ximalaya.ting.android.opensdk.constants.ConstantsOpenSdk;
//import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
//import com.ximalaya.ting.android.opensdk.model.PlayableModel;
//import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
//import com.ximalaya.ting.android.opensdk.model.live.schedule.Schedule;
//import com.ximalaya.ting.android.opensdk.model.track.CommonTrackList;
//import com.ximalaya.ting.android.opensdk.model.track.Track;
//import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
//import com.ximalaya.ting.android.opensdk.player.appnotification.XmNotificationCreater;
//import com.ximalaya.ting.android.opensdk.player.receive.PlayerReceiver;
//import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
//import com.ximalaya.ting.android.opensdk.player.service.XmPlayerConfig;
//import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;
//import com.ximalaya.ting.android.player.XMediaPlayerConstants;
//import com.yhao.floatwindow.FloatWindow;
//import com.yhao.floatwindow.Screen;
//import com.yhao.floatwindow.ViewStateListener;
//
//import java.security.SecureRandom;
//import java.security.cert.X509Certificate;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//
//import cn.jpush.android.api.JPushInterface;
//import cn.net.shoot.sharetracesdk.ShareTrace;
//import me.jessyan.autosize.AutoSize;
//
////import com.hjq.toast.ToastUtils;
//
////import com.slodonapp.RNUMConfigure;
//
///**
// * @author Li
// * @date 2019/03/13 15:14
// * @des App入口
// */
//
//public class ClientAppLike extends LibApplication {
//
//    public ClientAppLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
//        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
//    }
//    private static final String PROCESS = "com.health.client";
//    @Override
//    public void onBaseContextAttached(Context base) {
//        super.onBaseContextAttached(base);
//        MultiDex.install(base);
//        Beta.installTinker(this);
//    }
//    private void initPieWebView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            String processName = getApplication().getProcessName();
//            if (!PROCESS.equals(processName)) {
//                WebView.setDataDirectorySuffix(processName);
//            }
//        }
//    }
//    public static void handleSSLHandshake() {
//        try {
//            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[0];
//                }
//
//                @Override
//                public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                }
//            }};
//
//            SSLContext sc = SSLContext.getInstance("TLS");
//            // trustAllCerts信任所有的证书
//            sc.init(null, trustAllCerts, new SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
//        } catch (Exception ignored) {
//        }
//    }
//
//
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        initPieWebView();
//        LibApplication.getAppContext()=getAppContext();
//        ZXingLibrary.initDisplayOpinion(LibApplication.getAppContext());
//        FrameActivityManager.setApplication(getApplication());
//        EmojiCompat.Config config = new BundledEmojiCompatConfig(getApplication());
//        EmojiCompat.init(config);
//        buildSound();
//        ShareTrace.init(getApplication());
//        AutoSize.initCompatMultiProcess(getApplication());
//        ResUtil.getInstance().init(getApplication());
////        handleSSLHandshake();
//        ScreenUtils.setDensity(getApplication(), 375f);
//        if (BuildConfig.DEBUG) {
//            ARouter.openLog();
//            ARouter.openDebug();
//        }
//        buildFloat();
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(getApplication());
//        StyledDialog.init(getApplication());
//        ARouter.init(getApplication());
//        PlatformConfig.setWeixin(Ids.WX_APP_ID, Ids.WX_APP_SECRET);
//        PlatformConfig.setQQZone(Ids.QQ_APP_ID, Ids.QQ_APP_KEY);
//        PlatformConfig.setWXFileProvider("com.health.client.fileprovider");
//        UMConfigure.init(getApplication(),Ids.UMENG_APP_KEY, ChannelUtil.getChannel(getApplication()),UMConfigure.DEVICE_TYPE_PHONE,"");
//        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL);
//        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
//        initializeBugly();
//        getApplication().registerActivityLifecycleCallbacks(new BusinessActivityLifecycleCallbacks());
//        OneKeyLoginManager.getInstance().setDebug(BuildConfig.DEBUG);
//        initShanyanSDK(getApplication());
//        TXLiveBase.getInstance().setLicence(getApplication(), Ids.TENCENT_LIVE_LICENSE_URL, Ids.TENCENT_LIVE_LICENSE_KEY);
//
//    }
//    private void initializeBugly() {
//        Beta.enableHotfix = true;
//        // 设置是否自动下载补丁，默认为true
//        Beta.canAutoDownloadPatch = true;
//        // 设置是否自动合成补丁，默认为true
//        Beta.canAutoPatch = true;
//        // 设置是否提示用户重启，默认为false
//        Beta.canNotifyUserRestart = true;
//        // 补丁回调接口
//        Beta.betaPatchListener = new BetaPatchListener() {
//            @Override
//            public void onPatchReceived(String patchFile) {
////                Toast.makeText(getApplication(), "补丁下载地址" + patchFile, Toast.LENGTH_SHORT).show();
//                Log.i("CrashReport","补丁下载地址" + patchFile);
//            }
//
//            @Override
//            public void onDownloadReceived(long savedLength, long totalLength) {
////                Toast.makeText(getApplication(),
////                        String.format(Locale.getDefault(), "%s %d%%",
////                                Beta.strNotificationDownloading,
////                                (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)),
////                        Toast.LENGTH_SHORT).show();
//                Log.i("CrashReport","补丁下载进度:"+String.format(Locale.getDefault(), "%s %d%%",
//                        Beta.strNotificationDownloading,
//                        (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)));
//            }
//
//            @Override
//            public void onDownloadSuccess(String msg) {
////                Toast.makeText(getApplication(), "补丁下载成功", Toast.LENGTH_SHORT).show();
//                Log.i("CrashReport","补丁下载成功");
//            }
//
//            @Override
//            public void onDownloadFailure(String msg) {
////                Toast.makeText(getApplication(), "补丁下载失败", Toast.LENGTH_SHORT).show();
//                Log.i("CrashReport","补丁下载失败");
//            }
//
//            @Override
//            public void onApplySuccess(String msg) {
////                Toast.makeText(getApplication(), "补丁应用成功", Toast.LENGTH_SHORT).show();
//                Log.i("CrashReport","补丁应用成功");
//            }
//
//            @Override
//            public void onApplyFailure(String msg) {
////                Toast.makeText(getApplication(), "补丁应用失败", Toast.LENGTH_SHORT).show();
//                Log.i("CrashReport","补丁应用失败");
//            }
//
//            @Override
//            public void onPatchRollback() {
////                Toast.makeText(getApplication(), "补丁回滚", Toast.LENGTH_SHORT).show();
//                Log.i("CrashReport","补丁回滚");
//            }
//        };
//        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
//        // 调试时，将第三个参数改为true
//        Bugly.init(getApplication(), com.healthy.library.BuildConfig.CRASHID,
//                true);
//    }
//
//
//    private void initShanyanSDK(Context context) {
//        OneKeyLoginManager.getInstance().init(context, "4OVXp9h4", new InitListener() {
//            @Override
//            public void getInitStatus(int code, String result) {
//                if (code != 1022) {
//                    CrashReport.postCatchedException(new Throwable("初始化失败： code==" + code + "   result==" + result));
//                }
//                //闪验SDK初始化结果回调
//                Log.e("VVV", "初始化： code==" + code + "   result==" + result);
//            }
//        });
//    }
//
//
//    private IXmPlayerStatusListener mPlayerStatusListener = new IXmPlayerStatusListener() {
//
//
//        @Override
//        public void onPlayStart() {
//            //System.out.println("播放开始");
//            CommonTrackList infoList = XmPlayerManager.getInstance(getApplication()).getCommonTrackList();
//            PlayableModel info = XmPlayerManager.getInstance(getApplication()).getCurrSound();
//            if (info != null) {
//                if (info instanceof Track) {
//
//                    Track track = (Track) info;
//                    if (infoList.getTracks().size() == 1) {
//                        try {
//                            SoundHolder.getInstance().floatSoundNext.setImageResource(R.drawable.flaot_sound_next_g);
//                            SoundHolder.getInstance().floatSoundPre.setImageResource(R.drawable.flaot_sound_pre_g);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    } else {
//                        try {
//                            SoundHolder.getInstance().floatSoundNext.setImageResource(R.drawable.flaot_sound_next);
//                            SoundHolder.getInstance().floatSoundPre.setImageResource(R.drawable.flaot_sound_pre);
//                            if (track.equals(infoList.getTracks().get(infoList.getTracks().size() - 1))) {//最后一集
//                                SoundHolder.getInstance().floatSoundNext.setImageResource(R.drawable.flaot_sound_next_g);
//                            }
//                            if (track.equals(infoList.getTracks().get(0))) {//第一集
//                                SoundHolder.getInstance().floatSoundPre.setImageResource(R.drawable.flaot_sound_pre_g);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//
//                    if (SoundHolder.getInstance().soundAlbum != null) {
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("memberId", new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
//                        map.put("tracksId", track.getDataId() + "");
//                        map.put("albumsId", SoundHolder.getInstance().soundAlbum.id + "");
//                        if (track.getTrackTitle() != null) {
//
//                            map.put("title", track.getTrackTitle() + "");
//                        } else {
//
//                            map.put("title", SoundHolder.getInstance().soundAlbum.album_title + "");
//                        }
//                        map.put(Functions.FUNCTION, "8071");
//                        ObservableHelper.createObservable(getApplication(), map)
//                                .subscribe(new NoInsertStringObserver(null, getApplication(), false) {
//                                    @Override
//                                    protected void onGetResultSuccess(String obj) {
//                                        super.onGetResultSuccess(obj);
//
//                                    }
//
//                                    @Override
//                                    protected void onFinish() {
//                                        super.onFinish();
//                                    }
//                                });
//                    }
//
//                }
//            }
//
//
//            try {
//                SoundHolder.getInstance().floatSoundPlay.setImageResource(R.drawable.flaot_sound_pause);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onPlayPause() {
//            try {
//                SoundHolder.getInstance().floatSoundPlay.setImageResource(R.drawable.flaot_sound_play);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onPlayStop() {
//            try {
//                SoundHolder.getInstance().floatSoundPlay.setImageResource(R.drawable.flaot_sound_play);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onSoundPlayComplete() {
//            try {
//                FloatWindow.get().hide();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                SoundHolder.getInstance().floatSoundPlay.setImageResource(R.drawable.flaot_sound_play);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onSoundPrepared() {
//
//        }
//
//        @Override
//        public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {
//
//        }
//
//        @Override
//        public void onBufferingStart() {
//            try {
//                SoundHolder.getInstance().soundProgress.setIndeterminate(false);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onBufferingStop() {
//
//        }
//
//        @Override
//        public void onBufferProgress(int i) {
//            try {
//                SoundHolder.getInstance().soundProgress.setIndeterminate(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onPlayProgress(int currPos, int duration) {
//            try {
//                SoundHolder.getInstance().soundProgress.setIndeterminate(false);
//                SoundHolder.getInstance().soundProgress.setProgress((int) (100 * currPos / (float) duration));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            String title = "";
//            PlayableModel info = XmPlayerManager.getInstance(getApplication()).getCurrSound();
//            if (info != null) {
//                if (info instanceof Track) {
//                    title = ((Track) info).getTrackTitle();
//                } else if (info instanceof Schedule) {
//                    title = ((Schedule) info).getRelatedProgram().getProgramName();
//                } else if (info instanceof Radio) {
//                    title = ((Radio) info).getRadioName();
//                }
//            }
//            try {
//                SoundHolder.getInstance().floatName.setText(title);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public boolean onError(XmPlayerException e) {
//            return false;
//        }
//    };
//
//
//
//    private void buildFloat() {
//        if (BuildConfig.VERSION_CODE >= 2151) {
//            final View soundview = LayoutInflater.from(getApplication()).inflate(R.layout.float_sound_windows, null);
//
//            final LinearLayout floatCloseLL;
//            final ImageView floatClose;
//            final ProgressBar soundProgress;
//            final LinearLayout floatSoundControlLL;
//            final ImageView floatSoundPre;
//            final ImageView floatSoundPlay;
//            final ImageView floatSoundNext;
//
//            floatCloseLL = (LinearLayout) soundview.findViewById(R.id.floatCloseLL);
//            floatClose = (ImageView) soundview.findViewById(R.id.floatClose);
//            soundProgress = (ProgressBar) soundview.findViewById(R.id.soundProgress);
//            floatSoundControlLL = (LinearLayout) soundview.findViewById(R.id.floatSoundControlLL);
//            floatSoundPre = (ImageView) soundview.findViewById(R.id.floatSoundPre);
//            floatSoundPlay = (ImageView) soundview.findViewById(R.id.floatSoundPlay);
//            floatSoundNext = (ImageView) soundview.findViewById(R.id.floatSoundNext);
//            floatCloseLL.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    floatCloseLL.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                FloatWindow.get().hide();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            XmPlayerManager.getInstance(getApplication()).pause();
//                            floatCloseLL.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    String actionName = "com.ximalaya.ting.android.ACTION_CLOSE";
//                                    if ("com.ximalaya.ting.android".equals(getApplication().getPackageName())) {
//                                        actionName = "com.ximalaya.ting.android.ACTION_CLOSE_MAIN";
//                                    }
//                                    Intent intenclose = new Intent(actionName);
//                                    intenclose.setClass(getApplication(), PlayerReceiver.class);
//                                    getApplication().sendBroadcast(intenclose);
//                                }
//                            }, 100);
//
//                        }
//                    }, 300);
//
//                }
//            });
//            floatSoundPlay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (XmPlayerManager.getInstance(getApplication()).isPlaying()) {
//                        XmPlayerManager.getInstance(getApplication()).pause();
//                    } else {
//                        XmPlayerManager.getInstance(getApplication()).play();
//                    }
//                }
//            });
//            floatSoundPre.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    XmPlayerManager.getInstance(getApplication()).playPre();
//                }
//            });
//            floatSoundNext.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    XmPlayerManager.getInstance(getApplication()).playNext();
//                }
//            });
//
//            FloatWindow
//                    .with(getApplication())
//                    .setView(soundview)
//                    .setWidth(ScreenUtils.getScreenWidth(getApplication()))
//                    .setFilter(true)
//                    .setY(Screen.height, 0.7f)
//                    .setDesktopShow(true)
//                    .setViewStateListener(new ViewStateListener() {
//                        @Override
//                        public void onPositionUpdate(int x, int y) {
//
//                        }
//
//                        @Override
//                        public void onShow() {
//
//                        }
//
//                        @Override
//                        public void onHide() {
//
//                        }
//
//                        @Override
//                        public void onDismiss() {
//
//                        }
//
//                        @Override
//                        public void onMoveAnimStart() {
//
//                        }
//
//                        @Override
//                        public void onMoveAnimEnd() {
//
//                        }
//
//                        @Override
//                        public void onBackToDesktop() {
////                            FloatWindow.get().show();
//                        }
//                    })
//                    .build();
//        }
//
//    }
//
//
//
//    private void buildSound() {
//        ConstantsOpenSdk.isDebug = true;
//        XMediaPlayerConstants.isDebug = true;
//        XmPlayerManager.getInstance(getApplication()).addPlayerStatusListener(mPlayerStatusListener);
//        CommonRequest mXimalaya = CommonRequest.getInstanse();
//        String mAppSecret = "0a1272ccb09f79471eecb2b318c0f244";
//        mXimalaya.setAppkey("b5dd27b4c667281dd50b35e127f299f1");
//        mXimalaya.setPackid("com.health.client");
//        mXimalaya.init(getApplication(), mAppSecret);
//        XmPlayerConfig.getInstance(getApplication()).setSDKHandleAudioFocus(false);
//
//
//        XmNotificationCreater instanse = XmNotificationCreater.getInstanse(getApplication());
//        instanse.setNextPendingIntent((PendingIntent) null);
//        instanse.setPrePendingIntent((PendingIntent) null);
//
//        String actionName = "com.app.test.android.Action_Close";
//        Intent intent = new Intent(actionName);
//        intent.setClass(getApplication(), HMMPlayerReceiver.class);
//        PendingIntent broadcast = PendingIntent.getBroadcast(getApplication(), 0, intent, 0);
//        instanse.setClosePendingIntent(broadcast);
//
//        String pauseActionName = "com.app.test.android.Action_PAUSE_START";
//        Intent intent1 = new Intent(pauseActionName);
//        intent1.setClass(getApplication(), HMMPlayerReceiver.class);
//        PendingIntent broadcast1 = PendingIntent.getBroadcast(getApplication(), 0, intent1, 0);
//        instanse.setStartOrPausePendingIntent(broadcast1);
//
//
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    class BusinessActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
//
//        @Override
//        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//            FrameActivityManager.instance().addActivity(activity);
//            Log.v("ActivityCallbacks", activity.getClass().getSimpleName() + ":" + "onActivityCreated");
//            if ("VodDisplayActivity".equals(activity.getClass().getSimpleName()) || activity instanceof IsMediaWindows) {
//                try {
//                    XmPlayerManager.getInstance(getApplication()).stop();
//                    String actionName = "com.ximalaya.ting.android.ACTION_CLOSE";
//                    if ("com.ximalaya.ting.android".equals(getApplication().getPackageName())) {
//                        actionName = "com.ximalaya.ting.android.ACTION_CLOSE_MAIN";
//                    }
//                    Intent intenclose = new Intent(actionName);
//                    intenclose.setClass(getApplication(), PlayerReceiver.class);
//                    getApplication().sendBroadcast(intenclose);
//                    FloatWindow.get().hide();
//                } catch (Exception e) {
////                    e.printStackTrace();
//                }
//            }
//
//        }
//
//        @Override
//        public void onActivityStarted(Activity activity) {
//            Log.v("ActivityCallbacks", activity.getClass().getSimpleName() + ":" + "onActivityStarted");
//
//        }
//
//        @Override
//        public void onActivityResumed(Activity activity) {
//            Log.v("ActivityCallbacks", activity.getClass().getSimpleName() + ":" + "onActivityResumed");
//        }
//
//        @Override
//        public void onActivityPaused(Activity activity) {
//            Log.v("ActivityCallbacks", activity.getClass().getSimpleName() + ":" + "onActivityPaused");
//
//        }
//
//        @Override
//        public void onActivityStopped(Activity activity) {
//            Log.v("ActivityCallbacks", activity.getClass().getSimpleName() + ":" + "onActivityStopped");
//
//        }
//
//        @Override
//        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//            Log.v("ActivityCallbacks", activity.getClass().getSimpleName() + ":" + "onActivitySaveInstanceState");
//
//        }
//
//        @Override
//        public void onActivityDestroyed(Activity activity) {
//            FrameActivityManager.instance().removeActivity(activity);
//            Log.v("ActivityCallbacks", activity.getClass().getSimpleName() + ":" + "onActivityDestroyed");
//        }
//    }
//
//}
