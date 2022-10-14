package com.healthy.library.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ToolsSumGrow {

    
        public float amount;

        public String dayOfMonth;

        public String dateDesc;

        public long getrecordDateTime(){
                try {
                        return new SimpleDateFormat("yyyy-MM-dd").parse(dateDesc).getTime();
                } catch (ParseException e) {
                        e.printStackTrace();
                }
                return 0;
        }

}
