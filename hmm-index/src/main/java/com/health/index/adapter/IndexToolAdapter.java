package com.health.index.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.index.R;
import com.health.index.model.IndexMonTool;
import com.healthy.library.interfaces.OnItemClickListener;

import java.util.List;

public class IndexToolAdapter extends RecyclerView.Adapter<IndexToolAdapter.IndexToolHolder>{
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
    private List<IndexMonTool> monTools;

    public IndexToolAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mSelectedColor = Color.parseColor("#FF4C5D");
        mUnSelectedColor = Color.parseColor("#222222");
        mSelectedBg = Color.WHITE;
        mUnSelectedBg = Color.parseColor("#F5F5F9");
    }
    public void setData(List<IndexMonTool> list) {
        monTools = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public IndexToolHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = mInflater.inflate(R.layout.index_item_toolleft, viewGroup, false);
        final IndexToolHolder holder = new IndexToolHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(holder.getAdapterPosition(),view);
            }
        });
        return holder;
    }
    public void setSelected(int selectedIndex) {
        if (mCurrentSelectedIndex != selectedIndex) {
            mCurrentSelectedIndex = selectedIndex;
            //System.out.println("点击事件进入刷新");
            notifyDataSetChanged();
        }
    }
    @Override
    public void onBindViewHolder(@NonNull IndexToolHolder holder, int i) {
        IndexMonTool model = monTools.get(i);
        holder.tvProvince.setText(model.categoryName);
        holder.viewProvince.setVisibility(mCurrentSelectedIndex == i ? View.VISIBLE : View.INVISIBLE);
        holder.tvProvince.setTextColor(mCurrentSelectedIndex == i ? mSelectedColor : mUnSelectedColor);
        holder.itemView.setBackgroundColor(mCurrentSelectedIndex == i ? mSelectedBg : mUnSelectedBg);
    }

    @Override
    public int getItemCount() {
        return monTools == null ? 0 : monTools.size();
    }
    static class IndexToolHolder extends RecyclerView.ViewHolder {
        TextView tvProvince;
        View viewProvince;

        IndexToolHolder(@NonNull View itemView) {
            super(itemView);
            tvProvince = itemView.findViewById(R.id.tv_province);
            viewProvince = itemView.findViewById(R.id.view_selected);
        }
    }
}
