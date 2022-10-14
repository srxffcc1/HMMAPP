package com.health.index.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.index.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;

public class ToolsTitleAdapter extends BaseAdapter<String> {
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean visible=true;
    @Override
    public int getItemViewType(int position) {
        return 10;
    }
    public ToolsTitleAdapter() {
        this(R.layout.index_tools_title);
    }
    private ToolsTitleAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        TextView titlename=baseHolder.getView(R.id.titlename);
        titlename.setText(getModel());
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moutClickListener!=null){

                    moutClickListener.outClick(getModel(),view);
                }
            }
        });
        baseHolder.getView(R.id.parent_category).setVisibility(visible?View.VISIBLE:View.GONE);
        baseHolder.getView(R.id.titlename).setVisibility(visible?View.VISIBLE:View.GONE);


    }
}
