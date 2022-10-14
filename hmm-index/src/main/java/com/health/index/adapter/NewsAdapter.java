package com.health.index.adapter;

import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.index.R;
import com.health.index.model.IndexAllSee;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.SpUtils;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class NewsAdapter extends BaseQuickAdapter<IndexAllSee, BaseViewHolder> {
    public NewsAdapter() {
        this(R.layout.index_mon_see);
    }

    private NewsAdapter(int layoutResId) {
        super(layoutResId);

    }
    @Override
    protected void convert(BaseViewHolder helper, final IndexAllSee item) {
        helper.setText(R.id.content,item.title);
        helper.setText(R.id.count,(item.readQuantity+item.fictitiousReadQuantity)+"人正在看");
        ImageView imageView=helper.getView(R.id.icon);
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .asBitmap()
                .load(item.images)
                .placeholder(R.drawable.img_1_1_default)
                .error(R.drawable.img_1_1_default2)

                .into(imageView);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlPrefix = SpUtils.getValue(mContext, UrlKeys.H5_KNOWLEDGE);
                String url = String.format("%s?id=%s&scheme=NewsMessage", urlPrefix, item.id);
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEWARTICLE)
                        .withString("title", "资讯")
                        .withString("url", url)
                        .withBoolean("needShare",true)
                        .withBoolean("isinhome",true)
                        .withBoolean("needfindcollect",true)
                        .navigation();
            }
        });

    }
}
