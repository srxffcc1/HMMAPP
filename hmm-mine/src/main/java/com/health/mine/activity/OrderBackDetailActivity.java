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
        whatpay = ("WxPay".equals(detailModel.refundType) ? "微信" : "支付宝");
        wheresee = ("WxPay".equals(detailModel.refundType) ? "我的->钱包->支付中心（页面右上角）->账单" : "我的->账单");
        whatphone = ("WxPay".equals(detailModel.refundType) ? "95017" : "95188");

        backStatus.setText("");
        backMoney.getTxtContent().setText("");
        backCount.getTxtContent().setText("");
        backTime.getTxtContent().setText("");

        if (detailModel.refundStatus == 0) {

        }
        if (detailModel.refundStatus == 1) {


            backStatus.setText("退款状态：" + whatpay + "入账中");
            backMoney.getTxtContent().setText("¥" + detailModel.refundAllMoney);
            backCount.getTxtContent().setText(whatpay + "支付");
            backTime.getTxtContent().setText("预计" + detailModel.refundTime + "前");


            orderDetailLineModels.add(new OrderDetailLineModel(R.drawable.timelline_dot_green, "退款申请已提交",
                    SpanUtils.getBuilder(mContext, "您的退款申请已成功提交").create(), detailModel.applyTime, false));
            orderDetailLineModels.add(new OrderDetailLineModel(R.drawable.timelline_dot_green, "憨妈妈处理完成",
                    SpanUtils.getBuilder(mContext, whatpay + "处理完成后，退款会在1-3个工作日内退回至您的微信账户").create(), detailModel.applyTime, false));

            orderDetailLineModels.add(new OrderDetailLineModel(R.drawable.timelline_dot_yellow, ("WxPay".equals(detailModel.refundType) ? "微信" : "支付宝") + "入账中",
                    SpanUtils.getBuilder(mContext, whatpay).setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("正将")
                            .append(detailModel.refundAllMoney + "").setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("元入账至您的")
                            .append(whatpay + "账户").setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("，您可以打开" + whatpay + "，依次点击")
                            .append(wheresee).setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("核实，预计最晚")
                            .append(detailModel.refundTime).setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("完成入账，具体入账进度请使用交易号，拨打" + whatpay + "客服")
                            .append(whatphone).setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("查询。")
                            .create(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), true));

        }
        if (detailModel.refundStatus == 2) {

        }
        if (detailModel.refundStatus == 3) {

            backStatus.setText("退款状态：退款已入账");
            backMoney.getTxtContent().setText("¥" + detailModel.refundAllMoney);
            backCount.getTxtContent().setText(whatpay + "支付");
            backTime.getTxtContent().setText("已到账");


            orderDetailLineModels.add(new OrderDetailLineModel(R.drawable.timelline_dot_green, "退款申请已提交",
                    SpanUtils.getBuilder(mContext, "您的退款申请已成功提交").create(), detailModel.applyTime, false));
            orderDetailLineModels.add(new OrderDetailLineModel(R.drawable.timelline_dot_green, "憨妈妈处理完成",
                    SpanUtils.getBuilder(mContext, whatpay + "处理完成后，退款会在1-3个工作日内退回至您的微信账户").create(), detailModel.applyTime, false));

            orderDetailLineModels.add(new OrderDetailLineModel(R.drawable.timelline_dot_yellow, ("WxPay".equals(detailModel.refundType) ? "微信" : "支付宝") + "入账中",
                    SpanUtils.getBuilder(mContext, whatpay).setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("已于")
                            .append(detailModel.refundTime + "").setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("前将")
                            .append(detailModel.refundAllMoney + "").setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("元入账至您的")
                            .append(whatpay + "账户").setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("，您可以打开" + whatpay + "，依次点击")
                            .append(wheresee).setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("核实，如有疑问请使用交易号，拨打" + whatpay + "客服")
                            .append(whatphone).setForegroundColor(Color.parseColor("#FFAB2D"))
                            .append("查询。")
                            .create(), detailModel.refundTime, true));
        }
        orderBackStatusAdapter.setNewData(orderDetailLineModels);

    }
}
