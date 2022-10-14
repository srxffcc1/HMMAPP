package com.healthy.library.model;

/**
 * @author Li
 * @date 2019/03/06 09:43
 * @des 下单时 选择服务
 */

public class ServiceItemModel {
    private String name;

    public ServiceItemModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
