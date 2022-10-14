package com.health.sound.adapter;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.sound.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.routes.SoundRoutes;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class SoundTopAdapter extends BaseAdapter<String> {

    String audioType;

    public SoundTopAdapter(String audioType) {
        this(R.layout.sound_item_edition_top);
        this.audioType=audioType;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
         LinearLayout editionLeftLL;
         LinearLayout editionRightLL;
        editionLeftLL = (LinearLayout) holder.itemView.findViewById(R.id.editionLeftLL);
        editionRightLL = (LinearLayout) holder.itemView.findViewById(R.id.editionRightLL);
        editionLeftLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(SoundRoutes.SOUND_HISTORY)
                        .withInt("currentIndex",0)
                        .withString("audioType",audioType)
                        .navigation();
            }
        });
        editionRightLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(SoundRoutes.SOUND_HISTORY)
                        .withInt("currentIndex",1)
                        .withString("audioType",audioType)
                        .navigation();
            }
        });
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    private SoundTopAdapter(int layoutResId) {
        super(layoutResId);

    }

    private void initView() {
    }
}
