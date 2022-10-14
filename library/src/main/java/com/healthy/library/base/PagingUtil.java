package com.healthy.library.base;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分页工具类
 */

public class PagingUtil {

    private int pages;
    public int PAGE_SIZE = 10;
    /**
     * 获取总页数
     * @return
     */
    public int getPages() {
        return pages;
    }

    /**
     * 加载更多数据需要设置总页数
     * @param pages
     */
    public void setPages(int pages) {
        this.pages = pages;
    }

    private AtomicInteger mPageNum = new AtomicInteger(1);

    /**
     * 获取最新数据需要重置
     */
    public void resetPageNum() {
        mPageNum.set(1);
    }

    /**
     * 设置下一页
     */
    public void setNextPageNum() {
        mPageNum.incrementAndGet();
    }

    /**
     * 获取当前页
     * @return
     */
    public int getCurrentPageNum() {
        return mPageNum.get();
    }
    /**
     * 设置每页加载数量
     * @return
     */
    public void setPageSize(int pagesize) {
       PAGE_SIZE=pagesize;
    }
    /**
     * 设置每页加载数量
     * @return
     */
    public int getPageSize() {
        return PAGE_SIZE;
    }
}
