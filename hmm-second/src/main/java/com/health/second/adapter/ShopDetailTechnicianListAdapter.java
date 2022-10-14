package com.health.second.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.health.second.R;
import com.health.second.model.PeopleListModel;
import com.healthy.library.routes.SecondRoutes;
import com.healthy.library.widget.CornerFitCenterImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopDetailTechnicianListAdapter extends RecyclerView.Adapter<ShopDetailTechnicianListAdapter.ViewHolder> {
    private Context mContext;
    private String shopId;
    List<PeopleListModel> data = new ArrayList<>();

    public ShopDetailTechnicianListAdapter(Context mContext, List<PeopleListModel> listModels,String shopId) {
        this.mContext = mContext;
        this.data = listModels;
        this.shopId = shopId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_store_people_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final PeopleListModel model = data.get(position);
        com.healthy.library.businessutil.GlideCopy.with(mContext)
                .load(model.faceUrl)
                .into(holder.peopleImg);
        holder.peopleName.setText(model.realName);
        if (model.tagTechnician == null || TextUtils.isEmpty(model.tagTechnician)) {
            holder.peopleTag.setVisibility(View.GONE);
        } else {
            holder.peopleTag.setVisibility(View.VISIBLE);
            holder.peopleTag.setText(model.tagTechnician.split(",")[0]);
        }
        //holder.peopleYear.setText(model.positionTechnician);
        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map nokmap = new HashMap<String, String>();
                nokmap.put("soure", "门店服务人员图片点击量");
                MobclickAgent.onEvent(mContext, "event2APPShopDetialPeopleImgClick", nokmap);
                ARouter.getInstance()
                        .build( SecondRoutes.SECOND_TECHNICIAN_DETAIL)
                        .withString("shopId", shopId)
                        .withInt("pos", pos)
                        .navigation();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CornerFitCenterImageView peopleImg;
        private TextView peopleName, peopleLable, peopleYear, peopleTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            peopleImg = itemView.findViewById(R.id.peopleImg);
            peopleName = itemView.findViewById(R.id.peopleName);
            peopleLable = itemView.findViewById(R.id.peopleLable);
            peopleYear = itemView.findViewById(R.id.peopleYear);
            peopleTag = itemView.findViewById(R.id.peopleTag);
        }
    }
}
