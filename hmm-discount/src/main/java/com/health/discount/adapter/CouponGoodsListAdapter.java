package com.health.discount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.health.discount.R;
import com.healthy.library.model.SortGoodsListModel;

import java.util.ArrayList;
import java.util.List;

public class CouponGoodsListAdapter extends RecyclerView.Adapter<CouponGoodsListAdapter.ViewHolder> {

    private Context mContext;
    private List<SortGoodsListModel> mList = new ArrayList<>();
    private int spanSize;// 当前每行显示几列
    private MOutClickListener mOutClickListener;// 当前每行显示几列

    public CouponGoodsListAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(List<SortGoodsListModel> list) {
        this.mList = list;
        notifyItemInserted(this.mList.size());
    }

    public void addList(List<SortGoodsListModel> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
        notifyItemRangeInserted(mList.size(), list.size());
    }

    public interface MOutClickListener {
        void outClick(String data, SortGoodsListModel model);
    }
    public void setOutClickListener(MOutClickListener mOutClickListener) {
        this.mOutClickListener = mOutClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = null;
        /** 一行显示一条 */
        if (viewType == 1) {
            root = LayoutInflater.from(mContext).inflate(R.layout.item_publiccoupon_adapter_layout, parent, false);
        }
        /** 一行显示两条 */
        else {
            root = LayoutInflater.from(mContext).inflate(R.layout.item_coupon_goods_list_layout, parent, false);
        }
        return new ViewHolder(root, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (spanSize == 1) { /** 一行显示一条 */
            return 1;
        } else { /** 一行显示两条 */
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        //return mList != null ? mList.size() : 0;
        return 20;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == 1) {

        } else {

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        TextView mall_service_title;
//        TextView mall_service_money_left;
//        TextView mall_service_money;
//        TextView mall_service_money_old;
//        TextView mall_service_space;
//        ImageView passbasket;
//        CornerImageView mall_service_img;
//        View spinnerImg;
//
//        ImageView goodsImg_goods4;
//        TextView goodsTitle_goods4;
//        TextView goodSpace_goods4;
//        TextView mall_goods_moneyflag_goods4;
//        TextView mall_goods_moneyvalue_goods4;
//        TextView mall_goods_moneyvalue_org_goods4;
//        TextView tehui_goods4;
//        AutoClickImageView passbasket_goods4;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            initView(itemView, viewType);
        }

        private void initView(View itemView, int viewType) {
            if (viewType == 2) {

            } else if (viewType == 1) {

            }
        }
    }
}
