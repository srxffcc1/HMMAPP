package com.health.index.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.health.index.contract.WebContract;
import com.health.index.model.CommentModel;
import com.health.index.model.IndexMonTool;
import com.health.index.model.ShareModel;
import com.health.index.presenter.WebPresenter;
import com.healthy.library.BuildConfig;
import com.healthy.library.activity.AndroidBug5497Workaround;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.contract.LotteryContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.ActivityModel;
import com.healthy.library.model.LotteryModel;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.presenter.LotteryPresenter;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.ResUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.WebViewSetting;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.jvm.JvmField;

/**
 * @author Li
 * @date 2019/04/22 18:48
 * @des WebView
 */
@SuppressLint("InflateParams")
@Route(path = IndexRoutes.INDEX_WEBVIEWSINGLE)
public class WebViewActivitySingle extends BaseActivity implements IsFitsSystemWindows, WebContract.View, LotteryContract.View {

    private static final String TAG = "WebViewActivitySingle";
    @Autowired
    String course_id;
    @Autowired
    String liveStatus;
    @Autowired
    String scheme;//保留字段  Lottery


    @Autowired
    String url;
    @Autowired
    String title;
    @Autowired
    Boolean isShowTopBar = true; //默认显示

    @Autowired
    Boolean isShowShare = false; //是否显示分享按钮

    @Autowired
    String activity; //分享的数据

    LotteryPresenter lotteryPresenter;
    private TextView mTvTitle;
    private X5WebView mWebView;
    private Map<String, SHARE_MEDIA> mPlatformMap;
    private Group mGroupShare;
    private StatusLayout mStatusLayout;
    private static final int MSG_SHARE = 4;
    private static final int MSG_ERR_MSG = 1;
    private static final int MSG_TOKEN_INVALID = 2;
    private ImageView mIvCollection;
    private String mCollectId;
    private String mKnowledgeId;
    private ImageView mIvClose;
    private ImageView iv_share;
    private WebPresenter mPresenter;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;

    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private IX5WebChromeClient.CustomViewCallback customViewCallback;
    private ImageView mIvBack;
    private ConstraintLayout mWebTitleCl;
    private FrameLayout mContentLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_web_view;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SHARE:
                    ShareModel model = (ShareModel) msg.obj;
                    WebViewActivitySingle.this.share2(mPlatformMap.get(model.getPlatform()),
                            model.getUrl(), model.getDes(), model.getTitle());
                    break;
                case MSG_ERR_MSG:
                    showToast((String) msg.obj);
                    break;
                case MSG_TOKEN_INVALID:
                    showToast("登录失效，请重新登录");
                    ARouter.getInstance().build(AppRoutes.APP_LOGIN)
                            .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK)
                            .navigation();
                    break;
                default:
                    break;
            }

        }
    };
    /**
     * 用来注入token
     */
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);

        }

        public void openFileChooser(ValueCallback<Uri> valueCallback) {
            uploadMessage = valueCallback;
            openImageChooserActivity(null);
        }

        // For Android  >= 3.0
        public void openFileChooser(ValueCallback valueCallback, String acceptType) {
            uploadMessage = valueCallback;
            openImageChooserActivity(new String[]{acceptType});
        }

        //For Android  >= 4.1
        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
            uploadMessage = valueCallback;
            openImageChooserActivity(new String[]{acceptType});
        }

        // For Android >= 5.0
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            uploadMessageAboveL = filePathCallback;
            String[] acceptType = fileChooserParams.getAcceptTypes();
            System.out.println("接受参数:" + acceptType.length);
            openImageChooserActivity(acceptType);
            return true;
        }

        @Override
        public View getVideoLoadingProgressView() {
            FrameLayout frameLayout = new FrameLayout(WebViewActivitySingle.this);
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            return frameLayout;
        }

        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
            showCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            hideCustomView();
        }

    };

    /**
     * 视频播放全屏
     **/
    private void showCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        WebViewActivitySingle.this.getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(WebViewActivitySingle.this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    @Autowired
    boolean isNeedRef = false;
    private boolean isFirstLoad = true;


    private void openImageChooserActivity(String[] acceptType) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");//图片上传
//        i.setType("file/*");//文件上传
        if (acceptType != null && acceptType.length == 1) {
            i.setType(acceptType[0]);//文件上传
        } else {
            i.setType("*/*");//文件上传
        }
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }


    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (TextUtils.isEmpty(title)) {
                mTvTitle.setText(view.getTitle());
            }
            showContent();
            if (mWebView != null) {
                mWebView.getSettings().setBlockNetworkImage(false);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //https://hmmgfit//receive?lotteryActivityId=78448856115200000&prizeProfileId=39
            if (!TextUtils.isEmpty(url)) {
                if (url.contains("https://hmmgfit//receive")) { //抽奖成功 去领奖跳转列表
                    ARouter.getInstance()
                            .build(MineRoutes.MINE_AWARD_CENTER)
                            .navigation();
                    return true;
                }
            }
            return super.shouldOverrideUrlLoading(view, url);

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    };


    @Override
    protected void findViews() {
        //mWebView = findViewById(R.id.web_view);
        mContentLayout = findViewById(R.id.content_layout);
        mWebView = new X5WebView(this, null);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mContentLayout.addView(mWebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mGroupShare = findViewById(R.id.group_share);
        mStatusLayout = findViewById(R.id.layout_status);
        mIvCollection = findViewById(R.id.iv_collection);
        mWebTitleCl = findViewById(R.id.webTitleLL);
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mIvClose = findViewById(R.id.iv_close);
        iv_share = findViewById(R.id.iv_share);
        AndroidBug5497Workaround.assistActivity(this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
        lotteryPresenter = new LotteryPresenter(mContext, this);
        setStatusLayout(mStatusLayout);
        //System.out.println("获得的title" + title);
        checkTopShow();
        mPresenter = new WebPresenter(mContext, this);
        mPlatformMap = new HashMap<>();
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_wx), SHARE_MEDIA.WEIXIN);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_circle), SHARE_MEDIA.WEIXIN_CIRCLE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qq), SHARE_MEDIA.QQ);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qzone), SHARE_MEDIA.QZONE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_sina), SHARE_MEDIA.SINA);


        try {
            android.webkit.WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        WebViewSetting.setWebViewParam(mWebView, this);
        mWebView.addJavascriptInterface(new JavaScriptFunction(), "JavaScriptFunction");
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mWebViewClient);

        showLoading();
        getData();
        if (isShowShare&&activity!=null) {
            iv_share.setVisibility(View.VISIBLE);
            iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeckShareDialog seckShareDialog = SeckShareDialog.newInstance();
                    seckShareDialog.setActivityDialog(4, 0, resolveToolData(activity));
                    seckShareDialog.setExtraMap(new SimpleHashMapBuilder<String, String>().puts("type", "8").puts("scheme", "VoteDetail"));
                    seckShareDialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "voteShare");
                }
            });
        } else {
            iv_share.setVisibility(View.INVISIBLE);
        }
    }

    private ActivityModel resolveToolData(String obj) {
        ActivityModel result = new ActivityModel();
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
            Type type = new TypeToken<ActivityModel>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private void checkTopShow() {
        if (isShowTopBar) {
            mTvTitle.setText(title);
        } else {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mStatusLayout.getLayoutParams();
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.topToBottom = -1;
            mStatusLayout.setLayoutParams(layoutParams);
            mIvBack.setImageResource(R.drawable.ic_back_white);
            mTvTitle.setVisibility(View.GONE);
            mIvClose.setVisibility(View.GONE);
            //mWebTitleCl.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void getData() {
        if (!TextUtils.isEmpty(url)) {
            if (!url.contains("http")) {//说明不是标准url 缺少http协议 进行手动拼接
                url = "http://" + url;
            }
            onGetRealUrlSuccess(url);
        } else {
            if (!TextUtils.isEmpty(scheme)) {
                if (scheme.contains("Lottery")) {//说明是抽奖
                    lotteryPresenter.getLotteryInfo(new SimpleHashMapBuilder<String, Object>()
                            .puts("merchantId", SpUtils.getValue(mContext, SpKey.CHOSE_MC)));
                }
            }
        }

    }

    /**
     * 分享
     *
     * @param shareMedia 分享平台
     * @param url        链接地址
     * @param des        描述
     * @param title      标题
     */
    private void share2(SHARE_MEDIA shareMedia, String url, String des, String title) {

        if (shareMedia == SHARE_MEDIA.QQ) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("type", "QQ");
            MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap);
        }
        if (shareMedia == SHARE_MEDIA.QZONE) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("type", "QQ空间");
            MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap);

        }
        if (shareMedia == SHARE_MEDIA.WEIXIN) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("type", "微信");
            MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap);
        }
        if (shareMedia == SHARE_MEDIA.WEIXIN_CIRCLE) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("type", "朋友圈");
            MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap);
        }

        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setThumb(new UMImage(mContext, BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.index_share_humb))));
        web.setDescription(des);
        new ShareAction(WebViewActivitySingle.this)
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(this)
                .share();
    }

    @Override
    public void onBackPressed() {
        if (customView != null) {
            hideCustomView();
        } else {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
        }
    }

    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    public void onResume() {
        super.onResume();
        //不加 出现选择图片无法上传上去。
        mWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        mContentLayout.removeAllViews();
        mWebView.destroy();
        mWebView = null;
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public void onUpdateCollectedStatusSuccess(String collectStatus) {
        mIvCollection.setSelected("1".equals(collectStatus));
        mWebView.reload();
    }

    @Override
    public void onCheckCollectedStatusSuccess(String collectStatus, String collectId,
                                              String title, String des) {
        mGroupShare.setVisibility(View.VISIBLE);
        mIvCollection.setSelected("1".equals(collectStatus));
        mCollectId = collectId;
    }

    @Override
    public void onGetRealUrlSuccess(String url) {
        this.url = url;
        System.out.println("访问web地址：" + url);
        mWebView.loadUrl(url);
        mWebView.onResume();
    }

    @Override
    public void onAddCommentSuccess(String result) {

    }

    @Override
    public void onAddPraiseSuccess(String result) {

    }

    @Override
    public void onAddReplySuccess(String result) {

    }

    @Override
    public void getCommentListSuccess(List<CommentModel> indexVideos, PageInfoEarly pageInfoEarly) {

    }

    @Override
    public void onLotteryInfoSuccess(LotteryModel lotteryModel) {
        String api = "http://192.168.10.181:8000/lottery.html";
        if (!TextUtils.isEmpty(SpUtils.getValue(mContext, UrlKeys.H5_LOTTERY_URL))) {
            api = SpUtils.getValue(mContext, UrlKeys.H5_LOTTERY_URL);
        }
        url = api + "?id=" + lotteryModel.getId() + "&token=" + SpUtils.getValue(mContext, SpKey.TOKEN);
        isShowTopBar = false;
        checkTopShow();
        getData();
    }

    public class JavaScriptFunction {
        @JavascriptInterface
        public void share(final String platform, final String url, final String des, final String title) {
            // 1 微信 2 朋友圈 3 qq 4 空间
            ShareModel shareModel = new ShareModel();
            shareModel.setPlatform(platform);
            shareModel.setTitle(title);
            shareModel.setDes(des);
            shareModel.setUrl(url);

            Message message = new Message();
            message.what = MSG_SHARE;
            message.obj = shareModel;
            mHandler.sendMessage(message);
        }

        @JavascriptInterface
        public void tokenInvalid() {
            mHandler.sendEmptyMessage(MSG_TOKEN_INVALID);
        }

        @JavascriptInterface
        public void showErrMsg(String msg) {
            Message message = new Message();
            message.what = MSG_ERR_MSG;
            message.obj = msg;
            mHandler.sendMessage(message);
        }

    }

    private AlertDialog mShareDialog;


    private View.OnClickListener mShareClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    public void collection(View view) {
        mPresenter.updateCollectedStatus(mKnowledgeId, view.isSelected() ? "2" : "1", mCollectId);
    }

    public void close(View view) {
        finish();
    }

    public void webBack(View view) {
        if (customView != null) {
            hideCustomView();
        } else if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (customView != null) {
                hideCustomView();
            } else if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) {
                return;
            }
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            // Uri result = (((data == null) || (resultCode != RESULT_OK)) ? null : data.getData());
            if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(WebChromeClient.FileChooserParams
                        .parseResult(resultCode, data));
                uploadMessageAboveL = null;
//                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        } else {
            //这里uploadMessage跟uploadMessageAboveL在不同系统版本下分别持有了
            //WebView对象，在用户取消文件选择器的情况下，需给onReceiveValue传null返回值
            //否则WebView在未收到返回值的情况下，无法进行任何操作，文件选择器会失效
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            } else if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            }
        }
    }

    // 4. 选择内容回调到Html页面
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        mWebView.setVisibility(View.VISIBLE);
    }
}