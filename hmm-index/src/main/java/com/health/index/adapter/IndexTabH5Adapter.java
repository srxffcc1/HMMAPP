package com.health.index.adapter;

import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.index.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.NewsInfo;
import com.healthy.library.utils.JsBridge;
import com.healthy.library.utils.ResizeImgWebViewClient;
import com.healthy.library.utils.WebViewSetting;
import com.healthy.library.widget.X5WebView;
import com.tencent.smtt.sdk.WebChromeClient;

public class IndexTabH5Adapter extends BaseAdapter<String> {

    public X5WebView tipContentWeb;

    public void setmWebChromeClient(WebChromeClient mWebChromeClient) {
        this.mWebChromeClient = mWebChromeClient;
    }

    public WebChromeClient mWebChromeClient;



    @Override
    public int getItemViewType(int position) {
        return 24;
    }

    public IndexTabH5Adapter() {
        this(R.layout.index_mon_h5);
    }

    private IndexTabH5Adapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        LinearLayoutHelper helper = new LinearLayoutHelper();
        return helper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
         FrameLayout mContentLayout;
        mContentLayout = (FrameLayout) baseHolder.getView(R.id.content_layout);
        if(tipContentWeb==null){
            tipContentWeb = new X5WebView(context, null);
            tipContentWeb.setVerticalScrollBarEnabled(false);
            tipContentWeb.setHorizontalScrollBarEnabled(false);
            mContentLayout.addView(tipContentWeb, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            String sHead = "<html><head><meta name=\"viewport\" content=\"width=device-width, " +
                    "initial-scale=1.0, minimum-scale=0.5, maximum-scale=8.0, user-scalable=yes\" />" +
                    "<style>img{max-width:100% !important;height:auto}</style>"
                    + "<style>body{max-width:100% !important;word-break:break-all;}</style>" + "</head><body>";
            tipContentWeb.setWebViewClient(new ResizeImgWebViewClient(null));
            tipContentWeb.setWebChromeClient(mWebChromeClient);
            tipContentWeb.addJavascriptInterface(new JsBridge(), "JsBridge");

            WebViewSetting.setWebViewParam(tipContentWeb, context);
            tipContentWeb.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//            tipContentWeb.loadDataWithBaseURL(null,
//                    sHead + getModel() + "</body><script> document.body.style.lineHeight = 1.45 </script></html>", "text/html", "utf-8", null);
            tipContentWeb.loadUrl(getModel());
        }
    }

}
