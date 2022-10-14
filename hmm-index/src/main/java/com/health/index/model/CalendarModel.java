package com.health.index.model;

import java.util.Date;

/**
 * @author Li
 * @date 2019/05/08 16:19
 * @des 经期日历
 */
public class CalendarModel {

    /**
     * 月经第一天
     */
    private Date menstruationStartDate;

    /**
     * 月经最后一天
     */
    private Date menstruationEndDate;

    /**
     * 排卵日
     */
    private Date ovulationDate;
    /**
     * 排卵期第一天
     */
    private Date ovulationStartDate;
    /**
     * 排卵期最后一天
     */
    private Date ovulationEndDate;

    public Date getMenstruationStartDate() {
        return menstruationStartDate;
    }

    public void setMenstruationStartDate(Date menstruationStartDate) {
        this.menstruationStartDate = menstruationStartDate;
    }

    public Date getMenstruationEndDate() {
        return menstruationEndDate;
    }

    public void setMenstruationEndDate(Date menstruationEndDate) {
        this.menstruationEndDate = menstruationEndDate;
    }

    public Date getOvulationDate() {
        return ovulationDate;
    }

    public void setOvulationDate(Date ovulationDate) {
        this.ovulationDate = ovulationDate;
    }

    public Date getOvulationStartDate() {
        return ovulationStartDate;
    }

    public void setOvulationStartDate(Date ovulationStartDate) {
        this.ovulationStartDate = ovulationStartDate;
    }

    public Date getOvulationEndDate() {
        return ovulationEndDate;
    }

    public void setOvulationEndDate(Date ovulationEndDate) {
        this.ovulationEndDate = ovulationEndDate;
    }

    public boolean isEmpty() {
        return menstruationStartDate == null && menstruationEndDate == null
                && ovulationStartDate == null && ovulationEndDate == null
                && ovulationDate == null;
    }
}