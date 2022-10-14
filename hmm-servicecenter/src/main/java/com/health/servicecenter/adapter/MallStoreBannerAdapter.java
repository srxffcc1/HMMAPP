package com.health.servicecenter.adapter;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.loader.ImageNetAdapter;
import com.healthy.library.model.AdModel;
import com.healthy.library.presenter.AdPresenterCopy;
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

public class MallStoreBannerAdapter extends BaseAdapter<ArrayList<AdModel>> {
    private boolean isAdd = false;
    AdPresenterCopy adPresenterCopy;
    public MallStoreBannerAdapter(int viewId) {
        super(viewId);
    }

    public MallStoreBannerAdapter() {
        this(R.layout.mall_top_banner_layout);
        isAdd = false;
        adPresenterCopy = new AdPresenterCopy(context);
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        Banner banner = holder.getView(R.id.mall_top_banner);
        final List<AdModel> adModels = getDatas().get(0);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < adModels.size(); i++) {
            String url = adModels.get(i).photoUrls;
            data.add(url);
        }
        banner.setAdapter(new ImageNetAdapter(data, TransformUtil.dp2px(context, 10f), null))
                .setIndicator(new RectangleIndicator(context))
                .setIndicatorGravity(IndicatorConfig.Direction.RIGHT);//设置指示器位置（左，中，右）
        if (!isAdd) {
            isAdd = true;
            banner.stop().start();
        }
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(Object data, int position) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "母婴商品banner");
                MobclickAgent.onEvent(context, "eps_APP_MaternalandChildGoods_banner", nokmap);
                AdModel adModel = adModels.get(position);
                MARouterUtils.passToTarget(context,adModel);

            }

            @Override
            public void onBannerChanged(int position) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "母婴商品banner");
                MobclickAgent.onEvent(context, "eps_APP_MaternalandChildGoods_banner", nokmap);
            }
        });

    }

}
