package com.health.servicecenter.adapter;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.health.servicecenter.model.OrderDetailModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.TimestampUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.lihang.ShadowLayout;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ServiceOrderDetialTopAdapter extends BaseAdapter<OrderDetailModel> {

    int mSeconds = 0;
    CountDownTimer countDownTimer;

    public ServiceOrderDetialTopAdapter() {
        this(R.layout.item_service_order_detial_top_adapter);//待付款//待使用//已取消//已完成
    }

    public ServiceOrderDetialTopAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseHolder holder, int position) {
        ConstraintLayout combin_cardview_to_home = (ConstraintLayout) holder.getView(R.id.combin_cardview_to_home);
        RelativeLayout no_pay_title_rel = holder.getView(R.id.no_pay_title_rel);
        RelativeLayout extracting_title_rel = holder.getView(R.id.extracting_title_rel);
        RelativeLayout finish_title_rel = holder.getView(R.id.finish_title_rel);
        RelativeLayout cancle_title_rel = holder.getView(R.id.cancle_title_rel);
        RelativeLayout no_receipt_title_rel = holder.getView(R.id.no_receipt_title_rel);
        ImageTextView cancle_title = holder.getView(R.id.cancle_title);
        ImageView combin_goodsLimitUnder = holder.getView(R.id.combin_goodsLimitUnder);
        TextView receipt_content = holder.getView(R.id.receipt_content);
        TextView finish_title_txt = holder.getView(R.id.finish_title_txt);
        ShadowLayout goodsShadowLayout = holder.getView(R.id.goodsShadowLayout);
        final TextView clock_time = holder.getView(R.id.clock_time);
        TextView extracting_title = holder.getView(R.id.extracting_title);
        TextView goodsTitle = holder.getView(R.id.goodsTitle);
        TextView goodsQuilty = holder.getView(R.id.goodsQuilty);
        TextView goodsPrice = holder.getView(R.id.goodsPrice);
        CornerImageView goodsImg = holder.getView(R.id.goodsImg);
        OrderDetailModel model = getModel();
        if (model.status == 0) {//未支付
            no_pay_title_rel.setVisibility(View.VISIBLE);
            extracting_title_rel.setVisibility(View.GONE);
            finish_title_rel.setVisibility(View.GONE);
            cancle_title_rel.setVisibility(View.GONE);
            no_receipt_title_rel.setVisibility(View.GONE);
            if (countDownTimer != null) {
                countDownTimer.cancel();
            } else {
                String pattern = "yyyy-MM-dd HH:mm:ss";
                long totalTime = 24 * 60 * 60 * 1000L;
                mSeconds = (int) ((totalTime - (TimestampUtils.string2Date(new SimpleDateFormat(pattern).format(new Date()),
                        pattern).getTime() - TimestampUtils.string2Date(model.createTime, pattern).getTime())) / 1000);
            }
            if (mSeconds > 0) {
                countDownTimer = new CountDownTimer(mSeconds * 1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        millisUntilFinished = millisUntilFinished / 1000;
                        mSeconds = (int) millisUntilFinished;
                        int hours = (int) (millisUntilFinished / (60 * 60));
                        int minutes = (int) millisUntilFinished / 60 % 60;
                        int seconds = (int) millisUntilFinished % 60;

                        clock_time.setText(String.format("%02d", Math.max(0, hours)) + ":"
                                + String.format("%02d", Math.max(0, minutes)) + ":"
                                + String.format("%02d", Math.max(0, seconds)));
                    }

                    public void onFinish() {
                        //剩余支付时间结束后进行相应逻辑处理
                        if (moutClickListener != null) {
                            moutClickListener.outClick("刷新", null);
                        }
                    }
                }.start();
            } else {
                // 0
                if (moutClickListener != null) {
                    moutClickListener.outClick("刷新", null);
                }
            }
        } else if (model.status == 1) {//待使用
            no_pay_title_rel.setVisibility(View.GONE);
            extracting_title_rel.setVisibility(View.VISIBLE);
            finish_title_rel.setVisibility(View.GONE);
            cancle_title_rel.setVisibility(View.GONE);
            no_receipt_title_rel.setVisibility(View.GONE);
            extracting_title.setText("待使用");
        } else if (model.status == 2) {//已完成
            no_pay_title_rel.setVisibility(View.GONE);
            extracting_title_rel.setVisibility(View.GONE);
            finish_title_rel.setVisibility(View.VISIBLE);
            cancle_title_rel.setVisibility(View.GONE);
            no_receipt_title_rel.setVisibility(View.GONE);
            extracting_title.setText("待使用");
            if (model.commentStatus == 0) {//未评价
                finish_title_txt.setVisibility(View.VISIBLE);
            } else {//已评价
                finish_title_txt.setVisibility(View.GONE);
            }

        } else if (model.status == 3) {
            no_pay_title_rel.setVisibility(View.GONE);
            extracting_title_rel.setVisibility(View.GONE);
            finish_title_rel.setVisibility(View.GONE);
            cancle_title_rel.setVisibility(View.VISIBLE);
            no_receipt_title_rel.setVisibility(View.GONE);
            cancle_title.setText("已取消");
        } else if (model.status == 4) {
            no_pay_title_rel.setVisibility(View.GONE);
            extracting_title_rel.setVisibility(View.GONE);
            finish_title_rel.setVisibility(View.GONE);
            cancle_title_rel.setVisibility(View.VISIBLE);
            no_receipt_title_rel.setVisibility(View.GONE);
            cancle_title.setText("已退订");
        }

        if (model.orderGoodsList != null) {
            final OrderDetailModel.OrderGood listModel = model.orderGoodsList.get(0);
            if (listModel != null) {
                goodsTitle.setText(listModel.goodsTitle);
                goodsQuilty.setText("x" + listModel.goodsNumber);
                goodsPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(listModel.goodsPrice));
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(listModel.goodsImage)
                        .placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.img_1_1_default)
                        .into(goodsImg);
                goodsShadowLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance()
                                .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                                .withString("goodsId", listModel.goodsId + "")
                                .navigation();
                    }
                });
            }
        }

    }

}
