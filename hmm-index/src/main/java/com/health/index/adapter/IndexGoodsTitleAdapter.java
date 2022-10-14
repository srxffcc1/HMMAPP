//package com.health.index.adapter;
//
//import androidx.annotation.NonNull;
//import android.text.TextUtils;
//import android.widget.ImageView;
//
//import com.alibaba.android.vlayout.LayoutHelper;
//import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
//import com.bumptech.glide.Glide;
//import com.health.index.R;
//import com.healthy.library.base.BaseAdapter;
//import com.healthy.library.base.BaseHolder;
//
//public class IndexGoodsTitleAdapter extends BaseAdapter<String> {
//    @Override
//    public int getItemViewType(int position) {
//        return 4;
//    }
//    public IndexGoodsTitleAdapter() {
//        this(R.layout.index_mon_goods_title);
//    }
//    private IndexGoodsTitleAdapter(int viewId) {
//        super(viewId);
//    }
//
//    @Override
//    public LayoutHelper onCreateLayoutHelper() {
//        return new LinearLayoutHelper();
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
//        final ImageView questionIcon=baseHolder.getView(R.id.nameIconImg);
//        if(!TextUtils.isEmpty(getModel())){
//            com.healthy.library.businessutil.GlideCopy.with(context)
//                    .asBitmap()
//                    .load(getModel())
//                    .placeholder(R.drawable.img_1_1_default2)
//                    .error(R.drawable.img_avatar_default)
//                    
//                    .into(questionIcon);
//        }
//
//    }
//}
