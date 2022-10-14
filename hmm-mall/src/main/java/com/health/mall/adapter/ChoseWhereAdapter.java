package com.health.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.health.mall.R;
import com.healthy.library.model.ProvinceCityModel;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/04 15:46
 * @des 省列表适配器
 */

public class ChoseWhereAdapter extends RecyclerView.Adapter<ChoseWhereAdapter.ChoseWhere> {

    private LayoutInflater mInflater;
    private List<ProvinceCityModel> mProvinceModelList;
    private int mCurrentSelectedIndex = 0;

    public void setChoseItemClickListener(ChoseItemClickListener choseItemClickListener) {
        this.choseItemClickListener = choseItemClickListener;
    }

    ChoseItemClickListener choseItemClickListener;


//    private int mSelectedColor;
//    private int mUnSelectedColor;
//    private int mSelectedBg;
//    private int mUnSelectedBg;

    public ChoseWhereAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
//        mSelectedColor = Color.parseColor("#FF4C5D");
//        mUnSelectedColor = Color.parseColor("#222222");
//        mSelectedBg = Color.WHITE;
//        mUnSelectedBg = Color.parseColor("#F5F5F9");
    }

    @NonNull
    @Override
    public ChoseWhere onCreateViewHolder(@NonNull ViewGroup viewGroup,final int i) {
        View view = mInflater.inflate(R.layout.item_province, viewGroup, false);
        final ChoseWhere holder = new ChoseWhere(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choseItemClickListener != null) {
                    choseItemClickListener.onChoseClick(mProvinceModelList.get(holder.getAdapterPosition()));
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChoseWhere holder, int i) {
        ProvinceCityModel model = mProvinceModelList.get(i);

        holder.tvProvince.setText(model.getName());
//        holder.viewProvince.setVisibility(mCurrentSelectedIndex == i ? View.VISIBLE : View.INVISIBLE);
//
//        holder.tvProvince.setTextColor(mCurrentSelectedIndex == i ? mSelectedColor : mUnSelectedColor);
//        holder.itemView.setBackgroundColor(mCurrentSelectedIndex == i ? mSelectedBg : mUnSelectedBg);


    }

    @Override
    public int getItemCount() {
        return mProvinceModelList == null ? 0 : mProvinceModelList.size();
    }


    public void setSelected(int selectedIndex) {
        if (mCurrentSelectedIndex != selectedIndex) {
            mCurrentSelectedIndex = selectedIndex;
            notifyDataSetChanged();
        }
    }

    public List<ProvinceCityModel> getData() {
        return mProvinceModelList;
    }

    public void setData(List<ProvinceCityModel> list) {
        mProvinceModelList = list;
        notifyDataSetChanged();
    }

    static class ChoseWhere extends RecyclerView.ViewHolder {
        TextView tvProvince;
        View viewProvince;

        ChoseWhere(@NonNull View itemView) {
            super(itemView);
            tvProvince = itemView.findViewById(R.id.tv_province);
            viewProvince = itemView.findViewById(R.id.view_selected);
        }
    }
    public interface ChoseItemClickListener{
        void onChoseClick(ProvinceCityModel provinceCityModel);
    }
}
