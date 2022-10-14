package com.health.servicecenter.fragment;

import android.os.Bundle;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.servicecenter.R;
import com.health.servicecenter.adapter.OrderBackListAdapter;
import com.health.servicecenter.contract.OrderBackListFragmentContract;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.BackListModel;
import com.healthy.library.model.OrderListPageInfo;
import com.health.servicecenter.presenter.OrderBackListFragmentPresenter;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.StatusLayout;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderBackListFragment extends BaseFragment implements OnRefreshLoadMoreListener, OrderBackListFragmentContract.View, BaseAdapter.OnOutClickListener {

    private RecyclerView mRecyclerOrder;
    private String mType;
    private OrderBackListAdapter mOrderAdapter;
    private StatusLayout mStatusLayout;
    private SmartRefreshLayout mRefreshLayout;
    private int pageNum = 1;
    private OrderBackListFragmentPresenter presenter;

    public OrderBackListFragment() {
        // Required empty public constructor
    }

    public static OrderBackListFragment newInstance(String type) {
        OrderBackListFragment fragment = new OrderBackListFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString("type");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_back_list;
    }

    @Override
    protected void findViews() {
        mRecyclerOrder = findViewById(R.id.recycler_order);
        mRefreshLayout = findViewById(R.id.layout_refresh);
        mRefreshLayout.finishRefreshWithNoMoreData();
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        presenter = new OrderBackListFragmentPresenter(getActivity(), this);
        mRecyclerOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderAdapter = new OrderBackListAdapter();
        mRecyclerOrder.setAdapter(mOrderAdapter);
        mOrderAdapter.setOutClickListener(this);
    }

    @Override
    public void getData() {
        super.getData();
        Map<String, Object> map = new HashMap<>();
        if ("0".equals(mType)) {
            map.put("disposedStatus", new String[]{"0"});
            map.put("canceledStatus", new String[]{"0"});
            map.put("status", new String[]{"0"});
        }
        map.put("deletedStatus", new String[]{"0"});
        map.put("page", pageNum + "");
        map.put("pageSize", "10");
        map.put("memberId", new String(Base64.decode(SpUtils.getValue(mContext, SpKey.USER_ID).getBytes(), Base64.DEFAULT)));
        presenter.getBackList(map);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNum++;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        getData();

    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void onGetBackListSuccess(List<BackListModel> list, OrderListPageInfo pageInfo) {
        showContent();
        if (pageNum == 1) {
            mOrderAdapter.setData((ArrayList) list);
            if (list.size() == 0) {
                showEmpty();
            }
        } else {
            mOrderAdapter.addDatas((ArrayList) list);
        }
        if (pageInfo.getNextPage() == 0) {
            ////System.out.println("没有更多了");
            mRefreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            mRefreshLayout.setNoMoreData(false);
            mRefreshLayout.setEnableLoadMore(true);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mContext != null) {
                getData();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onRevokeOrderSuccess(String result) {
        showToast(result);
        getData();
    }

    @Override
    public void onDeleteOrderSuccess(String result) {
        showToast(result);
        getData();
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("revoke".equals(function)) {
            isAgree(1, "确定撤销售后申请吗？撤销后需要重新发起退款申请", (String) obj, "暂不撤销", "确认撤销");
        }
        if ("delete".equals(function)) {
            isAgree(2, "确定删除售后单，删除之后无法恢复", (String) obj, "取消", "确定");
        }
    }

    private void isAgree(final int type, String content, final String id, String cancle, String agree) {
        StyledDialog.init(getActivity());
        StyledDialog.buildIosAlert("提示", "\n" + content, new MyDialogListener() {
            @Override
            public void onFirst() {
            }

            @Override
            public void onThird() {
                super.onThird();
            }

            @Override
            public void onSecond() {
                if (type == 1) {
                    presenter.revokeOrder(id);
                } else if (type == 2) {
                    presenter.deleteOrder(id);
                }
            }
        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText(cancle, agree).show();
    }
}