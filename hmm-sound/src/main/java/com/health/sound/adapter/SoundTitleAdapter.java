package com.health.sound.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.sound.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.SoundRoutes;
import com.healthy.library.widget.ImageTextView;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class SoundTitleAdapter extends BaseAdapter<String> {

    boolean needMore=true;

    String categoryId;

    public void setAudioType(String audioType) {
        this.audioType = audioType;
    }

    String audioType;

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setNeedMore(boolean needMore) {
        this.needMore = needMore;
    }

    public SoundTitleAdapter() {
        this(R.layout.sound_item_title);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
         LinearLayout parentCategory;
         TextView titlename;
         ImageTextView titlemore;
        parentCategory = (LinearLayout) holder.itemView.findViewById(R.id.parent_category);
        titlename = (TextView)  holder.itemView.findViewById(R.id.titlename);
        titlemore = (ImageTextView)  holder.itemView.findViewById(R.id.titlemore);
        if(!needMore){
            titlemore.setVisibility(View.GONE);
        }
        if(needMore){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance()
                            .build(SoundRoutes.SOUND_MORE)
                            .withString("audioFrequencyCategoryId",categoryId)
                            .withString("audioType",audioType)
                            .withString("audioCategoryName",getModel())
                            .navigation();
                }
            });
        }
        titlename.setText(getModel());


    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private SoundTitleAdapter(int layoutResId) {
        super(layoutResId);

    }

    private void initView() {

    }
}
