package com.healthy.library.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.czy1121.view.CornerLabelView;
import com.healthy.library.R;
import com.healthy.library.business.CouponChoseDialog;
import com.healthy.library.businessutil.CouponUtil;
import com.healthy.library.dialog.CouponImgDialog;
import com.healthy.library.interfaces.IHmmCoupon;
import com.healthy.library.message.UpdateCheckInfoBackMsg;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.routes.DiscountRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.AutoFitCheckBox;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class CardAdapter extends BaseQuickAdapter<CouponInfoZ, CardAdapter.CardViewHolder> {

    public int adapterType = 0;// 应用场景 0 我的优惠券 优惠券历史 1 商品中的优惠券列表 2 购物车的优惠券 3下单页面可选 4下单页面不可直接用
    private boolean allDeleteCheckShow = false;//所有的删除用的checkbox是否显示
    public Map<Integer, Boolean> checkMap = new HashMap<>();//已选map
    private boolean allCheckChecked = false;//全选按钮
    private int allwidth;//保存控件大小
    private int nowtype = 1;
    List<IHmmCoupon> selectInfo = new ArrayList<>();
    private CouponChoseDialog.OnCouponCheckChangeListener onCouponCheckChangeListener;
    private LinearLayoutManager manager;

    public void setSelectList(List<IHmmCoupon> selectInfo) {
        this.selectInfo = selectInfo;
    }

    public List<IHmmCoupon> getSelectInfo() {
        return selectInfo;
    }

    public void setNowtype(int nowtype) {
        this.nowtype = nowtype;
    }


    public void setAllCheck(boolean allcheckcheck) {
        this.allCheckChecked = allcheckcheck;
        if (allcheckcheck) {
            for (int i = 0; i < getData().size(); i++) {
                checkMap.put(i, true);
            }
        } else {
            checkMap.clear();
        }


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        manager = (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    public String getCheckIds() {
        String result = "";
        if (allCheckChecked) {//说明全选了
            for (int i = 0; i < getData().size(); i++) {
                result = result + getData().get(i).getUseId() + ",";
            }
        } else {
            Iterator<Map.Entry<Integer, Boolean>> entries = checkMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<Integer, Boolean> entry = entries.next();
                if (entry.getValue()) {
                    result = result + getData().get(entry.getKey()).getUseId() + ",";
                }
            }
        }
        if (result.length() > 1) {
            result = result.substring(0, result.length() - 1);//去掉逗号
        }
        return result;
    }


    public CardAdapter() {
        super(R.layout.dis_item_card);
    }

    public void setAdapterType(int adapterType) {
        this.adapterType = adapterType;
    }

    @Override
    protected void convert(final CardViewHolder helper, final CouponInfoZ item) {

        if (adapterType == -1) {
            buildMineCardInHistory(helper, item);
        }
        if (adapterType == 0) {
            buildMineCard(helper, item);
            Glide.with(mContext)
                    .load(item.couponImg)
                    .into(helper.couponImg);
            helper.couponImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CouponImgDialog couponImgDialog = CouponImgDialog.newInstance();
                    couponImgDialog.setCouponId(item.getUseId());
                    couponImgDialog.setData(item.getCouponUseName(),item.couponImg);
                    couponImgDialog.show(((FragmentActivity) mContext).getSupportFragmentManager(), "coupon");
                }
            });
        }
        if (adapterType == 1) {
            buildCardInGoods(helper, item);
        }
        if (adapterType == 2) {
            buildCardInBasket(helper, item);
        }
        if (adapterType == 3) {
            buildCardInOrder(helper, item);
        }
        if (adapterType == 4) {
            buildCardInOrderNo(helper, item);
        }

    }

    private void buildCardInOrderNo(final CardViewHolder helper, final CouponInfoZ item) {
        helper.cardParent.setBackgroundResource(R.drawable.shape_packcener_item_bg);
        helper.cardLeftLL.setBackgroundResource(R.drawable.packcenter_left_bg_gray);
        helper.cardFlag.setBackgroundResource(R.drawable.shape_pack_flag_gray);
        helper.cardLimit.setTextColor(Color.parseColor("#868799"));
        ConstraintLayout.LayoutParams cardParentlayoutParams = (ConstraintLayout.LayoutParams) helper.cardParent.getLayoutParams();
        cardParentlayoutParams.height = (int) TransformUtil.dp2px(mContext, 100);
        helper.cardParent.setLayoutParams(cardParentlayoutParams);
        ConstraintLayout.LayoutParams cardLeftLLlayoutParams = (ConstraintLayout.LayoutParams) helper.cardLeftLL.getLayoutParams();
        cardLeftLLlayoutParams.width = (int) TransformUtil.dp2px(mContext, 105);
        helper.cardLeftLL.setLayoutParams(cardLeftLLlayoutParams);
        helper.cardButton.setVisibility(View.GONE);
        helper.checkInOrder.setVisibility(View.GONE);
        helper.cardRightBottom.setVisibility(View.VISIBLE);
        helper.cardDownUp.setVisibility(View.GONE);

        helper.cardLimit.setText(item.getCouponUseName());
        helper.cardUseLimit.setText(item.getCouponUseLimitName());
        helper.cardFlag.setText(item.getCouponTypeName());
        helper.cardTime.setText(item.getTimeValidity());
        helper.cardMoney.setText(FormatUtils.moneyKeep2Decimals(item.getCouponDenomination()));
        if (item.getCouponDenomination().length() > 4) {
            helper.cardMoney.setTextSize(24);
        } else if (item.getCouponDenomination().length() > 3) {
            helper.cardMoney.setTextSize(27);
        } else if (item.getCouponDenomination().length() > 2) {
            helper.cardMoney.setTextSize(34);
        } else if (item.getCouponDenomination().length() > 1) {
            helper.cardMoney.setTextSize(34);
        } else {
            helper.cardMoney.setTextSize(34);
        }

        helper.cardMoneyName.setText(item.getRequirement());
        helper.cardDownUp.setVisibility(View.VISIBLE);
        if (item.getCouponUseTip() != null && !TextUtils.isEmpty(item.getCouponUseTip())) {
            helper.cardReason.setText("使用说明");
            helper.cardContent.setText(item.getCouponUseTip());
            helper.cardRightBottomBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getCouponUseTip() != null && !TextUtils.isEmpty(item.getCouponUseTip())) {
                        helper.cardContent.setVisibility(helper.cardContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    }
                }
            });
        } else {
            helper.cardDownUp.setVisibility(View.GONE);
            helper.cardReason.setText("");
        }
    }

    private void buildCardInOrder(final CardViewHolder helper, final CouponInfoZ item) {
        helper.cardParent.setBackgroundResource(R.drawable.shape_packcener_item_bg);
        helper.cardLeftLL.setBackgroundResource(R.drawable.packcenter_left_bg);
        helper.cardFlag.setBackgroundResource(R.drawable.shape_pack_flag);
        helper.cardRightBottom.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams cardParentlayoutParams = (ConstraintLayout.LayoutParams) helper.cardParent.getLayoutParams();
        cardParentlayoutParams.height = (int) TransformUtil.dp2px(mContext, 80);
        helper.cardParent.setLayoutParams(cardParentlayoutParams);
        ConstraintLayout.LayoutParams cardLeftLLlayoutParams = (ConstraintLayout.LayoutParams) helper.cardLeftLL.getLayoutParams();
        cardLeftLLlayoutParams.width = (int) TransformUtil.dp2px(mContext, 105);
        helper.cardLeftLL.setLayoutParams(cardLeftLLlayoutParams);
        helper.cardButton.setVisibility(View.GONE);
        helper.checkInOrder.setVisibility(View.VISIBLE);

        helper.cardLimit.setText(item.getCouponUseName());
        helper.cardUseLimit.setText(item.getCouponUseLimitName());
        helper.cardFlag.setText(item.getCouponTypeName());
        helper.cardTime.setText(item.getTimeValidity());
        helper.cardMoney.setText(FormatUtils.moneyKeep2Decimals(item.getCouponDenomination()));
        if (item.getCouponDenomination().length() > 4) {
            helper.cardMoney.setTextSize(24);
        } else if (item.getCouponDenomination().length() > 3) {
            helper.cardMoney.setTextSize(27);
        } else if (item.getCouponDenomination().length() > 2) {
            helper.cardMoney.setTextSize(34);
        } else if (item.getCouponDenomination().length() > 1) {
            helper.cardMoney.setTextSize(34);
        } else {
            helper.cardMoney.setTextSize(34);
        }

        helper.cardMoneyName.setText(item.getRequirement());
        if (selectInfo != null && selectInfo.size() > 0) {
            helper.checkInOrder.setChecked(CouponUtil.checkItemCouponInSelectList(selectInfo, item));
            if (CouponUtil.checkItemCouponCanWhite(selectInfo, item)) {
                //System.out.println("当前券冲突");
                helper.cardMoneyLeft.setTextColor(Color.parseColor("#868799"));
                helper.cardMoney.setTextColor(Color.parseColor("#868799"));
                helper.cardMoneyName.setTextColor(Color.parseColor("#868799"));
                helper.cardLimit.setTextColor(Color.parseColor("#868799"));
                helper.cardFlag.setBackgroundResource(R.drawable.shape_pack_flag_gray);
                helper.cardRightBottom.setVisibility(View.VISIBLE);
                helper.cardReason.setText("此劵暂不可和已勾选劵叠加使用");
                helper.cardLeftLL.setBackgroundResource(R.drawable.packcenter_left_bg_gray);
            } else {
                //System.out.println("当前券不冲突");

                helper.cardMoneyLeft.setTextColor(Color.parseColor("#FFFFFF"));
                helper.cardMoney.setTextColor(Color.parseColor("#FFFFFF"));
                helper.cardMoneyName.setTextColor(Color.parseColor("#FFFFFF"));
                helper.cardLimit.setTextColor(Color.parseColor("#868799"));
                helper.cardFlag.setBackgroundResource(R.drawable.shape_pack_flag);
                helper.cardRightBottom.setVisibility(View.GONE);
                helper.cardDownUp.setVisibility(View.VISIBLE);
                if (item.getCouponUseTip() != null && !TextUtils.isEmpty(item.getCouponUseTip())) {
                    helper.cardReason.setText("使用说明");
                    helper.cardContent.setText(item.getCouponUseTip());
                    helper.cardRightBottomBottom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (item.getCouponUseTip() != null && !TextUtils.isEmpty(item.getCouponUseTip())) {
                                helper.cardContent.setVisibility(helper.cardContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                            }
                        }
                    });
                } else {
                    helper.cardDownUp.setVisibility(View.GONE);
                    helper.cardReason.setText("");
                }
                helper.cardLeftLL.setBackgroundResource(R.drawable.packcenter_left_bg);
            }
        } else {
            //System.out.println("当前券不冲突2");
            helper.cardMoneyLeft.setTextColor(Color.parseColor("#FFFFFF"));
            helper.cardMoney.setTextColor(Color.parseColor("#FFFFFF"));
            helper.cardMoneyName.setTextColor(Color.parseColor("#FFFFFF"));
            helper.cardLimit.setTextColor(Color.parseColor("#868799"));
            helper.cardFlag.setBackgroundResource(R.drawable.shape_pack_flag);
            helper.cardRightBottom.setVisibility(View.GONE);
            helper.cardDownUp.setVisibility(View.VISIBLE);
            if (item.getCouponUseTip() != null && !TextUtils.isEmpty(item.getCouponUseTip())) {
                helper.cardReason.setText("使用说明");
                helper.cardContent.setText(item.getCouponUseTip());
                helper.cardRightBottomBottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getCouponUseTip() != null && !TextUtils.isEmpty(item.getCouponUseTip())) {
                            helper.cardContent.setVisibility(helper.cardContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                        }
                    }
                });
            } else {
                helper.cardDownUp.setVisibility(View.GONE);
                helper.cardReason.setText("");
            }
            helper.cardLeftLL.setBackgroundResource(R.drawable.packcenter_left_bg);
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.checkInOrder.setChecked(!helper.checkInOrder.isChecked());
                if (helper.checkInOrder.isChecked()) {
                    CouponUtil.getSelectListResult(selectInfo, item);
                } else {
                    removeSelectInfo(item);
                }
                if (onCouponCheckChangeListener != null) {
                    onCouponCheckChangeListener.onCheckChange();
                }
                notifyDataSetChanged();

            }
        });
        helper.checkInOrder.setOnCheckedChangeListener(new AutoFitCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(AutoFitCheckBox buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                if (isChecked) {
                    CouponUtil.getSelectListResult(selectInfo, item);
                } else {
                    removeSelectInfo(item);
                }
                if (onCouponCheckChangeListener != null) {
                    onCouponCheckChangeListener.onCheckChange();
                }
                notifyDataSetChanged();
            }
        });
    }

    public void removeSelectInfo(CouponInfoZ selectInfosingle) {
        int whildeindex = -1;
        for (int i = 0; i < selectInfo.size(); i++) {
            if (selectInfo.get(i).getUseId().equals(selectInfosingle.getUseId())) {
                whildeindex = i;
            }
        }
        if (whildeindex != -1) {
            //System.out.println("移除下这个票");
            selectInfo.remove(whildeindex);
        }

    }

    private void buildCardInBasket(final CardViewHolder helper, final CouponInfoZ item) {
        helper.cardParent.setBackgroundResource(R.drawable.shape_packcener_item_bg_half_top);
        helper.cardLeftLL.setBackgroundResource(R.drawable.packcenter_left_bg_half);
        helper.cardRightBottomBottom.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams cardParentlayoutParams = (ConstraintLayout.LayoutParams) helper.cardParent.getLayoutParams();
        cardParentlayoutParams.height = (int) TransformUtil.dp2px(mContext, 80);
        helper.cardParent.setLayoutParams(cardParentlayoutParams);
        ConstraintLayout.LayoutParams cardLeftLLlayoutParams = (ConstraintLayout.LayoutParams) helper.cardLeftLL.getLayoutParams();
        cardLeftLLlayoutParams.width = (int) TransformUtil.dp2px(mContext, 105);
        helper.cardLeftLL.setLayoutParams(cardLeftLLlayoutParams);
        helper.cardButton.setVisibility(View.VISIBLE);
        helper.cardButton.setBackgroundResource(R.drawable.shape_pack_btn_go);
        helper.cardButton.setTextColor(Color.parseColor("#FFFFFF"));
        helper.checkInOrder.setVisibility(View.GONE);
        helper.goodsLL.setVisibility(View.VISIBLE);

        helper.cardLimit.setText(item.getCouponUseName());
        helper.cardUseLimit.setText(item.getCouponUseLimitName());
        helper.cardFlag.setText(item.getCouponTypeName());
        helper.cardTime.setText(item.getTimeValidity());
        helper.cardMoney.setText(FormatUtils.moneyKeep2Decimals(item.getCouponDenomination()));
        if (item.getCouponDenomination().length() > 4) {
            helper.cardMoney.setTextSize(24);
        } else if (item.getCouponDenomination().length() > 3) {
            helper.cardMoney.setTextSize(27);
        } else if (item.getCouponDenomination().length() > 2) {
            helper.cardMoney.setTextSize(34);
        } else if (item.getCouponDenomination().length() > 1) {
            helper.cardMoney.setTextSize(34);
        } else {
            helper.cardMoney.setTextSize(34);
        }

        helper.cardMoneyName.setText(item.getRequirement());
        helper.cardDownUp.setVisibility(View.VISIBLE);
        if (item.getCouponUseTip() != null && !TextUtils.isEmpty(item.getCouponUseTip())) {
            helper.cardReason.setText("使用说明");
            helper.cardContent.setText(item.getCouponUseTip());
            helper.cardRightBottomBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getCouponUseTip() != null && !TextUtils.isEmpty(item.getCouponUseTip())) {
                        helper.cardContent.setVisibility(helper.cardContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    }
                }
            });
        } else {
            helper.cardDownUp.setVisibility(View.GONE);
            helper.cardReason.setText("");
        }
    }

    private void buildCardInGoods(final CardViewHolder helper, final CouponInfoZ item) {
        helper.cardParent.setBackgroundResource(R.drawable.shape_packcener_item_bg);
        helper.cardLeftLL.setBackgroundResource(R.drawable.packcenter_left_bg);
        helper.cardRightBottom.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams cardParentlayoutParams = (ConstraintLayout.LayoutParams) helper.cardParent.getLayoutParams();
        cardParentlayoutParams.height = (int) TransformUtil.dp2px(mContext, 70);
        helper.cardParent.setLayoutParams(cardParentlayoutParams);
        ConstraintLayout.LayoutParams cardLeftLLlayoutParams = (ConstraintLayout.LayoutParams) helper.cardLeftLL.getLayoutParams();
        cardLeftLLlayoutParams.width = (int) TransformUtil.dp2px(mContext, 100);
        helper.cardLeftLL.setLayoutParams(cardLeftLLlayoutParams);
        helper.cardButton.setVisibility(View.VISIBLE);
        helper.cardButton.setBackgroundResource(R.drawable.shape_pack_btn_go);
        helper.cardButton.setTextColor(Color.parseColor("#FFFFFF"));
        helper.checkInOrder.setVisibility(View.GONE);
        helper.goodsLL.setVisibility(View.GONE);

        helper.cardLimit.setText(item.getCouponUseName());
        helper.cardUseLimit.setText(item.getCouponUseLimitName());
        helper.cardFlag.setText(item.getCouponTypeName());
        helper.cardTime.setText(item.getTimeValidity());
        helper.cardMoney.setText(FormatUtils.moneyKeep2Decimals(item.getCouponDenomination()));
        if (item.getCouponDenomination().length() > 4) {
            helper.cardMoney.setTextSize(24);
        } else if (item.getCouponDenomination().length() > 3) {
            helper.cardMoney.setTextSize(27);
        } else if (item.getCouponDenomination().length() > 2) {
            helper.cardMoney.setTextSize(34);
        } else if (item.getCouponDenomination().length() > 1) {
            helper.cardMoney.setTextSize(34);
        } else {
            helper.cardMoney.setTextSize(34);
        }

        helper.cardMoneyName.setText(item.getRequirement());
        helper.cardDownUp.setVisibility(View.VISIBLE);
        if (item.getCouponUseTip() != null && !TextUtils.isEmpty(item.getCouponUseTip())) {
            helper.cardReason.setText("使用说明");
            helper.cardContent.setText(item.getCouponUseTip());
            helper.cardRightBottomBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getCouponUseTip() != null && !TextUtils.isEmpty(item.getCouponUseTip())) {
                        helper.cardContent.setVisibility(helper.cardContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    }
                }
            });
        } else {
            helper.cardDownUp.setVisibility(View.GONE);
            helper.cardReason.setText("");
        }
    }

    private void buildMineCardInHistory(final CardViewHolder helper, final CouponInfoZ item) {
        helper.cardParent.setBackgroundResource(R.drawable.shape_packcener_item_bg);
        helper.cardLeftLL.setBackgroundResource(R.drawable.packcenter_left_bg_gray);
        helper.cardFlag.setBackgroundResource(R.drawable.shape_pack_flag_gray);
        helper.cardLimit.setTextColor(Color.parseColor("#868799"));
        ConstraintLayout.LayoutParams cardParentlayoutParams = (ConstraintLayout.LayoutParams) helper.cardParent.getLayoutParams();
        cardParentlayoutParams.height = (int) TransformUtil.dp2px(mContext, 100);
        helper.cardParent.setLayoutParams(cardParentlayoutParams);
        ConstraintLayout.LayoutParams cardLeftLLlayoutParams = (ConstraintLayout.LayoutParams) helper.cardLeftLL.getLayoutParams();
        cardLeftLLlayoutParams.width = (int) TransformUtil.dp2px(mContext, 105);
        helper.cardLeftLL.setLayoutParams(cardLeftLLlayoutParams);
        helper.cardButton.setVisibility(View.GONE);
        helper.checkInOrder.setVisibility(View.GONE);
        helper.cardRightBottom.setVisibility(View.VISIBLE);
        helper.cardDownUp.setVisibility(View.GONE);


        helper.deleteCheck.setVisibility(allDeleteCheckShow ? View.VISIBLE : View.GONE);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) helper.cardBigParentLL.getLayoutParams();
        layoutParams.width = (int) (ScreenUtils.getScreenWidth(mContext) - TransformUtil.dp2px(mContext, 20));
        helper.cardBigParentLL.setLayoutParams(layoutParams);
        helper.cardLimit.setText(item.getCouponUseName());
        helper.cardUseLimit.setText(item.getCouponUseLimitName());
        helper.cardFlag.setText(item.getCouponTypeName());
        helper.cardTime.setText(item.getTimeValidity());
        helper.cardMoney.setText(FormatUtils.moneyKeep2Decimals(item.getCouponDenomination()));
        if (item.getCouponDenomination().length() > 4) {
            helper.cardMoney.setTextSize(24);
        } else if (item.getCouponDenomination().length() > 3) {
            helper.cardMoney.setTextSize(27);
        } else if (item.getCouponDenomination().length() > 2) {
            helper.cardMoney.setTextSize(34);
        } else if (item.getCouponDenomination().length() > 1) {
            helper.cardMoney.setTextSize(34);
        } else {
            helper.cardMoney.setTextSize(34);
        }

        helper.cardMoneyName.setText(item.getRequirement());
        helper.deleteCheck.setChecked(false);
        if ((checkMap.get(helper.getPosition()) != null && checkMap.get(helper.getPosition())) || allCheckChecked) {
            helper.deleteCheck.setChecked(true);
        }
        helper.ivEmptyStock.setVisibility(View.VISIBLE);
        if (item.getStatus() == 2) {
            //判断当前的时间是不是在截至时间之后则为过期 否则为已使用
            helper.ivEmptyStock.setImageResource(R.drawable.dis_time);
        } else {
            if(item.getCouponUseName().contains("撤回")){
                helper.ivEmptyStock.setImageResource(R.drawable.dis_back);
            }else {
                helper.ivEmptyStock.setImageResource(R.drawable.dis_use);
            }

        }
        helper.cardDownUp.setVisibility(View.VISIBLE);
        if (item.getCouponUseTip() != null && !TextUtils.isEmpty(item.getCouponUseTip())) {
            helper.cardReason.setText("使用说明");
            helper.cardContent.setText(item.getCouponUseTip());
            helper.cardRightBottomBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getCouponUseTip() != null && !TextUtils.isEmpty(item.getCouponUseTip())) {
                        helper.cardContent.setVisibility(helper.cardContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    }
                }
            });
        } else {
            helper.cardDownUp.setVisibility(View.GONE);
            helper.cardReason.setText("");
        }
        helper.deleteCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                if (!isChecked) {
                    allCheckChecked = false;
                    EventBus.getDefault().post(new UpdateCheckInfoBackMsg(false));
                }
                checkMap.put(helper.getPosition(), isChecked);
            }
        });
    }

    private void buildMineCard(final CardViewHolder helper, final CouponInfoZ item) {
        helper.cardParent.setBackgroundResource(R.drawable.shape_packcener_item_bg);
        ConstraintLayout.LayoutParams cardParentlayoutParams = (ConstraintLayout.LayoutParams) helper.cardParent.getLayoutParams();
        cardParentlayoutParams.height = (int) TransformUtil.dp2px(mContext, 100);
        helper.cardParent.setLayoutParams(cardParentlayoutParams);
        ConstraintLayout.LayoutParams cardLeftLLlayoutParams = (ConstraintLayout.LayoutParams) helper.cardLeftLL.getLayoutParams();
        cardLeftLLlayoutParams.width = (int) TransformUtil.dp2px(mContext, 105);
        helper.cardLeftLL.setLayoutParams(cardLeftLLlayoutParams);
        helper.checkInOrder.setVisibility(View.GONE);
        helper.deleteCheck.setVisibility(allDeleteCheckShow ? View.VISIBLE : View.GONE);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) helper.cardBigParentLL.getLayoutParams();
        layoutParams.width = (int) (ScreenUtils.getScreenWidth(mContext) - TransformUtil.dp2px(mContext, 20));
        helper.cardBigParentLL.setLayoutParams(layoutParams);
        if (item.status == -1) {//未生效的
            helper.cardLeftLL.setBackgroundResource(R.drawable.packcenter_left_bg_gray);
            helper.cardFlag.setBackgroundResource(R.drawable.shape_pack_flag_gray);
            helper.cardLimit.setTextColor(Color.parseColor("#868799"));
            helper.cardTime.setTextColor(Color.parseColor("#868799"));
            helper.cardMoney.setTextColor(Color.parseColor("#868799"));
            helper.cardMoneyName.setTextColor(Color.parseColor("#868799"));
            helper.cardMoneyLeft.setTextColor(Color.parseColor("#868799"));
            helper.couponLabel.setVisibility(View.VISIBLE);
            helper.couponLabel.setFillColor(Color.parseColor("#868799"));
            helper.couponLabel.setText1("未生效");
            helper.cardButton.setText("去逛逛");
        } else {
            helper.cardLeftLL.setBackgroundResource(R.drawable.packcenter_left_bg);
            helper.cardFlag.setBackgroundResource(R.drawable.shape_pack_flag);
            helper.cardLimit.setTextColor(Color.parseColor("#222222"));
            helper.cardTime.setTextColor(Color.parseColor("#666666"));
            helper.cardMoney.setTextColor(Color.parseColor("#ffffff"));
            helper.cardMoneyName.setTextColor(Color.parseColor("#ffffff"));
            helper.cardMoneyLeft.setTextColor(Color.parseColor("#ffffff"));
            helper.cardButton.setBackgroundResource(R.drawable.shape_pack_btn);
            helper.couponLabel.setVisibility(View.GONE);
            helper.cardButton.setText("去使用");
        }
        helper.cardLimit.setText(item.getCouponUseName());
        helper.cardUseLimit.setText(item.getCouponUseLimitName());
        helper.cardFlag.setText(item.getCouponTypeName());
        helper.cardTime.setText(item.getTimeValidity());
        helper.cardMoney.setText(FormatUtils.moneyKeep2Decimals(item.getCouponDenomination()));
        if (item.getCouponDenomination().length() > 4) {
            helper.cardMoney.setTextSize(24);
        } else if (item.getCouponDenomination().length() > 3) {
            helper.cardMoney.setTextSize(27);
        } else if (item.getCouponDenomination().length() > 2) {
            helper.cardMoney.setTextSize(34);
        } else if (item.getCouponDenomination().length() > 1) {
            helper.cardMoney.setTextSize(34);
        } else {
            helper.cardMoney.setTextSize(34);
        }

        helper.cardMoneyName.setText(item.getRequirement());
        helper.deleteCheck.setChecked(false);
        if ((checkMap.get(helper.getPosition()) != null && checkMap.get(helper.getPosition())) || allCheckChecked) {
            helper.deleteCheck.setChecked(true);
        }
        helper.cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(DiscountRoutes.DIS_PUBLICCOUPON)
                        .withString("cardId", item.getCouponId())
                        .withString("cardName", item.getCouponNormalName())
                        .withString("time", item.getTimeValidity())
                        .navigation();
            }
        });
        helper.cardDownUp.setVisibility(View.VISIBLE);
        if (item.getCouponUseTip() != null && !TextUtils.isEmpty(item.getCouponUseTip())) {
            helper.cardReason.setText("使用说明");
            helper.cardContent.setText(item.getCouponUseTip());
            helper.cardRightBottomBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getCouponUseTip() != null && !TextUtils.isEmpty(item.getCouponUseTip())) {
                        helper.cardContent.setVisibility(helper.cardContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    }
                }
            });
        } else {
            helper.cardDownUp.setVisibility(View.GONE);
            helper.cardReason.setText("");
        }
        helper.deleteCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                if (!isChecked) {
                    allCheckChecked = false;
                    EventBus.getDefault().post(new UpdateCheckInfoBackMsg(false));
                }
                checkMap.put(helper.getPosition(), isChecked);
            }
        });

    }

    public void setAllcheckshow(boolean allcheckshow) {
        this.allDeleteCheckShow = allcheckshow;
        if (!allcheckshow) {
            this.allDeleteCheckShow = false;
            checkMap.clear();
        }
    }

    public void setOnCouponCheckChangeListener(CouponChoseDialog.OnCouponCheckChangeListener onCouponCheckChangeListener) {
        this.onCouponCheckChangeListener = onCouponCheckChangeListener;
    }

    public CouponChoseDialog.OnCouponCheckChangeListener getOnCouponCheckChangeListener() {
        return onCouponCheckChangeListener;
    }

    public static class CardViewHolder extends BaseViewHolder {
        public LinearLayout cardBigParentLL;
        public ImageView cardDownUp;
        public CheckBox deleteCheck;
        public ConstraintLayout cardBigParent;
        public Space parentTmp;
        public TextView cardContent;
        public ConstraintLayout cardParent;
        public LinearLayout cardLeftLL;
        public TextView cardMoney;
        public TextView cardMoneyName;
        public TextView cardFlag;
        public TextView cardLimit;
        public TextView cardTime;
        public TextView cardButton;
        public AutoFitCheckBox checkInOrder;
        public LinearLayout cardRightBottom;
        public RelativeLayout cardRightBottomBottom;
        public ImageView ivEmptyStock2;
        public CornerLabelView couponLabel;
        public ImageView ivEmptyStock;
        public LinearLayout goodsLL;
        public TextView goodsTip;
        public TextView cardReason;
        public LinearLayout goodListLL;
        public TextView cardMoneyLeft;
        public TextView cardUseLimit;
        public ImageView couponImg;
//        public ImageView goodsIcon;
//        public TextView goodsName;
//        public TextView goodsNum;
//        public AutoFitCheckBox goodCheck;

        public CardViewHolder(View itemView) {
            super(itemView);
            cardUseLimit = (TextView) itemView.findViewById(R.id.cardUseLimit);
            cardMoneyLeft = (TextView) itemView.findViewById(R.id.cardMoneyLeft);
            cardBigParentLL = (LinearLayout) itemView.findViewById(R.id.cardBigParentLL);
            cardReason = (TextView) itemView.findViewById(R.id.cardReason);
            deleteCheck = (CheckBox) itemView.findViewById(R.id.deleteCheck);
            cardBigParent = (ConstraintLayout) itemView.findViewById(R.id.cardBigParent);
            parentTmp = (Space) itemView.findViewById(R.id.parentTmp);
            cardContent = (TextView) itemView.findViewById(R.id.cardContent);
            cardParent = (ConstraintLayout) itemView.findViewById(R.id.cardParent);
            cardLeftLL = (LinearLayout) itemView.findViewById(R.id.cardLeftLL);
            cardMoney = (TextView) itemView.findViewById(R.id.cardMoney);
            cardMoneyName = (TextView) itemView.findViewById(R.id.cardMoneyName);
            cardFlag = (TextView) itemView.findViewById(R.id.cardFlag);
            cardLimit = (TextView) itemView.findViewById(R.id.cardLimit);
            cardTime = (TextView) itemView.findViewById(R.id.cardTime);
            cardButton = (TextView) itemView.findViewById(R.id.cardButton);
            checkInOrder = (AutoFitCheckBox) itemView.findViewById(R.id.checkInOrder);
            cardRightBottom = (LinearLayout) itemView.findViewById(R.id.cardRightBottom);
            cardRightBottomBottom = (RelativeLayout) itemView.findViewById(R.id.cardRightBottomBottom);
            ivEmptyStock2 = (ImageView) itemView.findViewById(R.id.iv_empty_stock2);
            couponLabel = (CornerLabelView) itemView.findViewById(R.id.couponLabel);
            ivEmptyStock = (ImageView) itemView.findViewById(R.id.iv_empty_stock);
            goodsLL = (LinearLayout) itemView.findViewById(R.id.goodsLL);
            goodsTip = (TextView) itemView.findViewById(R.id.goodsTip);
            goodListLL = (LinearLayout) itemView.findViewById(R.id.goodListLL);
//            goodsIcon = (ImageView) itemView.findViewById(R.id.goodsIcon);
//            goodsName = (TextView) itemView.findViewById(R.id.goodsName);
//            goodsNum = (TextView) itemView.findViewById(R.id.goodsNum);
//            goodCheck = (AutoFitCheckBox) itemView.findViewById(R.id.goodCheck);
            cardDownUp = (ImageView) itemView.findViewById(R.id.cardDownUp);
            couponImg = (ImageView) itemView.findViewById(R.id.couponImg);
        }
    }

}
