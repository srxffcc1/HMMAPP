package com.health.index.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.index.R;
import com.health.index.contract.WebContract;
import com.health.index.model.CommentModel;
import com.health.index.model.ShareModel;
import com.health.index.presenter.WebPresenter;
import com.healthy.library.BuildConfig;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.MallRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.ClipboardUtils;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.JsoupCopy;
import com.healthy.library.utils.ResUtil;
import com.healthy.library.utils.ResUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.WebViewSetting;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/04/22 18:48
 * @des WebView
 */
@SuppressLint("InflateParams")
@Route(path = IndexRoutes.INDEX_WEBVIEW)
public class WebViewActivity extends BaseActivity implements WebContract.View {

    @Autowired
    String course_id;
    @Autowired
    String liveStatus;


    @Autowired
    String url;
    @Autowired
    String title;


    @Autowired
    boolean isinhome = true;//????????????token??????
    @Autowired
    boolean leftnow = false;//?????????????????????
    @Autowired
    boolean videoshop = false;//????????????????????? ????????????????????????

    @Autowired
    boolean doctorshop = false;//???????????? ???????????????????????????

    @Autowired
    boolean needShare = false;
    @Autowired
    boolean needfindcollect = false;//??????????????????


    private TextView mTvTitle;
    private X5WebView mWebView;
    private Map<String, SHARE_MEDIA> mPlatformMap;
    //private StatusLayout mStatusLayout;
    private static final int MSG_SHARE = 4;
    private static final int MSG_ERR_MSG = 1;
    private static final int MSG_TOKEN_INVALID = 2;
    private String mCollectId;
    private String mKnowledgeId;

    //?????????????????? ??????????????????????????????
    @Autowired
    String stitle;
    @Autowired
    String sdes = " ";
    @Autowired
    String surl;
    @Autowired
    String zbitmap;

    Bitmap sbitmapFianl;

    //????????????????????????
    @Autowired
    String otherBitmap;

    @Autowired
    String otherFun;
    @Autowired
    String otherFunString;
    @Autowired
    String otherMap;

    Map<String, String> otherMapReal = new HashMap<>();


    private WebPresenter mPresenter;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SHARE:
                    ShareModel model = (ShareModel) msg.obj;
                    WebViewActivity.this.share2(mPlatformMap.get(model.getPlatform()),
                            model.getUrl(), model.getDes(), model.getTitle());
                    break;
                case MSG_ERR_MSG:
                    showToast((String) msg.obj);
                    break;
                case MSG_TOKEN_INVALID:
                    showToast("??????????????????????????????");
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
     * ????????????token
     */
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (isinhome) {
                String token = SpUtils.getValue(mContext, SpKey.TOKEN);
                String js = "localStorage.setItem('token', '" + token + "')";
                mWebView.evaluateJavascript(js, null);
            }

        }

        public void openFileChooser(ValueCallback<Uri> valueCallback) {
            uploadMessage = valueCallback;
            openImageChooserActivity();
        }

        // For Android  >= 3.0
        public void openFileChooser(ValueCallback valueCallback, String acceptType) {
            uploadMessage = valueCallback;
            openImageChooserActivity();
        }

        //For Android  >= 4.1
        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
            uploadMessage = valueCallback;
            openImageChooserActivity();
        }

        // For Android >= 5.0
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            uploadMessageAboveL = filePathCallback;
            openImageChooserActivity();
            return true;
        }
    };
    private ImageView ivOther;
    private ImageView ivShare;
    private ImageView ivCollection;
    private ProgressBar mProgressBar;
    private FrameLayout mContentLayout;


    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");//????????????
//        i.setType("file/*");//????????????
        i.setType("*/*");//????????????
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }


    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(final WebView view, String url) {
            super.onPageFinished(view, url);
            if (TextUtils.isEmpty(title)) {//??????????????????webview?????????
                if(view.getTitle()!=null&&view.getTitle().equals("????????????")){
                    mTvTitle.setText("??????");
                }else {
                    mTvTitle.setText(view.getTitle());
                }
            }
            //System.out.println("????????????");
            //showContent();
            mProgressBar.setVisibility(View.GONE);
            mWebView.getSettings().setBlockNetworkImage(false);
            if (needShare && TextUtils.isEmpty(sdes)) {//????????????webview??????
                if (mWebView != null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mWebView != null) {
                                mWebView.loadUrl("javascript:window.java_obj.getSource(document.documentElement.outerHTML);void(0)");
                            }
                        }
                    }, 1000);
                }
            }

            if (isinhome && needfindcollect) {//???????????? ?????? ??????
                try {
                    String pattern = "?id=";
                    String[] resultatrray = url.split("\\?");
                    mKnowledgeId = resultatrray.length > 1 ? resultatrray[1] : "";
                    try {
                        mKnowledgeId = mKnowledgeId.split("&")[0].trim().split("=")[1];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mPresenter.checkCollectedStatus(mKnowledgeId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mProgressBar.setVisibility(View.VISIBLE);
            // ???????????????????????????????????????????????????url
            // ????????????scheme?????????????????? & authority??????????????????????????????????????????
            //????????????????????? url = "js://webview?arg1=111&arg2=222"?????????????????????????????????????????????
            if (videoshop) {
                Uri uri = Uri.parse(url);
//            // ??????url????????? = ??????????????? js ??????
//            // ???????????????????????????
                //System.out.println("?????????url???" + uri);
                if (uri.toString().contains("addBargain")) {//
                    // ?????? authority  = ???????????????????????? webview????????????????????????????????????
                    // ????????????url,??????JS????????????Android???????????????
                    if ("1".equals(uri.getQueryParameter("type"))) {//????????????????????? ???????????????
//                        String goodsId = uri.getQueryParameter("goodsId");
//                        ARouter.getInstance()
//                                .build(MallRoutes.MALL_ORDER)
//                                .withString("goodsId", goodsId + "")
//                                .withString("orderType", "3")
//                                .navigation();
                        passToGoods(uri);
                    }
                    return true;
                }
            }
            if (doctorshop) {
                //System.out.println("?????????url???" + url);
                return handlerUrl(view, Uri.parse(url));
            }
            return super.shouldOverrideUrlLoading(view, url);

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            mProgressBar.setVisibility(View.VISIBLE);
            // ???????????????????????????????????????????????????url
            // ????????????scheme?????????????????? & authority??????????????????????????????????????????
            //????????????????????? url = "js://webview?arg1=111&arg2=222"?????????????????????????????????????????????
            if (videoshop) {
                Uri uri = request.getUrl();
//            // ??????url????????? = ??????????????? js ??????
//            // ???????????????????????????
                //System.out.println("?????????url???" + uri);
                if (uri.toString().contains("addBargain")) {//
                    // ?????? authority  = ???????????????????????? webview????????????????????????????????????
                    // ????????????url,??????JS????????????Android???????????????
                    if ("1".equals(uri.getQueryParameter("type"))) {//????????????????????? ???????????????
//                        String goodsId = uri.getQueryParameter("goodsId");
//                        ARouter.getInstance()
//                                .build(MallRoutes.MALL_ORDER)
//                                .withString("goodsId", goodsId + "")
//                                .withString("orderType", "3")
//                                .navigation();
                        passToGoods(uri);
                    }
                    return true;
                }
            }
            if (doctorshop) {
                //System.out.println("?????????url???" + request.getUrl());
                return handlerUrl(view, request.getUrl());
            }

            return super.shouldOverrideUrlLoading(view, request);
        }
    };

    private void passToGoods(Uri uri) {
        String goodsType = uri.getQueryParameter("goodsType");
        if (goodsType != null && "2".equals(goodsType)) {//??????
            String goodsId = uri.getQueryParameter("goodsId");
            ARouter.getInstance()
                    .build(ServiceRoutes.SERVICE_DETAIL)
                    .withString("id", goodsId)
                    .navigation();
        } else {
            String goodsId = uri.getQueryParameter("goodsId");
            try {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("goodsId", goodsId)
                        .navigation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean handlerUrl(WebView webView, Uri url) {
        if (url.toString().contains(WebConstants.WX_PAY_URL)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(url);
            startActivity(intent);
            return true;
        } else if (url.toString().contains(WebConstants.Al_PAY_URL)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(url);
            startActivity(intent);
            return true;
        } else if (url.toString().contains("wx/short/byclass")) {//????????????????????????
            ClipboardUtils.copy(mContext, url.toString());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(mContext, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }, 500);
            return false;
        } else {
            Map<String, String> headerParams = new HashMap<>();
            headerParams.put("Referer", WebConstants.WEB_DOMAIN);
            webView.loadUrl(url.toString(), headerParams);
            return true;
        }
    }

    private void initView() {
        ivOther = (ImageView) findViewById(R.id.iv_other);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivCollection = (ImageView) findViewById(R.id.iv_collection);
    }

    public final class InJavaScriptLocalObj {
        //??????????????????????????????,???????????????
        @JavascriptInterface
        public void getSource(String html) {
            //??????html??????????????????

            Document doc = JsoupCopy.parse(html);
            Elements pngs = null;
            pngs = doc.select("img[src]");
            for (int i = 0; i < pngs.size(); i++) {
                String src2 = pngs.get(0).attr("src");//??????src???????????????
                if (i == 0) {
                    com.healthy.library.businessutil.GlideCopy.with(mContext).load(src2)
                            .placeholder(R.drawable.img_1_1_default2)
                            .error(R.drawable.img_1_1_default)
                            
                            .into(new SimpleTarget<Drawable>() {

                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    sbitmapFianl = DrawableUtils.drawableToBitmap(resource);
                                }
                            });
                }
            }
//            //System.out.println(JsoupCopy.parse(html).body().html());
            try {
                if (!TextUtils.isEmpty(JsoupCopy.parse(html).body().getElementById("title").text())) {
                    stitle = JsoupCopy.parse(html).body().getElementById("title").text();
                } else {
                    stitle = title;
                }
            } catch (Exception e) {
                stitle = title;
                e.printStackTrace();
            }

            try {
                if (!TextUtils.isEmpty(JsoupCopy.parse(html).body().getElementById("content").text())) {
                    sdes = JsoupCopy.parse(html).body().getElementById("content").text();
                } else {
                    sdes = JsoupCopy.parse(html).body().text();
                }
            } catch (Exception e) {
                sdes = JsoupCopy.parse(html).body().text();
                e.printStackTrace();
            }


        }
    }


    public class WebConstants {

        public static final String WEB_DOMAIN = "https://h5.yewyw.com/vue/prePayH5Web";
        public static final String WX_PAY_URL = "weixin://wap/pay";
        public static final String Al_PAY_URL = "alipays://platformapi/startApp";
    }


    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_web_view_z;
    }

    @Override
    protected void findViews() {
        //mWebView = findViewById(R.id.web_view);
        mContentLayout = findViewById(R.id.content_layout);
        mWebView = new X5WebView(this, null);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mContentLayout.addView(mWebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        //mStatusLayout = findViewById(R.id.layout_status);
        mProgressBar = findViewById(R.id.progressBar);
        mTvTitle = findViewById(R.id.tv_title);
        initView();
    }

    public void showShareBottomDialog2(View view) {

        if (needShare && TextUtils.isEmpty(sdes)) {//????????????webview??????
            mWebView.loadUrl("javascript:window.java_obj.getSource(document.documentElement.outerHTML);void(0)");
        }


        if (mShareDialog == null) {
            try {
                mShareDialog = new AlertDialog.Builder(mContext).create();
                View shareSheet = LayoutInflater.from(mContext)
                        .inflate(R.layout.lib_video_share_sheet2, null);
                shareSheet.findViewById(R.id.iv_share_close)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mShareDialog.dismiss();
                            }
                        });
                shareSheet.findViewById(R.id.tv_wx).setOnClickListener(mShareClick);
                shareSheet.findViewById(R.id.tv_timeline).setOnClickListener(mShareClick);
                shareSheet.findViewById(R.id.tv_qq).setOnClickListener(mShareClick);
                shareSheet.findViewById(R.id.tv_qzone).setOnClickListener(mShareClick);
                shareSheet.findViewById(R.id.tv_sina).setOnClickListener(mShareClick);

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

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        //setStatusLayout(mStatusLayout);

        //System.out.println("?????????title" + title);
        mTvTitle.setText(title);
        mPresenter = new WebPresenter(mContext, this);
        mPlatformMap = new HashMap<>();
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_wx), SHARE_MEDIA.WEIXIN);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_circle), SHARE_MEDIA.WEIXIN_CIRCLE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qq), SHARE_MEDIA.QQ);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qzone), SHARE_MEDIA.QZONE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_sina), SHARE_MEDIA.SINA);

        //WebSettings webSettings = mWebView.getSettings();
        try {
            android.webkit.WebView.setWebContentsDebuggingEnabled(com.healthy.library.BuildConfig.DEBUG);
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (doctorshop) {

        }
        /*if (videoshop) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }*/

        if (needShare) {
            ivShare.setVisibility(View.VISIBLE);
        }
        if (needfindcollect) {
            ivCollection.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(zbitmap)) {
            sbitmapFianl = BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.index_share_humb));
        } else {
            //System.out.println("?????????bitmap??????" + zbitmap);
            if (!zbitmap.contains("R.")) {
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(zbitmap)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)
                        
                        .into(new SimpleTarget<Drawable>() {

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                //System.out.println("???????????????");
                                sbitmapFianl = DrawableUtils.drawableToBitmap(resource);
                            }
                        });
            } else {

                sbitmapFianl = BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(), ResUtil.getInstance().getResourceId(zbitmap)));
            }
        }
        if (TextUtils.isEmpty(stitle)) {
            stitle = title;
        }
        if (!TextUtils.isEmpty(otherBitmap)) {
            ivOther.setImageResource(ResUtil.getInstance().getResourceId(otherBitmap));
            ivOther.setVisibility(View.VISIBLE);
            String[] arrayLine = otherMap.split("-");
            for (int i = 0; i < arrayLine.length; i++) {
                if (!TextUtils.isEmpty(arrayLine[i].trim())) {
                    String[] array = arrayLine[i].split(":");
                    otherMapReal.put(array[0].trim(), array[1].trim());
                }
            }


            ivOther.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("Pass".equals(otherFun)) {//????????????
                        Postcard postcard = ARouter.getInstance()
                                .build(otherFunString);
                        for (Map.Entry<String, String> entry : otherMapReal.entrySet()) {
                            postcard.withString(entry.getKey(), entry.getValue());
                        }
                        postcard.navigation();
                    }
                }
            });
        }
        if (needShare && TextUtils.isEmpty(sdes)) {
            mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        }
        //??????WebView????????????
        WebViewSetting.setWebViewParam(mWebView, this);
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mWebView.addJavascriptInterface(new JavaScriptFunction(), "JavaScriptFunction");
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mWebViewClient);

        //showLoading();
        getData();
    }

    @Override
    public void getData() {
        if (!TextUtils.isEmpty(url)) {
            if (!url.contains("http")) {//??????????????????url ??????http?????? ??????????????????
                url = "http://" + url;
            }
            onGetRealUrlSuccess(url);
            if (TextUtils.isEmpty(surl)) {
                surl = url;
            }
        } else {

        }

    }

    /**
     * ??????
     *
     * @param shareMedia ????????????
     * @param url        ????????????
     * @param des        ??????
     * @param title      ??????
     */
    private void share2(SHARE_MEDIA shareMedia, String url, String des, String title) {

        if (shareMedia == SHARE_MEDIA.QQ) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("type", "QQ");
            MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap);
        }
        if (shareMedia == SHARE_MEDIA.QZONE) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("type", "QQ??????");
            MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap);

        }
        if (shareMedia == SHARE_MEDIA.WEIXIN) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("type", "??????");
            MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap);
        }
        if (shareMedia == SHARE_MEDIA.WEIXIN_CIRCLE) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("type", "?????????");
            MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap);
        }


        UMWeb web = new UMWeb(url);
        web.setTitle(title);
        web.setThumb(new UMImage(mContext, sbitmapFianl));
        web.setDescription(des);
        new ShareAction(WebViewActivity.this)
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(this)
                .share();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack() && !leftnow) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    public void onResume() {
        super.onResume();
        if (videoshop) {
            if (TextUtils.isEmpty(course_id) || "null".equals(course_id)) {
                //System.out.println("??????id?????????");
                mWebView.loadUrl(url);
                mWebView.onResume();
                return;
            }
            if (!TextUtils.isEmpty(course_id)) {
                Map<String, Object> map = new HashMap<>();

                String phone = SpUtils.getValue(mContext, SpKey.PHONE);
                int phoneed = 0;
                phoneed = Integer.parseInt(phone.substring(phone.length() - 4, phone.length()));

                map.put("course_id", course_id);
                map.put("nickname", SpUtils.getValue(mContext, SpKey.USER_NICK));
                map.put("liveStatus", liveStatus);
                mPresenter.getRealVideoUrl(map);
            } else {

            }
        } else {
            mWebView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        mContentLayout.removeAllViews();
        mWebView.destroy();
        mWebView = null;
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();
    }

    @Override
    public void onUpdateCollectedStatusSuccess(String collectStatus) {
        ivCollection.setSelected("1".equals(collectStatus));
        mWebView.reload();
    }

    @Override
    public void onCheckCollectedStatusSuccess(String collectStatus, String collectId,

                                              String title, String des) {
        if (TextUtils.isEmpty(stitle)) {

            stitle = title;
        }
        if (TextUtils.isEmpty(sdes)) {

            sdes = des;
        }
        ivCollection.setSelected("1".equals(collectStatus));
        mCollectId = collectId;
    }

    @Override
    public void onGetRealUrlSuccess(String url) {
        this.url = url;
        System.out.println("??????web?????????" + url);
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

    public class JavaScriptFunction {
        @JavascriptInterface
        public void share(final String platform, final String url, final String des, final String title) {
            // 1 ?????? 2 ????????? 3 qq 4 ?????? 5 ??????
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
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "H5??????");
            MobclickAgent.onEvent(mContext, "event2H5ShareClick", nokmap);

            mShareDialog.dismiss();
            //System.out.println("??????");
            //System.out.println("???????????????:"+mWebView.getTitle());
            share2(mPlatformMap.get(String.valueOf(v.getTag())), surl, sdes, stitle);
        }
    };

    public void collection(View view) {
        mPresenter.updateCollectedStatus(mKnowledgeId, view.isSelected() ? "2" : "1", mCollectId);
    }

    public void close(View view) {
        finish();
    }

    public void webBack(View view) {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mWebView.canGoBack()) {
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
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        } else {
            //??????uploadMessage???uploadMessageAboveL???????????????????????????????????????
            //WebView????????????????????????????????????????????????????????????onReceiveValue???null?????????
            //??????WebView???????????????????????????????????????????????????????????????????????????????????????
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            } else if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            }
        }
    }

    // 4. ?????????????????????Html??????
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
}