//package com.health.servicecenter.adapter;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.health.servicecenter.R;
//import com.healthy.library.model.StoreListModel;
//import com.healthy.library.base.BaseAdapter;
//import com.healthy.library.base.BaseHolder;
//
//import java.util.ArrayList;
//
//public class MallStoreNearbyAdapter extends BaseAdapter<ArrayList<StoreListModel>> {
//    private MallStoreNearbyListAdapter adapter;
//
//    public MallStoreNearbyAdapter() {
//        this(R.layout.mall_nearby_recycle_layout);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
//        RecyclerView recyclerView = holder.getView(R.id.recycler_discount);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new MallStoreNearbyListAdapter(context, getDatas().get(0));
//        recyclerView.setAdapter(adapter);
//    }
//
//    public MallStoreNearbyAdapter(int viewId) {
//        super(viewId);
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        return new LinearLayoutHelper();
//    }
//}
