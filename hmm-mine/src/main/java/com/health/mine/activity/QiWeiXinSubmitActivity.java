package com.health.mine.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.arouter.utils.TextUtils;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.lib_ShapeView.view.ShapeTextView;
import com.health.mine.R;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.contract.QiYeWeiXinUpLoadContract;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.model.Topic;
import com.healthy.library.net.RxLifecycleUtils;
import com.healthy.library.net.RxThreadUtils;
import com.healthy.library.presenter.QiYeWeiXinUpLoadPresenter;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.BitmapUtils;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.utils.ImageUtil;
import com.healthy.library.utils.PermissionUtils;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.lxj.matisse.Matisse;
import com.lxj.matisse.MimeType;
import com.lxj.matisse.engine.impl.GlideEngine;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

@Route(path = MineRoutes.MINE_QIYEWEIXIN)
public class QiWeiXinSubmitActivity extends BaseActivity implements QiYeWeiXinUpLoadContract.View {
    private static final int REQUEST_IMAGE = 666;
    private static final int REQUEST_IMAGE2 = 667;
    Bitmap sbitmapFianl;
    private TopBar topBar;
    private StatusLayout layoutStatus;
    private ImageView zixngBg;
    private android.widget.TextView zixngTitle;
    private android.widget.TextView zixngSTitle;
    private ImageView zxingImg;
    private com.example.lib_ShapeView.view.ShapeTextView zxingTip;
    private com.example.lib_ShapeView.view.ShapeTextView submitZxing;
    private android.widget.TextView submitZxingLine;
    private Bitmap sbitmapCode;
    QiYeWeiXinUpLoadPresenter qiYeWeiXinUpLoadPresenter;
    @Autowired
    String tokerWorkerId;
    @Autowired
    TokerWorkerInfoModel.TokerWorkerBean tokerWorker;
    private static final int RC_PERMISSION = 45;

    private String[] mPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qiyeweixin_submit;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        qiYeWeiXinUpLoadPresenter = new QiYeWeiXinUpLoadPresenter(this, this);
        GlideCopy.with(mContext).load(R.drawable.ww_ic_launcher)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        sbitmapFianl = DrawableUtils.drawableToBitmap(resource);
                        if(tokerWorker!=null&&tokerWorker.workWxUrl!=null){
                            zixngTitle.setText("修改企业二维码");
                            zixngSTitle.setText("点击二维码可选择相册进行修改");
                            urls=new SimpleArrayListBuilder<String>().adds(tokerWorker.workWxImg);
//                            sbitmapCode=CodeUtils.createImage(tokerWorker.workWxUrl,650, 650,sbitmapFianl);
                            zxingImg.setImageBitmap(CodeUtils.createImage(tokerWorker.workWxUrl,650, 650,sbitmapFianl));
                            zxingTip.setVisibility(View.GONE);
//                            submitZxing.getShapeDrawableBuilder().setSolidColor(Color.parseColor("#FA3C5A")).intoBackground();
                        }
                    }
                });
        zxingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtils.hasPermissions(mContext, mPermissions)) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_IMAGE);
                } else {
                    PermissionUtils.requestPermissions(QiWeiXinSubmitActivity.this, RC_PERMISSION, mPermissions);
                }

            }
        });

        submitZxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSubmit();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_PERMISSION) {
            if (!PermissionUtils.hasPermissions(mContext, mPermissions)) {
                showToast("需要同意存储权限才能选择图片");
//                PermissionUtils.requestPermissions(this, RC_PERMISSION, mPermissions);
            }
        }
    }

    private boolean checkIllegal() {
        if (sbitmapCode == null) {
            showToast("请选择企业微信名片");
            return false;
        }
        if (TextUtils.isEmpty(tokerWorkerId)) {
            showToast("当前只有导购可以关联企业微信二维码");
            return false;
        }
        return true;
    }

    private void goSubmit() {
        if (checkIllegal()) {
            if(urls!=null){
                onUpLoadSuccess(urls,1);
            }else {

                uploadImgs(new SimpleArrayListBuilder<Bitmap>().adds(sbitmapCode));
            }
        }
    }

    private List<String> mBase64Imgs = new ArrayList<>();

    private void uploadImgs(final List<Bitmap> bitmaps) {
        mBase64Imgs.clear();
        showLoading();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                for (Bitmap bitmap : bitmaps) {
                    String base64 = BitmapUtils.bitmapToBase64(bitmap);
                    emitter.onNext(base64);
                }
                emitter.onComplete();
            }
        }).compose(RxThreadUtils.<String>Obs_io_main())
                .to(RxLifecycleUtils.<String>bindLifecycle(this))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(String s) {
                        mBase64Imgs.add(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showContent();
                        showToast("上传图片失败");
                    }

                    @Override
                    public void onComplete() {
                        qiYeWeiXinUpLoadPresenter.uploadFile(mBase64Imgs, 0);
                    }
                });
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                final Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            System.out.println("解析成功:" + result);
//                            sbitmapCode=mBitmap;
                            urls=null;
                            sbitmapCode = BitmapUtils.compressBitmap(ImageUtil.getImageAbsolutePath(mContext, uri));
                            if(result!=null&&result.contains("weixin")){
                                showToast("已为您分析出企业微信二维码请选择上传");
                            }else {
                                showToast("请使用企业微信个人名片");
                                sbitmapCode=null;
                            }
                            if(mBitmap==null){
                                mBitmap=CodeUtils.createImage(result,650, 650,sbitmapFianl);
                            }
                            zxingImg.setImageBitmap(mBitmap);
                            zxingTip.setVisibility(View.GONE);
                            submitZxing.getShapeDrawableBuilder().setSolidColor(Color.parseColor("#FA3C5A")).intoBackground();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            zxingFail();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void zxingFail() {
        zxingTip.setVisibility(View.VISIBLE);
        submitZxing.getShapeDrawableBuilder().setSolidColor(Color.parseColor("#DCDDDC")).intoBackground();
        sbitmapCode = null;
        showToast("获取企业微信名片失败!\n请选择正确的名片图\n或更换名片图款式");
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        zixngBg = (ImageView) findViewById(R.id.zixngBg);
        zixngTitle = (TextView) findViewById(R.id.zixngTitle);
        zixngSTitle = (TextView) findViewById(R.id.zixngSTitle);
        zxingImg = (ImageView) findViewById(R.id.zxingImg);
        zxingTip = (ShapeTextView) findViewById(R.id.zxingTip);
        submitZxing = (ShapeTextView) findViewById(R.id.submitZxing);
        submitZxingLine = (TextView) findViewById(R.id.submitZxingLine);
    }

    @Override
    public void onSucessUpload() {
        finish();
    }
    List<String> urls;
    @Override
    public void onUpLoadSuccess(List<String> urls, int type) {
        this.urls=urls;
        if (ListUtil.isEmpty(this.urls)) {
            showToast("上传图片失败");
        } else {
            qiYeWeiXinUpLoadPresenter.uploadQiWeiXin(this.urls.get(0), tokerWorkerId);
        }
    }

    @Override
    public void onFailPost() {

    }
}
