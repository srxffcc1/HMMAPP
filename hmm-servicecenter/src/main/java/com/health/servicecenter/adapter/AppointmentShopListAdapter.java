package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.widget.Filter;
import android.widget.Filterable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.servicecenter.R;
import com.healthy.library.businessutil.ListUtil;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.widget.CornerImageView;

import java.util.ArrayList;

public class AppointmentShopListAdapter extends BaseQuickAdapter<ShopDetailModel, BaseViewHolder> implements Filterable {

    private ArrayList<ShopDetailModel> filterDatas;
    private ArrayList<ShopDetailModel> mDatas;
    private ArrayList<ShopDetailModel> filteredList;


    public AppointmentShopListAdapter(ArrayList<ShopDetailModel> mDatas) {
        super(R.layout.appointment_shop_list_adapter_layout);
        this.mDatas = mDatas;
        this.filterDatas = mDatas;
    }

    @Override
    public int getItemCount() {
        return filterDatas.size();
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopDetailModel shopDetailModel) {

        String url = shopDetailModel.envPicUrl;

        if (!TextUtils.isEmpty(url) && url.contains(",")) {
            //逗号截取取第一张
            url = url.split("\\,")[0];
        }

        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(url)
                .placeholder(R.drawable.img_1_1_default)
                .error(R.drawable.img_1_1_default)
                .into((CornerImageView) helper.getView(R.id.shopImg));
        String shopName = "";
        if (!TextUtils.isEmpty(shopDetailModel.chainShopName)) {
            shopName = shopDetailModel.shopName + "(" + shopDetailModel.chainShopName + ")";
        } else {
            shopName = shopDetailModel.shopName;
        }
        helper.setText(R.id.shopName, shopName)
                .setText(R.id.shopAddress, shopDetailModel.provinceName + shopDetailModel.cityName +
                        shopDetailModel.addressAreaName + shopDetailModel.addressDetails)
                .setText(R.id.distanceTxt, ParseUtils.parseDistance(shopDetailModel.distance + ""));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    filterDatas = mDatas;
                } else {
                    if (ListUtil.isEmpty(filteredList)) {
                        filteredList = new ArrayList<>();
                    } else {
                        filteredList.clear();
                    }

                    //LogUtil.e("filteredList", filteredList.toString());

                    for (int i = 0; i < mDatas.size(); i++) {

                        if (mDatas.get(i).shopName.contains(charString)) {
                            filteredList.add(mDatas.get(i));
                        }
                    }

                    filterDatas = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterDatas;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterDatas = (ArrayList<ShopDetailModel>) results.values;
                setNewData(filterDatas);
                if (mOnEmptyDataListener != null) {
                    mOnEmptyDataListener.onSearchResultData(ListUtil.isEmpty(filterDatas));
                }
            }
        };
    }

    public ArrayList<ShopDetailModel> getFilterData(){
        return filterDatas;
    }

    private OnSearchResultDataListener mOnEmptyDataListener;

    public void setOnSearchResultDataListener(OnSearchResultDataListener onEmptyDataListener) {
        this.mOnEmptyDataListener = onEmptyDataListener;
    }

    public interface OnSearchResultDataListener {
        void onSearchResultData(boolean isEmpty);
    }
}
