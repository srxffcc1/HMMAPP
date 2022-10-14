package com.health.second.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.health.second.R;
import com.health.second.model.PeopleListModel;
import com.healthy.library.base.BaseAdapter;
import com.healthy.library.base.BaseHolder;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建日期：2021/10/12 15:49
 *
 * @author LiuWei
 * @version 1.0
 * 包名： com.health.second.adapter
 * 类说明：
 */

public class ShopDetailTechnicianAdapter extends BaseAdapter<ArrayList<PeopleListModel>> {
    private String shopId;

    private ShopDetailTechnicianListAdapter adapter;

    public ShopDetailTechnicianAdapter() {
        this(R.layout.item_shop_detail_technician_adapter_layout);
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public ShopDetailTechnicianAdapter(int viewId) {
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
        adapter = new ShopDetailTechnicianListAdapter(context, getDatas().get(0), shopId);
        peopleRecycle.setAdapter(adapter);
        peopleMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moutClickListener != null) {
                    Map nokmap = new HashMap<String, String>();
                    nokmap.put("soure", "门店服务人员“更多”按钮点击量");
                    MobclickAgent.onEvent(context, "event2APPShopDetialPeopleMoreClick", nokmap);
                    moutClickListener.outClick("people", null);
                }
            }
        });
    }
}
