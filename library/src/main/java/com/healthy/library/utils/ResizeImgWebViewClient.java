package com.healthy.library.utils;


import android.util.Base64;

import com.alibaba.android.arouter.launcher.ARouter;
import com.healthy.library.BuildConfig;
import com.healthy.library.LibApplication;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.MemberAction;
import com.healthy.library.presenter.ActionPresenterCopy;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: long
 * @date: 2021/5/8
 * @des
 */
public class ResizeImgWebViewClient extends WebViewClient {

    Map<String, File> imgReplaceMaps = new HashMap<>();

    public ResizeImgWebViewClient(Map<String, File> imgReplaceMaps) {
        this.imgReplaceMaps = imgReplaceMaps;
    }

    public ResizeImgWebViewClient() {

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        resizeImg(view);
        addImgClickEvent(view);

    }

    String jsString = "javascript:(function () {" +
            "    let objs = document.getElementsByTagName('img');" +
            "    let imgList = new Array();" +
            "    for (let j = 0; j < objs.length; j++) {" +
            "        let parent = objs[j].parentNode;" +
            "        if (parent && parent.nodeName.toLowerCase() === 'a') {" +
            "" +
            "        } else {" +
            "            imgList.push(objs[j]);" +
            "        }" +
            "    }" +
            "" +
            "" +
            "    let array = new Array();" +
            "    for (let i = 0; i < imgList.length; i++) {" +
            "        array[i] = imgList[i].src;" +
            "    }" +
            "    for (let i = 0; i < imgList.length; i++) {" +
            "        imgList[i].i = i;" +
            "        imgList[i].onclick = function () {" +
            "            window.JsBridge.openImage(this.src, array, this.i);" +
            "        }" +
            "    }" +
            "})()";


    /**
     * ????????????????????????
     *
     * @param view
     */
    private void addImgClickEvent(WebView view) {

        view.loadUrl(jsString);

    }

    /**
     * ????????????????????????
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
//    ??????----kanjia
//    ??????----miaosha
//    ??????----pingtuan
//    ????????????-----xinke
//    ????????????-----lingquan
//    ????????????-----huodong
//    ????????????-----qiandao
//    ????????????-------jifen
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        boolean isIntercept = false;
        String mPath = "";
        if (url.contains("kanjia")) {//??????
            isIntercept = true;
            mPath = DiscountRoutes.DIS_NEWKICKLIST;
        }
        if (url.contains("miaosha")) {//??????
            isIntercept = true;
            mPath = DiscountRoutes.DIS_SECKILLLISTACTIVITY;
        }
        if (url.contains("pingtuan")) {//??????
            isIntercept = true;
            mPath = DiscountRoutes.DIS_NEWASSEMBLEACTIVITY;
        }
        if (url.contains("xinke")) {//????????????
            isIntercept = true;
            mPath = DiscountRoutes.DIS_NEWUSERACTIVITY;
        }
        if (url.contains("lingquan")) {//????????????
            isIntercept = true;
            mPath = DiscountRoutes.DIS_CARDCENTER;
            new ActionPresenterCopy(LibApplication.getAppContext()).posAction(
                    new SimpleHashMapBuilder<>()
                            .putObject(new MemberAction(
                                    BuildConfig.VERSION_NAME,
                                    1,
                                    7,
                                    "CardCenterFromChat",
                                    "",
                                    new String(Base64.decode(SpUtils.getValue(LibApplication.getAppContext(), SpKey.USER_ID).getBytes(), Base64.DEFAULT)))
                            ));
        }
        if (url.contains("huodong")) {//????????????
            isIntercept = true;
            mPath = DiscountRoutes.DIS_FLASHBUY;
        }
        if (url.contains("qiandao")) {//????????????
            isIntercept = true;
            mPath = ServiceRoutes.SERVICE_POINTSSIGNIN;
        }
        if (url.contains("jifen")) {//????????????
            isIntercept = true;
            mPath = DiscountRoutes.DIS_POINTHOME;
        }

        if (isIntercept) {
            ARouter.getInstance()
                    .build(mPath)
                    .navigation();
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);

    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
//        if(imgReplaceMaps.get(s)!=null){
//            try {
////                if(!ChannelUtil.isRealRelease()){
////                    Looper.prepare();
////                    Toast.makeText(LibApplication.getAppContext(),"??????????????????",Toast.LENGTH_SHORT).show();
////                    Looper.loop();
////                }
//                System.out.println("?????????????????????");
//                return new WebResourceResponse("image/png", "utf-8", new FileInputStream(imgReplaceMaps.get(s)));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
        return super.shouldInterceptRequest(webView, s);
    }


}