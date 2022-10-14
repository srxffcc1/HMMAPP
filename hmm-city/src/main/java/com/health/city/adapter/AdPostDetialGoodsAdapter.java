package com.health.city.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.city.R;
import com.healthy.library.model.PostImgDetial;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.widget.CornerImageView;


public class AdPostDetialGoodsAdapter extends BaseAdapter<PostImgDetial.PostingGoodsList> {
    public AdPostDetialGoodsAdapter() {
        this(R.layout.item_ad_post_goods_adapter_layout);
    }

    public AdPostDetialGoodsAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        CornerImageView goodsImg = (CornerImageView) holder.getView(R.id.goodsImg);
        TextView goodsMoney = (TextView) holder.getView(R.id.goodsMoney);
        final PostImgDetial.PostingGoodsList list = getDatas().get(position);
        if (list != null) {
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(list.goodsUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)
                    
                    .into(goodsImg);
            goodsMoney.setVisibility(View.VISIBLE);
            if (list.newGoodsDTO != null) {
                goodsMoney.setVisibility(View.VISIBLE);
                goodsMoney.setText("￥" + FormatUtils.moneyKeep2Decimals(list.newGoodsDTO.platformPrice != null ? list.newGoodsDTO.platformPrice : "0"));
            } else {
                goodsMoney.setVisibility(View.GONE);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.newGoodsDTO.differentSymbol == 1) {
                    ARouter.getInstance()
                            .build(SecondRoutes.SECOND_SERVICE_DETAIL)
                            .withString("id", list.goodsId)
                            .navigation();
                } else {
                    ARouter.getInstance()
                            .build(ServiceRoutes.SERVICE_DETAIL)
                            .withString("id", list.goodsId)
                            .navigation();
                }
            }
        });
    }
}
