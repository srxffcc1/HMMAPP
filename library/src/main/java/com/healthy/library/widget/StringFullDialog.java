package com.healthy.library.widget;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;

public class StringFullDialog extends BaseDialogFragment {


    private TextView dialogtitle;
    private TextView dialogbuttonLeft;
    private TextView dialogbuttonRight;
    View.OnClickListener leftClicker;
    View.OnClickListener rightClicker;
    String textLeft;

    String textRight;

    public StringFullDialog setLeftClicker(View.OnClickListener leftClicker) {
        this.leftClicker = leftClicker;
        return this;
    }

    public StringFullDialog setRightClicker(View.OnClickListener rightClicker) {
        this.rightClicker = rightClicker;
        return this;
    }

    public StringFullDialog setTextString(String textLeft, String textRight) {
        this.textLeft = textLeft;
        this.textRight = textRight;
        return this;
    }


    public StringFullDialog setUrl(SpannableStringBuilder url) {
        this.url = url;
        return this;
    }

    public String title;

    public StringFullDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isinhome;

    public StringFullDialog setIsinhome(boolean isinhome) {
        this.isinhome = isinhome;
        return this;
    }

    public SpannableStringBuilder url;


    private TextView mWebView;

    public static StringFullDialog newInstance() {

        Bundle args = new Bundle();
        StringFullDialog fragment = new StringFullDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_web_dialog_fullscreen, null);
        builder.setView(view);
        displayDialog(view);
        return builder.create();
    }

    private void displayDialog(final View view) {
        mWebView = (TextView) view.findViewById(R.id.dialogWebView);
        mWebView.setMovementMethod(LinkMovementMethod.getInstance());
//        dialogbutton = (TextView) view.findViewById(R.id.dialogbutton);
        dialogtitle = (TextView) view.findViewById(R.id.dialogtitle);
        dialogbuttonLeft = (TextView) view.findViewById(R.id.dialogbuttonLeft);
        dialogbuttonRight = (TextView) view.findViewById(R.id.dialogbuttonRight);
        if(url==null){
            dismiss();
        }
        mWebView.setText(url);
        dialogtitle.setText(title);
//        try {
//            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        dialogbuttonLeft.setText(textLeft);
        dialogbuttonRight.setText(textRight);
        dialogbuttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(leftClicker!=null){
                    leftClicker.onClick(v);
                    dismiss();
                }
            }
        });
        dialogbuttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rightClicker!=null){
                    rightClicker.onClick(v);
                    dismiss();
                }
            }
        });
//        mWebView.addJavascriptInterface(new JavaScriptFunction(), "JavaScriptFunction");
//        dialogbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SpUtils.store(view.getContext(),"StringDialog_"+getTag(),true);
//                dismiss();
//            }
//        });

    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景半透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

    }

    private void initView() {
    }
}
