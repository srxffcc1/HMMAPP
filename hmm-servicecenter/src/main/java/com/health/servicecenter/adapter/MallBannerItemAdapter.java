package com.health.servicecenter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.health.servicecenter.R;
import com.healthy.library.loader.ImageBgHolder;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

public class MallBannerItemAdapter extends BannerAdapter<String, BaseViewHolder> {


    public MallBannerItemAdapter(List<String> datas) {
        super(datas);
    }

    @Override
    public BaseViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mall_goods_service_banneritem,parent,false);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindView(BaseViewHolder holder, String data, int position, int size) {

    }
}
