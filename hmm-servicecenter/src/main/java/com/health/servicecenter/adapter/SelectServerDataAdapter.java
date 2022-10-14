package com.health.servicecenter.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.health.servicecenter.R;
import com.healthy.library.interfaces.OnItemClickListener;
import com.healthy.library.model.ServerDateTool;

import java.util.List;

public class SelectServerDataAdapter extends RecyclerView.Adapter<SelectServerDataAdapter.SelectDataHolder> {
    private LayoutInflater mInflater;

    public void setItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private OnItemClickListener mItemClickListener;
    private int mCurrentSelectedIndex = 0;
    private int mSelectedColor;
    private int mUnSelectedColor;
    private int mSelectedBg;
    private int mUnSelectedBg;
    private List<ServerDateTool> monTools;

    public SelectServerDataAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mSelectedColor = Color.parseColor("#FF4C5D");
        mUnSelectedColor = Color.parseColor("#222222");
        mSelectedBg = Color.WHITE;
        mUnSelectedBg = Color.parseColor("#F5F5F9");
    }

    public void setData(List<ServerDateTool> list) {
        monTools = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectDataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = mInflater.inflate(R.layout.server_data_item_toolleft, viewGroup, false);
        final SelectDataHolder holder = new SelectDataHolder(view);
        return holder;
    }

    public void setSelected(int selectedIndex) {
        if (mCurrentSelectedIndex != selectedIndex) {
            mCurrentSelectedIndex = selectedIndex;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectDataHolder holder, final int i) {
        holder.tvProvince.setGravity(Gravity.LEFT);
        holder.tvProvince.setText(monTools.get(i).getmMonthAddmDay());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.setMarginStart(35);
        holder.tvProvince.setLayoutParams(layoutParams);
        holder.viewProvince.setVisibility(mCurrentSelectedIndex == i ? View.VISIBLE : View.INVISIBLE);
        holder.tvProvince.setTextColor(mCurrentSelectedIndex == i ? mSelectedColor : mUnSelectedColor);
        holder.itemView.setBackgroundColor(mCurrentSelectedIndex == i ? mSelectedBg : mUnSelectedBg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(i, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return monTools == null ? 0 : monTools.size();
    }

    static class SelectDataHolder extends RecyclerView.ViewHolder {
        TextView tvProvince;
        View viewProvince;

        SelectDataHolder(@NonNull View itemView) {
            super(itemView);
            tvProvince = itemView.findViewById(R.id.tv_province);
            viewProvince = itemView.findViewById(R.id.view_selected);
        }
    }
}
