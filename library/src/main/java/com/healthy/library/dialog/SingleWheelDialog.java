package com.healthy.library.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
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
import com.healthy.library.base.BaseDialogFragment;
import com.healthy.library.widget.WheelPicker;

import java.util.ArrayList;

/**
 * @author Li
 * @date 2019/03/19 10:50
 * @des 单项选择器
 */

public class SingleWheelDialog extends BaseDialogFragment {
    private AlertDialog mAlertDialog;
    private OnConfirmClickListener mOnConfirmClick;
    private WheelPicker<String> mPicker;

    public static SingleWheelDialog newInstance(ArrayList<String> list) {
        return newInstance(list, 0);
    }

    public static SingleWheelDialog newInstance(ArrayList<String> list, int pos) {
        Bundle args = new Bundle();
        SingleWheelDialog fragment = new SingleWheelDialog();
        args.putStringArrayList("list", list);
        args.putInt("pos", pos);
        fragment.setArguments(args);
        return fragment;
    }
    public static SingleWheelDialog newInstance(ArrayList<String> list, String title) {
        Bundle args = new Bundle();
        SingleWheelDialog fragment = new SingleWheelDialog();
        args.putStringArrayList("list", list);
        args.putInt("pos", 0);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }


    private View.OnClickListener confirmCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SingleWheelDialog.this.dismiss();
            if (mOnConfirmClick != null) {
                mOnConfirmClick.onClick(mPicker.getCurrentPosition(), mPicker.getCurrentData());
            }
        }
    };
    private View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SingleWheelDialog.this.dismiss();
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getContext() != null && mAlertDialog == null) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_single_wheel, null);
            mPicker = view.findViewById(R.id.picker_single);

            TextView tvCancel = view.findViewById(R.id.tv_cancel);
            tvCancel.setOnClickListener(cancelClick);
            TextView title=view.findViewById(R.id.tv_title);
            TextView tvConfirm = view.findViewById(R.id.tv_confirm);
            tvConfirm.setOnClickListener(confirmCLick);


            if (getArguments() != null) {
                ArrayList<String> list = getArguments().getStringArrayList("list");
                int pos = getArguments().getInt("pos");
                if (list != null) {
                    mPicker.setDataList(list);
                    mPicker.setCurrentPosition(pos);
                }
                title.setText(getArguments().getString("title",""));
            }
            mAlertDialog = new AlertDialog.Builder(getContext())
                    .setView(view)
                    .create();
            mAlertDialog.setCancelable(false);
            mAlertDialog.setCanceledOnTouchOutside(false);
            Window window = mAlertDialog.getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.BottomDialogAnimation);
                View decorView = window.getDecorView();
                decorView.setPadding(0, 0, 0, 0);
                decorView.setBackgroundResource(R.drawable.shape_dialog);
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.gravity = Gravity.BOTTOM;
            }
        }
        return mAlertDialog;
    }

    public void setOnConfirmClick(OnConfirmClickListener onConfirmClick) {
        mOnConfirmClick = onConfirmClick;
    }

    public interface OnConfirmClickListener {
        /**
         * 点击完成事件
         *
         * @param pos  滚轮位置
         * @param data 滚轮数据
         */
        void onClick(int pos, String data);
    }
    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
