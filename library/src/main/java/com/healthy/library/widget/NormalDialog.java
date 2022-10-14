//package com.healthy.library.widget;
//
//import android.app.Dialog;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.webkit.WebChromeClient;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.DialogFragment;
//import androidx.fragment.app.FragmentManager;
//
//import com.healthy.library.R;
//import com.healthy.library.constant.SpKey;
//import com.healthy.library.constant.UrlKeys;
//import com.healthy.library.utils.SpUtils;
//
//public class NormalDialog extends DialogFragment {
//
//
//    public NormalDialog setLayoutRes(int layoutRes) {
//        this.layoutRes = layoutRes;
//        return this;
//    }
//    @Override
//    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
//        try {
//            super.show(manager, tag);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public int layoutRes;
//
//    public static NormalDialog newInstance() {
//
//        Bundle args = new Bundle();
//        NormalDialog fragment = new NormalDialog();
//        fragment.setArguments(args);
//        return fragment;
//    }
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        final View view = inflater.inflate(layoutRes, null);
//        builder.setView(view);
//        displayDialog(view);
//        return builder.create();
//    }
//
//    private void displayDialog(final View view) {
//        View closebutton = (View) view.findViewById(R.id.dialogbutton);
//        if(closebutton!=null){
//            closebutton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismiss();
//                }
//            });
//        }
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        //设置背景半透明
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        getDialog().setCancelable(true);
//        getDialog().setCanceledOnTouchOutside(true);
//
//    }
//}
