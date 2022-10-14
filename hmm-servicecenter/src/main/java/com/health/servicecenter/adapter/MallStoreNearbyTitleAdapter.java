//package com.health.servicecenter.adapter;
//
//import android.text.Html;
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//
//import com.alibaba.android.arouter.launcher.ARouter;
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.health.servicecenter.R;
//import com.healthy.library.base.BaseAdapter;
//import com.healthy.library.base.BaseHolder;
//import com.healthy.library.routes.ServiceRoutes;
//import com.umeng.analytics.MobclickAgent;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class MallStoreNearbyTitleAdapter extends BaseAdapter<String> {
//    public MallStoreNearbyTitleAdapter(int viewId) {
//        super(viewId);
//    }
//
//    public MallStoreNearbyTitleAdapter() {
//        this(R.layout.mall_top_store_layout);
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        return new LinearLayoutHelper();
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
//        TextView tv_title = holder.getView(R.id.tv_title);
//        TextView textViewEnd = holder.getView(R.id.textViewEnd);
//        tv_title.setText("实体门店");
//        textViewEnd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Map nokmap = new HashMap<String, String>();
//                nokmap.put("soure","母婴商品页“实体门店\"栏“>”");
//                MobclickAgent.onEvent(context, "btn_APP__MaternalandChildGoods_StoreList",nokmap);
//                ARouter.getInstance()
//                        .build(ServiceRoutes.SERVICE_NEARBYSTORES)
//                        .navigation();
//            }
//        });
//    }
//}
