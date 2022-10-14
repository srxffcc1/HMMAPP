package com.healthy.library.builder;

import androidx.annotation.Nullable;

import com.healthy.library.businessutil.ListUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SimpleArrayListBuilder<K> extends ArrayList<K> {

    ObjectIteraor objectCompare;

    ArrayList<K> superArray=new ArrayList<>();

    public SimpleArrayListBuilder setObjectCompare(ObjectIteraor objectCompare) {
        this.objectCompare = objectCompare;
        return this;
    }

    @Nullable
    public SimpleArrayListBuilder adds(K key) {
        super.add(key);
        return this;
    }
    @Nullable
    public SimpleArrayListBuilder whit(List<K> map) {
        for (int i = 0; i <map.size() ; i++) {
            superArray.add(map.get(i));
        }
        return this;
    }

    @Nullable
    public SimpleArrayListBuilder addList(List<K> map) {
        for (int i = 0; i <map.size() ; i++) {
            super.add(map.get(i));
        }
        return this;
    }

    @Nullable
    public ArrayList<K> addNotDuplicateList(Collection<? extends K> orgmap) {
        List<K>  map= (List<K>) orgmap;
        if(map==null||map.size()==0){//目标为空
            return new SimpleArrayListBuilder();
        }
        int start=superArray.size();
        for (int i = 0; i <map.size() ; i++) {
            if(objectCompare!=null){
                if(map.get(i)==null){
                    superArray.add(map.get(i));
                }else {
                    if(!ListUtil.checkObjIsInList(new SimpleArrayListBuilder<>().putList(superArray, objectCompare),objectCompare.getDesObj(map.get(i)).toString())){
                        superArray.add(map.get(i));
                    }
                }
            }else {
                superArray.add(map.get(i));
            }

        }
        System.out.println("去重start"+start);
        System.out.println("去重end"+superArray.size());
        List<K> result=ListUtil.getChildList(superArray,start,superArray.size());
//        for (int i = 0; i <result.size() ; i++) {
//            if(objectCompare!=null){
//                System.out.println(objectCompare.getDesObj(result.get(i)));
//            }
//        }
        superArray.clear();
        superArray.addAll(result);
        return superArray;
    }

    public SimpleArrayListBuilder putList(List deslist, ObjectIteraor tStringIteraor){
        if(deslist==null){
            return this;
        }
        for (int i = 0; i <deslist.size() ; i++) {
            if(deslist.get(i)!=null&&tStringIteraor.getDesObj(deslist.get(i))!=null){
                super.add((K) tStringIteraor.getDesObj(deslist.get(i)));
            }
        }
        return this;
    }




    public SimpleArrayListBuilder getChildList(List<K> map,int start,int end){
        for (int i = start; i <end+1 ; i++) {
            try {
                super.add(map.get(i));
            } catch (Exception e) {
            }
        }
        return this;
    }
}
