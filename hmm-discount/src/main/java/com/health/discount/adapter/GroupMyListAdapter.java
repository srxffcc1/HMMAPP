//package com.health.discount.adapter;
//
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.os.CountDownTimer;
//import android.util.SparseArray;
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.request.transition.Transition;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.health.discount.R;
//import com.healthy.library.model.KKGroup;
//import com.healthy.library.routes.DiscountRoutes;
//import com.healthy.library.routes.MallRoutes;
//import com.healthy.library.routes.MineRoutes;
//import com.healthy.library.routes.ServiceRoutes;
//import com.healthy.library.utils.DateUtils;
//import com.healthy.library.utils.DrawableUtils;
//import com.healthy.library.utils.StringUtils;
//import com.healthy.library.watcher.AlphaTextView;
//import com.healthy.library.widget.CornerImageView;
//import com.healthy.library.widget.DrawableTextView;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * @author Li
// * @date 2019/03/25 11:03
// * @des 推荐阅读
// */
//
//public class GroupMyListAdapter extends BaseQuickAdapter<KKGroup, BaseViewHolder> {
//
//    public String adCode;
//
//    public String lat;
//
//    public String lng;
//
//    GroupShareClicker groupShareClicker;
//
//    public void setGroupShareClicker(GroupShareClicker groupShareClicker) {
//        this.groupShareClicker = groupShareClicker;
//    }
//
//    private SparseArray<CountDownTimer> countDownCounters=new SparseArray<>();
//
//
//    public void setLocation(String adCode, String lat, String lng) {
//        this.adCode = adCode;
//        this.lat = lat;
//        this.lng = lng;
//    }
//
//    public GroupMyListAdapter() {
//        this(R.layout.dis_item_activity_group_list_mine);
//    }
//
//    private GroupMyListAdapter(int layoutResId) {
//        super(layoutResId);
//    }
//
//
//    @Override
//    protected void convert(final BaseViewHolder helper, final KKGroup item) {
//         TextView groupDay;
//         DrawableTextView groupLimit;
//         TextView groupStatus;
//         CornerImageView groupProduceImg;
//         TextView groupTitle;
//         TextView groupNum;
//         TextView groupStoreTip;
//         TextView groupStoreName;
//         AlphaTextView tvPay;
//         AlphaTextView tvCancel;
//         AlphaTextView tvCancelGoods;
//         TextView tvTotalPrice;
//
//
//
//
//        groupDay = (TextView) helper.itemView.findViewById(R.id.groupDay);
//        groupLimit = (DrawableTextView) helper.itemView.findViewById(R.id.groupLimit);
//        groupStatus = (TextView) helper.itemView.findViewById(R.id.groupStatus);
//        groupProduceImg = (CornerImageView) helper.itemView.findViewById(R.id.groupProduceImg);
//        groupTitle = (TextView) helper.itemView.findViewById(R.id.groupTitle);
//        groupNum = (TextView) helper.itemView.findViewById(R.id.groupNum);
//        groupStoreTip = (TextView) helper.itemView.findViewById(R.id.groupStoreTip);
//        groupStoreName = (TextView) helper.itemView.findViewById(R.id.groupStoreName);
//        tvPay = (AlphaTextView) helper.itemView.findViewById(R.id.tv_pay);
//        tvCancel = (AlphaTextView) helper.itemView.findViewById(R.id.tv_cancel);
//        tvCancelGoods = (AlphaTextView) helper.itemView.findViewById(R.id.tv_cancel_goods);
//        tvTotalPrice = (TextView) helper.itemView.findViewById(R.id.tv_total_price);
//        groupDay.setText(item.joinTime.split(" ")[0]);
//        CountDownTimer countDownTimer = countDownCounters.get(groupLimit.hashCode());
//        groupStatus.setText(item.statusStr);
//        groupTitle.setText(item.goodsTitle);
//        groupStoreName.setText(item.shopName);
//        groupNum.setText("x1");
//        tvTotalPrice.setText(StringUtils.getResultPrice(item.assemblePrice+""));
//        groupStatus.setTextColor(Color.parseColor("#FC2347"));
//
//        tvCancel.setVisibility(View.VISIBLE);
//        tvCancelGoods.setVisibility(View.VISIBLE);
//        com.healthy.library.businessutil.GlideCopy.with(mContext)
//                .load(item.goodsImage)
//                .placeholder(R.drawable.img_1_1_default2)
//                .error(R.drawable.img_1_1_default)
//
//                .into(groupProduceImg);
//
//        com.healthy.library.businessutil.GlideCopy.with(mContext).load(item.goodsImage)
//                .placeholder(R.drawable.img_1_1_default2)
//                .error(R.drawable.img_1_1_default)
//
//                .into(new SimpleTarget<Drawable>() {
//
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        item.bitmap= DrawableUtils.drawableToBitmap(resource);
//                    }
//                });
//
//
//
//        if("拼团中".equals(item.statusStr)){
//            checkTimeOut(countDownTimer,item,groupLimit,true);
//            tvCancel.setText("分享");
//            tvCancelGoods.setText("拼团详情");
//        }
//        if("拼团成功".equals(item.statusStr)){
//            checkTimeOut(countDownTimer,item,groupLimit,false);
//            tvCancel.setText("拼团详情");
//            tvCancelGoods.setText("订单详情");
//        }
//        if("拼团失败".equals(item.statusStr)){
//            checkTimeOut(countDownTimer,item,groupLimit,false);
//            tvCancel.setText("拼团详情");
//            groupStatus.setTextColor(Color.parseColor("#222222"));
//            tvCancelGoods.setVisibility(View.GONE);
//        }
//        helper.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ARouter.getInstance()
//                        .build(ServiceRoutes.SERVICE_DETAIL)
//                        .withString("cityNo",(Integer.parseInt(adCode) / 100 * 100)+"")
//                        .withString("areaNo",adCode)
//                        .withString("lng",lng)
//                        .withString("lat",lat)
//                        .withString("shopId",item.shopId+"")
//                        .withString("assembleId",item.assembleId)
//                        .navigation();
//            }
//        });
//        tvCancelGoods.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if("拼团详情".equals(((TextView)v).getText().toString())){
//                    ARouter.getInstance()
//                            .build(DiscountRoutes.DIS_GROUPDETAIL)
//                            .withString("orderId",item.orderId+"")
//                            .withString("teamNum",item.teamNum)
//                            .navigation();
//
//                }
//                if("分享".equals(((TextView)v).getText().toString())){
//                    if(groupShareClicker!=null){
//                        groupShareClicker.onGroupShareClick(v,item);
//                    }
//                }
//                if("订单详情".equals(((TextView)v).getText().toString())){
//                    ARouter.getInstance().build(ServiceRoutes.SERVICE_SERVICEORDERDETIAL)
//                            .withString("orderId", item.orderId+"")
//                            .navigation();
//                }
//
//            }
//        });
//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if("拼团详情".equals(((TextView)v).getText().toString())){
//                    ARouter.getInstance()
//                            .build(DiscountRoutes.DIS_GROUPDETAIL)
//                            .withString("teamNum",item.teamNum)
//                            .navigation();
//                }
//                if("分享".equals(((TextView)v).getText().toString())){
//                    if(groupShareClicker!=null){
//                        groupShareClicker.onGroupShareClick(v,item);
//                    }
//                }
//                if("订单详情".equals(((TextView)v).getText().toString())){
//                    ARouter.getInstance().build(ServiceRoutes.SERVICE_SERVICEORDERDETIAL)
//                            .withString("orderId", item.orderId+"")
//                            .navigation();
//                }
//
//            }
//        });
//    }
//
//
//    private void checkTimeOut(CountDownTimer countDownTimer,KKGroup url, final TextView destview,boolean needtimer) {
//        if(countDownTimer!=null){
//            countDownTimer.cancel();
//            countDownTimer=null;
//        }
//        if(needtimer){
//            Date startTime= null;
//            Date endTime= null;
//            try {
//                startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.regimentTime);
//                endTime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(url.endTime);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            long nd = 1000 * url.regimentTimeLength * 60 * 60;//加入时间之后需要多少小时
//            long desorg = startTime.getTime()+nd;
//            long timer = startTime.getTime()+nd;
//            if(endTime!=null&&endTime.getTime()<=timer){
//                timer=endTime.getTime();
//                desorg=endTime.getTime();
//            }
//            timer=timer-System.currentTimeMillis();
//
//            if (timer >0) {
//                //System.out.println("开始计时");
//                final long finalTimer = timer;
//                final long finalDesorg = desorg;
//                countDownTimer = new CountDownTimer(finalTimer, 1000) {
//                    public void onTick(long millisUntilFinished) {
//                        String array= DateUtils.getDistanceTimeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),DateUtils.formatLongAll(finalDesorg +""));
//                        destview.setText("倒计时 "+array+"");
//                    }
//                    public void onFinish() {
//                        destview.setText("过期");
//                    }
//                }.start();
//                countDownCounters.put(destview.hashCode(), countDownTimer);
//            } else {
//                destview.setText("过期");
//            }
//        }
//
//    }
//
//    private void initView() {
//
//
//    }
//    public interface GroupShareClicker{
//        void onGroupShareClick(View view,KKGroup kkGroup);
//    }
//}
