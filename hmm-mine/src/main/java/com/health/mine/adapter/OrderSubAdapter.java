package com.health.mine.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.mine.R;
import com.health.mine.model.OrderSubDetailModel;
import com.healthy.library.builder.SimpleCalendarBuilder;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.SpanUtils;
import com.healthy.library.watcher.AlphaTextView;
import com.healthy.library.widget.DrawableTextView;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Li
 * @date 2019/04/10 10:51
 * @des 订单
 */

public class OrderSubAdapter extends BaseQuickAdapter<OrderSubDetailModel, BaseViewHolder> {



    public void setOnSubOrderClicker(OnSubOrderClicker onSubOrderClicker) {
        this.onSubOrderClicker = onSubOrderClicker;
    }

    OnSubOrderClicker onSubOrderClicker;

    public OrderSubAdapter() {
        this(R.layout.mine_item_order_sub);
    }

    private OrderSubAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderSubDetailModel item) {
         ConstraintLayout layoutItem;
         DrawableTextView tvStoreName;
        DrawableTextView tvHomeName;
         TextView timeTitle;
         TextView time;
         TextView timeTip;
         TextView addressTitle;
         TextView address;
         ImageView addressMore;
         ImageView addressLoc;
         TextView teachTitle;
         TextView teach;
         TextView teachTip;
         View dividerBottom;
         final AlphaTextView tvCancel;
         TextView createTime;
         View addressLL=helper.itemView.findViewById(R.id.addressLL);

        createTime=(TextView) helper.itemView.findViewById(R.id.createTime);
        layoutItem = (ConstraintLayout) helper.itemView.findViewById(R.id.layout_item);
        tvStoreName = (DrawableTextView)helper.itemView. findViewById(R.id.tv_store_name);
        tvHomeName = (DrawableTextView)helper.itemView. findViewById(R.id.tv_home_name);
        timeTitle = (TextView) helper.itemView.findViewById(R.id.timeTitle);
        time = (TextView) helper.itemView.findViewById(R.id.time);
        timeTip = (TextView) helper.itemView.findViewById(R.id.timeTip);
        addressTitle = (TextView) helper.itemView.findViewById(R.id.addressTitle);
        address = (TextView) helper.itemView.findViewById(R.id.address);
        addressMore = (ImageView) helper.itemView.findViewById(R.id.addressMore);
        addressLoc = (ImageView) helper.itemView.findViewById(R.id.addressLoc);
        teachTitle = (TextView) helper.itemView.findViewById(R.id.teachTitle);
        teach = (TextView) helper.itemView.findViewById(R.id.teach);
        teachTip = (TextView) helper.itemView.findViewById(R.id.teachTip);
        dividerBottom = (View) helper.itemView.findViewById(R.id.divider_bottom);
        tvCancel = (AlphaTextView) helper.itemView.findViewById(R.id.tv_cancel);
        tvStoreName.setVisibility(View.INVISIBLE);
        tvHomeName.setVisibility(View.INVISIBLE);
        addressMore.setVisibility(View.GONE);
        addressLoc.setVisibility(View.GONE);
        timeTip.setVisibility(View.GONE);
        time.setText(DateUtils.getTimeFormat(item.serviceTime,"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm"));
        Date serviceTime = null;
        teach.setText(item.technicianName);
        if(TextUtils.isEmpty(item.technicianName)){
            teach.setText(SpanUtils.getBuilder(mContext,"不指定").setForegroundColor(Color.parseColor("#FFAB2D")).create());
        }
        tvCancel.setVisibility(View.VISIBLE);
        try {
            serviceTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.serviceTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        teachTip.setText("");
        if(DateUtils.isSameDate(new SimpleCalendarBuilder().build().getTime(),serviceTime)){
            timeTip.setVisibility(View.VISIBLE);
            timeTip.setText("(今天)");
        }
        if(DateUtils.isSameDate(new SimpleCalendarBuilder().add(Calendar.DAY_OF_MONTH, 1).build().getTime(),serviceTime)){
            timeTip.setVisibility(View.VISIBLE);
            timeTip.setText("(明天)");
        }
        if(DateUtils.isSameDate(new SimpleCalendarBuilder().add(Calendar.DAY_OF_MONTH, 2).build().getTime(),serviceTime)){
            timeTip.setVisibility(View.VISIBLE);
            timeTip.setText("(后天)");
        }
        if(item.appointmentType==1){
            if(TextUtils.isEmpty(item.chainShopName)){
                address.setText(item.shopName);
            }else {
                address.setText(item.shopName+"("+item.chainShopName+")");
            }
            addressMore.setVisibility(View.VISIBLE);
            addressLoc.setVisibility(View.VISIBLE);
            tvStoreName.setVisibility(View.VISIBLE);
        }else {
            if(!TextUtils.isEmpty(item.chainShopName)){
                teachTip.setText(item.shopName+"("+item.chainShopName+")"+"提供");
            }else {
                teachTip.setText(""+item.shopName+"提供");
            }
            address.setText(item.homeAddress);
            tvHomeName.setVisibility(View.VISIBLE);
        }
        if(item.appointmentStatus==1){
            tvCancel.setText("取消预约");
            if(serviceTime.before(new Date())){
                tvCancel.setText("删除");
            }
            if(serviceTime.getTime()>new Date().getTime()){
                if(serviceTime.getTime()-new Date().getTime()<(1000 * 60*30)){//小于30分钟了 离预约时间就不让退了
                    ////System.out.println("无效预约了");
                    tvCancel.setVisibility(View.GONE);
                }
            }

        }else {
            tvCancel.setText("删除");
        }
        createTime.setText("提交时间："+item.createTime);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(onSubOrderClicker!=null){



                    StyledDialog.init(mContext);
                    StyledDialog.buildIosAlert("", "\n确定要"+tvCancel.getText().toString()+"吗?", new MyDialogListener() {
                        @Override
                        public void onFirst() {

                        }

                        @Override
                        public void onThird() {
                            super.onThird();

                        }

                        @Override
                        public void onSecond() {
                            onSubOrderClicker.onSubClick(v,item.id+"", "取消预约".equals(tvCancel.getText().toString())?"2":"4");


                        }
                    }).setBtnColor(R.color.dialogutil_text_black, R.color.colorPrimaryDark,0).setBtnText("点错了", "确定").show();

                }
            }
        });
        addressLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.appointmentType==1){
                    NavigateUtils.navigate(mContext, item.homeAddress,
                            item.latitude+"", item.longitude+"");
                }
            }
        });



    }

    private void initView() {




    }

    public interface OnSubOrderClicker {
        void onSubClick(View view, String id, String appointmentStatus);
    }
}