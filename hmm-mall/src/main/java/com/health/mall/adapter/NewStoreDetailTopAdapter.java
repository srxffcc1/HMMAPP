package com.health.mall.adapter;

import android.graphics.Bitmap;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.health.mall.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.CouponInfoZ;
import com.healthy.library.model.ShopDetailModel;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.utils.DrawableUtils;
import com.healthy.library.widget.CornerImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewStoreDetailTopAdapter extends BaseAdapter<ShopDetailModel> {

    List<CouponInfoZ> list = new ArrayList<>();
    private Bitmap sBitmap;
    private float currentPageScrollMove = -1;

    public NewStoreDetailTopAdapter() {
        this(R.layout.new_store_detial_top_layout);
    }

    public void setCouponList(List<CouponInfoZ> list) {
        this.list = list;
        onDataChange();
        notifyDataSetChanged();
    }

    public NewStoreDetailTopAdapter(int viewId) {
        super(viewId);
    }

    public Bitmap getsBitmap() {
        return sBitmap;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        final ShopDetailModel storeDetialModel = getModel();
        TextView storeName = holder.getView(R.id.storeName);
        TextView storeAim = holder.getView(R.id.storeAim);
        TextView moreCoupon = holder.getView(R.id.moreCoupon);
        TextView couponLable = holder.getView(R.id.couponLable);
        LinearLayout couponList = holder.getView(R.id.couponList);
        ConstraintLayout couponCons = holder.getView(R.id.couponCons);
        CornerImageView storeLogo = holder.getView(R.id.storeLogo);
        couponCons.setVisibility(View.GONE);
        if (storeDetialModel.chainShopName != null && !TextUtils.isEmpty(storeDetialModel.chainShopName)) {
            storeName.setText(storeDetialModel.shopName + "(" + storeDetialModel.chainShopName + ")");
        } else {
            storeName.setText(storeDetialModel.shopName != null ? storeDetialModel.shopName : "");
        }
        if (storeDetialModel.envPicUrl != null && !TextUtils.isEmpty(storeDetialModel.envPicUrl)) {
            final String[] urlArray = storeDetialModel.envPicUrl.split(",");
            com.healthy.library.businessutil.GlideCopy.with(context)
                    .load(urlArray[0])
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_690_260_default)

                    .into(storeLogo);
            setHeadAdapter(holder, urlArray);
        }
        if (list.size() > 0) {
            couponCons.setVisibility(View.VISIBLE);
            couponList.removeAllViews();
            for (int i = 0; i < (list.size() > 3 ? 3 : list.size()); i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_mall_goods_coupon, couponList, false);
                TextView mall_coupon_value = view.findViewById(R.id.mall_coupon_value);
                mall_coupon_value.setText(list.get(i).getCouponNormalName());
                couponList.addView(view);
            }
        } else {
            couponCons.setVisibility(View.GONE);
        }
        moreCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("coupon", null);
                }
            }
        });
        couponList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    moutClickListener.outClick("coupon", null);
                }
            }
        });
    }

    private void setHeadAdapter(BaseHolder baseHolder, final String[] urlArray) {
        final ViewPager pager = baseHolder.itemView.findViewById(R.id.pager_header);
        final TextView tvTotal = baseHolder.itemView.findViewById(R.id.tvTotal);
        tvTotal.setText(urlArray.length + "张");
        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return urlArray.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, final int position) {
                ImageView imageView = new ImageView(context);
                if (position == 0) {
                    com.healthy.library.businessutil.GlideCopy.with(context).load(urlArray[position])
                            .placeholder(R.drawable.img_1_1_default2)
                            .error(R.drawable.img_1_1_default)

                            .into(new SimpleTarget<Drawable>() {

                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    sBitmap = DrawableUtils.drawableToBitmap(resource);
                                }
                            });
                }
                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(urlArray[position])
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_690_260_default)

                        .into(imageView);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                container.addView(imageView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "门店详情页商品图片/标题的点击量");
                        MobclickAgent.onEvent(context, "event2APPShopDetialImgClick", nokmap);
                        ARouter.getInstance().build(LibraryRoutes.LIBRARY_PHOTO_DETAIL)
                                .withCharSequenceArray("urls", urlArray)
                                .withInt("pos", position)
                                .navigation();
                    }
                });
                return imageView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
    }
}
