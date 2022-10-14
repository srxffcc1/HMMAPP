package com.healthy.library.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.healthy.library.R;

/**
 * @author liuwei
 * @des 隐私协议第二页
 */

public class YSXY2Dialog extends DialogFragment {
    private AlertDialog mAlertDialog;
    private TextView ysTitle2;
    private TextView topContent;
    private TextView agreeBtn;
    private TextView noAgreeBtn;


    private int clickNum = 0;

    public void setOnConfirmClick(OnSelectConfirm onConfirmClick) {
        mOnConfirmClick = onConfirmClick;
    }

    public void setOnConfirmPassClick(OnSelectConfirm onConfirmClick) {
        mOnConfirmPassClick = onConfirmClick;
    }

    public interface OnSelectConfirm {
        void onClick(int result);
    }

    private OnSelectConfirm mOnConfirmClick;
    private OnSelectConfirm mOnConfirmPassClick;

    public static YSXY2Dialog newInstance() {
        Bundle args = new Bundle();
        YSXY2Dialog fragment = new YSXY2Dialog();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (mAlertDialog == null && getContext() != null) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.ysxy_2_dialog_layout, null);
            ysTitle2 = (TextView) view.findViewById(R.id.ysTitle2);
            topContent = (TextView) view.findViewById(R.id.topContent);
            agreeBtn = (TextView) view.findViewById(R.id.agreeBtn);
            noAgreeBtn = (TextView) view.findViewById(R.id.noAgreeBtn);
            mAlertDialog = new AlertDialog.Builder(getContext())
                    .setView(view)
                    .create();
            mAlertDialog.setCancelable(false);
            mAlertDialog.setCanceledOnTouchOutside(false);
            /** 禁止物理返回键 */
            mAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    return false;
                }
            });
            Window window = mAlertDialog.getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.BottomDialogAnimation);
                View decorView = window.getDecorView();
                decorView.setPadding(0, 0, 0, 0);
                decorView.setBackgroundColor(Color.parseColor("#ffffff"));
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
            }
        }
        initData();
        return mAlertDialog;
    }


    private void initData() {
        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnConfirmClick!=null){
                    mOnConfirmClick.onClick(1);
                }
                dismiss();
            }
        });
        noAgreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnConfirmClick!=null){
                    mOnConfirmClick.onClick(2);
                }
                dismiss();
            }
        });
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}