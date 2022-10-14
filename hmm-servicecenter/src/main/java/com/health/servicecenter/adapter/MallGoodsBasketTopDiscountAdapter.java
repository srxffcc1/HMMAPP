package com.health.servicecenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.ActVip;
import com.healthy.library.widget.ImageTextView;
import com.tencent.bugly.proguard.A;

import java.util.Collections;
import java.util.List;

/**
 * 门店组
 */
public class MallGoodsBasketTopDiscountAdapter extends BaseAdapter<List<ActVip>> {

    int needsize = 1;
    boolean isshowALL = false;


    @Override
    public int getItemViewType(int position) {
        return 47;
    }

    public MallGoodsBasketTopDiscountAdapter() {
        this(R.layout.service_item_goodsbasket_topdiscount);
    }

    private MallGoodsBasketTopDiscountAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, final int i) {
        final LinearLayout discountTopLL;
        LinearLayout discountMoreLL;
        discountTopLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.discountTopLL);
        discountMoreLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.discountMoreLL);
        discountMoreLL.setVisibility(View.VISIBLE);
        final ImageTextView discountMoreImg = baseHolder.itemView.findViewById(R.id.discountMoreImg);
        discountMoreImg.setDrawable(R.drawable.discountdown, context);
        final ActVip actVip = getOnlyActAll(getModel());
//        actVip.setPopDetailFindInSales();
//        if(actVip.getSelPopInfo().size()>0){
//            if(actVip.getSelPopInfo().size()==actVip.PopInfo.size()){
//                //说明已选就是全部了 没必要展示这个按钮了
//                discountMoreLL.setVisibility(View.GONE);
//            }
//            if(isshowALL){
//                addFunctions(context, isshowALL?actVip.PopInfo.size():3, discountTopLL,actVip.PopInfo);
//                discountMoreImg.setText("收起");
//                discountMoreImg.setDrawable(R.drawable.discountup,context);
//            }else {
//                addFunctions(context, actVip.getSelPopInfo().size(), discountTopLL, actVip.getSelPopInfo());
//                discountMoreImg.setText("查看更多促销");
//                discountMoreImg.setDrawable(R.drawable.discountdown,context);
//            }
//        }else

        discountMoreLL.setVisibility(View.VISIBLE);
        {
            if (actVip.PopInfo.size() <= 3) {
                discountMoreLL.setVisibility(View.GONE);
            }
            discountMoreImg.setDrawable(R.drawable.discountdown, context);
            if (isshowALL) {
                addFunctions(context, isshowALL ? actVip.PopInfo.size() : 3, discountTopLL, actVip.PopInfo);
                discountMoreImg.setText("收起");
                discountMoreImg.setDrawable(R.drawable.discountup, context);
            } else {
                addFunctions(context, 3, discountTopLL, actVip.PopInfo);
                discountMoreImg.setText("查看更多促销");
                discountMoreImg.setDrawable(R.drawable.discountdown, context);
            }
        }
        discountMoreLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isshowALL = !isshowALL;
//                if(actVip.getSelPopInfo().size()>0){
//                    if(isshowALL){
//                        addFunctions(context, isshowALL?actVip.PopInfo.size():3, discountTopLL,actVip.PopInfo);
//                        discountMoreImg.setText("收起");
//                        discountMoreImg.setDrawable(R.drawable.discountup,context);
//                    }else {
//                        addFunctions(context, actVip.getSelPopInfo().size(), discountTopLL, actVip.getSelPopInfo());
//                        discountMoreImg.setText("查看更多促销");
//                        discountMoreImg.setDrawable(R.drawable.discountdown,context);
//                    }
//                }else
                {
                    if (isshowALL) {
                        addFunctions(context, isshowALL ? actVip.PopInfo.size() : 3, discountTopLL, actVip.PopInfo);
                        discountMoreImg.setText("收起");
                        discountMoreImg.setDrawable(R.drawable.discountup, context);
                    } else {
                        addFunctions(context, 3, discountTopLL, actVip.PopInfo);
                        discountMoreImg.setText("查看更多促销");
                        discountMoreImg.setDrawable(R.drawable.discountdown, context);
                    }
                }
            }
        });

    }

    private void addFunctions(final Context context, int needsize, final LinearLayout gridLayout, final List<ActVip.PopInfo> list) {
        gridLayout.removeAllViews();
        if (needsize > list.size()) {
            needsize = list.size();
        }
        for (int i = 0; i < needsize; i++) {
            final ActVip.PopInfo popInfo = list.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsbasket_topdiscount_item, gridLayout, false);
            View discountSingleMore;
            TextView discountContent;
            TextView discountTitle;
            discountSingleMore = (View) view.findViewById(R.id.discountSingleMore);
            discountContent = (TextView) view.findViewById(R.id.discountContent);
            discountTitle = (TextView) view.findViewById(R.id.discountTitle);
            discountSingleMore.setVisibility(View.VISIBLE);
            discountContent.setText(popInfo.PopDesc);
            discountTitle.setText(popInfo.PopLabelName);
            if (popInfo.PopDetail.size() > 0) {
                discountSingleMore.setVisibility(View.VISIBLE);
                discountSingleMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (moutClickListener != null) {
                            moutClickListener.outClick("查看更多", popInfo);
                        }
                    }
                });
            } else {
                discountSingleMore.setVisibility(View.GONE);
            }
            gridLayout.addView(view);
        }
    }

    private ActVip getOnlyActAll(List<ActVip> model) {
        ActVip actVip = new ActVip();
        for (int i = 0; i < model.size(); i++) {
            model.get(i).buildPopInfo();//把DetpId下放到PopInfo中
            actVip.PopInfo.addAll(model.get(i).getRealPopInfo());
        }
        Collections.sort(actVip.PopInfo);
        return actVip;
    }

    private void initView() {

    }
}
