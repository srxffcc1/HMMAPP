package com.health.servicecenter.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.servicecenter.R;
import com.healthy.library.model.SortGoodsListModel;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.LogUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.AutoClickImageView;
import com.healthy.library.widget.CornerImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MallGoodsItemHTransverseAdapter extends RecyclerView.Adapter<MallGoodsItemHTransverseAdapter.ViewHolder> {

    private Context mContext;
    private List<SortGoodsListModel> mList = new ArrayList<>();
    private int spanSize;// 当前每行显示几列
    private MOutClickListener mOutClickListener;// 当前每行显示几列
    public Map<String, String> imageScalemap = new HashMap<>();

    public MallGoodsItemHTransverseAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(List<SortGoodsListModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
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
            root = LayoutInflater.from(mContext).inflate(R.layout.item_mall_goods_2, parent, false);
        }
        /** 一行显示两条 */
        else {
            root = LayoutInflater.from(mContext).inflate(R.layout.item_mall_goods_4, parent, false);
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
        return mList != null ? mList.size() : 0;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public void setOnBasketClickListener(OnBasketClickListener onBasketClickListener) {
        this.onBasketClickListener = onBasketClickListener;
    }

    OnBasketClickListener onBasketClickListener;

    public interface OnBasketClickListener {
        void onBasketClick(View view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        final SortGoodsListModel model = mList.get(position);
        if (itemViewType == 1) {
            holder.mall_service_space.setVisibility(View.GONE);
            holder.mall_service_title.setText(model.getGoodsTitle());
//            if (model.getSpecCell() != null && model.getSpecCell().size() > 0) {
//                if (model.getSpecCell().get(0).specValue != null && model.getSpecCell().get(0).specValue.length > 0) {
//                    holder.mall_service_space.setText(model.getGoodsChildren().get(0).getGoodsSpecStr().toString());
//                    holder.mall_service_space.setVisibility(View.VISIBLE);
//                }
//            }
            holder.mall_service_money_left.setText("¥");
            holder.mall_service_money.setText(FormatUtils.moneyKeep2Decimals(model.getPlatformPrice()) + "");
            holder.mall_service_money_old.setText("¥" + FormatUtils.moneyKeep2Decimals(model.getStorePrice()));
            holder.mall_service_money_old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
//            if (model.getStorePrice() == 0) {
//                holder.mall_service_money_old.setVisibility(View.INVISIBLE);
//            } else {
//                holder.mall_service_money_old.setVisibility(View.VISIBLE);
//            }
            com.healthy.library.businessutil.GlideCopy.with(mContext).load(model.getHeadImage())
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_1_1_default)

                    .into(holder.mall_service_img);
            holder.passbasket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBasketClickListener != null) {
                        onBasketClickListener.onBasketClick(v);
                    }
                    mOutClickListener.outClick("addShopCat", model);
                }
            });
            if (!TextUtils.isEmpty(model.getPlusPrice())) {
                holder.vipGoldP.setVisibility(View.VISIBLE);
                holder.cardName.setText("PLUS");
                holder.cardPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(model.getPlusPrice()));
            } else {
                holder.vipGoldP.setVisibility(View.GONE);
            }
        } else {
            holder.goodSpace_goods4.setVisibility(View.GONE);
            holder.goodsTitle_goods4.setText(model.getGoodsTitle());
//            if (model.getSpecCell() != null && model.getSpecCell().size() > 0) {
//                if (model.getSpecCell().get(0).specValue != null && model.getSpecCell().get(0).specValue.length > 0) {
//                    holder.goodSpace_goods4.setText(model.getGoodsChildren().get(0).getGoodsSpecStr().toString());
//                    holder.goodSpace_goods4.setVisibility(View.VISIBLE);
//                }
//            }
            holder.mall_goods_moneyflag_goods4.setText("¥");
            holder.mall_goods_moneyvalue_goods4.setText(FormatUtils.moneyKeep2Decimals(model.getPlatformPrice()) + "");
            holder.mall_goods_moneyvalue_org_goods4.setText("¥" + FormatUtils.moneyKeep2Decimals(model.getStorePrice()));
            holder.mall_goods_moneyvalue_org_goods4.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
//            if (model.getStorePrice() == 0) {
//                holder.mall_goods_moneyvalue_org_goods4.setVisibility(View.INVISIBLE);
//            } else {
//                holder.mall_goods_moneyvalue_org_goods4.setVisibility(View.VISIBLE);
//            }
            if (!TextUtils.isEmpty(imageScalemap.get(model.getHeadImage()))) {
                String value = imageScalemap.get(model.getHeadImage());
                int height = Integer.parseInt(value.split(":")[0]);
                int swidth = Integer.parseInt(value.split(":")[1]);
                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) holder.goodsImg_goods4.getLayoutParams();
                layoutParams.height = height;
                layoutParams.width = swidth;
                holder.goodsImg_goods4.setLayoutParams(layoutParams);
                ////System.out.println("直接设置大小" + position);
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(model.getHeadImage())
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(holder.goodsImg_goods4);
            } else {
                com.healthy.library.businessutil.GlideCopy.with(mContext).load(model.getHeadImage())
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)

                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                if (holder.getPosition() != position) {
                                    return;
                                }
                                int swidth = (int) (ScreenUtils.getScreenWidth(mContext) / 2 - TransformUtil.dp2px(mContext, 8));
                                int height = (int) ((resource.getIntrinsicHeight() * 1.0 / resource.getIntrinsicWidth()) * swidth);
                                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) holder.goodsImg_goods4.getLayoutParams();
                                layoutParams.height = height;
                                layoutParams.width = swidth;
                                try {
                                    imageScalemap.put(model.getHeadImage(), height + ":" + swidth);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                holder.goodsImg_goods4.setLayoutParams(layoutParams);
                                com.healthy.library.businessutil.GlideCopy.with(holder.goodsImg_goods4.getContext()).load(resource).into(holder.goodsImg_goods4);
                            }
                        });
            }
            holder.passbasket_goods4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBasketClickListener != null) {
                        onBasketClickListener.onBasketClick(v);
                    }
                    mOutClickListener.outClick("addShopCat", model);
                }
            });
            if (!TextUtils.isEmpty(model.getPlusPrice())) {
                holder.vipGoldP.setVisibility(View.VISIBLE);
                holder.cardName.setText("PLUS");
                holder.cardPrice.setText("¥" + FormatUtils.moneyKeep2Decimals(model.getPlusPrice()));
            } else {
                holder.vipGoldP.setVisibility(View.GONE);
            }
        }
        TextView tagText = holder.itemView.findViewById(R.id.tagText);
        tagText.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(model.getTagFirst())) {
            tagText.setVisibility(View.VISIBLE);
            if (model.getTagFirst().length() > 3) {
                String org = model.getTagFirst();
                String resultOrg = org.substring(0, 2) + "\n" + org.substring(2, org.length());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgbig);
                tagText.setText(resultOrg);
            } else {
                tagText.setText(model.getTagFirst());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgsmall);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "搜索结果列表页商品");
                MobclickAgent.onEvent(mContext, "btn_APP__MaternalandChildGoods_CommodityDetails", nokmap);
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("id", model.getId() + "")
                        .withString("shopId", model.getShopId() + "")
                        .withString("bargainId", model.bargainId)
                        .withString("assembleId", model.assembleId)
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
        View spinnerImg;

        ImageView goodsImg_goods4;
        TextView goodsTitle_goods4;
        TextView goodSpace_goods4;
        TextView mall_goods_moneyflag_goods4;
        TextView mall_goods_moneyvalue_goods4;
        TextView mall_goods_moneyvalue_org_goods4;
        TextView tehui_goods4;
        TextView cardName;
        TextView cardPrice;
        AutoClickImageView passbasket_goods4;
        LinearLayout vipGoldP;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            initView(itemView, viewType);
        }

        private void initView(View itemView, int viewType) {
            if (viewType == 2) {
                goodsImg_goods4 = itemView.findViewById(R.id.goodsImg_goods4);
                goodsTitle_goods4 = itemView.findViewById(R.id.goodsTitle_goods4);
                goodSpace_goods4 = itemView.findViewById(R.id.goodSpace_goods4);
                mall_goods_moneyflag_goods4 = itemView.findViewById(R.id.mall_goods_moneyflag_goods4);
                mall_goods_moneyvalue_goods4 = itemView.findViewById(R.id.mall_goods_moneyvalue_goods4);
                mall_goods_moneyvalue_org_goods4 = itemView.findViewById(R.id.mall_goods_moneyvalue_org_goods4);
                tehui_goods4 = itemView.findViewById(R.id.tehui_goods4);
                passbasket_goods4 = itemView.findViewById(R.id.passbasket_goods4);
                vipGoldP = itemView.findViewById(R.id.vipGoldP);
                cardPrice = itemView.findViewById(R.id.cardPrice);
                cardName = itemView.findViewById(R.id.cardName);
            } else if (viewType == 1) {
                mall_service_title = itemView.findViewById(R.id.mall_service_title);
                mall_service_money_left = itemView.findViewById(R.id.mall_service_money_left);
                mall_service_money = itemView.findViewById(R.id.mall_service_money);
                mall_service_money_old = itemView.findViewById(R.id.mall_service_money_old);
                mall_service_space = itemView.findViewById(R.id.mall_service_space);
                passbasket = itemView.findViewById(R.id.passbasket);
                mall_service_img = itemView.findViewById(R.id.mall_service_img);
                spinnerImg = itemView.findViewById(R.id.spinnerImg);
                vipGoldP = itemView.findViewById(R.id.vipGoldP);
                cardPrice = itemView.findViewById(R.id.cardPrice);
                cardName = itemView.findViewById(R.id.cardName);
            }
        }
    }
}
