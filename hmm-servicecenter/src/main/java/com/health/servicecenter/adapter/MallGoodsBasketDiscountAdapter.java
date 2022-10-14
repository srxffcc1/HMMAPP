package com.health.servicecenter.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.ActVip;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.AutoFitCheckBox;
import com.healthy.library.widget.ExpandTextView;

import java.util.List;
import java.util.Map;

/**
 * 门店组
 */
public class MallGoodsBasketDiscountAdapter extends BaseAdapter<ActVip> {


    private Map<String, String> imageMap;

    @Override
    public int getItemViewType(int position) {
        return 43;
    }

    public MallGoodsBasketDiscountAdapter() {
        this(R.layout.service_item_goodsbasket_discount_lineall);
    }

    private MallGoodsBasketDiscountAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, final int i) {
        LinearLayout discountGoodsOrgAll;
        discountGoodsOrgAll = (LinearLayout) baseHolder.itemView.findViewById(R.id.discountLineAll);
        final ActVip actVip = getModel();
        actVip.setPopDetailFindInSales();
        TextView resetAct= baseHolder.itemView.findViewById(R.id.resetAct);
        resetAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moutClickListener!=null){
                    moutClickListener.outClick("重置活动",null);
                }
            }
        });
        addFunctions(context, discountGoodsOrgAll, getModel().PopInfo);
    }

    private void addFunctions(final Context context, final LinearLayout gridLayout, final List<ActVip.PopInfo> list) {
        gridLayout.removeAllViews();
        final ActVip actVip = getModel();
        for (int i = 0; i < list.size(); i++) {
            final ActVip.PopInfo popInfo = list.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsbasket_discount_line, gridLayout, false);
            final GridLayout discountGoodsLineALL = view.findViewById(R.id.discountGoodsLineALL);
            ConstraintLayout discountTopLL;
            final AutoFitCheckBox discountCheck;
            final TextView discountTitle;
            final ExpandTextView discountContent;
            View discountGoodsLineALLMore;
            ConstraintLayout discountGoodsLineALLLL;
            final TextView discountContentMoreText;
            final ImageView discountContentMoreImg;
            final View discountContentMore;
            discountTopLL = (ConstraintLayout) view.findViewById(R.id.discountTopLL);
            discountGoodsLineALLLL = (ConstraintLayout) view.findViewById(R.id.discountGoodsLineALLLL);
            discountCheck = (AutoFitCheckBox) view.findViewById(R.id.discountCheck);
            discountTitle = (TextView) view.findViewById(R.id.discountTitle);
            discountContent = (ExpandTextView) view.findViewById(R.id.discountContent);
            discountTitle.setText(popInfo.PopLabelName);
            discountGoodsLineALLMore= view.findViewById(R.id.discountGoodsLineALLMore);
            discountContentMoreText= view.findViewById(R.id.discountContentMoreText);
            discountContentMoreImg= view.findViewById(R.id.discountContentMoreImg);
            discountContentMore=view.findViewById(R.id.discountContentMore);
            discountCheck.setOnCheckedChangeListener(new AutoFitCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(AutoFitCheckBox buttonView, boolean isChecked) {
                    if (!discountCheck.isPressed()) {
                        return;
                    }
                    choseActLine(isChecked, context, discountGoodsLineALL, popInfo, actVip);

                }
            });
            discountGoodsLineALLMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (moutClickListener != null) {
                        moutClickListener.outClick("查看更多", popInfo);
                    }
                }
            });
            discountGoodsLineALLMore.setVisibility(View.VISIBLE);
//            if(popInfo.PopDetail.size()<=3){
//                discountGoodsLineALLMore.setVisibility(View.INVISIBLE);
//            }
            discountContent.setMaxLineCount(1);
            discountContent.clearText();
            discountContent.setText(popInfo.PopDesc);
            discountContent.setCallback(new ExpandTextView.Callback() {
                @Override
                public void onExpand() {
                    discountContentMore.setVisibility(View.VISIBLE);
                    discountContentMoreText.setText("收起");
                    discountContentMoreImg.setImageResource(R.drawable.order_detial_more_up);

                }

                @Override
                public void onCollapse() {
                    discountContentMore.setVisibility(View.VISIBLE);
                    discountContentMoreText.setText("展开");
                    discountContentMoreImg.setImageResource(R.drawable.order_detial_more_down);

                }

                @Override
                public void onLoss() {
                    discountContentMore.setVisibility(View.GONE);
                }
            });
            discountContent.setChanged(popInfo.isShowContent);
            discountContentMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popInfo.isShowContent=!popInfo.isShowContent;
                    discountContent.setChanged(popInfo.isShowContent);
                }
            });
//            discountContent.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    System.out.println("长按事件");
//                    if(popInfo.PopDesc.length()>15){
//                        ViewTooltip.on(discountContent)
//                                .autoHide(true, 7000)
//                                .corner(19)
//                                .color(Color.parseColor("#FF7A8B"))
//                                .distanceWithView(5)
//                                .position(ViewTooltip.Position.TOP)
//                                .padding(20, 20, 20, 20)
//                                .text(popInfo.PopDesc)
//                                .withShadow(false)
//                                .textSize(TypedValue.COMPLEX_UNIT_SP,12)
//                                .textColor(Color.WHITE)
//                                .show();
//                        return true;
//                    }
//                    return false;
//
//                }
//            });
            discountGoodsLineALL.removeAllViews();
            discountCheck.setVisibility(View.VISIBLE);
            if ("Y".equals(popInfo.DisFlag)) {
                discountCheck.setVisibility(View.INVISIBLE);
            }
            if ("Y".equals(popInfo.SelFlag)) {
                discountCheck.setChecked(true);
                addFunctions(context, discountGoodsLineALL, popInfo, popInfo.getRealPopDetail());
            } else {
                discountCheck.setChecked(false);
            }
            if (!"Y".equals(popInfo.DisFlag)) {
                gridLayout.addView(view);
            }
        }
    }

    private void choseActLine(final boolean isChecked, final Context context, final GridLayout discountGoodsLineALL, final ActVip.PopInfo popInfo, final ActVip actVip) {
        if (isChecked) {//说明选中了此营销活动
            if(popInfo.getRealPopDetail().size()==1){

                addFunctions(context, discountGoodsLineALL, popInfo, popInfo.getRealPopDetail());
                actVip.IsDelPop = "";
                choseActGoods(isChecked,popInfo,popInfo.getRealPopDetail().get(0),actVip,context);
                return;
            }else {
                addFunctions(context, discountGoodsLineALL, popInfo, popInfo.getRealPopDetail());
            }
        } else {
            getModel().removePop(popInfo);
        }
        int haspopselect= actVip.getSelPopInfo().size();
        popInfo.setCheck(isChecked);
        actVip.ReCalcPopNo = "";
        actVip.IsDelPop = null;
        if (!isChecked) {
            actVip.IsDelPop = "Y";
        }
        if (popInfo.getSelectCount() > 0) {
            if (moutClickListener != null) {
                moutClickListener.outClick("刷新礼物", null);
            }
        } else {
            if(haspopselect==0){
                actVip.ReCalcPopNo = popInfo.PopNo;
                if (moutClickListener != null) {
                    moutClickListener.outClick("促销计算初始", null);
                }
            }else {
                actVip.ReCalcPopNo = popInfo.PopNo;
                if (moutClickListener != null) {
                    moutClickListener.outClick("促销计算", null);
                }
            }

        }
    }

    private void addFunctions(final Context context, final GridLayout gridLayout, final ActVip.PopInfo popInfo, final List<ActVip.PopDetail> list) {
//        Collections.sort(list);
        int size = list.size() >= 3 ? 3 : list.size();
        gridLayout.removeAllViews();
        if (list != null && size > 0) {
            int column = 3;
            int needfixzzz = column - (size % column == 0 ? column : size % column);
            int mMargin = (int) TransformUtil.dp2px(context, 70);
            ViewGroup.LayoutParams gridlayoutparm = gridLayout.getLayoutParams();
            gridLayout.setLayoutParams(gridlayoutparm);
            gridLayout.setColumnCount(column);
            for (int i = 0; i < size; i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsbasket_discount_line_goods, gridLayout, false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(
                            GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
                    view.setLayoutParams(param);
                }
                final ActVip.PopDetail popDetail = list.get(i);
                final ActVip actVip = getModel();
                ImageView goodsImg;
                AutoFitCheckBox goodsCheck;
                TextView goodsTitle;
                LinearLayout goodsTagsLL;
                TextView goodsMoney;
                TextView goodsCount;
                goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
                goodsCheck = (AutoFitCheckBox) view.findViewById(R.id.goodsCheck);
                goodsTitle = (TextView) view.findViewById(R.id.goodsTitle);
                goodsTagsLL = (LinearLayout) view.findViewById(R.id.goodsTagsLL);
                goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
                goodsCount = (TextView) view.findViewById(R.id.goodsCount);
                goodsTitle.setText(popDetail.GoodsName);
                if(popDetail.filePath!=null&&imageMap.get(popDetail.getGoodsID())==null){
                    imageMap.put(popDetail.getGoodsID(),popDetail.filePath);
                }
                if(imageMap.get(popDetail.getGoodsID())!=null){
                    popDetail.filePath=imageMap.get(popDetail.getGoodsID());
                }

                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(popDetail.filePath)
                        .placeholder(R.drawable.img_1_1_default_z)
                        .error(R.drawable.img_1_1_default_z)
                        
                        .into(goodsImg);
                goodsCheck.setOnCheckedChangeListener(new AutoFitCheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(AutoFitCheckBox buttonView, boolean isChecked) {
                        if (!buttonView.isPressed()) {
                            return;
                        }
                        choseActGoods(isChecked, popInfo, popDetail, actVip, context);

                    }
                });
                if (popDetail.isIscheck()) {
                    System.out.println("重新设置");
                    goodsCheck.setChecked(true);
                } else {
                    System.out.println("重新设置");
                    goodsCheck.setChecked(false);
                }
                gridLayout.addView(view);
            }
            if (needfixzzz > 0) {
                for (int i = 0; i < needfixzzz; i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsbasket_discount_line_goods, gridLayout, false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(
                                GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
                        view.setLayoutParams(param);
                    }
                    view.setVisibility(View.INVISIBLE);
                    gridLayout.addView(view);
                }
            }
        }
    }

    private void choseActGoods(final boolean isChecked, final ActVip.PopInfo popInfo, final ActVip.PopDetail popDetail, final ActVip actVip, final Context context) {
        if (isChecked) {
            String result = popInfo.checkPopInfoCountLimit(popDetail);
            if (TextUtils.isEmpty(result)) {
                boolean isHasThisPop= actVip.checkHasThisPop(popInfo);
                popDetail.setIscheck(true);
                actVip.addGift(popDetail, popInfo);

                if(!isHasThisPop){//有这个活动了 则此时增加商品直接加就行了 避免重复获得刘景丹
                    actVip.ReCalcPopNo = popInfo.PopNo;
                    if (moutClickListener != null) {
                        moutClickListener.outClick("促销计算", null);
                    }
                }
            } else {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            }
        } else {
            actVip.removeGift(popDetail, popInfo);
            popDetail.setIscheck(false);
        }
        if (moutClickListener != null) {
            moutClickListener.outClick("刷新礼物", null);
        }
    }

    private void initView() {

    }

    public void setImageMap(Map<String, String> imageMap) {
        this.imageMap = imageMap;
    }

    public Map<String, String> getImageMap() {
        return imageMap;
    }
}
