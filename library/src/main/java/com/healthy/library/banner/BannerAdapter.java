package com.healthy.library.banner;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.healthy.library.R;
import com.healthy.library.model.AdModel;
import com.healthy.library.utils.ResUtil;

import java.util.List;

/**
 * @author: long
 * @date: 2021/4/6
 */
public class BannerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<Object> items;
    private int mBackgroundColor = Color.BLACK;
    private String mDimensionRatio = "";

    public void setDimensionRatio(String dimensionRatio){
        this.mDimensionRatio = dimensionRatio;
    }

    public void setBackgroundColor(int backgroundColor){
        this.mBackgroundColor = backgroundColor;
    }

    public void setData(List<Object> items) {
        this.items = items;
    }

    private View.OnClickListener onItemClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_photo, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        holder.setGone(R.id.iv_banner, true)
                .setGone(R.id.iv_loading, false)
                .setBackgroundColor(R.id.cl_content,mBackgroundColor);
        String data = "";
        if(items.get(position) instanceof AdModel){
            AdModel model = (AdModel) items.get(position);
            data = model.photoUrls;
        } else if(items.get(position) instanceof String){
            data = (String) items.get(position);
        }
        ImageView imageView = holder.getView(R.id.iv_banner);
        if(!TextUtils.isEmpty(mDimensionRatio)) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.dimensionRatio = mDimensionRatio;
            imageView.setLayoutParams(layoutParams);
        }
        RequestOptions requestOptions = new RequestOptions()
                .error(com.healthy.library.R.drawable.img_1_1_default)
                .placeholder(com.healthy.library.R.drawable.img_1_1_default2);

        if (data.contains("R.")) {
            Glide.with(holder.itemView.getContext())
                    .applyDefaultRequestOptions(requestOptions)
                    .load(ResUtil.getInstance().getResourceId(data))
                    .into(imageView);
        } else if (data.contains(".gif")) {
            Glide.with(holder.itemView.getContext())
                    .applyDefaultRequestOptions(requestOptions)
                    .asGif()
                    .load(data)
                    .into(imageView);
        } else {
            Glide.with(holder.itemView.getContext())
                    .applyDefaultRequestOptions(requestOptions)
                    .load(data)
                    .into(imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(v);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}
