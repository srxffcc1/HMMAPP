package com.health.city.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.ExifInterfaceImageHeaderParser;
import com.example.lib_ShapeView.builder.ShapeDrawableBuilder;
import com.example.lib_ShapeView.view.ShapeTextView;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.health.city.R;
import com.health.city.adapter.CityCommentReviewAdapter;
import com.health.city.contract.PostDetailContract;
import com.health.city.model.UserInfoCityModel;
import com.health.city.presenter.PostDetailPresenter;
import com.health.city.widget.DiscussDialog;
import com.health.mall.decoration.CommentDecoration;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.business.SeckShareDialog;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.contract.DataStatisticsContract;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.model.ActivityModel;
import com.healthy.library.model.Discuss;
import com.healthy.library.model.DiscussStore;
import com.healthy.library.model.EnlistActivityModel;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.model.PageInfoEarly;
import com.healthy.library.model.PostDetail;
import com.healthy.library.model.PrizeModel;
import com.healthy.library.presenter.DataStatisticsPresenter;
import com.healthy.library.routes.CityRoutes;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.JsBridge;
import com.healthy.library.utils.JsoupCopy;
import com.healthy.library.utils.MediaFileUtil;
import com.healthy.library.utils.ResizeImgWebViewClient;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.utils.WebViewSettingPost;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.X5WebView;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.hyb.library.KeyboardUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.WebChromeClient;
import com.umeng.analytics.MobclickAgent;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ????????????
 */
@Route(path = CityRoutes.CITY_POSTDETAIL)
public class PostDetailActivity extends BaseActivity implements IsNeedShare, CityCommentReviewAdapter.OnLikeClickListener,
        CityCommentReviewAdapter.OnReViewClickListener, DiscussDialog.OnScaleDialogClickListener,
        DiscussDialog.OnDiscussDialogClickListener, PostDetailContract.View, OnRefreshLoadMoreListener,
        DiscussDialog.OnDiscussDialogDismissListener, DataStatisticsContract.View {
    @Autowired
    String id;
    @Autowired
    boolean isshowDiscuss;
    @Autowired
    String merchantId;
    PostDetail post;
    private int currentPage = 1;
    private TextView txtTitle;
    private ImageView imgBack;
    private ImageView submit;
    private View divider;
    private SmartRefreshLayout layoutRefresh;
    private StatusLayout layoutStatus;
    private RecyclerView recycler;
    private ConstraintLayout needS;
    private ImageView passToTop;
    private View dividerb;
    PostDetailPresenter postDetailPresenter;
    CityCommentReviewAdapter cityCommentReviewAdapter;
    private boolean isheadadd = false;
    private boolean isHeadReview = true;
    private ConstraintLayout cityItemFragmentFollow;
    private LinearLayout cityItemFragmentFollowHead;
    private CornerImageView ivHeader;
    private TextView name;
    private TextView days;
    private TextView status;
    private TextView toFollow;
    private LinearLayout cityItemFragmentFollowChild;
    private FlexboxLayout tipTitle;
    private X5WebView tipContentWeb;
    private TextView tipContent;
    private GridLayout seeImgs;
    private TextView tipAddress;
    //    private TextView tvCommentDate;
    private RelativeLayout canlikediscuss;
    private ImageView headIcon4;
    private ImageView headIcon3;
    private ImageView headIcon2;
    private ImageView headIcon1;
    private TextView likecount;
    private View conDissdussEnd;
    private ConstraintLayout conCertStart;
    private TextView reviewTitle;
    private TextView reviewCount;
    private String mfromId;
    private Dialog reviewandwarndialog;
    private Dialog warndialog;
    private DiscussDialog reviewdialog;
    private AlertDialog mShareDialog;
    Map<String, Object> reviewmap = new HashMap<>();

    private int mMargin;
    private int mCornerRadius;
    String surl;
    String sdes;
    String stitle = "?????????";
    private Bitmap sBitmap;
    private Map<String, Object> detailmap = new HashMap<>();
    private LinearLayout toShare;
    private LinearLayout toDiscuss;
    private LinearLayout toLike;
    private String activitytype = "??????";
    private String warntype = "??????";
    private String warnid = "";
    private UserInfoCityModel userInfoCityModel;
    private ImageTextView likeimg;
    private View candelete;
    private View nameandstatus;
    private boolean isfirst = true;
    private String nowmanname;
    private Drawable followNormal;
    private Drawable followSelect;
    private Drawable followNormal2;
    private Drawable followSelect2;
    private View mStubView;
    private View iv_empty_stock;
    private GridLayout mPrizeGridLayout;
    private ImageView mPrizeActivityImg;
    private TextView mActivityTime;
    private TextView mTvAction;
    private String mUrl;
    private ActivityModel activity;
    private String mTvActionName;
    private TextView mActivityTitle;
    private TextView mTvActivityAddress;
    private EnlistActivityModel mEnListActivity;
    private ConstraintLayout mClActivityContent;
    private FrameLayout mContentLayout;
    private ImageView mIvEssences;
    private TextView mPostingTitle;
    private ImageView mIvUserBadge;
    private ShapeTextView mUserBadgeName;
    private DataStatisticsPresenter dataStatisticsPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.city_activity_post_detail;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setStatusLayout(layoutStatus);
        StyledDialog.init(this);


        followNormal = mContext.getResources().getDrawable(R.drawable.cityrightlike);
        followSelect = mContext.getResources().getDrawable(R.drawable.cityrightliker);
        followNormal2 = mContext.getResources().getDrawable(R.drawable.ic_star_tofollow);
        followSelect2 = mContext.getResources().getDrawable(R.drawable.ic_star_isfollow);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (post == null) {
                    return;
                }
                if (post != null && post.postingStatus == 1) {
                    //????????????????????? ??????Toast????????? ?????????
                    Toast.makeText(mContext, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                isHeadReview = true;
                reviewmap.clear();
                reviewmap.put("postingId", id);
                try {
                    reviewmap.put("memberState", userInfoCityModel.dateContent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                nowmanname = "????????????";
                warntype = "??????";
                warnid = id;
                showReviewWarnDialog("??????");
            }
        });
        layoutRefresh.setOnRefreshLoadMoreListener(this);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.addItemDecoration(new CommentDecoration(mContext, 1));
        cityCommentReviewAdapter = new CityCommentReviewAdapter();
        cityCommentReviewAdapter.setOnLikeClickListener(new CityCommentReviewAdapter.OnLikeClickListener() {
            @Override
            public void like(String commentDiscussId, String type) {
//                mDetailPresenter.likeClild(commentDiscussId,type);
            }
        });
        cityCommentReviewAdapter.setOnReViewClickListener(this);
        cityCommentReviewAdapter.setOnLikeClickListener(this);
        cityCommentReviewAdapter.bindToRecyclerView(recycler);

        postDetailPresenter = new PostDetailPresenter(merchantId,mContext, this);
        dataStatisticsPresenter = new DataStatisticsPresenter(mContext, this);

        mfromId = id;
        warnid = id;

        toLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (post != null && post.postingStatus == 1) {
                    //????????????????????? ??????Toast????????? ?????????
                    Toast.makeText(mContext, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }



                try {
                    Map<String, Object> map = new HashMap<>();
                    map.put("postingId", id);
                    map.put("type", post.praiseState == 0 ? "0" : "1");
                    postDetailPresenter.like(map);
                    post.praiseState = post.praiseState == 0 ? 1 : 0;
                    post.praiseNum = post.praiseNum + (post.praiseState == 0 ? -1 : 1);
                    likecount.setText(post.praiseNum + "?????????");
                    if (post.praiseState == 0) {//??????
                        likeimg.setText("??????");
                        likeimg.setDrawable(followNormal);
                        //                    likeimg.setCompoundDrawablePadding(5);

                    } else {
                        likeimg.setText("??????");
                        likeimg.setDrawable(followSelect);
                        //                    likeimg.setCompoundDrawablePadding(5);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        toShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_POSTURL);
                String url = String.format("%s?id=%s&scheme=HMMPostDetail&postId=%s&referral_code=%s", urlPrefix, id, id, SpUtils.getValue(mContext, SpKey.GETTOKEN));
                surl = url;
                try {
                    sdes = JsoupCopy.parse(post.getPostingContent()).text();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                stitle = "????????????";

                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "??????????????????");
                MobclickAgent.onEvent(mContext, "event2ShareClick", nokmap);
                dataStatisticsPresenter.shareAndLook(new SimpleHashMapBuilder<String, Object>().puts("sourceId", id).puts("sourceType", 2).puts("type", 2));

                showShare();
            }
        });


        toDiscuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewmap.clear();
                reviewmap.put("postingId", id);
                if (userInfoCityModel != null) {
                    reviewmap.put("memberState", userInfoCityModel.dateContent);
                }
                nowmanname = "????????????";
                showReviewDialog("??????");
            }
        });
        postDetailPresenter.getMine();
        getData();
        dataStatisticsPresenter.shareAndLook(new SimpleHashMapBuilder<String, Object>().puts("sourceId", id).puts("sourceType", 2).puts("type", 1));
    }

    @Override
    public void getData() {
        super.getData();
        detailmap.clear();
        if (post == null) {
            detailmap.put("id", id);
            postDetailPresenter.getPostDetail(detailmap);
        } else {
            detailmap.put("pageSize", "10");
            detailmap.put("postingId", id);
            detailmap.put("type", "1");
            detailmap.put("currentPage", currentPage + "");
            postDetailPresenter.getDisgussList(detailmap);
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        currentPage++;
        getData();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        isheadadd = false;
        currentPage = 1;
        post = null;
        getData();
    }

    @Override
    public void onSuccessDelete() {
        finish();
    }

    @Override
    public void onSuccessAdd() {
        currentPage = 1;
        getData();
        //onRefresh(null);
    }

    @Override
    public void onSuccessLike() {
//        onRefresh(null);
    }

    @Override
    public void onSuccessFan() {
//        onRefresh(null);
    }

    @Override
    public void onSuccessGetPostDetail(PostDetail post) {
        this.post = post;
        if (post == null) {
            Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (post.postingStatus == 1) {
            findViewById(R.id.canSend).setVisibility(View.GONE);
        } else {
            findViewById(R.id.canSend).setVisibility(View.VISIBLE);
        }
        getData();
        //postDetailPresenter.getDisgussList(listmap);
    }

    Map<String, File> imgReplaceMaps = new HashMap<>();

    public void prepareHeaderView() {
        //????????????  ???????????????
        Document doc = null;
        Elements pngs = null;
        try {
            doc = JsoupCopy.parse(post.postingRichContent);
            pngs = null;
            pngs = doc.select("img[src]");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (pngs != null) {
//            for (int i = 0; i < pngs.size(); i++) {
//                final String src2 = pngs.get(0).attr("src");//??????src???????????????
//                String imgPath = Environment.getExternalStorageDirectory() + "/mmtmppic/" + getSrcName(src2);
//                if (!new File(imgPath).exists()) {
//
//
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            FutureTarget<File> future = GlideCopy.with(mContext)
//                                    .load(src2)
//                                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
//                            try {
//                                File cacheFile = future.get();
//                                String path = cacheFile.getAbsolutePath();
//                                int angle = readPictureDegree(path);
//                                Log.e("TAG", "degree====" + angle);
//                                Bitmap bitmapori = BitmapFactory.decodeFile(path);
//                                // ??????????????????????????????
//                                Bitmap bitmap = rotaingImageView(angle, bitmapori);
//                                FileUtil.saveBitmap(bitmap, getSrcName(src2));
//                                imgReplaceMaps.put(src2, new File(Environment.getExternalStorageDirectory() + "/mmtmppic/" + getSrcName(src2)));
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        System.out.println("??????????????????");
//                                        addHeaderView();
//                                    }
//                                });
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            } catch (ExecutionException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//                } else {
//                    imgReplaceMaps.put(src2, new File(Environment.getExternalStorageDirectory() + "/mmtmppic/" + getSrcName(src2)));
//                    addHeaderView();
//                }
//            }
//        }
        addHeaderViewReal();
    }

    private String getSrcName(String src2) {
        String fileName = src2.substring(src2.lastIndexOf("/"));
        return fileName;
    }

    public static int readPictureDegree(String path) {
        ExifInterfaceImageHeaderParser exifInterfaceImageHeaderParser;
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.e("TAG", "???????????????????????? ========== " + orientation);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * ????????????
     *
     * @param angle  ???????????????
     * @param bitmap ????????????
     * @return ??????????????????
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Log.e("TAG", "angle===" + angle);
        Bitmap returnBm = null;
        // ???????????????????????????????????????
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // ?????????????????????????????????????????????????????????????????????
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    @Override
    public void onSuccessGetDiscuss(List<Discuss> discusses, PageInfoEarly pageInfoEarly) {
        if (!isheadadd) {
            addHeaderView();
            isheadadd = true;
        }

        if (pageInfoEarly == null) {
            if (currentPage == 1) {
                layoutRefresh.setEnableLoadMore(false);
            } else {
                layoutRefresh.finishLoadMoreWithNoMoreData();
            }
            return;
        }
        currentPage = pageInfoEarly.currentPage;
        if (currentPage == 1 || currentPage == 0) {
            if (discusses == null || discusses.size() == 0) {
                discusses.add(null);
            }
        }

        if (currentPage == 1) {
            cityCommentReviewAdapter.setNewData(discusses);
            if (discusses.size() == 0) {
//                showEmpty();
            }
        } else {
            cityCommentReviewAdapter.addData(discusses);
        }
        if (pageInfoEarly.isMore != 1) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            layoutRefresh.resetNoMoreData();
            layoutRefresh.finishLoadMore();
        }
        if (pageInfoEarly.totalNum > 0) {
            reviewCount.setText("???" + pageInfoEarly.totalNum + "???");
        } else {
            reviewCount.setText("");
        }
    }

    @Override
    public void onSuccessGetMine(UserInfoCityModel userInfoCityModel) {
        this.userInfoCityModel = userInfoCityModel;
        if (isfirst) {
            reviewmap.clear();
            reviewmap.put("postingId", id);
            reviewmap.put("memberState", userInfoCityModel.dateContent);
            nowmanname = "????????????";
            if (isshowDiscuss) {
                showReviewDialog("??????");
            }
            isfirst = false;
        }

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishRefresh();
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
        return null;
    }

    @Override
    public void onDiscussClick(View view, Map<String, Object> map) {
        if (post != null && post.postingStatus == 1) {
            //????????????????????? ??????Toast????????? ?????????
            Toast.makeText(mContext, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("??????".equals(activitytype)) {
            reviewmap.put("status", "0");
            reviewmap.put("content", map.get("content").toString());
            postDetailPresenter.addReview(reviewmap);
        } else {
            reviewmap.put("status", "0");
            reviewmap.put("content", map.get("content").toString());
            postDetailPresenter.addDiscuss(reviewmap);
        }
    }

    @Override
    public void onScaleClick(View view, Map<String, Object> map) {
        if (post != null && post.postingStatus == 1) {
            //????????????????????? ??????Toast????????? ?????????
            Toast.makeText(mContext, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        if (reviewdialog != null) {
            reviewdialog.dismiss();
            if ("??????".equals(activitytype)) {
                ARouter.getInstance()
                        .build(CityRoutes.CITY_ADDDIS)
                        .withString("activityType", activitytype)
                        .withString("postingDiscussId", reviewmap.get("postingDiscussId").toString())
                        .withString("toMemberId", reviewmap.get("toMemberId").toString())
                        .withString("toMemberType", reviewmap.get("toMemberType").toString())
                        .withString("fatherId", reviewmap.get("fatherId").toString())
                        .navigation(this, 1000);
            } else {
                Postcard postcard = ARouter.getInstance()
                        .build(CityRoutes.CITY_ADDDIS);
                try {
                    postcard.withString("activityType", activitytype);
                    postcard.withString("postingId", reviewmap.get("postingId").toString());
                    postcard.withString("memberState", reviewmap.get("memberState").toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postcard.navigation(this, 1000);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        currentPage = 1;
        getData();
        /*postDetailPresenter.getDisgussList(listmap);
        postDetailPresenter.getPostDetail(detailmap);*/
    }

    @Override
    public void onClick(View view, String commentDiscussId, String toMemberId, String fatherId, String fromName, String toMemberType) {
        if (post != null && post.postingStatus == 1) {
            //????????????????????? ??????Toast????????? ?????????
            Toast.makeText(mContext, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        if (post == null || post.postingStatus == 1 || TextUtils.isEmpty(toMemberId)) {
            return;
        }
        if (TextUtils.isEmpty(toMemberId)) {
            toMemberId = "";
        }
        isHeadReview = false;
        reviewmap.clear();
        reviewmap.put("postingDiscussId", commentDiscussId);
        reviewmap.put("toMemberId", toMemberId);
        reviewmap.put("toMemberType", toMemberType);
        reviewmap.put("fatherId", fatherId);
        nowmanname = fromName.replace("??????", "??????");
        warntype = fromName.split(":")[0];
        warnid = "??????".equals(warntype) ? fatherId : commentDiscussId;
        showReviewWarnDialog("??????");
    }

    @Override
    public void like(String commentDiscussId, String type) {
        if (post != null && post.postingStatus == 1) {
            //????????????????????? ??????Toast????????? ?????????
            Toast.makeText(mContext, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("postingDiscussId", commentDiscussId);
        map.put("type", type);
        postDetailPresenter.likeChild(map);
    }

    Runnable KeyHideRunnable = new Runnable() {
        @Override
        public void run() {
            KeyboardUtils.hideSoftInput(toDiscuss);
        }
    };

    @Override
    public void onDiscussDiss(final String result) {
        toDiscuss.postDelayed(KeyHideRunnable, 100);
        if (!TextUtils.isEmpty(result)) {
            StyledDialog.init(this);
            StyledDialog.buildIosAlert("", "???????????????????", new MyDialogListener() {
                @Override
                public void onFirst() {
                    SpUtils.store(LibApplication.getAppContext(), SpKey.DISCUSS_TMP, "");
                }

                @Override
                public void onThird() {
                    super.onThird();

                }

                @Override
                public void onSecond() {
                    DiscussStore postStore = new DiscussStore();
                    postStore.setDiscussContent(result);
                    String result = new Gson().toJson(postStore);
                    SpUtils.store(LibApplication.getAppContext(), SpKey.DISCUSS_TMP, result);
                }
            }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("?????????", "??????").show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tipContentWeb != null) {
            ((ViewGroup) tipContentWeb.getParent()).removeAllViews();
            mContentLayout.removeAllViews();
            tipContentWeb.destroy();
            tipContentWeb = null;
        }
        postDetailPresenter = null;
    }

    private void initView() {
        txtTitle = (TextView) findViewById(R.id.txt_title);
        imgBack = (ImageView) findViewById(R.id.img_back);
        submit = (ImageView) findViewById(R.id.submit);
        divider = (View) findViewById(R.id.divider);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        needS = (ConstraintLayout) findViewById(R.id.need_s);
        passToTop = (ImageView) findViewById(R.id.passToTop);
        passToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recycler.smoothScrollToPosition(0);
            }
        });
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition <= 3) {
                    passToTop.setVisibility(View.GONE);
                } else {
                    passToTop.setVisibility(View.VISIBLE);
                }
            }
        });
        dividerb = (View) findViewById(R.id.dividerb);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toShare = (LinearLayout) findViewById(R.id.toShare);
        toDiscuss = (LinearLayout) findViewById(R.id.toDiscuss);
        toLike = (LinearLayout) findViewById(R.id.toLike);
        likeimg = (ImageTextView) findViewById(R.id.likeimg);
    }

    /**
     * ??????????????????
     *
     * @param msg
     */
    private void isAgree(String msg) {
        StyledDialog.init(mContext);
        StyledDialog.buildIosAlert(
                "",
                msg,
                new MyDialogListener() {
                    @Override
                    public void onFirst() {
                    }

                    @Override
                    public void onThird() {
                        super.onThird();
                    }

                    @Override
                    public void onSecond() {

                    }
                }).setMsgColor(R.color.color_da1818).setBtnColor(0, R.color.colorPrimaryDark, 0)
                .setBtnText("??????")
                .show();
    }

    private void addHeaderView() {
//        if (handlerSubmit != null) {
//            handlerSubmit.removeCallbacks(runnableSubmit);
//        }
//        handlerSubmit.postDelayed(runnableSubmit, 100);
        addHeaderViewReal();
    }

    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private IX5WebChromeClient.CustomViewCallback customViewCallback;


    private WebChromeClient mWebChromeClient = new WebChromeClient() {


        @Override
        public View getVideoLoadingProgressView() {
            FrameLayout frameLayout = new FrameLayout(PostDetailActivity.this);
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
     * ??????????????????
     **/
    private void showCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        this.getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
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
     * ??????????????????
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
        tipContentWeb.setVisibility(View.VISIBLE);
    }


    private void addHeaderViewReal() {
//        cityCommentReviewAdapter.removeAllHeaderView();
        View headerView;
        LinearLayout headerLayout = cityCommentReviewAdapter.getHeaderLayout();
        if (headerLayout == null) {
            headerView = LayoutInflater.from(mContext)
                    .inflate(R.layout.city_item_activity_post_review_head, null);
            iv_empty_stock = headerView.findViewById(R.id.iv_empty_stock);
            cityItemFragmentFollow = (ConstraintLayout) headerView.findViewById(R.id.city_item_fragment_follow);
            cityItemFragmentFollowHead = (LinearLayout) headerView.findViewById(R.id.city_item_fragment_follow_head);
            ivHeader = (CornerImageView) headerView.findViewById(R.id.ivHeader);
            name = (TextView) headerView.findViewById(R.id.name);
            days = (TextView) headerView.findViewById(R.id.tv_comment_date);
            status = (TextView) headerView.findViewById(R.id.status);
            toFollow = (TextView) headerView.findViewById(R.id.toFollow);
            cityItemFragmentFollowChild = (LinearLayout) headerView.findViewById(R.id.city_item_fragment_follow_child);
            tipTitle = (FlexboxLayout) headerView.findViewById(R.id.tipTitle);
            mContentLayout = headerView.findViewById(R.id.content_layout);
            tipContentWeb = new X5WebView(this, null);
            tipContentWeb.setVerticalScrollBarEnabled(false);
            tipContentWeb.setHorizontalScrollBarEnabled(false);
            mContentLayout.addView(tipContentWeb, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            //tipContentWeb = (X5WebView) headerView.findViewById(R.id.tipContentWeb);
            tipContent = (TextView) headerView.findViewById(R.id.tipContent);
            tipAddress = (TextView) headerView.findViewById(R.id.tipAddress);
            mPostingTitle = headerView.findViewById(R.id.postingTitle);
            mIvEssences = headerView.findViewById(R.id.iv_essences);
            mIvUserBadge = headerView.findViewById(R.id.iv_user_badge);
            mUserBadgeName = headerView.findViewById(R.id.stv_user_badgeName);

            /** ?????????????????????????????? */
            if (mStubView == null && (post != null && post.postingType == 8)) {
                ViewStub mViewStub = (ViewStub) headerView.findViewById(R.id.enListViewStub);
                mStubView = mViewStub.inflate();
            }

            if (mStubView == null && (post != null && post.postingType == 7)) {
                ViewStub mViewStub = (ViewStub) headerView.findViewById(R.id.viewStub);
                mStubView = mViewStub.inflate();
            }

            if (post != null && (post.postingType == 7 || post.postingType == 8)) {
                /** ??????????????????????????? */
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) tipAddress.getLayoutParams();
                layoutParams.topToBottom = R.id.prize_content_layout;
                tipAddress.setLayoutParams(layoutParams);
            }

            if (mStubView != null) {
                //?????? or ????????????
                mTvAction = headerView.findViewById(R.id.tv_action);
                mTvAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /** ???????????????????????? */
                        if (post.postingType == 7 && activity != null) {
                            if ("?????????".equals(mTvActionName) || "?????????".equals(mTvActionName)) return;

                            if ("?????????".equals(mTvActionName)) {
                                if (activity.getFreezeStatus() == -1) {
                                    //??????????????????
                                    isAgree(TextUtils.isEmpty(activity.getFreezeReason()) ? "??????????????????????????????????????????????????????" : activity.getFreezeReason());
                                    return;
                                }
                                SeckShareDialog seckShareDialog = SeckShareDialog.newInstance();
                                seckShareDialog.setActivityDialog(4, 0, activity);
                                seckShareDialog.show(getSupportFragmentManager(), "voteShare");
                                return;
                            }

                            String mUrl = "";
                            String mHost = "";
                            if ("????????????".equals(mTvActionName)) {
                                mHost = "http://192.168.10.181:8000/voteApply.html";
                                if (!TextUtils.isEmpty(SpUtils.getValue(mContext, UrlKeys.H5_VOTE_APPLY_URL))) {
                                    mHost = SpUtils.getValue(mContext, UrlKeys.H5_VOTE_APPLY_URL);
                                }

                            }
                            if ("????????????".equals(mTvActionName)) {
                                mHost = "http://192.168.10.181:8000/voteList.html";
                                if (!TextUtils.isEmpty(SpUtils.getValue(mContext, UrlKeys.H5_VOTE_LIST_URL))) {
                                    mHost = SpUtils.getValue(mContext, UrlKeys.H5_VOTE_LIST_URL);
                                }
                            }
                            mUrl = mHost + "?id=" + activity.getId() + "&token=" + SpUtils.getValue(mContext, SpKey.TOKEN);
                            ARouter.getInstance()
                                    .build(IndexRoutes.INDEX_WEBVIEWSINGLE)
                                    .withString("url", mUrl)
                                    .withString("title", activity.getName())
                                    .withBoolean("needfindcollect", false)
                                    .navigation();
                            return;
                        }
                        /** ???????????????????????? */
                        if (post.postingType == 8 && mEnListActivity != null) {
                            ARouter.getInstance()
                                    .build(MineRoutes.MINE_ENLIST_DETAILS)
                                    .withString("id", mEnListActivity.getId())
                                    .navigation();
                            return;
                        }
                    }
                });
                if (post.postingType == 7) {
                    mPrizeGridLayout = headerView.findViewById(R.id.prize_child_gridLayout);
                }
                if (post.postingType == 8) {
                    mTvActivityAddress = headerView.findViewById(R.id.prize_child_activity_address);
                }
                //???????????? ?????????????????? ?????????-> ?????????????????? ??????-> ??????????????????
                mActivityTime = headerView.findViewById(R.id.prize_child_activity_time);
                mPrizeActivityImg = headerView.findViewById(R.id.prize_activity_img);
                mActivityTitle = headerView.findViewById(R.id.prize_activity_title);
                mClActivityContent = headerView.findViewById(R.id.prize_content_layout);
            }

            seeImgs = (GridLayout) headerView.findViewById(R.id.see_imgs);
//        tvCommentDate = (TextView)  headerView.findViewById(R.id.tv_comment_date);
            canlikediscuss = (RelativeLayout) headerView.findViewById(R.id.canlikediscuss);
            headIcon4 = (ImageView) headerView.findViewById(R.id.head_icon4);
            headIcon3 = (ImageView) headerView.findViewById(R.id.head_icon3);
            headIcon2 = (ImageView) headerView.findViewById(R.id.head_icon2);
            headIcon1 = (ImageView) headerView.findViewById(R.id.head_icon1);
            likecount = (TextView) headerView.findViewById(R.id.likecount);
            conDissdussEnd = (View) headerView.findViewById(R.id.con_dissduss_end);
            conCertStart = (ConstraintLayout) headerView.findViewById(R.id.con_cert_start);
            reviewTitle = (TextView) headerView.findViewById(R.id.review_title);
            reviewCount = (TextView) headerView.findViewById(R.id.review_count);
            candelete = (View) headerView.findViewById(R.id.candelete);
            nameandstatus = (View) headerView.findViewById(R.id.nameandstatus);
            tipContentWeb.setVisibility(View.GONE);
            tipContent.setVisibility(View.GONE);
            cityCommentReviewAdapter.addHeaderView(headerView);
        } else {
            headerView = cityCommentReviewAdapter.getHeaderLayout().getRootView();
        }

        if (post != null) {
            ImageView head_icon1 = headerView.findViewById(R.id.head_icon1);
            ImageView head_icon2 = headerView.findViewById(R.id.head_icon2);
            ImageView head_icon3 = headerView.findViewById(R.id.head_icon3);
            ImageView head_icon4 = headerView.findViewById(R.id.head_icon4);
            head_icon4.setVisibility(View.GONE);
            head_icon3.setVisibility(View.GONE);
            head_icon2.setVisibility(View.GONE);
            head_icon1.setVisibility(View.GONE);
            if (post.praiseMemberList != null && post.praiseNum > 0) {
                for (int j = 0; j < post.praiseMemberList.size(); j++) {
                    PostDetail.PraiseMember praiseMember = post.praiseMemberList.get(j);
                    if (j == 0) {
                        head_icon4.setVisibility(View.VISIBLE);
                        GlideCopy.with(head_icon4.getContext())
                                .load(praiseMember.faceUrl)
                                .placeholder(R.drawable.img_1_1_default2)
                                .error(R.drawable.ic_sex_women)
                                
                                .into(head_icon4);
                    }
                    if (j == 1) {
                        head_icon3.setVisibility(View.VISIBLE);
                        GlideCopy.with(head_icon3.getContext())
                                .load(praiseMember.faceUrl)
                                .placeholder(R.drawable.img_1_1_default2)
                                .error(R.drawable.ic_sex_women)
                                
                                .into(head_icon3);
                    }
                    if (j == 2) {
                        head_icon2.setVisibility(View.VISIBLE);
                        GlideCopy.with(head_icon2.getContext())
                                .load(praiseMember.faceUrl)
                                .placeholder(R.drawable.img_1_1_default2)
                                .error(R.drawable.ic_sex_women)
                                
                                .into(head_icon2);
                    }
                    if (j == 3) {
                        head_icon1.setVisibility(View.VISIBLE);
                        GlideCopy.with(head_icon1.getContext())
                                .load(praiseMember.faceUrl)
                                .placeholder(R.drawable.img_1_1_default2)
                                .error(R.drawable.ic_sex_women)
                                
                                .into(head_icon1);
                    }
                }
            }

            mPostingTitle.setVisibility(TextUtils.isEmpty(post.postingTitle) ? View.GONE : View.VISIBLE);
            mPostingTitle.setText(post.postingTitle);
            boolean empty = ListUtil.isEmpty(post.postingTagList);
            mIvEssences.setVisibility(empty ? View.GONE : View.VISIBLE);
            if (!empty) {
                //???????????????
                mIvEssences.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(CityRoutes.CITY_ESSENCES)
                                .navigation();
                    }
                });
            }

            boolean mBadgeEmpty = TextUtils.isEmpty(post.badgeId);
            int visibility = mBadgeEmpty ? View.GONE : View.VISIBLE;
            mIvUserBadge.setVisibility(visibility);
            mUserBadgeName.setVisibility(visibility);
            mUserBadgeName.setText(post.badgeName);
            if (!mBadgeEmpty) {
                ShapeDrawableBuilder shapeDrawableBuilder = mUserBadgeName.getShapeDrawableBuilder();
                if (0 == post.badgeType) {
                    //????????????
                    mIvUserBadge.setImageResource(R.drawable.icon_user_certification);
                    shapeDrawableBuilder
                            .setSolidColor(0)
                            .setGradientColors(Color.parseColor("#FF6060"), Color.parseColor("#FF256C"))
                            .intoBackground();
                }
                if (1 == post.badgeType) {
                    mIvUserBadge.setImageResource(R.drawable.icon_user_official_certification);
                    shapeDrawableBuilder.clearGradientColors();
                    shapeDrawableBuilder
                            .setSolidColor(Color.parseColor("#3889FD"))
                            .intoBackground();
                }
            }

            String nickname = "";
            if (post.anonymousStatus == 1) {
                nickname = "????????????";
            } else if (TextUtils.isEmpty(post.accountNickname)) {
                nickname = "???????????????";
            } else {
                nickname = post.accountNickname;
            }
            name.setText(nickname);
//            name.setText(post.anonymousStatus==0?post.accountNickname:"????????????");
            final String finalNickname = nickname;

            ivHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (post != null && post.postingStatus == 1) {
                        //????????????????????? ??????Toast????????? ?????????
                        Toast.makeText(mContext, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(post.postingType > 1 && post.postingType < 9){
                        return;
                    }
                    if (post.anonymousStatus != 1 && !"???????????????".equals(finalNickname)) {
                        ARouter.getInstance()
                                .build(CityRoutes.CITY_FANDETAIL)
                                .withString("searchMemberType", post.createSource + "")
                                .withString("memberId", post.memberId + "")
                                .navigation();
                    }
                }
            });

            if (post != null && post.postingStatus == 1) {
                //????????????????????? ??????Toast????????? ?????????
                String baseUserId = new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT));
                if (!baseUserId.equals(post.memberId)) {//??????????????????
                    Toast.makeText(mContext, "??????????????????????????????????????????~~", Toast.LENGTH_SHORT).show();
                    if (!post.memberId.equals(new String(Base64.decode(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))) {//??????????????????
                        Toast.makeText(LibApplication.getAppContext(), "??????????????????????????????????????????~~", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    iv_empty_stock.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(mContext, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
            } else {
                iv_empty_stock.setVisibility(View.GONE);
            }
            buildCanSend();

            nameandstatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (post != null && post.postingStatus == 1) {
                        //????????????????????? ??????Toast????????? ?????????
                        Toast.makeText(mContext, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(post.postingType > 1 && post.postingType < 9){
                        return;
                    }
                    if (post.anonymousStatus != 1 && !"???????????????".equals(finalNickname)) {
                        ARouter.getInstance()
                                .build(CityRoutes.CITY_FANDETAIL)
                                .withString("searchMemberType", post.createSource + "")
                                .withString("memberId", post.memberId + "")
                                .navigation();
                    }
                }
            });

            if (post.anonymousStatus == 1) {
                GlideCopy.with(ivHeader.getContext())
                        .asBitmap()
                        .load(R.drawable.img_avatar_default)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_avatar_default)
                        
                        .into(ivHeader);

            } else {
                GlideCopy.with(ivHeader.getContext())
                        .asBitmap()
                        .load(post.createUserFaceUrl)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_avatar_default)
                        
                        .into(ivHeader);
            }

            status.setText(post.memberState);

            if (TextUtils.isEmpty(post.memberState)) {
                status.setVisibility(View.GONE);
            } else {
                status.setText(post.memberState.replace("?????????", ""));
                status.setVisibility(View.VISIBLE);
            }
//            if(post.memberState!=null){
//                if(post.memberState.contains("??????")){//??????
//                    status.setBackgroundResource(R.drawable.shape_city_nofollow_red);
//                }else if(post.memberState.contains("???")||post.memberState.contains("??????")){//????????????
//                    status.setBackgroundResource(R.drawable.shape_city_nofollow_yellow);
//                }else {//???
//                    status.setBackgroundResource(R.drawable.shape_city_nofollow_green);
//                }
//            }
            if (TextUtils.isEmpty(post.memberState)) {
                status.setVisibility(View.GONE);
            } else {
                status.setVisibility(View.VISIBLE);
            }
            tipTitle.removeAllViews();
            if (post.topicList != null && post.topicList.size() > 0) {
                stitle = "";
                for (int i = 0; i < post.topicList.size(); i++) {
                    View tipchild = LayoutInflater.from(mContext).inflate(R.layout.city_item_tip_single, null);
                    TextView tipname = tipchild.findViewById(R.id.tipSmall);
                    tipname.setText(post.topicList.get(i).topicName);
                    final int finalI = i;
                    tipchild.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ARouter.getInstance()
                                    .build(CityRoutes.CITY_TIP)
                                    .withString("activitytype", "??????")
                                    .withString("topicId", post.topicList.get(finalI).id + "")
                                    .navigation();
                        }
                    });
                    stitle = stitle + "#" + post.topicList.get(i).topicName + "#";
                    tipTitle.addView(tipchild);
                }

            }
            String mBackUrl = "";
            /** ?????????????????? */
            if (post.postingType == 7) {
                mClActivityContent.setVisibility(View.GONE);
                mPrizeGridLayout.setVisibility(View.VISIBLE);
                activity = post.activity;
                if (activity != null) {
                    if (!TextUtils.isEmpty(activity.getBackgroundUrl())) {
                        if (activity.getBackgroundUrl().contains(",")) {
                            String[] split = activity.getBackgroundUrl().split(",");
                            mBackUrl = split[0];
                        } else {
                            mBackUrl = activity.getBackgroundUrl();
                        }
                    }
                    mClActivityContent.setVisibility(View.VISIBLE);
                    mTvAction.setEnabled(true);
                    mActivityTitle.setText(activity.getName());
                    mTvActionName = "????????????";
                    String mTime = "";
                    long mEnlistEndTime = DateUtils.str2Long(activity.getEnlistEndTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
                    long mEnlistStartTime = DateUtils.str2Long(activity.getEnlistStartTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
                    long mVotingStartTime = DateUtils.str2Long(activity.getVotingStartTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
                    //????????????????????????????????????
                    if (!activity.getSignUpStatus() && mEnlistEndTime > System.currentTimeMillis()) {
                        mTime = "???????????????" + DateUtils.getDateDay(activity.getEnlistStartTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd") + " - " +
                                DateUtils.getDateDay(activity.getEnlistEndTime(), "yyyy-MM-dd HH:mm:ss");
                    } else {
                        mTime = "???????????????" + DateUtils.getDateDay(activity.getVotingStartTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd") + " - " +
                                DateUtils.getDateDay(activity.getVotingEndTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd");
                    }
                    if (activity.getStatus() == 3) {
                        mTvActionName = "?????????";
                        mTvAction.setEnabled(false);
                    } else {
                        if (mEnlistStartTime < System.currentTimeMillis()) {
                            if (mEnlistEndTime > System.currentTimeMillis()) {//???????????????
                                if (activity.getSignUpStatus()) {
                                    mTvAction.setEnabled(false);
                                    mTvActionName = "?????????";
                                }
                            } else {
                                if (mVotingStartTime < System.currentTimeMillis()) { //???????????????
                                    if (activity.getSignUpStatus()) {
                                        mTvActionName = "?????????";
                                    } else {
                                        mTvActionName = "????????????";
                                    }
                                } else {
                                    mTvActionName = "???????????????";
                                    mTvAction.setEnabled(false);
                                }
                            }
                        } else {
                            mTvActionName = "???????????????";
                            mTvAction.setEnabled(false);
                        }
                    }
                    mTvAction.setText(mTvActionName);

                    mActivityTime.setText(mTime);

                    if (!ListUtil.isEmpty(activity.getPrizeList())) {
                        /** ???????????????*/
                        addPrizeView(mPrizeGridLayout, activity.getPrizeList());
                    } else {
                        mPrizeGridLayout.setVisibility(View.GONE);
                    }
                    Glide.with(mContext).load(mBackUrl).placeholder(R.drawable.img_1_1_default)
                            .error(R.drawable.img_1_1_default).into(mPrizeActivityImg);
                }
            }
            /** ?????????????????? */
            if (post.postingType == 8) {
                mClActivityContent.setVisibility(View.GONE);
                mEnListActivity = post.enlistActivity;
                if (mEnListActivity != null) {
                    mClActivityContent.setVisibility(View.VISIBLE);
                    mBackUrl = mEnListActivity.getPhotoUrl();
                    long mEnlistStartTime = DateUtils.str2Long(mEnListActivity.getEnlistStartTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
                    long mEnlistEndTime = DateUtils.str2Long(mEnListActivity.getEnlistEndTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
                    long mEndTime = DateUtils.str2Long(mEnListActivity.getEndTime(), DateUtils.DATE_FORMAT_PATTERN_YMD_HMS);
                    String mTime = "";
                    if (mEnlistEndTime > System.currentTimeMillis()) { // ?????????????????????????????????
                        mTime = DateUtils.getDateDay(mEnListActivity.getEnlistStartTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd") + "-" +
                                DateUtils.getDateDay(mEnListActivity.getEnlistEndTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd");
                        mTvAction.setVisibility(View.VISIBLE);
                    } else {//????????????????????????????????????
                        mTime = DateUtils.getDateDay(mEnListActivity.getStartTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd") + "-" +
                                DateUtils.getDateDay(mEnListActivity.getEndTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd");
                        mTvAction.setVisibility(View.GONE);
                    }
                    if (mEnlistStartTime > System.currentTimeMillis()) {
                        mTvActionName = "??????????????????";
                        mTvAction.setEnabled(false);
                    } else {
                        if (mEndTime < System.currentTimeMillis() || mEnListActivity.getStatus() == 2 || mEnListActivity.getStatus() == 3) {
                            mTvAction.setVisibility(View.VISIBLE);
                            mTvActionName = "?????????";
                            mTvAction.setEnabled(false);
                        } else {
                            mTvActionName = "????????????";
                            mTvAction.setEnabled(true);
                        }
                    }
                    mActivityTime.setText("?????????" + mTime);
                    mActivityTitle.setText(mEnListActivity.getName());
                    mTvAction.setText(mTvActionName);
                    if (mTvActivityAddress != null) {
                        mTvActivityAddress.setText(mEnListActivity.activityAddress());
                    }
                    Glide.with(mPrizeActivityImg.getContext()).load(mBackUrl).placeholder(R.drawable.img_1_1_default)
                            .error(R.drawable.img_1_1_default).into(mPrizeActivityImg);
                }
            }

            candelete.setVisibility(View.GONE);
            tipAddress.setText(post.postingAddress);
            if (TextUtils.isEmpty(post.postingAddress)) {
                tipAddress.setVisibility(View.GONE);
            }

            /*????????????????????????????????????????????????*/
            if (post.createSource == 2 && post.postingType != 1) {
                toFollow.setVisibility(View.GONE);
            } else {
                toFollow.setVisibility(View.VISIBLE);
            }

            /* ??????????????????/????????????????????????????????? */
            if ((post.createSource == 0 || post.createSource == 2) && !(post.postingType == 7 || post.postingType == 8)) {
//                post.postingRichContent="<p>??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????</p><p>??????????????????????????????????????????1??????????????????22?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????</p><p>??????</p><p>?????? ????????????????????????????????????????????????</p><p>?????? ??????</p><p>???????????????</p><p>????????? 1???</p><p>?????? 3???</p><p>?????? 1???</p><p>????????? 10??????????????????</p><p>???????????????</p><p>????????????</p><p>????????????????????????</p><p><img src=\"https://hmmimg.hanmama.com/technician-user/images/ac18c4d9-6e0f-44c5-bb89-42ec22b89089.9j\"></p><p>??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????</p><p><img src=\"https://hmmimg.hanmama.com/technician-user/images/4eb9a0fa-3d1e-4037-aadb-336a31fdd0ba.9j\"></p><p>??????????????????????????????????????????????????????</p><p>?????????????????????????????????????????????????????????15??????</p><p>" +
//                        "<a href=\"http://www.baidu.com?scheme=HMM\"><img src=\"https://hmmimg.hanmama.com/technician-user/images/7a4a8a5c-5b7f-4379-b26e-01df8649f3c8.9j\"></a></p>";
                String sHead = "<html><head><meta name=\"viewport\" content=\"width=device-width, " +
                        "initial-scale=1.0, minimum-scale=0.5, maximum-scale=8.0, user-scalable=yes\" />" +
                        "<style>img{max-width:100% !important;height:auto}</style>"
                        + "<style>body{max-width:100% !important;word-break:break-all;}</style>" + "</head><body>";
                tipContentWeb.setWebViewClient(new ResizeImgWebViewClient(imgReplaceMaps));
                tipContentWeb.setWebChromeClient(mWebChromeClient);
                tipContentWeb.addJavascriptInterface(new JsBridge(), "JsBridge");
                // ???????????????webview??????
                WebViewSettingPost.setWebViewParam(tipContentWeb, mContext);
//                System.out.println("??????:?????????:" + post.postingRichContent);
                Pattern pattern = Pattern.compile("<iframe[\\s\\S]*?</iframe>");
                Matcher matcher = pattern.matcher(post.postingRichContent);
                while (matcher.find()) {

                    System.out.println("??????:iframe");
                    System.out.println(matcher.group());
                    String src = "";
                    String iframe = matcher.group();
                    Pattern patternSrc = Pattern.compile("src=\\\"([\\s\\S]*?)\\\"");
                    Matcher matcherSrc = patternSrc.matcher(iframe);
                    if (matcherSrc.find()) {
                        src = matcherSrc.group(1);
                    }
                    String video = "<video src=\"" + src + "\" type=\"video/mp4\" width=\"300\" controls=\"controls\" loop=\"-1\" poster=\"false.png\"></video>";
                    post.postingRichContent = post.postingRichContent.replace(iframe, video);
                }
//                post.postingRichContent=post.postingRichContent.replace("<p><br></p>","<pbrp>");//????????????????????????
                post.postingRichContent = post.postingRichContent.replace("<p>", "").replace("</p>", "<br>").replace("<br><br>", "<br>");
//                post.postingRichContent=post.postingRichContent.replace("<pbrp>","<br>");
                tipContentWeb.setVisibility(View.VISIBLE);
                tipContentWeb.loadDataWithBaseURL(null,
                        sHead + post.postingRichContent + "</body><script> document.body.style.lineHeight = 1.45 </script></html>", "text/html", "utf-8", null);
            } else if (post.createSource == 1) {
                /* ????????????????????? */
                tipContent.setVisibility(View.VISIBLE);
                tipContent.setText(post.getPostingContent());
            } else {
                /* ??????????????????????????? */
                tipContent.setVisibility(View.VISIBLE);
                tipContent.setText(HtmlCompat.fromHtml(post.postingRichContent, HtmlCompat.FROM_HTML_MODE_LEGACY));
            }

            likecount.setText(post.praiseNum + "?????????");
            //days.setText(DateUtils.getClassDatePost(post.createTime));
            days.setText(post.createTimeStr);

            if (post.praiseState == 0) {//??????
                likeimg.setText("??????");
                likeimg.setDrawable(followNormal);
                likeimg.setCompoundDrawablePadding(5);

            } else {
                likeimg.setText("??????");
                likeimg.setDrawable(followSelect);
                likeimg.setCompoundDrawablePadding(5);
            }

            if (post.focusStatus == 0) {//??????
                toFollow.setText("??????");
                toFollow.setCompoundDrawablesWithIntrinsicBounds(followNormal2, null, null, null);
                toFollow.setCompoundDrawablePadding(9);

            } else {
                toFollow.setText("?????????");
                toFollow.setCompoundDrawablesWithIntrinsicBounds(followSelect2, null, null, null);
                toFollow.setCompoundDrawablePadding(9);
            }

            if (post.memberId != null && post.memberId.equals(new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)))) {
                toFollow.setVisibility(View.GONE);
            }
            if (post.anonymousStatus == 1) {
                toFollow.setVisibility(View.GONE);
            }
            if ("???????????????".equals(finalNickname) || post.anonymousStatus == 1) {
                toFollow.setVisibility(View.GONE);
            }
            toFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (post != null && post.postingStatus == 1) {
                        //????????????????????? ??????Toast????????? ?????????
                        Toast.makeText(mContext, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (post.focusStatus != 0) {
                        StyledDialog.init(mContext);
                        StyledDialog.buildIosAlert("", "??????????????????????", new MyDialogListener() {
                            @Override
                            public void onFirst() {

                            }

                            @Override
                            public void onThird() {
                                super.onThird();
                            }

                            @Override
                            public void onSecond() {
                                Map<String, Object> map = new HashMap<>();
                                map.put("friendId", post.memberId + "");
                                map.put("friendType", post.createSource + "");
                                map.put("type", post.focusStatus == 0 ? "0" : "1");
                                postDetailPresenter.fan(map);
                                post.focusStatus = post.focusStatus == 0 ? 1 : 0;
                                if (post.focusStatus == 0) {//??????
                                    toFollow.setText("??????");
                                    toFollow.setCompoundDrawablesWithIntrinsicBounds(followNormal2, null, null, null);
                                    toFollow.setCompoundDrawablePadding(9);

                                } else {
                                    toFollow.setText("?????????");
                                    toFollow.setCompoundDrawablesWithIntrinsicBounds(followSelect2, null, null, null);
                                    toFollow.setCompoundDrawablePadding(9);
                                }


                            }
                        }).setCancelable(true, true).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("??????", "??????").show();
                    } else {
                        Map<String, Object> map = new HashMap<>();
                        map.put("friendId", post.memberId + "");
                        map.put("friendType", post.createSource + "");
                        map.put("type", post.focusStatus == 0 ? "0" : "1");
                        postDetailPresenter.fan(map);
                        post.focusStatus = post.focusStatus == 0 ? 1 : 0;
                        if (post.focusStatus == 0) {//??????
                            toFollow.setText("??????");
                            toFollow.setCompoundDrawablesWithIntrinsicBounds(followNormal2, null, null, null);
                            toFollow.setCompoundDrawablePadding(9);

                        } else {
                            toFollow.setText("?????????");
                            toFollow.setCompoundDrawablesWithIntrinsicBounds(followSelect2, null, null, null);
                            toFollow.setCompoundDrawablePadding(9);
                        }
                    }
                }
            });

            List<String> showImg = new ArrayList<>();
            showImg.clear();
            if (!(post.createSource == 0 || post.createSource == 2)) {
                if (post.videoUrls != null) {
                    for (int j = 0; j < post.videoUrls.size(); j++) {
                        PostDetail.VideoUrl videoUrl = post.videoUrls.get(j);
                        showImg.add(videoUrl.url);
                    }
                }
                if (post.imgUrls != null) {
                    for (int j = 0; j < post.imgUrls.size(); j++) {
                        PostDetail.ImageUrl videoUrl = post.imgUrls.get(j);
                        showImg.add(videoUrl.getUrl());
                    }
                }
            }
            if (showImg.size() > 0) {
                seeImgs.setVisibility(View.VISIBLE);
            } else {
                seeImgs.setVisibility(View.GONE);
            }
            if (post.createSource != 0 && !ListUtil.isEmpty(showImg)) {
                addImages(mContext, seeImgs, showImg);
            }
            conCertStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((LinearLayoutManager) recycler.getLayoutManager()).scrollToPositionWithOffset(1, 0);
                }
            });
        }
    }

    private void buildCanSend() {
        if (post != null && post.postingStatus == 1) {
            cityCommentReviewAdapter.setPostingStatus(1);
        } else {
            cityCommentReviewAdapter.setPostingStatus(0);
        }
    }

    /**
     * ????????????
     *
     * @param gridLayout
     * @param prizeList
     */
    private void addPrizeView(final GridLayout gridLayout,
                              final List<PrizeModel> prizeList) {
        //??????????????????
        gridLayout.removeAllViews();

        int row = prizeList.size() < 3 ? 1 : 2;
        int mForSize = prizeList.size() > 6 ? 6 : prizeList.size();
        int needfixzzz = 3 - (mForSize % 3 == 0 ? 3 : mForSize % 3);
        int mMargin = (int) TransformUtil.dp2px(LibApplication.getAppContext(), 80);
        gridLayout.setRowCount(row);
        gridLayout.setColumnCount(3);
        int w = ((ScreenUtils.getScreenWidth(LibApplication.getAppContext()) - mMargin) / 3);
        for (int i = 0; i < mForSize; i++) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = w;
            View itemPrizeLayout = LayoutInflater.from(mContext).inflate(R.layout.city_item_fragment_activity_prize_child, gridLayout, false);
            CardView mCardView = itemPrizeLayout.findViewById(com.healthy.library.R.id.cardView);
            final ImageView mPrizeChildImg = itemPrizeLayout.findViewById(R.id.prize_child_img);
            TextView mPrizeChildTitle = itemPrizeLayout.findViewById(R.id.prize_child_title);
            TextView mPrizeChildCount = itemPrizeLayout.findViewById(R.id.prize_child_count);
            PrizeModel prizeModel = prizeList.get(i);
            String mGoodsImage;
            String mGoodsTitle;
            GoodsDetail goodsDto = prizeModel.getGoodsDto();
            if (goodsDto == null) {
                mGoodsImage = prizeModel.getGoodsImage();
                mGoodsTitle = prizeModel.getGoodsTitle();
            } else {
                mGoodsImage = goodsDto.goodsImage;
                mGoodsTitle = goodsDto.goodsTitle;
            }
            Glide.with(mPrizeChildImg.getContext()).load(mGoodsImage).centerCrop()
                    .error(R.drawable.img_1_1_default).placeholder(R.drawable.img_1_1_default)
                    .into(mPrizeChildImg);
            mPrizeChildTitle.setText(mGoodsTitle);
            mPrizeChildCount.setText(prizeModel.getName() + " " + prizeModel.getPrizeNum() + "???");
            ViewGroup.LayoutParams layoutParams = mCardView.getLayoutParams();
            layoutParams.width = (int) (w - TransformUtil.dp2px(mContext, 4f));
            mCardView.setLayoutParams(layoutParams);
            gridLayout.addView(itemPrizeLayout, params);
        }
        /*if (needfixzzz > 0) {
            for (int i = 0; i < needfixzzz; i++) {
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = w;
                View itemPrizeLayout = LayoutInflater.from(mContext).inflate(R.layout.city_item_fragment_activity_prize_child, gridLayout, false);
                final ImageView mPrizeChildImg = itemPrizeLayout.findViewById(R.id.prize_child_img);
                TextView mPrizeChildTitle = itemPrizeLayout.findViewById(R.id.prize_child_title);
                TextView mPrizeChildCount = itemPrizeLayout.findViewById(R.id.prize_child_count);
                itemPrizeLayout.setVisibility(View.INVISIBLE);
                gridLayout.addView(itemPrizeLayout, params);
            }
        }*/
    }

    public Map<String, Bitmap> bitmapList = new HashMap<>();


    private static class MyRunnable implements Runnable {

        @Override
        public void run() {

        }

    }


    private void addImages(final Context context, final GridLayout gridLayout,
                           final List<String> urls) {
        if (mMargin == 0) {
            mMargin = (int) TransformUtil.dp2px(mContext, 2);
            mCornerRadius = (int) TransformUtil.dp2px(mContext, 5);
        }
        //System.out.println("????????????");
        gridLayout.removeAllViews();
        if (urls != null && urls.size() > 0) {
            int row = 3;
            if (urls.size() == 1) {
                row = 1;
            }
            if (urls.size() == 2) {
                row = 2;
            }
            gridLayout.removeAllViews();
            gridLayout.setRowCount(row);
            int mWidth = (int) TransformUtil.dp2px(LibApplication.getAppContext(), 30);
            final int w = (ScreenUtils.getScreenWidth(mContext) - mWidth - (2 + 2 * (row - 1)) * mMargin) / row;
            int needsize = urls.size();
//            if (urls.size() >= 3) {
//                needsize = 3;
//            }
            for (int i = 0; i < needsize; i++) {
                final String url = urls.get(i);
                final int pos = i;
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = w;
                if (row == 3) {
                    params.height = (int) TransformUtil.dp2px(LibApplication.getAppContext(), 110f);
                } else if (row == 2) {
                    params.height = (int) TransformUtil.dp2px(LibApplication.getAppContext(), 170f);
                } else {
                    params.width = (int) TransformUtil.dp2px(LibApplication.getAppContext(), 220f);
                    params.height = (int) TransformUtil.dp2px(LibApplication.getAppContext(), 220f);
                }
                params.setMargins(mMargin, mMargin, mMargin, mMargin);

                View imageparent = LayoutInflater.from(mContext).inflate(R.layout.city_item_normal_image, gridLayout, false);

                final CornerImageView imageView = imageparent.findViewById(R.id.iv_pic);
                imageView.setCornerRadius(mCornerRadius);
                final ImageView isVideo = imageparent.findViewById(R.id.isVideo);
                final int finalI = i;
                /*if (MediaFileUtil.isVideoFileType(url)) {
                    isVideo.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.img_1_1_default);
                    final int finalRow = row;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = null;
                            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                            try {
                                //??????url???????????????
                                retriever.setDataSource(url, new HashMap());
                                //?????????????????????
                                bitmap = retriever.getFrameAtTime();

                                if (finalRow == 1) {//???????????????
                                    com.healthy.library.businessutil.GlideCopy.with(imageView.getContext())
                                            .load(bitmap)
                                            .placeholder(R.drawable.img_1_1_default)
                                            .error(R.drawable.img_1_1_default)
                                            
                                            .into(new SimpleTarget<Drawable>() {
                                                @Override
                                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                    int swidth = w;
                                                    int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);
                                                    ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) imageView.getLayoutParams();
                                                    layoutParams.height = height;
                                                    layoutParams.width = swidth;
                                                    imageView.setLayoutParams(layoutParams);
                                                    imageView.setImageDrawable(resource);
                                                }
                                            });
                                    if (finalI == 0) {
                                        sBitmap = bitmap;
                                    }
                                } else {
                                    final Bitmap finalBitmap = BitmapUtils.zoomImg(bitmap, imageView.getWidth() + 10, imageView.getHeight());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            imageView.setImageBitmap(finalBitmap);
                                        }
                                    });
                                    if (finalI == 0) {
                                        sBitmap = finalBitmap;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    isVideo.setVisibility(View.GONE);
                    if (row == 1) {
                        com.healthy.library.businessutil.GlideCopy.with(imageView.getContext())
                                .load(url)
                                .placeholder(R.drawable.img_1_1_default)
                                .error(R.drawable.img_1_1_default)
                                
                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        int swidth = w;
                                        int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);
                                        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) imageView.getLayoutParams();
                                        layoutParams.height = height;
                                        layoutParams.width = swidth;
                                        imageView.setLayoutParams(layoutParams);
                                        imageView.setImageDrawable(resource);
                                    }
                                });

                        if (finalI == 0) {
                            com.healthy.library.businessutil.GlideCopy.with(imageView.getContext()).load(url)
                                    .placeholder(R.drawable.img_1_1_default)
                                    .error(R.drawable.img_1_1_default)
                                    
                                    .into(new SimpleTarget<Drawable>() {

                                        @Override
                                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                            sBitmap = DrawableUtils.drawableToBitmap(resource);
                                        }
                                    });
                        }
                    } else {
                        com.healthy.library.businessutil.GlideCopy.with(imageView.getContext())
                                .load(url)
                                .placeholder(R.drawable.img_1_1_default)
                                .error(R.drawable.img_1_1_default)
                                
                                .into(imageView);

                        if (finalI == 0) {
                            com.healthy.library.businessutil.GlideCopy.with(mContext).load(url)
                                    .placeholder(R.drawable.img_1_1_default)
                                    .error(R.drawable.img_1_1_default)
                                    
                                    .into(new SimpleTarget<Drawable>() {

                                        @Override
                                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                            sBitmap = DrawableUtils.drawableToBitmap(resource);
                                        }
                                    });
                        }
                    }
                }*/
                if (MediaFileUtil.isVideoFileType(url)) {
                    isVideo.setVisibility(View.VISIBLE);
                    isVideo.setVisibility(View.VISIBLE);
                    if (bitmapList.get(url) != null) {
                        imageView.setImageBitmap(bitmapList.get(url));
                        //System.out.println("??????????????????");
                    } else {
                        imageView.setImageResource(com.healthy.library.R.drawable.img_1_1_default2);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bitmap = null;
                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                try {
                                    //??????url???????????????
                                    retriever.setDataSource(url, new HashMap());
                                    //?????????????????????
                                    bitmap = retriever.getFrameAtTime();
                                    final Bitmap finalBitmap = BitmapUtils.zoomImg(bitmap, imageView.getWidth() + 10, imageView.getHeight());
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            imageView.setImageBitmap(finalBitmap);
                                            bitmapList.put(url, finalBitmap);
                                        }
                                    });
                                } catch (Exception e) {
//                                            e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                } else {
                    isVideo.setVisibility(View.GONE);
                    try {
                        GlideCopy.with(context)
                                .load(url)
                                .placeholder(com.healthy.library.R.drawable.img_1_1_default2)
                                .error(com.healthy.library.R.drawable.img_1_1_default)
                                
                                .into(imageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                gridLayout.addView(imageparent, params);
                final String[] array = urls.toArray(new String[urls.size()]);
               /* final String[] array = new String[urls.size()];
                for (int j = 0; j < array.length; j++) {
                    array[j] = urls.get(j);
                }*/
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!MediaFileUtil.isVideoFileType(url)) {
                            ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                                    .withCharSequenceArray("urls", array)
                                    .withInt("pos", pos)
                                    .navigation();

                        } else {
                            ARouter.getInstance()
                                    .build(LibraryRoutes.LIBRARY_VIDEOPLAYER)
                                    .withString("videoUrl", url)
                                    .navigation();
                        }
                    }
                });
            }
        }
    }

    /**
     * ????????????
     */
    public void showReviewWarnDialog(final String activitytype) {
        if (post != null && post.postingStatus == 1) {
            //????????????????????? ??????Toast????????? ?????????
            Toast.makeText(mContext, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        final List<String> strings = new ArrayList<>();
        final List<Integer> stringsColors = new ArrayList<>();
        strings.add("??????");
        stringsColors.add(Color.parseColor("#29BDA9"));
        if (post.memberId != null && post.memberId.equals(new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))) && !"??????".equals(activitytype)) {
            strings.add("??????");
            stringsColors.add(Color.parseColor("#444444"));
        }
//        else if((new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT))).equals(reviewmap.get("toMemberId"))&&!warntype.equals("??????")){
//
//        }
        else {
            stringsColors.add(Color.parseColor("#444444"));
            strings.add("??????");
        }
        StyledDialog.init(this);
        reviewandwarndialog = StyledDialog.buildBottomItemDialog(strings, stringsColors, "??????", new MyItemDialogListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
                if ("??????".equals(text.toString())) {
                    showReviewDialog(activitytype);
                }
                if ("??????".equals(text.toString())) {
                    showWarnDialog();
                }
                if ("??????".equals(text.toString())) {
                    showDeleteDialog();
                }
            }

            @Override
            public void onBottomBtnClick() {

            }
        }).setCancelable(true, true).show();
        reviewandwarndialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                reviewandwarndialog = null;
            }
        });
    }

    private void showDeleteDialog() {
        StyledDialog.init(this);
        StyledDialog.buildIosAlert("", "?????????????????????????", new MyDialogListener() {
            @Override
            public void onFirst() {

            }

            @Override
            public void onSecond() {
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("postingStatus", 2 + "");
                postDetailPresenter.delete(map);
            }
        }).setCancelable(true, true).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("??????", "??????").show();
    }

    /**
     * ????????????
     */
    public void showWarnDialog() {

        final List<Integer> stringsColors = new ArrayList<>();
        final List<String> strings = new ArrayList<>();
//        strings.add("??????????????????");
        strings.add("????????????");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("????????????");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("????????????");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("????????????");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("????????????");
        stringsColors.add(Color.parseColor("#29BDA9"));
        strings.add("??????");
        stringsColors.add(Color.parseColor("#29BDA9"));
        StyledDialog.init(this);
        warndialog = StyledDialog.buildBottomItemDialog(strings, stringsColors, "??????", new MyItemDialogListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {
                Map<String, Object> map = new HashMap<>();
                if ("??????".equals(warntype)) {
                    map.put("type", "1");
                    map.put("sourceId", warnid);

                } else if ("??????".equals(warntype)) {
                    map.put("type", "2");
                    map.put("sourceId", warnid);
                } else {
                    map.put("type", "3");
                    map.put("sourceId", warnid);
                }
                map.put("reason", text.toString());
                postDetailPresenter.warn(map);
            }

            @Override
            public void onBottomBtnClick() {

            }

        }).setTitle("??????????????????").setTitleColor(R.color.dialogTitleColor).setTitleSize(15).setCancelable(true, true).show();
        warndialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                warndialog = null;
            }
        });
    }


    /**
     * ?????????????????????
     *
     * @param type
     */
    public void showReviewDialog(String type) {
        reviewdialog = new DiscussDialog(this, R.style.customdialogstyle);
        activitytype = type;
        reviewdialog.setHiht(nowmanname);
        reviewdialog.setOnScaleDialogClickListener(this);
        reviewdialog.setOnDiscussDialogClickListener(this);
        reviewdialog.setOnDiscussDialogDismissListener(this);
        try {
            reviewdialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
