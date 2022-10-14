package com.health.index.adapter;

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.index.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class IndexTitleAdapterWord extends BaseAdapter<String> {
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean visible=true;
    @Override
    public int getItemViewType(int position) {
        return 10;
    }
    public IndexTitleAdapterWord() {
        this(R.layout.index_mon_title_word);
    }
    private IndexTitleAdapterWord(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        TextView titlename=baseHolder.getView(R.id.titlename);
        TextView more=baseHolder.getView(R.id.titlemore);
        titlename.setText(getModel());
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moutClickListener.outClick(getModel(),view);
            }
        });
        baseHolder.getView(R.id.topspace).setVisibility(visible?View.VISIBLE:View.GONE);
        baseHolder.getView(R.id.endspace).setVisibility(visible?View.VISIBLE:View.GONE);
        baseHolder.getView(R.id.parent_category).setVisibility(visible?View.VISIBLE:View.GONE);
        baseHolder.getView(R.id.titlename).setVisibility(visible?View.VISIBLE:View.GONE);
        baseHolder.getView(R.id.titlemore).setVisibility(visible?View.VISIBLE:View.GONE);
        if("憨言厚语".equals(getModel())){
            more.setVisibility(View.GONE);
        }


    }
}
