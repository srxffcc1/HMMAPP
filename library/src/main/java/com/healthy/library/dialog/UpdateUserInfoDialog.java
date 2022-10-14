package com.healthy.library.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.healthy.library.R;
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.TransformUtil;

import java.io.Serializable;
import java.util.List;

/**
 * @author: long
 * @date: 2021/5/13
 * @des
 */
public class UpdateUserInfoDialog extends DialogFragment {

    private AlertDialog mAlertDialog;

    public static UpdateUserInfoDialog newInstance() {
        Bundle args = new Bundle();
        UpdateUserInfoDialog fragment = new UpdateUserInfoDialog();
        fragment.setArguments(args);
        return fragment;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (v.getId() == R.id.updateButton) {
                //跳转个人信息界面
                ARouter.getInstance()
                        .build(MineRoutes.MINE_PERSONAL_INFO)
                        .navigation();
            }
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (mAlertDialog == null && getActivity() != null) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update_user_info, null);

            view.findViewById(R.id.updateButton).setOnClickListener(onClickListener);
            view.findViewById(R.id.closeMessageDialog).setOnClickListener(onClickListener);

            mAlertDialog = new AlertDialog.Builder(getActivity())
                    .setView(view)
                    .create();

            mAlertDialog.setCancelable(false);
            mAlertDialog.setCanceledOnTouchOutside(false);

            Window window = mAlertDialog.getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.BottomDialogAnimation);
                View decorView = window.getDecorView();
                decorView.setPadding(0, 0, 0, 0);
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.CENTER;
            }
        }
        return mAlertDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null && getDialog() != null) {
            //设置背景半透明
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if(!manager.isDestroyed() && !manager.isStateSaved()) {
            try {
                super.show(manager, tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
