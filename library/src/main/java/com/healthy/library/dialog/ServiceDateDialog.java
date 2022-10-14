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
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.utils.TimestampUtils;
import com.healthy.library.widget.WheelPicker;
import com.tencent.bugly.crashreport.CrashReport;

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
 * @des 时间选择器 年月日时分
 */

public class ServiceDateDialog extends DialogFragment implements
        WheelPicker.OnWheelChangeListener<String> {
    private AlertDialog mAlertDialog;
    private List<String> mYears;
    private WheelPicker<String> mPickerYear;
    private WheelPicker<String> mPickerMonth;
    private long mMinMillSeconds;
    private long mCurrentMillSeconds=0;
    private long mMaxMillSeconds;
    private int mStartHour=9;
    private int mEndHour=18;
    private int mStartMin=0;
    private int mEndMin=0;
    private int mLimitDay=7;
    private Calendar mMinCalendar;
    private Calendar mMaxCalendar;
    private Calendar mCurrentCalendar;
    private boolean mFlagDay = true;
    private WheelPicker<String> mPickerDay;
    private WheelPicker<String> mPickerHour;
    private WheelPicker<String> mPickerMin;
    private OnDateConfirmListener mOnConfirmClick;
    private TextView mTvDate;
    private View.OnClickListener confirmCLick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ServiceDateDialog.this.dismiss();
            if (mOnConfirmClick != null) {
                Date date = TimestampUtils.string2Date(String.format("%s-%s-%s %s:%s",
                        mPickerYear.getCurrentData(),
                        mPickerMonth.getCurrentData(),
                        mPickerDay.getCurrentData(),
                        mPickerHour.getCurrentData(),
                        mPickerMin.getCurrentData()),
                        "yyyy-MM-dd HH:mm");
                mOnConfirmClick.onConfirm(0, date);
            }
        }
    };
    private View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ServiceDateDialog.this.dismiss();
        }
    };

    /**
     *
     * @param minMillSeconds 最小可选时间
     * @param maxMillSeconds 最大可选时间
     * @param startHour  营业开始时间时
     * @param endHour 营业结束时间分
     * @param startMin 开始时间分
     * @param endMin 结束时间分
     * @return
     */
    public static ServiceDateDialog newInstance(long minMillSeconds, long maxMillSeconds,
                                                int startHour, int endHour,
                                                int startMin, int endMin,int limit) {
        Bundle args = new Bundle();
        ServiceDateDialog fragment = new ServiceDateDialog();
        args.putLong("min", minMillSeconds);
        args.putLong("max", maxMillSeconds);
        args.putInt("startHour", startHour);
        args.putInt("endHour", endHour);
        args.putInt("startMin", startMin);
        args.putInt("endMin", endMin);
        args.putInt("limit", limit);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *
     * @param minMillSeconds 最小可选时间
     * @param maxMillSeconds 最大可选时间
     * @param startHour  营业开始时间时
     * @param endHour 营业结束时间分
     * @param startMin 开始时间分
     * @param endMin 结束时间分
     * @return
     */
    public static ServiceDateDialog newInstance(long minMillSeconds, long maxMillSeconds,long mCurrentMillSeconds,
                                                int startHour, int endHour,
                                                int startMin, int endMin,int limit) {
        Bundle args = new Bundle();
        ServiceDateDialog fragment = new ServiceDateDialog();
        args.putLong("mCurrent",mCurrentMillSeconds);
        args.putLong("min", minMillSeconds);
        args.putLong("max", maxMillSeconds);
        args.putInt("startHour", startHour);
        args.putInt("endHour", endHour);
        args.putInt("startMin", startMin);
        args.putInt("endMin", endMin);
        args.putInt("limit", limit);
        fragment.setArguments(args);

        return fragment;
    }

    public static ServiceDateDialog newInstance() {
        Bundle args = new Bundle();
        ServiceDateDialog fragment = new ServiceDateDialog();


        Calendar startCalendar = Calendar.getInstance();
        int currMin = startCalendar.get(Calendar.MINUTE);

        int destMin= getDestMin(currMin);
        if(destMin>50){
            startCalendar.set(Calendar.MINUTE,0);
            startCalendar.add(Calendar.HOUR_OF_DAY,1);
        }else {
            startCalendar.set(Calendar.MINUTE,destMin);
        }



        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(startCalendar.getTimeInMillis());
        endCalendar.add(Calendar.DAY_OF_MONTH, 7);

        args.putLong("min",startCalendar.getTimeInMillis());
        args.putLong("max", endCalendar.getTimeInMillis());
        args.putInt("startHour", 9);
        args.putInt("endHour", 18);
        args.putInt("startMin", 0);
        args.putInt("endMin", 0);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnConfirmClick(OnDateConfirmListener onConfirmClick) {
        mOnConfirmClick = onConfirmClick;
    }

    private void initYears() {
        mYears = new ArrayList<>();

        mMinCalendar = Calendar.getInstance();
        mMinCalendar.setTimeInMillis(mMinMillSeconds);


        mMaxCalendar = Calendar.getInstance();
        mMaxCalendar.setTimeInMillis(mMaxMillSeconds);

        mCurrentCalendar = Calendar.getInstance();
        if(mCurrentMillSeconds!=0){
            mCurrentCalendar.setTimeInMillis(mCurrentMillSeconds);
        }
        mCurrentCalendar.set(Calendar.SECOND, 0);//当前时间到0秒

        int minYear = mMinCalendar.get(Calendar.YEAR);
        int maxYear = mMaxCalendar.get(Calendar.YEAR);

        for (int i = minYear; i <= maxYear; i++) {
            mYears.add(String.valueOf(i));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (mAlertDialog == null && getContext() != null) {
            if (getArguments() != null) {
                mMinMillSeconds = getArguments().getLong("min");
                mMaxMillSeconds = getArguments().getLong("max");
                mStartHour = getArguments().getInt("startHour");
                mEndHour = getArguments().getInt("endHour");
                mStartMin = getArguments().getInt("startMin");
                mEndMin = getArguments().getInt("endMin");
                mLimitDay=getArguments().getInt("limit");
                mCurrentMillSeconds=getArguments().getLong("mCurrent");
                initYears();
            } else {
                return super.onCreateDialog(savedInstanceState);
            }
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_service_date_wheel, null);
            mPickerYear = view.findViewById(R.id.picker_year);
            mPickerMonth = view.findViewById(R.id.picker_month);
            mPickerDay = view.findViewById(R.id.picker_day);
            mPickerHour = view.findViewById(R.id.picker_hour);
            mPickerMin = view.findViewById(R.id.picker_min);
            mTvDate = view.findViewById(R.id.tv_date);
            TextView tvCancel = view.findViewById(R.id.tv_cancel);
            tvCancel.setOnClickListener(cancelClick);

            TextView tvConfirm = view.findViewById(R.id.tv_confirm);
            tvConfirm.setOnClickListener(confirmCLick);
            mPickerMonth.setOnWheelChangeListener(this);
            mPickerYear.setOnWheelChangeListener(this);
            mPickerDay.setOnWheelChangeListener(this);
            mPickerHour.setOnWheelChangeListener(this);
            mPickerMin.setOnWheelChangeListener(this);
            mPickerYear.setDataList(mYears);
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
        try {
            if (view == mPickerYear) {
                onYearChanged(item);
            }
            if (view == mPickerMonth) {
                onMonthChanged();
            }
            if (view == mPickerDay) {
                onDayChanged();
            }
            if (view == mPickerHour) {
                onHourChanged();
            }
            String date = String.format("%s-%s-%s %s:%s",
                    mPickerYear.getCurrentData(),
                    mPickerMonth.getCurrentData(),
                    mPickerDay.getCurrentData(),
                    mPickerHour.getCurrentData(),
                    mPickerMin.getCurrentData());
            mTvDate.setText(date);
        } catch (Exception e) {
            CrashReport.postCatchedException(e);
        }
    }

    private void onHourChanged() {
        int currMin = mCurrentCalendar.get(Calendar.MINUTE);
        int currHour = mCurrentCalendar.get(Calendar.HOUR_OF_DAY);
        int currDay = mCurrentCalendar.get(Calendar.DAY_OF_MONTH);




        //如果不是当前系统时间小时
        if (currDay == ParseUtils.parseInt(mPickerDay.getCurrentData())) {//选择的是当天
            if(currHour==ParseUtils.parseInt(mPickerHour.getCurrentData())){//选择是当前时 则需要取整计算
                int destMin= getDestMin(currMin);
                //System.out.println("目标分钟1："+destMin);
                List<String> hours = new ArrayList<>();
                while (destMin<60){
                    hours.add(String.format(Locale.CHINA, "%02d", destMin));
                    destMin+=10;
                }
                mPickerMin.setDataList(hours);
                mPickerMin.setCurrentPosition(0);

            }else {
                if(currHour+1==ParseUtils.parseInt(mPickerHour.getCurrentData())){//判断选择的下一个时间 是不是存在跨时间问题
                    int destMin= getDestMin(currMin);
                    //System.out.println("目标分钟33："+destMin);
                    if (destMin>=60){
                        destMin=destMin-60;
                    }
                    List<String> hours = new ArrayList<>();
                    if(ParseUtils.parseInt(mPickerHour.getCurrentData())==mEndHour){
                        //System.out.println("zzzzzzzz");
                        while (destMin<60&&destMin<=mEndMin){
                            //System.out.println("zzzzzzzzADD");
                            hours.add(String.format(Locale.CHINA, "%02d", destMin));
                            destMin+=10;
                        }
                    }else {
                        while (destMin<60){
                            hours.add(String.format(Locale.CHINA, "%02d", destMin));
                            destMin+=10;
                        }
                    }
                    mPickerMin.setDataList(hours);
                    mPickerMin.setCurrentPosition(0);
                }else {
                    int destMin=0;
                    //System.out.println("目标分钟3："+destMin);
                    List<String> hours = new ArrayList<>();

                    //System.out.println("目标分钟4："+currHour+":"+mEndHour);
                    if(ParseUtils.parseInt(mPickerHour.getCurrentData())==mEndHour){
                        while (destMin<60&&destMin<=mEndMin){
                            hours.add(String.format(Locale.CHINA, "%02d", destMin));
                            destMin+=10;
                        }
                    }else {
                        while (destMin<60){
                            hours.add(String.format(Locale.CHINA, "%02d", destMin));
                            destMin+=10;
                        }
                    }

                    mPickerMin.setDataList(hours);
                    mPickerMin.setCurrentPosition(0);
                }

            }
        } else {
            int destMin=0;
            //System.out.println("目标分钟2："+destMin);
            List<String> hours = new ArrayList<>();
            if(ParseUtils.parseInt(mPickerHour.getCurrentData())==mEndHour){
                //System.out.println("zzzzzzzz");
                while (destMin<60&&destMin<=mEndMin){
                    hours.add(String.format(Locale.CHINA, "%02d", destMin));
                    destMin+=10;
                }
            }else {
                while (destMin<60){
                    hours.add(String.format(Locale.CHINA, "%02d", destMin));
                    destMin+=10;
                }
            }
            mPickerMin.setDataList(hours);
            mPickerMin.setCurrentPosition(0);
        }
    }

    private void onDayChanged() {
        int year = Integer.parseInt(getNumber(mPickerYear.getCurrentData()));
        int month = Integer.parseInt(getNumber(mPickerMonth.getCurrentData()));
        int day = Integer.parseInt(getNumber(mPickerDay.getCurrentData()));

        //System.out.println("营业月份"+month);


        Calendar dayCalendar = Calendar.getInstance();//营业时间
        dayCalendar.set(Calendar.YEAR, year);
        dayCalendar.set(Calendar.MONTH, month -1);//因为比如要设置5月此处不能填写5 要减1
        dayCalendar.set(Calendar.DAY_OF_MONTH, day);
        dayCalendar.set(Calendar.HOUR_OF_DAY, mStartHour);
        dayCalendar.set(Calendar.MINUTE, mStartMin);




        int currYear=mCurrentCalendar.get(Calendar.YEAR);
        int currMonth=mCurrentCalendar.get(Calendar.MONTH)+1;
        int currMin = mCurrentCalendar.get(Calendar.MINUTE);
        int currHour = mCurrentCalendar.get(Calendar.HOUR_OF_DAY);
        int currDay = mCurrentCalendar.get(Calendar.DAY_OF_MONTH);



        int ccurrYear=dayCalendar.get(Calendar.YEAR);
        int ccurrMonth=dayCalendar.get(Calendar.MONTH)+1;
        int ccurrMin = dayCalendar.get(Calendar.MINUTE);
        int ccurrHour = dayCalendar.get(Calendar.HOUR_OF_DAY);
        int ccurrDay = dayCalendar.get(Calendar.DAY_OF_MONTH);


        //System.out.println("当前时间"+currYear+"年"+currMonth+"月"+currDay+"日"+currHour+":"+currMin+"");

        //System.out.println("营业时间"+ccurrYear+"年"+ccurrMonth+"月"+ccurrDay+"日"+ccurrHour+":"+ccurrMin+"");

        List<String> hours = new ArrayList<>();
        if (mCurrentCalendar.getTimeInMillis() > dayCalendar.getTimeInMillis()) {//当前时间大于选择的那天的营业时间
            //System.out.println("时间选择器：当前的时间大于营业");
            int destMin= getDestMin(currMin);
            if(destMin>50){
                for (int i = mCurrentCalendar.get(Calendar.HOUR_OF_DAY)+1; i < mEndHour+1; i++) {
                    hours.add(String.format(Locale.CHINA, "%02d", i));
                }
            }else {
                for (int i = mCurrentCalendar.get(Calendar.HOUR_OF_DAY); i < mEndHour+1; i++) {
                    hours.add(String.format(Locale.CHINA, "%02d", i));
                }
            }

        } else {

            //System.out.println("时间选择器：当前的时间小于营业时间");//说明选了以后的天
            for (int i = mStartHour; i < mEndHour+1; i++) {
                hours.add(String.format(Locale.CHINA, "%02d", i));
            }
        }
        mPickerHour.setDataList(hours);
        mPickerHour.setCurrentPosition(0);
    }

    public static int getDestMin(int currMin) {
        //System.out.println("目标推算分钟"+currMin);
        int result= (currMin + 30 + 10) / 10 * 10;
        //System.out.println("目标结果分钟"+result);
        return result;
    }

    private void onMonthChanged() {
        int year = Integer.parseInt(getNumber(mPickerYear.getCurrentData()));
        int month = Integer.parseInt(getNumber(mPickerMonth.getCurrentData()));


        List<String> days = new ArrayList<>();
        int totalDays = getDays(year, month);//当月天数
        if (year == mMinCalendar.get(Calendar.YEAR) && month == (mMinCalendar.get(Calendar.MONTH) + 1)) {//选的年等于最小年 选的月是最小可选月
            int day = mMinCalendar.get(Calendar.DAY_OF_MONTH);//计算出当前天数

            int mini = mCurrentCalendar.get(Calendar.MINUTE);//计算出当前天数

            Calendar dayCalendarCurrZeroSec = Calendar.getInstance();//营业时间
            dayCalendarCurrZeroSec.setTime(mCurrentCalendar.getTime());
            dayCalendarCurrZeroSec.set(Calendar.SECOND, 0);


            Calendar dayCalendarEnd = Calendar.getInstance();//营业时间
            dayCalendarEnd.set(Calendar.YEAR, year);
            dayCalendarEnd.set(Calendar.MONTH, month -1);//因为比如要设置5月此处不能填写5 要减1
            dayCalendarEnd.set(Calendar.DAY_OF_MONTH, day);
            dayCalendarEnd.set(Calendar.HOUR_OF_DAY, mEndHour);
            dayCalendarEnd.set(Calendar.MINUTE, mEndMin);
            dayCalendarEnd.set(Calendar.SECOND, 0);


            //System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dayCalendarCurrZeroSec.getTime()));
            //System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dayCalendarEnd.getTime()));
            if(day+mLimitDay>=totalDays){
                if(dayCalendarCurrZeroSec.getTimeInMillis()+((getDestMin(mini)-mini)*60*1000)>=dayCalendarEnd.getTimeInMillis()){//当前时间+30+10*10已经过了营业时间末尾
                    day=day+1;
                }
                for (int i = day; i <= totalDays; i++) {
                    days.add(String.format(Locale.CHINA, "%02d", i));
                }
            }else {
                if(dayCalendarCurrZeroSec.getTimeInMillis()+((getDestMin(mini)-mini)*60*1000)>=dayCalendarEnd.getTimeInMillis()){//当前时间已经过了营业时间末尾
                    day=day+1;
                }
                for (int i = day; i <= day+mLimitDay; i++) {
                    days.add(String.format(Locale.CHINA, "%02d", i));
                }
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
        String preData = "";
        if (mFlagDay) {//默认选择今天
            int m = mCurrentCalendar.get(Calendar.DAY_OF_MONTH);
            preData = String.format(Locale.CHINA, "%02d", m);
            mFlagDay = false;
        } else {
            if (mPickerDay.getDataList() != null) {
                preData = mPickerDay.getCurrentData();
            }
        }
        mPickerDay.setDataList(days);
        int index = days.indexOf(preData);
        index = Math.max(0, index);
        mPickerDay.setCurrentPosition(index);
    }

    private void onYearChanged(String item) {
        //获取当前选择的年份
        int year = Integer.parseInt(getNumber(item));
        int minYear = mMinCalendar.get(Calendar.YEAR);
        int maxYear = mMaxCalendar.get(Calendar.YEAR);

        Calendar dayCalendarCurrZeroSec = Calendar.getInstance();//营业时间
        dayCalendarCurrZeroSec.setTime(mCurrentCalendar.getTime());
        dayCalendarCurrZeroSec.set(Calendar.SECOND, 0);


        Calendar dayCalendarEnd = Calendar.getInstance();//营业时间
        dayCalendarEnd.setTime(mCurrentCalendar.getTime());
        dayCalendarEnd.set(Calendar.HOUR_OF_DAY, mEndHour);
        dayCalendarEnd.set(Calendar.MINUTE, mEndMin);
        dayCalendarEnd.set(Calendar.SECOND, 0);


        int mini = mCurrentCalendar.get(Calendar.MINUTE);//计算出当前分钟


        //同一年份
        List<String> monthList = new ArrayList<>();
        if (minYear == maxYear) {
            monthList.clear();
            int monthMin = mMinCalendar.get(Calendar.MONTH) + 1;
            int monthMax = mMaxCalendar.get(Calendar.MONTH) + 1;
            if(isLastDayOfMonth(mCurrentCalendar.getTime())&&dayCalendarCurrZeroSec.getTimeInMillis()+((getDestMin(mini)-mini)*60*1000)>=dayCalendarEnd.getTimeInMillis()){
                //加判是不是当前是不是月底需要判断是不是存在跨月问题
                monthMin=monthMin+1;
            }
            for (int i = monthMin; i <= monthMax; i++) {
                monthList.add(String.format(Locale.CHINA, "%02d", i));
            }
        } else if (year == minYear) {
            monthList.clear();
            int monthMin = mMinCalendar.get(Calendar.MONTH) + 1;
            if(isLastDayOfMonth(mCurrentCalendar.getTime())&&dayCalendarCurrZeroSec.getTimeInMillis()+((getDestMin(mini)-mini)*60*1000)>=dayCalendarEnd.getTimeInMillis()){
                //加判是不是当前是不是月底需要判断是不是存在跨月问题
                monthMin=monthMin+1;
            }
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
        mPickerMonth.setDataList(monthList);
        mPickerMonth.setCurrentPosition(0);
    }
    public static boolean isLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            return true;
        }
        return false;
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

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}