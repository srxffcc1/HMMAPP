package com.health.faq.model;

/**
 * @author Li
 * @date 2019/07/09 16:22
 * @des 回复被采纳
 */

public class ReplyAdoptedModel {
    private int type;
    private String income;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIncome() {
        return income == null ? "" : income;
    }

    public void setIncome(String income) {
        this.income = income;
    }
}
