package com.health.mine.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.mine.R;
import com.health.mine.model.JobArea;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @date 2019/04/13 15:17
 * @des 评论时添加图片
 */

public class MineAddLocAdapter extends RecyclerView.Adapter<MineAddLocAdapter.AddImgHolder> {
    public int limitCount=0;

    public int getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }

    private List<JobArea> mUrlList;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnTipChangeListener mListener;
    private boolean isaddEnd =true;

    public MineAddLocAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AddImgHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = mInflater.inflate(R.layout.mine_item_activity_addloc, viewGroup, false);
        final AddImgHolder holder = new AddImgHolder(view);
        holder.toFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int pos = holder.getAdapterPosition();
                    if(mUrlList.get(pos)==null){
                        if(isaddEnd){
                            if(pos==getData().size()-1){
                                mListener.onAddTip();
                            }
                        }else {
                            if(pos==0){
                                mListener.onAddTip();
                            }
                        }
                    }else {

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

    public List<JobArea> getData() {
        return mUrlList == null ? new ArrayList<JobArea>() : mUrlList;
    }

    public void setData(List<JobArea> urls) {
        mUrlList = urls;
        notifyDataSetChanged();
    }

    public void addDatas(List<JobArea> paths){
        for (int i = 0; i <paths.size() ; i++) {
            addData(paths.get(i));
        }
    }
    public void addData(JobArea paths){
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

    public void updateData(JobArea url, int pos) {
        mUrlList.set(pos, url);
        notifyItemChanged(pos);
    }

    public void delData(int pos) {
        JobArea url = mUrlList.get(pos);
        if(pos==limitCount-1){//删除的最后一张
            mUrlList.set(pos, null);
            notifyItemChanged(pos);
        }else if(mUrlList.size()==limitCount&&mUrlList.get(mUrlList.size()-1)!=null){//删除的不是最后一张但是总数已经
            mUrlList.remove(pos);
            mUrlList.add(mUrlList.size(),null);
            notifyDataSetChanged();
        }else {
            mUrlList.remove(pos);
            notifyItemRangeRemoved(pos, 1);
        }

    }
    public List<JobArea> getResultBean(){
        List<JobArea> result=new ArrayList<>();
        for (int i = 0; i < mUrlList.size(); i++) {
            JobArea filepath = mUrlList.get(i);
            if (filepath!=null) {
                result.add(filepath);
            }
        }
        return result;
    }
    public List<String> getResultName(){
        List<String> result=new ArrayList<>();
        for (int i = 0; i < mUrlList.size(); i++) {
            JobArea filepath = mUrlList.get(i);
            if (filepath!=null) {
                result.add(filepath.name);
            }
        }
        return result;
    }
    public List<String> getResult(){
        List<String> result=new ArrayList<>();
        for (int i = 0; i < mUrlList.size(); i++) {
            JobArea filepath = mUrlList.get(i);
            if (filepath!=null) {
                result.add(filepath.value);
            }
        }
        return result;
    }
    public void setListener(OnTipChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull AddImgHolder holder, int pos) {
        JobArea url = mUrlList.get(pos);
//        holder.ivIsVideo.setVisibility(View.GONE);


        final Drawable likeNormal = mContext.getResources().getDrawable(R.drawable.ic_tip_snormal);
        final Drawable likeRed = mContext.getResources().getDrawable(R.drawable.ic_tip_sadd);


        if(url==null){
            holder.toFollow.setCompoundDrawablesWithIntrinsicBounds(likeRed,null,null,null);
            holder.toFollow.setCompoundDrawablePadding(3);
            if(pos==getData().size()-1){
                holder.ivDel.setVisibility(View.GONE);
            }
            holder.toFollow.setText("意向地");
        }
        else {

            holder.toFollow.setText(url.name);
            holder.toFollow.setCompoundDrawablesWithIntrinsicBounds(likeNormal,null,null,null);
            holder.toFollow.setCompoundDrawablePadding(3);
            holder.ivDel.setVisibility(View.VISIBLE);
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
