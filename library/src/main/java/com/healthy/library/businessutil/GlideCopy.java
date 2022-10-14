package com.healthy.library.businessutil;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.healthy.library.utils.SpUtils;

public class GlideCopy {
    public static RequestManagerCopy with(Context context) {
        return new RequestManagerCopy().with(context);
    }
    public static RequestManagerCopy with(View view) {
        if(view!=null){
            return new RequestManagerCopy().with(view.getContext());
        }
        return new RequestManagerCopy().with();
    }
    public static RequestManagerCopy with(Fragment fragment) {
        return new RequestManagerCopy().with();
    }

    public static RequestManagerCopy with(Activity activity) {
        return new RequestManagerCopy().with(activity);
    }
    public static RequestManagerCopy with(FragmentActivity activity) {
        return new RequestManagerCopy().with(activity);
    }
    public static RequestManagerCopy with() {
        return new RequestManagerCopy().with();
    }
}
