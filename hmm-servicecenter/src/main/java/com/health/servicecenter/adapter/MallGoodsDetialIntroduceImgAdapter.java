package com.health.servicecenter.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.SimpleStringBuilder;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.utils.JsBridge;
import com.healthy.library.utils.ResizeImgWebViewClient;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.utils.WebViewSetting;
import com.healthy.library.widget.X5WebView;
import com.tencent.smtt.sdk.WebSettings;

public class MallGoodsDetialIntroduceImgAdapter extends BaseAdapter<String> {

    private X5WebView mWebView;
    //0 非webview展示 1webview展示
    private int type = 0;
    private ConstraintLayout mContentLayout;

    public void setType(int type) {
        this.type = type;
    }

    private boolean isLoadSuccess;

    public void setLoadSuccess(boolean loadSuccess) {
        this.isLoadSuccess = loadSuccess;
    }

    @Override
    public int getItemViewType(int position) {
        return 9;
    }

    public MallGoodsDetialIntroduceImgAdapter() {
        this(R.layout.item_order_detial_pic_b);
    }

    private MallGoodsDetialIntroduceImgAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        final ImageView imgIcon = baseHolder.itemView.findViewById(R.id.imgIcon);
        final ImageView imgIcon2 = baseHolder.itemView.findViewById(R.id.imgIcon2);
        final ConstraintLayout imgCon = baseHolder.itemView.findViewById(R.id.imgCon);
        //Log.e("MallImage", "是否加载完毕 = " + isLoadSuccess);

        imgCon.setVisibility(View.GONE);
        if (type == 0) {
            final String src2 = getDatas().get(i);//获取src的绝对路径
            imgCon.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            final int finalI = i;
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(src2)

                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            ////System.out.println("图片路径看看:有问题");
                            ViewGroup.LayoutParams params1 = imgIcon.getLayoutParams();
                            params1.height = 0;
                            imgIcon.setLayoutParams(params1);
                        }

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                            int swidth = (int) (ScreenUtils.getScreenWidth(context) - TransformUtil.dp2px(context, 10));
                            int height = (int) (resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth() * swidth);
                            Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                            if (height > 4096) {//需要做切割
                                ////System.out.println("需要做切割:有问题");
                                ViewGroup.LayoutParams params1 = imgIcon.getLayoutParams();
                                ViewGroup.LayoutParams params2 = imgIcon.getLayoutParams();
                                imgIcon2.setVisibility(View.GONE);
                                Bitmap topBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), (int) (bitmap.getHeight() / 2.0f));
                                Bitmap bottomBitmap = Bitmap.createBitmap(bitmap, 0, (int) (bitmap.getHeight() / 2.0f), bitmap.getWidth(),
                                        bitmap.getHeight() - (int) (bitmap.getHeight() / 2.0f));
                                if (checkBitmap(bitmap)) {
                                    if (params1 != null && topBitmap != null) {
                                        params1.height = height / 2;
                                        imgIcon.setLayoutParams(params1);
                                    }
                                    imgIcon.setImageBitmap(bottomBitmap);
                                    if (params2 != null && bottomBitmap != null) {
                                        params2.height = height / 2;
                                        imgIcon2.setLayoutParams(params2);
                                        imgIcon2.setVisibility(View.VISIBLE);
                                    }
                                    imgIcon2.setImageBitmap(bottomBitmap);
                                } else {
                                    params1.height = 0;
                                    imgIcon.setLayoutParams(params1);
                                }
                                imgIcon2.setImageBitmap(bottomBitmap);
                            } else {
                                imgIcon2.setVisibility(View.GONE);
                                ViewGroup.LayoutParams params1 = imgIcon.getLayoutParams();

                                if (checkBitmap(bitmap)) {
                                    if (params1 != null && bitmap != null) {
                                        params1.height = height;
                                        imgIcon.setLayoutParams(params1);
                                    }
                                    com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(imgIcon);
                                } else {
                                    params1.height = 0;
                                    imgIcon.setLayoutParams(params1);
                                }
                            }


                        }
                    });
            baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                            .withCharSequenceArray("urls", new SimpleStringBuilder().puts(src2).array())
                            .withInt("pos", 0)
                            .navigation();
                }
            });
        } else {
            if (isLoadSuccess) {
                return;
            }
            if (mWebView == null) {
                mContentLayout = baseHolder.getView(R.id.content_layout);
                mWebView = new X5WebView(context, null);
                mWebView.setVerticalScrollBarEnabled(false);
                mWebView.setHorizontalScrollBarEnabled(false);
                mContentLayout.addView(mWebView, new ConstraintLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            }
            /*((Activity) context).getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);*/
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                // chromium, enable hardware acceleration
//                mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//            }
            mWebView.setLayerType(View.LAYER_TYPE_NONE, null);
            //mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            imgCon.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            String sHead = "<html><head><meta name=\"viewport\" content=\"width=device-width, " +
                    "initial-scale=1.0, minimum-scale=0.5, maximum-scale=8.0, user-scalable=yes\" />" +
                    "<style>img{max-width:100% !important;height:auto}</style>"
                    + "<style>body{max-width:100% !important;word-break:break-all;}</style>" + "</head><body>";
            //获取src的绝对路径
            String body = getModel();
            //Log.e("MallGoods", "onBindViewHolder: " + body);
            //html富文本内容不为空且包含 开口P标签
            if (!TextUtils.isEmpty(body) && body.contains("<p>")) {
                body = body.replace("<p></p>", "");
            }
            String data = sHead + body + "</body></html>";

            WebViewSetting.setWebViewParam(mWebView, context);
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

            mWebView.addJavascriptInterface(new JsBridge(), "JsBridge");
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.loadDataWithBaseURL(null, data, "text/html",
                    "UTF-8", null);
            mWebView.setWebViewClient(resizeImgWebViewClient);
            isLoadSuccess = true;
        }
    }

    private ResizeImgWebViewClient resizeImgWebViewClient = new ResizeImgWebViewClient() {
    };

    private boolean checkBitmap(Bitmap bit) {
        //保存所有的像素的数组，图片宽×高
        int[] pixels = new int[bit.getWidth() * bit.getHeight()];
        bit.getPixels(pixels, 0, bit.getWidth(), 0, 0, bit.getWidth(), bit.getHeight());
        //只有一个像素 说明图片有问题 设置为不加载
        if (pixels.length == 1) {
            return false;
        } else {
            return true;
        }
    }

    public void onDestroy() {
        if (mWebView != null) {
            mContentLayout.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }
}
