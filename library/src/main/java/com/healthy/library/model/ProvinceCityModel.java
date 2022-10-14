package com.healthy.library.model;

/**
 * @author Li
 * @date 2019/04/11 09:18
 * @des 省市
 */

public class ProvinceCityModel {
    private String name;
    private String id;
    private String areaNo;

    public ProvinceCityModel() {
    }

    public ProvinceCityModel(String name, String id, String areaNo) {
        this.name = name;
        this.id = id;
        this.areaNo = areaNo;
    }

    public String getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(String areaNo) {
        this.areaNo = areaNo;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
