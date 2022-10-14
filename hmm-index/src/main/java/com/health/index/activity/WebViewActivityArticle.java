package com.health.index.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.index.R;
import com.health.index.adapter.WebViewActivityAdapter;
import com.health.index.contract.WebContract;
import com.health.index.model.CommentModel;
import com.health.index.model.ShareModel;
import com.health.index.presenter.WebPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.routes.AppRoutes;
import com.healthy.library.routes.IndexRoutes;
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
import com.hss01248.dialog.StyledDialog;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
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

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建日期：2022/1/5 16:17
 *
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.index.adapter
 * 类说明：新建的公共类webView  只用于展示百科知识及资讯文章
 */

@SuppressLint("InflateParams")
@Route(path = IndexRoutes.INDEX_WEBVIEWARTICLE)
public class WebViewActivityArticle extends BaseActivity implements WebContract.View,
        OnRefreshLoadMoreListener,
        TextView.OnEditorActionListener,
        WebViewActivityAdapter.OnLikeClickListener,
        WebViewActivityAdapter.OnReViewClickListener {

    @Autowired
    String url;
    @Autowired
    String title;
    @Autowired
    boolean isinhome = true;//表示需要token注入
    @Autowired
    boolean needShare = false;//是否需要分享
    @Autowired
    boolean needfindcollect = false;//是否需要收藏
    @Autowired
    boolean videoshop = false;//是不是直播页面 需要商品跳转拦截
    @Autowired
    boolean doctorshop = false;//是第三方 可能涉及到支付拦截

    private ConstraintLayout webTitleLL;
    private TextView tvTitle;
    private ImageView ivBack;
    private ImageView ivClose;
    private ImageView ivCollection;
    private ImageView ivOther;
    private ImageView ivShare;
    private SmartRefreshLayout layoutRefresh;
    private StatusLayout layoutStatus;
    private RecyclerView recyclerview;
    private ConstraintLayout bottomCommentLayout;
    private LinearLayout commentLayout;
    private LinearLayout clickLayout;
    private AppCompatImageView collectionImg;
    private ImageView shareImg;
    private EditText edit;
    private WebViewActivityAdapter activityAdapter;
    private X5WebView mWebView;
    private TextView commentTitle;
    private AlertDialog mShareDialog;
    //分享相关参数 没有的时候会从页面取
    String stitle;
    String sdes = " ";
    String surl;
    String zbitmap;
    Bitmap sbitmapFianl;
    private Map<String, SHARE_MEDIA> mPlatformMap;
    private static final int MSG_SHARE = 4;
    private static final int MSG_ERR_MSG = 1;
    private static final int MSG_TOKEN_INVALID = 2;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    private String mCollectId;
    private String mKnowledgeId;
    private String collectStatus = "2";//收藏状态

    private WebPresenter mPresenter;

    private Map<String, Object> replyMap = new HashMap();
    private int commentType = 1;//1是评论文章  2是评论评论

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view_article;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        mPresenter = new WebPresenter(mContext, this);
        tvTitle.setText(title);
        mPlatformMap = new HashMap<>();
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_wx), SHARE_MEDIA.WEIXIN);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_circle), SHARE_MEDIA.WEIXIN_CIRCLE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qq), SHARE_MEDIA.QQ);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_qzone), SHARE_MEDIA.QZONE);
        mPlatformMap.put(ResUtils.getStrById(this, R.string.lib_share_sina), SHARE_MEDIA.SINA);
        showLoading();
        buildAdapter();
        if (TextUtils.isEmpty(zbitmap)) {
            sbitmapFianl = BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.index_share_humb));
        } else {
            if (!zbitmap.contains("R.")) {
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(zbitmap)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(new SimpleTarget<Drawable>() {

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                sbitmapFianl = DrawableUtils.drawableToBitmap(resource);
                            }
                        });
            } else {

                sbitmapFianl = BitmapUtils.changeColor(BitmapFactory.decodeResource(mContext.getResources(), ResUtil.getInstance().getResourceId(zbitmap)));
            }
        }
    }

    @Override
    public void getData() {
        if (mKnowledgeId != null) {
            mPresenter.getCommentList(new SimpleHashMapBuilder<String, Object>().puts("knowledgeId", mKnowledgeId));
        }
    }

    private void buildAdapter() {
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        activityAdapter = new WebViewActivityAdapter();
        activityAdapter.bindToRecyclerView(recyclerview);
        activityAdapter.setOnLikeClickListener(this);
        activityAdapter.setOnReViewClickListener(this);
        addHeaderView();
        if (!TextUtils.isEmpty(url)) {
            if (!url.contains("http")) {//说明不是标准url 缺少http协议 进行手动拼接
                url = "http://" + url;
            }
            onGetRealUrlSuccess(url);
            if (TextUtils.isEmpty(surl)) {
                surl = url;
            }
        }
    }

    private void addHeaderView() {
        View headerView;
        LinearLayout headerLayout = activityAdapter.getHeaderLayout();
        if (headerLayout == null) {
            headerView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_webview_article_activity_adapter_header, null);
            mWebView = headerView.findViewById(R.id.webView);
            commentTitle = headerView.findViewById(R.id.commentTitle);
            mWebView.setVerticalScrollBarEnabled(false);
            mWebView.setHorizontalScrollBarEnabled(false);
            if (needShare) {
                mWebView.addJavascriptInterface(new WebViewActivityArticle.InJavaScriptLocalObj(), "java_obj");
            }
            //设置WebView相关参数
            WebViewSetting.setWebViewParam(mWebView, this);
            mWebView.getSettings().setBlockNetworkImage(true);
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mWebView.addJavascriptInterface(new WebViewActivityArticle.JavaScriptFunction(), "JavaScriptFunction");
            mWebView.setWebChromeClient(mWebChromeClient);
            mWebView.setWebViewClient(mWebViewClient);
            activityAdapter.addHeaderView(headerView);
        } else {
            headerView = activityAdapter.getHeaderLayout().getRootView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
        }
        mWebView = null;
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();
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
    public void onUpdateCollectedStatusSuccess(String collectStatus) {
        if (collectStatus.equals("1")) {
            collectionImg.setImageResource(R.drawable.index_collection_img_success);
        } else {
            collectionImg.setImageResource(R.drawable.index_collection_img);
        }
        this.collectStatus = collectStatus;
        if (mWebView != null) {
            mWebView.reload();
        }
    }

    @Override
    public void onCheckCollectedStatusSuccess(String collectStatus, String collectId, String title, String des) {
        if (TextUtils.isEmpty(stitle)) {
            stitle = title;
        }
        if (TextUtils.isEmpty(sdes)) {
            sdes = des;
        }
        if (collectStatus.equals("1")) {
            collectionImg.setImageResource(R.drawable.index_collection_img_success);
        } else {
            collectionImg.setImageResource(R.drawable.index_collection_img);
        }
        this.collectStatus = collectStatus;
        mCollectId = collectId;
    }

    @Override
    public void onGetRealUrlSuccess(String url) {
        this.url = url;
        System.out.println("加载的H5:"+url);
        mWebView.loadUrl(url);
        mWebView.onResume();
    }

    @Override
    public void onAddCommentSuccess(String result) {
        if (result != null && result.contains("成功")) {
            showToast("评论成功");
            edit.setText("");
        }
        getData();
    }

    @Override
    public void onAddPraiseSuccess(String result) {

    }

    @Override
    public void onAddReplySuccess(String result) {
        if (result != null && result.contains("成功")) {
            showToast("评论成功");
            edit.setText("");
            commentType = 1;
        }
        getData();
    }

    @Override
    public void getCommentListSuccess(List<CommentModel> indexVideos, PageInfoEarly pageInfoEarly) {
        commentTitle.setVisibility(View.VISIBLE);
        if (!ListUtil.isEmpty(indexVideos)) {
            activityAdapter.setNewData(indexVideos);
        } else {
            List<CommentModel> list = new ArrayList<>();
            CommentModel model = new CommentModel();
            model.id = "null";
            list.add(model);
            activityAdapter.setNewData(list);
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        webTitleLL = (ConstraintLayout) findViewById(R.id.webTitleLL);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivCollection = (ImageView) findViewById(R.id.iv_collection);
        ivOther = (ImageView) findViewById(R.id.iv_other);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        bottomCommentLayout = (ConstraintLayout) findViewById(R.id.bottomCommentLayout);
        commentLayout = (LinearLayout) findViewById(R.id.commentLayout);
        clickLayout = (LinearLayout) findViewById(R.id.clickLayout);
        collectionImg = (AppCompatImageView) findViewById(R.id.collectionImg);
        shareImg = (ImageView) findViewById(R.id.shareImg);
        edit = (EditText) findViewById(R.id.edit);
        edit.setOnEditorActionListener(this);
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            if (!TextUtils.isEmpty(edit.getText().toString())) {
                addComment(edit.getText().toString().trim());
            } else {
                showToast("请输入内容");
            }
        }
        return false;
    }

    private void addComment(String text) {
        if (commentType == 1) {
            mPresenter.addComment(new SimpleHashMapBuilder<String, Object>()
                    .puts("knowledgeId", mKnowledgeId)
                    .puts("content", text)
                    .puts("memberType", 1)
                    .puts("memberState", SpUtils.getValue(mContext, SpKey.STATUS_STR)));
        } else {
            replyMap.put("content", text);
            mPresenter.addReply(replyMap);
        }

    }

    @Override
    public void onClick(View view, String commentDiscussId, String toMemberId, String fatherId, String fromName, String toMemberType) {
        replyMap.clear();
        commentType = 2;
        replyMap.put("knowledgeDiscussId", commentDiscussId);
        replyMap.put("fromMemberType", "1");
        replyMap.put("toMemberId", toMemberId);
        replyMap.put("toMemberType", toMemberType);
        replyMap.put("parentId", fatherId);
        replyMap.put("function", "KD_10002");
        edit.setHint(fromName);
        showKeyBoard(edit);
    }

    @Override
    public void like(String id, String type) {
        if (type.equals("0")) {//取消赞
            mPresenter.addPraise(new SimpleHashMapBuilder<String, Object>()
                    .puts("discussId", id)
                    .puts("function", "KD_10006"), type);
        } else {//点赞
            mPresenter.addPraise(new SimpleHashMapBuilder<String, Object>()
                    .puts("discussId", id)
                    .puts("function", "KD_10005"), type);
        }
    }

    public final class InJavaScriptLocalObj {
        //一定也要加上这个注解,否则没有用
        @JavascriptInterface
        public void getSource(String html) {
            //获取html中的所有文本
            Document doc = JsoupCopy.parse(html);
            Elements pngs = null;
            pngs = doc.select("img[src]");
            for (int i = 0; i < pngs.size(); i++) {
                String src2 = pngs.get(0).attr("src");//获取src的绝对路径
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

    public class JavaScriptFunction {
        @JavascriptInterface
        public void share(final String platform, final String url, final String des, final String title) {
            // 1 微信 2 朋友圈 3 qq 4 空间 5 微博
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

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SHARE:
                    ShareModel model = (ShareModel) msg.obj;
                    WebViewActivityArticle.this.share2(mPlatformMap.get(model.getPlatform()),
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

    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");//图片上传
//        i.setType("file/*");//文件上传
        i.setType("*/*");//文件上传
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
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
        web.setThumb(new UMImage(mContext, sbitmapFianl));
        web.setDescription(des);
        new ShareAction(WebViewActivityArticle.this)
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(this)
                .share();
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(final WebView view, String url) {
            super.onPageFinished(view, url);
            if (TextUtils.isEmpty(title)) {//没有标题则取webview的标题
                if (view.getTitle() != null && view.getTitle().equals("新闻资讯")) {
                    tvTitle.setText("资讯");
                } else {
                    tvTitle.setText(view.getTitle());
                }
            }
            mWebView.getSettings().setBlockNetworkImage(false);
            if (needShare && TextUtils.isEmpty(sdes)) {//这里去取webview内容
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

            if (isinhome && needfindcollect) {//内部网站 需要 收藏
                try {
                    String pattern = "?id=";
                    String[] resultatrray = url.split("\\?");
                    mKnowledgeId = resultatrray.length > 1 ? resultatrray[1] : "";
                    try {
                        mKnowledgeId = mKnowledgeId.split("&")[0].trim().split("=")[1];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPresenter.getCommentList(new SimpleHashMapBuilder<String, Object>().puts("knowledgeId", mKnowledgeId));
                            mPresenter.checkCollectedStatus(mKnowledgeId);
                        }
                    }, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            changeWebViewConfig();
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 根据协议的参数，判断是否是所需要的url
            // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
            //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）
            if (videoshop) {
                Uri uri = Uri.parse(url);
//            // 如果url的协议 = 预先约定的 js 协议
//            // 就解析往下解析参数
                //System.out.println("跳转的url：" + uri);
                if (uri.toString().contains("addBargain")) {//
                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    return true;
                }
            }
            if (doctorshop) {
                //System.out.println("跳转的url：" + url);
                return handlerUrl(view, Uri.parse(url));
            }
            return super.shouldOverrideUrlLoading(view, url);

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            // 根据协议的参数，判断是否是所需要的url
            // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
            //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）
            if (videoshop) {
                Uri uri = request.getUrl();
//            // 如果url的协议 = 预先约定的 js 协议
//            // 就解析往下解析参数
                //System.out.println("跳转的url：" + uri);
                if (uri.toString().contains("addBargain")) {//
                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    return true;
                }
            }
            if (doctorshop) {
                return handlerUrl(view, request.getUrl());
            }

            return super.shouldOverrideUrlLoading(view, request);
        }
    };

    private void changeWebViewConfig() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mWebView == null) {
                    System.out.println("重新计算失败");
                    changeWebViewConfig();
                    return;
                }
                mWebView.measure(0, 0);
                ViewGroup.LayoutParams params = mWebView.getLayoutParams();
                // params.height = (int) TransformUtil.dp2px(mContext, mWebView.getHeight());
                params.height = mWebView.getMeasuredHeight();
                mWebView.setLayoutParams(params);
                showContent();
                System.out.println("重新计算成功");
            }
        }, 800);
    }

    private boolean handlerUrl(WebView webView, Uri url) {
        if (url.toString().contains(WebViewActivityAll.WebConstants.WX_PAY_URL)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(url);
            startActivity(intent);
            return true;
        } else if (url.toString().contains(WebViewActivityAll.WebConstants.Al_PAY_URL)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(url);
            startActivity(intent);
            return true;
        } else if (url.toString().contains("wx/short/byclass")) {//帮他复制到剪切板
            ClipboardUtils.copy(mContext, url.toString());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(mContext, "理财链接已经复制到剪切板", Toast.LENGTH_SHORT).show();
                }
            }, 500);
            return false;
        } else {
            Map<String, String> headerParams = new HashMap<>();
            headerParams.put("Referer", WebViewActivityAll.WebConstants.WEB_DOMAIN);
            webView.loadUrl(url.toString(), headerParams);
            return true;
        }
    }

    public void showShareBottomDialog2(View view) {

        if (needShare && TextUtils.isEmpty(sdes)) {//这里去取webview内容
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

    private View.OnClickListener mShareClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Map nokmap = new HashMap<String, String>();
            nokmap.put("soure", "H5页面");
            MobclickAgent.onEvent(mContext, "event2H5ShareClick", nokmap);
            mShareDialog.dismiss();
            share2(mPlatformMap.get(String.valueOf(v.getTag())), surl, sdes, stitle);
        }
    };

    public void collection(View view) {
        mPresenter.updateCollectedStatus(mKnowledgeId, collectStatus.equals("1") ? "2" : "1", mCollectId);
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

    public static void showKeyBoard(View edCount) {

        try {
            //设置可获得焦点
            edCount.setFocusable(true);
            edCount.setFocusableInTouchMode(true);
            //请求获得焦点
            edCount.requestFocus();
            //调用系统输入法
            InputMethodManager imm = (InputMethodManager) StyledDialog.weakReference.get().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edCount, InputMethodManager.RESULT_SHOWN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) StyledDialog.weakReference.get().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }
}