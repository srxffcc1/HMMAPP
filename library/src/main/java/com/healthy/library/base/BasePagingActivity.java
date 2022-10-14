//package com.healthy.library.base;
//
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.healthy.library.loadmore.LoadMoreAdapter;
//
//
///**
// * 用于分页的Activity
// */
//
//public abstract class BasePagingActivity extends AppCompatActivity implements IPaging {
//
//    private PagingUtil pagingUtil;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        pagingUtil = new PagingUtil();
//    }
//
//    /**
//     * 加载更多数据需要设置总页数
//     *
//     * @param pages
//     */
//    @Override
//    public void setPages(int pages) {
//        pagingUtil.setPages(pages);
//    }
//
//    /**
//     * 获取最新数据需要重置
//     */
//    @Override
//    public void resetPageNum() {
//        pagingUtil.resetPageNum();
//    }
//
//    /**
//     * 设置下一页
//     */
//    @Override
//    public void setNextPageNum() {
//        pagingUtil.setNextPageNum();
//    }
//
//    /**
//     * 获取当前页
//     *
//     * @return
//     */
//    @Override
//    public int getCurrentPageNum() {
//        return pagingUtil.getCurrentPageNum();
//    }
//
//    @Override
//    public void noMoreData() {
//        if (getLoadMoreAdapter() != null) {
//            getLoadMoreAdapter().setLoadMoreEnabled(false);
//            if (!getLoadMoreAdapter().getShowNoMoreEnabled()) {
//                getLoadMoreAdapter().setShowNoMoreEnabled(true);
//            }
//            getLoadMoreAdapter().getOriginalAdapter().notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void onLoadMore(LoadMoreAdapter.Enabled enabled) {
//        if (enabled.getLoadMoreEnabled()) {
//            if (pagingUtil.getPages() >= getCurrentPageNum()) {
//                onLoadMore();
//            } else {
//                noMoreData();
//            }
//        } else {
//            if (getLoadMoreAdapter() != null) {
//                getLoadMoreAdapter().getOriginalAdapter().notifyDataSetChanged();
//            }
//        }
//    }
//}
