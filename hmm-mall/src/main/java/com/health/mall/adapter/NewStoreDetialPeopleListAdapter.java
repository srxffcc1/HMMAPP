package com.health.mall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.health.mall.R;
import com.health.mall.model.PeopleListModel;
import com.healthy.library.routes.MallRoutes;
import com.healthy.library.widget.CornerFitCenterImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewStoreDetialPeopleListAdapter extends RecyclerView.Adapter<NewStoreDetialPeopleListAdapter.MallStoreNearbyListHolder> {
    private Context mContext;
    List<PeopleListModel> data = new ArrayList<>();

    public NewStoreDetialPeopleListAdapter(Context mContext, List<PeopleListModel> listModels) {
        this.mContext = mContext;
        this.data = listModels;
    }

    @NonNull
    @Override
    public MallStoreNearbyListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_store_list_people_layout, parent, false);
        return new MallStoreNearbyListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MallStoreNearbyListHolder holder, final int position) {

        final PeopleListModel model = data.get(position);
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(model.faceUrl)
                .into(holder.peopleImg);
        holder.peopleName.setText(model.realName);
        if (model.tagTechnician == null || TextUtils.isEmpty(model.tagTechnician)) {
            holder.peopleLable.setVisibility(View.GONE);
        } else {
            holder.peopleLable.setVisibility(View.VISIBLE);
            holder.peopleLable.setText(model.tagTechnician);
        }
        holder.peopleYear.setText(model.positionTechnician);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "门店服务人员图片点击量");
                MobclickAgent.onEvent(mContext, "event2APPShopDetialPeopleImgClick", nokmap);
                ARouter.getInstance()
                        .build(MallRoutes.MALL_STORE_MAN_DETAIL)
                        .withString("userId", model.id)
                        .navigation();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class MallStoreNearbyListHolder extends RecyclerView.ViewHolder {
        private CornerFitCenterImageView peopleImg;
        private TextView peopleName, peopleLable, peopleYear;

        public MallStoreNearbyListHolder(@NonNull View itemView) {
            super(itemView);
            peopleImg = itemView.findViewById(R.id.peopleImg);
            peopleName = itemView.findViewById(R.id.peopleName);
            peopleLable = itemView.findViewById(R.id.peopleLable);
            peopleYear = itemView.findViewById(R.id.peopleYear);
        }
    }
}
