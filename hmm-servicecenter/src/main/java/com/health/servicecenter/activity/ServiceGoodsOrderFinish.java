package com.health.servicecenter.activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.MallGoodsItemAdapter;
import com.health.servicecenter.adapter.MallGoodsPayFinishTopAdapter;
import com.health.servicecenter.adapter.MallGoodsTitleAdapter;
import com.health.servicecenter.contract.ServiceOrderFinishContract;
import com.health.servicecenter.presenter.ServiceOrderFinishPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.OrderDetialModel;
import com.healthy.library.model.OrderModel;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.TabChangeModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FrameActivityManager;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = ServiceRoutes.SERVICE_ORDER_FINISH)
public class ServiceGoodsOrderFinish extends BaseActivity implements IsFitsSystemWindows, ServiceOrderFinishContract.View, BaseAdapter.OnOutClickListener, OnRefreshLoadMoreListener {
    private com.healthy.library.widget.TopBar topBar;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
    private androidx.recyclerview.widget.RecyclerView recyclerList;

    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;


    ServiceOrderFinishPresenter serviceBasketPresenter;
    MallGoodsPayFinishTopAdapter mallGoodsPayFinishTopAdapter;
    MallGoodsTitleAdapter mallGoodsTitleAdapter;
    MallGoodsItemAdapter mallGoodsItemAdapter;
    int page = 1;


    int retryTime=0;

    //支付完成
    Map<String, Object> recommandMap = new HashMap<>();

    OrderDetialModel orderDetialModel;
    OrderModel orderServerDetialModel;

    @Autowired
    String orderId;
    @Autowired
    boolean isService = false;
    @Autowired
    String teamNum;
    @Autowired
    String orderType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_normal_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        serviceBasketPresenter = new ServiceOrderFinishPresenter(this, this);
        recommandMap.put("type", "6");
//        recommandMap.put("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE));
//        recommandMap.put("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE));
//        recommandMap.put("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE));
//        recommandMap.put("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG));
//        recommandMap.put("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG));
        recommandMap.put("pageNum", page + "");
        recommandMap.put("pageSize", "10");
        buildRecyclerView();
        getData();
        Map nokmap = new HashMap<String, String>();
        nokmap.put("soure", "下单确认页");
        MobclickAgent.onEvent(mContext, "下单确认页", nokmap);
        if ("9".equals(orderType) || "-4".equals(orderType)) {
            topBar.setTitle("领取奖品");
        } else {
            topBar.setTitle("支付中");
        }
//        serviceBasketPresenter.getGoodsRecommend(new SimpleHashMapBuilder<String, Object>()
//                .puts("position", "6")
//                .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
//                .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
//                .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
//                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
//                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
//                .puts("pageNum", page + "")
//                .puts("pageSize", "20"));

        serviceBasketPresenter.getGoodsRecommend(recommandMap);
        layoutRefresh.setOnRefreshLoadMoreListener(this);

    }

    @Override
    public void getData() {
        super.getData();
        //orderType 9 奖品领取类型
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isService) {
                    serviceBasketPresenter.getOrderDetail(new SimpleHashMapBuilder<String, Object>()
                            .puts("orderId", orderId)
                            .puts(Functions.FUNCTION, "25006"));
                } else {
                    serviceBasketPresenter.getOrderDetail(new SimpleHashMapBuilder<String, Object>()
                            .puts("mainOrderId", orderId)
                            .puts(Functions.FUNCTION, "25006"));
                }
            }
        },2000);


    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    private void buildRecyclerView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recyclerList.setLayoutManager(virtualLayoutManager);
        recyclerList.setAdapter(delegateAdapter);

        mallGoodsPayFinishTopAdapter = new MallGoodsPayFinishTopAdapter();
        delegateAdapter.addAdapter(mallGoodsPayFinishTopAdapter);
        mallGoodsPayFinishTopAdapter.setModel(null);
        mallGoodsPayFinishTopAdapter.setOutClickListener(this);
        mallGoodsPayFinishTopAdapter.setOrderType(orderType);

        mallGoodsTitleAdapter = new MallGoodsTitleAdapter();
        delegateAdapter.addAdapter(mallGoodsTitleAdapter);

        mallGoodsItemAdapter = new MallGoodsItemAdapter();
        delegateAdapter.addAdapter(mallGoodsItemAdapter);
        mallGoodsItemAdapter.setOutClickListener(new BaseAdapter.OnOutClickListener() {
            @Override
            public void outClick(@NotNull String function, @NotNull Object obj) {

                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "支付成功页面下方”精挑细选“专题列表商品点击【加入购物车】");
                MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_AddShoppingCart", nokmap);

                if ("addShopCat".equals(function)) {
                    RecommendList goodsListModel = (RecommendList) obj;
                    if (goodsListModel != null) {
                        serviceBasketPresenter.addShopCat(new SimpleHashMapBuilder<String, Object>()
                                .puts("shopId", goodsListModel.getShopId())
                                .puts("goodsShopId", goodsListModel.getShopId())
                                .puts("goodsSource", "1")
                                .puts("goodsType", goodsListModel.getGoodsType() + "")
                                .puts("goodsId", goodsListModel.getId() + "")
                                .puts("goodsSpecId", goodsListModel.skuId + "")
                                .puts("goodsQuantity", 1 + "")
                        );
                    }
                }
            }
        });
    }

    private void initView() {

        topBar = (TopBar) findViewById(R.id.top_bar);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
        recyclerList = (RecyclerView) findViewById(R.id.recycler_list);
    }


    @Override
    public void sucessGetOrderDetail(com.healthy.library.model.OrderList.OrderFather orderModel) {
        this.orderDetialModel = orderDetialModel;
        if ("5".equals(orderModel.orderType)) {
            mallGoodsPayFinishTopAdapter.setPayNoTimeString("积分余额不足/异常错误");
        }
        mallGoodsPayFinishTopAdapter.notifyDataSetChanged();
        if ("1".equals(orderModel.orderStatus)) {//支付成功了 则3秒之后关闭页面
            mallGoodsPayFinishTopAdapter.setModel("");
            mallGoodsPayFinishTopAdapter.setPayOk("1".equals(orderModel.orderStatus));
            topBar.setTitle(("9".equals(orderType) || "-4".equals(orderType)) ? "领奖成功" : "支付成功");
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                }
//            },3000);
        } else {
            topBar.setTitle(("9".equals(orderType) || "-4".equals(orderType)) ? "领奖失败" : "支付中");
            if(retryTime==0){
                retryTime=1;
                getData();
            }else {
                topBar.setTitle(("9".equals(orderType) || "-4".equals(orderType)) ? "领奖失败" : "支付失败");
                mallGoodsPayFinishTopAdapter.setModel("");
                mallGoodsPayFinishTopAdapter.setPayOk("1".equals(orderModel.orderStatus));
            }
        }
    }

    @Override
    public void sucessGetServerOrderDetail(OrderModel orderModel) {
        this.orderServerDetialModel = orderModel;
        mallGoodsPayFinishTopAdapter.setPayOk(orderServerDetialModel.payStatus == 1);
        mallGoodsPayFinishTopAdapter.setModel("");
        mallGoodsPayFinishTopAdapter.notifyDataSetChanged();
        if (orderServerDetialModel.payStatus == 1) {//支付成功了 则3秒之后关闭页面

            topBar.setTitle("支付成功");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 3000);
        } else {
            topBar.setTitle("支付失败");
        }
    }

    @Override
    public void successGetGoodsRecommend(List<RecommendList> result) {
        if (result == null) {
            return;
        }
        if (result.size() == 0) {
            ////System.out.println("目前推荐的长度");
            layoutRefresh.finishLoadMoreWithNoMoreData();
            if (page == 1 || page == 0) {
                mallGoodsTitleAdapter.setModel(null);
            }
        } else {
            mallGoodsTitleAdapter.setModel("精挑细选");
            if (page == 1) {
                mallGoodsItemAdapter.setData((ArrayList<RecommendList>) result);
                ////System.out.println("目前推荐的长度设置数据");
            } else {
                mallGoodsItemAdapter.addDatas((ArrayList<RecommendList>) result);
            }
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    public void successAddShopCat(String result) {
        showToast("添加购物车成功");
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("返回首页".equals(function)) {
            if ("-1".equals(orderType)) {
                finish();
            } else {
                try {
                    EventBus.getDefault().postSticky(new TabChangeModel(0));
                    FrameActivityManager.instance().finishOthersActivity(Class.forName("com.health.client.activity.MainActivity"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        if ("查看订单".equals(function)) {
            if (isService) {
//                ARouter.getInstance().build(ServiceRoutes.SERVICE_SERVICEORDERDETIAL)
//                        .withString("orderId", orderId+"")
//                        .withString("type", "待付款")
//                        .navigation();
            } else {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_ORDERLISTDETIAL)
                        .withString("orderId", orderId + "")
                        .withString("function","25006")
                        .navigation();
            }
            finish();
            return;
        }
        if ("重新支付".equals(function)) {
            if (isService) {
//                ARouter.getInstance()
//                        .build(ServiceRoutes.SERVICE_CHECKOUT_COUNTER)
//                        .withString("orderId", orderId)
//                        .withString("teamNum", teamNum)
//                        .withBoolean("isServiceOrder",isService)
//                        .navigation();
            } else {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_CHECKOUT_COUNTER)
                        .withString("orderId", orderId)
                        .withString("teamNum", teamNum)
                        .withBoolean("isServiceOrder", isService)
                        .navigation();
            }
            finish();
            return;
        }
    }

    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        layoutRefresh.finishLoadMore();
        layoutRefresh.finishRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        serviceBasketPresenter.getGoodsRecommend(new SimpleHashMapBuilder<String, Object>()
                .puts("type", "6")
                .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                .puts("pageNum", page + "")
                .puts("pageSize", "10"));
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        getData();
        serviceBasketPresenter.getGoodsRecommend(new SimpleHashMapBuilder<String, Object>()
                .puts("type", "6")
                .puts("provinceCode", LocUtil.getProvinceNo(mContext, SpKey.LOC_CHOSE))
                .puts("cityCode", LocUtil.getCityNo(mContext, SpKey.LOC_CHOSE))
                .puts("areasCode", LocUtil.getAreaNo(mContext, SpKey.LOC_CHOSE))
                .puts("longitude", LocUtil.getLongitude(mContext, SpKey.LOC_ORG))
                .puts("latitude", LocUtil.getLatitude(mContext, SpKey.LOC_ORG))
                .puts("pageNum", page + "")
                .puts("pageSize", "10"));
    }
}
