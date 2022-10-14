package com.health.index.model;

import java.io.Serializable;
import java.util.List;

public class IndexMonTool implements Serializable {


    public int id=-1;
    public String categoryName;
    public int knowOrInfoStatus;
    public int stage;

    public IndexMonTool(String categoryName) {
        this.categoryName = categoryName;
    }

    public IndexMonTool(String categoryName, int stage) {
        this.categoryName = categoryName;
        this.stage = stage;
    }
}
