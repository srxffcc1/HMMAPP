package com.health.index.adapter;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.index.R;
import com.health.index.model.IndexWarmWord;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.IndexRoutes;

public class IndexWordAdapter extends BaseAdapter<IndexWarmWord> {
    @Override
    public int getItemViewType(int position) {
        return 12;
    }
    public IndexWordAdapter() {
        this(R.layout.index_word);
    }
    private IndexWordAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        final IndexWarmWord indexWarmWord= getDatas().get(i);
        final TextView showtip=baseHolder.getView(R.id.showtip);
        final ImageView showtipimage=baseHolder.getView(R.id.showtipImage);
        if(indexWarmWord.type==1){
            showtip.setVisibility(View.VISIBLE);
            showtipimage.setVisibility(View.GONE);
        }else {
            showtip.setVisibility(View.GONE);
            showtipimage.setVisibility(View.VISIBLE);

        }
        if(TextUtils.isEmpty(indexWarmWord.imageUrl)){

            showtipimage.setVisibility(View.GONE);
        }else {
            com.healthy.library.businessutil.GlideCopy.with(showtipimage.getContext())
                    .asBitmap()
                    .load(indexWarmWord.imageUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_avatar_default)
                    
                    .into(showtipimage);
        }
        baseHolder.setText(R.id.content,indexWarmWord.content);
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(indexWarmWord.hyperLink)){
                    ARouter.getInstance()
                            .build(IndexRoutes.INDEX_WEBVIEW)
                            .withString("title", "憨言厚语")
                            .withBoolean("isinhome",false)
                            .withString("url", indexWarmWord.hyperLink)
                            .navigation();
                }else if(indexWarmWord.type==1){//说明是提醒
                    if(indexWarmWord.content.contains("待产")){
                        ARouter.getInstance()
                                .build(IndexRoutes.INDEX_BIRTH_PACKAGE)
                                .navigation();
                    }else if(indexWarmWord.content.contains("产检")){
                        ARouter.getInstance()
                                .build(IndexRoutes.INDEX_BIRTH_CHECK_LIST)
                                .withInt("id", -1)
                                .navigation();
                    }else if(indexWarmWord.content.contains("疫苗")){

                        ARouter.getInstance()
                                .build(IndexRoutes.INDEX_VACCINE_LIST)
                                .withInt("id", -1)
                                .navigation();
                    }
                }
            }
        });
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean visible=true;
}
