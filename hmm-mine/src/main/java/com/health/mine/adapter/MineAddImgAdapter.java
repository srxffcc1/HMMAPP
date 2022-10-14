package com.health.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.health.mine.R;
import com.healthy.library.routes.LibraryRoutes;
import com.healthy.library.utils.MediaFileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/04/13 15:17
 * @des 评论时添加图片
 */

public class MineAddImgAdapter extends RecyclerView.Adapter<MineAddImgAdapter.AddImgHolder> {
    public int limitCount=0;

    public int getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }

    private List<String> mUrlList;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnImgChangeListener mListener;
    private boolean isaddEnd =true;

    public MineAddImgAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public List<String> getResult(){
        List<String> result=new ArrayList<>();
        for (int i = 0; i < mUrlList.size(); i++) {
            String filepath = mUrlList.get(i);
            if (!TextUtils.isEmpty(filepath)) {
                result.add(filepath);
            }
        }
        return result;
    }

    @NonNull
    @Override
    public AddImgHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = mInflater.inflate(R.layout.mine_item_activity_post_add, viewGroup, false);
        final AddImgHolder holder = new AddImgHolder(view);
        holder.ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int pos = holder.getAdapterPosition();
                    if(TextUtils.isEmpty(mUrlList.get(pos))){
                        if(isaddEnd){
                            if(pos==getData().size()-1){
                                mListener.onAddImg();
                            }
                        }else {
                            if(pos==0){
                                mListener.onAddImg();
                            }
                        }
                    }else {
                        if(MediaFileUtil.isVideoFileType(mUrlList.get(pos))){
                            ARouter.getInstance()
                                    .build(LibraryRoutes.LIBRARY_VIDEOPLAYER)
                                    .withString("videoUrl", mUrlList.get(pos))
                                    .navigation();

                        }else {
                            mListener.onUpdate(pos);
                        }
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

    public List<String> getData() {
        return mUrlList == null ? new ArrayList<String>() : mUrlList;
    }

    public void setData(List<String> urls) {
        mUrlList = urls;
        notifyDataSetChanged();
    }

    public void addDatas(List<String> paths){
        for (int i = 0; i <paths.size() ; i++) {
            addData(paths.get(i));
        }
    }
    public void addData(String paths){
        if(isaddEnd){
            if(mUrlList.size()==limitCount){
                updateData(paths,limitCount-1);
            }else {
                mUrlList.add(0,paths);
                notifyItemRangeInserted(0,1);
            }
        }else {
            mUrlList.add(1,paths);
            notifyItemRangeInserted(2,1);
        }

    }

    public void updateData(String url, int pos) {
        mUrlList.set(pos, url);
        notifyItemChanged(pos);
    }

    public void delData(int pos) {
        String url = mUrlList.get(pos);
        if(pos==limitCount-1){//删除的最后一张
            mUrlList.set(pos, null);
            notifyItemChanged(pos);
        }else if(mUrlList.size()==limitCount&&!TextUtils.isEmpty(mUrlList.get(mUrlList.size()-1))){//删除的不是最后一张但是总数已经
            mUrlList.remove(pos);
            mUrlList.add(mUrlList.size(),null);
            notifyDataSetChanged();
        }else {
            mUrlList.remove(pos);
            notifyItemRangeRemoved(pos, 1);
        }

    }

    public void setListener(OnImgChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull AddImgHolder holder, int pos) {
        String url = mUrlList.get(pos);
        holder.ivIsVideo.setVisibility(View.GONE);
        if(TextUtils.isEmpty(url)){
            if(pos==getData().size()-1){
                holder.ivDel.setVisibility(View.GONE);
                holder.ivPic.setImageResource(R.drawable.ic_upload_pic);
            }
        } else {
//            if(MediaFileUtil.isVideoFileType(url)){
//                holder.ivIsVideo.setVisibility(View.VISIBLE);
//            }
            holder.ivDel.setVisibility(View.VISIBLE);
            com.healthy.library.businessutil.GlideCopy.with(holder.ivPic.getContext())
                    .load(url).centerCrop()
                    .into(holder.ivPic);
        }
    }

    @Override
    public int getItemCount() {
        return mUrlList == null ? 0 : mUrlList.size();
    }

    public interface OnImgChangeListener {
        /**
         * 添加图片
         */
        void onAddImg();

        /**
         * 添加图片
         */
        void onAddVideo(int pos);

        /**
         * 更新图片
         *
         * @param pos pos
         */
        void onUpdate(int pos);
        void onVideoUpdate(int pos);
    }

    static class AddImgHolder extends RecyclerView.ViewHolder {

        ImageView ivPic;
        ImageView ivDel;
        ImageView ivIsVideo;

        AddImgHolder(@NonNull View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.iv_pic);
            ivDel = itemView.findViewById(R.id.iv_del);
            ivIsVideo=itemView.findViewById(R.id.isVideo);
        }
    }
}
