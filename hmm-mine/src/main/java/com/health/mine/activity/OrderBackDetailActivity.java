package com.health.mine.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.health.mine.adapter.OrderBackStatusAdapter;
import com.health.mine.contract.OrderBackDetailContract;
import com.health.mine.model.BackDetialModel;
import com.health.mine.model.OrderBackDetailModel;
import com.health.mine.model.OrderDetailLineModel;
import com.health.mine.presenter.OrderBackDetailPresenter;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.widget.CommonInsertSection;
import com.healthy.library.widget.SectionView;
import com.healthy.library.widget.StatusLayout;
import com.healthy.library.widget.TopBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = MineRoutes.MINE_ORDER_BACK_DETAIL)
public class OrderBackDetailActivity extends BaseActivity implements OrderBackDetailContract.View {
    @Autowired
    String refundId;

    private com.healthy.library.widget.TopBar topBar;
    private android.widget.LinearLayout topTip;
    private android.widget.TextView backStatus;
    private com.healthy.library.widget.CommonInsertSection backMoney;
    private com.healthy.library.widget.CommonInsertSection backCount;
    private com.healthy.library.widget.CommonInsertSection backTime;
    private android.view.View dividerTop;
    private com.healthy.library.widget.StatusLayout layoutStatus;
    private com.healthy.library.widget.SectionView priceBackCount;
    private RecyclerView priceBackWhyRecycler;

    OrderBackDetailPresenter orderBackDetailPresenter;
    Map<String, Object> detailmap = new HashMap<>();

    // OrderBackDetailModel orderBackDetailModel;

    OrderBackStatusAdapter orderBackStatusAdapter;

    List<OrderDetailLineModel> orderDetailLineModels = new ArrayList<>();
    private String whatpay;
    private String wheresee;
    private String whatphone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_back_detail;
    }

    @Override
    protected void findViews() {
        super.findViews();
        initView();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        orderBackDetailPresenter = new OrderBackDetailPresenter(this, this);
        detailmap.put("refundId", refundId);

        orderBackStatusAdapter = new OrderBackStatusAdapter();
        priceBackWhyRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        orderBackStatusAdapter.bindToRecyclerView(priceBackWhyRecycler);
        getData();
    }

    @Override
    public void getData() {
        super.getData();
        showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                orderBackDetailPresenter.getBackDetial(refundId);
                //orderBackDetailPresenter.getOrderDetail(detailmap);
            }
        }, 1000);
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.top_bar);
        topTip = (LinearLayout) findViewById(R.id.top_tip);
        backStatus = (TextView) findViewById(R.id.backStatus);
        backMoney = (CommonInsertSection) findViewById(R.id.backMoney);
        backCount = (CommonInsertSection) findViewById(R.id.backCount);
        backTime = (CommonInsertSection) findViewById(R.id.backTime);
        dividerTop = (View) findViewById(R.id.divider_top);
        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
        priceBackCount = (SectionView) findViewById(R.id.priceBackCount);
        priceBackWhyRecycler = (RecyclerView) findViewById(R.id.priceBackWhyRecycler);
    }

    @Override
    public void onGetOrderDetailSuccess(OrderBackDetailModel detailModel) {
        //orderBackDetailModel = detailModel;

    }

    @Override
    public void onGetBackDetialSuccess(BackDetialModel detialModel) {
        OrderBackDetailModel orderBackDetailMode = new OrderBackDetailModel();
        orderBackDetailMode.applyTime = detialModel.getPaymentTime();
        orderBackDetailMode.failMsg = detialModel.getDisposeAmount();
        //orderBackDetailMode.refundId = detialModel.getId();
        orderBackDetailMode.refundStatus = detialModel.getStatus() == 2 ? 3 : 1;
        orderBackDetailMode.refundAllMoney = detialModel.getRefundAmount();
        orderBackDetailMode.refundTime = detialModel.getCreateTime();
        orderBackDetailMode.refundType = detialModel.getPaymentType() == 2 ? "WxPay" : "AliPay";
        buildView(orderBackDetailMode);
    }

    private void buildView(OrderBackDetailModel detailModel) {
        orderDetailLineModels.clear();
        whatpay = ("WxPay".equals(detailModel.refundType) ? "??????" : "?????????");
        wheresee = ("WxPay".equals(detailModel.refundType) ? "??????->??????->?????????????????????????????????->??????" : "??????->??????");
        whatphone = ("WxPay".equals(detailModel.refundType) ? "95017" : "95188");

        backStatus.setText("");
        backMoney.getTxtContent().setText("");
        backCount.getTxtContent().setText("");
        backTime.getTxtContent().setText("");

        if (detailModel.refundStatus == 0) {

        }
        if (detailModel.refundStatus == 1) {


            backStatus.setText("???????????????" + whatpay + "?????????");
            backMoney.getTxtContent().setText("??" + detailModel.refundAllMoney);
            backCount.getTxtContent().setText(whatpay + "??????");
            backTime.getTxtContent().setText("??????" + detailModel.refundTime + "???");


            orderDetailLineModels.add(new OrderDetailLineModel(R.drawable.timelline_dot_green, "?????????????????????",
                    SpanUtils.getBuilder(mContext, "?????????????????????????????????").create(), detailModel.applyTime, false));
            orderDetailLineModels.add(new OrderDetailLineModel(R.drawable.timelline_dot_green, "?????????????????????",
                    SpanUtils.getBuilder(mContext, whatpay + "??????????????????????????????1-3??????????????????????????????????????????").create(), detailModel.applyTime, false));

            orderDetailLineModels.add(new OrderDetailLineModel(R.drawable.timelline_dot_yellow, ("WxPay".equals(detailModel.refundType) ? "??????" : "?????????") + "?????????",
                    SpanUtils.getBuilder(mContext, whatpay).setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("??????")
                            .append(detailModel.refundAllMoney + "").setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("??????????????????")
                            .append(whatpay + "??????").setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("??????????????????" + whatpay + "???????????????")
                            .append(wheresee).setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("?????????????????????")
                            .append(detailModel.refundTime).setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("????????????????????????????????????????????????????????????" + whatpay + "??????")
                            .append(whatphone).setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("?????????")
                            .create(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), true));

        }
        if (detailModel.refundStatus == 2) {

        }
        if (detailModel.refundStatus == 3) {

            backStatus.setText("??????????????????????????????");
            backMoney.getTxtContent().setText("??" + detailModel.refundAllMoney);
            backCount.getTxtContent().setText(whatpay + "??????");
            backTime.getTxtContent().setText("?????????");


            orderDetailLineModels.add(new OrderDetailLineModel(R.drawable.timelline_dot_green, "?????????????????????",
                    SpanUtils.getBuilder(mContext, "?????????????????????????????????").create(), detailModel.applyTime, false));
            orderDetailLineModels.add(new OrderDetailLineModel(R.drawable.timelline_dot_green, "?????????????????????",
                    SpanUtils.getBuilder(mContext, whatpay + "??????????????????????????????1-3??????????????????????????????????????????").create(), detailModel.applyTime, false));

            orderDetailLineModels.add(new OrderDetailLineModel(R.drawable.timelline_dot_yellow, ("WxPay".equals(detailModel.refundType) ? "??????" : "?????????") + "?????????",
                    SpanUtils.getBuilder(mContext, whatpay).setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("??????")
                            .append(detailModel.refundTime + "").setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("??????")
                            .append(detailModel.refundAllMoney + "").setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("??????????????????")
                            .append(whatpay + "??????").setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("??????????????????" + whatpay + "???????????????")
                            .append(wheresee).setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("????????????????????????????????????????????????" + whatpay + "??????")
                            .append(whatphone).setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("?????????")
                            .create(), detailModel.refundTime, true));
        }
        orderBackStatusAdapter.setNewData(orderDetailLineModels);

    }
}
