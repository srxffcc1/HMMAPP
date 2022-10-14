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
import com.healthy.library.model.CategoryListModel;
import com.healthy.library.interfaces.OnItemClickListener;

import java.util.List;

public class MallSortLeftAdapter extends RecyclerView.Adapter<MallSortLeftAdapter.SotrLeftHolder> {


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
    private List<CategoryListModel> monTools;

    public MallSortLeftAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mSelectedColor = Color.parseColor("#F02846");
        mUnSelectedColor = Color.parseColor("#222222");
        mSelectedBg = Color.parseColor("#F5F5F9");
        mUnSelectedBg = Color.parseColor("#ffffffff");
    }

    public void setData(List<CategoryListModel> list) {
        monTools = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SotrLeftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_mall_sort_left_layout, parent, false);
        final MallSortLeftAdapter.SotrLeftHolder holder = new MallSortLeftAdapter.SotrLeftHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(holder.getAdapterPosition(), view);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SotrLeftHolder holder, int i) {
        holder.tvProvince.setGravity(Gravity.LEFT);
        holder.tvProvince.setText(monTools.get(i).getCategoryName());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.setMarginStart(35);
        holder.tvProvince.setLayoutParams(layoutParams);
        holder.viewProvince.setVisibility(mCurrentSelectedIndex == i ? View.VISIBLE : View.INVISIBLE);
        holder.tvProvince.setTextColor(mCurrentSelectedIndex == i ? mSelectedColor : mUnSelectedColor);
        holder.itemView.setBackgroundColor(mCurrentSelectedIndex == i ? mSelectedBg : mUnSelectedBg);
    }

    public void setSelected(int selectedIndex) {
        if (mCurrentSelectedIndex != selectedIndex) {
            mCurrentSelectedIndex = selectedIndex;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return monTools == null ? 0 : monTools.size();
    }

    static class SotrLeftHolder extends RecyclerView.ViewHolder {
        TextView tvProvince;
        View viewProvince;

        SotrLeftHolder(@NonNull View itemView) {
            super(itemView);
            tvProvince = itemView.findViewById(R.id.tv_province);
            viewProvince = itemView.findViewById(R.id.view_selected);
        }
    }
}
