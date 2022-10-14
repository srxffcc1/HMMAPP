package com.health.servicecenter.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.health.servicecenter.R;
import com.healthy.library.model.AppointmentTimeSettingModel;

import java.util.List;

public class SelectServerDialogTimeAdapter extends RecyclerView.Adapter<SelectServerDialogTimeAdapter.ServerViewHolder> {
    private Context mContext;
    private int mPosition = -1;
    private SetListener getListener;
    private List<AppointmentTimeSettingModel.ShopTimeSetting> list;

    public SelectServerDialogTimeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<AppointmentTimeSettingModel.ShopTimeSetting> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectServerDialogTimeAdapter.ServerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_server_dialog_time, parent, false);
        return new ServerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectServerDialogTimeAdapter.ServerViewHolder holder, final int position) {
        String dateStr = list.get(position).getDateStr();
        holder.selectServerTxt.setText(dateStr);
        holder.unSelectServerTxt.setText(dateStr);
        if (list.get(position).isDisabled()) {//禁用
            holder.unSelectServerItemRel.setVisibility(View.VISIBLE);
            holder.selectServerItemRel.setVisibility(View.GONE);
            holder.unSelectServerFullTxt.setVisibility(View.GONE);
            holder.unSelectServerItemRel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "该时间段不可预约，请选择其他时间段", Toast.LENGTH_SHORT).show();
                }
            });
        } else {//启用
            if (list.get(position).getCount() != null && list.get(position).getCount().equals("-1")) {//表示不限制预约数量
                holder.unSelectServerItemRel.setVisibility(View.GONE);
                holder.selectServerItemRel.setVisibility(View.VISIBLE);
                holder.selectServerItemRel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getListener.onClick(position);
                    }
                });
            } else {//限制预约量
                if (list.get(position).getSurplusCount() <= 0) {
                    holder.unSelectServerItemRel.setVisibility(View.VISIBLE);
                    holder.selectServerItemRel.setVisibility(View.GONE);
                    holder.unSelectServerFullTxt.setText("已约满");
                    holder.unSelectServerFullTxt.setVisibility(View.VISIBLE);
                    holder.unSelectServerItemRel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, "该时间段已约满，请选择其他时间段", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    holder.unSelectServerItemRel.setVisibility(View.GONE);
                    holder.selectServerItemRel.setVisibility(View.VISIBLE);
                    holder.selectServerItemRel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getListener.onClick(position);
                        }
                    });
                }
            }
        }
        //选中的服务项目切换背景
        if (position == getPosition()) {
            holder.selectServerTxt.setTextColor(Color.parseColor("#FF5D72"));
            holder.selectServerItemRel.setBackgroundResource(R.drawable.shape_sub_order_server_checked);
        } else {
            holder.selectServerTxt.setTextColor(Color.parseColor("#333333"));
            holder.selectServerItemRel.setBackgroundResource(R.drawable.shape_radius20_unchecked);
        }
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ServerViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout selectServerItem;
        RelativeLayout selectServerItemRel;
        TextView selectServerTxt;
        ImageView selectServerImg;
        RelativeLayout unSelectServerItemRel;
        TextView unSelectServerTxt;
        TextView unSelectServerFullTxt;

        public ServerViewHolder(@NonNull View itemView) {
            super(itemView);
            selectServerItem = (RelativeLayout) itemView.findViewById(R.id.selectServerItem);
            selectServerItemRel = (RelativeLayout) itemView.findViewById(R.id.selectServerItemRel);
            selectServerTxt = (TextView) itemView.findViewById(R.id.selectServerTxt);
            selectServerImg = (ImageView) itemView.findViewById(R.id.selectServerImg);
            unSelectServerItemRel = (RelativeLayout) itemView.findViewById(R.id.unSelectServerItemRel);
            unSelectServerTxt = (TextView) itemView.findViewById(R.id.unSelectServerTxt);
            unSelectServerFullTxt = (TextView) itemView.findViewById(R.id.unSelectServerFullTxt);
        }

    }

    public interface SetListener {

        void onClick(int position);

    }


    public void setServerListener(SetListener getListener) {
        this.getListener = getListener;
    }


    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}
