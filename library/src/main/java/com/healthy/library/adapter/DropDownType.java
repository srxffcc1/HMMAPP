package com.healthy.library.adapter;

import java.io.Serializable;

/**
 * @author: luoyang
 * @description:
 */
public class DropDownType implements Serializable {
    String id;
    String name;

    public DropDownType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
