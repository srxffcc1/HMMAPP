package com.health.servicecenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.MarqueeView;
import com.health.servicecenter.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.widget.AutoPollRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MallGoodsDetialUserHeadAdapter extends BaseAdapter<String> {


    AutoPollHeadAdapter autoPollHeadAdapter;
//    AutoPollNameAdapter autoPollNameAdapter;
    boolean isStart = false;
    private MarqueeView marqueeView;

    @Override
    public int getItemViewType(int position) {
        return 410;
    }

    public MallGoodsDetialUserHeadAdapter() {
        this(R.layout.service_item_goodsdetail_userhead);
    }

    private MallGoodsDetialUserHeadAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        LinearLayout bottomLinearLayout;
        AutoPollRecyclerView autoPollRecyclerView;
        marqueeView = (MarqueeView) baseHolder.itemView.findViewById(R.id.marqueeView);
        bottomLinearLayout = (LinearLayout) baseHolder.itemView.findViewById(R.id.bottomLinearLayout);
        autoPollRecyclerView = (AutoPollRecyclerView) baseHolder.itemView.findViewById(R.id.autoPollRecyclerView);
        autoPollRecyclerView.setLayoutManager(new GridLayoutManager(context, 6));//设置LinearLayoutManager.HORIZONTAL  则水平滚动
//        autoPollRecyclerRightView.setLayoutManager(new LinearLayoutManager(context));
        autoPollRecyclerView.setTIME_AUTO_POLL(10);
//        autoPollRecyclerRightView.setDxy(20, 2);
        List<String> list = new ArrayList<>();
        for (int j = 0; j < 50; j++) {
            list.add(j + "");
        }
        autoPollHeadAdapter = new AutoPollHeadAdapter(context, list);

        List<String> list2 = new ArrayList<>();
        for (int j = 0; j < 50; j++) {
            list2.add(j + "");
        }
        MarqueeFactory marqueeFactory=new ComplexViewMF(context);
        marqueeFactory.setData(list2);
        marqueeView.setMarqueeFactory(marqueeFactory);
//        autoPollNameAdapter = new AutoPollNameAdapter(context, list2);
        autoPollRecyclerView.setAdapter(autoPollHeadAdapter);
        if (!isStart) {
            autoPollRecyclerView.start();
            marqueeView.startFlipping();
        }
        isStart = true;
    }
    public class ComplexViewMF extends MarqueeFactory<View, String> {
        private LayoutInflater inflater;

        public ComplexViewMF(Context mContext) {
            super(mContext);
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public View generateMarqueeItemView(String data) {
            View mView = (View) inflater.inflate(R.layout.service_item_goodsdetail_frameman, null);
            return mView;
        }
    }
}
