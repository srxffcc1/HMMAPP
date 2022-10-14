package com.healthy.library.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.healthy.library.R;
import com.healthy.library.model.PostActivityList;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.utils.FormatUtils;


public class PostCouponAdapter extends BaseAdapter<PostActivityList.ActivityCoupon> {
    public PostCouponAdapter() {
        this(R.layout.item_post_coupon_adapter_layout);
    }

    public PostCouponAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private OnClickListener onClickListener;

    public void setClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(String couponId, String activityId);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        LinearLayout bgLiner;
        TextView couponMoney;
        TextView couponType;
        TextView couponContent;
        TextView couponTime;
        TextView receive;
        bgLiner = (LinearLayout) holder.getView(R.id.bgLiner);
        couponMoney = (TextView) holder.getView(R.id.couponMoney);
        couponType = (TextView) holder.getView(R.id.couponType);
        couponContent = (TextView) holder.getView(R.id.couponContent);
        couponTime = (TextView) holder.getView(R.id.couponTime);
        receive = (TextView) holder.getView(R.id.receive);
        final PostActivityList.ActivityCoupon bean = getDatas().get(position);
        bgLiner.setBackground(context.getResources().getDrawable(R.drawable.post_coupon_dialog_item));
        receive.setBackground(context.getResources().getDrawable(R.drawable.shape_post_coupon_item_select));
        if (FormatUtils.moneyKeep2Decimals(bean.denomination).length() >= 4) {
            SpannableString spannableString = new SpannableString(FormatUtils.moneyKeep2Decimals(bean.denomination) + "元");
            spannableString.setSpan(new AbsoluteSizeSpan(28, true), 0, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(12, true), spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗
            spannableString.setSpan(new StyleSpan(Typeface.NORMAL), spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
            couponMoney.setText(spannableString);
        } else {
            SpannableString spannableString = new SpannableString(FormatUtils.moneyKeep2Decimals(bean.denomination) + "元");
            spannableString.setSpan(new AbsoluteSizeSpan(30, true), 0, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(12, true), spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗
            spannableString.setSpan(new StyleSpan(Typeface.NORMAL), spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
            couponMoney.setText(spannableString);
        }
        couponType.setText(bean.getCouponTypeName());
        couponContent.setText(bean.getRequirement());
        couponTime.setText(bean.getTimeValidity());
        if (bean.isCanReceive()) {
            bgLiner.setBackground(context.getResources().getDrawable(R.drawable.post_coupon_dialog_item));
            receive.setBackground(context.getResources().getDrawable(R.drawable.shape_post_coupon_item_select));
            couponMoney.setTextColor(Color.parseColor("#fff08957"));
            couponType.setTextColor(Color.parseColor("#fff08957"));
            couponContent.setTextColor(Color.parseColor("#fff08957"));
            couponTime.setTextColor(Color.parseColor("#fff08957"));
            receive.setTextColor(Color.parseColor("#ffffffff"));
            receive.setText("领取");
            receive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClick(bean.getCouponId(), bean.getActivityId());
                    }
                }
            });
        } else {
            bgLiner.setBackground(context.getResources().getDrawable(R.drawable.post_coupon_dialog_item_invalid));
            receive.setBackground(context.getResources().getDrawable(R.drawable.shape_post_coupon_item_unselect));
            couponMoney.setTextColor(Color.parseColor("#999999"));
            couponType.setTextColor(Color.parseColor("#999999"));
            couponContent.setTextColor(Color.parseColor("#999999"));
            couponTime.setTextColor(Color.parseColor("#999999"));
            receive.setTextColor(Color.parseColor("#999999"));
            if (bean.couponQuantity <= 0) {
                receive.setText("已领完");
            } else {
                receive.setText("已领取");
            }
        }
    }

}
