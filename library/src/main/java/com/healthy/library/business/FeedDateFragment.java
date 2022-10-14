package com.healthy.library.business;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.healthy.library.R;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.interfaces.OnDateConfirmListener;
import com.healthy.library.widget.WheelPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Li
 * @date 2019/03/19 15:47
 * @des 时间选择器 年月日时分
 */

public class FeedDateFragment extends BaseFragment implements WheelPicker.OnWheelChangeListener<String>{
    private TextView tvDay;
    private TextView tvHour;
    private TextView tvMin;
    private WheelPicker<String> pickerDay;
    private WheelPicker<String> pickerHour;
    private WheelPicker<String> pickerMin;
    private String init;
    boolean isNeedInit=false;
    private OnDateConfirmListener mOnConfirmClick;
    private Calendar nowCal;

    public static FeedDateFragment newInstance(Map<String, Object> maporg) {
        FeedDateFragment fragment = new FeedDateFragment();
        Bundle args = new Bundle();
        BaseFragment.bundleMap(maporg, args);
        fragment.setArguments(args);
        return fragment;
    }

    public void reset(){

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_feed_date_wheel;
    }

    @Override
    protected void findViews() {
        initView();
        pickerDay.setOnWheelChangeListener(this);
        pickerHour.setOnWheelChangeListener(this);
        pickerMin.setOnWheelChangeListener(this);
        nowCal = Calendar.getInstance();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        initDay();
    }
    public FeedDateFragment setOnConfirmClick(OnDateConfirmListener onConfirmClick) {
        mOnConfirmClick = onConfirmClick;
        return this;

    }

    private void initDay() {
        try {
            init=get("init");
        } catch (Exception e) {
            init="";
            e.printStackTrace();
        }
        if(!TextUtils.isEmpty(init)){
            isNeedInit=true;
            //System.out.println(init+"默认");
        }

        List<String> yearmonday=getBetweenDate(get("beginTime"),get("endTime"));

        pickerDay.setDataList(yearmonday);

        if(isNeedInit){
            //System.out.println("开始初始化");
            for (int i = 0; i <yearmonday.size() ; i++) {
                //System.out.println("开始初始化"+init);
                if((init.split(" ")[0].trim()).equals(yearmonday.get(i))){//说明相等 那就选中
                    pickerDay.setCurrentPosition(i);
                    break;
                }
                if("今天".equals(yearmonday.get(i))&&new SimpleDateFormat(get("format")).format(new Date()).equals((init.split(" ")[0].trim()))){//说明是今天
                    pickerDay.setCurrentPosition(i);
                    break;
                }
            }
        }else {
            pickerDay.setCurrentPosition(0);
        }

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

    @Override
    public void onWheelSelected(View view, String item, int position) {
        if (view == pickerDay) {
            onDayChanged();
        }
        if (view == pickerHour) {
            onHourChanged();
        }
        if (view == pickerMin) {

        }
        if(mOnConfirmClick!=null){
            String day="0";
            String hour="0";
            String min="0";
            if("今天".equals(pickerDay.getCurrentData())){
                day=new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            }else {
                day=pickerDay.getCurrentData();
            }
            try {
                hour=pickerHour.getCurrentData();
                min=pickerMin.getCurrentData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mOnConfirmClick.onConfirm(0,new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(day+" "+hour+":"+min));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private String getNumber(String s) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        return m.replaceAll("").trim();
    }
    private void onHourChanged() {
        int destMin=0;
        List<String> mins = new ArrayList<>();
        if("今天".equals(pickerDay.getCurrentData())&&Integer.parseInt(getNumber(pickerHour.getCurrentData()))==nowCal.get(Calendar.HOUR_OF_DAY)){
            while (destMin<nowCal.get(Calendar.MINUTE)+1){
                mins.add(String.format(Locale.CHINA, "%02d", destMin));
                destMin+=1;
            }
        }else {
            while (destMin<60){
                mins.add(String.format(Locale.CHINA, "%02d", destMin));
                destMin+=1;
            }
        }

        pickerMin.setDataList(mins);
        if(isNeedInit){
            //System.out.println("开始初始化分"+init);
            for (int i = 0; i <mins.size() ; i++) {
                if(((init.split(" ")[1].trim()).split(":")[1].trim()).equals(mins.get(i))){
                    pickerMin.setCurrentPosition(i);
                    break;
                }
            }
        }else {
            pickerMin.setCurrentPosition(0);
        }
    }
    private void onDayChanged() {
        List<String> hours = new ArrayList<>();
        if("今天".equals(pickerDay.getCurrentData())){

            for (int i = 0; i <nowCal.get(Calendar.HOUR_OF_DAY)+1 ; i++) {
                hours.add(String.format("%02d", i)+"");
            }
        }else {
            for (int i = 0; i <24 ; i++) {
                hours.add(String.format("%02d", i)+"");
            }
        }

        pickerHour.setDataList(hours);
        if(isNeedInit){
            //System.out.println("开始初始化时"+init);
            for (int i = 0; i <hours.size() ; i++) {
                if(((init.split(" ")[1].trim()).split(":")[0].trim()).equals(hours.get(i))){
                    pickerHour.setCurrentPosition(i);
                    break;
                }
            }
        }else {
            pickerHour.setCurrentPosition(0);
        }
        isNeedInit=false;
    }

    private void initView() {
        tvDay = (TextView) findViewById(R.id.tv_day);
        tvHour = (TextView) findViewById(R.id.tv_hour);
        tvMin = (TextView) findViewById(R.id.tv_min);
        pickerDay = (WheelPicker) findViewById(R.id.picker_day);
        pickerHour = (WheelPicker) findViewById(R.id.picker_hour);
        pickerMin = (WheelPicker) findViewById(R.id.picker_min);
    }
}