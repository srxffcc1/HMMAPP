package com.healthy.library.builder;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SimpleHashMapBuilder<K, V> extends HashMap<K,  V> {

    @Nullable
    public SimpleHashMapBuilder puts(K key, V value) {
         super.put(key, value);
        return this;
    }
    @Nullable
    public SimpleHashMapBuilder putMap(Map<K,V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            super.put(entry.getKey(),entry.getValue());
        }
        return this;
    }

    @Nullable
    public SimpleHashMapBuilder putObject(Object obj) {
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (int i=0;i<fields.length;i++){//遍历
            try {
                //得到属性
                Field field = fields[i];
                //打开私有访问
                field.setAccessible(true);
                //获取属性
                K name = (K) field.getName();
                //获取属性值
                V value = (V) field.get(obj);
                //一个个赋值
//                //System.out.println("put进的内容"+name+":"+value);
                super.put(name,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        obj=null;
        return this;
    }
    public String text(){
        String result="";
        for (Map.Entry<K, V> entry : entrySet()) {
            result=entry.getKey().toString()+":"+entry.getValue().toString()+"-";
        }
        return result;
    }
}
