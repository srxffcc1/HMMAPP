package com.health.city.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.city.R;
import com.healthy.library.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/04/13 15:17
 * @des 评论时添加图片
 */

public class CityAddTipAdapter extends RecyclerView.Adapter<CityAddTipAdapter.AddImgHolder> {


    private List<Topic> mUrlList;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnTipChangeListener mListener;
    private boolean isaddEnd =false;//是否反序的意思

    public CityAddTipAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AddImgHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.city_item_activity_post_addtip, viewGroup, false);
        final AddImgHolder holder = new AddImgHolder(view);
        holder.toFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int pos = holder.getAdapterPosition();
                    if(mUrlList.get(pos)==null){
                        if(isaddEnd){

                            if(pos==getData().size()-1-1){
                                mListener.onAddTip();
                            }
                        }else {
                            if(pos==0){
                                mListener.onAddTip();
                            }
                        }
                    }else {
//                        mListener.onUpdate(pos);
                    }
                }

            }
        });
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int pos = holder.getAdapterPosition();
                    delData(pos);
                }

            }
        });
        return holder;
    }

    public List<Topic> getData() {
        return mUrlList == null ? new ArrayList<Topic>() : mUrlList;
    }

    public void setData(List<Topic> urls) {
        mUrlList = urls;
        notifyDataSetChanged();
    }

    public void addDatas(List<Topic> paths){
        if(isaddEnd){
            mUrlList.addAll(0,paths);
            notifyItemRangeInserted(0,paths.size());
        }else {
            mUrlList.addAll(1,paths);
            notifyItemRangeInserted(1,paths.size());
        }

    }
    public void addData(Topic paths){
        if(isaddEnd){
            mUrlList.add(0,paths);
            notifyItemRangeInserted(0,1);
        }else {
            mUrlList.add(1,paths);
            notifyItemRangeInserted(1,1);
        }

    }

    public void updateData(Topic url, int pos) {
        mUrlList.set(pos, url);
        notifyItemChanged(pos);
    }

    public void delData(int pos) {
        mUrlList.remove(pos);
        notifyItemRangeRemoved(pos, 1);
    }

    public void setListener(OnTipChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull AddImgHolder holder, int pos) {
        Topic url = mUrlList.get(pos);
//        holder.ivIsVideo.setVisibility(View.GONE);


        final Drawable likeNormal = mContext.getResources().getDrawable(R.drawable.ic_tip_snormal);
        final Drawable likeRed = mContext.getResources().getDrawable(R.drawable.ic_tip_sadd);


        if(url==null){
            holder.toFollow.setCompoundDrawablesWithIntrinsicBounds(likeRed,null,null,null);
            holder.toFollow.setCompoundDrawablePadding(3);
//            if(pos==1){
//                holder.ivDel.setVisibility(View.GONE);
//                holder.ivPic.setImageResource(R.drawable.ic_upload_pic);
//            }
            if(pos==0){
                holder.ivDel.setVisibility(View.GONE);
//                holder.toFollow.setImageResource(R.drawable.ic_upload_flv);
            }
        }
        else {
//            if(MediaFileUtil.isVideoFileType(url)){
//                holder.ivIsVideo.setVisibility(View.VISIBLE);
//            }
            holder.toFollow.setText(url.topicName);
            holder.toFollow.setCompoundDrawablesWithIntrinsicBounds(likeNormal,null,null,null);
            holder.toFollow.setCompoundDrawablePadding(3);
            holder.ivDel.setVisibility(View.VISIBLE);
//            com.healthy.library.businessutil.GlideCopy.with(mContext)
//                    .load(url).centerCrop()
//                    .into(holder.ivPic);
        }
    }

    @Override
    public int getItemCount() {
        return mUrlList == null ? 0 : mUrlList.size();
    }

    public interface OnTipChangeListener {
        /**
         * 添加图片
         */
        void onAddTip();

    }

    static class AddImgHolder extends RecyclerView.ViewHolder {

        TextView toFollow;
        ImageView ivDel;

        AddImgHolder(@NonNull View itemView) {
            super(itemView);
            toFollow = itemView.findViewById(R.id.toFollow);
            ivDel = itemView.findViewById(R.id.iv_del);
        }
    }
}
