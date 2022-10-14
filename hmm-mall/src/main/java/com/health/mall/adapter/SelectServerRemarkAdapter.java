package com.health.mall.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.health.mall.R;

import java.util.ArrayList;
import java.util.List;

public class SelectServerRemarkAdapter extends RecyclerView.Adapter<SelectServerRemarkAdapter.RemarkViewHolder> {
    private Context mContext;
    private int mPosition = -1;
    private RemarkItemListener listener;
    private String[] tags;

    public SelectServerRemarkAdapter(Context context, String[] tag) {
        this.mContext = context;
        this.tags = tag;
    }


    @NonNull
    @Override
    public SelectServerRemarkAdapter.RemarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_server_list, parent, false);
        return new SelectServerRemarkAdapter.RemarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemarkViewHolder holder, final int position) {
        holder.selectServerTxt.setText(tags[position]);
        holder.selectServerItemRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
                //Toast.makeText(mContext,"点击了："+getmPosition()+"-"+position,Toast.LENGTH_LONG).show();
                notifyDataSetChanged();

            }
        });
        //选中的服务项目切换背景
        if (position == getmPosition()) {
            holder.selectServerTxt.setTextColor(Color.parseColor("#FA3C5A"));
            holder.selectServerImg.setImageResource(R.drawable.ic_anonymous_checked);
            holder.selectServerItemRel.setBackgroundResource(R.drawable.shape_sub_order_server_checked);
        } else {
            holder.selectServerTxt.setTextColor(Color.parseColor("#444444"));
            holder.selectServerImg.setImageResource(R.drawable.ic_anonymous_unselected);
            holder.selectServerItemRel.setBackgroundResource(R.drawable.shape_sub_order_time_ll);

        }
    }


    @Override
    public int getItemCount() {
        return tags == null ? 0 : tags.length;
    }

    public class RemarkViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout selectServerItem, selectServerItemRel, selectServerItemRemark;
        TextView selectServerTxt;
        ImageView selectServerImg;

        public RemarkViewHolder(@NonNull View itemView) {
            super(itemView);
            selectServerItem = itemView.findViewById(R.id.selectServerItem);
            selectServerItemRel = itemView.findViewById(R.id.selectServerItemRel);
            selectServerTxt = itemView.findViewById(R.id.selectServerTxt);
            selectServerImg = itemView.findViewById(R.id.selectServerImg);
            selectServerItemRemark = itemView.findViewById(R.id.selectServerItemRemark);
        }
    }

    public interface RemarkItemListener {

        void onClick(int position);
    }


    public void setRemarkListener(RemarkItemListener getListener) {
        this.listener = getListener;
    }


    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}
