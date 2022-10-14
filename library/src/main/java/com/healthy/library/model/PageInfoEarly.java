package com.healthy.library.model;

import java.io.Serializable;

public class PageInfoEarly implements Serializable {

    public int isMore=0;
    public int totalPage;
    public int currentPage=1;
    public int totalNum=0;
    public int nextPage=0;
    public int pageNum=0;
    public int total=0;

    @Override
    public String toString() {
        return "PageInfoEarly{" +
                "isMore=" + isMore +
                ", totalPage=" + totalPage +
                ", currentPage=" + currentPage +
                ", totalNum=" + totalNum +
                '}';
    }
}
