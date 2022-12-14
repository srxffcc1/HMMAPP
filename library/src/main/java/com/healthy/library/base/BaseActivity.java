package com.healthy.library.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.R;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.HmmFloatWindowView;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.VersionPatchContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.IsNeedShareMini;
import com.healthy.library.interfaces.IsNeedVideo;
import com.healthy.library.interfaces.IsNoNeedCardDialog;
import com.healthy.library.interfaces.IsNoNeedPatchShow;
import com.healthy.library.interfaces.IsTranslucent;
import com.healthy.library.interfaces.OnNetRetryListener;
import com.healthy.library.interfaces.OnTopBarListener;
import com.healthy.library.message.CouponShowBean;
import com.healthy.library.message.SharePlatformMsg;
import com.healthy.library.message.UpdateKillMsg;
import com.healthy.library.message.UpdatePatchHasMsg;
import com.healthy.library.model.MemberAction;
import com.healthy.library.model.UpdatePatchVersion;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.net.download.DownloadInfo;
import com.healthy.library.net.download.HmmDownloadManager;
import com.healthy.library.presenter.ActionPresenterCopy;
import com.healthy.library.presenter.TongLianPhonePresenterCopy;
import com.healthy.library.presenter.VersionPatchPresenter;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.FrameActivityManager;
import com.healthy.library.utils.HandlerUtils;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.NewStatusBarUtil;
import com.healthy.library.utils.ObjUtil;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.utils.ResUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.SpUtilsOld;
import com.healthy.library.widget.CardPackDialog;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ezy.assist.compat.SettingsCompat;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import permison.FloatWindowManager;


/**
 * @author Li
 * @date 2019/03/01 11:29
 * @des activity
 */

public abstract class BaseActivity extends BaseNoTitleActivity implements OnTopBarListener, BaseView,
        OnNetRetryListener, UMShareListener, VersionPatchContract.View {
    public CardPackDialog cardPackDialog;
    protected Context mContext;
    protected Activity mActivity;
    private StatusLayout mStatusLayout;
    private CompositeDisposable mCompositeDisposable;

    private Map<String, SHARE_MEDIA> mPlatformMap = new HashMap<>();
    private AlertDialog mShareDialog;
    private String surl;
    private String sdes;
    private String spath;
    private String suserName;
    private String stitle;
    private Bitmap sBitmap;

    private boolean cantoast = true;
    private DownloadManager mDownloadManager;
    //private HtSdk mHtSdk;//?????????
    private TXLivePlayer mLivePlayer;
    private TXCloudVideoView txCloudVideoView;
    protected HandlerUtils.HandlerHolder handlerVideoDefault;
    private TongLianPhonePresenterCopy tongLianPhonePresenterCopy;

    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null || mActivity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DisplayUtility.INSTANCE.disabledDisplayDpiChange(this.getResources());
        mPlatformMap.clear();
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_wx), SHARE_MEDIA.WEIXIN);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_circle), SHARE_MEDIA.WEIXIN_CIRCLE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qq), SHARE_MEDIA.QQ);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qzone), SHARE_MEDIA.QZONE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_sina), SHARE_MEDIA.SINA);

        tongLianPhonePresenterCopy=new TongLianPhonePresenterCopy(mContext,null);
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getApplicationContext().registerReceiver(mDownloadCompleteReceiver, filter);
        if (this instanceof IsFitsSystemWindows) {  //?????????????????????????????????
            NewStatusBarUtil.setRootViewFitsSystemWindows(this, true);
            //?????????????????????
            NewStatusBarUtil.setTranslucentStatus(this);
            //?????????????????????????????????????????????????????????, ???????????????????????????????????????, ?????????????????????????????????
            //??????????????????????????????,?????????????????????, ??????????????????????????????????????????, ???????????????????????????????????????if??????
            if (!NewStatusBarUtil.setStatusBarDarkTheme(this, true)) {
                //????????????????????????????????? ???????????????????????????????????????????????????, ?????????????????????????????????????????????,
                //???????????????+???=???, ??????????????????????????????
                NewStatusBarUtil.setStatusBarColor(this, 0x55000000);
            }
        }

        handlerVideoDefault = new HandlerUtils.HandlerHolder(msg -> {
            if (msg.what == HmmFloatWindowView.REMOVE_FLOATING_WINDOW) {
                //?????????pass
                stopOnlineVideoFloat();
            }
            if (msg.what == HmmFloatWindowView.STOP_FLOATING_WINDOW) {
                stopOnlineVideoFloat();
            }

            if (msg.what == 0 || msg.what == 1) {
                cantoast = true;
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        slient();
        if (!(this instanceof IsTranslucent)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

//        setTransparentStatusBar(this);
        setContentView(getLayoutId());
        mContext = this;
        mActivity = this;
        StatusLayout statusLayout = findViewById(R.id.layout_status);
        TopBar topBar = findViewById(R.id.top_bar);
        if (statusLayout != null) {
            setStatusLayout(statusLayout);
        }
        if (topBar != null) {
            setTopBar(topBar);
        }
        findViews();
        init(savedInstanceState);
        /*getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                ViewDoubleClickUtil.hookView(findViewById(android.R.id.content));
            }
        });*/
//        if(this instanceof IsShowFloatWindows){
//            FloatWindow.get().show();
//            //System.out.println("??????????????????");
//        }else {
//
//            FloatWindow.get().hide();
//            //System.out.println("??????????????????");
//        }
        if (SpUtils.getValue(LibApplication.getAppContext(), "showKillDialog", false)) {
            showKillDialog();
        }
        new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                new SimpleHashMapBuilder<>()
                        .putObject(new MemberAction(BuildConfig.VERSION_NAME,1,1,getActivitySimpleName(),getActivitySimpleName(),
                                new String(Base64.decode(SpUtils.getValue(getApplication(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                        ));

    }



    private void slientUI() {//???????????????
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);//????????????
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        getWindow().getDecorView().setLayerType(View.LAYER_TYPE_HARDWARE,paint);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void killMine(UpdateKillMsg msg) {
        showKillDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void shouldCheckUpdatePatch(UpdatePatchHasMsg msg) {
        if (isFinishing()) {
            return;
        }
        if(this instanceof IsNoNeedPatchShow){
            return;
        }
        if (SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)) {
            return;
        }
        if (FrameActivityManager.checkNowIsTopActivity(this)) {
//            Toast.makeText(mContext, "??????????????????", Toast.LENGTH_SHORT).show();
            new VersionPatchPresenter(this,this).checkPatchVersion(new SimpleHashMapBuilder<>().puts("platform", "1").puts("version", BuildConfig.VERSION_NAME).puts(Functions.FUNCTION, "6004"));
        }
    }

    public void showKillDialog() {
        if (!SpUtils.getValue(mContext, "referralCodeInit", false)) {//???????????????????????????????????????????????????false ????????????
            return;
        }
        if (isFinishing()) {
            return;
        }
        if(this instanceof IsNoNeedPatchShow){
            return;
        }
        if (SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)) {
            return;
        }
        if(getActivitySimpleName().equals("AboutActivity")){//??????
            Toast.makeText(LibApplication.getAppContext(), "???????????????-???????????????", Toast.LENGTH_LONG).show();
        }else {
            if(System.currentTimeMillis()-SpUtils.getValue(mContext,SpKey.KILLTIME,0l)<(3600000*24*15)){//??????12?????? ???????????? ?????????????????? ????????????needKillFinish ???????????????
                return;
            }
        }
        StyledDialog.init(FrameActivityManager.instance().topActivity());
        StyledDialog.buildIosAlert("??????", "?????????????????????????????????????????????????????????", new MyDialogListener() {
            @Override
            public void onFirst() {

            }

            @Override
            public void onThird() {
                super.onThird();
            }

            @Override
            public void onSecond() {
                SpUtils.store(LibApplication.getAppContext(), "showKillDialog", false);
                FrameActivityManager.instance().appExit(LibApplication.getAppContext());
                System.exit(0);
            }
        }).setCancelable(false,false).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("??????", "??????").show();
        SpUtils.store(mContext,SpKey.KILLTIME,System.currentTimeMillis());
    }

    @androidx.annotation.NonNull
    public String getActivitySimpleName() {
        return getClass().getSimpleName();
    }


    private void showFileDialog() {
        if (isFinishing()) {
            return;
        }
        if(this instanceof IsNoNeedPatchShow){
            return;
        }
        if (SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)) {
            return;
        }
        if(System.currentTimeMillis()-SpUtils.getValue(mContext,SpKey.KILLFILETIME,0l)<(3600000)){//??????12?????? ???????????? ?????????????????? ????????????needKillFinish ???????????????
            return;
        }
        Toast.makeText(LibApplication.getAppContext(), "??????!\n??????SD?????????????????????", Toast.LENGTH_LONG).show();
        StyledDialog.init(mContext);
        StyledDialog.buildIosAlert("??????????????????", "??????????????????\n??????????????????????????????\n???????????????????????????????????????????", new MyDialogListener() {
            @Override
            public void onFirst() {

            }

            @Override
            public void onThird() {
                super.onThird();
            }

            @Override
            public void onSecond() {
                IntentUtils.toSelfSetting(LibApplication.getAppContext());
            }
        }).setCancelable(false,false).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("??????", "??????").show();
        SpUtils.store(mContext,SpKey.KILLFILETIME,System.currentTimeMillis());
    }

    public void showShare() {
        if (this instanceof IsNeedShare) {
            IsNeedShare isNeedShare = (IsNeedShare) this;
            surl = isNeedShare.getSurl();
            stitle = isNeedShare.getStitle();
            sdes = isNeedShare.getSdes();
            if (TextUtils.isEmpty(sdes)) {
                sdes = " ";
            }
            sBitmap = isNeedShare.getsBitmap();
            if (sBitmap == null) {
                sBitmap = BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.index_share_humb));
            }
            showShareBottomDialog();
        }
    }

    public void showShareMini() {
        if (this instanceof IsNeedShare) {
            IsNeedShare isNeedShare = (IsNeedShare) this;
            surl = isNeedShare.getSurl();
            stitle = isNeedShare.getStitle();
            sdes = isNeedShare.getSdes();

            if (this instanceof IsNeedShareMini) {
                IsNeedShareMini isNeedShareMini = (IsNeedShareMini) this;
                spath = isNeedShareMini.getPath();
                if (BuildConfig.DEBUG) {
                    Toast.makeText(this, spath, Toast.LENGTH_SHORT).show();
                }
                suserName = isNeedShareMini.getUserName();
            }
            if (TextUtils.isEmpty(sdes)) {
                sdes = " ";
            }
            sBitmap = isNeedShare.getsBitmap();
            if (sBitmap == null) {
                sBitmap = BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.index_share_humb));
            }
            showShareBottomDialog(true);
        }
    }

    public HmmFloatWindowView mFloatingView;
    private WindowManager.LayoutParams mFloatingViewParams;
    private WindowManager mWindowManager;
    public boolean isShowFloat = false;
    @Override
    protected void onResume() {
        super.onResume();
        try {
            Glide.with(this).resumeRequests();
        } catch (Exception e) {
        }
        isShareIng = false;
        if (this instanceof IsNeedVideo) {//????????????????????????
            if (isFinishing()) {
                return;
            }
            startOnlineVideoFloat();
        }
        tongLianPhonePresenterCopy.getTongLianPhoneStatus(new SimpleHashMapBuilder<>());
        if (SpUtils.getValue(mContext, SpKey.AUDITSTATUS, true)) {//???????????????

        } else {
            new VersionPatchPresenter(this,this).checkPatchVersion(new SimpleHashMapBuilder<>().puts("platform", "1").puts("version", BuildConfig.VERSION_NAME).puts(Functions.FUNCTION, "6004"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        try {
            Glide.with(this).pauseRequests();
        } catch (Exception e) {
        }

//        mActivity = null;
//        mContext = null;
        super.onDestroy();
        getApplicationContext().unregisterReceiver(mDownloadCompleteReceiver);
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        if (handlerVideoDefault != null) {
            handlerVideoDefault.removeCallbacksAndMessages(null);
            handlerVideoDefault = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isShareIng) {
            //System.out.println("ActivityCallbacks:????????????");
            EventBus.getDefault().post(new SharePlatformMsg());
        }
    }

    private String TAG = "TP";

    public void startOnlineVideoFloatEx() {

        if (this instanceof IsNeedVideo) {//????????????????????????
            IsNeedVideo isNeedVideo = (IsNeedVideo) this;
            if (!TextUtils.isEmpty(isNeedVideo.getToken()) && !"null".equals(isNeedVideo.getToken())) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    //System.out.println("??????????????????3");
                    FloatWindowManager.getInstance().applyOrShowFloatWindow(this);
                } else {
                    //System.out.println("??????????????????4");
                    if (!SettingsCompat.canDrawOverlays(mContext)) {
                        Toast.makeText(mContext, "???????????????????????????", Toast.LENGTH_SHORT).show();
                        SettingsCompat.manageDrawOverlays(mContext);
                    }
                }
                try {
                    WindowManager windowManager = getWindowManager(mContext);
                    int screenWidth = windowManager.getDefaultDisplay().getWidth();
                    int screenHeight = windowManager.getDefaultDisplay().getHeight();
                    if (mFloatingView == null) {
                        //System.out.println("??????????????????");
                        mFloatingView = new HmmFloatWindowView(mContext);
                        mFloatingView.setCourse_id(isNeedVideo.getCourseId());
                        mFloatingView.setLiveStatus(isNeedVideo.getLiveStatus());

                        txCloudVideoView = new TXCloudVideoView(this);


                        //?????? player ??????
                        mLivePlayer = new TXLivePlayer(mContext);
                        //?????? player ??????????????? view
                        mLivePlayer.setPlayerView(txCloudVideoView);
                        TXLivePlayConfig mPlayConfig = new TXLivePlayConfig();
                        //????????????
                        mPlayConfig.setAutoAdjustCacheTime(true);
                        mPlayConfig.setMinAutoAdjustCacheTime(1);
                        mPlayConfig.setMaxAutoAdjustCacheTime(5);
                        mLivePlayer.setConfig(mPlayConfig);
                        // ??????????????????
                        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                        // ????????????????????????
                        mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);

                        mLivePlayer.setPlayListener(new ITXLivePlayListener() {
                            @Override
                            public void onPlayEvent(int event, Bundle param) {
                                if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                                    stopOnlineVideoFloat();
                                    Log.e(TAG, "????????????");
                                } else if (event == TXLiveConstants.PLAY_EVT_RTMP_STREAM_BEGIN) {
                                    Log.e(TAG, "????????????????????????????????????");
                                }
                                if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                                    Log.e(TAG, "??????????????????");
                                }
                                if (event == TXLiveConstants.PLAY_EVT_GET_PLAYINFO_SUCC) {
                                    Log.e(TAG, "??????????????????????????????");
                                }
                                if (event == TXLiveConstants.PLAY_ERR_FILE_NOT_FOUND) {
                                    stopOnlineVideoFloat();
                                    Log.e(TAG, "?????????????????????");
                                }
                                if (event == TXLiveConstants.PLAY_ERR_HEVC_DECODE_FAIL) {
                                    stopOnlineVideoFloat();
                                    Log.e(TAG, "H265 ????????????");
                                }
                                if (event == TXLiveConstants.PLAY_ERR_HLS_KEY) {
                                    stopOnlineVideoFloat();
                                    Log.e(TAG, "HLS ?????? key ????????????");
                                }
                                if (event == TXLiveConstants.PLAY_ERR_GET_PLAYINFO_FAIL) {
                                    stopOnlineVideoFloat();
                                    Log.e(TAG, "??????????????????????????????");
                                }
                                if (event == TXLiveConstants.PLAY_WARNING_SEVER_CONN_FAIL) {
                                    stopOnlineVideoFloat();
                                    Log.e(TAG, "????????????????????????????????? RTMP:// ?????????????????????");
                                }

                            }

                            @Override
                            public void onNetStatus(Bundle status) {
                            }
                        });
//                        mHtSdk = HtSdk.getInstance();
                        if (isNeedVideo.getVideoHandler() != null) {
                            mFloatingView.setHandler(isNeedVideo.getVideoHandler());
                        } else {
                            mFloatingView.setHandler(handlerVideoDefault);
                        }
                        mFloatingView.pptContainer.addView(txCloudVideoView);
//                        FrameLayout liveWaitView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.live_wait_layout, null);
//                        FrameLayout liveOverView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.live_over_layout, null);
//
//                        mHtSdk.setLiveWaitView(liveWaitView);//???????????????????????????view
//                        mHtSdk.setLiveOverView(liveOverView);//????????????????????????view
//                        mHtSdk.init(mFloatingView.pptContainer, mFloatingView.videoViewContainer, isNeedVideo.getToken());
//                        mHtSdk.setPauseInBackground(true);
//                        mHtSdk.onResume();
                        if (mFloatingViewParams == null) {
                            mFloatingViewParams = new WindowManager.LayoutParams();
                            mFloatingViewParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                            mFloatingViewParams.format = PixelFormat.RGBA_8888;
                            mFloatingViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
                            mFloatingViewParams.gravity = Gravity.LEFT | Gravity.TOP;
                            mFloatingViewParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                            mFloatingViewParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            mFloatingViewParams.x = screenWidth;
                            mFloatingViewParams.y = (int) (screenHeight * 1.0 * 0.65);
                        }
                        mFloatingView.updateViewLayoutParams(mFloatingViewParams);
                        windowManager.addView(mFloatingView, mFloatingViewParams);
                        isShowFloat = true;
                        mLivePlayer.startPlay(isNeedVideo.getToken(), TXLivePlayer.PLAY_TYPE_VOD_HLS);
                        //System.out.println("?????????????????????");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    if (mLivePlayer != null) {
                        mLivePlayer.pause();
                        mLivePlayer.stopPlay(true);
                        txCloudVideoView.onDestroy();
                    }
                    isShowFloat = false;
                    mFloatingView = null;
                    mFloatingViewParams = null;

                }
                if(FloatWindowManager.isHideShow){
                    if (mLivePlayer != null) {
                        mLivePlayer.pause();
                        mLivePlayer.stopPlay(true);
                        txCloudVideoView.onDestroy();
                    }
                }

            }


        }
        //System.out.println("?????????????????????");

    }
    private String[] mSdPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static UpdatePatchVersion newUpdatePatchVersion;
    @Override
    public void onSucessCheckPatchVersion(UpdatePatchVersion result) {
        if (result == null) {
            return;
        }
        if(SpUtils.getValue(LibApplication.getAppContext(), "showKillDialog", false)){
            System.out.println("????????????:??????????????????-" + result.getVersionCode());
            showKillDialog();
            return;//??????????????????
        }
        if (BuildConfig.VERSION_CODE != result.getVersionCodeWithBase() && result.getVersionCodeWithBase() != 0) {//?????????????????? ??????????????????
            System.out.println("????????????:??????????????????-" + result.getVersionCode());
            return;
        }
        if(!getActivitySimpleName().equals("AboutActivity")){//??????
            if(newUpdatePatchVersion!=null&&newUpdatePatchVersion.getVersionCode()>=result.getVersionCode()){//?????????????????? ???????????????
                System.out.println("????????????:??????????????????-" + result.getVersionCode());
                return;
            }
        }

        UpdatePatchVersion nowUpdatepatchVersion = ObjUtil.getObj(SpUtils.getValue(mContext, SpKey.USER_PATCH), UpdatePatchVersion.class);
        if (nowUpdatepatchVersion == null) {
            nowUpdatepatchVersion = new UpdatePatchVersion();
        }
        if(SpUtilsOld.getValue(LibApplication.getAppContext(),SpKey.YSLOOK,false)){
            try {
                Tinker tinker = Tinker.with(getApplicationContext());
                String tinkerId = tinker.getTinkerLoadResultIfPresent().getPackageConfigByName(ShareConstants.NEW_TINKER_ID);
                if (!TextUtils.isEmpty(tinkerId) && tinkerId != null && tinkerId.contains("patch")) {
                    System.out.println("????????????:??????????????????-" + tinkerId);
                    nowUpdatepatchVersion.version = tinkerId;
                }else {//?????????0 ????????????????????? ?????????????????????
                    if(ChannelUtil.isRealRelease()){
                        nowUpdatepatchVersion.version = "0";
                    }
                    if(BuildConfig.DEBUG){
                        nowUpdatepatchVersion.version = "0";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (result != null) {
            if (result.getVersionCode() > nowUpdatepatchVersion.getVersionCode()) {//????????????????????????????????????????????????
                if (!ChannelUtil.isRealRelease()) {
                    System.out.println("????????????:??????????????????-" + result.getVersionCode());
                }
                if(getActivitySimpleName().equals("AboutActivity")){//??????
                    Toast.makeText(LibApplication.getAppContext(), "???????????????-???????????????", Toast.LENGTH_LONG).show();
                }
                if (PermissionUtils.hasPermissions(LibApplication.getAppContext(), mSdPermissions)) {
                    File patchFile = new File(HmmDownloadManager.FILE_PATH, "patch_signed_7zip.apk");
                    if (patchFile.exists()) {//???????????????????????????
                        patchFile.delete();
                    }
                    System.out.println("????????????:????????????");
                    newUpdatePatchVersion = result;
                    HmmDownloadManager.getInstance().download(result.downloadUrl);
                } else {
                    showFileDialog();

                }
            }
        }
    }

    /*** ?????? **/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(DownloadInfo info) {
        if (DownloadInfo.DOWNLOAD_OVER.equals(info.getDownloadStatus())) {
            patchFix();
        }
    }

    private void patchFix() {
        File patchFile = new File(HmmDownloadManager.FILE_PATH, "patch_signed_7zip.apk");
        if (patchFile.exists()&&SpUtilsOld.getValue(LibApplication.getAppContext(), SpKey.YSLOOK, false)) {
            try {
                Tinker.with(mContext).cleanPatch();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("????????????:????????????");
            TinkerManager.getInstance().applyPatch(getApplicationContext(), patchFile.getAbsolutePath());
            if (newUpdatePatchVersion != null) {
                SpUtils.store(mContext, SpKey.USER_PATCH, new Gson().toJson(newUpdatePatchVersion));
                newUpdatePatchVersion=null;
            }
        }
    }


    public void startOnlineVideoFloat() {
//
        if (this instanceof IsNeedVideo) {//????????????????????????

            IsNeedVideo isNeedVideo = (IsNeedVideo) this;
            if (!TextUtils.isEmpty(isNeedVideo.getToken()) && !"null".equals(isNeedVideo.getToken())) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    //System.out.println("??????????????????1");
                    FloatWindowManager.getInstance().applyOrShowFloatWindow(this);
                } else {
                    //System.out.println("??????????????????2");
                    if (!SettingsCompat.canDrawOverlays(mContext)) {
                        Toast.makeText(mContext, "???????????????????????????", Toast.LENGTH_SHORT).show();
                        SettingsCompat.manageDrawOverlays(mContext);
                    }
                }
                try {
                    WindowManager windowManager = getWindowManager(mContext);
                    int screenWidth = windowManager.getDefaultDisplay().getWidth();
                    int screenHeight = windowManager.getDefaultDisplay().getHeight();
                    if (mFloatingView == null) {
                        //System.out.println("??????????????????");
                        mFloatingView = new HmmFloatWindowView(mContext);
                        mFloatingView.setCourse_id(isNeedVideo.getCourseId());
                        mFloatingView.setLiveStatus(isNeedVideo.getLiveStatus());

                        txCloudVideoView = new TXCloudVideoView(this);


                        //?????? player ??????
                        mLivePlayer = new TXLivePlayer(mContext);
                        //?????? player ??????????????? view
                        mLivePlayer.setPlayerView(txCloudVideoView);
                        TXLivePlayConfig mPlayConfig = new TXLivePlayConfig();
                        //????????????
                        mPlayConfig.setAutoAdjustCacheTime(true);
                        mPlayConfig.setMinAutoAdjustCacheTime(1);
                        mPlayConfig.setMaxAutoAdjustCacheTime(5);
                        mLivePlayer.setConfig(mPlayConfig);

                        // ??????????????????
                        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
                        // ????????????????????????
                        mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);

                        mLivePlayer.setPlayListener(new ITXLivePlayListener() {
                            @Override
                            public void onPlayEvent(int event, Bundle param) {
                                if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                                    stopOnlineVideoFloat();
                                    Log.e(TAG, "????????????");
                                } else if (event == TXLiveConstants.PLAY_EVT_RTMP_STREAM_BEGIN) {
                                    Log.e(TAG, "????????????????????????????????????");
                                }
                                if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                                    Log.e(TAG, "??????????????????");
                                }
                                if (event == TXLiveConstants.PLAY_EVT_GET_PLAYINFO_SUCC) {
                                    Log.e(TAG, "??????????????????????????????");
                                }
                                if (event == TXLiveConstants.PLAY_ERR_FILE_NOT_FOUND) {
                                    stopOnlineVideoFloat();
                                    Log.e(TAG, "?????????????????????");
                                }
                                if (event == TXLiveConstants.PLAY_ERR_HEVC_DECODE_FAIL) {
                                    stopOnlineVideoFloat();
                                    Log.e(TAG, "H265 ????????????");
                                }
                                if (event == TXLiveConstants.PLAY_ERR_HLS_KEY) {
                                    stopOnlineVideoFloat();
                                    Log.e(TAG, "HLS ?????? key ????????????");
                                }
                                if (event == TXLiveConstants.PLAY_ERR_GET_PLAYINFO_FAIL) {
                                    stopOnlineVideoFloat();
                                    Log.e(TAG, "??????????????????????????????");
                                }
                                if (event == TXLiveConstants.PLAY_WARNING_SEVER_CONN_FAIL) {
                                    stopOnlineVideoFloat();
                                    Log.e(TAG, "????????????????????????????????? RTMP:// ?????????????????????");
                                }

                            }

                            @Override
                            public void onNetStatus(Bundle status) {
                            }
                        });
//                        mHtSdk = HtSdk.getInstance();
                        if (isNeedVideo.getVideoHandler() != null) {
                            mFloatingView.setHandler(isNeedVideo.getVideoHandler());
                        } else {
                            mFloatingView.setHandler(handlerVideoDefault);
                        }
                        mFloatingView.pptContainer.addView(txCloudVideoView);
//                        FrameLayout liveWaitView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.live_wait_layout, null);
//                        FrameLayout liveOverView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.live_over_layout, null);
//
//                        mHtSdk.setLiveWaitView(liveWaitView);//???????????????????????????view
//                        mHtSdk.setLiveOverView(liveOverView);//????????????????????????view
//                        mHtSdk.init(mFloatingView.pptContainer, mFloatingView.videoViewContainer, isNeedVideo.getToken());
//                        mHtSdk.setPauseInBackground(true);
//                        mHtSdk.onResume();
                        if (mFloatingViewParams == null) {
                            mFloatingViewParams = new WindowManager.LayoutParams();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//
                                mFloatingViewParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                            } else {
                                mFloatingViewParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                            }
//                        mFloatingViewParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                            mFloatingViewParams.format = PixelFormat.RGBA_8888;
                            mFloatingViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
                            mFloatingViewParams.gravity = Gravity.LEFT | Gravity.TOP;
                            mFloatingViewParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                            mFloatingViewParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            mFloatingViewParams.x = screenWidth;
                            mFloatingViewParams.y = (int) (screenHeight * 1.0 * 0.65);
                        }
                        mFloatingView.updateViewLayoutParams(mFloatingViewParams);
                        windowManager.addView(mFloatingView, mFloatingViewParams);
                        isShowFloat = true;
                        mLivePlayer.startPlay(isNeedVideo.getToken(), TXLivePlayer.PLAY_TYPE_VOD_HLS);
                        //System.out.println("?????????????????????");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (mLivePlayer != null) {
                        mLivePlayer.pause();
                        mLivePlayer.stopPlay(true);
                        txCloudVideoView.onDestroy();
                    }
                    isShowFloat = false;
                    mFloatingView = null;
                    mFloatingViewParams = null;
                    //????????????????????????Ex???????????????
                    startOnlineVideoFloatEx();
                }
                if(FloatWindowManager.isHideShow){
                    if (mLivePlayer != null) {
                        mLivePlayer.pause();
                        mLivePlayer.stopPlay(true);
                        txCloudVideoView.onDestroy();
                    }
                }

            }
        }
        //System.out.println("?????????????????????");

    }

    private WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }


    public void stopOnlineVideoFloat() {
        if (this instanceof IsNeedVideo) {//????????????????????????
            //System.out.println("????????????");
            try {
                SpUtils.store(LibApplication.getAppContext(), SpKey.LIVETMPCOURSEADDRESS, "");
                SpUtils.store(LibApplication.getAppContext(), SpKey.LIVETMPCOURSEID, "");
                mLivePlayer.pause();
                mLivePlayer.stopPlay(true);
                txCloudVideoView.onDestroy();
            } catch (Exception e) {
//                e.printStackTrace();
            }
            if (mFloatingView != null) {
                mFloatingView.removeAllViews();
                if (mFloatingView != null) {
                    try {
                        WindowManager windowManager = getWindowManager(mContext);
                        windowManager.removeView(mFloatingView);
                        mFloatingView.removeAllViews();
                        mFloatingView = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private View.OnClickListener mShareClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mShareDialog.dismiss();
            share(mPlatformMap.get(String.valueOf(v.getTag())), surl, sdes, stitle, sBitmap);
        }
    };


    /**
     * ??????
     *
     * @param shareMedia ????????????
     * @param url        ????????????
     * @param des        ??????
     * @param title      ??????
     */
    private void share(SHARE_MEDIA shareMedia, String url, String des, String title, Bitmap bitmap) {

        shareResult(shareMedia);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setThumb(new UMImage(mContext, bitmap));
        web.setDescription(des);
        new ShareAction(this)
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(this)
                .share();
    }

    /**
     * ??????
     *
     * @param shareMedia ????????????
     * @param url        ????????????
     * @param des        ??????
     * @param title      ??????
     */
    private void shareMin(SHARE_MEDIA shareMedia, String url, String des, String title, String path, String userName) {
        //System.out.println(url);
        UMMin umMin = new UMMin(url);
        umMin.setTitle(title);
        umMin.setThumb(new UMImage(mContext, sBitmap));
        umMin.setDescription(des);
        umMin.setPath(path);
        umMin.setUserName(userName);
        if (ChannelUtil.isRealRelease()) {

        } else {
            com.umeng.socialize.Config.setMiniPreView();
        }
        new ShareAction(this)
                .withMedia(umMin)
                .setPlatform(shareMedia)
                .setCallback(this)
                .share();
    }

    public void shareResult(SHARE_MEDIA shareMedia) {

    }



    private void showShareBottomDialog() {
        showShareBottomDialog(false);

    }

    private void showShareBottomDialog(boolean isMini) {
        if (isMini) {
            shareMin(SHARE_MEDIA.WEIXIN, surl, sdes, stitle, spath, suserName);
        } else {
            if (mShareDialog == null) {
                try {
                    mShareDialog = new AlertDialog.Builder(mContext).create();
                    View shareSheet = null;
                    shareSheet = LayoutInflater.from(mContext)
                            .inflate(R.layout.lib_video_share_sheet2, null);
                    shareSheet.findViewById(R.id.tv_wx).setOnClickListener(mShareClick);
                    shareSheet.findViewById(R.id.tv_timeline).setOnClickListener(mShareClick);
                    shareSheet.findViewById(R.id.tv_qq).setOnClickListener(mShareClick);
                    shareSheet.findViewById(R.id.tv_qzone).setOnClickListener(mShareClick);
                    shareSheet.findViewById(R.id.tv_sina).setOnClickListener(mShareClick);
                    shareSheet.findViewById(R.id.iv_share_close)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mShareDialog.dismiss();
                                }
                            });
                    mShareDialog.show();
                    mShareDialog.setContentView(shareSheet);
                    Window window = mShareDialog.getWindow();
                    if (window != null) {
                        window.setWindowAnimations(R.style.DialogAnim);
                        View decorView = window.getDecorView();
                        WindowManager.LayoutParams params = window.getAttributes();
                        params.width = WindowManager.LayoutParams.MATCH_PARENT;
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        params.gravity = Gravity.BOTTOM;
                        decorView.setPadding(0, 0, 0, 0);
                        window.setAttributes(params);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                mShareDialog.show();
            }
        }

    }


    public void setTransparentStatusBar(Activity activity) {
        //5.0?????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public StatusLayout.Status getNowStatus() {
        if (mStatusLayout != null) {

            return mStatusLayout.getStatus();
        }
        return null;
    }


    /**
     * ??????????????????id
     *
     * @return ??????id
     */
    protected abstract int getLayoutId();

    /**
     * ????????????
     */
    protected void findViews() {
    }

    /**
     * ?????????
     *
     * @param savedInstanceState onCreate??????????????????
     */
    protected abstract void init(Bundle savedInstanceState);


    @Override
    public void showLoading() {
        if (mStatusLayout != null) {
            mStatusLayout.updateStatus(StatusLayout.Status.STATUS_LOADING);
        }

    }


    @Override
    public void showNetErr() {
        if (mStatusLayout != null) {
            mStatusLayout.updateStatus(StatusLayout.Status.STATUS_NET_ERR);
        }
    }

    @Override
    public void showEmpty() {
        if (mStatusLayout != null) {
            mStatusLayout.updateStatus(StatusLayout.Status.STATUS_EMPTY);
        }
    }

    public void setTopBar(TopBar topBar) {
        topBar.setTopBarListener(this);
    }

    public void setStatusLayout(StatusLayout statusLayout) {
        mStatusLayout = statusLayout;
        mStatusLayout.setOnNetRetryListener(this);
    }


    @Override
    public void showToast(CharSequence msg) {
        Toast roast = Toast.makeText(LibApplication.getAppContext(), msg, Toast.LENGTH_SHORT);
        if (cantoast) {
            cantoast = false;
            //System.out.println("??????Toast");
            roast.show();
            changeStatus(2);
            //handlerVideoDefault.postDelayed(() -> BaseActivity.cantoast = true, 500);
        }
    }

    public void showToastIgnoreLife(final CharSequence msg) {
        Toast roast = Toast.makeText(LibApplication.getAppContext(), msg, Toast.LENGTH_SHORT);
        if (cantoast) {
            cantoast = false;
            //System.out.println("??????Toast");
            roast.show();
            //handlerVideoDefault.postDelayed(() -> BaseActivity.cantoast = true, 2000);
            changeStatus(3);
        }

    }

    private void changeStatus(long initialDelay) {
        Observable.intervalRange(0,1,initialDelay, 0, TimeUnit.SECONDS,Schedulers.io())
                .to(RxLifecycleUtils.bindLifecycle(this))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Long aLong) { }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        cantoast = true;
                    }
                });
    }

    boolean isShareIng = false;

    /**
     * ???????????????????????????true
     *
     * @param shareIng
     */
    public void setShareIng(boolean shareIng) {
        isShareIng = shareIng;
    }

    @Override
    public void finish() {
        stopOnlineVideoFloat();
        super.finish();
    }

    @Override
    public void onBackBtnPressed() {
        finish();
    }

    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onRequestStart(Disposable disposable) {
        addDisposable(disposable);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        try {
////            com.healthy.library.businessutil.GlideCopy.with(getApplicationContext()).pauseRequests();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        if (mCompositeDisposable != null) {
////            mCompositeDisposable.dispose();
////        }
//    }

    @Override
    public void showContent() {
        if (mStatusLayout != null) {
            mStatusLayout.updateStatus(StatusLayout.Status.STATUS_CONTENT);
        }
    }

    @Override
    public void showDataErr() {
        if (mStatusLayout != null) {
            mStatusLayout.updateStatus(StatusLayout.Status.STATUS_DATA_ERR);
        }
    }

    @Override
    public void onRetryClick() {
        getData();
    }

    @Override
    public void onRequestFinish() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showCoupon(final CouponShowBean msg) {
        //System.out.println("???????????????Activity:"+getClass().getName());
        if (FrameActivityManager.checkNowIsTopActivity(this)) {
            if (this instanceof IsNoNeedCardDialog) {//??????????????????????????? ?????????????????????????????????
                return;
            }
            if (isFinishing()) {//????????? ???????????? ?????????????????????
                EventBus.getDefault().post(msg);
                return;
            }
//            if(cardPackDialog!=null){
//                cardPackDialog.dismiss();
//            }
            cardPackDialog = CardPackDialog.newInstance();
            cardPackDialog.setPackId(msg.actId);
            cardPackDialog.setDetailId(msg.detailId);
            cardPackDialog.show(((FragmentActivity) FrameActivityManager.instance().topActivity())
                    .getSupportFragmentManager(), "?????????");
        }


    }

    @Override
    public void getData() {

    }

    /**
     * ??????????????????????????????.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {  //????????????????????????????????????
                View v = getCurrentFocus();      //???????????????????????????,ps:??????????????????????????????????????????????????????
                if (isShouldHideKeyboard(v, ev)) { //??????????????????????????????????????????????????????
                    hideKeyboard(v.getWindowToken());   //????????????
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * ??????InputMethodManager??????????????????
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * ??????EditText???????????????????????????????????????????????????????????????????????????????????????????????????EditText??????????????????
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //???????????????????????????????????????EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //????????????????????????????????????????????????
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // ?????????????????????EditText??????????????????????????????????????????
                return false;
            } else {
                return true;
            }
        }
        // ??????????????????EditText?????????
        return false;
    }

    @Override
    public void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        super.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public final void onStart(SHARE_MEDIA shareMedia) {
        isShareIng = true;
    }

    @Override
    public final void onResult(SHARE_MEDIA shareMedia) {
        Toast.makeText(LibApplication.getAppContext(), "????????????", Toast.LENGTH_SHORT).show();
    }

    @Override
    public final void onError(SHARE_MEDIA shareMedia, Throwable throwable) {
        Toast.makeText(LibApplication.getAppContext(), "????????????", Toast.LENGTH_SHORT).show();
    }

    @Override
    public final void onCancel(SHARE_MEDIA shareMedia) {
        Toast.makeText(LibApplication.getAppContext(), "???????????????", Toast.LENGTH_SHORT).show();
    }

    private final BroadcastReceiver mDownloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //System.out.println("????????????");
            openFile(intent, context);
        }
    };

    private void openFile(Intent intent, final Context context) {
        String action = intent.getAction();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            // ??????
            long resid = SpUtils.getValue(this, SpKey.USE_DOWN, 0L);
            if (downloadId == resid) {
                //id??????
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                Cursor c = mDownloadManager.query(query);
                if (c != null && c.moveToFirst()) {
                    int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                        installApk(downloadId);
                    } else {
                        //System.out.println("???????????????");
                        mDownloadManager.remove(downloadId);
                        SpUtils.store(this, SpKey.USE_DOWN, 0L);
                    }
                } else {
                    //System.out.println("???????????????3");
                    mDownloadManager.remove(downloadId);
                    SpUtils.store(this, SpKey.USE_DOWN, 0L);
                }
                if (c != null && !c.isClosed()) {
                    c.close();

                }
            }

        }
    }



    public static File queryDownloadedApk(Context context, long downloadId) {
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }

    private void installApk(long downloadId) {
        if (!SpUtils.getValue(LibApplication.getAppContext(), "ISAPKINSTALL", false)) {
            SpUtils.store(LibApplication.getAppContext(), "ISAPKINSTALL", true);
            long completeDownLoadId = downloadId;

            SpUtils.store(LibApplication.getAppContext(), SpKey.USE_DOWN, 0L);
            Uri uri;
            Intent intentInstall = new Intent();
            intentInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentInstall.setAction(Intent.ACTION_VIEW);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // 6.0??????
                uri = mDownloadManager.getUriForDownloadedFile(completeDownLoadId);
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { // 6.0 - 7.0
                File apkFile = queryDownloadedApk(LibApplication.getAppContext(), completeDownLoadId);
                uri = Uri.fromFile(apkFile);
            } else { // Android 7.0 ??????
                uri = FileProvider.getUriForFile(LibApplication.getAppContext(),
                        "com.health.client.fileprovider",
                        new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "?????????.apk"));
                intentInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            //System.out.println("????????????:"+uri);
            intentInstall.setDataAndType(uri, "application/vnd.android.package-archive");
            LibApplication.getAppContext().startActivity(intentInstall);
        }
    }

    //????????????????????????
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public void delayKillReal(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    System.out.println("ActivityCallbacks????????????????????????");
                    FrameActivityManager.instance().appExit(LibApplication.getAppContext());
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public static void hideSoftKeyBoard(final View v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }, 100);
    }

    public void getUserStatus(){
        if (tongLianPhonePresenterCopy!=null)
        tongLianPhonePresenterCopy.getTongLianPhoneStatus(new SimpleHashMapBuilder<>());
    }
}
