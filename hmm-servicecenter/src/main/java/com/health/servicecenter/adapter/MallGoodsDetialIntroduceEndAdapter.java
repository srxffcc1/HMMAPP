package com.health.servicecenter.adapter;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.model.GoodsDetail;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.utils.SpanUtils;

public class MallGoodsDetialIntroduceEndAdapter extends BaseAdapter<GoodsDetail> {

    private SpannableStringBuilder spannableStringBuilder;
    boolean needShowBottom=false;

    public void setNeedShowBottom(boolean needShowBottom) {
        this.needShowBottom = needShowBottom;
    }

    @Override
    public int getItemViewType(int position) {
        return 11;
    }

    public MallGoodsDetialIntroduceEndAdapter() {
        this(R.layout.service_item_goodsdetail_introduce_b);
        spannableStringBuilder = SpanUtils.getBuilder(context, "划线价格：").setForegroundColor(Color.parseColor("#ff444444")).setBold()
                .append("商品的专柜价、吊牌价、正品零售价、厂商指导价或该商品的曾经展示过的销售价等，").setForegroundColor(Color.parseColor("#ff444444"))
                .append("并非原价").setForegroundColor(Color.parseColor("#fff02846"))
                .append("，仅供参考。若商家单独对划线价格进行说明的，以商家的表述为准。\n\n").setForegroundColor(Color.parseColor("#ff444444"))
                .append("未划线价格：").setForegroundColor(Color.parseColor("#ff444444")).setBold()
                .append("商品的").setForegroundColor(Color.parseColor("#ff444444"))
                .append("实时标价").setForegroundColor(Color.parseColor("#fff02846"))
                .append("，不因表述的差异改变性质。具体成交价格根据商品参加活动，或会员使用优惠劵等发生变化，最终以订单结算页价格为准。\n\n").setForegroundColor(Color.parseColor("#ff444444"))
                .append("商品详情页（含主图）以图片或文字形式标注的一口价、促销价、优惠价等价格可能是在使用优惠券、满减或特定优惠活动和时段等情形下的价格，具体请以结算的页面标价、优惠条件或活动规则为准。\n").setForegroundColor(Color.parseColor("#ff444444"))

                .create();
    }

    private MallGoodsDetialIntroduceEndAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        GoodsDetail goodsDetail = getModel();
        ConstraintLayout imgLL;
        ConstraintLayout imgLLTop;
        WebView imgLLDetail;
        ConstraintLayout moneyLL;
        ConstraintLayout moneyLLTop;
        TextView moneyTitle;
        TextView moneyValue;
        LinearLayout picLL;
        TextView moneyValueBottom=baseHolder.itemView.findViewById(R.id.moneyValueBottom);
        imgLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.imgLL);
        imgLLTop = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.imgLLTop);
        imgLLDetail = (WebView) baseHolder.itemView.findViewById(R.id.imgLLDetail);
        moneyLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.moneyLL);
        moneyLLTop = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.moneyLLTop);
        moneyTitle = (TextView) baseHolder.itemView.findViewById(R.id.moneyTitle);
        moneyValue = (TextView) baseHolder.itemView.findViewById(R.id.moneyValue);
        LinearLayout videoLL = baseHolder.itemView.findViewById(R.id.videoLL);
        picLL = baseHolder.itemView.findViewById(R.id.picLL);
        if(needShowBottom){
            moneyValueBottom.setVisibility(View.VISIBLE);
        }
        moneyValue.setText(spannableStringBuilder);

    }

}
