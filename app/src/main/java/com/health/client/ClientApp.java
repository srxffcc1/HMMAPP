package com.health.client;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;

import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.GetPhoneInfoListener;
import com.chuanglan.shanyan_sdk.listener.InitListener;
import com.health.client.widget.HMMBetaPatchListener;
import com.healthy.library.LibApplication;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.constant.Ids;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.FrameActivityManager;
import com.healthy.library.utils.ResUtil;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpUtilsOld;
import com.hss01248.dialog.StyledDialog;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.jpush.android.api.JPushInterface;
import cn.net.shoot.sharetracesdk.ShareTrace;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.jessyan.autosize.AutoSize;

//import com.hjq.toast.ToastUtils;

//import com.slodonapp.RNUMConfigure;

/**
 * @author Li
 * @date 2019/03/13 15:14
 * @des App入口
 */

public class ClientApp extends LibApplication {//在融合之后 修改这里的继承 LibApplication

    static {
        PlatformConfig.setWeixin(Ids.WX_APP_ID, Ids.WX_APP_SECRET);
        PlatformConfig.setQQZone(Ids.QQ_APP_ID, Ids.QQ_APP_KEY);
        PlatformConfig.setWXFileProvider("com.health.client.fileprovider");

    }

    Application application;
    private boolean isLoadSuccess;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        Beta.installTinker();
    }

    private static final String PROCESS = "com.health.client";


    private void initPieWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName();
            if (!PROCESS.equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }
    }


    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    public static void initializeBugly(Application context) {
        Beta.enableHotfix = true;
        Beta.autoCheckUpgrade = false;
        Beta.canAutoDownloadPatch = false;
        Beta.canAutoPatch = false;
        Beta.canNotifyUserRestart = false;
        Beta.betaPatchListener = new HMMBetaPatchListener();
        try {
//            Bugly.putUserData(getApplicationContext(), "link10activity", SpUtils.getValue(getAppContext(),"link10activity"));
            Bugly.setUserId(context, new String(Base64.decode(SpUtils.getValue(context, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        } catch (Exception e) {
//            e.printStackTrace();
        }
        Bugly.setIsDevelopmentDevice(context, "test".equals(ChannelUtil.getChannel(context)));
        Bugly.init(context, com.healthy.library.BuildConfig.CRASHID,
                true);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Log.e("ClientApp","启动");
        initSDkAll(application);
        SpUtils.store(application, new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "推荐", false);
    }

    public void initSDkAll(final Application application) {
        buildFloat();
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
        }
        ARouter.openDebug();
        ARouter.init(application);
        Log.e("ClientApp", "开始" + DateUtils.long2Str(System.currentTimeMillis(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS));
        if (!isLoadSuccess) {
            Observable.just("").subscribeOn(Schedulers.io()).subscribe(s -> {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                initPieWebView();
                try {
                    SpUtils.store(LibApplication.getAppContext(),SpKey.VERSION_CHECK_FLAG,false);
                    SpUtils.store(application, "ISAPKINSTALL", false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ScreenUtils.setDensity(application, 375f);
                AutoSize.initCompatMultiProcess(application);
                StyledDialog.init(getApplicationContext());
                ZXingLibrary.initDisplayOpinion(application);
                EmojiCompat.Config config = new BundledEmojiCompatConfig(application);
                EmojiCompat.init(config);
                UMConfigure.preInit(application, Ids.UMENG_APP_KEY, ChannelUtil.getChannel(application));
                application.registerActivityLifecycleCallbacks(new BusinessActivityLifecycleCallbacks(null));
//                application.registerActivityLifecycleCallbacks(new BusinessActivityLifecycleCallbacks(new FloatingLifecycle()));
                //闪验SDK配置debug开关 （必须放在初始化之前，开启后可打印闪验SDK更加详细日志信息）
                try {
                    if (SpUtilsOld.getValue(application, SpKey.YSLOOK, false)) {
                        SpUtils.store(LibApplication.getAppContext(), "showKillDialog", false);

                        initSDk(application);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isLoadSuccess = true;
                Log.e("ClientApp", "结束" + DateUtils.long2Str(System.currentTimeMillis(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS));
            });
           /* new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();*/
        }
    }


    public static void initShanyanSDK(Context context) {
        OneKeyLoginManager.getInstance().init(context, Ids.SHANYAN_APP_KEY, new InitListener() {
            @Override
            public void getInitStatus(int code, String result) {
                if (code != 1022) {
//                    CrashReport.postCatchedException(new Throwable("初始化失败： code==" + code + "   result==" + result));
                }
                //闪验SDK初始化结果回调
                Log.e("VVV", "初始化： code==" + code + "   result==" + result);
            }
        });
    }

    public static void initSDk(Application context) {
        TXLiveBase.getInstance().setLicence(context, Ids.TENCENT_LIVE_LICENSE_URL, Ids.TENCENT_LIVE_LICENSE_KEY);
        buildSound(context);
        ResUtil.getInstance().init(context);
        handleSSLHandshake();
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL);
        UMConfigure.init(LibApplication.getAppContext(), Ids.UMENG_APP_KEY, ChannelUtil.getChannel(LibApplication.getAppContext()), UMConfigure.DEVICE_TYPE_PHONE, "");
//
        OneKeyLoginManager.getInstance().setDebug(BuildConfig.DEBUG);
//        XmPlayerManager.getInstance(context).init();
        initShanyanSDK(context);
        ShareTrace.init(context);
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(context);
        initializeBugly(context);
        initX5Sdk(context);//初始化x5 SDK
        OneKeyLoginManager.getInstance().getPhoneInfo(new GetPhoneInfoListener() {

            @Override
            public void getPhoneInfoStatus(int i, String s) {

            }
        });
    }

    public static void initX5Sdk(Application context) {
        Map<String, Object> map = new HashMap<>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(context, cb);
    }

    private static void buildSound(Application application) {
//        ConstantsOpenSdk.isDebug = true;
//        XMediaPlayerConstants.isDebug = true;
//        IXmPlayerStatusListenerImp iXmPlayerStatusListenerImp=new IXmPlayerStatusListenerImp(application);
//        XmPlayerManager.getInstance(application).addPlayerStatusListener(iXmPlayerStatusListenerImp);
//        CommonRequest mXimalaya = CommonRequest.getInstanse();
//        String mAppSecret = "0a1272ccb09f79471eecb2b318c0f244";
//        mXimalaya.setAppkey("b5dd27b4c667281dd50b35e127f299f1");
//        mXimalaya.setPackid("com.health.client");
//        mXimalaya.init(application, mAppSecret, new DeviceInfoProviderDefault(LibApplication.getAppContext()) {
//            @Override
//            public String oaid() {
//                return "";
//            }
//        });
//        XmPlayerConfig.getInstance(application).setSDKHandleAudioFocus(false);
//
//
//        XmNotificationCreater instanse = XmNotificationCreater.getInstanse(application);
//        instanse.setNextPendingIntent((PendingIntent) null);
//        instanse.setPrePendingIntent((PendingIntent) null);
//
//        String actionName = "com.app.test.android.Action_Close";
//        Intent intent = new Intent(actionName);
//        intent.setClass(application, HMMPlayerReceiver.class);
//        PendingIntent broadcast = PendingIntent.getBroadcast(application, 0, intent, 0);
//        instanse.setClosePendingIntent(broadcast);
//
//        String pauseActionName = "com.app.test.android.Action_PAUSE_START";
//        Intent intent1 = new Intent(pauseActionName);
//        intent1.setClass(application, HMMPlayerReceiver.class);
//        PendingIntent broadcast1 = PendingIntent.getBroadcast(application, 0, intent1, 0);
//        instanse.setStartOrPausePendingIntent(broadcast1);


    }

    private void buildFloat() {
//        if (BuildConfig.VERSION_CODE >= 2151) {
//            final View soundview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.float_sound_windows, null);
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
//                            try {
//                                SoundHolder.getInstance().soundAlbum.setStop(true);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            XmPlayerManager.getInstance(getApplicationContext()).pause();
//                            XmPlayerManager.getInstance(getApplicationContext()).stop();
//                            floatCloseLL.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    String actionName = "com.ximalaya.ting.android.ACTION_CLOSE";
//                                    if ("com.ximalaya.ting.android".equals(getPackageName())) {
//                                        actionName = "com.ximalaya.ting.android.ACTION_CLOSE_MAIN";
//                                    }
//                                    Intent intenclose = new Intent(actionName);
//                                    intenclose.setClass(getApplicationContext(), PlayerReceiver.class);
//                                    getApplicationContext().sendBroadcast(intenclose);
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
//                    if (XmPlayerManager.getInstance(getApplicationContext()).isPlaying()) {
//                        XmPlayerManager.getInstance(getApplicationContext()).pause();
//                    } else {
//                        XmPlayerManager.getInstance(getApplicationContext()).play();
//                    }
//                }
//            });
//            floatSoundPre.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    XmPlayerManager.getInstance(getApplicationContext()).playPre();
//                }
//            });
//            floatSoundNext.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    XmPlayerManager.getInstance(getApplicationContext()).playNext();
//                }
//            });
//
//            FloatWindow
//                    .with(getApplicationContext())
//                    .setView(soundview)
//                    .setWidth(ScreenUtils.getScreenWidth(getApplicationContext()))
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

    }

    private void initView() {

    }




    class BusinessActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {
        ActivityLifecycleCallbacks activityLifecycleCallbacksEx;
        public BusinessActivityLifecycleCallbacks(ActivityLifecycleCallbacks activityLifecycleCallbacks) {
            activityLifecycleCallbacksEx=activityLifecycleCallbacks;
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            FrameActivityManager.instance().addActivity(activity);
            if(!"FloatActivity".equals(activity.getClass().getSimpleName())){
            }
            Log.v("ActivityCallbacks", activity.getClass().getSimpleName() + ":" + "onActivityCreated");
            try {
                String result = "";
                String stringList = SpUtils.getValue(LibApplication.getAppContext(), "link10activity");
                String[] strings;
                if (stringList != null) {
                    strings = stringList.split("\n");
                    if (strings.length > 10) {
                        strings[0] = "";
                    }
                } else {
                    strings = new String[]{activity.getClass().getSimpleName() + ":" + "onActivityCreated\n"};
                }
                for (int i = 0; i < strings.length; i++) {
                    if (strings[i] != null && !TextUtils.isEmpty(strings[i].trim())) {
                        result = result + strings[i] + "\n";
                    }
                }
                SpUtils.store(LibApplication.getAppContext(), "link10activity", result);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if ("VideoPlayerActivity".equals(activity.getClass().getSimpleName()) || activity instanceof IsMediaWindows) {
//                try {
//                    XmPlayerManager.getInstance(getApplicationContext()).stop();
//                    String actionName = "com.ximalaya.ting.android.ACTION_CLOSE";
//                    if ("com.ximalaya.ting.android".equals(getPackageName())) {
//                        actionName = "com.ximalaya.ting.android.ACTION_CLOSE_MAIN";
//                    }
//                    Intent intenclose = new Intent(actionName);
//                    intenclose.setClass(getApplicationContext(), PlayerReceiver.class);
//                    getApplicationContext().sendBroadcast(intenclose);
//                    FloatWindow.get().hide();
//                } catch (Exception e) {
////                    e.printStackTrace();
//                }
//            }
            if(activityLifecycleCallbacksEx==null){
                return;
            }
            activityLifecycleCallbacksEx.onActivityCreated(activity,savedInstanceState);

        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.v("ActivityCallbacks", activity.getClass().getSimpleName() + ":" + "onActivityStarted");
            if(activityLifecycleCallbacksEx==null){
                return;
            }
            activityLifecycleCallbacksEx.onActivityStarted(activity);

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.v("ActivityCallbacks", activity.getClass().getSimpleName() + ":" + "onActivityResumed");
            if(activityLifecycleCallbacksEx==null){
                return;
            }
            activityLifecycleCallbacksEx.onActivityResumed(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.v("ActivityCallbacks", activity.getClass().getSimpleName() + ":" + "onActivityPaused");
            if(activityLifecycleCallbacksEx==null){
                return;
            }
            activityLifecycleCallbacksEx.onActivityPaused(activity);

        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.v("ActivityCallbacks", activity.getClass().getSimpleName() + ":" + "onActivityStopped");
            if(activityLifecycleCallbacksEx==null){
                return;
            }
            activityLifecycleCallbacksEx.onActivityStopped(activity);

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.v("ActivityCallbacks", activity.getClass().getSimpleName() + ":" + "onActivitySaveInstanceState");
            if(activityLifecycleCallbacksEx==null){
                return;
            }
            activityLifecycleCallbacksEx.onActivitySaveInstanceState(activity,outState);

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            FrameActivityManager.instance().removeActivity(activity);
            if(!"FloatActivity".equals(activity.getClass().getSimpleName())){
//                FrameActivityManager.instance().removeActivity(activity);
            }
            Log.v("ActivityCallbacks", activity.getClass().getSimpleName() + ":" + "onActivityDestroyed");
            if(activityLifecycleCallbacksEx==null){
                return;
            }
            activityLifecycleCallbacksEx.onActivityDestroyed(activity);
        }
    }
}
