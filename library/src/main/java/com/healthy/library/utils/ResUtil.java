package com.healthy.library.utils;

import android.content.Context;

import androidx.collection.ArrayMap;

import java.lang.reflect.Field;

public class ResUtil {

//    private static ArrayMap<String,Class> classmap=new ArrayMap<String,Class>();
//    static {
//
//    }
    public ResUtil init(Context context){
        fcontext=context;
//        Class[] classchild = rclass.getDeclaredClasses();
//        for (int i = 0; i < classchild.length; i++) {
//            classmap.put(classchild[i].getSimpleName(),classchild[i]);
//        }
        return this;
    }

    public static Context fcontext;


    public static final  ResUtil instance=new ResUtil();
    private ResUtil(){

    }
    public static ResUtil getInstance(){
        return instance;
    }


    public  int getResourceId(String resourcestring){
        try {
            String[] array=resourcestring.split("\\.");
            String classname=array[1];
            String resourcename=array[2];
            return fcontext.getResources().getIdentifier(resourcename, classname, fcontext.getPackageName());
        } catch (Exception e) {
//            e.printStackTrace();
            return -1;
        }
//        try {
//            Field field=classmap.get(classname).getField(resourcename);
//            return field.getInt(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return -1;
//        }

    }
}
