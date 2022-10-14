package com.health.servicecenter.adapter;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.BackListModel;
import com.healthy.library.utils.FormatUtils;

public class OrderBackDetailTipsAdapter extends BaseAdapter<String> {

    public OrderBackDetailTipsAdapter() {
        this(R.layout.item_order_back_detial_tips);
    }

    public OrderBackDetailTipsAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView tv_tips_txt = holder.getView(R.id.tv_tips_txt);
        SpannableString tipsTxt = new SpannableString("退款提示：" + " 请您耐心等待商家退款处理，通常情况下，退款会原路退回您的支付账户，商家同意退款后，预计会在3-5个工作日内到账。");
        tipsTxt.setSpan(new AbsoluteSizeSpan(13, true), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tipsTxt.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tipsTxt.setSpan(new AbsoluteSizeSpan(12, true), 6, tipsTxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tipsTxt.setSpan(new StyleSpan(Typeface.NORMAL), 6, tipsTxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_tips_txt.setText(tipsTxt);
    }
}
