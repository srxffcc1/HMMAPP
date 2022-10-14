package com.healthy.library.widget;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.healthy.library.BuildConfig;
import com.healthy.library.R;
import com.healthy.library.constant.SpKey;
import com.healthy.library.utils.SpUtils;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class WebDialog extends DialogFragment {
    private TextView dialogtitle;

    public WebDialog setUrl(String url) {
        this.url = url;
        return this;
    }

    public boolean isinhome;

    public WebDialog setIsinhome(boolean isinhome) {
        this.isinhome = isinhome;
        return this;
    }

    public String url;


    private X5WebView mWebView;
    private android.widget.TextView dialogbutton;

    public static WebDialog newInstance() {
        Bundle args = new Bundle();
        WebDialog fragment = new WebDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_web_dialog, null);
        builder.setView(view);
        displayDialog(view);
        return builder.create();
    }

    private void displayDialog(final View view) {
        mWebView = (X5WebView) view.findViewById(R.id.dialogWebView);
        dialogbutton = (TextView) view.findViewById(R.id.dialogbutton);
        dialogtitle = (TextView) view.findViewById(R.id.dialogtitle);
        //System.out.println("获得的url"+url);
        mWebView.loadUrl(url);
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setBlockNetworkImage(true);
//        mWebView.addJavascriptInterface(new JavaScriptFunction(), "JavaScriptFunction");
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        dialogbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtils.store(view.getContext(), "WebDialog_" + getTag(), true);
                dismiss();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWebView.destroy();
        mWebView = null;
    }

    /**
     * 用来注入token
     */
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (isinhome) {
                String token = SpUtils.getValue(getContext(), SpKey.TOKEN);
                String js = "localStorage.setItem('token', '" + token + "')";
                mWebView.evaluateJavascript(js, null);
            }

        }
    };
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (isinhome) {
                dialogtitle.setText(view.getTitle());
            }
            mWebView.getSettings().setBlockNetworkImage(false);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            if(isinhome){
//                mIvClose.setVisibility(View.VISIBLE);
//            }
            return super.shouldOverrideUrlLoading(view, request);
        }
    };

    private void initView() {


    }
}
