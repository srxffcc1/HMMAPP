package com.healthy.library.model;

import com.healthy.library.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GoodsPickDay {
    public String day;
    public String showSingleday;
    public String showday;
    public GoodsPickDay(String day) {
        this.day = day;
    }

    public String getDayShow() {
        try {
            Date date=new SimpleDateFormat("yyyy-MM-dd").parse(day);

            showSingleday= new SimpleDateFormat("M月d日").format(date);
            int diff= DateUtils.getDayDiffer(new Date(),date);
            if(diff==0){
                showday=showSingleday+"(今天)";
            }else if(diff==1){

                showday=showSingleday+"(明天)";
            }else {

                showday=showSingleday;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return showday;
    }
}
