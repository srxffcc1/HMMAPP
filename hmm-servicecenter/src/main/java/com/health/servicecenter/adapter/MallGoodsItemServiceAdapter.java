package com.health.servicecenter.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.RecommendList;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;

public class MallGoodsItemServiceAdapter extends BaseAdapter<RecommendList> {


    @Override
    public int getItemViewType(int position) {
        return 8;
    }

    public MallGoodsItemServiceAdapter() {
        this(R.layout.item_mall_goods_3);

    }

    private MallGoodsItemServiceAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        LinearLayoutHelper helper = new LinearLayoutHelper();
        return helper;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        final RecommendList recommendList = getDatas().get(i);
        CornerImageView mallServiceImg;
        TextView mallServiceTitle;
        TextView mallServiceMoneyLeft;
        TextView mallServiceMoney;
        TextView mallServiceMoneyOld;
        ImageTextView mallServiceAddress;
        TextView mallServiceDistance;
        mallServiceImg = (CornerImageView) baseHolder.itemView.findViewById(R.id.mall_service_img);
        mallServiceTitle = (TextView) baseHolder.itemView.findViewById(R.id.mall_service_title);
        mallServiceMoneyLeft = (TextView) baseHolder.itemView.findViewById(R.id.mall_service_money_left);
        mallServiceMoney = (TextView) baseHolder.itemView.findViewById(R.id.mall_service_money);
        mallServiceMoneyOld = (TextView) baseHolder.itemView.findViewById(R.id.mall_service_money_old);
        mallServiceAddress = (ImageTextView) baseHolder.itemView.findViewById(R.id.mall_service_address);
        mallServiceDistance = (TextView) baseHolder.itemView.findViewById(R.id.mall_service_distance);
        com.healthy.library.businessutil.GlideCopy.with(context).
                load(recommendList.filePath)
                .placeholder(R.drawable.img_1_1_default2)
                .error(R.drawable.img_1_1_default)
                
                .into(mallServiceImg);
        mallServiceAddress.setVisibility(View.VISIBLE);
        mallServiceAddress.setText(recommendList.shopName + "(" + recommendList.shopAddress + ")");
        if(TextUtils.isEmpty(recommendList.shopAddress)){
            mallServiceAddress.setText(recommendList.shopName);
            if(TextUtils.isEmpty(recommendList.shopName)){
                mallServiceAddress.setVisibility(View.INVISIBLE);
                mallServiceAddress.setText("");

            }
        }
        TextView tagText=baseHolder.itemView.findViewById(R.id.tagText);
        tagText.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(recommendList.getTagFirst())){
            tagText.setVisibility(View.VISIBLE);
            if(recommendList.getTagFirst().length()>3){
                String org=recommendList.getTagFirst();
                String resultOrg=org.substring(0,2)+"\n"+org.substring(2,org.length());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgbig);
                tagText.setText(resultOrg);
            }else {
                tagText.setText(recommendList.getTagFirst());
                tagText.setBackgroundResource(R.drawable.shape_mall_goods_ol_tagbgsmall);
            }
        }
        mallServiceDistance.setText(ParseUtils.parseDistance(recommendList.distance + ""));
        mallServiceDistance.setVisibility(View.GONE);
        mallServiceTitle.setText(recommendList.goodsTitle);
        mallServiceMoney.setText(FormatUtils.moneyKeep2Decimals(recommendList.platformPrice));
        if (Double.parseDouble(recommendList.getRetailPrice()) > 0) {
            mallServiceMoneyOld.setText("¥" + FormatUtils.moneyKeep2Decimals(recommendList.getRetailPrice()));
            mallServiceMoneyOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
//            if(recommendList.retailPrice.equals(recommendList.platformPrice)){
//                mallServiceMoneyOld.setText("");
//            }
        } else {
            mallServiceMoneyOld.setText("");
        }
        baseHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(ServiceRoutes.SERVICE_DETAIL)
                        .withString("shopId", recommendList.shopId)
                        .withString("goodsId", recommendList.goodsId + "")
                        .withString("cityNo", LocUtil.getCityNo(context, SpKey.LOC_CHOSE) + "")
                        .withString("lng", LocUtil.getLongitude(context, SpKey.LOC_CHOSE) + "")
                        .withString("lat", LocUtil.getLatitude(context, SpKey.LOC_CHOSE) + "")
                        .navigation();
            }
        });

    }

    private void initView() {


    }
}
