//package com.health.index.adapter;
//
//import androidx.annotation.NonNull;
//import android.view.View;
//
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.health.index.R;
//import com.healthy.library.base.BaseAdapter;
//import com.healthy.library.base.BaseHolder;
//
//public class IndexTitleRefreshAdapter extends BaseAdapter<String> {
//    @Override
//    public int getItemViewType(int position) {
//        return 11;
//    }
//    public IndexTitleRefreshAdapter() {
//        this(R.layout.index_mon_title_fresh);
//    }
//    private IndexTitleRefreshAdapter(int viewId) {
//        super(viewId);
//    }
//
//    boolean isshow=true;
//
//    public void setIsshow(boolean isshow) {
//        this.isshow = isshow;
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        return new LinearLayoutHelper();
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
//        baseHolder.itemView.setVisibility(isshow?View.VISIBLE:View.GONE);
//        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                moutClickListener.outClick(getModel(),view);
//            }
//        });
//
//    }
//}
