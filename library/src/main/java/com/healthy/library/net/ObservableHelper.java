package com.healthy.library.net;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.healthy.library.LibApplication;
import com.healthy.library.base.BaseActivity;
import com.healthy.library.utils.FrameActivityManager;

import java.util.HashMap;
import java.util.Map;

import autodispose2.AutoDispose;
import autodispose2.ObservableSubscribeProxy;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * @author Li
 * @date 2019/03/27 10:01
 * @des
 */

public class ObservableHelper {

    public static  @NonNull ObservableSubscribeProxy<String> createObservable(Context context, Map<String, Object> map) {
        Map<String, Object> newmap = new HashMap<>();
        newmap.putAll(map);
        LifecycleOwner lifecycleOwner = null;
        if(context instanceof LifecycleOwner){
            lifecycleOwner = (LifecycleOwner) context;
        } else {
            Activity activity=FrameActivityManager.instance().topActivityLifecycleOwner();
            if(activity!=null&&activity instanceof LifecycleOwner){
                lifecycleOwner = (LifecycleOwner) activity;
            }
        }
        return RetrofitHelper.createService(LibApplication.getAppContext())
                .getData(newmap)
                .compose(RxThreadUtils.Obs_io_main())
                .to(RxLifecycleUtils.bindLifecycle(lifecycleOwner));
    }

    /**
     * 不受控的
     * @param context
     * @param map
     * @return
     */
    public static Observable<String> createObservableNoLife(Context context, Map<String, Object> map) {
        Map<String,Object> newmap=new HashMap<>();
        newmap.putAll(map);
        return RetrofitHelper.createService(context)
                .getData(newmap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public static @NonNull ObservableSubscribeProxy<String> createUploadObservable(Context context, MultipartBody.Part part) {
        LifecycleOwner lifecycleOwner;
        if(context instanceof LifecycleOwner){
            lifecycleOwner = (LifecycleOwner) context;
        } else {
            lifecycleOwner = (LifecycleOwner) FrameActivityManager.instance().topActivityLifecycleOwner();
        }
        return RetrofitHelper.createServiceUpload(LibApplication.getAppContext())
                .uploadFile(part)
                .compose(RxThreadUtils.Obs_io_main())
                .to(RxLifecycleUtils.bindLifecycle(lifecycleOwner));
    }


    public static  @NonNull ObservableSubscribeProxy<String> createUploadObservable(Context context, Map<String, Object> map) {
//        Map<String,Object> newmap=new HashMap<>();
//        newmap.putAll(map);
        LifecycleOwner lifecycleOwner;
        if(context instanceof LifecycleOwner){
            lifecycleOwner = (LifecycleOwner) context;
        } else {
            lifecycleOwner = (LifecycleOwner) FrameActivityManager.instance().topActivityLifecycleOwner();
        }
        return RetrofitHelper.createServiceUpload(LibApplication.getAppContext())
                .getData(map)
                .compose(RxThreadUtils.Obs_io_main())
                .to(RxLifecycleUtils.bindLifecycle(lifecycleOwner));
    }

//    public static @NonNull ObservableSubscribeProxy<String> createObservable(Context context, Map<String, Object> map) {
//        Map<String, Object> newmap = new HashMap<>();
//        newmap.putAll(map);
//        LifecycleOwner lifecycleOwner = null;
//        if(context instanceof LifecycleOwner){
//            lifecycleOwner = (LifecycleOwner) context;
//        } else {
//            Activity activity=FrameActivityManager.instance().topActivityLifecycleOwner();
//            if(activity!=null&&activity instanceof LifecycleOwner){
//                lifecycleOwner = (LifecycleOwner) activity;
//            }
//        }
//        return RetrofitHelper.createServiceMall(LibApplication.getAppContext())
//                .getData(newmap)
//                .compose(RxThreadUtils.Obs_io_main())
//                .to(RxLifecycleUtils.bindLifecycle(lifecycleOwner));
//    }

}

