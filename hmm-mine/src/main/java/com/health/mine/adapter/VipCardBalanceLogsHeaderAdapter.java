package com.health.mine.adapter;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.mine.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.BalanceModel;
import com.healthy.library.utils.FormatUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class VipCardBalanceLogsHeaderAdapter extends BaseAdapter<String> {

    public VipCardBalanceLogsHeaderAdapter() {
        this(R.layout.item_vip_balance_logs_top);
    }

    public VipCardBalanceLogsHeaderAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        TextView content = holder.getView(R.id.content);
        DecimalFormat df = new DecimalFormat(",###,##0.00");
        if (getModel() != null && !TextUtils.isEmpty(getModel())) {
            BigDecimal bd = new BigDecimal(getModel());
//            SpannableString balance = new SpannableString("¥" + df.format(bd));
//            balance.setSpan(new AbsoluteSizeSpan(16, true), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            balance.setSpan(new AbsoluteSizeSpan(32, true), 2, balance.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            balance.setSpan(new StyleSpan(Typeface.NORMAL), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //正常
//            balance.setSpan(new StyleSpan(Typeface.BOLD), 2, balance.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//加粗
            content.setText("¥" + df.format(bd));
        } else {
            content.setText("￥0.00");
        }
    }

}
