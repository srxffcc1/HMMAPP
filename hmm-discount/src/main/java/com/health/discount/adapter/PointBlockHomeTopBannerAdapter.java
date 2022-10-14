package com.health.discount.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.loader.ImageNetAdapter;
import com.healthy.library.model.AdModel;
import com.healthy.library.model.ColorInfo;
import com.healthy.library.presenter.AdPresenterCopy;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.healthy.library.builder.SimpleHashMapBuilder;
import com.healthy.library.businessutil.ChannelUtil;
import com.healthy.library.utils.MARouterUtils;
import com.healthy.library.utils.TransformUtil;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Li
 * @date 2019/03/25 11:03
 * @des 推荐阅读
 */

public class PointBlockHomeTopBannerAdapter extends BaseAdapter<String> {
    private List<AdModel> adModelList;


    public PointBlockHomeTopBannerAdapter() {
        this(R.layout.dis_item_activity_point_top_banner);
    }

    public PointBlockHomeTopBannerAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, final int position) {
         Banner actTopBanner;
        actTopBanner = (Banner) holder.itemView.findViewById(R.id.act_top_banner);
        if(adModelList!=null&&adModelList.size()>0){
            buildBannerView(actTopBanner,adModelList);
        }
        Map nokmap = new HashMap<String, String>();
        nokmap.put("source", "各轮播图、广告图曝光量");
        MobclickAgent.onEvent(context, "event2APPShopBannerShow", nokmap);
        MobclickAgent.onEvent(context, "event2APPPointsShopBannerShow", new SimpleHashMapBuilder<String, String>().puts("source", "积分商城banner曝光量"));
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
//                    e.printStackTrace();
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
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "各轮播图、广告图曝光量");
                    MobclickAgent.onEvent(context, "event2APPShopBannerShow", nokmap);
                    MobclickAgent.onEvent(context, "event2APPPointsShopBannerClick", new SimpleHashMapBuilder<String, String>().puts("source", "积分商城banner位点击量"));
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

    private void initView() {
    }
}
