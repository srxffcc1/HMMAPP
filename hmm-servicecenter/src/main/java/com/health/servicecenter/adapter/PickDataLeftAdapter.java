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
import com.healthy.library.model.GoodsPickDay;
import com.healthy.library.interfaces.OnItemClickListener;

import java.util.List;

public class PickDataLeftAdapter extends RecyclerView.Adapter<PickDataLeftAdapter.SelectDataHolder> {
    private LayoutInflater mInflater;

    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private OnItemClickListener mItemClickListener;
    private int mCurrentSelectedIndex = 0;
    private int mSelectedColor;
    private int mUnSelectedColor;
    private int mSelectedBg;
    private int mUnSelectedBg;
    private List<GoodsPickDay> monTools;


    public PickDataLeftAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mSelectedColor = Color.parseColor("#F02846");
        mUnSelectedColor = Color.parseColor("#444444");
        mSelectedBg = Color.WHITE;
        mUnSelectedBg = Color.parseColor("#F5F5F9");
    }

    public void setData(List<GoodsPickDay> list) {
        monTools = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SelectDataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = mInflater.inflate(R.layout.item_pick_data_toolleft, viewGroup, false);
        final SelectDataHolder holder = new SelectDataHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(holder.getAdapterPosition(), view);
            }
        });
        return holder;
    }
    public String getSelectDay(){
        try {
            return monTools.get(mCurrentSelectedIndex).day;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSelected(int selectedIndex) {
        if (mCurrentSelectedIndex != selectedIndex) {
            mCurrentSelectedIndex = selectedIndex;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SelectDataHolder holder, int i) {
        // IndexMonTool model = monTools.get(i);
        holder.tvProvince.setGravity(Gravity.LEFT);
        holder.tvProvince.setText(monTools.get(i).getDayShow());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.setMarginStart(35);
        holder.tvProvince.setLayoutParams(layoutParams);
        holder.viewProvince.setVisibility(mCurrentSelectedIndex == i ? View.VISIBLE : View.INVISIBLE);
        holder.tvProvince.setTextColor(mCurrentSelectedIndex == i ? mSelectedColor : mUnSelectedColor);
        holder.itemView.setBackgroundColor(mCurrentSelectedIndex == i ? mSelectedBg : mUnSelectedBg);
        holder.tvProvince.getPaint().setFakeBoldText(mCurrentSelectedIndex == i?true:false);
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
