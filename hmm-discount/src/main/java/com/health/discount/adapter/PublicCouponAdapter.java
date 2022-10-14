package com.health.discount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.discount.R;
import com.healthy.library.model.CouponGoodsModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.MarketingGoodsList;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.SpUtils;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.CornerImageView;

import java.util.ArrayList;
import java.util.List;

public class PublicCouponAdapter extends RecyclerView.Adapter<PublicCouponAdapter.ViewHolder> {

    private Context mContext;
    private List<CouponGoodsModel> mList = new ArrayList<>();
    private int spanSize;// 当前每行显示几列
    private MOutClickListener mOutClickListener;// 当前每行显示几列

    public PublicCouponAdapter(Context context) {
        this.mContext = context;
    }

    public void clear() {//清空adapter
        this.mList.clear();
        notifyDataSetChanged();
    }

    public void setList(List<CouponGoodsModel> list) {
        this.mList = list;
        notifyItemInserted(this.mList.size());
    }

    public void addList(List<CouponGoodsModel> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
        notifyItemRangeInserted(mList.size(), list.size());
    }

    public interface MOutClickListener {
        void outClick(CouponGoodsModel couponGoodsModel);
    }

    public void setOutClickListener(MOutClickListener mOutClickListener) {
        this.mOutClickListener = mOutClickListener;
    }

    @NonNull
    @Override
    public PublicCouponAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = null;
        /** 一行显示一条 */
        if (viewType == 1) {
            root = LayoutInflater.from(mContext).inflate(R.layout.item_publiccoupon_adapter_layout, parent, false);
        }
        /** 一行显示两条 */
        else {
            root = LayoutInflater.from(mContext).inflate(R.layout.item_coupon_goods_list_layout, parent, false);
        }
        return new PublicCouponAdapter.ViewHolder(root, viewType);
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
    public void onBindViewHolder(@NonNull PublicCouponAdapter.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        final CouponGoodsModel publicCouponModel = mList.get(position);
        if (itemViewType == 1) {
            if (publicCouponModel != null) {
                com.healthy.library.businessutil.GlideCopy.with(mContext)
                        .load(publicCouponModel.headImage)
                        .error(R.drawable.img_1_1_default)
                        .placeholder(R.drawable.img_1_1_default)
                        .into(holder.mallServiceImg);
                holder.mallServiceTitle.setText(publicCouponModel.goodstitle);
//                if (publicCouponModel.goodsChildren != null && publicCouponModel.goodsChildren.size() > 0) {
//                    holder.mallServiceSpace.setText(publicCouponModel.goodsChildren.get(0).goodsSpecStr != null ? publicCouponModel.goodsChildren.get(0).goodsSpecStr : "");
//                }
                holder.mallServiceMoney.setText(FormatUtils.moneyKeep2Decimals(publicCouponModel.platformPrice));
                holder.mallServiceMoneyOld.setText("");
                holder.passbasket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOutClickListener.outClick(publicCouponModel);
                    }
                });
            }
        } else {
            if (publicCouponModel != null) {
                com.healthy.library.businessutil.GlideCopy.with(mContext)
                        .load(publicCouponModel.headImage)
                        .error(R.drawable.img_1_1_default)
                        .placeholder(R.drawable.img_1_1_default)
                        .into(holder.goodsImg_goods4);
                holder.goodsTitle_goods4.setText(publicCouponModel.goodstitle);
//                if (publicCouponModel.goodsChildren != null && publicCouponModel.goodsChildren.size() > 0) {
//                    holder.goodSpace_goods4.setText(publicCouponModel.goodsChildren.get(0).goodsSpecStr != null ? publicCouponModel.goodsChildren.get(0).goodsSpecStr : "");
//                }
                holder.mall_goods_moneyvalue_goods4.setText(FormatUtils.moneyKeep2Decimals(publicCouponModel.platformPrice));
                holder.mall_goods_moneyvalue_org_goods4.setText("");
                holder.passbasket_goods4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOutClickListener.outClick(publicCouponModel);
                    }
                });
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", publicCouponModel.id + "")
                        .withString("shopId", SpUtils.getValue(mContext, SpKey.CHOSE_SHOP))
                        .navigation();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CornerImageView mallServiceImg;
        TextView mallServiceTitle;
        TextView mallServiceSpace;
        ImageView spinnerImg;
        HorizontalScrollView couponListLL;
        LinearLayout couponList;
        TextView mallServiceMoneyLeft;
        TextView mallServiceMoney;
        TextView mallServiceMoneyOld;
        AutoClickImageView passbasket;

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
                mallServiceImg = (CornerImageView) itemView.findViewById(R.id.mall_service_img);
                mallServiceTitle = (TextView) itemView.findViewById(R.id.mall_service_title);
                mallServiceSpace = (TextView) itemView.findViewById(R.id.mall_service_space);
                spinnerImg = (ImageView) itemView.findViewById(R.id.spinnerImg);
                couponListLL = (HorizontalScrollView) itemView.findViewById(R.id.couponListLL);
                couponList = (LinearLayout) itemView.findViewById(R.id.couponList);
                mallServiceMoneyLeft = (TextView) itemView.findViewById(R.id.mall_service_money_left);
                mallServiceMoney = (TextView) itemView.findViewById(R.id.mall_service_money);
                mallServiceMoneyOld = (TextView) itemView.findViewById(R.id.mall_service_money_old);
                passbasket = (AutoClickImageView) itemView.findViewById(R.id.passbasket);
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
