package com.health.servicecenter.adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.amap.api.services.core.PoiItem;
import com.health.servicecenter.R;
import com.healthy.library.model.AddressModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.utils.ParseUtils;

public class SearchAddressAdapter extends BaseAdapter<PoiItem> {
    private ItemClickListener mItemClickListener;

    public void setmItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface ItemClickListener {

        void onClick(AddressModel model);

    }

    public SearchAddressAdapter() {
        this(R.layout.item_address_search_layout);
    }

    public SearchAddressAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        TextView address_name = holder.getView(R.id.address_name);
        TextView address_address = holder.getView(R.id.address_address);
        TextView address_num = holder.getView(R.id.address_num);
        RelativeLayout address_rel = holder.getView(R.id.address_rel);
//        if (position==0) {
//            address_name.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.servicecenter_nearby_address_red), null, null, null);
//            address_name.setTextColor(Color.parseColor("#fff02846"));
//        }
        address_name.setText(getDatas().get(position).getTitle());
        if (getDatas().get(position).getDistance() == 0) {
            address_num.setText("1米");
        } else {
            address_num.setText(ParseUtils.parseDistance(getDatas().get(position).getDistance() + ""));
        }

        address_address.setText(getDatas().get(position).getSnippet());
        address_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressModel model = new AddressModel();
                model.setProvince(getDatas().get(position).getProvinceCode());
                model.setProvinceName(getDatas().get(position).getProvinceName());
                model.setCity(getDatas().get(position).getCityCode());
                model.setCityName(getDatas().get(position).getCityName());
                model.setDistrict(getDatas().get(position).getAdCode());
                model.setDistrictName(getDatas().get(position).getAdName());
                model.setAddress(getDatas().get(position).getTitle());
                model.setLat(getDatas().get(position).getLatLonPoint().getLatitude() + "");
                model.setLng(getDatas().get(position).getLatLonPoint().getLongitude() + "");
                mItemClickListener.onClick(model);
            }
        });


    }

    @Override
    public int getItemCount() {
        return getDatas() == null ? 0 : getDatas().size();
    }

}
