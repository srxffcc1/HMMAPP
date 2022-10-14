package com.health.discount.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.discount.R;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.healthy.library.model.MainBlockModel;
import com.healthy.library.widget.ImageTextView;

public class ActZCityBlockAdapter extends BaseAdapter<MainBlockModel> {




    @Override
    public int getItemViewType(int position) {
        return 14;
    }

    public ActZCityBlockAdapter() {
        this(R.layout.dis_item_fragment_cityblock);
    }

    private ActZCityBlockAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
        MainBlockModel mainBlockModel=getDatas().get(i);
         LinearLayout cityPLL;
         ConstraintLayout cityTopLL;
         ImageTextView cityTitle;
         LinearLayout cityTitleLfet;
         ImageTextView cityMore;
         LinearLayout cityLL;
        cityPLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.cityPLL);
        cityTopLL = (ConstraintLayout) baseHolder.itemView.findViewById(R.id.cityTopLL);
        cityTitle = (ImageTextView) baseHolder.itemView.findViewById(R.id.cityTitle);
        cityTitleLfet = (LinearLayout) baseHolder.itemView.findViewById(R.id.cityTitleLfet);
        cityMore = (ImageTextView) baseHolder.itemView.findViewById(R.id.cityMore);
        cityLL = (LinearLayout) baseHolder.itemView.findViewById(R.id.cityLL);
        buildchildItems(cityLL,mainBlockModel);

    }

    private void buildchildItems(LinearLayout goodsList, MainBlockModel item) {
        goodsList.removeAllViews();
        for (int i = 0; i < item.detailList.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.dis_item_city_h, goodsList, false);

            goodsList.addView(view);
        }
    }

    private void initView() {

    }
}
