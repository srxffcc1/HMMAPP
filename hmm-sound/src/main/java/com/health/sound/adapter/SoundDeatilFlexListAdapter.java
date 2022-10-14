package com.health.sound.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.health.sound.R;
import com.health.sound.model.SoundTrack;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class SoundDeatilFlexListAdapter extends BaseAdapter<SoundTrack> {

    public long selectPage=1;

    public void setOnIndexClickListener(OnIndexClickListener onIndexClickListener) {
        this.onIndexClickListener = onIndexClickListener;
    }

    OnIndexClickListener onIndexClickListener;
    public SoundDeatilFlexListAdapter() {
        this(R.layout.sound_item_edition_flex_cell);
    }

    public SoundDeatilFlexListAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setAutoExpand(false);
        gridLayoutHelper.setBgColor(Color.parseColor("#F5F5F9"));
        return gridLayoutHelper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final SoundTrack soundIndex= getDatas().get(position);
         LinearLayout editionFlexCellP;
         TextView indeCell;
        editionFlexCellP = (LinearLayout) holder.itemView.findViewById(R.id.editionFlexCellP);
        indeCell = (TextView)  holder.itemView.findViewById(R.id.indeCell);
        indeCell.setText(soundIndex.pageString);
        indeCell.setBackgroundResource(R.drawable.shape_sound_flex_normal);
        if(soundIndex.page==selectPage){
            indeCell.setBackgroundResource(R.drawable.shape_sound_flex_select);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onIndexClickListener!=null){
                    onIndexClickListener.onIndexClick(soundIndex.page);
                    selectPage=soundIndex.page;
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void initView() {

    }
    public interface OnIndexClickListener{
        void onIndexClick(long page);
    }
}
