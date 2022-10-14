package com.healthy.library.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class MBaseQuickAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {
    public MBaseQuickAdapter(int layoutResId) {
        super(layoutResId);
    }

    public void setNewData(@Nullable List<T> data) {
//        ObjectIteraor objectIteraor = getDuplicateObjectIterator();
//        if(objectIteraor==null){
            super.setNewData(data);
//        }else{
//            ArrayList<T> listTmp=new SimpleArrayListBuilder<T>().setObjectCompare(objectIteraor).whit(mData).addNotDuplicateList(data);
//            super.setNewData(listTmp);
//        }
    }
    public void addData(@NonNull Collection<? extends T> newData) {
        ObjectIteraor objectIteraor = getDuplicateObjectIterator();
        if(objectIteraor==null){
            super.addData(newData);
        }else{
            ArrayList<T> listTmp=new SimpleArrayListBuilder<T>().setObjectCompare(objectIteraor).whit(mData).addNotDuplicateList(newData);
            super.addData(listTmp);
        }
    }

    public ObjectIteraor getDuplicateObjectIterator() {
        return null;
    }
}
