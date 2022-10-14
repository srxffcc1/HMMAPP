package com.health.index.model;

import java.util.Date;

/**
 * @author Li
 * @date 2019/05/10 10:21
 * @des
 */

public class CalendarTypeModel {
    private int type;
    private Date date;
    private CalendarSupModel supModel;

    public CalendarTypeModel() {
    }

    public CalendarTypeModel(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CalendarSupModel getSupModel() {
        return supModel;
    }

    public void setSupModel(CalendarSupModel supModel) {
        this.supModel = supModel;
    }
}
