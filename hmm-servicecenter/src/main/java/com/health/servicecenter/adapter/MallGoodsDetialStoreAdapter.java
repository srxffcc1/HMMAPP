package com.health.servicecenter.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.routes.MallRoutes;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.utils.NavigateUtils;
import com.healthy.library.utils.ParseUtils;
import com.healthy.library.utils.TransformUtil;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.umeng.analytics.MobclickAgent;

public class MallGoodsDetialStoreAdapter extends BaseAdapter<ShopDetailModel> {


    private boolean onlyOnePiece = true;

    //true 服务预约详情 false 商品详情
    private boolean isAppointmentDetail = false;

    public void setAppointmentDetail(boolean isAppointmentDetail) {
        this.isAppointmentDetail = isAppointmentDetail;
    }

    @Override
    public int getItemViewType(int position) {
        return 64;
    }

    public MallGoodsDetialStoreAdapter() {
        this(R.layout.service_item_goodsdetail_store);
    }

    private MallGoodsDetialStoreAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        ConstraintLayout storeTop;
        ImageTextView storeName;
        ImageTextView storeTip;
        ImageView storeMore;
        HorizontalScrollView scrollImg;
        LinearLayout layoutIv;
        ImageTextView locDetail;
        ImageTextView locMore;
        storeTop = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.storeTop);
        storeName = (ImageTextView) baseHolder.itemView.findViewById(R.id.storeName);
        storeTip = (ImageTextView) baseHolder.itemView.findViewById(R.id.storeTip);
        storeMore = (ImageView) baseHolder.itemView.findViewById(R.id.storeMore);
        scrollImg = (HorizontalScrollView) baseHolder.itemView.findViewById(R.id.scroll_img);
        layoutIv = (LinearLayout) baseHolder.itemView.findViewById(R.id.layout_iv);
        locDetail = (ImageTextView) baseHolder.itemView.findViewById(R.id.locDetail);
        locMore = (ImageTextView) baseHolder.itemView.findViewById(R.id.locMore);
        ImageTextView mNavigation = baseHolder.getView(R.id.navigation);
        ImageTextView mPhone = baseHolder.getView(R.id.phone);


        final ShopDetailModel shopDetailModel = getModel();
        if (shopDetailModel != null) {
            if (onlyOnePiece) {
                storeTip.setText("营业资质");
                storeTip.setDrawable(R.drawable.goodsorderstoreright, context);
            } else {
                storeTip.setText("可选门店");
                storeTip.setDrawable(R.drawable.ic_no, context);
            }
            storeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobclickAgent.onEvent(context, "event2APPGoodsDetialGoShopDetialClick", new SimpleHashMapBuilder<String, String>().puts("soure","点击门店进入门店详情页点击量"));
                    ARouter.getInstance()
                            .build(SecondRoutes.SECOND_SHOP_DETAIL)
                            .withString("shopId", shopDetailModel.id + "")
                            .withString("merchantId", shopDetailModel.userId)
                            .navigation();
                }
            });
            storeTip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onlyOnePiece) {
                        MobclickAgent.onEvent(context, "event2APPGoodsDetialGoShopDetialClick", new SimpleHashMapBuilder<String, String>().puts("soure","点击门店进入门店详情页点击量"));
                        ARouter.getInstance()
                                .build(SecondRoutes.SECOND_SHOP_DETAIL)
                                .withString("shopId", shopDetailModel.id + "")
                                .navigation();
                    } else {
                        if (moutClickListener != null) {
                            MobclickAgent.onEvent(context, "event2APPGoodsDetialChangeServiceClick", new SimpleHashMapBuilder<String, String>().puts("soure","服务商品详情页-切换适用门店切换量"));
                            moutClickListener.outClick("可选门店", null);
                        }
                    }
                }
            });
            storeName.setText(shopDetailModel.shopName);
            if (!TextUtils.isEmpty(shopDetailModel.chainShopName)) {

                storeName.setText(shopDetailModel.shopName + "(" + shopDetailModel.chainShopName + ")");
            }

            baseHolder.setVisibility(R.id.locMore, isAppointmentDetail ? View.GONE : View.VISIBLE);
            baseHolder.setVisibility(R.id.phone, isAppointmentDetail ? View.VISIBLE : View.GONE);
            //3.2 服务预约隐藏电话按钮
            //baseHolder.setVisibility(android.R.id.tabs, isAppointmentDetail ? View.VISIBLE : View.GONE);
            baseHolder.setVisibility(R.id.navigation, isAppointmentDetail ? View.VISIBLE : View.GONE);
            String address = shopDetailModel.cityName + shopDetailModel.addressDetails;

            if (isAppointmentDetail) {
                if (shopDetailModel.distance > 0) {
                    address += " " + ParseUtils.parseDistance(shopDetailModel.distance + "");
                }

                mNavigation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavigateUtils.navigate(context, shopDetailModel.addressDetails,
                                shopDetailModel.latitude + "", shopDetailModel.longitude + "");
                    }
                });
                mPhone.setVisibility(View.GONE);
                /*mPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhoneUtils.callPhone(context, newStoreDetialModel.appointmentPhone);
                    }
                });*/

            } else {
                if (shopDetailModel.distance > 0) {
                    locMore.setText(ParseUtils.parseDistance(shopDetailModel.distance + ""));
                } else {
                    locMore.setText("");
                }
                baseHolder.itemView.findViewById(R.id.locLL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavigateUtils.navigate(context, shopDetailModel.addressDetails,
                                shopDetailModel.latitude + "", shopDetailModel.longitude + "");
                    }
                });
            }
            locDetail.setText(address);

            setTopImgList(shopDetailModel, layoutIv);
        }
    }

    private void setTopImgList(ShopDetailModel goodsModel, LinearLayout layoutIv) {
        layoutIv.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) TransformUtil.dp2px(context, 140),
                (int) TransformUtil.dp2px(context, 105)
        );
        if (goodsModel.envPicUrl != null) {
            final String[] urls = new String[goodsModel.envPicUrl.split(",").length];
            for (int i = 0; i < urls.length; i++) {
                String bean = goodsModel.envPicUrl.split(",")[i];
                urls[i] = bean;
            }
            for (int i = 0; i < urls.length; i++) {
                final int pos = i;
                String bean = urls[i];
                String url = bean;
                CornerImageView iv = new CornerImageView(context);
                iv.setCornerRadius(TransformUtil.dp2px(context, 3));
                layoutIv.addView(iv, params);
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(url)
                        .placeholder(R.drawable.img_1_1_default)
                        .error(R.drawable.img_1_1_default)

                        .into(iv);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                                .withCharSequenceArray("urls", urls)
                                .withInt("pos", pos)
                                .navigation();
                    }
                });
            }
        }
    }

    public void setOnlyOnePiece(boolean onlyOnePiece) {
        this.onlyOnePiece = onlyOnePiece;
    }

    public boolean getOnlyOnePiece() {
        return onlyOnePiece;
    }
}
