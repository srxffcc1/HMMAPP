package com.healthy.library.builder;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleStringBuilder<T>  {
    List<String> list=new ArrayList<>();

    @Nullable
    public SimpleStringBuilder puts(String des) {
        list.add(des);
        return this;
    }
    public SimpleStringBuilder putobj(T des,StringIteraor<T> tStringIteraor){
            list.add(tStringIteraor.getDesString(des));

        return this;
    }
    public SimpleStringBuilder putList(List<T> deslist,StringIteraor<T> tStringIteraor){
        for (int i = 0; i <deslist.size() ; i++) {
            list.add(tStringIteraor.getDesString(deslist.get(i)));
        }
        return this;
    }

    public SimpleStringBuilder putList(T[] deslist, StringIteraor<T> tStringIteraor){
        if(deslist==null){
            return this;
        }
        for (int i = 0; i <deslist.length ; i++) {
            if(tStringIteraor!=null){

                list.add(tStringIteraor.getDesString(deslist[i]));
            }else {
                list.add(deslist[i].toString());
            }
        }
        return this;
    }

    public String[] array(){
        return list.toArray(new String[list.size()] );
    }
    public String text(String aplit){
        String result="";
        for (int i = 0; i <list.size() ; i++) {
            if(TextUtils.isEmpty(list.get(i))){

                result=result+" "+aplit;
            }else {

                result=result+list.get(i)+aplit;
            }
        }
        return result;
    }
    public String text(){
        return text(",");
    }
    public interface StringIteraor<T>{
        String getDesString(T t);
    }
}
