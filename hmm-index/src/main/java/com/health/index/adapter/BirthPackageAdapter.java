package com.health.index.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.index.R;
import com.health.index.model.BirthPackageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/04/24 17:12
 * @des 待产包
 */
public class BirthPackageAdapter extends RecyclerView.Adapter<BirthPackageAdapter.BirthPackageHolder> {


    private List<BirthPackageModel> mList;
    private LayoutInflater mInflater;
    private OnChangeStatusListener mListener;

    public BirthPackageAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BirthPackageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.index_item_birth_package, viewGroup, false);
        final BirthPackageHolder holder = new BirthPackageHolder(view);
        holder.ivCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int pos = holder.getAdapterPosition();
                    int status = mList.get(pos).getPrepareStatus();
                    int packageType = mList.get(pos).getPackageType();
                    String key;
                    int id = mList.get(pos).getId();
                    if (packageType == BirthPackageModel.PACKAGE_MOM) {
                        key = status == BirthPackageModel.PREPARED ?
                                "momLaborStatusOne" : "momLaborStatusTwo";
                    } else {
                        key = status == BirthPackageModel.PREPARED ?
                                "babyLaborStatusOne" : "babyLaborStatusTwo";
                    }
                    mListener.onChangeStatusClick(key, id, pos, status);
                }
            }
        });
        holder.mExpandView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                mList.get(pos).setHidden(!mList.get(pos).isHidden());
                notifyItemChanged(pos);
            }
        });

        return holder;
    }

    public void setListener(OnChangeStatusListener listener) {
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull BirthPackageHolder holder, int pos) {
        BirthPackageModel model = mList.get(pos);
        holder.tvTitle.setText(model.getTitle());
        holder.tvIntroduction.setText(model.getIntroduction());
        holder.tvDes.setText(model.getCountDes());
        holder.ivCheck.setImageResource(model.getPrepareStatus() == BirthPackageModel.PREPARED ?
                R.drawable.index_ic_package_prepared : R.drawable.index_ic_package_unprepared);
        holder.tvIntroduction.setVisibility(model.isHidden() ? View.GONE : View.VISIBLE);
        holder.ivExpand.setImageResource(model.isHidden() ? R.drawable.index_ic_package_expand :
                R.drawable.index_ic_package_collapse);


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public void setData(List<BirthPackageModel> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public List<BirthPackageModel> getData() {
        return mList == null ? new ArrayList<BirthPackageModel>() : mList;
    }


    static class BirthPackageHolder extends RecyclerView.ViewHolder {
        private ImageView ivCheck;
        private TextView tvTitle;
        private TextView tvIntroduction;
        private TextView tvDes;
        private ImageView ivExpand;
        private View mExpandView;

        BirthPackageHolder(@NonNull View itemView) {
            super(itemView);
            ivCheck = itemView.findViewById(R.id.iv_check);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvIntroduction = itemView.findViewById(R.id.tv_introduction);
            tvDes = itemView.findViewById(R.id.tv_unit);
            ivExpand = itemView.findViewById(R.id.iv_expand);
            mExpandView = itemView.findViewById(R.id.view_expand);
        }
    }

    public interface OnChangeStatusListener {
        /**
         * 改变待产包状态
         *
         * @param key    参数字段key
         * @param id     id
         * @param pos    位置
         * @param status 待产包状态
         */
        void onChangeStatusClick(String key, int id, int pos, int status);
    }


}
