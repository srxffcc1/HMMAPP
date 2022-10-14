package com.health.index.model;

/**
 * @author Li
 * @date 2019/04/25 15:25
 * @des B超项
 */

public class AnalyzeModel {
    private String id;
    private String name;
    private String value;

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value == null ? "" : value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
