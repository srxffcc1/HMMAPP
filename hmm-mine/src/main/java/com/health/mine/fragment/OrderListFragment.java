//package com.health.mine.fragment;
//
//import android.os.Bundle;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.health.mine.R;
//import com.health.mine.adapter.OrderAdapter;
//import com.health.mine.contract.OrderListContract;
//import com.health.mine.model.OrderDetailModel;
//import com.health.mine.presenter.OrderListPresenter;
//import com.healthy.library.base.BaseFragment;
//import com.healthy.library.decoration.OrderDecoration;
//import com.healthy.library.model.OlderResultFresh;
//import com.healthy.library.model.PayResultFresh;
//import com.healthy.library.model.PageInfoEarly;
//import com.healthy.library.widget.StatusLayout;
//import com.scwang.smart.refresh.layout.SmartRefreshLayout;
//import com.scwang.smart.refresh.layout.api.RefreshLayout;
//import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.List;
//
///**
// * @author Li
// * @date 2019/04/10 09:29
// * @des 订单列表
// */
//
//public class OrderListFragment extends BaseFragment implements OrderListContract.View,
//        OnRefreshLoadMoreListener {
//
//    private RecyclerView mRecyclerOrder;
//    private boolean mFirst;
//    private String mType;
//    private OrderListPresenter mPresenter;
//    private OrderAdapter mOrderAdapter;
//    private StatusLayout mStatusLayout;
//    private SmartRefreshLayout mRefreshLayout;
//    private int mCurrentPage;
//
//    public static OrderListFragment newInstance(String type, boolean first) {
//        Bundle args = new Bundle();
//        OrderListFragment fragment = new OrderListFragment();
//        fragment.setArguments(args);
//        args.putString("type", type);
//        args.putBoolean("first", first);
//        return fragment;
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.mine_fragment_order_list;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onCreate() {
//        super.onCreate();
//        EventBus.getDefault().register(this);
//        if (getArguments() != null) {
//            setStatusLayout(mStatusLayout);
//            mRefreshLayout.setOnRefreshLoadMoreListener(this);
//            mFirst = getArguments().getBoolean("first");
//            mType = getArguments().getString("type");
//            mPresenter = new OrderListPresenter(mContext, this);
//            mOrderAdapter = new OrderAdapter();
//            mRecyclerOrder.setLayoutManager(new LinearLayoutManager(mContext));
//            mOrderAdapter.bindToRecyclerView(mRecyclerOrder);
//            mRecyclerOrder.addItemDecoration(new OrderDecoration(mContext));
////            mOrderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
////                @Override
////                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
////                    final OrderDetailModel model = (OrderDetailModel) adapter.getData().get(position);
////                    int id = view.getId();
////                    if (id == R.id.layout_item) {
////                        ARouter.getInstance().build(MineRoutes.MINE_ORDER_DETAIL)
////                                .withString("orderId", model.getId()+"")
////                                .navigation();
////                    } else if (id == R.id.tv_pay) {
////                        ARouter.getInstance()
////                                .build(MallRoutes.MALL_CHECKOUT_COUNTER)
////                                .withString("orderId", model.getId()+"")
////                                .navigation();
////                    } else if (id == R.id.tv_cancel) {
////                        new AlertDialog.Builder(mContext)
////                                .setTitle("提示")
////                                .setMessage("是否取消该订单")
////                                .setNegativeButton("取消", null)
////                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
////                                    @Override
////                                    public void onClick(DialogInterface dialog, int which) {
////                                        mPresenter.cancelOrder(model.getId()+"");
////                                    }
////                                }).create().show();
////                    } else if (id == R.id.tv_evaluate) {
////                        ARouter.getInstance().build(MineRoutes.MINE_EVALUATE)
////                                .withString("orderId", model.getId()+"")
////                                .withString("shopId", model.getShopId()+"")
////                                .withString("shopBrand", model.getId()+"")
////                                .navigation();
////                    } else if (id == R.id.tv_store_name) {
//////                        ARouter.getInstance()
//////                                .build(MallRoutes.MALL_STORE_DETAIL)
//////                                .withString("shopId", model.getShopId()+"")
//////                                .withString("shopBrand", model.getShopBrand())
//////                                .navigation();
////                    }
////                }
////            });
//        }
////        if (mFirst) {
////            getData();
////        }
//        getData();
//    }
//
//    @Override
//    protected void onVisible() {
//        super.onVisible();
////        getData();
//    }
//
//    @Override
//    protected void onFirstVisible() {
//        super.onFirstVisible();
////        if (!mFirst) {
////            getData();
////        }
//    }
//
//    @Override
//    protected void findViews() {
//        mRecyclerOrder = getContentView().findViewById(R.id.recycler_order);
//        mStatusLayout = getContentView().findViewById(R.id.layout_status);
//        mRefreshLayout = getContentView().findViewById(R.id.layout_refresh);
//    }
//
//    @Override
//    public void getData() {
//        mPresenter.getOrderList(1, mType);
//    }
//
//    @Override
//    public void onGetOrderListSuccess(List<OrderDetailModel> list, PageInfoEarly pageInfo) {
//        mCurrentPage = pageInfo.currentPage;
//        if (mCurrentPage==1||mCurrentPage==0) {
//            mOrderAdapter.setNewData(list);
//            if (list.size() == 0) {
//                showEmpty();
//            }
//        } else {
//            mOrderAdapter.addData(list);
//        }
//        if (pageInfo.isMore!=1) {
//            //System.out.println("没有更多了");
//            mRefreshLayout.finishLoadMoreWithNoMoreData();
//        } else {
//            mRefreshLayout.setNoMoreData(false);
//            mRefreshLayout.setEnableLoadMore(true);
//        }
//    }
//
//    @Override
//    public void onLoadMore(RefreshLayout refreshLayout) {
//        mPresenter.getOrderList(++mCurrentPage, mType);
//        EventBus.getDefault().post(new OlderResultFresh());
//    }
//
//    @Override
//    public void onRefresh(RefreshLayout refreshLayout) {
//        mPresenter.getOrderList(1, mType);
//        EventBus.getDefault().post(new OlderResultFresh());
//    }
//
//    @Override
//    public void onRequestFinish() {
//        mRefreshLayout.finishRefresh();
//        mRefreshLayout.finishLoadMore();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    public void onCancelOrderSuccess() {
//        onRefresh(mRefreshLayout);
//    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getPayResult(PayResultFresh model) {
//        //System.out.println("重新刷新");
//        getData();
//    }
//}