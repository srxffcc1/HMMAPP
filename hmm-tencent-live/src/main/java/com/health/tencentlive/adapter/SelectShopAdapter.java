package com.health.tencentlive.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.tencentlive.R;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.utils.IntentUtils;
import com.healthy.library.utils.ParseUtils;

/**
 * @author Li
 * @date 2019/03/04 17:28
 * @des 商品列表
 */

public class SelectShopAdapter extends BaseQuickAdapter<ShopDetailModel, BaseViewHolder> {

    public String shopIdSelect = "";

    public String getShopIdSelect() {
        return shopIdSelect;
    }

    public void setShopIdSelect(String shopIdSelect) {
        this.shopIdSelect = shopIdSelect;
    }

    OnShopClickListener onShopClickListener;

    public void setOnShopClickListener(OnShopClickListener onShopClickListener) {
        this.onShopClickListener = onShopClickListener;
    }

    public SelectShopAdapter() {
        this(R.layout.item_pick_shop);
    }


    private SelectShopAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ShopDetailModel item) {

        ImageView checkOk;
        ImageView ivBranchShopDial;
        TextView tvShopName;
        TextView tvShopAddress;
        ImageView ivAddress;
        TextView tvDistance;
        TextView locTip;

        checkOk = (ImageView) helper.itemView.findViewById(R.id.checkOk);
        ivBranchShopDial = (ImageView) helper.itemView.findViewById(R.id.iv_branch_shop_dial);
        tvShopName = (TextView) helper.itemView.findViewById(R.id.tv_shop_name);
        tvShopAddress = (TextView) helper.itemView.findViewById(R.id.tv_shop_address);
        ivAddress = (ImageView) helper.itemView.findViewById(R.id.iv_address);
        tvDistance = (TextView) helper.itemView.findViewById(R.id.tv_distance);
        locTip = helper.itemView.findViewById(R.id.locTip);
        tvShopName.setText(item.shopName);
        tvShopAddress.setText(item.addressDetails);
        tvDistance.setText(ParseUtils.parseDistance(item.distance + ""));
        checkOk.setVisibility(View.INVISIBLE);
        if (shopIdSelect != null && shopIdSelect.equals(item.id)) {
            checkOk.setVisibility(View.VISIBLE);
        }
        if (helper.getPosition() == 0) {
            if (item.distance / 1000 < 10) {
                locTip.setVisibility(View.VISIBLE);
            } else {
                locTip.setVisibility(View.GONE);
            }
        } else {
            locTip.setVisibility(View.GONE);
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopIdSelect = item.id;
                if (onShopClickListener != null) {
                    onShopClickListener.onShopClick(item);
                }
            }
        });
        ivBranchShopDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(item.appointmentPhone)) {
                    Toast.makeText(mContext, "该商家未预留电话", Toast.LENGTH_SHORT).show();
                    return;
                }
                IntentUtils.dial(mContext, item.appointmentPhone);
            }
        });

    }

    private void initView() {

    }

    public interface OnShopClickListener {
        void onShopClick(ShopDetailModel model);
    }
}
