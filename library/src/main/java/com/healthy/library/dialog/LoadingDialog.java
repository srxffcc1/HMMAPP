//package com.healthy.library.dialog;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.widget.TextView;
//
//import com.healthy.library.R;
//
//
///**
// * @author Li
// * @date 2018/10/18 0018
// * @des 加载框
// */
//public class LoadingDialog extends AlertDialog {
//    private TextView mTxtMsg;
//
//    public LoadingDialog(@NonNull Context context) {
//        this(context, R.style.LoadingDialog);
//    }
//
//    public LoadingDialog(@NonNull Context context, int themeResId) {
//        super(context, themeResId);
//        initDialog(context);
//    }
//
//    private void initDialog(Context context) {
//        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_loading, null);
//        mTxtMsg = view.findViewById(R.id.txt_loading_msg);
//        setView(view);
//        setCanceledOnTouchOutside(false);
//        Window window = getWindow();
//        if (window != null) {
//            View decorView = window.getDecorView();
//            decorView.setBackground(new ColorDrawable(Color.TRANSPARENT));
//            decorView.setPadding(0, 0, 0, 0);
//        }
//    }
//
//    public void setMessage(CharSequence msg) {
//        mTxtMsg.setText(msg);
//    }
//
//    public CharSequence getMessage() {
//        return mTxtMsg.getText();
//    }
//}
