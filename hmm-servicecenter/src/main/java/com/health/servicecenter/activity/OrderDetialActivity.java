package com.health.servicecenter.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.MallGoodsItemAdapter;
import com.health.servicecenter.adapter.MallGoodsTitleAdapter;
import com.health.servicecenter.adapter.OrderDetailAdviserAdapter;
import com.health.servicecenter.adapter.OrderDetialHeaderAdapter;
import com.health.servicecenter.adapter.OrderDetialPayTypeAdapter;
import com.health.servicecenter.adapter.OrderDetialPriceAdapter;
import com.health.servicecenter.contract.OrderDetialContract;
import com.health.servicecenter.presenter.OrderDetialPresenter;
import com.health.servicecenter.utils.ActivityManager;
import com.health.servicecenter.widget.OrderBackErrorDialog;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.Functions;
import com.healthy.library.constant.SpKey;
import com.healthy.library.contract.QiYeWeiXinContract;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.model.OrderList;
import com.healthy.library.model.QiYeWeXin;
import com.healthy.library.model.QiYeWeXinKey;
import com.healthy.library.model.QiYeWeXinWorkShop;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.model.TokerWorkerInfoModel;
import com.healthy.library.presenter.QiYeWeiXinPresenter;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.SpUtils;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = ServiceRoutes.SERVICE_ORDERLISTDETIAL)
public class OrderDetialActivity extends BaseActivity implements IsFitsSystemWindows,
        OrderDetialContract.View,
        BaseAdapter.OnOutClickListener, OnRefreshLoadMoreListener, QiYeWeiXinContract.View {

    @Autowired
    String orderId;
    @Autowired
    String function = "25006";

    private ImageView img_back;
    private RecyclerView order_detial_recycleView;
    private TextView topBar_title, btn_go_pay, btn_go_cancle, btn_go_confirm, shop_cart_num;
    private RelativeLayout shop_cart_rel;
    private ConstraintLayout bottomT;

    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private OrderDetialHeaderAdapter orderDetialHeaderAdapter;
    private OrderDetialPriceAdapter orderDetialPriceAdapter;
    private OrderDetialPayTypeAdapter orderDetialPayTypeAdapter;
    private OrderDetailAdviserAdapter orderDetailAdviserAdapter;
    private MallGoodsTitleAdapter mallGoodsTitleAdapter;
    private MallGoodsItemAdapter mallGoodsItemAdapter;
    private OrderDetialPresenter orderDetialPresenter;
    private SmartRefreshLayout layoutRefresh;
    private int page = 1;
    private int firstPageSize = 0;
    private Map<String, Object> map = new HashMap<>();
    private CountDownTimer countDownTimer;
    private OrderList detialModel;
    private QiYeWeiXinPresenter qiYeWeiXinPresenter;

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detial;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        ActivityManager.addActivity(this);
        showLoading();
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        orderDetialPresenter = new OrderDetialPresenter(this, this);
        qiYeWeiXinPresenter = new QiYeWeiXinPresenter(this, this);

        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        order_detial_recycleView.setLayoutManager(virtualLayoutManager);
        order_detial_recycleView.setAdapter(delegateAdapter);

        orderDetialHeaderAdapter = new OrderDetialHeaderAdapter();
        delegateAdapter.addAdapter(orderDetialHeaderAdapter);


//        orderDetialGoodsAdapter = new OrderDetialGoodsAdapter();
//        delegateAdapter.addAdapter(orderDetialGoodsAdapter);
//        orderDetialGoodsAdapter.setOutClickListener(this);


        orderDetialPriceAdapter = new OrderDetialPriceAdapter();
        delegateAdapter.addAdapter(orderDetialPriceAdapter);


        orderDetialPayTypeAdapter = new OrderDetialPayTypeAdapter();
        delegateAdapter.addAdapter(orderDetialPayTypeAdapter);

        orderDetailAdviserAdapter = new OrderDetailAdviserAdapter();
        delegateAdapter.addAdapter(orderDetailAdviserAdapter);


        mallGoodsTitleAdapter = new MallGoodsTitleAdapter();
        delegateAdapter.addAdapter(mallGoodsTitleAdapter);


        mallGoodsItemAdapter = new MallGoodsItemAdapter();
        delegateAdapter.addAdapter(mallGoodsItemAdapter);
        mallGoodsItemAdapter.setOutClickListener(new BaseAdapter.OnOutClickListener() {
            @Override
            public void outClick(@NotNull String function, @NotNull Object obj) {
                if ("addShopCat".equals(function)) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "订单详情页页面下方”为您推荐“专题列表商品点击【加入购物车】");
                    MobclickAgent.onEvent(mContext, "btn_APP_MaternalandChildGoods_CommodityDetails_AddShoppingCart", nokmap);
                    RecommendList goodsListModel = (RecommendList) obj;
                    if (goodsListModel != null) {
                        orderDetialPresenter.addShopCat(new SimpleHashMapBuilder<String, Object>()
                                .puts("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
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


        layoutRefresh.setOnRefreshLoadMoreListener(this);
    }


    private void getDetial() {
        Map<String, Object> map = new HashMap<>();
        if ("25007".equals(function)) {
            function = "25007";
            map.put("subOrderId", orderId);
        } else {
            function = "25006";
            map.put("mainOrderId", orderId);
        }
        map.put(Functions.FUNCTION, function);
        orderDetialPresenter.getDetial(map);
    }

    private void getList() {
        map.put("pageNum", page + "");
        map.put("pageSize", 10 + "");
        map.put("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP));
        map.put("type", "6");
        map.put("firstPageSize", firstPageSize + "");
        orderDetialPresenter.getGoodsList(map);
    }

    @Override
    public void onGetDetialSuccess(final OrderList model) {
        getList();
        showContent();
        detialModel = model;
        bottomT.setVisibility(View.GONE);
        if (model == null) {
            showToast("未获取到订单信息！");
            return;
        }
        orderDetialHeaderAdapter.setModel(model);
        orderDetialPriceAdapter.setModel(model);
        orderDetialPayTypeAdapter.setModel(model);
        if (model.orderChild != null) {//子订单
            final String orderId = model.orderChild.mainId;
            final String subOrderId = model.orderChild.orderId;
            if (model.orderChild.getOrderStatus() == 0) {
                btn_go_confirm.setVisibility(View.GONE);
                btn_go_pay.setVisibility(View.VISIBLE);
                btn_go_cancle.setVisibility(View.VISIBLE);
                btn_go_cancle.setText("取消订单");
                bottomT.setVisibility(View.VISIBLE);
                topBar_title.setVisibility(View.GONE);
                downTime(Integer.parseInt(model.orderChild.residuePayTime));//剩余支付时间倒计时
                btn_go_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isAgree(1, "", "是否取消订单", orderId, null);
                    }
                });
            } else if (model.orderChild.getOrderStatus() == 1) {
                btn_go_confirm.setVisibility(View.GONE);
                btn_go_pay.setVisibility(View.GONE);
                btn_go_cancle.setVisibility(View.GONE);
                if (model.orderChild.refundId != null) {
                    btn_go_cancle.setVisibility(View.VISIBLE);
                    btn_go_cancle.setText("售后详情");
                } else {
                    btn_go_cancle.setVisibility(View.VISIBLE);
                    btn_go_cancle.setText("申请售后");
                }
                bottomT.setVisibility(View.VISIBLE);
                topBar_title.setVisibility(View.GONE);
                btn_go_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.orderChild.refundId != null) {
                            ARouter.getInstance().
                                    build(ServiceRoutes.SERVICE_ORDERBACKDETIAL)
                                    .withString("refundId", model.orderChild.lastRefundId + "")
                                    .navigation();
                        } else {
                            if (isLatestWeek(model.orderChild.paySuccessTime, new Date())) {
                                ARouter.getInstance().
                                        build(ServiceRoutes.SERVICE_ORDERBACKSELECTGOODS)
                                        .withString("orderId", subOrderId + "")
                                        .withString("function", "25007")
                                        .navigation();
                            } else {
                                OrderBackErrorDialog orderBackErrorDialog = OrderBackErrorDialog.newInstance();
                                orderBackErrorDialog.show(getSupportFragmentManager(), "7天");
                            }
                        }
                    }
                });
            } else if (model.orderChild.getOrderStatus() == 2) {
                bottomT.setVisibility(View.GONE);
                btn_go_confirm.setVisibility(View.GONE);
                btn_go_pay.setVisibility(View.GONE);
                btn_go_cancle.setVisibility(View.GONE);
            } else if (model.orderChild.getOrderStatus() == 3) {
                btn_go_confirm.setVisibility(View.GONE);
                btn_go_pay.setVisibility(View.GONE);
                btn_go_cancle.setVisibility(View.VISIBLE);
                btn_go_cancle.setText("删除订单");
                btn_go_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isAgree(2, "确认删除订单？", "删除之后无法恢复", subOrderId, null);
                    }
                });
            } else if (model.orderChild.getOrderStatus() == 4) {
                bottomT.setVisibility(View.GONE);
            } else if (model.orderChild.getOrderStatus() == 5) {
                btn_go_confirm.setVisibility(View.GONE);
                btn_go_pay.setVisibility(View.GONE);
                btn_go_cancle.setVisibility(View.GONE);
                if (model.orderChild.refundId != null) {
                    btn_go_cancle.setVisibility(View.VISIBLE);
                    btn_go_cancle.setText("售后详情");
                } else {
                    btn_go_cancle.setVisibility(View.VISIBLE);
                    btn_go_cancle.setText("申请售后");
                }
                bottomT.setVisibility(View.VISIBLE);
                topBar_title.setVisibility(View.GONE);
                btn_go_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.orderChild.refundId != null) {
                            ARouter.getInstance().
                                    build(ServiceRoutes.SERVICE_ORDERBACKDETIAL)
                                    .withString("refundId", model.orderChild.lastRefundId + "")
                                    .navigation();
                        } else {
                            if (isLatestWeek(model.orderChild.paySuccessTime, new Date())) {
                                ARouter.getInstance().
                                        build(ServiceRoutes.SERVICE_ORDERBACKSELECTGOODS)
                                        .withString("orderId", subOrderId + "")
                                        .withString("function", "25007")
                                        .navigation();
                            } else {
                                OrderBackErrorDialog orderBackErrorDialog = OrderBackErrorDialog.newInstance();
                                orderBackErrorDialog.show(getSupportFragmentManager(), "7天");
                            }
                        }
                    }
                });
            } else if (model.orderChild.getOrderStatus() == 6) {
                btn_go_confirm.setVisibility(View.VISIBLE);
                btn_go_pay.setVisibility(View.GONE);
                btn_go_cancle.setVisibility(View.GONE);
                bottomT.setVisibility(View.VISIBLE);
                btn_go_confirm.setText("确认收货");
                btn_go_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isAgree(3, "确认收到货了吗？", "为了保障您的售后权益，请收到商品\n检查无误后再确认收货", model.orderChild.orderDetailList.get(0).subOrderId, model.orderChild.ticket);
                    }
                });
                btn_go_cancle.setVisibility(View.GONE);
                if (model.orderChild.refundId != null) {
                    bottomT.setVisibility(View.VISIBLE);
                    btn_go_cancle.setVisibility(View.VISIBLE);
                    btn_go_cancle.setText("售后详情");
                } else {
                    bottomT.setVisibility(View.VISIBLE);
                    btn_go_cancle.setVisibility(View.VISIBLE);
                    btn_go_cancle.setText("申请售后");
                }
                btn_go_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.orderChild.refundId != null) {
                            ARouter.getInstance().
                                    build(ServiceRoutes.SERVICE_ORDERBACKDETIAL)
                                    .withString("refundId", model.orderChild.lastRefundId + "")
                                    .navigation();
                        } else {
                            if (isLatestWeek(model.orderChild.paySuccessTime, new Date())) {
                                ARouter.getInstance().
                                        build(ServiceRoutes.SERVICE_ORDERBACKSELECTGOODS)
                                        .withString("orderId", subOrderId + "")
                                        .withString("function", "25007")
                                        .navigation();
                            } else {
                                OrderBackErrorDialog orderBackErrorDialog = OrderBackErrorDialog.newInstance();
                                orderBackErrorDialog.show(getSupportFragmentManager(), "7天");
                            }
                        }
                    }
                });
            }
            btn_go_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detialModel != null) {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "订单详情页");
                        MobclickAgent.onEvent(mContext, "event2APPOrderDetialClick", nokmap);
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_CHECKOUT_COUNTER)
                                .withString("orderId", orderId)
                                .withString("orderType", detialModel.orderChild.getOrderRace() == 5 ? "-1" : detialModel.orderChild.getOrderType() + "")
                                .navigation();
                    }
                }
            });
        } else if (model.orderFather != null) {//主订单
            final String orderId = model.orderFather.orderId;
            if (model.orderFather.getOrderStatus() == 0) {
                btn_go_confirm.setVisibility(View.GONE);
                btn_go_pay.setVisibility(View.VISIBLE);
                btn_go_cancle.setVisibility(View.VISIBLE);
                btn_go_cancle.setText("取消订单");
                bottomT.setVisibility(View.VISIBLE);
                topBar_title.setVisibility(View.GONE);
                downTime(Integer.parseInt(model.orderFather.residuePayTime));//剩余支付时间倒计时
                btn_go_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isAgree(1, "", "是否取消订单", orderId, null);
                    }
                });
            } else if (model.orderFather.getOrderStatus() == 1) {
                bottomT.setVisibility(View.GONE);
                topBar_title.setVisibility(View.GONE);
            } else if (model.orderFather.getOrderStatus() == 2) {
                bottomT.setVisibility(View.GONE);
                btn_go_confirm.setVisibility(View.GONE);
                btn_go_pay.setVisibility(View.GONE);
                btn_go_cancle.setVisibility(View.GONE);
            } else if (model.orderFather.getOrderStatus() == 3) {
                btn_go_confirm.setVisibility(View.GONE);
                btn_go_pay.setVisibility(View.GONE);
                btn_go_cancle.setVisibility(View.VISIBLE);
                bottomT.setVisibility(View.VISIBLE);
                btn_go_cancle.setText("删除订单");
                btn_go_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isAgree(2, "确认删除订单？", "删除之后无法恢复", orderId, null);
                    }
                });
            } else if (model.orderFather.getOrderStatus() == 4) {
                bottomT.setVisibility(View.GONE);
            } else if (model.orderFather.getOrderStatus() == 5) {
                bottomT.setVisibility(View.GONE);
                topBar_title.setVisibility(View.GONE);
            } else {
                bottomT.setVisibility(View.GONE);
                topBar_title.setVisibility(View.GONE);
            }
            btn_go_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (detialModel != null) {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "订单详情页");
                        MobclickAgent.onEvent(mContext, "event2APPOrderDetialClick", nokmap);
                        ARouter.getInstance()
                                .build(ServiceRoutes.SERVICE_CHECKOUT_COUNTER)
                                .withString("orderId", orderId)
                                .withString("orderType", detialModel.orderFather.getOrderRace() == 5 ? "-1" : detialModel.orderFather.orderType + "")
                                .navigation();
                    }
                }
            });
        }
        if(model.orderChild!=null&&model.orderChild.canRefund==0){
            if(btn_go_cancle.getText().toString().equals("申请售后")){
                btn_go_cancle.setVisibility(View.GONE);
            }
        }

        buildQiYeWeiXin();
    }

    private void buildQiYeWeiXin() {
        if (detialModel != null) {
            if (detialModel.orderChild != null) {
                if (detialModel.orderChild.getOrderStatus() == 1 || detialModel.orderChild.getOrderStatus() == 5 || detialModel.orderChild.getOrderStatus() == 2) {
                    qiYeWeiXinPresenter.getMineWorker(new SimpleHashMapBuilder<String, Object>());
                }
            }
            if (detialModel.orderFather != null) {
                if (detialModel.orderFather.getOrderStatus() == 1 || detialModel.orderFather.getOrderStatus() == 5 || detialModel.orderFather.getOrderStatus() == 2) {
                    qiYeWeiXinPresenter.getMineWorker(new SimpleHashMapBuilder<String, Object>());
                }
            }
        }

    }

    private void downTime(int residuePayTime) {
        if (residuePayTime > 0) {
            countDownTimer = new CountDownTimer(residuePayTime * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    //剩余支付时间结束后刷新订单状态
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getDetial();//为啥延时  因为本地倒计时可能跟后台有一点点差异  会导致状态可能有问题
                        }
                    }, 1500);
                }
            }.start();
        }
    }

    @Override
    public void onCancleOrderSuccess(String result) {
        showToast("订单已取消");
//        finish();
        getData();
    }

    @Override
    public void onGetShopCartSuccess(ShopCartModel model) {
        if (model == null) {
            shop_cart_num.setText("0");
            return;
        }
        if (model.total == 0) {
            shop_cart_num.setVisibility(View.INVISIBLE);
        } else if (model.total > 99) {
            shop_cart_num.setText("99+");
        } else {
            shop_cart_num.setText(model.total + "");
        }
    }

    @Override
    public void successAddShopCat(String result) {
        if (result.contains("购物车添加商品")) {
            showToast("已加入购物车");
        }
        orderDetialPresenter.getShopCart();
    }

    @Override
    public void onDeleteOrderSuccess(String result) {
        showToast(result);
        finish();
    }

    @Override
    public void onConfirmOrderSuccess(String result) {
        showToast(result);
//        finish();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        page = 1;
        getDetial();
        orderDetialPresenter.getShopCart();
    }

    @Override
    public void onGetGoodsListSuccess(List<RecommendList> list, int firstPageSize) {
        if (list == null) {
            return;
        }
        if (list.size() == 0) {
            layoutRefresh.finishLoadMoreWithNoMoreData();
        } else {
            if (page == 1) {
                this.firstPageSize = firstPageSize;
                mallGoodsItemAdapter.setData((ArrayList) list);
            } else {
                mallGoodsItemAdapter.addDatas((ArrayList) list);
            }
            mallGoodsTitleAdapter.setModel("为您推荐");
            layoutRefresh.setNoMoreData(false);
            layoutRefresh.setEnableLoadMore(true);
        }
    }

    @Override
    protected void findViews() {
        super.findViews();
        img_back = findViewById(R.id.img_back);
        topBar_title = findViewById(R.id.topBar_title);
        btn_go_pay = findViewById(R.id.btn_go_pay);
        btn_go_cancle = findViewById(R.id.btn_go_cancle);
        btn_go_confirm = findViewById(R.id.btn_go_confirm);
        shop_cart_rel = findViewById(R.id.shop_cart_rel);
        order_detial_recycleView = findViewById(R.id.order_detial_recycleView);
        shop_cart_num = findViewById(R.id.shop_cart_num);
        bottomT = findViewById(R.id.bottomT);
        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shop_cart_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_BASKET)
                        .navigation();
            }
        });
        shop_cart_rel.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        order_detial_recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstCompletelyVisibleItemPosition = virtualLayoutManager.findFirstCompletelyVisibleItemPosition();
                //int lastCompletelyVisibleItemPosition = virtualLayoutManager.findLastVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == -1 || firstCompletelyVisibleItemPosition > 0) {
                    topBar_title.setVisibility(View.VISIBLE);
                } else {
                    if (firstCompletelyVisibleItemPosition == 0) {
                        if (detialModel != null) {
                            if (detialModel.getOrderStatus() == 0) {
                                topBar_title.setVisibility(View.GONE);
                            } else if (detialModel.getOrderStatus() == 1) {
                                if (detialModel.getOrderRace() == 3) {
                                    topBar_title.setVisibility(View.VISIBLE);
                                }
                            } else if (detialModel.getOrderStatus() == 2) {
                                if (detialModel.getOrderRace() == 3) {
                                    topBar_title.setVisibility(View.VISIBLE);
                                }
                            } else if (detialModel.getOrderStatus() == 3) {
                                topBar_title.setVisibility(View.GONE);
                            } else if (detialModel.getOrderStatus() == 4) {
                                if (detialModel.getOrderRace() == 3) {
                                    topBar_title.setVisibility(View.VISIBLE);
                                }
                            } else if (detialModel.getOrderStatus() == 5) {
                                if (detialModel.getOrderRace() == 3) {
                                    topBar_title.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            topBar_title.setVisibility(View.GONE);
                        }
                    } else {
                        topBar_title.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if ("countDownTime".equals(function)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDetial();
                }
            }, 1500);
        }
    }

    private void isAgree(final int type, String title, String msg, final String id, final String ticket) {
        StyledDialog.init(this);
        StyledDialog.buildIosAlert(title, "\n" + msg, new MyDialogListener() {
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
                    orderDetialPresenter.cancleOrder(id);
                } else if (type == 2) {
                    orderDetialPresenter.deleteOrder(id);
                } else if (type == 3) {
                    orderDetialPresenter.confirmOder(new SimpleHashMapBuilder<String, Object>().puts("subOrderId", id).puts("ticket", ticket));
                }
            }
        }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark, 0).setBtnText("取消", "确定").show();
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
        map.put("pageNum", page + "");
        orderDetialPresenter.getGoodsList(map);

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        getData();
    }

    public boolean isLatestWeek(String orderTime, Date now) {
        if (orderTime == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fromDate = null;
        try {
            fromDate = sdf.parse(orderTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (fromDate == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(now);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -7);  //设置为7天前
        Date before7days = calendar.getTime();   //得到7天前的时间
        if (before7days.getTime() < fromDate.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSucessGetWeiXinKey(QiYeWeXinKey qiYeWeXinKey) {

    }

    @Override
    public void onSucessGetRecommandWeiXinGroup(List<QiYeWeXin> qiYeWeXins) {

    }

    @Override
    public void onSucessGetRecommandWorkerGroup(List<QiYeWeXinWorkShop> qiYeWeXins) {

    }

    @Override
    public void onSucessGetMineWorker(TokerWorkerInfoModel tokerWorkerInfoModel) {
        if (tokerWorkerInfoModel != null&& !TextUtils.isEmpty(tokerWorkerInfoModel.bindingList.get(0).bindingTokerWorker.workWxUrl)) {
            orderDetailAdviserAdapter.setType(0);
            orderDetailAdviserAdapter.setModel(tokerWorkerInfoModel);
        }
    }

    @Override
    public void onSucessGetPublicWorker(TokerWorkerInfoModel tokerWorkerInfoModel) {
        if (tokerWorkerInfoModel != null&& !TextUtils.isEmpty(tokerWorkerInfoModel.bindingList.get(0).bindingTokerWorker.workWxUrl)) {
            orderDetailAdviserAdapter.setType(1);
            orderDetailAdviserAdapter.setModel(tokerWorkerInfoModel);
        }
    }
}