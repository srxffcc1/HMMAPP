package com.health.discount.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.discount.R;
import com.health.discount.model.PointTab;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.loader.ImageNetAdapter;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.ColorInfo;
import com.healthy.library.routes.ServiceRoutes;
import com.healthy.library.utils.GlideOptions;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.TransformUtil;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class PointBlockHomeBlockAdapter extends BaseAdapter<PointTab> {


    private List<AdModel> adModelList;

    public PointBlockHomeBlockAdapter() {
        this(R.layout.dis_item_activity_point_block);
    }

    public PointBlockHomeBlockAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
        ConstraintLayout blockTitleLL;
        TextView blockTitle;
        GridLayout blockGrid;
        ConstraintLayout parentCategory;
        ImageView blockGoodsIcon;
        TextView blockGoodsTitle;
        TextView blockGoodsPoint;
        Banner actTopBanner;
        ConstraintLayout blockPP = holder.itemView.findViewById(R.id.blockPP);
        actTopBanner = (Banner) holder.itemView.findViewById(R.id.act_top_banner);
        blockTitleLL = (ConstraintLayout) holder.itemView.findViewById(R.id.blockTitleLL);
        blockTitle = (TextView) holder.itemView.findViewById(R.id.blockTitle);
        blockGrid = (GridLayout) holder.itemView.findViewById(R.id.blockGrid);
        final PointTab pointTab = getDatas().get(position);
        blockTitle.setText(pointTab.themeName);
        blockTitleLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MobclickAgent.onEvent(context, "event2APPPointsShopMoreClick", new SimpleHashMapBuilder<String, String>().puts("soure", "积分商城首页专区右上角“更多”点击量"));
                if (moutClickListener != null) {
                    moutClickListener.outClick("专区列表", new String[]{pointTab.id, pointTab.themeName});
                }
            }
        });
//        if(pointTab.pointGoodsList!=null&&pointTab.pointGoodsList.size()>0){
//            blockPP.setVisibility(View.VISIBLE);
//        }else {
//            blockPP.setVisibility(View.GONE);
//        }
        addImages(context, blockGrid, pointTab.goodsList);
        actTopBanner.setVisibility(View.GONE);
        if (position == 0 && adModelList != null && adModelList.size() > 0) {
            actTopBanner.setVisibility(View.VISIBLE);
            buildBannerView(actTopBanner, adModelList);
        }
    }

    private int mMargin;

    private void addImages(final Context context, final GridLayout gridLayout, final List<PointTab.PointGoods> urls) {
        if (mMargin == 0) {
            mMargin = (int) TransformUtil.dp2px(context, 2);
        }
        gridLayout.postDelayed(new Runnable() {


            @Override
            public void run() {
                gridLayout.removeAllViews();
                if (urls != null && urls.size() > 0) {
                    int row = 3;
                    gridLayout.setColumnCount(row);
                    int w = (gridLayout.getWidth() - gridLayout.getPaddingLeft() - gridLayout.getPaddingRight() - (2 + 2 * (row - 1)) * mMargin) / row;
//                    //System.out.println("选项卡列表2:" + gridLayout.getWidth());
                    for (int i = 0; i < urls.size(); i++) {
                        final PointTab.PointGoods pointGoods = urls.get(i);
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = w;
//                        //System.out.println("选项卡列表2:" + params.width);
                        params.setMargins(mMargin, mMargin, mMargin, mMargin);
                        View imageparent = LayoutInflater.from(context).inflate(R.layout.dis_function_block, gridLayout, false);

                        TextView blockGoodsPoint;
                        TextView blockGoodsTitle;
                        ImageView blockGoodsIcon;
                        ConstraintLayout parentCategory;
                        parentCategory = (ConstraintLayout) imageparent.findViewById(R.id.parent_category);
                        blockGoodsIcon = (ImageView) imageparent.findViewById(R.id.blockGoodsIcon);
                        blockGoodsTitle = (TextView) imageparent.findViewById(R.id.blockGoodsTitle);
                        blockGoodsPoint = (TextView) imageparent.findViewById(R.id.blockGoodsPoint);

                        com.healthy.library.businessutil.GlideCopy.with(context)
                                .load(pointGoods.filePath)
                                .error(R.drawable.img_1_1_default2)
                                .apply(GlideOptions.withRoundedOptions((int) TransformUtil.dp2px(context, 5f), RoundedCornersTransformation.CornerType.TOP, R.drawable.img_690_260_default, R.drawable.img_690_260_default))
                                .placeholder(R.drawable.img_1_1_default2)
                                .into(blockGoodsIcon);
                        blockGoodsTitle.setText(pointGoods.goodsTitle);
                        blockGoodsPoint.setText(pointGoods.getPointsRealPrice());
                        imageparent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ARouter.getInstance()
                                        .build(ServiceRoutes.SERVICE_DETAIL)
                                        .withString("id", pointGoods.id)
                                        .withString("marketingType", "5")
                                        .navigation();
                            }
                        });
                        gridLayout.addView(imageparent, params);
                    }
                }
            }
        }, 100);

    }

    public void setAdModelList(List<AdModel> adModelList) {
        this.adModelList = adModelList;
    }

    public List<AdModel> getAdModelList() {
        return adModelList;
    }

    private void buildBannerView(Banner banner, final List<AdModel> bannerimgs) {
        List<ColorInfo> colorList = new ArrayList<>();
        ImageNetAdapter imageLoader;
        int count;
        if (bannerimgs != null && bannerimgs.size() > 0) {
            colorList.clear();
            count = bannerimgs.size();
            for (int j = 0; j < bannerimgs.size(); j++) {
                ColorInfo info = new ColorInfo();
                info.setImgUrl(bannerimgs.get(j).photoUrls);
                colorList.add(info);

            }
            for (int j = 0; j < colorList.size(); j++) {
                try {
                    colorList.get(j).setPerfectColor(Color.parseColor(bannerimgs.get(j).colorValue));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            imageLoader = new ImageNetAdapter(new SimpleArrayListBuilder<String>().putList(bannerimgs, new ObjectIteraor<AdModel>() {
                @Override
                public Object getDesObj(AdModel adModel) {
                    return adModel.photoUrls;
                }
            }), TransformUtil.dp2px(context, 10f), colorList);
            banner.setAdapter(imageLoader)
                    .setIndicator(new RectangleIndicator(context))
                    .setIndicatorGravity(IndicatorConfig.Direction.RIGHT);//设置指示器位置（左，中，右）
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(Object data, int position) {
                    AdModel adModel = bannerimgs.get(position);
                    MARouterUtils.passToTarget(context,adModel);
                }

                @Override
                public void onBannerChanged(int position) {

                }
            });
            //System.out.println("修改背景版333");
//            banner.setIndicator(new CircleIndicator(mContext));
            banner.stop();
            banner.start();

        }
    }
}
