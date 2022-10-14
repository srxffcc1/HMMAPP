package com.healthy.library.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.utils.LogUtils;


public class SignContractDialog extends BaseDialogFragment {

    private TextView couponTitle;
    private WebView mWebView;
    private ImageView close;
    private TextView couponFinish;
    private String url = null;
    private getContentListener getListener;

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SignContractDialog newInstance() {
        Bundle args = new Bundle();
        SignContractDialog fragment = new SignContractDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public SignContractDialog setData(String url) {
        this.url = url;
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.dimAmount = 0.5f;
        window.setAttributes(params);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.item_account_sign_contract_dialog_layout, null);
        builder.setView(view);
        displayDialog(view);
        Dialog result = builder.create();
        return result;
    }

    private void displayDialog(View view) {
        couponTitle = (TextView) view.findViewById(R.id.couponTitle);
        mWebView = (WebView) view.findViewById(R.id.webView);
        close = (ImageView) view.findViewById(R.id.close);
        couponFinish = view.findViewById(R.id.couponFinish);
        initData();
    }

    private void initData() {
        WebSettings wSet = mWebView.getSettings();
        wSet.setJavaScriptEnabled(true);
        // 启动应用缓存
        wSet.setAppCacheEnabled(false);
        // 设置缓存模式
        wSet.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.clearFormData();
        mWebView.loadUrl(url);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListener.resultContent();
                dismiss();
            }
        });
    }

    public interface getContentListener {

        void resultContent();
    }

    public void setResultListener(getContentListener getListener) {
        this.getListener = getListener;
    }
}
