package com.healthy.library.base;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.healthy.library.R;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.healthy.library.widget.StatusLayout.Status.STATUS_EMPTY;
@Deprecated
public abstract class BaseRefreshLoadActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener,BaseQuickAdapter.OnItemClickListener {
    private RecyclerView rv;
    public BaseQuickAdapter baseAdapter;
    private SwipeRefreshLayout mLayoutRefresh;
    private int PAGE_SIZE = 10;
    private int PAGE = 1;
    private Map map = new HashMap();
    private StatusLayout sl_error;
    private TopBar topBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_refresh_load;
    }

    @Override
    protected void findViews() {
        map.put("page", PAGE);
        map.put("page_size", PAGE_SIZE);
        rv = findViewById(R.id.rv);
        mLayoutRefresh = findViewById(R.id.srl_refresh);
        sl_error = findViewById(R.id.sl_error);
        topBar = findViewById(R.id.top_bar);
        topBar.setTitle(getTopBarText());
        baseAdapter = getAdapter();
        setStatusLayout(sl_error);
        rv.setAdapter(baseAdapter);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        baseAdapter.bindToRecyclerView(rv);
        mLayoutRefresh.setOnRefreshListener(this);
        baseAdapter.setOnItemClickListener(this);
        if (isLoadMore()) {
            baseAdapter.setOnLoadMoreListener(this, rv);
        }
        onRefresh();

    }

    public abstract BaseQuickAdapter getAdapter();

    public abstract boolean isLoadMore();

    public abstract String getTopBarText();

    @Override
    protected void init(Bundle savedInstanceState) {

    }
    /*
     *
     * 刷新*/

    @Override
    public void onRefresh() {
        if (isLoadMore()) {
            PAGE = 1;
            map.put("page", PAGE);
        }

        getRvData(true);
    }

    /*
     * 刷新成功
     * */
    public void refreshSuccess(List data) {
        mLayoutRefresh.setRefreshing(false);
        setData(true, data);
    }

    /*
     * 刷新失败
     * */
    public void refreshFail(StatusLayout.Status status) {
        mLayoutRefresh.setRefreshing(false);
        sl_error.updateStatus(status);
    }

    /*
     * 加载更多
     * */

    @Override
    public void onLoadMoreRequested() {
        map.put("page", PAGE);
        getRvData(false);

    }

    /*
     * 加载成功
     * */
    public void loadSuccess(List data) {
        boolean isRefresh = PAGE == 1;
        setData(isRefresh, data);
    }

    /*
     * 加载失败
     * */
    public void loadFail() {
        baseAdapter.loadMoreEnd();
    }

    protected abstract void getRvData(boolean isRefresh);


    private void setData(boolean isRefresh, List data) {
        if (isLoadMore()) {
            PAGE++;
            final int size = data == null ? 0 : data.size();
            if (isRefresh) {
                if (data.size() == 0) {
                    sl_error.updateStatus(STATUS_EMPTY);
                    return;
                }
                baseAdapter.setNewData(data);
            } else {
                if (size > 0) {
                    baseAdapter.addData(data);
                }
            }
            if (size < PAGE_SIZE) {
                //第一页如果不够一页就不显示没有更多数据布局
                baseAdapter.loadMoreEnd(true);
            } else {
                baseAdapter.loadMoreEnd(false);
            }
        } else {
            if (data.size() == 0) {
                sl_error.updateStatus(STATUS_EMPTY);
                return;
            }
            baseAdapter.setNewData(data);
        }

    }
}
