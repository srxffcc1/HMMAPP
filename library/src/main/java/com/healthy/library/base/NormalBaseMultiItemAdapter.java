package com.healthy.library.base;

import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

public abstract class NormalBaseMultiItemAdapter<T extends MultiItemEntity> extends NormalBaseAdapter<T> {

    public static final int DEFAULT_VIEW_TYPE=78;
    public SparseIntArray layouts;

    public NormalBaseMultiItemAdapter(int viewId) {
        this(new ArrayList<T>(),viewId);
    }

    public NormalBaseMultiItemAdapter(@NonNull ArrayList<T> list, int viewId) {
        super(list, viewId);
        addItemType(DEFAULT_VIEW_TYPE, viewId);
    }
    @Override
    public int getItemViewType(int position) {
        return getDefItemViewType(position);
    }
    public void  addItemType(int type,int  layoutResId ) {
        if (layouts == null) {
            layouts = new SparseIntArray();
        }
        layouts.put(type, layoutResId);
    }

    public int getDefItemViewType(int position) {
        try {
            MultiItemEntity item= (MultiItemEntity) list.get(position);
            return item.getItemType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEFAULT_VIEW_TYPE;
    }
    public int getItemType(int position) {
        try {
            MultiItemEntity item= (MultiItemEntity) list.get(position);
            return item.getItemType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEFAULT_VIEW_TYPE;
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BaseHolder(LayoutInflater.from(context).inflate(getLayoutId(viewType), parent, false));
    }
    public int getLayoutId(int viewType) {
        if(layouts.indexOfKey(viewType)<0){
            return layouts.get(DEFAULT_VIEW_TYPE);
        }
        try {
            return layouts.get(viewType);//后面要用value
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layouts.get(DEFAULT_VIEW_TYPE);
    }
}
