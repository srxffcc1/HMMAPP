package com.health.servicecenter.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.builder.SimpleStringBuilder;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;

import org.jsoup.select.Elements;

import java.util.List;

public class MallGoodsDetialIntroduceAdapter extends BaseAdapter<GoodsDetail> {


    @Override
    public int getItemViewType(int position) {
        return 4;
    }

    public MallGoodsDetialIntroduceAdapter() {
        this(R.layout.service_item_goodsdetail_introduce);
    }

    private MallGoodsDetialIntroduceAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        GoodsDetail goodsDetail=getModel();
         ConstraintLayout imgLL;
         ConstraintLayout imgLLTop;
         WebView imgLLDetail;
         ConstraintLayout moneyLL;
         ConstraintLayout moneyLLTop;
         TextView moneyTitle;
         TextView moneyValue;
         LinearLayout picLL;
        imgLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.imgLL);
        imgLLTop = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.imgLLTop);
        imgLLDetail = (WebView) baseHolder.itemView.findViewById(R.id.imgLLDetail);
        moneyLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.moneyLL);
        moneyLLTop = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.moneyLLTop);
        moneyTitle = (TextView) baseHolder.itemView.findViewById(R.id.moneyTitle);
        moneyValue = (TextView) baseHolder.itemView.findViewById(R.id.moneyValue);
        LinearLayout videoLL=baseHolder.itemView.findViewById(R.id.videoLL);
        picLL=baseHolder.itemView.findViewById(R.id.picLL);
        buildTopVideo(videoLL,goodsDetail.contentVideos);

        imgLLDetail.setVisibility(View.GONE);
//        {
//            String sHead=   "<html><head><meta name=\"viewport\" content=\"width=device-width, " +
//                    "initial-scale=1.0, minimum-scale=0.5, maximum-scale=8.0, user-scalable=yes\" />"+
//                    "<style>img{max-width:100% !important;height:auto}</style>"
//                    +"<style>body{max-width:100% !important;}</style>"+"</head><body>";
//            String head2="<html><head><style>img{width:100%!important;height:auto}</style></head><body>";
//            String tet=goodsDetail.additionNote;
//            Pattern pattern=Pattern.compile("<img.*?>");
//            Matcher matcher=pattern.matcher(tet);
//            while (matcher.find()){
//                ////System.out.println(matcher.group(0));
//                tet=tet.replace(matcher.group(0),"");
//            }
//            tet=tet.replace("<p></p>","").replace("<p><br></p>","");
//            ////System.out.println("ggg:"+tet);
////            ////System.out.println("ggg:"+JsoupCopy.parse(goodsDetail.additionNote).select("img[src]").remove().html());
//            imgLLDetail.loadDataWithBaseURL(null,
//                    head2+ tet +"</body></html>", "text/html", "utf-8", null);
//            imgLLDetail.setWebViewClient(new ResizeImgWebviewClient());
//            imgLLDetail.getSettings().setJavaScriptEnabled(true);
//            imgLLDetail.addJavascriptInterface(new JsBridge(), "JsBridge");
//
//            imgLLDetail.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
//            imgLLDetail.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//
//            imgLLDetail.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
//            WebViewSettingPost.setWebViewParam(imgLLDetail,context);
//            imgLLDetail.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//            WebSettings settings = imgLLDetail.getSettings();
//            int screenDensity = context.getResources().getDisplayMetrics().densityDpi;
//            WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
////            ////System.out.println("dpi解析:"+screenDensity);
//            if(screenDensity>=DisplayMetrics.DENSITY_XXXHIGH){
//                zoomDensity = WebSettings.ZoomDensity.FAR;
//                settings.setTextZoom(300);
//            }else if(screenDensity>=DisplayMetrics.DENSITY_XXHIGH){
//                zoomDensity = WebSettings.ZoomDensity.FAR;
//                settings.setTextZoom(250);
//            }else if(screenDensity>=DisplayMetrics.DENSITY_XHIGH){
//                zoomDensity = WebSettings.ZoomDensity.FAR;
//                settings.setTextZoom(250);
//            }else if(screenDensity>=DisplayMetrics.DENSITY_MEDIUM){
//                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
//                settings.setTextZoom(150);
//            }else {
//                zoomDensity = WebSettings.ZoomDensity.CLOSE;
//                settings.setTextZoom(100);
//            }
//            settings.setSupportZoom(false);
//            settings.setDefaultZoom(zoomDensity);
//        }
    }

    private void buildTopPic(LinearLayout picLL, Elements select) {
        picLL.removeAllViews();
        for (int i = 0; i <select.size() ; i++) {
            final String src2=select.get(i).attr("src");//获取src的绝对路径
//            ////System.out.println("图片路径看看:"+src2);
            View view= LayoutInflater.from(context).inflate(R.layout.item_order_detial_pic,picLL,false);
            final ImageView imgIcon=view.findViewById(R.id.imgIcon);
            final int finalI = i;
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(src2)
                    
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            ////System.out.println("图片路径看看:有问题");
                            ViewGroup.LayoutParams params1=imgIcon.getLayoutParams();
                            params1.height=0;
                            imgIcon.setLayoutParams(params1);
                        }

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                            int swidth = (int)(ScreenUtils.getScreenWidth(context)- TransformUtil.dp2px(context,20));
                            int height = (int)(resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth() * swidth);
                            ViewGroup.LayoutParams params1=imgIcon.getLayoutParams();

                            Bitmap bitmap=((BitmapDrawable) resource).getBitmap();
                            if(checkBitmap(bitmap)){
                                if(params1!=null&&bitmap!=null){
                                    params1.height=height;
                                    imgIcon.setLayoutParams(params1);
                                }
                                com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(imgIcon);
                            }else {
                                params1.height=0;
                                imgIcon.setLayoutParams(params1);
                            }



                        }
                    });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                            .withCharSequenceArray("urls", new SimpleStringBuilder().puts(src2).array())
                            .withInt("pos", 0)
                            .navigation();
                }
            });
            picLL.addView(view);

        }
    }

    private boolean checkBitmap(Bitmap bit) {
        int[] pixels = new int[bit.getWidth()*bit.getHeight()];//保存所有的像素的数组，图片宽×高
        bit.getPixels(pixels,0,bit.getWidth(),0,0,bit.getWidth(),bit.getHeight());
        if(pixels.length==1){//只有一个像素 说明图片有问题 设置为不加载
            return false;
        }else {
            return true;
        }
    }

    private void buildTopVideo(LinearLayout videoLL, List<GoodsDetail.HeadImages> contentVideos) {
        videoLL.removeAllViews();
        for (int i = 0; i <contentVideos.size() ; i++) {
            final GoodsDetail.HeadImages headImages=contentVideos.get(i);
            View view= LayoutInflater.from(context).inflate(R.layout.item_order_detial_video,videoLL,false);
            final ImageView imgIcon=view.findViewById(R.id.imgIcon);
            if(!TextUtils.isEmpty(headImages.filePath)){
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(headImages.thumbsPath)
                        
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                ////System.out.println("图片路径看看:有问题");
                                ViewGroup.LayoutParams params1=imgIcon.getLayoutParams();
                                params1.height=0;
                                imgIcon.setLayoutParams(params1);
                            }

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                int swidth = (int)(ScreenUtils.getScreenWidth(context)- TransformUtil.dp2px(context,20));
                                int height = (int)(resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth() * swidth);
//                                int height = (int) TransformUtil.dp2px(context,203);
//                                int swidth = (int)(resource.getIntrinsicWidth() * 1.0/resource.getIntrinsicHeight()*height);
                                ViewGroup.LayoutParams params1=imgIcon.getLayoutParams();

                                Bitmap bitmap=((BitmapDrawable) resource).getBitmap();
                                if(checkBitmap(bitmap)){
                                    if(params1!=null&&bitmap!=null){
                                        params1.height=height;
//                                        params1.width=swidth;
                                        imgIcon.setLayoutParams(params1);
                                    }
                                    com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(imgIcon);
                                }else {
                                    params1.height=0;
//                                    params1.width=swidth;
                                    imgIcon.setLayoutParams(params1);
                                }



                            }
                        });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                                .withCharSequenceArray("urls", new SimpleStringBuilder().puts(headImages.filePath).array())
                                .withInt("pos", 0)
                                .navigation();
                    }
                });
                videoLL.addView(view);
            }

        }
    }

    private void initView() {

    }
    public class ResizeImgWebviewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            resizeImg(view);
            addImgClickEvent(view);

        }

        /**
         * 添加图片点击事件
         *
         * @param view
         */
        private void addImgClickEvent(WebView view) {
            view.loadUrl("javascript:(function(){ "
                    + "var objs = document.getElementsByTagName('img');"
                    + " var array=new Array(); " + " for(var j=0;j<objs.length;j++){ "
                    + "array[j]=objs[j].src;" + " }  "
                    + "for(var i=0;i<objs.length;i++){"
                    +"objs[i].i=i;"
                    + "objs[i].onclick=function(){  window.JsBridge.openImage(this.src,array,this.i);" + "}  " + "}    })()");

//            ////System.out.println("javascript:(function(){ "
//                    + "var objs = document.getElementsByTagName('img');"
//                    + " var array=new Array(); " + " for(var j=0;j<objs.length;j++){ "
//                    + "array[j]=objs[j].src;" + " }  "
//                    + "for(var i=0;i<objs.length;i++){"
//                    +"objs[i].i=i;"
//                    + "objs[i].οnclick=function(){  window.JsBridge.openImage(this.src,array,this.i);" + "}  " + "}    })()");
//
//            ////System.out.println("javascript:(function(){"
//                    + "var objs = document.getElementsByTagName('img'); "
//                    + "for(var i=0;i<objs.length;i++)  "
//                    + "{"
//                    + "objs[i].onclick=function()  "
//                    + "    {  "
//                    + " window.JsBridge.openImage(this.src);  "
//                    + "    }  "
//                    + "}"
//                    + "})()");

//            view.loadUrl("javascript:(function(){"
//                    + "var objs = document.getElementsByTagName('img'); "
//                    + "for(var i=0;i<objs.length;i++)  "
//                    + "{"
//                    + "objs[i].onclick=function()  "
//                    + "    {  "
//                    + " window.JsBridge.openImage(this.src);  "
//                    + "    }  "
//                    + "}"
//                    + "})()");
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
    public class JsBridge {
        /**
         * 响应webview上点击图片事件（大图预览）
         *
         * @param url
         */

        @JavascriptInterface
        public void openImage(String url,final String[] array,int postion) {
//            ////System.out.println("点击的图片");

            ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                    .withCharSequenceArray("urls", array)
                    .withInt("pos", postion)
                    .navigation();
        }
    }
}
