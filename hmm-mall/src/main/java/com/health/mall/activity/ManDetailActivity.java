package com.health.mall.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.health.mall.R;
import com.health.mall.contract.ManDetailContract;
import com.health.mall.presenter.ManDetailPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.interfaces.IsNeedShare;
import com.healthy.library.interfaces.OnSubmitListener;
import com.healthy.library.model.TechnicianResult;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.routes.MallRoutes;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/04 17:24
 * @des 列表
 */
@Route(path = MallRoutes.MALL_STORE_MAN_DETAIL)
public class ManDetailActivity extends BaseActivity implements IsNeedShare,BaseQuickAdapter.OnItemClickListener,
        ManDetailContract.View, View.OnClickListener, OnRefreshLoadMoreListener {

    @Autowired
    String userId;

    @Autowired
    String shopId;

    @Autowired
    boolean needsub;

    private TopBar mTopBar;
    private ManDetailPresenter mPresenter;
    private int page=1;
    private ConstraintLayout conHeadStart;
    private android.widget.ImageView headIcon;
    private android.widget.TextView headName;
    private android.widget.TextView headDust;
    private android.widget.TextView headTip;
    private android.widget.TextView headYear;
    private View conHeadEnd;
    private ConstraintLayout conContentStart;
    private android.widget.TextView contentDonorsTitle;
    private android.widget.TextView contentDonorsContent;
    private android.widget.TextView contentIntroTitle;
    private android.widget.TextView contentIntroContent;
    private View conContentEnd;
    private ConstraintLayout conCertStart;
    private android.widget.TextView certTitle;
    private android.widget.GridLayout certImgs;
    private View conCertEnd;
    private ConstraintLayout conHealthStart;
    private android.widget.TextView healthTitle;
    private android.widget.GridLayout healthImgs;
    private View conHealthEnd;
    TechnicianResult technicianResult;
    String surl;
    String sdes;
    String stitle;
    private Bitmap sBitmap;

    private Map<String, Object> map = new HashMap<>();;
    private android.widget.LinearLayout couponBottom;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_man_detail;
    }

    @Override
    protected void findViews() {
        mTopBar = findViewById(R.id.top_bar);
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        setTopBar(mTopBar);
        mPresenter = new ManDetailPresenter(mContext, this, getLifecycle());
        getData();
        mTopBar.setSubmitListener(new OnSubmitListener() {
            @Override
            public void onSubmitBtnPressed() {
                buildDes();
                showShare();
            }
        });
        couponBottom.setVisibility(View.GONE);
        if(!needsub){
            couponBottom.setVisibility(View.GONE);
        }
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "服务人员详情页浏览量");
        MobclickAgent.onEvent(mContext, "event_APP_PeopleDetial_Stop", nokmap);
    }

    private void buildDes() {
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "服务人员详情页分享量");
        MobclickAgent.onEvent(mContext, "event2APPPeopleDetialShareClick", nokmap);
        String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_serviceStaff);
        String url = String.format("%s?id=%s&shopid=%s", urlPrefix,userId ,shopId);
        surl=url;
        stitle="这个老师不错哟，推荐给你看看";
        sdes=" ";
    }

    @Override
    public void getData() {
        mPresenter.getManDetail(userId);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }


    @Override
    public void onGetFirstSuccess(TechnicianResult result) {
        technicianResult=result;
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(result.faceUrl)
                .placeholder(R.drawable.img_1_1_default)
                .error(R.drawable.img_1_1_default)

                .into(headIcon);

        com.healthy.library.businessutil.GlideCopy.with(mContext).load(result.faceUrl)
                .placeholder(R.drawable.img_1_1_default)
                .error(R.drawable.img_1_1_default)

                .into(new SimpleTarget<Drawable>() {

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        sBitmap= DrawableUtils.drawableToBitmap(resource);
                    }
                });



        mTopBar.getTxtTitle().setText(result.realName);
        headName.setText(result.realName);
//        headYear.setText(com.healthy.library.utils.JsoupCopy.parse(result.tagTechnician).text());

        try {
            try {
                headTip.setText(com.healthy.library.utils.JsoupCopy.parse(result.tagTechnician).text().replace(",","|"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            headDust.setText(com.healthy.library.utils.JsoupCopy.parse(result.profession).text());
        } catch (Exception e) {
            e.printStackTrace();
        }


        contentDonorsContent.setText(com.healthy.library.utils.JsoupCopy.parse(result.beGood).text());
        contentIntroContent.setText(com.healthy.library.utils.JsoupCopy.parse(result.remark).text());
        try {
            addImages(mContext,certImgs,result.certificateId.split(","));
            if(TextUtils.isEmpty(result.certificateId)){
                conCertStart.setVisibility(View.GONE);
                conCertEnd.setVisibility(View.GONE);
            }
            addImages(mContext,healthImgs,result.healthCertificateUrl.split(","));
            if(TextUtils.isEmpty(result.healthCertificateUrl)){
                conHealthStart.setVisibility(View.GONE);
                conHealthEnd.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onGetMoreSuccess(TechnicianResult result) {

    }

    @Override
    public void onGetStoreListFail() {

    }

    @Override
    public void onClick(View view) {
        
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {

    }

    public void toSub(View view){
        ARouter.getInstance()
                .build(MallRoutes.MALL_ORDERSUBNEW)
                .withString("shopId",shopId)
                //.withString("userId",technicianResult.userId+"")
                //.withString("technicianName",technicianResult.realName+" "+technicianResult.profession)
                .withString("type","1")
                .navigation();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
    }

    private void initView() {
        conHeadStart = (ConstraintLayout) findViewById(R.id.con_head_start);
        headIcon = (ImageView) findViewById(R.id.head_icon);
        headName = (TextView) findViewById(R.id.head_name);
        headDust = (TextView) findViewById(R.id.head_dust);
        headTip = (TextView) findViewById(R.id.head_tip);
        headYear = (TextView) findViewById(R.id.head_year);
        conHeadEnd = (View) findViewById(R.id.con_head_end);
        conContentStart = (ConstraintLayout) findViewById(R.id.con_content_start);
        contentDonorsTitle = (TextView) findViewById(R.id.content_donors_title);
        contentDonorsContent = (TextView) findViewById(R.id.content_donors_content);
        contentIntroTitle = (TextView) findViewById(R.id.content_intro_title);
        contentIntroContent = (TextView) findViewById(R.id.content_intro_content);
        conContentEnd = (View) findViewById(R.id.con_content_end);
        conCertStart = (ConstraintLayout) findViewById(R.id.con_cert_start);
        certTitle = (TextView) findViewById(R.id.cert_title);
        certImgs = (GridLayout) findViewById(R.id.cert_imgs);
        conCertEnd = (View) findViewById(R.id.con_cert_end);
        conHealthStart = (ConstraintLayout) findViewById(R.id.con_health_start);
        healthTitle = (TextView) findViewById(R.id.health_title);
        healthImgs = (GridLayout) findViewById(R.id.health_imgs);
        conHealthEnd = (View) findViewById(R.id.con_health_end);
        couponBottom = (LinearLayout) findViewById(R.id.couponBottom);
    }

    private int mMargin;
    private int mCornerRadius;
    private void addImages(final Context context, final GridLayout gridLayout, final String[] urls) {
        if (mMargin == 0) {
            mMargin = (int) TransformUtil.dp2px(mContext, 2);
            mCornerRadius = (int) TransformUtil.dp2px(mContext, 3);
        }
        gridLayout.post(new Runnable() {
            @Override
            public void run() {
                gridLayout.removeAllViews();
                int row = 3;
//                if(urls.length==1){
//                    row=1;
//                }
                gridLayout.setRowCount(row);
                int w = (gridLayout.getWidth()-gridLayout.getPaddingLeft()-gridLayout.getPaddingRight() - (2 + 2 * (row - 1)) * mMargin) / row;
                for (int i = 0; i < urls.length; i++) {
                    String url = urls[i];
                    final int pos = i;
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = w;
                    params.height = (int)TransformUtil.dp2px(context, 100f);
                    params.setMargins(mMargin, mMargin, mMargin, mMargin);
                    CornerImageView imageView = new CornerImageView(mContext);
                    imageView.setCornerRadius(mCornerRadius);

//                    if (url.startsWith("http://")) {
//                        url = url.replace("http://","https://");
//                    }

                    com.healthy.library.businessutil.GlideCopy.with(context)
                            .load(url)
                            .placeholder(R.drawable.img_1_1_default)
                            .error(R.drawable.img_1_1_default)

                            .into(imageView);
                    gridLayout.addView(imageView, params);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                                    .withCharSequenceArray("urls", urls)
                                    .withInt("pos", pos)
                                    .navigation();
                        }
                    });
                }
            }
        });

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
}
