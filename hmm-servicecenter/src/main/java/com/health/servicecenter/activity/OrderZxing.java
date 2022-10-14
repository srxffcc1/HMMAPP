package com.health.servicecenter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.servicecenter.R;
import com.health.servicecenter.contract.OrderZxingContract;
import com.healthy.library.model.OrderGoodsListModel;
import com.health.servicecenter.presenter.OrderZxingPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.interfaces.IsFitsSystemWindows;
import com.healthy.library.widget.CornerImageView;

//@Route(path = ServiceRoutes.SERVICE_ORDERZXING)
public class OrderZxing extends BaseActivity implements IsFitsSystemWindows, OrderZxingContract.View {
    @Autowired
    String orderId;
    private ImageView img_back, iv_zxingImg;
    private TextView goodsTitle, goodsMoney, goodsCount, zxing_num;
    private CornerImageView goodsImg;


    private OrderZxingPresenter orderZxingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_zxing;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        orderZxingPresenter = new OrderZxingPresenter(this, this);
        orderZxingPresenter.getOrderDetail(orderId);
    }

    @Override
    protected void findViews() {
        super.findViews();
        img_back = findViewById(R.id.img_back);
        iv_zxingImg = findViewById(R.id.iv_zxingImg);
        goodsTitle = findViewById(R.id.goodsTitle);
        goodsMoney = findViewById(R.id.goodsMoney);
        goodsCount = findViewById(R.id.goodsCount);
        zxing_num = findViewById(R.id.zxing_num);
        goodsImg = findViewById(R.id.goodsImg);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onGetOrderDetailSuccess(OrderGoodsListModel detailModel) {
        if (detailModel != null) {
            if (detailModel.getTicketList() != null && detailModel.getTicketList().size() > 0) {

            } else {
                showToast("获取核销码失败");
            }

        } else {
            showToast("未获取到数据");
        }
    }
}