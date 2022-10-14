package com.healthy.library.message;

public class IndexSearchInfo {
    public String searchStr;
    public int type;//1搜索2跳转

    public IndexSearchInfo(String searchStr,int type) {
        this.searchStr = searchStr;
        this.type = type;
    }
}
