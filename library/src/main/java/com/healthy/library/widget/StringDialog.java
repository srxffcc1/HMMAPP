package com.healthy.library.widget;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.healthy.library.BuildConfig;
import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.constant.SpKey;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.utils.SpUtils;

import java.util.Map;

public class StringDialog extends BaseDialogFragment {
    private TextView dialogtitle;

    public StringDialog setUrl(SpannableStringBuilder url) {
        this.url = url;
        return this;
    }
    public StringDialog setUrl(String urlJustString) {
        this.urlJustString = urlJustString;
        return this;
    }
    public String title;

    public String urlJustString;

    public StringDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isinhome;

    public StringDialog setIsinhome(boolean isinhome) {
        this.isinhome = isinhome;
        return this;
    }

    public SpannableStringBuilder url;


    private TextView mWebView;
    private View dialogbutton;

    public static StringDialog newInstance() {
        Bundle args = new Bundle();
        StringDialog fragment = new StringDialog();
        fragment.setArguments(args);
        return fragment;
    }
    public static StringDialog newInstance(Map<String, Object> maporg) {
        Bundle args = new Bundle();
        StringDialog fragment = new StringDialog();
        BaseDialogFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        int layoutres=R.layout.layout_string_dialog;
        if(getArguments().getInt("layout",0)!=0){
            layoutres=getArguments().getInt("layout",0);
        }
        final View view = inflater.inflate(layoutres, null);
        builder.setView(view);
        displayDialog(view);
        return builder.create();
    }

    private void displayDialog(final View view) {
        mWebView = (TextView) view.findViewById(R.id.dialogWebView);
        dialogbutton = (View) view.findViewById(R.id.dialogbutton);
        dialogtitle = (TextView) view.findViewById(R.id.dialogtitle);
        if(urlJustString==null){
            mWebView.setText(url);
        }else {
            mWebView.setText(Html.fromHtml(urlJustString));
        }
        dialogtitle.setText(title);
//        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
//        mWebView.addJavascriptInterface(new JavaScriptFunction(), "JavaScriptFunction");
        dialogbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtils.store(view.getContext(),"StringDialog_"+getTag(),true);
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
}
