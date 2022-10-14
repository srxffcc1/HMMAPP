package com.health.servicecenter.adapter;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.ActVip;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.utils.TransformUtil;

import java.util.List;
import java.util.Map;

/**
 * 门店组
 */
public class MallGoodsBasketDiscountTopGoodsAdapter extends BaseAdapter<ActVip> {


    private Map<String, String> imageMap;

    @Override
    public int getItemViewType(int position) {
        return 41;
    }

    public MallGoodsBasketDiscountTopGoodsAdapter() {
        this(R.layout.service_item_goodsbasket_discount_orgbasketwithselect);
    }

    private MallGoodsBasketDiscountTopGoodsAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, final int i) {
        GridLayout discountGoodsOrgAll;
        discountGoodsOrgAll = (GridLayout) baseHolder.itemView.findViewById(R.id.discountGoodsOrgAll);
        addFunctions(context, discountGoodsOrgAll, getModel().getGoodsListWithOutGift());
    }

    private void addFunctions(final Context context, final GridLayout gridLayout, final List<ActVip.SaleInfo> list) {
        gridLayout.removeAllViews();
        if (list != null && list.size() > 0) {
            int column = 4;
            int needfixzzz = column - (list.size() % column == 0 ? column : list.size() % column);
            int mMargin = (int) TransformUtil.dp2px(context, 70);
            ViewGroup.LayoutParams gridlayoutparm = gridLayout.getLayoutParams();
            gridLayout.setLayoutParams(gridlayoutparm);
            gridLayout.setColumnCount(column);
            for (int i = 0; i < list.size(); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsbasket_discount_orgbasketwithselect_itemgoods, gridLayout, false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    GridLayout.LayoutParams param = new GridLayout.LayoutParams(GridLayout.spec(
                            GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
                    view.setLayoutParams(param);
                }
                ActVip.SaleInfo saleInfo = list.get(i);
                ImageView goodsImg;
                TextView goodsTitle;
                LinearLayout goodsTagsLL;
                TextView goodsMoney;
                TextView goodsCount;
                goodsImg = (ImageView) view.findViewById(R.id.goodsImg);
                goodsTitle = (TextView) view.findViewById(R.id.goodsTitle);
                goodsTagsLL = (LinearLayout) view.findViewById(R.id.goodsTagsLL);
                goodsMoney = (TextView) view.findViewById(R.id.goodsMoney);
                goodsCount = (TextView) view.findViewById(R.id.goodsCount);
                if(saleInfo.filePath!=null&&imageMap.get(saleInfo.getGoodsID())==null){
                    imageMap.put(saleInfo.getGoodsID(),saleInfo.filePath);
                }
                if(!TextUtils.isEmpty(imageMap.get(saleInfo.getGoodsID()))){
                    saleInfo.filePath=imageMap.get(saleInfo.getGoodsID());
                }
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(saleInfo.filePath)
                        .placeholder(R.drawable.img_1_1_default_z)
                        .error(R.drawable.img_1_1_default_z)
                        
                        .into(goodsImg);

                if (saleInfo != null) {
                    goodsTitle.setText(saleInfo.GoodsName);
                    goodsCount.setText("x"+saleInfo.Number);
                    goodsMoney.setText("¥"+FormatUtils.moneyKeep2Decimals(saleInfo.SalePrice));
                }
                gridLayout.addView(view);
            }
            if (needfixzzz > 0) {
                for (int i = 0; i < needfixzzz; i++) {
                    View view = LayoutInflater.from(context).inflate(R.layout.service_item_goodsbasket_discount_orgbasketwithselect_itemgoods, gridLayout, false);
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

    private void initView() {

    }

    public void setImageMap(Map<String, String> imageMap) {
        this.imageMap = imageMap;
    }

    public Map<String, String> getImageMap() {
        return imageMap;
    }
}
