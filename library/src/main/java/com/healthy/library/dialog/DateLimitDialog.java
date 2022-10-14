package com.healthy.library.dialog;

import android.app.Dialog;
import android.os.Bundle;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.healthy.library.R;
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.interfaces.OnDateLimitConfirmListener;
import com.healthy.library.utils.TimestampUtils;
import com.healthy.library.watcher.AlphaTextView;
import com.healthy.library.widget.WheelPicker;

import java.text.SimpleDateFormat;
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
 * @des 时间选择器 时间段 年月日
 */

public class DateLimitDialog extends DialogFragment implements WheelPicker.OnWheelChangeListener<String> {


    private AlertDialog mAlertDialog;
    private AlphaTextView tvCancel;
    private AlphaTextView tvConfirm;
    private TextView tvChooseTimeTitle;
    private TextView startTimeTitle;
    private TextView endTimeTitle;
    private ConstraintLayout startCon;
    private LinearLayout pickerStartYearLL;
    private LinearLayout pickerStartMonthLL;
    private LinearLayout pickerStartDayLL;
    private ConstraintLayout endCon;
    private LinearLayout pickerEndYearLL;
    private LinearLayout pickerEndMonthLL;
    private LinearLayout pickerEndDayLL;

    private WheelPicker<String> pickerStartYear;
    private WheelPicker<String> pickerStartMonth;
    private WheelPicker<String> pickerStartDay;

    private WheelPicker<String> pickerEndYear;
    private WheelPicker<String> pickerEndMonth;
    private WheelPicker<String> pickerEndDay;
    private long minMillSeconds;
    private long maxMillSeconds;
    private Calendar mMinCalendar;
    private Calendar mMaxCalendar;
    private boolean mFlagDay = false;
    private Calendar startCalendar;

    OnDateLimitConfirmListener mOnConfirmClick;

    boolean isNeedInit=true;
    private int yearendTmp;
    private int monendTmp;

    public void setOnConfirmClick(OnDateLimitConfirmListener onConfirmClick) {
        mOnConfirmClick = onConfirmClick;
    }


    private View.OnClickListener confirmCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateLimitDialog.this.dismiss();
            if (mOnConfirmClick != null) {
                Date dateUp = TimestampUtils.string2Date(String.format("%s/%s/%s",
                        pickerStartYear.getCurrentData(),
                        pickerStartMonth.getCurrentData(),
                        pickerStartDay.getCurrentData()),
                        "yyyy/MM/dd");
                Date dateDown = TimestampUtils.string2Date(String.format("%s/%s/%s",
                        pickerEndYear.getCurrentData(),
                        pickerEndMonth.getCurrentData(),
                        pickerEndDay.getCurrentData()),
                        "yyyy/MM/dd");
                mOnConfirmClick.onConfirm(0, dateUp,dateDown);
            }
        }
    };
    private View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateLimitDialog.this.dismiss();
        }
    };


    public static DateLimitDialog newInstance(long minMillSeconds, long maxMillSeconds) {
        Bundle args = new Bundle();
        DateLimitDialog fragment = new DateLimitDialog();
        args.putLong("min", minMillSeconds);
        args.putLong("max", maxMillSeconds);
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (mAlertDialog == null && getContext() != null) {
            if (getArguments() != null) {
                minMillSeconds = getArguments().getLong("min");
                maxMillSeconds = getArguments().getLong("max");

//                Calendar mMaxCalendarTmp = Calendar.getInstance();
//                mMaxCalendarTmp.setTimeInMillis(maxMillSeconds);
//                mMaxCalendarTmp.set(Calendar.HOUR_OF_DAY,24);
//                mMaxCalendarTmp.set(Calendar.MINUTE,0);
//                mMaxCalendarTmp.set(Calendar.SECOND,0);
//                maxMillSeconds=mMaxCalendarTmp.getTimeInMillis();
            } else {
                return super.onCreateDialog(savedInstanceState);
            }
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_feed_date_limit_wheel, null);
            initView(view);

            init();
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
        }

        return mAlertDialog;
    }
//    private WheelPicker<String> pickerStartYear;
//    private WheelPicker<String> pickerStartMonth;
//    private WheelPicker<String> pickerStartDay;
//    private WheelPicker<String> pickerEndYear;
//    private WheelPicker<String> pickerEndMonth;
//    private WheelPicker<String> pickerEndDay;
    private void init() {
        pickerStartYear.setOnWheelChangeListener(this);
        pickerStartMonth.setOnWheelChangeListener(this);
        pickerStartDay.setOnWheelChangeListener(this);
        pickerEndYear.setOnWheelChangeListener(this);
        pickerEndMonth.setOnWheelChangeListener(this);
        pickerEndDay.setOnWheelChangeListener(this);


        initStartYear();
//        initStartMonth();
//        initStartDay();
//        initEndYear();
//        initEndMonth();
//        initEndDay();
    }

    private void initEndDay() {
        //System.out.println("初始化：结束的天");
        int year = Integer.parseInt(getNumber(pickerEndYear.getCurrentData()));
        int month = Integer.parseInt(getNumber(pickerEndMonth.getCurrentData()));
        List<String> days = new ArrayList<>();
        int totalDays = getDays(year, month);//当月天数
        if (year == mMaxCalendar.get(Calendar.YEAR) && month == (mMaxCalendar.get(Calendar.MONTH) + 1)) {//选的年等于结束年 选的月是结束月
            int daytmp = 1;
            if(startCalendar.get(Calendar.MONTH)+1==month){
//                //System.out.println("当前的月份和开始选择的一致");
                 daytmp = startCalendar.get(Calendar.DAY_OF_MONTH);
            }
            //计算出当前天数
//            //System.out.println("选的年等于结束年 选的月是结束月");
            totalDays=mMaxCalendar.get(Calendar.DAY_OF_MONTH);
            for (int i = daytmp; i <= totalDays; i++) {
//                //System.out.println("选的年等于结束年 选的月是结束月ZZ");
                days.add(String.format(Locale.CHINA, "%02d", i));
            }

        } else if (year == startCalendar.get(Calendar.YEAR) && month == (startCalendar.get(Calendar.MONTH) + 1)) {//选的年月 和 start一致
//            //System.out.println("选的年月 和 start一致");
            int daytmp = 1;
            if(startCalendar.get(Calendar.MONTH)+1==month){
//                //System.out.println("当前的月份和开始选择的一致");
                daytmp = startCalendar.get(Calendar.DAY_OF_MONTH);
            }
            //计算出当前天数
//            //System.out.println("选的年等于结束年 选的月是结束月");
            for (int i = daytmp; i <= totalDays; i++) {
//                //System.out.println("选的年等于结束年 选的月是结束月ZZ");
                days.add(String.format(Locale.CHINA, "%02d", i));
            }
        } else {
            //不会发生这里
//            //System.out.println("普通选择");
            for (int i = 1; i <= totalDays; i++) {
                days.add(String.format(Locale.CHINA, "%02d", i));
            }
        }
        int daynowTmp=Integer.parseInt(getNumber(pickerStartDay.getCurrentData()));
        int dayendTmp= -1;
        try {
            dayendTmp = Integer.parseInt(getNumber(pickerEndDay.getCurrentData()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        pickerEndDay.setDataList(days);
        if(isNeedInit){
            pickerEndDay.setCurrentPosition(isNeedInit?days.size()-1:0);
        }else {
            if(dayendTmp!=-1){
                //System.out.println("不需要日选0初始化");
                Date dateUp = TimestampUtils.string2Date(String.format("%s/%s/%s",
                        pickerStartYear.getCurrentData(),
                        pickerStartMonth.getCurrentData(),
                        pickerStartDay.getCurrentData()),
                        "yyyy/MM/dd");
                Date dateDown = TimestampUtils.string2Date(String.format("%s/%s/%s",
                        pickerEndYear.getCurrentData(),
                        pickerEndMonth.getCurrentData(),
                        String.format(Locale.CHINA, "%02d", dayendTmp)),
                        "yyyy/MM/dd");
                //System.out.println(new SimpleDateFormat("yyyy/MM/dd").format(dateUp));
                //System.out.println(new SimpleDateFormat("yyyy/MM/dd").format(dateDown));
                if(dateDown.after(dateUp)||dateDown.getTime()==dateUp.getTime()){
                    //System.out.println("不需要日选0");
                    for (int i = 0; i <days.size() ; i++) {
                        if(dayendTmp==Integer.parseInt(days.get(i))){
                            pickerEndDay.setCurrentPosition(i);
                            break;
                        }
                    }
                }else {

                    pickerEndDay.setCurrentPosition(0);
                }
            }else {

                pickerEndDay.setCurrentPosition(0);
            }
        }
        isNeedInit=false;
    }

    private void initEndMonth() {
        //System.out.println("初始化：结束的月");
        int minYear = startCalendar.get(Calendar.YEAR);
        int maxYear = mMaxCalendar.get(Calendar.YEAR);

        int year = Integer.parseInt(getNumber(pickerStartYear.getCurrentData()));



        //同一年份
        List<String> monthList = new ArrayList<>();
        if (minYear == maxYear) {
            monthList.clear();
            int monthMin = startCalendar.get(Calendar.MONTH) + 1;
            int monthMax = mMaxCalendar.get(Calendar.MONTH) + 1;
            for (int i = monthMin; i <= monthMax; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d", i));
            }
        } else if (year == minYear) {
            monthList.clear();
            int monthMin = startCalendar.get(Calendar.MONTH) + 1;
            for (int i = monthMin; i <= 12; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d", i));
            }
        } else if (year == maxYear) {
            monthList.clear();
            int monthMax = mMaxCalendar.get(Calendar.MONTH) + 1;
            for (int i = 1; i <= monthMax; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d", i));
            }
        } else {
            monthList.clear();
            for (int i = 1; i <= 12; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d", i));
            }
        }

        int monnowTmp=Integer.parseInt(getNumber(pickerStartMonth.getCurrentData()));
        monendTmp = -1;
        try {
            monendTmp = Integer.parseInt(getNumber(pickerEndMonth.getCurrentData()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        pickerEndMonth.setDataList(monthList);
        if(isNeedInit){

            pickerEndMonth.setCurrentPosition(isNeedInit?monthList.size()-1:0);
        }else {
            if(monnowTmp!=-1){
                //System.out.println("不需要月选0初始化");
                Date dateUp = TimestampUtils.string2Date(String.format("%s/%s/%s",
                        pickerStartYear.getCurrentData(),
                        pickerStartMonth.getCurrentData(),
                        "01"),
                        "yyyy/MM/dd");
                Date dateDown = TimestampUtils.string2Date(String.format("%s/%s/%s",
                        pickerEndYear.getCurrentData(),
                        String.format(Locale.CHINA, "%02d", monendTmp),
                        "01"),
                        "yyyy/MM/dd");
                //System.out.println(new SimpleDateFormat("yyyy/MM/dd").format(dateUp));
                //System.out.println(new SimpleDateFormat("yyyy/MM/dd").format(dateDown));
                if(dateDown.after(dateUp)||dateDown.getTime()==dateUp.getTime()){
                    for (int i = 0; i <monthList.size() ; i++) {
                        //System.out.println("不需要月选0");
                        if(monendTmp ==Integer.parseInt(getNumber(monthList.get(i)))){

                            pickerEndMonth.setCurrentPosition(i);
                            break;
                        }
                    }
                }else {

                    pickerEndMonth.setCurrentPosition(0);
                }
            }else {

                pickerEndMonth.setCurrentPosition(0);
            }
        }

    }
    private String getNumber(String s) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        return m.replaceAll("").trim();
    }
    private void initEndYear() {
        //System.out.println("初始化：结束的年");
        int year = Integer.parseInt(getNumber(pickerStartYear.getCurrentData()));
        int month = Integer.parseInt(getNumber(pickerStartMonth.getCurrentData()));
        int day = Integer.parseInt(getNumber(pickerStartDay.getCurrentData()));



        startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, year);
        startCalendar.set(Calendar.MONTH, month -1);//因为比如要设置5月此处不能填写5 要减1
        startCalendar.set(Calendar.DAY_OF_MONTH, day);

        List<String> mYears = new ArrayList<>();

        mMaxCalendar = Calendar.getInstance();
        mMaxCalendar.setTimeInMillis(maxMillSeconds);

        int minYear = startCalendar.get(Calendar.YEAR);
        int maxYear = mMaxCalendar.get(Calendar.YEAR);

        for (int i = minYear; i <= maxYear; i++) {
            mYears.add(String.valueOf(i));
        }

        int yearnowTmp=Integer.parseInt(getNumber(pickerStartYear.getCurrentData()));
        yearendTmp = -1;
        try {
            yearendTmp = Integer.parseInt(getNumber(pickerEndYear.getCurrentData()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        pickerEndYear.setDataList(mYears);
        if(isNeedInit){

            pickerEndYear.setCurrentPosition(isNeedInit?mYears.size()-1:0);
        }else {
            if(yearendTmp >=yearnowTmp&& yearendTmp !=-1){
                for (int i = 0; i <mYears.size() ; i++) {
                    if(yearendTmp ==Integer.parseInt(mYears.get(i))){

                        pickerEndYear.setCurrentPosition(i);
                        break;
                    }
                }
            }else {
                pickerEndYear.setCurrentPosition(0);
            }
        }
    }

    private void initStartDay() {
        //System.out.println("初始化：开始的天");
        int year = Integer.parseInt(getNumber(pickerStartYear.getCurrentData()));
        int month = Integer.parseInt(getNumber(pickerStartMonth.getCurrentData()));


        startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, year);
        startCalendar.set(Calendar.MONTH, month -1);//因为比如要设置5月此处不能填写5 要减1

        List<String> days = new ArrayList<>();
        int totalDays = getDays(year, month);//当月天数
        if (year == mMinCalendar.get(Calendar.YEAR) && month == (mMinCalendar.get(Calendar.MONTH) + 1)) {//选的年等于最小年 选的月是最小可选月
            int daytmp = mMinCalendar.get(Calendar.DAY_OF_MONTH);//计算出当前天数
            for (int i = daytmp; i <= totalDays; i++) {
                days.add(String.format(Locale.CHINA, "%02d", i));
            }

        } else if (year == mMaxCalendar.get(Calendar.YEAR) && month == (mMaxCalendar.get(Calendar.MONTH) + 1)) {//选的年最大年 选的月是最大月
            for (int i = 1; i <= mMaxCalendar.get(Calendar.DAY_OF_MONTH); i++) {
                days.add(String.format(Locale.CHINA, "%02d", i));
            }
        } else if (year == mMinCalendar.get(Calendar.YEAR) && month == (mMaxCalendar.get(Calendar.MONTH) + 1)) {//选的年最小年 选的月是最大月
            for (int i = 1; i <= mMaxCalendar.get(Calendar.DAY_OF_MONTH); i++) {
                days.add(String.format(Locale.CHINA, "%02d", i));
            }
        } else {
            //不会发生这里
            for (int i = 1; i <= totalDays; i++) {
                days.add(String.format(Locale.CHINA, "%02d", i));
            }
        }
        pickerStartDay.setDataList(days);
        pickerStartDay.setCurrentPosition(isNeedInit?days.size()-1:0);
    }

    private void initStartMonth() {
        int minYear = mMinCalendar.get(Calendar.YEAR);
        int maxYear = mMaxCalendar.get(Calendar.YEAR);

        int year = Integer.parseInt(getNumber(pickerStartYear.getCurrentData()));



        startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, year);

        //同一年份
        List<String> monthList = new ArrayList<>();
        if (minYear == maxYear) {
            monthList.clear();
            int monthMin = mMinCalendar.get(Calendar.MONTH) + 1;
            int monthMax = mMaxCalendar.get(Calendar.MONTH) + 1;
            for (int i = monthMin; i <= monthMax; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d", i));
            }
        } else if (year == minYear) {
            monthList.clear();
            int monthMin = mMinCalendar.get(Calendar.MONTH) + 1;
            for (int i = monthMin; i <= 12; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d", i));
            }
        } else if (year == maxYear) {
            monthList.clear();
            int monthMax = mMaxCalendar.get(Calendar.MONTH) + 1;
            for (int i = 1; i <= monthMax; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d", i));
            }
        } else {
            monthList.clear();
            for (int i = 1; i <= 12; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d", i));
            }
        }
        pickerStartMonth.setDataList(monthList);
        pickerStartMonth.setCurrentPosition(isNeedInit?monthList.size()-1:0);
    }
    //获得这号月几天
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
    private boolean isLeapYear(int year) {
        return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
    }
    private void initStartYear() {
        List mYears = new ArrayList<>();

        mMinCalendar = Calendar.getInstance();
        mMinCalendar.setTimeInMillis(minMillSeconds);


        mMaxCalendar = Calendar.getInstance();
        mMaxCalendar.setTimeInMillis(maxMillSeconds);

        int minYear = mMinCalendar.get(Calendar.YEAR);
        int maxYear = mMaxCalendar.get(Calendar.YEAR);

        for (int i = minYear; i <= maxYear; i++) {
            mYears.add(String.valueOf(i));
        }
        pickerStartYear.setDataList(mYears);
        pickerStartYear.setCurrentPosition(isNeedInit?mYears.size()-1:0);
    }


    @Override
    public void onWheelSelected(View view, String item, int position) {

        if (view == pickerStartYear) {
            initStartMonth();
            initEndYear();
        }
        if (view == pickerStartMonth) {
            initStartDay();
            initEndYear();
        }
        if (view == pickerStartDay) {
            initEndYear();
        }
        if (view == pickerEndYear) {
            initEndMonth();
        }
        if (view == pickerEndMonth) {
            initEndDay();
        }
        if (view == pickerEndDay) {

        }
    }
    //获得这号月几天

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView(View view) {
        tvCancel = (AlphaTextView) view.findViewById(R.id.tv_cancel);
        tvConfirm = (AlphaTextView) view.findViewById(R.id.tv_confirm);
        tvChooseTimeTitle = (TextView) view.findViewById(R.id.tv_choose_time_title);
        startTimeTitle = (TextView) view.findViewById(R.id.startTimeTitle);
        endTimeTitle = (TextView) view.findViewById(R.id.endTimeTitle);
        startCon = (ConstraintLayout) view.findViewById(R.id.startCon);
        pickerStartYearLL = (LinearLayout) view.findViewById(R.id.picker_start_yearLL);
        pickerStartYear = (WheelPicker) view.findViewById(R.id.picker_start_year);
        pickerStartMonthLL = (LinearLayout) view.findViewById(R.id.picker_start_monthLL);
        pickerStartMonth = (WheelPicker) view.findViewById(R.id.picker_start_month);
        pickerStartDayLL = (LinearLayout) view.findViewById(R.id.picker_start_dayLL);
        pickerStartDay = (WheelPicker) view.findViewById(R.id.picker_start_day);
        endCon = (ConstraintLayout) view.findViewById(R.id.endCon);
        pickerEndYearLL = (LinearLayout) view.findViewById(R.id.picker_end_yearLL);
        pickerEndYear = (WheelPicker) view.findViewById(R.id.picker_end_year);
        pickerEndMonthLL = (LinearLayout) view.findViewById(R.id.picker_end_monthLL);
        pickerEndMonth = (WheelPicker) view.findViewById(R.id.picker_end_month);
        pickerEndDayLL = (LinearLayout) view.findViewById(R.id.picker_end_dayLL);
        pickerEndDay = (WheelPicker) view.findViewById(R.id.picker_end_day);
    }
}