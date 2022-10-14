package com.healthy.library.builder;

import java.util.Calendar;
import java.util.Date;

public class SimpleCalendarBuilder  {
    Calendar calendar;
    public SimpleCalendarBuilder() {
        calendar = Calendar.getInstance();
    }

    public SimpleCalendarBuilder add(int field, int amount){
        calendar.add(field,amount);
        return this;
    }
    public SimpleCalendarBuilder setTime(Date date){
        calendar.setTime(date);
        return this;
    }
    public Calendar build(){
        return calendar;
    }
}
