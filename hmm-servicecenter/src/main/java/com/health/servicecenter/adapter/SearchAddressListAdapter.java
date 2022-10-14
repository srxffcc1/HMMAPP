package com.health.servicecenter.adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.health.servicecenter.R;
import com.healthy.library.model.AddressModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.ParseUtils;

public class SearchAddressListAdapter extends BaseAdapter<PoiItem> {
    private ItemClickListener mItemClickListener;
    private LatLng latLng;

    public void setmItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface ItemClickListener {

        void onClick(AddressModel model);

    }

    public SearchAddressListAdapter() {
        this(R.layout.item_address_search_list_layout);

    }

    public SearchAddressListAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        latLng = new LatLng(Double.parseDouble(LocUtil.getLatitude(context, SpKey.LOC_CHOSE)),
                Double.parseDouble(LocUtil.getLongitude(context, SpKey.LOC_CHOSE)));//当前定位经纬度
        TextView address_name = holder.getView(R.id.address_name);
        TextView address_address = holder.getView(R.id.address_address);
        TextView address_num = holder.getView(R.id.address_num);
        RelativeLayout search_list_rel = holder.getView(R.id.search_list_rel);
        address_name.setText(getDatas().get(position).getTitle());
        address_num.setText("");
        if (getDatas().get(position).getLatLonPoint() != null) {
            address_num.setText(ParseUtils.parseDistance(AMapUtils.calculateLineDistance(latLng, convertToLatLng(getDatas().get(position).getLatLonPoint())) + ""));//距离转换
        }
        address_address.setText(getDatas().get(position).getSnippet());
        search_list_rel.setOnClickListener(new View.OnClickListener() {
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

    public LatLng convertToLatLng(LatLonPoint latLonPoint) {//LatLonPoint转LatLng
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    @Override
    public int getItemCount() {
        return getDatas() == null ? 0 : getDatas().size();
    }


}
