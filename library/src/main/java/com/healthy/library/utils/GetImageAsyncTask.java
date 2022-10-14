//package com.healthy.library.utils;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.Target;
//
//import java.io.File;
//
//public class GetImageAsyncTask extends AsyncTask<String, Void, File> {
//
//    public GetImgPathListener getImgPathListener;
//    private final Context context;
//
//    public interface GetImgPathListener {
//
//        void getPath(String path);
//
//    }
//
//
//    public void setTaskListener(GetImgPathListener getListener) {
//        this.getImgPathListener = getListener;
//    }
//
//    public GetImageAsyncTask(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    protected File doInBackground(String... params) {
//        String imgUrl = params[0];
//        Log.e("imgUrl", imgUrl);
//        try {
//            return com.healthy.library.businessutil.GlideCopy.with(context)
//                    .load(imgUrl)
//                    .downloadOnly(100, 100)
//                    .get();
//        } catch (Exception ex) {
//            return null;
//        }
//    }
//
//    @Override
//    protected void onPostExecute(File result) {
//        if (result == null) {
//            return;
//        }
//        //此path就是对应文件的缓存路径
//        String path = result.getPath();
//        if (path!=null){
//            getImgPathListener.getPath(path);
//        }
////        Log.e("path", path);
////        Bitmap bmp = BitmapFactory.decodeFile(path);
////        img.setImageBitmap(bmp);
//
//    }
//
//}
