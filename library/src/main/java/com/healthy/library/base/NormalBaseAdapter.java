package com.healthy.library.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;

import java.util.ArrayList;
import java.util.List;

public abstract class NormalBaseAdapter<T> extends RecyclerView.Adapter<BaseHolder> {
    public int viewId;
    public List list = new ArrayList<T>();

    public Context getContext() {
        return context;
    }

    public Context context;
    T model = null;
    public BaseAdapter.OnOutClickListener moutClickListener;
    public Boolean isClickInit() {
        return moutClickListener!=null;
    }
    public NormalBaseAdapter(int viewId) {
        this(new ArrayList<T>(),viewId);
    }

    public NormalBaseAdapter(@NonNull ArrayList<T> list, int viewId) {
        this.list = list;
        this.viewId=viewId;
    }

    public ObjectIteraor getDuplicateObjectIterator() {
        return null;
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BaseHolder(LayoutInflater.from(parent.getContext()).inflate(viewId, parent, false));
    }
    public void  onDataChange() {}
    public void clear(){
        model=null;
        this.list.clear();
        onDataChange();
        notifyDataSetChanged();
    }
    public void setData(ArrayList<T> list) {
//        ObjectIteraor objectIteraor = getDuplicateObjectIterator();
//        if(objectIteraor==null){
            setDataReal(list);
//        }else{
//            ArrayList<T> listTmp=new SimpleArrayListBuilder<T>().setObjectCompare(objectIteraor).whit(this.list).addNotDuplicateList(list);
//            setDataReal(listTmp);
//        }
    }
    public void setDataAll(ArrayList<T> list) {
//        ObjectIteraor objectIteraor = getDuplicateObjectIterator();
//        if(objectIteraor==null){
            setDataRealAll(list);
//        }else{
//            ArrayList<T> listTmp=new SimpleArrayListBuilder<T>().setObjectCompare(objectIteraor).whit(this.list).addNotDuplicateList(list);
//            setDataReal(listTmp);
//        }
    }
    public void setDataRealAll(ArrayList<T> list) {
        this.list.addAll(list);
        onDataChange();
        notifyDataSetChanged();
    }
    public void setOutClickListener(BaseAdapter.OnOutClickListener outClickListener){
        this.moutClickListener=outClickListener;
    }
    public void setDataReal(ArrayList<T> list) {
        this.list = list;
        onDataChange();
        notifyDataSetChanged();
    }
    public void setModel( T model) {
        this.model = model;
        this.list.clear();
        onDataChange();
        notifyDataSetChanged();
    }

    public void addDatas(ArrayList<T> list) {
        ObjectIteraor objectIteraor = getDuplicateObjectIterator();

        if(objectIteraor==null){
            addDatasReal(list);
        }else{
            ArrayList<T> listTmp=new SimpleArrayListBuilder<T>().setObjectCompare(objectIteraor).whit(this.list).addNotDuplicateList(list);
            addDatasReal(listTmp);
        }

    }
    public void addDatasReal(ArrayList<T> list) {
        this.list.addAll(list);
        onDataChange();
        notifyItemRangeInserted(this.list.size(), list.size());
    }
    public void addData( T t) {
        this.list.add(t);
        onDataChange();
        notifyItemInserted(this.list.size());
    }
    public T getModel(){
        return model;
    }
    public List<T> getDatas() {
        return list;
    }

    public int getViewId()  {
        return viewId;
    }

    @Override
    public int getItemCount() {
        if (model != null) {
            return 1;
        } else {
            return list.size();
        }
    }
}
