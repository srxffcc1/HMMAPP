package com.healthy.library.utils;

import android.webkit.JavascriptInterface;

import com.alibaba.android.arouter.launcher.ARouter;
import com.healthy.library.routes.LibraryRoutes;

/**
 * JS调用原生方法工具类
 *
 * @author: long
 * @date: 2021/4/5
 */
public class JsBridge {

    @JavascriptInterface
    public void openImage(String url, final String[] array, int postion) {
        //System.out.println("点击的图片");

        ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                .withCharSequenceArray("urls", array)
                .withInt("pos", postion)
                .navigation();
    }

}
