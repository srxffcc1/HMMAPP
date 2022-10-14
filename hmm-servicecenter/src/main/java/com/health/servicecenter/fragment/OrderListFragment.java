package com.health.servicecenter.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.servicecenter.R;
import com.health.servicecenter.adapter.OrderListAdapter;
import com.health.servicecenter.contract.OrderListFragmentContract;
import com.health.servicecenter.contract.OrderListFragmentGoodsContract;
import com.health.servicecenter.model.ConfirmOderModel;
import com.health.servicecenter.presenter.OrderListFragmentGoodsPresenter;
import com.health.servicecenter.widget.OrderBackErrorDialog;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.model.OrderList;
import com.healthy.library.model.OrderListPageInfo;
import com.health.servicecenter.presenter.OrderListFragmentPresenter;
import com.healthy.library.base.BaseFragment;
import com.healthy.library.base.BaseMultiItemAdapter;
import com.healthy.library.constant.Functions;
import com.healthy.library.message.UpdateOrderNum;
import com.healthy.library.widget.StatusLayout;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderListFragment extends BaseFragment implements OnRefreshLoadMoreListener
        , OrderListFragmentContract.View,  BaseAdapter.OnOutClickListener, OrderListFragmentGoodsContract.View {
    private RecyclerView mRecyclerOrder;
    private String listType;
    private OrderListAdapter mOrderAdapter;
    private StatusLayout mStatusLayout;
    private SmartRefreshLayout mRefreshLayout;
    private int pageNum = 1;
    private OrderListFragmentPresenter orderListPresenter;
    private Map<String, Object> map = new HashMap<>();

    public OrderListFragment() {
    }

    public static OrderListFragment newInstance(String status) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putString("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        if (getArguments() != null) {
            listType = getArguments().getString("status");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_order_list;
    }

    @Override
    protected void findViews() {
        mRecyclerOrder = findViewById(R.id.recycler_order);
        mStatusLayout = findViewById(R.id.layout_status);
        mRefreshLayout = findViewById(R.id.layout_refresh);
        mRefreshLayout.finishRefreshWithNoMoreData();
        mRefreshLayout.setOnRefreshLoadMoreListener(this);
        orderListPresenter = new OrderListFragmentPresenter(getActivity(), this);
        mRecyclerOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderAdapter = new OrderListAdapter();
        mRecyclerOrder.setAdapter(mOrderAdapter);
        mOrderAdapter.setOutClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        if ("0".equals(listType)) {
            map.put("listType", "0");
        } else if ("1".equals(listType)) {
            map.put("listType", "1");
        } else if ("2".equals(listType)) {
            map.put("listType", "2");
        } else if ("3".equals(listType)) {
            map.put("listType", "3");
        }
        map.put("page", pageNum + "");
        map.put("pageSize", 5 + "");
        orderListPresenter.getOrderList(map);
    }

    @Override
    public void onGetOrderListSuccess(List<OrderList> list, OrderListPageInfo pageInfo) {
        showContent();
        EventBus.getDefault().post(new UpdateOrderNum(1));
        if (pageNum == 1) {
            if (list.size() == 0) {
                showEmpty();
            } else {
                mOrderAdapter.setData((ArrayList) list);
                for (int i = 0; i < list.size(); i++) {
                    OrderList orderList = list.get(i);
                    OrderListFragmentGoodsPresenter presenter = new OrderListFragmentGoodsPresenter(mContext, this);
                    if (orderList.isMain == 0) {
                        presenter.getOrderListGoods(new SimpleHashMapBuilder<String, Object>()
                                .puts(Functions.FUNCTION, "25007")
                                .puts("subOrderId", orderList.orderId), orderList);
                    } else if (orderList.isMain == 1) {
                        presenter.getOrderListGoods(new SimpleHashMapBuilder<String, Object>()
                                .puts(Functions.FUNCTION, "25006")
                                .puts("mainOrderId", orderList.orderId), orderList);
                    }
                }
            }
        } else {
            mOrderAdapter.addDatas((ArrayList) list);
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    OrderList orderList = list.get(i);
                    OrderListFragmentGoodsPresenter presenter = new OrderListFragmentGoodsPresenter(mContext, this);
                    if (orderList.isMain == 0) {
                        presenter.getOrderListGoods(new SimpleHashMapBuilder<String, Object>()
                                .puts(Functions.FUNCTION, "25007")
                                .puts("subOrderId", orderList.orderId), orderList);
                    } else if (orderList.isMain == 1) {
                        presenter.getOrderListGoods(new SimpleHashMapBuilder<String, Object>()
                                .puts(Functions.FUNCTION, "25006")
                                .puts("mainOrderId", orderList.orderId), orderList);
                    }
                }
            }
        }
        if (pageNum == 0) {//这个接口如果没数据则返回的pageNum=0
            showEmpty();
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
    public void onDeleteOrderSuccess(String result) {
        showToast(result);
        pageNum = 1;
        getData();
    }

    @Override
    public void onConfirmOrderSuccess(String result) {
        showToast(result);
        pageNum = 1;
        getData();
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
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("delete".equals(function)) {
            isAgree(1, "提示", "是否删除订单", obj);

        } else if ("confirmOder".equals(function)) {
            isAgree(2, "确认收到货了吗？", "为了保障您的售后权益，请收到商品\n检查无误后再确认收货", obj);
        }

        if ("seven".equals(function)) {
            OrderBackErrorDialog orderBackErrorDialog = OrderBackErrorDialog.newInstance();
            orderBackErrorDialog.show(getChildFragmentManager(), "7天");
        }
    }

    private void isAgree(final int type, String title, String content, final Object obj) {
        StyledDialog.init(getActivity());
        StyledDialog.buildIosAlert(title, "\n" + content, new MyDialogListener() {
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
                    orderListPresenter.deleteOrder((String) obj);
                } else if (type == 2) {
                    ConfirmOderModel confirmOderModel = (ConfirmOderModel) obj;
                    orderListPresenter.confirmOder(new SimpleHashMapBuilder<String, Object>().puts("subOrderId", confirmOderModel.subOrderId).puts("ticket", confirmOderModel.ticket));
                }
            }
        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
    }

    @Override
    public void onGetOrderListGoodsSuccess() {
        if (mOrderAdapter != null) {
            mOrderAdapter.notifyDataSetChanged();
        }
    }
}