//package com.health.index.adapter;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.OrientationHelper;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.widget.ImageView;
//
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.health.index.R;
//import com.healthy.library.model.IndexMenu;
//import com.healthy.library.base.BaseAdapter;
//import com.healthy.library.base.BaseHolder;
//
//import java.util.ArrayList;
//
//public class IndexFunctionListAdapter extends BaseAdapter<ArrayList<IndexMenu>> {
//
//    private LinearLayoutManager layoutManager;
//
//    public void setTmprecyclerView(RecyclerView tmprecyclerView) {
//        this.tmprecyclerView = tmprecyclerView;
//    }
//
//    RecyclerView tmprecyclerView;
//    IndexFunctionAdapter indexFunctionAdapter;
//    @Override
//    public int getItemViewType(int position) {
//        return 2;
//    }
//    public IndexFunctionListAdapter() {
//        this(R.layout.index_mon_function_list);
//    }
//    private IndexFunctionListAdapter(int viewId) {
//        super(viewId);
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        return new LinearLayoutHelper();
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull final BaseHolder baseHolder, int i) {
//
//
//        RecyclerView recycler_fun=baseHolder.getView(R.id.recycler_fun);
//        layoutManager = new LinearLayoutManager(context, OrientationHelper.HORIZONTAL,false);
//        final ImageView recycler_scroller=baseHolder.getView(R.id.recycler_scroller);
//        recycler_fun.setLayoutManager(layoutManager);
////        PagerSnapHelper pagerSnapHelper=new PagerSnapHelper();
////
////        try {
////            recycler_fun.setOnFlingListener(null);
////            pagerSnapHelper.attachToRecyclerView(recycler_fun);
////        } catch (IllegalStateException e) {
////            e.printStackTrace();
////        }
//        if(indexFunctionAdapter==null){
//            indexFunctionAdapter=new IndexFunctionAdapter();
//        }
//        recycler_fun.setAdapter(indexFunctionAdapter);
//        indexFunctionAdapter.setData(getDatas().get(0));
//        indexFunctionAdapter.setOutClickListener(getMoutClickListener());
//        recycler_fun.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
//                if(firstCompletelyVisibleItemPosition<1){//第一屏幕
//                    recycler_scroller.setImageResource(R.drawable.main_scroller_l);
//                }else {
//                    recycler_scroller.setImageResource(R.drawable.main_scroller_r);
//                }
//            }
//        });
//
////        final IndexMenu indexMenu=getData().get(i);
////        final View parent_category=baseHolder.getView(R.id.parent_category);
////        com.healthy.library.businessutil.GlideCopy.with(context)
////                .load(indexMenu.imageRes)
////                .placeholder(R.drawable.img_avatar_default)
////                .error(R.drawable.img_avatar_default)
////                
////                .into((ImageView) baseHolder.getView(R.id.iv_category));
////
////        if(indexMenu.name.equals("专家答疑")){
////
////        }
////
////        baseHolder.setText(R.id.tv_category,indexMenu.name);
////        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                moutClickListener.outClick(indexMenu.name,indexMenu);
////            }
////        });
//    }
//}
