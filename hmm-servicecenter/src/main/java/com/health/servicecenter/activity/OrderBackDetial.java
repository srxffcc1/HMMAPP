package com.health.servicecenter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.health.servicecenter.R;
import com.health.servicecenter.adapter.OrderBackDetailTipsAdapter;
import com.health.servicecenter.adapter.OrderBackDetialFooterAdapter;
import com.health.servicecenter.adapter.OrderBackDetialGoodsAdapter;
import com.health.servicecenter.adapter.OrderBackDetialHeaderAdapter;
import com.health.servicecenter.contract.OrderBackDetialContract;
import com.health.servicecenter.presenter.OrderBackDetialPresenter;
import com.health.servicecenter.utils.ActivityManager;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.model.BackListModel;
import com.healthy.library.routes.ServiceRoutes;

@Route(path = ServiceRoutes.SERVICE_ORDERBACKDETIAL)
public class OrderBackDetial extends BaseActivity implements OrderBackDetialContract.View {
    @Autowired
    String refundId;

    private RecyclerView back_recyclerView;
    private ImageView iv_back;
    private ImageView iv_close;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private OrderBackDetialHeaderAdapter orderBackDetialHeaderAdapter;
    private OrderBackDetialGoodsAdapter orderBackDetialGoodsAdapter;
    private OrderBackDetialFooterAdapter orderBackDetialFooterAdapter;
    private OrderBackDetailTipsAdapter orderBackDetailTipsAdapter;

    private OrderBackDetialPresenter orderBackDetialPresenter;

    @Override
    protected void findViews() {
        super.findViews();
        back_recyclerView = findViewById(R.id.back_recyclerView);
        iv_back = findViewById(R.id.iv_back);
        iv_close = findViewById(R.id.iv_close);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.finishAllActivity();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        ARouter.getInstance().inject(this);
        orderBackDetialPresenter = new OrderBackDetialPresenter(this, this);
        virtualLayoutManager = new VirtualLayoutManager(mContext);

        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        back_recyclerView.setLayoutManager(virtualLayoutManager);
        back_recyclerView.setAdapter(delegateAdapter);

        orderBackDetialHeaderAdapter = new OrderBackDetialHeaderAdapter();
        delegateAdapter.addAdapter(orderBackDetialHeaderAdapter);

        orderBackDetailTipsAdapter = new OrderBackDetailTipsAdapter();
        delegateAdapter.addAdapter(orderBackDetailTipsAdapter);

        orderBackDetialGoodsAdapter = new OrderBackDetialGoodsAdapter();
        delegateAdapter.addAdapter(orderBackDetialGoodsAdapter);

        orderBackDetialFooterAdapter = new OrderBackDetialFooterAdapter();
        delegateAdapter.addAdapter(orderBackDetialFooterAdapter);

        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
    }

    @Override
    public void getData() {
        super.getData();
        orderBackDetialPresenter.getBackDetial(refundId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_back_detial;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }

    @Override
    public void onGetBackDetialSuccess(BackListModel detialModel) {
        if (detialModel == null) {
            showToast("获取详情失败");
            return;
        }
        orderBackDetialHeaderAdapter.setModel(detialModel);
        orderBackDetialGoodsAdapter.setModel(detialModel);
        orderBackDetialFooterAdapter.setModel(detialModel);
        orderBackDetailTipsAdapter.setModel("");
    }
}