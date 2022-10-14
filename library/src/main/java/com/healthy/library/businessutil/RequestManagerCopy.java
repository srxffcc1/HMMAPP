package com.healthy.library.businessutil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.healthy.library.LibApplication;
import com.healthy.library.utils.FrameActivityManager;

import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestManagerCopy {
    RequestManager requestManager;
    DiskCacheStrategy diskCacheStrategy;
    float thumbnail;

    public RequestManagerCopy() {
        diskCacheStrategy=DiskCacheStrategy.AUTOMATIC;
        thumbnail=1f;
    }

    public RequestManagerCopy with() {
        Context context= null;
        try {
            context = FrameActivityManager.instance().topActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestManager = Glide.with(context==null?LibApplication.getAppContext():context);
        return this;
    }

    public RequestManagerCopy with(Context context) {
        if(context instanceof Activity){
            if(((Activity)context).isFinishing()){
                context=LibApplication.getAppContext();
            }
        }
        requestManager = Glide.with(context==null?LibApplication.getAppContext():context);
        return this;
    }

    public RequestBuilder<Drawable> load(@Nullable Bitmap bitmap) {
        return requestManager.load(bitmap).diskCacheStrategy(diskCacheStrategy);
    }

    public RequestBuilder<Drawable> load(@Nullable Drawable drawable) {
        return requestManager.load(drawable).diskCacheStrategy(diskCacheStrategy);
    }

    public RequestBuilder<Drawable> load(@Nullable String string) {
        if(string!=null){
            if(string.contains("http")){
                if(string.contains("oss")){
//                    if(string.contains("mp4")){
//                        String leftphoto=string.split("\\?")[0];
//                        string= leftphoto+"?x-oss-process=video/snapshot,t_1000,f_jpg,w_"+ (int)(ScreenUtils.getScreenWidth(LibApplication.getAppContext())*0.8)+",h_0,m_fast,ar_auto";
//                    }else if(string.contains("png")||string.contains("jpg")){
//                        String leftphoto=string.split("\\?")[0];
//                        string= leftphoto+"?x-oss-process=image/resize,w_"+ (int)(ScreenUtils.getScreenWidth(LibApplication.getAppContext())*0.8)+",m_lfit";
//                    }
                }else if(string.contains("?")){//图片奇怪的有问题哟
                    String str = string.split("\\?").length > 1 ? string.split("\\?")[1] : string.split("\\?")[0];
                    Map<String, String> map = new HashMap<>();
                    String[] resultarray = str.split("&");
                    for (int i = 0; i < resultarray.length; i++) {
                        map.put(resultarray[i].split("=")[0], resultarray[i].split("=").length > 1 ? resultarray[i].split("=")[1] : "");
                    }
                    Set<Map.Entry<String, String>> set = map.entrySet();
                    for (Map.Entry<String, String> me : set) {
                        if(me.getValue().contains("http")){
                            string= URLDecoder.decode(me.getValue());
                            break;
                        }
                    }
                }

            }
        }
        if(string !=null&&string.contains("gif")){
            RequestBuilder<GifDrawable> load = asGif().load(string);
            return (RequestBuilder)load;
        }
        return requestManager.load(string);
    }

    public RequestBuilder<Drawable> load(@Nullable Uri uri) {
        return requestManager.load(uri).diskCacheStrategy(diskCacheStrategy);
    }

    public RequestBuilder<Drawable> load(@RawRes @DrawableRes @Nullable Integer resourceId) {
        return requestManager.load(resourceId).diskCacheStrategy(diskCacheStrategy);
    }


    public RequestBuilder<Drawable> load(@Nullable URL url) {
        return requestManager.load(url).diskCacheStrategy(diskCacheStrategy);
    }


    public RequestBuilder<Drawable> load(@Nullable byte[] model) {
        return requestManager.load(model).diskCacheStrategy(diskCacheStrategy);
    }


    public RequestBuilder<Drawable> load(@Nullable Object model) {
        return requestManager.load(model).diskCacheStrategy(diskCacheStrategy);
    }
    public RequestBuilder<Bitmap> asBitmap() {
        return requestManager.asBitmap().diskCacheStrategy(diskCacheStrategy);
    }
    public RequestBuilder<GifDrawable> asGif() {
        return requestManager.asGif();
    }
}
