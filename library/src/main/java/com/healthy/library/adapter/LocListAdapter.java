package com.healthy.library.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.healthy.library.R;
import com.healthy.library.model.LocVip;


public class LocListAdapter extends BaseQuickAdapter<LocVip.Local, BaseViewHolder> {
    LocChangeListener locChangeListener;

    public void setLocChangeListener(LocChangeListener locChangeListener) {
        this.locChangeListener = locChangeListener;
    }

    public LocListAdapter() {
        this(R.layout.item_activity_loc_change);
    }

    private LocListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final LocVip.Local item) {
        TextView locName;
        View dividerLoc;
        locName = (TextView) helper.itemView.findViewById(R.id.loc_name);
        dividerLoc = (View) helper.itemView.findViewById(R.id.divider_loc);
        locName.setText(item.getCityName()+ "·" +item.partnerName);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(locChangeListener!=null){
                    locChangeListener.onLocClick(item);
                }
            }
        });

    }

    public interface LocChangeListener{
        void onLocClick(LocVip.Local item);
    }
}
