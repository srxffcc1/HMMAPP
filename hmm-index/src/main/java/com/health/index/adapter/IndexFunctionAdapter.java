package com.health.index.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.healthy.library.model.IndexMenu;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.utils.ScreenUtils;

public class IndexFunctionAdapter extends BaseAdapter<IndexMenu> {
    public void setTmprecyclerView(RecyclerView tmprecyclerView) {
        this.tmprecyclerView = tmprecyclerView;
    }

    RecyclerView tmprecyclerView;

//    BaseAdapter.OnOutClickListener moutClickListener;

//    public void setOutClickListener(BaseAdapter.OnOutClickListener onOutClickListener) {
//        this.moutClickListener = onOutClickListener;
//    }

    @Override
    public int getItemViewType(int position) {
        return 2;
    }

    protected void convert(BaseViewHolder helper, IndexMenu item) {

    }

    public IndexFunctionAdapter() {
        this(R.layout.index_mon_function);
    }


    private IndexFunctionAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new GridLayoutHelper(5);
    }


    @Override
    public void onBindViewHolder(@NonNull final BaseHolder baseHolder, int i) {
        final IndexMenu indexMenu= getDatas().get(i);
        final View parent_category=baseHolder.getView(R.id.parent_category);
        parent_category.setLayoutParams(new RecyclerView.LayoutParams(((int)ScreenUtils.getScreenWidth(context)/5),RecyclerView.LayoutParams.WRAP_CONTENT));
        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(indexMenu.imageRes)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)

                .into((ImageView) baseHolder.getView(R.id.iv_category));

//        if(indexMenu.name.equals("专家答疑")){
//
//        }

        baseHolder.setText(R.id.tv_category,indexMenu.name);
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moutClickListener.outClick(indexMenu.name,indexMenu);
            }
        });
    }
}
