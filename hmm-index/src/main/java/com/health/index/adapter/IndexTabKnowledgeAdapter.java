package com.health.index.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.index.R;
import com.healthy.library.model.NewsInfo;
import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.constant.UrlKeys;
import com.healthy.library.routes.IndexRoutes;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.StaggeredGridLayoutHelperFix;

import java.util.HashMap;
import java.util.Map;

public class IndexTabKnowledgeAdapter extends BaseAdapter<NewsInfo> {
    public Map<String, String> imageScalemap = new HashMap<>();
    @Override
    public int getItemViewType(int position) {
        return 21;
    }

    public IndexTabKnowledgeAdapter() {
        this(R.layout.index_mon_knowledge);
    }

    private IndexTabKnowledgeAdapter(int viewId) {
        super(viewId);
    }

    private OnLikeClickListener onLikeClickListener;

    public void setOnLikeClickListener(OnLikeClickListener onLikeClickListener) {
        this.onLikeClickListener = onLikeClickListener;
    }

    public interface OnLikeClickListener {
        void articleLike(String id, String function);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        StaggeredGridLayoutHelperFix helper = new StaggeredGridLayoutHelperFix();
//        helper.setMargin(TransformUtil.newDp2px(LibApplication.getAppContext(), 5),
//                TransformUtil.newDp2px(LibApplication.getAppContext(), 12),
//                TransformUtil.newDp2px(LibApplication.getAppContext(), 5),
//                0);
        helper.setLane(2);
        helper.setHGap(3);
        return helper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        final NewsInfo indexAllSee = getDatas().get(i);
        final Drawable likeNormal = context.getResources().getDrawable(R.drawable.cityrightlike);
        final Drawable likeSelect = context.getResources().getDrawable(R.drawable.cityrightliker);
        final ImageTextView collectionNum = baseHolder.itemView.findViewById(R.id.collectionNum);

        baseHolder.setText(R.id.content, indexAllSee.title);
        if (indexAllSee.categoryName == null) {
            baseHolder.setVisibility(R.id.categoryName, false);
        } else {
            baseHolder.setVisibility(R.id.categoryName, true);
            baseHolder.setText(R.id.categoryName, indexAllSee.categoryName);
        }
        baseHolder.setText(R.id.count, indexAllSee.readQuantity + indexAllSee.fictitiousReadQuantity + "人正在看");
        if (indexAllSee.praise) {
            collectionNum.setTextColor(Color.parseColor("#F93F60"));
            collectionNum.setDrawable(likeSelect);
        } else {
            collectionNum.setDrawable(likeNormal);
            collectionNum.setTextColor(Color.parseColor("#444444"));
        }
        collectionNum.setText(indexAllSee.praiseCount + "");
        final ImageView imageView = baseHolder.getView(R.id.icon);
//        Glide.with(context)
//                .load(indexAllSee.images)
//                .placeholder(R.drawable.img_1_1_default)
//                .error(R.drawable.img_1_1_default)
//                
//                .into(imageView);

        if (!TextUtils.isEmpty(imageScalemap.get(indexAllSee.images))) {
            String value = imageScalemap.get(indexAllSee.images);
            int height = Integer.parseInt(value.split(":")[0]);
            int swidth = Integer.parseInt(value.split(":")[1]);
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) imageView.getLayoutParams();
            layoutParams.height = height;
            layoutParams.width = swidth;
            imageView.setLayoutParams(layoutParams);
            //System.out.println("直接设置大小" + position);
            Glide.with(context).load(indexAllSee.images)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)
                    
                    .into(imageView);
        } else {
            Glide.with(context).load(indexAllSee.images)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)
                    
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            int swidth =(int) (ScreenUtils.getScreenWidth(context) / 2 - TransformUtil.dp2px(context,10));
                            int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);

                            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) imageView.getLayoutParams();
                            layoutParams.height = height;
                            layoutParams.width = swidth;
                            imageScalemap.put(indexAllSee.images, height + ":" + swidth);
                            imageView.setLayoutParams(layoutParams);
                            com.healthy.library.businessutil.GlideCopy.with(context).load(resource).into(imageView);
                        }
                    });
        }


        baseHolder.setOnClickListener(R.id.collectionNum, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLikeClickListener != null) {
                    onLikeClickListener.articleLike(indexAllSee.id + "", indexAllSee.praise == true ? "KD_10004" : "KD_10003");
                    indexAllSee.praise = indexAllSee.praise == true ? false : true;
                    indexAllSee.praiseCount = indexAllSee.praiseCount + (indexAllSee.praise == true ? 1 : -1);
                    collectionNum.setText(indexAllSee.praiseCount == 0 ? "  " : indexAllSee.praiseCount + "");
                    if (indexAllSee.praise) {
                        collectionNum.setTextColor(Color.parseColor("#F93F60"));
                        collectionNum.setDrawable(likeSelect);
                    } else {
                        collectionNum.setDrawable(likeNormal);
                        collectionNum.setTextColor(Color.parseColor("#444444"));
                    }
                }
            }
        });
        baseHolder.setOnClickListener(R.id.icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlPrefix = SpUtils.getValue(context, UrlKeys.H5_KNOWLEDGE);
                String url = String.format("%s?id=%s&scheme=NewsMessage", urlPrefix, indexAllSee.id);
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEWARTICLE)
                        .withString("title", "资讯")
                        .withBoolean("needShare", true)
                        .withBoolean("isinhome", true)
                        .withBoolean("needfindcollect", true)
                        .withString("url", url)
                        .navigation();
            }
        });
        baseHolder.setOnClickListener(R.id.content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlPrefix = SpUtils.getValue(context, UrlKeys.H5_KNOWLEDGE);
                String url = String.format("%s?id=%s&scheme=NewsMessage", urlPrefix, indexAllSee.id);
                ARouter.getInstance()
                        .build(IndexRoutes.INDEX_WEBVIEWARTICLE)
                        .withString("title", "资讯")
                        .withBoolean("needShare", true)
                        .withBoolean("isinhome", true)
                        .withBoolean("needfindcollect", true)
                        .withString("url", url)
                        .navigation();
            }
        });
    }
}
