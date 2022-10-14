//package com.health.index.adapter;
//
//import androidx.annotation.NonNull;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.bumptech.glide.Glide;
//import com.google.android.flexbox.FlexboxLayout;
//import com.health.index.R;
//import com.health.index.model.IndexDisCountShop;
//import com.healthy.library.base.BaseAdapter;
//import com.healthy.library.base.BaseHolder;
//import com.healthy.library.utils.ParseUtils;
//import com.healthy.library.utils.StringUtils;
//
//public class IndexGoodsAdapter extends BaseAdapter<IndexDisCountShop> {
//    @Override
//    public int getItemViewType(int position) {
//        return 3;
//    }
//    public IndexGoodsAdapter() {
//        this(R.layout.index_mon_goods);
//    }
//    private IndexGoodsAdapter(int viewId) {
//        super(viewId);
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        return new LinearLayoutHelper();
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int postion) {
//        final IndexDisCountShop indexDisCountShop= getDatas().get(postion);
//        FlexboxLayout tipTitle = (FlexboxLayout) baseHolder.getView(R.id.tipTitle);;
//        ImageView iv_shop=baseHolder.getView(R.id.iv_shop);
//
//        baseHolder.setText(R.id.tv_product_name, indexDisCountShop.shopName);
////        baseHolder.setText(R.id.tv_product_effect, indexDisCountShop.maxDiscountGoods.goodsTitle);
//        baseHolder.setText(R.id.tv_product_price, StringUtils.getResultPrice(indexDisCountShop.maxDiscountGoods.platformPrice.toString()));
//        baseHolder.setText(R.id.tv_distance, ParseUtils.parseDistance(indexDisCountShop.distance+""));
//        com.healthy.library.businessutil.GlideCopy.with(context)
//                .load(indexDisCountShop.maxDiscountGoods.headImages.get(0))
//                    .into(iv_shop);
//        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                moutClickListener.outClick("憨妈优选",indexDisCountShop);
//            }
//        });
//        indexDisCountShop.topicList.clear();
//        if(indexDisCountShop.distance<=3000){
//            indexDisCountShop.topicList.add("距离近");
//        }else {
//            indexDisCountShop.topicList.add("较优惠");
//        }
//        tipTitle.removeAllViews();
//        if(indexDisCountShop.topicList!=null&&indexDisCountShop.topicList.size()>0){
//            for (int i = 0; i <(indexDisCountShop.topicList.size()>=3?3:indexDisCountShop.topicList.size()) ; i++) {
//                View tipchild= LayoutInflater.from(context).inflate(R.layout.index_item_tip_goods,tipTitle,false);
//                TextView tipname=tipchild.findViewById(R.id.tipSmall);
//                tipname.setText(indexDisCountShop.topicList.get(i));
//                final int finalI = i;
//                tipTitle.addView(tipchild);
//            }
//
//        }
//
//    }
//}
