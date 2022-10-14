package com.healthy.library.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ToolsGrow {

                public int recordId;

                public String recordDate;

                public double height;

                public double weight;

                public String deliveryDate;

                public String timeOfBirthDesc;

                public int monthOfAge;

                public long getrecordDateTime(){
                    try {
                        return new SimpleDateFormat("yyyy-MM-dd").parse(recordDate).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }


}
