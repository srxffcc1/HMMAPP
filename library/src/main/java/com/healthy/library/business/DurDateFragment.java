package com.healthy.library.business;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.healthy.library.R;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.interfaces.OnDateDurConfirmListener;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.widget.WheelPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/19 15:47
 * @des 时间选择器 年月日时分
 */

public class DurDateFragment extends BaseFragment implements WheelPicker.OnWheelChangeListener<String> {
    private TextView tvHour;
    private TextView tvMin;
    private WheelPicker<String> pickerHour;
    private WheelPicker<String> pickerMin;
    private String init;
    boolean isNeedInit=false;
    boolean isNeedOut=true;
    OnDateDurConfirmListener mOnConfirmClick;
    public DurDateFragment setOnConfirmClick(OnDateDurConfirmListener onConfirmClick) {
        mOnConfirmClick = onConfirmClick;
        return this;

    }
    public static DurDateFragment newInstance(Map<String, Object> maporg) {
        DurDateFragment fragment = new DurDateFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_feed_dur_wheel;
    }

    @Override
    protected void findViews() {
        initView();
        pickerHour.setOnWheelChangeListener(this);
        pickerMin.setOnWheelChangeListener(this);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        initHour();
    }

    private void initHour() {
        try {
            init=get("init");
        } catch (Exception e) {
            init="";
            e.printStackTrace();
        }
        if(!TextUtils.isEmpty(init)){
            isNeedInit=true;
        }
        onDayChanged();
    }

    /**
     *
     * @param begin 2020-05-01 必须2位
     * @param end 2020-05-01 必须2位
     * @return
     */
    public  List<String> getBetweenDate(String begin, String end){
        SimpleDateFormat format = new SimpleDateFormat(get("format"));
        List<String> betweenList = new ArrayList<String>();
        try{
            Calendar startDay = Calendar.getInstance();
            startDay.setTime(format.parse(begin));
            startDay.add(Calendar.DATE, -1);
            boolean flag=true;
            while(flag){
                startDay.add(Calendar.DATE, 1);
                Date newDate = startDay.getTime();
                String newend=format.format(newDate);
//                //System.out.println("日期进来");
//                //System.out.println("日期进来:"+newend);
                if(newend.equals(format.format(new Date()))){//说明是今日
                    //System.out.println("日期今天");
                    betweenList.add("今天");
                }else {
//                    //System.out.println("日期添加");
                    betweenList.add(newend);
                }
                if(end.equals(newend)){
                    flag=false;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return betweenList;
    }

    /**
     * sec是秒
     * @param sec
     */
    public void reset(long sec){
        isNeedOut=false;
        sec=sec*1000;
        try {
            pickerHour.setCurrentPosition((int)DateUtils.getDistanceTimeOnlySecondStringArray(sec)[1]);
        } catch (Exception e) {
            pickerHour.setCurrentPosition(0);
            e.printStackTrace();
        }
        try {
            pickerMin.setCurrentPosition((int)DateUtils.getDistanceTimeOnlySecondStringArray(sec)[2]);
        } catch (Exception e) {
            pickerMin.setCurrentPosition(0);
            e.printStackTrace();
        }

        isNeedOut=true;
    }

    public void reset(int hour,int min){
        isNeedOut=false;
        pickerHour.setCurrentPosition(hour-1);
        pickerMin.setCurrentPosition(min-1);
        isNeedOut=true;
    }
    public void reset(){
        isNeedOut=false;
        pickerHour.setCurrentPosition(0);
        pickerMin.setCurrentPosition(30);
        isNeedOut=true;
    }
    @Override
    public void onWheelSelected(View view, String item, int position) {
        if (view == pickerHour) {
            onHourChanged();
        }
        if (view == pickerMin) {
        }
        if(isNeedOut){
            if(mOnConfirmClick!=null){
                if(view.isPressed()){
                    mOnConfirmClick.onConfirm(0,pickerHour.getCurrentData()+":"+pickerMin.getCurrentData());
                }
            }
        }

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
    private void onDayChanged() {
        List<String> hours = new ArrayList<>();
        for (int i = 0; i <4 ; i++) {
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
    }

    private void initView() {
        tvHour = (TextView) findViewById(R.id.tv_hour);
        tvMin = (TextView) findViewById(R.id.tv_min);
        pickerHour = (WheelPicker) findViewById(R.id.picker_hour);
        pickerMin = (WheelPicker) findViewById(R.id.picker_min);
    }
}