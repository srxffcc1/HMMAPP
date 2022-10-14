package com.healthy.library.dialog;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.healthy.library.R;
import com.healthy.library.widget.WheelPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Li
 * @date 2019/05/08 17:40
 * @des 经期日历选择范围
 */
public class ChooseCalendarDialog extends DialogFragment implements
        WheelPicker.OnWheelChangeListener<String> {
    private boolean mFlagMonth = true;
    private AlertDialog mAlertDialog;
    private WheelPicker<String> mPickerYear;

    public void setConfirmClickListener(OnConfirmClickListener confirmClickListener) {
        mConfirmClickListener = confirmClickListener;
    }

    private OnConfirmClickListener mConfirmClickListener;
    private WheelPicker<String> mPickerMonth;
    private View.OnClickListener confirmCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ChooseCalendarDialog.this.dismiss();
            if (mConfirmClickListener != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(getNumber(mPickerYear.getCurrentData())));
                calendar.set(Calendar.MONTH, Integer.parseInt(getNumber(mPickerMonth.getCurrentData()))-1);
                mConfirmClickListener.onConfirmClick(calendar.getTime());
            }

        }
    };
    private View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ChooseCalendarDialog.this.dismiss();
        }
    };

    public static ChooseCalendarDialog newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable("date", date);
        ChooseCalendarDialog fragment = new ChooseCalendarDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onWheelSelected(View view, String item, int position) {
        //获取当前选择的年份
        int year = Integer.parseInt(getNumber(item));
        int minYear = mStartCalendar.get(Calendar.YEAR);
        int maxYear = mEndCalendar.get(Calendar.YEAR);
        //同一年份
        List<String> monthList = new ArrayList<>();
        if (minYear == maxYear) {
            monthList.clear();
            int monthMin = mStartCalendar.get(Calendar.MONTH) + 1;
            int monthMax = mEndCalendar.get(Calendar.MONTH) + 1;
            for (int i = monthMin; i <= monthMax; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d月", i));
            }
        } else if (year == minYear) {
            monthList.clear();
            for (int i = mStartCalendar.get(Calendar.MONTH) + 1; i <= 12; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d月", i));
            }
        } else if (year == maxYear) {
            monthList.clear();
            int monthMax = mEndCalendar.get(Calendar.MONTH) + 1;
            for (int i = 1; i <= monthMax; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d月", i));
            }
        } else {
            monthList.clear();
            for (int i = 1; i <= 12; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d月", i));
            }
        }

        String preData = "";

        if (mFlagMonth) {
            int m = mCurrentCalendar.get(Calendar.MONTH) + 1;
            preData = String.format(Locale.CHINA, "%02d月", m);
            mFlagMonth = false;
        } else {
            if (mPickerMonth.getDataList() != null) {
                preData = mPickerMonth.getCurrentData();
            }
        }

        mPickerMonth.setDataList(monthList);
        int index = monthList.indexOf(preData);
        index = Math.max(0, index);
        mPickerMonth.setCurrentPosition(index);
    }

    private String getNumber(String s) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        return m.replaceAll("").trim();
    }

    private Calendar mStartCalendar;
    private Calendar mEndCalendar;
    private Calendar mCurrentCalendar;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if (mAlertDialog == null && getContext() != null) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_calendar, null);
            mPickerYear = view.findViewById(R.id.picker_year);
            mPickerMonth = view.findViewById(R.id.picker_month);

            TextView tvCancel = view.findViewById(R.id.tv_cancel);
            tvCancel.setOnClickListener(cancelClick);

            TextView tvConfirm = view.findViewById(R.id.tv_confirm);
            tvConfirm.setOnClickListener(confirmCLick);

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
            if (getArguments() != null) {
                Date date = (Date) getArguments().getSerializable("date");
                mStartCalendar = Calendar.getInstance();
                mStartCalendar.setTime(date);
                mStartCalendar.add(Calendar.MONTH, -2);

                mEndCalendar = Calendar.getInstance();
                mEndCalendar.setTime(date);
                mEndCalendar.add(Calendar.MONTH, 12);

                mCurrentCalendar = Calendar.getInstance();
                mCurrentCalendar.setTime(date);

                int startYear = mStartCalendar.get(Calendar.YEAR);
                int endYear = mEndCalendar.get(Calendar.YEAR);

                List<String> years = new ArrayList<>();
                for (int i = startYear; i <= endYear; i++) {
                    years.add(String.valueOf(i + "年"));
                }
                mPickerYear.setOnWheelChangeListener(this);
                mPickerYear.setDataList(years);
                int index = mPickerYear.getDataList().indexOf(mCurrentCalendar.get(Calendar.YEAR) + "年");
                mPickerYear.setCurrentPosition(index);
            }
        }
        return mAlertDialog;
    }

    public interface OnConfirmClickListener {
        /**
         * 选择日期
         *
         * @param date 选择的时间
         */
        void onConfirmClick(Date date);
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
