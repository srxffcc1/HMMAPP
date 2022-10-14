package com.health.index.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.health.index.R;
import com.health.index.adapter.ToolCommentReviewAdapter;
import com.health.index.contract.ToolsModDiscussContract;
import com.healthy.library.model.ToolsFoodDetail;
import com.healthy.library.model.ToolsFoodDiscuss;
import com.healthy.library.model.UserInfoMonModel;
import com.health.index.presenter.ToolsModDiscussPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.WebViewSettingOld;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.hyb.library.PreventKeyboardBlockUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = IndexRoutes.INDEX_TOOLS_FOOD_DETAIL)
public class ToolsFoodDetailActivity extends BaseActivity implements IsNeedShare,OnRefreshLoadMoreListener,IsFitsSystemWindows, ToolsModDiscussContract.View , ToolCommentReviewAdapter.OnReViewClickListener, ToolCommentReviewAdapter.OnLikeClickListener {

    ToolsModDiscussPresenter toolsModDiscussPresenter;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private androidx.recyclerview.widget.RecyclerView recycler;
    private androidx.constraintlayout.widget.ConstraintLayout needS;
    private android.widget.ImageView passToSendPost;
    private android.widget.ImageView passToTop;
    private TextView passTmp;
    private View dividerb;
    private androidx.constraintlayout.widget.ConstraintLayout canSend;
    private android.widget.ImageView like;
    private android.widget.ImageView collect;
    private android.widget.ImageView toolLeftTip;
    private EditText reViewText;
    private ToolCommentReviewAdapter toolCommentReviewAdapter;

    Map<String,Object> reviewmap=new HashMap<>();

    public String showType="评论";//回复
    public String showHint="点这里说两句...";

    public String memberState;

    ToolsFoodDetail toolsFoodDetail;
    @Autowired
    String recipeType;
    @Autowired
    String recipeImg;
    @Autowired
    String foodName;

    @Autowired
    String foodPractice=" ";

    private Bitmap sBitmap;


    @Autowired
    String id;
    private com.healthy.library.widget.TopBar topBar;


    @Override
    protected int getLayoutId() {
        return R.layout.index_activity_tools_food_detail;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        PreventKeyboardBlockUtil.getInstance().setContext(mActivity).setBtnView(canSend).register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreventKeyboardBlockUtil.getInstance().setContext(mActivity).unRegister();
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        if(!TextUtils.isEmpty(recipeImg)){
            com.healthy.library.businessutil.GlideCopy.with(mContext).load(recipeImg)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(new SimpleTarget<Drawable>() {

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            sBitmap= DrawableUtils.drawableToBitmap(resource);
                        }
                    });
        }

        topBar.setTitle(foodName);

        topBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                buildDes();
                showShare();
            }
        });

        toolsModDiscussPresenter=new ToolsModDiscussPresenter(mActivity,this);
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        toolCommentReviewAdapter=new ToolCommentReviewAdapter();
        toolCommentReviewAdapter.bindToRecyclerView(recycler);
        toolCommentReviewAdapter.setOnLikeClickListener(this);
        toolCommentReviewAdapter.setOnReViewClickListener(this);
        reViewText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEND){
                    if(TextUtils.isEmpty(reViewText.getText().toString())){
                        return false;
                    }
                    toolsModDiscussPresenter.reView(new SimpleHashMapBuilder<String, Object>().putMap(reviewmap).puts("content",reViewText.getText().toString()));
                }
                return false;
            }
        });
        toolsModDiscussPresenter.getMine(new SimpleHashMapBuilder<String, Object>()
                .puts(Functions.FUNCTION,"1011")
        );
        getData();
        toolsModDiscussPresenter.see(new SimpleHashMapBuilder<String, Object>()
                .puts(Functions.FUNCTION,"8094").puts("id", id));
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolsModDiscussPresenter.like(new SimpleHashMapBuilder<String, Object>()
                        .puts(Functions.FUNCTION,"8089").puts("recipeId", id).puts("upvote",toolsFoodDetail.upvote==0?"1":"0"));
            }
        });
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolsModDiscussPresenter.like(new SimpleHashMapBuilder<String, Object>()
                        .puts(Functions.FUNCTION,"8089").puts("recipeId", id).puts("collect",toolsFoodDetail.collect==0?"1":"0"));
            }
        });
    }

    @Override
    public void getData() {
        super.getData();
        showType="评论";
        showHint="点这里说两句...";
        reViewText.setHint(showHint);
        reViewText.setText("");
        toolsModDiscussPresenter.getList(new SimpleHashMapBuilder<String, Object>()
                .puts(Functions.FUNCTION,"9047")
                .puts("memberId",new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                .puts("currentPage",currentPage+"")
                .puts("pageSize",10+"")
                .puts("recipeId", id)
        );
        toolsModDiscussPresenter.detail(new SimpleHashMapBuilder<String, Object>()
                .puts(Functions.FUNCTION,"8089").puts("recipeId", id));
    }
    private UserInfoMonModel resolveMineData(String obj) {
        UserInfoMonModel result = null;
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
            Type type = new TypeToken<UserInfoMonModel>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private ToolsFoodDetail resolveDetailData(String obj) {
        ToolsFoodDetail result = new ToolsFoodDetail();
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
            Type type = new TypeToken<ToolsFoodDetail>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public void onClick(View view, String commentDiscussId, String toMemberId, String fatherId, String fromName, String toMemberType) {

        if(fromName.contains("评论")){
            showType="评论";
            showHint="点这里说两句...";
        }else {
            showType="回复";
            showHint=fromName;
        }
        reviewmap.clear();
        reviewmap.put(Functions.FUNCTION,"9049");
        reviewmap.put("recipeDiscussId",commentDiscussId);
        reViewText.setHint(showHint);
        showReviewDialog();
    }

    @Override
    public void like(String recipeDiscussId, String type) {
        toolsModDiscussPresenter.like(new SimpleHashMapBuilder<String, Object>()
        .puts(Functions.FUNCTION,"9048")
        .puts("recipeDiscussId",recipeDiscussId)
        .puts("type",type)
        );
    }

    @Override
    public void onSucessgetList(String result) {
        onSucessgetList(resolveList(result),resolveRefreshData(result));
    }
    private PageInfoEarly resolveRefreshData(String obj) {
        PageInfoEarly result = new PageInfoEarly();
        try {
            JSONObject data = new JSONObject(obj).getJSONObject("data");
            String userShopInfoDTOS = data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<PageInfoEarly>() {
            }.getType();
            result = gson.fromJson(userShopInfoDTOS, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    private ArrayList<ToolsFoodDiscuss> resolveList(String obj) {
        ArrayList<ToolsFoodDiscuss> result = new ArrayList<>();
        try {
            JSONArray data=new JSONObject(obj).getJSONObject("data").getJSONArray("items");
            String userShopInfoDTOS=data.toString();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            Type type = new TypeToken<List<ToolsFoodDiscuss>>() {
            }.getType();
            result=gson.fromJson(userShopInfoDTOS,type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    private int currentPage=1;
    private boolean isheadadd=false;
    public void onSucessgetList(List<ToolsFoodDiscuss> discusses, PageInfoEarly pageInfoEarly) {
        if(!isheadadd){
            addHeaderView();
            isheadadd=true;
        }

        if (pageInfoEarly == null) {
            if (currentPage == 1) {
                layoutRefresh.setEnableLoadMore(false);
            } else {
                layoutRefresh.setEnableLoadMore(false);
                layoutRefresh.finishLoadMoreWithNoMoreData();
            }
            return;
        }
        currentPage = pageInfoEarly.currentPage;
        if(currentPage==1||currentPage==0){
            if(discusses==null||discusses.size()==0){
                discusses.add(null);
            }
        }

        if (currentPage == 1) {
            toolCommentReviewAdapter.setNewData(discusses);
            if (discusses.size() == 0) {
            }
        } else {
            toolCommentReviewAdapter.addData(discusses);
        }
        if (pageInfoEarly.isMore != 1) {

            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void onSucessReView(String result) {
        Toast.makeText(mContext,"回复成功",Toast.LENGTH_SHORT).show();
        currentPage=1;
        getData();
    }

    @Override
    public void onSucessReDiscuss(String result) {
        Toast.makeText(mContext,"评论成功",Toast.LENGTH_SHORT).show();
        currentPage=1;
        getData();
    }

    @Override
    public void onSucessLike(String result) {
//        currentPage=1;
//        getData();
    }

    @Override
    public void onSucessGetMine(String result) {
        UserInfoMonModel userInfoMonModel=resolveMineData(result);
        if(userInfoMonModel!=null){
            memberState=userInfoMonModel.statusName;
        }
        showType="评论";
        showHint="点这里说两句...";
        reViewText.setHint(showHint);
        reviewmap.clear();
        reviewmap.put(Functions.FUNCTION,"9046");
        reviewmap.put("recipeId", id);
        reviewmap.put("memberState",memberState);
    }

    @Override
    public void onSucessSee(String result) {

    }

    @Override
    public void onSucessGetDetail(String result) {
        collect.setImageResource(R.drawable.index_ic_collection);
        like.setImageResource(R.drawable.tools_food_like_normal);
        toolsFoodDetail=resolveDetailData(result);
        //System.out.println("重新设定图标");
        if(toolsFoodDetail!=null){
            if(toolsFoodDetail.collect==1){
                collect.setImageResource(R.drawable.index_ic_collection_ok);
            }
            if(toolsFoodDetail.upvote==1){
                like.setImageResource(R.drawable.tools_food_like_select);
            }
        }
    }

    private void initView() {

        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        needS = (ConstraintLayout) findViewById(R.id.need_s);
        passToSendPost = (ImageView) findViewById(R.id.passToSendPost);
        passToTop = (ImageView) findViewById(R.id.passToTop);
        passTmp = (TextView) findViewById(R.id.passTmp);
        dividerb = (View) findViewById(R.id.dividerb);
        canSend = (ConstraintLayout) findViewById(R.id.canSend);
        like = (ImageView) findViewById(R.id.like);
        collect = (ImageView) findViewById(R.id.collect);
        toolLeftTip = (ImageView) findViewById(R.id.toolLeftTip);
        reViewText = (EditText) findViewById(R.id.reViewText);

        topBar = (TopBar) findViewById(R.id.top_bar);
    }

    private void addHeaderView() {
        toolCommentReviewAdapter.removeAllHeaderView();
        View headerView = LayoutInflater.from(mContext)
                .inflate(R.layout.index_activity_tools_item_food_detail_top, null);
         android.webkit.WebView tipContent;
         TextView line;
         TextView toolRightJ;
        View toolRightJJ;
        tipContent = (WebView) headerView.findViewById(R.id.tipContent);
        line = (TextView) headerView.findViewById(R.id.line);
        toolRightJ = (TextView) headerView.findViewById(R.id.toolRightJ);
        toolRightJJ=headerView.findViewById(R.id.toolRightJJ);
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_recipeStepContUrl);
        String url = String.format("%s?id=%s", urlPrefix, id);
        //System.out.println("载入的URL:"+url);

        tipContent.loadUrl(url);
        tipContent.setWebViewClient(new ResizeImgWebviewClient());
        tipContent.addJavascriptInterface(new JsBridge(), "JsBridge");
        tipContent.setWebChromeClient(mWebChromeClient);
        tipContent.getSettings().setUserAgentString(tipContent.getSettings().getUserAgentString().concat("APPS"));
        WebViewSettingOld.setWebViewParam(tipContent,this);
        toolRightJJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showType="评论";
                showHint="点这里说两句...";
                reViewText.setHint(showHint);
                reviewmap.clear();
                reviewmap.put(Functions.FUNCTION,"9046");
                reviewmap.put("recipeId", id);
                reviewmap.put("memberState",memberState);
                showReviewDialog();
            }
        });
        toolCommentReviewAdapter.addHeaderView(headerView);

    }

    @Override
    public String getSurl() {
        return surl;
    }

    @Override
    public String getSdes() {
        return sdes;
    }

    @Override
    public String getStitle() {
        return stitle;
    }

    @Override
    public Bitmap getsBitmap() {

        return sBitmap;
    }

    public class JsBridge {
        /**
         * 响应webview上点击图片事件（大图预览）
         *
         * @param url
         */
        @JavascriptInterface
        public void openImage(String url) {
            //System.out.println("点击的图片");


            final String[] array=new String[]{url};
            ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                    .withCharSequenceArray("urls", array)
                    .withInt("pos", 0)
                    .navigation();
        }

        @JavascriptInterface
        public void openImage(String url,final String[] array,int postion) {
            //System.out.println("点击的图片");

            ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                    .withCharSequenceArray("urls", array)
                    .withInt("pos", postion)
                    .navigation();
        }
    }
    boolean isinhome = true;//是不是外部网站 false说明是外部网站
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
                view.evaluateJavascript(js, null);
            }

        }
    };

    public class ResizeImgWebviewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            resizeImg(view);
            addImgClickEvent(view);

        }

        /**
         * 添加图片点击事件
         *
         * @param view
         */
        private void addImgClickEvent(WebView view) {
//            view.loadUrl("javascript:(function(){ "
//                    + "var objs = document.getElementsByTagName('img');"
//                    + " var array=new Array(); " + " for(var j=0;j<objs.length;j++){ "
//                    + "array[j]=objs[j].src;" + " }  "
//                    + "for(var i=0;i<objs.length;i++){"
//                    +"objs[i].i=i;"
//                    + "objs[i].onclick=function(){  window.JsBridge.openImage(this.src,array,this.i);" + "}  " + "}    })()");

//            //System.out.println("javascript:(function(){ "
//                    + "var objs = document.getElementsByTagName('img');"
//                    + " var array=new Array(); " + " for(var j=0;j<objs.length;j++){ "
//                    + "array[j]=objs[j].src;" + " }  "
//                    + "for(var i=0;i<objs.length;i++){"
//                    +"objs[i].i=i;"
//                    + "objs[i].οnclick=function(){  window.JsBridge.openImage(this.src,array,this.i);" + "}  " + "}    })()");
//
//            //System.out.println("javascript:(function(){"
//                    + "var objs = document.getElementsByTagName('img'); "
//                    + "for(var i=0;i<objs.length;i++)  "
//                    + "{"
//                    + "objs[i].onclick=function()  "
//                    + "    {  "
//                    + " window.JsBridge.openImage(this.src);  "
//                    + "    }  "
//                    + "}"
//                    + "})()");

            view.loadUrl("javascript:(function(){"
                    + "var objs = document.getElementsByTagName('img'); "
                    + "for(var i=0;i<objs.length;i++)  "
                    + "{"
                    + "objs[i].onclick=function()  "
                    + "    {  "
                    + " window.JsBridge.openImage(this.src);  "
                    + "    }  "
                    + "}"
                    + "})()");
        }

        /**
         * 重新调整图片宽高
         *
         * @param view
         */
        private void resizeImg(WebView view) {
//            view.loadUrl("javascript:(function(){" +
//                    "var objs = document.getElementsByTagName('img'); " +
//                    "for(var i=0;i<objs.length;i++)  " +
//                    "{"
//                    + "var img = objs[i];   " +
//                    "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
//                    "}" +
//                    "})()");
        }

    }
    private void showReviewDialog() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        reViewText.requestFocus();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currentPage++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currentPage=1;
        getData();

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
        layoutRefresh.finishLoadMore();
    }



    String surl;
    String sdes;
    String stitle;




    private void buildDes() {
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_recipeInfoUrl);
        String url = String.format("%s?id=%s&scheme=RecipeInfo&foodName=%s&recipeType="+recipeType, urlPrefix, id, URLEncoder.encode(foodName));
        surl=url;

        String foodname = foodName;
        if ("1".equals(recipeType)) {
            foodname = "月子餐-" + foodname;
        }
        if ("2".equals(recipeType)) {
            foodname = "宝宝辅食-" + foodname;

        }
        if ("3".equals(recipeType)) {
            foodname = "孕期食谱-" + foodname;

        }
        stitle=foodname;
        sdes=" ";

    }

}
