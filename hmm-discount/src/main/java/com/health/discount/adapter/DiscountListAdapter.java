package com.health.discount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.health.discount.R;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.MarketingGoodsList;
import com.healthy.library.model.PopDetailInfo;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.CornerImageView;

import java.util.ArrayList;
import java.util.List;

public class DiscountListAdapter extends RecyclerView.Adapter<DiscountListAdapter.ViewHolder> {

    private Context mContext;
    private List<PopDetailInfo.GoodsDTOListBean> mList = new ArrayList<>();
    private int spanSize;// 当前每行显示几列
    private MOutClickListener mOutClickListener;// 当前每行显示几列

    public DiscountListAdapter(Context context) {
        this.mContext = context;
    }

    public void clear() {//清空adapter
        this.mList.clear();
        notifyDataSetChanged();
    }

    public void setList(List<PopDetailInfo.GoodsDTOListBean> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(List<PopDetailInfo.GoodsDTOListBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
        notifyItemRangeInserted(mList.size(), list.size());
    }

    public interface MOutClickListener {
        void outClick(PopDetailInfo.GoodsDTOListBean goodsDTOListBean);
    }

    public void setOutClickListener(DiscountListAdapter.MOutClickListener mOutClickListener) {
        this.mOutClickListener = mOutClickListener;
    }

    @NonNull
    @Override
    public DiscountListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = null;
        /** 一行显示一条 */
        if (viewType == 1) {
            root = LayoutInflater.from(mContext).inflate(R.layout.item_discount_list_adapter_layout, parent, false);
        }
        /** 一行显示两条 */
        else {
            root = LayoutInflater.from(mContext).inflate(R.layout.item_coupon_goods_list_layout, parent, false);
        }
        return new DiscountListAdapter.ViewHolder(root, viewType);
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
        return mList != null ? mList.size() : 0;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountListAdapter.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        final PopDetailInfo.GoodsDTOListBean model = mList.get(position);
        if (itemViewType == 1) {
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .load(model.headImage)
                    .placeholder(R.drawable.img_1_1_default)
                    .error(R.drawable.img_1_1_default)
                    .into(holder.mall_service_img);
            holder.mall_service_title.setText(model.goodsTitle);
            holder.mall_service_money.setText(FormatUtils.moneyKeep2Decimals(model.platformPrice));
            holder.passbasket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOutClickListener.outClick(model);
                }
            });
        } else {
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .load(model.headImage)
                    .placeholder(R.drawable.img_1_1_default)
                    .error(R.drawable.img_1_1_default)
                    .into(holder.goodsImg_goods4);
            holder.goodsTitle_goods4.setText(model.goodsTitle);
            holder.mall_goods_moneyvalue_goods4.setText(FormatUtils.moneyKeep2Decimals(model.platformPrice));
            holder.passbasket_goods4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOutClickListener.outClick(model);
                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", model.id)
                        .withString("marketingType", model.marketingType)
                        .withString("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                        .navigation();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mall_service_title;
        TextView mall_service_money_left;
        TextView mall_service_money;
        TextView mall_service_money_old;
        TextView mall_service_space;
        ImageView passbasket;
        CornerImageView mall_service_img;

        ImageView goodsImg_goods4;
        TextView goodsTitle_goods4;
        TextView goodSpace_goods4;
        TextView mall_goods_moneyflag_goods4;
        TextView mall_goods_moneyvalue_goods4;
        TextView mall_goods_moneyvalue_org_goods4;
        TextView tehui_goods4;
        AutoClickImageView passbasket_goods4;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            initView(itemView, viewType);
        }

        private void initView(View itemView, int viewType) {
            if (viewType == 1) {
                mall_service_title = itemView.findViewById(R.id.mall_service_title);
                mall_service_money_left = itemView.findViewById(R.id.mall_service_money_left);
                mall_service_money = itemView.findViewById(R.id.mall_service_money);
                mall_service_money_old = itemView.findViewById(R.id.mall_service_money_old);
                mall_service_space = itemView.findViewById(R.id.mall_service_space);
                passbasket = itemView.findViewById(R.id.passbasket);
                mall_service_img = itemView.findViewById(R.id.mall_service_img);
            } else if (viewType == 2) {
                goodsImg_goods4 = itemView.findViewById(R.id.goodsImg_goods4);
                goodsTitle_goods4 = itemView.findViewById(R.id.goodsTitle_goods4);
                goodSpace_goods4 = itemView.findViewById(R.id.goodSpace_goods4);
                mall_goods_moneyflag_goods4 = itemView.findViewById(R.id.mall_goods_moneyflag_goods4);
                mall_goods_moneyvalue_goods4 = itemView.findViewById(R.id.mall_goods_moneyvalue_goods4);
                mall_goods_moneyvalue_org_goods4 = itemView.findViewById(R.id.mall_goods_moneyvalue_org_goods4);
                tehui_goods4 = itemView.findViewById(R.id.tehui_goods4);
                passbasket_goods4 = itemView.findViewById(R.id.passbasket_goods4);
            }
        }
    }
}

