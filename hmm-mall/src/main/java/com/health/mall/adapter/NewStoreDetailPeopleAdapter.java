package com.health.mall.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.mall.R;
import com.health.mall.model.PeopleListModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewStoreDetailPeopleAdapter extends BaseAdapter<ArrayList<PeopleListModel>> {

    private NewStoreDetialPeopleListAdapter adapter;

    public NewStoreDetailPeopleAdapter() {
        this(R.layout.new_store_detial_people_layout);
    }

    public NewStoreDetailPeopleAdapter(int viewId) {
        super(viewId);
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        RecyclerView peopleRecycle = holder.getView(R.id.peopleRecycle);
        TextView peopleMore = holder.getView(R.id.peopleMore);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        peopleRecycle.setLayoutManager(layoutManager);
        adapter = new NewStoreDetialPeopleListAdapter(context, getDatas().get(0));
        peopleRecycle.setAdapter(adapter);
        peopleMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (moutClickListener!=null){
                  Map nokmap = new HashMap<String, String>();
                  nokmap.put("soure", "门店服务人员“更多”按钮点击量");
                  MobclickAgent.onEvent(context, "event2APPShopDetialPeopleMoreClick", nokmap);
                  moutClickListener.outClick("people",null);
              }
            }
        });
    }
}
