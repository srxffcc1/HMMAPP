package com.health.mine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.health.mine.R;
import com.healthy.library.model.BankCardModel;

import java.util.ArrayList;
import java.util.List;

public class SelectBankCardAdapter extends RecyclerView.Adapter<SelectBankCardAdapter.ViewHolder> {
    private Context mContext;
    private SetListener getListener;
    private List<BankCardModel> list = new ArrayList<>();

    public SelectBankCardAdapter(Context mContext, List<BankCardModel> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @NonNull
    @Override
    public SelectBankCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_select_bank_card_layout, parent, false);
        return new SelectBankCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.cardName.setText(list.get(position).getBankName());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect(position);
                getListener.onClick(position);
                notifyDataSetChanged();
            }
        });
        //选中的项目切换背景
        if (list.get(position).isSelect()) {
            holder.selectImg.setImageResource(R.drawable.ic_anonymous_checked);
        } else {
            holder.selectImg.setImageResource(R.drawable.ic_anonymous_unselected);
        }
    }

    private void changeSelect(int position) {
        for (int i = 0; i < list.size(); i++) {
            if (i == position) {
                list.get(i).setSelect(true);
            } else {
                list.get(i).setSelect(false);
            }
        }
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView cardName;
        ImageView cardIcon;
        ImageView selectImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.cardName);
            cardIcon = itemView.findViewById(R.id.cardIcon);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            selectImg = itemView.findViewById(R.id.selectImg);
        }
    }


    public interface SetListener {

        void onClick(int position);

    }

    public void setServerListener(SetListener getListener) {
        this.getListener = getListener;
    }

}
