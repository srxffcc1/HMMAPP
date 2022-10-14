package com.health.index.model;

/**
 * @author Li
 * @date 2019/04/24 15:33
 * @des 产检项
 */
public class BirthCheckModel {
    private int id;
    private String title;
    private String date;
    private String keys;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date == null ? "" : date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKeys() {
        return keys == null ? "" : keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }
}
