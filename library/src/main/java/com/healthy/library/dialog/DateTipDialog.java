package com.healthy.library.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.healthy.library.R;
import com.healthy.library.interfaces.OnDateTipConfirmListener;
import com.healthy.library.watcher.AlphaTextView;
import com.healthy.library.widget.WheelPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Li
 * @date 2019/03/19 15:47
 * @des 时间选择器 年月日
 */

public class DateTipDialog extends DialogFragment implements WheelPicker.OnWheelChangeListener<String> {

    private AlertDialog mAlertDialog;

    String[] days;
    OnDateTipConfirmListener mOnConfirmClick;
    private AlphaTextView tvCancel;
    private AlphaTextView tvConfirm;
    private LinearLayout pickerDayLL;
    private WheelPicker pickerDay;
    private LinearLayout pickerHourLL;
    private WheelPicker pickerHour;
    private TextView tvHour;
    private LinearLayout pickerMinLL;
    private WheelPicker pickerMin;
    private TextView tvMin;

    boolean isNeedInit=false;
    private String init;


    public static DateTipDialog newInstance(String init,String[] days) {
        Bundle args = new Bundle();
        DateTipDialog fragment = new DateTipDialog();
        args.putStringArray("days",days);
        args.putString("init",init);
        fragment.setArguments(args);

        return fragment;
    }

    public void setOnConfirmClick(OnDateTipConfirmListener onConfirmClick) {
        mOnConfirmClick = onConfirmClick;
    }

    private View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateTipDialog.this.dismiss();
        }
    };

    private View.OnClickListener confirmCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateTipDialog.this.dismiss();
            if (mOnConfirmClick != null) {
                String[] result=new String[]{(pickerDay.getCurrentPosition()+1)+"",pickerHour.getCurrentData().toString(),pickerMin.getCurrentData().toString()};
                mOnConfirmClick.onConfirm(0, result);
            }
        }
    };

    @Override
    public void onWheelSelected(View view, String item, int position) {
        if (view == pickerDay) {

        } else if (view == pickerHour) {

        }else {

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (mAlertDialog == null && getContext() != null) {
            if (getArguments() != null) {
                days=getArguments().getStringArray("days");
                init=getArguments().getString("init");
            } else {
                return super.onCreateDialog(savedInstanceState);
            }
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_tip, null);
            initView(view);
            initDate();

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

    private void initDate() {
        tvCancel.setOnClickListener(cancelClick);
        tvConfirm.setOnClickListener(confirmCLick);



        if(!TextUtils.isEmpty(init)){
            isNeedInit=true;
        }

        initDay();

//        onDayChanged();


    }

    private void initDay() {
        List<String> day = new ArrayList<>();
        for (int i = 0; i <days.length ; i++) {
            day.add(days[i]);
        }
        pickerDay.setDataList(day);
        pickerDay.setCurrentPosition(0);
        onDayChanged();
    }

    private void onHourChanged() {
        int destMin=0;
        List<String> mins = new ArrayList<>();
        while (destMin<60){
            mins.add(String.format(Locale.CHINA, "%02d", destMin));
            destMin+=1;
        }
        pickerMin.setDataList(mins);
        if(isNeedInit){
            for (int i = 0; i <mins.size() ; i++) {
                if(init.split(":")[1].trim().equals(mins.get(i))){

                    pickerMin.setCurrentPosition(i);
                    break;
                }
            }
        }else {
            pickerMin.setCurrentPosition(30);
        }
        isNeedInit=false;
    }


    private void initView(View view) {

        tvCancel = (AlphaTextView) view.findViewById(R.id.tv_cancel);
        tvConfirm = (AlphaTextView) view.findViewById(R.id.tv_confirm);
        pickerDayLL = (LinearLayout) view.findViewById(R.id.picker_dayLL);
        pickerDay = (WheelPicker)view. findViewById(R.id.picker_day);
        pickerHourLL = (LinearLayout) view.findViewById(R.id.picker_hourLL);
        pickerHour = (WheelPicker) view.findViewById(R.id.picker_hour);
        tvHour = (TextView) view.findViewById(R.id.tv_hour);
        pickerMinLL = (LinearLayout)view. findViewById(R.id.picker_minLL);
        pickerMin = (WheelPicker)view. findViewById(R.id.picker_min);
        tvMin = (TextView) view.findViewById(R.id.tv_min);
    }
    private void onDayChanged() {
        List<String> hours = new ArrayList<>();
        for (int i = 0; i <24 ; i++) {
            hours.add(String.format("%02d", i)+"");
        }
        pickerHour.setDataList(hours);

        if(isNeedInit){
            for (int i = 0; i <hours.size() ; i++) {
                if(init.split(":")[0].trim().equals(hours.get(i))){
                    pickerHour.setCurrentPosition(i);
                    break;
                }
            }
        }else {
            pickerHour.setCurrentPosition(0);
        }
        onHourChanged();
    }
}