package com.health.sound.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.health.sound.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.SoundAlbum;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class SoundGridAdapter extends BaseAdapter<SoundAlbum> {


    String audioType;


    public SoundGridAdapter(
            String audioType) {
        this(R.layout.sound_item_edittion_sec);

        this. audioType=
                audioType;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final SoundAlbum soundAlbum = getDatas().get(position);
         ConstraintLayout parentCategory;
         CornerImageView ivCategory;
         ImageTextView ivCategoryCount;
         TextView tvCategory;

        parentCategory = (ConstraintLayout)holder.itemView. findViewById(R.id.parent_category);
        ivCategory = (CornerImageView) holder.itemView.findViewById(R.id.iv_category);
        ivCategoryCount = (ImageTextView) holder.itemView.findViewById(R.id.iv_category_count);
        tvCategory = (TextView) holder.itemView.findViewById(R.id.tv_category);

        com.healthy.library.businessutil.GlideCopy.with(context)
                .load(soundAlbum.cover_url)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)
                
                .into(ivCategory);
        tvCategory.setText(soundAlbum.album_title);
        ivCategoryCount.setText(ParseUtils.parseNumber(soundAlbum.play_count+"",10000,"万"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(SoundRoutes.SOUND_DETAIL)
                        .withString("id",soundAlbum.id+"")
                        .withString("audioType",audioType)
                        .navigation();
            }
        });
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(3);
        gridLayoutHelper.setAutoExpand(false);
        return gridLayoutHelper;
    }

    private SoundGridAdapter(int layoutResId) {
        super(layoutResId);

    }

    private void initView() {


    }
}
