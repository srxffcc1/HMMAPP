//package com.health.servicecenter.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.bumptech.glide.Glide;
//import com.health.servicecenter.R;
//import com.healthy.library.model.StoreListModel;
//import com.healthy.library.routes.ServiceRoutes;
//import com.healthy.library.utils.ParseUtils;
//import com.healthy.library.widget.CornerImageView;
//import com.healthy.library.widget.RatingView;
//import com.umeng.analytics.MobclickAgent;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class MallStoreNearbyListAdapter extends RecyclerView.Adapter<MallStoreNearbyListAdapter.MallStoreNearbyListHolder> {
//    private Context mContext;
//    List<StoreListModel> data = new ArrayList<>();
//
//    public MallStoreNearbyListAdapter(Context mContext, List<StoreListModel> listModels) {
//        this.mContext = mContext;
//        this.data = listModels;
//    }
//
//    @NonNull
//    @Override
//    public MallStoreNearbyListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mall_store_layout, parent, false);
//        return new MallStoreNearbyListHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MallStoreNearbyListHolder holder, final int position) {
//
//        if (position == 0) {
//            if (data.get(position).getDistance() / 1000 < 10) {
//                holder.tv_distance.setVisibility(View.VISIBLE);
//                holder.tv_distance.setText("离我最近");
//            } else {
//                holder.tv_distance.setVisibility(View.GONE);
//            }
//        } else {
//            holder.tv_distance.setText("");
//            holder.tv_distance.setVisibility(View.GONE);
//        }
//        View tv_video_flag=holder.itemView.findViewById(R.id.tv_video_flag);
//        tv_video_flag.setVisibility(View.GONE);
//        if(data.get(position).courseFlag==2){
//            tv_video_flag.setVisibility(View.VISIBLE);
//        }
//        com.healthy.library.businessutil.GlideCopy.with(mContext)
//                .load(data.get(position).getFilePath())
//                .into(holder.img);
//        holder.tv_product_name.setText(data.get(position).getShopName());
//        holder.tv_product_price_prefix.setText(data.get(position).getShopAddress());
//        holder.ratingView.setRating(data.get(position).getScore());
//        holder.tv_product_effect.setText(data.get(position).getScore() + "");
//        holder.tv_product_distence.setText(ParseUtils.parseDistance(data.get(position).getDistance() + ""));
//        holder.card_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Map nokmap = new HashMap<String, String>();
//                nokmap.put("soure","母婴商品页“实体门店\"栏门店");
//                MobclickAgent.onEvent(mContext, "btn_APP__MaternalandChildGoods_StoreList",nokmap);
//                ARouter.getInstance()
//                        .build(ServiceRoutes.SERVICE_NEARBYSTORES)
//                        .withString("shopId", data.get(position).getShopId()+"")
//                        .navigation();
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        if (data == null) {
//            return 0;
//        } else if (data.size() > 3) {
//            return 3;
//        } else {
//            return data.size();
//        }
//    }
//
//    public class MallStoreNearbyListHolder extends RecyclerView.ViewHolder {
//        private CornerImageView img;
//        private TextView tv_distance, tv_product_name, tv_product_price_prefix, tv_product_effect, tv_product_distence;
//        private CardView card_view;
//        private RatingView ratingView;
//
//        public MallStoreNearbyListHolder(@NonNull View itemView) {
//            super(itemView);
//            img = itemView.findViewById(R.id.iv_shop);
//            tv_distance = itemView.findViewById(R.id.tv_distance);
//            card_view = itemView.findViewById(R.id.card_view);
//            tv_product_name = itemView.findViewById(R.id.tv_product_name);
//            tv_product_price_prefix = itemView.findViewById(R.id.tv_product_price_prefix);
//            ratingView = itemView.findViewById(R.id.rating_customer_score);
//            tv_product_effect = itemView.findViewById(R.id.tv_product_effect);
//            tv_product_distence = itemView.findViewById(R.id.tv_product_distence);
//        }
//    }
//}
