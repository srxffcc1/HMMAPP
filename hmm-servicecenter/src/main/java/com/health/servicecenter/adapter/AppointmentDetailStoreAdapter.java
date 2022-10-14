package com.health.servicecenter.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.AppointmentMainItemModel;
import com.healthy.library.utils.GlideOptions;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.ImageTextView;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class AppointmentDetailStoreAdapter extends BaseAdapter<AppointmentMainItemModel> {

    public AppointmentDetailStoreAdapter() {
        this(R.layout.appointment_detial_store_adapter_layout);
    }

    public AppointmentDetailStoreAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
       /* ConstraintLayout specLayout;
        TextView title;
        TextView payTypeTitle;*/
        TextView payType;
        TextView cancleTitle;
        TextView cancle;
       /* ConstraintLayout storeTop;
        ImageTextView storeName;
        ImageTextView storeTip;
        ImageView storeMore;
        HorizontalScrollView scrollImg;
        LinearLayout layoutIv;
        ConstraintLayout locLL;
        ImageTextView locDetail;
        TextView locMore;
        ConstraintLayout navigationLayout;
        ImageTextView navigation;
        ImageTextView phone;*/

      /*  specLayout = (ConstraintLayout) holder.getView(R.id.specLayout);
        title = (TextView) holder.getView(R.id.title);
        payTypeTitle = (TextView) holder.getView(R.id.payTypeTitle);*/
        payType = (TextView) holder.getView(R.id.payType);
        cancleTitle = (TextView) holder.getView(R.id.cancleTitle);
        cancle = (TextView) holder.getView(R.id.cancle);
        /*storeTop = (ConstraintLayout) holder.getView(R.id.storeTop);
        storeName = (ImageTextView) holder.getView(R.id.storeName);
        storeTip = (ImageTextView) holder.getView(R.id.storeTip);
        storeMore = (ImageView) holder.getView(R.id.storeMore);
        scrollImg = (HorizontalScrollView) holder.getView(R.id.scroll_img);
        layoutIv = (LinearLayout) holder.getView(R.id.layout_iv);
        locLL = (ConstraintLayout) holder.getView(R.id.locLL);
        locDetail = (ImageTextView) holder.getView(R.id.locDetail);
        locMore = (TextView) holder.getView(R.id.locMore);
        navigationLayout = (ConstraintLayout) holder.getView(R.id.navigationLayout);
        navigation = (ImageTextView) holder.getView(R.id.navigation);
        phone = (ImageTextView) holder.getView(R.id.phone);*/

        final AppointmentMainItemModel model = getModel();

        //支付类型（1:在线支付 2:到店支付）
        payType.setText(1 == model.getPayType() ? "到店付款" : "在线支付");

        if (1 == model.getSupportRefund()) {
            //cancleTitle.setVisibility(View.VISIBLE);
            //cancle.setVisibility(View.VISIBLE);
            String advanceCancelUnit = model.getAdvanceCancelUnit();
            if ("1".equals(advanceCancelUnit)) advanceCancelUnit = "小时";
            if ("2".equals(advanceCancelUnit)) advanceCancelUnit = "天";
            cancle.setText("到店前" + model.getAdvanceCancelNumber() + advanceCancelUnit + "可取消");
        } else {
            //cancleTitle.setVisibility(View.GONE);
            //cancle.setVisibility(View.GONE);
            cancle.setText("不支持取消");
        }

        /*phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        buildGoodsHlist(layoutIv);*/

    }

    /**
     * 服务商品详情 店铺信息图片设置
     *
     * @param goodsHList
     */
    private void buildGoodsHlist(LinearLayout goodsHList) {
        goodsHList.removeAllViews();
        for (int i = 0; i < 10; i++) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_appointment_detial_image_layout, goodsHList, false);

            ImageView topImgTagIcon = view.findViewById(R.id.item_detial_iv);

            Drawable drawable;

            if (i % 2 == 0) {
                drawable = context.getResources().getDrawable(R.drawable.index_header_pregnancy_top);
            } else {
                drawable = context.getResources().getDrawable(R.drawable.index_header_parenting_top);
            }
            com.healthy.library.businessutil.GlideCopy.with(context).load(drawable)
                    .placeholder(R.drawable.img_1_1_default)
                    .apply(GlideOptions.withRoundedOptions((int) TransformUtil.dp2px(context, 12f),
                            RoundedCornersTransformation.CornerType.TOP,
                            R.drawable.img_1_1_default, R.drawable.img_1_1_default))
                    .error(R.drawable.img_1_1_default)

                    .into(topImgTagIcon);

            goodsHList.addView(view);

            //buildchild5items(goodsList, itemchild);
        }
    }
}
