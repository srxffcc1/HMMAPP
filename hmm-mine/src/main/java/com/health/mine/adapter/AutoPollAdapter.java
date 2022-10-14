package com.health.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.health.mine.R;
import com.health.mine.model.HanMomInfoModel;
import com.healthy.library.widget.CornerImageView;

import java.util.List;

public class AutoPollAdapter extends RecyclerView.Adapter<AutoPollAdapter.BaseViewHolder> {
    private final Context mContext;
    private final List<HanMomInfoModel.ScrollListModel> mData;

    public AutoPollAdapter(Context context, List<HanMomInfoModel.ScrollListModel> list) {
        this.mContext = context;
        this.mData = list;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.han_mom_liner_bottom, parent, false);
        BaseViewHolder holder = new BaseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.content.setText(mData.get(position % mData.size()).content);
        try {
            com.healthy.library.businessutil.GlideCopy.with(mContext)
                    .load(mData.get(position % mData.size()).memberAvatarUrl)
                    .placeholder(R.drawable.img_1_1_default2)
                    .error(R.drawable.img_avatar_default)

                    .into(holder.userImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        CornerImageView userImg;
        public BaseViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.userNickName);
            userImg = itemView.findViewById(R.id.userImg);
        }
    }
}
