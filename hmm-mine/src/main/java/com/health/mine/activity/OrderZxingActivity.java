//package com.health.mine.activity;
//
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.cardview.widget.CardView;
//import androidx.constraintlayout.widget.ConstraintLayout;
//
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.alibaba.android.arouter.facade.annotation.Autowired;
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.bumptech.glide.Glide;
//import com.health.mine.R;
//import com.health.mine.contract.OrderZxingContract;
//import com.health.mine.model.OrderDetailModel;
//import com.health.mine.presenter.OrderZxingDetailPresenter;
//import com.healthy.library.base.BaseActivity;
//import com.healthy.library.interfaces.IsFitsSystemWindows;
//import com.healthy.library.message.UpdateOrderByInfo;
//import com.healthy.library.routes.MineRoutes;
//import com.healthy.library.utils.ImageUtilK;
//import com.healthy.library.utils.StringUtils;
//import com.healthy.library.watcher.AlphaTextView;
//import com.healthy.library.widget.CornerImageView;
//import com.healthy.library.widget.ImageTextView;
//import com.healthy.library.widget.StatusLayout;
//import com.healthy.library.widget.TopBar;
//import com.liys.doubleclicklibrary.click.DoubleClickCancel;
//import com.scwang.smart.refresh.layout.SmartRefreshLayout;
//import com.scwang.smart.refresh.layout.api.RefreshLayout;
//import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Li
// * @date 2019/04/10 15:17
// * @des 订单详情
// */
//@Route(path = MineRoutes.MINE_ORDER_ZXING_DETAIL)
//public class OrderZxingActivity extends BaseActivity implements OrderZxingContract.View, OnRefreshLoadMoreListener, IsFitsSystemWindows, DoubleClickCancel {
//
//    @Autowired
//    String orderId;
//    private OrderZxingDetailPresenter mPresenter;
//    private LayoutInflater layoutInflater;
//    private ConstraintLayout conGoodsHead;
//    private android.widget.TextView goodsTitle;
//    private CornerImageView goodsImg;
//    private android.widget.TextView goodsName, goodsCount;
//    private android.widget.TextView goodsTip;
//    private android.widget.TextView moneyFlag;
//    private android.widget.TextView moneyValue;
//    private android.view.View conGoodsEnd;
//    private android.widget.LinearLayout conZxing;
//    private com.healthy.library.watcher.AlphaTextView tvZxing;
//    private android.widget.TextView zxingValue;
//    private android.widget.ImageView imgZxing, img_back;
//    private android.widget.TextView tipZxing;
//    private android.view.View conZxingEnd;
//    private android.widget.TextView moreZxing;
//    private OrderDetailModel.OrderGood mdetailModel;
//    private List<OrderDetailModel.Ticket> imgsnouse = new ArrayList<>();
//    private List<OrderDetailModel.Ticket> imgsuse = new ArrayList<>();
//    private List<OrderDetailModel.Ticket> imgsback = new ArrayList<>();
//    private TopBar topBar;
//    private StatusLayout layoutStatus;
//    private ConstraintLayout conZxinglist;
//    private TextView zxingCount;
//    private TextView zxingTime;
//    private TextView back_zxing_count;
//    private LinearLayout llZxinglist;
//    private LinearLayout back_ll_zxinglist;
//    private View conZxinglistEnd;
//    private ImageTextView underview;
//    private CardView more_zxing_cardview;
//    private CardView back_cardView;
//    private boolean isShow = false;
//    private com.scwang.smart.refresh.layout.SmartRefreshLayout layoutRefresh;
//    //    private WriteRecevier recevier;
//    private List<OrderDetailModel.Ticket> resultList = new ArrayList();
//
//
//    @Override
//    protected void findViews() {
//        super.findViews();
//        initView();
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
////        recevier = new WriteRecevier();
////        IntentFilter filter = new IntentFilter();
////        filter.addAction(WriteRecevier.TAG);
////        registerReceiver(recevier, filter);
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.mine_activity_order_zxing;
//    }
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//        ARouter.getInstance().inject(this);
//        mPresenter = new OrderZxingDetailPresenter(mContext, this);
//        layoutInflater = LayoutInflater.from(this);
//        layoutRefresh.setOnRefreshLoadMoreListener(this);
//        layoutRefresh.setEnableLoadMore(false);
//        getData();
//        more_zxing_cardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isShow) {
//                    isShow = true;
//                } else {
//                    isShow = false;
//                }
//                buildData();
//            }
//        });
//    }
//
//    @Override
//    public void getData() {
//        super.getData();
//        mPresenter.getOrderDetail(orderId);
//    }
//
//    @Override
//    public void onGetOrderDetailSuccess(OrderDetailModel.OrderGood detailModel) {
//        mdetailModel = detailModel;
//        buildData();
//    }
//
//    private void buildData() {
//        imgsuse.clear();
//        imgsnouse.clear();
//        imgsback.clear();
//        if (mdetailModel.ticketList != null && mdetailModel.ticketList.size() > 0) {
//            if (mdetailModel.ticketList.size() > 1) {
//                resultList.clear();
//                List<OrderDetailModel.Ticket> list = new ArrayList();
//                for (int i = 0; i < mdetailModel.ticketList.size(); i++) {
//                    if (mdetailModel.ticketList.get(i).isRefund == 0 && mdetailModel.ticketList.get(i).isUse == 0) {
//                        list.add(mdetailModel.ticketList.get(i));
//                        break;
//                    }
//                }
//                if (list.size() == 1) {
//                    if (!isShow) {
//                        resultList.add(list.get(0));
//                        underview.setText("剩余" + (mdetailModel.ticketList.size() - 1) + "个核销码");
//                    } else {
//                        resultList.addAll(mdetailModel.ticketList);
//                        underview.setText("收起");
//                    }
//                    more_zxing_cardview.setVisibility(View.VISIBLE);
//                } else {
//                    more_zxing_cardview.setVisibility(View.GONE);
//                }
//                for (int i = 0; i < resultList.size(); i++) {
//                    OrderDetailModel.Ticket ticket = resultList.get(i);
//                    if (ticket.isUse == 1) {
//                        imgsuse.add(ticket);
//                    } else {
//                        imgsnouse.add(ticket);
//                    }
//                    if (ticket.isRefund == 1) {
//                        imgsback.add(ticket);
//                    }
//                }
//
//            } else {
//                more_zxing_cardview.setVisibility(View.GONE);
//                for (int i = 0; i < mdetailModel.ticketList.size(); i++) {
//                    OrderDetailModel.Ticket ticket = mdetailModel.ticketList.get(i);
//                    if (ticket.isUse == 1) {
//                        imgsuse.add(ticket);
//                    } else {
//                        imgsnouse.add(ticket);
//                    }
//                    if (ticket.isRefund == 1) {
//                        imgsback.add(ticket);
//                    }
//                }
//            }
//        }
//        if (imgsuse.size() > 0) {
//            zxingCount.setText("已使用  数量  " + imgsuse.size());
//            conZxinglist.setVisibility(View.VISIBLE);
//            conZxinglistEnd.setVisibility(View.VISIBLE);
//        } else {
//            conZxinglist.setVisibility(View.GONE);
//            conZxinglistEnd.setVisibility(View.GONE);
//        }
//        if (imgsback.size() > 0) {
//            back_zxing_count.setText("已退订  数量  " + imgsback.size());
//            back_cardView.setVisibility(View.VISIBLE);
//        } else {
//            back_cardView.setVisibility(View.GONE);
//        }
//
//        com.healthy.library.businessutil.GlideCopy.with(this)
//                .load(mdetailModel.goodsImage)
//                .placeholder(R.drawable.img_1_1_default)
//                .error(R.drawable.img_1_1_default)
//                
//                .into(goodsImg);
//        goodsName.setText(mdetailModel.goodsTitle + "");
//        goodsCount.setText("x" + mdetailModel.goodsNumber);
//        if (mdetailModel.expiredTime == null || "null".equals(mdetailModel.expiredTime)) {
//            goodsTip.setText(mdetailModel.description);
//            moneyValue.setText("¥" + StringUtils.getResultPrice(mdetailModel.goodsPrice + ""));
//        } else {
//            zxingTime.setText("有效期至 " + mdetailModel.expiredTime);
//            goodsTip.setText("有效期至 " + mdetailModel.expiredTime);
//            moneyValue.setText("数量 " + mdetailModel.getNowTicketSize() + "");
//        }
//
//        buildZxingQcodeList();
//        buildZxingList();
//        buildBackList();
//    }
//
//    private void buildZxingList() {
//
//        llZxinglist.removeAllViews();
//        for (int i = 0; i < imgsuse.size(); i++) {
//            OrderDetailModel.Ticket ticket = imgsuse.get(i);
//
//
//            View view = layoutInflater.inflate(R.layout.mine_item_zxing, null);
//
//            TextView zxing_value = view.findViewById(R.id.zxing_value);
//            if (!TextUtils.isEmpty(ticket.ticketNo)){
//                String bankCard = ticket.ticketNo;
//                String str = "";
//                str = bankCard.substring(0, 4) + " " + bankCard.substring(4, bankCard.length());
//                zxing_value.setText(str);
//                zxing_value.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                zxing_value.setTextColor(Color.parseColor("#ff9596a4"));
//            }
//            TextView tv_zxing = view.findViewById(R.id.tv_zxing);
//            tv_zxing.setBackgroundResource(R.drawable.mine_shape_payno);
//            llZxinglist.addView(view);
//
//        }
//    }
//
//
//    private void buildZxingQcodeList() {
//        conZxing.removeAllViews();
//        for (int i = 0; i < imgsnouse.size(); i++) {
//            OrderDetailModel.Ticket ticket = imgsnouse.get(i);
//            if (ticket.qrcodeBase64 != null) {
//                View view = layoutInflater.inflate(R.layout.mine_item_zxingimg, null);
//
//                TextView tv_zxing = view.findViewById(R.id.zxing_value);
//                if (!TextUtils.isEmpty(ticket.ticketNo)){
//                    String bankCard = ticket.ticketNo;
//                    String str = "";
//                    str = bankCard.substring(0, 4) + " " + bankCard.substring(4, bankCard.length());
//                    tv_zxing.setText(str);
//                }
//                Bitmap zxing = new ImageUtilK().base64ToBitmap(ticket.qrcodeBase64);
//                ImageView img_zxing = view.findViewById(R.id.img_zxing);
//                img_zxing.setImageBitmap(zxing);
//
//                conZxing.addView(view);
//            }
//
//
//        }
//    }
//
//    private void buildBackList() {
//        back_ll_zxinglist.removeAllViews();
//        for (int i = 0; i < imgsback.size(); i++) {
//            OrderDetailModel.Ticket ticket = imgsback.get(i);
//
//
//            View view = layoutInflater.inflate(R.layout.mine_item_zxing, null);
//
//            TextView zxing_value = view.findViewById(R.id.zxing_value);
//            if (!TextUtils.isEmpty(ticket.ticketNo)){
//                String bankCard = ticket.ticketNo;
//                String str = "";
//                str = bankCard.substring(0, 4) + " " + bankCard.substring(4, bankCard.length());
//                zxing_value.setText(str);
//                zxing_value.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                zxing_value.setTextColor(Color.parseColor("#ff9596a4"));
//            }
//            TextView tv_zxing = view.findViewById(R.id.tv_zxing);
//            tv_zxing.setBackgroundResource(R.drawable.mine_shape_payno);
//            back_ll_zxinglist.addView(view);
//
//        }
//    }
//
//    private void initView() {
//        conGoodsHead = (ConstraintLayout) findViewById(R.id.con_goods_head);
//        goodsTitle = (TextView) findViewById(R.id.goods_title);
//        goodsImg = (CornerImageView) findViewById(R.id.goods_img);
//        goodsName = (TextView) findViewById(R.id.goods_name);
//        goodsTip = (TextView) findViewById(R.id.goods_tip);
//        moneyFlag = (TextView) findViewById(R.id.money_flag);
//        moneyValue = (TextView) findViewById(R.id.money_value);
//        conGoodsEnd = (View) findViewById(R.id.con_goods_end);
//        conZxing = (LinearLayout) findViewById(R.id.con_zxing);
//        tvZxing = (AlphaTextView) findViewById(R.id.tv_zxing);
//        zxingValue = (TextView) findViewById(R.id.zxing_value);
//        imgZxing = (ImageView) findViewById(R.id.img_zxing);
//        img_back = (ImageView) findViewById(R.id.img_back);
//        tipZxing = (TextView) findViewById(R.id.tip_zxing);
//        conZxingEnd = (View) findViewById(R.id.con_zxing_end);
//        moreZxing = (TextView) findViewById(R.id.more_zxing);
//        topBar = (TopBar) findViewById(R.id.top_bar);
//        layoutStatus = (StatusLayout) findViewById(R.id.layout_status);
//        conZxinglist = (ConstraintLayout) findViewById(R.id.con_zxinglist);
//        zxingCount = (TextView) findViewById(R.id.zxing_count);
//        zxingTime = (TextView) findViewById(R.id.zxing_time);
//        goodsCount = (TextView) findViewById(R.id.goodsCount);
//        back_zxing_count = (TextView) findViewById(R.id.back_zxing_count);
//        llZxinglist = (LinearLayout) findViewById(R.id.ll_zxinglist);
//        back_ll_zxinglist = (LinearLayout) findViewById(R.id.back_ll_zxinglist);
//        conZxinglistEnd = (View) findViewById(R.id.con_zxinglist_end);
//        layoutRefresh = (SmartRefreshLayout) findViewById(R.id.layout_refresh);
//        more_zxing_cardview = (CardView) findViewById(R.id.more_zxing_cardview);
//        back_cardView = (CardView) findViewById(R.id.back_cardView);
//        underview = (ImageTextView) findViewById(R.id.underview);
//        img_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        unregisterReceiver(recevier);
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//
//    }
//
//    @Override
//    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//        getData();
//    }
//
//    @Override
//    public void onRequestFinish() {
//        super.onRequestFinish();
//        layoutRefresh.finishLoadMore();
//        layoutRefresh.finishRefresh();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateOrderInfo(UpdateOrderByInfo msg) {
//        getData();
//    }
//
//
////    private class WriteRecevier extends BroadcastReceiver {
////        public static final String TAG = "com.health.mime.WriteRecevier";
////
////        @Override
////        public void onReceive(Context context, Intent intent) {
////            String action = intent.getAction();
////            if (action.equals(TAG)) {
////                //说明获得到了核销状态改变
////            }
////        }
////    }
//}