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
import com.healthy.library.model.OrderSub;

import java.util.ArrayList;
import java.util.List;

public class SelectServerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private int mPosition = -1;
    private SetListener getListener;
    private List<OrderSub.ServiceTypeListBean> list = new ArrayList<>();

    public SelectServerAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(OrderSub orderSub) {
        this.list = orderSub.serviceTypeList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_server_list, parent, false);
        return new SelectServerAdapter.ServerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((ServerViewHolder) holder).selectServerTxt.setText(list.get(position).serviceName + "");
        ((ServerViewHolder) holder).selectServerItemRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListener.onClick(position);
                notifyDataSetChanged();

            }
        });
        //选中的服务项目切换背景
        if (position == getmPosition()) {
            ((ServerViewHolder) holder).selectServerTxt.setTextColor(Color.parseColor("#FA3C5A"));
            ((ServerViewHolder) holder).selectServerImg.setImageResource(R.drawable.ic_anonymous_checked);
            ((ServerViewHolder) holder).selectServerItemRel.setBackgroundResource(R.drawable.shape_sub_order_server_checked);
        } else {
            ((ServerViewHolder) holder).selectServerTxt.setTextColor(Color.parseColor("#444444"));
            ((ServerViewHolder) holder).selectServerImg.setImageResource(R.drawable.ic_anonymous_unselected);
            ((ServerViewHolder) holder).selectServerItemRel.setBackgroundResource(R.drawable.shape_sub_order_time_ll);

        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ServerViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout selectServerItem, selectServerItemRel, selectServerItemRemark;
        TextView selectServerTxt;
        ImageView selectServerImg;

        public ServerViewHolder(@NonNull View itemView) {
            super(itemView);
            selectServerItem = itemView.findViewById(R.id.selectServerItem);
            selectServerItemRel = itemView.findViewById(R.id.selectServerItemRel);
            selectServerTxt = itemView.findViewById(R.id.selectServerTxt);
            selectServerImg = itemView.findViewById(R.id.selectServerImg);
            selectServerItemRemark = itemView.findViewById(R.id.selectServerItemRemark);
        }

    }


    public interface SetListener {

        void onClick(int position);
    }


    public void setServerListener(SetListener getListener) {
        this.getListener = getListener;
    }


    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}
