package com.healthy.library.base;


import com.healthy.library.loadmore.LoadMoreAdapter;

/**
 * 分页
 */
@Deprecated
public interface IPaging extends LoadMoreAdapter.OnLoadMoreListener {


    /**
     * 加载更多数据需要设置总页数
     *
     * @param pages
     */
    void setPages(int pages);

    /**
     * 获取最新数据需要重置
     */
    void resetPageNum();

    /**
     * 设置下一页
     */
    void setNextPageNum();

    /**
     * 获取当前页
     *
     * @return
     */
    int getCurrentPageNum();

    /**
     * 加载更多数据
     */
    void onLoadMore();

    /**
     * 没有更多的数据
     */
    void noMoreData();

    /**
     * h获取每页加载数量
     */
    int getPageSize();

    void setPageSize(int pageSize);

    LoadMoreAdapter getLoadMoreAdapter();
}
