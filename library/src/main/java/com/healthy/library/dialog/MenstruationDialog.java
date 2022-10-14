package com.healthy.library.dialog;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.healthy.library.R;
import com.healthy.library.widget.WheelPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/03/18 17:22
 * @des 月经周期、月经时长选择框
 */

public class MenstruationDialog extends DialogFragment {
    private AlertDialog mAlertDialog;
    private List<String> mCycleList;
    private List<String> mDurationList;
    private OnConfirmListener mOnConfirmListener;
    private WheelPicker<String> mPickerCycle;
    private WheelPicker<String> mPickerDuration;

    public static MenstruationDialog newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        MenstruationDialog fragment = new MenstruationDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        mOnConfirmListener = onConfirmListener;
    }

    private View.OnClickListener confirmCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MenstruationDialog.this.dismiss();
            if (mOnConfirmListener != null) {
                mOnConfirmListener.onConfirm(
                        mPickerCycle.getCurrentPosition(),
                        mPickerDuration.getCurrentPosition(),
                        mPickerCycle.getCurrentData(),
                        mPickerDuration.getCurrentData());
            }
        }
    };
    private View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MenstruationDialog.this.dismiss();
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if (mAlertDialog == null && getContext() != null) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_menstruation, null);
            mPickerCycle = view.findViewById(R.id.picker_cycle);
            mPickerDuration = view.findViewById(R.id.picker_duration);

            TextView tvCancel = view.findViewById(R.id.tv_cancel);
            tvCancel.setOnClickListener(cancelClick);

            TextView tvConfirm = view.findViewById(R.id.tv_confirm);
            tvConfirm.setOnClickListener(confirmCLick);

            TextView tvTitle = view.findViewById(R.id.tv_title);
            if (getArguments() != null) {
                String title = getArguments().getString("title");
                if (TextUtils.isEmpty(title)) {
                    tvTitle.setVisibility(View.GONE);
                } else {
                    tvTitle.setVisibility(View.VISIBLE);
                    tvTitle.setText(title);
                }
            }

            initData();
            mPickerCycle.setDataList(mCycleList);
            mPickerDuration.setDataList(mDurationList);
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

    private void initData() {
        mCycleList = new ArrayList<>();
        mDurationList = new ArrayList<>();

        int startCycle = 24;
        int endCycle = 35;
        int startDuration = 1;
        int endDuration = 12;
        for (int i = startCycle; i <= endCycle; i++) {
            mCycleList.add(String.format("周期%s天", i));
        }
        for (int i = startDuration; i <= endDuration; i++) {
            mDurationList.add(String.format("时长%s天", i));
        }
    }

    public interface OnConfirmListener {
        /**
         * 完成
         *
         * @param posCycle    周期pos
         * @param posDuration 时长pos
         * @param txtCycle    周期content
         * @param txtDuration 时长content
         */
        void onConfirm(int posCycle, int posDuration, String txtCycle, String txtDuration);
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
