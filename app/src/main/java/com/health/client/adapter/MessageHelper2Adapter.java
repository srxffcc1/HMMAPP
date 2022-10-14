package com.health.client.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.client.R;
import com.health.client.model.MonMessageHelper;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.routes.MineRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.DateUtils;
import com.healthy.library.widget.CornerImageView;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class MessageHelper2Adapter extends BaseQuickAdapter<MonMessageHelper, BaseViewHolder> {


    String type;

    public MessageHelper2Adapter() {
        this(R.layout.home_item_activity_messagehelper2);
    }

    private MessageHelper2Adapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MonMessageHelper item) {

        ConstraintLayout hasContent;
        TextView helpTime;
        TextView tipTitle;
        CornerImageView tipImg;
        TextView tipContent;
        LinearLayout tipCanPass;

        hasContent = (ConstraintLayout) helper.getView(R.id.hasContent);
        helpTime = (TextView) helper.getView(R.id.helpTime);
        tipTitle = (TextView) helper.getView(R.id.tipTitle);
        tipImg = (CornerImageView) helper.getView(R.id.tipImg);
        tipContent = (TextView) helper.getView(R.id.tipContent);
        tipCanPass = (LinearLayout) helper.getView(R.id.tipCanPass);

        tipContent.setText(item.content);
        if (TextUtils.isEmpty(item.topOrder)) {
            tipTitle.setText("您的服务订单");
        } else {
            tipTitle.setText(item.topOrder);
        }
        if (item.content.contains("优惠券")) {
            tipTitle.setText("您的优惠券");

        }
        helpTime.setText(DateUtils.getClassDate(item.createTime));
        if (!TextUtils.isEmpty(item.goodsImage)) {
            tipImg.setVisibility(View.VISIBLE);
            com.healthy.library.businessutil.GlideCopy.with(tipImg.getContext())
                    .asBitmap()
                    .load(item.goodsImage)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_avatar_default)
                    
                    .into(tipImg);
        } else {
            tipImg.setVisibility(View.GONE);
        }
        tipCanPass.setVisibility(View.VISIBLE);
        if (item.topOrder != null) {
            if (item.topOrder.contains("售后")) {
                if (item.topOrder.contains("退款成功")) {
                    tipCanPass.setVisibility(View.GONE);
                }
            }
        }

        if ("母婴服务小助手".equals(type)) {
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tipCanPass.getVisibility() == View.GONE) {
                        return;
                    }
                    if (item.content != null && item.content.contains("优惠") || item.topOrder.contains("礼包") || item.topOrder.contains("优惠")) {
                        ARouter.getInstance().build(DiscountRoutes.DIS_COUPONLIST)
                                .withString("tabIndex", "1")
                                .navigation();
                    } else if (item.topOrder != null && item.topOrder.contains("售后")) {
                        if ("1".equals(item.orderRace)) {
                            ARouter.getInstance()
                                    .build(MineRoutes.MINE_ORDER_BACK_DETAIL)
                                    .withString("refundId", item.resourceId + "")
                                    .navigation();
                        } else {
                            if (item.topOrder.contains("退款成功")) {
                                return;
                            }
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_ORDERBACKDETIAL)
                                    .withString("refundId", item.resourceId + "")
                                    .navigation();
                        }
                    } else {
                        if (TextUtils.isEmpty(item.resourceId)) {
                            Toast.makeText(mContext, "订单已删除", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(item.orderRace)) {
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_ORDERLISTDETIAL)
                                    .withString("orderId", item.resourceId + "")
                                    .withString("function", "25006")
                                    .navigation();
                        } else {
                            ARouter.getInstance()
                                    .build(ServiceRoutes.SERVICE_ORDERLISTDETIAL)
                                    .withString("orderId", item.resourceId + "")
                                    .withString("function", "25006")
                                    .navigation();

                        }

                    }

                }
            });
        }
    }

    private void initView() {

    }

    public void setType(String type) {
        this.type = type;
    }
}
