package com.health.servicecenter.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.constant.SpKey;
import com.healthy.library.model.RecommendList;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.FormatUtils;
import com.healthy.library.businessutil.LocUtil;
import com.healthy.library.utils.ScreenUtils;
import com.healthy.library.widget.CornerImageView;
import com.healthy.library.widget.ImageTextView;
import com.healthy.library.widget.ObservableScrollView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MallGoodsDetialChoseAdapter extends BaseAdapter<ArrayList<RecommendList>> {

    private boolean isLoadSuccess;
    private ImageView mRecyclerScroller;
    //0 非webview展示 1webview展示
    private int type = 0;

    public void setType(int type) {
        this.type = type;
    }

    public void setLoadSuccess(boolean loadSuccess) {
        this.isLoadSuccess = loadSuccess;
    }

    @Override
    public int getItemViewType(int position) {
        return 4;
    }

    public MallGoodsDetialChoseAdapter() {
        this(R.layout.service_item_goodsdetail_chose);
    }

    private MallGoodsDetialChoseAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    ObservableScrollView.OnObservableScrollListener onObservableScrollListener = new ObservableScrollView.OnObservableScrollListener() {
        @Override
        public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
            int scrollX = scrollView.getScrollX();
            if (scrollX < (ScreenUtils.getScreenWidth(context) / 5)) {//第一屏幕
                mRecyclerScroller.setImageResource(R.drawable.main_scroller_l);
            } else {
                mRecyclerScroller.setImageResource(R.drawable.main_scroller_r);
            }
        }
    };

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        ArrayList<RecommendList> hlist = getDatas().get(0);
        ConstraintLayout mRecommendLL;
        ConstraintLayout mRecommendLLTop;
        ImageTextView mBasketTitle;
        ImageView mBasketMore;
        ObservableScrollView mRecyclerFun;
        LinearLayout mFunctionGrid;
        mRecommendLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.recommandLL);
        mRecommendLLTop = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.recommandLLTop);
        mBasketTitle = (ImageTextView) baseHolder.itemView.findViewById(R.id.basketTitle);
        mBasketMore = (ImageView) baseHolder.itemView.findViewById(R.id.basketMore);
        mRecyclerFun = (ObservableScrollView) baseHolder.itemView.findViewById(R.id.recycler_fun);
        mFunctionGrid = (LinearLayout) baseHolder.itemView.findViewById(R.id.functionGrid);
        mRecyclerScroller = (ImageView) baseHolder.itemView.findViewById(R.id.recycler_scroller);
        mRecyclerFun.setOnObservableScrollListener(onObservableScrollListener);
        if (hlist.size() > 3) {
            mRecyclerScroller.setVisibility(View.VISIBLE);
        } else {
            mRecyclerScroller.setVisibility(View.GONE);
        }

        //type == 1 时才会触发isLoadSuccess变为true操作进行避免多次绘制
        if (isLoadSuccess) return;
        buildList(mFunctionGrid, hlist);
        //type如果为1 = ServiceGoodsDetail 优化滑动问题，也用于避免影响其他页面
        if (type == 1) {
            isLoadSuccess = true;
        }
        mRecommendLLTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    MobclickAgent.onEvent(context, "event2APPGoodsDetialServiceMoreClick", new SimpleHashMapBuilder<String, String>().puts("soure","商品详情页-服务精选“更多”点击量"));
                    moutClickListener.outClick("更多服务", null);
                }
            }
        });
    }

    private void buildList(LinearLayout mFunctionGrid, ArrayList<RecommendList> hlist) {
        mFunctionGrid.removeAllViews();
        for (int i = 0; i < hlist.size(); i++) {
            final RecommendList recommendList = hlist.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.item_mall_goods_h, mFunctionGrid, false);
            CornerImageView mMallGoodsImg;
            TextView mNumberTmp;
            ImageTextView mMallOnlineTip;
            ConstraintLayout mMallGoodsImgUnder;
            TextView mMallGoodsContext;
            TextView mMallGoodsSpace;
            TextView mMallGoodsMoneyflag;
            TextView mMallGoodsMoneyvalue;
            TextView mMallGoodsSales;
            ImageView mPassbasket;
            HorizontalScrollView mCouponListLL;
            LinearLayout mCouponList;
            TextView mMallCouponValue;
            TextView mMallGoodsSales2;
            TextView mMallGoodsSales2Dis;
            Group mNormalTypeGroup;
            Group mSeachTypeGroup;
            TextView discountTip = view.findViewById(R.id.discountTip);


            mMallGoodsImg = (CornerImageView) view.findViewById(R.id.mall_goods_img);
            mNumberTmp = (TextView) view.findViewById(R.id.numberTmp);
            mMallOnlineTip = (ImageTextView) view.findViewById(R.id.mall_online_tip);
            mMallGoodsImgUnder = (ConstraintLayout) view.findViewById(R.id.mall_goods_img_under);
            mMallGoodsContext = (TextView) view.findViewById(R.id.mall_goods_context);
            mMallGoodsSpace = (TextView) view.findViewById(R.id.mall_goods_space);
            mMallGoodsMoneyflag = (TextView) view.findViewById(R.id.mall_goods_moneyflag);
            mMallGoodsMoneyvalue = (TextView) view.findViewById(R.id.mall_goods_moneyvalue);
            mMallGoodsSales = (TextView) view.findViewById(R.id.mall_goods_sales);
            mPassbasket = (ImageView) view.findViewById(R.id.passbasket);
            mCouponListLL = (HorizontalScrollView) view.findViewById(R.id.couponListLL);
            mCouponList = (LinearLayout) view.findViewById(R.id.couponList);
            mMallCouponValue = (TextView) view.findViewById(R.id.mall_coupon_value);
            mMallGoodsSales2 = (TextView) view.findViewById(R.id.mall_goods_sales2);
            mMallGoodsSales2Dis = (TextView) view.findViewById(R.id.mall_goods_sales2_dis);
            mNormalTypeGroup = (Group) view.findViewById(R.id.normalTypeGroup);
            mSeachTypeGroup = (Group) view.findViewById(R.id.seachTypeGroup);
            if (recommendList != null) {
                discountTip.setVisibility(View.GONE);
                double discount = 1;
                try {
                    discount = Double.parseDouble(recommendList.platformPrice) / Double.parseDouble(recommendList.getRetailPrice());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (discount < 1 && discount > 0) {
                    discountTip.setVisibility(View.VISIBLE);
                    discountTip.setText(String.format("%.1f", discount * 10) + "折");
                }

                com.healthy.library.businessutil.GlideCopy.with(context)
                        .load(recommendList.filePath)
                        .placeholder(R.drawable.img_1_1_default2)
                        .error(R.drawable.img_1_1_default)
                        
                        .into(mMallGoodsImg);
                mMallGoodsContext.setText(recommendList.goodsTitle);
                mMallGoodsMoneyvalue.setText(FormatUtils.moneyKeep2Decimals(recommendList.platformPrice));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map nokmap = new HashMap<String, String>();
                        nokmap.put("soure", "商家详情页“精选服务”专题");
                        MobclickAgent.onEvent(context, "eps_APP_MaternalandChildGoods_CommodityDetails_SelectServices", nokmap);
                        MobclickAgent.onEvent(context, "event2APPGoodsDetialServiceGoodsClick", new SimpleHashMapBuilder<String, String>().puts("soure","商品详情页-服务精选“商品图片/标题”点击量"));
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
                mFunctionGrid.addView(view);
            }

        }
    }

    private void initView() {

    }
}
