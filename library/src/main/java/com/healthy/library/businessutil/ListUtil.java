package com.healthy.library.businessutil;

import com.healthy.library.builder.ObjectIteraor;
import com.healthy.library.builder.SimpleArrayListBuilder;
import com.tencent.bugly.proguard.K;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListUtil {
    public static boolean isEmpty(List list){
        if(list==null){
            return true;
        }
        if (list.size()==0){
            return true;
        }
        return false;
    }

    public interface Where<D> {
        boolean where(D obj) ;
    }
    public static final <D> List<D> where(Collection<D> colls , Where<D> gb){
        Iterator<D> iter = colls.iterator() ;
        List<D> set=new ArrayList<D>();
        while(iter.hasNext()) {
            D d = iter.next() ;
            if(gb.where(d)){
                set.add(d);
            }
        }
        return set;
    }

    public static boolean checkObjIsInList(List<String> seachList,String t){
        boolean result=false;
        if(seachList==null){
            return true;
        }
        for (int i = 0; i <seachList.size() ; i++) {
            if(t.equals(seachList.get(i))){
                return true;
            }
        }
        return result;
    }
    public static int checkObjIsInListWithResult(List<String> seachList,String t){
        int result=-1;
        if(seachList==null){
            return -1;
        }
        for (int i = 0; i <seachList.size() ; i++) {
            if(t!=null){
                if(t.equals(seachList.get(i))){
                    return i;
                }
            }
        }
        return result;
    }
    public static<T> List<T>  flashDuplicateList(List<T> seachList, ObjectIteraor tStringIteraor){
        List<T> newList = new ArrayList<>();
//        Map<String, T> clearMap = new HashMap<>();
//        for (int i = 0; i <seachList.size() ; i++) {
//            clearMap.put(tStringIteraor.getDesObj(seachList.get(i)).toString(),seachList.get(i));
//        }
//        Set<Map.Entry<String, T>> set = clearMap.entrySet();
//        // 遍历键值对对象的集合，得到每一个键值对对象
//        for (Map.Entry<String, T> me : set) {
//            localShopList.add(me.getValue());
//        }
        Set set = new HashSet();
        for (Iterator<T> inter = seachList.iterator(); inter.hasNext();) {
            T element = inter.next();
            if (set.add(tStringIteraor.getDesObj(element).toString()))
                newList.add(element);
        }
        seachList.clear();
        seachList.addAll(newList);
        return seachList;
    }
    public static<K> List<K> getChildList(List<K> map, int start, int end){
        List<K> result=new ArrayList<>();
        for (int i = start; i <end+1 ; i++) {
            try {
                result.add(map.get(i));
            } catch (Exception e) {
            }
        }
        return result;
    }
}
