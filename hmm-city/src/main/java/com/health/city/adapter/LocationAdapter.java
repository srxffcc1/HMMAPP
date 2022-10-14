package com.health.city.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.city.R;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class LocationAdapter extends BaseQuickAdapter<PoiItem, BaseViewHolder> {

    private String selectareaName;

    public void setSelectAreaName(String selectareaName) {
        this.selectareaName = selectareaName;
    }

    public void setOnLocationClickListener(OnLocationClickListener onLocationClickListener) {
        this.onLocationClickListener = onLocationClickListener;
    }

    OnLocationClickListener onLocationClickListener;

    public LocationAdapter() {
        this(R.layout.city_item_location);
    }

    private LocationAdapter(int layoutResId) {
        super(layoutResId);

    }

    @Override
    protected void convert(BaseViewHolder helper, final PoiItem item) {
        TextView shopName;
        TextView shopAddress;
        ImageView shopSelect;
        shopName = (TextView) helper.getView(R.id.shopName);
        shopAddress = (TextView) helper.getView(R.id.shopAddress);
        shopSelect = (ImageView) helper.getView(R.id.shopSelect);
        shopName.setText(item.getTitle());
        shopAddress.setText(item.getSnippet());

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onLocationClickListener != null) {
                    onLocationClickListener.onClick(view,item);
                }
            }
        });
        if(item.getTitle().equals(selectareaName)){
            shopSelect.setVisibility(View.VISIBLE);
        }else {
            shopSelect.setVisibility(View.GONE);
        }

    }
    public interface OnLocationClickListener{
        void onClick(View view,PoiItem item);
    }
}
