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
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.widget.WheelPicker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择技师
 */

public class SelectTechnicianDateDialog extends DialogFragment implements
        WheelPicker.OnWheelChangeListener<String> {
    private AlertDialog mAlertDialog;
    private List<String> mYears = new ArrayList<>();
    private WheelPicker<String> mPickerYear;
    private List<AppointmentMainItemModel.TechnicianList> list;

    public void setOnConfirmClick(OnSelectConfirm onConfirmClick) {
        mOnConfirmClick = onConfirmClick;
    }

    public interface OnSelectConfirm {
        void onClick(int currentPosition);
    }

    private OnSelectConfirm mOnConfirmClick;

    public static SelectTechnicianDateDialog newInstance() {
        Bundle args = new Bundle();
        SelectTechnicianDateDialog fragment = new SelectTechnicianDateDialog();
        //args.putSerializable("list", (Serializable) technicianLists);
        fragment.setArguments(args);

        return fragment;
    }

    public void setTechnicianList(List<AppointmentMainItemModel.TechnicianList> technicianLists) {
        this.list = technicianLists;
    }

    private View.OnClickListener confirmCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SelectTechnicianDateDialog.this.dismiss();
            if (mOnConfirmClick != null) {
                mOnConfirmClick.onClick(mPickerYear.getCurrentPosition());
            }
        }
    };
    private View.OnClickListener cancelClick = v -> SelectTechnicianDateDialog.this.dismiss();

    private void initYears() {
        if (ListUtil.isEmpty(list)) {
            dismiss();
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null) {
                mYears.add("");
                continue;
            }
            mYears.add(list.get(i).getNickname());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (mAlertDialog == null && getContext() != null) {
            if (getArguments() != null) {
                initYears();
            } else {
                return super.onCreateDialog(savedInstanceState);
            }
            View view = LayoutInflater.from(getContext()).inflate(R.layout.lieve_chart_dialog_date_wheel, null);
            mPickerYear = view.findViewById(R.id.picker_year);
            TextView tvCancel = view.findViewById(R.id.tv_cancel);
            tvCancel.setOnClickListener(cancelClick);
            TextView tvConfirm = view.findViewById(R.id.tv_confirm);
            tvConfirm.setOnClickListener(confirmCLick);
            mPickerYear.setOnWheelChangeListener(this);
            mPickerYear.setDataList(mYears);
            mAlertDialog = new AlertDialog.Builder(getContext())
                    .setView(view)
                    .create();
            mAlertDialog.setCancelable(true);
            mAlertDialog.setCanceledOnTouchOutside(true);
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


    @Override
    public void onWheelSelected(View view, String item, int position) {
        if (view == mPickerYear) {
        }
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