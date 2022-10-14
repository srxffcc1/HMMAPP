package com.health.second.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.lib_ShapeView.layout.ShapeConstraintLayout;
import com.health.second.R;
import com.healthy.library.businessutil.GlideCopy;
import com.healthy.library.routes.LibraryRoutes;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建日期：2021/10/15 10:34
 *
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.second.adapter
 * 类说明：
 */

public class BannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String[] items;
    private Context context;

    public void setData(String[] items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_image_layout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        CardViewHolder cardViewHolder = (CardViewHolder) holder;
        GlideCopy.with(context)
                .load(items[position])
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into(cardViewHolder.shopImg);
        cardViewHolder.imgNum.setText(items.length + "");
        if (position == 0) {
            cardViewHolder.numLayout.setVisibility(View.VISIBLE);
        } else {
            cardViewHolder.numLayout.setVisibility(View.GONE);
        }
        cardViewHolder.shopImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "门店详情页商品图片/标题的点击量");
                MobclickAgent.onEvent(context, "event2APPShopDetialImgClick", nokmap);
                ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                        .withCharSequenceArray("urls", items)
                        .withInt("pos", position)
                        .navigation();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.length : 0;
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        private final ImageView shopImg;
        private final TextView imgNum;
        private final ShapeConstraintLayout numLayout;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            imgNum = itemView.findViewById(R.id.imgNum);
            shopImg = itemView.findViewById(R.id.shopImg);
            numLayout = itemView.findViewById(R.id.numLayout);
        }
    }
}
