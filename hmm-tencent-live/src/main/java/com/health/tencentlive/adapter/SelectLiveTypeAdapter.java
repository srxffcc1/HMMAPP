package com.health.tencentlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.health.tencentlive.R;

import java.util.ArrayList;
import java.util.List;

public class SelectLiveTypeAdapter extends RecyclerView.Adapter<SelectLiveTypeAdapter.ViewHolder> {
    private Context mContext;
    private int mPosition = -1;
    private SetListener getListener;
    private List<String> list = new ArrayList<>();

    public SelectLiveTypeAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_live_type_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.selectTxt.setText(list.get(position));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListener.onClick(position, list.get(position));
                notifyDataSetChanged();
            }
        });
        //选中的项目切换背景
        if (position == getmPosition()) {
            holder.selectImg.setImageResource(R.drawable.ic_anonymous_checked);
        } else {
            holder.selectImg.setImageResource(R.drawable.ic_anonymous_unselected);


        }
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView selectTxt;
        ImageView selectImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selectTxt = itemView.findViewById(R.id.selectTxt);
            selectImg = itemView.findViewById(R.id.selectImg);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }


    }


    public interface SetListener {

        void onClick(int position, String content);

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
