package com.health.servicecenter.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.OrderBackSelectGoodsAdapter;
import com.health.servicecenter.contract.OrderDetialContract;
import com.health.servicecenter.presenter.OrderDetialPresenter;
import com.health.servicecenter.utils.ActivityManager;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.constant.Functions;
import com.healthy.library.model.OrderList;
import com.healthy.library.model.RecommendList;
import com.healthy.library.model.ShopCartModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = ServiceRoutes.SERVICE_ORDERBACKSELECTGOODS)
public class OrderBackSelectGoods extends BaseActivity implements OrderDetialContract.View, BaseAdapter.OnOutClickListener {
    @Autowired
    String orderId;
    @Autowired
    String function;

    private OrderDetialPresenter orderDetialPresenter;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private ConstraintLayout webTitleLL;
    private TextView tvTitle;
    private TextView submitBtn;
    private ImageView ivBack;
    private ImageView ivClose;
    private ImageTextView storeTitle;
    private RecyclerView goodsRecycle;
    private OrderBackSelectGoodsAdapter orderBackSelectGoodsAdapter;
    List<OrderList.OrderDetailListBean> list = new ArrayList<>();
    private String deliveryShopName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_back_select_goods;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        ActivityManager.addActivity(this);
        orderDetialPresenter = new OrderDetialPresenter(this, this);
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        orderDetialPresenter = new OrderDetialPresenter(this, this);

        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        goodsRecycle.setLayoutManager(virtualLayoutManager);
        goodsRecycle.setAdapter(delegateAdapter);

        orderBackSelectGoodsAdapter = new OrderBackSelectGoodsAdapter();
        delegateAdapter.addAdapter(orderBackSelectGoodsAdapter);
        orderBackSelectGoodsAdapter.setOutClickListener(this);
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        Map<String, Object> map = new HashMap<>();
        map.put("subOrderId", orderId);
        map.put(Functions.FUNCTION, function);
        orderDetialPresenter.getDetial(map);
    }

    @Override
    protected void findViews() {
        webTitleLL = (ConstraintLayout) findViewById(R.id.webTitleLL);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        submitBtn = (TextView) findViewById(R.id.submitBtn);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        storeTitle = (ImageTextView) findViewById(R.id.store_title);
        goodsRecycle = (RecyclerView) findViewById(R.id.goodsRecycle);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitBtn.getBackground().setAlpha(160);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() > 0 && deliveryShopName != null) {
                    ARouter.getInstance().build(ServiceRoutes.SERVICE_COMITBACK)
                            .withObject("goodsList", list)
                            .withString("orderId", orderId)
                            .withString("deliveryShopName", deliveryShopName)
                            .navigation();
                    finish();
                } else {
                    showToast("您还未选择退货商品");
                }
            }
        });
    }


    @Override
    public void onGetDetialSuccess(OrderList model) {
        if (model != null) {
            if (model.orderChild != null) {
                if (model.orderChild.refundId != null) {
                    showToast("已申请售后");
                    finish();
                    return;
                } else {
                    showContent();
                    final OrderList.OrderChild orderChild = model.orderChild;
                    if (orderChild.orderDetailList != null && orderChild.orderDetailList.size() > 0) {
                        storeTitle.setText(orderChild.deliveryShopName);
                        deliveryShopName = orderChild.deliveryShopName;
                        orderBackSelectGoodsAdapter.setData((ArrayList) orderChild.orderDetailList);
                    }
                }
            } else {
                showToast("获取订单信息失败");
                showEmpty();
            }
        }
    }

    @Override
    public void outClick(@NotNull String function, @NotNull Object obj) {
        if (function.equals("selcet")) {
            OrderList.OrderDetailListBean detailsBean = (OrderList.OrderDetailListBean) obj;
            if (detailsBean.isSelect) {
                list.add(detailsBean);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (detailsBean.orderDetailId.equals(list.get(i).orderDetailId)) {
                        list.remove(i);
                        i--;
                    }
                }
            }
            if (list.size() > 0) {
                submitBtn.getBackground().setAlpha(255);
            } else {
                submitBtn.getBackground().setAlpha(160);
            }
        }
    }

    @Override
    public void onCancleOrderSuccess(String result) {

    }

    @Override
    public void onGetShopCartSuccess(ShopCartModel model) {

    }

    @Override
    public void successAddShopCat(String result) {

    }

    @Override
    public void onDeleteOrderSuccess(String result) {

    }

    @Override
    public void onConfirmOrderSuccess(String result) {

    }

    @Override
    public void onGetGoodsListSuccess(List<RecommendList> list, int firstPageSize) {

    }
}