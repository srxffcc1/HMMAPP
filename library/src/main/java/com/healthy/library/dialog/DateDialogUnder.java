package com.healthy.library.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.utils.TimestampUtils;
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
 * @date 2019/03/19 15:47
 * @des 时间选择器 年月日
 */

public class DateDialogUnder extends DialogFragment implements
        WheelPicker.OnWheelChangeListener<String> {
    private AlertDialog mAlertDialog;
    private List<String> mYears;
    private WheelPicker<String> mPickerYear;
    private WheelPicker<String> mPickerMonth;
    private WheelPicker<String> mPickerDay;
    private long mMinMillSeconds;
    private long mMaxMillSeconds;
    private long mCurrentMillSeconds;
    private Calendar mMinCalendar;
    private Calendar mMaxCalendar;
    private Calendar mCurrentCalendar;
    private boolean mFlagMonth = true;
    private boolean mFlagDay = true;

    public void setOnConfirmClick(OnDateConfirmListener onConfirmClick) {
        mOnConfirmClick = onConfirmClick;
    }

    private OnDateConfirmListener mOnConfirmClick;

    public static DateDialogUnder newInstance(long currentMillSeconds,
                                              long minMillSeconds, long maxMillSeconds, String title) {
        Bundle args = new Bundle();
        DateDialogUnder fragment = new DateDialogUnder();
        args.putLong("min", minMillSeconds);
        args.putLong("max", maxMillSeconds);
        args.putLong("current", currentMillSeconds);
        args.putString("title",title);
        fragment.setArguments(args);

        return fragment;
    }

    private View.OnClickListener confirmCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateDialogUnder.this.dismiss();
            if (mOnConfirmClick != null) {
                //System.out.println(mPickerYear.getCurrentData()+" "+mPickerMonth.getCurrentData()+" "+mPickerDay.getCurrentData());
                Date date = TimestampUtils.string2Date(String.format("%s%s%s",
                        mPickerYear.getCurrentData(),
                        mPickerMonth.getCurrentData(),
                        mPickerDay.getCurrentData()),
                        "yyyy年MM月dd日");
                mOnConfirmClick.onConfirm(0, date);
            }
        }
    };
    private View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateDialogUnder.this.dismiss();
        }
    };

    private void initYears() {
        mYears = new ArrayList<>();

        mMinCalendar = Calendar.getInstance();
        mMinCalendar.setTimeInMillis(mMinMillSeconds);

        mMaxCalendar = Calendar.getInstance();
        mMaxCalendar.setTimeInMillis(mMaxMillSeconds);

        mCurrentCalendar = Calendar.getInstance();
        mCurrentCalendar.setTimeInMillis(mCurrentMillSeconds);

        int minYear = mMinCalendar.get(Calendar.YEAR);
        int maxYear = mMaxCalendar.get(Calendar.YEAR);

        for (int i = minYear; i <= maxYear; i++) {
            mYears.add(String.valueOf(i + "年"));
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (mAlertDialog == null && getContext() != null) {

            if (getArguments() != null) {
                mMinMillSeconds = getArguments().getLong("min");
                mMaxMillSeconds = getArguments().getLong("max");
                mCurrentMillSeconds = getArguments().getLong("current");
                initYears();
            } else {
                return super.onCreateDialog(savedInstanceState);
            }
            View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_date_wheel, null);
            mPickerYear = view.findViewById(R.id.picker_year);
            mPickerMonth = view.findViewById(R.id.picker_month);
            mPickerDay = view.findViewById(R.id.picker_day);
            TextView tvCancel = view.findViewById(R.id.tv_cancel);
            tvCancel.setOnClickListener(cancelClick);

            TextView tvConfirm = view.findViewById(R.id.tv_confirm);
            tvConfirm.setOnClickListener(confirmCLick);

            TextView tvTitle=view.findViewById(R.id.tv_title);
            if (getArguments() != null) {
                String title = getArguments().getString("title");
                if (TextUtils.isEmpty(title)) {
                    tvTitle.setVisibility(View.GONE);
                } else {
                    tvTitle.setVisibility(View.VISIBLE);
                    tvTitle.setText(title);
                }
            }

            mPickerMonth.setOnWheelChangeListener(this);
            mPickerYear.setOnWheelChangeListener(this);

            mPickerYear.setDataList(mYears);
            int index = mPickerYear.getDataList().indexOf(mCurrentCalendar.get(Calendar.YEAR) + "年");
            mPickerYear.setCurrentPosition(0);


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


    @Override
    public void onWheelSelected(View view, String item, int position) {
        if (view == mPickerYear) {
            onYearChanged(item);
        } else if (view == mPickerMonth) {
            onMonthChanged();
        }
    }

    private void onMonthChanged() {
        int year = Integer.parseInt(getNumber(mPickerYear.getCurrentData()));
        int month = Integer.parseInt(getNumber(mPickerMonth.getCurrentData()));
        List<String> days = new ArrayList<>();
        int totalDays = getDays(year, month);
        if (year == mMinCalendar.get(Calendar.YEAR)
                && month == (mMinCalendar.get(Calendar.MONTH) + 1)) {
            int day = mMinCalendar.get(Calendar.DAY_OF_MONTH);
            for (int i = day; i <= totalDays; i++) {
                days.add(String.format(Locale.CHINA, "%02d日", i));
            }

        } else if (year == mMaxCalendar.get(Calendar.YEAR)
                && month == (mMaxCalendar.get(Calendar.MONTH) + 1)) {
            for (int i = 1; i <= mMaxCalendar.get(Calendar.DAY_OF_MONTH); i++) {
                days.add(String.format(Locale.CHINA, "%02d日", i));
            }
        } else {
            for (int i = 1; i <= totalDays; i++) {
                days.add(String.format(Locale.CHINA, "%02d日", i));
            }
        }
        String preData = "";
        if (mFlagDay) {
            int m = mCurrentCalendar.get(Calendar.DAY_OF_MONTH);
            preData = String.format(Locale.CHINA, "%02d日", m);
            mFlagDay = false;
        } else {
            if (mPickerDay.getDataList() != null) {
                preData = mPickerDay.getCurrentData();
            }
        }
        mPickerDay.setDataList(days);
        int index = days.indexOf(preData);
        index = Math.max(0, index);
        mPickerDay.setCurrentPosition(0);
    }

    private void onYearChanged(String item) {
        //获取当前选择的年份
        int year = Integer.parseInt(getNumber(item));
        int minYear = mMinCalendar.get(Calendar.YEAR);
        int maxYear = mMaxCalendar.get(Calendar.YEAR);
        //同一年份
        List<String> monthList = new ArrayList<>();
        if (minYear == maxYear) {
            monthList.clear();
            int monthMin = mMinCalendar.get(Calendar.MONTH) + 1;
            int monthMax = mMaxCalendar.get(Calendar.MONTH) + 1;
            for (int i = monthMin; i <= monthMax; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d月", i));
            }
        } else if (year == minYear) {
            monthList.clear();
            for (int i = mMinCalendar.get(Calendar.MONTH) + 1; i <= 12; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d月", i));
            }
        } else if (year == maxYear) {
            monthList.clear();
            int monthMax = mMaxCalendar.get(Calendar.MONTH) + 1;
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
        mPickerMonth.setCurrentPosition(0);
    }

    private String getNumber(String s) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        return m.replaceAll("").trim();
    }


    private boolean isLeapYear(int year) {
        return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
    }

    private int getDays(int year, int month) {
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            return 31;
        }
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